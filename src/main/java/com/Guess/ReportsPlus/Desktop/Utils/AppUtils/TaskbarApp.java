package com.Guess.ReportsPlus.Desktop.Utils.AppUtils;

import java.util.HashMap;
import java.util.Map;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.util.Duration;

public class TaskbarApp {
	public static Map<String, ImageView> taskBarAppButtons = new HashMap<>();
	private final String name;
	private final HBox taskBarApps;
	private final CustomWindow customWindow;
	private final Image image;
	private final int height = 14;
	private ImageView appButton;
	private BorderPane container;
	private Popup tooltipPopup = new Popup();
	private Popup optionsPopup = new Popup();
	private boolean optionsPopupVisible = false;

	public TaskbarApp(String name, String appTitle, HBox taskBarApps, CustomWindow customWindow, Image image) {
		this.name = name;
		this.taskBarApps = taskBarApps;
		this.customWindow = customWindow;
		this.image = image;

		initializeApp();
		addTooltip(appButton, appTitle);
	}

	private void addTooltip(ImageView icon, String tooltipText) {
		Button tooltipButton = new Button(tooltipText);
		tooltipButton.setStyle(
				"-fx-background-color: rgb(0,0,0,0.3); -fx-text-fill: white; -fx-padding: 5 10; -fx-border-color: #313131; -fx-border-width: 1; -fx-background-radius: 5; -fx-border-radius: 5;");
		tooltipPopup.getContent().clear();
		tooltipPopup.getContent().add(tooltipButton);

		FadeTransition fadeIn = new FadeTransition(Duration.millis(100), tooltipButton);
		fadeIn.setFromValue(0);
		fadeIn.setToValue(1);

		FadeTransition fadeOut = new FadeTransition(Duration.millis(100), tooltipButton);
		fadeOut.setFromValue(1);
		fadeOut.setToValue(0);
		fadeOut.setOnFinished(event -> tooltipPopup.hide());

		tooltipButton.widthProperty()
				.addListener((obs, oldVal, newVal) -> updatePopupPosition(icon, tooltipPopup, tooltipButton));
		tooltipButton.heightProperty()
				.addListener((obs, oldVal, newVal) -> updatePopupPosition(icon, tooltipPopup, tooltipButton));

		container.setOnMouseEntered(event -> {
			if (!optionsPopupVisible) {
				updatePopupPosition(icon, tooltipPopup, tooltipButton);
				tooltipPopup.show(icon.getScene().getWindow());
				fadeIn.play();
			}
		});

		container.setOnMouseExited(event -> fadeOut.play());
	}

	private void addOptionsPopup(ImageView icon) {
		VBox buttonContainer = new VBox(5);
		buttonContainer.setAlignment(Pos.CENTER_LEFT);
		buttonContainer.setSpacing(0);
		buttonContainer.setPadding(new Insets(2, 5, 2, 5));
		buttonContainer.setStyle(
				"-fx-background-color: rgb(0,0,0,0.3); -fx-border-color: #313131; -fx-border-width: 1; -fx-background-radius: 5; -fx-border-radius: 5;");
		buttonContainer.getChildren().add(createStyledButton("Close", _ -> {
			customWindow.closeWindow();
			optionsPopup.hide();
			optionsPopupVisible = false;
		}));

		if (!customWindow.isMinimized) {
			buttonContainer.getChildren().add(createStyledButton("Minimize", _ -> {
				customWindow.minimizeWindow();
				optionsPopup.hide();
				optionsPopupVisible = false;
			}));
		}

		buttonContainer.widthProperty()
				.addListener((obs, oldVal, newVal) -> updatePopupPosition(icon, optionsPopup, buttonContainer));
		buttonContainer.heightProperty()
				.addListener((obs, oldVal, newVal) -> updatePopupPosition(icon, optionsPopup, buttonContainer));

		optionsPopup.getContent().clear();
		optionsPopup.getContent().add(buttonContainer);

		optionsPopup.show(icon.getScene().getWindow());
		optionsPopupVisible = true;
	}

	private Button createStyledButton(String text, EventHandler<ActionEvent> action) {
		Button button = new Button(text);
		button.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
		button.setOnAction(action);
		return button;
	}

	private void updatePopupPosition(ImageView icon, Popup popup, VBox container) {
		double buttonX = icon.localToScreen(icon.getBoundsInLocal()).getMinX();
		double buttonY = icon.localToScreen(icon.getBoundsInLocal()).getMinY();

		double popupWidth = 0;
		double popupHeight = 0;

		popupWidth = Math.max(popupWidth, container.getWidth());
		popupHeight += container.getHeight();

		double x = buttonX + (icon.getFitWidth() - popupWidth) / 2;
		double y = buttonY - popupHeight - height;

		popup.setX(x);
		popup.setY(y);
	}

	private void updatePopupPosition(ImageView icon, Popup popup, Button... buttons) {
		double buttonX = icon.localToScreen(icon.getBoundsInLocal()).getMinX();
		double buttonY = icon.localToScreen(icon.getBoundsInLocal()).getMinY();

		double popupWidth = 0;
		double popupHeight = 0;

		for (Button btn : buttons) {
			popupWidth = Math.max(popupWidth, btn.getWidth());
			popupHeight += btn.getHeight();
		}

		double x = buttonX + (icon.getFitWidth() - popupWidth) / 2;
		double y = buttonY - popupHeight - height;

		popup.setX(x);
		popup.setY(y);
	}

	private void initializeApp() {
		appButton = new ImageView(image);
		appButton.setFitWidth(30);
		appButton.setFitHeight(30);
		appButton.setPreserveRatio(true);

		container = new BorderPane(appButton);
		container.setPadding(new Insets(0, 10, 0, 10));

		Platform.runLater(() -> taskBarApps.getChildren().add(container));

		taskBarAppButtons.put(name, appButton);

		container.setOnMouseClicked(event -> {
			if (event.getClickCount() == 1 && event.getButton() == MouseButton.PRIMARY) {
				if (customWindow != null) {
					if (customWindow.isMinimized) {
						customWindow.restoreWindow(customWindow.title);
					}
					customWindow.bringToFront();
				}
			} else if (event.getButton() == MouseButton.SECONDARY) {
				if (optionsPopupVisible) {
					optionsPopup.hide();
					optionsPopupVisible = false;
				} else {
					tooltipPopup.hide();
					addOptionsPopup(appButton);
				}
			}
		});
	}

	public void removeApp() {
		if (taskBarApps.getChildren().contains(container)) {
			taskBarApps.getChildren().remove(container);
		}
		if (taskBarAppButtons.containsKey(name)) {
			taskBarAppButtons.remove(name);
		}
	}
}
