package com.Guess.ReportsPlus.logs.TrafficCitation;

import static com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager.getWindow;
import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.Windows.Apps.CourtViewController.getNextIndex;
import static com.Guess.ReportsPlus.Windows.Apps.CourtViewController.needCourtRefresh;
import static com.Guess.ReportsPlus.Windows.Apps.LogViewController.citationLogUpdate;
import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.notesViewController;
import static com.Guess.ReportsPlus.util.CourtData.CourtUtils.findCaseByNumber;
import static com.Guess.ReportsPlus.util.CourtData.CourtUtils.generateCaseNumber;
import static com.Guess.ReportsPlus.util.CourtData.CourtUtils.loadCourtCases;
import static com.Guess.ReportsPlus.util.CourtData.CourtUtils.scheduleOutcomeRevealForSingleCase;
import static com.Guess.ReportsPlus.util.Misc.AudioUtil.playSound;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logDebug;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logInfo;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logWarn;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getJarPath;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.toTitleCase;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.updateTextFromNotepad;
import static com.Guess.ReportsPlus.util.Report.reportUtil.createReportWindow;
import static com.Guess.ReportsPlus.util.Report.reportUtil.extractMaxFine;
import static com.Guess.ReportsPlus.util.Report.reportUtil.pullValueFromReport;
import static com.Guess.ReportsPlus.util.Report.treeViewUtils.findXMLValue;
import static com.Guess.ReportsPlus.util.Server.ClientUtils.isConnected;
import static com.Guess.ReportsPlus.util.Strings.URLStrings.trafficCitationLogURL;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.logs.CitationsData;
import com.Guess.ReportsPlus.logs.Impound.ImpoundReportUtils;
import com.Guess.ReportsPlus.util.CourtData.Case;
import com.Guess.ReportsPlus.util.CourtData.CourtUtils;
import com.Guess.ReportsPlus.util.History.Ped;
import com.Guess.ReportsPlus.util.Misc.NotificationManager;
import com.Guess.ReportsPlus.util.Report.nestedReportUtils.FieldConfig;
import com.Guess.ReportsPlus.util.Report.nestedReportUtils.FieldType;
import com.Guess.ReportsPlus.util.Report.nestedReportUtils.RowConfig;
import com.Guess.ReportsPlus.util.Report.nestedReportUtils.SectionConfig;
import com.Guess.ReportsPlus.util.Report.nestedReportUtils.TransferConfig;
import com.Guess.ReportsPlus.util.Server.ClientUtils;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.animation.PauseTransition;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class TrafficCitationUtils {

	public static Map<String, Object> citationLayout() {
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

		SectionConfig vehicleInfoSection = new SectionConfig(
				localization.getLocalizedMessage("ReportWindows.CitationVehicleInfoHeading",
						"(If Applicable) Offender Vehicle Information"),
				new RowConfig(
						new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldModel", "model"), 4,
								FieldType.TEXT_FIELD),
						new FieldConfig(
								localization.getLocalizedMessage("ReportWindows.FieldPlateNumber", "plate number"), 4,
								FieldType.TEXT_FIELD),
						new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldColor", "color"), 4,
								FieldType.COMBO_BOX_COLOR)),
				new RowConfig(
						new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldType", "type"), 4,
								FieldType.COMBO_BOX_TYPE),
						new FieldConfig(
								localization.getLocalizedMessage("ReportWindows.OtherInfoField",
										localization.getLocalizedMessage("ReportWindows.OtherInfoField", "other info")),
								8, FieldType.TEXT_FIELD)));
		vehicleInfoSection.setHasButton(true);

		Map<String, Object> citationReport = createReportWindow(
				localization.getLocalizedMessage("ReportWindows.CitationReportTitle", "Citation Report"),
				new TransferConfig(
						localization.getLocalizedMessage("ReportWindows.TransferReportInfoButton",
								"Transfer Information To New Report"),
						new RowConfig(new FieldConfig("transferimpoundbtn", 12, FieldType.TRANSFER_BUTTON))),
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
								new FieldConfig(localization.getLocalizedMessage("ReportWindows.CitationNumField",
										"citation number"), 2, FieldType.NUMBER_FIELD))),
				offenderInfoSection, vehicleInfoSection,
				new SectionConfig(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"),
						new RowConfig(
								new FieldConfig(localization.getLocalizedMessage("ReportWindows.FieldNotes", "notes"),
										12, FieldType.TEXT_AREA))),
				new SectionConfig(localization.getLocalizedMessage("ReportWindows.CitationsHeading", "Citation(s)"),
						new RowConfig(new FieldConfig("citationview", 6, FieldType.CITATION_TREE_VIEW))));
		return citationReport;
	}

	public static Map<String, Object> newCitation() {
		Map<String, Object> citationReport = citationLayout();

		Map<String, Object> citationReportMap = (Map<String, Object>) citationReport
				.get(localization.getLocalizedMessage("ReportWindows.CitationReportTitle", "Citation Report") + " Map");

		TextField officername = (TextField) citationReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
		TextField officerrank = (TextField) citationReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
		TextField officerdiv = (TextField) citationReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
		TextField officeragen = (TextField) citationReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
		TextField officernum = (TextField) citationReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));

		TextField offenderName = (TextField) citationReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
		TextField offenderAge = (TextField) citationReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"));
		TextField offenderGender = (TextField) citationReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"));
		TextField offenderAddress = (TextField) citationReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"));
		TextField offenderDescription = (TextField) citationReportMap.get(
				localization.getLocalizedMessage("ReportWindows.FieldOffenderDescription", "offender description"));

		ComboBox area = (ComboBox) citationReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
		ComboBox street = (ComboBox) citationReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
		TextField county = (TextField) citationReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
		TextField num = (TextField) citationReportMap
				.get(localization.getLocalizedMessage("ReportWindows.CitationNumField", "citation number"));
		TextField date = (TextField) citationReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
		TextField time = (TextField) citationReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));

		ComboBox color = (ComboBox) citationReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldColor", "color"));
		ComboBox type = (ComboBox) citationReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldType", "type"));

		TextField plateNumber = (TextField) citationReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldPlateNumber", "plate number"));
		TextField otherInfo = (TextField) citationReportMap
				.get(localization.getLocalizedMessage("ReportWindows.OtherInfoField", "other info"));
		TextField model = (TextField) citationReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldModel", "model"));

		TextArea notes = (TextArea) citationReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "notes"));

		TableView citationtable = (TableView) citationReportMap.get("CitationTableView");
		ComboBox citationType = (ComboBox) citationReportMap.get("CitationType");

		Button transferimpoundbtn = (Button) citationReportMap.get("transferimpoundbtn");
		transferimpoundbtn.setText(localization.getLocalizedMessage("ReportWindows.NewLabel", "New") + " "
				+ localization.getLocalizedMessage("ReportWindows.ImpoundReportTitle", "Impound Report"));

		MenuButton pullnotesbtn = (MenuButton) citationReport.get("pullNotesBtn");
		pullnotesbtn.setPopupSide(Side.TOP);

		Label legacyLabel = (Label) citationReport.get("legacyLabel");
		legacyLabel.setVisible(true);

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
							updateTextFromNotepad(area.getEditor(), noteArea, "-area");
							updateTextFromNotepad(county, noteArea, "-county");
							updateTextFromNotepad(street.getEditor(), noteArea, "-street");
							updateTextFromNotepad(offenderName, noteArea, "-name");
							updateTextFromNotepad(offenderAge, noteArea, "-age");
							updateTextFromNotepad(offenderGender, noteArea, "-gender");
							updateTextFromNotepad(offenderDescription, noteArea, "-description");
							updateTextFromNotepad(notes, noteArea, "-comments");
							updateTextFromNotepad(offenderAddress, noteArea, "-address");
							updateTextFromNotepad(model, noteArea, "-model");
							updateTextFromNotepad(plateNumber, noteArea, "-plate");
							updateTextFromNotepad(num, noteArea, "-number");
							updateTextFromNotepad(type, noteArea, "-type");
						});
					}
				}
			} else {
				logError("NotesViewController Is Null");
			}
		});

		transferimpoundbtn.setOnAction(_ -> {

			Map<String, Object> impoundReportObj = ImpoundReportUtils.newImpound();

			Map<String, Object> impoundReportMap = (Map<String, Object>) impoundReportObj.get(
					localization.getLocalizedMessage("ReportWindows.ImpoundReportTitle", "Impound Report") + " Map");

			TextField officernameimp = (TextField) impoundReportMap
					.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
			TextField officerrankimp = (TextField) impoundReportMap
					.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
			TextField officerdivimp = (TextField) impoundReportMap
					.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
			TextField officeragenimp = (TextField) impoundReportMap
					.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
			TextField officernumimp = (TextField) impoundReportMap
					.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));

			TextField offenderNameimp = (TextField) impoundReportMap
					.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
			TextField offenderAgeimp = (TextField) impoundReportMap
					.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"));
			TextField offenderGenderimp = (TextField) impoundReportMap
					.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"));
			TextField offenderAddressimp = (TextField) impoundReportMap
					.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"));

			TextField numimp = (TextField) impoundReportMap
					.get(localization.getLocalizedMessage("ReportWindows.ImpoundNumField", "impound number"));
			TextField dateimp = (TextField) impoundReportMap
					.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
			TextField timeimp = (TextField) impoundReportMap
					.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));

			ComboBox colorimp = (ComboBox) impoundReportMap
					.get(localization.getLocalizedMessage("ReportWindows.FieldColor", "color"));
			ComboBox typeimp = (ComboBox) impoundReportMap
					.get(localization.getLocalizedMessage("ReportWindows.FieldType", "type"));
			TextField plateNumberimp = (TextField) impoundReportMap
					.get(localization.getLocalizedMessage("ReportWindows.FieldPlateNumber", "plate number"));
			TextField modelimp = (TextField) impoundReportMap
					.get(localization.getLocalizedMessage("ReportWindows.FieldModel", "model"));

			TextArea notesimp = (TextArea) impoundReportMap
					.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "notes"));

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
		Label warningLabel = (Label) citationReport.get("warningLabel");

		ComboBox<String> statusValue = (ComboBox) citationReport.get("statusValue");

		submitBtn.setOnAction(_ -> {
			if (num.getText().trim().isEmpty()) {
				warningLabel.setVisible(true);
				warningLabel.setText("Citation Number can't be empty!");
				warningLabel.setStyle("-fx-font-family: \"Inter 28pt Bold\"; -fx-text-fill: red;");
				PauseTransition pause = new PauseTransition(Duration.seconds(2));
				pause.setOnFinished(e -> warningLabel.setVisible(false));
				pause.play();
			} else {
				for (String fieldName : citationReportMap.keySet()) {
					Object field = citationReportMap.get(fieldName);
					if (field instanceof ComboBox<?> comboBox) {
						if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
							comboBox.getSelectionModel().selectFirst();
						}
					}
				}

				if (citationType.getSelectionModel().getSelectedIndex() == 1 && offenderName.getText().isEmpty()) {
					if (offenderName.getText().trim().isEmpty()) {
						logError("Offender Name Cant Be Empty if Printing Ticket");
						warningLabel.setVisible(true);
						warningLabel.setText("Offender Name Field Empty!");
						warningLabel.setStyle("-fx-font-family: \"Inter 28pt Bold\"; -fx-text-fill: red;");
						PauseTransition pause = new PauseTransition(Duration.seconds(2));
						pause.setOnFinished(e -> warningLabel.setVisible(false));
						pause.play();
						return;
					}
				} else if (citationType.getSelectionModel().getSelectedIndex() == 2
						&& plateNumber.getText().isEmpty()) {
					logError("Vehicle Plate Cant Be Empty if Issuing Parking Ticket");
					warningLabel.setVisible(true);
					warningLabel.setText("Vehicle Plate Field Empty!");
					warningLabel.setStyle("-fx-font-family: \"Inter 28pt Bold\"; -fx-text-fill: red;");
					PauseTransition pause = new PauseTransition(Duration.seconds(2));
					pause.setOnFinished(e -> warningLabel.setVisible(false));
					pause.play();
					return;
				}

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
							int minFine = maxFine / 3;
							int randomFine = minFine + random.nextInt(maxFine - minFine + 1);
							chargesBuilder.append("Fined: ").append(randomFine).append(" | ");
						} catch (NumberFormatException e) {
							logError("Error parsing fine value " + fine + ": ", e);
							chargesBuilder.append("Fined: ").append(fine).append(" | ");
						}
					} else if (formData.getCitation().contains("MaxFine:")) {
						logDebug("Using Custom fine");
						int maxFine = Integer.parseInt(Objects.requireNonNull(extractMaxFine(formData.getCitation())));
						Random random = new Random();
						int minFine = maxFine / 3;
						int randomFine = minFine + random.nextInt(maxFine - minFine + 1);
						chargesBuilder.append("Fined: ").append(randomFine).append(" | ");
					} else {
						chargesBuilder.append("Fined: Not Found | ");
					}
				}
				if (stringBuilder.length() > 0) {
					stringBuilder.setLength(stringBuilder.length() - 1);
				}

				TrafficCitationReport trafficCitationReport = new TrafficCitationReport();
				trafficCitationReport.setStatus(statusValue.getValue());
				trafficCitationReport.setOfficerRank(officerrank.getText());
				trafficCitationReport.setCitationNumber(num.getText());
				trafficCitationReport.setCitationDate(date.getText());
				trafficCitationReport.setCitationTime(time.getText());
				trafficCitationReport.setCitationCharges(stringBuilder.toString());
				trafficCitationReport.setCitationComments(notes.getText());
				trafficCitationReport.setOffenderVehiclePlate((plateNumber.getText()));

				trafficCitationReport.setCitationCounty(toTitleCase(county.getText()));
				trafficCitationReport.setCitationArea(toTitleCase(area.getEditor().getText()));
				trafficCitationReport.setCitationStreet(toTitleCase(street.getEditor().getText()));
				trafficCitationReport.setOffenderName(toTitleCase(offenderName.getText()));
				trafficCitationReport.setOffenderGender(toTitleCase(offenderGender.getText()));
				trafficCitationReport.setOffenderAge(toTitleCase(offenderAge.getText()));
				trafficCitationReport.setOffenderHomeAddress(toTitleCase(offenderAddress.getText()));
				trafficCitationReport.setOffenderDescription(toTitleCase(offenderDescription.getText()));
				trafficCitationReport.setOffenderVehicleModel(toTitleCase(model.getText()));
				trafficCitationReport.setOffenderVehicleColor(toTitleCase(color.getValue().toString()));
				trafficCitationReport.setOffenderVehicleType(toTitleCase(type.getValue().toString()));
				trafficCitationReport.setOffenderVehicleOther(toTitleCase(otherInfo.getText()));
				trafficCitationReport.setOfficerName(toTitleCase(officername.getText()));
				trafficCitationReport.setOfficerNumber(toTitleCase(officernum.getText()));
				trafficCitationReport.setOfficerDivision(toTitleCase(officerdiv.getText()));
				trafficCitationReport.setOfficerAgency(toTitleCase(officeragen.getText()));
				try {
					TrafficCitationUtils.addTrafficCitationReport(trafficCitationReport);
				} catch (JAXBException e) {
					logError("Could not create TrafficCitationReport: ", e);
				}
				Optional<Ped> pedOptional = Ped.PedHistoryUtils.findPedByName(trafficCitationReport.getOffenderName());
				if (pedOptional.isPresent()) {
					logDebug("Ped is present in history, adding new citations.. ");
					Ped ped1 = pedOptional.get();
					String beforePriors = ped1.getCitationPriors();
					String afterPriors = (beforePriors + stringBuilder.toString().trim()).replaceAll("null", "");
					ped1.setCitationPriors(afterPriors);
					try {
						Ped.PedHistoryUtils.addPed(ped1);
					} catch (JAXBException e) {
						logError("Error updating ped priors from citationReport: ", e);
					}
				}

				if (!offenderName.getText().isEmpty() && offenderName.getText() != null
						&& !stringBuilder.toString().isEmpty() && stringBuilder.toString() != null) {
					String casenum = generateCaseNumber(num.getText());

					Optional<Case> caseToUpdateOptional = findCaseByNumber(casenum);
					if (!caseToUpdateOptional.isPresent()) {
						Case case1 = new Case();
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
						NotificationManager.showNotificationInfo("Report Manager",
								"A new Citation Report has been submitted. Case#: " + casenum + " Name: "
										+ offenderName.getText());
						logInfo("Added case from citation, Case#: " + casenum + " Name: " + offenderName.getText());
						needCourtRefresh.set(1);

						if (isConnected) {
							logDebug("Trying to send Citation_Update Signal to server...");

							String selectedCitationType = (String) citationType.getSelectionModel().getSelectedItem();
							if (selectedCitationType.equalsIgnoreCase(localization
									.getLocalizedMessage("ReportWindows.CitationTypeNonPrinted", "Non-Printed"))) {
								ClientUtils.sendMessageToServer("CITATION_UPDATE:name=" + offenderName.getText().trim()
										+ "|plate=" + plateNumber.getText().trim() + "|type=" + 1);
							} else if (selectedCitationType.equalsIgnoreCase(localization
									.getLocalizedMessage("ReportWindows.CitationTypePrinted", "Printed Citation"))) {
								ClientUtils.sendMessageToServer("CITATION_UPDATE:name=" + offenderName.getText().trim()
										+ "|plate=" + plateNumber.getText().trim() + "|type=" + 2);
							} else if (selectedCitationType.equalsIgnoreCase(localization
									.getLocalizedMessage("ReportWindows.CitationTypeParking", "Parking Citation"))) {
								ClientUtils.sendMessageToServer("CITATION_UPDATE:name=" + offenderName.getText().trim()
										+ "|plate=" + plateNumber.getText().trim() + "|type=" + 3);
							}
						} else {
							logWarn("Not connected to send Citation_Update Signal to server...");
						}
					} else {
						logWarn("Case #: " + casenum + " already exists, not adding new case");
					}
				} else {
					NotificationManager.showNotificationInfo("Report Manager",
							"A new Citation Report has been submitted.");
					NotificationManager.showNotificationWarning("Report Manager",
							"Could not create court case from citation because either name or offences field(s) were empty.");
					logError(
							"Could not create court case from citation because either name or offences field(s) were empty.");
				}
				/*
				 * TODO: !inprogress remove this code, was to update ped lookup from arrest
				 * if (pedLookupViewController != null) {
				 * if
				 * (Objects.requireNonNull(pedLookupViewController).getPedRecordPane().isVisible
				 * ()) {
				 * if (pedLookupViewController.getPedSearchField().getEditor().getText()
				 * .equalsIgnoreCase(offenderName.getText())) {
				 * try {
				 * pedLookupViewController.onPedSearchBtnClick(new ActionEvent());
				 * } catch (IOException e) {
				 * logError("Error searching name to update ped lookup from citationReport: "
				 * + pedLookupViewController.getPedfnamefield().getText().trim() + " "
				 * + pedLookupViewController.getPedlnamefield().getText().trim(), e);
				 * }
				 * }
				 * }
				 * }
				 */
				try {
					if (ConfigReader.configRead("soundSettings", "playCreateReport").equalsIgnoreCase("true")) {
						playSound(getJarPath() + "/sounds/alert-success.wav");
					}
				} catch (IOException e) {
					logError("Error getting configValue for playCreateReport: ", e);
				}
				citationLogUpdate();

				CustomWindow window = getWindow("Citation Report");
				if (window != null) {
					window.closeWindow();
				}
			}
		});

		Button offenderInfoBtn = (Button) citationReport.get(
				localization.getLocalizedMessage("ReportWindows.OffenderInfoSectionHeading", "Offender Information")
						+ "_button");

		Button vehInfoBtn = (Button) citationReport
				.get(localization.getLocalizedMessage("ReportWindows.CitationVehicleInfoHeading",
						"(If Applicable) Offender Vehicle Information") + "_button");

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

		return citationReport;
	}

	public static TrafficCitationReports loadTrafficCitationReports() throws JAXBException {
		File file = new File(trafficCitationLogURL);
		if (!file.exists()) {
			return new TrafficCitationReports();
		}

		try {
			JAXBContext context = JAXBContext.newInstance(TrafficCitationReports.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (TrafficCitationReports) unmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			logError("Error loading TrafficCitationReports: ", e);
			throw e;
		}
	}

	private static void saveTrafficCitationReports(TrafficCitationReports TrafficCitationReports) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(TrafficCitationReports.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		File file = new File(trafficCitationLogURL);
		marshaller.marshal(TrafficCitationReports, file);
	}

	public static void addTrafficCitationReport(TrafficCitationReport TrafficCitationReport) throws JAXBException {
		TrafficCitationReports TrafficCitationReports = loadTrafficCitationReports();

		if (TrafficCitationReports.getTrafficCitationReportList() == null) {
			TrafficCitationReports.setTrafficCitationReportList(new java.util.ArrayList<>());
		}

		Optional<TrafficCitationReport> existingReport = TrafficCitationReports.getTrafficCitationReportList().stream()
				.filter(e -> e.getCitationNumber().equals(TrafficCitationReport.getCitationNumber())).findFirst();

		if (existingReport.isPresent()) {
			TrafficCitationReports.getTrafficCitationReportList().remove(existingReport.get());
			TrafficCitationReports.getTrafficCitationReportList().add(TrafficCitationReport);
			logInfo("TrafficCitationReport with number " + TrafficCitationReport.getCitationNumber() + " updated.");
		} else {
			TrafficCitationReports.getTrafficCitationReportList().add(TrafficCitationReport);
			logInfo("TrafficCitationReport with number " + TrafficCitationReport.getCitationNumber() + " added.");
		}

		saveTrafficCitationReports(TrafficCitationReports);
	}

	public static void deleteTrafficCitationReport(String TrafficCitationReportnumber) throws JAXBException {
		TrafficCitationReports TrafficCitationReports = loadTrafficCitationReports();

		if (TrafficCitationReports.getTrafficCitationReportList() != null) {
			TrafficCitationReports.getTrafficCitationReportList()
					.removeIf(e -> e.getCitationNumber().equals(TrafficCitationReportnumber));
			saveTrafficCitationReports(TrafficCitationReports);
			logInfo("TrafficCitationReport with number " + TrafficCitationReportnumber + " deleted.");
		}
	}

}