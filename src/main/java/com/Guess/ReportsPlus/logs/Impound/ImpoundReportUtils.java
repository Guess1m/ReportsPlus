package com.Guess.ReportsPlus.logs.Impound;

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
import static com.Guess.ReportsPlus.Windows.Apps.LogViewController.impoundLogUpdate;
import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.notesViewController;
import static com.Guess.ReportsPlus.util.Misc.AudioUtil.playSound;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.*;
import static com.Guess.ReportsPlus.util.Report.nestedReportUtils.*;
import static com.Guess.ReportsPlus.util.Report.reportUtil.createReportWindow;
import static com.Guess.ReportsPlus.util.Report.reportUtil.pullValueFromReport;
import static com.Guess.ReportsPlus.util.Strings.URLStrings.impoundLogURL;

public class ImpoundReportUtils {

    public static Map<String, Object> impoundLayout() {
        SectionConfig vehInfoSection = new SectionConfig(
                localization.getLocalizedMessage("ReportWindows.OffenderVehicleInfoSectionHeading",
                        "Offender Vehicle Information"),
                new RowConfig(new FieldConfig(
                        localization.getLocalizedMessage("ReportWindows.FieldModel", "model"), 6,
                        FieldType.TEXT_FIELD), new FieldConfig(
                        localization.getLocalizedMessage("ReportWindows.FieldPlateNumber", "plate number"), 6,
                        FieldType.TEXT_FIELD)), new RowConfig(
                new FieldConfig(
                        localization.getLocalizedMessage("ReportWindows.FieldType", "type"), 7,
                        FieldType.COMBO_BOX_TYPE), new FieldConfig(
                localization.getLocalizedMessage("ReportWindows.FieldColor", "color"), 5,
                FieldType.COMBO_BOX_COLOR)));
        vehInfoSection.setHasButton(true);

        SectionConfig offenderInfoSection = new SectionConfig(
                localization.getLocalizedMessage("ReportWindows.OffenderInfoSectionHeading",
                        "Offender Information"), new RowConfig(
                new FieldConfig(
                        localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"), 4,
                        FieldType.TEXT_FIELD), new FieldConfig(
                localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"), 4,
                FieldType.TEXT_FIELD), new FieldConfig(
                localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"), 4,
                FieldType.TEXT_FIELD)), new RowConfig(
                new FieldConfig(
                        localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress",
                                "offender address"), 12,
                        FieldType.TEXT_FIELD)));
        offenderInfoSection.setHasButton(true);

        Map<String, Object> impoundReport = createReportWindow(
                localization.getLocalizedMessage("ReportWindows.ImpoundReportTitle", "Impound Report"), null,
                new SectionConfig(
                        localization.getLocalizedMessage("ReportWindows.OfficerInfoSectionHeading",
                                "Officer Information"), new RowConfig(
                        new FieldConfig(
                                localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"), 5,
                                FieldType.OFFICER_NAME), new FieldConfig(
                        localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"), 5,
                        FieldType.OFFICER_RANK), new FieldConfig(
                        localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"), 2,
                        FieldType.OFFICER_NUMBER)), new RowConfig(
                        new FieldConfig(
                                localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"), 6,
                                FieldType.OFFICER_DIVISION), new FieldConfig(
                        localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"), 6,
                        FieldType.OFFICER_AGENCY))), new SectionConfig(
                        localization.getLocalizedMessage("ReportWindows.LocationInfoSectionHeading",
                                "Location / Timestamp Information"),
                        new RowConfig(new FieldConfig(
                                localization.getLocalizedMessage("ReportWindows.FieldDate", "date"), 5,
                                FieldType.DATE_FIELD), new FieldConfig(
                                localization.getLocalizedMessage("ReportWindows.FieldTime", "time"), 5,
                                FieldType.TIME_FIELD), new FieldConfig(
                                localization.getLocalizedMessage("ReportWindows.ImpoundNumField", "impound number"), 2,
                                FieldType.NUMBER_FIELD))), offenderInfoSection, vehInfoSection, new SectionConfig(
                        localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"),
                        new RowConfig(new FieldConfig(
                                localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"), 12,
                                FieldType.TEXT_AREA))));
        return impoundReport;
    }

    public static Map<String, Object> newImpound() {
        Map<String, Object> impoundReport = impoundLayout();

        Map<String, Object> impoundReportMap = (Map<String, Object>) impoundReport.get(localization.getLocalizedMessage("ReportWindows.ImpoundReportTitle", "Impound Report") + " Map");

        TextField officername = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
        TextField officerrank = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
        TextField officerdiv = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
        TextField officeragen = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
        TextField officernum = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));

        TextField offenderName = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
        TextField offenderAge = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"));
        TextField offenderGender = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"));
        TextField offenderAddress = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"));

        TextField num = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.ImpoundNumField", "impound number"));
        TextField date = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
        TextField time = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));

        ComboBox color = (ComboBox) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldColor", "color"));
        ComboBox type = (ComboBox) impoundReportMap.get(
                localization.getLocalizedMessage("ReportWindows.FieldType", "type"));
        TextField plateNumber = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldPlateNumber", "plate number"));
        TextField model = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldModel", "model"));

        TextArea notes = (TextArea) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", localization.getLocalizedMessage("ReportWindows.FieldNotes", "notes")));

        MenuButton pullnotesbtn = (MenuButton) impoundReport.get("pullNotesBtn");
        pullnotesbtn.setPopupSide(Side.TOP);

        Label warningLabel = (Label) impoundReport.get("warningLabel");

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
                            updateTextFromNotepad(offenderName, noteArea, "-name");
                            updateTextFromNotepad(offenderAge, noteArea, "-age");
                            updateTextFromNotepad(offenderGender, noteArea, "-gender");
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
                log("NotesViewController Is Null", LogUtils.Severity.ERROR);
            }
        });

        Button submitBtn = (Button) impoundReport.get("submitBtn");
        ComboBox<String> statusValue = (ComboBox) impoundReport.get("statusValue");

        submitBtn.setOnAction(event -> {
            if (num.getText().trim().isEmpty()) {
                warningLabel.setVisible(true);
                warningLabel.setText("Impound Number can't be empty!");
                warningLabel.setStyle("-fx-font-family: \"Inter 28pt Bold\"; -fx-text-fill: red;");
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(e -> warningLabel.setVisible(false));
                pause.play();
            } else {
                for (String fieldName : impoundReportMap.keySet()) {
                    Object field = impoundReportMap.get(fieldName);
                    if (field instanceof ComboBox<?> comboBox) {
                        if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
                            comboBox.getSelectionModel().selectFirst();
                        }
                    }
                }

                ImpoundReport impoundReport1 = new ImpoundReport();
                impoundReport1.setStatus(statusValue.getValue());
                impoundReport1.setImpoundNumber(num.getText());
                impoundReport1.setImpoundDate(date.getText());
                impoundReport1.setImpoundTime(time.getText());
                impoundReport1.setOfficerRank(officerrank.getText());
                impoundReport1.setImpoundComments(notes.getText());
                impoundReport1.setImpoundPlateNumber(plateNumber.getText());

                impoundReport1.setOwnerName(toTitleCase(offenderName.getText()));
                impoundReport1.setOwnerAge(toTitleCase(offenderAge.getText()));
                impoundReport1.setOwnerGender(toTitleCase(offenderGender.getText()));
                impoundReport1.setOwnerAddress(toTitleCase(offenderAddress.getText()));
                impoundReport1.setImpoundModel(toTitleCase(model.getText()));
                impoundReport1.setImpoundType(toTitleCase(type.getValue().toString()));
                impoundReport1.setImpoundColor(toTitleCase(color.getValue().toString()));
                impoundReport1.setOfficerName(toTitleCase(officername.getText()));
                impoundReport1.setOfficerNumber(toTitleCase(officernum.getText()));
                impoundReport1.setOfficerDivision(toTitleCase(officerdiv.getText()));
                impoundReport1.setOfficerAgency(toTitleCase(officeragen.getText()));
                try {
                    ImpoundReportUtils.addImpoundReport(impoundReport1);
                } catch (JAXBException e) {
                    logError("Could not create new ImpoundReport: ", e);
                }
                try {
                    if (ConfigReader.configRead("soundSettings", "playCreateReport").equalsIgnoreCase("true")) {
                        playSound(getJarPath() + "/sounds/alert-success.wav");
                    }
                } catch (IOException e) {
                    logError("Error getting configValue for playCreateReport: ", e);
                }

                impoundLogUpdate();

                NotificationManager.showNotificationInfo("Report Manager", "A new Impound Report has been submitted.");
                CustomWindow window = getWindow("Impound Report");
                if (window != null) {
                    window.closeWindow();
                }
            }
        });

        Button offenderInfoBtn = (Button) impoundReport.get(localization.getLocalizedMessage("ReportWindows.OffenderInfoSectionHeading",
                "Offender Information") + "_button");

        Button vehInfoBtn = (Button) impoundReport.get(localization.getLocalizedMessage("ReportWindows.OffenderVehicleInfoSectionHeading",
                "Offender Vehicle Information") + "_button");

        offenderInfoBtn.setOnAction(event -> {
            String fulln = pullValueFromReport("ped", "Pedfnamefield") + " " + pullValueFromReport("ped", "Pedlnamefield");
            if (!fulln.trim().isEmpty()) {
                offenderName.setText(fulln);
            }
            offenderAge.setText(pullValueFromReport("ped", "Peddobfield"));
            offenderGender.setText(pullValueFromReport("ped", "Pedgenfield"));
            offenderAddress.setText(pullValueFromReport("ped", "Pedaddressfield"));
        });

        vehInfoBtn.setOnAction(event -> {
            plateNumber.setText(pullValueFromReport("veh", "Vehplatefield2"));
            model.setText(pullValueFromReport("veh", "Vehmodelfield"));
            String typ = pullValueFromReport("veh", "Vehtypecombobox");
            if (!typ.trim().isEmpty()) {
                type.setValue(typ);
            }
        });

        return impoundReport;
    }

    public static ImpoundReports loadImpoundReports() throws JAXBException {
        File file = new File(impoundLogURL);
        if (!file.exists()) {
            return new ImpoundReports();
        }

        try {
            JAXBContext context = JAXBContext.newInstance(ImpoundReports.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (ImpoundReports) unmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            logError("Error loading ImpoundReports: ", e);
            throw e;
        }
    }

    private static void saveImpoundReports(ImpoundReports ImpoundReports) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ImpoundReports.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        File file = new File(impoundLogURL);
        marshaller.marshal(ImpoundReports, file);
    }

    public static void addImpoundReport(ImpoundReport ImpoundReport) throws JAXBException {
        ImpoundReports ImpoundReports = loadImpoundReports();

        if (ImpoundReports.getImpoundReportList() == null) {
            ImpoundReports.setImpoundReportList(new java.util.ArrayList<>());
        }

        Optional<ImpoundReport> existingReport = ImpoundReports.getImpoundReportList().stream().filter(e -> e.getImpoundNumber().equals(ImpoundReport.getImpoundNumber())).findFirst();

        if (existingReport.isPresent()) {
            ImpoundReports.getImpoundReportList().remove(existingReport.get());
            ImpoundReports.getImpoundReportList().add(ImpoundReport);
            log("ImpoundReport with number " + ImpoundReport.getImpoundNumber() + " updated.", LogUtils.Severity.INFO);
        } else {
            ImpoundReports.getImpoundReportList().add(ImpoundReport);
            log("ImpoundReport with number " + ImpoundReport.getImpoundNumber() + " added.", LogUtils.Severity.INFO);
        }

        saveImpoundReports(ImpoundReports);
    }

    public static void deleteImpoundReport(String ImpoundReportnumber) throws JAXBException {
        ImpoundReports ImpoundReports = loadImpoundReports();

        if (ImpoundReports.getImpoundReportList() != null) {
            ImpoundReports.getImpoundReportList().removeIf(e -> e.getImpoundNumber().equals(ImpoundReportnumber));
            saveImpoundReports(ImpoundReports);
            log("ImpoundReport with number " + ImpoundReportnumber + " deleted.", LogUtils.Severity.INFO);
        }
    }
}