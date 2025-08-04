package com.Guess.ReportsPlus.Windows.Apps;

import static com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager.createCustomWindow;
import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.MainApplication.getDate;
import static com.Guess.ReportsPlus.MainApplication.getTime;
import static com.Guess.ReportsPlus.MainApplication.mainDesktopControllerObj;
import static com.Guess.ReportsPlus.Windows.Server.CurrentIDViewController.generateRandomNumber;
import static com.Guess.ReportsPlus.Windows.Settings.settingsController.UIDarkColor;
import static com.Guess.ReportsPlus.Windows.Settings.settingsController.UILightColor;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logInfo;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationInfo;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.updateStyleProperty;
import static com.Guess.ReportsPlus.util.Strings.URLStrings.calloutDataURL;
import static com.Guess.ReportsPlus.util.Strings.URLStrings.calloutHistoryURL;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.config.ConfigWriter;
import com.Guess.ReportsPlus.logs.Callout.CalloutReportUtils;
import com.Guess.ReportsPlus.util.Other.controllerUtils;
import com.Guess.ReportsPlus.util.Other.Callout.CalloutDisplayItem;
import com.Guess.ReportsPlus.util.Other.Callout.CalloutManager;
import com.Guess.ReportsPlus.util.Other.Callout.Messages.StoredMessage;
import com.Guess.ReportsPlus.util.Strings.URLStrings;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class CalloutViewController {
	public static CalloutViewController calloutViewController;
	@FXML
	private BorderPane root;
	@FXML
	private ToggleButton displayCallHistoryBtn;
	@FXML
	private Button exitBtn;
	@FXML
	private Button createCallBtn;
	@FXML
	private TableView<CalloutDisplayItem> historyCalloutsTable;
	@FXML
	private TableView<CalloutDisplayItem> activeCalloutsTable;
	@FXML
	private Button settingsBtn;
	@FXML
	private ToggleButton displayActiveCallsBtn;
	@FXML
	private VBox historyCalloutsContainer;
	@FXML
	private StackPane calloutPane;
	@FXML
	private GridPane callInfoContainer;
	@FXML
	private VBox notesContainer;
	@FXML
	private TextField callInfoCountyField;
	@FXML
	private TextField callInfoPriorityField;
	@FXML
	private Label callInfoPriority;
	@FXML
	private TextField callInfoNumberField;
	@FXML
	private TextField callInfoTypeField;
	@FXML
	private TextField callInfoDateField;
	@FXML
	private Label callInfoDate;
	@FXML
	private TextField callInfoTimeField;
	@FXML
	private Label callInfoNumber;
	@FXML
	private TextField callInfoStreetField;
	@FXML
	private Label callInfoType;
	@FXML
	private TextField callInfoAreaField;
	@FXML
	private Label callInfoTime;
	@FXML
	private Label callInfoCounty;
	@FXML
	private Label callInfoStreet;
	@FXML
	private Label callInfoArea;
	@FXML
	private Button callInfoDeleteButton;
	@FXML
	private Tab callInfoNotesTab;
	@FXML
	private TabPane callInfoTabPane;
	@FXML
	private BorderPane notesContainerParent;
	@FXML
	private HBox callInfoTypeContainer;
	@FXML
	private TextField callInfoCommentField;
	@FXML
	private Button callInfoTypeSend;
	private CalloutDisplayItem selectedCallout;
	private boolean inActiveCallout;

	private String getTextColorForPriority(String priority) {
		if (priority == null) {
			return "black";
		}
		String highPriority = "code3";
		String mediumPriority = "code2";
		String lowPriority = "code1";
		String defaultValue = "default";
		try {
			highPriority = ConfigReader.configRead("calloutManager", "highPriorityValue");
			mediumPriority = ConfigReader.configRead("calloutManager", "mediumPriorityValue");
			lowPriority = ConfigReader.configRead("calloutManager", "lowPriorityValue");
			defaultValue = ConfigReader.configRead("calloutManager", "defaultValue");
		} catch (IOException e) {
			logError("CalloutManager; Error reading priority values from config: ", e);
		}
		String highValue = "#FF3B30";
		String mediumValue = "#FF9500";
		String lowValue = "#34C759";
		String defaultColor = "#5AC8FA";
		try {
			highValue = ConfigReader.configRead("calloutManager", "highPriorityColor");
			mediumValue = ConfigReader.configRead("calloutManager", "mediumPriorityColor");
			lowValue = ConfigReader.configRead("calloutManager", "lowPriorityColor");
			defaultColor = ConfigReader.configRead("calloutManager", "defaultColor");
		} catch (IOException e) {
			logError("CalloutManager; Error reading priority colors from config: ", e);
		}
		if (priority.equalsIgnoreCase(highPriority)) {
			return highValue;
		} else if (priority.equalsIgnoreCase(mediumPriority)) {
			return mediumValue;
		} else if (priority.equalsIgnoreCase(lowPriority)) {
			return lowValue;
		} else if (priority.equalsIgnoreCase(defaultValue)) {
			return defaultColor;
		} else {
			return "black";
		}
	}

	private void setCellTextAndPriorityStyle(TableCell<CalloutDisplayItem, String> cell, String textValue,
			CalloutDisplayItem rowItem) {
		if (textValue == null) {
			cell.setText(null);
			cell.setStyle("");
			return;
		}
		cell.setText(textValue);
		String styleToApply = "-fx-text-fill: " + getTextColorForPriority(null)
				+ ";  -fx-font-family: 'Inter 24pt Regular';";
		if (rowItem != null) {
			String priority = rowItem.getPriority();
			styleToApply = "-fx-text-fill: " + getTextColorForPriority(priority)
					+ "; -fx-font-family: 'Inter 24pt Regular';";
		}
		cell.setStyle(styleToApply);
	}

	private Callback<TableColumn<CalloutDisplayItem, String>, TableCell<CalloutDisplayItem, String>> createPriorityStyledCellFactory() {
		return column -> new TableCell<>() {
			@Override
			protected void updateItem(String itemData, boolean empty) {
				super.updateItem(itemData, empty);
				if (empty) {
					setText(null);
					setStyle("");
				} else {
					CalloutDisplayItem rowDisplayItem = getTableRow() != null ? getTableRow().getItem() : null;
					setCellTextAndPriorityStyle(this, itemData, rowDisplayItem);
				}
			}
		};
	}

	public void initialize() {
		calloutViewController = this;
		setupActiveCalloutsTable();
		setupHistoryCalloutsTable();
		CalloutManager.loadActiveCallouts(activeCalloutsTable);
		CalloutManager.loadHistoryCallouts(historyCalloutsTable);
		inActiveCallout = true;
		displayActiveCallsBtn.setSelected(true);
		activeCalloutsTable.setVisible(true);
		historyCalloutsContainer.setVisible(false);
		callInfoContainer.setVisible(false);
		callInfoCommentField.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.ENTER) {
				onSendNoteClick(new ActionEvent());
			}
		});
		displayActiveCallsBtn.setOnAction(event -> {
			if (!displayActiveCallsBtn.isSelected()) {
				displayActiveCallsBtn.setSelected(true);
			}
			inActiveCallout = true;
			activeCalloutsTable.setVisible(true);
			historyCalloutsContainer.setVisible(false);
			callInfoContainer.setVisible(false);
			displayCallHistoryBtn.setSelected(false);
			CalloutManager.loadActiveCallouts(activeCalloutsTable);
			activeCalloutsTable.getSelectionModel().clearSelection();
		});
		displayCallHistoryBtn.setOnAction(event -> {
			if (!displayCallHistoryBtn.isSelected()) {
				displayCallHistoryBtn.setSelected(true);
			}
			inActiveCallout = false;
			historyCalloutsContainer.setVisible(true);
			activeCalloutsTable.setVisible(false);
			callInfoContainer.setVisible(false);
			displayActiveCallsBtn.setSelected(false);
			CalloutManager.loadHistoryCallouts(historyCalloutsTable);
			historyCalloutsTable.getSelectionModel().clearSelection();
		});
		activeCalloutsTable.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldSelection, newSelection) -> {
					if (newSelection != null) {
						showCallDetails(newSelection);
					}
				});
		historyCalloutsTable.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldSelection, newSelection) -> {
					if (newSelection != null) {
						showCallDetails(newSelection);
					}
				});
		Platform.runLater(() -> {
			loadLocale();
		});
	}

	private void loadLocale() {
		callInfoArea.setText(localization.getLocalizedMessage("Callout_Manager.CalloutArea", "Area"));
		callInfoCounty.setText(localization.getLocalizedMessage("Callout_Manager.CalloutCounty", "County"));
		callInfoDate.setText(localization.getLocalizedMessage("Callout_Manager.CalloutDate", "Date"));
		callInfoNumber.setText(localization.getLocalizedMessage("Callout_Manager.CalloutNumberShort", "Call#"));
		callInfoStreet.setText(localization.getLocalizedMessage("Callout_Manager.CalloutStreet", "Street"));
		callInfoTime.setText(localization.getLocalizedMessage("Callout_Manager.CalloutTime", "Time"));
		callInfoPriority.setText(localization.getLocalizedMessage("Callout_Manager.CalloutPriority", "Priority"));
		callInfoType.setText(localization.getLocalizedMessage("Callout_Manager.CalloutType", "Type"));
		callInfoTypeSend.setText(localization.getLocalizedMessage("Callout_Manager.SendButton", "Send"));
		displayActiveCallsBtn
				.setText(localization.getLocalizedMessage("Callout_Manager.ActiveCallsLabel", "Active Calls"));
		displayCallHistoryBtn
				.setText(localization.getLocalizedMessage("Callout_Manager.CallHistoryLabel", "Call History"));
		createCallBtn.setText(localization.getLocalizedMessage("Callout_Manager.CreateCallButton", "Create Call"));
		settingsBtn.setText(localization.getLocalizedMessage("Settings.MainHeader", "Settings"));
		exitBtn.setText(localization.getLocalizedMessage("Callout_Manager.ExitButton", "Exit"));
		callInfoDeleteButton.setText(localization.getLocalizedMessage("Callout_Manager.DelCalloutButton", "Delete"));
		callInfoNotesTab.setText(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"));
	}

	private void showCallDetails(CalloutDisplayItem callout) {
		activeCalloutsTable.setVisible(false);
		historyCalloutsContainer.setVisible(false);
		callInfoContainer.setVisible(true);
		callInfoNumberField.setText(callout.getNumber());
		callInfoTypeField.setText(callout.getType());
		callInfoPriorityField.setText(callout.getPriority());
		callInfoStreetField.setText(callout.getStreet());
		callInfoAreaField.setText(callout.getArea());
		callInfoCountyField.setText(callout.getCounty());
		callInfoDateField.setText(callout.getStartDate());
		callInfoTimeField.setText(callout.getStartTime());
		refreshNotesForCallout(callout);
		selectedCallout = callout;
	}

	private void refreshNotesForCallout(CalloutDisplayItem callout) {
		notesContainer.getChildren().clear();
		if (callout.getDescription() != null && !callout.getDescription().isEmpty()) {
			notesContainer.getChildren().add(createCalloutNote(callout.getStartDate(), callout.getStartTime(),
					"DISPATCH", callout.getDescription(), null, callout));
		}
		if (callout.getMessage() != null && !callout.getMessage().isEmpty()) {
			notesContainer.getChildren().add(createCalloutNote(callout.getStartDate(), callout.getStartTime(),
					"DISPATCH", callout.getMessage(), null, callout));
		}
		for (StoredMessage storedMessage : callout.getStoredMessages()) {
			notesContainer.getChildren().add(createCalloutNote(storedMessage.getDate(), storedMessage.getTime(),
					storedMessage.getSender(), storedMessage.getMessage(), storedMessage, callout));
		}
	}

	private GridPane createCalloutNote(String date, String time, String sender, String text, StoredMessage message,
			CalloutDisplayItem callout) {
		FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("Windows/Templates/calloutNote-template.fxml"));
		Parent anchorPaneParent;
		try {
			anchorPaneParent = loader.load();
		} catch (IOException e) {
			logError("Could not load callout note template", e);
			return null;
		}
		GridPane anchorPane = (GridPane) anchorPaneParent;
		GridPane container = (GridPane) anchorPane.lookup("#notesTemplateContainer");
		Label datelbl = (Label) anchorPane.lookup("#notesTemplateDate");
		Label timelbl = (Label) anchorPane.lookup("#notesTemplateTime");
		Label senderlbl = (Label) anchorPane.lookup("#notesTemplateSender");
		Label textlbl = (Label) anchorPane.lookup("#notesTemplateText");
		Button deleteButton = (Button) anchorPane.lookup("#deleteNoteButton");
		deleteButton.setVisible(false);
		datelbl.setStyle("-fx-font-family: 'Inter 24pt Regular';");
		timelbl.setStyle("-fx-font-family: 'Inter 24pt Regular';");
		textlbl.setStyle("-fx-font-family: 'Inter 24pt Regular';");
		if (container != null && datelbl != null && timelbl != null && senderlbl != null && textlbl != null) {
			datelbl.setText(date);
			timelbl.setText(time);
			senderlbl.setText(sender);
			textlbl.setText(text);
		}
		String secclr = "lightblue";
		String accclr = "blue";
		try {
			accclr = ConfigReader.configRead("uiColors", "accentColor");
			secclr = ConfigReader.configRead("uiColors", "secondaryColor");
		} catch (IOException e) {
			logError("Could not read config for secondary/accent, using default: ", e);
		}
		if (message != null) {
			deleteButton.setVisible(true);
			deleteButton.setOnAction(event -> {
				callout.removeStoredMessage(message);
				if (inActiveCallout) {
					CalloutManager.updateCallout(calloutDataURL, callout);
				} else {
					CalloutManager.updateCallout(calloutHistoryURL, callout);
				}
				refreshNotesForCallout(callout);
			});
		}
		try {
			if (ConfigReader.configRead("uiColors", "UIDarkMode").equalsIgnoreCase("true")) {
				container.setStyle("-fx-background-color: rgb(0,0,0,0.05); -fx-border-color: " + accclr
						+ "; -fx-border-width: 0 0 1 0;");
				datelbl.setStyle(updateStyleProperty(datelbl, "-fx-text-fill", UIDarkColor));
				timelbl.setStyle(updateStyleProperty(timelbl, "-fx-text-fill", UIDarkColor));
				senderlbl.setStyle(updateStyleProperty(senderlbl, "-fx-text-fill", UIDarkColor));
				textlbl.setStyle(updateStyleProperty(textlbl, "-fx-text-fill", UIDarkColor));
			} else {
				container.setStyle("-fx-background-color: rgb(255,255,255,0.05); -fx-border-color: " + accclr
						+ "; -fx-border-width: 0 0 1 0;");
				datelbl.setStyle(updateStyleProperty(datelbl, "-fx-text-fill", UILightColor));
				timelbl.setStyle(updateStyleProperty(timelbl, "-fx-text-fill", UILightColor));
				senderlbl.setStyle(updateStyleProperty(senderlbl, "-fx-text-fill", UILightColor));
				textlbl.setStyle(updateStyleProperty(textlbl, "-fx-text-fill", UILightColor));
			}
			senderlbl.setStyle(
					"-fx-text-fill: " + secclr + "; -fx-font-family: 'Inter 28pt Medium'; -fx-font-size: 14;");
		} catch (IOException e) {
			logError("Error loading uiColors.UIDarkMode from createCalloutNote: ", e);
		}
		return anchorPane;
	}

	private void setupActiveCalloutsTable() {
		activeCalloutsTable.setEditable(true);
		activeCalloutsTable.setPlaceholder(
				new Label(localization.getLocalizedMessage("Callout_Manager.NoActiveCallouts", "No active callouts.")));
		activeCalloutsTable.getItems().clear();
		activeCalloutsTable.getColumns().clear();
		Callback<TableColumn<CalloutDisplayItem, String>, TableCell<CalloutDisplayItem, String>> priorityStyledCellFactory = createPriorityStyledCellFactory();
		TableColumn<CalloutDisplayItem, String> numberCol = new TableColumn<>(
				localization.getLocalizedMessage("Callout_Manager.CalloutNumberShort", "Call#"));
		numberCol.setCellValueFactory(new PropertyValueFactory<>("number"));
		numberCol.setPrefWidth(50);
		numberCol.setCellFactory(priorityStyledCellFactory);
		TableColumn<CalloutDisplayItem, String> streetCol = new TableColumn<>(
				localization.getLocalizedMessage("Callout_Manager.CalloutStreet", "Street"));
		streetCol.setCellValueFactory(new PropertyValueFactory<>("street"));
		streetCol.setPrefWidth(120);
		streetCol.setCellFactory(priorityStyledCellFactory);
		TableColumn<CalloutDisplayItem, String> areaCol = new TableColumn<>(
				localization.getLocalizedMessage("Callout_Manager.CalloutArea", "Area"));
		areaCol.setCellValueFactory(new PropertyValueFactory<>("area"));
		areaCol.setPrefWidth(90);
		areaCol.setCellFactory(priorityStyledCellFactory);
		TableColumn<CalloutDisplayItem, String> countyCol = new TableColumn<>(
				localization.getLocalizedMessage("Callout_Manager.CalloutCounty", "County"));
		countyCol.setCellValueFactory(new PropertyValueFactory<>("county"));
		countyCol.setPrefWidth(80);
		countyCol.setCellFactory(priorityStyledCellFactory);
		TableColumn<CalloutDisplayItem, String> typeCol = new TableColumn<>(
				localization.getLocalizedMessage("Callout_Manager.CalloutType", "Type"));
		typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
		typeCol.setPrefWidth(150);
		typeCol.setCellFactory(priorityStyledCellFactory);
		TableColumn<CalloutDisplayItem, String> priorityCol = new TableColumn<>(
				localization.getLocalizedMessage("Callout_Manager.CalloutPriority", "Priority"));
		priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
		priorityCol.setPrefWidth(80);
		priorityCol.setCellFactory(priorityStyledCellFactory);
		TableColumn<CalloutDisplayItem, String> statusCol = new TableColumn<>(
				localization.getLocalizedMessage("Callout_Manager.CalloutStatus", "Status"));
		statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
		statusCol.setPrefWidth(110);
		statusCol.setEditable(true);
		statusCol.setCellFactory(column -> new TableCell<>() {
			private final ComboBox<String> statusDropdown = new ComboBox<>();
			{
				String[] statuses = { "Responded", "Not Responded" };
				statusDropdown.getItems().addAll(statuses);
				try {
					statusDropdown.getStylesheets()
							.add(Objects.requireNonNull(Launcher.class.getResource("css/callout/calloutManager.css"))
									.toExternalForm());
					statusDropdown.getStyleClass().add("combo-boxCal");
				} catch (NullPointerException e) {
					logError("CSS for ComboBox not found: css/callout/calloutManager.css", e);
				}
				statusDropdown.setPrefWidth(Double.MAX_VALUE);
				statusDropdown.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
					if (newVal != null) {
						updateComboBoxStyle(newVal);
					}
				});
				statusDropdown.setOnAction(event -> {
					if (getTableRow() != null && getTableRow().getItem() != null) {
						CalloutDisplayItem item = getTableRow().getItem();
						String selectedStatus = statusDropdown.getSelectionModel().getSelectedItem();
						if (item != null && selectedStatus != null && !selectedStatus.equals(item.getStatus())) {
							item.setStatus(selectedStatus);
							CalloutManager.setValueByNumber(URLStrings.calloutDataURL, item.getNumber(), "Status",
									selectedStatus);
							logInfo("Status updated for callout #" + item.getNumber() + " to " + selectedStatus);
							String sender = "";
							String rank = "";
							try {
								sender = ConfigReader.configRead("userInfo", "Name");
								rank = ConfigReader.configRead("userInfo", "Rank").split(" ")[0];
							} catch (IOException e) {
								logError("Error loading userInfo.Name or userInfo.Rank from setupActiveCalloutsTable: ",
										e);
							}
							item.addStoredMessage(new StoredMessage(rank + " " + sender, getDate(),
									getTime(false, false), "Status", "Status updated to " + selectedStatus + "."));
							CalloutManager.updateCallout(calloutDataURL, item);
						}
					}
				});
			}

			private void updateComboBoxStyle(String status) {
				statusDropdown.pseudoClassStateChanged(PseudoClass.getPseudoClass("responded"),
						status.equals("Responded"));
				statusDropdown.pseudoClassStateChanged(PseudoClass.getPseudoClass("notresponded"),
						status.equals("Not Responded"));
			}

			@Override
			protected void updateItem(String currentStatus, boolean empty) {
				super.updateItem(currentStatus, empty);
				if (empty || currentStatus == null) {
					setGraphic(null);
					setText(null);
					setStyle("");
				} else {
					statusDropdown.setValue(currentStatus);
					updateComboBoxStyle(currentStatus);
					setGraphic(statusDropdown);
					setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
				}
			}
		});
		TableColumn<CalloutDisplayItem, Void> actionCol = new TableColumn<>(
				localization.getLocalizedMessage("Callout_Manager.Actions", "Actions"));
		actionCol.setPrefWidth(110);
		Callback<TableColumn<CalloutDisplayItem, Void>, TableCell<CalloutDisplayItem, Void>> cellFactory = param -> {
			return new TableCell<>() {
				private final Button btn = new Button(
						localization.getLocalizedMessage("Callout_Manager.CloseCalloutButton", "Close Callout"));
				{
					try {
						String defStyle = "-fx-background-color: "
								+ controllerUtils.hexToRgba(ConfigReader.configRead("uiColors", "secondaryColor"), 0.5)
								+ "; -fx-border-color: rgb(100,100,100,0.1); -fx-text-fill: white;  -fx-font-family: 'Inter 24pt Regular'; -fx-padding: 3 10 3 10;";
						btn.setStyle(defStyle);
					} catch (IOException e) {
						logError("Error loading uiColors.secondaryColor (for close button): ", e);
						btn.setStyle("-fx-padding: 3 10 3 10;");
					}
					btn.setOnAction((ActionEvent event) -> {
						CalloutDisplayItem item = getTableView().getItems().get(getIndex());
						if (item != null) {
							CalloutManager.addCallout(calloutHistoryURL, item.getNumber(), item.getType(),
									item.getDescription(), item.getMessage(), item.getPriority(), item.getStreet(),
									item.getArea(), item.getCounty(), item.getStartTime(), item.getStartDate(),
									item.getStatus(),
									item.getMessages());
							CalloutManager.deleteCallout(URLStrings.calloutDataURL, item.getNumber());
							CalloutManager.loadActiveCallouts(activeCalloutsTable);
							CalloutManager.loadHistoryCallouts(historyCalloutsTable);
						}
					});
				}

				@Override
				public void updateItem(Void item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setGraphic(null);
					} else {
						setGraphic(btn);
						setAlignment(Pos.CENTER);
					}
				}
			};
		};
		actionCol.setCellFactory(cellFactory);
		activeCalloutsTable.getColumns().addAll(numberCol, streetCol, areaCol, countyCol, typeCol, priorityCol,
				statusCol, actionCol);
		for (TableColumn<CalloutDisplayItem, ?> column : activeCalloutsTable.getColumns()) {
			column.setReorderable(false);
			column.setSortable(false);
		}
		activeCalloutsTable.setItems(FXCollections.observableArrayList());
	}

	private void setupHistoryCalloutsTable() {
		historyCalloutsTable.setPlaceholder(
				new Label(localization.getLocalizedMessage("Callout_Manager.NoCalloutHistory", "No callout history.")));
		historyCalloutsTable.getItems().clear();
		historyCalloutsTable.getColumns().clear();
		Callback<TableColumn<CalloutDisplayItem, String>, TableCell<CalloutDisplayItem, String>> priorityStyledCellFactory = createPriorityStyledCellFactory();
		TableColumn<CalloutDisplayItem, String> numberCol = new TableColumn<>(
				localization.getLocalizedMessage("Callout_Manager.CalloutNumberShort", "Call#"));
		numberCol.setCellValueFactory(new PropertyValueFactory<>("number"));
		numberCol.setPrefWidth(50);
		numberCol.setCellFactory(priorityStyledCellFactory);
		TableColumn<CalloutDisplayItem, String> streetCol = new TableColumn<>(
				localization.getLocalizedMessage("Callout_Manager.CalloutStreet", "Street"));
		streetCol.setCellValueFactory(new PropertyValueFactory<>("street"));
		streetCol.setPrefWidth(120);
		streetCol.setCellFactory(priorityStyledCellFactory);
		TableColumn<CalloutDisplayItem, String> areaCol = new TableColumn<>(
				localization.getLocalizedMessage("Callout_Manager.CalloutArea", "Area"));
		areaCol.setCellValueFactory(new PropertyValueFactory<>("area"));
		areaCol.setPrefWidth(90);
		areaCol.setCellFactory(priorityStyledCellFactory);
		TableColumn<CalloutDisplayItem, String> countyCol = new TableColumn<>(
				localization.getLocalizedMessage("Callout_Manager.CalloutCounty", "County"));
		countyCol.setCellValueFactory(new PropertyValueFactory<>("county"));
		countyCol.setPrefWidth(80);
		countyCol.setCellFactory(priorityStyledCellFactory);
		TableColumn<CalloutDisplayItem, String> typeCol = new TableColumn<>(
				localization.getLocalizedMessage("Callout_Manager.CalloutType", "Type"));
		typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
		typeCol.setPrefWidth(150);
		typeCol.setCellFactory(priorityStyledCellFactory);
		TableColumn<CalloutDisplayItem, String> priorityCol = new TableColumn<>(
				localization.getLocalizedMessage("Callout_Manager.CalloutPriority", "Priority"));
		priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
		priorityCol.setPrefWidth(80);
		priorityCol.setCellFactory(priorityStyledCellFactory);
		TableColumn<CalloutDisplayItem, String> statusCol = new TableColumn<>(
				localization.getLocalizedMessage("Callout_Manager.CalloutStatus", "Status"));
		statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
		statusCol.setPrefWidth(95);
		statusCol.setCellFactory(column -> new TableCell<>() {
			@Override
			protected void updateItem(String statusValue, boolean empty) {
				super.updateItem(statusValue, empty);
				if (empty || statusValue == null) {
					setText(null);
					setStyle("");
					setGraphic(null);
				} else {
					Label statusLabel = new Label();
					String baseStyle = "-fx-padding: 3px 7px; -fx-background-radius: 4px; -fx-border-radius: 4px;";
					statusLabel.setText(statusValue);
					String priorityTextColor = getTextColorForPriority(null);
					String finalLabelStyle;
					if ("Not Responded".equals(statusValue)) {
						finalLabelStyle = baseStyle
								+ "-fx-text-fill: white; -fx-background-color: #e53e3e;  -fx-font-family: 'Inter 24pt Regular';";
					} else if ("Responded".equals(statusValue)) {
						finalLabelStyle = baseStyle
								+ "-fx-text-fill: white; -fx-background-color: #38a169;  -fx-font-family: 'Inter 24pt Regular';";
					} else {
						finalLabelStyle = baseStyle + "-fx-background-color: #e2e8f0; -fx-text-fill: "
								+ priorityTextColor + "; -fx-font-family: 'Inter 24pt Regular';";
					}
					statusLabel.setStyle(finalLabelStyle);
					setGraphic(statusLabel);
					setText(null);
					setAlignment(Pos.CENTER_LEFT);
				}
			}
		});
		TableColumn<CalloutDisplayItem, Void> actionCol = new TableColumn<>(
				localization.getLocalizedMessage("Callout_Manager.Actions", "Actions"));
		actionCol.setPrefWidth(130);
		Callback<TableColumn<CalloutDisplayItem, Void>, TableCell<CalloutDisplayItem, Void>> cellFactory = param -> {
			return new TableCell<>() {
				private final Button createReportBtn = new Button(
						localization.getLocalizedMessage("Callout_Manager.NewCalloutButtonShort", "Report"));
				private final Button deleteHistoryBtn = new Button(
						localization.getLocalizedMessage("Callout_Manager.DelCalloutButton", "Delete"));
				private final HBox buttonsPane = new HBox(5);
				{
					buttonsPane.setAlignment(Pos.CENTER);
					try {
						String defStyle = "-fx-background-color: "
								+ controllerUtils.hexToRgba(ConfigReader.configRead("uiColors", "secondaryColor"), 0.7)
								+ "; -fx-border-color: rgb(100,100,100,0.1); -fx-text-fill: white; -fx-font-family: 'Inter 24pt Regular'; -fx-padding: 3 10 3 10;";
						createReportBtn.setStyle(defStyle);
					} catch (IOException e) {
						logError("Error loading uiColors.secondaryColor (for new report button): ", e);
						createReportBtn.setStyle("-fx-padding: 3 10 3 10;");
					}
					createReportBtn.setMinWidth(Region.USE_PREF_SIZE);
					String deleteBtnStyle = "-fx-background-color: tomato; -fx-text-fill: white; -fx-padding: 3 10;";
					deleteHistoryBtn.setStyle(deleteBtnStyle);
					deleteHistoryBtn.setMinWidth(Region.USE_PREF_SIZE);
					buttonsPane.getChildren().addAll(createReportBtn, deleteHistoryBtn);
					createReportBtn.setOnAction((ActionEvent event) -> {
						CalloutDisplayItem item = getTableView().getItems().get(getIndex());
						if (item != null) {
							Map<String, Object> calloutReportObj = CalloutReportUtils.newCallout();
							Map<String, Object> calloutControls = (Map<String, Object>) calloutReportObj
									.get(localization.getLocalizedMessage("ReportWindows.CalloutReportTitle",
											"Callout Report") + " Map");
							((TextField) calloutControls.get(localization
									.getLocalizedMessage("ReportWindows.CalloutNumberField", "callout num")))
									.setText(item.getNumber());
							((ComboBox<String>) calloutControls
									.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area")))
									.setValue(item.getArea());
							String notes = item.getDescription();
							if (item.getMessage() != null && !item.getMessage().isEmpty()) {
								notes += "\n" + item.getMessage();
							}
							((TextArea) calloutControls
									.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "notes")))
									.setText(notes);
							((TextField) calloutControls
									.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county")))
									.setText(item.getCounty());
							((ComboBox<String>) calloutControls
									.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street")))
									.setValue(item.getStreet());
							((TextField) calloutControls
									.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date")))
									.setText(item.getStartDate());
							((TextField) calloutControls
									.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time")))
									.setText(item.getStartTime());
							((TextField) calloutControls
									.get(localization.getLocalizedMessage("ReportWindows.FieldType", "type")))
									.setText(item.getType());
							((TextField) calloutControls
									.get(localization.getLocalizedMessage("ReportWindows.CalloutCodeField", "code")))
									.setText(item.getPriority());
						}
					});
					deleteHistoryBtn.setOnAction((ActionEvent event) -> {
						CalloutDisplayItem item = getTableView().getItems().get(getIndex());
						if (item != null) {
							CalloutManager.deleteCallout(calloutHistoryURL, item.getNumber());
							logInfo("Deleted callout #" + item.getNumber() + " from Callout History via table button.");
							CalloutManager.loadHistoryCallouts(historyCalloutsTable);
						}
					});
				}

				@Override
				public void updateItem(Void item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setGraphic(null);
					} else {
						setGraphic(buttonsPane);
						setAlignment(Pos.CENTER);
					}
				}
			};
		};
		actionCol.setCellFactory(cellFactory);
		historyCalloutsTable.getColumns().addAll(numberCol, streetCol, areaCol, countyCol, typeCol, priorityCol,
				statusCol, actionCol);
		for (TableColumn<CalloutDisplayItem, ?> column : historyCalloutsTable.getColumns()) {
			column.setSortable(false);
			column.setReorderable(false);
		}
		historyCalloutsTable.setItems(FXCollections.observableArrayList());
	}

	public BorderPane getRoot() {
		return root;
	}

	public TableView<CalloutDisplayItem> getActiveCalloutsTable() {
		return activeCalloutsTable;
	}

	public TableView<CalloutDisplayItem> getHistoryCalloutsTable() {
		return historyCalloutsTable;
	}

	public Button getExitBtn() {
		return exitBtn;
	}

	public Button getCallInfoTypeSend() {
		return callInfoTypeSend;
	}

	public Button getSettingsBtn() {
		return settingsBtn;
	}

	public Button getCreateCallBtn() {
		return createCallBtn;
	}

	public ToggleButton getDisplayActiveCallsBtn() {
		return displayActiveCallsBtn;
	}

	public ToggleButton getDisplayCallHistoryBtn() {
		return displayCallHistoryBtn;
	}

	public Tab getCallInfoNotesTab() {
		return callInfoNotesTab;
	}

	public StackPane getCalloutPane() {
		return calloutPane;
	}

	public VBox getNotesContainer() {
		return notesContainer;
	}

	public GridPane getCallInfoContainer() {
		return callInfoContainer;
	}

	public TextField getCallInfoCountyField() {
		return callInfoCountyField;
	}

	public TextField getCallInfoAreaField() {
		return callInfoAreaField;
	}

	public TextField getCallInfoDateField() {
		return callInfoDateField;
	}

	public TextField getCallInfoNumberField() {
		return callInfoNumberField;
	}

	public TextField getCallInfoPriorityField() {
		return callInfoPriorityField;
	}

	public TextField getCallInfoStreetField() {
		return callInfoStreetField;
	}

	public TextField getCallInfoTimeField() {
		return callInfoTimeField;
	}

	public TextField getCallInfoTypeField() {
		return callInfoTypeField;
	}

	public Label getCallInfoArea() {
		return callInfoArea;
	}

	public Label getCallInfoCounty() {
		return callInfoCounty;
	}

	public Label getCallInfoDate() {
		return callInfoDate;
	}

	public Label getCallInfoNumber() {
		return callInfoNumber;
	}

	public Label getCallInfoPriority() {
		return callInfoPriority;
	}

	public Label getCallInfoStreet() {
		return callInfoStreet;
	}

	public Label getCallInfoTime() {
		return callInfoTime;
	}

	public Label getCallInfoType() {
		return callInfoType;
	}

	public HBox getCallInfoTypeContainer() {
		return callInfoTypeContainer;
	}

	public TabPane getCallInfoTabPane() {
		return callInfoTabPane;
	}

	@FXML
	public void createCallBtnPress(ActionEvent actionEvent) {
		VBox createCallLayout = new VBox(10);
		createCallLayout.setPadding(new Insets(5));
		createCallLayout.setAlignment(Pos.CENTER);
		createCallLayout.setStyle("-fx-background-color: #f4f6f8;");
		GridPane formGrid = new GridPane();
		formGrid.setHgap(10);
		formGrid.setVgap(7);
		formGrid.setAlignment(Pos.CENTER);
		int rowIndex = 0;
		String labelStyle = "-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #34495e; -fx-font-family: 'Inter 24pt Regular';";
		String fieldStyle = "-fx-font-size: 13px; -fx-border-color: #bdc3c7; -fx-border-radius: 3; -fx-background-radius: 3; -fx-font-family: 'Inter 24pt Regular';";
		Label numberLabel = new Label(localization.getLocalizedMessage("Callout_Manager.CalloutNumber", "Number:"));
		numberLabel.setStyle(labelStyle);
		TextField numberField = new TextField(generateRandomNumber(5));
		numberField.setEditable(false);
		numberField.setStyle(fieldStyle);
		formGrid.add(numberLabel, 0, rowIndex);
		formGrid.add(numberField, 1, rowIndex++);
		Label typeLabel = new Label(localization.getLocalizedMessage("CalloutPopup.TypeLabel", "Type:"));
		typeLabel.setStyle(labelStyle);
		TextField typeField = new TextField();
		typeField.setStyle(fieldStyle);
		formGrid.add(typeLabel, 0, rowIndex);
		formGrid.add(typeField, 1, rowIndex++);
		Label priorityLabel = new Label(localization.getLocalizedMessage("CalloutPopup.PriorityLabel", "Priority:"));
		priorityLabel.setStyle(labelStyle);
		ComboBox<String> priorityComboBox = new ComboBox<>();
		try {
			priorityComboBox.getItems().addAll(ConfigReader.configRead("calloutManager", "highPriorityValue"),
					ConfigReader.configRead("calloutManager", "mediumPriorityValue"),
					ConfigReader.configRead("calloutManager", "lowPriorityValue"),
					ConfigReader.configRead("calloutManager", "defaultValue"));
			priorityComboBox.setValue(ConfigReader.configRead("calloutManager", "defaultValue"));
		} catch (IOException e) {
			logError("CreateCalloutWindow; Error reading priority values from config: ", e);
			priorityComboBox.getItems().addAll("High", "Medium", "Low", "Default");
			priorityComboBox.setValue("Default");
		}
		priorityComboBox.setStyle(fieldStyle);
		priorityComboBox.setPrefWidth(Double.MAX_VALUE);
		formGrid.add(priorityLabel, 0, rowIndex);
		formGrid.add(priorityComboBox, 1, rowIndex++);
		Label streetLabel = new Label(localization.getLocalizedMessage("CalloutPopup.Streetlabel", "Street:"));
		streetLabel.setStyle(labelStyle);
		TextField streetField = new TextField();
		streetField.setStyle(fieldStyle);
		formGrid.add(streetLabel, 0, rowIndex);
		formGrid.add(streetField, 1, rowIndex++);
		Label areaLabel = new Label(localization.getLocalizedMessage("CalloutPopup.AreaLabel", "Area:"));
		areaLabel.setStyle(labelStyle);
		TextField areaField = new TextField();
		areaField.setStyle(fieldStyle);
		formGrid.add(areaLabel, 0, rowIndex);
		formGrid.add(areaField, 1, rowIndex++);
		Label countyLabel = new Label(localization.getLocalizedMessage("CalloutPopup.CountyLabel", "County:"));
		countyLabel.setStyle(labelStyle);
		TextField countyField = new TextField();
		countyField.setStyle(fieldStyle);
		formGrid.add(countyLabel, 0, rowIndex);
		formGrid.add(countyField, 1, rowIndex++);
		Label startDateLabel = new Label(localization.getLocalizedMessage("CalloutPopup.DateLabel", "Date:"));
		startDateLabel.setStyle(labelStyle);
		TextField startDatePicker = new TextField(getDate());
		startDatePicker.setEditable(false);
		startDatePicker.setStyle(fieldStyle);
		startDatePicker.setPrefWidth(Double.MAX_VALUE);
		formGrid.add(startDateLabel, 0, rowIndex);
		formGrid.add(startDatePicker, 1, rowIndex++);
		Label startTimeLabel = new Label(localization.getLocalizedMessage("CalloutPopup.TimeLabel", "Time:"));
		startTimeLabel.setStyle(labelStyle);
		TextField startTimeField = new TextField(getTime(false, false));
		startTimeField.setEditable(false);
		startTimeField.setStyle(fieldStyle);
		formGrid.add(startTimeLabel, 0, rowIndex);
		formGrid.add(startTimeField, 1, rowIndex++);
		Label descriptionLabel = new Label(
				localization.getLocalizedMessage("CalloutPopup.DescriptionLabel", "Description:"));
		descriptionLabel.setStyle(labelStyle);
		TextArea descriptionArea = new TextArea();
		descriptionArea.setStyle(fieldStyle);
		descriptionArea.setPrefRowCount(3);
		descriptionArea.setWrapText(true);
		formGrid.add(descriptionLabel, 0, rowIndex);
		formGrid.add(descriptionArea, 1, rowIndex++);
		Label messageLabel = new Label(
				localization.getLocalizedMessage("Callout_Manager.Additional", "Additional (Optional):"));
		messageLabel.setStyle(labelStyle);
		TextArea messageArea = new TextArea();
		messageArea.setStyle(fieldStyle);
		messageArea.setPrefRowCount(3);
		messageArea.setWrapText(true);
		formGrid.add(messageLabel, 0, rowIndex);
		formGrid.add(messageArea, 1, rowIndex++);
		for (Node node : formGrid.getChildren()) {
			if (GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == 1 && node instanceof Region) {
				((Region) node).setPrefWidth(280);
				((Region) node).setMaxWidth(Double.MAX_VALUE);
			}
		}
		ColumnConstraints col0 = new ColumnConstraints();
		col0.setHgrow(Priority.NEVER);
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setHgrow(Priority.ALWAYS);
		formGrid.getColumnConstraints().addAll(col0, col1);
		Button createButton = new Button(localization.getLocalizedMessage("Settings.NotiSaveButton", "Save"));
		createButton.setStyle(
				"-fx-background-color: #3f8157; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 4 8; -fx-font-family: \"Inter 28pt Medium\"");
		HBox buttonBox = new HBox(createButton);
		buttonBox.setAlignment(Pos.CENTER_RIGHT);
		buttonBox.setPadding(new Insets(3));
		ScrollPane scrollPane = new ScrollPane(formGrid);
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);
		scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-padding: 5;");
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		VBox.setVgrow(scrollPane, Priority.ALWAYS);
		createCallLayout.getChildren().addAll(scrollPane, buttonBox);
		BorderPane createCallWindowRoot = new BorderPane(createCallLayout);
		createCallWindowRoot.setPrefHeight(520);
		createCallWindowRoot.setPrefWidth(500);
		CustomWindow window = createCustomWindow(mainDesktopControllerObj.getDesktopContainer(), createCallWindowRoot,
				"Create New Callout", true, 1, true, true, mainDesktopControllerObj.getTaskBarApps(),
				new Image(Objects.requireNonNull(
						Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Apps/setting.png"))));
		createButton.setOnAction(e -> {
			String number = numberField.getText().trim();
			String type = typeField.getText().trim();
			String priority = priorityComboBox.getValue();
			String street = streetField.getText().trim();
			String area = areaField.getText().trim();
			String county = countyField.getText().trim();
			String localDate = startDatePicker.getText().trim();
			String startTime = startTimeField.getText().trim();
			String description = descriptionArea.getText().trim();
			String message = messageArea.getText().trim();
			String status = "Not Responded";
			String errorTitle = "Error";
			if (number.isEmpty()) {
				showNotificationInfo(errorTitle, "Callout Number is required.");
				numberField.requestFocus();
				return;
			}
			if (type.isEmpty()) {
				showNotificationInfo(errorTitle, "Callout Type is required.");
				typeField.requestFocus();
				return;
			}
			if (priority == null) {
				showNotificationInfo(errorTitle, "Priority is required.");
				priorityComboBox.requestFocus();
				return;
			}
			if (localDate == null) {
				showNotificationInfo(errorTitle, "Start Date is required.");
				startDatePicker.requestFocus();
				return;
			}
			if (street.isEmpty()) {
				showNotificationInfo(errorTitle, "Street is required.");
				streetField.requestFocus();
				return;
			}
			if (area.isEmpty()) {
				showNotificationInfo(errorTitle, "Area is required.");
				areaField.requestFocus();
				return;
			}
			if (county.isEmpty()) {
				showNotificationInfo(errorTitle, "County is required.");
				countyField.requestFocus();
				return;
			}
			if (startTime.isEmpty()) {
				showNotificationInfo(errorTitle, "Start time is required.");
				startTimeField.requestFocus();
				return;
			}
			CalloutManager.addCallout(URLStrings.calloutDataURL, number, type, description, message, priority, street,
					area, county, startTime, localDate, status, null);
			logInfo("New callout created: #" + number);
			showNotificationInfo("Callout Manager", "Callout #" + number + " created successfully.");
			CalloutManager.loadActiveCallouts(activeCalloutsTable);
			if (window != null) {
				window.closeWindow();
			}
		});
	}

	@FXML
	public void exitBtnPress(ActionEvent actionEvent) {
		CustomWindow thisWindow = WindowManager.getWindow("Callout Manager");
		if (thisWindow != null) {
			thisWindow.closeWindow();
		}
	}

	@FXML
	public void settingsBtnPress(ActionEvent actionEvent) {
		VBox settingsLayout = new VBox(15);
		settingsLayout.setPadding(new Insets(20));
		settingsLayout.setAlignment(Pos.TOP_CENTER);
		settingsLayout.setStyle("-fx-background-color: #f0f0f0;");
		GridPane priorityGrid = new GridPane();
		priorityGrid.setHgap(10);
		priorityGrid.setVgap(8);
		priorityGrid.setAlignment(Pos.CENTER);
		Label p1NameLabel = new Label("Priority 1 Name:");
		p1NameLabel.setStyle("-fx-font-size: 12px; -fx-font-family: 'Inter 24pt Regular';");
		TextField p1NameField = new TextField();
		p1NameField.setPrefWidth(120);
		try {
			p1NameField.setText(ConfigReader.configRead("calloutManager", "highPriorityValue"));
		} catch (IOException e) {
			logError("CalloutManagerSettings; Error reading highPriorityValue from config: ", e);
			p1NameField.setText("High");
		}
		Label p1ColorLabel = new Label("Color:");
		p1ColorLabel.setStyle("-fx-font-size: 12px; -fx-font-family: 'Inter 24pt Regular';");
		ColorPicker p1ColorPicker = new ColorPicker();
		try {
			p1ColorPicker.setValue(Color.valueOf(ConfigReader.configRead("calloutManager", "highPriorityColor")));
		} catch (IOException e) {
			p1ColorPicker.setValue(Color.RED);
			logError("CalloutManagerSettings; Error reading highPriorityColor from config: ", e);
		}
		priorityGrid.add(p1NameLabel, 0, 0);
		priorityGrid.add(p1NameField, 1, 0);
		priorityGrid.add(p1ColorLabel, 2, 0);
		priorityGrid.add(p1ColorPicker, 3, 0);
		Label p2NameLabel = new Label("Priority 2 Name:");
		p2NameLabel.setStyle("-fx-font-size: 12px; -fx-font-family: 'Inter 24pt Regular';");
		TextField p2NameField = new TextField();
		p2NameField.setPrefWidth(120);
		try {
			p2NameField.setText(ConfigReader.configRead("calloutManager", "mediumPriorityValue"));
		} catch (IOException e) {
			logError("CalloutManagerSettings; Error reading mediumPriorityValue from config: ", e);
			p2NameField.setText("Medium");
		}
		Label p2ColorLabel = new Label("Color:");
		p2ColorLabel.setStyle("-fx-font-size: 12px; -fx-font-family: 'Inter 24pt Regular';");
		ColorPicker p2ColorPicker = new ColorPicker(Color.ORANGE);
		try {
			p2ColorPicker.setValue(Color.valueOf(ConfigReader.configRead("calloutManager", "mediumPriorityColor")));
		} catch (IOException e) {
			logError("CalloutManagerSettings; Error reading mediumPriorityColor from config: ", e);
			p2ColorPicker.setValue(Color.ORANGE);
		}
		priorityGrid.add(p2NameLabel, 0, 1);
		priorityGrid.add(p2NameField, 1, 1);
		priorityGrid.add(p2ColorLabel, 2, 1);
		priorityGrid.add(p2ColorPicker, 3, 1);
		Label p3NameLabel = new Label("Priority 3 Name:");
		p3NameLabel.setStyle("-fx-font-size: 12px; -fx-font-family: 'Inter 24pt Regular';");
		TextField p3NameField = new TextField();
		p3NameField.setPrefWidth(120);
		try {
			p3NameField.setText(ConfigReader.configRead("calloutManager", "lowPriorityValue"));
		} catch (IOException e) {
			logError("CalloutManagerSettings; Error reading lowPriorityValue from config: ", e);
			p3NameField.setText("Low");
		}
		Label p3ColorLabel = new Label("Color:");
		p3ColorLabel.setStyle("-fx-font-size: 12px; -fx-font-family: 'Inter 24pt Regular';");
		ColorPicker p3ColorPicker = new ColorPicker(Color.GREEN);
		try {
			p3ColorPicker.setValue(Color.valueOf(ConfigReader.configRead("calloutManager", "lowPriorityColor")));
		} catch (IOException e) {
			logError("CalloutManagerSettings; Error reading lowPriorityColor from config: ", e);
			p3ColorPicker.setValue(Color.YELLOWGREEN);
		}
		priorityGrid.add(p3NameLabel, 0, 2);
		priorityGrid.add(p3NameField, 1, 2);
		priorityGrid.add(p3ColorLabel, 2, 2);
		priorityGrid.add(p3ColorPicker, 3, 2);
		Label p4NameLabel = new Label("Priority 4 Name:");
		p4NameLabel.setStyle("-fx-font-size: 12px; -fx-font-family: 'Inter 24pt Regular';");
		TextField p4NameField = new TextField();
		p4NameField.setPrefWidth(120);
		try {
			p4NameField.setText(ConfigReader.configRead("calloutManager", "defaultValue"));
		} catch (IOException e) {
			logError("CalloutManagerSettings; Error reading defaultValue from config: ", e);
			p4NameField.setText("Default");
		}
		Label p4ColorLabel = new Label("Color:");
		p4ColorLabel.setStyle("-fx-font-size: 12px; -fx-font-family: 'Inter 24pt Regular';");
		ColorPicker p4ColorPicker = new ColorPicker();
		try {
			p4ColorPicker.setValue(Color.valueOf(ConfigReader.configRead("calloutManager", "defaultColor")));
		} catch (IOException e) {
			logError("CalloutManagerSettings; Error reading defaultColor from config: ", e);
			p4ColorPicker.setValue(Color.LIGHTBLUE);
		}
		priorityGrid.add(p4NameLabel, 0, 3);
		priorityGrid.add(p4NameField, 1, 3);
		priorityGrid.add(p4ColorLabel, 2, 3);
		priorityGrid.add(p4ColorPicker, 3, 3);
		settingsLayout.getChildren().add(priorityGrid);
		settingsLayout.getChildren().add(new Separator(Orientation.HORIZONTAL));
		Button saveButton = new Button(localization.getLocalizedMessage("Settings.NotiSaveButton", "Save"));
		saveButton.setStyle(
				"-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-family: 'Inter 24pt Regular';");
		saveButton.setPadding(new Insets(5));
		saveButton.setOnAction(e -> {
			logInfo("--- Callout Settings Saved ---");
			String p1Name = p1NameField.getText();
			Color p1Color = p1ColorPicker.getValue();
			String p1HexColor = String.format("#%02x%02x%02x", (int) (p1Color.getRed() * 255),
					(int) (p1Color.getGreen() * 255), (int) (p1Color.getBlue() * 255));
			logInfo("Priority 1: Name='" + p1Name + "', Color=" + p1HexColor);
			ConfigWriter.configwrite("calloutManager", "highPriorityColor", p1HexColor);
			ConfigWriter.configwrite("calloutManager", "highPriorityValue", p1Name);
			String p2Name = p2NameField.getText();
			Color p2Color = p2ColorPicker.getValue();
			String p2HexColor = String.format("#%02x%02x%02x", (int) (p2Color.getRed() * 255),
					(int) (p2Color.getGreen() * 255), (int) (p2Color.getBlue() * 255));
			logInfo("Priority 2: Name='" + p2Name + "', Color=" + p2HexColor);
			ConfigWriter.configwrite("calloutManager", "mediumPriorityColor", p2HexColor);
			ConfigWriter.configwrite("calloutManager", "mediumPriorityValue", p2Name);
			String p3Name = p3NameField.getText();
			Color p3Color = p3ColorPicker.getValue();
			String p3HexColor = String.format("#%02x%02x%02x", (int) (p3Color.getRed() * 255),
					(int) (p3Color.getGreen() * 255), (int) (p3Color.getBlue() * 255));
			logInfo("Priority 3: Name='" + p3Name + "', Color=" + p3HexColor);
			ConfigWriter.configwrite("calloutManager", "lowPriorityColor", p3HexColor);
			ConfigWriter.configwrite("calloutManager", "lowPriorityValue", p3Name);
			String p4Name = p4NameField.getText();
			Color p4Color = p4ColorPicker.getValue();
			String p4HexColor = String.format("#%02x%02x%02x", (int) (p4Color.getRed() * 255),
					(int) (p4Color.getGreen() * 255), (int) (p4Color.getBlue() * 255));
			logInfo("Priority 4: Name='" + p4Name + "', Color=" + p4HexColor);
			ConfigWriter.configwrite("calloutManager", "defaultColor", p4HexColor);
			ConfigWriter.configwrite("calloutManager", "defaultValue", p4Name);
			logInfo("----------------------");
			CalloutManager.loadActiveCallouts(activeCalloutsTable);
			CalloutManager.loadHistoryCallouts(historyCalloutsTable);
		});
		settingsLayout.getChildren().add(saveButton);
		BorderPane settingsWindowRoot = new BorderPane(settingsLayout);
		settingsWindowRoot.setPrefWidth(600);
		settingsWindowRoot.setPrefHeight(Region.USE_COMPUTED_SIZE);
		createCustomWindow(mainDesktopControllerObj.getDesktopContainer(), settingsWindowRoot,
				"Callout Manager Settings", false, 1, true, true, mainDesktopControllerObj.getTaskBarApps(),
				new Image(Objects.requireNonNull(
						Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Apps/setting.png"))));
	}

	@FXML
	public void callInfoDeleteBtnPress(ActionEvent actionEvent) {
		if (inActiveCallout) {
			CalloutManager.deleteCallout(calloutDataURL, selectedCallout.getNumber());
			displayActiveCallsBtn.fire();
		} else {
			CalloutManager.deleteCallout(calloutHistoryURL, selectedCallout.getNumber());
			displayCallHistoryBtn.fire();
		}
	}

	@FXML
	public void onSendNoteClick(ActionEvent actionEvent) {
		String text = callInfoCommentField.getText().trim();
		if (text.isEmpty()) {
			return;
		}
		String sender = "";
		String rank = "";
		try {
			sender = ConfigReader.configRead("userInfo", "Name");
			rank = ConfigReader.configRead("userInfo", "Rank");
		} catch (IOException e) {
			logError("Error loading userInfo.Name or userInfo.Rank from setupActiveCalloutsTable: ", e);
		}
		selectedCallout.addStoredMessage(
				new StoredMessage(rank + " " + sender, getDate(), getTime(false, false), "Message", text));
		if (inActiveCallout) {
			CalloutManager.updateCallout(calloutDataURL, selectedCallout);
		} else {
			CalloutManager.updateCallout(calloutHistoryURL, selectedCallout);
		}
		refreshNotesForCallout(selectedCallout);
	}
}
