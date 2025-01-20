package com.Guess.ReportsPlus.Windows.Server;

import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.Guess.ReportsPlus.util.Server.ClientUtils;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Server.ClientUtils.isConnected;

public class ClientController {
	public static ClientController clientController;
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
	@javafx.fxml.FXML
	private Button connectBtn;
	@javafx.fxml.FXML
	private AnchorPane mainHeader;
	@javafx.fxml.FXML
	private Label lbl1;
	@javafx.fxml.FXML
	private Label lbl4;
	@javafx.fxml.FXML
	private Label lbl2;
	@javafx.fxml.FXML
	private Label lbl3;
	@javafx.fxml.FXML
	private Label clientLabel;
	@javafx.fxml.FXML
	private Button helpbtn;
	
	public void initialize() throws IOException {
		if (ConfigReader.configRead("connectionSettings", "lastIPV4Connection") != null) {
			inputHostField.setText(ConfigReader.configRead("connectionSettings", "lastIPV4Connection"));
		}
		if (ConfigReader.configRead("connectionSettings", "lastPortConnection") != null) {
			inputPortField.setText(ConfigReader.configRead("connectionSettings", "lastPortConnection"));
		}
		
		inputPortField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				inputPortField.setText(newValue.replaceAll("[^\\d]", ""));
			}
		});
		
		addLocalization();
		
		Platform.runLater(() -> {
			if (isConnected) {
				portField.setText(ClientUtils.port);
				inetField.setText(ClientUtils.inet);
				statusLabel.setText(localization.getLocalizedMessage("ServerConnectionWindow.Connected", "Connected"));
				statusLabel.setStyle("-fx-background-color: green;");
			} else {
				statusLabel.setText(
						localization.getLocalizedMessage("ServerConnectionWindow.NotConnected", "Not Connected"));
				statusLabel.setStyle("-fx-background-color: #ff5e5e;");
			}
		});
	}
	
	private void addLocalization() {
		clientLabel.setText(localization.getLocalizedMessage("ServerConnectionWindow.ClientHeading", "Client"));
		lbl1.setText(localization.getLocalizedMessage("ServerConnectionWindow.InputPortLabel", "Input Port:"));
		lbl2.setText(
				localization.getLocalizedMessage("ServerConnectionWindow.ConnectionIPV4Label", "Connection IPV4:"));
		lbl3.setText(localization.getLocalizedMessage("ServerConnectionWindow.InputAddressLabel", "Input Address:"));
		lbl4.setText(
				localization.getLocalizedMessage("ServerConnectionWindow.ConnectionPortLabel", "Connection Port:"));
		
		connectBtn.setText(localization.getLocalizedMessage("ServerConnectionWindow.ConnectButton", "Connect"));
		helpbtn.setText(localization.getLocalizedMessage("ServerConnectionWindow.Help", "Help"));
		helpbtn.setText(localization.getLocalizedMessage("ServerConnectionWindow.Help", "Help"));
		statusLabel.setText(localization.getLocalizedMessage("ServerConnectionWindow.NotConnected", "Not Connected"));
		
		inetField.setPromptText(
				localization.getLocalizedMessage("ServerConnectionWindow.NotConnected", "Not Connected"));
		portField.setPromptText(
				localization.getLocalizedMessage("ServerConnectionWindow.NotConnected", "Not Connected"));
		inputHostField.setPromptText(
				localization.getLocalizedMessage("ServerConnectionWindow.AddressPrompt", "Address"));
	}
	
	@javafx.fxml.FXML
	public void connectBtnPress() throws IOException {
		if (!isConnected) {
			if (!inputHostField.getText().isEmpty() && !inputPortField.getText().isEmpty()) {
				ClientUtils.connectToService(inputHostField.getText(), Integer.parseInt(inputPortField.getText()));
			} else {
				String beforeText = statusLabel.getText();
				statusLabel.setText(localization.getLocalizedMessage("ServerConnectionWindow.MissingFields",
				                                                     "Please Input The Server Address and Port"));
				PauseTransition pause = new PauseTransition(Duration.seconds(1.2));
				pause.setOnFinished(event -> statusLabel.setText(beforeText));
				pause.play();
			}
		} else {
			log("Tried to connect, but there is already a connection established", LogUtils.Severity.WARN);
		}
	}
	
	@javafx.fxml.FXML
	public void helpBtnPress() {
		Stage stage = (Stage) root.getScene().getWindow();
		showHelpDialog(stage);
	}
	
	private void showHelpDialog(Stage owner) {
		Alert helpDialog = new Alert(Alert.AlertType.INFORMATION);
		helpDialog.setTitle(localization.getLocalizedMessage("ServerConnectionWindow.Help", "Help"));
		helpDialog.setHeaderText(
				localization.getLocalizedMessage("ServerConnectionWindow.HowTo", "How To Use The Client"));
		helpDialog.setContentText(localization.getLocalizedMessage("ServerConnectionWindow.HelpLineOne",
		                                                           "1. The top left field is the port you would like to connect to use to connect to the server. This must match the port that the server was started with.") + "\n\n" + localization.getLocalizedMessage(
				"ServerConnectionWindow.HelpLineTwo",
				"2. The bottom left field is the internal IP to connect to. This number needs to match the internal IP of the server.") + "\n\n" + localization.getLocalizedMessage(
				"ServerConnectionWindow.HelpLineThree",
				"3. The top right field is the internal IP of the server the client is connected to.") + "\n\n" + localization.getLocalizedMessage(
				"ServerConnectionWindow.HelpLineFour",
				"4. The bottom right field is the port of the established connection.") + "\n\n");
		
		helpDialog.initOwner(owner);
		helpDialog.initModality(Modality.APPLICATION_MODAL);
		
		helpDialog.showAndWait();
	}
	
	public BorderPane getRoot() {
		return root;
	}
	
	public Label getLbl1() {
		return lbl1;
	}
	
	public Label getLbl2() {
		return lbl2;
	}
	
	public Label getLbl3() {
		return lbl3;
	}
	
	public Label getLbl4() {
		return lbl4;
	}
	
	public Label getStatusLabel() {
		return statusLabel;
	}
	
	public TextField getInetField() {
		return inetField;
	}
	
	public TextField getPortField() {
		return portField;
	}
	
	public AnchorPane getMainHeader() {
		return mainHeader;
	}
	
	public Button getConnectBtn() {
		return connectBtn;
	}
}
