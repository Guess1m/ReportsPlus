package com.drozal.dataterminal.util;

import javafx.stage.Screen;
import javafx.stage.Stage;

public class windowUtils {

    public static void setWindowedFullscreen(Stage stage) {
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();
        stage.setX(screen.getVisualBounds().getMinX());
        stage.setY(screen.getVisualBounds().getMinY());
        stage.setWidth(screenWidth);
        stage.setHeight(screenHeight);
    }

    public static void restoreDefaultState(Stage stage, double width, double height) {
        stage.setHeight(height);
        stage.setWidth(width);
        stage.centerOnScreen();
    }

}
