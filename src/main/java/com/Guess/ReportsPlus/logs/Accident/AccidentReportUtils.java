package com.Guess.ReportsPlus.logs.Accident;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.Windows.Apps.LogViewController;
import com.Guess.ReportsPlus.config.ConfigReader;
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
import static com.Guess.ReportsPlus.util.Misc.stringUtil.accidentLogURL;
import static com.Guess.ReportsPlus.util.Misc.stringUtil.getJarPath;
import static com.Guess.ReportsPlus.util.Report.reportUtil.createReportWindow;
import static com.Guess.ReportsPlus.util.Report.reportUtil.generateReportNumber;

public class AccidentReportUtils {
	
	public static Map<String, Object> accidentLayout() {
		Map<String, Object> accidentReport = createReportWindow(localization.getLocalizedMessage("ReportWindows.AccidentReportTitle", "Accident Report"), null,
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
		                                                                                                                            new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.AccidentNumberField", "accident number"), 2,
		                                                                                                                                                              nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                        new nestedReportUtils.SectionConfig(localization.getLocalizedMessage("ReportWindows.AccidentInformationSection", "Accident Information"), true, new nestedReportUtils.RowConfig(
				                                                        new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.AccidentWeatherConditionsField", "weather conditions"), 6, nestedReportUtils.FieldType.TEXT_FIELD),
				                                                        new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.AccidentRoadConditionsField", "road conditions"), 6, nestedReportUtils.FieldType.TEXT_FIELD)), new nestedReportUtils.RowConfig(
				                                                        new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.AccidentOtherVehiclesField", "other vehicles involved"), 12, nestedReportUtils.FieldType.TEXT_FIELD)),
		                                                                                            new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldWitnesses", "witnesses"), 12, nestedReportUtils.FieldType.TEXT_FIELD)),
		                                                                                            new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.AccidentInjuriesField", "injuries"), 12, nestedReportUtils.FieldType.TEXT_FIELD)),
		                                                                                            new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.AccidentDamagesField", "damages"), 12, nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                        new nestedReportUtils.SectionConfig(localization.getLocalizedMessage("ReportWindows.OffenderInfoSectionHeading", "Offender Information"), true,
		                                                                                            new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"), 4, nestedReportUtils.FieldType.TEXT_FIELD),
		                                                                                                                            new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"), 4, nestedReportUtils.FieldType.TEXT_FIELD),
		                                                                                                                            new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"), 4, nestedReportUtils.FieldType.TEXT_FIELD)),
		                                                                                            new nestedReportUtils.RowConfig(
				                                                                                            new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"), 6, nestedReportUtils.FieldType.TEXT_FIELD),
				                                                                                            new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOffenderDescription", "offender description"), 6, nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                        new nestedReportUtils.SectionConfig(localization.getLocalizedMessage("ReportWindows.OffenderVehicleInfoSectionHeading", "Offender Vehicle Information"), true,
		                                                                                            new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldModel", "model"), 6, nestedReportUtils.FieldType.TEXT_FIELD),
		                                                                                                                            new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldPlateNumber", "plate number"), 6, nestedReportUtils.FieldType.TEXT_FIELD)),
		                                                                                            new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldType", localization.getLocalizedMessage("ReportWindows.FieldType", "type")), 7,
		                                                                                                                                                              nestedReportUtils.FieldType.COMBO_BOX_TYPE),
		                                                                                                                            new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldColor", "color"), 5, nestedReportUtils.FieldType.COMBO_BOX_COLOR))),
		                                                        new nestedReportUtils.SectionConfig(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"), true, new nestedReportUtils.RowConfig(
				                                                        new nestedReportUtils.FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldNotes", localization.getLocalizedMessage("ReportWindows.FieldNotes", "notes")), 12, nestedReportUtils.FieldType.TEXT_AREA))));
		return accidentReport;
	}
	
	public static Map<String, Object> newAccident() {
		Map<String, Object> accidentReport = accidentLayout();
		
		Map<String, Object> accidentReportMap = (Map<String, Object>) accidentReport.get(localization.getLocalizedMessage("ReportWindows.AccidentReportTitle", "Accident Report") + " Map");
		
		TextField name = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
		TextField rank = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
		TextField div = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
		TextField agen = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
		TextField num = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));
		ComboBox street = (ComboBox) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
		ComboBox area = (ComboBox) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
		TextField county = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
		TextField date = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
		TextField time = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
		TextField accidentnum = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.AccidentNumberField", "accident number"));
		TextField weatherConditions = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.AccidentWeatherConditionsField", "weather conditions"));
		TextField roadConditions = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.AccidentRoadConditionsField", "road conditions"));
		TextField otherVehiclesInvolved = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.AccidentOtherVehiclesField", "other vehicles involved"));
		TextField witnesses = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldWitnesses", "witnesses"));
		TextField injuries = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.AccidentInjuriesField", "injuries"));
		TextField damages = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.AccidentDamagesField", "damages"));
		TextField offenderName = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
		TextField offenderAge = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"));
		TextField offenderGender = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"));
		TextField offenderAddress = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"));
		TextField offenderDescription = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderDescription", "offender description"));
		TextField model = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldModel", "model"));
		TextField plateNumber = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldPlateNumber", "plate number"));
		ComboBox type = (ComboBox) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldType", localization.getLocalizedMessage("ReportWindows.FieldType", "type")));
		ComboBox color = (ComboBox) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldColor", "color"));
		TextArea notes = (TextArea) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", localization.getLocalizedMessage("ReportWindows.FieldNotes", "notes")));
		
		Label warningLabel = (Label) accidentReport.get("warningLabel");
		
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
		accidentnum.setText(generateReportNumber());
		
		MenuButton pullnotesbtn = (MenuButton) accidentReport.get("pullNotesBtn");
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
							updateTextFromNotepad(plateNumber, noteArea, "-platenum");
							updateTextFromNotepad(model, noteArea, "-model");
							updateTextFromNotepad(area.getEditor(), noteArea, "-area");
							updateTextFromNotepad(county, noteArea, "-county");
							updateTextFromNotepad(street.getEditor(), noteArea, "-street");
							updateTextFromNotepad(offenderName, noteArea, "-name");
							updateTextFromNotepad(offenderAge, noteArea, "-age");
							updateTextFromNotepad(offenderGender, noteArea, "-gender");
							updateTextFromNotepad(offenderDescription, noteArea, "-description");
							updateTextFromNotepad(offenderAddress, noteArea, "-address");
							updateTextFromNotepad(accidentnum, noteArea, "-number");
							updateTextFromNotepad(notes, noteArea, "-comments");
							updateTextFromNotepad(type, noteArea, "-type");
						});
					}
				}
			} else {
				log("NotesViewController Is Null", LogUtils.Severity.ERROR);
			}
		});
		
		Button submitBtn = (Button) accidentReport.get("submitBtn");
		
		ComboBox<String> statusValue = (ComboBox) accidentReport.get("statusValue");
		
		submitBtn.setOnAction(event -> {
			if (accidentnum.getText().trim().isEmpty()) {
				warningLabel.setVisible(true);
				warningLabel.setText("Accident Number can't be empty!");
				warningLabel.setStyle("-fx-font-family: \"Segoe UI Black\"; -fx-text-fill: red;");
				PauseTransition pause = new PauseTransition(Duration.seconds(2));
				pause.setOnFinished(e -> warningLabel.setVisible(false));
				pause.play();
			} else {
				for (String fieldName : accidentReportMap.keySet()) {
					Object field = accidentReportMap.get(fieldName);
					if (field instanceof ComboBox<?> comboBox) {
						if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
							comboBox.getSelectionModel().selectFirst();
						}
					}
				}
				AccidentReport accidentReport1 = new AccidentReport();
				accidentReport1.setStatus(statusValue.getValue());
				accidentReport1.setAccidentNumber(accidentnum.getText());
				accidentReport1.setAccidentDate(date.getText());
				accidentReport1.setAccidentTime(time.getText());
				accidentReport1.setOfficerRank(rank.getText());
				accidentReport1.setComments(notes.getText());
				accidentReport1.setOfficerNumber(num.getText());
				accidentReport1.setPlateNumber(plateNumber.getText());
				
				accidentReport1.setOfficerName(toTitleCase(name.getText()));
				accidentReport1.setOfficerDivision(toTitleCase(div.getText()));
				accidentReport1.setOfficerAgency(toTitleCase(agen.getText()));
				accidentReport1.setStreet(toTitleCase(street.getEditor().getText()));
				accidentReport1.setArea(toTitleCase(area.getEditor().getText()));
				accidentReport1.setCounty(toTitleCase(county.getText()));
				accidentReport1.setWeatherConditions(toTitleCase(weatherConditions.getText()));
				accidentReport1.setRoadConditions(toTitleCase(roadConditions.getText()));
				accidentReport1.setOtherVehiclesInvolved(toTitleCase(otherVehiclesInvolved.getText()));
				accidentReport1.setWitnesses(toTitleCase(witnesses.getText()));
				accidentReport1.setInjuriesReported(toTitleCase(injuries.getText()));
				accidentReport1.setDamageDetails(toTitleCase(damages.getText()));
				accidentReport1.setOwnerName(toTitleCase(offenderName.getText()));
				accidentReport1.setOwnerAge(toTitleCase(offenderAge.getText()));
				accidentReport1.setOwnerGender(toTitleCase(offenderGender.getText()));
				accidentReport1.setOwnerAddress(toTitleCase(offenderAddress.getText()));
				accidentReport1.setOwnerDescription(toTitleCase(offenderDescription.getText()));
				accidentReport1.setModel(toTitleCase(model.getText()));
				accidentReport1.setType(toTitleCase(type.getValue().toString()));
				accidentReport1.setColor(toTitleCase(color.getValue().toString()));
				
				try {
					AccidentReportUtils.addAccidentReport(accidentReport1);
				} catch (JAXBException e) {
					logError("Could not add new AccidentReport: ", e);
				}
				try {
					if (ConfigReader.configRead("soundSettings", "playCreateReport").equalsIgnoreCase("true")) {
						playSound(getJarPath() + "/sounds/alert-success.wav");
					}
				} catch (IOException e) {
					logError("Error getting configValue for playCreateReport: ", e);
				}
				LogViewController.needRefresh.set(1);
				
				NotificationManager.showNotificationInfo("Report Manager", "A new Accident Report has been submitted.");
				
				CustomWindow window = getWindow(localization.getLocalizedMessage("ReportWindows.AccidentReportTitle", "Accident Report"));
				if (window != null) {
					window.closeWindow();
				}
			}
		});
		return accidentReport;
	}
	
	public static AccidentReports loadAccidentReports() throws JAXBException {
		File file = new File(accidentLogURL);
		if (!file.exists()) {
			return new AccidentReports();
		}
		
		try {
			JAXBContext context = JAXBContext.newInstance(AccidentReports.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (AccidentReports) unmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			logError("Error loading AccidentReports: ", e);
			throw e;
		}
	}
	
	private static void saveAccidentReports(AccidentReports AccidentReports) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(AccidentReports.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		File file = new File(accidentLogURL);
		marshaller.marshal(AccidentReports, file);
	}
	
	public static void addAccidentReport(AccidentReport AccidentReport) throws JAXBException {
		AccidentReports AccidentReports = loadAccidentReports();
		
		if (AccidentReports.getAccidentReportList() == null) {
			AccidentReports.setAccidentReportList(new java.util.ArrayList<>());
		}
		
		Optional<AccidentReport> existingReport = AccidentReports.getAccidentReportList().stream().filter(e -> e.getAccidentNumber().equals(AccidentReport.getAccidentNumber())).findFirst();
		
		if (existingReport.isPresent()) {
			AccidentReports.getAccidentReportList().remove(existingReport.get());
			AccidentReports.getAccidentReportList().add(AccidentReport);
			log("AccidentReport with number " + AccidentReport.getAccidentNumber() + " updated.", LogUtils.Severity.INFO);
		} else {
			AccidentReports.getAccidentReportList().add(AccidentReport);
			log("AccidentReport with number " + AccidentReport.getAccidentNumber() + " added.", LogUtils.Severity.INFO);
		}
		
		saveAccidentReports(AccidentReports);
	}
	
	public static void deleteAccidentReport(String AccidentReportnumber) throws JAXBException {
		AccidentReports AccidentReports = loadAccidentReports();
		
		if (AccidentReports.getAccidentReportList() != null) {
			AccidentReports.getAccidentReportList().removeIf(e -> e.getAccidentNumber().equals(AccidentReportnumber));
			saveAccidentReports(AccidentReports);
			log("AccidentReport with number " + AccidentReportnumber + " deleted.", LogUtils.Severity.INFO);
		}
	}
	
}