package com.Guess.ReportsPlus.logs.Incident;

import static com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager.getWindow;
import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.Windows.Apps.LogViewController.incidentLogUpdate;
import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.notesViewController;
import static com.Guess.ReportsPlus.util.Misc.AudioUtil.playSound;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logInfo;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getJarPath;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.toTitleCase;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.updateTextFromNotepad;
import static com.Guess.ReportsPlus.util.Report.reportUtil.createReportWindow;
import static com.Guess.ReportsPlus.util.Strings.URLStrings.incidentLogURL;

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

public class IncidentReportUtils {

	public static Map<String, Object> incidentLayout() {
		Map<String, Object> incidentReport = createReportWindow(
				localization.getLocalizedMessage("ReportWindows.IncidentReportTitle", "Incident Report"), null,
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
				new SectionConfig(
						localization.getLocalizedMessage("ReportWindows.TimeLocationInfoheader",
								"Timestamp / Location Information"),
						new RowConfig(
								new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"), 3,
										FieldType.DATE_FIELD),
								new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"), 4,
										FieldType.TIME_FIELD),
								new FieldConfig(localization.getLocalizedMessage("ReportWindows.IncidentNumField",
										"incident num"), 5, FieldType.NUMBER_FIELD)),
						new RowConfig(
								new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"),
										5, FieldType.COMBO_BOX_STREET),
								new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"), 4,
										FieldType.COMBO_BOX_AREA),
								new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"),
										3, FieldType.COUNTY_FIELD))),
				new SectionConfig(
						localization.getLocalizedMessage("ReportWindows.PartiesInvolvedSectionHeader",
								"Parties Involved"),
						new RowConfig(
								new FieldConfig(
										localization.getLocalizedMessage("ReportWindows.SuspectsField", "suspect(s)"),
										6, FieldType.TEXT_FIELD),
								new FieldConfig(localization.getLocalizedMessage("ReportWindows.VictimsWitnessField",
										"victim(s) / witness(s)"), 6, FieldType.TEXT_FIELD)),
						new RowConfig(new FieldConfig(
								localization.getLocalizedMessage("ReportWindows.StatementField", "statement"), 12,
								FieldType.TEXT_AREA))),
				new SectionConfig(
						localization.getLocalizedMessage("ReportWindows.NotesSummarySectionheader", "Notes / Summary"),
						new RowConfig(new FieldConfig(
								localization.getLocalizedMessage("ReportWindows.IncidentSummaryField", "summary"), 12,
								FieldType.TEXT_AREA)),
						new RowConfig(
								new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"),
										12, FieldType.TEXT_AREA))));
		return incidentReport;
	}

	public static Map<String, Object> newIncident() {
		Map<String, Object> incidentReport = incidentLayout();

		Map<String, Object> incidentReportMap = (Map<String, Object>) incidentReport
				.get(localization.getLocalizedMessage("ReportWindows.IncidentReportTitle", "Incident Report") + " Map");

		TextField name = (TextField) incidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
		TextField rank = (TextField) incidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
		TextField div = (TextField) incidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
		TextField agen = (TextField) incidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
		TextField num = (TextField) incidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));

		TextField incidentnum = (TextField) incidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.IncidentNumField", "incident num"));
		TextField date = (TextField) incidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
		TextField time = (TextField) incidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
		ComboBox street = (ComboBox) incidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
		ComboBox area = (ComboBox) incidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
		TextField county = (TextField) incidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));

		TextField suspects = (TextField) incidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.SuspectsField", "suspect(s)"));
		TextField vicwit = (TextField) incidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.VictimsWitnessField", "victim(s) / witness(s)"));
		TextArea statement = (TextArea) incidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.StatementField", "statement"));

		TextArea summary = (TextArea) incidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.IncidentSummaryField", "summary"));
		TextArea notes = (TextArea) incidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"));

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
				logError("NotesViewController Is Null");
			}
		});

		Button submitBtn = (Button) incidentReport.get("submitBtn");
		Label warningLabel = (Label) incidentReport.get("warningLabel");

		Label legacyLabel = (Label) incidentReport.get("legacyLabel");
		legacyLabel.setVisible(true);

		ComboBox<String> statusValue = (ComboBox) incidentReport.get("statusValue");

		submitBtn.setOnAction(event -> {
			if (incidentnum.getText().trim().isEmpty()) {
				warningLabel.setVisible(true);
				warningLabel.setText("Incident Number can't be empty!");
				warningLabel.setStyle("-fx-font-family: \"Inter 28pt Bold\"; -fx-text-fill: red;");
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
				incidentLogUpdate();

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

		Optional<IncidentReport> existingReport = IncidentReports.getIncidentReportList().stream()
				.filter(e -> e.getIncidentNumber().equals(IncidentReport.getIncidentNumber())).findFirst();

		if (existingReport.isPresent()) {
			IncidentReports.getIncidentReportList().remove(existingReport.get());
			IncidentReports.getIncidentReportList().add(IncidentReport);
			logInfo("IncidentReport with number " + IncidentReport.getIncidentNumber() + " updated.");
		} else {
			IncidentReports.getIncidentReportList().add(IncidentReport);
			logInfo("IncidentReport with number " + IncidentReport.getIncidentNumber() + " added.");
		}

		saveIncidentReports(IncidentReports);
	}

	public static void deleteIncidentReport(String IncidentReportnumber) throws JAXBException {
		IncidentReports IncidentReports = loadIncidentReports();

		if (IncidentReports.getIncidentReportList() != null) {
			IncidentReports.getIncidentReportList().removeIf(e -> e.getIncidentNumber().equals(IncidentReportnumber));
			saveIncidentReports(IncidentReports);
			logInfo("IncidentReport with number " + IncidentReportnumber + " deleted.");
		}
	}
}