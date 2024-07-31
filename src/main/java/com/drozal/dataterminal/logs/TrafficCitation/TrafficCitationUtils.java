package com.drozal.dataterminal.logs.TrafficCitation;

import com.drozal.dataterminal.NotesViewController;
import com.drozal.dataterminal.actionController;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.logs.CitationsData;
import com.drozal.dataterminal.logs.Impound.ImpoundReportUtils;
import com.drozal.dataterminal.logs.Search.SearchReport;
import com.drozal.dataterminal.logs.Search.SearchReports;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Report.nestedReportUtils;
import com.drozal.dataterminal.util.server.Objects.CourtData.Case;
import com.drozal.dataterminal.util.server.Objects.CourtData.CourtUtils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.collections.ObservableList;
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
import java.util.Random;

import static com.drozal.dataterminal.DataTerminalHomeApplication.*;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.controllerUtils.*;
import static com.drozal.dataterminal.util.Misc.stringUtil.searchLogURL;
import static com.drozal.dataterminal.util.Report.reportCreationUtil.generateReportNumber;
import static com.drozal.dataterminal.util.Report.reportUtil.createReportWindow;
import static com.drozal.dataterminal.util.Report.treeViewUtils.findXMLValue;
import static com.drozal.dataterminal.util.server.Objects.CourtData.CourtUtils.generateCaseNumber;

public class TrafficCitationUtils {
	
	public static int countReports() {
		try {
			List<SearchReport> logs = TrafficCitationUtils.loadSearchReports().getSearchReportList();
			
			if (logs == null) {
				return 0;
			}
			
			return logs.size();
		} catch (Exception e) {
			logError("Exception", e);
			return -1;
		}
	}
	
	public static Map<String, Object> citationLayout() {
		Map<String, Object> citationReport = createReportWindow("Citation Report", 7, 9,
		                                                        new nestedReportUtils.TransferConfig("Transfer Information To New Report",
		                                                                                             new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(
				                                                                                             "transferimpoundbtn", 12,
				                                                                                             nestedReportUtils.FieldType.TRANSFER_BUTTON))),
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
				                                                        new nestedReportUtils.FieldConfig("street", 4,
				                                                                                          nestedReportUtils.FieldType.TEXT_FIELD),
				                                                        new nestedReportUtils.FieldConfig("area", 4,
				                                                                                          nestedReportUtils.FieldType.COMBO_BOX_AREA),
				                                                        new nestedReportUtils.FieldConfig("county", 4,
				                                                                                          nestedReportUtils.FieldType.TEXT_FIELD)),
		                                                                                            new nestedReportUtils.RowConfig(
				                                                                                            new nestedReportUtils.FieldConfig("date", 5,
				                                                                                                                              nestedReportUtils.FieldType.TEXT_FIELD),
				                                                                                            new nestedReportUtils.FieldConfig("time", 5,
				                                                                                                                              nestedReportUtils.FieldType.TEXT_FIELD),
				                                                                                            new nestedReportUtils.FieldConfig(
						                                                                                            "citation number", 2,
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
				                                                                                            "offender address", 6,
				                                                                                            nestedReportUtils.FieldType.TEXT_FIELD),
		                                                                                                                            new nestedReportUtils.FieldConfig(
				                                                                                                                            "offender description",
				                                                                                                                            6,
				                                                                                                                            nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                        new nestedReportUtils.SectionConfig(
				                                                        "(If Applicable) Offender Vehicle Information",
				                                                        false, new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("model", 4,
				                                                                                                                                 nestedReportUtils.FieldType.TEXT_FIELD),
				                                                                                               new nestedReportUtils.FieldConfig(
						                                                                                               "plate number", 4,
						                                                                                               nestedReportUtils.FieldType.TEXT_FIELD),
				                                                                                               new nestedReportUtils.FieldConfig("color", 4,
				                                                                                                                                 nestedReportUtils.FieldType.COMBO_BOX_COLOR)),
				                                                        new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("type", 4,
				                                                                                                                          nestedReportUtils.FieldType.COMBO_BOX_TYPE),
				                                                                                        new nestedReportUtils.FieldConfig("other info", 8,
				                                                                                                                          nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                        new nestedReportUtils.SectionConfig("Citation Notes", true, new nestedReportUtils.RowConfig(
				                                                        new nestedReportUtils.FieldConfig("notes", 12,
				                                                                                          nestedReportUtils.FieldType.TEXT_AREA))),
		                                                        new nestedReportUtils.SectionConfig("Citation(s)", true, new nestedReportUtils.RowConfig(
				                                                        new nestedReportUtils.FieldConfig("citationview", 6,
				                                                                                          nestedReportUtils.FieldType.CITATION_TREE_VIEW))));
		return citationReport;
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
			
			Map<String, Object> impoundReportObj = ImpoundReportUtils.newImpound(reportChart, areaReportChart,
			                                                                     notesViewController);
			
			Map<String, Object> impoundReportMap = (Map<String, Object>) impoundReportObj.get("Impound Report Map");
			
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
	
	public static SearchReports loadSearchReports() throws JAXBException {
		File file = new File(searchLogURL);
		if (!file.exists()) {
			return new SearchReports();
		}
		
		try {
			JAXBContext context = JAXBContext.newInstance(SearchReports.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (SearchReports) unmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			logError("Error loading SearchReports: ", e);
			throw e;
		}
	}
	
	private static void saveSearchReports(SearchReports SearchReports) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(SearchReports.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		File file = new File(searchLogURL);
		marshaller.marshal(SearchReports, file);
	}
	
	public static void addSearchReport(SearchReport SearchReport) throws JAXBException {
		SearchReports SearchReports = loadSearchReports();
		
		if (SearchReports.getSearchReportList() == null) {
			SearchReports.setSearchReportList(new java.util.ArrayList<>());
		}
		
		Optional<SearchReport> existingReport = SearchReports.getSearchReportList().stream().filter(
				e -> e.getSearchNumber().equals(SearchReport.getSearchNumber())).findFirst();
		
		if (existingReport.isPresent()) {
			SearchReports.getSearchReportList().remove(existingReport.get());
			SearchReports.getSearchReportList().add(SearchReport);
			log("SearchReport with number " + SearchReport.getSearchNumber() + " updated.", LogUtils.Severity.INFO);
		} else {
			SearchReports.getSearchReportList().add(SearchReport);
			log("SearchReport with number " + SearchReport.getSearchNumber() + " added.", LogUtils.Severity.INFO);
		}
		
		saveSearchReports(SearchReports);
	}
	
	public static void deleteSearchReport(String SearchReportnumber) throws JAXBException {
		SearchReports SearchReports = loadSearchReports();
		
		if (SearchReports.getSearchReportList() != null) {
			SearchReports.getSearchReportList().removeIf(e -> e.getSearchNumber().equals(SearchReportnumber));
			saveSearchReports(SearchReports);
		}
	}
	
	public static Optional<SearchReport> findSearchReportByNumber(String SearchReportnumber) throws JAXBException {
		SearchReports SearchReports = loadSearchReports();
		
		if (SearchReports.getSearchReportList() != null) {
			return SearchReports.getSearchReportList().stream().filter(
					e -> e.getSearchNumber().equals(SearchReportnumber)).findFirst();
		}
		
		return Optional.empty();
	}
	
	public static void modifySearchReport(String number, SearchReport updatedSearchReport) throws JAXBException {
		SearchReports SearchReports = loadSearchReports();
		
		if (SearchReports.getSearchReportList() != null) {
			for (int i = 0; i < SearchReports.getSearchReportList().size(); i++) {
				SearchReport e = SearchReports.getSearchReportList().get(i);
				if (e.getSearchNumber().equals(number)) {
					SearchReports.getSearchReportList().set(i, updatedSearchReport);
					saveSearchReports(SearchReports);
					return;
				}
			}
		}
	}
	
}