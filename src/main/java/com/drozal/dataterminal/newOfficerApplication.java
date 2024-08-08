package com.drozal.dataterminal;

import com.drozal.dataterminal.util.Window.ResizeHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class newOfficerApplication extends Application {
	
	public static void main(String[] args) {
		launch();
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		Stage newOfficerStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("newOfficer-view.fxml"));
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
	}
}
