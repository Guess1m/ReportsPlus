package com.drozal.dataterminal.logs.TrafficCitation;

import com.drozal.dataterminal.Windows.Main.actionController;
import com.drozal.dataterminal.Windows.Main.newOfficerController;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.logs.CitationsData;
import com.drozal.dataterminal.logs.Impound.ImpoundReportUtils;
import com.drozal.dataterminal.util.CourtData.Case;
import com.drozal.dataterminal.util.CourtData.CourtUtils;
import com.drozal.dataterminal.util.History.Ped;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Misc.NotificationManager;
import com.drozal.dataterminal.util.Report.nestedReportUtils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.animation.PauseTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Side;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.drozal.dataterminal.DataTerminalHomeApplication.*;
import static com.drozal.dataterminal.Windows.Main.actionController.getNextIndex;
import static com.drozal.dataterminal.Windows.Main.actionController.notesViewController;
import static com.drozal.dataterminal.util.CourtData.CourtUtils.*;
import static com.drozal.dataterminal.util.Misc.AudioUtil.playSound;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.controllerUtils.*;
import static com.drozal.dataterminal.util.Misc.stringUtil.getJarPath;
import static com.drozal.dataterminal.util.Misc.stringUtil.trafficCitationLogURL;
import static com.drozal.dataterminal.util.Report.reportUtil.*;
import static com.drozal.dataterminal.util.Report.treeViewUtils.findXMLValue;

public class TrafficCitationUtils {

    public static int countReports() {
        try {
            List<TrafficCitationReport> logs = TrafficCitationUtils.loadTrafficCitationReports().getTrafficCitationReportList();

            if (logs == null) {
                return 0;
            }

            return logs.size();
        } catch (Exception e) {
            logError("Exception", e);
            return -1;
        }
    }

    public static Map<String, Object> citationLayout() {
        Map<String, Object> citationReport = createReportWindow("Citation Report", 7, 9, new nestedReportUtils.TransferConfig("Transfer Information To New Report", new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("transferimpoundbtn", 12, nestedReportUtils.FieldType.TRANSFER_BUTTON))), new nestedReportUtils.SectionConfig("Officer Information", true, new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("name", 5, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("rank", 5, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("number", 2, nestedReportUtils.FieldType.TEXT_FIELD)), new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("division", 6, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("agency", 6, nestedReportUtils.FieldType.TEXT_FIELD))), new nestedReportUtils.SectionConfig("Location / Timestamp Information", true, new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("street", 4, nestedReportUtils.FieldType.COMBO_BOX_STREET), new nestedReportUtils.FieldConfig("area", 4, nestedReportUtils.FieldType.COMBO_BOX_AREA), new nestedReportUtils.FieldConfig("county", 4, nestedReportUtils.FieldType.TEXT_FIELD)), new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("date", 5, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("time", 5, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("citation number", 2, nestedReportUtils.FieldType.TEXT_FIELD))), new nestedReportUtils.SectionConfig("Offender Information", true, new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("offender name", 4, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("offender age", 4, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("offender gender", 4, nestedReportUtils.FieldType.TEXT_FIELD)), new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("offender address", 6, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("offender description", 6, nestedReportUtils.FieldType.TEXT_FIELD))), new nestedReportUtils.SectionConfig("(If Applicable) Offender Vehicle Information", false, new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("model", 4, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("plate number", 4, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("color", 4, nestedReportUtils.FieldType.COMBO_BOX_COLOR)), new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("type", 4, nestedReportUtils.FieldType.COMBO_BOX_TYPE), new nestedReportUtils.FieldConfig("other info", 8, nestedReportUtils.FieldType.TEXT_FIELD))), new nestedReportUtils.SectionConfig("Citation Notes", true, new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("notes", 12, nestedReportUtils.FieldType.TEXT_AREA))), new nestedReportUtils.SectionConfig("Citation(s)", true, new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("citationview", 6, nestedReportUtils.FieldType.CITATION_TREE_VIEW))));
        return citationReport;
    }

    public static Map<String, Object> newCitation(BarChart<String, Number> reportChart, AreaChart areaReportChart) {
        Map<String, Object> citationReport = citationLayout();

        Map<String, Object> citationReportMap = (Map<String, Object>) citationReport.get("Citation Report Map");

        TextField officername = (TextField) citationReportMap.get("name");
        TextField officerrank = (TextField) citationReportMap.get("rank");
        TextField officerdiv = (TextField) citationReportMap.get("division");
        TextField officeragen = (TextField) citationReportMap.get("agency");
        TextField officernum = (TextField) citationReportMap.get("number");

        TextField offenderName = (TextField) citationReportMap.get("offender name");
        TextField offenderAge = (TextField) citationReportMap.get("offender age");
        TextField offenderGender = (TextField) citationReportMap.get("offender gender");
        TextField offenderAddress = (TextField) citationReportMap.get("offender address");
        TextField offenderDescription = (TextField) citationReportMap.get("offender description");

        ComboBox area = (ComboBox) citationReportMap.get("area");
        ComboBox street = (ComboBox) citationReportMap.get("street");
        TextField county = (TextField) citationReportMap.get("county");
        TextField num = (TextField) citationReportMap.get("citation number");
        TextField date = (TextField) citationReportMap.get("date");
        TextField time = (TextField) citationReportMap.get("time");

        ComboBox color = (ComboBox) citationReportMap.get("color");
        ComboBox type = (ComboBox) citationReportMap.get("type");
        TextField plateNumber = (TextField) citationReportMap.get("plate number");
        TextField otherInfo = (TextField) citationReportMap.get("other info");
        TextField model = (TextField) citationReportMap.get("model");

        TextArea notes = (TextArea) citationReportMap.get("notes");

        TreeView citationtreeview = (TreeView) citationReportMap.get("citationview");
        TableView citationtable = (TableView) citationReportMap.get("CitationTableView");

        Button transferimpoundbtn = (Button) citationReportMap.get("transferimpoundbtn");
        transferimpoundbtn.setText("New Impound Report");

        BorderPane root = (BorderPane) citationReport.get("root");
        Stage stage = (Stage) root.getScene().getWindow();

        MenuButton pullnotesbtn = (MenuButton) citationReport.get("pullNotesBtn");
        pullnotesbtn.setPopupSide(Side.TOP);

        try {
            officername.setText(ConfigReader.configRead("userInfo", "Name"));
            officerrank.setText(ConfigReader.configRead("userInfo", "Rank"));
            officerdiv.setText(ConfigReader.configRead("userInfo", "Division"));
            officeragen.setText(ConfigReader.configRead("userInfo", "Agency"));
            officernum.setText(ConfigReader.configRead("userInfo", "Number"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        date.setText(getDate());
        time.setText(getTime());
        num.setText(generateReportNumber());

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
                            updateTextFromNotepad(model, noteArea, "-model");
                            updateTextFromNotepad(plateNumber, noteArea, "-plate");
                            updateTextFromNotepad(num, noteArea, "-number");
                        });
                    }
                }
            } else {
                log("NotesViewController Is Null", LogUtils.Severity.ERROR);
            }
        });

        transferimpoundbtn.setOnAction(event -> {

            Map<String, Object> impoundReportObj = ImpoundReportUtils.newImpound(reportChart, areaReportChart);

            Map<String, Object> impoundReportMap = (Map<String, Object>) impoundReportObj.get("Impound Report Map");

            TextField officernameimp = (TextField) impoundReportMap.get("name");
            TextField officerrankimp = (TextField) impoundReportMap.get("rank");
            TextField officerdivimp = (TextField) impoundReportMap.get("division");
            TextField officeragenimp = (TextField) impoundReportMap.get("agency");
            TextField officernumimp = (TextField) impoundReportMap.get("number");

            TextField offenderNameimp = (TextField) impoundReportMap.get("offender name");
            TextField offenderAgeimp = (TextField) impoundReportMap.get("offender age");
            TextField offenderGenderimp = (TextField) impoundReportMap.get("offender gender");
            TextField offenderAddressimp = (TextField) impoundReportMap.get("offender address");

            TextField numimp = (TextField) impoundReportMap.get("impound number");
            TextField dateimp = (TextField) impoundReportMap.get("date");
            TextField timeimp = (TextField) impoundReportMap.get("time");

            ComboBox colorimp = (ComboBox) impoundReportMap.get("color");
            ComboBox typeimp = (ComboBox) impoundReportMap.get("type");
            TextField plateNumberimp = (TextField) impoundReportMap.get("plate number");
            TextField modelimp = (TextField) impoundReportMap.get("model");

            TextArea notesimp = (TextArea) impoundReportMap.get("notes");

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

        submitBtn.setOnAction(event -> {
            if (num.getText().trim().isEmpty()) {
                warningLabel.setVisible(true);
                warningLabel.setText("Citation Number can't be empty!");
                warningLabel.setStyle("-fx-font-family: \"Segoe UI Black\"; -fx-text-fill: red;");
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
                        log("Using Custom fine", LogUtils.Severity.DEBUG);
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
                    log("Ped is present in history, adding new citations.. ", LogUtils.Severity.DEBUG);
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

                if (!offenderName.getText().isEmpty() && offenderName.getText() != null && !stringBuilder.toString().isEmpty() && stringBuilder.toString() != null) {
                    Case case1 = new Case();
                    String casenum = generateCaseNumber();
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
                    } catch (JAXBException | IOException e) {
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
                    NotificationManager.showNotificationInfo("Report Manager", "A new Citation Report has been submitted. Case#: " + casenum + " Name: " + offenderName.getText(), mainRT);
                    log("Added case from citation, Case#: " + casenum + " Name: " + offenderName.getText(), LogUtils.Severity.INFO);
                    actionController.needCourtRefresh.set(1);
                } else {
                    NotificationManager.showNotificationInfo("Report Manager", "A new Citation Report has been submitted.", mainRT);
                    NotificationManager.showNotificationWarning("Report Manager", "Could not create court case from citation because either name or offences field(s) were empty.", mainRT);
                    log("Could not create court case from citation because either name or offences field(s) were empty.", LogUtils.Severity.ERROR);
                }

                actionController controllerVar = null;
                if (controller != null) {
                    controllerVar = controller;
                } else if (newOfficerController.controller != null) {
                    controllerVar = newOfficerController.controller;
                } else {
                    log("Settings Controller Var could not be set", LogUtils.Severity.ERROR);
                }
                if (Objects.requireNonNull(controllerVar).getPedRecordPane().isVisible()) {
                    if (controllerVar.getPedSearchField().getEditor().getText().equalsIgnoreCase(offenderName.getText())) {
                        try {
                            controllerVar.onPedSearchBtnClick(new ActionEvent());
                        } catch (IOException e) {
                            logError("Error searching name to update ped lookup from citationReport: " + controllerVar.getPedfnamefield().getText().trim() + " " + controllerVar.getPedlnamefield().getText().trim(), e);
                        }
                    }
                }
                playSound(getJarPath() + "/sounds/alert-success.wav");
                actionController.needRefresh.set(1);
                updateChartIfMismatch(reportChart);
                refreshChart(areaReportChart, "area");
                stage.close();
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

        Optional<TrafficCitationReport> existingReport = TrafficCitationReports.getTrafficCitationReportList().stream().filter(e -> e.getCitationNumber().equals(TrafficCitationReport.getCitationNumber())).findFirst();

        if (existingReport.isPresent()) {
            TrafficCitationReports.getTrafficCitationReportList().remove(existingReport.get());
            TrafficCitationReports.getTrafficCitationReportList().add(TrafficCitationReport);
            log("TrafficCitationReport with number " + TrafficCitationReport.getCitationNumber() + " updated.", LogUtils.Severity.INFO);
        } else {
            TrafficCitationReports.getTrafficCitationReportList().add(TrafficCitationReport);
            log("TrafficCitationReport with number " + TrafficCitationReport.getCitationNumber() + " added.", LogUtils.Severity.INFO);
        }

        saveTrafficCitationReports(TrafficCitationReports);
    }

    public static void deleteTrafficCitationReport(String TrafficCitationReportnumber) throws JAXBException {
        TrafficCitationReports TrafficCitationReports = loadTrafficCitationReports();

        if (TrafficCitationReports.getTrafficCitationReportList() != null) {
            TrafficCitationReports.getTrafficCitationReportList().removeIf(e -> e.getCitationNumber().equals(TrafficCitationReportnumber));
            saveTrafficCitationReports(TrafficCitationReports);
            log("TrafficCitationReport with number " + TrafficCitationReportnumber + " deleted.", LogUtils.Severity.INFO);
        }
    }

}