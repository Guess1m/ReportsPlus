package com.Guess.ReportsPlus.util.Misc;

import com.Guess.ReportsPlus.util.Misc.LogUtils.Severity;
import com.Guess.ReportsPlus.util.Report.treeViewUtils;

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

import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.stringUtil.getJarPath;

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
			log("Updater Not Found At: " + jarPath, Severity.ERROR);
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
				log("Git Version: " + latestVersion, Severity.INFO);
				log("App Version: " + stringUtil.version, Severity.INFO);
				
				if (!gitVersion.equalsIgnoreCase(stringUtil.version)) {
					NotificationManager.showNotificationErrorPersistent("Update Available", localization.getLocalizedMessage("Desktop.NewVersionAvailable", "New Version Available!") + " " + gitVersion + " Check Updates App!");
				}
				
				reader.close();
			} else {
				log("Failed to fetch version file: HTTP error code " + responseCode, Severity.ERROR);
			}
		} catch (UnknownHostException e) {
			log("UnknownHostException: Unable to resolve host " + rawUrl + ". Check your network connection.", Severity.ERROR);
		} catch (IOException e) {
			logError("Cant check for updates: ", e);
		}
	}
	
	public static void startUpdate(String url, String destination, String nameOfUpdate, boolean replaceFiles) {
		try {
			log("Starting " + nameOfUpdate + " Update!", Severity.INFO);
			
			if (!isInternetAvailable()) {
				log("Internet connection is unavailable. Aborting update.", Severity.ERROR);
				return;
			}
			
			double startTime = System.currentTimeMillis();
			
			log("Preparing to download file from: " + url, Severity.DEBUG);
			log("Target destination: " + destination, Severity.DEBUG);
			Path downloadedFile = downloadFile(url, destination);
			
			log("Extracting downloaded file to: " + destination, Severity.DEBUG);
			extractZip(downloadedFile, destination, replaceFiles);
			
			double endTime = System.currentTimeMillis();
			log(nameOfUpdate + " Update Finished: " + (endTime - startTime) / 1000 + "sec", Severity.INFO);
		} catch (FileNotFoundException e) {
			logError(nameOfUpdate + " Zip Not Found: ", e);
		} catch (IOException e) {
			logError("An error occurred while downloading or extracting the " + nameOfUpdate + " Zip: ", e);
		} catch (Exception e) {
			logError("An unexpected error occurred during the update process: ", e);
		}
	}
	
	public static boolean isInternetAvailable() {
		try {
			log("Checking internet connection...", Severity.DEBUG);
			URL testUrl = new URL("https://www.google.com");
			HttpURLConnection connection = (HttpURLConnection) testUrl.openConnection();
			connection.setRequestMethod("HEAD");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			int responseCode = connection.getResponseCode();
			boolean isConnected = (responseCode == 200);
			log("Internet connection status: " + (isConnected ? "Available" : "Unavailable"), Severity.INFO);
			return isConnected;
		} catch (IOException e) {
			log("Internet connection check failed: " + e.getMessage(), Severity.ERROR);
			return false;
		}
	}
	
	public static boolean copyUpdaterJar() throws IOException {
		log("Running copyUpdaterJar", LogUtils.Severity.INFO);
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
					log("Updater Jar replaced at Path: " + updaterDestinationPath, LogUtils.Severity.INFO);
				} else {
					log("Updater Jar created at Path: " + updaterDestinationPath, LogUtils.Severity.INFO);
				}
				return true;
			} else {
				log("Resource not found: " + sourceJarPath, LogUtils.Severity.ERROR);
				return false;
			}
		}
	}
	
	private static Path downloadFile(String fileUrl, String destinationPath) throws IOException {
		if (destinationPath == null) {
			log("Destination path is null. Aborting download.", Severity.ERROR);
			return null;
		}
		
		log("Starting download from: " + fileUrl, Severity.INFO);
		double startTime = System.currentTimeMillis();
		URL url = new URL(fileUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		connection.setRequestProperty("User-Agent", "Mozilla/5.0");
		
		Path destinationDir = Path.of(destinationPath);
		if (!Files.exists(destinationDir)) {
			log("Destination directory does not exist. Creating: " + destinationDir, Severity.DEBUG);
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
		log("Download completed in " + (endTime - startTime) / 1000 + " seconds.", Severity.INFO);
		log("File saved to: " + destinationFile, Severity.INFO);
		return destinationFile;
	}
	
	private static void extractZip(Path zipFile, String destinationDir, boolean replaceExisting) throws IOException {
		if (destinationDir == null) {
			log("Destination directory is null. Aborting extraction.", Severity.ERROR);
			return;
		}
		
		log("Starting extraction from: " + zipFile + " to " + destinationDir, Severity.INFO);
		
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
						log("Extracted: " + entryPath, Severity.INFO);
					} else {
						log("Skipped: " + entryPath + " (already exists)", Severity.INFO);
					}
				}
				zipInputStream.closeEntry();
			}
			
			log("Extraction completed to: " + destinationDir, Severity.INFO);
		} catch (IOException e) {
			logError("Error occurred during ZIP extraction: ", e);
			throw e;
		}
		
		if (Files.exists(zipFile)) {
			Files.delete(zipFile);
			log("ZIP file deleted after extraction.", Severity.INFO);
		}
	}
}