package com.drozal.dataterminal;

import com.drozal.dataterminal.Desktop.mainDesktopController;
import com.drozal.dataterminal.Windows.Main.actionController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataTerminalHomeApplication extends Application {
	
	public static Stage mainRT;
	public static actionController controller;
	public static mainDesktopController mainDesktopControllerObj;
	public static Stage mainDesktopStage;
	
	public static String getDate() {
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
		return currentTime.format(formatter);
	}
	
	public static String getTime() {
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a", Locale.ENGLISH);
		return currentTime.format(formatter);
	}
	
	public static void main(String[] args) {
		launch();
	}
	
	// todo remove old application launch args
	/*@Override
	public void start(Stage stage) throws IOException {
		
		mainRT = new Stage();
		FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("Windows/Main/DataTerminalHome-view.fxml"));
		mainRT.initStyle(StageStyle.UNDECORATED);
		Parent root = loader.load();
		controller = loader.getController();
		Scene scene = new Scene(root);
		mainRT.setScene(scene);
		mainRT.getIcons().add(new Image(
				Objects.requireNonNull(newOfficerApplication.class.getResourceAsStream("imgs/icons/Icon.png"))));
		mainRT.show();
		
		String startupValue = ConfigReader.configRead("layout", "mainWindowLayout");
		switch (startupValue) {
			case "TopLeft" -> snapToTopLeft(mainRT);
			case "TopRight" -> snapToTopRight(mainRT);
			case "BottomLeft" -> snapToBottomLeft(mainRT);
			case "BottomRight" -> snapToBottomRight(mainRT);
			case "FullLeft" -> snapToLeft(mainRT);
			case "FullRight" -> snapToRight(mainRT);
			default -> {
				mainRT.centerOnScreen();
				mainRT.setMinHeight(450);
				mainRT.setMinWidth(450);
				if (ConfigReader.configRead("uiSettings", "fullscreenOnStartup").equals("true")) {
					setWindowedFullscreen(mainRT);
					
				} else {
					mainRT.setHeight(800);
					mainRT.setWidth(1150);
				}
			}
		}
		
		mainRT.setAlwaysOnTop(false);
	}*/
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("Windows/Desktop/desktop-main.fxml"));
		Scene scene = new Scene(fxmlLoader.load());
		mainDesktopControllerObj = fxmlLoader.getController();
		primaryStage.setTitle("Simulation Desktop");
		primaryStage.setScene(scene);
		primaryStage.show();
		mainDesktopStage = primaryStage;
		
		DataTerminalHomeApplication.mainRT = mainDesktopStage;
		
	}
}