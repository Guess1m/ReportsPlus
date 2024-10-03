package com.drozal.dataterminal.logs.Callout;

import com.drozal.dataterminal.Windows.Apps.LogViewController;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Misc.NotificationManager;
import com.drozal.dataterminal.util.Report.nestedReportUtils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.animation.PauseTransition;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.drozal.dataterminal.DataTerminalHomeApplication.getDate;
import static com.drozal.dataterminal.DataTerminalHomeApplication.getTime;
import static com.drozal.dataterminal.Windows.Other.NotesViewController.notesViewController;
import static com.drozal.dataterminal.util.Misc.AudioUtil.playSound;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.controllerUtils.toTitleCase;
import static com.drozal.dataterminal.util.Misc.controllerUtils.updateTextFromNotepad;
import static com.drozal.dataterminal.util.Misc.stringUtil.calloutLogURL;
import static com.drozal.dataterminal.util.Misc.stringUtil.getJarPath;
import static com.drozal.dataterminal.util.Report.reportUtil.createReportWindow;
import static com.drozal.dataterminal.util.Report.reportUtil.generateReportNumber;

public class CalloutReportUtils {
	
	public static int countReports() {
		try {
			List<CalloutReport> logs = CalloutReportUtils.loadCalloutReports().getCalloutReportList();
			
			if (logs == null) {
				return 0;
			}
			
			return logs.size();
		} catch (Exception e) {
			logError("Exception", e);
			return -1;
		}
	}
	
	public static Map<String, Object> calloutLayout() {
		Map<String, Object> calloutReport = createReportWindow("Callout Report", 6, 7, null,
		                                                       new nestedReportUtils.SectionConfig(
				                                                       "Officer Information", true,
				                                                       new nestedReportUtils.RowConfig(
						                                                       new nestedReportUtils.FieldConfig("name",
						                                                                                         5,
						                                                                                         nestedReportUtils.FieldType.TEXT_FIELD),
						                                                       new nestedReportUtils.FieldConfig("rank",
						                                                                                         5,
						                                                                                         nestedReportUtils.FieldType.TEXT_FIELD),
						                                                       new nestedReportUtils.FieldConfig(
								                                                       "number", 2,
								                                                       nestedReportUtils.FieldType.TEXT_FIELD)),
				                                                       new nestedReportUtils.RowConfig(
						                                                       new nestedReportUtils.FieldConfig(
								                                                       "division", 6,
								                                                       nestedReportUtils.FieldType.TEXT_FIELD),
						                                                       new nestedReportUtils.FieldConfig(
								                                                       "agency", 6,
								                                                       nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                       new nestedReportUtils.SectionConfig(
				                                                       "Location Information", true,
				                                                       new nestedReportUtils.RowConfig(
						                                                       new nestedReportUtils.FieldConfig(
								                                                       "street", 5,
								                                                       nestedReportUtils.FieldType.COMBO_BOX_STREET),
						                                                       new nestedReportUtils.FieldConfig("area",
						                                                                                         4,
						                                                                                         nestedReportUtils.FieldType.COMBO_BOX_AREA),
						                                                       new nestedReportUtils.FieldConfig(
								                                                       "county", 3,
								                                                       nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                       new nestedReportUtils.SectionConfig(
				                                                       "Callout Information", true,
				                                                       new nestedReportUtils.RowConfig(
						                                                       new nestedReportUtils.FieldConfig("date",
						                                                                                         6,
						                                                                                         nestedReportUtils.FieldType.TEXT_FIELD),
						                                                       new nestedReportUtils.FieldConfig("time",
						                                                                                         6,
						                                                                                         nestedReportUtils.FieldType.TEXT_FIELD)),
				                                                       new nestedReportUtils.RowConfig(
						                                                       new nestedReportUtils.FieldConfig("type",
						                                                                                         4,
						                                                                                         nestedReportUtils.FieldType.TEXT_FIELD),
						                                                       new nestedReportUtils.FieldConfig("code",
						                                                                                         4,
						                                                                                         nestedReportUtils.FieldType.TEXT_FIELD),
						                                                       new nestedReportUtils.FieldConfig(
								                                                       "calloutnumber", 4,
								                                                       nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                       new nestedReportUtils.SectionConfig("Callout Notes",
		                                                                                           true,
		                                                                                           new nestedReportUtils.RowConfig(
				                                                                                           new nestedReportUtils.FieldConfig(
						                                                                                           "notes",
						                                                                                           12,
						                                                                                           nestedReportUtils.FieldType.TEXT_AREA))));
		return calloutReport;
	}
	
	public static Map<String, Object> newCallout() {
		Map<String, Object> calloutReport = calloutLayout();
		
		Map<String, Object> calloutReportMap = (Map<String, Object>) calloutReport.get("Callout Report Map");
		
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
		BorderPane root = (BorderPane) calloutReport.get("root");
		TextField calloutcode = (TextField) calloutReportMap.get("code");
		
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
				Stage rootstage = (Stage) root.getScene().getWindow();
				rootstage.close();
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