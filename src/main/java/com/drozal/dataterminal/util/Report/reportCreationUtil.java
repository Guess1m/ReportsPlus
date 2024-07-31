package com.drozal.dataterminal.util.Report;

import com.drozal.dataterminal.NotesViewController;
import com.drozal.dataterminal.actionController;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.logs.Arrest.ArrestReportUtils;
import com.drozal.dataterminal.logs.ChargesData;
import com.drozal.dataterminal.logs.CitationsData;
import com.drozal.dataterminal.logs.Impound.ImpoundReportUtils;
import com.drozal.dataterminal.logs.Incident.IncidentReportUtils;
import com.drozal.dataterminal.logs.Search.SearchReportUtils;
import com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationUtils;
import com.drozal.dataterminal.logs.TrafficStop.TrafficStopLogEntry;
import com.drozal.dataterminal.logs.TrafficStop.TrafficStopReportLogs;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.server.Objects.CourtData.Case;
import com.drozal.dataterminal.util.server.Objects.CourtData.CourtUtils;
import jakarta.xml.bind.JAXBException;
import javafx.collections.ObservableList;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.drozal.dataterminal.DataTerminalHomeApplication.*;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.controllerUtils.*;
import static com.drozal.dataterminal.util.Report.Layouts.*;
import static com.drozal.dataterminal.util.Report.treeViewUtils.findXMLValue;
import static com.drozal.dataterminal.util.server.Objects.CourtData.CourtUtils.generateCaseNumber;
import static com.drozal.dataterminal.util.server.Objects.CourtData.CourtUtils.parseCourtData;

public class reportCreationUtil {
	
	public static void newTrafficStop(BarChart<String, Number> reportChart, AreaChart areaReportChart, Object vbox, NotesViewController notesViewController) {
		Map<String, Object> trafficStopReport = trafficStopLayout();
		
		Map<String, Object> trafficStopReportMap = (Map<String, Object>) trafficStopReport.get(
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
		TextField streetts = (TextField) trafficStopReportMap.get("street");
		TextField countyts = (TextField) trafficStopReportMap.get("county");
		TextField stopnumts = (TextField) trafficStopReportMap.get("stop number");
		TextField datets = (TextField) trafficStopReportMap.get("date");
		TextField timets = (TextField) trafficStopReportMap.get("time");
		
		TextArea notests = (TextArea) trafficStopReportMap.get("notes");
		
		Button transferarrestbtnts = (Button) trafficStopReportMap.get("transferarrestbtn");
		transferarrestbtnts.setText("New Arrest Report");
		Button transfercitationbtnts = (Button) trafficStopReportMap.get("transfercitationbtn");
		transfercitationbtnts.setText("New Citation Report");
		
		BorderPane rootts = (BorderPane) trafficStopReport.get("root");
		Stage stagets = (Stage) rootts.getScene().getWindow();
		
		Label warningLabelts = (Label) trafficStopReport.get("warningLabel");
		Button pullNotesBtnts = (Button) trafficStopReport.get("pullNotesBtn");
		
		try {
			officernamets.setText(ConfigReader.configRead("userInfo", "Name"));
			officerrankts.setText(ConfigReader.configRead("userInfo", "Rank"));
			officerdivts.setText(ConfigReader.configRead("userInfo", "Division"));
			officeragents.setText(ConfigReader.configRead("userInfo", "Agency"));
			officernumarrestts.setText(ConfigReader.configRead("userInfo", "Number"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		datets.setText(getDate());
		timets.setText(getTime());
		
		pullNotesBtnts.setOnAction(event -> {
			if (notesViewController != null) {
				updateTextFromNotepad(areats.getEditor(), notesViewController.getNotepadTextArea(), "-area");
				updateTextFromNotepad(countyts, notesViewController.getNotepadTextArea(), "-county");
				updateTextFromNotepad(streetts, notesViewController.getNotepadTextArea(), "-street");
				updateTextFromNotepad(offenderNamets, notesViewController.getNotepadTextArea(), "-name");
				updateTextFromNotepad(offenderAgets, notesViewController.getNotepadTextArea(), "-age");
				updateTextFromNotepad(offenderGenderts, notesViewController.getNotepadTextArea(), "-gender");
				updateTextFromNotepad(offenderDescriptionts, notesViewController.getNotepadTextArea(), "-description");
				updateTextFromNotepad(notests, notesViewController.getNotepadTextArea(), "-comments");
				updateTextFromNotepad(offenderAddressts, notesViewController.getNotepadTextArea(), "-address");
				updateTextFromNotepad(modelts, notesViewController.getNotepadTextArea(), "-model");
				updateTextFromNotepad(plateNumberts, notesViewController.getNotepadTextArea(), "-plate");
				updateTextFromNotepad(stopnumts, notesViewController.getNotepadTextArea(), "-number");
			} else {
				log("NotesViewController Is Null", LogUtils.Severity.ERROR);
			}
		});
		
		transferarrestbtnts.setOnAction(event -> {
			Map<String, Object> arrestReportObj = ArrestReportUtils.newArrest(reportChart, areaReportChart,
			                                                                  notesViewController);
			
			Map<String, Object> arrestReportMap = (Map<String, Object>) arrestReportObj.get("Arrest Report Map");
			
			TextField officernamearr = (TextField) arrestReportMap.get("name");
			TextField officerrankarr = (TextField) arrestReportMap.get("rank");
			TextField officerdivarr = (TextField) arrestReportMap.get("division");
			TextField officeragenarr = (TextField) arrestReportMap.get("agency");
			TextField officernumarrestarr = (TextField) arrestReportMap.get("number");
			
			TextField offenderNamearr = (TextField) arrestReportMap.get("offender name");
			TextField offenderAgearr = (TextField) arrestReportMap.get("offender age");
			TextField offenderGenderarr = (TextField) arrestReportMap.get("offender gender");
			TextField offenderAddressarr = (TextField) arrestReportMap.get("offender address");
			TextField offenderDescriptionarr = (TextField) arrestReportMap.get("offender description");
			
			ComboBox areaarr = (ComboBox) arrestReportMap.get("area");
			TextField streetarr = (TextField) arrestReportMap.get("street");
			TextField countyarr = (TextField) arrestReportMap.get("county");
			TextField arrestnumarr = (TextField) arrestReportMap.get("arrest number");
			TextField datearr = (TextField) arrestReportMap.get("date");
			TextField timearr = (TextField) arrestReportMap.get("time");
			
			TextArea notesarr = (TextArea) arrestReportMap.get("notes");
			
			officernamearr.setText(officernamets.getText());
			officerdivarr.setText(officerdivts.getText());
			officerrankarr.setText(officerrankts.getText());
			officeragenarr.setText(officeragents.getText());
			officernumarrestarr.setText(officernumarrestts.getText());
			timearr.setText(timets.getText());
			datearr.setText(datets.getText());
			offenderNamearr.setText(offenderNamets.getText());
			offenderAddressarr.setText(offenderAddressts.getText());
			offenderGenderarr.setText(offenderGenderts.getText());
			offenderAgearr.setText(offenderAgets.getText());
			offenderDescriptionarr.setText(offenderDescriptionts.getText());
			areaarr.setValue(areats.getEditor().getText());
			countyarr.setText(countyts.getText());
			streetarr.setText(streetts.getText());
			arrestnumarr.setText(stopnumts.getText());
			notesarr.setText(notests.getText());
		});
		
		transfercitationbtnts.setOnAction(event -> {
			Map<String, Object> trafficCitationObj = TrafficCitationUtils.newCitation(reportChart, areaReportChart,
			                                                                          notesViewController);
			
			Map<String, Object> citationReportMap = (Map<String, Object>) trafficCitationObj.get("Citation Report Map");
			
			TextField officernamecit = (TextField) citationReportMap.get("name");
			TextField officerrankcit = (TextField) citationReportMap.get("rank");
			TextField officerdivcit = (TextField) citationReportMap.get("division");
			TextField officeragencit = (TextField) citationReportMap.get("agency");
			TextField officernumcit = (TextField) citationReportMap.get("number");
			
			TextField offenderNamecit = (TextField) citationReportMap.get("offender name");
			TextField offenderAgecit = (TextField) citationReportMap.get("offender age");
			TextField offenderGendercit = (TextField) citationReportMap.get("offender gender");
			TextField offenderAddresscit = (TextField) citationReportMap.get("offender address");
			TextField offenderDescriptioncit = (TextField) citationReportMap.get("offender description");
			
			ComboBox areacit = (ComboBox) citationReportMap.get("area");
			TextField streetcit = (TextField) citationReportMap.get("street");
			TextField countycit = (TextField) citationReportMap.get("county");
			TextField numcit = (TextField) citationReportMap.get("citation number");
			TextField datecit = (TextField) citationReportMap.get("date");
			TextField timecit = (TextField) citationReportMap.get("time");
			
			ComboBox colorcit = (ComboBox) citationReportMap.get("color");
			ComboBox typecit = (ComboBox) citationReportMap.get("type");
			TextField plateNumbercit = (TextField) citationReportMap.get("plate number");
			TextField otherInfocit = (TextField) citationReportMap.get("other info");
			TextField modelcit = (TextField) citationReportMap.get("model");
			
			TextArea notescit = (TextArea) citationReportMap.get("notes");
			
			officernamecit.setText(officernamets.getText());
			officerdivcit.setText(officerdivts.getText());
			officerrankcit.setText(officerrankts.getText());
			officeragencit.setText(officeragents.getText());
			officernumcit.setText(officernumarrestts.getText());
			timecit.setText(timets.getText());
			datecit.setText(datets.getText());
			typecit.getSelectionModel().select(typets.getSelectionModel().getSelectedItem());
			colorcit.getSelectionModel().select(colorts.getSelectionModel().getSelectedItem());
			offenderNamecit.setText(offenderNamets.getText());
			offenderAddresscit.setText(offenderAddressts.getText());
			offenderGendercit.setText(offenderGenderts.getText());
			offenderAgecit.setText(offenderAgets.getText());
			offenderDescriptioncit.setText(offenderDescriptionts.getText());
			areacit.setValue(areats.getEditor().getText());
			countycit.setText(countyts.getText());
			streetcit.setText(streetts.getText());
			modelcit.setText(modelts.getText());
			plateNumbercit.setText(plateNumberts.getText());
			otherInfocit.setText(otherInfots.getText());
			numcit.setText(stopnumts.getText());
			notescit.setText(notests.getText());
		});
		
		Button submitBtn = (Button) trafficStopReport.get("submitBtn");
		submitBtn.setOnAction(event -> {
			
			for (String fieldName : trafficStopReportMap.keySet()) {
				Object field = trafficStopReportMap.get(fieldName);
				if (field instanceof ComboBox<?> comboBox) {
					if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
						comboBox.getSelectionModel().selectFirst();
					}
				}
			}
			List<TrafficStopLogEntry> logs = TrafficStopReportLogs.loadLogsFromXML();
			
			logs.add(new TrafficStopLogEntry(datets.getText(), timets.getText(), modelts.getText(),
			                                 otherInfots.getText(), offenderNamets.getText(), offenderAgets.getText(),
			                                 offenderAddressts.getText(), offenderDescriptionts.getText(),
			                                 offenderGenderts.getText(), officernamets.getText(),
			                                 officerrankts.getText(), officernumarrestts.getText(),
			                                 officerdivts.getText(), officeragents.getText(), stopnumts.getText(),
			                                 notests.getText(), streetts.getText(), countyts.getText(),
			                                 areats.getEditor().getText(), plateNumberts.getText(),
			                                 colorts.getValue().toString(), typets.getValue().toString()));
			TrafficStopReportLogs.saveLogsToXML(logs);
			actionController.needRefresh.set(1);
			updateChartIfMismatch(reportChart);
			refreshChart(areaReportChart, "area");
			showNotificationInfo("Report Manager", "A new Traffic Stop Report has been submitted.", mainRT);
			stagets.close();
		});
	}
	
	public static String generateReportNumber() {
		int num_length = 7;
		StringBuilder DeathReportNumber = new StringBuilder();
		for (int i = 0; i < num_length; i++) {
			SecureRandom RANDOM = new SecureRandom();
			int digit = RANDOM.nextInt(10);
			DeathReportNumber.append(digit);
		}
		return DeathReportNumber.toString();
	}
}