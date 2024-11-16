package com.Guess.ReportsPlus.logs.TrafficStop;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.Windows.Apps.LogViewController;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.logs.Arrest.ArrestReportUtils;
import com.Guess.ReportsPlus.logs.TrafficCitation.TrafficCitationUtils;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.Guess.ReportsPlus.util.Misc.NotificationManager;
import com.Guess.ReportsPlus.util.Report.nestedReportUtils;
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
import static com.Guess.ReportsPlus.MainApplication.getDate;
import static com.Guess.ReportsPlus.MainApplication.getTime;
import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.notesViewController;
import static com.Guess.ReportsPlus.util.Misc.AudioUtil.playSound;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.controllerUtils.toTitleCase;
import static com.Guess.ReportsPlus.util.Misc.controllerUtils.updateTextFromNotepad;
import static com.Guess.ReportsPlus.util.Misc.stringUtil.getJarPath;
import static com.Guess.ReportsPlus.util.Misc.stringUtil.trafficstopLogURL;
import static com.Guess.ReportsPlus.util.Report.reportUtil.createReportWindow;
import static com.Guess.ReportsPlus.util.Report.reportUtil.generateReportNumber;

public class TrafficStopReportUtils {
	
	public static Map<String, Object> trafficStopLayout() {
		Map<String, Object> trafficStopReport = createReportWindow(localization.getLocalizedMessage("ReportWindows.TrafficStopReportTitle", "Traffic Stop Report"),
		                                                           new nestedReportUtils.TransferConfig(
				                                                           localization.getLocalizedMessage("ReportWindows.TransferReportInfoButton", "Transfer Information To New Report"),
				                                                           new nestedReportUtils.RowConfig(
						                                                           new nestedReportUtils.FieldConfig("transferarrestbtn", 6, nestedReportUtils.FieldType.TRANSFER_BUTTON),
						                                                           new nestedReportUtils.FieldConfig("transfercitationbtn", 6, nestedReportUtils.FieldType.TRANSFER_BUTTON))),
		                                                           new nestedReportUtils.SectionConfig(
				                                                           localization.getLocalizedMessage("ReportWindows.OfficerInfoSectionHeading", "Officer Information"), true,
				                                                           new nestedReportUtils.RowConfig(
						                                                           new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"), 5,
						                                                                                             nestedReportUtils.FieldType.TEXT_FIELD),
						                                                           new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"), 5,
						                                                                                             nestedReportUtils.FieldType.TEXT_FIELD),
						                                                           new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"), 2,
						                                                                                             nestedReportUtils.FieldType.TEXT_FIELD)), new nestedReportUtils.RowConfig(
				                                                           new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"), 6,
				                                                                                             nestedReportUtils.FieldType.TEXT_FIELD),
				                                                           new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"), 6,
				                                                                                             nestedReportUtils.FieldType.TEXT_FIELD))), new nestedReportUtils.SectionConfig(
						localization.getLocalizedMessage("ReportWindows.LocationInfoSectionHeading", "Location / Timestamp Information"), true, new nestedReportUtils.RowConfig(
						new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"), 4, nestedReportUtils.FieldType.COMBO_BOX_STREET),
						new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"), 4, nestedReportUtils.FieldType.COMBO_BOX_AREA),
						new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"), 4, nestedReportUtils.FieldType.TEXT_FIELD)),
						new nestedReportUtils.RowConfig(
								new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"), 5, nestedReportUtils.FieldType.TEXT_FIELD),
								new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"), 5, nestedReportUtils.FieldType.TEXT_FIELD),
								new nestedReportUtils.FieldConfig("stop number", 2, nestedReportUtils.FieldType.TEXT_FIELD))), new nestedReportUtils.SectionConfig(
						localization.getLocalizedMessage("ReportWindows.OffenderInfoSectionHeading", "Offender Information"), true, new nestedReportUtils.RowConfig(
						new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"), 4, nestedReportUtils.FieldType.TEXT_FIELD),
						new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"), 4, nestedReportUtils.FieldType.TEXT_FIELD),
						new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"), 4, nestedReportUtils.FieldType.TEXT_FIELD)),
						new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"), 6,
						                                                                  nestedReportUtils.FieldType.TEXT_FIELD),
						                                new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOffenderDescription", "offender description"), 6,
						                                                                  nestedReportUtils.FieldType.TEXT_FIELD))), new nestedReportUtils.SectionConfig(
						localization.getLocalizedMessage("ReportWindows.OffenderVehicleInfoSectionHeading", "Offender Vehicle Information"), true, new nestedReportUtils.RowConfig(
						new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldModel", "model"), 4, nestedReportUtils.FieldType.TEXT_FIELD),
						new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldPlateNumber", "plate number"), 4, nestedReportUtils.FieldType.TEXT_FIELD),
						new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldColor", "color"), 4, nestedReportUtils.FieldType.COMBO_BOX_COLOR)),
						new nestedReportUtils.RowConfig(
								new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldType", "type"), 4, nestedReportUtils.FieldType.COMBO_BOX_TYPE),
								new nestedReportUtils.FieldConfig(
										localization.getLocalizedMessage("ReportWindows.OtherInfoField", localization.getLocalizedMessage("ReportWindows.OtherInfoField", "other info")), 8,
										nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                           new nestedReportUtils.SectionConfig(localization.getLocalizedMessage("ReportWindows.CommentsSectionHeading", "Comments"), true,
		                                                                                               new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(
				                                                                                               localization.getLocalizedMessage("ReportWindows.FieldNotes", "notes"), 12,
				                                                                                               nestedReportUtils.FieldType.TEXT_AREA))));
		return trafficStopReport;
	}
	
	public static Map<String, Object> newTrafficStop() {
		Map<String, Object> trafficStopReport = trafficStopLayout();
		
		Map<String, Object> trafficStopReportMap = (Map<String, Object>) trafficStopReport.get(
				localization.getLocalizedMessage("ReportWindows.TrafficStopReportTitle", "Traffic Stop Report") + " Map");
		
		TextField officernamets = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
		TextField officerrankts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
		TextField officerdivts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
		TextField officeragents = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
		TextField officernumarrestts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));
		
		TextField offenderNamets = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
		TextField offenderAgets = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"));
		TextField offenderGenderts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"));
		TextField offenderAddressts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"));
		TextField offenderDescriptionts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderDescription", "offender description"));
		
		ComboBox colorts = (ComboBox) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldColor", "color"));
		ComboBox typets = (ComboBox) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldType", "type"));
		TextField plateNumberts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldPlateNumber", "plate number"));
		TextField otherInfots = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.OtherInfoField", "other info"));
		TextField modelts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldModel", "model"));
		
		ComboBox areats = (ComboBox) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
		ComboBox streetts = (ComboBox) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
		TextField countyts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
		TextField stopnumts = (TextField) trafficStopReportMap.get("stop number");
		TextField datets = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
		TextField timets = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
		
		TextArea notests = (TextArea) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "notes"));
		
		Button transferarrestbtnts = (Button) trafficStopReportMap.get("transferarrestbtn");
		transferarrestbtnts.setText(localization.getLocalizedMessage("ReportWindows.NewLabel", "New") + " " + localization.getLocalizedMessage("ReportWindows.ArrestReportTitle", "Arrest Report"));
		Button transfercitationbtnts = (Button) trafficStopReportMap.get("transfercitationbtn");
		transfercitationbtnts.setText(
				localization.getLocalizedMessage("ReportWindows.NewLabel", "New") + " " + localization.getLocalizedMessage("ReportWindows.CitationReportTitle", "Citation Report"));
		
		MenuButton pullNotesBtnts = (MenuButton) trafficStopReport.get("pullNotesBtn");
		pullNotesBtnts.setPopupSide(Side.TOP);
		
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
		stopnumts.setText(generateReportNumber());
		
		pullNotesBtnts.setOnMouseEntered(actionEvent -> {
			pullNotesBtnts.getItems().clear();
			if (notesViewController != null) {
				for (Tab tab : notesViewController.getTabPane().getTabs()) {
					MenuItem menuItem = new MenuItem(tab.getText());
					pullNotesBtnts.getItems().add(menuItem);
					AnchorPane anchorPane = (AnchorPane) tab.getContent();
					TextArea noteArea = (TextArea) anchorPane.lookup("#notepadTextArea");
					if (noteArea != null) {
						menuItem.setOnAction(event2 -> {
							updateTextFromNotepad(areats.getEditor(), noteArea, "-area");
							updateTextFromNotepad(countyts, noteArea, "-county");
							updateTextFromNotepad(streetts.getEditor(), noteArea, "-street");
							updateTextFromNotepad(offenderNamets, noteArea, "-name");
							updateTextFromNotepad(offenderAgets, noteArea, "-age");
							updateTextFromNotepad(offenderGenderts, noteArea, "-gender");
							updateTextFromNotepad(offenderDescriptionts, noteArea, "-description");
							updateTextFromNotepad(notests, noteArea, "-comments");
							updateTextFromNotepad(offenderAddressts, noteArea, "-address");
							updateTextFromNotepad(modelts, noteArea, "-model");
							updateTextFromNotepad(plateNumberts, noteArea, "-plate");
							updateTextFromNotepad(stopnumts, noteArea, "-number");
							updateTextFromNotepad(typets, noteArea, "-type");
						});
					}
				}
			} else {
				log("NotesViewController Is Null", LogUtils.Severity.ERROR);
			}
		});
		
		transferarrestbtnts.setOnAction(event -> {
			Map<String, Object> arrestReportObj = ArrestReportUtils.newArrest();
			
			Map<String, Object> arrestReportMap = (Map<String, Object>) arrestReportObj.get(localization.getLocalizedMessage("ReportWindows.ArrestReportTitle", "Arrest Report") + " Map");
			
			TextField officernamearr = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
			TextField officerrankarr = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
			TextField officerdivarr = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
			TextField officeragenarr = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
			TextField officernumarrestarr = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));
			
			TextField offenderNamearr = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
			TextField offenderAgearr = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"));
			TextField offenderGenderarr = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"));
			TextField offenderAddressarr = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"));
			TextField offenderDescriptionarr = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderDescription", "offender description"));
			
			ComboBox areaarr = (ComboBox) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
			ComboBox streetarr = (ComboBox) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
			TextField countyarr = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
			TextField arrestnumarr = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.ArrestNumberField", "arrest number"));
			TextField datearr = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
			TextField timearr = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
			
			TextArea notesarr = (TextArea) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "notes"));
			
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
			streetarr.getEditor().setText(streetts.getEditor().getText());
			arrestnumarr.setText(stopnumts.getText());
			notesarr.setText(notests.getText());
		});
		
		transfercitationbtnts.setOnAction(event -> {
			Map<String, Object> trafficCitationObj = TrafficCitationUtils.newCitation();
			
			Map<String, Object> citationReportMap = (Map<String, Object>) trafficCitationObj.get(localization.getLocalizedMessage("ReportWindows.CitationReportTitle", "Citation Report") + " Map");
			
			TextField officernamecit = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
			TextField officerrankcit = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
			TextField officerdivcit = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
			TextField officeragencit = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
			TextField officernumcit = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));
			
			TextField offenderNamecit = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
			TextField offenderAgecit = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"));
			TextField offenderGendercit = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"));
			TextField offenderAddresscit = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"));
			TextField offenderDescriptioncit = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderDescription", "offender description"));
			
			ComboBox areacit = (ComboBox) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
			ComboBox streetcit = (ComboBox) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
			TextField countycit = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
			TextField numcit = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.CitationNumField", "citation number"));
			TextField datecit = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
			TextField timecit = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
			
			ComboBox colorcit = (ComboBox) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldColor", "color"));
			ComboBox typecit = (ComboBox) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldType", "type"));
			TextField plateNumbercit = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldPlateNumber", "plate number"));
			TextField otherInfocit = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.OtherInfoField", "other info"));
			TextField modelcit = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldModel", "model"));
			
			TextArea notescit = (TextArea) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "notes"));
			
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
			streetcit.getEditor().setText(streetts.getEditor().getText());
			modelcit.setText(modelts.getText());
			plateNumbercit.setText(plateNumberts.getText());
			otherInfocit.setText(otherInfots.getText());
			numcit.setText(stopnumts.getText());
			notescit.setText(notests.getText());
		});
		
		Button submitBtn = (Button) trafficStopReport.get("submitBtn");
		Label warningLabel = (Label) trafficStopReport.get("warningLabel");
		
		submitBtn.setOnAction(event -> {
			if (stopnumts.getText().trim().isEmpty()) {
				warningLabel.setVisible(true);
				warningLabel.setText("Stop Number can't be empty!");
				warningLabel.setStyle("-fx-font-family: \"Segoe UI Black\"; -fx-text-fill: red;");
				PauseTransition pause = new PauseTransition(Duration.seconds(2));
				pause.setOnFinished(e -> warningLabel.setVisible(false));
				pause.play();
			} else {
				for (String fieldName : trafficStopReportMap.keySet()) {
					Object field = trafficStopReportMap.get(fieldName);
					if (field instanceof ComboBox<?> comboBox) {
						if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
							comboBox.getSelectionModel().selectFirst();
						}
					}
				}
				
				TrafficStopReport trafficStopReport1 = new TrafficStopReport();
				trafficStopReport1.setDate((datets.getText()));
				trafficStopReport1.setTime((timets.getText()));
				trafficStopReport1.setRank((officerrankts.getText()));
				trafficStopReport1.setStopNumber((stopnumts.getText()));
				trafficStopReport1.setPlateNumber((plateNumberts.getText()));
				trafficStopReport1.setCommentsTextArea((notests.getText()));
				
				trafficStopReport1.setResponseModel(toTitleCase(modelts.getText()));
				trafficStopReport1.setResponseOtherInfo(toTitleCase(otherInfots.getText()));
				trafficStopReport1.setOperatorName(toTitleCase(offenderNamets.getText()));
				trafficStopReport1.setOperatorAge(toTitleCase(offenderAgets.getText()));
				trafficStopReport1.setOperatorAddress(toTitleCase(offenderAddressts.getText()));
				trafficStopReport1.setOperatorDescription(toTitleCase(offenderDescriptionts.getText()));
				trafficStopReport1.setOperatorGender(toTitleCase(offenderGenderts.getText()));
				trafficStopReport1.setName(toTitleCase(officernamets.getText()));
				trafficStopReport1.setNumber(toTitleCase(officernumarrestts.getText()));
				trafficStopReport1.setDivision(toTitleCase(officerdivts.getText()));
				trafficStopReport1.setAgency(toTitleCase(officeragents.getText()));
				trafficStopReport1.setStreet(toTitleCase(streetts.getEditor().getText()));
				trafficStopReport1.setCounty(toTitleCase(countyts.getText()));
				trafficStopReport1.setArea(toTitleCase(areats.getEditor().getText()));
				trafficStopReport1.setColor(toTitleCase(colorts.getValue().toString()));
				trafficStopReport1.setType(toTitleCase(typets.getValue().toString()));
				try {
					TrafficStopReportUtils.addTrafficStopReport(trafficStopReport1);
				} catch (JAXBException e) {
					logError("Could not create new TrafficStopReport: ", e);
				}
				
				try {
					if (ConfigReader.configRead("soundSettings", "playCreateReport").equalsIgnoreCase("true")) {
						playSound(getJarPath() + "/sounds/alert-success.wav");
					}
				} catch (IOException e) {
					logError("Error getting configValue for playCreateReport: ", e);
				}
				LogViewController.needRefresh.set(1);
				
				NotificationManager.showNotificationInfo("Report Manager", "A new Traffic Stop Report has been submitted.");
				CustomWindow window = getWindow("Traffic Stop Report");
				if (window != null) {
					window.closeWindow();
				}
			}
		});
		return trafficStopReport;
	}
	
	public static TrafficStopReports loadTrafficStopReports() throws JAXBException {
		File file = new File(trafficstopLogURL);
		if (!file.exists()) {
			return new TrafficStopReports();
		}
		
		try {
			JAXBContext context = JAXBContext.newInstance(TrafficStopReports.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (TrafficStopReports) unmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			logError("Error loading TrafficStopReports: ", e);
			throw e;
		}
	}
	
	private static void saveTrafficStopReports(TrafficStopReports TrafficStopReports) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(TrafficStopReports.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		File file = new File(trafficstopLogURL);
		marshaller.marshal(TrafficStopReports, file);
	}
	
	public static void addTrafficStopReport(TrafficStopReport TrafficStopReport) throws JAXBException {
		TrafficStopReports TrafficStopReports = loadTrafficStopReports();
		
		if (TrafficStopReports.getTrafficStopReportList() == null) {
			TrafficStopReports.setTrafficStopReportList(new java.util.ArrayList<>());
		}
		
		Optional<TrafficStopReport> existingReport = TrafficStopReports.getTrafficStopReportList().stream().filter(e -> e.getStopNumber().equals(TrafficStopReport.getStopNumber())).findFirst();
		
		if (existingReport.isPresent()) {
			TrafficStopReports.getTrafficStopReportList().remove(existingReport.get());
			TrafficStopReports.getTrafficStopReportList().add(TrafficStopReport);
			log("TrafficStopReport with number " + TrafficStopReport.getStopNumber() + " updated.", LogUtils.Severity.INFO);
		} else {
			TrafficStopReports.getTrafficStopReportList().add(TrafficStopReport);
			log("TrafficStopReport with number " + TrafficStopReport.getStopNumber() + " added.", LogUtils.Severity.INFO);
		}
		
		saveTrafficStopReports(TrafficStopReports);
	}
	
	public static void deleteTrafficStopReport(String TrafficStopReportnumber) throws JAXBException {
		TrafficStopReports TrafficStopReports = loadTrafficStopReports();
		
		if (TrafficStopReports.getTrafficStopReportList() != null) {
			TrafficStopReports.getTrafficStopReportList().removeIf(e -> e.getStopNumber().equals(TrafficStopReportnumber));
			saveTrafficStopReports(TrafficStopReports);
			log("TrafficStopReport with number " + TrafficStopReportnumber + " deleted.", LogUtils.Severity.INFO);
		}
	}
	
}