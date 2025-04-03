package com.Guess.ReportsPlus;

import com.Guess.ReportsPlus.Desktop.desktopLoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class newOfficerApplication extends Application {
	public static desktopLoginController desktopLoginController;
	
	public static void main(String[] args) {
		launch();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("Windows/Desktop/desktop-login.fxml"));
		Scene scene = new Scene(fxmlLoader.load());
		desktopLoginController = fxmlLoader.getController();
		primaryStage.setTitle("ReportsPlus Login");
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Logo.png")));
		primaryStage.show();
		
		primaryStage.setMaximized(true);
		
	}
}
