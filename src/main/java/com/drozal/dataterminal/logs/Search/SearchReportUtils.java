package com.drozal.dataterminal.logs.Search;

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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
import static com.drozal.dataterminal.util.Misc.stringUtil.searchLogURL;
import static com.drozal.dataterminal.util.Report.reportCreationUtil.generateReportNumber;
import static com.drozal.dataterminal.util.Report.reportUtil.createReportWindow;

public class SearchReportUtils {
	//TODO fix ranks being titlecase / request stage focus on transfer only / set button text on rowclick to "update"
	
	public static int countReports() {
		try {
			List<SearchReport> logs = SearchReportUtils.loadSearchReports().getSearchReportList();
			
			if (logs == null) {
				return 0;
			}
			
			return logs.size();
		} catch (Exception e) {
			logError("Exception", e);
			return -1;
		}
	}
	
	public static Map<String, Object> searchLayout() {
		Map<String, Object> searchReport = createReportWindow("Search Report", 5, 7, null,
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
		                                                      new nestedReportUtils.SectionConfig(
				                                                      "Timestamp / Location Information", true,
				                                                      new nestedReportUtils.RowConfig(
						                                                      new nestedReportUtils.FieldConfig("date",
						                                                                                        3,
						                                                                                        nestedReportUtils.FieldType.TEXT_FIELD),
						                                                      new nestedReportUtils.FieldConfig("time",
						                                                                                        4,
						                                                                                        nestedReportUtils.FieldType.TEXT_FIELD),
						                                                      new nestedReportUtils.FieldConfig(
								                                                      "search num", 5,
								                                                      nestedReportUtils.FieldType.TEXT_FIELD)),
				                                                      new nestedReportUtils.RowConfig(
						                                                      new nestedReportUtils.FieldConfig(
								                                                      "street", 5,
								                                                      nestedReportUtils.FieldType.TEXT_FIELD),
						                                                      new nestedReportUtils.FieldConfig("area",
						                                                                                        4,
						                                                                                        nestedReportUtils.FieldType.COMBO_BOX_AREA),
						                                                      new nestedReportUtils.FieldConfig(
								                                                      "county", 3,
								                                                      nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                      new nestedReportUtils.SectionConfig("Search Information",
		                                                                                          true,
		                                                                                          new nestedReportUtils.RowConfig(
				                                                                                          new nestedReportUtils.FieldConfig(
						                                                                                          "grounds for search",
						                                                                                          6,
						                                                                                          nestedReportUtils.FieldType.TEXT_FIELD),
				                                                                                          new nestedReportUtils.FieldConfig(
						                                                                                          "witness(s)",
						                                                                                          6,
						                                                                                          nestedReportUtils.FieldType.TEXT_FIELD)),
		                                                                                          new nestedReportUtils.RowConfig(
				                                                                                          new nestedReportUtils.FieldConfig(
						                                                                                          "searched individual",
						                                                                                          12,
						                                                                                          nestedReportUtils.FieldType.TEXT_FIELD)),
		                                                                                          new nestedReportUtils.RowConfig(
				                                                                                          new nestedReportUtils.FieldConfig(
						                                                                                          "search type",
						                                                                                          6,
						                                                                                          nestedReportUtils.FieldType.COMBO_BOX_SEARCH_TYPE),
				                                                                                          new nestedReportUtils.FieldConfig(
						                                                                                          "search method",
						                                                                                          6,
						                                                                                          nestedReportUtils.FieldType.COMBO_BOX_SEARCH_METHOD))),
		                                                      new nestedReportUtils.SectionConfig(
				                                                      "Field Sobriety Information (If Applicable)",
				                                                      false, new nestedReportUtils.RowConfig(
				                                                      new nestedReportUtils.FieldConfig(
						                                                      "test(s) conducted", 4,
						                                                      nestedReportUtils.FieldType.TEXT_FIELD),
				                                                      new nestedReportUtils.FieldConfig("result", 4,
				                                                                                        nestedReportUtils.FieldType.TEXT_FIELD),
				                                                      new nestedReportUtils.FieldConfig(
						                                                      "bac measurement", 4,
						                                                      nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                      new nestedReportUtils.SectionConfig("Notes / Summary",
		                                                                                          true,
		                                                                                          new nestedReportUtils.RowConfig(
				                                                                                          new nestedReportUtils.FieldConfig(
						                                                                                          "seized item(s)",
						                                                                                          12,
						                                                                                          nestedReportUtils.FieldType.TEXT_AREA)),
		                                                                                          new nestedReportUtils.RowConfig(
				                                                                                          new nestedReportUtils.FieldConfig(
						                                                                                          "comments",
						                                                                                          12,
						                                                                                          nestedReportUtils.FieldType.TEXT_AREA))));
		return searchReport;
	}
	
	public static Map<String, Object> newSearch(BarChart<String, Number> reportChart, AreaChart areaReportChart, NotesViewController notesViewController) {
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
		searchnum.setText(generateReportNumber());
		
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
			
			SearchReport searchReport1 = new SearchReport();
			searchReport1.setSearchNumber(searchnum.getText());
			searchReport1.setSearchDate(date.getText());
			searchReport1.setSearchTime(time.getText());
			searchReport1.setOfficerRank(rank.getText());
			searchReport1.setSearchComments(notes.getText());
			searchReport1.setSearchSeizedItems(seizeditems.getText());
			
			searchReport1.setSearchGrounds(toTitleCase(grounds.getText()));
			searchReport1.setSearchType(toTitleCase(type.getValue().toString()));
			searchReport1.setSearchMethod(toTitleCase(method.getValue().toString()));
			searchReport1.setSearchWitnesses(toTitleCase(witness.getText()));
			searchReport1.setOfficerName(toTitleCase(name.getText()));
			searchReport1.setOfficerNumber(toTitleCase(num.getText()));
			searchReport1.setOfficerAgency(toTitleCase(agen.getText()));
			searchReport1.setOfficerDivision(toTitleCase(div.getText()));
			searchReport1.setSearchStreet(toTitleCase(street.getText()));
			searchReport1.setSearchArea(toTitleCase(area.getEditor().getText()));
			searchReport1.setSearchCounty(toTitleCase(county.getText()));
			searchReport1.setSearchedPersons(toTitleCase(searchedindividual.getText()));
			searchReport1.setTestsConducted(toTitleCase(testconducted.getText()));
			searchReport1.setTestResults(toTitleCase(result.getText()));
			searchReport1.setBreathalyzerBACMeasure(toTitleCase(bacmeasurement.getText()));
			
			try {
				SearchReportUtils.addSearchReport(searchReport1);
			} catch (JAXBException e) {
				logError("Error creating SearchReport: ", e);
			}
			
			actionController.needRefresh.set(1);
			updateChartIfMismatch(reportChart);
			refreshChart(areaReportChart, "area");
			showNotificationInfo("Report Manager", "A new Search Report has been submitted.", mainRT);
			stage.close();
		});
		return searchReport;
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