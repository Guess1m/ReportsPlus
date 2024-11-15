package com.Guess.ReportsPlus.Windows.Apps;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager;
import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.util.History.Ped;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.Guess.ReportsPlus.util.Misc.NoteTab;
import jakarta.xml.bind.JAXBException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.MainApplication.mainDesktopControllerObj;
import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.*;
import static com.Guess.ReportsPlus.Windows.Server.CurrentIDViewController.defaultPedImagePath;
import static com.Guess.ReportsPlus.logs.Arrest.ArrestReportUtils.newArrest;
import static com.Guess.ReportsPlus.logs.TrafficCitation.TrafficCitationUtils.newCitation;
import static com.Guess.ReportsPlus.util.History.Ped.PedHistoryUtils.findPedByName;
import static com.Guess.ReportsPlus.util.Misc.AudioUtil.playSound;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.controllerUtils.*;
import static com.Guess.ReportsPlus.util.Misc.stringUtil.getJarPath;
import static com.Guess.ReportsPlus.util.Misc.stringUtil.pedImageFolderURL;
import static com.Guess.ReportsPlus.util.Server.recordUtils.grabPedData;

public class PedLookupViewController {

    public static PedLookupViewController pedLookupViewController;
    private final List<String> recentPedSearches = new ArrayList<>();
    @javafx.fxml.FXML
    private TextField pedgenfield;
    @javafx.fxml.FXML
    private TextField pedaffiliationfield;
    @javafx.fxml.FXML
    private TextField pedlnamefield;
    @javafx.fxml.FXML
    private TextField pedfnamefield;
    @javafx.fxml.FXML
    private AnchorPane pedRecordPane;
    @javafx.fxml.FXML
    private TextField pedgunlicensetypefield;
    @javafx.fxml.FXML
    private TextField pedgunlicensestatusfield;
    @javafx.fxml.FXML
    private Label noPedImageFoundlbl;
    @javafx.fxml.FXML
    private TextField pedprobationstatusfield;
    @javafx.fxml.FXML
    private TextField pedfishinglicstatusfield;
    @javafx.fxml.FXML
    private TextField peddobfield;
    @javafx.fxml.FXML
    private Label ped13;
    @javafx.fxml.FXML
    private Label ped12;
    @javafx.fxml.FXML
    private Label ped15;
    @javafx.fxml.FXML
    private Label ped14;
    @javafx.fxml.FXML
    private Label ped17;
    private ScrollPane pedPane;
    @javafx.fxml.FXML
    private Label ped16;
    @javafx.fxml.FXML
    private Label ped19;
    @javafx.fxml.FXML
    private Label ped18;
    @javafx.fxml.FXML
    private Label ped3;
    @javafx.fxml.FXML
    private Label ped4;
    @javafx.fxml.FXML
    private Label ped5;
    @javafx.fxml.FXML
    private TextField pedlicnumfield;
    @javafx.fxml.FXML
    private Label ped6;
    @javafx.fxml.FXML
    private Label ped1;
    @javafx.fxml.FXML
    private Label ped11;
    @javafx.fxml.FXML
    private Label ped2;
    @javafx.fxml.FXML
    private TextField pedboatinglicstatusfield;
    @javafx.fxml.FXML
    private Label ped10;
    @javafx.fxml.FXML
    private TextField peddescfield;
    @javafx.fxml.FXML
    private Label ped7;
    @javafx.fxml.FXML
    private Label ped8;
    @javafx.fxml.FXML
    private Label ped9;
    @javafx.fxml.FXML
    private ListView pedcitationpriorslistview;
    @javafx.fxml.FXML
    private Label noRecordFoundLabelPed;
    @javafx.fxml.FXML
    private TextField pedlicensefield;
    @javafx.fxml.FXML
    private TextField pedwantedfield;
    @javafx.fxml.FXML
    private TextField pedparolestatusfield;
    @javafx.fxml.FXML
    private Label ped23;
    @javafx.fxml.FXML
    private TextField pedhuntinglicstatusfield;
    @javafx.fxml.FXML
    private TextField pedgunlicenseclassfield;
    @javafx.fxml.FXML
    private Label ped20;
    @javafx.fxml.FXML
    private Label ped22;
    @javafx.fxml.FXML
    private BorderPane root;
    @javafx.fxml.FXML
    private Label ped21;
    @javafx.fxml.FXML
    private Button pedSearchBtn;
    @javafx.fxml.FXML
    private ComboBox pedSearchField;
    @javafx.fxml.FXML
    private TextField pedaddressfield;
    @javafx.fxml.FXML
    private Label lookupmainlbl;
    @javafx.fxml.FXML
    private TextField pedflagfield;
    @javafx.fxml.FXML
    private TextField pedaliasfield;
    @javafx.fxml.FXML
    private ImageView pedImageView;
    @javafx.fxml.FXML
    private ListView pedarrestpriorslistview;
    @javafx.fxml.FXML
    private Button probabilitySettingsBtn;
    @javafx.fxml.FXML
    private AnchorPane pedLookupPane;
    @javafx.fxml.FXML
    private AnchorPane lookupPane;
    @javafx.fxml.FXML
    private TextField pedtimesstoppedfield;
    @javafx.fxml.FXML
    private Label lbl1;
    @javafx.fxml.FXML
    private Button addDataToNotesBtn;
    @javafx.fxml.FXML
    private Button infobtn1;
    @javafx.fxml.FXML
    private Button infobtn3;
    @javafx.fxml.FXML
    private Button infobtn2;
    @javafx.fxml.FXML
    private Label info1;
    @javafx.fxml.FXML
    private Label info5;
    @javafx.fxml.FXML
    private Label info4;
    @javafx.fxml.FXML
    private Label info3;
    @javafx.fxml.FXML
    private Label info2;

    public void initialize() {
        noPedImageFoundlbl.setVisible(false);
        pedRecordPane.setVisible(false);
        noRecordFoundLabelPed.setVisible(false);

        pedSearchField.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    onPedSearchBtnClick(new ActionEvent());
                } catch (IOException e) {
                    logError("Error executing pedsearch from Enter: ", e);
                }
            }
        });
        
        addLocalization();
    }
    
    private void addLocalization() {
        lookupmainlbl.setText(localization.getLocalizedMessage("PedLookup.MainHeader", "D.M.V Pedestrian Lookup"));
        lbl1.setText(localization.getLocalizedMessage("PedLookup.SearchPedLabel", "Search Ped:"));
        noPedImageFoundlbl.setText(
                localization.getLocalizedMessage("PedLookup.NoPedImageFoundlbl", "No Image Found In System"));
        noRecordFoundLabelPed.setText(
                localization.getLocalizedMessage("PedLookup.NoPedFoundInSystem", "No Record Found In System"));
        
        addDataToNotesBtn.setText(
                localization.getLocalizedMessage("PedLookup.AddDataToNotesButton", "Add Data To Notes"));
        pedSearchBtn.setText(localization.getLocalizedMessage("PedLookup.SearchPedButton", "Search"));
        probabilitySettingsBtn.setText(
                localization.getLocalizedMessage("PedLookup.ProbabilitySettingsButton", "Probability Settings"));
        infobtn3.setText(
                localization.getLocalizedMessage("PedLookup.UpdateOtherInfoButton", "Update Other Information"));
        infobtn2.setText(localization.getLocalizedMessage("PedLookup.CreateCitationButton", "Create New Citation"));
        infobtn1.setText(localization.getLocalizedMessage("PedLookup.CreateArrestButton", "Create New Arrest"));
        
        ped21.setText(localization.getLocalizedMessage("PedLookup.ArrestHistoryLabel", "Arrest History:"));
        ped22.setText(localization.getLocalizedMessage("PedLookup.CitationHistoryLabel", "Citation History:"));
        
        info1.setText(localization.getLocalizedMessage("PedLookup.BasicInfoLabel", "Basic Information"));
        info2.setText(localization.getLocalizedMessage("PedLookup.LegalInfoLabel", "Legal Information"));
        info3.setText(localization.getLocalizedMessage("PedLookup.LicenseInfoLabel", "Licensing Information"));
        info4.setText(localization.getLocalizedMessage("PedLookup.OtherInfoLabel", "Other Information"));
        info5.setText(localization.getLocalizedMessage("PedLookup.PriorHistoryLabel", "Prior History"));
        
        ped1.setText(localization.getLocalizedMessage("PedLookup.FieldFirstName", "First Name:"));
        ped2.setText(localization.getLocalizedMessage("PedLookup.FieldLastName", "Last Name:"));
        ped3.setText(localization.getLocalizedMessage("PedLookup.FieldGender", "Gender:"));
        ped4.setText(localization.getLocalizedMessage("PedLookup.FieldWantedStatus", "Wanted Status:"));
        ped5.setText(localization.getLocalizedMessage("PedLookup.FieldLicenseStatus", "License Status:"));
        ped6.setText(localization.getLocalizedMessage("PedLookup.FieldBirthday", "Birthday:"));
        ped7.setText(localization.getLocalizedMessage("PedLookup.FieldAddress", "Address:"));
        ped8.setText(localization.getLocalizedMessage("PedLookup.FieldDescription", "Description:"));
        ped9.setText(localization.getLocalizedMessage("PedLookup.FieldLicenseNumber", "License Number:"));
        ped10.setText(localization.getLocalizedMessage("PedLookup.FieldAlias", "Alias(s):"));
        ped11.setText(localization.getLocalizedMessage("PedLookup.FieldAffiliations", "Affiliation(s):"));
        ped12.setText(localization.getLocalizedMessage("PedLookup.FieldParoleStatus", "Parole Status:"));
        ped13.setText(localization.getLocalizedMessage("PedLookup.FieldProbationStatus", "Probation Status:"));
        ped14.setText(localization.getLocalizedMessage("PedLookup.FieldTimesStopped", "Times Stopped:"));
        ped15.setText(localization.getLocalizedMessage("PedLookup.FieldGunLicenseStatus", "Gun License Status:"));
        ped16.setText(localization.getLocalizedMessage("PedLookup.FieldGunLicenseType", "Gun License Type"));
        ped17.setText(localization.getLocalizedMessage("PedLookup.FieldGunLicenseClass", "Gun License Class"));
        ped18.setText(
                localization.getLocalizedMessage("PedLookup.FieldFishingLicenseStatus", "Fishing License Status"));
        ped19.setText(
                localization.getLocalizedMessage("PedLookup.FieldBoatingLicenseStatus", "Boating License Status"));
        ped20.setText(
                localization.getLocalizedMessage("PedLookup.FieldHuntingLicenseStatus", "Hunting License Status"));
        ped23.setText(localization.getLocalizedMessage("PedLookup.FieldFlags", "Flag(s):"));
    }
    
    private void processPedData(String name, String licenseNumber, String gender, String birthday, String address, String isWanted, String licenseStatus, String pedModel) {
        Optional<Ped> searchedPed = Ped.PedHistoryUtils.findPedByNumber(licenseNumber);
        Ped ped = searchedPed.orElseGet(() -> {
            try {
                return createNewPed(name, licenseNumber, gender, birthday, address, isWanted, licenseStatus, pedModel);
            } catch (IOException e) {
                logError("Error creating new ped: ", e);
                return null;
            }
        });
        if (ped != null) {
            if (setPedRecordFields(ped)) {
                try {
                    if (ConfigReader.configRead("soundSettings", "playLookupWarning").equalsIgnoreCase("true")) {
                        playSound(getJarPath() + "/sounds/alert-wanted.wav");
                    }
                } catch (IOException e) {
                    logError("Error getting configValue for playLookupWarning: ", e);
                }
            }
        }
        pedRecordPane.setVisible(true);
        noRecordFoundLabelPed.setVisible(false);
    }

    private void processOwnerData(String owner, String vehPlateNum) {
        Optional<Ped> searchedPed = findPedByName(owner);
        Ped ped = searchedPed.orElseGet(() -> {
            try {
                return createOwnerPed(owner, vehPlateNum);
            } catch (IOException e) {
                logError("Error creating ownerPed: ", e);
                return null;
            }
        });

        if (ped != null) {
            if (setPedRecordFields(ped)) {
                try {
                    if (ConfigReader.configRead("soundSettings", "playLookupWarning").equalsIgnoreCase("true")) {
                        playSound(getJarPath() + "/sounds/alert-wanted.wav");
                    }
                } catch (IOException e) {
                    logError("Error getting configValue for playLookupWarning: ", e);
                }
            }
        }
        pedRecordPane.setVisible(true);
        noRecordFoundLabelPed.setVisible(false);
    }

    private boolean setPedRecordFields(Ped ped) {
        boolean playAudio = false;
        pedfnamefield.setText(ped.getFirstName());
        pedlnamefield.setText(ped.getLastName());
        pedgenfield.setText(ped.getGender());
        peddobfield.setText(ped.getBirthday());
        pedaddressfield.setText(ped.getAddress());

        pedlicensefield.setText(ped.getLicenseStatus());
        if (ped.getLicenseStatus().equalsIgnoreCase("EXPIRED") || ped.getLicenseStatus().equalsIgnoreCase(
                "SUSPENDED") || ped.getLicenseStatus().equalsIgnoreCase("REVOKED")) {
            pedlicensefield.setStyle("-fx-text-fill: red !important;");
            playAudio = true;

        } else {
            pedlicensefield.setStyle("-fx-text-fill: #006600 !important;");
            pedlicensefield.setText("Valid");
        }

        // Outstanding warrants
        pedwantedfield.setText(ped.getOutstandingWarrants() != null ? ped.getOutstandingWarrants() : "False");
        if (ped.getOutstandingWarrants() != null) {
            pedwantedfield.setStyle("-fx-text-fill: red !important;");
            playAudio = true;

        } else {
            pedwantedfield.setStyle("-fx-text-fill: black;");
        }

        pedgunlicensestatusfield.setText(ped.getGunLicenseStatus() != null ? ped.getGunLicenseStatus() : "False");
        if (ped.getGunLicenseStatus().equalsIgnoreCase("false")) {
            pedgunlicensestatusfield.setStyle("-fx-text-fill: black !important;");
        } else {
            pedgunlicensestatusfield.setStyle("-fx-text-fill: #006600 !important;");
            pedgunlicensestatusfield.setText("Valid");
        }

        pedprobationstatusfield.setText(ped.getProbationStatus() != null ? ped.getProbationStatus() : "False");
        if (ped.getProbationStatus() != null && ped.getProbationStatus().equalsIgnoreCase("true")) {
            pedprobationstatusfield.setStyle("-fx-text-fill: red !important;");
            pedprobationstatusfield.setText("On Probation");
        } else {
            pedprobationstatusfield.setStyle("-fx-text-fill: black !important;");
        }

        // Fishing license status
        pedfishinglicstatusfield.setText(
                ped.getFishingLicenseStatus() != null ? ped.getFishingLicenseStatus() : "False");
        if (ped.getFishingLicenseStatus() != null && ped.getFishingLicenseStatus().equalsIgnoreCase("true")) {
            pedfishinglicstatusfield.setStyle("-fx-text-fill: #006600 !important;");
            pedfishinglicstatusfield.setText("Valid");
        } else {
            pedfishinglicstatusfield.setStyle("-fx-text-fill: black !important;");
        }

        pedboatinglicstatusfield.setText(
                ped.getBoatingLicenseStatus() != null ? ped.getBoatingLicenseStatus() : "False");
        if (ped.getBoatingLicenseStatus() != null && ped.getBoatingLicenseStatus().equalsIgnoreCase("true")) {
            pedboatinglicstatusfield.setStyle("-fx-text-fill: #006600 !important;");
            pedboatinglicstatusfield.setText("Valid");
        } else {
            pedboatinglicstatusfield.setStyle("-fx-text-fill: black !important;");
        }

        pedgunlicenseclassfield.setText(ped.getGunLicenseClass() != null ? ped.getGunLicenseClass() : "No License");
        pedgunlicensetypefield.setText(ped.getGunLicenseType() != null ? ped.getGunLicenseType() : "No License");

        pedhuntinglicstatusfield.setText(
                ped.getHuntingLicenseStatus() != null ? ped.getHuntingLicenseStatus() : "False");
        if (ped.getHuntingLicenseStatus() != null && ped.getHuntingLicenseStatus().equalsIgnoreCase("true")) {
            pedhuntinglicstatusfield.setStyle("-fx-text-fill: #006600 !important;");
            pedhuntinglicstatusfield.setText("Valid");
        } else {
            pedhuntinglicstatusfield.setStyle("-fx-text-fill: black !important;");
        }

        pedlicnumfield.setText(ped.getLicenseNumber() != null ? ped.getLicenseNumber() : "No Data In System");
        pedlicnumfield.setStyle(
                ped.getLicenseNumber() == null ? "-fx-text-fill: #e65c00 !important;" : "-fx-text-fill: black;");

        String affiliations = ped.getAffiliations();
        if (affiliations == null || affiliations.equalsIgnoreCase("No Data In System")) {
            pedaffiliationfield.setText("No Data In System");
            pedaffiliationfield.setStyle("-fx-text-fill: #e65c00 !important;");
        } else {
            pedaffiliationfield.setText(affiliations);
            pedaffiliationfield.setStyle("-fx-text-fill: black !important;");
        }

        String flags = ped.getFlags();
        if (flags == null || flags.equalsIgnoreCase("No Data In System")) {
            pedflagfield.setText("No Data In System");
            pedflagfield.setStyle("-fx-text-fill: #e65c00 !important;");
        } else {
            pedflagfield.setText(flags);
            pedflagfield.setStyle("-fx-text-fill: black !important;");
        }

        String description = ped.getDescription();
        if (description == null || description.equalsIgnoreCase("No Data In System")) {
            peddescfield.setText("No Data In System");
            peddescfield.setStyle("-fx-text-fill: #e65c00 !important;");
        } else {
            peddescfield.setText(description);
            peddescfield.setStyle("-fx-text-fill: black !important;");
        }

        String aliases = ped.getAliases();
        if (aliases == null || aliases.equalsIgnoreCase("No Data In System")) {
            pedaliasfield.setText("No Data In System");
            pedaliasfield.setStyle("-fx-text-fill: #e65c00 !important;");
        } else {
            pedaliasfield.setText(aliases);
            pedaliasfield.setStyle("-fx-text-fill: black !important;");
        }

        pedparolestatusfield.setText(ped.getParoleStatus() != null ? ped.getParoleStatus() : "False");
        if (ped.getParoleStatus() != null && ped.getParoleStatus().equalsIgnoreCase("true")) {
            pedparolestatusfield.setStyle("-fx-text-fill: red !important;");
            pedparolestatusfield.setText("On Parole");
        } else {
            pedparolestatusfield.setStyle("-fx-text-fill: black !important;");
        }

        pedtimesstoppedfield.setText(ped.getTimesStopped() != null ? ped.getTimesStopped() : "No Data");
        pedtimesstoppedfield.setStyle(
                ped.getTimesStopped() == null ? "-fx-text-fill: #e65c00 !important;" : "-fx-text-fill: black;");

        ped6.setText("Birthday: (" + calculateAge(ped.getBirthday()) + ")");

        String pedModel = ped.getModel();
        if (pedModel != null && !pedModel.equalsIgnoreCase("Not Found")) {
            File pedImgFolder = new File(pedImageFolderURL);
            if (pedImgFolder.exists()) {
                log("Detected pedImage folder..", LogUtils.Severity.DEBUG);

                File[] matchingFiles = pedImgFolder.listFiles((dir, name) -> name.equalsIgnoreCase(pedModel + ".jpg"));

                if (matchingFiles != null && matchingFiles.length > 0) {
                    File matchingFile = matchingFiles[0];
                    log("Matching pedImage found: " + matchingFile.getName(), LogUtils.Severity.INFO);

                    try {
                        String fileURI = matchingFile.toURI().toString();
                        pedImageView.setImage(new Image(fileURI));
                        noPedImageFoundlbl.setVisible(true);
                        noPedImageFoundlbl.setText(
                                localization.getLocalizedMessage("PedLookup.PedImageFoundlbl", "Image Found in File:"));
                    } catch (Exception e) {
                        Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
                        pedImageView.setImage(defImage);
                        noPedImageFoundlbl.setVisible(true);
                        noPedImageFoundlbl.setText(localization.getLocalizedMessage("PedLookup.NoPedImageFoundlbl",
                                                                                    "No Image Found In System"));
                        logError("Could not set ped image: ", e);
                    }
                } else {
                    log("No matching image found for the model: " + pedModel + ", displaying no image found.",
                            LogUtils.Severity.WARN);
                    Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
                    pedImageView.setImage(defImage);
                    noPedImageFoundlbl.setVisible(true);
                    noPedImageFoundlbl.setText(localization.getLocalizedMessage("PedLookup.NoPedImageFoundlbl",
                                                                                "No Image Found In System"));
                }
            } else {
                Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
                pedImageView.setImage(defImage);
                noPedImageFoundlbl.setVisible(true);
                noPedImageFoundlbl.setText(
                        localization.getLocalizedMessage("PedLookup.NoPedImageFoundlbl", "No Image Found In System"));
            }
        } else {
            Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
            pedImageView.setImage(defImage);
            noPedImageFoundlbl.setVisible(true);
            noPedImageFoundlbl.setText(
                    localization.getLocalizedMessage("PedLookup.NoPedImageFoundlbl", "No Image Found In System"));
        }

        String citationPriors = ped.getCitationPriors();
        if (citationPriors == null) {
            citationPriors = "";
        }

        Pattern pattern = Pattern.compile("MaxFine:\\S+");
        Matcher matcher = pattern.matcher(citationPriors);
        String updatedCitPriors = matcher.replaceAll("").trim();

        ObservableList<Label> arrestPriors = createLabels(ped.getArrestPriors());
        ObservableList<Label> citPriors = createLabels(updatedCitPriors);

        pedarrestpriorslistview.setItems(arrestPriors);
        pedcitationpriorslistview.setItems(citPriors);
        return playAudio;
    }

    @javafx.fxml.FXML
    public void onPedSearchBtnClick(ActionEvent actionEvent) throws IOException {
        String searchedName = pedSearchField.getEditor().getText().trim();
        if (!searchedName.isEmpty()) {
            updateRecentSearches(recentPedSearches, pedSearchField, searchedName);
        }
        pedSearchField.getEditor().setText(searchedName);
        pedSearchField.getEditor().positionCaret(pedSearchField.getEditor().getText().length());

        log("Searched: " + searchedName, LogUtils.Severity.INFO);
        String pedFilePath = getJarPath() + File.separator + "serverData" + File.separator + "ServerWorldPeds.data";
        String carFilePath = getJarPath() + File.separator + "serverData" + File.separator + "ServerWorldCars.data";

        Map<String, String> pedData = grabPedData(pedFilePath, searchedName);
        Map<String, String> ownerSearch = grabPedData(carFilePath, searchedName);
        Optional<Ped> pedOptional = findPedByName(searchedName);

        String gender = pedData.getOrDefault("gender", "Not Found");
        String birthday = pedData.getOrDefault("birthday", "Not Found");
        String address = pedData.getOrDefault("address", "Not Found");
        String isWanted = pedData.getOrDefault("iswanted", "Not Found");
        String licenseStatus = formatLicenseStatus(pedData.getOrDefault("licensestatus", "Not Found"));
        String licenseNumber = pedData.getOrDefault("licensenumber", "Not Found");
        String name = pedData.getOrDefault("name", "Not Found");
        String pedModel = pedData.getOrDefault("pedmodel", "Not Found");

        String owner = ownerSearch.getOrDefault("owner", "Not Found");
        String ownerPlateNum = ownerSearch.getOrDefault("licenseplate", "Not Found");

        if (pedOptional.isPresent()) {
            log("Found: [" + name + "] From PedHistory file", LogUtils.Severity.DEBUG);
            Ped ped = pedOptional.get();
            if (ped.getModel() == null) {
                ped.setModel("Not Found");
                try {
                    Ped.PedHistoryUtils.addPed(ped);
                } catch (JAXBException e) {
                    logError("Could not save new pedModel: ", e);
                }
                log("Set ped as 'Not Found' since it created before pedModel was added", LogUtils.Severity.WARN);
            }
            processPedData(ped.getName(), ped.getLicenseNumber(), ped.getGender(), ped.getBirthday(), ped.getAddress(),
                    ped.getWantedStatus(), ped.getLicenseStatus(), ped.getModel());
        } else if (!name.equals("Not Found")) {
            log("Found: [" + name + "] From WorldPed file", LogUtils.Severity.DEBUG);
            processPedData(name, licenseNumber, gender, birthday, address, isWanted, licenseStatus, pedModel);
        } else if (owner != null && !owner.equalsIgnoreCase("Not Found") && !owner.equalsIgnoreCase(
                "Los Santos Police Department") && !owner.equalsIgnoreCase(
                "Los Santos Sheriff's Office") && !owner.equalsIgnoreCase(
                "Blaine County Sheriff's Office") && !owner.equalsIgnoreCase("San Andreas Highway Patrol")) {
            log("Found Vehicle Owner: [" + owner + "] From WorldVeh file, plate#: " + ownerPlateNum,
                    LogUtils.Severity.DEBUG);
            processOwnerData(owner, ownerPlateNum);
        } else {
            log("No Ped With Name: [" + searchedName + "] Found Anywhere", LogUtils.Severity.WARN);
            pedRecordPane.setVisible(false);
            noRecordFoundLabelPed.setVisible(true);
        }
    }

    @javafx.fxml.FXML
    public void onLookupProbabilitySettingsClick(ActionEvent actionEvent) {
        WindowManager.createCustomWindow(mainDesktopControllerObj.getDesktopContainer(),
                                         "Windows/Settings/probability-settings-view.fxml", "Lookup Probability Config",
                                         false, 1, true, false, mainDesktopControllerObj.getTaskBarApps(),
                                         new Image(Objects.requireNonNull(
                        Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Apps/setting.png"))));
    }

    @javafx.fxml.FXML
    public void pedAddDataToNotes(ActionEvent actionEvent) throws IOException {
        if (!noRecordFoundLabelPed.isVisible()) {
            String name = "";
            String age;
            String gender;
            String address;
            String description;
            StringBuilder fullString = new StringBuilder();
            if (pedfnamefield != null && !pedfnamefield.getText().isEmpty() && pedlnamefield != null && !pedlnamefield.getText().isEmpty()) {
                name = pedfnamefield.getText().trim() + " " + pedlnamefield.getText().trim();
                fullString.append("-name ").append(name).append(" ");
            }
            if (peddobfield != null && !peddobfield.getText().isEmpty()) {
                age = peddobfield.getText().trim();
                fullString.append("-age ").append(age).append(" ");
            }
            if (pedgenfield != null && !pedgenfield.getText().isEmpty()) {
                gender = pedgenfield.getText().trim();
                fullString.append("-gender ").append(gender).append(" ");
            }
            if (pedaddressfield != null && !pedaddressfield.getText().isEmpty()) {
                address = pedaddressfield.getText().trim();
                fullString.append("-address ").append(address).append(" ");
            }
            if (peddescfield != null && !peddescfield.getText().isEmpty()) {
                if (!peddescfield.getText().equalsIgnoreCase("no data in system")) {
                    description = peddescfield.getText().trim();
                    fullString.append("-description ").append(description).append(" ");
                }
            }

            notesTabList.add(new NoteTab(name, fullString.toString()));

            if (notesViewController != null) {
                createNoteTabs();
            }
        }
    }

    @javafx.fxml.FXML
    public void pedCreateCitationReport(ActionEvent actionEvent) {
        String name = pedfnamefield.getText().trim() + " " + pedlnamefield.getText().trim();
        String age = calculateAge(peddobfield.getText().trim());
        String gender = pedgenfield.getText().trim();
        String address = pedaddressfield.getText().trim();
        String desc = peddescfield.getText().trim();

        Map<String, Object> trafficCitationObj = newCitation();
        
        Map<String, Object> citationReportMap = (Map<String, Object>) trafficCitationObj.get(
                localization.getLocalizedMessage("ReportWindows.CitationReportTitle", "Citation Report") + " Map");
        
        TextField offenderName = (TextField) citationReportMap.get(
                localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
        TextField offenderAge = (TextField) citationReportMap.get(
                localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"));
        TextField offenderGender = (TextField) citationReportMap.get(
                localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"));
        TextField offenderAddress = (TextField) citationReportMap.get(
                localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"));
        TextField offenderDescription = (TextField) citationReportMap.get(
                localization.getLocalizedMessage("ReportWindows.FieldOffenderDescription", "offender description"));

        offenderName.setText(name);
        offenderAge.setText(age);
        offenderGender.setText(gender);
        offenderAddress.setText(address);
        if (!desc.equalsIgnoreCase("No Data In System") && !desc.isEmpty()) {
            offenderDescription.setText(desc);
        }
    }

    @javafx.fxml.FXML
    public void pedCreateArrestReport(ActionEvent actionEvent) {
        String name = pedfnamefield.getText().trim() + " " + pedlnamefield.getText().trim();
        String age = calculateAge(peddobfield.getText().trim());
        String gender = pedgenfield.getText().trim();
        String address = pedaddressfield.getText().trim();
        String desc = peddescfield.getText().trim();

        Map<String, Object> arrestReportObj = newArrest();
        
        Map<String, Object> arrestReportMap = (Map<String, Object>) arrestReportObj.get(
                localization.getLocalizedMessage("ReportWindows.ArrestReportTitle", "Arrest Report") + " Map");
        
        TextField offenderName = (TextField) arrestReportMap.get(
                localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
        TextField offenderAge = (TextField) arrestReportMap.get(
                localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"));
        TextField offenderGender = (TextField) arrestReportMap.get(
                localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"));
        TextField offenderAddress = (TextField) arrestReportMap.get(
                localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"));
        TextField offenderDescription = (TextField) arrestReportMap.get(
                localization.getLocalizedMessage("ReportWindows.FieldOffenderDescription", "offender description"));

        offenderName.setText(name);
        offenderAge.setText(age);
        offenderGender.setText(gender);
        offenderAddress.setText(address);
        if (!desc.equalsIgnoreCase("No Data In System") && !desc.isEmpty()) {
            offenderDescription.setText(desc);
        }
    }

    @javafx.fxml.FXML
    public void pedUpdateInfo(ActionEvent actionEvent) {
        String searchedLicenseNum = pedlicnumfield.getText();
        Optional<Ped> optionalPed = Ped.PedHistoryUtils.findPedByNumber(searchedLicenseNum);

        if (optionalPed.isPresent()) {
            Ped ped = optionalPed.get();

            // Update flags
            String pedflagfieldText = pedflagfield.getText();
            if (!pedflagfieldText.equalsIgnoreCase("No Data In System") && !pedflagfieldText.isEmpty()) {
                pedflagfield.setStyle("-fx-text-fill: black !important;");
                ped.setFlags(pedflagfieldText.trim());
            } else {
                pedflagfield.setStyle("-fx-text-fill: #e65c00 !important;");
                pedflagfield.setText("No Data In System");
                ped.setFlags(null);
            }

            String affiliationText = pedaffiliationfield.getText();
            if (!affiliationText.equalsIgnoreCase("No Data In System") && !affiliationText.isEmpty()) {
                pedaffiliationfield.setStyle("-fx-text-fill: black !important;");
                ped.setAffiliations(affiliationText.trim());
            } else {
                pedaffiliationfield.setStyle("-fx-text-fill: #e65c00 !important;");
                pedaffiliationfield.setText("No Data In System");
                ped.setAffiliations(null);
            }

            String descText = peddescfield.getText();
            if (!descText.equalsIgnoreCase("No Data In System") && !descText.isEmpty()) {
                peddescfield.setStyle("-fx-text-fill: black !important;");
                ped.setDescription(descText.trim());
            } else {
                peddescfield.setStyle("-fx-text-fill: #e65c00 !important;");
                peddescfield.setText("No Data In System");
                ped.setDescription(null);
            }

            String aliasText = pedaliasfield.getText();
            if (!aliasText.equalsIgnoreCase("No Data In System") && !aliasText.isEmpty()) {
                pedaliasfield.setStyle("-fx-text-fill: black !important;");
                ped.setAliases(aliasText.trim());
            } else {
                pedaliasfield.setStyle("-fx-text-fill: #e65c00 !important;");
                pedaliasfield.setText("No Data In System");
                ped.setAliases(null);
            }

            try {
                Ped.PedHistoryUtils.addPed(ped);
            } catch (JAXBException e) {
                logError("Could not add ped from update fields button: ", e);
            }
        }
    }

    public Label getNoPedImageFoundlbl() {
        return noPedImageFoundlbl;
    }

    public Label getPed13() {
        return ped13;
    }

    public Label getPed12() {
        return ped12;
    }

    public Label getPed15() {
        return ped15;
    }

    public Label getPed14() {
        return ped14;
    }

    public Label getPed17() {
        return ped17;
    }

    public Label getPed16() {
        return ped16;
    }

    public Label getPed19() {
        return ped19;
    }

    public Label getPed18() {
        return ped18;
    }

    public Label getPed3() {
        return ped3;
    }

    public Label getPed4() {
        return ped4;
    }

    public Label getPed5() {
        return ped5;
    }

    public Label getPed6() {
        return ped6;
    }

    public Label getPed1() {
        return ped1;
    }

    public Label getPed11() {
        return ped11;
    }

    public Label getPed2() {
        return ped2;
    }

    public Label getPed10() {
        return ped10;
    }

    public Label getPed7() {
        return ped7;
    }

    public Label getPed8() {
        return ped8;
    }

    public Label getPed9() {
        return ped9;
    }

    public Label getPed23() {
        return ped23;
    }

    public Label getPed20() {
        return ped20;
    }

    public Label getPed22() {
        return ped22;
    }

    public BorderPane getRoot() {
        return root;
    }

    public Label getPed21() {
        return ped21;
    }

    public Label getLookupmainlbl() {
        return lookupmainlbl;
    }

    public Button getProbabilitySettingsBtn() {
        return probabilitySettingsBtn;
    }

    public AnchorPane getPedLookupPane() {
        return pedLookupPane;
    }

    public AnchorPane getLookupPane() {
        return lookupPane;
    }

    public TextField getPedlnamefield() {
        return pedlnamefield;
    }

    public TextField getPedfnamefield() {
        return pedfnamefield;
    }

    public AnchorPane getPedRecordPane() {
        return pedRecordPane;
    }

    public Button getPedSearchBtn() {
        return pedSearchBtn;
    }

    public Button getAddDataToNotesBtn() {
        return addDataToNotesBtn;
    }

    public Label getLbl1() {
        return lbl1;
    }

    public ComboBox getPedSearchField() {
        return pedSearchField;
    }

    public Label getInfo1() {
        return info1;
    }

    public Label getInfo2() {
        return info2;
    }

    public Label getInfo3() {
        return info3;
    }

    public Label getInfo4() {
        return info4;
    }

    public Label getInfo5() {
        return info5;
    }

    public Button getInfobtn1() {
        return infobtn1;
    }

    public Button getInfobtn2() {
        return infobtn2;
    }

    public Button getInfobtn3() {
        return infobtn3;
    }
}
