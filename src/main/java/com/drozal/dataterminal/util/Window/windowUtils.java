package com.drozal.dataterminal.util.Window;

import com.drozal.dataterminal.util.Misc.LogUtils;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.List;

import static com.drozal.dataterminal.DataTerminalHomeApplication.mainRT;
import static com.drozal.dataterminal.Windows.Other.NotesViewController.notesStage;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;

public class windowUtils {
	
	public static void centerStageOnMainApp(Stage stage) {
		Rectangle2D mainDesktopStageBounds = new Rectangle2D(mainRT.getX(), mainRT.getY(), mainRT.getWidth(),
		                                                     mainRT.getHeight());
		List<Screen> screens = Screen.getScreensForRectangle(mainDesktopStageBounds.getMinX(),
		                                                     mainDesktopStageBounds.getMinY(),
		                                                     mainDesktopStageBounds.getWidth(),
		                                                     mainDesktopStageBounds.getHeight());
		
		if (!screens.isEmpty()) {
			Screen screen = screens.get(0);
			Rectangle2D screenBounds = screen.getVisualBounds();
			
			double centerX = screenBounds.getMinX() + (screenBounds.getWidth() - stage.getWidth()) / 2;
			double centerY = screenBounds.getMinY() + (screenBounds.getHeight() - stage.getHeight()) / 2;
			
			stage.setX(centerX);
			stage.setY(centerY);
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