package com.Guess.ReportsPlus.Windows.Misc;

import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.config.ConfigWriter;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
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
	
	@javafx.fxml.FXML
	private Label validInternetConnectionLabel;
	@javafx.fxml.FXML
	private Label foundDownloadVersionLabel;
	@javafx.fxml.FXML
	private Label helpLabel;
	@javafx.fxml.FXML
	private CheckBox armChipCheckbox;
	@javafx.fxml.FXML
	private BorderPane root;
	@javafx.fxml.FXML
	private Label foundDownloadURLLabel;
	@javafx.fxml.FXML
	private Label updateStatusLabel;
	@javafx.fxml.FXML
	private CheckBox intelChipCheckbox;
	@javafx.fxml.FXML
	private Label foundUpdateUtilityLabel;
	@javafx.fxml.FXML
	private Label autoUpdateUtilityHeader;
	@javafx.fxml.FXML
	private Button startAutoUpdateBtn;
	@javafx.fxml.FXML
	private Label validServerConnectionLabel;
	@javafx.fxml.FXML
	private CheckBox updateServerCheckbox;
	@javafx.fxml.FXML
	private Label foundServerVersionLabel;
	
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
	}
	
	@javafx.fxml.FXML
	public void updateBtn(ActionEvent actionEvent) {
		log("AutoUpdate button pressed", LogUtils.Severity.DEBUG);
		updateStatus(updateStatusLabel, "Performing Checks...", "blue");
		
		final boolean[] updateServer = {false};
		if (updateServerCheckbox.isSelected()) {
			updateServer[0] = true;
		} else {
			updateServer[0] = false;
		}
		log("Attempting Server Update: " + updateServer[0], LogUtils.Severity.DEBUG);
		
		String downloadURL = null;
		if (intelChipCheckbox.isSelected()) {
			downloadURL = intelChipApplicationDownloadURL;
			log("Intel Download Selected", LogUtils.Severity.DEBUG);
			ConfigWriter.configwrite("updater", "useIntel", "true");
		} else {
			downloadURL = applicationDownloadURL;
			log("ARM/Win Download Selected", LogUtils.Severity.DEBUG);
			ConfigWriter.configwrite("updater", "useIntel", "false");
		}
		log("Using Download URL: " + downloadURL, LogUtils.Severity.DEBUG);
		
		final boolean[] errorsFound = {false};
		StringBuilder errors = new StringBuilder();
		
		String finalDownloadURL = downloadURL;
		Platform.runLater(() -> {
			validInternetConnection = isInternetAvailable();
			if (validInternetConnection) {
				log("Valid Internet Connection", LogUtils.Severity.DEBUG);
				updateStatus(validInternetConnectionLabel, localization.getLocalizedMessage("UpdatesWindow.validAutoUpdateCheck", "OK"), "green");
			} else {
				log("Invalid Internet Connection", LogUtils.Severity.DEBUG);
				updateStatus(validInternetConnectionLabel, localization.getLocalizedMessage("UpdatesWindow.invalidAutoUpdateCheck", "Invalid"), "red");
				errorsFound[0] = true;
				errors.append("Invalid Internet Connection ");
			}
			
			foundUpdateUtility = checkForUpdateUtility();
			if (foundUpdateUtility) {
				log("Found Update Utility", LogUtils.Severity.DEBUG);
				updateStatus(foundUpdateUtilityLabel, localization.getLocalizedMessage("UpdatesWindow.validAutoUpdateCheck", "OK"), "green");
			} else {
				log("Update Utility Was Not Found!", LogUtils.Severity.DEBUG);
				updateStatus(foundUpdateUtilityLabel, localization.getLocalizedMessage("UpdatesWindow.invalidAutoUpdateCheck", "Invalid"), "red");
				errorsFound[0] = true;
				errors.append("Update Utility Was Not Found, ");
			}
			
			validServerConnection = isConnected;
			if (validServerConnection) {
				log("Valid Server Connection Found", LogUtils.Severity.DEBUG);
				updateStatus(validServerConnectionLabel, localization.getLocalizedMessage("UpdatesWindow.validAutoUpdateCheck", "OK"), "green");
			} else {
				log("Invalid Server Connection", LogUtils.Severity.WARN);
				updateStatus(validServerConnectionLabel, localization.getLocalizedMessage("UpdatesWindow.invalidAutoUpdateCheck", "Invalid"), "red");
				if (updateServer[0]) {
					errorsFound[0] = true;
					errors.append("Server Connection Was Not Found, ");
				}
			}
			
			foundServerDownloadVersion = serverVersion;
			if (foundServerDownloadVersion != null) {
				log("Current Server Version: " + foundServerDownloadVersion, LogUtils.Severity.DEBUG);
				updateStatus(foundServerVersionLabel, foundServerDownloadVersion, "green");
			} else {
				log("Current Server Version Not Found!", LogUtils.Severity.WARN);
				updateStatus(foundServerVersionLabel, localization.getLocalizedMessage("UpdatesWindow.invalidAutoUpdateCheck", "Invalid"), "red");
				if (updateServer[0]) {
					errorsFound[0] = true;
					errors.append("Current Server Version Not Found, ");
				}
			}
			
			foundDownloadURL = checkForDownloadURL(finalDownloadURL);
			if (foundDownloadURL) {
				log("Found Download URL: " + finalDownloadURL, LogUtils.Severity.DEBUG);
				updateStatus(foundDownloadURLLabel, localization.getLocalizedMessage("UpdatesWindow.validAutoUpdateCheck", "OK"), "green");
			} else {
				log("Download URL Was Not Found!", LogUtils.Severity.WARN);
				updateStatus(foundDownloadURLLabel, localization.getLocalizedMessage("UpdatesWindow.invalidAutoUpdateCheck", "Invalid"), "red");
				errorsFound[0] = true;
				errors.append("Download URL Was Not Found, ");
			}
			
			foundDownloadVersion = checkForDownloadVersion();
			if (foundDownloadVersion != null) {
				log("Found Download Version: " + foundDownloadVersion, LogUtils.Severity.DEBUG);
				updateStatus(foundDownloadVersionLabel, foundDownloadVersion, "green");
			} else {
				log("Download Version Was Not Found!", LogUtils.Severity.WARN);
				updateStatus(foundDownloadVersionLabel, localization.getLocalizedMessage("UpdatesWindow.invalidAutoUpdateCheck", "Invalid"), "red");
				errorsFound[0] = true;
				errors.append("Download Version Was Not Found, ");
			}
			
			String ErrorsString = errors.toString();
			if (ErrorsString.endsWith(", ")) {
				ErrorsString = ErrorsString.substring(0, ErrorsString.length() - 2);
			}
			
			if (errorsFound[0]) {
				log("Update Check Finished With Errors!: " + ErrorsString, LogUtils.Severity.ERROR);
				showNotificationError("AutoUpdate Utility", "Autoupdate check finished with errors!");
				updateStatus(updateStatusLabel, localization.getLocalizedMessage("UpdatesWindow.failedAutoUpdateCheck", "Issues Found"), "red");
				updateStatus(helpLabel, localization.getLocalizedMessage("UpdatesWindow.checksDidntPassLabel", "Can't Update:") + " " + ErrorsString, "red");
			} else {
				updateStatus(updateStatusLabel, localization.getLocalizedMessage("UpdatesWindow.successfulAutoUpdateCheck", "Successful Check!"), "green");
				updateStatus(helpLabel, "Performing Update..", "green");
				
				if (updateServer[0]) {
					log("Sending update message to server", LogUtils.Severity.DEBUG);
					sendMessageToServer("UPDATE_MESSAGE");
				}
				
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						runUpdate();
					}
				}, 2500);
				showNotificationInfo("AutoUpdate Utility", "Autoupdate check successful!");
			}
		});
		
	}
	
	private void runUpdate() {
		log("Attempting to run Updater and Close application!", LogUtils.Severity.WARN);
		boolean canUpdate = runJar(getJarPath() + File.separator + "tools" + File.separator + "Updater.jar");
		if (canUpdate) {
			handleClose();
		} else {
			log("Not able to update, issue running Updater", LogUtils.Severity.ERROR);
			updateStatus(helpLabel, "Not able to update, issue running Updater", "red");
		}
	}
	
	private boolean checkForUpdateUtility() {
		String updaterPath = getJarPath() + File.separator + "tools" + File.separator + "Updater.jar";
		File jarFile = new File(updaterPath);
		if (jarFile.exists()) {
			log("Updater Found At: " + updaterPath, LogUtils.Severity.DEBUG);
			return true;
		} else {
			log("Updater Not Found At: " + updaterPath, LogUtils.Severity.ERROR);
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
				log("Failed to fetch version file: Unable to get response code. URL might not exist.", LogUtils.Severity.ERROR);
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
				log("Failed to fetch version file: HTTP error code " + responseCode, LogUtils.Severity.ERROR);
				return null;
			}
		} catch (UnknownHostException e) {
			log("UnknownHostException: Unable to resolve host " + rawUrl + ". Check your network connection.", LogUtils.Severity.ERROR);
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
	
	@javafx.fxml.FXML
	public void intelChecked(ActionEvent actionEvent) {
		if (armChipCheckbox.isSelected()) {
			armChipCheckbox.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
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
