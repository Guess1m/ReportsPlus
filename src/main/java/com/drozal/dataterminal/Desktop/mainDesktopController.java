package com.drozal.dataterminal.Desktop;

import com.drozal.dataterminal.DataTerminalHomeApplication;
import com.drozal.dataterminal.Desktop.Utils.AppUtils.DesktopApp;
import com.drozal.dataterminal.Desktop.Utils.WindowUtils.CustomWindow;
import com.drozal.dataterminal.Launcher;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import static com.drozal.dataterminal.Desktop.Utils.AppUtils.AppUtils.editableDesktop;
import static com.drozal.dataterminal.Desktop.Utils.WindowUtils.WindowManager.createFakeWindow;

public class mainDesktopController {
	
	public static AnchorPane newApp;
	public static AnchorPane newApp2;
	@javafx.fxml.FXML
	private AnchorPane root1;
	@javafx.fxml.FXML
	private Button button1;
	@javafx.fxml.FXML
	private BorderPane taskBar;
	@javafx.fxml.FXML
	private HBox taskBarApps;
	
	public void initialize() {
		button1.setOnAction(event -> editableDesktop = !editableDesktop);
		
		DesktopApp desktopApp = new DesktopApp("TestApp1", new Image(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Logo.png")));
		newApp = desktopApp.createDesktopApp((mouseEvent -> {
			if (!editableDesktop) {
				if (mouseEvent.getClickCount() == 2) {
					CustomWindow mainApp = createFakeWindow(root1, "Windows/Main/DataTerminalHome-view.fxml", "Primary",
					                                        true, 3, taskBarApps);
					DataTerminalHomeApplication.controller = (com.drozal.dataterminal.Windows.Main.actionController) mainApp.controller;
				}
			}
		}));
		
		DesktopApp desktopApp2 = new DesktopApp("TestApp2", new Image(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Logo.png")));
		newApp2 = desktopApp2.createDesktopApp((mouseEvent -> {
			if (!editableDesktop) {
				if (mouseEvent.getClickCount() == 2) {
					createFakeWindow(root1, "Windows/Main/newOfficer-view.fxml", "Primary", true, 3, taskBarApps);
				}
			}
		}));
		root1.getChildren().addAll(newApp, newApp2);
		
		Platform.runLater(() -> {
			// todo add ability for custom image
			Image image = new Image(
					Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/desktopBackground.jpg"));
			BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
			                                                      BackgroundRepeat.NO_REPEAT,
			                                                      BackgroundPosition.DEFAULT,
			                                                      new BackgroundSize(100, 100, true, true, false,
			                                                                         true));
			
			root1.setBackground(new Background(backgroundImage));
		});
	}
	
	public Button getButton1() {
		return button1;
	}
	
	public BorderPane getTaskBar() {
		return taskBar;
	}
}