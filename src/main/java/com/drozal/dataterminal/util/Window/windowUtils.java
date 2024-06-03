package com.drozal.dataterminal.util.Window;

import com.drozal.dataterminal.util.Misc.LogUtils;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.List;

import static com.drozal.dataterminal.DataTerminalHomeApplication.mainRT;
import static com.drozal.dataterminal.actionController.*;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;

/**
 * The type Window utils.
 */
public class windowUtils {

    /**
     * Snap to top left.
     *
     * @param stage the stage
     */
    public static void snapToTopLeft(Stage stage) {
        Rectangle2D parentBounds = new Rectangle2D(mainRT.getX(), mainRT.getY(), mainRT.getWidth(), mainRT.getHeight());
        List<Screen> screens = Screen.getScreensForRectangle(parentBounds.getMinX(), parentBounds.getMinY(), parentBounds.getWidth(), parentBounds.getHeight());
        if (!screens.isEmpty()) {
            Screen screen = screens.get(0);
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
            Screen screen = screens.get(0);
            Rectangle2D screenBounds = screen.getVisualBounds();

            double centerX = screenBounds.getMinX() + (screenBounds.getWidth() - stage.getWidth()) / 2;
            double centerY = screenBounds.getMinY() + (screenBounds.getHeight() - stage.getHeight()) / 2;

            stage.setX(centerX);
            stage.setY(centerY);
        }
    }

    /**
     * Snap to bottom left.
     *
     * @param stage the stage
     */
    public static void snapToBottomLeft(Stage stage) {
        Rectangle2D parentBounds = new Rectangle2D(mainRT.getX(), mainRT.getY(), mainRT.getWidth(), mainRT.getHeight());
        List<Screen> screens = Screen.getScreensForRectangle(parentBounds.getMinX(), parentBounds.getMinY(), parentBounds.getWidth(), parentBounds.getHeight());
        if (!screens.isEmpty()) {
            Screen screen = screens.get(0);
            Rectangle2D screenBounds = screen.getVisualBounds();
            double halfScreenWidth = screenBounds.getWidth() / 2;
            double halfScreenHeight = screenBounds.getHeight() / 2;
            stage.setX(screenBounds.getMinX());
            stage.setY(screenBounds.getMaxY() - halfScreenHeight);
            stage.setWidth(halfScreenWidth);
            stage.setHeight(halfScreenHeight);
        }
    }

    /**
     * Snap to top right.
     *
     * @param stage the stage
     */
    public static void snapToTopRight(Stage stage) {
        Rectangle2D parentBounds = new Rectangle2D(mainRT.getX(), mainRT.getY(), mainRT.getWidth(), mainRT.getHeight());
        List<Screen> screens = Screen.getScreensForRectangle(parentBounds.getMinX(), parentBounds.getMinY(), parentBounds.getWidth(), parentBounds.getHeight());
        if (!screens.isEmpty()) {
            Screen screen = screens.get(0);
            Rectangle2D screenBounds = screen.getVisualBounds();
            double halfScreenWidth = screenBounds.getWidth() / 2;
            double halfScreenHeight = screenBounds.getHeight() / 2;
            stage.setX(screenBounds.getMaxX() - halfScreenWidth);
            stage.setY(screenBounds.getMinY());
            stage.setWidth(halfScreenWidth);
            stage.setHeight(halfScreenHeight);
        }
    }

    /**
     * Snap to bottom right.
     *
     * @param stage the stage
     */
    public static void snapToBottomRight(Stage stage) {
        Rectangle2D parentBounds = new Rectangle2D(mainRT.getX(), mainRT.getY(), mainRT.getWidth(), mainRT.getHeight());
        List<Screen> screens = Screen.getScreensForRectangle(parentBounds.getMinX(), parentBounds.getMinY(), parentBounds.getWidth(), parentBounds.getHeight());
        if (!screens.isEmpty()) {
            Screen screen = screens.get(0);
            Rectangle2D screenBounds = screen.getVisualBounds();
            double halfScreenWidth = screenBounds.getWidth() / 2;
            double halfScreenHeight = screenBounds.getHeight() / 2;
            stage.setX(screenBounds.getMaxX() - halfScreenWidth);
            stage.setY(screenBounds.getMaxY() - halfScreenHeight);
            stage.setWidth(halfScreenWidth);
            stage.setHeight(halfScreenHeight);
        }
    }

    /**
     * Snap to left.
     *
     * @param stage the stage
     */
    public static void snapToLeft(Stage stage) {
        Rectangle2D parentBounds = new Rectangle2D(mainRT.getX(), mainRT.getY(), mainRT.getWidth(), mainRT.getHeight());
        List<Screen> screens = Screen.getScreensForRectangle(parentBounds.getMinX(), parentBounds.getMinY(), parentBounds.getWidth(), parentBounds.getHeight());
        if (!screens.isEmpty()) {
            Screen screen = screens.get(0);
            Rectangle2D screenBounds = screen.getVisualBounds();
            double halfScreenWidth = screenBounds.getWidth() / 2;
            stage.setX(screenBounds.getMinX());
            stage.setY(screenBounds.getMinY());
            stage.setWidth(halfScreenWidth);
            stage.setHeight(screenBounds.getHeight());
        }
    }

    /**
     * Snap to right.
     *
     * @param stage the stage
     */
    public static void snapToRight(Stage stage) {
        Rectangle2D parentBounds = new Rectangle2D(mainRT.getX(), mainRT.getY(), mainRT.getWidth(), mainRT.getHeight());
        List<Screen> screens = Screen.getScreensForRectangle(parentBounds.getMinX(), parentBounds.getMinY(), parentBounds.getWidth(), parentBounds.getHeight());
        if (!screens.isEmpty()) {
            Screen screen = screens.get(0);
            Rectangle2D screenBounds = screen.getVisualBounds();
            double halfScreenWidth = screenBounds.getWidth() / 2;
            stage.setX(screenBounds.getMinX() + halfScreenWidth);
            stage.setY(screenBounds.getMinY());
            stage.setWidth(halfScreenWidth);
            stage.setHeight(screenBounds.getHeight());
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

    /**
     * Sets windowed fullscreen.
     *
     * @param stage the stage
     */
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

    /**
     * Restore default state.
     *
     * @param stage  the stage
     * @param width  the width
     * @param height the height
     */
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
            IDStage.setHeight(274);
            IDStage.setWidth(514);
        }
        stage.centerOnScreen();
    }

    /**
     * Toggle windowed fullscreen.
     *
     * @param stage  the stage
     * @param width  the width
     * @param height the height
     */
    public static void toggleWindowedFullscreen(Stage stage, double width, double height) {
        if (!(stage.getX() == Screen.getPrimary().getBounds().getMinX() || stage.getY() == Screen.getPrimary().getBounds().getMinY())) {
            setWindowedFullscreen(stage);
        } else {
            restoreDefaultState(stage, width, height);
        }
    }
}