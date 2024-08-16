package com.drozal.dataterminal.logs.Arrest;

import com.drozal.dataterminal.Windows.Main.actionController;
import com.drozal.dataterminal.Windows.Main.newOfficerController;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.logs.ChargesData;
import com.drozal.dataterminal.logs.Impound.ImpoundReportUtils;
import com.drozal.dataterminal.logs.Incident.IncidentReportUtils;
import com.drozal.dataterminal.logs.Search.SearchReportUtils;
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
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.controllerUtils.*;
import static com.drozal.dataterminal.util.Misc.stringUtil.arrestLogURL;
import static com.drozal.dataterminal.util.Report.reportUtil.createReportWindow;
import static com.drozal.dataterminal.util.Report.reportUtil.generateReportNumber;
import static com.drozal.dataterminal.util.Report.treeViewUtils.findXMLValue;

public class ArrestReportUtils {

    public static int countReports() {
        try {
            List<ArrestReport> logs = ArrestReportUtils.loadArrestReports().getArrestReportList();

            if (logs == null) {
                return 0;
            }

            return logs.size();
        } catch (Exception e) {
            logError("Exception", e);
            return -1;
        }
    }

    public static Map<String, Object> arrestLayout() {
        Map<String, Object> arrestReport = createReportWindow("Arrest Report", 7, 9, new nestedReportUtils.TransferConfig("Transfer Information To New Report", new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("transferimpoundbtn", 4, nestedReportUtils.FieldType.TRANSFER_BUTTON), new nestedReportUtils.FieldConfig("transferincidentbtn", 4, nestedReportUtils.FieldType.TRANSFER_BUTTON), new nestedReportUtils.FieldConfig("transfersearchbtn", 4, nestedReportUtils.FieldType.TRANSFER_BUTTON))), new nestedReportUtils.SectionConfig("Officer Information", true, new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("name", 5, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("rank", 5, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("number", 2, nestedReportUtils.FieldType.TEXT_FIELD)), new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("division", 6, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("agency", 6, nestedReportUtils.FieldType.TEXT_FIELD))), new nestedReportUtils.SectionConfig("Location / Timestamp Information", true, new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("street", 4, nestedReportUtils.FieldType.COMBO_BOX_STREET), new nestedReportUtils.FieldConfig("area", 4, nestedReportUtils.FieldType.COMBO_BOX_AREA), new nestedReportUtils.FieldConfig("county", 4, nestedReportUtils.FieldType.TEXT_FIELD)), new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("date", 5, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("time", 5, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("arrest number", 2, nestedReportUtils.FieldType.TEXT_FIELD))), new nestedReportUtils.SectionConfig("Offender Information", true, new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("offender name", 4, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("offender age", 4, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("offender gender", 4, nestedReportUtils.FieldType.TEXT_FIELD)), new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("offender address", 6, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("offender description", 6, nestedReportUtils.FieldType.TEXT_FIELD))), new nestedReportUtils.SectionConfig("(If Applicable) Offender Medical Information", false, new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("ambulance required (Y/N)", 6, nestedReportUtils.FieldType.TEXT_FIELD), new nestedReportUtils.FieldConfig("taser deployed (Y/N)", 6, nestedReportUtils.FieldType.TEXT_FIELD)), new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("other information", 12, nestedReportUtils.FieldType.TEXT_FIELD))), new nestedReportUtils.SectionConfig("Charge Notes", true, new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("notes", 12, nestedReportUtils.FieldType.TEXT_AREA))), new nestedReportUtils.SectionConfig("Charge(s)", true, new nestedReportUtils.RowConfig(new nestedReportUtils.FieldConfig("chargeview", 6, nestedReportUtils.FieldType.CHARGES_TREE_VIEW))));
        return arrestReport;
    }

    public static Map<String, Object> newArrest(BarChart<String, Number> reportChart, AreaChart areaReportChart) {
        Map<String, Object> arrestReport = arrestLayout();

        Map<String, Object> arrestReportMap = (Map<String, Object>) arrestReport.get("Arrest Report Map");

        TextField officername = (TextField) arrestReportMap.get("name");
        TextField officerrank = (TextField) arrestReportMap.get("rank");
        TextField officerdiv = (TextField) arrestReportMap.get("division");
        TextField officeragen = (TextField) arrestReportMap.get("agency");
        TextField officernumarrest = (TextField) arrestReportMap.get("number");

        TextField offenderName = (TextField) arrestReportMap.get("offender name");
        TextField offenderAge = (TextField) arrestReportMap.get("offender age");
        TextField offenderGender = (TextField) arrestReportMap.get("offender gender");
        TextField offenderAddress = (TextField) arrestReportMap.get("offender address");
        TextField offenderDescription = (TextField) arrestReportMap.get("offender description");

        ComboBox area = (ComboBox) arrestReportMap.get("area");
        ComboBox street = (ComboBox) arrestReportMap.get("street");
        TextField county = (TextField) arrestReportMap.get("county");
        TextField arrestnum = (TextField) arrestReportMap.get("arrest number");
        TextField date = (TextField) arrestReportMap.get("date");
        TextField time = (TextField) arrestReportMap.get("time");

        TextField ambulancereq = (TextField) arrestReportMap.get("ambulance required (Y/N)");
        TextField taserdep = (TextField) arrestReportMap.get("taser deployed (Y/N)");
        TextField othermedinfo = (TextField) arrestReportMap.get("other information");

        TextArea notes = (TextArea) arrestReportMap.get("notes");

        TreeView chargetreeview = (TreeView) arrestReportMap.get("chargeview");
        TableView chargetable = (TableView) arrestReportMap.get("ChargeTableView");

        Button transferimpoundbtn = (Button) arrestReportMap.get("transferimpoundbtn");
        transferimpoundbtn.setText("New Impound Report");
        Button transferincidentbtn = (Button) arrestReportMap.get("transferincidentbtn");
        transferincidentbtn.setText("New Incident Report");
        Button transfersearchbtn = (Button) arrestReportMap.get("transfersearchbtn");
        transfersearchbtn.setText("New Search Report");

        BorderPane root = (BorderPane) arrestReport.get("root");
        Stage stage = (Stage) root.getScene().getWindow();

        MenuButton pullnotesbtn = (MenuButton) arrestReport.get("pullNotesBtn");
        pullnotesbtn.setPopupSide(Side.TOP);

        try {
            officername.setText(ConfigReader.configRead("userInfo", "Name"));
            officerrank.setText(ConfigReader.configRead("userInfo", "Rank"));
            officerdiv.setText(ConfigReader.configRead("userInfo", "Division"));
            officeragen.setText(ConfigReader.configRead("userInfo", "Agency"));
            officernumarrest.setText(ConfigReader.configRead("userInfo", "Number"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        date.setText(getDate());
        time.setText(getTime());
        arrestnum.setText(generateReportNumber());

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
                            updateTextFromNotepad(arrestnum, noteArea, "-number");
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

            TextArea notesimp = (TextArea) impoundReportMap.get("notes");

            officernameimp.setText(officername.getText());
            officerdivimp.setText(officerdiv.getText());
            officerrankimp.setText(officerrank.getText());
            officeragenimp.setText(officeragen.getText());
            officernumimp.setText(officernumarrest.getText());
            timeimp.setText(time.getText());
            dateimp.setText(date.getText());
            offenderAddressimp.setText(offenderAddress.getText());
            offenderNameimp.setText(offenderName.getText());
            offenderAgeimp.setText(offenderAge.getText());
            offenderGenderimp.setText(offenderGender.getText());
            notesimp.setText(notes.getText());
            numimp.setText(arrestnum.getText());
        });

        transferincidentbtn.setOnAction(event -> {
            Map<String, Object> incidentReportObj = IncidentReportUtils.newIncident(reportChart, areaReportChart);

            Map<String, Object> incidentReportMap = (Map<String, Object>) incidentReportObj.get("Incident Report Map");

            TextField nameinc = (TextField) incidentReportMap.get("name");
            TextField rankinc = (TextField) incidentReportMap.get("rank");
            TextField divinc = (TextField) incidentReportMap.get("division");
            TextField ageninc = (TextField) incidentReportMap.get("agency");
            TextField officernuminc = (TextField) incidentReportMap.get("number");

            TextField incidentnum = (TextField) incidentReportMap.get("incident num");
            TextField dateinc = (TextField) incidentReportMap.get("date");
            TextField timeinc = (TextField) incidentReportMap.get("time");
            ComboBox streetinc = (ComboBox) incidentReportMap.get("street");
            ComboBox areainc = (ComboBox) incidentReportMap.get("area");
            TextField countyinc = (TextField) incidentReportMap.get("county");

            TextField suspectsinc = (TextField) incidentReportMap.get("suspect(s)");
            TextArea notesinc = (TextArea) incidentReportMap.get("notes");

            nameinc.setText(officername.getText());
            divinc.setText(officerdiv.getText());
            rankinc.setText(officerrank.getText());
            ageninc.setText(officeragen.getText());
            officernuminc.setText(officernumarrest.getText());
            dateinc.setText(date.getText());
            timeinc.setText(time.getText());
            incidentnum.setText(arrestnum.getText());
            countyinc.setText(county.getText());
            areainc.setValue(area.getEditor().getText());
            streetinc.getEditor().setText(street.getEditor().getText());
            suspectsinc.setText(offenderName.getText());
            notesinc.setText(notes.getText());
        });

        transfersearchbtn.setOnAction(event -> {
            Map<String, Object> ArrestReportObj = SearchReportUtils.newSearch(reportChart, areaReportChart);

            Map<String, Object> ArrestReportMap = (Map<String, Object>) ArrestReportObj.get("Search Report Map");

            TextField namesrch = (TextField) ArrestReportMap.get("name");
            TextField ranksrch = (TextField) ArrestReportMap.get("rank");
            TextField divsrch = (TextField) ArrestReportMap.get("division");
            TextField agensrch = (TextField) ArrestReportMap.get("agency");
            TextField numsrch = (TextField) ArrestReportMap.get("number");

            TextField searchnum = (TextField) ArrestReportMap.get("search num");
            TextField datesrch = (TextField) ArrestReportMap.get("date");
            TextField timesrch = (TextField) ArrestReportMap.get("time");
            ComboBox streetsrch = (ComboBox) ArrestReportMap.get("street");
            ComboBox areasrch = (ComboBox) ArrestReportMap.get("area");
            TextField countysrch = (TextField) ArrestReportMap.get("county");

            TextField searchedindividualsrch = (TextField) ArrestReportMap.get("searched individual");

            searchnum.setText(arrestnum.getText());
            namesrch.setText(officername.getText());
            divsrch.setText(officerdiv.getText());
            ranksrch.setText(officerrank.getText());
            agensrch.setText(officeragen.getText());
            numsrch.setText(officernumarrest.getText());
            timesrch.setText(time.getText());
            datesrch.setText(date.getText());
            searchedindividualsrch.setText(offenderName.getText());
            countysrch.setText(county.getText());
            areasrch.setValue(area.getEditor().getText());
            streetsrch.getEditor().setText(street.getEditor().getText());
        });

        Button submitBtn = (Button) arrestReport.get("submitBtn");
        Label warningLabel = (Label) arrestReport.get("warningLabel");

        submitBtn.setOnAction(event -> {
            if (arrestnum.getText().trim().isEmpty()) {
                warningLabel.setVisible(true);
                warningLabel.setText("Arrest Number can't be empty!");
                warningLabel.setStyle("-fx-font-family: \"Segoe UI Black\"; -fx-text-fill: red;");
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(e -> warningLabel.setVisible(false));
                pause.play();
            } else {
                for (String fieldName : arrestReportMap.keySet()) {
                    Object field = arrestReportMap.get(fieldName);
                    if (field instanceof ComboBox<?> comboBox) {
                        if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
                            comboBox.getSelectionModel().selectFirst();
                        }
                    }
                }

                ObservableList<ChargesData> formDataList = chargetable.getItems();
                StringBuilder stringBuilder = new StringBuilder();
                StringBuilder chargesBuilder = new StringBuilder();
                for (ChargesData formData : formDataList) {
                    String probationChance = findXMLValue(formData.getCharge(), "probation_chance", "data/Charges.xml");

                    String minYears = findXMLValue(formData.getCharge(), "min_years", "data/Charges.xml");
                    String maxYears = findXMLValue(formData.getCharge(), "max_years", "data/Charges.xml");
                    String minMonths = findXMLValue(formData.getCharge(), "min_months", "data/Charges.xml");
                    String maxMonths = findXMLValue(formData.getCharge(), "max_months", "data/Charges.xml");

                    String suspChance = findXMLValue(formData.getCharge(), "susp_chance", "data/Charges.xml");
                    String minSusp = findXMLValue(formData.getCharge(), "min_susp", "data/Charges.xml");
                    String maxSusp = findXMLValue(formData.getCharge(), "max_susp", "data/Charges.xml");
                    String revokeChance = findXMLValue(formData.getCharge(), "revoke_chance", "data/Charges.xml");

                    String fine = findXMLValue(formData.getCharge(), "fine", "data/Charges.xml");
                    String finek = findXMLValue(formData.getCharge(), "fine_k", "data/Charges.xml");

                    String isTraffic = findXMLValue(formData.getCharge(), "traffic", "data/Charges.xml");

                    stringBuilder.append(formData.getCharge()).append(" | ");
                    chargesBuilder.append(parseCourtData(isTraffic, probationChance, minYears, maxYears, minMonths, maxMonths, suspChance, minSusp, maxSusp, revokeChance, fine, finek) + " | ");
                }
                if (stringBuilder.length() > 0) {
                    stringBuilder.setLength(stringBuilder.length() - 1);
                }

                ArrestReport arrestReport1 = new ArrestReport();
                arrestReport1.setArrestNumber((arrestnum.getText()));
                arrestReport1.setArrestDate((date.getText()));
                arrestReport1.setArrestTime((time.getText()));
                arrestReport1.setArrestCharges((stringBuilder.toString()));
                arrestReport1.setArrestDetails((notes.getText()));
                arrestReport1.setOfficerRank((officerrank.getText()));

                arrestReport1.setArrestCounty(toTitleCase(county.getText()));
                arrestReport1.setArrestArea(toTitleCase(area.getEditor().getText()));
                arrestReport1.setArrestStreet(toTitleCase(street.getEditor().getText()));
                arrestReport1.setArresteeName(toTitleCase(offenderName.getText()));
                arrestReport1.setArresteeAge(toTitleCase(offenderAge.getText()));
                arrestReport1.setArresteeGender(toTitleCase(offenderGender.getText()));
                arrestReport1.setArresteeDescription(toTitleCase(offenderDescription.getText()));
                arrestReport1.setAmbulanceYesNo(toTitleCase(ambulancereq.getText()));
                arrestReport1.setTaserYesNo(toTitleCase(taserdep.getText()));
                arrestReport1.setArresteeMedicalInformation(toTitleCase(othermedinfo.getText()));
                arrestReport1.setArresteeHomeAddress(toTitleCase(offenderAddress.getText()));
                arrestReport1.setOfficerName(toTitleCase(officername.getText()));
                arrestReport1.setOfficerNumber(toTitleCase(officernumarrest.getText()));
                arrestReport1.setOfficerDivision(toTitleCase(officerdiv.getText()));
                arrestReport1.setOfficerAgency(toTitleCase(officeragen.getText()));
                try {
                    ArrestReportUtils.addArrestReport(arrestReport1);
                } catch (JAXBException e) {
                    logError("Could not create new ArrestReport: ", e);
                }
                Optional<Ped> pedOptional = Ped.PedHistoryUtils.findPedByName(arrestReport1.getArresteeName());
                if (pedOptional.isPresent()) {
                    log("Ped is present in history, adding new charges.. ", LogUtils.Severity.DEBUG);
                    Ped ped1 = pedOptional.get();
                    String beforePriors = ped1.getArrestPriors();
                    String afterPriors = (beforePriors + stringBuilder.toString().trim()).replaceAll("null", "");
                    ped1.setArrestPriors(afterPriors);
                    try {
                        Ped.PedHistoryUtils.addPed(ped1);
                    } catch (JAXBException e) {
                        logError("Error updating ped priors from arrestReport: ", e);
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
                    NotificationManager.showNotificationInfo("Report Manager", "A new Arrest Report has been submitted. Case#: " + casenum + " Name: " + offenderName.getText(), mainRT);
                    log("Added case from arrest, Case#: " + casenum + " Name: " + offenderName.getText(), LogUtils.Severity.INFO);
                    actionController.needCourtRefresh.set(1);
                } else {
                    NotificationManager.showNotificationInfo("Report Manager", "A new Arrest Report has been submitted.", mainRT);
                    NotificationManager.showNotificationWarning("Report Manager", "Could not create court case from arrest because either name or offences field(s) were empty.", mainRT);
                    log("Could not create court case from arrest because either name or offences field(s) were empty.", LogUtils.Severity.ERROR);
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
                            logError("Error searching name to update ped lookup from arrestreport: " + controllerVar.getPedfnamefield().getText().trim() + " " + controllerVar.getPedlnamefield().getText().trim(), e);
                        }
                    }
                }

                actionController.needRefresh.set(1);
                updateChartIfMismatch(reportChart);
                refreshChart(areaReportChart, "area");

                stage.close();
            }
        });

        return arrestReport;
    }

    public static ArrestReports loadArrestReports() throws JAXBException {
        File file = new File(arrestLogURL);
        if (!file.exists()) {
            return new ArrestReports();
        }

        try {
            JAXBContext context = JAXBContext.newInstance(ArrestReports.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (ArrestReports) unmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            logError("Error loading ArrestReports: ", e);
            throw e;
        }
    }

    private static void saveArrestReports(ArrestReports ArrestReports) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ArrestReports.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        File file = new File(arrestLogURL);
        marshaller.marshal(ArrestReports, file);
    }

    public static void addArrestReport(ArrestReport ArrestReport) throws JAXBException {
        ArrestReports ArrestReports = loadArrestReports();

        if (ArrestReports.getArrestReportList() == null) {
            ArrestReports.setArrestReportList(new ArrayList<>());
        }

        Optional<ArrestReport> existingReport = ArrestReports.getArrestReportList().stream().filter(e -> e.getArrestNumber().equals(ArrestReport.getArrestNumber())).findFirst();

        if (existingReport.isPresent()) {
            ArrestReports.getArrestReportList().remove(existingReport.get());
            ArrestReports.getArrestReportList().add(ArrestReport);
            log("ArrestReport with number " + ArrestReport.getArrestNumber() + " updated.", LogUtils.Severity.INFO);
        } else {
            ArrestReports.getArrestReportList().add(ArrestReport);
            log("ArrestReport with number " + ArrestReport.getArrestNumber() + " added.", LogUtils.Severity.INFO);
        }

        saveArrestReports(ArrestReports);
    }

    public static void deleteArrestReport(String ArrestReportnumber) throws JAXBException {
        ArrestReports ArrestReports = loadArrestReports();

        if (ArrestReports.getArrestReportList() != null) {
            ArrestReports.getArrestReportList().removeIf(e -> e.getArrestNumber().equals(ArrestReportnumber));
            saveArrestReports(ArrestReports);
            log("ArrestReport with number " + ArrestReportnumber + " deleted.", LogUtils.Severity.INFO);
        }
    }

}