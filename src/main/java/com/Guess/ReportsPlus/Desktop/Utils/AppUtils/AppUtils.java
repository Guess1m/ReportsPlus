package com.Guess.ReportsPlus.Desktop.Utils.AppUtils;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class AppUtils {
    public static Boolean editableDesktop = false;
    public static List<DesktopApp> DesktopApps = new ArrayList<>();
    private static double xOffset = 0;
    private static double yOffset = 0;

    public static void setUpDragEvents(Button button, VBox app) {
        button.setOnMousePressed(event -> handleMousePressed(event, app));
        button.setOnMouseDragged(event -> handleMouseDragged(event, app));
    }

    private static void handleMousePressed(MouseEvent event, VBox pane) {
        if (editableDesktop) {
            xOffset = event.getSceneX() - pane.getTranslateX();
            yOffset = event.getSceneY() - pane.getTranslateY();
        }
    }

    private static void handleMouseDragged(MouseEvent event, VBox pane) {
        if (editableDesktop) {
            double newX = event.getSceneX() - xOffset;
            double newY = event.getSceneY() - yOffset;

            double roundedX = Math.round(newX / 5) * 5;
            double roundedY = Math.round(newY / 5) * 5;

            pane.setTranslateX(roundedX);
            pane.setTranslateY(roundedY);
        }
    }

}
