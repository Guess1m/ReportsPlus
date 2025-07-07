package com.Guess.ReportsPlus.logs.Accident;

import static com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager.getWindow;
import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.Windows.Apps.LogViewController.accidentReportUpdate;
import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.notesViewController;
import static com.Guess.ReportsPlus.util.Misc.AudioUtil.playSound;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logInfo;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getJarPath;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.toTitleCase;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.updateTextFromNotepad;
import static com.Guess.ReportsPlus.util.Report.reportUtil.createReportWindow;
import static com.Guess.ReportsPlus.util.Report.reportUtil.pullValueFromReport;
import static com.Guess.ReportsPlus.util.Strings.URLStrings.accidentLogURL;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.util.Misc.NotificationManager;
import com.Guess.ReportsPlus.util.Report.nestedReportUtils.FieldConfig;
import com.Guess.ReportsPlus.util.Report.nestedReportUtils.FieldType;
import com.Guess.ReportsPlus.util.Report.nestedReportUtils.RowConfig;
import com.Guess.ReportsPlus.util.Report.nestedReportUtils.SectionConfig;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.animation.PauseTransition;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class AccidentReportUtils {

	public static Map<String, Object> accidentLayout() {
		SectionConfig offenderInfoSection = new SectionConfig(
				localization.getLocalizedMessage("ReportWindows.OffenderInfoSectionHeading", "Offender Information"),
				new RowConfig(
						new FieldConfig(
								localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"), 4,
								FieldType.TEXT_FIELD),
						new FieldConfig(
								localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"), 4,
								FieldType.TEXT_FIELD),
						new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOffenderGender",
								"offender gender"), 4, FieldType.TEXT_FIELD)),
				new RowConfig(
						new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress",
								"offender address"), 6, FieldType.TEXT_FIELD),
						new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOffenderDescription",
								"offender description"), 6, FieldType.TEXT_FIELD)));
		offenderInfoSection.setHasButton(true);

		SectionConfig vehInfoSection = new SectionConfig(
				localization.getLocalizedMessage("ReportWindows.OffenderVehicleInfoSectionHeading",
						"Offender Vehicle Information"),
				new RowConfig(
						new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldModel", "model"), 6,
								FieldType.TEXT_FIELD),
						new FieldConfig(
								localization.getLocalizedMessage("ReportWindows.FieldPlateNumber", "plate number"), 6,
								FieldType.TEXT_FIELD)),
				new RowConfig(
						new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldType", "type"), 7,
								FieldType.COMBO_BOX_TYPE),
						new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldColor", "color"), 5,
								FieldType.COMBO_BOX_COLOR)));
		vehInfoSection.setHasButton(true);

		Map<String, Object> accidentReport = createReportWindow(
				localization.getLocalizedMessage("ReportWindows.AccidentReportTitle", "Accident Report"), null,
				new SectionConfig(
						localization.getLocalizedMessage("ReportWindows.OfficerInfoSectionHeading",
								"Officer Information"),
						new RowConfig(
								new FieldConfig(
										localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"), 5,
										FieldType.OFFICER_NAME),
								new FieldConfig(
										localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"), 5,
										FieldType.OFFICER_RANK),
								new FieldConfig(
										localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"),
										2, FieldType.OFFICER_NUMBER)),
						new RowConfig(
								new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision",
										"division"), 6, FieldType.OFFICER_DIVISION),
								new FieldConfig(
										localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"),
										6, FieldType.OFFICER_AGENCY))),
				new SectionConfig(
						localization.getLocalizedMessage("ReportWindows.LocationInfoSectionHeading",
								"Location / Timestamp Information"),
						new RowConfig(
								new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"),
										4, FieldType.COMBO_BOX_STREET),
								new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"), 4,
										FieldType.COMBO_BOX_AREA),
								new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"),
										4, FieldType.COUNTY_FIELD)),
						new RowConfig(
								new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"), 5,
										FieldType.DATE_FIELD),
								new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"), 5,
										FieldType.TIME_FIELD),
								new FieldConfig(localization.getLocalizedMessage("ReportWindows.AccidentNumberField",
										"accident number"), 2, FieldType.NUMBER_FIELD))),
				new SectionConfig(
						localization.getLocalizedMessage("ReportWindows.AccidentInformationSection",
								"Accident Information"),
						new RowConfig(
								new FieldConfig(
										localization.getLocalizedMessage("ReportWindows.AccidentWeatherConditionsField",
												"weather conditions"),
										6, FieldType.TEXT_FIELD),
								new FieldConfig(
										localization.getLocalizedMessage("ReportWindows.AccidentRoadConditionsField",
												"road conditions"),
										6, FieldType.TEXT_FIELD)),
						new RowConfig(new FieldConfig(
								localization.getLocalizedMessage("ReportWindows.AccidentOtherVehiclesField",
										"other vehicles involved"),
								12, FieldType.TEXT_FIELD)),
						new RowConfig(new FieldConfig(
								localization.getLocalizedMessage("ReportWindows.FieldWitnesses", "witnesses"), 12,
								FieldType.TEXT_FIELD)),
						new RowConfig(new FieldConfig(
								localization.getLocalizedMessage("ReportWindows.AccidentInjuriesField", "injuries"), 12,
								FieldType.TEXT_FIELD)),
						new RowConfig(new FieldConfig(
								localization.getLocalizedMessage("ReportWindows.AccidentDamagesField", "damages"), 12,
								FieldType.TEXT_FIELD))),
				offenderInfoSection, vehInfoSection,
				new SectionConfig(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"),
						new RowConfig(
								new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"),
										12, FieldType.TEXT_AREA))));
		return accidentReport;
	}

	public static Map<String, Object> newAccident() {
		Map<String, Object> accidentReport = accidentLayout();

		Map<String, Object> accidentReportMap = (Map<String, Object>) accidentReport
				.get(localization.getLocalizedMessage("ReportWindows.AccidentReportTitle", "Accident Report") + " Map");

		TextField name = (TextField) accidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
		TextField rank = (TextField) accidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
		TextField div = (TextField) accidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
		TextField agen = (TextField) accidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
		TextField num = (TextField) accidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));
		ComboBox street = (ComboBox) accidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
		ComboBox area = (ComboBox) accidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
		TextField county = (TextField) accidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
		TextField date = (TextField) accidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
		TextField time = (TextField) accidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
		TextField accidentnum = (TextField) accidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.AccidentNumberField", "accident number"));
		TextField weatherConditions = (TextField) accidentReportMap.get(
				localization.getLocalizedMessage("ReportWindows.AccidentWeatherConditionsField", "weather conditions"));
		TextField roadConditions = (TextField) accidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.AccidentRoadConditionsField", "road conditions"));
		TextField otherVehiclesInvolved = (TextField) accidentReportMap.get(localization
				.getLocalizedMessage("ReportWindows.AccidentOtherVehiclesField", "other vehicles involved"));
		TextField witnesses = (TextField) accidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldWitnesses", "witnesses"));
		TextField injuries = (TextField) accidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.AccidentInjuriesField", "injuries"));
		TextField damages = (TextField) accidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.AccidentDamagesField", "damages"));
		TextField offenderName = (TextField) accidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
		TextField offenderAge = (TextField) accidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"));
		TextField offenderGender = (TextField) accidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"));
		TextField offenderAddress = (TextField) accidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"));
		TextField offenderDescription = (TextField) accidentReportMap.get(
				localization.getLocalizedMessage("ReportWindows.FieldOffenderDescription", "offender description"));
		TextField model = (TextField) accidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldModel", "model"));
		TextField plateNumber = (TextField) accidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldPlateNumber", "plate number"));
		ComboBox type = (ComboBox) accidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldType", "type"));
		ComboBox color = (ComboBox) accidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldColor", "color"));
		TextArea notes = (TextArea) accidentReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"));

		Label warningLabel = (Label) accidentReport.get("warningLabel");
		Label legacyLabel = (Label) accidentReport.get("legacyLabel");
		legacyLabel.setVisible(true);

		MenuButton pullnotesbtn = (MenuButton) accidentReport.get("pullNotesBtn");
		pullnotesbtn.setPopupSide(Side.TOP);

		pullnotesbtn.setOnMouseEntered(_ -> {
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
				logError("NotesViewController Is Null");
			}
		});

		Button submitBtn = (Button) accidentReport.get("submitBtn");

		ComboBox<String> statusValue = (ComboBox) accidentReport.get("statusValue");

		submitBtn.setOnAction(_ -> {
			if (accidentnum.getText().trim().isEmpty()) {
				warningLabel.setVisible(true);
				warningLabel.setText("Accident Number can't be empty!");
				warningLabel.setStyle("-fx-font-family: \"Inter 28pt Bold\"; -fx-text-fill: red;");
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
				accidentReportUpdate();

				NotificationManager.showNotificationInfo("Report Manager", "A new Accident Report has been submitted.");

				CustomWindow window = getWindow(
						localization.getLocalizedMessage("ReportWindows.AccidentReportTitle", "Accident Report"));
				if (window != null) {
					window.closeWindow();
				}
			}
		});

		Button offenderInfoBtn = (Button) accidentReport.get(
				localization.getLocalizedMessage("ReportWindows.OffenderInfoSectionHeading", "Offender Information")
						+ "_button");

		Button vehInfoBtn = (Button) accidentReport
				.get(localization.getLocalizedMessage("ReportWindows.OffenderVehicleInfoSectionHeading",
						"Offender Vehicle Information") + "_button");

		offenderInfoBtn.setOnAction(_ -> {
			String fulln = pullValueFromReport("ped", "Pedfnamefield") + " "
					+ pullValueFromReport("ped", "Pedlnamefield");
			if (!fulln.trim().isEmpty()) {
				offenderName.setText(fulln);
			}
			offenderAge.setText(pullValueFromReport("ped", "Peddobfield"));
			offenderGender.setText(pullValueFromReport("ped", "Pedgenfield"));
			offenderAddress.setText(pullValueFromReport("ped", "Pedaddressfield"));
			offenderDescription.setText(pullValueFromReport("ped", "Peddescfield"));
		});

		vehInfoBtn.setOnAction(_ -> {
			plateNumber.setText(pullValueFromReport("vehicle", "Vehplatefield2"));
			model.setText(pullValueFromReport("vehicle", "Vehmodelfield"));
			String typ = pullValueFromReport("vehicle", "Vehtypecombobox");
			if (!typ.trim().isEmpty()) {
				type.setValue(typ);
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

		Optional<AccidentReport> existingReport = AccidentReports.getAccidentReportList().stream()
				.filter(e -> e.getAccidentNumber().equals(AccidentReport.getAccidentNumber())).findFirst();

		if (existingReport.isPresent()) {
			AccidentReports.getAccidentReportList().remove(existingReport.get());
			AccidentReports.getAccidentReportList().add(AccidentReport);
			logInfo("AccidentReport with number " + AccidentReport.getAccidentNumber() + " updated.");
		} else {
			AccidentReports.getAccidentReportList().add(AccidentReport);
			logInfo("AccidentReport with number " + AccidentReport.getAccidentNumber() + " added.");
		}

		saveAccidentReports(AccidentReports);
	}

	public static void deleteAccidentReport(String AccidentReportnumber) throws JAXBException {
		AccidentReports AccidentReports = loadAccidentReports();

		if (AccidentReports.getAccidentReportList() != null) {
			AccidentReports.getAccidentReportList().removeIf(e -> e.getAccidentNumber().equals(AccidentReportnumber));
			saveAccidentReports(AccidentReports);
			logInfo("AccidentReport with number " + AccidentReportnumber + " deleted.");
		}
	}

}