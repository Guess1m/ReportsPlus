package com.Guess.ReportsPlus.logs.Incident;

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
import static com.Guess.ReportsPlus.util.Misc.controllerUtils.toTitleCase;
import static com.Guess.ReportsPlus.util.Misc.controllerUtils.updateTextFromNotepad;
import static com.Guess.ReportsPlus.util.Misc.stringUtil.getJarPath;
import static com.Guess.ReportsPlus.util.Misc.stringUtil.incidentLogURL;
import static com.Guess.ReportsPlus.util.Report.reportUtil.createReportWindow;
import static com.Guess.ReportsPlus.util.Report.reportUtil.generateReportNumber;

public class IncidentReportUtils {
	
	public static Map<String, Object> incidentLayout() {
		Map<String, Object> incidentReport = createReportWindow(localization.getLocalizedMessage("ReportWindows.IncidentReportTitle", "Incident Report"), null,
		                                                        new nestedReportUtils.SectionConfig(localization.getLocalizedMessage("ReportWindows.OfficerInfoSectionHeading", "Officer Information"), true,
		                                                                                            new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"), 5, nestedReportUtils.FieldType.TEXT_FIELD),
		                                                                                                                            new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"), 5, nestedReportUtils.FieldType.TEXT_FIELD),
		                                                                                                                            new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"), 2, nestedReportUtils.FieldType.TEXT_FIELD)),
		                                                                                            new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"), 6, nestedReportUtils.FieldType.TEXT_FIELD),
		                                                                                                                            new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"), 6, nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                        new nestedReportUtils.SectionConfig(localization.getLocalizedMessage("ReportWindows.TimeLocationInfoheader", "Timestamp / Location Information"), true,
		                                                                                            new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"), 3, nestedReportUtils.FieldType.TEXT_FIELD),
		                                                                                                                            new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"), 4, nestedReportUtils.FieldType.TEXT_FIELD),
		                                                                                                                            new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.IncidentNumField", "incident num"), 5, nestedReportUtils.FieldType.TEXT_FIELD)),
		                                                                                            new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"), 5, nestedReportUtils.FieldType.COMBO_BOX_STREET),
		                                                                                                                            new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"), 4, nestedReportUtils.FieldType.COMBO_BOX_AREA),
		                                                                                                                            new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"), 3, nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                        new nestedReportUtils.SectionConfig(localization.getLocalizedMessage("ReportWindows.PartiesInvolvedSectionHeader", "Parties Involved"), false,
		                                                                                            new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.SuspectsField", "suspect(s)"), 6, nestedReportUtils.FieldType.TEXT_FIELD),
		                                                                                                                            new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.VictimsWitnessField", "victim(s) / witness(s)"), 6,
		                                                                                                                                                              nestedReportUtils.FieldType.TEXT_FIELD)),
		                                                                                            new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.StatementField", "statement"), 12, nestedReportUtils.FieldType.TEXT_AREA))),
		                                                        new nestedReportUtils.SectionConfig(localization.getLocalizedMessage("ReportWindows.NotesSummarySectionheader", "Notes / Summary"), true,
		                                                                                            new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.IncidentSummaryField", "summary"), 12, nestedReportUtils.FieldType.TEXT_AREA)),
		                                                                                            new nestedReportUtils.RowConfig(
				                                                                                            new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldNotes", localization.getLocalizedMessage("ReportWindows.FieldNotes", "notes")), 12,
				                                                                                                                              nestedReportUtils.FieldType.TEXT_AREA))));
		return incidentReport;
	}
	
	public static Map<String, Object> newIncident() {
		Map<String, Object> incidentReport = incidentLayout();
		
		Map<String, Object> incidentReportMap = (Map<String, Object>) incidentReport.get(localization.getLocalizedMessage("ReportWindows.IncidentReportTitle", "Incident Report") + " Map");
		
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
		TextArea notes = (TextArea) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", localization.getLocalizedMessage("ReportWindows.FieldNotes", "notes")));
		
		try {
			name.setText(ConfigReader.configRead("userInfo", "Name"));
			rank.setText(ConfigReader.configRead("userInfo", "Rank"));
			div.setText(ConfigReader.configRead("userInfo", "Division"));
			agen.setText(ConfigReader.configRead("userInfo", "Agency"));
			num.setText(ConfigReader.configRead("userInfo", "Number"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		date.setText(getDate());
		time.setText(getTime());
		incidentnum.setText(generateReportNumber());
		
		MenuButton pullnotesbtn = (MenuButton) incidentReport.get("pullNotesBtn");
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
							updateTextFromNotepad(area.getEditor(), noteArea, "-area");
							updateTextFromNotepad(county, noteArea, "-county");
							updateTextFromNotepad(street.getEditor(), noteArea, "-street");
							updateTextFromNotepad(vicwit, noteArea, "-name");
							updateTextFromNotepad(notes, noteArea, "-comments");
							updateTextFromNotepad(incidentnum, noteArea, "-number");
						});
					}
				}
			} else {
				log("NotesViewController Is Null", LogUtils.Severity.ERROR);
			}
		});
		
		Button submitBtn = (Button) incidentReport.get("submitBtn");
		Label warningLabel = (Label) incidentReport.get("warningLabel");
		
		ComboBox<String> statusValue = (ComboBox) incidentReport.get("statusValue");
		
		submitBtn.setOnAction(event -> {
			if (incidentnum.getText().trim().isEmpty()) {
				warningLabel.setVisible(true);
				warningLabel.setText("Incident Number can't be empty!");
				warningLabel.setStyle("-fx-font-family: \"Segoe UI Black\"; -fx-text-fill: red;");
				PauseTransition pause = new PauseTransition(Duration.seconds(2));
				pause.setOnFinished(e -> warningLabel.setVisible(false));
				pause.play();
			} else {
				for (String fieldName : incidentReportMap.keySet()) {
					Object field = incidentReportMap.get(fieldName);
					if (field instanceof ComboBox<?> comboBox) {
						if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
							comboBox.getSelectionModel().selectFirst();
						}
					}
				}
				
				IncidentReport incidentReport1 = new IncidentReport();
				incidentReport1.setStatus(statusValue.getValue());
				incidentReport1.setIncidentArea(toTitleCase(area.getEditor().getText()));
				incidentReport1.setIncidentComments(notes.getText());
				incidentReport1.setIncidentActionsTaken(summary.getText());
				incidentReport1.setIncidentCounty(toTitleCase(county.getText()));
				incidentReport1.setIncidentDate(date.getText());
				incidentReport1.setIncidentNumber(toTitleCase(incidentnum.getText()));
				incidentReport1.setIncidentStatement(toTitleCase(statement.getText()));
				incidentReport1.setIncidentStreet(toTitleCase(street.getEditor().getText()));
				incidentReport1.setIncidentTime(time.getText());
				incidentReport1.setIncidentVictims(toTitleCase(vicwit.getText()));
				incidentReport1.setIncidentWitnesses(toTitleCase(suspects.getText()));
				incidentReport1.setOfficerAgency(toTitleCase(agen.getText()));
				incidentReport1.setOfficerDivision(toTitleCase(div.getText()));
				incidentReport1.setOfficerName(toTitleCase(name.getText()));
				incidentReport1.setOfficerNumber(toTitleCase(num.getText()));
				incidentReport1.setOfficerRank(rank.getText());
				try {
					IncidentReportUtils.addIncidentReport(incidentReport1);
				} catch (JAXBException e) {
					logError("Error creating IncidentReport: ", e);
				}
				
				try {
					if (ConfigReader.configRead("soundSettings", "playCreateReport").equalsIgnoreCase("true")) {
						playSound(getJarPath() + "/sounds/alert-success.wav");
					}
				} catch (IOException e) {
					logError("Error getting configValue for playCreateReport: ", e);
				}
				LogViewController.needRefresh.set(1);
				
				NotificationManager.showNotificationInfo("Report Manager", "A new Incident Report has been submitted.");
				CustomWindow window = getWindow("Incident Report");
				if (window != null) {
					window.closeWindow();
				}
			}
		});
		return incidentReport;
	}
	
	public static IncidentReports loadIncidentReports() throws JAXBException {
		File file = new File(incidentLogURL);
		if (!file.exists()) {
			return new IncidentReports();
		}
		
		try {
			JAXBContext context = JAXBContext.newInstance(IncidentReports.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (IncidentReports) unmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			logError("Error loading IncidentReports: ", e);
			throw e;
		}
	}
	
	private static void saveIncidentReports(IncidentReports IncidentReports) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(IncidentReports.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		File file = new File(incidentLogURL);
		marshaller.marshal(IncidentReports, file);
	}
	
	public static void addIncidentReport(IncidentReport IncidentReport) throws JAXBException {
		IncidentReports IncidentReports = loadIncidentReports();
		
		if (IncidentReports.getIncidentReportList() == null) {
			IncidentReports.setIncidentReportList(new java.util.ArrayList<>());
		}
		
		Optional<IncidentReport> existingReport = IncidentReports.getIncidentReportList().stream().filter(e -> e.getIncidentNumber().equals(IncidentReport.getIncidentNumber())).findFirst();
		
		if (existingReport.isPresent()) {
			IncidentReports.getIncidentReportList().remove(existingReport.get());
			IncidentReports.getIncidentReportList().add(IncidentReport);
			log("IncidentReport with number " + IncidentReport.getIncidentNumber() + " updated.", LogUtils.Severity.INFO);
		} else {
			IncidentReports.getIncidentReportList().add(IncidentReport);
			log("IncidentReport with number " + IncidentReport.getIncidentNumber() + " added.", LogUtils.Severity.INFO);
		}
		
		saveIncidentReports(IncidentReports);
	}
	
	public static void deleteIncidentReport(String IncidentReportnumber) throws JAXBException {
		IncidentReports IncidentReports = loadIncidentReports();
		
		if (IncidentReports.getIncidentReportList() != null) {
			IncidentReports.getIncidentReportList().removeIf(e -> e.getIncidentNumber().equals(IncidentReportnumber));
			saveIncidentReports(IncidentReports);
			log("IncidentReport with number " + IncidentReportnumber + " deleted.", LogUtils.Severity.INFO);
		}
	}
}