package com.Guess.ReportsPlus;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getServerDataFolderPath;
import static com.Guess.ReportsPlus.util.Server.ClientUtils.isConnected;
import static com.Guess.ReportsPlus.util.Server.recordUtils.extractValueByKey;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import com.Guess.ReportsPlus.Desktop.mainDesktopController;
import com.Guess.ReportsPlus.config.ConfigReader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApplication extends Application {
	public static Stage mainRT;
	public static mainDesktopController mainDesktopControllerObj;
	public static Stage mainDesktopStage;

	public static String getDate() {
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
		return currentTime.format(formatter);
	}

	public static String getTime(boolean systemTime, boolean ignoreConfigSetting) {
		LocalDateTime currentTime = LocalDateTime.now();
		boolean use24Hour = false;
		if (!ignoreConfigSetting) {
			try {
				use24Hour = ConfigReader.configRead("uiSettings", "use24Hour").equalsIgnoreCase("true");
			} catch (IOException | NullPointerException e) {
				use24Hour = false;
				logError("Error getting uiSettings.use24Hour: ", e);
			}
		}
		DateTimeFormatter storageFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
		DateTimeFormatter displayFormatter;
		if (use24Hour) {
			displayFormatter = DateTimeFormatter.ofPattern("HH:mm a", Locale.ENGLISH);
		} else {
			displayFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
		}
		try {
			if (systemTime || !isConnected
					|| ConfigReader.configRead("connectionSettings", "useGameTime").equalsIgnoreCase("false")) {
				return currentTime.format(displayFormatter);
			}
		} catch (IOException e) {
			logError("Error checking time source: ", e);
		}
		String serverDataFolderPath = getServerDataFolderPath();
		if (!serverDataFolderPath.isEmpty()) {
			File gameDataFile = new File(serverDataFolderPath + "ServerGameData.data");
			if (gameDataFile.isFile()) {
				try {
					String gameDataFileContent = Files.readString(Paths.get(gameDataFile.toURI()));
					String timeValue = extractValueByKey(gameDataFileContent, "time");
					if (timeValue != null && !timeValue.equalsIgnoreCase("Unknown")) {
						LocalTime parsedTime = LocalTime.parse(timeValue, storageFormatter);
						return parsedTime.format(displayFormatter);
					} else if (timeValue == null) {
						logError("timeValue was null; using system time instead");
					}
				} catch (IOException | DateTimeParseException e) {
					logError("Error parsing time, using system time: ", e);
				}
			}
		}
		return currentTime.format(displayFormatter);
	}

	public static void main(String[] args) {
		launch();
	}

	public static void openMainDesktop() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("Windows/Desktop/desktop-main.fxml"));
		Scene scene = new Scene(fxmlLoader.load());
		mainDesktopControllerObj = fxmlLoader.getController();
		Stage primaryStage = new Stage();
		primaryStage.setTitle("ReportsPlus Desktop");
		primaryStage.setScene(scene);
		primaryStage.getIcons()
				.add(new Image(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Logo.png")));
		primaryStage.show();
		mainDesktopStage = primaryStage;
		String windowConfig = ConfigReader.configRead("uiSettings", "windowDisplaySetting");
		if (windowConfig.equalsIgnoreCase("Fullscreen")) {
			primaryStage.setMaximized(false);
			primaryStage.setFullScreen(true);
		} else {
			primaryStage.setFullScreen(false);
			primaryStage.setMaximized(true);
			primaryStage.centerOnScreen();
			primaryStage.setAlwaysOnTop(ConfigReader.configRead("uiSettings", "windowAOT").equalsIgnoreCase("true"));
		}
		MainApplication.mainRT = mainDesktopStage;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		openMainDesktop();
	}
}