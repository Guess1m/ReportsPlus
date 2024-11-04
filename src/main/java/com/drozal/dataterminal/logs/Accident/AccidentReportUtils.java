package com.drozal.dataterminal.logs.Accident;

import com.drozal.dataterminal.Desktop.Utils.WindowUtils.CustomWindow;
import com.drozal.dataterminal.Windows.Apps.LogViewController;
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
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.drozal.dataterminal.DataTerminalHomeApplication.getDate;
import static com.drozal.dataterminal.DataTerminalHomeApplication.getTime;
import static com.drozal.dataterminal.Desktop.Utils.WindowUtils.WindowManager.getWindow;
import static com.drozal.dataterminal.Windows.Other.NotesViewController.notesViewController;
import static com.drozal.dataterminal.util.Misc.AudioUtil.playSound;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.controllerUtils.toTitleCase;
import static com.drozal.dataterminal.util.Misc.controllerUtils.updateTextFromNotepad;
import static com.drozal.dataterminal.util.Misc.stringUtil.accidentLogURL;
import static com.drozal.dataterminal.util.Misc.stringUtil.getJarPath;
import static com.drozal.dataterminal.util.Report.reportUtil.createReportWindow;
import static com.drozal.dataterminal.util.Report.reportUtil.generateReportNumber;

public class AccidentReportUtils {
	
	public static int countReports() {
		try {
			List<AccidentReport> logs = AccidentReportUtils.loadAccidentReports().getAccidentReportList();
			
			if (logs == null) {
				return 0;
			}
			
			return logs.size();
		} catch (Exception e) {
			logError("Exception", e);
			return -1;
		}
	}
	
	public static Map<String, Object> accidentLayout() {
		Map<String, Object> accidentReport = createReportWindow("Accident Report", null,
		                                                        new nestedReportUtils.SectionConfig(
				                                                        "Officer Information", true,
				                                                        new nestedReportUtils.RowConfig(
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "name", 5,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD),
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "rank", 5,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD),
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "number", 2,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD)),
				                                                        new nestedReportUtils.RowConfig(
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "division", 6,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD),
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "agency", 6,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                        new nestedReportUtils.SectionConfig(
				                                                        "Location / Timestamp Information", true,
				                                                        new nestedReportUtils.RowConfig(
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "street", 4,
								                                                        nestedReportUtils.FieldType.COMBO_BOX_STREET),
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "area", 4,
								                                                        nestedReportUtils.FieldType.COMBO_BOX_AREA),
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "county", 4,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD)),
				                                                        new nestedReportUtils.RowConfig(
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "date", 5,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD),
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "time", 5,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD),
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "accident number", 2,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                        new nestedReportUtils.SectionConfig(
				                                                        "Accident Information", true,
				                                                        new nestedReportUtils.RowConfig(
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "weather conditions", 6,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD),
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "road conditions", 6,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD)),
				                                                        new nestedReportUtils.RowConfig(
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "other vehicles involved", 12,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD)),
				                                                        new nestedReportUtils.RowConfig(
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "witnesses", 12,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD)),
				                                                        new nestedReportUtils.RowConfig(
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "injuries", 12,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD)),
				                                                        new nestedReportUtils.RowConfig(
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "damages", 12,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                        new nestedReportUtils.SectionConfig(
				                                                        "Offender Information", true,
				                                                        new nestedReportUtils.RowConfig(
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "offender name", 4,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD),
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "offender age", 4,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD),
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "offender gender", 4,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD)),
				                                                        new nestedReportUtils.RowConfig(
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "offender address", 6,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD),
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "offender description", 6,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD))),
		                                                        new nestedReportUtils.SectionConfig(
				                                                        "Offender Vehicle Information", true,
				                                                        new nestedReportUtils.RowConfig(
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "model", 6,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD),
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "plate number", 6,
								                                                        nestedReportUtils.FieldType.TEXT_FIELD)),
				                                                        new nestedReportUtils.RowConfig(
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "type", 7,
								                                                        nestedReportUtils.FieldType.COMBO_BOX_TYPE),
						                                                        new nestedReportUtils.FieldConfig(
								                                                        "color", 5,
								                                                        nestedReportUtils.FieldType.COMBO_BOX_COLOR))),
		                                                        new nestedReportUtils.SectionConfig("Citation Notes",
		                                                                                            true,
		                                                                                            new nestedReportUtils.RowConfig(
				                                                                                            new nestedReportUtils.FieldConfig(
						                                                                                            "notes",
						                                                                                            12,
						                                                                                            nestedReportUtils.FieldType.TEXT_AREA))));
		return accidentReport;
	}
	
	public static Map<String, Object> newAccident() {
		Map<String, Object> accidentReport = accidentLayout();
		
		Map<String, Object> accidentReportMap = (Map<String, Object>) accidentReport.get("Accident Report Map");
		
		TextField name = (TextField) accidentReportMap.get("name");
		TextField rank = (TextField) accidentReportMap.get("rank");
		TextField div = (TextField) accidentReportMap.get("division");
		TextField agen = (TextField) accidentReportMap.get("agency");
		TextField num = (TextField) accidentReportMap.get("number");
		ComboBox street = (ComboBox) accidentReportMap.get("street");
		ComboBox area = (ComboBox) accidentReportMap.get("area");
		TextField county = (TextField) accidentReportMap.get("county");
		TextField date = (TextField) accidentReportMap.get("date");
		TextField time = (TextField) accidentReportMap.get("time");
		TextField accidentnum = (TextField) accidentReportMap.get("accident number");
		TextField weatherConditions = (TextField) accidentReportMap.get("weather conditions");
		TextField roadConditions = (TextField) accidentReportMap.get("road conditions");
		TextField otherVehiclesInvolved = (TextField) accidentReportMap.get("other vehicles involved");
		TextField witnesses = (TextField) accidentReportMap.get("witnesses");
		TextField injuries = (TextField) accidentReportMap.get("injuries");
		TextField damages = (TextField) accidentReportMap.get("damages");
		TextField offenderName = (TextField) accidentReportMap.get("offender name");
		TextField offenderAge = (TextField) accidentReportMap.get("offender age");
		TextField offenderGender = (TextField) accidentReportMap.get("offender gender");
		TextField offenderAddress = (TextField) accidentReportMap.get("offender address");
		TextField offenderDescription = (TextField) accidentReportMap.get("offender description");
		TextField model = (TextField) accidentReportMap.get("model");
		TextField plateNumber = (TextField) accidentReportMap.get("plate number");
		ComboBox type = (ComboBox) accidentReportMap.get("type");
		ComboBox color = (ComboBox) accidentReportMap.get("color");
		TextArea notes = (TextArea) accidentReportMap.get("notes");
		
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
				
				CustomWindow window = getWindow("Accident Report");
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
		
		Optional<AccidentReport> existingReport = AccidentReports.getAccidentReportList().stream().filter(
				e -> e.getAccidentNumber().equals(AccidentReport.getAccidentNumber())).findFirst();
		
		if (existingReport.isPresent()) {
			AccidentReports.getAccidentReportList().remove(existingReport.get());
			AccidentReports.getAccidentReportList().add(AccidentReport);
			log("AccidentReport with number " + AccidentReport.getAccidentNumber() + " updated.",
			    LogUtils.Severity.INFO);
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