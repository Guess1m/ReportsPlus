package com.drozal.dataterminal.Desktop.Utils.AppUtils;

import com.drozal.dataterminal.Desktop.Utils.WindowUtils.CustomWindow;
import javafx.animation.FadeTransition;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class TaskbarApp {
	public static Map<String, Button> taskBarAppButtons = new HashMap<>();
	private final String name;
	private final String appTitle;
	private final HBox taskBarApps;
	private final CustomWindow customWindow;
	private Button appButton;
	
	public TaskbarApp(String name, String appTitle, HBox taskBarApps, CustomWindow customWindow) {
		this.name = name;
		this.appTitle = appTitle;
		this.taskBarApps = taskBarApps;
		this.customWindow = customWindow;
		
		initializeApp();
	}
	
	private void addTooltip(Button button, String tooltipText) {
		Popup popup = new Popup();
		Button tooltipButton = new Button(tooltipText);
		tooltipButton.setStyle("-fx-background-color: rgb(255,255,255,0.6); -fx-text-fill: black; -fx-padding: 5;");
		popup.getContent().add(tooltipButton);
		
		FadeTransition fadeIn = new FadeTransition(Duration.millis(250), tooltipButton);
		fadeIn.setFromValue(0);
		fadeIn.setToValue(1);
		
		FadeTransition fadeOut = new FadeTransition(Duration.millis(250), tooltipButton);
		fadeOut.setFromValue(1);
		fadeOut.setToValue(0);
		fadeOut.setOnFinished(event -> popup.hide());
		
		tooltipButton.widthProperty().addListener(
				(obs, oldVal, newVal) -> updatePopupPosition(button, popup, tooltipButton));
		tooltipButton.heightProperty().addListener(
				(obs, oldVal, newVal) -> updatePopupPosition(button, popup, tooltipButton));
		
		button.setOnMouseEntered(event -> {
			popup.show(button.getScene().getWindow());
			fadeIn.play();
		});
		
		button.setOnMouseExited(event -> fadeOut.play());
	}
	
	private void updatePopupPosition(Button button, Popup popup, Button tooltipButton) {
		double buttonX = button.localToScreen(button.getBoundsInLocal()).getMinX();
		double buttonY = button.localToScreen(button.getBoundsInLocal()).getMinY();
		
		double buttonWidth = button.getWidth();
		
		double popupWidth = tooltipButton.getWidth();
		double popupHeight = tooltipButton.getHeight();
		
		double x = buttonX + (buttonWidth - popupWidth) / 2;
		double y = buttonY - popupHeight;
		
		popup.setX(x);
		popup.setY(y - 7);
	}
	
	private void initializeApp() {
		
		appButton = new Button(appTitle.charAt(0) + "");
		appButton.setPrefSize(42, 39);
		
		appButton.setStyle("-fx-background-color: white; -fx-text-fill: black;");
		addTooltip(appButton, appTitle);
		
		taskBarApps.getChildren().add(appButton);
		
		taskBarAppButtons.put(name, appButton);
		
		appButton.setOnAction(event -> {
			if (customWindow != null) {
				if (customWindow.isMinimized) {
					customWindow.restoreWindow(customWindow.title);
				}
				customWindow.bringToFront();
			}
		});
	}
	
	public void removeApp() {
		
		taskBarApps.getChildren().remove(appButton);
		
		taskBarAppButtons.remove(name);
		
		if (customWindow != null) {
			customWindow.closeWindow();
		}
	}
}
