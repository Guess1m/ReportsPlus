package com.drozal.dataterminal.util.Window;

import com.drozal.dataterminal.util.Misc.LogUtils;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.List;

import static com.drozal.dataterminal.DataTerminalHomeApplication.mainRT;
import static com.drozal.dataterminal.actionController.*;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;

public class windowUtils {

    public static void snapToTopLeft(Stage stage) {
        Rectangle2D parentBounds = new Rectangle2D(mainRT.getX(), mainRT.getY(),
                mainRT.getWidth(), mainRT.getHeight());
        List<Screen> screens = Screen.getScreensForRectangle(parentBounds.getMinX(), parentBounds.getMinY(),
                parentBounds.getWidth(), parentBounds.getHeight());
        if (!screens.isEmpty()) {
            Screen screen = screens.get(0); // Assuming the parentStage is on the first screen
            Rectangle2D screenBounds = screen.getVisualBounds();
            double halfScreenWidth = screenBounds.getWidth() / 2;
            double halfScreenHeight = screenBounds.getHeight() / 2;
            stage.setX(screenBounds.getMinX());
            stage.setY(screenBounds.getMinY());
            stage.setWidth(halfScreenWidth);
            stage.setHeight(halfScreenHeight);
        }
    }


    /**
     * Centers the given stage on the same screen as the mainRT stage
     * without altering the stage's width or height.
     *
     * @param stage the stage to be centered
     */
    public static void centerStageOnMainApp(Stage stage) {
        Rectangle2D mainRTBounds = new Rectangle2D(mainRT.getX(), mainRT.getY(), mainRT.getWidth(), mainRT.getHeight());
        List<Screen> screens = Screen.getScreensForRectangle(mainRTBounds.getMinX(), mainRTBounds.getMinY(), mainRTBounds.getWidth(), mainRTBounds.getHeight());

        if (!screens.isEmpty()) {
            Screen screen = screens.get(0); // Assuming the mainRT stage is on the first screen
            Rectangle2D screenBounds = screen.getVisualBounds();

            double centerX = screenBounds.getMinX() + (screenBounds.getWidth() - stage.getWidth()) / 2;
            double centerY = screenBounds.getMinY() + (screenBounds.getHeight() - stage.getHeight()) / 2;

            stage.setX(centerX);
            stage.setY(centerY);
        }
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

    public static void restoreDefaultState(Stage stage, double width, double height) {
        stage.setHeight(height);
        stage.setWidth(width);
        if (stage.equals(mainRT)) {
            mainRT.setHeight(800);
            mainRT.setWidth(1150);
        }
        if (stage.equals(notesStage)) {
            notesStage.setHeight(285);
            notesStage.setWidth(685);
        }
        if (stage.equals(CalloutStage)) {
            CalloutStage.setHeight(342);
            CalloutStage.setWidth(618);
        }
        if (stage.equals(IDStage)) {
            IDStage.setHeight(253);
            IDStage.setWidth(415);
        } else {
            stage.setHeight(800);
            stage.setWidth(1150);
        }
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