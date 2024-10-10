package com.drozal.dataterminal.Windows.Apps;

import com.drozal.dataterminal.Desktop.Utils.WindowUtils.CustomWindow;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.logs.Accident.AccidentReport;
import com.drozal.dataterminal.logs.Accident.AccidentReportUtils;
import com.drozal.dataterminal.logs.Accident.AccidentReports;
import com.drozal.dataterminal.logs.Arrest.ArrestReport;
import com.drozal.dataterminal.logs.Arrest.ArrestReportUtils;
import com.drozal.dataterminal.logs.Arrest.ArrestReports;
import com.drozal.dataterminal.logs.Callout.CalloutReport;
import com.drozal.dataterminal.logs.Callout.CalloutReportUtils;
import com.drozal.dataterminal.logs.Callout.CalloutReports;
import com.drozal.dataterminal.logs.ChargesData;
import com.drozal.dataterminal.logs.CitationsData;
import com.drozal.dataterminal.logs.Death.DeathReport;
import com.drozal.dataterminal.logs.Death.DeathReportUtils;
import com.drozal.dataterminal.logs.Death.DeathReports;
import com.drozal.dataterminal.logs.Impound.ImpoundReport;
import com.drozal.dataterminal.logs.Impound.ImpoundReportUtils;
import com.drozal.dataterminal.logs.Impound.ImpoundReports;
import com.drozal.dataterminal.logs.Incident.IncidentReport;
import com.drozal.dataterminal.logs.Incident.IncidentReportUtils;
import com.drozal.dataterminal.logs.Incident.IncidentReports;
import com.drozal.dataterminal.logs.Patrol.PatrolReport;
import com.drozal.dataterminal.logs.Patrol.PatrolReportUtils;
import com.drozal.dataterminal.logs.Patrol.PatrolReports;
import com.drozal.dataterminal.logs.Search.SearchReport;
import com.drozal.dataterminal.logs.Search.SearchReportUtils;
import com.drozal.dataterminal.logs.Search.SearchReports;
import com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationReport;
import com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationReports;
import com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationUtils;
import com.drozal.dataterminal.logs.TrafficStop.TrafficStopReport;
import com.drozal.dataterminal.logs.TrafficStop.TrafficStopReportUtils;
import com.drozal.dataterminal.logs.TrafficStop.TrafficStopReports;
import jakarta.xml.bind.JAXBException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.drozal.dataterminal.Desktop.Utils.WindowUtils.WindowManager.getWindow;
import static com.drozal.dataterminal.logs.Accident.AccidentReportUtils.newAccident;
import static com.drozal.dataterminal.logs.Arrest.ArrestReportUtils.newArrest;
import static com.drozal.dataterminal.logs.Callout.CalloutReportUtils.newCallout;
import static com.drozal.dataterminal.logs.Death.DeathReportUtils.newDeathReport;
import static com.drozal.dataterminal.logs.Impound.ImpoundReportUtils.newImpound;
import static com.drozal.dataterminal.logs.Incident.IncidentReportUtils.newIncident;
import static com.drozal.dataterminal.logs.Patrol.PatrolReportUtils.newPatrol;
import static com.drozal.dataterminal.logs.Search.SearchReportUtils.newSearch;
import static com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationUtils.newCitation;
import static com.drozal.dataterminal.logs.TrafficStop.TrafficStopReportUtils.newTrafficStop;
import static com.drozal.dataterminal.util.Misc.AudioUtil.playSound;
import static com.drozal.dataterminal.util.Misc.InitTableColumns.*;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.NotificationManager.showNotificationInfo;
import static com.drozal.dataterminal.util.Misc.stringUtil.getJarPath;
import static com.drozal.dataterminal.util.Report.treeViewUtils.addChargesToTable;
import static com.drozal.dataterminal.util.Report.treeViewUtils.addCitationsToTable;

public class LogViewController {
	public static LogViewController logController;
	public static SimpleIntegerProperty needRefresh = new SimpleIntegerProperty();
	@javafx.fxml.FXML
	private Label logbrwsrlbl;
	@javafx.fxml.FXML
	private Label reportPlusLabelFill;
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
	private VBox bkgclr2;
	@javafx.fxml.FXML
	private TableView patrolTable;
	@javafx.fxml.FXML
	private TableView accidentReportTable;
	@javafx.fxml.FXML
	private TabPane tabPane;
	@javafx.fxml.FXML
	private TableView incidentTable;
	@javafx.fxml.FXML
	private TableView searchTable;
	
	public void initialize() {
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
	
	public void calloutLogUpdate(List<CalloutReport> logEntries) {
		if (logEntries == null) {
			logEntries = new ArrayList<>();
		}
		calloutTable.getItems().clear();
		calloutTable.getItems().addAll(logEntries);
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
	
	@javafx.fxml.FXML
	public void onDeathReportRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			DeathReport deathReport = (DeathReport) deathReportTable.getSelectionModel().getSelectedItem();
			
			if (deathReport != null) {
				Map<String, Object> deathReportObj = newDeathReport();
				
				Map<String, Object> deathReport1 = (Map<String, Object>) deathReportObj.get("Death Report Map");
				
				TextField name = (TextField) deathReport1.get("name");
				TextField rank = (TextField) deathReport1.get("rank");
				TextField div = (TextField) deathReport1.get("division");
				TextField agen = (TextField) deathReport1.get("agency");
				TextField num = (TextField) deathReport1.get("number");
				TextField date = (TextField) deathReport1.get("date");
				TextField time = (TextField) deathReport1.get("time");
				ComboBox street = (ComboBox) deathReport1.get("street");
				ComboBox area = (ComboBox) deathReport1.get("area");
				TextField county = (TextField) deathReport1.get("county");
				TextField deathNum = (TextField) deathReport1.get("death num");
				TextField decedent = (TextField) deathReport1.get("decedent name");
				TextField age = (TextField) deathReport1.get("age/dob");
				TextField gender = (TextField) deathReport1.get("gender");
				TextField address = (TextField) deathReport1.get("address");
				TextField description = (TextField) deathReport1.get("description");
				TextField causeofdeath = (TextField) deathReport1.get("cause of death");
				TextField modeofdeath = (TextField) deathReport1.get("mode of death");
				TextField witnesses = (TextField) deathReport1.get("witnesses");
				TextArea notes = (TextArea) deathReport1.get("notes");
				TextField timeofdeath = (TextField) deathReport1.get("time of death");
				TextField dateofdeath = (TextField) deathReport1.get("date of death");
				
				timeofdeath.setText(deathReport.getTimeOfDeath());
				dateofdeath.setText(deathReport.getDateOfDeath());
				name.setText(deathReport.getName());
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
				
				BorderPane root = (BorderPane) deathReportObj.get("root");
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
				
				Map<String, Object> calloutReportMap = (Map<String, Object>) calloutReportObj.get("Callout Report Map");
				
				TextField officername = (TextField) calloutReportMap.get("name");
				TextField officerrank = (TextField) calloutReportMap.get("rank");
				TextField officerdiv = (TextField) calloutReportMap.get("division");
				TextField officeragen = (TextField) calloutReportMap.get("agency");
				TextField officernum = (TextField) calloutReportMap.get("number");
				TextField calloutnum = (TextField) calloutReportMap.get("calloutnumber");
				ComboBox calloutarea = (ComboBox) calloutReportMap.get("area");
				TextArea calloutnotes = (TextArea) calloutReportMap.get("notes");
				TextField calloutcounty = (TextField) calloutReportMap.get("county");
				ComboBox calloutstreet = (ComboBox) calloutReportMap.get("street");
				TextField calloutdate = (TextField) calloutReportMap.get("date");
				TextField callouttime = (TextField) calloutReportMap.get("time");
				TextField callouttype = (TextField) calloutReportMap.get("type");
				TextField calloutcode = (TextField) calloutReportMap.get("code");
				
				officername.setText(calloutReport.getName());
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
				
				BorderPane root = (BorderPane) calloutReportObj.get("root");
				Stage stage = (Stage) root.getScene().getWindow();
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
				
				Map<String, Object> patrolReportMap = (Map<String, Object>) patrolReportObj.get("Patrol Report Map");
				
				TextField name = (TextField) patrolReportMap.get("name");
				TextField rank = (TextField) patrolReportMap.get("rank");
				TextField div = (TextField) patrolReportMap.get("division");
				TextField agen = (TextField) patrolReportMap.get("agency");
				TextField num = (TextField) patrolReportMap.get("number");
				TextField patrolnum = (TextField) patrolReportMap.get("patrolnumber");
				TextArea notes = (TextArea) patrolReportMap.get("notes");
				TextField date = (TextField) patrolReportMap.get("date");
				TextField starttime = (TextField) patrolReportMap.get("starttime");
				TextField stoptime = (TextField) patrolReportMap.get("stoptime");
				TextField length = (TextField) patrolReportMap.get("length");
				TextField vehicle = (TextField) patrolReportMap.get("vehicle");
				
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
				Stage stage = (Stage) root.getScene().getWindow();
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
				
				Map<String, Object> trafficStopReportMap = (Map<String, Object>) trafficStopReportObj.get(
						"Traffic Stop Report Map");
				
				TextField officernamets = (TextField) trafficStopReportMap.get("name");
				TextField officerrankts = (TextField) trafficStopReportMap.get("rank");
				TextField officerdivts = (TextField) trafficStopReportMap.get("division");
				TextField officeragents = (TextField) trafficStopReportMap.get("agency");
				TextField officernumarrestts = (TextField) trafficStopReportMap.get("number");
				
				TextField offenderNamets = (TextField) trafficStopReportMap.get("offender name");
				TextField offenderAgets = (TextField) trafficStopReportMap.get("offender age");
				TextField offenderGenderts = (TextField) trafficStopReportMap.get("offender gender");
				TextField offenderAddressts = (TextField) trafficStopReportMap.get("offender address");
				TextField offenderDescriptionts = (TextField) trafficStopReportMap.get("offender description");
				
				ComboBox colorts = (ComboBox) trafficStopReportMap.get("color");
				ComboBox typets = (ComboBox) trafficStopReportMap.get("type");
				TextField plateNumberts = (TextField) trafficStopReportMap.get("plate number");
				TextField otherInfots = (TextField) trafficStopReportMap.get("other info");
				TextField modelts = (TextField) trafficStopReportMap.get("model");
				
				ComboBox areats = (ComboBox) trafficStopReportMap.get("area");
				ComboBox streetts = (ComboBox) trafficStopReportMap.get("street");
				TextField countyts = (TextField) trafficStopReportMap.get("county");
				TextField stopnumts = (TextField) trafficStopReportMap.get("stop number");
				TextField datets = (TextField) trafficStopReportMap.get("date");
				TextField timets = (TextField) trafficStopReportMap.get("time");
				
				TextArea notests = (TextArea) trafficStopReportMap.get("notes");
				
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
				officernamets.setText(trafficStopReport.getName());
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
				Stage stage = (Stage) root.getScene().getWindow();
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
				
				Map<String, Object> incidentReportMap = (Map<String, Object>) incidentReportObj.get(
						"Incident Report Map");
				
				TextField name = (TextField) incidentReportMap.get("name");
				TextField rank = (TextField) incidentReportMap.get("rank");
				TextField div = (TextField) incidentReportMap.get("division");
				TextField agen = (TextField) incidentReportMap.get("agency");
				TextField num = (TextField) incidentReportMap.get("number");
				
				TextField incidentnum = (TextField) incidentReportMap.get("incident num");
				TextField date = (TextField) incidentReportMap.get("date");
				TextField time = (TextField) incidentReportMap.get("time");
				ComboBox street = (ComboBox) incidentReportMap.get("street");
				ComboBox area = (ComboBox) incidentReportMap.get("area");
				TextField county = (TextField) incidentReportMap.get("county");
				
				TextField suspects = (TextField) incidentReportMap.get("suspect(s)");
				TextField vicwit = (TextField) incidentReportMap.get("victim(s) / witness(s)");
				TextArea statement = (TextArea) incidentReportMap.get("statement");
				
				TextArea summary = (TextArea) incidentReportMap.get("summary");
				TextArea notes = (TextArea) incidentReportMap.get("notes");
				
				name.setText(incidentReport.getOfficerName());
				incidentnum.setText(incidentReport.getIncidentNumber());
				rank.setText(incidentReport.getOfficerRank());
				div.setText(incidentReport.getOfficerDivision());
				agen.setText(incidentReport.getOfficerAgency());
				num.setText(incidentReport.getOfficerNumber());
				
				street.getEditor().setText(incidentReport.getIncidentStreet());
				area.setValue(incidentReport.getIncidentArea());
				county.setText(incidentReport.getIncidentCounty());
				suspects.setText(incidentReport.getIncidentWitnesses());
				vicwit.setText(incidentReport.getIncidentVictims());
				statement.setText(incidentReport.getIncidentStatement());
				
				date.setText(incidentReport.getIncidentDate());
				time.setText(incidentReport.getIncidentTime());
				summary.setText(incidentReport.getIncidentActionsTaken());
				notes.setText(incidentReport.getIncidentComments());
				
				BorderPane root = (BorderPane) incidentReportObj.get("root");
				Stage stage = (Stage) root.getScene().getWindow();
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
				
				Map<String, Object> impoundReportMap = (Map<String, Object>) impoundReportObj.get("Impound Report Map");
				
				TextField officername = (TextField) impoundReportMap.get("name");
				TextField officerrank = (TextField) impoundReportMap.get("rank");
				TextField officerdiv = (TextField) impoundReportMap.get("division");
				TextField officeragen = (TextField) impoundReportMap.get("agency");
				TextField officernum = (TextField) impoundReportMap.get("number");
				
				TextField offenderName = (TextField) impoundReportMap.get("offender name");
				TextField offenderAge = (TextField) impoundReportMap.get("offender age");
				TextField offenderGender = (TextField) impoundReportMap.get("offender gender");
				TextField offenderAddress = (TextField) impoundReportMap.get("offender address");
				
				TextField num = (TextField) impoundReportMap.get("impound number");
				TextField date = (TextField) impoundReportMap.get("date");
				TextField time = (TextField) impoundReportMap.get("time");
				
				ComboBox color = (ComboBox) impoundReportMap.get("color");
				ComboBox type = (ComboBox) impoundReportMap.get("type");
				TextField plateNumber = (TextField) impoundReportMap.get("plate number");
				TextField model = (TextField) impoundReportMap.get("model");
				
				TextArea notes = (TextArea) impoundReportMap.get("notes");
				
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
				Stage stage = (Stage) root.getScene().getWindow();
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
				
				Map<String, Object> citationReportMap = (Map<String, Object>) trafficCitationObj.get(
						"Citation Report Map");
				
				TextField officername = (TextField) citationReportMap.get("name");
				TextField officerrank = (TextField) citationReportMap.get("rank");
				TextField officerdiv = (TextField) citationReportMap.get("division");
				TextField officeragen = (TextField) citationReportMap.get("agency");
				TextField officernum = (TextField) citationReportMap.get("number");
				
				TextField offenderName = (TextField) citationReportMap.get("offender name");
				TextField offenderAge = (TextField) citationReportMap.get("offender age");
				TextField offenderGender = (TextField) citationReportMap.get("offender gender");
				TextField offenderAddress = (TextField) citationReportMap.get("offender address");
				TextField offenderDescription = (TextField) citationReportMap.get("offender description");
				
				ComboBox area = (ComboBox) citationReportMap.get("area");
				ComboBox street = (ComboBox) citationReportMap.get("street");
				TextField county = (TextField) citationReportMap.get("county");
				TextField num = (TextField) citationReportMap.get("citation number");
				TextField date = (TextField) citationReportMap.get("date");
				TextField time = (TextField) citationReportMap.get("time");
				
				ComboBox color = (ComboBox) citationReportMap.get("color");
				ComboBox type = (ComboBox) citationReportMap.get("type");
				TextField plateNumber = (TextField) citationReportMap.get("plate number");
				TextField otherInfo = (TextField) citationReportMap.get("other info");
				TextField model = (TextField) citationReportMap.get("model");
				
				TextArea notes = (TextArea) citationReportMap.get("notes");
				
				officername.setText(trafficCitationReport.getOfficerName());
				officerrank.setText(trafficCitationReport.getOfficerRank());
				officerdiv.setText(trafficCitationReport.getOfficerDivision());
				officeragen.setText(trafficCitationReport.getOfficerAgency());
				officernum.setText(trafficCitationReport.getOfficerNumber());
				street.setValue(trafficCitationReport.getCitationStreet());
				area.setValue(trafficCitationReport.getCitationArea());
				county.setText(trafficCitationReport.getCitationCounty());
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
				
				BorderPane root = (BorderPane) trafficCitationObj.get("root");
				Stage stage = (Stage) root.getScene().getWindow();
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
				
				Map<String, Object> searchReportMap = (Map<String, Object>) searchReportObj.get("Search Report Map");
				
				TextField name = (TextField) searchReportMap.get("name");
				TextField rank = (TextField) searchReportMap.get("rank");
				TextField div = (TextField) searchReportMap.get("division");
				TextField agen = (TextField) searchReportMap.get("agency");
				TextField num = (TextField) searchReportMap.get("number");
				
				TextField searchnum = (TextField) searchReportMap.get("search num");
				TextField date = (TextField) searchReportMap.get("date");
				TextField time = (TextField) searchReportMap.get("time");
				ComboBox street = (ComboBox) searchReportMap.get("street");
				ComboBox area = (ComboBox) searchReportMap.get("area");
				TextField county = (TextField) searchReportMap.get("county");
				
				TextField grounds = (TextField) searchReportMap.get("grounds for search");
				TextField witness = (TextField) searchReportMap.get("witness(s)");
				TextField searchedindividual = (TextField) searchReportMap.get("searched individual");
				ComboBox type = (ComboBox) searchReportMap.get("search type");
				ComboBox method = (ComboBox) searchReportMap.get("search method");
				
				TextField testconducted = (TextField) searchReportMap.get("test(s) conducted");
				TextField result = (TextField) searchReportMap.get("result");
				TextField bacmeasurement = (TextField) searchReportMap.get("bac measurement");
				
				TextArea seizeditems = (TextArea) searchReportMap.get("seized item(s)");
				TextArea notes = (TextArea) searchReportMap.get("comments");
				
				name.setText(searchReport.getOfficerName());
				div.setText(searchReport.getOfficerDivision());
				agen.setText(searchReport.getOfficerAgency());
				num.setText(searchReport.getOfficerNumber());
				
				street.getEditor().setText(searchReport.getSearchStreet());
				area.setValue(searchReport.getSearchArea());
				county.setText(searchReport.getSearchCounty());
				
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
				Stage stage = (Stage) root.getScene().getWindow();
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
				
				Map<String, Object> arrestReportMap = (Map<String, Object>) arrestReportObj.get("Arrest Report Map");
				
				TextField officername = (TextField) arrestReportMap.get("name");
				TextField officerrank = (TextField) arrestReportMap.get("rank");
				TextField officerdiv = (TextField) arrestReportMap.get("division");
				TextField officeragen = (TextField) arrestReportMap.get("agency");
				TextField officernumarrest = (TextField) arrestReportMap.get("number");
				
				TextField offenderName = (TextField) arrestReportMap.get("offender name");
				TextField offenderAge = (TextField) arrestReportMap.get("offender age");
				TextField offenderGender = (TextField) arrestReportMap.get("offender gender");
				TextField offenderAddress = (TextField) arrestReportMap.get("offender address");
				TextField offenderDescription = (TextField) arrestReportMap.get("offender description");
				
				ComboBox area = (ComboBox) arrestReportMap.get("area");
				ComboBox street = (ComboBox) arrestReportMap.get("street");
				TextField county = (TextField) arrestReportMap.get("county");
				TextField arrestnum = (TextField) arrestReportMap.get("arrest number");
				TextField date = (TextField) arrestReportMap.get("date");
				TextField time = (TextField) arrestReportMap.get("time");
				
				TextField ambulancereq = (TextField) arrestReportMap.get("ambulance required (Y/N)");
				TextField taserdep = (TextField) arrestReportMap.get("taser deployed (Y/N)");
				TextField othermedinfo = (TextField) arrestReportMap.get("other information");
				
				TextArea notes = (TextArea) arrestReportMap.get("notes");
				
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
				county.setText(arrestReport.getArrestCounty());
				ambulancereq.setText(arrestReport.getAmbulanceYesNo());
				taserdep.setText(arrestReport.getTaserYesNo());
				othermedinfo.setText(arrestReport.getArresteeMedicalInformation());
				area.setValue(arrestReport.getArrestArea());
				date.setText(arrestReport.getArrestDate());
				time.setText(arrestReport.getArrestTime());
				notes.setText(arrestReport.getArrestDetails());
				
				BorderPane root = (BorderPane) arrestReportObj.get("root");
				Stage stage = (Stage) root.getScene().getWindow();
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
					CustomWindow window = getWindow("Arrest Report");
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
				
				Map<String, Object> accidentReportMap = (Map<String, Object>) accidentReportObj.get(
						"Accident Report Map");
				
				TextField name = (TextField) accidentReportMap.get("name");
				TextField rank = (TextField) accidentReportMap.get("rank");
				TextField div = (TextField) accidentReportMap.get("division");
				TextField agen = (TextField) accidentReportMap.get("agency");
				TextField num = (TextField) accidentReportMap.get("number");
				ComboBox street = (ComboBox) accidentReportMap.get("street");
				ComboBox area = (ComboBox) accidentReportMap.get("area");
				TextField county = (TextField) accidentReportMap.get("county");
				TextField date = (TextField) accidentReportMap.get("date");
				TextField time = (TextField) accidentReportMap.get("time");
				TextField accidentnum = (TextField) accidentReportMap.get("accident number");
				TextField weatherConditions = (TextField) accidentReportMap.get("weather conditions");
				TextField roadConditions = (TextField) accidentReportMap.get("road conditions");
				TextField otherVehiclesInvolved = (TextField) accidentReportMap.get("other vehicles involved");
				TextField witnesses = (TextField) accidentReportMap.get("witnesses");
				TextField injuries = (TextField) accidentReportMap.get("injuries");
				TextField damages = (TextField) accidentReportMap.get("damages");
				TextField offenderName = (TextField) accidentReportMap.get("offender name");
				TextField offenderAge = (TextField) accidentReportMap.get("offender age");
				TextField offenderGender = (TextField) accidentReportMap.get("offender gender");
				TextField offenderAddress = (TextField) accidentReportMap.get("offender address");
				TextField offenderDescription = (TextField) accidentReportMap.get("offender description");
				TextField model = (TextField) accidentReportMap.get("model");
				TextField plateNumber = (TextField) accidentReportMap.get("plate number");
				ComboBox type = (ComboBox) accidentReportMap.get("type");
				ComboBox color = (ComboBox) accidentReportMap.get("color");
				TextArea notes = (TextArea) accidentReportMap.get("notes");
				
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
				Stage stage = (Stage) root.getScene().getWindow();
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
					CustomWindow window = getWindow("Accident Report");
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
				
				Button submitBtn = (Button) accidentReportObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				accidentReportTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	public Label getLogbrwsrlbl() {
		return logbrwsrlbl;
	}
	
	public VBox getBkgclr2() {
		return bkgclr2;
	}
	
	public Label getReportPlusLabelFill() {
		return reportPlusLabelFill;
	}
	
	public TabPane getTabPane() {
		return tabPane;
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
