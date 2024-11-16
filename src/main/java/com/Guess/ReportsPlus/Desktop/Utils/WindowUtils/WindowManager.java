package com.Guess.ReportsPlus.Desktop.Utils.WindowUtils;

import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class WindowManager {
	public static Map<String, CustomWindow> windows = new HashMap<>();
	public static Map<String, CustomWindow> minimizedWindows = new HashMap<>();
	public static Map<String, double[]> windowPositions = new HashMap<>(); // Store x, y positions for each window
	
	public static CustomWindow createCustomWindow(AnchorPane root, String fileName, String title, boolean resizable, int priority, boolean centerOnDesktop, boolean reopen, HBox taskBarApps, Image image) {
		if (!windows.containsKey(title)) {
			URL fxmlUrl = Launcher.class.getResource(fileName);
			if (fxmlUrl == null) {
				throw new RuntimeException("FXML file not found: " + fileName);
			}
			
			CustomWindow customWindow = new CustomWindow(fileName, title, resizable, priority, taskBarApps, root, image);
			
			if (root != null) {
				Platform.runLater(() -> {
					root.getChildren().add(customWindow.getWindowPane());
					
					// Restore position if available
					double[] savedPosition = windowPositions.get(title);
					if (savedPosition != null) {
						customWindow.setPosition(savedPosition[0], savedPosition[1]);
					} else if (centerOnDesktop) {
						customWindow.centerOnDesktop();
					}
					
					windows.put(title, customWindow);
					customWindow.bringToFront();
				});
			}
			return customWindow;
			
		} else {
			CustomWindow customWindow = windows.get(title);
			if (reopen) {
				// Save the current position before closing
				double x = customWindow.getWindowPane().getLayoutX();
				double y = customWindow.getWindowPane().getLayoutY();
				windowPositions.put(title, new double[]{x, y});
				
				LogUtils.log(customWindow.title + " is already created, closing", LogUtils.Severity.WARN);
				customWindow.closeWindow();
				windows.remove(title);
				
				// Recreate the window with the saved position
				return createCustomWindow(root, fileName, title, resizable, priority, centerOnDesktop, false, taskBarApps, image);
			} else {
				customWindow.bringToFront();
			}
		}
		return null;
	}
	
	public static void createCustomWindow(AnchorPane root, BorderPane window, String title, boolean resizable, int priority, boolean centerOnDesktop, boolean reopen, HBox taskBarApps, Image image) {
		if (!windows.containsKey(title)) {
			CustomWindow customWindow = new CustomWindow(window, title, resizable, priority, taskBarApps, root, image);
			
			if (root != null) {
				root.getChildren().add(customWindow.getWindowPane());
				
				// Restore position if available
				double[] savedPosition = windowPositions.get(title);
				if (savedPosition != null) {
					customWindow.setPosition(savedPosition[0], savedPosition[1]);
				} else if (centerOnDesktop) {
					customWindow.centerOnDesktop();
				}
				
				windows.put(title, customWindow);
				customWindow.bringToFront();
			}
		} else {
			CustomWindow customWindow = windows.get(title);
			if (reopen) {
				// Save the current position before closing
				double x = customWindow.getWindowPane().getLayoutX();
				double y = customWindow.getWindowPane().getLayoutY();
				windowPositions.put(title, new double[]{x, y});
				
				LogUtils.log(customWindow.title + " is already created, closing", LogUtils.Severity.WARN);
				customWindow.closeWindow();
				windows.remove(title);
				
				// Recreate the window with the saved position
				createCustomWindow(root, window, title, resizable, priority, centerOnDesktop, false, taskBarApps, image);
			} else {
				customWindow.bringToFront();
			}
		}
	}
	
	public static CustomWindow getWindow(String title) {
		return windows.get(title);
	}
}
