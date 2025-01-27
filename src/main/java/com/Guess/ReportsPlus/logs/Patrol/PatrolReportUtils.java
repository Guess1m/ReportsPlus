package com.Guess.ReportsPlus.logs.Patrol;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.Windows.Apps.LogViewController;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.Guess.ReportsPlus.util.Misc.NotificationManager;
import com.Guess.ReportsPlus.util.Report.nestedReportUtils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.animation.PauseTransition;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager.getWindow;
import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.MainApplication.getDate;
import static com.Guess.ReportsPlus.MainApplication.getTime;
import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.notesViewController;
import static com.Guess.ReportsPlus.util.Misc.AudioUtil.playSound;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.*;
import static com.Guess.ReportsPlus.util.Report.reportUtil.createReportWindow;
import static com.Guess.ReportsPlus.util.Report.reportUtil.generateReportNumber;
import static com.Guess.ReportsPlus.util.Strings.URLStrings.patrolLogURL;

public class PatrolReportUtils {
	
	public static Map<String, Object> patrolLayout() {
		Map<String, Object> patrolReport = createReportWindow(localization.getLocalizedMessage("ReportWindows.PatrolReportTitle", "Patrol Report"), null, new nestedReportUtils.SectionConfig(localization.getLocalizedMessage("ReportWindows.OfficerInfoSectionHeading", "Officer Information"), true, new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"), 5, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"), 5, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"), 2, nestedReportUtils.FieldType.TEXT_FIELD)), new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"), 6, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"), 6, nestedReportUtils.FieldType.TEXT_FIELD))), new nestedReportUtils.SectionConfig(localization.getLocalizedMessage("ReportWindows.ShiftInfoheader", "Shift Information"), true, new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.StartTimeField", "starttime"), 3, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.StopTimeField", "stoptime"), 4, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.PatrolNumField", "patrolnumber"), 5, nestedReportUtils.FieldType.TEXT_FIELD)), new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.LengthField", "length"), 3, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"), 3, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.VehicleField", "vehicle"), 6, nestedReportUtils.FieldType.TEXT_FIELD))), new nestedReportUtils.SectionConfig(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"), true, new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"), 12, nestedReportUtils.FieldType.TEXT_AREA))));
		return patrolReport;
	}
	
	public static Map<String, Object> newPatrol() {
		Map<String, Object> patrolReport = patrolLayout();
		
		Map<String, Object> patrolReportMap = (Map<String, Object>) patrolReport.get(localization.getLocalizedMessage("ReportWindows.PatrolReportTitle", "Patrol Report") + " Map");
		
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
		
		Label warningLabel = (Label) patrolReport.get("warningLabel");
		
		try {
			name.setText(ConfigReader.configRead("userInfo", "Name"));
			rank.setText(ConfigReader.configRead("userInfo", "Rank"));
			div.setText(ConfigReader.configRead("userInfo", "Division"));
			agen.setText(ConfigReader.configRead("userInfo", "Agency"));
			num.setText(ConfigReader.configRead("userInfo", "Number"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		stoptime.setText(getTime(false));
		date.setText(getDate());
		patrolnum.setText(generateReportNumber());
		
		MenuButton pullnotesbtn = (MenuButton) patrolReport.get("pullNotesBtn");
		pullnotesbtn.setPopupSide(Side.TOP);
		
		pullnotesbtn.setOnMouseEntered(actionEvent -> {
			pullnotesbtn.getItems().clear();
			if (notesViewController != null) {
				for (Tab tab : notesViewController.getTabPane().getTabs()) {
					MenuItem menuItem = new MenuItem(tab.getText());
					pullnotesbtn.getItems().add(menuItem);
					AnchorPane anchorPane = (AnchorPane) tab.getContent();
					TextArea noteArea = (TextArea) anchorPane.lookup("#notepadTextArea");
					if (noteArea != null) {
						menuItem.setOnAction(event2 -> {
							updateTextFromNotepad(patrolnum, noteArea, "-number");
							updateTextFromNotepad(notes, noteArea, "-comments");
						});
					}
				}
			} else {
				log("NotesViewController Is Null", LogUtils.Severity.ERROR);
			}
		});
		
		Button submitBtn = (Button) patrolReport.get("submitBtn");
		
		ComboBox<String> statusValue = (ComboBox) patrolReport.get("statusValue");
		
		submitBtn.setOnAction(event -> {
			if (patrolnum.getText().trim().isEmpty()) {
				warningLabel.setVisible(true);
				warningLabel.setText("Patrol Number can't be empty!");
				warningLabel.setStyle("-fx-font-family: \"Segoe UI Black\"; -fx-text-fill: red;");
				PauseTransition pause = new PauseTransition(Duration.seconds(2));
				pause.setOnFinished(e -> warningLabel.setVisible(false));
				pause.play();
			} else {
				for (String fieldName : patrolReportMap.keySet()) {
					Object field = patrolReportMap.get(fieldName);
					if (field instanceof ComboBox<?> comboBox) {
						if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
							comboBox.getSelectionModel().selectFirst();
						}
					}
				}
				PatrolReport patrolReport1 = new PatrolReport();
				patrolReport1.setStatus(statusValue.getValue());
				patrolReport1.setPatrolNumber(toTitleCase(patrolnum.getText()));
				patrolReport1.setPatrolDate(date.getText());
				patrolReport1.setPatrolLength(toTitleCase(length.getText()));
				patrolReport1.setPatrolStartTime(starttime.getText());
				patrolReport1.setPatrolStopTime(stoptime.getText());
				patrolReport1.setOfficerRank((rank.getText()));
				patrolReport1.setOfficerName(toTitleCase(name.getText()));
				patrolReport1.setOfficerNumber(toTitleCase(num.getText()));
				patrolReport1.setOfficerDivision(toTitleCase(div.getText()));
				patrolReport1.setOfficerAgency(toTitleCase(agen.getText()));
				patrolReport1.setOfficerVehicle(toTitleCase(vehicle.getText()));
				patrolReport1.setPatrolComments(notes.getText());
				
				try {
					PatrolReportUtils.addPatrolReport(patrolReport1);
				} catch (JAXBException e) {
					logError("Could not add new PatrolReport: ", e);
				}
				
				try {
					if (ConfigReader.configRead("soundSettings", "playCreateReport").equalsIgnoreCase("true")) {
						playSound(getJarPath() + "/sounds/alert-success.wav");
					}
				} catch (IOException e) {
					logError("Error getting configValue for playCreateReport: ", e);
				}
				LogViewController.needRefresh.set(1);
				
				NotificationManager.showNotificationInfo("Report Manager", "A new Patrol Report has been submitted.");
				
				CustomWindow window = getWindow("Patrol Report");
				if (window != null) {
					window.closeWindow();
				}
			}
		});
		return patrolReport;
	}
	
	public static PatrolReports loadPatrolReports() throws JAXBException {
		File file = new File(patrolLogURL);
		if (!file.exists()) {
			return new PatrolReports();
		}
		
		try {
			JAXBContext context = JAXBContext.newInstance(PatrolReports.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (PatrolReports) unmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			logError("Error loading PatrolReports: ", e);
			throw e;
		}
	}
	
	private static void savePatrolReports(PatrolReports PatrolReports) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(PatrolReports.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		File file = new File(patrolLogURL);
		marshaller.marshal(PatrolReports, file);
	}
	
	public static void addPatrolReport(PatrolReport PatrolReport) throws JAXBException {
		PatrolReports PatrolReports = loadPatrolReports();
		
		if (PatrolReports.getPatrolReportList() == null) {
			PatrolReports.setPatrolReportList(new java.util.ArrayList<>());
		}
		
		Optional<PatrolReport> existingReport = PatrolReports.getPatrolReportList().stream().filter(e -> e.getPatrolNumber().equals(PatrolReport.getPatrolNumber())).findFirst();
		
		if (existingReport.isPresent()) {
			PatrolReports.getPatrolReportList().remove(existingReport.get());
			PatrolReports.getPatrolReportList().add(PatrolReport);
			log("PatrolReport with number " + PatrolReport.getPatrolNumber() + " updated.", LogUtils.Severity.INFO);
		} else {
			PatrolReports.getPatrolReportList().add(PatrolReport);
			log("PatrolReport with number " + PatrolReport.getPatrolNumber() + " added.", LogUtils.Severity.INFO);
		}
		
		savePatrolReports(PatrolReports);
	}
	
	public static void deletePatrolReport(String PatrolReportnumber) throws JAXBException {
		PatrolReports PatrolReports = loadPatrolReports();
		
		if (PatrolReports.getPatrolReportList() != null) {
			PatrolReports.getPatrolReportList().removeIf(e -> e.getPatrolNumber().equals(PatrolReportnumber));
			savePatrolReports(PatrolReports);
			log("PatrolReport with number " + PatrolReportnumber + " deleted.", LogUtils.Severity.INFO);
		}
	}
	
}