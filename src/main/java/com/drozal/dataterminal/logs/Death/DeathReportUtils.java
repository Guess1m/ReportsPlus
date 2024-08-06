package com.drozal.dataterminal.logs.Death;

import com.drozal.dataterminal.actionController;
import com.drozal.dataterminal.config.ConfigReader;
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
import static com.drozal.dataterminal.util.Misc.stringUtil.DeathReportLogURL;
import static com.drozal.dataterminal.util.Report.reportUtil.createReportWindow;
import static com.drozal.dataterminal.util.Report.reportUtil.generateReportNumber;

public class DeathReportUtils {

    public static int countReports() {
        try {
            List<DeathReport> logs = DeathReportUtils.loadDeathReports().getDeathReportList();

            if (logs == null) {
                return 0;
            }

            return logs.size();
        } catch (Exception e) {
            logError("Exception", e);
            return -1;
        }
    }

    public static Map<String, Object> deathReportLayout() {
        Map<String, Object> deathReport = createReportWindow("Death Report", 7, 8, null, new nestedReportUtils.SectionConfig("Officer Information", true, new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("name", 5, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("rank", 5, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("number", 2, nestedReportUtils.FieldType.TEXT_FIELD)), new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("division", 6, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("agency", 6, nestedReportUtils.FieldType.TEXT_FIELD))), new nestedReportUtils.SectionConfig("Location / Timestamp Information", true, new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("street", 4, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("area", 4, nestedReportUtils.FieldType.COMBO_BOX_AREA), new nestedReportUtils.FieldConfig("county", 4, nestedReportUtils.FieldType.TEXT_FIELD)), new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("date", 5, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("time", 5, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("death num", 2, nestedReportUtils.FieldType.TEXT_FIELD))), new nestedReportUtils.SectionConfig("Deceased Information", true, new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("decedent name", 4, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("age/dob", 4, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("gender", 4, nestedReportUtils.FieldType.TEXT_FIELD)), new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("address", 6, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("description", 6, nestedReportUtils.FieldType.TEXT_FIELD))), new nestedReportUtils.SectionConfig("Death Information", true, new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("time of death", 6, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("date of death", 6, nestedReportUtils.FieldType.TEXT_FIELD)), new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("cause of death", 12, nestedReportUtils.FieldType.TEXT_FIELD)), new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("mode of death", 6, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("witnesses", 6, nestedReportUtils.FieldType.TEXT_FIELD))), new nestedReportUtils.SectionConfig("Death Report Notes", true, new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("notes", 12, nestedReportUtils.FieldType.TEXT_AREA))));
        return deathReport;
    }

    public static Map<String, Object> newDeathReport(BarChart<String, Number> reportChart, AreaChart areaReportChart) {
        Map<String, Object> deathReport = deathReportLayout();

        Map<String, Object> deathReportMap = (Map<String, Object>) deathReport.get("Death Report Map");

        TextField name = (TextField) deathReportMap.get("name");
        TextField rank = (TextField) deathReportMap.get("rank");
        TextField div = (TextField) deathReportMap.get("division");
        TextField agen = (TextField) deathReportMap.get("agency");
        TextField num = (TextField) deathReportMap.get("number");

        TextField date = (TextField) deathReportMap.get("date");
        TextField time = (TextField) deathReportMap.get("time");
        TextField street = (TextField) deathReportMap.get("street");
        ComboBox area = (ComboBox) deathReportMap.get("area");
        TextField county = (TextField) deathReportMap.get("county");
        TextField deathNum = (TextField) deathReportMap.get("death num");
        deathNum.setText(generateReportNumber());

        TextField decedent = (TextField) deathReportMap.get("decedent name");
        TextField age = (TextField) deathReportMap.get("age/dob");
        TextField gender = (TextField) deathReportMap.get("gender");
        TextField address = (TextField) deathReportMap.get("address");
        TextField description = (TextField) deathReportMap.get("description");

        TextField causeofdeath = (TextField) deathReportMap.get("cause of death");
        TextField modeofdeath = (TextField) deathReportMap.get("mode of death");
        TextField witnesses = (TextField) deathReportMap.get("witnesses");

        TextField timeofdeath = (TextField) deathReportMap.get("time of death");
        TextField dateofdeath = (TextField) deathReportMap.get("date of death");

        TextArea notes = (TextArea) deathReportMap.get("notes");

        BorderPane root = (BorderPane) deathReport.get("root");
        Stage stage = (Stage) root.getScene().getWindow();

        Label warningLabel = (Label) deathReport.get("warningLabel");


        try {
            name.setText(ConfigReader.configRead("userInfo", "Name"));
            rank.setText(ConfigReader.configRead("userInfo", "Rank"));
            div.setText(ConfigReader.configRead("userInfo", "Division"));
            agen.setText(ConfigReader.configRead("userInfo", "Agency"));
            num.setText(ConfigReader.configRead("userInfo", "Number"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        time.setText(getTime());
        date.setText(getDate());
        timeofdeath.setText(getTime());
        dateofdeath.setText(getDate());

        Button pullNotesBtn = (Button) deathReport.get("pullNotesBtn");

        pullNotesBtn.setOnAction(event -> {
            if (notesViewController != null) {
                updateTextFromNotepad(notes, notesViewController.getNotepadTextArea(), "-comments");
                updateTextFromNotepad(decedent, notesViewController.getNotepadTextArea(), "-name");
                updateTextFromNotepad(age, notesViewController.getNotepadTextArea(), "-age");
                updateTextFromNotepad(address, notesViewController.getNotepadTextArea(), "-address");
                updateTextFromNotepad(description, notesViewController.getNotepadTextArea(), "-description");
                updateTextFromNotepad(deathNum, notesViewController.getNotepadTextArea(), "-number");
                updateTextFromNotepad(county, notesViewController.getNotepadTextArea(), "-county");
                updateTextFromNotepad(area.getEditor(), notesViewController.getNotepadTextArea(), "-area");
                updateTextFromNotepad(street, notesViewController.getNotepadTextArea(), "-street");
                updateTextFromNotepad(gender, notesViewController.getNotepadTextArea(), "-gender");
            } else {
                log("NotesViewController Is Null", LogUtils.Severity.ERROR);
            }
        });

        Button submitBtn = (Button) deathReport.get("submitBtn");

        submitBtn.setOnAction(event -> {
            if (deathNum.getText().trim().isEmpty()) {
                warningLabel.setVisible(true);
                warningLabel.setText("Death Number can't be empty!");
                warningLabel.setStyle("-fx-font-family: \"Segoe UI Black\"; -fx-text-fill: red;");
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
                deathReport1.setStreet(toTitleCase(street.getText()));
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

                actionController.needRefresh.set(1);
                updateChartIfMismatch(reportChart);
                refreshChart(areaReportChart, "area");
                NotificationManager.showNotificationInfo("Report Manager", "A new Death Report has been submitted.", mainRT);

                stage.close();
            }
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
            log("DeathReport with number " + DeathReport.getDeathReportNumber() + " updated.", LogUtils.Severity.INFO);
        } else {
            DeathReports.getDeathReportList().add(DeathReport);
            log("DeathReport with number " + DeathReport.getDeathReportNumber() + " added.", LogUtils.Severity.INFO);
        }

        saveDeathReports(DeathReports);
    }

    public static void deleteDeathReport(String DeathReportnumber) throws JAXBException {
        DeathReports DeathReports = loadDeathReports();

        if (DeathReports.getDeathReportList() != null) {
            DeathReports.getDeathReportList().removeIf(e -> e.getDeathReportNumber().equals(DeathReportnumber));
            saveDeathReports(DeathReports);
            log("DeathReport with number " + DeathReportnumber + " deleted.", LogUtils.Severity.INFO);
        }
    }

}