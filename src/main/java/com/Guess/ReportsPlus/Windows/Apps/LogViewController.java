package com.Guess.ReportsPlus.Windows.Apps;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.logs.Accident.AccidentReport;
import com.Guess.ReportsPlus.logs.Accident.AccidentReportUtils;
import com.Guess.ReportsPlus.logs.Accident.AccidentReports;
import com.Guess.ReportsPlus.logs.Arrest.ArrestReport;
import com.Guess.ReportsPlus.logs.Arrest.ArrestReportUtils;
import com.Guess.ReportsPlus.logs.Arrest.ArrestReports;
import com.Guess.ReportsPlus.logs.Callout.CalloutReport;
import com.Guess.ReportsPlus.logs.Callout.CalloutReportUtils;
import com.Guess.ReportsPlus.logs.Callout.CalloutReports;
import com.Guess.ReportsPlus.logs.ChargesData;
import com.Guess.ReportsPlus.logs.CitationsData;
import com.Guess.ReportsPlus.logs.Death.DeathReport;
import com.Guess.ReportsPlus.logs.Death.DeathReportUtils;
import com.Guess.ReportsPlus.logs.Death.DeathReports;
import com.Guess.ReportsPlus.logs.Impound.ImpoundReport;
import com.Guess.ReportsPlus.logs.Impound.ImpoundReportUtils;
import com.Guess.ReportsPlus.logs.Impound.ImpoundReports;
import com.Guess.ReportsPlus.logs.Incident.IncidentReport;
import com.Guess.ReportsPlus.logs.Incident.IncidentReportUtils;
import com.Guess.ReportsPlus.logs.Incident.IncidentReports;
import com.Guess.ReportsPlus.logs.Patrol.PatrolReport;
import com.Guess.ReportsPlus.logs.Patrol.PatrolReportUtils;
import com.Guess.ReportsPlus.logs.Patrol.PatrolReports;
import com.Guess.ReportsPlus.logs.Search.SearchReport;
import com.Guess.ReportsPlus.logs.Search.SearchReportUtils;
import com.Guess.ReportsPlus.logs.Search.SearchReports;
import com.Guess.ReportsPlus.logs.TrafficCitation.TrafficCitationReport;
import com.Guess.ReportsPlus.logs.TrafficCitation.TrafficCitationReports;
import com.Guess.ReportsPlus.logs.TrafficCitation.TrafficCitationUtils;
import com.Guess.ReportsPlus.logs.TrafficStop.TrafficStopReport;
import com.Guess.ReportsPlus.logs.TrafficStop.TrafficStopReportUtils;
import com.Guess.ReportsPlus.logs.TrafficStop.TrafficStopReports;
import jakarta.xml.bind.JAXBException;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager.getWindow;
import static com.Guess.ReportsPlus.Launcher.localization;
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
import static com.Guess.ReportsPlus.util.Misc.AudioUtil.playSound;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationInfo;
import static com.Guess.ReportsPlus.util.Other.InitTableColumns.*;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getJarPath;
import static com.Guess.ReportsPlus.util.Report.treeViewUtils.addChargesToTable;
import static com.Guess.ReportsPlus.util.Report.treeViewUtils.addCitationsToTable;

public class LogViewController {
	public static LogViewController logController;
	public static SimpleIntegerProperty needRefresh = new SimpleIntegerProperty();
	List<BorderPane> sideButtons = new ArrayList<>();
	private BorderPane activePane;
	
	@javafx.fxml.FXML
	private AnchorPane logPane;
	@javafx.fxml.FXML
	private TableView trafficStopTable;
	@javafx.fxml.FXML
	private TableView arrestTable;
	@javafx.fxml.FXML
	private TableView deathReportTable;
	@javafx.fxml.FXML
	private TableView impoundTable;
	@javafx.fxml.FXML
	private BorderPane root;
	@javafx.fxml.FXML
	private TableView citationTable;
	@javafx.fxml.FXML
	private TableView calloutTable;
	@javafx.fxml.FXML
	private TableView patrolTable;
	@javafx.fxml.FXML
	private TableView accidentReportTable;
	@javafx.fxml.FXML
	private TableView incidentTable;
	@javafx.fxml.FXML
	private TableView searchTable;
	@javafx.fxml.FXML
	private Label reportsInProgressLabel;
	@javafx.fxml.FXML
	private Label totalReportsHeader;
	@javafx.fxml.FXML
	private Label totalReportsLabel;
	@javafx.fxml.FXML
	private ProgressBar closedReportsProgressBar;
	@javafx.fxml.FXML
	private Label closedReportsHeader;
	@javafx.fxml.FXML
	private Label reportsInProgressHeader;
	@javafx.fxml.FXML
	private Label closedReportsLabel;
	@javafx.fxml.FXML
	private BorderPane callout;
	@javafx.fxml.FXML
	private BorderPane arrest;
	@javafx.fxml.FXML
	private BorderPane death;
	@javafx.fxml.FXML
	private BorderPane accident;
	@javafx.fxml.FXML
	private BorderPane search;
	@javafx.fxml.FXML
	private BorderPane patrol;
	@javafx.fxml.FXML
	private BorderPane trafficStop;
	@javafx.fxml.FXML
	private BorderPane citation;
	@javafx.fxml.FXML
	private BorderPane impound;
	@javafx.fxml.FXML
	private BorderPane incident;
	@javafx.fxml.FXML
	private StackPane tableStackPane;
	@javafx.fxml.FXML
	private ProgressBar reportsInProgressBar;
	@javafx.fxml.FXML
	private Label subHeading;
	
	public void initialize() {
		sideButtons.addAll(Arrays.asList(callout, arrest, death, accident, search, patrol, trafficStop, citation, impound, incident));
		
		initializeCalloutColumns(calloutTable);
		initializeArrestColumns(arrestTable);
		initializeCitationColumns(citationTable);
		initializeImpoundColumns(impoundTable);
		initializeIncidentColumns(incidentTable);
		initializePatrolColumns(patrolTable);
		initializeSearchColumns(searchTable);
		initializeTrafficStopColumns(trafficStopTable);
		initializeDeathReportColumns(deathReportTable);
		initializeAccidentColumns(accidentReportTable);
		loadLogs();
		
		needRefresh.set(0);
		needRefresh.addListener((obs, oldValue, newValue) -> {
			if (newValue.equals(1)) {
				loadLogs();
				needRefresh.set(0);
			}
		});
		
		addHoverListener(calloutTable, callout);
		addHoverListener(arrestTable, arrest);
		addHoverListener(deathReportTable, death);
		addHoverListener(accidentReportTable, accident);
		addHoverListener(searchTable, search);
		addHoverListener(patrolTable, patrol);
		addHoverListener(trafficStopTable, trafficStop);
		addHoverListener(citationTable, citation);
		addHoverListener(impoundTable, impound);
		addHoverListener(incidentTable, incident);
		
		Platform.runLater(() -> {
			List<Node> nodesToRemove = new ArrayList<>();
			for (Node table : tableStackPane.getChildren()) {
				if (table instanceof TableView) {
					nodesToRemove.add(table);
				}
			}
			
			setActive(calloutTable, callout);
			
			for (BorderPane otherPane : sideButtons) {
				otherPane.setStyle("-fx-background-color: transparent;");
				for (Node node : otherPane.getChildren()) {
					if (node instanceof Label) {
						node.setStyle("-fx-font-family: \"Inter 28pt Medium\";");
					}
				}
			}
			
			activePane = callout;
			callout.setStyle("-fx-background-color: rgb(0,0,0,0.1); -fx-background-radius: 7 0 0 7;");
			for (Node node : callout.getChildren()) {
				if (node instanceof Label) {
					node.setStyle("-fx-font-family: \"Inter 28pt Bold\";");
				}
			}
		});
		
		subHeading.setText(localization.getLocalizedMessage("LogBrowser.reportDatabaseLabel", "Report Database"));
	}
	
	private void loadLogs() {
		try {
			ImpoundReports impoundReports = ImpoundReportUtils.loadImpoundReports();
			List<ImpoundReport> impoundReportslist = impoundReports.getImpoundReportList();
			impoundLogUpdate(impoundReportslist);
		} catch (JAXBException e) {
			logError("Error loading ImpoundReports: ", e);
		}
		
		try {
			TrafficCitationReports trafficCitationReports = TrafficCitationUtils.loadTrafficCitationReports();
			List<TrafficCitationReport> trafficCitationList = trafficCitationReports.getTrafficCitationReportList();
			citationLogUpdate(trafficCitationList);
		} catch (JAXBException e) {
			logError("Error loading TrafficCitationReports: ", e);
		}
		
		try {
			PatrolReports patrolReports = PatrolReportUtils.loadPatrolReports();
			List<PatrolReport> patrolReportList = patrolReports.getPatrolReportList();
			patrolLogUpdate(patrolReportList);
		} catch (JAXBException e) {
			logError("Error loading PatrolReports: ", e);
		}
		
		try {
			ArrestReports arrestReports = ArrestReportUtils.loadArrestReports();
			List<ArrestReport> arrestReportList = arrestReports.getArrestReportList();
			arrestLogUpdate(arrestReportList);
		} catch (JAXBException e) {
			logError("Error loading ArrestReport: ", e);
		}
		
		try {
			SearchReports searchReports = SearchReportUtils.loadSearchReports();
			List<SearchReport> searchReportsList1 = searchReports.getSearchReportList();
			searchLogUpdate(searchReportsList1);
		} catch (JAXBException e) {
			logError("Error loading SearchReports: ", e);
		}
		
		try {
			IncidentReports incidentReports = IncidentReportUtils.loadIncidentReports();
			List<IncidentReport> incidentReportList1 = incidentReports.getIncidentReportList();
			incidentLogUpdate(incidentReportList1);
		} catch (JAXBException e) {
			logError("Error loading IncidentReports: ", e);
		}
		
		try {
			TrafficStopReports trafficStopReports = TrafficStopReportUtils.loadTrafficStopReports();
			List<TrafficStopReport> trafficStopReportList = trafficStopReports.getTrafficStopReportList();
			trafficStopLogUpdate(trafficStopReportList);
		} catch (JAXBException e) {
			logError("Error loading TrafficStopReports: ", e);
		}
		
		try {
			CalloutReports calloutReports = CalloutReportUtils.loadCalloutReports();
			List<CalloutReport> calloutReportListl = calloutReports.getCalloutReportList();
			calloutLogUpdate(calloutReportListl);
		} catch (JAXBException e) {
			logError("Error loading CalloutReports: ", e);
		}
		
		try {
			DeathReports deathReports = DeathReportUtils.loadDeathReports();
			List<DeathReport> deathReportList = deathReports.getDeathReportList();
			deathReportUpdate(deathReportList);
		} catch (JAXBException e) {
			logError("Error loading DeathReports: ", e);
		}
		
		try {
			AccidentReports accidentReports = AccidentReportUtils.loadAccidentReports();
			List<AccidentReport> accidentReportsList = accidentReports.getAccidentReportList();
			accidentReportUpdate(accidentReportsList);
		} catch (JAXBException e) {
			logError("Error loading accidentReports: ", e);
		}
	}
	
	public void calloutLogUpdate(List<CalloutReport> logEntries) {
		if (logEntries == null) {
			logEntries = new ArrayList<>();
		}
		calloutTable.getItems().clear();
		calloutTable.getItems().addAll(logEntries);
	}
	
	public void citationLogUpdate(List<TrafficCitationReport> logEntries) {
		if (logEntries == null) {
			logEntries = new ArrayList<>();
		}
		citationTable.getItems().clear();
		citationTable.getItems().addAll(logEntries);
	}
	
	public void patrolLogUpdate(List<PatrolReport> logEntries) {
		if (logEntries == null) {
			logEntries = new ArrayList<>();
		}
		patrolTable.getItems().clear();
		patrolTable.getItems().addAll(logEntries);
	}
	
	public void arrestLogUpdate(List<ArrestReport> logEntries) {
		if (logEntries == null) {
			logEntries = new ArrayList<>();
		}
		arrestTable.getItems().clear();
		arrestTable.getItems().addAll(logEntries);
	}
	
	public void searchLogUpdate(List<SearchReport> logEntries) {
		if (logEntries == null) {
			logEntries = new ArrayList<>();
		}
		searchTable.getItems().clear();
		searchTable.getItems().addAll(logEntries);
	}
	
	public void incidentLogUpdate(List<IncidentReport> logEntries) {
		if (logEntries == null) {
			logEntries = new ArrayList<>();
		}
		incidentTable.getItems().clear();
		incidentTable.getItems().addAll(logEntries);
	}
	
	public void trafficStopLogUpdate(List<TrafficStopReport> logEntries) {
		if (logEntries == null) {
			logEntries = new ArrayList<>();
		}
		trafficStopTable.getItems().clear();
		trafficStopTable.getItems().addAll(logEntries);
	}
	
	public void impoundLogUpdate(List<ImpoundReport> logEntries) {
		if (logEntries == null) {
			logEntries = new ArrayList<>();
		}
		impoundTable.getItems().clear();
		impoundTable.getItems().addAll(logEntries);
	}
	
	public void deathReportUpdate(List<DeathReport> logEntries) {
		if (logEntries == null) {
			logEntries = new ArrayList<>();
		}
		
		deathReportTable.getItems().clear();
		deathReportTable.getItems().addAll(logEntries);
	}
	
	public void accidentReportUpdate(List<AccidentReport> logEntries) {
		if (logEntries == null) {
			logEntries = new ArrayList<>();
		}
		
		accidentReportTable.getItems().clear();
		accidentReportTable.getItems().addAll(logEntries);
	}
	
	private void addHoverListener(TableView table, BorderPane pane) {
		pane.setOnMouseEntered(event -> {
			if (activePane == pane) {
				return;
			}
			pane.setStyle("-fx-background-color: rgb(0,0,0,0.05); -fx-background-radius: 7 0 0 7;");
			for (Node node : pane.getChildren()) {
				if (node instanceof Label) {
					node.setStyle("-fx-font-family: \"Inter 28pt Bold\";");
				}
			}
		});
		
		pane.setOnMouseExited(event -> {
			if (activePane == pane) {
				return;
			}
			pane.setStyle("-fx-background-color: transparent;");
			for (Node node : pane.getChildren()) {
				if (node instanceof Label) {
					node.setStyle("-fx-font-family: \"Inter 28pt Medium\";");
				}
			}
		});
		
		pane.setOnMouseClicked(event -> {
			
			if (activePane == pane) {
				return;
			}
			setActive(table, pane);
			
			for (BorderPane otherPane : sideButtons) {
				otherPane.setStyle("-fx-background-color: transparent;");
				for (Node node : otherPane.getChildren()) {
					if (node instanceof Label) {
						node.setStyle("-fx-font-family: \"Inter 28pt Medium\";");
					}
				}
			}
			
			activePane = pane;
			pane.setStyle("-fx-background-color: rgb(0,0,0,0.1); -fx-background-radius: 7 0 0 7;");
			for (Node node : pane.getChildren()) {
				if (node instanceof Label) {
					node.setStyle("-fx-font-family: \"Inter 28pt Bold\";");
				}
			}
		});
	}
	
	@javafx.fxml.FXML
	public void onDeathReportRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			DeathReport deathReport = (DeathReport) deathReportTable.getSelectionModel().getSelectedItem();
			
			if (deathReport != null) {
				Map<String, Object> deathReportObj = newDeathReport();
				
				Map<String, Object> deathReport1 = (Map<String, Object>) deathReportObj.get(localization.getLocalizedMessage("ReportWindows.DeathReportTitle", "Death Report") + " Map");
				
				TextField name = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
				TextField rank = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
				TextField div = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
				TextField agen = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
				TextField num = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));
				TextField date = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
				TextField time = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
				ComboBox street = (ComboBox) deathReport1.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
				ComboBox area = (ComboBox) deathReport1.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
				TextField county = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
				TextField deathNum = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.DeathNumField", "death num"));
				TextField decedent = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.DeathDecedentField", "decedent name"));
				TextField age = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.DeathAgeDOBField", "age/dob"));
				TextField gender = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.DeathGenderField", "gender"));
				TextField address = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.DeathReportAddressField", "address"));
				TextField description = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.DeathDescField", "description"));
				TextField causeofdeath = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.CauseOfDeathField", "cause of death"));
				TextField modeofdeath = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.ModeOfDeathField", "mode of death"));
				TextField witnesses = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.FieldWitnesses", "witnesses"));
				TextArea notes = (TextArea) deathReport1.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"));
				TextField timeofdeath = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.TimeOfDeathField", "time of death"));
				TextField dateofdeath = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.DateOfDeathField", "date of death"));
				
				timeofdeath.setText(deathReport.getTimeOfDeath());
				dateofdeath.setText(deathReport.getDateOfDeath());
				name.setText(deathReport.getOfficerName());
				rank.setText(deathReport.getRank());
				div.setText(deathReport.getDivision());
				agen.setText(deathReport.getAgency());
				num.setText(deathReport.getNumber());
				date.setText(deathReport.getDate());
				time.setText(deathReport.getTime());
				street.getEditor().setText(deathReport.getStreet());
				area.setValue(deathReport.getArea());
				county.setText(deathReport.getCounty());
				deathNum.setText(deathReport.getDeathReportNumber());
				decedent.setText(deathReport.getDecedent());
				age.setText(deathReport.getAge());
				gender.setText(deathReport.getGender());
				address.setText(deathReport.getAddress());
				description.setText(deathReport.getDescription());
				causeofdeath.setText(deathReport.getCauseOfDeath());
				modeofdeath.setText(deathReport.getModeOfDeath());
				witnesses.setText(deathReport.getWitnesses());
				notes.setText(deathReport.getNotesTextArea());
				
				Button delBtn = (Button) deathReportObj.get("delBtn");
				delBtn.setVisible(true);
				delBtn.setDisable(false);
				delBtn.setOnAction(actionEvent -> {
					String numToDelete = deathNum.getText();
					try {
						DeathReportUtils.deleteDeathReport(numToDelete);
						showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete);
					} catch (JAXBException e) {
						logError("Could not delete DeathReport #" + numToDelete + ": ", e);
					}
					CustomWindow window = getWindow("Death Report");
					if (window != null) {
						window.closeWindow();
					}
					try {
						if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
							playSound(getJarPath() + "/sounds/alert-delete.wav");
						}
					} catch (IOException e) {
						logError("Error getting configValue for playDeleteReport: ", e);
					}
					needRefresh.set(1);
				});
				
				deathNum.setEditable(false);
				MenuButton pullnotesbtn = (MenuButton) deathReportObj.get("pullNotesBtn");
				pullnotesbtn.setVisible(false);
				
				ComboBox<String> statusValue = (ComboBox<String>) deathReportObj.get("statusValue");
				statusValue.setValue("Reopened");
				
				Button submitBtn = (Button) deathReportObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				deathReportTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	@javafx.fxml.FXML
	public void onCalloutRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			CalloutReport calloutReport = (CalloutReport) calloutTable.getSelectionModel().getSelectedItem();
			
			if (calloutReport != null) {
				
				Map<String, Object> calloutReportObj = newCallout();
				
				Map<String, Object> calloutReportMap = (Map<String, Object>) calloutReportObj.get(localization.getLocalizedMessage("ReportWindows.CalloutReportTitle", "Callout Report") + " Map");
				
				TextField officername = (TextField) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
				TextField officerrank = (TextField) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
				TextField officerdiv = (TextField) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
				TextField officeragen = (TextField) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
				TextField officernum = (TextField) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));
				TextField calloutnum = (TextField) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.CalloutNumberField", "callout num"));
				ComboBox calloutarea = (ComboBox) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
				TextArea calloutnotes = (TextArea) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"));
				TextField calloutcounty = (TextField) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
				ComboBox calloutstreet = (ComboBox) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
				TextField calloutdate = (TextField) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
				TextField callouttime = (TextField) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
				TextField callouttype = (TextField) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldType", localization.getLocalizedMessage("ReportWindows.FieldType", "type")));
				TextField calloutcode = (TextField) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.CalloutCodeField", "code"));
				
				officername.setText(calloutReport.getOfficerName());
				officerrank.setText(calloutReport.getRank());
				officerdiv.setText(calloutReport.getDivision());
				officeragen.setText(calloutReport.getAgency());
				officernum.setText(calloutReport.getNumber());
				calloutdate.setText(calloutReport.getDate());
				callouttime.setText(calloutReport.getTime());
				calloutstreet.setValue(calloutReport.getAddress());
				calloutarea.setValue(calloutReport.getArea());
				calloutcounty.setText(calloutReport.getCounty());
				calloutnotes.setText(calloutReport.getNotesTextArea());
				calloutnum.setText(calloutReport.getCalloutNumber());
				callouttype.setText(calloutReport.getResponseType());
				calloutcode.setText(calloutReport.getResponseGrade());
				
				ComboBox<String> statusValue = (ComboBox<String>) calloutReportObj.get("statusValue");
				statusValue.setValue("Reopened");
				
				Button delBtn = (Button) calloutReportObj.get("delBtn");
				delBtn.setVisible(true);
				delBtn.setDisable(false);
				delBtn.setOnAction(actionEvent -> {
					String numToDelete = calloutnum.getText();
					try {
						CalloutReportUtils.deleteCalloutReport(numToDelete);
						showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete);
					} catch (JAXBException e) {
						logError("Could not delete CalloutReport #" + numToDelete + ": ", e);
					}
					CustomWindow window = getWindow("Callout Report");
					if (window != null) {
						window.closeWindow();
					}
					try {
						if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
							playSound(getJarPath() + "/sounds/alert-delete.wav");
						}
					} catch (IOException e) {
						logError("Error getting configValue for playDeleteReport: ", e);
					}
					needRefresh.set(1);
				});
				
				MenuButton pullnotesbtn = (MenuButton) calloutReportObj.get("pullNotesBtn");
				pullnotesbtn.setVisible(false);
				calloutnum.setEditable(false);
				
				Button submitBtn = (Button) calloutReportObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				calloutTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	@javafx.fxml.FXML
	public void onPatrolRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			PatrolReport patrolReport = (PatrolReport) patrolTable.getSelectionModel().getSelectedItem();
			
			if (patrolReport != null) {
				
				Map<String, Object> patrolReportObj = newPatrol();
				
				Map<String, Object> patrolReportMap = (Map<String, Object>) patrolReportObj.get(localization.getLocalizedMessage("ReportWindows.PatrolReportTitle", "Patrol Report") + " Map");
				
				TextField name = (TextField) patrolReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
				TextField rank = (TextField) patrolReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
				TextField div = (TextField) patrolReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
				TextField agen = (TextField) patrolReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
				TextField num = (TextField) patrolReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));
				TextField patrolnum = (TextField) patrolReportMap.get(localization.getLocalizedMessage("ReportWindows.PatrolNumField", "patrolnumber"));
				TextArea notes = (TextArea) patrolReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"));
				TextField date = (TextField) patrolReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
				TextField starttime = (TextField) patrolReportMap.get(localization.getLocalizedMessage("ReportWindows.StartTimeField", "starttime"));
				TextField stoptime = (TextField) patrolReportMap.get(localization.getLocalizedMessage("ReportWindows.StopTimeField", "stoptime"));
				TextField length = (TextField) patrolReportMap.get(localization.getLocalizedMessage("ReportWindows.LengthField", "length"));
				TextField vehicle = (TextField) patrolReportMap.get(localization.getLocalizedMessage("ReportWindows.VehicleField", "vehicle"));
				
				name.setText(patrolReport.getOfficerName());
				patrolnum.setText(patrolReport.getPatrolNumber());
				rank.setText(patrolReport.getOfficerRank());
				div.setText(patrolReport.getOfficerDivision());
				agen.setText(patrolReport.getOfficerAgency());
				num.setText(patrolReport.getOfficerNumber());
				date.setText(patrolReport.getPatrolDate());
				starttime.setText(patrolReport.getPatrolStartTime());
				stoptime.setText(patrolReport.getPatrolStopTime());
				length.setText(patrolReport.getPatrolLength());
				vehicle.setText(patrolReport.getOfficerVehicle());
				notes.setText(patrolReport.getPatrolComments());
				
				BorderPane root = (BorderPane) patrolReportObj.get("root");
				Button delBtn = (Button) patrolReportObj.get("delBtn");
				delBtn.setVisible(true);
				delBtn.setDisable(false);
				delBtn.setOnAction(actionEvent -> {
					String numToDelete = patrolnum.getText();
					try {
						PatrolReportUtils.deletePatrolReport(numToDelete);
						showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete);
					} catch (JAXBException e) {
						logError("Could not delete PatrolReport #" + numToDelete + ": ", e);
					}
					CustomWindow window = getWindow("Patrol Report");
					if (window != null) {
						window.closeWindow();
					}
					try {
						if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
							playSound(getJarPath() + "/sounds/alert-delete.wav");
						}
					} catch (IOException e) {
						logError("Error getting configValue for playDeleteReport: ", e);
					}
					needRefresh.set(1);
				});
				
				MenuButton pullnotesbtn = (MenuButton) patrolReportObj.get("pullNotesBtn");
				pullnotesbtn.setVisible(false);
				patrolnum.setEditable(false);
				
				ComboBox<String> statusValue = (ComboBox<String>) patrolReportObj.get("statusValue");
				statusValue.setValue("Reopened");
				
				Button submitBtn = (Button) patrolReportObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				patrolTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	@javafx.fxml.FXML
	public void onTrafficStopRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			TrafficStopReport trafficStopReport = (TrafficStopReport) trafficStopTable.getSelectionModel().getSelectedItem();
			
			if (trafficStopReport != null) {
				
				Map<String, Object> trafficStopReportObj = newTrafficStop();
				
				Map<String, Object> trafficStopReportMap = (Map<String, Object>) trafficStopReportObj.get(localization.getLocalizedMessage("ReportWindows.TrafficStopReportTitle", "Traffic Stop Report") + " Map");
				
				TextField officernamets = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
				TextField officerrankts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
				TextField officerdivts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
				TextField officeragents = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
				TextField officernumarrestts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));
				
				TextField offenderNamets = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
				TextField offenderAgets = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"));
				TextField offenderGenderts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"));
				TextField offenderAddressts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"));
				TextField offenderDescriptionts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderDescription", "offender description"));
				
				ComboBox colorts = (ComboBox) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldColor", "color"));
				ComboBox typets = (ComboBox) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldType", localization.getLocalizedMessage("ReportWindows.FieldType", "type")));
				TextField plateNumberts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldPlateNumber", "plate number"));
				TextField otherInfots = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.OtherInfoField", "other info"));
				TextField modelts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldModel", "model"));
				
				ComboBox areats = (ComboBox) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
				ComboBox streetts = (ComboBox) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
				TextField countyts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
				TextField stopnumts = (TextField) trafficStopReportMap.get("stop number");
				TextField datets = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
				TextField timets = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
				
				TextArea notests = (TextArea) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"));
				
				stopnumts.setText(trafficStopReport.getStopNumber());
				datets.setText(trafficStopReport.getDate());
				timets.setText(trafficStopReport.getTime());
				officerrankts.setText(trafficStopReport.getRank());
				notests.setText(trafficStopReport.getCommentsTextArea());
				plateNumberts.setText(trafficStopReport.getPlateNumber());
				
				offenderDescriptionts.setText(trafficStopReport.getOperatorDescription());
				otherInfots.setText(trafficStopReport.getResponseOtherInfo());
				areats.setValue(trafficStopReport.getArea());
				streetts.getEditor().setText(trafficStopReport.getStreet());
				countyts.setText(trafficStopReport.getCounty());
				
				offenderNamets.setText(trafficStopReport.getOperatorName());
				officernamets.setText(trafficStopReport.getOfficerName());
				officerdivts.setText(trafficStopReport.getDivision());
				officeragents.setText(trafficStopReport.getAgency());
				officernumarrestts.setText(trafficStopReport.getNumber());
				offenderAgets.setText(trafficStopReport.getOperatorAge());
				offenderGenderts.setText(trafficStopReport.getOperatorGender());
				offenderAddressts.setText(trafficStopReport.getOperatorAddress());
				colorts.setValue(trafficStopReport.getColor());
				typets.setValue(trafficStopReport.getType());
				modelts.setText(trafficStopReport.getResponseModel());
				
				BorderPane root = (BorderPane) trafficStopReportObj.get("root");
				Button delBtn = (Button) trafficStopReportObj.get("delBtn");
				delBtn.setVisible(true);
				delBtn.setDisable(false);
				delBtn.setOnAction(actionEvent -> {
					String numToDelete = stopnumts.getText();
					try {
						TrafficStopReportUtils.deleteTrafficStopReport(numToDelete);
						showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete);
					} catch (JAXBException e) {
						logError("Could not delete TrafficStopReport #" + numToDelete + ": ", e);
					}
					CustomWindow window = getWindow("Traffic Stop Report");
					if (window != null) {
						window.closeWindow();
					}
					try {
						if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
							playSound(getJarPath() + "/sounds/alert-delete.wav");
						}
					} catch (IOException e) {
						logError("Error getting configValue for playDeleteReport: ", e);
					}
					needRefresh.set(1);
					
				});
				
				MenuButton pullnotesbtn = (MenuButton) trafficStopReportObj.get("pullNotesBtn");
				pullnotesbtn.setVisible(false);
				stopnumts.setEditable(false);
				
				ComboBox<String> statusValue = (ComboBox<String>) trafficStopReportObj.get("statusValue");
				statusValue.setValue("Reopened");
				
				Button submitBtn = (Button) trafficStopReportObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				trafficStopTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	@javafx.fxml.FXML
	public void onIncidentRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			IncidentReport incidentReport = (IncidentReport) incidentTable.getSelectionModel().getSelectedItem();
			
			if (incidentReport != null) {
				
				Map<String, Object> incidentReportObj = newIncident();
				
				Map<String, Object> incidentReportMap = (Map<String, Object>) incidentReportObj.get(localization.getLocalizedMessage("ReportWindows.IncidentReportTitle", "Incident Report") + " Map");
				
				TextField name = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
				TextField rank = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
				TextField div = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
				TextField agen = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
				TextField num = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));
				
				TextField incidentnum = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.IncidentNumField", "incident num"));
				TextField date = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
				TextField time = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
				ComboBox street = (ComboBox) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
				ComboBox area = (ComboBox) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
				TextField county = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
				
				TextField suspects = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.SuspectsField", "suspect(s)"));
				TextField vicwit = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.VictimsWitnessField", "victim(s) / witness(s)"));
				TextArea statement = (TextArea) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.StatementField", "statement"));
				
				TextArea summary = (TextArea) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.IncidentSummaryField", "summary"));
				TextArea notes = (TextArea) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"));
				
				name.setText(incidentReport.getOfficerName());
				incidentnum.setText(incidentReport.getIncidentNumber());
				rank.setText(incidentReport.getOfficerRank());
				div.setText(incidentReport.getOfficerDivision());
				agen.setText(incidentReport.getOfficerAgency());
				num.setText(incidentReport.getOfficerNumber());
				
				street.getEditor().setText(incidentReport.getIncidentStreet());
				area.setValue(incidentReport.getArea());
				county.setText(incidentReport.getCounty());
				suspects.setText(incidentReport.getIncidentWitnesses());
				vicwit.setText(incidentReport.getIncidentVictims());
				statement.setText(incidentReport.getIncidentStatement());
				
				date.setText(incidentReport.getIncidentDate());
				time.setText(incidentReport.getIncidentTime());
				summary.setText(incidentReport.getIncidentActionsTaken());
				notes.setText(incidentReport.getIncidentComments());
				
				Button delBtn = (Button) incidentReportObj.get("delBtn");
				delBtn.setVisible(true);
				delBtn.setDisable(false);
				delBtn.setOnAction(actionEvent -> {
					String numToDelete = incidentnum.getText();
					try {
						IncidentReportUtils.deleteIncidentReport(numToDelete);
						showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete);
					} catch (JAXBException e) {
						logError("Could not delete IncidentReport #" + numToDelete + ": ", e);
					}
					CustomWindow window = getWindow("Incident Report");
					if (window != null) {
						window.closeWindow();
					}
					try {
						if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
							playSound(getJarPath() + "/sounds/alert-delete.wav");
						}
					} catch (IOException e) {
						logError("Error getting configValue for playDeleteReport: ", e);
					}
					needRefresh.set(1);
					
				});
				
				MenuButton pullnotesbtn = (MenuButton) incidentReportObj.get("pullNotesBtn");
				pullnotesbtn.setVisible(false);
				incidentnum.setEditable(false);
				
				ComboBox<String> statusValue = (ComboBox<String>) incidentReportObj.get("statusValue");
				statusValue.setValue("Reopened");
				
				Button submitBtn = (Button) incidentReportObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				incidentTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	@javafx.fxml.FXML
	public void onImpoundRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			ImpoundReport impoundReport = (ImpoundReport) impoundTable.getSelectionModel().getSelectedItem();
			
			if (impoundReport != null) {
				
				Map<String, Object> impoundReportObj = newImpound();
				
				Map<String, Object> impoundReportMap = (Map<String, Object>) impoundReportObj.get(localization.getLocalizedMessage("ReportWindows.ImpoundReportTitle", "Impound Report") + " Map");
				
				TextField officername = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
				TextField officerrank = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
				TextField officerdiv = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
				TextField officeragen = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
				TextField officernum = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));
				
				TextField offenderName = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
				TextField offenderAge = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"));
				TextField offenderGender = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"));
				TextField offenderAddress = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"));
				
				TextField num = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.ImpoundNumField", "impound number"));
				TextField date = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
				TextField time = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
				
				ComboBox color = (ComboBox) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldColor", "color"));
				ComboBox type = (ComboBox) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldType", localization.getLocalizedMessage("ReportWindows.FieldType", "type")));
				TextField plateNumber = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldPlateNumber", "plate number"));
				TextField model = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldModel", "model"));
				
				TextArea notes = (TextArea) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"));
				
				num.setText(impoundReport.getImpoundNumber());
				date.setText(impoundReport.getImpoundDate());
				time.setText(impoundReport.getImpoundTime());
				officerrank.setText(impoundReport.getOfficerRank());
				notes.setText(impoundReport.getImpoundComments());
				plateNumber.setText(impoundReport.getImpoundPlateNumber());
				
				offenderName.setText(impoundReport.getOwnerName());
				officername.setText(impoundReport.getOfficerName());
				officerdiv.setText(impoundReport.getOfficerDivision());
				officeragen.setText(impoundReport.getOfficerAgency());
				officernum.setText(impoundReport.getOfficerNumber());
				offenderAge.setText(impoundReport.getOwnerAge());
				offenderGender.setText(impoundReport.getOwnerGender());
				offenderAddress.setText(impoundReport.getOwnerAddress());
				color.setValue(impoundReport.getImpoundColor());
				type.setValue(impoundReport.getImpoundType());
				model.setText(impoundReport.getImpoundModel());
				
				BorderPane root = (BorderPane) impoundReportObj.get("root");
				Button delBtn = (Button) impoundReportObj.get("delBtn");
				delBtn.setVisible(true);
				delBtn.setDisable(false);
				delBtn.setOnAction(actionEvent -> {
					String numToDelete = num.getText();
					try {
						ImpoundReportUtils.deleteImpoundReport(numToDelete);
						showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete);
					} catch (JAXBException e) {
						logError("Could not delete ImpoundReport #" + numToDelete + ": ", e);
					}
					CustomWindow window = getWindow("Impound Report");
					if (window != null) {
						window.closeWindow();
					}
					try {
						if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
							playSound(getJarPath() + "/sounds/alert-delete.wav");
						}
					} catch (IOException e) {
						logError("Error getting configValue for playDeleteReport: ", e);
					}
					needRefresh.set(1);
					
				});
				
				MenuButton pullnotesbtn = (MenuButton) impoundReportObj.get("pullNotesBtn");
				pullnotesbtn.setVisible(false);
				num.setEditable(false);
				
				ComboBox<String> statusValue = (ComboBox<String>) impoundReportObj.get("statusValue");
				statusValue.setValue("Reopened");
				
				Button submitBtn = (Button) impoundReportObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				impoundTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	@javafx.fxml.FXML
	public void onCitationRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			TrafficCitationReport trafficCitationReport = (TrafficCitationReport) citationTable.getSelectionModel().getSelectedItem();
			
			if (trafficCitationReport != null) {
				Map<String, Object> trafficCitationObj = newCitation();
				
				Map<String, Object> citationReportMap = (Map<String, Object>) trafficCitationObj.get(localization.getLocalizedMessage("ReportWindows.CitationReportTitle", "Citation Report") + " Map");
				
				TextField officername = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
				TextField officerrank = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
				TextField officerdiv = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
				TextField officeragen = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
				TextField officernum = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));
				
				TextField offenderName = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
				TextField offenderAge = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"));
				TextField offenderGender = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"));
				TextField offenderAddress = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"));
				TextField offenderDescription = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderDescription", "offender description"));
				
				ComboBox area = (ComboBox) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
				ComboBox street = (ComboBox) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
				TextField county = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
				TextField num = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.CitationNumField", "citation number"));
				TextField date = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
				TextField time = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
				
				ComboBox color = (ComboBox) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldColor", "color"));
				ComboBox type = (ComboBox) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldType", localization.getLocalizedMessage("ReportWindows.FieldType", "type")));
				TextField plateNumber = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldPlateNumber", "plate number"));
				TextField otherInfo = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.OtherInfoField", "other info"));
				TextField model = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldModel", "model"));
				
				TextArea notes = (TextArea) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"));
				
				officername.setText(trafficCitationReport.getOfficerName());
				officerrank.setText(trafficCitationReport.getOfficerRank());
				officerdiv.setText(trafficCitationReport.getOfficerDivision());
				officeragen.setText(trafficCitationReport.getOfficerAgency());
				officernum.setText(trafficCitationReport.getOfficerNumber());
				street.setValue(trafficCitationReport.getCitationStreet());
				area.setValue(trafficCitationReport.getArea());
				county.setText(trafficCitationReport.getCounty());
				type.setValue(trafficCitationReport.getOffenderVehicleType());
				color.setValue(trafficCitationReport.getOffenderVehicleColor());
				date.setText(trafficCitationReport.getCitationDate());
				time.setText(trafficCitationReport.getCitationTime());
				notes.setText(trafficCitationReport.getCitationComments());
				num.setText(trafficCitationReport.getCitationNumber());
				plateNumber.setText(trafficCitationReport.getOffenderVehiclePlate());
				otherInfo.setText(trafficCitationReport.getOffenderVehicleOther());
				model.setText(trafficCitationReport.getOffenderVehicleModel());
				offenderName.setText(trafficCitationReport.getOffenderName());
				offenderAge.setText(trafficCitationReport.getOffenderAge());
				offenderGender.setText(trafficCitationReport.getOffenderGender());
				offenderDescription.setText(trafficCitationReport.getOffenderDescription());
				offenderAddress.setText(trafficCitationReport.getOffenderHomeAddress());
				
				Button delBtn = (Button) trafficCitationObj.get("delBtn");
				delBtn.setVisible(true);
				delBtn.setDisable(false);
				delBtn.setOnAction(actionEvent -> {
					String numToDelete = num.getText();
					try {
						TrafficCitationUtils.deleteTrafficCitationReport(numToDelete);
						showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete);
					} catch (JAXBException e) {
						logError("Could not delete TrafficCitationReport #" + numToDelete + ": ", e);
					}
					CustomWindow window = getWindow("Citation Report");
					if (window != null) {
						window.closeWindow();
					}
					try {
						if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
							playSound(getJarPath() + "/sounds/alert-delete.wav");
						}
					} catch (IOException e) {
						logError("Error getting configValue for playDeleteReport: ", e);
					}
					needRefresh.set(1);
					
				});
				
				MenuButton pullnotesbtn = (MenuButton) trafficCitationObj.get("pullNotesBtn");
				pullnotesbtn.setVisible(false);
				num.setEditable(false);
				
				TableView citationtable1 = (TableView) citationReportMap.get("CitationTableView");
				ObservableList<CitationsData> citationList = FXCollections.observableArrayList();
				citationtable1.setItems(citationList);
				
				ComboBox<String> statusValue = (ComboBox<String>) trafficCitationObj.get("statusValue");
				statusValue.setValue("Reopened");
				
				addCitationsToTable(trafficCitationReport.getCitationCharges(), citationList);
				
				Button submitBtn = (Button) trafficCitationObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				citationTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	@javafx.fxml.FXML
	public void onSearchRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			SearchReport searchReport = (SearchReport) searchTable.getSelectionModel().getSelectedItem();
			
			if (searchReport != null) {
				Map<String, Object> searchReportObj = newSearch();
				
				Map<String, Object> searchReportMap = (Map<String, Object>) searchReportObj.get(localization.getLocalizedMessage("ReportWindows.SearchReportTitle", "Search Report") + " Map");
				
				TextField name = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
				TextField rank = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
				TextField div = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
				TextField agen = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
				TextField num = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));
				
				TextField searchnum = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.SearchNumField", "search num"));
				TextField date = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
				TextField time = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
				ComboBox street = (ComboBox) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
				ComboBox area = (ComboBox) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
				TextField county = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
				
				TextField grounds = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.GroundsForSearchField", "grounds for search"));
				TextField witness = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.WitnessesField", "witness(s)"));
				TextField searchedindividual = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.SearchedIndividualField", "searched individual"));
				ComboBox type = (ComboBox) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.SearchTypeField", "search type"));
				ComboBox method = (ComboBox) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.SearchMethodField", "search method"));
				
				TextField testconducted = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.TestsConductedField", "test(s) conducted"));
				TextField result = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.TestResultField", "result"));
				TextField bacmeasurement = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.BACMeasurementField", "bac measurement"));
				
				TextArea seizeditems = (TextArea) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.SeizedItemsField", "seized item(s)"));
				TextArea notes = (TextArea) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.CommentsField", "comments"));
				
				name.setText(searchReport.getOfficerName());
				div.setText(searchReport.getOfficerDivision());
				agen.setText(searchReport.getOfficerAgency());
				num.setText(searchReport.getOfficerNumber());
				
				street.getEditor().setText(searchReport.getSearchStreet());
				area.setValue(searchReport.getArea());
				county.setText(searchReport.getCounty());
				
				testconducted.setText(searchReport.getTestsConducted());
				grounds.setText(searchReport.getSearchGrounds());
				witness.setText(searchReport.getSearchWitnesses());
				searchedindividual.setText(searchReport.getSearchedPersons());
				type.setValue(searchReport.getSearchType());
				method.setValue(searchReport.getSearchMethod());
				result.setText(searchReport.getTestResults());
				bacmeasurement.setText(searchReport.getBreathalyzerBACMeasure());
				
				searchnum.setText(searchReport.getSearchNumber());
				rank.setText(searchReport.getOfficerRank());
				date.setText(searchReport.getSearchDate());
				time.setText(searchReport.getSearchTime());
				seizeditems.setText(searchReport.getSearchSeizedItems());
				notes.setText(searchReport.getSearchComments());
				
				BorderPane root = (BorderPane) searchReportObj.get("root");
				Button delBtn = (Button) searchReportObj.get("delBtn");
				delBtn.setVisible(true);
				delBtn.setDisable(false);
				delBtn.setOnAction(actionEvent -> {
					String numToDelete = searchnum.getText();
					try {
						SearchReportUtils.deleteSearchReport(numToDelete);
						showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete);
					} catch (JAXBException e) {
						logError("Could not delete SearchReport #" + numToDelete + ": ", e);
					}
					CustomWindow window = getWindow("Search Report");
					if (window != null) {
						window.closeWindow();
					}
					try {
						if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
							playSound(getJarPath() + "/sounds/alert-delete.wav");
						}
					} catch (IOException e) {
						logError("Error getting configValue for playDeleteReport: ", e);
					}
					needRefresh.set(1);
					
				});
				
				MenuButton pullnotesbtn = (MenuButton) searchReportObj.get("pullNotesBtn");
				pullnotesbtn.setVisible(false);
				searchnum.setEditable(false);
				
				ComboBox<String> statusValue = (ComboBox<String>) searchReportObj.get("statusValue");
				statusValue.setValue("Reopened");
				
				Button submitBtn = (Button) searchReportObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				searchTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	@javafx.fxml.FXML
	public void onArrestRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			ArrestReport arrestReport = (ArrestReport) arrestTable.getSelectionModel().getSelectedItem();
			
			if (arrestReport != null) {
				Map<String, Object> arrestReportObj = newArrest();
				
				Map<String, Object> arrestReportMap = (Map<String, Object>) arrestReportObj.get(localization.getLocalizedMessage("ReportWindows.ArrestReportTitle", "Arrest Report") + " Map");
				
				TextField officername = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
				TextField officerrank = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
				TextField officerdiv = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
				TextField officeragen = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
				TextField officernumarrest = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));
				
				TextField offenderName = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
				TextField offenderAge = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"));
				TextField offenderGender = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"));
				TextField offenderAddress = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"));
				TextField offenderDescription = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderDescription", "offender description"));
				
				ComboBox area = (ComboBox) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
				ComboBox street = (ComboBox) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
				TextField county = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
				TextField arrestnum = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.ArrestNumberField", "arrest number"));
				TextField date = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
				TextField time = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
				
				TextField ambulancereq = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldAmbulanceRequired", "ambulance required (Y/N)"));
				TextField taserdep = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTaserDeployed", "taser deployed (Y/N)"));
				TextField othermedinfo = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOtherInformation", "other information"));
				
				TextArea notes = (TextArea) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"));
				
				arrestnum.setText(arrestReport.getArrestNumber());
				officername.setText(arrestReport.getOfficerName());
				officerdiv.setText(arrestReport.getOfficerDivision());
				officeragen.setText(arrestReport.getOfficerAgency());
				officernumarrest.setText(arrestReport.getOfficerNumber());
				officerrank.setText(arrestReport.getOfficerRank());
				offenderName.setText(arrestReport.getArresteeName());
				offenderAge.setText(arrestReport.getArresteeAge());
				offenderGender.setText(arrestReport.getArresteeGender());
				offenderAddress.setText(arrestReport.getArresteeHomeAddress());
				offenderDescription.setText(arrestReport.getArresteeDescription());
				street.getEditor().setText(arrestReport.getArrestStreet());
				county.setText(arrestReport.getCounty());
				ambulancereq.setText(arrestReport.getAmbulanceYesNo());
				taserdep.setText(arrestReport.getTaserYesNo());
				othermedinfo.setText(arrestReport.getArresteeMedicalInformation());
				area.setValue(arrestReport.getArea());
				date.setText(arrestReport.getArrestDate());
				time.setText(arrestReport.getArrestTime());
				notes.setText(arrestReport.getArrestDetails());
				
				BorderPane root = (BorderPane) arrestReportObj.get("root");
				Button delBtn = (Button) arrestReportObj.get("delBtn");
				delBtn.setVisible(true);
				delBtn.setDisable(false);
				delBtn.setOnAction(actionEvent -> {
					String numToDelete = arrestnum.getText();
					try {
						ArrestReportUtils.deleteArrestReport(numToDelete);
						showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete);
					} catch (JAXBException e) {
						logError("Could not delete ArrestReport #" + numToDelete + ": ", e);
					}
					CustomWindow window = getWindow(localization.getLocalizedMessage("ReportWindows.ArrestReportTitle", "Arrest Report"));
					if (window != null) {
						window.closeWindow();
					}
					try {
						if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
							playSound(getJarPath() + "/sounds/alert-delete.wav");
						}
					} catch (IOException e) {
						logError("Error getting configValue for playDeleteReport: ", e);
					}
					needRefresh.set(1);
					
				});
				
				MenuButton pullnotesbtn = (MenuButton) arrestReportObj.get("pullNotesBtn");
				pullnotesbtn.setVisible(false);
				arrestnum.setEditable(false);
				
				ComboBox<String> statusValue = (ComboBox<String>) arrestReportObj.get("statusValue");
				statusValue.setValue("Reopened");
				
				TableView chargetable = (TableView) arrestReportMap.get("ChargeTableView");
				ObservableList<ChargesData> chargeList = FXCollections.observableArrayList();
				chargetable.setItems(chargeList);
				
				addChargesToTable(arrestReport.getArrestCharges(), chargeList);
				
				Button submitBtn = (Button) arrestReportObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				arrestTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	@javafx.fxml.FXML
	public void onAccidentReportRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			AccidentReport accidentReport = (AccidentReport) accidentReportTable.getSelectionModel().getSelectedItem();
			
			if (accidentReport != null) {
				Map<String, Object> accidentReportObj = newAccident();
				
				Map<String, Object> accidentReportMap = (Map<String, Object>) accidentReportObj.get(localization.getLocalizedMessage("ReportWindows.AccidentReportTitle", "Accident Report") + " Map");
				
				TextField name = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
				TextField rank = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
				TextField div = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
				TextField agen = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
				TextField num = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));
				ComboBox street = (ComboBox) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
				ComboBox area = (ComboBox) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
				TextField county = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
				TextField date = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
				TextField time = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
				TextField accidentnum = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.AccidentNumberField", "accident number"));
				TextField weatherConditions = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.AccidentWeatherConditionsField", "weather conditions"));
				TextField roadConditions = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.AccidentRoadConditionsField", "road conditions"));
				TextField otherVehiclesInvolved = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.AccidentOtherVehiclesField", "other vehicles involved"));
				TextField witnesses = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldWitnesses", "witnesses"));
				TextField injuries = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.AccidentInjuriesField", "injuries"));
				TextField damages = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.AccidentDamagesField", "damages"));
				TextField offenderName = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
				TextField offenderAge = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"));
				TextField offenderGender = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"));
				TextField offenderAddress = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"));
				TextField offenderDescription = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderDescription", "offender description"));
				TextField model = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldModel", "model"));
				TextField plateNumber = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldPlateNumber", "plate number"));
				ComboBox type = (ComboBox) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldType", localization.getLocalizedMessage("ReportWindows.FieldType", "type")));
				ComboBox color = (ComboBox) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldColor", "color"));
				TextArea notes = (TextArea) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"));
				
				accidentnum.setText(accidentReport.getAccidentNumber());
				name.setText(accidentReport.getOfficerName());
				rank.setText(accidentReport.getOfficerRank());
				div.setText(accidentReport.getOfficerDivision());
				agen.setText(accidentReport.getOfficerAgency());
				num.setText(accidentReport.getOfficerNumber());
				street.setValue(accidentReport.getStreet());
				area.setValue(accidentReport.getArea());
				county.setText(accidentReport.getCounty());
				date.setText(accidentReport.getAccidentDate());
				time.setText(accidentReport.getAccidentTime());
				weatherConditions.setText(accidentReport.getWeatherConditions());
				roadConditions.setText(accidentReport.getRoadConditions());
				otherVehiclesInvolved.setText(accidentReport.getOtherVehiclesInvolved());
				witnesses.setText(accidentReport.getWitnesses());
				injuries.setText(accidentReport.getInjuriesReported());
				damages.setText(accidentReport.getDamageDetails());
				offenderName.setText(accidentReport.getOwnerName());
				offenderAge.setText(accidentReport.getOwnerAge());
				offenderGender.setText(accidentReport.getOwnerGender());
				offenderAddress.setText(accidentReport.getOwnerAddress());
				offenderDescription.setText(accidentReport.getOwnerDescription());
				model.setText(accidentReport.getModel());
				plateNumber.setText(accidentReport.getPlateNumber());
				type.setValue(accidentReport.getType());
				color.setValue(accidentReport.getColor());
				notes.setText(accidentReport.getComments());
				
				BorderPane root = (BorderPane) accidentReportObj.get("root");
				Button delBtn = (Button) accidentReportObj.get("delBtn");
				delBtn.setVisible(true);
				delBtn.setDisable(false);
				delBtn.setOnAction(actionEvent -> {
					String numToDelete = accidentnum.getText();
					try {
						AccidentReportUtils.deleteAccidentReport(numToDelete);
						showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete);
					} catch (JAXBException e) {
						logError("Could not delete AccidentReport #" + numToDelete + ": ", e);
					}
					CustomWindow window = getWindow(localization.getLocalizedMessage("ReportWindows.AccidentReportTitle", "Accident Report"));
					if (window != null) {
						window.closeWindow();
					}
					try {
						if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
							playSound(getJarPath() + "/sounds/alert-delete.wav");
						}
					} catch (IOException e) {
						logError("Error getting configValue for playDeleteReport: ", e);
					}
					needRefresh.set(1);
					
				});
				
				MenuButton pullnotesbtn = (MenuButton) accidentReportObj.get("pullNotesBtn");
				pullnotesbtn.setVisible(false);
				accidentnum.setEditable(false);
				
				ComboBox<String> statusValue = (ComboBox<String>) accidentReportObj.get("statusValue");
				statusValue.setValue("Reopened");
				
				Button submitBtn = (Button) accidentReportObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				accidentReportTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	private <T> void setActive(TableView<T> table, BorderPane sideButton) {
		tableStackPane.getChildren().clear();
		tableStackPane.getChildren().add(table);
		
		ObservableList<T> items = table.getItems();
		
		long closed = 0, inProg = 0, pending = 0, cancelled = 0, reOpen = 0;
		
		for (T item : items) {
			try {
				Method getStatusMethod = item.getClass().getMethod("getStatus");
				String status = (String) getStatusMethod.invoke(item);
				
				if ("Closed".equalsIgnoreCase(status)) {
					closed++;
				} else if ("In Progress".equalsIgnoreCase(status)) {
					inProg++;
				} else if ("Pending".equalsIgnoreCase(status)) {
					pending++;
				} else if ("Cancelled".equalsIgnoreCase(status)) {
					cancelled++;
				} else if ("Reopened".equalsIgnoreCase(status)) {
					reOpen++;
				}
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
				logError("Could not obtain getStatus method for update: ", e);
			}
		}
		
		long totalReports = (closed + inProg + reOpen + pending + cancelled);
		long totalInProgress = (reOpen + inProg + pending);
		long totalClosed = (closed + cancelled);
		
		double closedPercentage = ((double) totalClosed / totalReports) * 100;
		double inProgressPercentage = ((double) totalInProgress / totalReports) * 100;
		
		closedReportsProgressBar.setProgress(closedPercentage / 100);
		reportsInProgressBar.setProgress(inProgressPercentage / 100);
		
		reportsInProgressLabel.setText(String.valueOf(totalInProgress));
		closedReportsLabel.setText(String.valueOf(totalClosed));
		totalReportsLabel.setText(String.valueOf(totalReports));
		
		activePane = sideButton;
	}
	
	public TableView getArrestTable() {
		return arrestTable;
	}
	
	public TableView getCalloutTable() {
		return calloutTable;
	}
	
	public TableView getAccidentReportTable() {
		return accidentReportTable;
	}
	
	public TableView getCitationTable() {
		return citationTable;
	}
	
	public TableView getDeathReportTable() {
		return deathReportTable;
	}
	
	public TableView getImpoundTable() {
		return impoundTable;
	}
	
	public TableView getIncidentTable() {
		return incidentTable;
	}
	
	public TableView getPatrolTable() {
		return patrolTable;
	}
	
	public TableView getSearchTable() {
		return searchTable;
	}
	
	public TableView getTrafficStopTable() {
		return trafficStopTable;
	}
	
	public BorderPane getRoot() {
		return root;
	}
}
