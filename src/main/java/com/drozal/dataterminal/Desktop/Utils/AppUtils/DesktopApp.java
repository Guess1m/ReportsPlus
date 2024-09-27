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
		AnchorPane.setLeftAnchor(anchorPane, 28.0);
		AnchorPane.setTopAnchor(anchorPane, 31.0);
		
		BorderPane borderPaneButton = new BorderPane();
		AnchorPane.setBottomAnchor(borderPaneButton, 20.0);
		AnchorPane.setLeftAnchor(borderPaneButton, 0.0);
		AnchorPane.setRightAnchor(borderPaneButton, 0.0);
		AnchorPane.setTopAnchor(borderPaneButton, 0.0);
		
		Button appButton = createImageButton(image);
		borderPaneButton.setCenter(appButton);
		
		BorderPane borderPaneLabel = new BorderPane();
		AnchorPane.setBottomAnchor(borderPaneLabel, 0.0);
		AnchorPane.setLeftAnchor(borderPaneLabel, 0.0);
		AnchorPane.setRightAnchor(borderPaneLabel, 0.0);
		AnchorPane.setTopAnchor(borderPaneLabel, 65.0);
		
		Label appLabel = new Label(name);
		appLabel.setStyle("-fx-font-family: 'Segoe UI Semibold'; -fx-text-fill:  white; -fx-font-size: 13px;");
		appLabel.setMouseTransparent(true);
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
		imageView.setFitHeight(40);
		imageView.setFitWidth(40);
		imageView.setPreserveRatio(false);
		imageView.setMouseTransparent(true);
		button.setGraphic(imageView);
		button.setStyle("-fx-background-color: transparent;");
		
		return button;
	}
	
	private void setUpDragEvents(Button button, AnchorPane app) {
		AppUtils.setUpDragEvents(button, app);
	}
}