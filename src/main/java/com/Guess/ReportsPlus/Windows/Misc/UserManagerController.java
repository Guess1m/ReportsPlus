package com.Guess.ReportsPlus.Windows.Misc;

import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.config.ConfigWriter;
import com.Guess.ReportsPlus.util.Other.controllerUtils;
import com.Guess.ReportsPlus.util.Strings.dropdownInfo;
import com.Guess.ReportsPlus.util.UserProfiles.User;
import jakarta.xml.bind.JAXBException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.MainApplication.mainDesktopControllerObj;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.Severity.DEBUG;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.Severity.WARN;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Other.InitTableColumns.commonTableFontSize;

public class UserManagerController {
	public static UserManagerController userManagerController;
	private final ObservableList<User> userData = FXCollections.observableArrayList();
	
	@FXML
	private BorderPane root;
	@FXML
	private TableView<User> userElementsTable;
	@FXML
	private TextField callsignField;
	@FXML
	private ComboBox agencyDropdown;
	@FXML
	private TextField numberField;
	@FXML
	private TextField nameField;
	@FXML
	private ComboBox rankDropdown;
	@FXML
	private ComboBox divisionDropdown;
	@FXML
	private AnchorPane mainPane;
	@FXML
	private Label statusLabel;
	@FXML
	private Label userProfileLabel;
	@FXML
	private Label editProfileSubLabel;
	@FXML
	private Label editProfileLabel;
	@FXML
	private Label userProfileSubLabel;
	@FXML
	private Label agency;
	@FXML
	private Label division;
	@FXML
	private Label number;
	@FXML
	private Label name;
	@FXML
	private Label callsign;
	@FXML
	private Label rank;
	@FXML
	private Button delbtn;
	@FXML
	private Button savebtn;
	
	public void initialize() {
		initColumns();
		
		statusLabel.setVisible(false);
		
		rankDropdown.getItems().addAll(dropdownInfo.ranks);
		divisionDropdown.getItems().addAll(dropdownInfo.divisions);
		agencyDropdown.getItems().addAll(dropdownInfo.agencies);
		
		updateTableData();
		
		String name = "", callsign = "", agency = "", division = "", rank = "", number = "";
		try {
			name = ConfigReader.configRead("userInfo", "Name");
			callsign = ConfigReader.configRead("userInfo", "Callsign");
			agency = ConfigReader.configRead("userInfo", "Agency");
			division = ConfigReader.configRead("userInfo", "Division");
			rank = ConfigReader.configRead("userInfo", "Rank");
			number = ConfigReader.configRead("userInfo", "Number");
		} catch (Exception e) {
			logError("Error reading userInfo: ", e);
		}
		
		nameField.setText(name);
		callsignField.setText(callsign);
		agencyDropdown.setValue(agency);
		divisionDropdown.setValue(division);
		rankDropdown.setValue(rank);
		numberField.setText(number);
		
		editProfileLabel.setText(localization.getLocalizedMessage("UserManager.editProfileLabel", "Edit Current Profile:"));
		editProfileSubLabel.setText(localization.getLocalizedMessage("UserManager.editProfileSubLabel", "Switch to or edit the selected profile"));
		userProfileLabel.setText(localization.getLocalizedMessage("UserManager.userProfileLabel", "User Profiles:"));
		userProfileSubLabel.setText(localization.getLocalizedMessage("UserManager.userProfileSubLabel", "These are the found user profiles"));
	}
	
	@FXML
	public void saveButtonClick(ActionEvent actionEvent) {
		boolean valid = true;
		
		if (agencyDropdown.getValue() == null) {
			log("AgencyDropdown is null, can't save user", WARN);
			valid = false;
		}
		if (divisionDropdown.getValue() == null) {
			log("DivisionDropdown is null, can't save user", WARN);
			valid = false;
		}
		if (rankDropdown.getValue() == null) {
			log("RankDropdown is null, can't save user", WARN);
			valid = false;
		}
		
		if (!valid) {
			showStatusUpdate(3, "Fill out all fields!", "#ff2d2d");
			return;
		}
		
		String name = nameField.getText().trim();
		String callsign = callsignField.getText().trim();
		String number = numberField.getText().trim();
		String agency = agencyDropdown.getValue().toString();
		String division = divisionDropdown.getValue().toString();
		String rank = rankDropdown.getValue().toString();
		
		User newUser = new User(name, agency, division, rank, number, callsign);
		try {
			User.addUser(newUser);
			updateTableData();
		} catch (JAXBException e) {
			logError("Error while adding user '" + name + "': ", e);
		}
		
		ConfigWriter.configwrite("userInfo", "Name", name);
		ConfigWriter.configwrite("userInfo", "Callsign", callsign);
		ConfigWriter.configwrite("userInfo", "Agency", agency);
		ConfigWriter.configwrite("userInfo", "Division", division);
		ConfigWriter.configwrite("userInfo", "Rank", rank);
		ConfigWriter.configwrite("userInfo", "Number", number);
		
		try {
			mainDesktopControllerObj.getOfficerInfoName().setText(ConfigReader.configRead("userInfo", "Name") + ", " + ConfigReader.configRead("userInfo", "Agency"));
		} catch (IOException e) {
			logError("Unable to read userInfo name from config (2), ", e);
		}
		
		showStatusUpdate(3, "Successfully added profile", "green");
		
	}
	
	@FXML
	public void onRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			User user = (User) userElementsTable.getSelectionModel().getSelectedItem();
			if (user != null) {
				nameField.setText(user.getName());
				callsignField.setText(user.getCallsign());
				numberField.setText(user.getNumber());
				
				agencyDropdown.setValue(user.getAgency());
				divisionDropdown.setValue(user.getDivision());
				rankDropdown.setValue(user.getRank());
			}
		}
	}
	
	@FXML
	public void deleteButtonClick(ActionEvent actionEvent) throws IOException {
		String name = nameField.getText().trim();
		
		if (name.equalsIgnoreCase(ConfigReader.configRead("userInfo", "Name"))) {
			log("You cannot delete the active user profile!", DEBUG);
			showStatusUpdate(2, "You cannot delete the active user profile!", "#ff2d2d");
			return;
		}
		
		try {
			User.deleteUser(name);
			updateTableData();
		} catch (JAXBException e) {
			logError("Error while deleting user '" + name + "': ", e);
		}
	}
	
	private void initColumns() {
		TableColumn<User, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameColumn.setCellFactory(column -> new TableCell<User, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize + "-fx-font-family: \"Segoe UI Semibold\";");
					setGraphic(label);
				}
			}
		});
		
		TableColumn<User, String> agencyColumn = new TableColumn<>("Agency");
		agencyColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(agencyColumn, Pos.CENTER_LEFT, "Agency:");
		agencyColumn.setCellValueFactory(new PropertyValueFactory<>("agency"));
		agencyColumn.setCellFactory(column -> new TableCell<User, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize + "-fx-font-family: \"Segoe UI Semibold\";");
					setGraphic(label);
				}
			}
		});
		
		TableColumn<User, String> divisionColumn = new TableColumn<>("Division");
		;
		divisionColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(divisionColumn, Pos.CENTER_LEFT, "Division:");
		divisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
		divisionColumn.setCellFactory(column -> new TableCell<User, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize + "-fx-font-family: \"Segoe UI Semibold\";");
					setGraphic(label);
				}
			}
		});
		
		TableColumn<User, String> rankColumn = new TableColumn<>("Rank");
		;
		rankColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		controllerUtils.setColumnAlignment(rankColumn, Pos.CENTER_LEFT, "Rank:");
		rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
		rankColumn.setCellFactory(column -> new TableCell<User, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					Label label = new Label(item);
					label.setStyle(commonTableFontSize + "-fx-font-family: \"Segoe UI Semibold\";");
					setGraphic(label);
				}
			}
		});
		ObservableList<TableColumn<User, ?>> columns = FXCollections.observableArrayList(nameColumn, rankColumn, agencyColumn, divisionColumn);
		for (TableColumn<User, ?> column : columns) {
			column.setReorderable(false);
			column.setEditable(false);
		}
		userElementsTable.getColumns().addAll(nameColumn, rankColumn, agencyColumn, divisionColumn);
		nameColumn.setMaxWidth(155);
		nameColumn.setMinWidth(80);
		rankColumn.setMaxWidth(190);
		userElementsTable.setItems(userData);
	}
	
	private void updateTableData() {
		userData.clear();
		
		try {
			List<User> userList = User.loadUserProfiles().getUserList();
			if (userList == null) {
				try {
					User u = new User(ConfigReader.configRead("userInfo", "Name"), ConfigReader.configRead("userInfo", "Agency"), ConfigReader.configRead("userInfo", "Division"), ConfigReader.configRead("userInfo", "Rank"), ConfigReader.configRead("userInfo", "Number"), ConfigReader.configRead("userInfo", "Callsign"));
					try {
						User.addUser(u);
					} catch (JAXBException e) {
						logError("Error while adding current user: ", e);
					}
					log("Added current account: " + u.toString(), DEBUG);
				} catch (IOException e) {
					logError("Error reading user info: ", e);
				}
			}
			for (User u : User.loadUserProfiles().getUserList()) {
				userData.add(u);
			}
		} catch (JAXBException e) {
			logError("Failed to load user profiles: ", e);
		}
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
	
	public BorderPane getRoot() {
		return root;
	}
	
	public Label getAgency() {
		return agency;
	}
	
	public Label getCallsign() {
		return callsign;
	}
	
	public Label getDivision() {
		return division;
	}
	
	public Label getName() {
		return name;
	}
	
	public Label getNumber() {
		return number;
	}
	
	public Label getRank() {
		return rank;
	}
	
	public Label getEditProfileLabel() {
		return editProfileLabel;
	}
	
	public Label getEditProfileSubLabel() {
		return editProfileSubLabel;
	}
	
	public Label getUserProfileLabel() {
		return userProfileLabel;
	}
	
	public Label getUserProfileSubLabel() {
		return userProfileSubLabel;
	}
	
	public Button getDelbtn() {
		return delbtn;
	}
	
	public Button getSavebtn() {
		return savebtn;
	}
}