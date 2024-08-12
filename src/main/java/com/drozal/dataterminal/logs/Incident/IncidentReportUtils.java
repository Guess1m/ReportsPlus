package com.drozal.dataterminal.logs.Incident;

import com.drozal.dataterminal.Windows.Main.actionController;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Misc.NotificationManager;
import com.drozal.dataterminal.util.Report.nestedReportUtils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.animation.PauseTransition;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.drozal.dataterminal.DataTerminalHomeApplication.*;
import static com.drozal.dataterminal.Windows.Main.actionController.notesViewController;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.controllerUtils.*;
import static com.drozal.dataterminal.util.Misc.stringUtil.incidentLogURL;
import static com.drozal.dataterminal.util.Report.reportUtil.createReportWindow;
import static com.drozal.dataterminal.util.Report.reportUtil.generateReportNumber;

public class IncidentReportUtils {
	
	public static int countReports() {
		try {
			List<IncidentReport> logs = IncidentReportUtils.loadIncidentReports().getIncidentReportList();
			
			if (logs == null) {
				return 0;
			}
			
			return logs.size();
		} catch (Exception e) {
			logError("Exception", e);
			return -1;
		}
	}
	
	public static Map<String, Object> incidentLayout() {
		Map<String, Object> incidentReport = createReportWindow("Incident Report", 5, 7, null,
		                                                        new nestedReportUtils.SectionConfig(
				                                                        "Officer Information", true,
				                                                        new nestedReportUtils.RowConfig(
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "name", 5,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD),
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "rank", 5,
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
				                                                        "Timestamp / Location Information", true,
				                                                        new nestedReportUtils.RowConfig(
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "date", 3,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD),
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "time", 4,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD),
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "incident num", 5,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD)),
				                                                        new nestedReportUtils.RowConfig(
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "street", 5,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD),
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "area", 4,
								                                                        nestedReportUtils.FieldType.COMBO_BOX_AREA),
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "county", 3,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                        new nestedReportUtils.SectionConfig("Parties Involved",
		                                                                                            false,
		                                                                                            new nestedReportUtils.RowConfig(
				                                                                                            new nestedReportUtils.FieldConfig(
						                                                                                            "suspect(s)",
						                                                                                            6,
						                                                                                            nestedReportUtils.FieldType.TEXT_FIELD),
				                                                                                            new nestedReportUtils.FieldConfig(
						                                                                                            "victim(s) / witness(s)",
						                                                                                            6,
						                                                                                            nestedReportUtils.FieldType.TEXT_FIELD)),
		                                                                                            new nestedReportUtils.RowConfig(
				                                                                                            new nestedReportUtils.FieldConfig(
						                                                                                            "statement",
						                                                                                            12,
						                                                                                            nestedReportUtils.FieldType.TEXT_AREA))),
		                                                        new nestedReportUtils.SectionConfig("Notes / Summary",
		                                                                                            true,
		                                                                                            new nestedReportUtils.RowConfig(
				                                                                                            new nestedReportUtils.FieldConfig(
						                                                                                            "summary",
						                                                                                            12,
						                                                                                            nestedReportUtils.FieldType.TEXT_AREA)),
		                                                                                            new nestedReportUtils.RowConfig(
				                                                                                            new nestedReportUtils.FieldConfig(
						                                                                                            "notes",
						                                                                                            12,
						                                                                                            nestedReportUtils.FieldType.TEXT_AREA))));
		return incidentReport;
	}
	
	public static Map<String, Object> newIncident(BarChart<String, Number> reportChart, AreaChart areaReportChart) {
		Map<String, Object> incidentReport = incidentLayout();
		
		Map<String, Object> incidentReportMap = (Map<String, Object>) incidentReport.get("Incident Report Map");
		
		TextField name = (TextField) incidentReportMap.get("name");
		TextField rank = (TextField) incidentReportMap.get("rank");
		TextField div = (TextField) incidentReportMap.get("division");
		TextField agen = (TextField) incidentReportMap.get("agency");
		TextField num = (TextField) incidentReportMap.get("number");
		
		TextField incidentnum = (TextField) incidentReportMap.get("incident num");
		TextField date = (TextField) incidentReportMap.get("date");
		TextField time = (TextField) incidentReportMap.get("time");
		TextField street = (TextField) incidentReportMap.get("street");
		ComboBox area = (ComboBox) incidentReportMap.get("area");
		TextField county = (TextField) incidentReportMap.get("county");
		
		TextField suspects = (TextField) incidentReportMap.get("suspect(s)");
		TextField vicwit = (TextField) incidentReportMap.get("victim(s) / witness(s)");
		TextArea statement = (TextArea) incidentReportMap.get("statement");
		
		TextArea summary = (TextArea) incidentReportMap.get("summary");
		TextArea notes = (TextArea) incidentReportMap.get("notes");
		
		BorderPane root = (BorderPane) incidentReport.get("root");
		Stage stage = (Stage) root.getScene().getWindow();
		
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
		
		Button pullNotesBtn = (Button) incidentReport.get("pullNotesBtn");
		
		pullNotesBtn.setOnAction(event -> {
			if (notesViewController != null) {
				updateTextFromNotepad(area.getEditor(), notesViewController.getNotepadTextArea(), "-area");
				updateTextFromNotepad(county, notesViewController.getNotepadTextArea(), "-county");
				updateTextFromNotepad(street, notesViewController.getNotepadTextArea(), "-street");
				updateTextFromNotepad(vicwit, notesViewController.getNotepadTextArea(), "-name");
				updateTextFromNotepad(notes, notesViewController.getNotepadTextArea(), "-comments");
				updateTextFromNotepad(incidentnum, notesViewController.getNotepadTextArea(), "-number");
			} else {
				log("NotesViewController Is Null", LogUtils.Severity.ERROR);
			}
		});
		
		Button submitBtn = (Button) incidentReport.get("submitBtn");
		Label warningLabel = (Label) incidentReport.get("warningLabel");
		
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
				incidentReport1.setIncidentArea(toTitleCase(area.getEditor().getText()));
				incidentReport1.setIncidentComments(notes.getText());
				incidentReport1.setIncidentActionsTaken(summary.getText());
				incidentReport1.setIncidentCounty(toTitleCase(county.getText()));
				incidentReport1.setIncidentDate(date.getText());
				incidentReport1.setIncidentNumber(toTitleCase(incidentnum.getText()));
				incidentReport1.setIncidentStatement(toTitleCase(statement.getText()));
				incidentReport1.setIncidentStreet(toTitleCase(street.getText()));
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
				
				actionController.needRefresh.set(1);
				updateChartIfMismatch(reportChart);
				refreshChart(areaReportChart, "area");
				NotificationManager.showNotificationInfo("Report Manager", "A new Incident Report has been submitted.",
				                                         mainRT);
				stage.close();
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
		
		Optional<IncidentReport> existingReport = IncidentReports.getIncidentReportList().stream().filter(
				e -> e.getIncidentNumber().equals(IncidentReport.getIncidentNumber())).findFirst();
		
		if (existingReport.isPresent()) {
			IncidentReports.getIncidentReportList().remove(existingReport.get());
			IncidentReports.getIncidentReportList().add(IncidentReport);
			log("IncidentReport with number " + IncidentReport.getIncidentNumber() + " updated.",
			    LogUtils.Severity.INFO);
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