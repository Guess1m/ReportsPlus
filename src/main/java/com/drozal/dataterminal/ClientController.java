package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.util.Report.reportUtil;
import com.drozal.dataterminal.util.server.ClientUtils;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class ClientController {
	private static AnchorPane titleBar;
	@javafx.fxml.FXML
	private TextField inetField;
	@javafx.fxml.FXML
	private BorderPane root;
	@javafx.fxml.FXML
	private TextField portField;
	@javafx.fxml.FXML
	private TextField inputPortField;
	@javafx.fxml.FXML
	private TextField inputHostField;
	@javafx.fxml.FXML
	private Label statusLabel;
	
	public Label getStatusLabel() {
		return statusLabel;
	}
	
	public TextField getInetField() {
		return inetField;
	}
	
	public TextField getPortField() {
		return portField;
	}
	
	public void initialize() throws IOException {
		titleBar = reportUtil.createSimpleTitleBar("Client Interface", false);
		
		root.setTop(titleBar);
		
		if (ConfigReader.configRead("lastIPV4Connection") != null) {
			inputHostField.setText(ConfigReader.configRead("lastIPV4Connection"));
		}
		if (ConfigReader.configRead("lastPortConnection") != null) {
			inputPortField.setText(ConfigReader.configRead("lastPortConnection"));
		}
		
		inputPortField.textProperty()
		              .addListener((observable, oldValue, newValue) -> {
			              if (!newValue.matches("\\d*")) {
				              inputPortField.setText(newValue.replaceAll("[^\\d]", ""));
			              }
		              });
		
		/* Temp removed for localhost ability
		inputHostField.textProperty()
		              .addListener((observable, oldValue, newValue) -> {
			              if (!newValue.matches("[0-9.]*")) {
				              inputHostField.setText(oldValue);
			              }
		              });*/
	}
	
	@javafx.fxml.FXML
	public void connectBtnPress(ActionEvent actionEvent) throws IOException {
		if (!inputHostField.getText()
		                   .isEmpty() && !inputPortField.getText()
		                                                .isEmpty()) {
			ClientUtils.connectToService(inputHostField.getText(), Integer.parseInt(inputPortField.getText()));
		} else {
			String beforeText = statusLabel.getText();
			statusLabel.setText("Please Input The Server Address and Port");
			PauseTransition pause = new PauseTransition(Duration.seconds(1.2));
			pause.setOnFinished(event -> statusLabel.setText(beforeText));
			pause.play();
		}
	}
	
	@javafx.fxml.FXML
	public void helpBtnPress(ActionEvent actionEvent) {
		Stage stage = (Stage) root.getScene()
		                          .getWindow();
		showHelpDialog(stage);
	}
	
	private void showHelpDialog(Stage owner) {
		Alert helpDialog = new Alert(Alert.AlertType.INFORMATION);
		helpDialog.setTitle("Help");
		helpDialog.setHeaderText("How To Use The Client");
		helpDialog.setContentText(
				"1. The top left field is the port you would like to connect to use to connect to the server. This must match the port that the server was started with.\n\n" + "2. The bottom left field is the internal IP to connect to. This number needs to match the internal IP of the server.\n\n" + "3. The top right field is the internal IP of the server the client is connected to.\n\n" + "4. The bottom right field is the port of the established connection.\n\n");
		
		helpDialog.initOwner(owner);
		helpDialog.initModality(Modality.APPLICATION_MODAL);
		
		helpDialog.showAndWait();
	}
	
}
