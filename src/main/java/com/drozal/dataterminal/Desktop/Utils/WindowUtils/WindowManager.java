package com.drozal.dataterminal.Desktop.Utils.WindowUtils;

import com.drozal.dataterminal.Launcher;
import com.drozal.dataterminal.util.Misc.LogUtils;
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
	
	public static CustomWindow createFakeWindow(AnchorPane root, String fileName, String title, boolean resizable, int priority, boolean centerOnDesktop, boolean reopen, HBox taskBarApps, Image image) {
		if (!windows.containsKey(title)) {
			URL fxmlUrl = Launcher.class.getResource(fileName);
			if (fxmlUrl == null) {
				throw new RuntimeException("FXML file not found: " + fileName);
			}
			CustomWindow customWindow = new CustomWindow(fileName, title, resizable, priority, taskBarApps, root,
			                                             image);
			
			if (root != null) {
				Platform.runLater(() -> {
					root.getChildren().add(customWindow.getWindowPane());
					if (centerOnDesktop) {
						customWindow.centerOnDesktop();
					}
					
					windows.put(title, customWindow);
					
					customWindow.bringToFront();
				});
			}
			return customWindow;
			
		}
		{
			CustomWindow customWindow = windows.get(title);
			if (reopen) {
				LogUtils.log(windows.get(title) + " is already created, closing", LogUtils.Severity.WARN);
				customWindow.closeWindow();
				createFakeWindow(root, fileName, title, resizable, priority, centerOnDesktop, true, taskBarApps, image);
			} else {
				customWindow.bringToFront();
			}
		}
		return null;
	}
	
	public static void createFakeWindow(AnchorPane root, BorderPane window, String title, boolean resizable, int priority, boolean centerOnDesktop, boolean reopen, HBox taskBarApps, Image image) {
		if (!windows.containsKey(title)) {
			CustomWindow customWindow = new CustomWindow(window, title, resizable, priority, taskBarApps, root, image);
			if (root != null) {
				root.getChildren().add(customWindow.getWindowPane());
				if (centerOnDesktop) {
					customWindow.centerOnDesktop();
				}
				
				windows.put(title, customWindow);
				
				customWindow.bringToFront();
			}
		}
		{
			CustomWindow customWindow = windows.get(title);
			if (reopen) {
				LogUtils.log(windows.get(title) + " is already created, closing", LogUtils.Severity.WARN);
				customWindow.closeWindow();
				createFakeWindow(root, window, title, resizable, priority, centerOnDesktop, true, taskBarApps, image);
			} else {
				customWindow.bringToFront();
			}
		}
	}
	
	public static CustomWindow getWindow(String title) {
		return windows.get(title);
	}
}
