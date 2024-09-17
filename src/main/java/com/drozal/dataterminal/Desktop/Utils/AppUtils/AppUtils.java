package com.drozal.dataterminal.Desktop.Utils.AppUtils;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class AppUtils {
	public static Boolean editableDesktop = false;
	public static List<DesktopApp> DesktopApps = new ArrayList<>();
	private static double xOffset = 0;
	private static double yOffset = 0;
	
	public static void setUpDragEvents(Button button, AnchorPane app) {
		button.setOnMousePressed(event -> handleMousePressed(event, app));
		button.setOnMouseDragged(event -> handleMouseDragged(event, app));
	}
	
	private static void handleMousePressed(MouseEvent event, AnchorPane pane) {
		if (editableDesktop) {
			xOffset = event.getSceneX() - pane.getTranslateX();
			yOffset = event.getSceneY() - pane.getTranslateY();
		}
	}
	
	private static void handleMouseDragged(MouseEvent event, AnchorPane pane) {
		if (editableDesktop) {
			pane.setTranslateX(event.getSceneX() - xOffset);
			pane.setTranslateY(event.getSceneY() - yOffset);
		}
	}
}
