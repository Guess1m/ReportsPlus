package com.drozal.dataterminal.util.Misc;

import com.drozal.dataterminal.Launcher;
import com.drozal.dataterminal.config.ConfigReader;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.controllerUtils.changeImageColor;

public class NotificationManager {
	private static final Queue<Notification> notificationQueue = new LinkedList<>();
	private static boolean isNotificationShowing = false;
	
	public static void showNotificationInfo(String title, String message, Object owner) {
		enqueueNotification(new Notification(NotificationType.INFO, title, message, owner));
	}
	
	public static void showNotificationError(String title, String message, Object owner) {
		enqueueNotification(new Notification(NotificationType.ERROR, title, message, owner));
	}
	
	public static void showNotificationWarning(String title, String message, Object owner) {
		enqueueNotification(new Notification(NotificationType.WARNING, title, message, owner));
	}
	
	private static void enqueueNotification(Notification notification) {
		notificationQueue.offer(notification);
		showNextNotification();
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
			Stage ownerStage = (Stage) notification.owner;
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
			Image coloredImage = changeImageColor(icon.getImage(), textClr);
			icon.setImage(coloredImage);
			icon.setFitWidth(24);
			icon.setFitHeight(24);
			
			ImageView closeIcon = new ImageView(
					new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("imgs/icons/cross.png"))));
			Image coloredImageClose = changeImageColor(closeIcon.getImage(), textClr);
			closeIcon.setImage(coloredImageClose);
			closeIcon.setFitWidth(12);
			closeIcon.setFitHeight(13);
			
			Button closeButton = new Button();
			closeButton.setGraphic(closeIcon);
			closeButton.setStyle("-fx-background-color: transparent;");
			
			AnchorPane closePane = new AnchorPane(closeButton);
			closePane.setStyle("-fx-background-color: rgba(50,50,50,0.2);");
			closePane.setPrefSize(29, 66);
			AnchorPane.setLeftAnchor(closeButton, 0.0);
			AnchorPane.setRightAnchor(closeButton, 0.0);
			AnchorPane.setTopAnchor(closeButton, 0.0);
			AnchorPane.setBottomAnchor(closeButton, 0.0);
			
			VBox contentBox = new VBox(5, titleLabel, messageLabel);
			contentBox.setAlignment(Pos.CENTER_LEFT);
			contentBox.setPadding(new Insets(0));
			contentBox.setStyle("-fx-background-color: " + primClr + "; -fx-background-radius: 7;");
			
			HBox mainBox = new HBox(10, icon, contentBox, closePane);
			mainBox.setAlignment(Pos.CENTER_LEFT);
			mainBox.setPadding(new Insets(0, 0, 0, 10));
			mainBox.setStyle("-fx-background-color: " + primClr + "; -fx-background-radius: 7;");
			
			AnchorPane anchorPane = new AnchorPane(mainBox);
			anchorPane.setStyle("-fx-background-color: " + primClr + ";");
			AnchorPane.setBottomAnchor(mainBox, 0.0);
			AnchorPane.setLeftAnchor(mainBox, 0.0);
			AnchorPane.setRightAnchor(mainBox, 0.0);
			AnchorPane.setTopAnchor(mainBox, 0.0);
			
			Stage popup = new Stage();
			popup.initOwner(ownerStage);
			popup.initStyle(StageStyle.TRANSPARENT);
			Scene scene = new Scene(anchorPane);
			scene.setFill(null);
			popup.setScene(scene);
			popup.setAlwaysOnTop(true);
			
			closeButton.setOnAction(event -> popup.hide());
			
			String configPosition = "BottomLeft";
			try {
				configPosition = ConfigReader.configRead("notificationSettings", "notificationPosition");
			} catch (IOException e) {
				logError("Could not pull notificationPosition from config: ", e);
			}
			
			popup.show();
			
			double x = ownerStage.getX() + 30;
			double y = ownerStage.getY() + ownerStage.getHeight() - 90;
			switch (configPosition) {
				case "BottomLeft" -> {
					x = ownerStage.getX() + 223;
					y = ownerStage.getY() + ownerStage.getHeight() - popup.getHeight() - 20;
				}
				case "BottomRight" -> {
					x = ownerStage.getX() + ownerStage.getWidth() - popup.getWidth() - 20;
					y = ownerStage.getY() + ownerStage.getHeight() - popup.getHeight() - 20;
				}
				case "TopLeft" -> {
					x = ownerStage.getX() + 223;
					y = ownerStage.getY() + 136;
				}
				case "TopRight" -> {
					x = ownerStage.getX() + ownerStage.getWidth() - popup.getWidth() - 20;
					y = ownerStage.getY() + 136;
				}
			}
			
			popup.setX(x);
			popup.setY(y);
			
			String displayDuration = "";
			try {
				displayDuration = ConfigReader.configRead("notificationSettings", "displayDuration");
			} catch (IOException e) {
				logError("Could not pull displayDuration from config: ", e);
			}
			String fadeOutDuration = "";
			try {
				fadeOutDuration = ConfigReader.configRead("notificationSettings", "fadeOutDuration");
			} catch (IOException e) {
				logError("Could not pull fadeOutDuration from config: ", e);
			}
			
			PauseTransition pauseTransition = new PauseTransition(
					Duration.seconds(Double.parseDouble(displayDuration)));
			String finalFadeOutDuration = fadeOutDuration;
			pauseTransition.setOnFinished(event -> {
				FadeTransition fadeOutTransition = new FadeTransition(
						Duration.seconds(Double.parseDouble(finalFadeOutDuration)), popup.getScene().getRoot());
				fadeOutTransition.setFromValue(1);
				fadeOutTransition.setToValue(0);
				fadeOutTransition.setOnFinished(e -> {
					popup.hide();
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
		Object owner;
		
		Notification(NotificationType type, String title, String message, Object owner) {
			this.type = type;
			this.title = title;
			this.message = message;
			this.owner = owner;
		}
	}
}