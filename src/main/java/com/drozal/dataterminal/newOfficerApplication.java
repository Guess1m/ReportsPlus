package com.drozal.dataterminal;

import com.drozal.dataterminal.Desktop.mainDesktopController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class newOfficerApplication extends Application {
	
	public static mainDesktopController mainDesktopControllerObj;
	public static Stage mainDesktopStage;
	
	public static void main(String[] args) {
		launch();
	}
	
	/*
	todo remove old
	@Override
	public void start(Stage stage) throws Exception {
		Stage newOfficerStage = new Stage();
		FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("Windows/Main/newOfficer-view.fxml"));
		Parent root = loader.load();
		Scene newScene = new Scene(root);
		newOfficerStage.setTitle("New Officer Sign In");
		newOfficerStage.setScene(newScene);
		newOfficerStage.initStyle(StageStyle.UNDECORATED);
		newOfficerStage.setResizable(true);
		newOfficerStage.getIcons().add(new Image(
				Objects.requireNonNull(newOfficerApplication.class.getResourceAsStream("imgs/icons/Icon.png"))));
		newOfficerStage.show();
		newOfficerStage.setMinHeight(newOfficerStage.getHeight());
		newOfficerStage.setMinWidth(newOfficerStage.getWidth());
		newOfficerStage.setMaxHeight(newOfficerStage.getHeight());
		ResizeHelper.addResizeListener(newOfficerStage);
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
	}
	
}
