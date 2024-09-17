package com.drozal.dataterminal.Desktop.Utils.WindowUtils;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static com.drozal.dataterminal.newOfficerApplication.mainDesktopStage;

public class WindowManager {
	public static Map<String, CustomWindow> windows = new HashMap<>();
	public static Map<String, CustomWindow> minimizedWindows = new HashMap<>();
	
	public static void createFakeWindow(AnchorPane root, String fileName, String title, boolean resizable, int priority, HBox taskBarApps) {
		if (!windows.containsKey(title)) {
			CustomWindow customWindow = null;
			try {
				customWindow = new CustomWindow(fileName, title, mainDesktopStage, resizable, priority, taskBarApps);
			} catch (IOException e) {
				throw new RuntimeException("Could not create new CustomWindow: " + e);
			}
			
			if (root != null) {
				root.getChildren().add(customWindow.getWindowPane());
				customWindow.centerOnDesktop();
				
				windows.put(title, customWindow);
				
				reorderWindowsByPriority(root);
			}
		}
	}
	
	private static void reorderWindowsByPriority(AnchorPane root) {
		root.getChildren().removeIf(
				node -> windows.values().stream().anyMatch(window -> window.getWindowPane().equals(node)));
		
		windows.values().stream().sorted(Comparator.comparingInt(CustomWindow::getPriority)).forEach(
				window -> root.getChildren().add(window.getWindowPane()));
	}
}
