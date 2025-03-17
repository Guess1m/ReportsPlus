package com.Guess.ReportsPlus;

import com.Guess.ReportsPlus.Desktop.mainDesktopController;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.Guess.ReportsPlus.util.Strings.URLStrings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getServerDataFolderPath;
import static com.Guess.ReportsPlus.util.Server.ClientUtils.isConnected;
import static com.Guess.ReportsPlus.util.Server.recordUtils.extractValueByKey;

public class MainApplication extends Application {

    public static Stage mainRT;
    public static mainDesktopController mainDesktopControllerObj;
    public static Stage mainDesktopStage;

    //TODO: implement sending test file

    public static String getDate() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        return currentTime.format(formatter);
    }

    public static String getTime(boolean systemTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);

        try {
            if (systemTime || !isConnected || ConfigReader.configRead("connectionSettings", "useGameTime").equalsIgnoreCase("false")) {
                return currentTime.format(formatter);
            }
        } catch (IOException e) {
            logError("Error getting connectionSettings.useGameTime: ", e);
        }

        String serverDataFolderPath = getServerDataFolderPath();
        if (!serverDataFolderPath.isEmpty()) {
            File gameDataFile = new File(serverDataFolderPath + "ServerGameData.data");

            if (gameDataFile.isFile()) {
                try {
                    String gameDataFileContent = Files.readString(Paths.get(URLStrings.serverGameDataFileURL));
                    String timeValue = extractValueByKey(gameDataFileContent, "time");

                    if (timeValue != null) {
                        return timeValue;
                    }

                    log("timeValue was null; using system time instead", LogUtils.Severity.ERROR);
                } catch (IOException e) {
                    log("Error reading game data file, falling back to system time.", LogUtils.Severity.ERROR);
                }
            }
        }

        return currentTime.format(formatter);
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
        primaryStage.getIcons().add(new Image(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Logo.png")));
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