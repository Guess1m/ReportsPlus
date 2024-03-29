package com.drozal.dataterminal;

import com.drozal.dataterminal.util.ResizeHelper;
import com.drozal.dataterminal.util.windowUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataTerminalHomeApplication extends Application {

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

        Stage mainRT = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DataTerminalHome-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        mainRT.initStyle(StageStyle.UNDECORATED);
        mainRT.setScene(scene);
        mainRT.setResizable(true);
        mainRT.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/Icon.png")));
        mainRT.show();
        mainRT.setHeight(800);
        mainRT.setWidth(1150);
        mainRT.setMinHeight(mainRT.getHeight() - 200);
        mainRT.setMinWidth(mainRT.getWidth() - 200);
        mainRT.centerOnScreen();
        windowUtils.setWindowedFullscreen(mainRT);
        ResizeHelper.addResizeListener(mainRT);
    }
}
