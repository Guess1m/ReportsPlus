package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.drozal.dataterminal.util.windowUtils.*;

public class DataTerminalHomeApplication extends Application {

    public static Stage mainRT;

    public static Stage getMainRT() {
        return mainRT;
    }

    public static void setMainRT(Stage mainRT) {
        DataTerminalHomeApplication.mainRT = mainRT;
    }

    public static String getDate() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentTime.format(formatter);
    }

    public static String getTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        return currentTime.format(formatter);
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * Adds a focus listener to the root stage that refocuses the provided stages when the root stage gains focus.
     *
     * @param root   the root stage
     * @param stages the stages to refocus
     */
    public static void addFocusFix(Stage root, Stage... stages) {
        root.focusedProperty().addListener((observable, oldValue, newValue) -> {
            for (Stage stage : stages) {
                System.out.println("Checking stage: " + stage);
                if (stage != null) {
                    boolean notesFocused = stage.isShowing();
                    System.out.println("Stage showing: " + notesFocused);
                    if (newValue) {
                        System.out.println("Root stage focused. Requesting focus for: " + stage);
                        if (notesFocused) {
                            stage.requestFocus();
                        }
                    }
                } else {
                    System.out.println("Stage is null");
                }
            }
        });
    }

    @Override
    public void start(Stage stage) throws IOException {

        mainRT = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DataTerminalHome-view.fxml"));
        mainRT.initStyle(StageStyle.UNDECORATED);
        Parent root = loader.load();
        actionController controller = loader.getController();
        Scene scene = new Scene(root);
        mainRT.setScene(scene);
        mainRT.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/Icon.png")));
        mainRT.show();

        String startupValue = ConfigReader.configRead("mainWindowLayout");
        switch (startupValue) {
            case "TopLeft" -> snapToTopLeft(mainRT);
            case "TopRight" -> snapToTopRight(mainRT);
            case "BottomLeft" -> snapToBottomLeft(mainRT);
            case "BottomRight" -> snapToBottomRight(mainRT);
            case "FullLeft" -> snapToLeft(mainRT);
            case "FullRight" -> snapToRight(mainRT);
            default -> {
                mainRT.centerOnScreen();
                mainRT.setMinHeight(450);
                mainRT.setMinWidth(450);
                if (ConfigReader.configRead("fullscreenOnStartup").equals("true")) {
                    setWindowedFullscreen(mainRT);

                } else {
                    mainRT.setHeight(800);
                    mainRT.setWidth(1150);
                }
            }
        }

        mainRT.setAlwaysOnTop(false);
    }
}