package com.drozal.dataterminal.logs.TrafficStop;

import com.drozal.dataterminal.actionController;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.logs.Arrest.ArrestReportUtils;
import com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationUtils;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Misc.NotificationManager;
import com.drozal.dataterminal.util.Report.nestedReportUtils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.animation.PauseTransition;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.drozal.dataterminal.DataTerminalHomeApplication.*;
import static com.drozal.dataterminal.actionController.notesViewController;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.controllerUtils.*;
import static com.drozal.dataterminal.util.Misc.stringUtil.trafficstopLogURL;
import static com.drozal.dataterminal.util.Report.reportUtil.createReportWindow;
import static com.drozal.dataterminal.util.Report.reportUtil.generateReportNumber;

public class TrafficStopReportUtils {

    public static int countReports() {
        try {
            List<TrafficStopReport> logs = TrafficStopReportUtils.loadTrafficStopReports().getTrafficStopReportList();

            if (logs == null) {
                return 0;
            }

            return logs.size();
        } catch (Exception e) {
            logError("Exception", e);
            return -1;
        }
    }

    public static Map<String, Object> trafficStopLayout() {
        Map<String, Object> trafficStopReport = createReportWindow("Traffic Stop Report", 6, 8, new nestedReportUtils.TransferConfig("Transfer Information To New Report", new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("transferarrestbtn", 6, nestedReportUtils.FieldType.TRANSFER_BUTTON), new nestedReportUtils.FieldConfig("transfercitationbtn", 6, nestedReportUtils.FieldType.TRANSFER_BUTTON))), new nestedReportUtils.SectionConfig("Officer Information", true, new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("name", 5, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("rank", 5, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("number", 2, nestedReportUtils.FieldType.TEXT_FIELD)), new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("division", 6, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("agency", 6, nestedReportUtils.FieldType.TEXT_FIELD))), new nestedReportUtils.SectionConfig("Location / Timestamp Information", true, new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("street", 4, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("area", 4, nestedReportUtils.FieldType.COMBO_BOX_AREA), new nestedReportUtils.FieldConfig("county", 4, nestedReportUtils.FieldType.TEXT_FIELD)), new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("date", 5, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("time", 5, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("stop number", 2, nestedReportUtils.FieldType.TEXT_FIELD))), new nestedReportUtils.SectionConfig("Offender Information", true, new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("offender name", 4, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("offender age", 4, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("offender gender", 4, nestedReportUtils.FieldType.TEXT_FIELD)), new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("offender address", 6, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("offender description", 6, nestedReportUtils.FieldType.TEXT_FIELD))), new nestedReportUtils.SectionConfig("Offender Vehicle Information", true, new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("model", 4, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("plate number", 4, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("color", 4, nestedReportUtils.FieldType.COMBO_BOX_COLOR)), new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("type", 4, nestedReportUtils.FieldType.COMBO_BOX_TYPE), new nestedReportUtils.FieldConfig("other info", 8, nestedReportUtils.FieldType.TEXT_FIELD))), new nestedReportUtils.SectionConfig("Comments", true, new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("notes", 12, nestedReportUtils.FieldType.TEXT_AREA))));
        return trafficStopReport;
    }

    public static Map<String, Object> newTrafficStop(BarChart<String, Number> reportChart, AreaChart areaReportChart) {
        Map<String, Object> trafficStopReport = trafficStopLayout();

        Map<String, Object> trafficStopReportMap = (Map<String, Object>) trafficStopReport.get("Traffic Stop Report Map");

        TextField officernamets = (TextField) trafficStopReportMap.get("name");
        TextField officerrankts = (TextField) trafficStopReportMap.get("rank");
        TextField officerdivts = (TextField) trafficStopReportMap.get("division");
        TextField officeragents = (TextField) trafficStopReportMap.get("agency");
        TextField officernumarrestts = (TextField) trafficStopReportMap.get("number");

        TextField offenderNamets = (TextField) trafficStopReportMap.get("offender name");
        TextField offenderAgets = (TextField) trafficStopReportMap.get("offender age");
        TextField offenderGenderts = (TextField) trafficStopReportMap.get("offender gender");
        TextField offenderAddressts = (TextField) trafficStopReportMap.get("offender address");
        TextField offenderDescriptionts = (TextField) trafficStopReportMap.get("offender description");

        ComboBox colorts = (ComboBox) trafficStopReportMap.get("color");
        ComboBox typets = (ComboBox) trafficStopReportMap.get("type");
        TextField plateNumberts = (TextField) trafficStopReportMap.get("plate number");
        TextField otherInfots = (TextField) trafficStopReportMap.get("other info");
        TextField modelts = (TextField) trafficStopReportMap.get("model");

        ComboBox areats = (ComboBox) trafficStopReportMap.get("area");
        TextField streetts = (TextField) trafficStopReportMap.get("street");
        TextField countyts = (TextField) trafficStopReportMap.get("county");
        TextField stopnumts = (TextField) trafficStopReportMap.get("stop number");
        TextField datets = (TextField) trafficStopReportMap.get("date");
        TextField timets = (TextField) trafficStopReportMap.get("time");

        TextArea notests = (TextArea) trafficStopReportMap.get("notes");

        Button transferarrestbtnts = (Button) trafficStopReportMap.get("transferarrestbtn");
        transferarrestbtnts.setText("New Arrest Report");
        Button transfercitationbtnts = (Button) trafficStopReportMap.get("transfercitationbtn");
        transfercitationbtnts.setText("New Citation Report");

        BorderPane rootts = (BorderPane) trafficStopReport.get("root");
        Stage stagets = (Stage) rootts.getScene().getWindow();

        Label warningLabelts = (Label) trafficStopReport.get("warningLabel");
        Button pullNotesBtnts = (Button) trafficStopReport.get("pullNotesBtn");

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

        pullNotesBtnts.setOnAction(event -> {
            if (notesViewController != null) {
                updateTextFromNotepad(areats.getEditor(), notesViewController.getNotepadTextArea(), "-area");
                updateTextFromNotepad(countyts, notesViewController.getNotepadTextArea(), "-county");
                updateTextFromNotepad(streetts, notesViewController.getNotepadTextArea(), "-street");
                updateTextFromNotepad(offenderNamets, notesViewController.getNotepadTextArea(), "-name");
                updateTextFromNotepad(offenderAgets, notesViewController.getNotepadTextArea(), "-age");
                updateTextFromNotepad(offenderGenderts, notesViewController.getNotepadTextArea(), "-gender");
                updateTextFromNotepad(offenderDescriptionts, notesViewController.getNotepadTextArea(), "-description");
                updateTextFromNotepad(notests, notesViewController.getNotepadTextArea(), "-comments");
                updateTextFromNotepad(offenderAddressts, notesViewController.getNotepadTextArea(), "-address");
                updateTextFromNotepad(modelts, notesViewController.getNotepadTextArea(), "-model");
                updateTextFromNotepad(plateNumberts, notesViewController.getNotepadTextArea(), "-plate");
                updateTextFromNotepad(stopnumts, notesViewController.getNotepadTextArea(), "-number");
            } else {
                log("NotesViewController Is Null", LogUtils.Severity.ERROR);
            }
        });

        transferarrestbtnts.setOnAction(event -> {
            Map<String, Object> arrestReportObj = ArrestReportUtils.newArrest(reportChart, areaReportChart);

            Map<String, Object> arrestReportMap = (Map<String, Object>) arrestReportObj.get("Arrest Report Map");

            TextField officernamearr = (TextField) arrestReportMap.get("name");
            TextField officerrankarr = (TextField) arrestReportMap.get("rank");
            TextField officerdivarr = (TextField) arrestReportMap.get("division");
            TextField officeragenarr = (TextField) arrestReportMap.get("agency");
            TextField officernumarrestarr = (TextField) arrestReportMap.get("number");

            TextField offenderNamearr = (TextField) arrestReportMap.get("offender name");
            TextField offenderAgearr = (TextField) arrestReportMap.get("offender age");
            TextField offenderGenderarr = (TextField) arrestReportMap.get("offender gender");
            TextField offenderAddressarr = (TextField) arrestReportMap.get("offender address");
            TextField offenderDescriptionarr = (TextField) arrestReportMap.get("offender description");

            ComboBox areaarr = (ComboBox) arrestReportMap.get("area");
            TextField streetarr = (TextField) arrestReportMap.get("street");
            TextField countyarr = (TextField) arrestReportMap.get("county");
            TextField arrestnumarr = (TextField) arrestReportMap.get("arrest number");
            TextField datearr = (TextField) arrestReportMap.get("date");
            TextField timearr = (TextField) arrestReportMap.get("time");

            TextArea notesarr = (TextArea) arrestReportMap.get("notes");

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
            streetarr.setText(streetts.getText());
            arrestnumarr.setText(stopnumts.getText());
            notesarr.setText(notests.getText());
        });

        transfercitationbtnts.setOnAction(event -> {
            Map<String, Object> trafficCitationObj = TrafficCitationUtils.newCitation(reportChart, areaReportChart);

            Map<String, Object> citationReportMap = (Map<String, Object>) trafficCitationObj.get("Citation Report Map");

            TextField officernamecit = (TextField) citationReportMap.get("name");
            TextField officerrankcit = (TextField) citationReportMap.get("rank");
            TextField officerdivcit = (TextField) citationReportMap.get("division");
            TextField officeragencit = (TextField) citationReportMap.get("agency");
            TextField officernumcit = (TextField) citationReportMap.get("number");

            TextField offenderNamecit = (TextField) citationReportMap.get("offender name");
            TextField offenderAgecit = (TextField) citationReportMap.get("offender age");
            TextField offenderGendercit = (TextField) citationReportMap.get("offender gender");
            TextField offenderAddresscit = (TextField) citationReportMap.get("offender address");
            TextField offenderDescriptioncit = (TextField) citationReportMap.get("offender description");

            ComboBox areacit = (ComboBox) citationReportMap.get("area");
            TextField streetcit = (TextField) citationReportMap.get("street");
            TextField countycit = (TextField) citationReportMap.get("county");
            TextField numcit = (TextField) citationReportMap.get("citation number");
            TextField datecit = (TextField) citationReportMap.get("date");
            TextField timecit = (TextField) citationReportMap.get("time");

            ComboBox colorcit = (ComboBox) citationReportMap.get("color");
            ComboBox typecit = (ComboBox) citationReportMap.get("type");
            TextField plateNumbercit = (TextField) citationReportMap.get("plate number");
            TextField otherInfocit = (TextField) citationReportMap.get("other info");
            TextField modelcit = (TextField) citationReportMap.get("model");

            TextArea notescit = (TextArea) citationReportMap.get("notes");

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
            streetcit.setText(streetts.getText());
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
                trafficStopReport1.setStreet(toTitleCase(streetts.getText()));
                trafficStopReport1.setCounty(toTitleCase(countyts.getText()));
                trafficStopReport1.setArea(toTitleCase(areats.getEditor().getText()));
                trafficStopReport1.setColor(toTitleCase(colorts.getValue().toString()));
                trafficStopReport1.setType(toTitleCase(typets.getValue().toString()));
                try {
                    TrafficStopReportUtils.addTrafficStopReport(trafficStopReport1);
                } catch (JAXBException e) {
                    logError("Could not create new TrafficStopReport: ", e);
                }

                actionController.needRefresh.set(1);
                updateChartIfMismatch(reportChart);
                refreshChart(areaReportChart, "area");
                NotificationManager.showNotificationInfo("Report Manager", "A new Traffic Stop Report has been submitted.", mainRT);
                stagets.close();
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