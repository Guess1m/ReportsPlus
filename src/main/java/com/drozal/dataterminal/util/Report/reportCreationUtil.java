package com.drozal.dataterminal.util.Report;

import com.drozal.dataterminal.NotesViewController;
import com.drozal.dataterminal.actionController;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.logs.Arrest.ArrestLogEntry;
import com.drozal.dataterminal.logs.Arrest.ArrestReportLogs;
import com.drozal.dataterminal.logs.ChargesData;
import com.drozal.dataterminal.logs.CitationsData;
import com.drozal.dataterminal.logs.Impound.ImpoundLogEntry;
import com.drozal.dataterminal.logs.Impound.ImpoundReportLogs;
import com.drozal.dataterminal.logs.Incident.IncidentLogEntry;
import com.drozal.dataterminal.logs.Incident.IncidentReportLogs;
import com.drozal.dataterminal.logs.Patrol.PatrolLogEntry;
import com.drozal.dataterminal.logs.Patrol.PatrolReportLogs;
import com.drozal.dataterminal.logs.Search.SearchLogEntry;
import com.drozal.dataterminal.logs.Search.SearchReportLogs;
import com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationLogEntry;
import com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationReportLogs;
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
	
	public static void newArrest(BarChart<String, Number> reportChart, AreaChart areaReportChart, Object vbox, NotesViewController notesViewController) {
		Map<String, Object> arrestReport = arrestLayout();
		
		Map<String, Object> arrestReportMap = (Map<String, Object>) arrestReport.get("Arrest Report Map");
		
		TextField officername = (TextField) arrestReportMap.get("name");
		TextField officerrank = (TextField) arrestReportMap.get("rank");
		TextField officerdiv = (TextField) arrestReportMap.get("division");
		TextField officeragen = (TextField) arrestReportMap.get("agency");
		TextField officernumarrest = (TextField) arrestReportMap.get("number");
		
		TextField offenderName = (TextField) arrestReportMap.get("offender name");
		TextField offenderAge = (TextField) arrestReportMap.get("offender age");
		TextField offenderGender = (TextField) arrestReportMap.get("offender gender");
		TextField offenderAddress = (TextField) arrestReportMap.get("offender address");
		TextField offenderDescription = (TextField) arrestReportMap.get("offender description");
		
		ComboBox area = (ComboBox) arrestReportMap.get("area");
		TextField street = (TextField) arrestReportMap.get("street");
		TextField county = (TextField) arrestReportMap.get("county");
		TextField arrestnum = (TextField) arrestReportMap.get("arrest number");
		TextField date = (TextField) arrestReportMap.get("date");
		TextField time = (TextField) arrestReportMap.get("time");
		
		TextField ambulancereq = (TextField) arrestReportMap.get("ambulance required (Y/N)");
		TextField taserdep = (TextField) arrestReportMap.get("taser deployed (Y/N)");
		TextField othermedinfo = (TextField) arrestReportMap.get("other information");
		
		TextArea notes = (TextArea) arrestReportMap.get("notes");
		
		TreeView chargetreeview = (TreeView) arrestReportMap.get("chargeview");
		TableView chargetable = (TableView) arrestReportMap.get("ChargeTableView");
		
		Button transferimpoundbtn = (Button) arrestReportMap.get("transferimpoundbtn");
		transferimpoundbtn.setText("New Impound Report");
		Button transferincidentbtn = (Button) arrestReportMap.get("transferincidentbtn");
		transferincidentbtn.setText("New Incident Report");
		Button transfersearchbtn = (Button) arrestReportMap.get("transfersearchbtn");
		transfersearchbtn.setText("New Search Report");
		
		BorderPane root = (BorderPane) arrestReport.get("root");
		Stage stage = (Stage) root.getScene().getWindow();
		
		Label warningLabel = (Label) arrestReport.get("warningLabel");
		Button pullNotesBtn = (Button) arrestReport.get("pullNotesBtn");
		
		try {
			officername.setText(ConfigReader.configRead("userInfo", "Name"));
			officerrank.setText(ConfigReader.configRead("userInfo", "Rank"));
			officerdiv.setText(ConfigReader.configRead("userInfo", "Division"));
			officeragen.setText(ConfigReader.configRead("userInfo", "Agency"));
			officernumarrest.setText(ConfigReader.configRead("userInfo", "Number"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		date.setText(getDate());
		time.setText(getTime());
		
		pullNotesBtn.setOnAction(event -> {
			if (notesViewController != null) {
				updateTextFromNotepad(area.getEditor(), notesViewController.getNotepadTextArea(), "-area");
				updateTextFromNotepad(county, notesViewController.getNotepadTextArea(), "-county");
				updateTextFromNotepad(street, notesViewController.getNotepadTextArea(), "-street");
				updateTextFromNotepad(offenderName, notesViewController.getNotepadTextArea(), "-name");
				updateTextFromNotepad(offenderAge, notesViewController.getNotepadTextArea(), "-age");
				updateTextFromNotepad(offenderGender, notesViewController.getNotepadTextArea(), "-gender");
				updateTextFromNotepad(offenderDescription, notesViewController.getNotepadTextArea(), "-description");
				updateTextFromNotepad(notes, notesViewController.getNotepadTextArea(), "-comments");
				updateTextFromNotepad(offenderAddress, notesViewController.getNotepadTextArea(), "-address");
				updateTextFromNotepad(arrestnum, notesViewController.getNotepadTextArea(), "-number");
			} else {
				log("NotesViewController Is Null", LogUtils.Severity.ERROR);
			}
		});
		
		transferimpoundbtn.setOnAction(event -> {
			
			Map<String, Object> impoundReport = impoundLayout();
			
			Map<String, Object> impoundReportMap = (Map<String, Object>) impoundReport.get("Impound Report Map");
			
			TextField officernameimp = (TextField) impoundReportMap.get("name");
			TextField officerrankimp = (TextField) impoundReportMap.get("rank");
			TextField officerdivimp = (TextField) impoundReportMap.get("division");
			TextField officeragenimp = (TextField) impoundReportMap.get("agency");
			TextField officernumimp = (TextField) impoundReportMap.get("number");
			
			TextField offenderNameimp = (TextField) impoundReportMap.get("offender name");
			TextField offenderAgeimp = (TextField) impoundReportMap.get("offender age");
			TextField offenderGenderimp = (TextField) impoundReportMap.get("offender gender");
			TextField offenderAddressimp = (TextField) impoundReportMap.get("offender address");
			
			TextField numimp = (TextField) impoundReportMap.get("citation number");
			TextField dateimp = (TextField) impoundReportMap.get("date");
			TextField timeimp = (TextField) impoundReportMap.get("time");
			
			ComboBox colorimp = (ComboBox) impoundReportMap.get("color");
			ComboBox typeimp = (ComboBox) impoundReportMap.get("type");
			TextField plateNumberimp = (TextField) impoundReportMap.get("plate number");
			TextField modelimp = (TextField) impoundReportMap.get("model");
			
			TextArea notesimp = (TextArea) impoundReportMap.get("notes");
			
			BorderPane rootimp = (BorderPane) impoundReport.get("root");
			Stage stageimp = (Stage) rootimp.getScene().getWindow();
			
			if (!stageimp.isFocused()) {
				stageimp.requestFocus();
			}
			
			Label warningLabelimp = (Label) impoundReport.get("warningLabel");
			Button pullNotesBtnimp = (Button) impoundReport.get("pullNotesBtn");
			
			officernameimp.setText(officername.getText());
			officerdivimp.setText(officerdiv.getText());
			officerrankimp.setText(officerrank.getText());
			officeragenimp.setText(officeragen.getText());
			officernumimp.setText(officernumarrest.getText());
			timeimp.setText(time.getText());
			dateimp.setText(date.getText());
			offenderAddressimp.setText(offenderAddress.getText());
			offenderNameimp.setText(offenderName.getText());
			offenderAgeimp.setText(offenderAge.getText());
			offenderGenderimp.setText(offenderGender.getText());
			notesimp.setText(notes.getText());
			numimp.setText(arrestnum.getText());
			
			pullNotesBtnimp.setOnAction(event1 -> {
				if (notesViewController != null) {
					updateTextFromNotepad(offenderNameimp, notesViewController.getNotepadTextArea(), "-name");
					updateTextFromNotepad(offenderAgeimp, notesViewController.getNotepadTextArea(), "-age");
					updateTextFromNotepad(offenderGenderimp, notesViewController.getNotepadTextArea(), "-gender");
					updateTextFromNotepad(notesimp, notesViewController.getNotepadTextArea(), "-comments");
					updateTextFromNotepad(offenderAddressimp, notesViewController.getNotepadTextArea(), "-address");
					updateTextFromNotepad(modelimp, notesViewController.getNotepadTextArea(), "-model");
					updateTextFromNotepad(plateNumberimp, notesViewController.getNotepadTextArea(), "-plate");
					updateTextFromNotepad(numimp, notesViewController.getNotepadTextArea(), "-number");
				} else {
					log("NotesViewController Is Null", LogUtils.Severity.ERROR);
				}
			});
			
			Button submitBtnimp = (Button) impoundReport.get("submitBtn");
			submitBtnimp.setOnAction(event2 -> {
				
				for (String fieldName : impoundReportMap.keySet()) {
					Object field = impoundReportMap.get(fieldName);
					if (field instanceof ComboBox<?> comboBox) {
						if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
							comboBox.getSelectionModel().selectFirst();
						}
					}
				}
				
				List<ImpoundLogEntry> logs = ImpoundReportLogs.loadLogsFromXML();
				
				logs.add(new ImpoundLogEntry(numimp.getText(), dateimp.getText(), timeimp.getText(),
				                             offenderNameimp.getText(), offenderAgeimp.getText(),
				                             offenderGenderimp.getText(), offenderAddressimp.getText(),
				                             plateNumberimp.getText(), modelimp.getText(),
				                             typeimp.getValue().toString(), colorimp.getValue().toString(),
				                             notesimp.getText(), officerrankimp.getText(), officernameimp.getText(),
				                             officernumimp.getText(), officerdivimp.getText(),
				                             officeragenimp.getText()));
				ImpoundReportLogs.saveLogsToXML(logs);
				actionController.needRefresh.set(1);
				updateChartIfMismatch(reportChart);
				refreshChart(areaReportChart, "area");
				showNotificationInfo("Report Manager", "A new Impound Report has been submitted.", mainRT);
				stageimp.close();
			});
		});
		
		transferincidentbtn.setOnAction(event -> {
			Map<String, Object> incidentReport = incidentLayout();
			
			Map<String, Object> incidentReportMap = (Map<String, Object>) incidentReport.get("Incident Report Map");
			
			TextField nameinc = (TextField) incidentReportMap.get("name");
			TextField rankinc = (TextField) incidentReportMap.get("rank");
			TextField divinc = (TextField) incidentReportMap.get("division");
			TextField ageninc = (TextField) incidentReportMap.get("agency");
			TextField officernuminc = (TextField) incidentReportMap.get("number");
			
			TextField incidentnum = (TextField) incidentReportMap.get("incident num");
			TextField dateinc = (TextField) incidentReportMap.get("date");
			TextField timeinc = (TextField) incidentReportMap.get("time");
			TextField streetinc = (TextField) incidentReportMap.get("street");
			ComboBox areainc = (ComboBox) incidentReportMap.get("area");
			TextField countyinc = (TextField) incidentReportMap.get("county");
			
			TextField suspectsinc = (TextField) incidentReportMap.get("suspect(s)");
			TextField vicwitinc = (TextField) incidentReportMap.get("victim(s) / witness(s)");
			TextArea statementinc = (TextArea) incidentReportMap.get("statement");
			
			TextArea summaryinc = (TextArea) incidentReportMap.get("summary");
			TextArea notesinc = (TextArea) incidentReportMap.get("notes");
			
			BorderPane rootinc = (BorderPane) incidentReport.get("root");
			Stage stageinc = (Stage) rootinc.getScene().getWindow();
			
			if (!stageinc.isFocused()) {
				stageinc.requestFocus();
			}
			
			Label warningLabelinc = (Label) incidentReport.get("warningLabel");
			
			nameinc.setText(officername.getText());
			divinc.setText(officerdiv.getText());
			rankinc.setText(officerrank.getText());
			ageninc.setText(officeragen.getText());
			officernuminc.setText(officernumarrest.getText());
			dateinc.setText(date.getText());
			timeinc.setText(time.getText());
			incidentnum.setText(arrestnum.getText());
			countyinc.setText(county.getText());
			areainc.setValue(area.getEditor().getText());
			streetinc.setText(street.getText());
			suspectsinc.setText(offenderName.getText());
			notesinc.setText(notes.getText());
			
			Button pullNotesBtninc = (Button) incidentReport.get("pullNotesBtn");
			
			pullNotesBtninc.setOnAction(event1 -> {
				if (notesViewController != null) {
					updateTextFromNotepad(areainc.getEditor(), notesViewController.getNotepadTextArea(), "-area");
					updateTextFromNotepad(countyinc, notesViewController.getNotepadTextArea(), "-county");
					updateTextFromNotepad(streetinc, notesViewController.getNotepadTextArea(), "-street");
					updateTextFromNotepad(suspectsinc, notesViewController.getNotepadTextArea(), "-name");
					updateTextFromNotepad(notesinc, notesViewController.getNotepadTextArea(), "-comments");
					updateTextFromNotepad(officernuminc, notesViewController.getNotepadTextArea(), "-number");
				} else {
					log("NotesViewController Is Null", LogUtils.Severity.ERROR);
				}
			});
			
			Button submitBtninc = (Button) incidentReport.get("submitBtn");
			
			submitBtninc.setOnAction(event2 -> {
				
				for (String fieldName : incidentReportMap.keySet()) {
					Object field = incidentReportMap.get(fieldName);
					if (field instanceof ComboBox<?> comboBox) {
						if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
							comboBox.getSelectionModel().selectFirst();
						}
					}
				}
				
				List<IncidentLogEntry> logs = IncidentReportLogs.loadLogsFromXML();
				
				logs.add(new IncidentLogEntry(incidentnum.getText(), dateinc.getText(), timeinc.getText(),
				                              statementinc.getText(), suspectsinc.getText(), vicwitinc.getText(),
				                              nameinc.getText(), rankinc.getText(), officernuminc.getText(),
				                              ageninc.getText(), divinc.getText(), streetinc.getText(),
				                              areainc.getEditor().getText(), countyinc.getText(), summaryinc.getText(),
				                              notesinc.getText()));
				IncidentReportLogs.saveLogsToXML(logs);
				actionController.needRefresh.set(1);
				updateChartIfMismatch(reportChart);
				refreshChart(areaReportChart, "area");
				showNotificationInfo("Report Manager", "A new Incident Report has been submitted.", mainRT);
				stageinc.close();
			});
		});
		
		transfersearchbtn.setOnAction(event -> {
			Map<String, Object> searchReport = searchLayout();
			
			Map<String, Object> searchReportMap = (Map<String, Object>) searchReport.get("Search Report Map");
			
			TextField namesrch = (TextField) searchReportMap.get("name");
			TextField ranksrch = (TextField) searchReportMap.get("rank");
			TextField divsrch = (TextField) searchReportMap.get("division");
			TextField agensrch = (TextField) searchReportMap.get("agency");
			TextField numsrch = (TextField) searchReportMap.get("number");
			
			TextField searchnum = (TextField) searchReportMap.get("search num");
			TextField datesrch = (TextField) searchReportMap.get("date");
			TextField timesrch = (TextField) searchReportMap.get("time");
			TextField streetsrch = (TextField) searchReportMap.get("street");
			ComboBox areasrch = (ComboBox) searchReportMap.get("area");
			TextField countysrch = (TextField) searchReportMap.get("county");
			
			TextField groundssrch = (TextField) searchReportMap.get("grounds for search");
			TextField witnesssrch = (TextField) searchReportMap.get("witness(s)");
			TextField searchedindividualsrch = (TextField) searchReportMap.get("searched individual");
			ComboBox typesrch = (ComboBox) searchReportMap.get("search type");
			ComboBox methodsrch = (ComboBox) searchReportMap.get("search method");
			
			TextField testconductedsrch = (TextField) searchReportMap.get("test(s) conducted");
			TextField resultsrch = (TextField) searchReportMap.get("result");
			TextField bacmeasurementsrch = (TextField) searchReportMap.get("bac measurement");
			
			TextArea seizeditemssrch = (TextArea) searchReportMap.get("seized item(s)");
			TextArea notessrch = (TextArea) searchReportMap.get("comments");
			
			BorderPane rootsrch = (BorderPane) searchReport.get("root");
			Stage stagesrch = (Stage) rootsrch.getScene().getWindow();
			
			if (!stagesrch.isFocused()) {
				stagesrch.requestFocus();
			}
			
			searchnum.setText(arrestnum.getText());
			namesrch.setText(officername.getText());
			divsrch.setText(officerdiv.getText());
			ranksrch.setText(officerrank.getText());
			agensrch.setText(officeragen.getText());
			numsrch.setText(officernumarrest.getText());
			timesrch.setText(time.getText());
			datesrch.setText(date.getText());
			searchedindividualsrch.setText(offenderName.getText());
			countysrch.setText(county.getText());
			areasrch.setValue(area.getEditor().getText());
			streetsrch.setText(street.getText());
			
			Label warningLabelsrch = (Label) searchReport.get("warningLabel");
			
			Button pullNotesBtnsrch = (Button) searchReport.get("pullNotesBtn");
			
			pullNotesBtnsrch.setOnAction(event1 -> {
				if (notesViewController != null) {
					updateTextFromNotepad(areasrch.getEditor(), notesViewController.getNotepadTextArea(), "-area");
					updateTextFromNotepad(countysrch, notesViewController.getNotepadTextArea(), "-county");
					updateTextFromNotepad(streetsrch, notesViewController.getNotepadTextArea(), "-street");
					updateTextFromNotepad(searchedindividualsrch, notesViewController.getNotepadTextArea(), "-name");
					updateTextFromNotepad(notessrch, notesViewController.getNotepadTextArea(), "-comments");
					updateTextFromNotepad(searchnum, notesViewController.getNotepadTextArea(), "-number");
					updateTextFromNotepad(seizeditemssrch, notesViewController.getNotepadTextArea(), "-searchitems");
				} else {
					log("NotesViewController Is Null", LogUtils.Severity.ERROR);
				}
			});
			
			Button submitBtnsrch = (Button) searchReport.get("submitBtn");
			
			submitBtnsrch.setOnAction(event2 -> {
				
				for (String fieldName : searchReportMap.keySet()) {
					Object field = searchReportMap.get(fieldName);
					if (field instanceof ComboBox<?> comboBox) {
						if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
							comboBox.getSelectionModel().selectFirst();
						}
					}
				}
				
				List<SearchLogEntry> logs = SearchReportLogs.loadLogsFromXML();
				
				logs.add(new SearchLogEntry(searchnum.getText(), searchedindividualsrch.getText(), datesrch.getText(),
				                            timesrch.getText(), seizeditemssrch.getText(), groundssrch.getText(),
				                            typesrch.getValue().toString(), methodsrch.getValue().toString(),
				                            witnesssrch.getText(), ranksrch.getText(), namesrch.getText(),
				                            numsrch.getText(), agensrch.getText(), divsrch.getText(),
				                            streetsrch.getText(), areasrch.getEditor().getText(), countysrch.getText(),
				                            notessrch.getText(), testconductedsrch.getText(), resultsrch.getText(),
				                            bacmeasurementsrch.getText()));
				
				SearchReportLogs.saveLogsToXML(logs);
				actionController.needRefresh.set(1);
				updateChartIfMismatch(reportChart);
				refreshChart(areaReportChart, "area");
				showNotificationInfo("Report Manager", "A new Search Report has been submitted.", mainRT);
				stagesrch.close();
			});
		});
		
		Button submitBtn = (Button) arrestReport.get("submitBtn");
		submitBtn.setOnAction(event -> {
			
			for (String fieldName : arrestReportMap.keySet()) {
				Object field = arrestReportMap.get(fieldName);
				if (field instanceof ComboBox<?> comboBox) {
					if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
						comboBox.getSelectionModel().selectFirst();
					}
				}
			}
			
			List<ArrestLogEntry> logs = ArrestReportLogs.loadLogsFromXML();
			
			ObservableList<ChargesData> formDataList = chargetable.getItems();
			StringBuilder stringBuilder = new StringBuilder();
			StringBuilder chargesBuilder = new StringBuilder();
			for (ChargesData formData : formDataList) {
				String probationChance = findXMLValue(formData.getCharge(), "probation_chance", "data/Charges.xml");
				
				String minYears = findXMLValue(formData.getCharge(), "min_years", "data/Charges.xml");
				String maxYears = findXMLValue(formData.getCharge(), "max_years", "data/Charges.xml");
				String minMonths = findXMLValue(formData.getCharge(), "min_months", "data/Charges.xml");
				String maxMonths = findXMLValue(formData.getCharge(), "max_months", "data/Charges.xml");
				
				String suspChance = findXMLValue(formData.getCharge(), "susp_chance", "data/Charges.xml");
				String minSusp = findXMLValue(formData.getCharge(), "min_susp", "data/Charges.xml");
				String maxSusp = findXMLValue(formData.getCharge(), "max_susp", "data/Charges.xml");
				String revokeChance = findXMLValue(formData.getCharge(), "revoke_chance", "data/Charges.xml");
				
				String fine = findXMLValue(formData.getCharge(), "fine", "data/Charges.xml");
				String finek = findXMLValue(formData.getCharge(), "fine_k", "data/Charges.xml");
				
				String isTraffic = findXMLValue(formData.getCharge(), "traffic", "data/Charges.xml");
				
				stringBuilder.append(formData.getCharge()).append(" | ");
				chargesBuilder.append(
						parseCourtData(isTraffic, probationChance, minYears, maxYears, minMonths, maxMonths, suspChance,
						               minSusp, maxSusp, revokeChance, fine, finek) + " | ");
			}
			if (stringBuilder.length() > 0) {
				stringBuilder.setLength(stringBuilder.length() - 2);
			}
			
			logs.add(new ArrestLogEntry(arrestnum.getText(), date.getText(), time.getText(), stringBuilder.toString(),
			                            county.getText(), area.getEditor().getText(), street.getText(),
			                            offenderName.getText(), offenderAge.getText(), offenderGender.getText(),
			                            offenderDescription.getText(), ambulancereq.getText(), taserdep.getText(),
			                            othermedinfo.getText(), offenderAddress.getText(), notes.getText(),
			                            officerrank.getText(), officername.getText(), officernumarrest.getText(),
			                            officerdiv.getText(), officeragen.getText()));
			ArrestReportLogs.saveLogsToXML(logs);
			
			if (!offenderName.getText().isEmpty() && offenderName.getText() != null && !stringBuilder.toString().isEmpty() && stringBuilder.toString() != null) {
				Case case1 = new Case();
				String casenum = generateCaseNumber();
				case1.setCaseNumber(casenum);
				case1.setCourtDate(date.getText());
				case1.setCaseTime(time.getText());
				case1.setName(toTitleCase(offenderName.getText()));
				case1.setOffenceDate(date.getText());
				case1.setAge(toTitleCase(offenderAge.getText()));
				case1.setAddress(toTitleCase(offenderAddress.getText()));
				case1.setGender(toTitleCase(offenderGender.getText()));
				case1.setCounty(toTitleCase(county.getText()));
				case1.setStreet(toTitleCase(street.getText()));
				case1.setArea(area.getEditor().getText());
				case1.setNotes(notes.getText());
				case1.setOffences(stringBuilder.toString());
				case1.setOutcomes(chargesBuilder.toString());
				try {
					CourtUtils.addCase(case1);
				} catch (JAXBException e) {
					throw new RuntimeException(e);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				log("Added case from citation, Case#: " + casenum + " Name: " + offenderName.getText(),
				    LogUtils.Severity.INFO);
				actionController.needCourtRefresh.set(1);
			} else {
				log("Could not create court case from citation because either name or offences field(s) were empty.",
				    LogUtils.Severity.ERROR);
			}
			
			actionController.needRefresh.set(1);
			updateChartIfMismatch(reportChart);
			refreshChart(areaReportChart, "area");
			showNotificationInfo("Report Manager", "A new Arrest Report has been submitted.", mainRT);
			stage.close();
		});
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
	
	public static void newPatrol(BarChart<String, Number> reportChart, AreaChart areaReportChart, Object vbox, NotesViewController notesViewController) {
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
		
		Button pullNotesBtn = (Button) patrolReport.get("pullNotesBtn");
		
		pullNotesBtn.setOnAction(event -> {
			if (notesViewController != null) {
				updateTextFromNotepad(patrolnum, notesViewController.getNotepadTextArea(), "-number");
				updateTextFromNotepad(notes, notesViewController.getNotepadTextArea(), "-comments");
			} else {
				log("NotesViewController Is Null", LogUtils.Severity.ERROR);
			}
		});
		
		Button submitBtn = (Button) patrolReport.get("submitBtn");
		
		submitBtn.setOnAction(event -> {
			
			for (String fieldName : patrolReportMap.keySet()) {
				Object field = patrolReportMap.get(fieldName);
				if (field instanceof ComboBox<?> comboBox) {
					if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
						comboBox.getSelectionModel().selectFirst();
					}
				}
			}
			
			List<PatrolLogEntry> logs = PatrolReportLogs.loadLogsFromXML();
			
			logs.add(new PatrolLogEntry(patrolnum.getText(), date.getText(), length.getText(), starttime.getText(),
			                            stoptime.getText(), rank.getText(), name.getText(), num.getText(),
			                            div.getText(), agen.getText(), vehicle.getText(), notes.getText()));
			
			PatrolReportLogs.saveLogsToXML(logs);
			actionController.needRefresh.set(1);
			updateChartIfMismatch(reportChart);
			refreshChart(areaReportChart, "area");
			showNotificationInfo("Report Manager", "A new Patrol Report has been submitted.", mainRT);
			
			stage.close();
		});
	}
	
	public static void newCitation(BarChart<String, Number> reportChart, AreaChart areaReportChart, Object vbox, NotesViewController notesViewController) {
		Map<String, Object> citationReport = citationLayout();
		
		Map<String, Object> citationReportMap = (Map<String, Object>) citationReport.get("Citation Report Map");
		
		TextField officername = (TextField) citationReportMap.get("name");
		TextField officerrank = (TextField) citationReportMap.get("rank");
		TextField officerdiv = (TextField) citationReportMap.get("division");
		TextField officeragen = (TextField) citationReportMap.get("agency");
		TextField officernum = (TextField) citationReportMap.get("number");
		
		TextField offenderName = (TextField) citationReportMap.get("offender name");
		TextField offenderAge = (TextField) citationReportMap.get("offender age");
		TextField offenderGender = (TextField) citationReportMap.get("offender gender");
		TextField offenderAddress = (TextField) citationReportMap.get("offender address");
		TextField offenderDescription = (TextField) citationReportMap.get("offender description");
		
		ComboBox area = (ComboBox) citationReportMap.get("area");
		TextField street = (TextField) citationReportMap.get("street");
		TextField county = (TextField) citationReportMap.get("county");
		TextField num = (TextField) citationReportMap.get("citation number");
		TextField date = (TextField) citationReportMap.get("date");
		TextField time = (TextField) citationReportMap.get("time");
		
		ComboBox color = (ComboBox) citationReportMap.get("color");
		ComboBox type = (ComboBox) citationReportMap.get("type");
		TextField plateNumber = (TextField) citationReportMap.get("plate number");
		TextField otherInfo = (TextField) citationReportMap.get("other info");
		TextField model = (TextField) citationReportMap.get("model");
		
		TextArea notes = (TextArea) citationReportMap.get("notes");
		
		TreeView citationtreeview = (TreeView) citationReportMap.get("citationview");
		TableView citationtable = (TableView) citationReportMap.get("CitationTableView");
		
		Button transferimpoundbtn = (Button) citationReportMap.get("transferimpoundbtn");
		transferimpoundbtn.setText("New Impound Report");
		
		BorderPane root = (BorderPane) citationReport.get("root");
		Stage stage = (Stage) root.getScene().getWindow();
		
		Label warningLabel = (Label) citationReport.get("warningLabel");
		Button pullNotesBtn = (Button) citationReport.get("pullNotesBtn");
		
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
				updateTextFromNotepad(area.getEditor(), notesViewController.getNotepadTextArea(), "-area");
				updateTextFromNotepad(county, notesViewController.getNotepadTextArea(), "-county");
				updateTextFromNotepad(street, notesViewController.getNotepadTextArea(), "-street");
				updateTextFromNotepad(offenderName, notesViewController.getNotepadTextArea(), "-name");
				updateTextFromNotepad(offenderAge, notesViewController.getNotepadTextArea(), "-age");
				updateTextFromNotepad(offenderGender, notesViewController.getNotepadTextArea(), "-gender");
				updateTextFromNotepad(offenderDescription, notesViewController.getNotepadTextArea(), "-description");
				updateTextFromNotepad(notes, notesViewController.getNotepadTextArea(), "-comments");
				updateTextFromNotepad(offenderAddress, notesViewController.getNotepadTextArea(), "-address");
				updateTextFromNotepad(model, notesViewController.getNotepadTextArea(), "-model");
				updateTextFromNotepad(plateNumber, notesViewController.getNotepadTextArea(), "-plate");
				updateTextFromNotepad(num, notesViewController.getNotepadTextArea(), "-number");
			} else {
				log("NotesViewController Is Null", LogUtils.Severity.ERROR);
			}
		});
		
		transferimpoundbtn.setOnAction(event -> {
			
			Map<String, Object> impoundReport = impoundLayout();
			
			Map<String, Object> impoundReportMap = (Map<String, Object>) impoundReport.get("Impound Report Map");
			
			TextField officernameimp = (TextField) impoundReportMap.get("name");
			TextField officerrankimp = (TextField) impoundReportMap.get("rank");
			TextField officerdivimp = (TextField) impoundReportMap.get("division");
			TextField officeragenimp = (TextField) impoundReportMap.get("agency");
			TextField officernumimp = (TextField) impoundReportMap.get("number");
			
			TextField offenderNameimp = (TextField) impoundReportMap.get("offender name");
			TextField offenderAgeimp = (TextField) impoundReportMap.get("offender age");
			TextField offenderGenderimp = (TextField) impoundReportMap.get("offender gender");
			TextField offenderAddressimp = (TextField) impoundReportMap.get("offender address");
			
			TextField numimp = (TextField) impoundReportMap.get("citation number");
			TextField dateimp = (TextField) impoundReportMap.get("date");
			TextField timeimp = (TextField) impoundReportMap.get("time");
			
			ComboBox colorimp = (ComboBox) impoundReportMap.get("color");
			ComboBox typeimp = (ComboBox) impoundReportMap.get("type");
			TextField plateNumberimp = (TextField) impoundReportMap.get("plate number");
			TextField modelimp = (TextField) impoundReportMap.get("model");
			
			TextArea notesimp = (TextArea) impoundReportMap.get("notes");
			
			BorderPane rootimp = (BorderPane) impoundReport.get("root");
			Stage stageimp = (Stage) rootimp.getScene().getWindow();
			
			if (!stageimp.isFocused()) {
				stageimp.requestFocus();
			}
			
			Label warningLabelimp = (Label) impoundReport.get("warningLabel");
			Button pullNotesBtnimp = (Button) impoundReport.get("pullNotesBtn");
			
			officernameimp.setText(officername.getText());
			officerdivimp.setText(officerdiv.getText());
			officerrankimp.setText(officerrank.getText());
			officeragenimp.setText(officeragen.getText());
			officernumimp.setText(officernum.getText());
			timeimp.setText(time.getText());
			dateimp.setText(date.getText());
			offenderAddressimp.setText(offenderAddress.getText());
			offenderNameimp.setText(offenderName.getText());
			offenderAgeimp.setText(offenderAge.getText());
			offenderGenderimp.setText(offenderGender.getText());
			plateNumberimp.setText(plateNumber.getText());
			notesimp.setText(notes.getText());
			modelimp.setText(model.getText());
			typeimp.getSelectionModel().select(type.getSelectionModel().getSelectedItem());
			colorimp.getSelectionModel().select(color.getSelectionModel().getSelectedItem());
			numimp.setText(num.getText());
			
			pullNotesBtnimp.setOnAction(event1 -> {
				if (notesViewController != null) {
					updateTextFromNotepad(offenderNameimp, notesViewController.getNotepadTextArea(), "-name");
					updateTextFromNotepad(offenderAgeimp, notesViewController.getNotepadTextArea(), "-age");
					updateTextFromNotepad(offenderGenderimp, notesViewController.getNotepadTextArea(), "-gender");
					updateTextFromNotepad(notesimp, notesViewController.getNotepadTextArea(), "-comments");
					updateTextFromNotepad(offenderAddressimp, notesViewController.getNotepadTextArea(), "-address");
					updateTextFromNotepad(modelimp, notesViewController.getNotepadTextArea(), "-model");
					updateTextFromNotepad(plateNumberimp, notesViewController.getNotepadTextArea(), "-plate");
					updateTextFromNotepad(numimp, notesViewController.getNotepadTextArea(), "-number");
				} else {
					log("NotesViewController Is Null", LogUtils.Severity.ERROR);
				}
			});
			
			Button submitBtnimp = (Button) impoundReport.get("submitBtn");
			submitBtnimp.setOnAction(event2 -> {
				
				for (String fieldName : impoundReportMap.keySet()) {
					Object field = impoundReportMap.get(fieldName);
					if (field instanceof ComboBox<?> comboBox) {
						if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
							comboBox.getSelectionModel().selectFirst();
						}
					}
				}
				List<ImpoundLogEntry> logs = ImpoundReportLogs.loadLogsFromXML();
				
				logs.add(new ImpoundLogEntry(numimp.getText(), dateimp.getText(), timeimp.getText(),
				                             offenderNameimp.getText(), offenderAgeimp.getText(),
				                             offenderGenderimp.getText(), offenderAddressimp.getText(),
				                             plateNumberimp.getText(), modelimp.getText(),
				                             typeimp.getValue().toString(), colorimp.getValue().toString(),
				                             notesimp.getText(), officerrankimp.getText(), officernameimp.getText(),
				                             officernumimp.getText(), officerdivimp.getText(),
				                             officeragenimp.getText()));
				ImpoundReportLogs.saveLogsToXML(logs);
				actionController.needRefresh.set(1);
				updateChartIfMismatch(reportChart);
				refreshChart(areaReportChart, "area");
				showNotificationInfo("Report Manager", "A new Impound Report has been submitted.", mainRT);
				stageimp.close();
			});
		});
		
		Button submitBtn = (Button) citationReport.get("submitBtn");
		submitBtn.setOnAction(event -> {
			
			for (String fieldName : citationReportMap.keySet()) {
				Object field = citationReportMap.get(fieldName);
				if (field instanceof ComboBox<?> comboBox) {
					if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
						comboBox.getSelectionModel().selectFirst();
					}
				}
			}
			List<TrafficCitationLogEntry> logs = TrafficCitationReportLogs.loadLogsFromXML();
			ObservableList<CitationsData> formDataList = citationtable.getItems();
			StringBuilder stringBuilder = new StringBuilder();
			StringBuilder chargesBuilder = new StringBuilder();
			for (CitationsData formData : formDataList) {
				stringBuilder.append(formData.getCitation()).append(" | ");
				
				String fine = findXMLValue(formData.getCitation(), "fine", "data/Citations.xml");
				
				if (fine != null) {
					try {
						int maxFine = Integer.parseInt(fine);
						Random random = new Random();
						int randomFine = random.nextInt(maxFine + 1);
						chargesBuilder.append("Fined: ").append(randomFine).append(" | ");
					} catch (NumberFormatException e) {
						logError("Error parsing fine value " + fine + ": ", e);
						chargesBuilder.append("Fined: ").append(fine).append(" | ");
					}
				} else {
					chargesBuilder.append("Fined: Not Found | ");
				}
			}
			if (stringBuilder.length() > 0) {
				stringBuilder.setLength(stringBuilder.length() - 2);
			}
			
			logs.add(
					new TrafficCitationLogEntry(num.getText(), date.getText(), time.getText(), stringBuilder.toString(),
					                            county.getText(), area.getEditor().getText(), street.getText(),
					                            offenderName.getText(), offenderGender.getText(), offenderAge.getText(),
					                            offenderAddress.getText(), offenderDescription.getText(),
					                            model.getText(), color.getValue().toString(),
					                            type.getValue().toString(), plateNumber.getText(), otherInfo.getText(),
					                            officerrank.getText(), officername.getText(), officernum.getText(),
					                            officerdiv.getText(), officeragen.getText(), notes.getText()));
			TrafficCitationReportLogs.saveLogsToXML(logs);
			
			if (!offenderName.getText().isEmpty() && offenderName.getText() != null && !stringBuilder.toString().isEmpty() && stringBuilder.toString() != null) {
				Case case1 = new Case();
				String casenum = generateCaseNumber();
				case1.setCaseNumber(casenum);
				case1.setCourtDate(date.getText());
				case1.setCaseTime(time.getText());
				case1.setName(toTitleCase(offenderName.getText()));
				case1.setOffenceDate(date.getText());
				case1.setAge(toTitleCase(offenderAge.getText()));
				case1.setAddress(toTitleCase(offenderAddress.getText()));
				case1.setGender(toTitleCase(offenderGender.getText()));
				case1.setCounty(toTitleCase(county.getText()));
				case1.setStreet(toTitleCase(street.getText()));
				case1.setArea(area.getEditor().getText());
				case1.setNotes(notes.getText());
				case1.setOffences(stringBuilder.toString());
				case1.setOutcomes(chargesBuilder.toString());
				try {
					CourtUtils.addCase(case1);
				} catch (JAXBException e) {
					throw new RuntimeException(e);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				log("Added case from citation, Case#: " + casenum + " Name: " + offenderName.getText(),
				    LogUtils.Severity.INFO);
				actionController.needCourtRefresh.set(1);
			} else {
				log("Could not create court case from citation because either name or offences field(s) were empty.",
				    LogUtils.Severity.ERROR);
			}
			
			actionController.needRefresh.set(1);
			updateChartIfMismatch(reportChart);
			refreshChart(areaReportChart, "area");
			showNotificationInfo("Report Manager", "A new Citation Report has been submitted.", mainRT);
			stage.close();
		});
	}
	
	public static void newIncident(BarChart<String, Number> reportChart, AreaChart areaReportChart, Object vbox, NotesViewController notesViewController) {
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
		
		Label warningLabel = (Label) incidentReport.get("warningLabel");
		
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
		
		submitBtn.setOnAction(event -> {
			
			for (String fieldName : incidentReportMap.keySet()) {
				Object field = incidentReportMap.get(fieldName);
				if (field instanceof ComboBox<?> comboBox) {
					if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
						comboBox.getSelectionModel().selectFirst();
					}
				}
			}
			
			List<IncidentLogEntry> logs = IncidentReportLogs.loadLogsFromXML();
			
			logs.add(new IncidentLogEntry(incidentnum.getText(), date.getText(), time.getText(), statement.getText(),
			                              suspects.getText(), vicwit.getText(), name.getText(), rank.getText(),
			                              num.getText(), agen.getText(), div.getText(), street.getText(),
			                              area.getEditor().getText(), county.getText(), summary.getText(),
			                              notes.getText()));
			
			IncidentReportLogs.saveLogsToXML(logs);
			actionController.needRefresh.set(1);
			updateChartIfMismatch(reportChart);
			refreshChart(areaReportChart, "area");
			showNotificationInfo("Report Manager", "A new Incident Report has been submitted.", mainRT);
			stage.close();
		});
	}
	
	public static void newSearch(BarChart<String, Number> reportChart, AreaChart areaReportChart, Object vbox, NotesViewController notesViewController) {
		Map<String, Object> searchReport = searchLayout();
		
		Map<String, Object> searchReportMap = (Map<String, Object>) searchReport.get("Search Report Map");
		
		TextField name = (TextField) searchReportMap.get("name");
		TextField rank = (TextField) searchReportMap.get("rank");
		TextField div = (TextField) searchReportMap.get("division");
		TextField agen = (TextField) searchReportMap.get("agency");
		TextField num = (TextField) searchReportMap.get("number");
		
		TextField searchnum = (TextField) searchReportMap.get("search num");
		TextField date = (TextField) searchReportMap.get("date");
		TextField time = (TextField) searchReportMap.get("time");
		TextField street = (TextField) searchReportMap.get("street");
		ComboBox area = (ComboBox) searchReportMap.get("area");
		TextField county = (TextField) searchReportMap.get("county");
		
		TextField grounds = (TextField) searchReportMap.get("grounds for search");
		TextField witness = (TextField) searchReportMap.get("witness(s)");
		TextField searchedindividual = (TextField) searchReportMap.get("searched individual");
		ComboBox type = (ComboBox) searchReportMap.get("search type");
		ComboBox method = (ComboBox) searchReportMap.get("search method");
		
		TextField testconducted = (TextField) searchReportMap.get("test(s) conducted");
		TextField result = (TextField) searchReportMap.get("result");
		TextField bacmeasurement = (TextField) searchReportMap.get("bac measurement");
		
		TextArea seizeditems = (TextArea) searchReportMap.get("seized item(s)");
		TextArea notes = (TextArea) searchReportMap.get("comments");
		
		BorderPane root = (BorderPane) searchReport.get("root");
		Stage stage = (Stage) root.getScene().getWindow();
		
		Label warningLabel = (Label) searchReport.get("warningLabel");
		
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
		
		Button pullNotesBtn = (Button) searchReport.get("pullNotesBtn");
		
		pullNotesBtn.setOnAction(event -> {
			if (notesViewController != null) {
				updateTextFromNotepad(area.getEditor(), notesViewController.getNotepadTextArea(), "-area");
				updateTextFromNotepad(county, notesViewController.getNotepadTextArea(), "-county");
				updateTextFromNotepad(street, notesViewController.getNotepadTextArea(), "-street");
				updateTextFromNotepad(searchedindividual, notesViewController.getNotepadTextArea(), "-name");
				updateTextFromNotepad(notes, notesViewController.getNotepadTextArea(), "-comments");
				updateTextFromNotepad(searchnum, notesViewController.getNotepadTextArea(), "-number");
				updateTextFromNotepad(seizeditems, notesViewController.getNotepadTextArea(), "-searchitems");
			} else {
				log("NotesViewController Is Null", LogUtils.Severity.ERROR);
			}
		});
		
		Button submitBtn = (Button) searchReport.get("submitBtn");
		
		submitBtn.setOnAction(event -> {
			
			for (String fieldName : searchReportMap.keySet()) {
				Object field = searchReportMap.get(fieldName);
				if (field instanceof ComboBox<?> comboBox) {
					if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
						comboBox.getSelectionModel().selectFirst();
					}
				}
			}
			
			List<SearchLogEntry> logs = SearchReportLogs.loadLogsFromXML();
			
			logs.add(new SearchLogEntry(searchnum.getText(), searchedindividual.getText(), date.getText(),
			                            time.getText(), seizeditems.getText(), grounds.getText(),
			                            type.getValue().toString(), method.getValue().toString(), witness.getText(),
			                            rank.getText(), name.getText(), num.getText(), agen.getText(), div.getText(),
			                            street.getText(), area.getEditor().getText(), county.getText(), notes.getText(),
			                            testconducted.getText(), result.getText(), bacmeasurement.getText()));
			
			SearchReportLogs.saveLogsToXML(logs);
			actionController.needRefresh.set(1);
			updateChartIfMismatch(reportChart);
			refreshChart(areaReportChart, "area");
			showNotificationInfo("Report Manager", "A new Search Report has been submitted.", mainRT);
			stage.close();
		});
	}
	
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
			Map<String, Object> arrestReport = arrestLayout();
			
			Map<String, Object> arrestReportMap = (Map<String, Object>) arrestReport.get("Arrest Report Map");
			
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
			
			TextField ambulancereqarr = (TextField) arrestReportMap.get("ambulance required (Y/N)");
			TextField taserdeparr = (TextField) arrestReportMap.get("taser deployed (Y/N)");
			TextField othermedinfoarr = (TextField) arrestReportMap.get("other information");
			
			TextArea notesarr = (TextArea) arrestReportMap.get("notes");
			
			TreeView chargetreeviewarr = (TreeView) arrestReportMap.get("chargeview");
			TableView chargetablearr = (TableView) arrestReportMap.get("ChargeTableView");
			
			Button transferimpoundbtnarr = (Button) arrestReportMap.get("transferimpoundbtn");
			transferimpoundbtnarr.setText("New Impound Report");
			Button transferincidentbtnarr = (Button) arrestReportMap.get("transferincidentbtn");
			transferincidentbtnarr.setText("New Incident Report");
			Button transfersearchbtnarr = (Button) arrestReportMap.get("transfersearchbtn");
			transfersearchbtnarr.setText("New Search Report");
			
			BorderPane rootarr = (BorderPane) arrestReport.get("root");
			Stage stagearr = (Stage) rootarr.getScene().getWindow();
			
			Label warningLabelarr = (Label) arrestReport.get("warningLabel");
			Button pullNotesBtnarr = (Button) arrestReport.get("pullNotesBtn");
			
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
			
			pullNotesBtnarr.setOnAction(event1 -> {
				if (notesViewController != null) {
					updateTextFromNotepad(areaarr.getEditor(), notesViewController.getNotepadTextArea(), "-area");
					updateTextFromNotepad(countyarr, notesViewController.getNotepadTextArea(), "-county");
					updateTextFromNotepad(streetarr, notesViewController.getNotepadTextArea(), "-street");
					updateTextFromNotepad(offenderNamearr, notesViewController.getNotepadTextArea(), "-name");
					updateTextFromNotepad(offenderAgearr, notesViewController.getNotepadTextArea(), "-age");
					updateTextFromNotepad(offenderGenderarr, notesViewController.getNotepadTextArea(), "-gender");
					updateTextFromNotepad(offenderDescriptionarr, notesViewController.getNotepadTextArea(),
					                      "-description");
					updateTextFromNotepad(notesarr, notesViewController.getNotepadTextArea(), "-comments");
					updateTextFromNotepad(offenderAddressarr, notesViewController.getNotepadTextArea(), "-address");
					updateTextFromNotepad(arrestnumarr, notesViewController.getNotepadTextArea(), "-number");
				} else {
					log("NotesViewController Is Null", LogUtils.Severity.ERROR);
				}
			});
			
			transferimpoundbtnarr.setOnAction(event2 -> {
				
				Map<String, Object> impoundReport = impoundLayout();
				
				Map<String, Object> impoundReportMap = (Map<String, Object>) impoundReport.get("Impound Report Map");
				
				TextField officernameimp = (TextField) impoundReportMap.get("name");
				TextField officerrankimp = (TextField) impoundReportMap.get("rank");
				TextField officerdivimp = (TextField) impoundReportMap.get("division");
				TextField officeragenimp = (TextField) impoundReportMap.get("agency");
				TextField officernumimp = (TextField) impoundReportMap.get("number");
				
				TextField offenderNameimp = (TextField) impoundReportMap.get("offender name");
				TextField offenderAgeimp = (TextField) impoundReportMap.get("offender age");
				TextField offenderGenderimp = (TextField) impoundReportMap.get("offender gender");
				TextField offenderAddressimp = (TextField) impoundReportMap.get("offender address");
				
				TextField numimp = (TextField) impoundReportMap.get("citation number");
				TextField dateimp = (TextField) impoundReportMap.get("date");
				TextField timeimp = (TextField) impoundReportMap.get("time");
				
				ComboBox colorimp = (ComboBox) impoundReportMap.get("color");
				ComboBox typeimp = (ComboBox) impoundReportMap.get("type");
				TextField plateNumberimp = (TextField) impoundReportMap.get("plate number");
				TextField modelimp = (TextField) impoundReportMap.get("model");
				
				TextArea notesimp = (TextArea) impoundReportMap.get("notes");
				
				BorderPane rootimp = (BorderPane) impoundReport.get("root");
				Stage stageimp = (Stage) rootimp.getScene().getWindow();
				
				if (!stageimp.isFocused()) {
					stageimp.requestFocus();
				}
				
				Label warningLabelimp = (Label) impoundReport.get("warningLabel");
				Button pullNotesBtnimp = (Button) impoundReport.get("pullNotesBtn");
				
				officernameimp.setText(officernamearr.getText());
				officerdivimp.setText(officerdivarr.getText());
				officerrankimp.setText(officerrankarr.getText());
				officeragenimp.setText(officeragenarr.getText());
				officernumimp.setText(officernumarrestarr.getText());
				timeimp.setText(timearr.getText());
				dateimp.setText(datearr.getText());
				offenderAddressimp.setText(offenderAddressarr.getText());
				offenderNameimp.setText(offenderNamearr.getText());
				offenderAgeimp.setText(offenderAgearr.getText());
				offenderGenderimp.setText(offenderGenderarr.getText());
				notesimp.setText(notesarr.getText());
				numimp.setText(arrestnumarr.getText());
				
				pullNotesBtnimp.setOnAction(event1 -> {
					if (notesViewController != null) {
						updateTextFromNotepad(offenderNameimp, notesViewController.getNotepadTextArea(), "-name");
						updateTextFromNotepad(offenderAgeimp, notesViewController.getNotepadTextArea(), "-age");
						updateTextFromNotepad(offenderGenderimp, notesViewController.getNotepadTextArea(), "-gender");
						updateTextFromNotepad(notesimp, notesViewController.getNotepadTextArea(), "-comments");
						updateTextFromNotepad(offenderAddressimp, notesViewController.getNotepadTextArea(), "-address");
						updateTextFromNotepad(modelimp, notesViewController.getNotepadTextArea(), "-model");
						updateTextFromNotepad(plateNumberimp, notesViewController.getNotepadTextArea(), "-plate");
						updateTextFromNotepad(numimp, notesViewController.getNotepadTextArea(), "-number");
					} else {
						log("NotesViewController Is Null", LogUtils.Severity.ERROR);
					}
				});
				
				Button submitBtnimp = (Button) impoundReport.get("submitBtn");
				submitBtnimp.setOnAction(event3 -> {
					
					for (String fieldName : impoundReportMap.keySet()) {
						Object field = impoundReportMap.get(fieldName);
						if (field instanceof ComboBox<?> comboBox) {
							if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
								comboBox.getSelectionModel().selectFirst();
							}
						}
					}
					List<ImpoundLogEntry> logs = ImpoundReportLogs.loadLogsFromXML();
					
					logs.add(new ImpoundLogEntry(numimp.getText(), dateimp.getText(), timeimp.getText(),
					                             offenderNameimp.getText(), offenderAgeimp.getText(),
					                             offenderGenderimp.getText(), offenderAddressimp.getText(),
					                             plateNumberimp.getText(), modelimp.getText(),
					                             typeimp.getValue().toString(), colorimp.getValue().toString(),
					                             notesimp.getText(), officerrankimp.getText(), officernameimp.getText(),
					                             officernumimp.getText(), officerdivimp.getText(),
					                             officeragenimp.getText()));
					ImpoundReportLogs.saveLogsToXML(logs);
					actionController.needRefresh.set(1);
					updateChartIfMismatch(reportChart);
					refreshChart(areaReportChart, "area");
					showNotificationInfo("Report Manager", "A new Impound Report has been submitted.", mainRT);
					stageimp.close();
				});
			});
			
			transferincidentbtnarr.setOnAction(event3 -> {
				Map<String, Object> incidentReport = incidentLayout();
				
				Map<String, Object> incidentReportMap = (Map<String, Object>) incidentReport.get("Incident Report Map");
				
				TextField nameinc = (TextField) incidentReportMap.get("name");
				TextField rankinc = (TextField) incidentReportMap.get("rank");
				TextField divinc = (TextField) incidentReportMap.get("division");
				TextField ageninc = (TextField) incidentReportMap.get("agency");
				TextField officernuminc = (TextField) incidentReportMap.get("number");
				
				TextField incidentnum = (TextField) incidentReportMap.get("incident num");
				TextField dateinc = (TextField) incidentReportMap.get("date");
				TextField timeinc = (TextField) incidentReportMap.get("time");
				TextField streetinc = (TextField) incidentReportMap.get("street");
				ComboBox areainc = (ComboBox) incidentReportMap.get("area");
				TextField countyinc = (TextField) incidentReportMap.get("county");
				
				TextField suspectsinc = (TextField) incidentReportMap.get("suspect(s)");
				TextField vicwitinc = (TextField) incidentReportMap.get("victim(s) / witness(s)");
				TextArea statementinc = (TextArea) incidentReportMap.get("statement");
				
				TextArea summaryinc = (TextArea) incidentReportMap.get("summary");
				TextArea notesinc = (TextArea) incidentReportMap.get("notes");
				
				BorderPane rootinc = (BorderPane) incidentReport.get("root");
				Stage stageinc = (Stage) rootinc.getScene().getWindow();
				
				if (!stageinc.isFocused()) {
					stageinc.requestFocus();
				}
				
				Label warningLabelinc = (Label) incidentReport.get("warningLabel");
				
				nameinc.setText(officernamearr.getText());
				divinc.setText(officerdivarr.getText());
				rankinc.setText(officerrankarr.getText());
				ageninc.setText(officeragenarr.getText());
				officernuminc.setText(officernumarrestarr.getText());
				dateinc.setText(datearr.getText());
				timeinc.setText(timearr.getText());
				incidentnum.setText(arrestnumarr.getText());
				countyinc.setText(countyarr.getText());
				areainc.setValue(areaarr.getEditor().getText());
				streetinc.setText(streetarr.getText());
				suspectsinc.setText(offenderNamearr.getText());
				notesinc.setText(notesarr.getText());
				
				Button pullNotesBtninc = (Button) incidentReport.get("pullNotesBtn");
				
				pullNotesBtninc.setOnAction(event1 -> {
					if (notesViewController != null) {
						updateTextFromNotepad(areainc.getEditor(), notesViewController.getNotepadTextArea(), "-area");
						updateTextFromNotepad(countyinc, notesViewController.getNotepadTextArea(), "-county");
						updateTextFromNotepad(streetinc, notesViewController.getNotepadTextArea(), "-street");
						updateTextFromNotepad(suspectsinc, notesViewController.getNotepadTextArea(), "-name");
						updateTextFromNotepad(notesinc, notesViewController.getNotepadTextArea(), "-comments");
						updateTextFromNotepad(officernuminc, notesViewController.getNotepadTextArea(), "-number");
					} else {
						log("NotesViewController Is Null", LogUtils.Severity.ERROR);
					}
				});
				
				Button submitBtninc = (Button) incidentReport.get("submitBtn");
				
				submitBtninc.setOnAction(event2 -> {
					
					for (String fieldName : incidentReportMap.keySet()) {
						Object field = incidentReportMap.get(fieldName);
						if (field instanceof ComboBox<?> comboBox) {
							if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
								comboBox.getSelectionModel().selectFirst();
							}
						}
					}
					
					List<IncidentLogEntry> logs = IncidentReportLogs.loadLogsFromXML();
					
					logs.add(new IncidentLogEntry(incidentnum.getText(), dateinc.getText(), timeinc.getText(),
					                              statementinc.getText(), suspectsinc.getText(), vicwitinc.getText(),
					                              nameinc.getText(), rankinc.getText(), officernuminc.getText(),
					                              ageninc.getText(), divinc.getText(), streetinc.getText(),
					                              areainc.getEditor().getText(), countyinc.getText(),
					                              summaryinc.getText(), notesinc.getText()));
					IncidentReportLogs.saveLogsToXML(logs);
					actionController.needRefresh.set(1);
					updateChartIfMismatch(reportChart);
					refreshChart(areaReportChart, "area");
					showNotificationInfo("Report Manager", "A new Incident Report has been submitted.", mainRT);
					stageinc.close();
				});
			});
			
			transfersearchbtnarr.setOnAction(event4 -> {
				Map<String, Object> searchReport = searchLayout();
				
				Map<String, Object> searchReportMap = (Map<String, Object>) searchReport.get("Search Report Map");
				
				TextField namesrch = (TextField) searchReportMap.get("name");
				TextField ranksrch = (TextField) searchReportMap.get("rank");
				TextField divsrch = (TextField) searchReportMap.get("division");
				TextField agensrch = (TextField) searchReportMap.get("agency");
				TextField numsrch = (TextField) searchReportMap.get("number");
				
				TextField searchnum = (TextField) searchReportMap.get("search num");
				TextField datesrch = (TextField) searchReportMap.get("date");
				TextField timesrch = (TextField) searchReportMap.get("time");
				TextField streetsrch = (TextField) searchReportMap.get("street");
				ComboBox areasrch = (ComboBox) searchReportMap.get("area");
				TextField countysrch = (TextField) searchReportMap.get("county");
				
				TextField groundssrch = (TextField) searchReportMap.get("grounds for search");
				TextField witnesssrch = (TextField) searchReportMap.get("witness(s)");
				TextField searchedindividualsrch = (TextField) searchReportMap.get("searched individual");
				ComboBox typesrch = (ComboBox) searchReportMap.get("search type");
				ComboBox methodsrch = (ComboBox) searchReportMap.get("search method");
				
				TextField testconductedsrch = (TextField) searchReportMap.get("test(s) conducted");
				TextField resultsrch = (TextField) searchReportMap.get("result");
				TextField bacmeasurementsrch = (TextField) searchReportMap.get("bac measurement");
				
				TextArea seizeditemssrch = (TextArea) searchReportMap.get("seized item(s)");
				TextArea notessrch = (TextArea) searchReportMap.get("comments");
				
				BorderPane rootsrch = (BorderPane) searchReport.get("root");
				Stage stagesrch = (Stage) rootsrch.getScene().getWindow();
				
				if (!stagesrch.isFocused()) {
					stagesrch.requestFocus();
				}
				
				searchnum.setText(arrestnumarr.getText());
				namesrch.setText(officernamearr.getText());
				divsrch.setText(officerdivarr.getText());
				ranksrch.setText(officerrankarr.getText());
				agensrch.setText(officeragenarr.getText());
				numsrch.setText(officernumarrestarr.getText());
				timesrch.setText(timearr.getText());
				datesrch.setText(datearr.getText());
				searchedindividualsrch.setText(offenderNamearr.getText());
				countysrch.setText(countyarr.getText());
				areasrch.setValue(areaarr.getEditor().getText());
				streetsrch.setText(streetarr.getText());
				
				Label warningLabelsrch = (Label) searchReport.get("warningLabel");
				
				Button pullNotesBtnsrch = (Button) searchReport.get("pullNotesBtn");
				
				pullNotesBtnsrch.setOnAction(event1 -> {
					if (notesViewController != null) {
						updateTextFromNotepad(areasrch.getEditor(), notesViewController.getNotepadTextArea(), "-area");
						updateTextFromNotepad(countysrch, notesViewController.getNotepadTextArea(), "-county");
						updateTextFromNotepad(streetsrch, notesViewController.getNotepadTextArea(), "-street");
						updateTextFromNotepad(searchedindividualsrch, notesViewController.getNotepadTextArea(),
						                      "-name");
						updateTextFromNotepad(notessrch, notesViewController.getNotepadTextArea(), "-comments");
						updateTextFromNotepad(searchnum, notesViewController.getNotepadTextArea(), "-number");
						updateTextFromNotepad(seizeditemssrch, notesViewController.getNotepadTextArea(),
						                      "-searchitems");
					} else {
						log("NotesViewController Is Null", LogUtils.Severity.ERROR);
					}
				});
				
				Button submitBtnsrch = (Button) searchReport.get("submitBtn");
				
				submitBtnsrch.setOnAction(event2 -> {
					
					for (String fieldName : searchReportMap.keySet()) {
						Object field = searchReportMap.get(fieldName);
						if (field instanceof ComboBox<?> comboBox) {
							if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
								comboBox.getSelectionModel().selectFirst();
							}
						}
					}
					List<SearchLogEntry> logs = SearchReportLogs.loadLogsFromXML();
					
					logs.add(new SearchLogEntry(searchnum.getText(), searchedindividualsrch.getText(),
					                            datesrch.getText(), timesrch.getText(), seizeditemssrch.getText(),
					                            groundssrch.getText(), typesrch.getValue().toString(),
					                            methodsrch.getValue().toString(), witnesssrch.getText(),
					                            ranksrch.getText(), namesrch.getText(), numsrch.getText(),
					                            agensrch.getText(), divsrch.getText(), streetsrch.getText(),
					                            areasrch.getEditor().getText(), countysrch.getText(),
					                            notessrch.getText(), testconductedsrch.getText(), resultsrch.getText(),
					                            bacmeasurementsrch.getText()));
					
					SearchReportLogs.saveLogsToXML(logs);
					actionController.needRefresh.set(1);
					updateChartIfMismatch(reportChart);
					refreshChart(areaReportChart, "area");
					showNotificationInfo("Report Manager", "A new Search Report has been submitted.", mainRT);
					stagesrch.close();
				});
			});
			
			Button submitBtn = (Button) arrestReport.get("submitBtn");
			submitBtn.setOnAction(event5 -> {
				
				for (String fieldName : arrestReportMap.keySet()) {
					Object field = arrestReportMap.get(fieldName);
					if (field instanceof ComboBox<?> comboBox) {
						if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
							comboBox.getSelectionModel().selectFirst();
						}
					}
				}
				
				List<ArrestLogEntry> logs = ArrestReportLogs.loadLogsFromXML();
				
				ObservableList<ChargesData> formDataList = chargetablearr.getItems();
				StringBuilder stringBuilder = new StringBuilder();
				StringBuilder chargesBuilder = new StringBuilder();
				for (ChargesData formData : formDataList) {
					String probationChance = findXMLValue(formData.getCharge(), "probation_chance", "data/Charges.xml");
					
					String minYears = findXMLValue(formData.getCharge(), "min_years", "data/Charges.xml");
					String maxYears = findXMLValue(formData.getCharge(), "max_years", "data/Charges.xml");
					String minMonths = findXMLValue(formData.getCharge(), "min_months", "data/Charges.xml");
					String maxMonths = findXMLValue(formData.getCharge(), "max_months", "data/Charges.xml");
					
					String suspChance = findXMLValue(formData.getCharge(), "susp_chance", "data/Charges.xml");
					String minSusp = findXMLValue(formData.getCharge(), "min_susp", "data/Charges.xml");
					String maxSusp = findXMLValue(formData.getCharge(), "max_susp", "data/Charges.xml");
					String revokeChance = findXMLValue(formData.getCharge(), "revoke_chance", "data/Charges.xml");
					
					String fine = findXMLValue(formData.getCharge(), "fine", "data/Charges.xml");
					String finek = findXMLValue(formData.getCharge(), "fine_k", "data/Charges.xml");
					
					String isTraffic = findXMLValue(formData.getCharge(), "traffic", "data/Charges.xml");
					
					stringBuilder.append(formData.getCharge()).append(" | ");
					chargesBuilder.append(
							parseCourtData(isTraffic, probationChance, minYears, maxYears, minMonths, maxMonths,
							               suspChance, minSusp, maxSusp, revokeChance, fine, finek) + " | ");
				}
				if (stringBuilder.length() > 0) {
					stringBuilder.setLength(stringBuilder.length() - 2);
				}
				
				logs.add(new ArrestLogEntry(arrestnumarr.getText(), datearr.getText(), timearr.getText(),
				                            stringBuilder.toString(), countyarr.getText(),
				                            areaarr.getEditor().getText(), streetarr.getText(),
				                            offenderNamearr.getText(), offenderAgearr.getText(),
				                            offenderGenderarr.getText(), offenderDescriptionarr.getText(),
				                            ambulancereqarr.getText(), taserdeparr.getText(), othermedinfoarr.getText(),
				                            offenderAddressarr.getText(), notesarr.getText(), officerrankarr.getText(),
				                            officernamearr.getText(), officernumarrestarr.getText(),
				                            officerdivarr.getText(), officeragenarr.getText()));
				ArrestReportLogs.saveLogsToXML(logs);
				
				if (!offenderNamearr.getText().isEmpty() && offenderNamearr.getText() != null && !stringBuilder.toString().isEmpty() && stringBuilder.toString() != null) {
					Case case1 = new Case();
					String casenum = generateCaseNumber();
					case1.setCaseNumber(casenum);
					case1.setCourtDate(datearr.getText());
					case1.setCaseTime(timearr.getText());
					case1.setName(toTitleCase(offenderNamearr.getText()));
					case1.setOffenceDate(datearr.getText());
					case1.setAge(toTitleCase(offenderAgearr.getText()));
					case1.setAddress(toTitleCase(offenderAddressarr.getText()));
					case1.setGender(toTitleCase(offenderGenderarr.getText()));
					case1.setCounty(toTitleCase(countyarr.getText()));
					case1.setStreet(toTitleCase(streetarr.getText()));
					case1.setArea(areaarr.getEditor().getText());
					case1.setNotes(notesarr.getText());
					case1.setOffences(stringBuilder.toString());
					case1.setOutcomes(chargesBuilder.toString());
					try {
						CourtUtils.addCase(case1);
					} catch (JAXBException e) {
						throw new RuntimeException(e);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					log("Added arrest case from Traffic Stop, Case#: " + casenum + " Name: " + offenderNamearr.getText(),
					    LogUtils.Severity.INFO);
					actionController.needCourtRefresh.set(1);
				} else {
					log("Could not create court case from Traffic Stop because either name or offences field(s) were empty.",
					    LogUtils.Severity.ERROR);
				}
				
				actionController.needRefresh.set(1);
				updateChartIfMismatch(reportChart);
				refreshChart(areaReportChart, "area");
				showNotificationInfo("Report Manager", "A new Arrest Report has been submitted.", mainRT);
				stagearr.close();
			});
		});
		
		transfercitationbtnts.setOnAction(event -> {
			Map<String, Object> citationReport = citationLayout();
			
			Map<String, Object> citationReportMap = (Map<String, Object>) citationReport.get("Citation Report Map");
			
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
			
			TreeView citationtreeview = (TreeView) citationReportMap.get("citationview");
			TableView citationtable = (TableView) citationReportMap.get("CitationTableView");
			
			Button transferimpoundbtn = (Button) citationReportMap.get("transferimpoundbtn");
			
			BorderPane rootcit = (BorderPane) citationReport.get("root");
			Stage stagecit = (Stage) rootcit.getScene().getWindow();
			
			Label warningLabelcit = (Label) citationReport.get("warningLabel");
			Button pullNotesBtncit = (Button) citationReport.get("pullNotesBtn");
			
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
			
			pullNotesBtncit.setOnAction(event1 -> {
				if (notesViewController != null) {
					updateTextFromNotepad(areacit.getEditor(), notesViewController.getNotepadTextArea(), "-area");
					updateTextFromNotepad(countycit, notesViewController.getNotepadTextArea(), "-county");
					updateTextFromNotepad(streetcit, notesViewController.getNotepadTextArea(), "-street");
					updateTextFromNotepad(offenderNamecit, notesViewController.getNotepadTextArea(), "-name");
					updateTextFromNotepad(offenderAgecit, notesViewController.getNotepadTextArea(), "-age");
					updateTextFromNotepad(offenderGendercit, notesViewController.getNotepadTextArea(), "-gender");
					updateTextFromNotepad(offenderDescriptioncit, notesViewController.getNotepadTextArea(),
					                      "-description");
					updateTextFromNotepad(notescit, notesViewController.getNotepadTextArea(), "-comments");
					updateTextFromNotepad(offenderAddresscit, notesViewController.getNotepadTextArea(), "-address");
					updateTextFromNotepad(modelcit, notesViewController.getNotepadTextArea(), "-model");
					updateTextFromNotepad(plateNumbercit, notesViewController.getNotepadTextArea(), "-plate");
					updateTextFromNotepad(numcit, notesViewController.getNotepadTextArea(), "-number");
				} else {
					log("NotesViewController Is Null", LogUtils.Severity.ERROR);
				}
			});
			
			transferimpoundbtn.setOnAction(event2 -> {
				
				Map<String, Object> impoundReport = impoundLayout();
				
				Map<String, Object> impoundReportMap = (Map<String, Object>) impoundReport.get("Impound Report Map");
				
				TextField officernameimp = (TextField) impoundReportMap.get("name");
				TextField officerrankimp = (TextField) impoundReportMap.get("rank");
				TextField officerdivimp = (TextField) impoundReportMap.get("division");
				TextField officeragenimp = (TextField) impoundReportMap.get("agency");
				TextField officernumimp = (TextField) impoundReportMap.get("number");
				
				TextField offenderNameimp = (TextField) impoundReportMap.get("offender name");
				TextField offenderAgeimp = (TextField) impoundReportMap.get("offender age");
				TextField offenderGenderimp = (TextField) impoundReportMap.get("offender gender");
				TextField offenderAddressimp = (TextField) impoundReportMap.get("offender address");
				
				TextField numimp = (TextField) impoundReportMap.get("citation number");
				TextField dateimp = (TextField) impoundReportMap.get("date");
				TextField timeimp = (TextField) impoundReportMap.get("time");
				
				ComboBox colorimp = (ComboBox) impoundReportMap.get("color");
				ComboBox typeimp = (ComboBox) impoundReportMap.get("type");
				TextField plateNumberimp = (TextField) impoundReportMap.get("plate number");
				TextField modelimp = (TextField) impoundReportMap.get("model");
				
				TextArea notesimp = (TextArea) impoundReportMap.get("notes");
				
				BorderPane rootimp = (BorderPane) impoundReport.get("root");
				Stage stageimp = (Stage) rootimp.getScene().getWindow();
				
				if (!stageimp.isFocused()) {
					stageimp.requestFocus();
				}
				
				Label warningLabelimp = (Label) impoundReport.get("warningLabel");
				Button pullNotesBtnimp = (Button) impoundReport.get("pullNotesBtn");
				
				officernameimp.setText(officernamecit.getText());
				officerdivimp.setText(officerdivcit.getText());
				officerrankimp.setText(officerrankcit.getText());
				officeragenimp.setText(officeragencit.getText());
				officernumimp.setText(officernumcit.getText());
				timeimp.setText(timecit.getText());
				dateimp.setText(datecit.getText());
				offenderAddressimp.setText(offenderAddresscit.getText());
				offenderNameimp.setText(offenderNamecit.getText());
				offenderAgeimp.setText(offenderAgecit.getText());
				offenderGenderimp.setText(offenderGendercit.getText());
				plateNumberimp.setText(plateNumbercit.getText());
				notesimp.setText(notescit.getText());
				modelimp.setText(modelcit.getText());
				typeimp.getSelectionModel().select(typecit.getSelectionModel().getSelectedItem());
				colorimp.getSelectionModel().select(colorcit.getSelectionModel().getSelectedItem());
				numimp.setText(numcit.getText());
				
				pullNotesBtnimp.setOnAction(event1 -> {
					if (notesViewController != null) {
						updateTextFromNotepad(offenderNameimp, notesViewController.getNotepadTextArea(), "-name");
						updateTextFromNotepad(offenderAgeimp, notesViewController.getNotepadTextArea(), "-age");
						updateTextFromNotepad(offenderGenderimp, notesViewController.getNotepadTextArea(), "-gender");
						updateTextFromNotepad(notesimp, notesViewController.getNotepadTextArea(), "-comments");
						updateTextFromNotepad(offenderAddressimp, notesViewController.getNotepadTextArea(), "-address");
						updateTextFromNotepad(modelimp, notesViewController.getNotepadTextArea(), "-model");
						updateTextFromNotepad(plateNumberimp, notesViewController.getNotepadTextArea(), "-plate");
						updateTextFromNotepad(numimp, notesViewController.getNotepadTextArea(), "-number");
					} else {
						log("NotesViewController Is Null", LogUtils.Severity.ERROR);
					}
				});
				
				Button submitBtnimp = (Button) impoundReport.get("submitBtn");
				submitBtnimp.setOnAction(event3 -> {
					
					for (String fieldName : impoundReportMap.keySet()) {
						Object field = impoundReportMap.get(fieldName);
						if (field instanceof ComboBox<?> comboBox) {
							if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
								comboBox.getSelectionModel().selectFirst();
							}
						}
					}
					List<ImpoundLogEntry> logs = ImpoundReportLogs.loadLogsFromXML();
					
					logs.add(new ImpoundLogEntry(numimp.getText(), dateimp.getText(), timeimp.getText(),
					                             offenderNameimp.getText(), offenderAgeimp.getText(),
					                             offenderGenderimp.getText(), offenderAddressimp.getText(),
					                             plateNumberimp.getText(), modelimp.getText(),
					                             typeimp.getValue().toString(), colorimp.getValue().toString(),
					                             notesimp.getText(), officerrankimp.getText(), officernameimp.getText(),
					                             officernumimp.getText(), officerdivimp.getText(),
					                             officeragenimp.getText()));
					ImpoundReportLogs.saveLogsToXML(logs);
					actionController.needRefresh.set(1);
					updateChartIfMismatch(reportChart);
					refreshChart(areaReportChart, "area");
					showNotificationInfo("Report Manager", "A new Impound Report has been submitted.", mainRT);
					stageimp.close();
				});
			});
			transferimpoundbtn.setText("New Impound Report");
			
			Button submitBtncit = (Button) citationReport.get("submitBtn");
			submitBtncit.setOnAction(event3 -> {
				
				for (String fieldName : citationReportMap.keySet()) {
					Object field = citationReportMap.get(fieldName);
					if (field instanceof ComboBox<?> comboBox) {
						if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
							comboBox.getSelectionModel().selectFirst();
						}
					}
				}
				List<TrafficCitationLogEntry> logs = TrafficCitationReportLogs.loadLogsFromXML();
				ObservableList<CitationsData> formDataList = citationtable.getItems();
				StringBuilder stringBuilder = new StringBuilder();
				StringBuilder chargesBuilder = new StringBuilder();
				for (CitationsData formData : formDataList) {
					stringBuilder.append(formData.getCitation()).append(" | ");
					
					String fine = findXMLValue(formData.getCitation(), "fine", "data/Citations.xml");
					
					if (fine != null) {
						try {
							int maxFine = Integer.parseInt(fine);
							Random random = new Random();
							int randomFine = random.nextInt(maxFine + 1);
							chargesBuilder.append("Fined: ").append(randomFine).append(" | ");
						} catch (NumberFormatException e) {
							logError("Error parsing fine value " + fine + ": ", e);
							chargesBuilder.append("Fined: ").append(fine).append(" | ");
						}
					} else {
						chargesBuilder.append("Fined: Not Found | ");
					}
				}
				if (stringBuilder.length() > 0) {
					stringBuilder.setLength(stringBuilder.length() - 2);
				}
				
				logs.add(new TrafficCitationLogEntry(numcit.getText(), datecit.getText(), timecit.getText(),
				                                     stringBuilder.toString(), countycit.getText(),
				                                     areacit.getEditor().getText(), streetcit.getText(),
				                                     offenderNamecit.getText(), offenderGendercit.getText(),
				                                     offenderAgecit.getText(), offenderAddresscit.getText(),
				                                     offenderDescriptioncit.getText(), modelcit.getText(),
				                                     colorcit.getValue().toString(), typecit.getValue().toString(),
				                                     plateNumbercit.getText(), otherInfocit.getText(),
				                                     officerrankcit.getText(), officernamecit.getText(),
				                                     officernumcit.getText(), officerdivcit.getText(),
				                                     officeragencit.getText(), notescit.getText()));
				TrafficCitationReportLogs.saveLogsToXML(logs);
				
				if (!offenderNamecit.getText().isEmpty() && offenderNamecit.getText() != null && !stringBuilder.toString().isEmpty() && stringBuilder.toString() != null) {
					Case casecit = new Case();
					String casenum = generateCaseNumber();
					casecit.setCaseNumber(casenum);
					casecit.setCourtDate(datecit.getText());
					casecit.setCaseTime(timecit.getText());
					casecit.setName(toTitleCase(offenderNamecit.getText()));
					casecit.setOffenceDate(datecit.getText());
					casecit.setAge(toTitleCase(offenderAgecit.getText()));
					casecit.setAddress(toTitleCase(offenderAddresscit.getText()));
					casecit.setGender(toTitleCase(offenderGendercit.getText()));
					casecit.setCounty(toTitleCase(countycit.getText()));
					casecit.setStreet(toTitleCase(streetcit.getText()));
					casecit.setArea(areacit.getEditor().getText());
					casecit.setNotes(notescit.getText());
					casecit.setOffences(stringBuilder.toString());
					casecit.setOutcomes(chargesBuilder.toString());
					try {
						CourtUtils.addCase(casecit);
					} catch (JAXBException e) {
						throw new RuntimeException(e);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					log("Added citation case from Traffic Stop, Case#: " + casenum + " Name: " + offenderNamecit.getText(),
					    LogUtils.Severity.INFO);
					actionController.needCourtRefresh.set(1);
				} else {
					log("Could not create court case from Traffic Stop because either name or offences field(s) were empty.",
					    LogUtils.Severity.ERROR);
				}
				
				actionController.needRefresh.set(1);
				updateChartIfMismatch(reportChart);
				refreshChart(areaReportChart, "area");
				showNotificationInfo("Report Manager", "A new Citation Report has been submitted.", mainRT);
				stagecit.close();
			});
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