package com.drozal.dataterminal.Desktop.Utils.AppUtils;

import com.drozal.dataterminal.Desktop.Utils.WindowUtils.CustomWindow;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class TaskbarApp {
	public static Map<String, ImageView> taskBarAppButtons = new HashMap<>();
	private final String name;
	private final HBox taskBarApps;
	private final CustomWindow customWindow;
	private final Image image;
	private ImageView appButton;
	private BorderPane container;
	
	public TaskbarApp(String name, String appTitle, HBox taskBarApps, CustomWindow customWindow, Image image) {
		this.name = name;
		this.taskBarApps = taskBarApps;
		this.customWindow = customWindow;
		this.image = image;
		
		initializeApp();
		addTooltip(appButton, appTitle);
	}
	
	private void addTooltip(ImageView icon, String tooltipText) {
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
				(obs, oldVal, newVal) -> updatePopupPosition(icon, popup, tooltipButton));
		tooltipButton.heightProperty().addListener(
				(obs, oldVal, newVal) -> updatePopupPosition(icon, popup, tooltipButton));
		
		container.setOnMouseEntered(event -> {
			updatePopupPosition(icon, popup, tooltipButton);
			popup.show(icon.getScene().getWindow());
			fadeIn.play();
		});
		
		container.setOnMouseExited(event -> fadeOut.play());
	}
	
	private void updatePopupPosition(ImageView icon, Popup popup, Button tooltipButton) {
		double buttonX = icon.localToScreen(icon.getBoundsInLocal()).getMinX();
		double buttonY = icon.localToScreen(icon.getBoundsInLocal()).getMinY();
		
		double buttonWidth = icon.getFitWidth();
		
		double popupWidth = tooltipButton.getWidth();
		double popupHeight = tooltipButton.getHeight();
		
		double x = buttonX + (buttonWidth - popupWidth) / 2;
		double y = buttonY - popupHeight - 5;
		
		popup.setX(x);
		popup.setY(y);
	}
	
	private void initializeApp() {
		appButton = new ImageView(image);
		appButton.setFitWidth(35);
		appButton.setFitHeight(35);
		appButton.setPreserveRatio(true);
		
		container = new BorderPane(appButton);
		container.setPadding(new Insets(0, 10, 0, 10));
		
		taskBarApps.getChildren().add(container);
		
		taskBarAppButtons.put(name, appButton);
		
		container.setOnMouseClicked(event -> {
			if (event.getClickCount() == 1) {
				if (customWindow != null) {
					if (customWindow.isMinimized) {
						customWindow.restoreWindow(customWindow.title);
					}
					customWindow.bringToFront();
				}
			}
		});
	}
	
	public void removeApp() {
		taskBarApps.getChildren().remove(container);
		taskBarAppButtons.remove(name);
		
		if (customWindow != null) {
			customWindow.closeWindow();
		}
	}
}
