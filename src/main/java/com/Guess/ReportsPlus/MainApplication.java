package com.Guess.ReportsPlus;

import com.Guess.ReportsPlus.Desktop.mainDesktopController;
import com.Guess.ReportsPlus.config.ConfigReader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MainApplication extends Application {

    public static Stage mainRT;
    public static mainDesktopController mainDesktopControllerObj;
    public static Stage mainDesktopStage;

    public static String getDate() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        return currentTime.format(formatter);
    }

    public static String getTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a", Locale.ENGLISH);
        return currentTime.format(formatter);
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("Windows/Desktop/desktop-main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        mainDesktopControllerObj = fxmlLoader.getController();
        primaryStage.setTitle("ReportsPlus Desktop");
        primaryStage.setScene(scene);
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
}