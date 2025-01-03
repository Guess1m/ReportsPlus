package com.Guess.ReportsPlus.Windows.Misc;

import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.config.ConfigWriter;
import com.Guess.ReportsPlus.util.Misc.LogUtils.Severity;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Timer;
import java.util.TimerTask;

import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationError;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationInfo;
import static com.Guess.ReportsPlus.util.Misc.controllerUtils.handleClose;
import static com.Guess.ReportsPlus.util.Misc.stringUtil.getJarPath;
import static com.Guess.ReportsPlus.util.Misc.updateUtil.isInternetAvailable;
import static com.Guess.ReportsPlus.util.Misc.updateUtil.runJar;
import static com.Guess.ReportsPlus.util.Server.ClientUtils.*;

public class AutoUpdaterToolController {
	
	static String applicationDownloadURL = "https://github.com/Guess1m/ReportsPlus/releases/latest/download/Application.zip";
	static String intelChipApplicationDownloadURL = "https://github.com/Guess1m/ReportsPlus/releases/latest/download/ApplicationIntelChip.zip";
	private boolean validInternetConnection = false;
	private boolean foundUpdateUtility = false;
	private boolean validServerConnection = false;
	private String foundServerDownloadVersion = null;
	private boolean foundDownloadURL = false;
	private String foundDownloadVersion = null;
	
	@FXML
	private Label validInternetConnectionLabel;
	@FXML
	private Label foundDownloadVersionLabel;
	@FXML
	private Label helpLabel;
	@FXML
	private CheckBox armChipCheckbox;
	@FXML
	private BorderPane root;
	@FXML
	private Label foundDownloadURLLabel;
	@FXML
	private Label updateStatusLabel;
	@FXML
	private CheckBox intelChipCheckbox;
	@FXML
	private Label foundUpdateUtilityLabel;
	@FXML
	private Label autoUpdateUtilityHeader;
	@FXML
	private Button startAutoUpdateBtn;
	@FXML
	private Label validServerConnectionLabel;
	@FXML
	private CheckBox updateServerCheckbox;
	@FXML
	private Label foundServerVersionLabel;
	
	public static String getParentDirectory() {
		try {
			String jarPath = Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			File currentDir = new File(jarPath).getParentFile();
			
			return currentDir.getParent();
		} catch (Exception e) {
			logError("Could not get parent directory: ", e);
			return null;
		}
	}
	
	public void initialize() {
		updateLocale();
		
		Platform.runLater(() -> {
			String useIntelConfig = null;
			try {
				useIntelConfig = ConfigReader.configRead("updater", "useIntel");
			} catch (IOException e) {
				logError("Error reading useIntel config setting", e);
			}
			if (useIntelConfig != null) {
				if (useIntelConfig.equalsIgnoreCase("true")) {
					intelChipCheckbox.setSelected(true);
					armChipCheckbox.setSelected(false);
				} else {
					armChipCheckbox.setSelected(true);
					intelChipCheckbox.setSelected(false);
				}
			}
		});
		updateStatus(updateStatusLabel, localization.getLocalizedMessage("UpdatesWindow.notStartedLabel", "Not Started"), "#ab5b38");
	}
	
	private void updateLocale() {
		intelChipCheckbox.setText(localization.getLocalizedMessage("UpdatesWindow.IntelChipCheckbox", "Use Intel Chip MacOS Download (Only select if using a Intel chip mac)"));
		armChipCheckbox.setText(localization.getLocalizedMessage("UpdatesWindow.armChipCheckbox", "Use Windows / ARM MacOS Download"));
		startAutoUpdateBtn.setText(localization.getLocalizedMessage("UpdatesWindow.startAutoUpdateBtn", "Start AutoUpdate"));
		autoUpdateUtilityHeader.setText(localization.getLocalizedMessage("UpdatesWindow.autoUpdateUtilityHeader", "AutoUpdate Utility"));
		updateServerCheckbox.setText(localization.getLocalizedMessage("UpdatesWindow.updateServerCheckbox", "Update Server (Requires you to be connected)"));
	}
	
	@FXML
	public void updateBtn(ActionEvent actionEvent) {
		log("AutoUpdate button pressed", Severity.DEBUG);
		updateStatus(updateStatusLabel, "Performing Checks...", "blue");
		
		final boolean[] updateServer = {false};
		if (updateServerCheckbox.isSelected()) {
			updateServer[0] = true;
		} else {
			updateServer[0] = false;
		}
		log("Attempting Server Update: " + updateServer[0], Severity.DEBUG);
		
		String downloadURL = null;
		if (intelChipCheckbox.isSelected()) {
			downloadURL = intelChipApplicationDownloadURL;
			log("Intel Download Selected", Severity.DEBUG);
			ConfigWriter.configwrite("updater", "useIntel", "true");
		} else {
			downloadURL = applicationDownloadURL;
			log("ARM/Win Download Selected", Severity.DEBUG);
			ConfigWriter.configwrite("updater", "useIntel", "false");
		}
		log("Using Download URL: " + downloadURL, Severity.DEBUG);
		
		final boolean[] errorsFound = {false};
		StringBuilder errors = new StringBuilder();
		
		String finalDownloadURL = downloadURL;
		Platform.runLater(() -> {
			validInternetConnection = isInternetAvailable();
			if (validInternetConnection) {
				log("Valid Internet Connection", Severity.DEBUG);
				updateStatus(validInternetConnectionLabel, localization.getLocalizedMessage("UpdatesWindow.validAutoUpdateCheck", "OK"), "green");
			} else {
				log("Invalid Internet Connection", Severity.DEBUG);
				updateStatus(validInternetConnectionLabel, localization.getLocalizedMessage("UpdatesWindow.invalidAutoUpdateCheck", "Invalid"), "red");
				errorsFound[0] = true;
				errors.append("Invalid Internet Connection ");
			}
			
			foundUpdateUtility = checkForUpdateUtility();
			if (foundUpdateUtility) {
				log("Found Update Utility", Severity.DEBUG);
				updateStatus(foundUpdateUtilityLabel, localization.getLocalizedMessage("UpdatesWindow.validAutoUpdateCheck", "OK"), "green");
			} else {
				log("Update Utility Was Not Found!", Severity.DEBUG);
				updateStatus(foundUpdateUtilityLabel, localization.getLocalizedMessage("UpdatesWindow.invalidAutoUpdateCheck", "Invalid"), "red");
				errorsFound[0] = true;
				errors.append("Update Utility Was Not Found, ");
			}
			
			validServerConnection = isConnected;
			if (validServerConnection) {
				log("Valid Server Connection Found", Severity.DEBUG);
				updateStatus(validServerConnectionLabel, localization.getLocalizedMessage("UpdatesWindow.validAutoUpdateCheck", "OK"), "green");
			} else {
				log("Invalid Server Connection", Severity.WARN);
				updateStatus(validServerConnectionLabel, localization.getLocalizedMessage("UpdatesWindow.invalidAutoUpdateCheck", "Invalid"), "red");
				if (updateServer[0]) {
					errorsFound[0] = true;
					errors.append("Server Connection Was Not Found, ");
				}
			}
			
			foundServerDownloadVersion = serverVersion;
			if (foundServerDownloadVersion != null) {
				log("Current Server Version: " + foundServerDownloadVersion, Severity.DEBUG);
				updateStatus(foundServerVersionLabel, foundServerDownloadVersion, "green");
			} else {
				log("Current Server Version Not Found!", Severity.WARN);
				updateStatus(foundServerVersionLabel, localization.getLocalizedMessage("UpdatesWindow.invalidAutoUpdateCheck", "Invalid"), "red");
				if (updateServer[0]) {
					errorsFound[0] = true;
					errors.append("Current Server Version Not Found, ");
				}
			}
			
			foundDownloadURL = checkForDownloadURL(finalDownloadURL);
			if (foundDownloadURL) {
				log("Found Download URL: " + finalDownloadURL, Severity.DEBUG);
				updateStatus(foundDownloadURLLabel, localization.getLocalizedMessage("UpdatesWindow.validAutoUpdateCheck", "OK"), "green");
			} else {
				log("Download URL Was Not Found!", Severity.WARN);
				updateStatus(foundDownloadURLLabel, localization.getLocalizedMessage("UpdatesWindow.invalidAutoUpdateCheck", "Invalid"), "red");
				errorsFound[0] = true;
				errors.append("Download URL Was Not Found, ");
			}
			
			foundDownloadVersion = checkForDownloadVersion();
			if (foundDownloadVersion != null) {
				log("Found Download Version: " + foundDownloadVersion, Severity.DEBUG);
				updateStatus(foundDownloadVersionLabel, foundDownloadVersion, "green");
			} else {
				log("Download Version Was Not Found!", Severity.WARN);
				updateStatus(foundDownloadVersionLabel, localization.getLocalizedMessage("UpdatesWindow.invalidAutoUpdateCheck", "Invalid"), "red");
				errorsFound[0] = true;
				errors.append("Download Version Was Not Found, ");
			}
			
			String ErrorsString = errors.toString();
			if (ErrorsString.endsWith(", ")) {
				ErrorsString = ErrorsString.substring(0, ErrorsString.length() - 2);
			}
			
			if (errorsFound[0]) {
				log("Update Check Finished With Errors!: " + ErrorsString, Severity.ERROR);
				showNotificationError("AutoUpdate Utility", "Autoupdate check finished with errors!");
				updateStatus(updateStatusLabel, localization.getLocalizedMessage("UpdatesWindow.failedAutoUpdateCheck", "Issues Found"), "red");
				updateStatus(helpLabel, localization.getLocalizedMessage("UpdatesWindow.checksDidntPassLabel", "Can't Update:") + " " + ErrorsString, "red");
			} else {
				updateStatus(updateStatusLabel, localization.getLocalizedMessage("UpdatesWindow.successfulAutoUpdateCheck", "Successful Check!"), "green");
				updateStatus(helpLabel, "Downloading Update..", "green");
				
				if (updateServer[0]) {
					log("Sending update message to server", Severity.DEBUG);
					sendMessageToServer("UPDATE_MESSAGE");
				}
				
				downloadFile(finalDownloadURL, getJarPath());
				
				showNotificationInfo("AutoUpdate Utility", "Autoupdate check successful!");
			}
		});
		
	}
	
	private void downloadFile(String fileUrl, String destinationPath) {
		if (destinationPath == null) {
			log("Destination path is null. Aborting download.", Severity.ERROR);
			return;
		}
		
		Task<Boolean> downloadTask = new Task<>() {
			@Override
			protected Boolean call() throws IOException {
				log("Preparing to download file from: " + fileUrl, Severity.DEBUG);
				URL url = new URL(fileUrl);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestProperty("User-Agent", "Mozilla/5.0");
				
				int totalFileSize = connection.getContentLength();
				if (totalFileSize == -1) {
					log("Failed to retrieve file size. Progress tracking unavailable.", Severity.WARN);
				}
				
				Path destinationDir = Path.of(destinationPath);
				if (!Files.exists(destinationDir)) {
					Files.createDirectories(destinationDir);
				}
				
				String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
				Path destinationFile = destinationDir.resolve(fileName);
				
				try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream()); FileOutputStream out = new FileOutputStream(destinationFile.toFile())) {
					
					byte[] buffer = new byte[8192];
					int bytesRead;
					long totalBytesRead = 0;
					
					long start = System.currentTimeMillis();
					while ((bytesRead = in.read(buffer)) != -1) {
						out.write(buffer, 0, bytesRead);
						totalBytesRead += bytesRead;
						
						if (totalFileSize > 0) {
							updateProgress(totalBytesRead, totalFileSize);
						}
					}
					long end = System.currentTimeMillis();
					long delta = end - start;
					log("Download completed in " + Math.round(delta) + "ms. Total bytes read: " + totalBytesRead, Severity.DEBUG);
					return true;
				} catch (IOException e) {
					log("Error during file download: " + e.getMessage(), Severity.ERROR);
					return false;
				}
			}
		};
		
		downloadTask.setOnSucceeded(e -> {
			updateStatus(updateStatusLabel, "Download Complete", "green");
			showNotificationInfo("Download", "Download finished successfully.");
			
			log("Application Update Download was Successful, launching updater", Severity.DEBUG);
			updateStatus(helpLabel, "Downloaded Update! Running Updater..", "green");
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					runUpdater();
				}
			}, 3000);
		});
		
		downloadTask.setOnFailed(e -> {
			updateStatus(updateStatusLabel, "Download Failed", "red");
			showNotificationError("Download", "Download failed: " + downloadTask.getException().getMessage());
		});
		
		downloadTask.progressProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.doubleValue() >= 0) {
				int percentComplete = (int) (newValue.doubleValue() * 100);
				String colorToUse = percentComplete <= 25 ? "red" : percentComplete <= 50 ? "#cc6600" : percentComplete <= 75 ? "orange" : "green";
				updateStatus(updateStatusLabel, percentComplete + "%", colorToUse);
			}
		});
		
		Thread thread = new Thread(downloadTask);
		thread.setDaemon(true);
		thread.start();
		
	}
	
	private void runUpdater() {
		log("Attempting to run Updater and Close application!", Severity.WARN);
		boolean canUpdate = runJar(getJarPath() + File.separator + "tools" + File.separator + "Updater.jar");
		if (canUpdate) {
			handleClose();
		} else {
			log("Not able to update, issue running Updater", Severity.ERROR);
			updateStatus(helpLabel, "Not able to update, issue running Updater", "red");
		}
	}
	
	private boolean checkForUpdateUtility() {
		String updaterPath = getJarPath() + File.separator + "tools" + File.separator + "Updater.jar";
		File jarFile = new File(updaterPath);
		if (jarFile.exists()) {
			log("Updater Found At: " + updaterPath, Severity.DEBUG);
			return true;
		} else {
			log("Updater Not Found At: " + updaterPath, Severity.ERROR);
			return false;
		}
	}
	
	private boolean checkForDownloadURL(String urlString) {
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("HEAD");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			int responseCode = connection.getResponseCode();
			return responseCode >= 200 && responseCode < 400;
		} catch (IOException e) {
			return false;
		}
	}
	
	private String checkForDownloadVersion() {
		String rawUrl = "https://raw.githubusercontent.com/Guess1m/ReportsPlus/main/version.txt";
		HttpURLConnection connection = null;
		try {
			URL url = new URL(rawUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			connection.setRequestMethod("GET");
			
			int responseCode;
			try {
				responseCode = connection.getResponseCode();
			} catch (IOException e) {
				log("Failed to fetch version file: Unable to get response code. URL might not exist.", Severity.ERROR);
				return null;
			}
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder content = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					content.append(line).append("\n");
				}
				reader.close();
				return content.toString().trim();
			} else {
				log("Failed to fetch version file: HTTP error code " + responseCode, Severity.ERROR);
				return null;
			}
		} catch (UnknownHostException e) {
			log("UnknownHostException: Unable to resolve host " + rawUrl + ". Check your network connection.", Severity.ERROR);
			return null;
		} catch (IOException e) {
			logError("Can't check for updates: ", e);
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
	
	@FXML
	public void intelChecked(ActionEvent actionEvent) {
		if (armChipCheckbox.isSelected()) {
			armChipCheckbox.setSelected(false);
		}
	}
	
	@FXML
	public void armChecked(ActionEvent actionEvent) {
		if (intelChipCheckbox.isSelected()) {
			intelChipCheckbox.setSelected(false);
		}
	}
	
	private void updateStatus(Label label, String status, String hexColor) {
		label.setText(status);
		label.setStyle("-fx-text-fill: " + hexColor + ";");
	}
}
