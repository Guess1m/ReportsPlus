package com.Guess.ReportsPlus.Desktop;

import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.config.ConfigWriter;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.Guess.ReportsPlus.util.Strings.dropdownInfo;
import com.Guess.ReportsPlus.util.UserProfiles.Profiles;
import com.Guess.ReportsPlus.util.UserProfiles.User;
import jakarta.xml.bind.JAXBException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.Guess.ReportsPlus.Desktop.Utils.AppUtils.AppConfig.appConfig.*;
import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.MainApplication.openMainDesktop;
import static com.Guess.ReportsPlus.config.ConfigReader.checkAndSetDefaultValues;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getJarPath;

public class desktopLoginController {
	
	@javafx.fxml.FXML
	private TextField usernameField;
	@javafx.fxml.FXML
	private Button loginRegisterButton;
	@javafx.fxml.FXML
	private TextField registrationOfficerNameField;
	@javafx.fxml.FXML
	private Label loginErrorLabel;
	@javafx.fxml.FXML
	private BorderPane registrationWindow;
	@javafx.fxml.FXML
	private TextField registrationCallsignField;
	@javafx.fxml.FXML
	private Button registerButton;
	@javafx.fxml.FXML
	private Button loginButton;
	@javafx.fxml.FXML
	private TextField passwordField;
	@javafx.fxml.FXML
	private TextField registrationPasswordField;
	@javafx.fxml.FXML
	private TextField registrationOfficerNumberField;
	@javafx.fxml.FXML
	private BorderPane loginWindow;
	@javafx.fxml.FXML
	private Label registrationErrorLabel;
	@javafx.fxml.FXML
	private TextField registrationUsernameField;
	@javafx.fxml.FXML
	private BorderPane root;
	@javafx.fxml.FXML
	private ComboBox<String> registrationRankBox;
	@javafx.fxml.FXML
	private ComboBox<String> registrationDivisionBox;
	@javafx.fxml.FXML
	private ComboBox<String> registrationAgencyBox;
	@javafx.fxml.FXML
	private Label officerloginLabel;
	@javafx.fxml.FXML
	private Label passLabel;
	@javafx.fxml.FXML
	private Label usernameLabel;
	@javafx.fxml.FXML
	private Label rankLabel;
	@javafx.fxml.FXML
	private Label divisionLabel;
	@javafx.fxml.FXML
	private Label officerregistrationLabel;
	@javafx.fxml.FXML
	private Label agencyLabel;
	
	public void initialize() {
		addLocale();
		checkConfigUserInProfiles();
		Platform.runLater(() -> checkForMissingCredentials());
	}
	
	private void checkConfigUserInProfiles() {
		try {
			String userName = ConfigReader.configRead("userInfo", "Name");
			if (userName == null || userName.isEmpty()) {
				return;
			}
			
			Profiles userProfiles = User.loadUserProfiles();
			boolean userExists = false;
			
			if (userProfiles.getUserList() != null) {
				for (User user : userProfiles.getUserList()) {
					if (userName.equals(user.getName())) {
						userExists = true;
						break;
					}
				}
			}
			
			if (!userExists) {
				String agency = ConfigReader.configRead("userInfo", "Agency");
				String division = ConfigReader.configRead("userInfo", "Division");
				String rank = ConfigReader.configRead("userInfo", "Rank");
				String number = ConfigReader.configRead("userInfo", "Number");
				String callsign = ConfigReader.configRead("userInfo", "Callsign");
				
				User newUser = new User(userName, agency, division, rank, number, callsign);
				User.addUser(newUser);
			}
		} catch (IOException e) {
			log("Config file not found or error reading userInfo: " + e.getMessage(), LogUtils.Severity.INFO);
		} catch (JAXBException e) {
			log("Error loading user profiles: " + e.getMessage(), LogUtils.Severity.ERROR);
		}
	}
	
	private void addLocale() {
		officerloginLabel.setText(localization.getLocalizedMessage("Desktop.officerloginLabel", "Officer Login:"));
		passLabel.setText(localization.getLocalizedMessage("Desktop.passLabel", "Password:"));
		usernameLabel.setText(localization.getLocalizedMessage("Desktop.usernameLabel", "Username:"));
		loginRegisterButton.setText(localization.getLocalizedMessage("Desktop.registerButton", "Register"));
		loginButton.setText(localization.getLocalizedMessage("Desktop.loginButton", "Login"));
		
		rankLabel.setText(localization.getLocalizedMessage("UserManager.RankLabel", "Rank:"));
		divisionLabel.setText(localization.getLocalizedMessage("UserManager.DivisionLabel", "Division:"));
		agencyLabel.setText(localization.getLocalizedMessage("UserManager.Agencylabel", "Agency:"));
		registerButton.setText(localization.getLocalizedMessage("Desktop.registerButton", "Register"));
		officerregistrationLabel.setText(localization.getLocalizedMessage("Desktop.officerregistrationLabel", "Officer Registration:"));
	}
	
	private void checkForMissingCredentials() {
		try {
			Profiles userProfiles = User.loadUserProfiles();
			if (userProfiles.getUserList() != null) {
				List<User> missingCreds = new ArrayList<>();
				for (User userObj : userProfiles.getUserList()) {
					if (userObj.getUsername() == null || userObj.getPassword() == null) {
						missingCreds.add(userObj);
					}
				}
				if (!missingCreds.isEmpty()) {
					showMissingCredentialsPopup(missingCreds);
				}
			}
		} catch (JAXBException e) {
			log("Error loading user profiles", LogUtils.Severity.ERROR);
		}
	}
	
	@javafx.fxml.FXML
	public void loginBtnClick(ActionEvent actionEvent) {
		String user = usernameField.getText().trim();
		String pass = passwordField.getText().trim();
		if (user.isEmpty() || pass.isEmpty()) {
			showError(loginErrorLabel, "Fill out username and password");
			return;
		}
		try {
			Profiles userProfiles = User.loadUserProfiles();
			if (userProfiles.getUserList() != null) {
				for (User userObj : userProfiles.getUserList()) {
					if (userObj.getUsername() != null && userObj.getPassword() != null && userObj.getUsername().equals(user) && userObj.getPassword().equals(pass)) {
						log("Login successful", LogUtils.Severity.INFO);
						loginUser(userObj.getName(), userObj.getNumber(), userObj.getRank(), userObj.getDivision(), userObj.getAgency());
						return;
					}
				}
			}
		} catch (JAXBException e) {
			log("Error loading user profiles", LogUtils.Severity.ERROR);
		}
		showError(loginErrorLabel, "Username or password is incorrect");
	}
	
	@javafx.fxml.FXML
	public void registerButtonClick(ActionEvent actionEvent) {
		String username = registrationUsernameField.getText().trim();
		String password = registrationPasswordField.getText().trim();
		String division = registrationDivisionBox.getValue() == null ? null : registrationDivisionBox.getValue().toString();
		String agency = registrationAgencyBox.getValue() == null ? null : registrationAgencyBox.getValue().toString();
		String rank = registrationRankBox.getValue() == null ? null : registrationRankBox.getValue().toString();
		String officerNumber = registrationOfficerNumberField.getText().trim();
		String callsign = registrationCallsignField.getText().trim();
		String name = registrationOfficerNameField.getText().trim();
		if (username.isEmpty() || password.isEmpty() || division == null || agency == null || officerNumber.isEmpty() || callsign.isEmpty() || rank == null || name.isEmpty()) {
			showError(registrationErrorLabel, "Fill out all fields");
			return;
		}
		try {
			User user = new User(name, agency, division, rank, officerNumber, callsign);
			Profiles userProfiles = User.loadUserProfiles();
			
			if (userProfiles.getUserList() == null) {
				userProfiles.setUserList(new ArrayList<>());
			}
			
			for (User u : userProfiles.getUserList()) {
				if (u.getUsername() != null && u.getUsername().equalsIgnoreCase(username)) {
					showError(registrationErrorLabel, "Username already exists");
					return;
				}
			}
			
			user.setUsername(username);
			user.setPassword(password);
			User.addUser(user);
			loginUser(user.getName(), user.getNumber(), user.getRank(), user.getDivision(), user.getAgency());
		} catch (Exception e) {
			log("Error adding user: " + e, LogUtils.Severity.ERROR);
			showError(registrationErrorLabel, "Error adding user");
		}
	}
	
	private void loginUser(String name, String number, String rank, String division, String agency) {
		String configFilePath = getJarPath() + File.separator + "config.properties";
		File configFile = new File(configFilePath);
		if (configFile.exists()) {
			ConfigWriter.configwrite("uiSettings", "firstLogin", "false");
			log("exists, printing values", LogUtils.Severity.INFO);
		} else {
			try {
				configFile.createNewFile();
				log("Created Config: " + configFile.getAbsolutePath(), LogUtils.Severity.INFO);
			} catch (IOException e) {
				log("Failed to create config file", LogUtils.Severity.ERROR);
			}
		}
		if (!doesAppConfigExist()) {
			createAppConfig();
		}
		ConfigWriter.configwrite("userInfo", "Agency", agency);
		ConfigWriter.configwrite("userInfo", "Division", division);
		ConfigWriter.configwrite("userInfo", "Name", name);
		ConfigWriter.configwrite("userInfo", "Rank", rank);
		ConfigWriter.configwrite("userInfo", "Number", number);
		checkAndSetDefaultValues();
		checkAndSetDefaultAppValues();
		Stage stag = (Stage) root.getScene().getWindow();
		stag.close();
		try {
			openMainDesktop();
		} catch (IOException e) {
			log("Error opening main desktop: " + e, LogUtils.Severity.ERROR);
			showError(loginErrorLabel, "Error opening main desktop");
		}
	}
	
	@javafx.fxml.FXML
	public void loginRegisterButtonClick(ActionEvent actionEvent) {
		loginWindow.setVisible(false);
		loginWindow.getChildren().clear();
		loginWindow = null;
		
		registrationRankBox.getItems().addAll(dropdownInfo.ranks);
		registrationDivisionBox.getItems().addAll(dropdownInfo.divisions);
		registrationAgencyBox.getItems().addAll(dropdownInfo.agencies);
		
		registrationWindow.setVisible(true);
	}
	
	private void showError(Label node, String message) {
		node.setText(message);
		node.setStyle("-fx-text-fill: red; -fx-font-family: 'Inter 24pt Regular'; -fx-font-size: 14px;");
		node.setVisible(true);
		Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1.5), evt -> node.setVisible(false)));
		timeline1.play();
	}
	
	private void showMissingCredentialsPopup(List<User> users) {
		Stage popupStage = new Stage();
		VBox rootVBox = new VBox(20);
		rootVBox.setPadding(new Insets(20));
		rootVBox.setStyle("-fx-background-color: #f4f4f4;");
		
		Label header = new Label("Update Missing Credentials");
		header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
		header.setMaxWidth(Double.MAX_VALUE);
		header.setAlignment(javafx.geometry.Pos.CENTER);
		rootVBox.getChildren().add(header);
		
		Separator headerSeparator = new Separator();
		headerSeparator.setStyle("-fx-background-color: #e0e0e0;");
		rootVBox.getChildren().add(headerSeparator);
		
		List<MissingCredential> missingList = new ArrayList<>();
		for (User user : users) {
			VBox userContainer = new VBox(15);
			userContainer.setPadding(new Insets(15));
			userContainer.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 8; -fx-border-color: #e0e0e0; -fx-border-radius: 8;");
			
			Label officerLabel = new Label("Officer: " + user.getName());
			officerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #34495e;");
			userContainer.getChildren().add(officerLabel);
			
			HBox credentialsBox = new HBox(30);
			MissingCredential mc = new MissingCredential(user, null, null);
			
			VBox usernameBox = new VBox(8);
			Label usernameLabel = new Label(localization.getLocalizedMessage("Desktop.usernameLabel", "Username:"));
			usernameLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 14px;");
			if (user.getUsername() == null) {
				TextField usernameFieldPopup = new TextField();
				usernameFieldPopup.setPromptText("Enter username");
				usernameFieldPopup.setStyle("-fx-background-radius: 4; -fx-border-radius: 4;");
				usernameBox.getChildren().addAll(usernameLabel, usernameFieldPopup);
				mc.usernameField = usernameFieldPopup;
			} else {
				Label usernameValue = new Label(user.getUsername());
				usernameValue.setStyle("-fx-font-size: 14px; -fx-text-fill: #2c3e50;");
				usernameBox.getChildren().addAll(usernameLabel, usernameValue);
			}
			
			VBox passwordBox = new VBox(8);
			Label passwordLabel = new Label(localization.getLocalizedMessage("Desktop.passLabel", "Password:"));
			passwordLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 14px;");
			if (user.getPassword() == null) {
				TextField passwordFieldPopup = new TextField();
				passwordFieldPopup.setPromptText("Enter password");
				passwordFieldPopup.setStyle("-fx-background-radius: 4; -fx-border-radius: 4;");
				passwordBox.getChildren().addAll(passwordLabel, passwordFieldPopup);
				mc.passwordField = passwordFieldPopup;
			} else {
				Label passwordValue = new Label(user.getPassword());
				passwordValue.setStyle("-fx-font-size: 14px; -fx-text-fill: #2c3e50;");
				passwordBox.getChildren().addAll(passwordLabel, passwordValue);
			}
			
			credentialsBox.getChildren().addAll(usernameBox, passwordBox);
			userContainer.getChildren().add(credentialsBox);
			rootVBox.getChildren().add(userContainer);
			missingList.add(mc);
		}
		
		Label errorLabel = new Label();
		errorLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 14px; -fx-font-family: 'Inter 28pt Medium';");
		
		Button saveButton = new Button("Save All");
		saveButton.setStyle("-fx-font-size: 13px; -fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 5 10 ;");
		saveButton.setOnAction(e -> {
			for (MissingCredential entry : missingList) {
				if (entry.usernameField != null && entry.usernameField.getText().trim().isEmpty()) {
					errorLabel.setText("Fill out all username fields");
					return;
				}
				if (entry.passwordField != null && entry.passwordField.getText().trim().isEmpty()) {
					errorLabel.setText("Fill out all password fields");
					return;
				}
			}
			
			Set<String> usernames = new HashSet<>();
			for (MissingCredential entry : missingList) {
				if (entry.usernameField != null) {
					String username = entry.usernameField.getText().trim();
					if (usernames.contains(username)) {
						errorLabel.setText("Duplicate username: " + username);
						return;
					}
					usernames.add(username);
				}
			}
			
			for (MissingCredential entry : missingList) {
				if (entry.usernameField != null) {
					entry.user.setUsername(entry.usernameField.getText().trim());
				}
				if (entry.passwordField != null) {
					entry.user.setPassword(entry.passwordField.getText().trim());
				}
				try {
					User.addUser(entry.user);
				} catch (JAXBException ex) {
					log("Error adding user: " + ex, LogUtils.Severity.ERROR);
					errorLabel.setText("Error adding user, check log!");
				}
			}
			popupStage.close();
		});
		
		var bottomHBox = new HBox(saveButton, errorLabel);
		bottomHBox.setSpacing(20);
		bottomHBox.setAlignment(Pos.CENTER_LEFT);
		
		rootVBox.getChildren().add(bottomHBox);
		
		Scene scene = new Scene(rootVBox);
		popupStage.setScene(scene);
		popupStage.setTitle("Update Missing Credentials");
		popupStage.showAndWait();
	}
	
	private class MissingCredential {
		User user;
		TextField usernameField;
		TextField passwordField;
		
		MissingCredential(User user, TextField usernameField, TextField passwordField) {
			this.user = user;
			this.usernameField = usernameField;
			this.passwordField = passwordField;
		}
	}
	
}
