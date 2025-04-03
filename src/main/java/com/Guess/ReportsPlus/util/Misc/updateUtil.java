package com.Guess.ReportsPlus.util.Misc;

import com.Guess.ReportsPlus.util.Report.treeViewUtils;
import com.Guess.ReportsPlus.util.Strings.updateStrings;
import javafx.concurrent.Task;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.Guess.ReportsPlus.Desktop.mainDesktopController.updatesAppObj;
import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.MainApplication.mainDesktopControllerObj;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.*;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationWarning;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getJarPath;
import static com.Guess.ReportsPlus.util.Strings.updateStrings.version;

public class updateUtil {
	public static String gitVersion;
	
	public static boolean runJar(String jarPath) {
		File jarFile = new File(jarPath);
		if (jarFile.exists()) {
			try {
				ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarPath);
				processBuilder.start();
				return true;
			} catch (IOException e) {
				logError("Error Starting Updater: " + jarPath, e);
				return false;
			}
		} else {
			logError("Updater Not Found At: " + jarPath);
			return false;
		}
	}
	
	public static void checkForUpdates() {
		String rawUrl = "https://raw.githubusercontent.com/Guess1m/ReportsPlus/main/version.txt";
		try {
			URL url = new URL(rawUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(5000);
			connection.setRequestMethod("GET");
			
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String latestVersion = reader.readLine();
				gitVersion = latestVersion;
				logInfo("Git Version: " + latestVersion);
				logInfo("App Version: " + updateStrings.version);
				
				if (updateStrings.version.contains("dev")) {
					logDebug("Dev Version Detected: [" + version + "]");
					showNotificationWarning("Dev Version Detected", "Detected Development version: [" + version + "], please report any issues! :)", true);
					if (mainDesktopControllerObj != null) {
						mainDesktopControllerObj.getVersionLabel().setText("Dev Version [" + version + "]");
						mainDesktopControllerObj.getVersionLabel().setStyle("-fx-text-fill: blue;");
					}
				} else {
					if (!gitVersion.equalsIgnoreCase(updateStrings.version)) {
						NotificationManager.showNotificationErrorPersistent("Update Available", localization.getLocalizedMessage("Desktop.NewVersionAvailable", "New Version Available!") + " [" + gitVersion + "] Check Updates App!");
						updatesAppObj.setAlertVisibility(true);
					}
				}
				
				reader.close();
			} else {
				logError("Failed to fetch version file: HTTP error code " + responseCode);
			}
		} catch (UnknownHostException e) {
			logError("UnknownHostException: Unable to resolve host " + rawUrl + ". Check your network connection.");
		} catch (IOException e) {
			logError("Cant check for updates: ", e);
		}
	}
	
	public static Task<Boolean> startUpdate(String url, String destination, String nameOfUpdate, boolean replaceFiles) {
		Task<Boolean> updateTask = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				try {
					logInfo("Starting " + nameOfUpdate + " Update!");
					
					if (!isInternetAvailable()) {
						logError("[Updater] Internet connection is unavailable. Aborting update.");
					}
					
					double startTime = System.currentTimeMillis();
					
					logDebug("[Updater] Preparing to download file from: " + url);
					logDebug("[Updater] Target destination: " + destination);
					Path downloadedFile = downloadFile(url, destination);
					
					logDebug("[Updater] Extracting downloaded file to: " + destination);
					extractZip(downloadedFile, destination, replaceFiles);
					
					double endTime = System.currentTimeMillis();
					logInfo("[Updater] " + nameOfUpdate + " Update Finished: " + (endTime - startTime) / 1000 + "sec");
					return true;
				} catch (FileNotFoundException e) {
					logError("[Updater] " + nameOfUpdate + " Zip Not Found: ");
					return false;
				} catch (IOException e) {
					logError("[Updater] Error downloading/extracting the " + nameOfUpdate + " Zip: ");
					return false;
				} catch (Exception e) {
					logError("[Updater] Unexpected Error during the update process: ");
					return false;
				}
			}
		};
		return updateTask;
	}
	
	public static boolean isInternetAvailable() {
		try {
			logDebug("Checking internet connection...");
			URL testUrl = new URL("https://www.google.com");
			HttpURLConnection connection = (HttpURLConnection) testUrl.openConnection();
			connection.setRequestMethod("HEAD");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			int responseCode = connection.getResponseCode();
			boolean isConnected = (responseCode == 200);
			logInfo("Internet connection status: " + (isConnected ? "Available" : "Unavailable"));
			return isConnected;
		} catch (IOException e) {
			logError("Internet connection check failed: " + e.getMessage());
			return false;
		}
	}
	
	public static boolean copyUpdaterJar() throws IOException {
		logInfo("Running copyUpdaterJar");
		String sourceJarPath = "/com/Guess/ReportsPlus/data/updater/Updater.jar";
		Path destinationDir = Paths.get(getJarPath(), "tools");
		
		if (!Files.exists(destinationDir)) {
			Files.createDirectories(destinationDir);
		}
		
		Path updaterDestinationPath = destinationDir.resolve(Paths.get(sourceJarPath).getFileName());
		boolean fileExistsBefore = Files.exists(updaterDestinationPath);
		
		try (InputStream inputStream = treeViewUtils.class.getResourceAsStream(sourceJarPath)) {
			if (inputStream != null) {
				Files.copy(inputStream, updaterDestinationPath, StandardCopyOption.REPLACE_EXISTING);
				
				if (fileExistsBefore) {
					logInfo("Updater Jar replaced at Path: " + updaterDestinationPath);
				} else {
					logInfo("Updater Jar created at Path: " + updaterDestinationPath);
				}
				return true;
			} else {
				logError("Resource not found: " + sourceJarPath);
				return false;
			}
		}
	}
	
	private static Path downloadFile(String fileUrl, String destinationPath) throws IOException {
		if (destinationPath == null) {
			logError("[Downloader] Destination path is null. Aborting download.");
			return null;
		}
		
		logInfo("[Downloader] Starting download from: " + fileUrl);
		double startTime = System.currentTimeMillis();
		URL url = new URL(fileUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		connection.setRequestProperty("User-Agent", "Mozilla/5.0");
		
		Path destinationDir = Path.of(destinationPath);
		if (!Files.exists(destinationDir)) {
			logDebug("[Downloader] Destination directory does not exist. Creating: " + destinationDir);
			Files.createDirectories(destinationDir);
		}
		
		String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
		Path destinationFile = destinationDir.resolve(fileName);
		
		try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream()); FileOutputStream out = new FileOutputStream(destinationFile.toFile())) {
			
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
		}
		
		double endTime = System.currentTimeMillis();
		logInfo("[Downloader] Download completed in " + (endTime - startTime) / 1000 + " seconds.");
		logInfo("[Downloader] File saved to: " + destinationFile);
		return destinationFile;
	}
	
	private static void extractZip(Path zipFile, String destinationDir, boolean replaceExisting) throws IOException {
		if (destinationDir == null) {
			logError("[Extractor] Destination directory is null. Aborting extraction.");
			return;
		}
		
		logInfo("[Extractor] Starting extraction from: " + zipFile + " to " + destinationDir);
		
		try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {
			ZipEntry entry;
			while ((entry = zipInputStream.getNextEntry()) != null) {
				String entryName = entry.getName();
				
				if (entryName.startsWith("Sounds/")) {
					entryName = entryName.substring("Sounds/".length());
				}
				
				if (entryName.isBlank()) {
					continue;
				}
				
				Path entryPath = Path.of(destinationDir, entryName);
				
				if (entry.isDirectory()) {
					if (!Files.exists(entryPath)) {
						Files.createDirectories(entryPath);
					}
				} else {
					if (!Files.exists(entryPath.getParent())) {
						Files.createDirectories(entryPath.getParent());
					}
					
					if (!Files.exists(entryPath) || replaceExisting) {
						try (FileOutputStream out = new FileOutputStream(entryPath.toFile())) {
							byte[] buffer = new byte[1024];
							int bytesRead;
							while ((bytesRead = zipInputStream.read(buffer)) != -1) {
								out.write(buffer, 0, bytesRead);
							}
						}
						logInfo("[Extractor] Extracted: " + entryPath);
					} else {
						logInfo("[Extractor] Skipped: " + entryPath + " (already exists)");
					}
				}
				zipInputStream.closeEntry();
			}
			
			logInfo("[Extractor] Extraction completed to: " + destinationDir);
		} catch (IOException e) {
			logError("[Extractor] Error occurred during ZIP extraction: ", e);
			throw e;
		}
		
		if (Files.exists(zipFile)) {
			Files.delete(zipFile);
			logInfo("[Extractor] ZIP file deleted after extraction.");
		}
	}
}