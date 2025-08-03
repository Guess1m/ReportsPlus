package com.Guess.ReportsPlus.Windows.Misc;

import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.MainApplication.mainDesktopControllerObj;
import static com.Guess.ReportsPlus.Windows.Settings.settingsController.UIDarkColor;
import static com.Guess.ReportsPlus.Windows.Settings.settingsController.UILightColor;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logDebug;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logInfo;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logWarn;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationInfo;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.config.ConfigWriter;
import com.Guess.ReportsPlus.util.Strings.dropdownInfo;
import com.Guess.ReportsPlus.util.UserProfiles.Profiles;
import com.Guess.ReportsPlus.util.UserProfiles.User;

import jakarta.xml.bind.JAXBException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class NewUserManagerController {
	private static final ObservableList<User> userData = FXCollections.observableArrayList();
	public static NewUserManagerController newUserManagerController;
	CustomWindow addUserWindow = null;
	@FXML
	private BorderPane root;
	@FXML
	private Label header;
	@FXML
	private Button newUserBtn;
	@FXML
	private SplitPane splitPane;
	@FXML
	private ListView<User> userListView;
	@FXML
	private VBox userDetailPane;
	@FXML
	private Label placeholderLabel;
	private Label statusLabel;

	public void initialize() {
		newUserManagerController = this;
		setupListView();
		Platform.runLater(NewUserManagerController::refreshUsersList);
		loadLocale();
		Platform.runLater(() -> {
			String textColor;
			try {
				textColor = ConfigReader.configRead("uiColors", "UIDarkMode").equalsIgnoreCase("true")
						? "-fx-text-fill: " + UIDarkColor + ";"
						: "-fx-text-fill: " + UILightColor + ";";
			} catch (IOException e) {
				logError("UserManager; Failed to read UI colors from config", e);
				textColor = "-fx-text-fill: black;";
			}
			placeholderLabel.setStyle(textColor);
		});
	}

	private void setupListView() {
		userListView.setMinWidth(240);
		userListView.setItems(userData);
		userListView.setCellFactory(lv -> new ListCell<>() {
			private final VBox graphicBox = new VBox(2);
			private final Label nameLabel = new Label();
			private final Label agencyLabel = new Label();
			{
				nameLabel.setFont(Font.font("Inter 28pt Bold", 15));
				agencyLabel.setFont(Font.font("Inter 28pt Medium", 12));
				agencyLabel.setOpacity(0.8);
				graphicBox.getChildren().addAll(nameLabel, agencyLabel);
				graphicBox.setPadding(new Insets(8, 12, 8, 12));
			}

			@Override
			protected void updateItem(User user, boolean empty) {
				super.updateItem(user, empty);
				if (empty || user == null) {
					setText(null);
					setGraphic(null);
					return;
				}
				try {
					String activeUserName = ConfigReader.configRead("userInfo", "Name");
					if (user.getName().equals(activeUserName)) {
						nameLabel.setText(user.getName() + " (Active)");
					} else {
						nameLabel.setText(user.getName());
					}
					agencyLabel.setText(user.getAgency());
					nameLabel.setStyle("-fx-text-fill: " + ConfigReader.configRead("uiColors", "mainColor") + ";");
					agencyLabel
							.setStyle("-fx-text-fill: " + ConfigReader.configRead("uiColors", "secondaryColor") + ";");
					String bkgColor = ConfigReader.configRead("uiColors", "UIDarkMode").equalsIgnoreCase("true")
							? "rgba(0,0,0,0.05)"
							: "rgba(255,255,255,0.15)";
					String accentColor = ConfigReader.configRead("uiColors", "accentColor");
					selectedProperty().addListener((obs, wasSelected, isSelected) -> graphicBox
							.setStyle("-fx-background-radius: 5; -fx-background-color: "
									+ (isSelected ? hexToRgba(accentColor, 0.3) : bkgColor) + ";"));
					hoverProperty().addListener((obs, wasHovered, isHovered) -> {
						if (!isSelected()) {
							graphicBox.setStyle("-fx-background-radius: 5; -fx-background-color: "
									+ (isHovered ? hexToRgba(accentColor, 0.15) : bkgColor) + ";");
						}
					});
					graphicBox.setStyle("-fx-background-radius: 5; -fx-background-color: " + bkgColor + ";");
				} catch (IOException e) {
					logError("UserManager; Failed to apply theme to user list cell", e);
				}
				setGraphic(graphicBox);
			}
		});
		userListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				showUserDetails(newSelection);
			} else {
				showPlaceholder();
			}
		});
	}

	public static void refreshUsersList() {
		User selectedUser = null;
		if (newUserManagerController != null && newUserManagerController.userListView != null) {
			selectedUser = newUserManagerController.userListView.getSelectionModel().getSelectedItem();
		}
		userData.clear();
		try {
			List<User> userList = User.loadUserProfiles().getUserList();
			if (userList == null || userList.isEmpty()) {
				try {
					User u = new User(ConfigReader.configRead("userInfo", "Name"),
							ConfigReader.configRead("userInfo", "Agency"),
							ConfigReader.configRead("userInfo", "Division"),
							ConfigReader.configRead("userInfo", "Rank"),
							ConfigReader.configRead("userInfo", "Number"),
							ConfigReader.configRead("userInfo", "Callsign"));
					User.addUser(u);
					userList = User.loadUserProfiles().getUserList();
				} catch (IOException | JAXBException e) {
					logError("UserManager; Error recreating default user info: ", e);
				}
			}
			if (userList != null) {
				String activeUserName = ConfigReader.configRead("userInfo", "Name");
				userList.sort((u1, u2) -> {
					boolean u1IsActive = u1.getName().equals(activeUserName);
					boolean u2IsActive = u2.getName().equals(activeUserName);
					if (u1IsActive && !u2IsActive) {
						return -1;
					} else if (!u1IsActive && u2IsActive) {
						return 1;
					} else {
						return u1.getName().compareToIgnoreCase(u2.getName());
					}
				});
				userData.addAll(userList);
			}
		} catch (JAXBException | IOException e) {
			logError("UserManager; Failed to load user profiles: ", e);
		}
		if (selectedUser != null) {
			for (User u : userData) {
				if (u.getName().equals(selectedUser.getName())) {
					newUserManagerController.userListView.getSelectionModel().select(u);
					return;
				}
			}
		}
		if (newUserManagerController != null) {
			newUserManagerController.showPlaceholder();
		}
	}

	private void showUserDetails(User user) {
		if (user == null) {
			showPlaceholder();
			return;
		}
		placeholderLabel.setVisible(false);
		userDetailPane.getChildren().clear();
		String btnStyle = "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12; -fx-font-family: 'Inter 28pt Medium';";
		String hoverStyle, nonTransparentBtn, textColor, activeUserName = "";
		try {
			hoverStyle = "-fx-background-color: " + ConfigReader.configRead("uiColors", "accentColor");
			nonTransparentBtn = "-fx-background-color: " + ConfigReader.configRead("uiColors", "mainColor") + ";";
			textColor = ConfigReader.configRead("uiColors", "UIDarkMode").equalsIgnoreCase("true")
					? "-fx-text-fill: " + UIDarkColor + ";"
					: "-fx-text-fill: " + UILightColor + ";";
			activeUserName = ConfigReader.configRead("userInfo", "Name");
		} catch (IOException e) {
			logError("UserManager; Failed to read UI colors from config", e);
			return;
		}
		String title = localization.getLocalizedMessage("UserManager.editProfileLabel", "Edit Profile:") + " "
				+ user.getName();
		if (user.getName().equals(activeUserName)) {
			title += " (Active)";
		}
		Label editProfileLabel = new Label(title);
		editProfileLabel.setFont(Font.font("Inter 28pt Bold", 18));
		try {
			editProfileLabel.setStyle("-fx-text-fill: " + ConfigReader.configRead("uiColors", "accentColor") + ";");
		} catch (IOException e) {
			logError("UserManager; Error setting profile label style", e);
		}
		GridPane formGrid = new GridPane();
		formGrid.setHgap(15);
		formGrid.setVgap(15);
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(50);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(50);
		formGrid.getColumnConstraints().addAll(col1, col2);
		Label numberLabel = new Label(localization.getLocalizedMessage("Callout_Manager.CalloutNumber", "Number:"));
		numberLabel.setOpacity(0.7);
		numberLabel.setFont(Font.font("Inter 24pt Regular", 13));
		numberLabel.setStyle(textColor);
		TextField numberField = new TextField(user.getNumber());
		VBox numberVbox = new VBox(5, numberLabel, numberField);
		Label callsignLabel = new Label(localization.getLocalizedMessage("UserManager.CallsignLabel", "Callsign:"));
		callsignLabel.setOpacity(0.7);
		callsignLabel.setFont(Font.font("Inter 24pt Regular", 13));
		callsignLabel.setStyle(textColor);
		TextField callsignField = new TextField(user.getCallsign());
		VBox callsignVbox = new VBox(5, callsignLabel, callsignField);
		formGrid.add(numberVbox, 0, 0);
		formGrid.add(callsignVbox, 1, 0);
		Label rankLabel = new Label(localization.getLocalizedMessage("UserManager.RankLabel", "Rank:"));
		rankLabel.setOpacity(0.7);
		rankLabel.setFont(Font.font("Inter 24pt Regular", 13));
		rankLabel.setStyle(textColor);
		ComboBox<String> rankDropdown = new ComboBox<>();
		rankDropdown.getItems().addAll(dropdownInfo.ranks);
		rankDropdown.setValue(user.getRank());
		rankDropdown.setMaxWidth(Double.MAX_VALUE);
		VBox rankVbox = new VBox(5, rankLabel, rankDropdown);
		formGrid.add(rankVbox, 0, 1);
		Label divisionLabel = new Label(localization.getLocalizedMessage("UserManager.DivisionLabel", "Division:"));
		divisionLabel.setOpacity(0.7);
		divisionLabel.setFont(Font.font("Inter 24pt Regular", 13));
		divisionLabel.setStyle(textColor);
		ComboBox<String> divisionDropdown = new ComboBox<>();
		divisionDropdown.getItems().addAll(dropdownInfo.divisions);
		divisionDropdown.setValue(user.getDivision());
		divisionDropdown.setMaxWidth(Double.MAX_VALUE);
		VBox divisionVbox = new VBox(5, divisionLabel, divisionDropdown);
		formGrid.add(divisionVbox, 1, 1);
		Label agencyLabel = new Label(localization.getLocalizedMessage("UserManager.Agencylabel", "Agency:"));
		agencyLabel.setOpacity(0.7);
		agencyLabel.setFont(Font.font("Inter 24pt Regular", 13));
		agencyLabel.setStyle(textColor);
		ComboBox<String> agencyDropdown = new ComboBox<>();
		agencyDropdown.getItems().addAll(dropdownInfo.agencies);
		agencyDropdown.setValue(user.getAgency());
		agencyDropdown.setMaxWidth(Double.MAX_VALUE);
		VBox agencyVbox = new VBox(5, agencyLabel, agencyDropdown);
		GridPane.setColumnSpan(agencyVbox, 2);
		formGrid.add(agencyVbox, 0, 2);
		VBox credentialsContainer = new VBox(10);
		credentialsContainer.setPadding(new Insets(10, 0, 10, 0));
		Button showCredsButton = new Button("Manage Credentials");
		showCredsButton.setStyle(nonTransparentBtn + btnStyle);
		showCredsButton.setOnMouseEntered(e -> showCredsButton.setStyle(hoverStyle + ";" + btnStyle));
		showCredsButton.setOnMouseExited(e -> showCredsButton.setStyle(nonTransparentBtn + btnStyle));
		credentialsContainer.getChildren().add(showCredsButton);
		statusLabel = new Label();
		statusLabel.setFont(Font.font("Inter 28pt Medium", 13));
		statusLabel.setVisible(false);
		Button deleteButton = new Button(
				localization.getLocalizedMessage("Callout_Manager.DelCalloutButton", "Delete"));
		deleteButton.setStyle(nonTransparentBtn + btnStyle);
		deleteButton.setOnMouseEntered(e -> deleteButton.setStyle(hoverStyle + ";" + btnStyle));
		deleteButton.setOnMouseExited(e -> deleteButton.setStyle(nonTransparentBtn + btnStyle));
		Button saveButton = new Button(localization.getLocalizedMessage("Settings.NotiSaveButton", "Save"));
		saveButton.setStyle(nonTransparentBtn + btnStyle);
		saveButton.setOnMouseEntered(e -> saveButton.setStyle(hoverStyle + ";" + btnStyle));
		saveButton.setOnMouseExited(e -> saveButton.setStyle(nonTransparentBtn + btnStyle));
		HBox actionButtonsHBox = new HBox(15, deleteButton, saveButton);
		actionButtonsHBox.setAlignment(Pos.CENTER_RIGHT);
		BorderPane bottomPane = new BorderPane();
		bottomPane.setLeft(statusLabel);
		bottomPane.setRight(actionButtonsHBox);
		BorderPane.setAlignment(statusLabel, Pos.CENTER_LEFT);
		final TextField[] usernameField = new TextField[1];
		final PasswordField[] passwordField = new PasswordField[1];
		String finalTextColor = textColor;
		showCredsButton.setOnAction(e -> askForActiveUserPassword(user, credentialsContainer, usernameField,
				passwordField, finalTextColor));
		deleteButton.setOnAction(e -> {
			try {
				if (user.getName().equalsIgnoreCase(ConfigReader.configRead("userInfo", "Name"))) {
					showStatusUpdate(3, "Cannot delete the active profile!", "#ff2d2d");
					return;
				}
				User.deleteUser(user.getName());
				refreshUsersList();
				showPlaceholder();
			} catch (JAXBException | IOException exception) {
				logError("UserManager; Error while deleting user '" + user.getName() + "': ", exception);
			}
		});
		saveButton.setOnAction(e -> {
			logInfo("UserManager; Save button clicked for user: " + user.getName());
			if (agencyDropdown.getValue() == null || divisionDropdown.getValue() == null
					|| rankDropdown.getValue() == null) {
				logWarn("UserManager; Save failed due to missing required fields (Agency, Division, or Rank).");
				showStatusUpdate(3, "Please fill out all fields!", "#ff2d2d");
				return;
			}
			String name = user.getName();
			String callsign = callsignField.getText().trim();
			String number = numberField.getText().trim();
			String agency = agencyDropdown.getValue();
			String division = divisionDropdown.getValue();
			String rank = rankDropdown.getValue();
			User updatedUser = new User(name, agency, division, rank, number, callsign);
			logDebug("UserManager; Updated basic info for user '" + name + "': " + updatedUser);
			if (usernameField[0] != null && passwordField[0] != null) {
				logInfo("UserManager; Credentials pane is open, checking for credential updates.");
				String newUsername = usernameField[0].getText().trim().toLowerCase();
				String newPassword = passwordField[0].getText().trim();
				if (newUsername.isEmpty()) {
					logWarn("UserManager; Save failed. Username cannot be blank.");
					showStatusUpdate(4, "Username cannot be blank.", "#ff2d2d");
					return;
				}
				if (!newUsername.equalsIgnoreCase(user.getUsername() != null ? user.getUsername() : "")) {
					logInfo("UserManager; Username change detected for '" + name + "'. New username: '" + newUsername
							+ "'. Checking for duplicates.");
					try {
						Profiles profiles = User.loadUserProfiles();
						if (profiles != null && profiles.getUserList() != null) {
							for (User existingUser : profiles.getUserList()) {
								if (existingUser.getUsername() != null
										&& existingUser.getUsername().equalsIgnoreCase(newUsername)) {
									logWarn("UserManager; Save failed. Duplicate username '" + newUsername
											+ "' found.");
									showStatusUpdate(4, "Username '" + newUsername + "' already exists.", "#ff2d2d");
									return;
								}
							}
						}
						logInfo("UserManager; New username '" + newUsername + "' is unique.");
					} catch (JAXBException ex) {
						logError("UserManager; Error checking for existing usernames on save: ", ex);
					}
				}
				updatedUser.setUsername(newUsername);
				if (!newPassword.isEmpty()) {
					logInfo("UserManager; New password provided for user '" + name + "'. Password will be updated.");
					updatedUser.setPassword(newPassword);
				} else {
					logInfo("UserManager; New password field empty. Password for '" + name + "' will not be changed.");
					updatedUser.setPassword(user.getPassword());
				}
			} else {
				logInfo("UserManager; Credentials pane not open. Retaining existing username and password for " + name);
				updatedUser.setUsername(user.getUsername());
				updatedUser.setPassword(user.getPassword());
			}
			try {
				logInfo("UserManager; Attempting to save user profile for: " + updatedUser.getName());
				User.addUser(updatedUser);
				logInfo("UserManager; Successfully saved user profile for: " + updatedUser.getName());
				showStatusUpdate(3, "Profile saved successfully.", "green");
				refreshUsersList();
			} catch (JAXBException exception) {
				logError("UserManager; Error while saving user '" + name + "': ", exception);
				showStatusUpdate(3, "Error saving profile.", "#ff2d2d");
			}
			try {
				if (name.equalsIgnoreCase(ConfigReader.configRead("userInfo", "Name"))) {
					logInfo("UserManager; Saved profile was the active user. Updating active user config.");
					ConfigWriter.configwrite("userInfo", "Callsign", callsign);
					ConfigWriter.configwrite("userInfo", "Agency", agency);
					ConfigWriter.configwrite("userInfo", "Division", division);
					ConfigWriter.configwrite("userInfo", "Rank", rank);
					ConfigWriter.configwrite("userInfo", "Number", number);
					mainDesktopControllerObj.getOfficerInfoName().setText(ConfigReader.configRead("userInfo", "Name")
							+ ", " + ConfigReader.configRead("userInfo", "Agency"));
					logInfo("User Manager; User [" + name + "] saved and assigned");
				}
				showNotificationInfo("User Manager", "User [" + name + "] saved.");
			} catch (Exception exception) {
				logError("UserManager; Unable to read/write user info from config, ", exception);
			}
		});
		userDetailPane.getChildren().addAll(editProfileLabel, formGrid, new javafx.scene.control.Separator(),
				credentialsContainer, bottomPane);
		userDetailPane.setSpacing(15);
		VBox.setVgrow(formGrid, Priority.ALWAYS);
	}

	@FXML
	public void createUserBtn(ActionEvent actionEvent) {
		userListView.getSelectionModel().clearSelection();
		showCreateNewUserForm();
	}

	private void askForActiveUserPassword(User selectedUser, VBox container, final TextField[] usernameFieldHolder,
			final PasswordField[] passwordFieldHolder, String textColor) {
		container.getChildren().clear();
		String activeUserName;
		try {
			activeUserName = ConfigReader.configRead("userInfo", "Name");
		} catch (IOException e) {
			logError("Could not read active user name.", e);
			container.getChildren().add(new Label("Error: Could not identify active user."));
			return;
		}
		User activeUser = User.getUser(activeUserName);
		if (activeUser == null || activeUser.getPassword() == null) {
			container.getChildren().clear();
			Label noticeLabel = new Label("The active user '" + activeUserName
					+ "' does not have a password set.\nCredentials cannot be managed.");
			noticeLabel.setWrapText(true);
			container.getChildren().add(noticeLabel);
			return;
		}
		Label promptLabel = new Label("Enter password for '" + activeUser.getName() + "' to manage credentials:");
		promptLabel.setStyle(textColor);
		PasswordField authPassField = new PasswordField();
		Button verifyButton = new Button("Verify");
		String btnStyle = "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12; -fx-font-family: 'Inter 28pt Medium';";
		try {
			verifyButton.setStyle("-fx-background-color: " + ConfigReader.configRead("uiColors", "mainColor") + ";"
					+ btnStyle);
		} catch (IOException e) {
			logError("UserManager; Failed to read UI colors from config for verify button", e);
		}
		Label errorLabel = new Label();
		errorLabel.setStyle("-fx-text-fill: red;");
		errorLabel.setVisible(false);
		verifyButton.setOnAction(e -> {
			if (authPassField.getText().equals(activeUser.getPassword())) {
				showEditableCredentials(selectedUser, container, usernameFieldHolder, passwordFieldHolder, textColor);
			} else {
				errorLabel.setText("Incorrect password.");
				errorLabel.setVisible(true);
			}
		});
		container.getChildren().addAll(promptLabel, authPassField, new HBox(10, verifyButton, errorLabel));
	}

	private void showEditableCredentials(User selectedUser, VBox container, final TextField[] usernameFieldHolder,
			final PasswordField[] passwordFieldHolder, String textColor) {
		container.getChildren().clear();
		GridPane credGrid = new GridPane();
		credGrid.setHgap(15);
		credGrid.setVgap(10);
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(50);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(50);
		credGrid.getColumnConstraints().addAll(col1, col2);
		Label usernameLabel = new Label("Username:");
		usernameLabel.setStyle(textColor);
		TextField usernameField = new TextField(selectedUser.getUsername());
		usernameFieldHolder[0] = usernameField;
		VBox userVbox = new VBox(5, usernameLabel, usernameField);
		Label passwordLabel = new Label("New Password (leave blank to keep current):");
		passwordLabel.setStyle(textColor);
		PasswordField passwordField = new PasswordField();
		passwordFieldHolder[0] = passwordField;
		VBox passVbox = new VBox(5, passwordLabel, passwordField);
		credGrid.add(userVbox, 0, 0);
		credGrid.add(passVbox, 1, 0);
		Label infoLabel = new Label("Changes will be applied when you click the main 'Save' button.");
		infoLabel.setWrapText(true);
		infoLabel.setStyle(textColor + "-fx-opacity: 0.7;");
		container.getChildren().addAll(credGrid, infoLabel);
	}

	private void showCreateNewUserForm() {
		placeholderLabel.setVisible(false);
		userDetailPane.getChildren().clear();
		String hoverStyle, nonTransparentBtn, textColor;
		try {
			hoverStyle = "-fx-background-color: " + ConfigReader.configRead("uiColors", "accentColor");
			nonTransparentBtn = "-fx-background-color: " + ConfigReader.configRead("uiColors", "mainColor") + ";";
			textColor = ConfigReader.configRead("uiColors", "UIDarkMode").equalsIgnoreCase("true")
					? "-fx-text-fill: " + UIDarkColor + ";"
					: "-fx-text-fill: " + UILightColor + ";";
		} catch (IOException e) {
			logError("UserManager; Failed to read UI colors from config for create form", e);
			return;
		}
		Label createProfileLabel = new Label(
				localization.getLocalizedMessage("UserManager.newProfileButton", "Create New Profile"));
		try {
			createProfileLabel.setStyle("-fx-text-fill: " + ConfigReader.configRead("uiColors", "accentColor")
					+ "; -fx-font-family: 'Inter 28pt Bold';");
		} catch (IOException e) {
			logError("UserManager; Error setting create profile label style", e);
		}
		GridPane formGrid = new GridPane();
		formGrid.setHgap(15);
		formGrid.setVgap(15);
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(50);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(50);
		formGrid.getColumnConstraints().addAll(col1, col2);
		Label firstNameLabel = new Label(localization.getLocalizedMessage("Desktop.firstNameLabel", "First Name:"));
		firstNameLabel.setOpacity(0.7);
		firstNameLabel.setFont(Font.font("Inter 24pt Regular", 13));
		firstNameLabel.setStyle(textColor);
		TextField firstNameField = new TextField();
		VBox firstNameVBox = new VBox(5, firstNameLabel, firstNameField);
		Label lastNameLabel = new Label(localization.getLocalizedMessage("Desktop.lastNameLabel", "Last Name:"));
		lastNameLabel.setOpacity(0.7);
		lastNameLabel.setFont(Font.font("Inter 24pt Regular", 13));
		lastNameLabel.setStyle(textColor);
		TextField lastNameField = new TextField();
		VBox lastNameVBox = new VBox(5, lastNameLabel, lastNameField);
		formGrid.add(firstNameVBox, 0, 0);
		formGrid.add(lastNameVBox, 1, 0);
		Label usernameLabel = new Label(localization.getLocalizedMessage("Desktop.usernameLabel", "Username:"));
		usernameLabel.setOpacity(0.7);
		usernameLabel.setFont(Font.font("Inter 24pt Regular", 13));
		usernameLabel.setStyle(textColor);
		TextField usernameField = new TextField();
		VBox usernameVBox = new VBox(5, usernameLabel, usernameField);
		Label passwordLabel = new Label(localization.getLocalizedMessage("Desktop.passLabel", "Password:"));
		passwordLabel.setOpacity(0.7);
		passwordLabel.setFont(Font.font("Inter 24pt Regular", 13));
		passwordLabel.setStyle(textColor);
		PasswordField passwordField = new PasswordField();
		VBox passwordVBox = new VBox(5, passwordLabel, passwordField);
		formGrid.add(usernameVBox, 0, 1);
		formGrid.add(passwordVBox, 1, 1);
		Label numberLabel = new Label(localization.getLocalizedMessage("Callout_Manager.CalloutNumber", "Number:"));
		numberLabel.setOpacity(0.7);
		numberLabel.setFont(Font.font("Inter 24pt Regular", 13));
		numberLabel.setStyle(textColor);
		TextField numberField = new TextField();
		VBox numberVbox = new VBox(5, numberLabel, numberField);
		Label callsignLabel = new Label(localization.getLocalizedMessage("UserManager.CallsignLabel", "Callsign:"));
		callsignLabel.setOpacity(0.7);
		callsignLabel.setFont(Font.font("Inter 24pt Regular", 13));
		callsignLabel.setStyle(textColor);
		TextField callsignField = new TextField();
		VBox callsignVbox = new VBox(5, callsignLabel, callsignField);
		formGrid.add(numberVbox, 0, 2);
		formGrid.add(callsignVbox, 1, 2);
		Label rankLabel = new Label(localization.getLocalizedMessage("UserManager.RankLabel", "Rank:"));
		rankLabel.setOpacity(0.7);
		rankLabel.setFont(Font.font("Inter 24pt Regular", 13));
		rankLabel.setStyle(textColor);
		ComboBox<String> rankDropdown = new ComboBox<>();
		rankDropdown.getItems().addAll(dropdownInfo.ranks);
		rankDropdown.setMaxWidth(Double.MAX_VALUE);
		VBox rankVbox = new VBox(5, rankLabel, rankDropdown);
		formGrid.add(rankVbox, 0, 3);
		Label divisionLabel = new Label(localization.getLocalizedMessage("UserManager.DivisionLabel", "Division:"));
		divisionLabel.setOpacity(0.7);
		divisionLabel.setFont(Font.font("Inter 24pt Regular", 13));
		divisionLabel.setStyle(textColor);
		ComboBox<String> divisionDropdown = new ComboBox<>();
		divisionDropdown.getItems().addAll(dropdownInfo.divisions);
		divisionDropdown.setMaxWidth(Double.MAX_VALUE);
		VBox divisionVbox = new VBox(5, divisionLabel, divisionDropdown);
		formGrid.add(divisionVbox, 1, 3);
		Label agencyLabel = new Label(localization.getLocalizedMessage("UserManager.Agencylabel", "Agency:"));
		agencyLabel.setOpacity(0.7);
		agencyLabel.setFont(Font.font("Inter 24pt Regular", 13));
		agencyLabel.setStyle(textColor);
		ComboBox<String> agencyDropdown = new ComboBox<>();
		agencyDropdown.getItems().addAll(dropdownInfo.agencies);
		agencyDropdown.setMaxWidth(Double.MAX_VALUE);
		VBox agencyVbox = new VBox(5, agencyLabel, agencyDropdown);
		GridPane.setColumnSpan(agencyVbox, 2);
		formGrid.add(agencyVbox, 0, 4);
		statusLabel = new Label();
		statusLabel.setFont(Font.font("Inter 28pt Medium", 13));
		statusLabel.setVisible(false);
		Button cancelButton = new Button(localization.getLocalizedMessage("Global.Cancel", "Cancel"));
		Button createButton = new Button(
				localization.getLocalizedMessage("UserManager.createButton", "Create Profile"));
		String btnStyle = "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12";
		cancelButton.setStyle(nonTransparentBtn + btnStyle);
		cancelButton.setOnMouseEntered(e -> cancelButton.setStyle(hoverStyle + ";" + btnStyle));
		cancelButton.setOnMouseExited(e -> cancelButton.setStyle(nonTransparentBtn + btnStyle));
		createButton.setStyle(nonTransparentBtn + btnStyle);
		createButton.setOnMouseEntered(e -> createButton.setStyle(hoverStyle + ";" + btnStyle));
		createButton.setOnMouseExited(e -> createButton.setStyle(nonTransparentBtn + btnStyle));
		HBox actionButtonsHBox = new HBox(15, cancelButton, createButton);
		actionButtonsHBox.setAlignment(Pos.CENTER_RIGHT);
		BorderPane bottomPane = new BorderPane();
		bottomPane.setLeft(statusLabel);
		bottomPane.setRight(actionButtonsHBox);
		BorderPane.setAlignment(statusLabel, Pos.CENTER_LEFT);
		cancelButton.setOnAction(e -> showPlaceholder());
		createButton.setOnAction(e -> {
			if (firstNameField.getText().trim().isEmpty() || lastNameField.getText().trim().isEmpty()
					|| usernameField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty()
					|| numberField.getText().trim().isEmpty() || callsignField.getText().trim().isEmpty()
					|| rankDropdown.getValue() == null || divisionDropdown.getValue() == null
					|| agencyDropdown.getValue() == null) {
				showStatusUpdate(3, "All fields are required.", "#ff2d2d");
				return;
			}
			String username = usernameField.getText().trim().toLowerCase();
			String fullName = firstNameField.getText().trim() + " " + lastNameField.getText().trim();
			try {
				Profiles profiles = User.loadUserProfiles();
				if (profiles != null && profiles.getUserList() != null) {
					for (User existingUser : profiles.getUserList()) {
						if (existingUser.getUsername() != null
								&& existingUser.getUsername().equalsIgnoreCase(username)) {
							showStatusUpdate(3, "Username already exists.", "#ff2d2d");
							return;
						}
					}
				}
			} catch (JAXBException jaxbException) {
				logError("UserManager; Error checking for existing usernames: ", jaxbException);
			}
			User newUser = new User(fullName, agencyDropdown.getValue(), divisionDropdown.getValue(),
					rankDropdown.getValue(), numberField.getText().trim(), callsignField.getText().trim());
			newUser.setUsername(username);
			newUser.setPassword(passwordField.getText().trim());
			try {
				User.addUser(newUser);
				showStatusUpdate(3, "Profile created successfully.", "green");
				refreshUsersList();
				for (User u : userListView.getItems()) {
					if (u.getName().equals(fullName)) {
						userListView.getSelectionModel().select(u);
						break;
					}
				}
			} catch (JAXBException jaxbException) {
				logError("UserManager; Error creating new user '" + fullName + "': ", jaxbException);
				showStatusUpdate(4, "Error creating profile.", "#ff2d2d");
			}
		});
		userDetailPane.getChildren().addAll(createProfileLabel, formGrid, bottomPane);
		userDetailPane.setSpacing(20);
		VBox.setVgrow(formGrid, Priority.ALWAYS);
	}

	private void loadLocale() {
		header.setText(localization.getLocalizedMessage("UserManager.userProfileLabel", "Active User Profiles:"));
		newUserBtn.setText(localization.getLocalizedMessage("UserManager.newProfileButton", "Create New Profile"));
		placeholderLabel.setText(
				localization.getLocalizedMessage("UserManager.selectUserPrompt", "Select a user to view details"));
	}

	public BorderPane getRoot() {
		return root;
	}

	public Button getNewUserBtn() {
		return newUserBtn;
	}

	public Label getHeader() {
		return header;
	}

	private String hexToRgba(String hex, double opacity) {
		hex = hex.startsWith("#") ? hex.substring(1) : hex;
		int r = Integer.parseInt(hex.substring(0, 2), 16);
		int g = Integer.parseInt(hex.substring(2, 4), 16);
		int b = Integer.parseInt(hex.substring(4, 6), 16);
		return String.format("rgba(%d, %d, %d, %.2f)", r, g, b, opacity);
	}

	private void showPlaceholder() {
		userDetailPane.getChildren().clear();
		userDetailPane.getChildren().add(placeholderLabel);
		placeholderLabel.setVisible(true);
	}

	private void showStatusUpdate(int length, String message, String color) {
		if (statusLabel == null)
			return;
		statusLabel.setVisible(true);
		statusLabel.setText(message);
		statusLabel.setStyle("-fx-text-fill: " + color + ";");
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					if (statusLabel != null) {
						statusLabel.setVisible(false);
					}
				});
				timer.cancel();
			}
		}, length * 1000L);
	}
}