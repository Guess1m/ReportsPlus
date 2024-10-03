package com.drozal.dataterminal.Desktop;

import com.drozal.dataterminal.DataTerminalHomeApplication;
import com.drozal.dataterminal.Desktop.Utils.AppUtils.AppConfig.appConfig;
import com.drozal.dataterminal.Launcher;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.newOfficerApplication;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Misc.dropdownInfo;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.drozal.dataterminal.DataTerminalHomeApplication.mainDesktopControllerObj;
import static com.drozal.dataterminal.DataTerminalHomeApplication.mainDesktopStage;
import static com.drozal.dataterminal.Desktop.Utils.AppUtils.AppConfig.appConfig.checkAndSetDefaultAppValues;
import static com.drozal.dataterminal.config.ConfigReader.checkAndSetDefaultValues;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;

public class desktopLoginController {
	@javafx.fxml.FXML
	private ComboBox agencyDropDown;
	@javafx.fxml.FXML
	private AnchorPane vbox;
	@javafx.fxml.FXML
	private Button loginBtn;
	@javafx.fxml.FXML
	private TextField numberField;
	@javafx.fxml.FXML
	private TextField nameField;
	@javafx.fxml.FXML
	private ComboBox divisionDropDown;
	@javafx.fxml.FXML
	private ComboBox rankDropdown;
	@javafx.fxml.FXML
	private Label incompleteLabel;
	
	public void initialize() {
		rankDropdown.getItems().addAll(dropdownInfo.ranks);
		divisionDropDown.getItems().addAll(dropdownInfo.divisions);
		divisionDropDown.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			@Override
			public ListCell<String> call(ListView<String> p) {
				return new ListCell<>() {
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (item == null || empty) {
							setText(null);
						} else {
							setText(item);
							setAlignment(javafx.geometry.Pos.CENTER);
							
							if (item.contains("=")) {
								setStyle("-fx-font-weight: bold;");
							} else {
								setStyle("-fx-font-weight: none;");
							}
						}
					}
				};
			}
		});
		agencyDropDown.getItems().addAll(dropdownInfo.agencies);
	}
	
	@javafx.fxml.FXML
	public void loginButtonClick(ActionEvent actionEvent) throws IOException {
		if (agencyDropDown.getValue() == null || divisionDropDown.getValue() == null || rankDropdown.getValue() == null || nameField.getText().isEmpty() || numberField.getText().isEmpty()) {
			incompleteLabel.setText("Fill Out Form.");
			incompleteLabel.setStyle("-fx-text-fill: red;");
			incompleteLabel.setVisible(true);
			Timeline timeline1 = new Timeline(
					new KeyFrame(Duration.seconds(1), evt -> incompleteLabel.setVisible(false)));
			timeline1.play();
		} else {
			String jarPath = null;
			try {
				jarPath = newOfficerApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			} catch (URISyntaxException e) {
				logError("NewOfficer JarPath URISyntax Exception", e);
			}
			
			jarPath = URLDecoder.decode(Objects.requireNonNull(jarPath), StandardCharsets.UTF_8);
			
			String jarDir = new File(jarPath).getParent();
			
			String configFilePath = jarDir + File.separator + "config.properties";
			File configFile = new File(configFilePath);
			if (configFile.exists()) {
				log("exists, printing values", LogUtils.Severity.INFO);
			} else {
				try {
					configFile.createNewFile();
					log("Config: " + configFile.getAbsolutePath(), LogUtils.Severity.INFO);
				} catch (IOException e) {
					logError("Failed to create config file", e);
				}
			}
			appConfig.createAppConfig();
			
			String agency = agencyDropDown.getValue().toString().trim();
			String division = divisionDropDown.getValue().toString().trim();
			String rank = rankDropdown.getValue().toString().trim();
			
			ConfigWriter.configwrite("userInfo", "Agency", agency);
			ConfigWriter.configwrite("userInfo", "Division", division);
			ConfigWriter.configwrite("userInfo", "Name", nameField.getText().trim());
			ConfigWriter.configwrite("userInfo", "Rank", rank);
			ConfigWriter.configwrite("userInfo", "Number", numberField.getText().trim());
			checkAndSetDefaultValues();
			checkAndSetDefaultAppValues();
			
			Stage stag = (Stage) vbox.getScene().getWindow();
			stag.close();
			
			FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("Windows/Desktop/desktop-main.fxml"));
			Scene scene = new Scene(fxmlLoader.load());
			mainDesktopControllerObj = fxmlLoader.getController();
			Stage primaryStage = new Stage();
			primaryStage.setTitle("ReportsPlus Desktop");
			primaryStage.setScene(scene);
			primaryStage.show();
			mainDesktopStage = primaryStage;
			
			primaryStage.setFullScreen(true); // todo make callout and id windows part of the main window
			
			DataTerminalHomeApplication.mainRT = mainDesktopStage;
		}
	}
}
