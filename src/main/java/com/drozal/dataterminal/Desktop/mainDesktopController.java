package com.drozal.dataterminal.Desktop;

import com.drozal.dataterminal.DataTerminalHomeApplication;
import com.drozal.dataterminal.Desktop.Utils.AppUtils.DesktopApp;
import com.drozal.dataterminal.Desktop.Utils.WindowUtils.CustomWindow;
import com.drozal.dataterminal.Launcher;
import com.drozal.dataterminal.Windows.Main.actionController;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import static com.drozal.dataterminal.Desktop.Utils.AppUtils.AppUtils.editableDesktop;
import static com.drozal.dataterminal.Desktop.Utils.WindowUtils.WindowManager.createFakeWindow;

public class mainDesktopController {
	
	@javafx.fxml.FXML
	private Button button1;
	@javafx.fxml.FXML
	private BorderPane taskBar;
	@javafx.fxml.FXML
	private HBox taskBarApps;
	
	double verticalSpacing = 100.0;
	@javafx.fxml.FXML
	private AnchorPane bottomBar;
	@javafx.fxml.FXML
	private AnchorPane desktopContainer;
	@javafx.fxml.FXML
	private VBox container;
	public static CustomWindow mainApplicationWindow;
	
	private void addAppToDesktop(AnchorPane root, AnchorPane newApp, int appIndex) {
		AnchorPane.setLeftAnchor(newApp, 28.0);
		AnchorPane.setTopAnchor(newApp, 31.0 + (appIndex * verticalSpacing));
		root.getChildren().add(newApp);
	}
	
	public void initialize() {
		button1.setOnAction(event -> editableDesktop = !editableDesktop);
		
		DesktopApp desktopAppObj = new DesktopApp("Main App", new Image(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Logo.png")));
		AnchorPane newApp = desktopAppObj.createDesktopApp(mouseEvent -> {
			if (!editableDesktop) {
				if (mouseEvent.getClickCount() == 2) {
					mainApplicationWindow = createFakeWindow(desktopContainer,
					                                         "Windows/Main/DataTerminalHome-view.fxml", "Primary", true,
					                                         1, false, taskBarApps);
					assert mainApplicationWindow != null;
					DataTerminalHomeApplication.controller = (com.drozal.dataterminal.Windows.Main.actionController) mainApplicationWindow.controller;
				}
			}
		});
		addAppToDesktop(desktopContainer, newApp, 0);
		
		DesktopApp notesAppObj = new DesktopApp("Notes App", new Image(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Logo.png")));
		AnchorPane notesApp = notesAppObj.createDesktopApp(mouseEvent -> {
			if (!editableDesktop) {
				if (mouseEvent.getClickCount() == 2) {
					CustomWindow mainApp = createFakeWindow(desktopContainer, "Windows/Other/notes-view.fxml",
					                                        "Notes Application", true, 2, true, taskBarApps);
					actionController.notesViewController = (com.drozal.dataterminal.Windows.Other.NotesViewController) mainApp.controller;
				}
			}
		});
		addAppToDesktop(desktopContainer, notesApp, 1);
		
		DesktopApp settingsAppObj = new DesktopApp("Settings App", new Image(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Logo.png")));
		AnchorPane settingsApp = settingsAppObj.createDesktopApp(mouseEvent -> {
			if (!editableDesktop) {
				if (mouseEvent.getClickCount() == 2) {
					CustomWindow mainApp = createFakeWindow(desktopContainer, "Windows/Settings/settings-view.fxml",
					                                        "Settings Application", true, 2, true, taskBarApps);
				}
			}
		});
		addAppToDesktop(desktopContainer, settingsApp, 2);
		
		DesktopApp updatesAppObj = new DesktopApp("Updates App", new Image(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Logo.png")));
		AnchorPane updatesApp = updatesAppObj.createDesktopApp(mouseEvent -> {
			if (!editableDesktop) {
				if (mouseEvent.getClickCount() == 2) {
					CustomWindow mainApp = createFakeWindow(desktopContainer, "Windows/Misc/updates-view.fxml",
					                                        "Settings Application", true, 2, true, taskBarApps);
				}
			}
		});
		addAppToDesktop(desktopContainer, updatesApp, 3);
		
		Platform.runLater(() -> {
			// todo add ability for custom image
			Image image = new Image(
					Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/desktopBackground.jpg"));
			BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
			                                                      BackgroundRepeat.NO_REPEAT,
			                                                      BackgroundPosition.DEFAULT,
			                                                      new BackgroundSize(100, 100, true, true, false,
			                                                                         true));
			
			container.setBackground(new Background(backgroundImage));
		});
	}
	
	public Button getButton1() {
		return button1;
	}
	
	public BorderPane getTaskBar() {
		return taskBar;
	}
}