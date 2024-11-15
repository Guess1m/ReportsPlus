package com.Guess.ReportsPlus.logs.Callout;

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
import static com.Guess.ReportsPlus.util.Misc.stringUtil.calloutLogURL;
import static com.Guess.ReportsPlus.util.Misc.stringUtil.getJarPath;
import static com.Guess.ReportsPlus.util.Report.reportUtil.createReportWindow;
import static com.Guess.ReportsPlus.util.Report.reportUtil.generateReportNumber;

public class CalloutReportUtils {
	
	public static Map<String, Object> calloutLayout() {
		Map<String, Object> calloutReport = createReportWindow(
				localization.getLocalizedMessage("ReportWindows.CalloutReportTitle", "Callout Report"), null,
		                                                       new nestedReportUtils.SectionConfig(
				                                                       localization.getLocalizedMessage(
						                                                       "ReportWindows.OfficerInfoSectionHeading",
						                                                       "Officer Information"), true,
				                                                       new nestedReportUtils.RowConfig(
						                                                       new nestedReportUtils.FieldConfig(
								                                                       localization.getLocalizedMessage(
										                                                       "ReportWindows.FieldOfficerName",
										                                                       "name"),
						                                                                                         5,
						                                                                                         nestedReportUtils.FieldType.TEXT_FIELD),
						                                                       new nestedReportUtils.FieldConfig(
								                                                       localization.getLocalizedMessage(
										                                                       "ReportWindows.FieldOfficerRank",
										                                                       "rank"),
						                                                                                         5,
						                                                                                         nestedReportUtils.FieldType.TEXT_FIELD),
						                                                       new nestedReportUtils.FieldConfig(
								                                                       localization.getLocalizedMessage(
										                                                       "ReportWindows.FieldOfficerNumber",
										                                                       "number"), 2,
								                                                       nestedReportUtils.FieldType.TEXT_FIELD)),
				                                                       new nestedReportUtils.RowConfig(
						                                                       new nestedReportUtils.FieldConfig(
								                                                       localization.getLocalizedMessage(
										                                                       "ReportWindows.FieldOfficerDivision",
										                                                       "division"), 6,
								                                                       nestedReportUtils.FieldType.TEXT_FIELD),
						                                                       new nestedReportUtils.FieldConfig(
								                                                       localization.getLocalizedMessage(
										                                                       "ReportWindows.FieldOfficerAgency",
										                                                       "agency"), 6,
								                                                       nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                       new nestedReportUtils.SectionConfig(
				                                                       "Location Information", true,
				                                                       new nestedReportUtils.RowConfig(
						                                                       new nestedReportUtils.FieldConfig(
								                                                       localization.getLocalizedMessage(
										                                                       "ReportWindows.FieldStreet",
										                                                       "street"), 5,
								                                                       nestedReportUtils.FieldType.COMBO_BOX_STREET),
						                                                       new nestedReportUtils.FieldConfig(
								                                                       localization.getLocalizedMessage(
										                                                       "ReportWindows.FieldArea",
										                                                       "area"),
						                                                                                         4,
						                                                                                         nestedReportUtils.FieldType.COMBO_BOX_AREA),
						                                                       new nestedReportUtils.FieldConfig(
								                                                       localization.getLocalizedMessage(
										                                                       "ReportWindows.FieldCounty",
										                                                       "county"), 3,
								                                                       nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                       new nestedReportUtils.SectionConfig(
				                                                       localization.getLocalizedMessage(
						                                                       "CalloutPopup.MainHeading",
						                                                       "Callout Information"), true,
				                                                       new nestedReportUtils.RowConfig(
						                                                       new nestedReportUtils.FieldConfig(
								                                                       localization.getLocalizedMessage(
										                                                       "ReportWindows.FieldDate",
										                                                       "date"),
						                                                                                         6,
						                                                                                         nestedReportUtils.FieldType.TEXT_FIELD),
						                                                       new nestedReportUtils.FieldConfig(
								                                                       localization.getLocalizedMessage(
										                                                       "ReportWindows.FieldTime",
										                                                       "time"),
						                                                                                         6,
						                                                                                         nestedReportUtils.FieldType.TEXT_FIELD)),
				                                                       new nestedReportUtils.RowConfig(
						                                                       new nestedReportUtils.FieldConfig(
								                                                       localization.getLocalizedMessage(
										                                                       "ReportWindows.FieldType",
										                                                       localization.getLocalizedMessage(
												                                                       "ReportWindows.FieldType",
												                                                       "type")),
						                                                                                         4,
						                                                                                         nestedReportUtils.FieldType.TEXT_FIELD),
						                                                       new nestedReportUtils.FieldConfig(
								                                                       localization.getLocalizedMessage(
										                                                       "ReportWindows.CalloutCodeField",
										                                                       "code"),
						                                                                                         4,
						                                                                                         nestedReportUtils.FieldType.TEXT_FIELD),
						                                                       new nestedReportUtils.FieldConfig(
								                                                       localization.getLocalizedMessage(
										                                                       "ReportWindows.CalloutNumberField",
										                                                       "callout num"), 4,
								                                                       nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                       new nestedReportUtils.SectionConfig("Callout Notes",
		                                                                                           true,
		                                                                                           new nestedReportUtils.RowConfig(
				                                                                                           new nestedReportUtils.FieldConfig(
						                                                                                           localization.getLocalizedMessage(
								                                                                                           "ReportWindows.FieldNotes",
								                                                                                           localization.getLocalizedMessage(
										                                                                                           "ReportWindows.FieldNotes",
										                                                                                           "notes")),
						                                                                                           12,
						                                                                                           nestedReportUtils.FieldType.TEXT_AREA))));
		return calloutReport;
	}
	
	public static Map<String, Object> newCallout() {
		Map<String, Object> calloutReport = calloutLayout();
		
		Map<String, Object> calloutReportMap = (Map<String, Object>) calloutReport.get(
				localization.getLocalizedMessage("ReportWindows.CalloutReportTitle", "Callout Report") + " Map");
		
		TextField officername = (TextField) calloutReportMap.get(
				localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
		TextField officerrank = (TextField) calloutReportMap.get(
				localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
		TextField officerdiv = (TextField) calloutReportMap.get(
				localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
		TextField officeragen = (TextField) calloutReportMap.get(
				localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
		TextField officernum = (TextField) calloutReportMap.get(
				localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));
		TextField calloutnum = (TextField) calloutReportMap.get(
				localization.getLocalizedMessage("ReportWindows.CalloutNumberField", "callout num"));
		ComboBox calloutarea = (ComboBox) calloutReportMap.get(
				localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
		TextArea calloutnotes = (TextArea) calloutReportMap.get(
				localization.getLocalizedMessage("ReportWindows.FieldNotes",
				                                 localization.getLocalizedMessage("ReportWindows.FieldNotes",
				                                                                  "notes")));
		TextField calloutcounty = (TextField) calloutReportMap.get(
				localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
		ComboBox calloutstreet = (ComboBox) calloutReportMap.get(
				localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
		TextField calloutdate = (TextField) calloutReportMap.get(
				localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
		TextField callouttime = (TextField) calloutReportMap.get(
				localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
		TextField callouttype = (TextField) calloutReportMap.get(
				localization.getLocalizedMessage("ReportWindows.FieldType",
				                                 localization.getLocalizedMessage("ReportWindows.FieldType", "type")));
		TextField calloutcode = (TextField) calloutReportMap.get(
				localization.getLocalizedMessage("ReportWindows.CalloutCodeField", "code"));
		
		try {
			officername.setText(ConfigReader.configRead("userInfo", "Name"));
			officerrank.setText(ConfigReader.configRead("userInfo", "Rank"));
			officerdiv.setText(ConfigReader.configRead("userInfo", "Division"));
			officeragen.setText(ConfigReader.configRead("userInfo", "Agency"));
			officernum.setText(ConfigReader.configRead("userInfo", "Number"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		calloutdate.setText(getDate());
		callouttime.setText(getTime());
		calloutnum.setText(generateReportNumber());
		
		MenuButton pullNotesBtn = (MenuButton) calloutReport.get("pullNotesBtn");
		pullNotesBtn.setPopupSide(Side.TOP);
		
		pullNotesBtn.setOnMouseEntered(actionEvent -> {
			pullNotesBtn.getItems().clear();
			if (notesViewController != null) {
				for (Tab tab : notesViewController.getTabPane().getTabs()) {
					MenuItem menuItem = new MenuItem(tab.getText());
					pullNotesBtn.getItems().add(menuItem);
					AnchorPane anchorPane = (AnchorPane) tab.getContent();
					TextArea noteArea = (TextArea) anchorPane.lookup("#notepadTextArea");
					if (noteArea != null) {
						menuItem.setOnAction(event2 -> {
							updateTextFromNotepad(calloutarea.getEditor(), noteArea, "-area");
							updateTextFromNotepad(calloutcounty, noteArea, "-county");
							updateTextFromNotepad(calloutstreet.getEditor(), noteArea, "-street");
							updateTextFromNotepad(calloutnum, noteArea, "-number");
							updateTextFromNotepad(calloutnotes, noteArea, "-notes");
						});
					}
				}
			} else {
				log("NotesViewController Is Null", LogUtils.Severity.ERROR);
			}
		});
		
		Button submitBtn = (Button) calloutReport.get("submitBtn");
		Label warningLabel = (Label) calloutReport.get("warningLabel");
		
		submitBtn.setOnAction(event -> {
			if (calloutnum.getText().trim().isEmpty()) {
				warningLabel.setVisible(true);
				warningLabel.setText("Callout Number can't be empty!");
				warningLabel.setStyle("-fx-font-family: \"Segoe UI Black\"; -fx-text-fill: red;");
				PauseTransition pause = new PauseTransition(Duration.seconds(2));
				pause.setOnFinished(e -> warningLabel.setVisible(false));
				pause.play();
			} else {
				for (String fieldName : calloutReportMap.keySet()) {
					Object field = calloutReportMap.get(fieldName);
					if (field instanceof ComboBox<?> comboBox) {
						if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
							comboBox.getSelectionModel().selectFirst();
						}
					}
				}
				
				CalloutReport callout1 = new CalloutReport();
				callout1.setDate(calloutdate.getText());
				callout1.setTime(callouttime.getText());
				callout1.setName(toTitleCase(officername.getText()));
				callout1.setRank(officerrank.getText());
				callout1.setNumber(toTitleCase(officernum.getText()));
				callout1.setDivision(toTitleCase(officerdiv.getText()));
				callout1.setAgency(toTitleCase(officeragen.getText()));
				callout1.setResponseType(toTitleCase(callouttype.getText()));
				callout1.setResponseGrade(toTitleCase(calloutcode.getText()));
				callout1.setCalloutNumber(toTitleCase(calloutnum.getText()));
				callout1.setNotesTextArea(calloutnotes.getText());
				callout1.setAddress(toTitleCase(calloutstreet.getEditor().getText()));
				callout1.setCounty(toTitleCase(calloutcounty.getText()));
				callout1.setArea(toTitleCase(calloutarea.getEditor().getText()));
				
				try {
					CalloutReportUtils.addCalloutReport(callout1);
				} catch (JAXBException e) {
					logError("Could not create new JAXB CalloutReport:", e);
				}
				
				try {
					if (ConfigReader.configRead("soundSettings", "playCreateReport").equalsIgnoreCase("true")) {
						playSound(getJarPath() + "/sounds/alert-success.wav");
					}
				} catch (IOException e) {
					logError("Error getting configValue for playCreateReport: ", e);
				}
				LogViewController.needRefresh.set(1);
				
				NotificationManager.showNotificationInfo("Report Manager", "A new Callout Report has been submitted.");
				CustomWindow window = getWindow("Callout Report");
				if (window != null) {
					window.closeWindow();
				}
			}
		});
		return calloutReport;
	}
	
	public static CalloutReports loadCalloutReports() throws JAXBException {
		File file = new File(calloutLogURL);
		if (!file.exists()) {
			return new CalloutReports();
		}
		
		try {
			JAXBContext context = JAXBContext.newInstance(CalloutReports.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (CalloutReports) unmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			logError("Error loading CalloutReports: ", e);
			throw e;
		}
	}
	
	private static void saveCalloutReports(CalloutReports CalloutReports) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(CalloutReports.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		File file = new File(calloutLogURL);
		marshaller.marshal(CalloutReports, file);
	}
	
	public static void addCalloutReport(CalloutReport CalloutReport) throws JAXBException {
		CalloutReports CalloutReports = loadCalloutReports();
		
		if (CalloutReports.getCalloutReportList() == null) {
			CalloutReports.setCalloutReportList(new java.util.ArrayList<>());
		}
		
		Optional<CalloutReport> existingReport = CalloutReports.getCalloutReportList().stream().filter(
				e -> e.getCalloutNumber().equals(CalloutReport.getCalloutNumber())).findFirst();
		
		if (existingReport.isPresent()) {
			CalloutReports.getCalloutReportList().remove(existingReport.get());
			CalloutReports.getCalloutReportList().add(CalloutReport);
			log("CalloutReport with number " + CalloutReport.getCalloutNumber() + " updated.", LogUtils.Severity.INFO);
		} else {
			CalloutReports.getCalloutReportList().add(CalloutReport);
			log("CalloutReport with number " + CalloutReport.getCalloutNumber() + " added.", LogUtils.Severity.INFO);
		}
		
		saveCalloutReports(CalloutReports);
	}
	
	public static void deleteCalloutReport(String CalloutReportnumber) throws JAXBException {
		CalloutReports CalloutReports = loadCalloutReports();
		
		if (CalloutReports.getCalloutReportList() != null) {
			CalloutReports.getCalloutReportList().removeIf(e -> e.getCalloutNumber().equals(CalloutReportnumber));
			saveCalloutReports(CalloutReports);
			log("CalloutReport with number " + CalloutReportnumber + " deleted.", LogUtils.Severity.INFO);
		}
	}
	
}