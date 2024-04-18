package com.drozal.dataterminal.util;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import static com.drozal.dataterminal.util.LogUtils.log;

public class windowUtils {

    public static void snapToTopLeft(Stage stage) {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double halfScreenWidth = primaryScreenBounds.getWidth() / 2;
        double halfScreenHeight = primaryScreenBounds.getHeight() / 2;
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(halfScreenWidth);
        stage.setHeight(halfScreenHeight);
    }

    public static void snapToBottomLeft(Stage stage) {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double halfScreenWidth = primaryScreenBounds.getWidth() / 2;
        double halfScreenHeight = primaryScreenBounds.getHeight() / 2;
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMaxY() - halfScreenHeight);
        stage.setWidth(halfScreenWidth);
        stage.setHeight(halfScreenHeight);
    }

    public static void snapToTopRight(Stage stage) {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double halfScreenWidth = primaryScreenBounds.getWidth() / 2;
        double halfScreenHeight = primaryScreenBounds.getHeight() / 2;
        stage.setX(primaryScreenBounds.getMaxX() - halfScreenWidth);
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(halfScreenWidth);
        stage.setHeight(halfScreenHeight);
    }

    public static void snapToBottomRight(Stage stage) {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double halfScreenWidth = primaryScreenBounds.getWidth() / 2;
        double halfScreenHeight = primaryScreenBounds.getHeight() / 2;
        stage.setX(primaryScreenBounds.getMaxX() - halfScreenWidth);
        stage.setY(primaryScreenBounds.getMaxY() - halfScreenHeight);
        stage.setWidth(halfScreenWidth);
        stage.setHeight(halfScreenHeight);
    }

    public static void snapToLeft(Stage stage) {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double halfScreenWidth = primaryScreenBounds.getWidth() / 2;

        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(halfScreenWidth);
        stage.setHeight(primaryScreenBounds.getHeight());
    }

    public static void snapToRight(Stage stage) {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double halfScreenWidth = primaryScreenBounds.getWidth() / 2;

        stage.setX(primaryScreenBounds.getMinX() + halfScreenWidth);
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(halfScreenWidth);
        stage.setHeight(primaryScreenBounds.getHeight());
    }

    public static void setWindowedFullscreen(Stage stage) {
        Screen screen = getScreenContainingStage(stage);
        if (screen != null) {
            double screenWidth = screen.getVisualBounds().getWidth();
            double screenHeight = screen.getVisualBounds().getHeight();
            stage.setX(screen.getVisualBounds().getMinX());
            stage.setY(screen.getVisualBounds().getMinY());
            stage.setWidth(screenWidth);
            stage.setHeight(screenHeight);
        } else {
            log("No screen found for the stage center.", LogUtils.Severity.WARN);
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