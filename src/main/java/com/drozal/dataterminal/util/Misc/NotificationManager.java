package com.drozal.dataterminal.util.Misc;

import com.drozal.dataterminal.Launcher;
import com.drozal.dataterminal.config.ConfigReader;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

import static com.drozal.dataterminal.DataTerminalHomeApplication.mainDesktopControllerObj;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.controllerUtils.changeImageColor;

public class NotificationManager {
	public static final ArrayList<AnchorPane> currentNotifications = new ArrayList<>();
	private static final Queue<Notification> notificationQueue = new LinkedList<>();
	private static boolean isNotificationShowing = false;
	
	public static void showNotificationInfo(String title, String message) {
		enqueueNotification(new Notification(NotificationType.INFO, title, message));
	}
	
	public static void showNotificationError(String title, String message) {
		enqueueNotification(new Notification(NotificationType.ERROR, title, message));
	}
	
	public static void showNotificationWarning(String title, String message) {
		enqueueNotification(new Notification(NotificationType.WARNING, title, message));
	}
	
	private static void enqueueNotification(Notification notification) {
		try {
			if (ConfigReader.configRead("notificationSettings", "enabled").equalsIgnoreCase("true")) {
				notificationQueue.offer(notification);
				showNextNotification();
			} else {
				log("Notifications Are Disabled", LogUtils.Severity.DEBUG);
			}
		} catch (IOException e) {
			logError("Error Getting NotificationsEnabled Setting: ", e);
		}
	}
	
	private static void showNextNotification() {
		if (isNotificationShowing || notificationQueue.isEmpty()) {
			return;
		}
		
		Notification notification = notificationQueue.poll();
		if (notification != null) {
			isNotificationShowing = true;
			displayNotification(notification);
		}
	}
	
	private static void displayNotification(Notification notification) {
		Platform.runLater(() -> {
			String textClr, primClr;
			try {
				if (notification.type == NotificationType.INFO) {
					textClr = ConfigReader.configRead("notificationSettings", "notificationInfoTextColor");
					primClr = ConfigReader.configRead("notificationSettings", "notificationInfoPrimary");
				} else if (notification.type == NotificationType.WARNING) {
					textClr = ConfigReader.configRead("notificationSettings", "notificationWarnTextColor");
					primClr = ConfigReader.configRead("notificationSettings", "notificationWarnPrimary");
				} else {
					textClr = "#ffffff";
					primClr = "#db4437";
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
			Label titleLabel = new Label(notification.title);
			titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: " + textClr + ";");
			
			Label messageLabel = new Label(notification.message);
			messageLabel.setWrapText(true);
			messageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + textClr + ";");
			
			ImageView icon = new ImageView(
					new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("imgs/icons/warning.png"))));
			icon.setImage(changeImageColor(icon.getImage(), textClr));
			icon.setFitWidth(24);
			icon.setFitHeight(24);
			
			ImageView closeIcon = new ImageView(
					new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("imgs/icons/cross.png"))));
			closeIcon.setImage(changeImageColor(closeIcon.getImage(), textClr));
			closeIcon.setFitWidth(12);
			closeIcon.setFitHeight(13);
			
			Button closeButton = new Button();
			closeButton.setGraphic(closeIcon);
			closeButton.setStyle("-fx-background-color: transparent;");
			
			VBox contentBox = new VBox(5, titleLabel, messageLabel);
			contentBox.setAlignment(Pos.CENTER_LEFT);
			contentBox.setPadding(new Insets(0));
			contentBox.setStyle("-fx-background-color: " + primClr + "; -fx-background-radius: 7;");
			
			HBox mainBox = new HBox(10, icon, contentBox, closeButton);
			mainBox.setAlignment(Pos.CENTER_LEFT);
			mainBox.setPadding(new Insets(10));
			mainBox.setStyle("-fx-background-color: " + primClr + "; -fx-background-radius: 7;");
			
			AnchorPane anchorPane = new AnchorPane(mainBox);
			
			mainDesktopControllerObj.getDesktopContainer().getChildren().add(anchorPane);
			currentNotifications.add(anchorPane);
			
			String configPosition = "BottomLeft";
			try {
				configPosition = ConfigReader.configRead("notificationSettings", "notificationPosition");
			} catch (IOException e) {
				logError("Could not pull notificationPosition from config: ", e);
			}
			
			switch (configPosition) {
				case "BottomLeft" -> {
					AnchorPane.setBottomAnchor(anchorPane, 20.0);
					AnchorPane.setLeftAnchor(anchorPane, 20.0);
				}
				case "BottomRight" -> {
					AnchorPane.setBottomAnchor(anchorPane, 20.0);
					AnchorPane.setRightAnchor(anchorPane, 20.0);
				}
				case "TopLeft" -> {
					AnchorPane.setTopAnchor(anchorPane, 20.0);
					AnchorPane.setLeftAnchor(anchorPane, 20.0);
				}
				case "TopRight" -> {
					AnchorPane.setTopAnchor(anchorPane, 20.0);
					AnchorPane.setRightAnchor(anchorPane, 20.0);
				}
			}
			
			anchorPane.toFront();
			
			closeButton.setOnAction(event -> {
				mainDesktopControllerObj.getDesktopContainer().getChildren().remove(anchorPane);
				currentNotifications.remove(anchorPane);
				isNotificationShowing = false;
				showNextNotification();
			});
			
			String displayDuration = "1.2";
			try {
				displayDuration = ConfigReader.configRead("notificationSettings", "displayDuration");
			} catch (IOException e) {
				logError("Could not pull displayDuration from config: ", e);
			}
			String fadeDuration = "1.7";
			try {
				fadeDuration = ConfigReader.configRead("notificationSettings", "fadeOutDuration");
			} catch (IOException e) {
				logError("Could not pull fadeOutDuration from config: ", e);
			}
			
			PauseTransition pauseTransition = new PauseTransition(
					Duration.seconds(Double.parseDouble(displayDuration)));
			String finalFadeDuration = fadeDuration;
			pauseTransition.setOnFinished(event -> {
				FadeTransition fadeOutTransition = new FadeTransition(
						Duration.seconds(Double.parseDouble(finalFadeDuration)), anchorPane);
				fadeOutTransition.setFromValue(1);
				fadeOutTransition.setToValue(0);
				fadeOutTransition.setOnFinished(e -> {
					mainDesktopControllerObj.getDesktopContainer().getChildren().remove(anchorPane);
					currentNotifications.remove(anchorPane);
					isNotificationShowing = false;
					showNextNotification();
				});
				fadeOutTransition.play();
			});
			pauseTransition.play();
		});
	}
	
	private enum NotificationType {
		INFO, WARNING, ERROR
	}
	
	private static class Notification {
		NotificationType type;
		String title;
		String message;
		
		Notification(NotificationType type, String title, String message) {
			this.type = type;
			this.title = title;
			this.message = message;
		}
	}
}
