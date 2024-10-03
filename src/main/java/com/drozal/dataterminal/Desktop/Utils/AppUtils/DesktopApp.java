package com.drozal.dataterminal.Desktop.Utils.AppUtils;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;

import static com.drozal.dataterminal.Desktop.Utils.AppUtils.AppUtils.DesktopApps;

public class DesktopApp {
	
	private final String name;
	private final Image image;
	private AnchorPane mainPane;
	
	public DesktopApp(String name, Image image) {
		this.name = name;
		this.image = image;
	}
	
	public AnchorPane createDesktopApp(EventHandler<MouseEvent> mouseClickHandler) {
		if (name == null || image == null) {
			throw new IllegalArgumentException("Name and image cannot be null.");
		}
		
		AnchorPane anchorPane = new AnchorPane();
		AnchorPane.setLeftAnchor(anchorPane, 0.0);
		AnchorPane.setTopAnchor(anchorPane, 0.0);
		
		BorderPane borderPaneButton = new BorderPane();
		AnchorPane.setBottomAnchor(borderPaneButton, 0.0);
		AnchorPane.setLeftAnchor(borderPaneButton, 0.0);
		AnchorPane.setRightAnchor(borderPaneButton, 0.0);
		AnchorPane.setTopAnchor(borderPaneButton, 0.0);
		
		Button appButton = createImageButton(image);
		borderPaneButton.setCenter(appButton);
		
		BorderPane borderPaneLabel = new BorderPane();
		AnchorPane.setBottomAnchor(borderPaneLabel, 0.0);
		AnchorPane.setLeftAnchor(borderPaneLabel, 0.0);
		AnchorPane.setRightAnchor(borderPaneLabel, 0.0);
		AnchorPane.setTopAnchor(borderPaneLabel, 90.0);
		
		Label appLabel = new Label(name);
		appLabel.setStyle("-fx-font-family: 'Segoe UI Semibold'; -fx-text-fill:  white; -fx-font-size: 15px;");
		appLabel.setMouseTransparent(true);
		appLabel.setAlignment(Pos.BOTTOM_CENTER);
		appLabel.setTextAlignment(TextAlignment.CENTER);
		appLabel.setMaxWidth(95);
		appLabel.setMinWidth(95);
		borderPaneLabel.setCenter(appLabel);
		
		anchorPane.getChildren().addAll(borderPaneButton, borderPaneLabel);
		
		setUpDragEvents(appButton, anchorPane);
		
		appButton.setOnMouseClicked(mouseClickHandler);
		
		DesktopApps.add(this);
		mainPane = anchorPane;
		return anchorPane;
	}
	
	private Button createImageButton(Image image) {
		Button button = new Button();
		ImageView imageView = new ImageView(image);
		
		imageView.setFitHeight(48);
		imageView.setFitWidth(48);
		imageView.setPreserveRatio(true);
		imageView.setMouseTransparent(true);
		
		button.setGraphic(imageView);
		button.setStyle("-fx-background-color: transparent;");
		
		return button;
	}
	
	public AnchorPane getMainPane() {
		return mainPane;
	}
	
	private void setUpDragEvents(Button button, AnchorPane app) {
		AppUtils.setUpDragEvents(button, app);
	}
	
	public String getName() {
		return name;
	}
	
	public double getX() {
		return mainPane.getTranslateX();
	}
	
	public double getY() {
		return mainPane.getTranslateY();
	}
}
