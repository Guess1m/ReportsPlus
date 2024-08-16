package com.drozal.dataterminal.logs.Patrol;

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
import javafx.geometry.Side;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
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

import static com.drozal.dataterminal.DataTerminalHomeApplication.*;
import static com.drozal.dataterminal.Windows.Main.actionController.notesViewController;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.controllerUtils.*;
import static com.drozal.dataterminal.util.Misc.stringUtil.patrolLogURL;
import static com.drozal.dataterminal.util.Report.reportUtil.createReportWindow;
import static com.drozal.dataterminal.util.Report.reportUtil.generateReportNumber;

public class PatrolReportUtils {
	
	public static int countReports() {
		try {
			List<PatrolReport> logs = PatrolReportUtils.loadPatrolReports().getPatrolReportList();
			
			if (logs == null) {
				return 0;
			}
			
			return logs.size();
		} catch (Exception e) {
			logError("Exception", e);
			return -1;
		}
	}
	
	public static Map<String, Object> patrolLayout() {
		Map<String, Object> patrolReport = createReportWindow("Patrol Report", 6, 7, null,
		                                                      new nestedReportUtils.SectionConfig("Officer Information",
		                                                                                          true,
		                                                                                          new nestedReportUtils.RowConfig(
				                                                                                          new nestedReportUtils.FieldConfig(
						                                                                                          "name",
						                                                                                          5,
						                                                                                          nestedReportUtils.FieldType.TEXT_FIELD),
				                                                                                          new nestedReportUtils.FieldConfig(
						                                                                                          "rank",
						                                                                                          5,
						                                                                                          nestedReportUtils.FieldType.TEXT_FIELD),
				                                                                                          new nestedReportUtils.FieldConfig(
						                                                                                          "number",
						                                                                                          2,
						                                                                                          nestedReportUtils.FieldType.TEXT_FIELD)),
		                                                                                          new nestedReportUtils.RowConfig(
				                                                                                          new nestedReportUtils.FieldConfig(
						                                                                                          "division",
						                                                                                          6,
						                                                                                          nestedReportUtils.FieldType.TEXT_FIELD),
				                                                                                          new nestedReportUtils.FieldConfig(
						                                                                                          "agency",
						                                                                                          6,
						                                                                                          nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                      new nestedReportUtils.SectionConfig("Shift Information",
		                                                                                          true,
		                                                                                          new nestedReportUtils.RowConfig(
				                                                                                          new nestedReportUtils.FieldConfig(
						                                                                                          "starttime",
						                                                                                          3,
						                                                                                          nestedReportUtils.FieldType.TEXT_FIELD),
				                                                                                          new nestedReportUtils.FieldConfig(
						                                                                                          "stoptime",
						                                                                                          4,
						                                                                                          nestedReportUtils.FieldType.TEXT_FIELD),
				                                                                                          new nestedReportUtils.FieldConfig(
						                                                                                          "patrolnumber",
						                                                                                          5,
						                                                                                          nestedReportUtils.FieldType.TEXT_FIELD)),
		                                                                                          new nestedReportUtils.RowConfig(
				                                                                                          new nestedReportUtils.FieldConfig(
						                                                                                          "length",
						                                                                                          3,
						                                                                                          nestedReportUtils.FieldType.TEXT_FIELD),
				                                                                                          new nestedReportUtils.FieldConfig(
						                                                                                          "date",
						                                                                                          3,
						                                                                                          nestedReportUtils.FieldType.TEXT_FIELD),
				                                                                                          new nestedReportUtils.FieldConfig(
						                                                                                          "vehicle",
						                                                                                          6,
						                                                                                          nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                      new nestedReportUtils.SectionConfig("Patrol Notes", true,
		                                                                                          new nestedReportUtils.RowConfig(
				                                                                                          new nestedReportUtils.FieldConfig(
						                                                                                          "notes",
						                                                                                          12,
						                                                                                          nestedReportUtils.FieldType.TEXT_AREA))));
		return patrolReport;
	}
	
	public static Map<String, Object> newPatrol(BarChart<String, Number> reportChart, AreaChart areaReportChart) {
		Map<String, Object> patrolReport = patrolLayout();
		
		Map<String, Object> patrolReportMap = (Map<String, Object>) patrolReport.get("Patrol Report Map");
		
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
		
		BorderPane root = (BorderPane) patrolReport.get("root");
		Stage stage = (Stage) root.getScene().getWindow();
		
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
		stoptime.setText(getTime());
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
				
				actionController.needRefresh.set(1);
				updateChartIfMismatch(reportChart);
				refreshChart(areaReportChart, "area");
				NotificationManager.showNotificationInfo("Report Manager", "A new Patrol Report has been submitted.",
				                                         mainRT);
				
				stage.close();
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
		
		Optional<PatrolReport> existingReport = PatrolReports.getPatrolReportList().stream().filter(
				e -> e.getPatrolNumber().equals(PatrolReport.getPatrolNumber())).findFirst();
		
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