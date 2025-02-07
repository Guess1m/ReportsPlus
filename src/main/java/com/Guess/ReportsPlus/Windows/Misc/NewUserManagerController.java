package com.Guess.ReportsPlus.Windows.Misc;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.config.ConfigWriter;
import com.Guess.ReportsPlus.util.Strings.dropdownInfo;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager.createCustomWindow;
import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.MainApplication.mainDesktopControllerObj;
import static com.Guess.ReportsPlus.Windows.Settings.settingsController.UIDarkColor;
import static com.Guess.ReportsPlus.Windows.Settings.settingsController.UILightColor;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.Severity.DEBUG;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.Severity.WARN;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;

public class NewUserManagerController {
	private static final ObservableList<User> userData = FXCollections.observableArrayList();
	public static NewUserManagerController newUserManagerController;
	CustomWindow addUserWindow = null;
	
	//TODO: add total reports to banner
	
	@FXML
	private BorderPane root;
	@FXML
	private AnchorPane mainPane;
	@FXML
	private VBox usersList;
	@FXML
	private Label header;
	@FXML
	private Button newUserBtn;
	
	private static void addUserToList(VBox userVBox) {
		newUserManagerController.getUsersList().getChildren().add(userVBox);
	}
	
	public static void refreshUsersList() {
		newUserManagerController.getUsersList().getChildren().clear();
		newUserManagerController.userData.clear();
		try {
			List<User> userList = User.loadUserProfiles().getUserList();
			if (userList == null) {
				try {
					User u = new User(ConfigReader.configRead("userInfo", "Name"), ConfigReader.configRead("userInfo", "Agency"), ConfigReader.configRead("userInfo", "Division"), ConfigReader.configRead("userInfo", "Rank"), ConfigReader.configRead("userInfo", "Number"), ConfigReader.configRead("userInfo", "Callsign"));
					try {
						User.addUser(u);
					} catch (JAXBException e) {
						logError("UserManager; Error while adding current user: ", e);
					}
					log("UserManager; Added current account: " + u.toString(), DEBUG);
				} catch (IOException e) {
					logError("UserManager; Error reading user info: ", e);
				}
			}
			for (User u : User.loadUserProfiles().getUserList()) {
				userData.add(u);
			}
		} catch (JAXBException e) {
			logError("UserManager; Failed to load user profiles: ", e);
		}
		
		for (User u : userData) {
			log("UserManager; Added User To List: " + u.toString(), DEBUG);
			addUserToList(new UserProfile(u.getName(), u.getRank(), u.getAgency(), u.getDivision()).getProfileBox());
		}
	}
	
	public void initialize() {
		Platform.runLater(() -> {
			refreshUsersList();
		});
		
		loadLocale();
	}
	
	private void loadLocale() {
		header.setText(localization.getLocalizedMessage("UserManager.userProfileLabel", "Active User Profiles:"));
		newUserBtn.setText(localization.getLocalizedMessage("UserManager.newProfileButton", "Create New Profile"));
	}
	
	public VBox getUsersList() {
		return usersList;
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
	
	@FXML
	public void createUserBtn(ActionEvent actionEvent) {
		Label newUserLabel = new Label(localization.getLocalizedMessage("UserManager.newProfileLabel", "Name:"));
		newUserLabel.setFont(Font.font("Inter 28pt Medium", 14));
		
		TextField newUserName = new TextField();
		newUserName.setPrefWidth(180);
		
		Button saveBtn = new Button(localization.getLocalizedMessage("Settings.NotiSaveButton", "Save"));
		saveBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 13px;");
		saveBtn.setPrefWidth(100);
		
		BorderPane saveBtnBox = new BorderPane();
		saveBtnBox.setRight(saveBtn);
		
		VBox box = new VBox(10, newUserLabel, newUserName, saveBtnBox);
		box.setAlignment(Pos.TOP_LEFT);
		box.setPadding(new Insets(15));
		box.setStyle("-fx-background-color: #F4F4F4; -fx-border-radius: 10; -fx-background-radius: 10;");
		
		BorderPane window = new BorderPane();
		window.setCenter(box);
		window.setPrefSize(300, 150);
		window.setStyle("-fx-background-color: white; -fx-border-color: #CCC; -fx-border-width: 1; -fx-border-radius: 10;");
		
		saveBtn.setOnAction((ActionEvent event) -> {
			if (newUserName.getText().trim().isEmpty()) {
				return;
			}
			User user = new User();
			user.setName(newUserName.getText().trim());
			try {
				User.addUser(user);
				refreshUsersList();
			} catch (JAXBException e) {
				logError("UserManager; Error while adding current user [" + newUserName.getText().trim() + "]: ", e);
			}
			
			if (addUserWindow != null) {
				addUserWindow.closeWindow();
			}
		});
		
		addUserWindow = createCustomWindow(mainDesktopControllerObj.getDesktopContainer(), window, "New Profile", false, 1, true, true, mainDesktopControllerObj.getTaskBarApps(), new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Apps/profile.png"))));
	}
	
}

class UserProfile {
	private VBox mainVbox;
	private EditProfileDropdown editProfileDropdown;
	
	public UserProfile(String name, String rank, String agency, String division) {
		mainVbox = new VBox();
		mainVbox.setPrefWidth(575);
		mainVbox.setStyle("-fx-background-radius: 10;");
		
		GridPane gridPane = new GridPane();
		
		ColumnConstraints column1 = new ColumnConstraints();
		ColumnConstraints column2 = new ColumnConstraints();
		column1.setHgrow(Priority.ALWAYS);
		column1.setPercentWidth(45);
		column2.setHgrow(Priority.ALWAYS);
		column2.setPercentWidth(55);
		
		gridPane.getColumnConstraints().addAll(column1, column2);
		
		gridPane.setHgap(50);
		gridPane.setVgap(5);
		
		Label bannerName = new Label(name);
		bannerName.setFont(Font.font("Inter 28pt Bold", 15));
		try {
			bannerName.setStyle("-fx-text-fill: " + ConfigReader.configRead("uiColors", "mainColor") + ";");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		Label bannerRank = new Label(rank);
		bannerRank.setOpacity(0.71);
		bannerRank.setFont(Font.font("Inter 28pt Medium", 12));
		
		Label bannerAgency = new Label(agency);
		bannerAgency.setOpacity(0.8);
		bannerAgency.setFont(Font.font("Inter 28pt Bold", 14));
		try {
			bannerAgency.setStyle("-fx-text-fill: " + ConfigReader.configRead("uiColors", "secondaryColor") + ";");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		Label bannerDivision = new Label(division);
		bannerDivision.setOpacity(0.71);
		bannerDivision.setFont(Font.font("Inter 28pt Medium", 12));
		
		try {
			if (ConfigReader.configRead("uiColors", "UIDarkMode").equalsIgnoreCase("true")) {
				bannerRank.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				bannerDivision.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				
				gridPane.setStyle("-fx-background-color: rgb(0,0,0,0.05); -fx-background-radius: 10;");
				
			} else {
				bannerRank.setStyle("-fx-text-fill: " + UILightColor + ";");
				bannerDivision.setStyle("-fx-text-fill: " + UILightColor + ";");
				
				gridPane.setStyle("-fx-background-color: rgb(255,255,255,0.15); -fx-background-radius: 10;");
			}
		} catch (IOException exception) {
			logError("UserManager; Unable to read UI colors from config (3), ", exception);
		}
		
		gridPane.add(bannerName, 0, 0);
		gridPane.add(bannerRank, 1, 1);
		gridPane.add(bannerAgency, 1, 0);
		gridPane.add(bannerDivision, 0, 1);
		gridPane.setPadding(new Insets(15, 25, 15, 25));
		
		editProfileDropdown = new EditProfileDropdown(name);
		
		gridPane.setOnMouseClicked(event -> {
			if (mainVbox.getChildren().contains(editProfileDropdown.getDropdown())) {
				mainVbox.getChildren().remove(editProfileDropdown.getDropdown());
			} else {
				mainVbox.getChildren().add(editProfileDropdown.getDropdown());
			}
		});
		
		mainVbox.getChildren().add(gridPane);
	}
	
	public VBox getProfileBox() {
		return mainVbox;
	}
}

class EditProfileDropdown {
	private AnchorPane dropdown;
	private Label statusLabel;
	
	public EditProfileDropdown(String officerName) {
		User user = User.getUser(officerName);
		if (user == null) {
			return;
		}
		
		String hoverStyle;
		String nonTransparentBtn;
		try {
			hoverStyle = "-fx-background-color: " + ConfigReader.configRead("uiColors", "accentColor");
			nonTransparentBtn = "-fx-background-color: " + ConfigReader.configRead("uiColors", "mainColor") + ";";
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		dropdown = new AnchorPane();
		dropdown.setMaxHeight(Double.NEGATIVE_INFINITY);
		
		VBox profileVbox = new VBox(5);
		profileVbox.setSpacing(5);
		AnchorPane.setBottomAnchor(profileVbox, 0.0);
		AnchorPane.setLeftAnchor(profileVbox, 0.0);
		AnchorPane.setRightAnchor(profileVbox, 0.0);
		AnchorPane.setTopAnchor(profileVbox, 0.0);
		
		Label editProfileLabel = new Label(localization.getLocalizedMessage("UserManager.editProfileLabel", "Edit Profile:"));
		editProfileLabel.setFont(Font.font("Inter 28pt Bold", 15));
		try {
			editProfileLabel.setStyle("-fx-text-fill: " + ConfigReader.configRead("uiColors", "accentColor") + ";");
		} catch (IOException e) {
			logError("UserManager; Error setting profile label style", e);
		}
		
		HBox profileFieldsHBox = new HBox(15);
		
		VBox numberVbox = new VBox();
		HBox.setHgrow(numberVbox, Priority.ALWAYS);
		Label numberLabel = new Label(localization.getLocalizedMessage("Callout_Manager.CalloutNumber", "Number:"));
		numberLabel.setOpacity(0.7);
		numberLabel.setFont(Font.font("Inter 24pt Regular", 13));
		TextField numberField = new TextField();
		numberVbox.getChildren().addAll(numberLabel, numberField);
		
		VBox callsignVbox = new VBox();
		HBox.setHgrow(callsignVbox, Priority.ALWAYS);
		Label callsignLabel = new Label(localization.getLocalizedMessage("UserManager.CallsignLabel", "Callsign:"));
		callsignLabel.setOpacity(0.7);
		callsignLabel.setFont(Font.font("Inter 24pt Regular", 13));
		TextField callsignField = new TextField();
		callsignVbox.getChildren().addAll(callsignLabel, callsignField);
		
		profileFieldsHBox.getChildren().addAll(numberVbox, callsignVbox);
		VBox.setMargin(profileFieldsHBox, new Insets(10, 20, 10, 20));
		
		Label rankLabel = new Label(localization.getLocalizedMessage("UserManager.RankLabel", "Rank:"));
		rankLabel.setOpacity(0.7);
		rankLabel.setFont(Font.font("Inter 24pt Regular", 13));
		rankLabel.setPadding(new Insets(0, 0, -5, 20));
		
		ComboBox<String> rankDropdown = new ComboBox<>();
		AnchorPane rankAnchorPane = new AnchorPane();
		rankAnchorPane.getChildren().add(rankDropdown);
		AnchorPane.setBottomAnchor(rankDropdown, 0.0);
		AnchorPane.setLeftAnchor(rankDropdown, 0.0);
		AnchorPane.setRightAnchor(rankDropdown, 0.0);
		AnchorPane.setTopAnchor(rankDropdown, 0.0);
		VBox.setMargin(rankAnchorPane, new Insets(0, 20, 0, 20));
		rankDropdown.getItems().addAll(dropdownInfo.ranks);
		
		Label divisionLabel = new Label(localization.getLocalizedMessage("UserManager.DivisionLabel", "Division:"));
		divisionLabel.setOpacity(0.7);
		divisionLabel.setFont(Font.font("Inter 24pt Regular", 13));
		divisionLabel.setPadding(new Insets(0, 0, -5, 20));
		
		ComboBox<String> divisionDropdown = new ComboBox<>();
		AnchorPane divisionAnchorPane = new AnchorPane();
		divisionAnchorPane.getChildren().add(divisionDropdown);
		AnchorPane.setBottomAnchor(divisionDropdown, 0.0);
		AnchorPane.setLeftAnchor(divisionDropdown, 0.0);
		AnchorPane.setRightAnchor(divisionDropdown, 0.0);
		AnchorPane.setTopAnchor(divisionDropdown, 0.0);
		VBox.setMargin(divisionAnchorPane, new Insets(0, 20, 0, 20));
		divisionDropdown.getItems().addAll(dropdownInfo.divisions);
		
		Label agencyLabel = new Label(localization.getLocalizedMessage("UserManager.Agencylabel", "Agency:"));
		agencyLabel.setOpacity(0.7);
		agencyLabel.setFont(Font.font("Inter 24pt Regular", 13));
		agencyLabel.setPadding(new Insets(0, 0, -5, 20));
		
		ComboBox<String> agencyDropdown = new ComboBox<>();
		AnchorPane agencyAnchorPane = new AnchorPane();
		agencyAnchorPane.getChildren().add(agencyDropdown);
		AnchorPane.setBottomAnchor(agencyDropdown, 0.0);
		AnchorPane.setLeftAnchor(agencyDropdown, 0.0);
		AnchorPane.setRightAnchor(agencyDropdown, 0.0);
		AnchorPane.setTopAnchor(agencyDropdown, 0.0);
		VBox.setMargin(agencyAnchorPane, new Insets(0, 20, 0, 20));
		agencyDropdown.getItems().addAll(dropdownInfo.agencies);
		
		Button deleteButton = new Button(localization.getLocalizedMessage("Callout_Manager.DelCalloutButton", "Delete"));
		deleteButton.setStyle(nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12");
		deleteButton.setOnMouseEntered(e -> deleteButton.setStyle(hoverStyle + ";-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
		deleteButton.setOnMouseExited(e -> deleteButton.setStyle(nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
		
		deleteButton.setFont(Font.font("Inter 28pt Medium", 13));
		
		Button saveButton = new Button(localization.getLocalizedMessage("Settings.NotiSaveButton", "Save"));
		saveButton.setStyle(nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12");
		saveButton.setOnMouseEntered(e -> saveButton.setStyle(hoverStyle + ";-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
		saveButton.setOnMouseExited(e -> saveButton.setStyle(nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
		
		saveButton.setFont(Font.font("Inter 28pt Medium", 13));
		
		statusLabel = new Label();
		statusLabel.setVisible(false);
		
		HBox actionButtonsHBox = new HBox(15);
		actionButtonsHBox.setPadding(new Insets(10, 20, 0, 0));
		actionButtonsHBox.getChildren().addAll(statusLabel, deleteButton, saveButton);
		actionButtonsHBox.setAlignment(Pos.CENTER);
		
		Label statusLabel = new Label();
		statusLabel.setFont(Font.font("Inter 28pt Medium", 13));
		BorderPane profileActionsPane = new BorderPane();
		profileActionsPane.setCenter(statusLabel);
		profileActionsPane.setRight(actionButtonsHBox);
		
		profileVbox.getChildren().addAll(editProfileLabel, profileFieldsHBox, rankLabel, rankAnchorPane, divisionLabel, divisionAnchorPane, agencyLabel, agencyAnchorPane, profileActionsPane);
		
		profileVbox.setPadding(new Insets(15, 15, 15, 15));
		
		deleteButton.setOnAction(e -> {
			try {
				if (officerName.equalsIgnoreCase(ConfigReader.configRead("userInfo", "Name"))) {
					log("UserManager; You cannot delete the active user profile!", DEBUG);
					showStatusUpdate(2, "You cannot delete the active user profile!", "#ff2d2d");
					return;
				}
				
				User.deleteUser(officerName);
				NewUserManagerController.refreshUsersList();
			} catch (JAXBException | IOException exception) {
				logError("UserManager; Error while deleting user '" + officerName + "': ", exception);
			}
		});
		
		saveButton.setOnAction(e -> {
			boolean valid = true;
			
			if (agencyDropdown.getValue() == null) {
				log("UserManager; AgencyDropdown is null, can't save user", WARN);
				valid = false;
			}
			if (divisionDropdown.getValue() == null) {
				log("UserManager; DivisionDropdown is null, can't save user", WARN);
				valid = false;
			}
			if (rankDropdown.getValue() == null) {
				log("UserManager; RankDropdown is null, can't save user", WARN);
				valid = false;
			}
			
			if (!valid) {
				showStatusUpdate(3, "Fill out all fields!", "#ff2d2d");
				return;
			}
			
			String name = officerName.trim();
			String callsign = callsignField.getText().trim();
			String number = numberField.getText().trim();
			String agency = agencyDropdown.getValue().toString();
			String division = divisionDropdown.getValue().toString();
			String rank = rankDropdown.getValue().toString();
			
			User newUser = new User(name, agency, division, rank, number, callsign);
			try {
				User.addUser(newUser);
				NewUserManagerController.refreshUsersList();
			} catch (JAXBException exception) {
				logError("UserManager; Error while adding user '" + name + "': ", exception);
			}
			
			ConfigWriter.configwrite("userInfo", "Name", name);
			ConfigWriter.configwrite("userInfo", "Callsign", callsign);
			ConfigWriter.configwrite("userInfo", "Agency", agency);
			ConfigWriter.configwrite("userInfo", "Division", division);
			ConfigWriter.configwrite("userInfo", "Rank", rank);
			ConfigWriter.configwrite("userInfo", "Number", number);
			
			try {
				mainDesktopControllerObj.getOfficerInfoName().setText(ConfigReader.configRead("userInfo", "Name") + ", " + ConfigReader.configRead("userInfo", "Agency"));
			} catch (IOException exception) {
				logError("UserManager; Unable to read userInfo name from config (2), ", exception);
			}
			
			showStatusUpdate(3, "Successfully added profile", "green");
			
		});
		
		numberField.setText(user.getNumber());
		callsignField.setText(user.getCallsign());
		agencyDropdown.setValue(user.getAgency());
		divisionDropdown.setValue(user.getDivision());
		rankDropdown.setValue(user.getRank());
		
		try {
			if (ConfigReader.configRead("uiColors", "UIDarkMode").equalsIgnoreCase("true")) {
				numberLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				callsignLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				agencyLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				divisionLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				rankLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				
				dropdown.setStyle("-fx-background-color: rgb(0,0,0,0.085); -fx-background-radius: 10;");
				
			} else {
				numberLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				callsignLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				agencyLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				divisionLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				rankLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				
				dropdown.setStyle("-fx-background-color: rgb(255,255,255,0.1); -fx-background-radius: 10;");
			}
		} catch (IOException exception) {
			logError("UserManager; Unable to read UI colors from config (4), ", exception);
		}
		
		dropdown.getChildren().add(profileVbox);
	}
	
	private void showStatusUpdate(int Length, String message, String color) {
		statusLabel.setVisible(true);
		statusLabel.setText(message);
		statusLabel.setStyle("-fx-text-fill: " + color + ";");
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				statusLabel.setVisible(false);
			}
		}, Length * 1000);
	}
	
	public AnchorPane getDropdown() {
		return dropdown;
	}
}