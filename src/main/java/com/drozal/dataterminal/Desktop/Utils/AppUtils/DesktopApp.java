package com.drozal.dataterminal.Desktop.Utils.AppUtils;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import static com.drozal.dataterminal.Desktop.Utils.AppUtils.AppUtils.DesktopApps;

public class DesktopApp {
	
	private String name;
	private Image image;
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
		AnchorPane.setLeftAnchor(anchorPane, 28.0);
		AnchorPane.setTopAnchor(anchorPane, 31.0);
		
		// Create BorderPane for the button
		BorderPane borderPaneButton = new BorderPane();
		AnchorPane.setBottomAnchor(borderPaneButton, 20.0);
		AnchorPane.setLeftAnchor(borderPaneButton, 0.0);
		AnchorPane.setRightAnchor(borderPaneButton, 0.0);
		AnchorPane.setTopAnchor(borderPaneButton, 0.0);
		
		Button appButton = createImageButton(image);
		borderPaneButton.setCenter(appButton);
		
		// Create BorderPane for the label
		BorderPane borderPaneLabel = new BorderPane();
		AnchorPane.setBottomAnchor(borderPaneLabel, 0.0);
		AnchorPane.setLeftAnchor(borderPaneLabel, 0.0);
		AnchorPane.setRightAnchor(borderPaneLabel, 0.0);
		AnchorPane.setTopAnchor(borderPaneLabel, 65.0);
		
		Label appLabel = new Label(name);
		appLabel.setMouseTransparent(true);
		borderPaneLabel.setCenter(appLabel);
		
		// Add BorderPane children to the AnchorPane
		anchorPane.getChildren().addAll(borderPaneButton, borderPaneLabel);
		
		setUpDragEvents(appButton, anchorPane);
		
		// Setting up the mouse click event with the provided handler
		appButton.setOnMouseClicked(mouseClickHandler);
		
		DesktopApps.add(this);
		mainPane = anchorPane;
		return anchorPane;
	}
	
	public AnchorPane getMainPane() {
		return mainPane;
	}
	
	private Button createImageButton(Image image) {
		Button button = new Button();
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(51.0);
		imageView.setFitWidth(57.0);
		imageView.setPreserveRatio(true);
		imageView.setMouseTransparent(true);
		button.setGraphic(imageView);
		button.setStyle("-fx-background-color: transparent;");
		
		return button;
	}
	
	private void setUpDragEvents(Button button, AnchorPane app) {
		// Assuming you're using AppUtils for drag functionality
		AppUtils.setUpDragEvents(button, app);
	}
}