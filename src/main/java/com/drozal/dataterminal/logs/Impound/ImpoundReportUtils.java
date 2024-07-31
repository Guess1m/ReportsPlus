package com.drozal.dataterminal.logs.Impound;

import com.drozal.dataterminal.NotesViewController;
import com.drozal.dataterminal.actionController;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Report.nestedReportUtils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.drozal.dataterminal.DataTerminalHomeApplication.*;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.controllerUtils.*;
import static com.drozal.dataterminal.util.Misc.stringUtil.calloutLogURL;
import static com.drozal.dataterminal.util.Report.reportCreationUtil.generateReportNumber;
import static com.drozal.dataterminal.util.Report.reportUtil.createReportWindow;

public class ImpoundReportUtils {
	
	public static int countReports() {
		try {
			List<ImpoundReport> logs = ImpoundReportUtils.loadImpoundReports().getImpoundReportList();
			
			if (logs == null) {
				return 0;
			}
			
			return logs.size();
		} catch (Exception e) {
			logError("Exception", e);
			return -1;
		}
	}
	
	public static Map<String, Object> impoundLayout() {
		Map<String, Object> impoundReport = createReportWindow("Impound Report", 7, 9, null,
		                                                       new nestedReportUtils.SectionConfig("Officer Information", true,
		                                                                                           new nestedReportUtils.RowConfig(
				                                                                                           new nestedReportUtils.FieldConfig("name", 5,
				                                                                                                                             nestedReportUtils.FieldType.TEXT_FIELD),
				                                                                                           new nestedReportUtils.FieldConfig("rank", 5,
				                                                                                                                             nestedReportUtils.FieldType.TEXT_FIELD),
				                                                                                           new nestedReportUtils.FieldConfig("number", 2,
				                                                                                                                             nestedReportUtils.FieldType.TEXT_FIELD)),
		                                                                                           new nestedReportUtils.RowConfig(
				                                                                                           new nestedReportUtils.FieldConfig("division", 6,
				                                                                                                                             nestedReportUtils.FieldType.TEXT_FIELD),
				                                                                                           new nestedReportUtils.FieldConfig("agency", 6,
				                                                                                                                             nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                       new nestedReportUtils.SectionConfig("Location / Timestamp Information",
		                                                                                           true, new nestedReportUtils.RowConfig(
				                                                       new nestedReportUtils.FieldConfig("date", 5, nestedReportUtils.FieldType.TEXT_FIELD),
				                                                       new nestedReportUtils.FieldConfig("time", 5, nestedReportUtils.FieldType.TEXT_FIELD),
				                                                       new nestedReportUtils.FieldConfig("citation number", 2,
				                                                                                         nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                       new nestedReportUtils.SectionConfig("Offender Information", true,
		                                                                                           new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(
				                                                                                           "offender name", 4,
				                                                                                           nestedReportUtils.FieldType.TEXT_FIELD),
		                                                                                                                           new nestedReportUtils.FieldConfig(
				                                                                                                                           "offender age",
				                                                                                                                           4,
				                                                                                                                           nestedReportUtils.FieldType.TEXT_FIELD),
		                                                                                                                           new nestedReportUtils.FieldConfig(
				                                                                                                                           "offender gender",
				                                                                                                                           4,
				                                                                                                                           nestedReportUtils.FieldType.TEXT_FIELD)),
		                                                                                           new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(
				                                                                                           "offender address", 12,
				                                                                                           nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                       new nestedReportUtils.SectionConfig("Offender Vehicle Information", true,
		                                                                                           new nestedReportUtils.RowConfig(
				                                                                                           new nestedReportUtils.FieldConfig("model", 6,
				                                                                                                                             nestedReportUtils.FieldType.TEXT_FIELD),
				                                                                                           new nestedReportUtils.FieldConfig("plate number",
				                                                                                                                             6,
				                                                                                                                             nestedReportUtils.FieldType.TEXT_FIELD)),
		                                                                                           new nestedReportUtils.RowConfig(
				                                                                                           new nestedReportUtils.FieldConfig("type", 7,
				                                                                                                                             nestedReportUtils.FieldType.COMBO_BOX_TYPE),
				                                                                                           new nestedReportUtils.FieldConfig("color", 5,
				                                                                                                                             nestedReportUtils.FieldType.COMBO_BOX_COLOR))),
		                                                       new nestedReportUtils.SectionConfig("Citation Notes", true, new nestedReportUtils.RowConfig(
				                                                       new nestedReportUtils.FieldConfig("notes", 12,
				                                                                                         nestedReportUtils.FieldType.TEXT_AREA))));
		return impoundReport;
	}
	
	public static void newImpound(BarChart<String, Number> reportChart, AreaChart areaReportChart, Object vbox, NotesViewController notesViewController) {
		Map<String, Object> impoundReport = impoundLayout();
		
		Map<String, Object> impoundReportMap = (Map<String, Object>) impoundReport.get("Impound Report Map");
		
		TextField officername = (TextField) impoundReportMap.get("name");
		TextField officerrank = (TextField) impoundReportMap.get("rank");
		TextField officerdiv = (TextField) impoundReportMap.get("division");
		TextField officeragen = (TextField) impoundReportMap.get("agency");
		TextField officernum = (TextField) impoundReportMap.get("number");
		
		TextField offenderName = (TextField) impoundReportMap.get("offender name");
		TextField offenderAge = (TextField) impoundReportMap.get("offender age");
		TextField offenderGender = (TextField) impoundReportMap.get("offender gender");
		TextField offenderAddress = (TextField) impoundReportMap.get("offender address");
		
		TextField num = (TextField) impoundReportMap.get("citation number");
		TextField date = (TextField) impoundReportMap.get("date");
		TextField time = (TextField) impoundReportMap.get("time");
		
		ComboBox color = (ComboBox) impoundReportMap.get("color");
		ComboBox type = (ComboBox) impoundReportMap.get("type");
		TextField plateNumber = (TextField) impoundReportMap.get("plate number");
		TextField model = (TextField) impoundReportMap.get("model");
		
		TextArea notes = (TextArea) impoundReportMap.get("notes");
		
		BorderPane root = (BorderPane) impoundReport.get("root");
		Stage stage = (Stage) root.getScene().getWindow();
		
		Label warningLabel = (Label) impoundReport.get("warningLabel");
		Button pullNotesBtn = (Button) impoundReport.get("pullNotesBtn");
		
		try {
			officername.setText(ConfigReader.configRead("userInfo", "Name"));
			officerrank.setText(ConfigReader.configRead("userInfo", "Rank"));
			officerdiv.setText(ConfigReader.configRead("userInfo", "Division"));
			officeragen.setText(ConfigReader.configRead("userInfo", "Agency"));
			officernum.setText(ConfigReader.configRead("userInfo", "Number"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		date.setText(getDate());
		time.setText(getTime());
		
		pullNotesBtn.setOnAction(event -> {
			if (notesViewController != null) {
				updateTextFromNotepad(offenderName, notesViewController.getNotepadTextArea(), "-name");
				updateTextFromNotepad(offenderAge, notesViewController.getNotepadTextArea(), "-age");
				updateTextFromNotepad(offenderGender, notesViewController.getNotepadTextArea(), "-gender");
				updateTextFromNotepad(notes, notesViewController.getNotepadTextArea(), "-comments");
				updateTextFromNotepad(offenderAddress, notesViewController.getNotepadTextArea(), "-address");
				updateTextFromNotepad(model, notesViewController.getNotepadTextArea(), "-model");
				updateTextFromNotepad(plateNumber, notesViewController.getNotepadTextArea(), "-plate");
				updateTextFromNotepad(num, notesViewController.getNotepadTextArea(), "-number");
			} else {
				log("NotesViewController Is Null", LogUtils.Severity.ERROR);
			}
		});
		
		Button submitBtn = (Button) impoundReport.get("submitBtn");
		submitBtn.setOnAction(event -> {
			
			for (String fieldName : impoundReportMap.keySet()) {
				Object field = impoundReportMap.get(fieldName);
				if (field instanceof ComboBox<?> comboBox) {
					if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
						comboBox.getSelectionModel().selectFirst();
					}
				}
			}
			List<ImpoundLogEntry> logs = ImpoundReportLogs.loadLogsFromXML();
			
			logs.add(new ImpoundLogEntry(num.getText(), date.getText(), time.getText(), offenderName.getText(),
			                             offenderAge.getText(), offenderGender.getText(), offenderAddress.getText(),
			                             plateNumber.getText(), model.getText(), type.getValue().toString(),
			                             color.getValue().toString(), notes.getText(), officerrank.getText(),
			                             officername.getText(), officernum.getText(), officerdiv.getText(),
			                             officeragen.getText()));
			ImpoundReportLogs.saveLogsToXML(logs);
			actionController.needRefresh.set(1);
			updateChartIfMismatch(reportChart);
			refreshChart(areaReportChart, "area");
			showNotificationInfo("Report Manager", "A new Impound Report has been submitted.", mainRT);
			stage.close();
		});
	}
	
	public static ImpoundReports loadImpoundReports() throws JAXBException {
		File file = new File(calloutLogURL);
		if (!file.exists()) {
			return new ImpoundReports();
		}
		
		try {
			JAXBContext context = JAXBContext.newInstance(ImpoundReports.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (ImpoundReports) unmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			logError("Error loading ImpoundReports: ", e);
			throw e;
		}
	}
	
	private static void saveImpoundReports(ImpoundReports ImpoundReports) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(ImpoundReports.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		File file = new File(calloutLogURL);
		marshaller.marshal(ImpoundReports, file);
	}
	
	public static void addImpoundReport(ImpoundReport ImpoundReport) throws JAXBException {
		ImpoundReports ImpoundReports = loadImpoundReports();
		
		if (ImpoundReports.getImpoundReportList() == null) {
			ImpoundReports.setImpoundReportList(new java.util.ArrayList<>());
		}
		
		Optional<ImpoundReport> existingReport = ImpoundReports.getImpoundReportList().stream().filter(
				e -> e.getCalloutNumber().equals(ImpoundReport.getCalloutNumber())).findFirst();
		
		if (existingReport.isPresent()) {
			ImpoundReports.getImpoundReportList().remove(existingReport.get());
			ImpoundReports.getImpoundReportList().add(ImpoundReport);
			log("ImpoundReport with number " + ImpoundReport.getCalloutNumber() + " updated.", LogUtils.Severity.INFO);
		} else {
			ImpoundReports.getImpoundReportList().add(ImpoundReport);
			log("ImpoundReport with number " + ImpoundReport.getCalloutNumber() + " added.", LogUtils.Severity.INFO);
		}
		
		saveImpoundReports(ImpoundReports);
	}
	
	public static void deleteImpoundReport(String ImpoundReportnumber) throws JAXBException {
		ImpoundReports ImpoundReports = loadImpoundReports();
		
		if (ImpoundReports.getImpoundReportList() != null) {
			ImpoundReports.getImpoundReportList().removeIf(e -> e.getCalloutNumber().equals(ImpoundReportnumber));
			saveImpoundReports(ImpoundReports);
		}
	}
	
	public static Optional<ImpoundReport> findImpoundReportByNumber(String ImpoundReportnumber) throws JAXBException {
		ImpoundReports ImpoundReports = loadImpoundReports();
		
		if (ImpoundReports.getImpoundReportList() != null) {
			return ImpoundReports.getImpoundReportList().stream().filter(
					e -> e.getCalloutNumber().equals(ImpoundReportnumber)).findFirst();
		}
		
		return Optional.empty();
	}
	
	public static void modifyImpoundReport(String number, ImpoundReport updatedImpoundReport) throws JAXBException {
		ImpoundReports ImpoundReports = loadImpoundReports();
		
		if (ImpoundReports.getImpoundReportList() != null) {
			for (int i = 0; i < ImpoundReports.getImpoundReportList().size(); i++) {
				ImpoundReport e = ImpoundReports.getImpoundReportList().get(i);
				if (e.getCalloutNumber().equals(number)) {
					ImpoundReports.getImpoundReportList().set(i, updatedImpoundReport);
					saveImpoundReports(ImpoundReports);
					return;
				}
			}
		}
	}
	
}