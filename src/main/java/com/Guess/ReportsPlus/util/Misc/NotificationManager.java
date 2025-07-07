package com.Guess.ReportsPlus.util.Misc;

import static com.Guess.ReportsPlus.MainApplication.mainDesktopControllerObj;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logDebug;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logWarn;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.changeImageColor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.config.ConfigReader;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class NotificationManager {
	public static final ArrayList<AnchorPane> currentNotifications = new ArrayList<>();
	public static final ScrollPane notificationContainerScrollPane = new ScrollPane();
	public static final VBox notificationContainer = new VBox(10);
	private static final Queue<Notification> notificationQueue = new LinkedList<>();

	public static void showNotificationInfo(String title, String message) {
		enqueueNotification(new Notification(NotificationType.INFO, title, message));
	}

	public static void showNotificationError(String title, String message) {
		enqueueNotification(new Notification(NotificationType.ERROR, title, message));
	}

	public static void showNotificationErrorPersistent(String title, String message) {
		enqueueNotification(new Notification(NotificationType.ERROR_PERSISTENT, title, message));
	}

	public static void showNotificationWarning(String title, String message) {
		enqueueNotification(new Notification(NotificationType.WARNING, title, message));
	}

	public static void showNotificationWarning(String title, String message, boolean persistent) {
		enqueueNotification(new Notification(NotificationType.WARNING, title, message, persistent));
	}

	private static void enqueueNotification(Notification notification) {
		Platform.runLater(() -> {
			notificationQueue.offer(notification);
			showNextNotification();
		});
	}

	private static void showNextNotification() {
		if (notificationQueue.isEmpty()) {
			return;
		}

		Notification notification = notificationQueue.poll();
		if (notification != null) {
			displayNotification(notification);
		}
	}

	private static void displayNotification(Notification notification) {
		Platform.runLater(() -> {
			try {
				boolean notificationsGloballyEnabled = ConfigReader.configRead("notificationSettings", "enabled")
						.equalsIgnoreCase("true");
				if (notification.persistent || notificationsGloballyEnabled) {

					String textClr = "#ffffff", primClr = "#db4437";
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
						logError("Could not pull notification color from config: ", e);
					}

					Label titleLabel = new Label(notification.title);
					titleLabel.setStyle(
							"-fx-font-family: 'Inter 28pt Bold'; -fx-font-size: 14px; -fx-text-fill: " + textClr + ";");

					Label messageLabel = new Label(notification.message);
					messageLabel.setWrapText(true);
					messageLabel.setStyle("-fx-font-family: 'Inter 28pt Medium'; -fx-font-size: 12px; -fx-text-fill: "
							+ textClr + ";");

					ImageView icon = new ImageView(new Image(
							Objects.requireNonNull(Launcher.class.getResourceAsStream("imgs/icons/warning.png"))));
					icon.setImage(changeImageColor(icon.getImage(), textClr));
					icon.setFitWidth(24);
					icon.setFitHeight(24);

					ImageView closeIcon = new ImageView(new Image(
							Objects.requireNonNull(Launcher.class.getResourceAsStream("imgs/icons/cross.png"))));
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

					notificationContainer.getChildren().add(0, anchorPane);
					notificationContainerScrollPane.setVvalue(0.0);
					currentNotifications.add(anchorPane);

					String configPosition = "BottomRight";
					try {
						configPosition = ConfigReader.configRead("notificationSettings", "notificationPosition");
					} catch (IOException e) {
						logError("Could not pull notificationPosition from config: ", e);
					}

					AnchorPane.clearConstraints(mainBox);
					AnchorPane.clearConstraints(notificationContainerScrollPane);

					switch (configPosition) {
						case "BottomLeft" -> {
							AnchorPane.setBottomAnchor(notificationContainerScrollPane, 20.0);
							AnchorPane.setLeftAnchor(notificationContainerScrollPane, 20.0);
							notificationContainer.setAlignment(Pos.BOTTOM_LEFT);
							AnchorPane.setBottomAnchor(mainBox, 0.0);
							AnchorPane.setLeftAnchor(mainBox, 0.0);
						}
						case "BottomRight" -> {
							AnchorPane.setBottomAnchor(notificationContainerScrollPane, 20.0);
							AnchorPane.setRightAnchor(notificationContainerScrollPane, 20.0);
							notificationContainer.setAlignment(Pos.BOTTOM_RIGHT);
							AnchorPane.setBottomAnchor(mainBox, 0.0);
							AnchorPane.setRightAnchor(mainBox, 0.0);
						}
						case "TopLeft" -> {
							AnchorPane.setTopAnchor(notificationContainerScrollPane, 20.0);
							AnchorPane.setLeftAnchor(notificationContainerScrollPane, 20.0);
							notificationContainer.setAlignment(Pos.TOP_LEFT);
							AnchorPane.setTopAnchor(mainBox, 0.0);
							AnchorPane.setLeftAnchor(mainBox, 0.0);
						}
						case "TopRight" -> {
							AnchorPane.setTopAnchor(notificationContainerScrollPane, 20.0);
							AnchorPane.setRightAnchor(notificationContainerScrollPane, 20.0);
							notificationContainer.setAlignment(Pos.TOP_RIGHT);
							AnchorPane.setTopAnchor(mainBox, 0.0);
							AnchorPane.setRightAnchor(mainBox, 0.0);
						}
					}

					notificationContainerScrollPane
							.setMaxHeight(mainDesktopControllerObj.getDesktopContainer().getHeight() - 50);

					closeButton.setOnAction(_ -> {
						notificationContainer.getChildren().remove(anchorPane);
						currentNotifications.remove(anchorPane);
						showNextNotification();
					});

					if (notification.type != NotificationType.ERROR_PERSISTENT && !notification.persistent) {
						String displayDuration = "3.5";
						try {
							displayDuration = ConfigReader.configRead("notificationSettings", "displayDuration");
						} catch (IOException e) {
							logError("Could not pull displayDuration from config: ", e);
						}
						String fadeDuration = "2.5";
						try {
							fadeDuration = ConfigReader.configRead("notificationSettings", "fadeOutDuration");
						} catch (IOException e) {
							logError("Could not pull fadeOutDuration from config: ", e);
						}

						PauseTransition pauseTransition = new PauseTransition(
								Duration.seconds(Double.parseDouble(displayDuration)));
						String finalFadeDuration = fadeDuration;
						pauseTransition.setOnFinished(_ -> {
							FadeTransition fadeOutTransition = new FadeTransition(
									Duration.seconds(Double.parseDouble(finalFadeDuration)), anchorPane);
							fadeOutTransition.setFromValue(1);
							fadeOutTransition.setToValue(0);
							fadeOutTransition.setOnFinished(e -> {
								notificationContainer.getChildren().remove(anchorPane);
								currentNotifications.remove(anchorPane);
								showNextNotification();
							});
							fadeOutTransition.play();
						});
						pauseTransition.play();
					}

				} else {
					logDebug("Notifications Are Disabled; not showing: [" + notification.title + "]");
				}
			} catch (IOException e) {
				logError("Error Getting NotificationsEnabled Setting: ", e);
			} catch (NullPointerException e) {
				logWarn("Notification config is null or a required resource is missing: " + e.getStackTrace());
			}
		});
	}

	private enum NotificationType {
		INFO, WARNING, ERROR, ERROR_PERSISTENT
	}

	private static class Notification {
		NotificationType type;
		String title;
		String message;
		boolean persistent = false;

		Notification(NotificationType type, String title, String message) {
			this.type = type;
			this.title = title;
			this.message = message;
			this.persistent = false;
		}

		Notification(NotificationType type, String title, String message, boolean persistent) {
			this.type = type;
			this.title = title;
			this.message = message;
			this.persistent = persistent;
		}
	}
}
