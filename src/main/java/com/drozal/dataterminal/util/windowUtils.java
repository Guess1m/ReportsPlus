package com.drozal.dataterminal.util;

import javafx.stage.Screen;
import javafx.stage.Stage;

public class windowUtils {

    public static void setWindowedFullscreen(Stage stage) {
        Screen screen = getScreenContainingStage(stage);
        if (screen != null) {
            double screenWidth = screen.getVisualBounds().getWidth();
            double screenHeight = screen.getVisualBounds().getHeight();
            stage.setX(screen.getVisualBounds().getMinX());
            stage.setY(screen.getVisualBounds().getMinY());
            stage.setWidth(screenWidth);
            stage.setHeight(screenHeight);
            System.out.println(stage.getWidth());
            System.out.println(stage.getHeight());
        } else {
            // Handle the case when no screen is found
            System.out.println("No screen found for the stage center.");
        }
    }

    private static Screen getScreenContainingStage(Stage stage) {
        double centerX = stage.getX() + stage.getWidth() / 2.0;
        double centerY = stage.getY() + stage.getHeight() / 2.0;

        for (Screen screen : Screen.getScreens()) {
            if (screen.getVisualBounds().contains(centerX, centerY)) {
                return screen;
            }
        }
        return null;
    }

    public static void restoreDefaultState(Stage stage, double width, double height) {
        stage.setHeight(height);
        stage.setWidth(width);
        stage.centerOnScreen();
    }

    public static void toggleWindowedFullscreen(Stage stage, double width, double height) {
        if (!(stage.getX() == Screen.getPrimary().getBounds().getMinX() || stage.getY() == Screen.getPrimary().getBounds().getMinY())) {
            setWindowedFullscreen(stage);
        } else {
            restoreDefaultState(stage, width, height);
        }
    }
}