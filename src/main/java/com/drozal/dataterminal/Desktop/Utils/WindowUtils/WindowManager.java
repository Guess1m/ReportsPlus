package com.drozal.dataterminal.Desktop.Utils.WindowUtils;

import com.drozal.dataterminal.Launcher;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class WindowManager {
	public static Map<String, CustomWindow> windows = new HashMap<>();
	public static Map<String, CustomWindow> minimizedWindows = new HashMap<>();
	
	public static CustomWindow createFakeWindow(AnchorPane root, String fileName, String title, boolean resizable, int priority, HBox taskBarApps) {
		if (!windows.containsKey(title)) {
			try {
				URL fxmlUrl = Launcher.class.getResource(fileName);
				if (fxmlUrl == null) {
					throw new RuntimeException("FXML file not found: " + fileName);
				}
				CustomWindow customWindow = new CustomWindow(fileName, title, resizable, priority, taskBarApps, root);
				
				if (root != null) {
					root.getChildren().add(customWindow.getWindowPane());
					customWindow.centerOnDesktop();
					
					windows.put(title, customWindow);
					
					reorderWindowsByPriority(root);
				}
				return customWindow;
			} catch (IOException e) {
				throw new RuntimeException("Could not create new CustomWindow: " + e);
			}
		}
		return null;
	}
	
	private static void reorderWindowsByPriority(AnchorPane root) {
		root.getChildren().removeIf(
				node -> windows.values().stream().anyMatch(window -> window.getWindowPane().equals(node)));
		
		windows.values().stream().sorted(Comparator.comparingInt(CustomWindow::getPriority)).forEach(
				window -> root.getChildren().add(window.getWindowPane()));
	}
}
