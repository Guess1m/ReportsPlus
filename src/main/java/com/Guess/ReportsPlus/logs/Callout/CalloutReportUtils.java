package com.Guess.ReportsPlus.logs.Callout;

import static com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager.getWindow;
import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.Windows.Apps.LogViewController.calloutLogUpdate;
import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.notesViewController;
import static com.Guess.ReportsPlus.util.Misc.AudioUtil.playSound;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logInfo;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getJarPath;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.toTitleCase;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.updateTextFromNotepad;
import static com.Guess.ReportsPlus.util.Report.reportUtil.createReportWindow;
import static com.Guess.ReportsPlus.util.Strings.URLStrings.calloutLogURL;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.util.Misc.NotificationManager;
import com.Guess.ReportsPlus.util.Report.nestedReportUtils.FieldConfig;
import com.Guess.ReportsPlus.util.Report.nestedReportUtils.FieldType;
import com.Guess.ReportsPlus.util.Report.nestedReportUtils.RowConfig;
import com.Guess.ReportsPlus.util.Report.nestedReportUtils.SectionConfig;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.animation.PauseTransition;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class CalloutReportUtils {

	public static Map<String, Object> calloutLayout() {
		Map<String, Object> calloutReport = createReportWindow(
				localization.getLocalizedMessage("ReportWindows.CalloutReportTitle", "Callout Report"), null,
				new SectionConfig(
						localization.getLocalizedMessage("ReportWindows.OfficerInfoSectionHeading",
								"Officer Information"),
						new RowConfig(
								new FieldConfig(
										localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"), 5,
										FieldType.OFFICER_NAME),
								new FieldConfig(
										localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"), 5,
										FieldType.OFFICER_RANK),
								new FieldConfig(
										localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"),
										2, FieldType.OFFICER_NUMBER)),
						new RowConfig(
								new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision",
										"division"), 6, FieldType.OFFICER_DIVISION),
								new FieldConfig(
										localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"),
										6, FieldType.OFFICER_AGENCY))),
				new SectionConfig("Location Information",
						new RowConfig(
								new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"),
										5, FieldType.COMBO_BOX_STREET),
								new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"), 4,
										FieldType.COMBO_BOX_AREA),
								new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"),
										3, FieldType.COUNTY_FIELD))),
				new SectionConfig(localization.getLocalizedMessage("CalloutPopup.MainHeading", "Callout Information"),
						new RowConfig(
								new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"), 6,
										FieldType.DATE_FIELD),
								new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"), 6,
										FieldType.TIME_FIELD)),
						new RowConfig(
								new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldType", "type"), 4,
										FieldType.TEXT_FIELD),
								new FieldConfig(
										localization.getLocalizedMessage("ReportWindows.CalloutCodeField", "code"), 4,
										FieldType.TEXT_FIELD),
								new FieldConfig(localization.getLocalizedMessage("ReportWindows.CalloutNumberField",
										"callout num"), 4, FieldType.NUMBER_FIELD))),
				new SectionConfig(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"),
						new RowConfig(
								new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"),
										12, FieldType.TEXT_AREA))));
		return calloutReport;
	}

	public static Map<String, Object> newCallout() {
		Map<String, Object> calloutReport = calloutLayout();

		Map<String, Object> calloutReportMap = (Map<String, Object>) calloutReport
				.get(localization.getLocalizedMessage("ReportWindows.CalloutReportTitle", "Callout Report") + " Map");

		TextField officername = (TextField) calloutReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
		TextField officerrank = (TextField) calloutReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
		TextField officerdiv = (TextField) calloutReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
		TextField officeragen = (TextField) calloutReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
		TextField officernum = (TextField) calloutReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));
		TextField calloutnum = (TextField) calloutReportMap
				.get(localization.getLocalizedMessage("ReportWindows.CalloutNumberField", "callout num"));
		ComboBox calloutarea = (ComboBox) calloutReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
		TextArea calloutnotes = (TextArea) calloutReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"));
		TextField calloutcounty = (TextField) calloutReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
		ComboBox calloutstreet = (ComboBox) calloutReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
		TextField calloutdate = (TextField) calloutReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
		TextField callouttime = (TextField) calloutReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
		TextField callouttype = (TextField) calloutReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldType", "type"));
		TextField calloutcode = (TextField) calloutReportMap
				.get(localization.getLocalizedMessage("ReportWindows.CalloutCodeField", "code"));

		MenuButton pullNotesBtn = (MenuButton) calloutReport.get("pullNotesBtn");
		pullNotesBtn.setPopupSide(Side.TOP);

		Label legacyLabel = (Label) calloutReport.get("legacyLabel");
		legacyLabel.setVisible(true);

		pullNotesBtn.setOnMouseEntered(_ -> {
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
				logError("NotesViewController Is Null");
			}
		});

		Button submitBtn = (Button) calloutReport.get("submitBtn");
		Label warningLabel = (Label) calloutReport.get("warningLabel");
		ComboBox<String> statusValue = (ComboBox) calloutReport.get("statusValue");

		submitBtn.setOnAction(_ -> {
			if (calloutnum.getText().trim().isEmpty()) {
				warningLabel.setVisible(true);
				warningLabel.setText("Callout Number can't be empty!");
				warningLabel.setStyle("-fx-font-family: \"Inter 28pt Bold\"; -fx-text-fill: red;");
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
				callout1.setStatus(statusValue.getValue());
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
				calloutLogUpdate();

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

		Optional<CalloutReport> existingReport = CalloutReports.getCalloutReportList().stream()
				.filter(e -> e.getCalloutNumber().equals(CalloutReport.getCalloutNumber())).findFirst();

		if (existingReport.isPresent()) {
			CalloutReports.getCalloutReportList().remove(existingReport.get());
			CalloutReports.getCalloutReportList().add(CalloutReport);
			logInfo("CalloutReport with number " + CalloutReport.getCalloutNumber() + " updated.");
		} else {
			CalloutReports.getCalloutReportList().add(CalloutReport);
			logInfo("CalloutReport with number " + CalloutReport.getCalloutNumber() + " added.");
		}

		saveCalloutReports(CalloutReports);
	}

	public static void deleteCalloutReport(String CalloutReportnumber) throws JAXBException {
		CalloutReports CalloutReports = loadCalloutReports();

		if (CalloutReports.getCalloutReportList() != null) {
			CalloutReports.getCalloutReportList().removeIf(e -> e.getCalloutNumber().equals(CalloutReportnumber));
			saveCalloutReports(CalloutReports);
			logInfo("CalloutReport with number " + CalloutReportnumber + " deleted.");
		}
	}

}