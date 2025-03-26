package com.Guess.ReportsPlus.logs.Search;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.Guess.ReportsPlus.util.Misc.NotificationManager;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.animation.PauseTransition;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager.getWindow;
import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.Windows.Apps.LogViewController.searchLogUpdate;
import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.notesViewController;
import static com.Guess.ReportsPlus.util.Misc.AudioUtil.playSound;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.*;
import static com.Guess.ReportsPlus.util.Report.nestedReportUtils.*;
import static com.Guess.ReportsPlus.util.Report.reportUtil.createReportWindow;
import static com.Guess.ReportsPlus.util.Strings.URLStrings.searchLogURL;

public class SearchReportUtils {
	
	public static Map<String, Object> searchLayout() {
		Map<String, Object> searchReport = createReportWindow(localization.getLocalizedMessage("ReportWindows.SearchReportTitle", "Search Report"), null,
		                                                      new SectionConfig(localization.getLocalizedMessage("ReportWindows.OfficerInfoSectionHeading", localization.getLocalizedMessage("ReportWindows.OfficerInfoSectionHeading", "Officer Information")),
		                                                                        new RowConfig(new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"), 5, FieldType.OFFICER_NAME),
		                                                                                      new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"), 5, FieldType.OFFICER_RANK),
		                                                                                      new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"), 2, FieldType.OFFICER_NUMBER)),
		                                                                        new RowConfig(new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"), 6, FieldType.OFFICER_DIVISION),
		                                                                                      new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"), 6, FieldType.OFFICER_AGENCY))),
		                                                      new SectionConfig(localization.getLocalizedMessage("ReportWindows.TimeLocationInfoheader", "Timestamp / Location Information"),
		                                                                        new RowConfig(new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"), 3, FieldType.DATE_FIELD),
		                                                                                      new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"), 4, FieldType.TIME_FIELD),
		                                                                                      new FieldConfig(localization.getLocalizedMessage("ReportWindows.SearchNumField", "search num"), 5, FieldType.NUMBER_FIELD)),
		                                                                        new RowConfig(new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"), 5, FieldType.COMBO_BOX_STREET),
		                                                                                      new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"), 4, FieldType.COMBO_BOX_AREA),
		                                                                                      new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"), 3, FieldType.COUNTY_FIELD))),
		                                                      new SectionConfig(localization.getLocalizedMessage("ReportWindows.SearchInfoheader", "Search Information"),
		                                                                        new RowConfig(new FieldConfig(localization.getLocalizedMessage("ReportWindows.GroundsForSearchField", "grounds for search"), 6, FieldType.TEXT_FIELD),
		                                                                                      new FieldConfig(localization.getLocalizedMessage("ReportWindows.WitnessesField", "witness(s)"), 6, FieldType.TEXT_FIELD)),
		                                                                        new RowConfig(new FieldConfig(localization.getLocalizedMessage("ReportWindows.SearchedIndividualField", "searched individual"), 12, FieldType.TEXT_FIELD)),
		                                                                        new RowConfig(new FieldConfig(localization.getLocalizedMessage("ReportWindows.SearchTypeField", "search type"), 6, FieldType.COMBO_BOX_SEARCH_TYPE),
		                                                                                      new FieldConfig(localization.getLocalizedMessage("ReportWindows.SearchMethodField", "search method"), 6, FieldType.COMBO_BOX_SEARCH_METHOD))),
		                                                      new SectionConfig(localization.getLocalizedMessage("ReportWindows.FieldSobInfoheader", "Field Sobriety Information (If Applicable)"),
		                                                                        new RowConfig(new FieldConfig(localization.getLocalizedMessage("ReportWindows.TestsConductedField", "test(s) conducted"), 4, FieldType.TEXT_FIELD),
		                                                                                      new FieldConfig(localization.getLocalizedMessage("ReportWindows.TestResultField", "result"), 4, FieldType.TEXT_FIELD),
		                                                                                      new FieldConfig(localization.getLocalizedMessage("ReportWindows.BACMeasurementField", "bac measurement"), 4, FieldType.TEXT_FIELD))),
		                                                      new SectionConfig(localization.getLocalizedMessage("ReportWindows.NotesSummarySectionheader", "Notes / Summary"),
		                                                                        new RowConfig(new FieldConfig(localization.getLocalizedMessage("ReportWindows.SeizedItemsField", "seized item(s)"), 12, FieldType.TEXT_AREA)),
		                                                                        new RowConfig(new FieldConfig(localization.getLocalizedMessage("ReportWindows.CommentsField", "comments"), 12, FieldType.TEXT_AREA))));
		return searchReport;
	}
	
	public static Map<String, Object> newSearch() {
		Map<String, Object> searchReport = searchLayout();
		
		Map<String, Object> searchReportMap = (Map<String, Object>) searchReport.get(localization.getLocalizedMessage("ReportWindows.SearchReportTitle", "Search Report") + " Map");
		
		TextField name = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
		TextField rank = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
		TextField div = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
		TextField agen = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
		TextField num = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));
		
		TextField searchnum = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.SearchNumField", "search num"));
		TextField date = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
		TextField time = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
		ComboBox street = (ComboBox) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
		ComboBox area = (ComboBox) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
		TextField county = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
		
		TextField grounds = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.GroundsForSearchField", "grounds for search"));
		TextField witness = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.WitnessesField", "witness(s)"));
		TextField searchedindividual = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.SearchedIndividualField", "searched individual"));
		ComboBox type = (ComboBox) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.SearchTypeField", "search type"));
		ComboBox method = (ComboBox) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.SearchMethodField", "search method"));
		
		TextField testconducted = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.TestsConductedField", "test(s) conducted"));
		TextField result = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.TestResultField", "result"));
		TextField bacmeasurement = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.BACMeasurementField", "bac measurement"));
		
		TextArea seizeditems = (TextArea) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.SeizedItemsField", "seized item(s)"));
		TextArea notes = (TextArea) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.CommentsField", "comments"));
		
		Label warningLabel = (Label) searchReport.get("warningLabel");
		
		MenuButton pullnotesbtn = (MenuButton) searchReport.get("pullNotesBtn");
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
							updateTextFromNotepad(searchedindividual, noteArea, "-name");
							updateTextFromNotepad(notes, noteArea, "-comments");
							updateTextFromNotepad(searchnum, noteArea, "-number");
							updateTextFromNotepad(seizeditems, noteArea, "-searchitems");
						});
					}
				}
			} else {
				log("NotesViewController Is Null", LogUtils.Severity.ERROR);
			}
		});
		
		Button submitBtn = (Button) searchReport.get("submitBtn");
		
		ComboBox<String> statusValue = (ComboBox) searchReport.get("statusValue");
		
		submitBtn.setOnAction(event -> {
			if (searchnum.getText().trim().isEmpty()) {
				warningLabel.setVisible(true);
				warningLabel.setText("Search Number can't be empty!");
				warningLabel.setStyle("-fx-font-family: \"Inter 28pt Bold\"; -fx-text-fill: red;");
				PauseTransition pause = new PauseTransition(Duration.seconds(2));
				pause.setOnFinished(e -> warningLabel.setVisible(false));
				pause.play();
			} else {
				for (String fieldName : searchReportMap.keySet()) {
					Object field = searchReportMap.get(fieldName);
					if (field instanceof ComboBox<?> comboBox) {
						if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
							comboBox.getSelectionModel().selectFirst();
						}
					}
				}
				
				SearchReport searchReport1 = new SearchReport();
				searchReport1.setStatus(statusValue.getValue());
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
				searchReport1.setSearchStreet(toTitleCase(street.getEditor().getText()));
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
				
				try {
					if (ConfigReader.configRead("soundSettings", "playCreateReport").equalsIgnoreCase("true")) {
						playSound(getJarPath() + "/sounds/alert-success.wav");
					}
				} catch (IOException e) {
					logError("Error getting configValue for playCreateReport: ", e);
				}
				searchLogUpdate();
				
				NotificationManager.showNotificationInfo("Report Manager", "A new Search Report has been submitted.");
				CustomWindow window = getWindow("Search Report");
				if (window != null) {
					window.closeWindow();
				}
			}
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
		
		Optional<SearchReport> existingReport = SearchReports.getSearchReportList().stream().filter(e -> e.getSearchNumber().equals(SearchReport.getSearchNumber())).findFirst();
		
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
			log("SearchReport with number " + SearchReportnumber + " deleted.", LogUtils.Severity.INFO);
		}
	}
	
}