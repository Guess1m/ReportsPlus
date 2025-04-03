package com.Guess.ReportsPlus.logs.Death;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.config.ConfigReader;
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
import static com.Guess.ReportsPlus.Windows.Apps.LogViewController.deathReportLogUpdate;
import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.notesViewController;
import static com.Guess.ReportsPlus.util.Misc.AudioUtil.playSound;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logInfo;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.*;
import static com.Guess.ReportsPlus.util.Report.nestedReportUtils.*;
import static com.Guess.ReportsPlus.util.Report.reportUtil.*;
import static com.Guess.ReportsPlus.util.Strings.URLStrings.DeathReportLogURL;

public class DeathReportUtils {
	
	public static Map<String, Object> deathReportLayout() {
		SectionConfig decedentInfoSection = new SectionConfig(localization.getLocalizedMessage("ReportWindows.DeceasedInfoSectionHeader", "Deceased Information"),
		                                                      new RowConfig(new FieldConfig(localization.getLocalizedMessage("ReportWindows.DeathDecedentField", "decedent name"), 4, FieldType.TEXT_FIELD),
		                                                                    new FieldConfig(localization.getLocalizedMessage("ReportWindows.DeathAgeDOBField", "age/dob"), 4, FieldType.TEXT_FIELD),
		                                                                    new FieldConfig(localization.getLocalizedMessage("ReportWindows.DeathGenderField", "gender"), 4, FieldType.TEXT_FIELD)),
		                                                      new RowConfig(new FieldConfig(localization.getLocalizedMessage("ReportWindows.DeathReportAddressField", "address"), 6, FieldType.TEXT_FIELD),
		                                                                    new FieldConfig(localization.getLocalizedMessage("ReportWindows.DeathDescField", "description"), 6, FieldType.TEXT_FIELD)));
		decedentInfoSection.setHasButton(true);
		
		Map<String, Object> deathReport = createReportWindow(localization.getLocalizedMessage("ReportWindows.DeathReportTitle", "Death Report"), null, new SectionConfig(localization.getLocalizedMessage("ReportWindows.OfficerInfoSectionHeading", "Officer Information"), new RowConfig(
				                                                     new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"), 5, FieldType.OFFICER_NAME), new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"), 5, FieldType.OFFICER_RANK),
				                                                     new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"), 2, FieldType.OFFICER_NUMBER)), new RowConfig(new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"), 6, FieldType.OFFICER_DIVISION),
		                                                                                                                                                                                                          new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"), 6, FieldType.OFFICER_AGENCY))),
		                                                     new SectionConfig(localization.getLocalizedMessage("ReportWindows.LocationInfoSectionHeading", "Location / Timestamp Information"),
		                                                                       new RowConfig(new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"), 4, FieldType.COMBO_BOX_STREET),
		                                                                                     new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"), 4, FieldType.COMBO_BOX_AREA),
		                                                                                     new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"), 4, FieldType.COUNTY_FIELD)),
		                                                                       new RowConfig(new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"), 5, FieldType.DATE_FIELD),
		                                                                                     new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"), 5, FieldType.TIME_FIELD),
		                                                                                     new FieldConfig(localization.getLocalizedMessage("ReportWindows.DeathNumField", "death num"), 2, FieldType.NUMBER_FIELD))), decedentInfoSection,
		                                                     new SectionConfig(localization.getLocalizedMessage("ReportWindows.DeathInfoSectionHeader", "Death Information"),
		                                                                       new RowConfig(new FieldConfig(localization.getLocalizedMessage("ReportWindows.TimeOfDeathField", "time of death"), 6, FieldType.TEXT_FIELD),
		                                                                                     new FieldConfig(localization.getLocalizedMessage("ReportWindows.DateOfDeathField", "date of death"), 6, FieldType.TEXT_FIELD)),
		                                                                       new RowConfig(new FieldConfig(localization.getLocalizedMessage("ReportWindows.CauseOfDeathField", "cause of death"), 12, FieldType.TEXT_FIELD)),
		                                                                       new RowConfig(new FieldConfig(localization.getLocalizedMessage("ReportWindows.ModeOfDeathField", "mode of death"), 6, FieldType.TEXT_FIELD),
		                                                                                     new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldWitnesses", "witnesses"), 6, FieldType.TEXT_FIELD))),
		                                                     new SectionConfig(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"), new RowConfig(new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"), 12, FieldType.TEXT_AREA))));
		return deathReport;
	}
	
	public static Map<String, Object> newDeathReport() {
		Map<String, Object> deathReport = deathReportLayout();
		
		Map<String, Object> deathReportMap = (Map<String, Object>) deathReport.get(localization.getLocalizedMessage("ReportWindows.DeathReportTitle", "Death Report") + " Map");
		
		TextField name = (TextField) deathReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
		TextField rank = (TextField) deathReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
		TextField div = (TextField) deathReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
		TextField agen = (TextField) deathReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
		TextField num = (TextField) deathReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));
		
		TextField date = (TextField) deathReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
		TextField time = (TextField) deathReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
		ComboBox street = (ComboBox) deathReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
		ComboBox area = (ComboBox) deathReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
		TextField county = (TextField) deathReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
		TextField deathNum = (TextField) deathReportMap.get(localization.getLocalizedMessage("ReportWindows.DeathNumField", "death num"));
		deathNum.setText(generateReportNumber());
		
		TextField decedent = (TextField) deathReportMap.get(localization.getLocalizedMessage("ReportWindows.DeathDecedentField", "decedent name"));
		TextField age = (TextField) deathReportMap.get(localization.getLocalizedMessage("ReportWindows.DeathAgeDOBField", "age/dob"));
		TextField gender = (TextField) deathReportMap.get(localization.getLocalizedMessage("ReportWindows.DeathGenderField", "gender"));
		TextField address = (TextField) deathReportMap.get(localization.getLocalizedMessage("ReportWindows.DeathReportAddressField", "address"));
		TextField description = (TextField) deathReportMap.get(localization.getLocalizedMessage("ReportWindows.DeathDescField", "description"));
		
		TextField causeofdeath = (TextField) deathReportMap.get(localization.getLocalizedMessage("ReportWindows.CauseOfDeathField", "cause of death"));
		TextField modeofdeath = (TextField) deathReportMap.get(localization.getLocalizedMessage("ReportWindows.ModeOfDeathField", "mode of death"));
		TextField witnesses = (TextField) deathReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldWitnesses", "witnesses"));
		
		TextField timeofdeath = (TextField) deathReportMap.get(localization.getLocalizedMessage("ReportWindows.TimeOfDeathField", "time of death"));
		TextField dateofdeath = (TextField) deathReportMap.get(localization.getLocalizedMessage("ReportWindows.DateOfDeathField", "date of death"));
		
		TextArea notes = (TextArea) deathReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"));
		
		Label warningLabel = (Label) deathReport.get("warningLabel");
		
		MenuButton pullnotesbtn = (MenuButton) deathReport.get("pullNotesBtn");
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
							updateTextFromNotepad(notes, noteArea, "-comments");
							updateTextFromNotepad(decedent, noteArea, "-name");
							updateTextFromNotepad(age, noteArea, "-age");
							updateTextFromNotepad(address, noteArea, "-address");
							updateTextFromNotepad(description, noteArea, "-description");
							updateTextFromNotepad(deathNum, noteArea, "-number");
							updateTextFromNotepad(county, noteArea, "-county");
							updateTextFromNotepad(area.getEditor(), noteArea, "-area");
							updateTextFromNotepad(street.getEditor(), noteArea, "-street");
							updateTextFromNotepad(gender, noteArea, "-gender");
						});
					}
				}
			} else {
				logError("NotesViewController Is Null");
			}
		});
		
		Button submitBtn = (Button) deathReport.get("submitBtn");
		ComboBox<String> statusValue = (ComboBox) deathReport.get("statusValue");
		
		submitBtn.setOnAction(event -> {
			if (deathNum.getText().trim().isEmpty()) {
				warningLabel.setVisible(true);
				warningLabel.setText("Death Number can't be empty!");
				warningLabel.setStyle("-fx-font-family: \"Inter 28pt Bold\"; -fx-text-fill: red;");
				PauseTransition pause = new PauseTransition(Duration.seconds(2));
				pause.setOnFinished(e -> warningLabel.setVisible(false));
				pause.play();
			} else {
				for (String fieldName : deathReportMap.keySet()) {
					Object field = deathReportMap.get(fieldName);
					if (field instanceof ComboBox<?> comboBox) {
						if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
							comboBox.getSelectionModel().selectFirst();
						}
					}
				}
				DeathReport deathReport1 = new DeathReport();
				deathReport1.setStatus(statusValue.getValue());
				deathReport1.setAddress(toTitleCase(address.getText()));
				deathReport1.setCauseOfDeath(toTitleCase(causeofdeath.getText()));
				deathReport1.setDeathReportNumber(deathNum.getText());
				deathReport1.setAge(toTitleCase(age.getText()));
				deathReport1.setArea(toTitleCase(area.getEditor().getText()));
				deathReport1.setAgency(toTitleCase(agen.getText()));
				deathReport1.setCounty(toTitleCase(county.getText()));
				deathReport1.setDescription(toTitleCase(description.getText()));
				deathReport1.setModeOfDeath(toTitleCase(modeofdeath.getText()));
				deathReport1.setDate(date.getText());
				deathReport1.setTime(time.getText());
				deathReport1.setGender(toTitleCase(gender.getText()));
				deathReport1.setDecedent(toTitleCase(decedent.getText()));
				deathReport1.setDivision(toTitleCase(div.getText()));
				deathReport1.setWitnesses(toTitleCase(witnesses.getText()));
				deathReport1.setStreet(toTitleCase(street.getEditor().getText()));
				deathReport1.setName(toTitleCase(name.getText()));
				deathReport1.setNotesTextArea(notes.getText());
				deathReport1.setNumber(toTitleCase(num.getText()));
				deathReport1.setRank(rank.getText());
				deathReport1.setTimeOfDeath(timeofdeath.getText());
				deathReport1.setDateOfDeath(dateofdeath.getText());
				try {
					DeathReportUtils.addDeathReport(deathReport1);
				} catch (JAXBException e) {
					logError("JAXB Error creating death report: ", e);
				}
				
				try {
					if (ConfigReader.configRead("soundSettings", "playCreateReport").equalsIgnoreCase("true")) {
						playSound(getJarPath() + "/sounds/alert-success.wav");
					}
				} catch (IOException e) {
					logError("Error getting configValue for playCreateReport: ", e);
				}
				deathReportLogUpdate();
				NotificationManager.showNotificationInfo("Report Manager", "A new Death Report has been submitted.");
				
				CustomWindow window = getWindow("Death Report");
				if (window != null) {
					window.closeWindow();
				}
			}
		});
		
		Button pullDecedentInfoBtn = (Button) deathReport.get(localization.getLocalizedMessage("ReportWindows.DeceasedInfoSectionHeader", "Deceased Information") + "_button");
		
		pullDecedentInfoBtn.setOnAction(event -> {
			String fulln = pullValueFromReport("ped", "Pedfnamefield") + " " + pullValueFromReport("ped", "Pedlnamefield");
			if (!fulln.trim().isEmpty()) {
				decedent.setText(fulln);
			}
			age.setText(pullValueFromReport("ped", "Peddobfield"));
			gender.setText(pullValueFromReport("ped", "Pedgenfield"));
			address.setText(pullValueFromReport("ped", "Pedaddressfield"));
			description.setText(pullValueFromReport("ped", "Peddescfield"));
		});
		
		return deathReport;
	}
	
	public static DeathReports loadDeathReports() throws JAXBException {
		File file = new File(DeathReportLogURL);
		if (!file.exists()) {
			return new DeathReports();
		}
		
		try {
			JAXBContext context = JAXBContext.newInstance(DeathReports.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (DeathReports) unmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			logError("Error loading DeathReports: ", e);
			throw e;
		}
	}
	
	private static void saveDeathReports(DeathReports DeathReports) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(DeathReports.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		File file = new File(DeathReportLogURL);
		marshaller.marshal(DeathReports, file);
	}
	
	public static void addDeathReport(DeathReport DeathReport) throws JAXBException {
		DeathReports DeathReports = loadDeathReports();
		
		if (DeathReports.getDeathReportList() == null) {
			DeathReports.setDeathReportList(new java.util.ArrayList<>());
		}
		
		Optional<DeathReport> existingReport = DeathReports.getDeathReportList().stream().filter(e -> e.getDeathReportNumber().equals(DeathReport.getDeathReportNumber())).findFirst();
		
		if (existingReport.isPresent()) {
			DeathReports.getDeathReportList().remove(existingReport.get());
			DeathReports.getDeathReportList().add(DeathReport);
			logInfo("DeathReport with number " + DeathReport.getDeathReportNumber() + " updated.");
		} else {
			DeathReports.getDeathReportList().add(DeathReport);
			logInfo("DeathReport with number " + DeathReport.getDeathReportNumber() + " added.");
		}
		
		saveDeathReports(DeathReports);
	}
	
	public static void deleteDeathReport(String DeathReportnumber) throws JAXBException {
		DeathReports DeathReports = loadDeathReports();
		
		if (DeathReports.getDeathReportList() != null) {
			DeathReports.getDeathReportList().removeIf(e -> e.getDeathReportNumber().equals(DeathReportnumber));
			saveDeathReports(DeathReports);
			logInfo("DeathReport with number " + DeathReportnumber + " deleted.");
		}
	}
	
}