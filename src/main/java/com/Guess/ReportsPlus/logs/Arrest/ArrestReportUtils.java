package com.Guess.ReportsPlus.logs.Arrest;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.Windows.Apps.LogViewController;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.logs.ChargesData;
import com.Guess.ReportsPlus.logs.Impound.ImpoundReportUtils;
import com.Guess.ReportsPlus.logs.Incident.IncidentReportUtils;
import com.Guess.ReportsPlus.logs.Search.SearchReportUtils;
import com.Guess.ReportsPlus.util.CourtData.Case;
import com.Guess.ReportsPlus.util.CourtData.CourtUtils;
import com.Guess.ReportsPlus.util.History.Ped;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.Guess.ReportsPlus.util.Misc.NotificationManager;
import com.Guess.ReportsPlus.util.Report.nestedReportUtils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.animation.PauseTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager.getWindow;
import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.MainApplication.getDate;
import static com.Guess.ReportsPlus.MainApplication.getTime;
import static com.Guess.ReportsPlus.Windows.Apps.CourtViewController.getNextIndex;
import static com.Guess.ReportsPlus.Windows.Apps.CourtViewController.needCourtRefresh;
import static com.Guess.ReportsPlus.Windows.Apps.PedLookupViewController.pedLookupViewController;
import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.notesViewController;
import static com.Guess.ReportsPlus.util.CourtData.CourtUtils.*;
import static com.Guess.ReportsPlus.util.Misc.AudioUtil.playSound;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.controllerUtils.toTitleCase;
import static com.Guess.ReportsPlus.util.Misc.controllerUtils.updateTextFromNotepad;
import static com.Guess.ReportsPlus.util.Misc.stringUtil.arrestLogURL;
import static com.Guess.ReportsPlus.util.Misc.stringUtil.getJarPath;
import static com.Guess.ReportsPlus.util.Report.reportUtil.createReportWindow;
import static com.Guess.ReportsPlus.util.Report.reportUtil.generateReportNumber;
import static com.Guess.ReportsPlus.util.Report.treeViewUtils.findXMLValue;

public class ArrestReportUtils {
	
	public static Map<String, Object> arrestLayout() {
		Map<String, Object> arrestReport = createReportWindow(localization.getLocalizedMessage("ReportWindows.ArrestReportTitle", "Arrest Report"), new nestedReportUtils.TransferConfig(localization.getLocalizedMessage("ReportWindows.TransferReportInfoButton", "Transfer Information To New Report"),
		                                                                                                                                                                                 new nestedReportUtils.RowConfig(
				                                                                                                                                                                                 new nestedReportUtils.FieldConfig("transferimpoundbtn", 4, nestedReportUtils.FieldType.TRANSFER_BUTTON),
				                                                                                                                                                                                 new nestedReportUtils.FieldConfig("transferincidentbtn", 4, nestedReportUtils.FieldType.TRANSFER_BUTTON),
				                                                                                                                                                                                 new nestedReportUtils.FieldConfig("transfersearchbtn", 4, nestedReportUtils.FieldType.TRANSFER_BUTTON))),
		                                                      new nestedReportUtils.SectionConfig(localization.getLocalizedMessage("ReportWindows.OfficerInfoSectionHeading", "Officer Information"), true,
		                                                                                          new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"), 5, nestedReportUtils.FieldType.TEXT_FIELD),
		                                                                                                                          new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"), 5, nestedReportUtils.FieldType.TEXT_FIELD),
		                                                                                                                          new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"), 2, nestedReportUtils.FieldType.TEXT_FIELD)),
		                                                                                          new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"), 6, nestedReportUtils.FieldType.TEXT_FIELD),
		                                                                                                                          new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"), 6, nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                      new nestedReportUtils.SectionConfig(localization.getLocalizedMessage("ReportWindows.LocationInfoSectionHeading", "Location / Timestamp Information"), true,
		                                                                                          new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"), 4, nestedReportUtils.FieldType.COMBO_BOX_STREET),
		                                                                                                                          new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"), 4, nestedReportUtils.FieldType.COMBO_BOX_AREA),
		                                                                                                                          new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"), 4, nestedReportUtils.FieldType.TEXT_FIELD)),
		                                                                                          new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"), 5, nestedReportUtils.FieldType.TEXT_FIELD),
		                                                                                                                          new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"), 5, nestedReportUtils.FieldType.TEXT_FIELD),
		                                                                                                                          new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.ArrestNumberField", "arrest number"), 2, nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                      new nestedReportUtils.SectionConfig(localization.getLocalizedMessage("ReportWindows.OffenderInfoSectionHeading", "Offender Information"), true,
		                                                                                          new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"), 4, nestedReportUtils.FieldType.TEXT_FIELD),
		                                                                                                                          new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"), 4, nestedReportUtils.FieldType.TEXT_FIELD),
		                                                                                                                          new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"), 4, nestedReportUtils.FieldType.TEXT_FIELD)),
		                                                                                          new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"), 6, nestedReportUtils.FieldType.TEXT_FIELD),
		                                                                                                                          new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOffenderDescription", "offender description"), 6,
		                                                                                                                                                            nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                      new nestedReportUtils.SectionConfig(localization.getLocalizedMessage("ReportWindows.OffenderMedicalInfoSectionHeading", "(If Applicable) Offender Medical Information"), false, new nestedReportUtils.RowConfig(
				                                                      new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldAmbulanceRequired", "ambulance required (Y/N)"), 6, nestedReportUtils.FieldType.TEXT_FIELD),
				                                                      new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldTaserDeployed", "taser deployed (Y/N)"), 6, nestedReportUtils.FieldType.TEXT_FIELD)), new nestedReportUtils.RowConfig(
				                                                      new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOtherInformation", "other information"), 12, nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                      new nestedReportUtils.SectionConfig(localization.getLocalizedMessage("ReportWindows.ChargeNotesSectionHeading", "Charge Notes"), true, new nestedReportUtils.RowConfig(
				                                                      new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldNotes", localization.getLocalizedMessage("ReportWindows.FieldNotes", "notes")), 12, nestedReportUtils.FieldType.TEXT_AREA))),
		                                                      new nestedReportUtils.SectionConfig(localization.getLocalizedMessage("ReportWindows.ChargesSectionHeading", "Charge(s)"), true,
		                                                                                          new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("chargeview", 6, nestedReportUtils.FieldType.CHARGES_TREE_VIEW))));
		return arrestReport;
	}
	
	public static Map<String, Object> newArrest() {
		Map<String, Object> arrestReport = arrestLayout();
		
		Map<String, Object> arrestReportMap = (Map<String, Object>) arrestReport.get(localization.getLocalizedMessage("ReportWindows.ArrestReportTitle", "Arrest Report") + " Map");
		
		TextField officername = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
		TextField officerrank = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
		TextField officerdiv = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
		TextField officeragen = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
		TextField officernumarrest = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));
		
		TextField offenderName = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
		TextField offenderAge = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"));
		TextField offenderGender = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"));
		TextField offenderAddress = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"));
		TextField offenderDescription = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderDescription", "offender description"));
		
		ComboBox area = (ComboBox) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
		ComboBox street = (ComboBox) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
		TextField county = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
		TextField arrestnum = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.ArrestNumberField", "arrest number"));
		TextField date = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
		TextField time = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
		
		TextField ambulancereq = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldAmbulanceRequired", "ambulance required (Y/N)"));
		TextField taserdep = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTaserDeployed", "taser deployed (Y/N)"));
		TextField othermedinfo = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOtherInformation", "other information"));
		
		TextArea notes = (TextArea) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", localization.getLocalizedMessage("ReportWindows.FieldNotes", "notes")));
		
		TableView chargetable = (TableView) arrestReportMap.get("ChargeTableView");
		
		Button transferimpoundbtn = (Button) arrestReportMap.get("transferimpoundbtn");
		transferimpoundbtn.setText(localization.getLocalizedMessage("ReportWindows.NewLabel", "New") + " " + localization.getLocalizedMessage("ReportWindows.ImpoundReportTitle", "Impound Report"));
		Button transferincidentbtn = (Button) arrestReportMap.get("transferincidentbtn");
		transferincidentbtn.setText(localization.getLocalizedMessage("ReportWindows.NewLabel", "New") + " " + localization.getLocalizedMessage("ReportWindows.IncidentReportTitle", "Incident Report"));
		Button transfersearchbtn = (Button) arrestReportMap.get("transfersearchbtn");
		transfersearchbtn.setText(localization.getLocalizedMessage("ReportWindows.NewLabel", "New") + " " + localization.getLocalizedMessage("ReportWindows.SearchReportTitle", "Search Report"));
		
		MenuButton pullnotesbtn = (MenuButton) arrestReport.get("pullNotesBtn");
		pullnotesbtn.setPopupSide(Side.TOP);
		
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
		arrestnum.setText(generateReportNumber());
		
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
							updateTextFromNotepad(offenderName, noteArea, "-name");
							updateTextFromNotepad(offenderAge, noteArea, "-age");
							updateTextFromNotepad(offenderGender, noteArea, "-gender");
							updateTextFromNotepad(offenderDescription, noteArea, "-description");
							updateTextFromNotepad(notes, noteArea, "-comments");
							updateTextFromNotepad(offenderAddress, noteArea, "-address");
							updateTextFromNotepad(arrestnum, noteArea, "-number");
						});
					}
				}
			} else {
				log("NotesViewController Is Null", LogUtils.Severity.ERROR);
			}
		});
		
		transferimpoundbtn.setOnAction(event -> {
			
			Map<String, Object> impoundReportObj = ImpoundReportUtils.newImpound();
			
			Map<String, Object> impoundReportMap = (Map<String, Object>) impoundReportObj.get(localization.getLocalizedMessage("ReportWindows.ImpoundReportTitle", "Impound Report") + " Map");
			
			TextField officernameimp = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
			TextField officerrankimp = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
			TextField officerdivimp = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
			TextField officeragenimp = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
			TextField officernumimp = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));
			
			TextField offenderNameimp = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
			TextField offenderAgeimp = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"));
			TextField offenderGenderimp = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"));
			TextField offenderAddressimp = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"));
			
			TextField numimp = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.ImpoundNumField", "impound number"));
			TextField dateimp = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
			TextField timeimp = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
			
			TextArea notesimp = (TextArea) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", localization.getLocalizedMessage("ReportWindows.FieldNotes", "notes")));
			
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
		});
		
		transferincidentbtn.setOnAction(event -> {
			Map<String, Object> incidentReportObj = IncidentReportUtils.newIncident();
			
			Map<String, Object> incidentReportMap = (Map<String, Object>) incidentReportObj.get(localization.getLocalizedMessage("ReportWindows.IncidentReportTitle", "Incident Report") + " Map");
			
			TextField nameinc = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
			TextField rankinc = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
			TextField divinc = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
			TextField ageninc = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
			TextField officernuminc = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));
			
			TextField incidentnum = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.IncidentNumField", "incident num"));
			TextField dateinc = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
			TextField timeinc = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
			ComboBox streetinc = (ComboBox) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
			ComboBox areainc = (ComboBox) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
			TextField countyinc = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
			
			TextField suspectsinc = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.SuspectsField", "suspect(s)"));
			TextArea notesinc = (TextArea) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", localization.getLocalizedMessage("ReportWindows.FieldNotes", "notes")));
			
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
			streetinc.getEditor().setText(street.getEditor().getText());
			suspectsinc.setText(offenderName.getText());
			notesinc.setText(notes.getText());
		});
		
		transfersearchbtn.setOnAction(event -> {
			Map<String, Object> ArrestReportObj = SearchReportUtils.newSearch();
			
			Map<String, Object> ArrestReportMap = (Map<String, Object>) ArrestReportObj.get(localization.getLocalizedMessage("ReportWindows.SearchReportTitle", "Search Report") + " Map");
			
			TextField namesrch = (TextField) ArrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
			TextField ranksrch = (TextField) ArrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
			TextField divsrch = (TextField) ArrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
			TextField agensrch = (TextField) ArrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
			TextField numsrch = (TextField) ArrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));
			
			TextField searchnum = (TextField) ArrestReportMap.get(localization.getLocalizedMessage("ReportWindows.SearchNumField", "search num"));
			TextField datesrch = (TextField) ArrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
			TextField timesrch = (TextField) ArrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
			ComboBox streetsrch = (ComboBox) ArrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
			ComboBox areasrch = (ComboBox) ArrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
			TextField countysrch = (TextField) ArrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
			
			TextField searchedindividualsrch = (TextField) ArrestReportMap.get(localization.getLocalizedMessage("ReportWindows.SearchedIndividualField", "searched individual"));
			
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
			streetsrch.getEditor().setText(street.getEditor().getText());
		});
		
		Button submitBtn = (Button) arrestReport.get("submitBtn");
		Label warningLabel = (Label) arrestReport.get("warningLabel");
		
		ComboBox<String> statusValue = (ComboBox) arrestReport.get("statusValue");
		
		submitBtn.setOnAction(event -> {
			if (arrestnum.getText().trim().isEmpty()) {
				warningLabel.setVisible(true);
				warningLabel.setText("Arrest Number can't be empty!");
				warningLabel.setStyle("-fx-font-family: \"Segoe UI Black\"; -fx-text-fill: red;");
				PauseTransition pause = new PauseTransition(Duration.seconds(2));
				pause.setOnFinished(e -> warningLabel.setVisible(false));
				pause.play();
			} else {
				for (String fieldName : arrestReportMap.keySet()) {
					Object field = arrestReportMap.get(fieldName);
					if (field instanceof ComboBox<?> comboBox) {
						if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
							comboBox.getSelectionModel().selectFirst();
						}
					}
				}
				
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
					chargesBuilder.append(parseCourtData(isTraffic, probationChance, minYears, maxYears, minMonths, maxMonths, suspChance, minSusp, maxSusp, revokeChance, fine, finek) + " | ");
				}
				if (stringBuilder.length() > 0) {
					stringBuilder.setLength(stringBuilder.length() - 1);
				}
				
				ArrestReport arrestReport1 = new ArrestReport();
				arrestReport1.setStatus(statusValue.getValue());
				arrestReport1.setArrestNumber((arrestnum.getText()));
				arrestReport1.setArrestDate((date.getText()));
				arrestReport1.setArrestTime((time.getText()));
				arrestReport1.setArrestCharges((stringBuilder.toString()));
				arrestReport1.setArrestDetails((notes.getText()));
				arrestReport1.setOfficerRank((officerrank.getText()));
				
				arrestReport1.setArrestCounty(toTitleCase(county.getText()));
				arrestReport1.setArrestArea(toTitleCase(area.getEditor().getText()));
				arrestReport1.setArrestStreet(toTitleCase(street.getEditor().getText()));
				arrestReport1.setArresteeName(toTitleCase(offenderName.getText()));
				arrestReport1.setArresteeAge(toTitleCase(offenderAge.getText()));
				arrestReport1.setArresteeGender(toTitleCase(offenderGender.getText()));
				arrestReport1.setArresteeDescription(toTitleCase(offenderDescription.getText()));
				arrestReport1.setAmbulanceYesNo(toTitleCase(ambulancereq.getText()));
				arrestReport1.setTaserYesNo(toTitleCase(taserdep.getText()));
				arrestReport1.setArresteeMedicalInformation(toTitleCase(othermedinfo.getText()));
				arrestReport1.setArresteeHomeAddress(toTitleCase(offenderAddress.getText()));
				arrestReport1.setOfficerName(toTitleCase(officername.getText()));
				arrestReport1.setOfficerNumber(toTitleCase(officernumarrest.getText()));
				arrestReport1.setOfficerDivision(toTitleCase(officerdiv.getText()));
				arrestReport1.setOfficerAgency(toTitleCase(officeragen.getText()));
				try {
					ArrestReportUtils.addArrestReport(arrestReport1);
				} catch (JAXBException e) {
					logError("Could not create new ArrestReport: ", e);
				}
				Optional<Ped> pedOptional = Ped.PedHistoryUtils.findPedByName(arrestReport1.getArresteeName());
				if (pedOptional.isPresent()) {
					log("Ped is present in history, adding new charges.. ", LogUtils.Severity.DEBUG);
					Ped ped1 = pedOptional.get();
					String beforePriors = ped1.getArrestPriors();
					String afterPriors = (beforePriors + stringBuilder.toString().trim()).replaceAll("null", "");
					ped1.setArrestPriors(afterPriors);
					try {
						Ped.PedHistoryUtils.addPed(ped1);
					} catch (JAXBException e) {
						logError("Error updating ped priors from arrestReport: ", e);
					}
				}
				
				if (!offenderName.getText().isEmpty() && offenderName.getText() != null && !stringBuilder.toString().isEmpty() && stringBuilder.toString() != null) {
					Case case1 = new Case();
					String casenum = generateCaseNumber(arrestnum.getText());
					
					Optional<Case> caseToUpdateOptional = findCaseByNumber(casenum);
					if (!caseToUpdateOptional.isPresent()) {
						case1.setCaseNumber(casenum);
						case1.setCourtDate(date.getText());
						case1.setCaseTime(time.getText().replace(".", ""));
						case1.setName(toTitleCase(offenderName.getText()));
						case1.setOffenceDate(date.getText());
						case1.setAge(toTitleCase(offenderAge.getText()));
						case1.setAddress(toTitleCase(offenderAddress.getText()));
						case1.setGender(toTitleCase(offenderGender.getText()));
						case1.setCounty(toTitleCase(county.getText()));
						case1.setStreet(toTitleCase(street.getEditor().getText()));
						case1.setArea(area.getEditor().getText());
						case1.setNotes(notes.getText());
						case1.setOffences(stringBuilder.toString());
						case1.setOutcomes(chargesBuilder.toString());
						case1.setStatus("Pending");
						try {
							String index = getNextIndex(loadCourtCases());
							case1.setIndex(index);
						} catch (JAXBException e) {
							throw new RuntimeException(e);
						}
						try {
							CourtUtils.addCase(case1);
						} catch (JAXBException | IOException e) {
							throw new RuntimeException(e);
						}
						try {
							scheduleOutcomeRevealForSingleCase(case1.getCaseNumber());
						} catch (JAXBException | IOException e) {
							throw new RuntimeException(e);
						}
						NotificationManager.showNotificationInfo("Report Manager", "A new Arrest Report has been submitted. Case#: " + casenum + " Name: " + offenderName.getText());
						log("Added case from arrest, Case#: " + casenum + " Name: " + offenderName.getText(), LogUtils.Severity.INFO);
						needCourtRefresh.set(1);
					} else {
						log("Case #: " + casenum + " already exists, not adding new case", LogUtils.Severity.WARN);
					}
				} else {
					NotificationManager.showNotificationInfo("Report Manager", "A new Arrest Report has been submitted.");
					NotificationManager.showNotificationWarning("Report Manager", "Could not create court case from arrest because either name or offences field(s) were empty.");
					log("Could not create court case from arrest because either name or offences field(s) were empty.", LogUtils.Severity.ERROR);
				}
				
				if (pedLookupViewController != null) {
					if (Objects.requireNonNull(pedLookupViewController).getPedRecordPane().isVisible()) {
						if (pedLookupViewController.getPedSearchField().getEditor().getText().equalsIgnoreCase(offenderName.getText())) {
							try {
								pedLookupViewController.onPedSearchBtnClick(new ActionEvent());
							} catch (IOException e) {
								logError("Error searching name to update ped lookup from arrestreport: " + pedLookupViewController.getPedfnamefield().getText().trim() + " " + pedLookupViewController.getPedlnamefield().getText().trim(), e);
							}
						}
					}
				}
				
				try {
					if (ConfigReader.configRead("soundSettings", "playCreateReport").equalsIgnoreCase("true")) {
						playSound(getJarPath() + "/sounds/alert-success.wav");
					}
				} catch (IOException e) {
					logError("Error getting configValue for playCreateReport: ", e);
				}
				LogViewController.needRefresh.set(1);
				
				CustomWindow window = getWindow(localization.getLocalizedMessage("ReportWindows.ArrestReportTitle", "Arrest Report"));
				if (window != null) {
					window.closeWindow();
				}
			}
		});
		
		return arrestReport;
	}
	
	public static ArrestReports loadArrestReports() throws JAXBException {
		File file = new File(arrestLogURL);
		if (!file.exists()) {
			return new ArrestReports();
		}
		
		try {
			JAXBContext context = JAXBContext.newInstance(ArrestReports.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (ArrestReports) unmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			logError("Error loading ArrestReports: ", e);
			throw e;
		}
	}
	
	private static void saveArrestReports(ArrestReports ArrestReports) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(ArrestReports.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		File file = new File(arrestLogURL);
		marshaller.marshal(ArrestReports, file);
	}
	
	public static void addArrestReport(ArrestReport ArrestReport) throws JAXBException {
		ArrestReports ArrestReports = loadArrestReports();
		
		if (ArrestReports.getArrestReportList() == null) {
			ArrestReports.setArrestReportList(new ArrayList<>());
		}
		
		Optional<ArrestReport> existingReport = ArrestReports.getArrestReportList().stream().filter(e -> e.getArrestNumber().equals(ArrestReport.getArrestNumber())).findFirst();
		
		if (existingReport.isPresent()) {
			ArrestReports.getArrestReportList().remove(existingReport.get());
			ArrestReports.getArrestReportList().add(ArrestReport);
			log("ArrestReport with number " + ArrestReport.getArrestNumber() + " updated.", LogUtils.Severity.INFO);
		} else {
			ArrestReports.getArrestReportList().add(ArrestReport);
			log("ArrestReport with number " + ArrestReport.getArrestNumber() + " added.", LogUtils.Severity.INFO);
		}
		
		saveArrestReports(ArrestReports);
	}
	
	public static void deleteArrestReport(String ArrestReportnumber) throws JAXBException {
		ArrestReports ArrestReports = loadArrestReports();
		
		if (ArrestReports.getArrestReportList() != null) {
			ArrestReports.getArrestReportList().removeIf(e -> e.getArrestNumber().equals(ArrestReportnumber));
			saveArrestReports(ArrestReports);
			log("ArrestReport with number " + ArrestReportnumber + " deleted.", LogUtils.Severity.INFO);
		}
	}
	
}