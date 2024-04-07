package com.drozal.dataterminal;

import com.catwithawand.borderlessscenefx.scene.BorderlessScene;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.util.windowUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
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

    public static Spinner<Integer> createSpinner(Spinner spinner, int min, int max, int initialValue) {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initialValue);
        spinner.setValueFactory(valueFactory);
        return spinner;
    }

    @Override
    public void start(Stage stage) throws IOException {

        mainRT = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DataTerminalHome-view.fxml"));
        Parent root = loader.load();
        actionController controller = loader.getController();
        BorderlessScene scene = new BorderlessScene(mainRT, StageStyle.TRANSPARENT, root, Color.TRANSPARENT);
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
                    windowUtils.setWindowedFullscreen(mainRT);
                } else {
                    mainRT.setHeight(800);
                    mainRT.setWidth(1150);
                }
            }

        }
        scene.setMoveControl(controller.topPane);
        mainRT.setAlwaysOnTop(false);
    }
}
