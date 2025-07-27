package com.Guess.ReportsPlus.Windows.Apps;

import static com.Guess.ReportsPlus.Desktop.mainDesktopController.newReportWindow;
import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.MainApplication.mainDesktopControllerObj;
import static com.Guess.ReportsPlus.logs.Accident.AccidentReportUtils.newAccident;
import static com.Guess.ReportsPlus.logs.Arrest.ArrestReportUtils.newArrest;
import static com.Guess.ReportsPlus.logs.Callout.CalloutReportUtils.newCallout;
import static com.Guess.ReportsPlus.logs.Death.DeathReportUtils.newDeathReport;
import static com.Guess.ReportsPlus.logs.Impound.ImpoundReportUtils.newImpound;
import static com.Guess.ReportsPlus.logs.Incident.IncidentReportUtils.newIncident;
import static com.Guess.ReportsPlus.logs.Patrol.PatrolReportUtils.newPatrol;
import static com.Guess.ReportsPlus.logs.Search.SearchReportUtils.newSearch;
import static com.Guess.ReportsPlus.logs.TrafficCitation.TrafficCitationUtils.newCitation;
import static com.Guess.ReportsPlus.logs.TrafficStop.TrafficStopReportUtils.newTrafficStop;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logDebug;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logInfo;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logWarn;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationInfo;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationWarning;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.createFolderIfNotExists;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getCustomDataLogsFolderPath;
import static com.Guess.ReportsPlus.util.Report.Database.DynamicDB.getPrimaryKeyColumn;
import static com.Guess.ReportsPlus.util.Report.Database.DynamicDB.isValidDatabase;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager;
import com.Guess.ReportsPlus.Windows.Other.LayoutBuilderController;
import com.Guess.ReportsPlus.util.Report.Database.CustomReport;
import com.Guess.ReportsPlus.util.Report.Database.CustomReportUtilities;
import com.Guess.ReportsPlus.util.Report.Database.DynamicDB;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public class NewReportVewController {
	public static NewReportVewController newReportVewController;
	@FXML
	private BorderPane root;
	@FXML
	private Label customReportsLabel;
	@FXML
	private GridPane customReportsGrid;
	@FXML
	private Button createNewReportBtn;
	@FXML
	private ComboBox<String> legacyReportsComboBox;
	@FXML
	private Button importDefaultBtn;

	public void initialize() throws IOException {
		legacyReportsComboBox.getItems().addAll("Citation Report",
				"Arrest Report", "Accident Report", "Callout Report",
				"Death Report", "Impound Report", "Incident Report", "Patrol Report", "Search Report",
				"Traffic Stop Report");
		legacyReportsComboBox.setOnAction(_ -> {
			String selectedReport = legacyReportsComboBox.getSelectionModel().getSelectedItem();
			if (selectedReport == null)
				return;
			logInfo("NewReport; Selected legacy report: " + selectedReport);
			switch (selectedReport) {
				case "Accident Report":
					newAccident();
					break;
				case "Arrest Report":
					newArrest();
					break;
				case "Callout Report":
					newCallout();
					break;
				case "Death Report":
					newDeathReport();
					break;
				case "Impound Report":
					newImpound();
					break;
				case "Incident Report":
					newIncident();
					break;
				case "Patrol Report":
					newPatrol();
					break;
				case "Search Report":
					newSearch();
					break;
				case "Traffic Stop Report":
					newTrafficStop();
					break;
				case "Citation Report":
					newCitation();
					break;
				default:
					logWarn("NewReport; Unknown legacy report type selected: " + selectedReport);
			}
			closeWindow();
		});
		addLocale();
		refreshCustomReports();
		importDefaultBtn.setOnAction(_ -> {
			logInfo("NewReport; Importing default reports");
			CustomReportUtilities.addDefaultReports();
			refreshCustomReports();
			showNotificationInfo("Default Reports Imported", "Default reports have been successfully imported.");
		});
	}

	@FXML
	public void createReportButton(ActionEvent actionEvent) {
		CustomWindow layoutWindow = WindowManager.createCustomWindow(mainDesktopControllerObj.getDesktopContainer(),
				"Windows/Other/layout-builder-view.fxml", "Layout Builder", true, 1, true, false,
				mainDesktopControllerObj.getTaskBarApps(),
				new Image(Objects.requireNonNull(
						Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/report.png"))));
		LayoutBuilderController.layoutBuilderController = (LayoutBuilderController) (layoutWindow != null
				? layoutWindow.controller
				: null);
		closeWindow();
	}

	public BorderPane getRoot() {
		return root;
	}

	private void refreshCustomReports() {
		customReportsGrid.getChildren().clear();
		customReportsGrid.getRowConstraints().clear();
		customReportsGrid.getColumnConstraints().clear();
		logDebug("NewReport; ---------- Loading Databases ----------");
		try {
			if (!loadDatabaseButtons(getCustomDataLogsFolderPath(), customReportsGrid)) {
				logInfo("NewReport; No Custom Databases Found");
				showNotificationWarning("Report Warning", "No reports found, please create one below");
			}
		} catch (IOException e) {
			logError("Error loading database buttons", e);
		}
		logDebug("NewReport; ---------- Finished Loading Databases ----------");
	}

	private boolean loadDatabaseButtons(String dataFolderPath, GridPane gridPane) throws IOException {
		File folder = new File(dataFolderPath);
		if (!folder.exists() || !folder.isDirectory()) {
			logError("NewReport; Invalid data folder path: " + dataFolderPath);
			return false;
		}
		File[] files = folder.listFiles((dir, name) -> name.endsWith(".db"));
		if (files == null || files.length == 0) {
			logInfo("NewReport; No database files found in: " + dataFolderPath);
			return false;
		}
		logInfo("NewReport; Found " + files.length + " database files in: " + dataFolderPath);
		gridPane.setHgap(20);
		gridPane.setVgap(20);
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setHgrow(Priority.SOMETIMES);
		col1.setPercentWidth(50.0);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setHgrow(Priority.SOMETIMES);
		col2.setPercentWidth(50.0);
		gridPane.getColumnConstraints().addAll(col1, col2);
		int buttonCount = 0;
		for (File dbFile : files) {
			String dbFilePath = dbFile.getAbsolutePath();
			if (!isValidDatabase(dbFilePath, dbFile.getName())) {
				logWarn("NewReport; Invalid or unreadable database file: " + dbFilePath);
				continue;
			}
			logInfo("NewReport; [" + dbFile.getName() + "] Valid database detected, creating button.");
			String buttonText = dbFile.getName().replace(".db", "");
			Button dbButton = new Button(buttonText);
			dbButton.setMaxWidth(Double.MAX_VALUE);
			dbButton.getStyleClass().add("custom-report-button");
			dbButton.setOnAction(e -> {
				logDebug("NewReport; Clicked Database: " + dbFile.getName());
				String reportTitle = dbFile.getName().replace(".db", "").trim();
				createFolderIfNotExists(getCustomDataLogsFolderPath());
				if (reportTitle.isEmpty()) {
					logError("LayoutBuilder; Report Title Field is Empty");
					return;
				}
				String data;
				Map<String, String> layoutScheme;
				Map<String, String> reportSchema;
				try {
					data = getPrimaryKeyColumn(dbFilePath, "data");
					layoutScheme = DynamicDB.getTableColumnsDefinition(dbFilePath, "layout");
					reportSchema = DynamicDB.getTableColumnsDefinition(dbFilePath, "data");
				} catch (SQLException ex) {
					logError("Failed to extract schemas from database: " + dbFile.getName(), ex);
					return;
				}
				new CustomReport(reportTitle, data, layoutScheme, reportSchema, null, null);
				closeWindow();
			});
			Button deleteButton = new Button("X");
			deleteButton.getStyleClass().add("removeButton");
			deleteButton.setStyle("-fx-font-size: 10;");
			deleteButton.setOnAction(_ -> showConfirmDeleteWindow(dbFile, buttonText));
			HBox buttonContainer = new HBox(3, dbButton, deleteButton);
			buttonContainer.setAlignment(Pos.CENTER);
			HBox.setHgrow(dbButton, Priority.ALWAYS);
			int column = buttonCount % 2;
			int row = buttonCount / 2;
			if (column == 0) {
				RowConstraints rowConstraints = new RowConstraints();
				rowConstraints.setMinHeight(Region.USE_COMPUTED_SIZE);
				rowConstraints.setPrefHeight(Region.USE_COMPUTED_SIZE);
				rowConstraints.setVgrow(Priority.NEVER);
				gridPane.getRowConstraints().add(rowConstraints);
			}
			gridPane.add(buttonContainer, column, row);
			GridPane.setHalignment(buttonContainer, HPos.CENTER);
			GridPane.setValignment(buttonContainer, VPos.CENTER);
			buttonCount++;
		}
		return buttonCount > 0;
	}

	private void showConfirmDeleteWindow(File fileToDelete, String reportName) {
		Label message = new Label(localization.getLocalizedMessage("NewReportApp.ConfirmDeletion",
				"Are you sure you want to perform this action?") + " Delete '" + reportName + "'?");
		message.setWrapText(true);
		message.setPrefHeight(Region.USE_COMPUTED_SIZE);
		message.setStyle("-fx-font-family: \"Inter 28pt Bold\"; -fx-font-size: 14;");
		message.setPrefWidth(Region.USE_COMPUTED_SIZE);
		Button yesBtn = new Button(localization.getLocalizedMessage("Callout_Manager.DelCalloutButton", "Delete"));
		yesBtn.setStyle("-fx-background-color:rgb(135, 69, 69); -fx-text-fill: white;");
		Button noBtn = new Button(localization.getLocalizedMessage("NewReportApp.CancelButton", "Cancel"));
		noBtn.setStyle("-fx-background-color:rgb(92, 142, 93); -fx-text-fill: white;");
		HBox buttonBox = new HBox(10, yesBtn, noBtn);
		buttonBox.setAlignment(Pos.CENTER_RIGHT);
		VBox dialogContent = new VBox(20, message, buttonBox);
		dialogContent.setPadding(new Insets(10));
		dialogContent.setStyle("-fx-background-color: #F4F4F4;");
		VBox.setVgrow(buttonBox, Priority.ALWAYS);
		BorderPane layoutPane = new BorderPane();
		layoutPane.setCenter(dialogContent);
		layoutPane.setPrefSize(550, Region.USE_COMPUTED_SIZE);
		CustomWindow confirmDialog = WindowManager.createCustomWindow(mainDesktopControllerObj.getDesktopContainer(),
				layoutPane, "Confirm Deletion", true, 1, true, true,
				mainDesktopControllerObj.getTaskBarApps(),
				new Image(Objects.requireNonNull(
						Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/report.png"))));
		yesBtn.setOnAction(_ -> {
			confirmDialog.closeWindow();
			if (fileToDelete.delete()) {
				logInfo("Successfully deleted report: " + fileToDelete.getName());
				refreshCustomReports();
				showNotificationInfo("Report Deleted", "The report '" + fileToDelete.getName() + "' has been deleted.");
			} else {
				logError("Failed to delete report: " + fileToDelete.getName());
				Alert errorAlert = new Alert(Alert.AlertType.ERROR,
						"Could not delete the report file. It may be in use by another application.");
				errorAlert.showAndWait();
			}
		});
		noBtn.setOnAction(_ -> confirmDialog.closeWindow());
	}

	private void addLocale() {
		customReportsLabel.setText(
				localization.getLocalizedMessage("NewReportApp.selectReportTypeLabel", "Select a Report Type:"));
		createNewReportBtn
				.setText(localization.getLocalizedMessage("NewReportApp.createNewReport", "Create New Report (BETA)"));
		legacyReportsComboBox
				.setPromptText(localization.getLocalizedMessage("LogBrowser.legacyReportsButton", "Legacy Reports"));
	}

	public Label getCustomReportsLabel() {
		return customReportsLabel;
	}

	private void closeWindow() {
		if (newReportWindow != null) {
			newReportWindow.closeWindow();
		}
	}
}