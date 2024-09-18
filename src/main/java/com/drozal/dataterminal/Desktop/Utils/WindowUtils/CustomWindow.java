package com.drozal.dataterminal.Desktop.Utils.WindowUtils;

import com.drozal.dataterminal.Desktop.Utils.AppUtils.DesktopApp;
import com.drozal.dataterminal.Desktop.Utils.AppUtils.TaskbarApp;
import com.drozal.dataterminal.Launcher;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import static com.drozal.dataterminal.DataTerminalHomeApplication.mainDesktopControllerObj;
import static com.drozal.dataterminal.Desktop.Utils.AppUtils.AppUtils.DesktopApps;
import static com.drozal.dataterminal.Desktop.Utils.WindowUtils.WindowManager.minimizedWindows;
import static com.drozal.dataterminal.Desktop.Utils.WindowUtils.WindowManager.windows;

public class CustomWindow {
	private static double xOffset;
	private static double yOffset;
	public final String title;
	private final int priority;
	private final Pane windowPane;
	private final Stage stage;
	public boolean isMinimized = false;
	private double originalWidth;
	private double originalHeight;
	private double originalX;
	private double originalY;
	private boolean isMaximized = false;
	public Object controller;
	private TaskbarApp taskbarApp;
	
	public CustomWindow(String fileName, String title, Stage stage, boolean resizable, int priority, HBox taskBarApps) throws IOException {
		URL fxmlUrl = Launcher.class.getResource(fileName);
		if (fxmlUrl == null) {
			throw new IOException("FXML file not found: " + fileName);
		}
		FXMLLoader loader = new FXMLLoader(fxmlUrl);
		Parent root = loader.load();
		this.windowPane = (BorderPane) root;
		this.title = title;
		this.stage = stage;
		this.priority = priority;
		controller = loader.getController();
		
		initializeWindow(resizable);
		addMainStageResizeListener();
		
		this.taskbarApp = new TaskbarApp(title, title, taskBarApps, this);
	}
	
	private void addMainStageResizeListener() {
		stage.widthProperty().addListener((obs, oldVal, newVal) -> keepWithinBounds());
		stage.heightProperty().addListener((obs, oldVal, newVal) -> keepWithinBounds());
	}
	
	private void keepWithinBounds() {
		double mainStageWidth = stage.getWidth();
		double mainStageHeight = stage.getHeight();
		
		double windowX = windowPane.getLayoutX();
		double windowY = windowPane.getLayoutY();
		double windowWidth = windowPane.getPrefWidth();
		double windowHeight = windowPane.getPrefHeight();
		
		if (windowX + windowWidth > mainStageWidth) {
			windowPane.setLayoutX(mainStageWidth - windowWidth);
		}
		if (windowY + windowHeight > mainStageHeight) {
			windowPane.setLayoutY(mainStageHeight - windowHeight);
		}
		
		if (windowX < 0) {
			windowPane.setLayoutX(0);
		}
		if (windowY < 0) {
			windowPane.setLayoutY(0);
		}
	}
	
	private void initializeWindow(boolean resizable) {
		windowPane.setStyle("-fx-border-color: black; -fx-background-color: white;");
		windowPane.setPrefSize(windowPane.prefWidth(-1), windowPane.prefHeight(-1));
		AnchorPane titleBar = createTitleBar(title);
		((BorderPane) windowPane).setTop(titleBar);
		if (resizable) {
			enableResize((BorderPane) windowPane);
		}
		windowPane.setOnMouseClicked(event -> bringToFront());
	}
	
	public void bringToFront() {
		
		int currentPriority = this.getPriority();
		
		boolean hasHigherPriorityWindow = windows.values().stream().anyMatch(
				window -> window.getPriority() > currentPriority);
		
		if (!hasHigherPriorityWindow) {
			this.getWindowPane().toFront();
		}
		
		windows.values().stream().filter(window -> window.getPriority() < currentPriority).forEach(
				window -> window.getWindowPane().toBack());
		
		windows.values().stream().filter(window -> window.getPriority() == currentPriority && window != this).forEach(
				window -> window.getWindowPane().toBack());
		
		if (mainDesktopControllerObj != null) {
			for (DesktopApp app : DesktopApps) {
				app.getMainPane().toBack();
			}
			mainDesktopControllerObj.getButton1().toBack();
			mainDesktopControllerObj.getTaskBar().toFront();
		}
	}
	
	public int getPriority() {
		return priority;
	}
	
	public AnchorPane createTitleBar(String titleText) {
		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setSaturation(-1.0);
		colorAdjust.setBrightness(-0.45);
		
		Label titleLabel = new Label(titleText);
		titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
		titleLabel.setAlignment(Pos.CENTER);
		AnchorPane.setLeftAnchor(titleLabel, 0.0);
		AnchorPane.setRightAnchor(titleLabel, 0.0);
		AnchorPane.setTopAnchor(titleLabel, 0.0);
		AnchorPane.setBottomAnchor(titleLabel, 0.0);
		titleLabel.setEffect(colorAdjust);
		titleLabel.setMouseTransparent(true);
		
		AnchorPane titleBar = new AnchorPane(titleLabel);
		titleBar.setMinHeight(30);
		titleBar.setStyle("-fx-background-color: #383838;");
		
		Image placeholderImage = new Image(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Logo.png"));
		ImageView placeholderImageView = new ImageView(placeholderImage);
		placeholderImageView.setFitWidth(49);
		placeholderImageView.setFitHeight(49);
		AnchorPane.setLeftAnchor(placeholderImageView, 0.0);
		AnchorPane.setTopAnchor(placeholderImageView, -10.0);
		AnchorPane.setBottomAnchor(placeholderImageView, -10.0);
		placeholderImageView.setEffect(colorAdjust);
		
		Image closeImage = new Image(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Logo.png"));
		ImageView closeImageView = new ImageView(closeImage);
		closeImageView.setFitWidth(15);
		closeImageView.setFitHeight(15);
		AnchorPane.setRightAnchor(closeImageView, 15.0);
		AnchorPane.setTopAnchor(closeImageView, 7.0);
		closeImageView.setEffect(colorAdjust);
		
		Image maximizeImage = new Image(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Logo.png"));
		ImageView maximizeImageView = new ImageView(maximizeImage);
		maximizeImageView.setFitWidth(15);
		maximizeImageView.setFitHeight(15);
		AnchorPane.setRightAnchor(maximizeImageView, 42.5);
		AnchorPane.setTopAnchor(maximizeImageView, 7.0);
		maximizeImageView.setEffect(colorAdjust);
		
		Image minimizeImage = new Image(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Logo.png"));
		ImageView minimizeImageView = new ImageView(minimizeImage);
		minimizeImageView.setFitWidth(15);
		minimizeImageView.setFitHeight(15);
		AnchorPane.setRightAnchor(minimizeImageView, 70.0);
		AnchorPane.setTopAnchor(minimizeImageView, 7.0);
		minimizeImageView.setEffect(colorAdjust);
		
		Rectangle closeRect = new Rectangle(20, 20);
		Rectangle maximizeRect = new Rectangle(20, 20);
		Rectangle minimizeRect = new Rectangle(20, 20);
		
		closeRect.setFill(Color.TRANSPARENT);
		minimizeRect.setFill(Color.TRANSPARENT);
		maximizeRect.setFill(Color.TRANSPARENT);
		
		closeRect.setOnMouseClicked(event -> closeWindow());
		minimizeRect.setOnMouseClicked(event -> minimizeWindow());
		maximizeRect.setOnMouseClicked(event -> toggleMaximize());
		
		AnchorPane.setRightAnchor(closeRect, 12.5);
		AnchorPane.setTopAnchor(closeRect, 6.3);
		AnchorPane.setRightAnchor(minimizeRect, 70.0);
		AnchorPane.setTopAnchor(minimizeRect, 6.3);
		AnchorPane.setRightAnchor(maximizeRect, 42.5);
		AnchorPane.setTopAnchor(maximizeRect, 6.3);
		
		titleBar.getChildren().addAll(placeholderImageView, closeRect, maximizeRect, minimizeRect, closeImageView,
		                              maximizeImageView, minimizeImageView);
		
		titleBar.setOnMousePressed(event -> {
			xOffset = event.getSceneX();
			yOffset = event.getSceneY();
		});
		
		titleBar.setOnMouseDragged(event -> {
			double deltaX = event.getSceneX() - xOffset;
			double deltaY = event.getSceneY() - yOffset;
			
			double newX = windowPane.getLayoutX() + deltaX;
			double newY = windowPane.getLayoutY() + deltaY;
			
			double maxWidth = stage.getWidth() - windowPane.getPrefWidth();
			double maxHeight = stage.getHeight() - windowPane.getPrefHeight();
			
			if (newX < 0) {
				newX = 0;
			}
			if (newY < 0) {
				newY = 0;
			}
			if (newX > maxWidth) {
				newX = maxWidth;
			}
			if (newY > maxHeight) {
				newY = maxHeight;
			}
			
			windowPane.setLayoutX(newX);
			windowPane.setLayoutY(newY);
			xOffset = event.getSceneX();
			yOffset = event.getSceneY();
		});
		
		closeRect.toFront();
		minimizeRect.toFront();
		maximizeRect.toFront();
		return titleBar;
	}
	
	private void enableResize(BorderPane pane) {
		final double resizeMargin = 10;
		
		pane.setOnMouseMoved(event -> {
			double x = event.getX();
			double y = event.getY();
			
			if (x > pane.getWidth() - resizeMargin && y > pane.getHeight() - resizeMargin) {
				pane.setCursor(javafx.scene.Cursor.SE_RESIZE);
			} else if (x > pane.getWidth() - resizeMargin) {
				pane.setCursor(javafx.scene.Cursor.E_RESIZE);
			} else if (y > pane.getHeight() - resizeMargin) {
				pane.setCursor(javafx.scene.Cursor.S_RESIZE);
			} else {
				pane.setCursor(javafx.scene.Cursor.DEFAULT);
			}
		});
		
		pane.setOnMouseDragged(event -> {
			double x = event.getX();
			double y = event.getY();
			
			if (pane.getCursor() == javafx.scene.Cursor.SE_RESIZE) {
				double newWidth = Math.max(x, 50);
				double newHeight = Math.max(y, 50);
				
				double maxWidth = stage.getWidth() - windowPane.getLayoutX();
				double maxHeight = stage.getHeight() - windowPane.getLayoutY();
				
				if (newWidth > maxWidth) {
					newWidth = maxWidth;
				}
				if (newHeight > maxHeight) {
					newHeight = maxHeight;
				}
				
				pane.setPrefSize(newWidth, newHeight);
			} else if (pane.getCursor() == javafx.scene.Cursor.E_RESIZE) {
				double newWidth = Math.max(x, 50);
				pane.setPrefWidth(newWidth);
			} else if (pane.getCursor() == javafx.scene.Cursor.S_RESIZE) {
				double newHeight = Math.max(y, 50);
				pane.setPrefHeight(newHeight);
			}
		});
	}
	
	public void closeWindow() {
		CustomWindow customWindow = windows.get(title);
		if (customWindow != null) {
			if (customWindow.getWindowPane().getParent() != null) {
				AnchorPane root = (AnchorPane) customWindow.getWindowPane().getParent();
				root.getChildren().remove(customWindow.getWindowPane());
			}
			windows.remove(title);
			minimizedWindows.remove(title);
			taskbarApp.removeApp();
		}
	}
	
	private void minimizeWindow() {
		CustomWindow customWindow = windows.get(title);
		if (customWindow != null) {
			customWindow.getWindowPane().setVisible(false);
			minimizedWindows.put(title, customWindow);
			isMinimized = true;
		}
	}
	
	public void restoreWindow(String title) {
		CustomWindow customWindow = minimizedWindows.get(title);
		if (customWindow != null) {
			customWindow.getWindowPane().setVisible(true);
			AnchorPane parent = (AnchorPane) customWindow.getWindowPane().getParent();
			if (parent != null && !parent.getChildren().contains(customWindow.getWindowPane())) {
				parent.getChildren().add(customWindow.getWindowPane());
			}
			minimizedWindows.remove(title);
			isMinimized = false;
		}
	}
	
	private void toggleMaximize() {
		if (isMaximized) {
			restoreWindowSize();
		} else {
			maximizeWindow();
		}
		isMaximized = !isMaximized;
	}
	
	private void maximizeWindow() {
		if (stage != null) {
			double stageWidth = stage.getWidth();
			double stageHeight = stage.getHeight();
			
			originalWidth = windowPane.getPrefWidth();
			originalHeight = windowPane.getPrefHeight();
			originalX = windowPane.getLayoutX();
			originalY = windowPane.getLayoutY();
			
			windowPane.setPrefWidth(stageWidth);
			windowPane.setPrefHeight(stageHeight);
			windowPane.setLayoutX(0);
			windowPane.setLayoutY(0);
		}
	}
	
	private void restoreWindowSize() {
		if (stage != null) {
			windowPane.setPrefWidth(originalWidth);
			windowPane.setPrefHeight(originalHeight);
			windowPane.setLayoutX(originalX);
			windowPane.setLayoutY(originalY);
		}
	}
	
	public void centerOnDesktop() {
		if (stage != null && windowPane != null) {
			double stageWidth = stage.getWidth();
			double stageHeight = stage.getHeight();
			
			double windowWidth = windowPane.getPrefWidth();
			double windowHeight = windowPane.getPrefHeight();
			
			double x = (stageWidth - windowWidth) / 2;
			double y = (stageHeight - windowHeight) / 2;
			
			windowPane.setLayoutX(x);
			windowPane.setLayoutY(y);
		}
	}
	
	public Pane getWindowPane() {
		return windowPane;
	}
}