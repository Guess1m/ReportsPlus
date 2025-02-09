package com.Guess.ReportsPlus.Windows.Apps;

import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.logs.LookupObjects.PedObject;
import com.Guess.ReportsPlus.util.History.Ped;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.Guess.ReportsPlus.util.Other.NoteTab;
import com.Guess.ReportsPlus.util.Server.Objects.ID.ID;
import com.Guess.ReportsPlus.util.Strings.URLStrings;
import jakarta.xml.bind.JAXBException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.*;
import static com.Guess.ReportsPlus.Windows.Server.CurrentIDViewController.defaultPedImagePath;
import static com.Guess.ReportsPlus.logs.Arrest.ArrestReportUtils.newArrest;
import static com.Guess.ReportsPlus.logs.TrafficCitation.TrafficCitationUtils.newCitation;
import static com.Guess.ReportsPlus.util.History.IDHistory.getHistoryIDFromName;
import static com.Guess.ReportsPlus.util.History.IDHistory.searchIDHisForName;
import static com.Guess.ReportsPlus.util.History.Ped.PedHistoryUtils.findPedByName;
import static com.Guess.ReportsPlus.util.History.Ped.PedHistoryUtils.findPedByNumber;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.*;
import static com.Guess.ReportsPlus.util.Misc.AudioUtil.playSound;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationError;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.*;
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
	private ScrollPane pedPane;
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
		noPedImageFoundlbl.setText(localization.getLocalizedMessage("PedLookup.NoPedImageFoundlbl", "No Image Found In System"));
		noRecordFoundLabelPed.setText(localization.getLocalizedMessage("PedLookup.NoPedFoundInSystem", "No Record Found In System"));
		
		addDataToNotesBtn.setText(localization.getLocalizedMessage("PedLookup.AddDataToNotesButton", "Add Data To Notes"));
		pedSearchBtn.setText(localization.getLocalizedMessage("PedLookup.SearchPedButton", "Search"));
		infobtn3.setText(localization.getLocalizedMessage("PedLookup.UpdateOtherInfoButton", "Update Other Information"));
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
		ped18.setText(localization.getLocalizedMessage("PedLookup.FieldFishingLicenseStatus", "Fishing License Status"));
		ped19.setText(localization.getLocalizedMessage("PedLookup.FieldBoatingLicenseStatus", "Boating License Status"));
		ped20.setText(localization.getLocalizedMessage("PedLookup.FieldHuntingLicenseStatus", "Hunting License Status"));
		ped23.setText(localization.getLocalizedMessage("PedLookup.FieldFlags", "Flag(s):"));
	}
	
	private void processPedData(boolean owner, String name_value, String licenseNumber_value, String modelName_value, String birthday_value, String gender_value, String address_value, String isWanted_value, String licenseStatus_value, String licenseExp_value, String weaponPermitType_value, String weaponPermitStatus_value, String weaponPermitExpiration_value, String fishPermitStatus_value, String fishPermitExpiration_value, String timesStopped_value, String huntPermitStatus_value, String huntPermitExpiration_value, String isOnParole_value, String isOnProbation_value) {
		Optional<Ped> searchedPed;
		if (owner) {
			searchedPed = findPedByName(name_value);
		} else {
			searchedPed = findPedByNumber(licenseNumber_value);
		}
		
		Ped searchedPedObject = searchedPed.orElseGet(() -> {
			Ped ped = new Ped();
			if (name_value != null) {
				ped.setName(name_value);
			} else {
				log("ProcessPedData; name_value was null, set as ERROR", LogUtils.Severity.WARN);
				ped.setName("ERROR");
			}
			if (licenseNumber_value != null) {
				ped.setLicenseNumber(licenseNumber_value);
			} else {
				log("ProcessPedData; licenseNumber_value was null, generating", LogUtils.Severity.WARN);
				ped.setLicenseNumber(generateLicenseNumber());
			}
			if (gender_value != null) {
				ped.setGender(gender_value);
			} else {
				log("ProcessPedData; gender_value was null, generating", LogUtils.Severity.WARN);
				ped.setGender(calculateTrueFalseProbability("50") ? "Male" : "Female");
			}
			if (birthday_value != null) {
				ped.setBirthday(birthday_value);
			} else {
				log("ProcessPedData; birthday_value was null, generating", LogUtils.Severity.WARN);
				ped.setBirthday(generateBirthday(23, 65));
			}
			if (address_value != null) {
				ped.setAddress(address_value);
			} else {
				log("ProcessPedData; address_value was null, generating", LogUtils.Severity.WARN);
				ped.setAddress(getRandomAddress());
			}
			if (isWanted_value != null) {
				ped.setWantedStatus(isWanted_value);
			} else {
				log("ProcessPedData; isWanted_value was null, generating", LogUtils.Severity.WARN);
				ped.setWantedStatus(calculateTrueFalseProbability("15") ? "true" : "false");
			}
			if (licenseStatus_value != null) {
				ped.setLicenseStatus(licenseStatus_value);
			} else {
				log("ProcessPedData; licenseStatus_value was null, generating", LogUtils.Severity.WARN);
				ped.setLicenseStatus(calculateLicenseStatus(55, 22, 23));
			}
			
			try {
				if (ped.getWantedStatus().equalsIgnoreCase("true")) {
					try {
						String warrant = null;
						try {
							warrant = getRandomChargeWithWarrant(URLStrings.chargesFilePath);
						} catch (IOException e) {
							logError("ProcessPedData; Error getting randomCharge: ", e);
						}
						if (warrant != null) {
							String department = getRandomDepartment();
							String number = generateLicenseNumber();
							String issuedDate = generateExpiredLicenseExpirationDate(5);
							ped.setOutstandingWarrants(warrant);
							ped.setWarrantAgency(department);
							ped.setWarrantNumber(number);
							ped.setDateWarrantIssued(issuedDate);
						} else {
							ped.setOutstandingWarrants("WANTED - No details");
						}
					} catch (ParserConfigurationException | SAXException e) {
						logError("ProcessPedData; Error getting random charge: ", e);
						ped.setOutstandingWarrants("WANTED - Error retrieving details");
					}
				}
			} catch (Exception e) {
				logError("Could not set warrantStatus: ", e);
			}
			
			try {
				int totalChargePriors = 0;
				try {
					totalChargePriors = setArrestPriors(ped);
				} catch (IOException e) {
					logError("Could not fetch arrestPriors: ", e);
				}
				int totalCitationPriors = 0;
				try {
					totalCitationPriors = setCitationPriors(ped);
				} catch (IOException e) {
					logError("Could not fetch citationPriors: ", e);
				}
				
				if (totalChargePriors >= 1) {
					try {
						ped.setParoleStatus(String.valueOf(calculateTrueFalseProbability(ConfigReader.configRead("pedHistory", "onParoleChance"))));
					} catch (IOException e) {
						logError("Could not set ParoleStatus: ", e);
					}
					try {
						ped.setProbationStatus(String.valueOf(calculateTrueFalseProbability(ConfigReader.configRead("pedHistory", "onProbationChance"))));
					} catch (IOException e) {
						logError("Could not set ProbationStatus: ", e);
					}
				}
				
				//BUG: not using timesStopped since it relies on chargepriors and citationpriors
				String totalStops = String.valueOf(calculateTotalStops(totalChargePriors + totalCitationPriors));
				ped.setTimesStopped(totalStops);
				
				int baseFlagFactor = 5;
				try {
					baseFlagFactor = Integer.parseInt(ConfigReader.configRead("pedHistory", "baseFlagProbability"));
				} catch (IOException e) {
					logError("Could not fetch baseFlagFactor: ", e);
				}
				
				String flags = assignFlagsBasedOnPriors(totalChargePriors, baseFlagFactor, 0.9, 2);
				
				if (flags != null && flags.length() > 0 && !flags.equals("")) {
					ped.setFlags(flags);
				}
				
			} catch (Exception e) {
				logError("Could not set priors: ", e);
			}
			
			try {
				if (fishPermitStatus_value != null && !fishPermitStatus_value.equalsIgnoreCase("not found")) {
					ped.setFishingLicenseStatus(fishPermitStatus_value);
				} else {
					log("ProcessPedData; fishPermitStatus_value was null, generating", LogUtils.Severity.WARN);
					boolean hasFishingLicense = calculateTrueFalseProbability(ConfigReader.configRead("pedHistory", "hasFishingLicense"));
					
					if (hasFishingLicense) {
						String licstatus = calculateLicenseStatus(Integer.parseInt(ConfigReader.configRead("pedHistory", "validLicenseChance")), Integer.parseInt(ConfigReader.configRead("pedHistory", "expiredLicenseChance")), Integer.parseInt(ConfigReader.configRead("pedHistory", "suspendedLicenseChance")));
						ped.setFishingLicenseStatus(licstatus);
						
						log("ProcessPedData; generated fishingLicenseNumber", LogUtils.Severity.WARN);
						ped.setFishingLicenseNumber(generateLicenseNumber());
					} else {
						log("ProcessPedData; hasFishingLicense was false, set FishingLicenseStatus None", LogUtils.Severity.WARN);
						ped.setFishingLicenseStatus("None");
						ped.setFishingLicenseNumber("None");
						ped.setFishingLicenseExpiration("None");
					}
				}
				
				if (fishPermitExpiration_value != null && !fishPermitExpiration_value.equalsIgnoreCase("not found")) {
					ped.setFishingLicenseExpiration(fishPermitExpiration_value);
				} else {
					log("ProcessPedData; fishPermitExpiration_value was null, generating expiration", LogUtils.Severity.WARN);
					if (ped.getFishingLicenseStatus().equalsIgnoreCase("valid")) {
						ped.setFishingLicenseExpiration(generateValidLicenseExpirationDate());
					} else if (ped.getFishingLicenseStatus().equalsIgnoreCase("suspended")) {
						ped.setFishingLicenseExpiration("Suspended License");
					} else if (ped.getFishingLicenseStatus().equalsIgnoreCase("revoked")) {
						ped.setFishingLicenseExpiration("Revoked License");
					} else if (ped.getFishingLicenseStatus().equalsIgnoreCase("expired")) {
						ped.setFishingLicenseExpiration(generateExpiredLicenseExpirationDate(3));
					} else {
						ped.setFishingLicenseExpiration("None");
					}
				}
				
			} catch (IOException e) {
				logError("Could not set fishingLicenseStatus: ", e);
			}
			
			try {
				if (ped.getBoatingLicenseStatus() == null || ped.getBoatingLicenseStatus().isEmpty()) {
					log("ProcessPedData; boating lic status is null, generating status/exp/lic#", LogUtils.Severity.WARN);
					boolean boatLicStatus = calculateTrueFalseProbability(ConfigReader.configRead("pedHistory", "hasBoatingLicense"));
					if (boatLicStatus) {
						String licstatus = calculateLicenseStatus(Integer.parseInt(ConfigReader.configRead("pedHistory", "validLicenseChance")), Integer.parseInt(ConfigReader.configRead("pedHistory", "expiredLicenseChance")), Integer.parseInt(ConfigReader.configRead("pedHistory", "suspendedLicenseChance")));
						
						if (licstatus.equalsIgnoreCase("suspended")) {
							ped.setBoatingLicenseExpiration("Suspended License");
						} else if (licstatus.equalsIgnoreCase("expired")) {
							ped.setBoatingLicenseExpiration(generateExpiredLicenseExpirationDate(3));
						} else {
							ped.setBoatingLicenseExpiration(generateValidLicenseExpirationDate());
						}
						
						ped.setBoatingLicenseStatus(licstatus);
						ped.setBoatingLicenseNumber(generateLicenseNumber());
					} else {
						ped.setBoatingLicenseStatus("None");
						ped.setBoatingLicenseNumber("None");
						ped.setBoatingLicenseExpiration("None");
					}
				}
			} catch (IOException e) {
				logError("Could not set boatingLicenseStatus: ", e);
			}
			
			try {
				if (modelName_value != null) {
					ped.setModel(modelName_value);
				} else {
					log("ProcessPedData; modelName_value is null, generating from gender value: " + ped.getGender(), LogUtils.Severity.WARN);
					
					ArrayList<String> maleModels = new ArrayList<>(Arrays.asList("[ig_zimbor][0][0]", "[mp_m_weed_01][0][0]", "[s_m_m_bouncer_01][0][0]", "[s_m_m_postal_02][0][0]", "[s_m_y_waretech_01][0][0]", "[a_m_m_eastsa_01][0][0]"));
					
					ArrayList<String> femaleModels = new ArrayList<>(Arrays.asList("[a_f_m_bevhills_02][0][0]", "[a_f_y_femaleagent][0][0]", "[a_f_y_soucent_02][0][0]", "[csb_mrs_r][0][0]", "[mp_f_counterfeit_01][0][0]", "[mp_f_cardesign_01][0][0]"));
					
					if (ped.getGender() != null) {
						Random random = new Random();
						if (ped.getGender().equalsIgnoreCase("female")) {
							String model = femaleModels.get(random.nextInt(femaleModels.size()));
							log("ProcessPedData; Generated new Female model [" + model + "]", LogUtils.Severity.WARN);
							ped.setModel(model);
						} else if (ped.getGender().equalsIgnoreCase("male")) {
							String model = maleModels.get(random.nextInt(maleModels.size()));
							log("ProcessPedData; Generated new Male model [" + model + "]", LogUtils.Severity.WARN);
							ped.setModel(model);
						} else {
							log("ProcessPedData; Set model as 'Not Found'", LogUtils.Severity.ERROR);
							ped.setModel("Not Found");
						}
					} else {
						log("ProcessPedData; Set model as 'Not Found' [2]", LogUtils.Severity.ERROR);
						ped.setModel("Not Found");
					}
				}
			} catch (Exception e) {
				logError("Could not set model: ", e);
			}
			
			try {
				if (fishPermitStatus_value != null) {
					ped.setFishingLicenseStatus(fishPermitStatus_value);
				} else {
					log("ProcessPedData; fishPermitStatus_value was null, generating", LogUtils.Severity.WARN);
					boolean hasFishingLicense = calculateTrueFalseProbability(ConfigReader.configRead("pedHistory", "hasFishingLicense"));
					
					if (hasFishingLicense) {
						String licstatus = calculateLicenseStatus(Integer.parseInt(ConfigReader.configRead("pedHistory", "validLicenseChance")), Integer.parseInt(ConfigReader.configRead("pedHistory", "expiredLicenseChance")), Integer.parseInt(ConfigReader.configRead("pedHistory", "suspendedLicenseChance")));
						ped.setFishingLicenseStatus(licstatus);
					} else {
						log("ProcessPedData; hasFishingLicense was false, set FishingLicenseStatus None", LogUtils.Severity.WARN);
						ped.setFishingLicenseStatus("None");
					}
				}
				
				if (fishPermitExpiration_value != null) {
					ped.setFishingLicenseExpiration(fishPermitExpiration_value);
				} else {
					log("ProcessPedData; fishPermitExpiration_value was null, generating expiration", LogUtils.Severity.WARN);
					if (ped.getFishingLicenseStatus() != null) {
						if (ped.getFishingLicenseStatus().equalsIgnoreCase("valid")) {
							ped.setFishingLicenseExpiration(generateValidLicenseExpirationDate());
						} else if (ped.getFishingLicenseStatus().equalsIgnoreCase("suspended")) {
							ped.setFishingLicenseExpiration("Suspended License");
						} else if (ped.getFishingLicenseStatus().equalsIgnoreCase("revoked")) {
							ped.setFishingLicenseExpiration("Revoked License");
						} else if (ped.getFishingLicenseStatus().equalsIgnoreCase("expired")) {
							ped.setFishingLicenseExpiration(generateExpiredLicenseExpirationDate(3));
						} else {
							ped.setFishingLicenseExpiration("None");
						}
					} else {
						ped.setFishingLicenseExpiration("None");
					}
				}
				
				if (ped.getFishingLicenseNumber() == null || ped.getFishingLicenseNumber().isEmpty()) {
					log("ProcessPedData; generated fishingLicenseNumber", LogUtils.Severity.WARN);
					ped.setFishingLicenseNumber(generateLicenseNumber());
				}
				
			} catch (IOException e) {
				logError("Could not set fishingLicenseStatus: ", e);
			}
			
			try {
				if (weaponPermitStatus_value != null) {
					ped.setGunLicenseStatus(weaponPermitStatus_value);
				} else {
					log("ProcessPedData; weaponPermitStatus_value was null, generating", LogUtils.Severity.WARN);
					Boolean hasGunLicense = calculateTrueFalseProbability(ConfigReader.configRead("pedHistoryGunPermit", "hasGunLicense"));
					
					if (hasGunLicense) {
						String gunlicstatus = calculateLicenseStatus(Integer.parseInt(ConfigReader.configRead("pedHistory", "validLicenseChance")), Integer.parseInt(ConfigReader.configRead("pedHistory", "expiredLicenseChance")), Integer.parseInt(ConfigReader.configRead("pedHistory", "suspendedLicenseChance")));
						ped.setGunLicenseStatus(gunlicstatus);
					} else {
						log("ProcessPedData; hasGunLicense was false, set GunLicenseStatus None", LogUtils.Severity.WARN);
						ped.setGunLicenseStatus("None");
						ped.setGunLicenseExpiration("None");
						ped.setGunLicenseNumber("None");
						ped.setGunLicenseClass("None");
						ped.setGunLicenseType("None");
					}
				}
				
				if (weaponPermitExpiration_value != null) {
					ped.setGunLicenseExpiration(weaponPermitExpiration_value);
				} else {
					log("ProcessPedData; weaponPermitExpiration_value was null, generating expiration", LogUtils.Severity.WARN);
					
					if (ped.getGunLicenseStatus().equalsIgnoreCase("valid") || ped.getGunLicenseStatus().equalsIgnoreCase("expired") || ped.getGunLicenseStatus().equalsIgnoreCase("suspended") || ped.getGunLicenseStatus().equalsIgnoreCase("revoked")) {
						
						if (ped.getGunLicenseStatus().equalsIgnoreCase("valid")) {
							ped.setGunLicenseExpiration(generateValidLicenseExpirationDate());
							if (ped.getGunLicenseNumber() == null || ped.getGunLicenseNumber().isEmpty()) {
								log("ProcessPedData; generated gunLicenseNumber", LogUtils.Severity.WARN);
								ped.setGunLicenseNumber(generateLicenseNumber());
							}
						} else if (ped.getGunLicenseStatus().equalsIgnoreCase("suspended")) {
							ped.setGunLicenseExpiration("Suspended License");
							if (ped.getGunLicenseNumber() == null || ped.getGunLicenseNumber().isEmpty()) {
								log("ProcessPedData; generated gunLicenseNumber", LogUtils.Severity.WARN);
								ped.setGunLicenseNumber(generateLicenseNumber());
							}
						} else if (ped.getGunLicenseStatus().equalsIgnoreCase("revoked")) {
							ped.setGunLicenseExpiration("Revoked License");
							if (ped.getGunLicenseNumber() == null || ped.getGunLicenseNumber().isEmpty()) {
								log("ProcessPedData; generated gunLicenseNumber", LogUtils.Severity.WARN);
								ped.setGunLicenseNumber(generateLicenseNumber());
							}
						} else if (ped.getGunLicenseStatus().equalsIgnoreCase("expired")) {
							ped.setGunLicenseExpiration(generateExpiredLicenseExpirationDate(3));
						} else {
							ped.setGunLicenseExpiration("None");
						}
					}
				}
				
				if (weaponPermitType_value != null) {
					ped.setGunLicenseType(weaponPermitType_value);
				} else {
					log("ProcessPedData; weaponPermitType_value is null, setting type 'None'", LogUtils.Severity.WARN);
					ped.setGunLicenseType("None");
				}
				
				if (ped.getGunLicenseStatus().equalsIgnoreCase("valid") || ped.getGunLicenseStatus().equalsIgnoreCase("expired") || ped.getGunLicenseStatus().equalsIgnoreCase("revoked") || ped.getGunLicenseStatus().equalsIgnoreCase("suspended")) {
					log("ProcessPedData; weaponPermitType_value was null, generating type/class/number", LogUtils.Severity.WARN);
					String licclass = getGunLicenseClass();
					String number = generateLicenseNumber();
					ped.setGunLicenseClass(licclass);
					ped.setGunLicenseNumber(number);
				}
				
				if (huntPermitStatus_value != null) {
					ped.setHuntingLicenseStatus(huntPermitStatus_value);
				} else {
					log("ProcessPedData; huntPermitStatus_value was null, generating", LogUtils.Severity.WARN);
					boolean huntlic = calculateTrueFalseProbability(ConfigReader.configRead("pedHistory", "hasHuntingLicense"));
					
					if (huntlic) {
						String licstatus = calculateLicenseStatus(Integer.parseInt(ConfigReader.configRead("pedHistory", "validLicenseChance")), Integer.parseInt(ConfigReader.configRead("pedHistory", "expiredLicenseChance")), Integer.parseInt(ConfigReader.configRead("pedHistory", "suspendedLicenseChance")));
						ped.setHuntingLicenseStatus(licstatus);
					} else {
						log("ProcessPedData; huntlic was false, set HuntingLicenseStatus None", LogUtils.Severity.WARN);
						ped.setHuntingLicenseStatus("None");
						ped.setHuntingLicenseNumber("None");
						ped.setHuntingLicenseExpiration("None");
					}
				}
				
				if (huntPermitExpiration_value != null) {
					ped.setHuntingLicenseExpiration(huntPermitExpiration_value);
				} else {
					log("ProcessPedData; huntPermitExpiration_value was null, generating expiration", LogUtils.Severity.WARN);
					if (ped.getHuntingLicenseStatus().equalsIgnoreCase("valid") || ped.getHuntingLicenseStatus().equalsIgnoreCase("expired") || ped.getHuntingLicenseStatus().equalsIgnoreCase("suspended") || ped.getHuntingLicenseStatus().equalsIgnoreCase("revoked")) {
						
						if (ped.getHuntingLicenseStatus().equalsIgnoreCase("valid")) {
							ped.setHuntingLicenseExpiration(generateValidLicenseExpirationDate());
							if (ped.getHuntingLicenseNumber() == null || ped.getHuntingLicenseNumber().isEmpty()) {
								log("ProcessPedData; generated HuntingLicenseNumber", LogUtils.Severity.WARN);
								ped.setHuntingLicenseNumber(generateLicenseNumber());
							}
						} else if (ped.getHuntingLicenseStatus().equalsIgnoreCase("suspended")) {
							ped.setHuntingLicenseExpiration("Suspended License");
							if (ped.getHuntingLicenseNumber() == null || ped.getHuntingLicenseNumber().isEmpty()) {
								log("ProcessPedData; generated HuntingLicenseNumber", LogUtils.Severity.WARN);
								ped.setHuntingLicenseNumber(generateLicenseNumber());
							}
						} else if (ped.getHuntingLicenseStatus().equalsIgnoreCase("revoked")) {
							ped.setHuntingLicenseExpiration("Revoked License");
							if (ped.getHuntingLicenseNumber() == null || ped.getHuntingLicenseNumber().isEmpty()) {
								log("ProcessPedData; generated HuntingLicenseNumber", LogUtils.Severity.WARN);
								ped.setHuntingLicenseNumber(generateLicenseNumber());
							}
						} else if (ped.getHuntingLicenseStatus().equalsIgnoreCase("expired")) {
							ped.setHuntingLicenseExpiration(generateExpiredLicenseExpirationDate(3));
						} else {
							ped.setHuntingLicenseExpiration("None");
						}
					}
				}
				
			} catch (IOException e) {
				logError("Could not set gunLicenseStatus: ", e);
			}
			
			try {
				Ped.PedHistoryUtils.addPed(ped);
			} catch (JAXBException e) {
				logError("Error adding ped to PedHistory: ", e);
			}
			
			return ped;
		});
		
		if (searchedPedObject != null) {
			if (setPedRecordFields(searchedPedObject)) {
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
		if (ped.getLicenseStatus().equalsIgnoreCase("EXPIRED") || ped.getLicenseStatus().equalsIgnoreCase("SUSPENDED") || ped.getLicenseStatus().equalsIgnoreCase("REVOKED") || ped.getLicenseStatus().equalsIgnoreCase("NONE") || ped.getLicenseStatus().equalsIgnoreCase("UNLICENSED")) {
			pedlicensefield.setStyle("-fx-text-fill: red !important;");
			playAudio = true;
		} else if (ped.getLicenseStatus().equalsIgnoreCase("VALID")) {
			pedlicensefield.setStyle("-fx-text-fill: #060 !important;");
		}
		
		pedwantedfield.getStyleClass().clear();
		pedwantedfield.setOnMouseClicked(null);
		if (ped.getOutstandingWarrants() != null) {
			pedwantedfield.setStyle("-fx-text-fill: red !important;");
			playAudio = true;
			pedwantedfield.setText("WARRANT");
			
			boolean updated = false;
			if (ped.getDateWarrantIssued() == null) {
				ped.setDateWarrantIssued(generateExpiredLicenseExpirationDate(5));
				updated = true;
			}
			if (ped.getWarrantNumber() == null) {
				ped.setWarrantNumber(generateLicenseNumber());
				updated = true;
			}
			if (ped.getWarrantAgency() == null) {
				ped.setWarrantAgency(getRandomDepartment());
				updated = true;
			}
			if (updated) {
				try {
					Ped.PedHistoryUtils.addPed(ped);
				} catch (JAXBException e) {
					logError("Error updating Ped for warrant license info: ", e);
				}
			}
			pedwantedfield.getStyleClass().add("valid-field");
			
			createWarrantInfoPopup(pedwantedfield, localization.getLocalizedMessage("PedLookup.WarrantInformationTitle", "Issued Warrant Information:"), ped.getName(), ped.getBirthday(), ped.getDateWarrantIssued(), ped.getWarrantNumber(), ped.getWarrantAgency(), ped.getOutstandingWarrants());
		} else {
			pedwantedfield.setText("False");
			pedwantedfield.getStyleClass().add("text-field");
			pedwantedfield.setStyle("-fx-text-fill: black !important;");
		}
		
		pedgunlicensestatusfield.getStyleClass().clear();
		pedgunlicensestatusfield.setOnMouseClicked(null);
		pedgunlicensestatusfield.setText(ped.getGunLicenseStatus());
		if (ped.getGunLicenseStatus() == null || ped.getGunLicenseStatus().equalsIgnoreCase("false") || ped.getGunLicenseStatus().equalsIgnoreCase("Not Found") || ped.getGunLicenseStatus().equalsIgnoreCase("None")) {
			pedgunlicensestatusfield.setText("False");
			pedgunlicensestatusfield.getStyleClass().add("text-field");
			pedgunlicensestatusfield.setStyle("-fx-text-fill: black !important;");
		} else if (ped.getGunLicenseStatus().equalsIgnoreCase("suspended") || ped.getGunLicenseStatus().equalsIgnoreCase("revoked") || ped.getGunLicenseStatus().equalsIgnoreCase("expired")) {
			pedgunlicensestatusfield.getStyleClass().add("valid-field");
			pedgunlicensestatusfield.setStyle("-fx-text-fill: orange !important;");
			pedgunlicensestatusfield.setText(ped.getGunLicenseStatus().toUpperCase());
			
			createGunLicenseInfoPopup(pedgunlicensestatusfield, localization.getLocalizedMessage("PedLookup.GunLicenseInfoTitle", "Gun License Information:"), ped.getName(), ped.getBirthday(), ped.getGunLicenseExpiration(), ped.getGunLicenseStatus(), ped.getGunLicenseNumber(), ped.getGunLicenseType(), ped.getGunLicenseClass());
			
		} else if (ped.getGunLicenseStatus().equalsIgnoreCase("valid")) {
			pedgunlicensestatusfield.getStyleClass().add("valid-field");
			pedgunlicensestatusfield.setStyle("-fx-text-fill: #060 !important;");
			pedgunlicensestatusfield.setText("Valid");
			
			boolean updated = false;
			if (ped.getGunLicenseExpiration() == null) {
				ped.setGunLicenseExpiration(generateValidLicenseExpirationDate());
				updated = true;
			}
			if (ped.getGunLicenseNumber() == null) {
				ped.setGunLicenseNumber(generateLicenseNumber());
				updated = true;
			}
			if (updated) {
				try {
					Ped.PedHistoryUtils.addPed(ped);
				} catch (JAXBException e) {
					logError("Error updating Ped for gun license info 2: ", e);
				}
			}
			createGunLicenseInfoPopup(pedgunlicensestatusfield, localization.getLocalizedMessage("PedLookup.GunLicenseInfoTitle", "Gun License Information:"), ped.getName(), ped.getBirthday(), ped.getGunLicenseExpiration(), ped.getGunLicenseStatus(), ped.getGunLicenseNumber(), ped.getGunLicenseType(), ped.getGunLicenseClass());
			
		}
		
		pedprobationstatusfield.setText(ped.getProbationStatus() != null ? ped.getProbationStatus() : "False");
		if (ped.getProbationStatus() != null && ped.getProbationStatus().equalsIgnoreCase("true")) {
			pedprobationstatusfield.setStyle("-fx-text-fill: red !important;");
			pedprobationstatusfield.setText("On Probation");
		} else {
			pedprobationstatusfield.setStyle("-fx-text-fill: black !important;");
		}
		
		pedfishinglicstatusfield.getStyleClass().clear();
		pedfishinglicstatusfield.setOnMouseClicked(null);
		pedfishinglicstatusfield.setText(ped.getFishingLicenseStatus());
		if (ped.getFishingLicenseStatus() == null || ped.getFishingLicenseStatus().equalsIgnoreCase("false") || ped.getFishingLicenseStatus().equalsIgnoreCase("Not Found") || ped.getFishingLicenseStatus().equalsIgnoreCase("None")) {
			pedfishinglicstatusfield.setText("False");
			pedfishinglicstatusfield.getStyleClass().add("text-field");
			pedfishinglicstatusfield.setStyle("-fx-text-fill: black !important;");
		} else if (ped.getFishingLicenseStatus().equalsIgnoreCase("suspended") || ped.getFishingLicenseStatus().equalsIgnoreCase("expired") || ped.getFishingLicenseStatus().equalsIgnoreCase("revoked")) {
			pedfishinglicstatusfield.getStyleClass().add("valid-field");
			pedfishinglicstatusfield.setStyle("-fx-text-fill: orange !important;");
			pedfishinglicstatusfield.setText(ped.getFishingLicenseStatus().toUpperCase());
			
			createLicenseInfoPopup(pedfishinglicstatusfield, localization.getLocalizedMessage("PedLookup.FishLicenseInfoTitle", "Fishing License Information:"), ped.getName(), ped.getBirthday(), ped.getFishingLicenseExpiration(), ped.getFishingLicenseStatus(), ped.getFishingLicenseNumber());
			
		} else if (ped.getFishingLicenseStatus().equalsIgnoreCase("valid")) {
			pedfishinglicstatusfield.getStyleClass().add("valid-field");
			pedfishinglicstatusfield.setStyle("-fx-text-fill: #060 !important;");
			pedfishinglicstatusfield.setText("Valid");
			
			boolean updated = false;
			if (ped.getFishingLicenseExpiration() == null) {
				ped.setFishingLicenseExpiration(generateValidLicenseExpirationDate());
				updated = true;
			}
			if (ped.getFishingLicenseNumber() == null) {
				ped.setFishingLicenseNumber(generateLicenseNumber());
				updated = true;
			}
			if (updated) {
				try {
					Ped.PedHistoryUtils.addPed(ped);
				} catch (JAXBException e) {
					logError("Error updating Ped for fishing license info 2: ", e);
				}
			}
			createLicenseInfoPopup(pedfishinglicstatusfield, localization.getLocalizedMessage("PedLookup.FishLicenseInfoTitle", "Fishing License Information:"), ped.getName(), ped.getBirthday(), ped.getFishingLicenseExpiration(), ped.getFishingLicenseStatus(), ped.getFishingLicenseNumber());
			
		} else {
			log("Unexpected fishing license status: " + ped.getFishingLicenseStatus(), LogUtils.Severity.ERROR);
			showNotificationError("Ped Lookup", "Unexpected fishing license status: " + ped.getFishingLicenseStatus());
			
			pedfishinglicstatusfield.setText("Unknown");
			pedfishinglicstatusfield.getStyleClass().add("text-field");
			pedfishinglicstatusfield.setStyle("-fx-text-fill: red !important;");
		}
		
		pedboatinglicstatusfield.getStyleClass().clear();
		pedboatinglicstatusfield.setOnMouseClicked(null);
		if (ped.getBoatingLicenseStatus() == null || ped.getBoatingLicenseStatus().equalsIgnoreCase("Not Found") || ped.getBoatingLicenseStatus().equalsIgnoreCase("None")) {
			pedboatinglicstatusfield.setText("False");
			pedboatinglicstatusfield.getStyleClass().add("text-field");
			pedboatinglicstatusfield.setStyle("-fx-text-fill: black !important;");
		} else if (ped.getBoatingLicenseStatus().equalsIgnoreCase("suspended") || ped.getBoatingLicenseStatus().equalsIgnoreCase("expired") || ped.getBoatingLicenseStatus().equalsIgnoreCase("revoked")) {
			pedboatinglicstatusfield.getStyleClass().add("valid-field");
			pedboatinglicstatusfield.setStyle("-fx-text-fill: orange !important;");
			pedboatinglicstatusfield.setText(ped.getBoatingLicenseStatus().toUpperCase());
			
			createLicenseInfoPopup(pedboatinglicstatusfield, localization.getLocalizedMessage("PedLookup.BoatLicenseInfoTitle", "Boating License Information:"), ped.getName(), ped.getBirthday(), ped.getBoatingLicenseExpiration(), ped.getBoatingLicenseStatus(), ped.getBoatingLicenseNumber());
			
		} else if (ped.getBoatingLicenseStatus().equalsIgnoreCase("valid")) {
			pedboatinglicstatusfield.getStyleClass().add("valid-field");
			pedboatinglicstatusfield.setStyle("-fx-text-fill: #060 !important;");
			pedboatinglicstatusfield.setText("Valid");
			
			boolean updated = false;
			if (ped.getBoatingLicenseExpiration() == null) {
				ped.setBoatingLicenseExpiration(generateValidLicenseExpirationDate());
				updated = true;
			}
			if (ped.getBoatingLicenseNumber() == null) {
				ped.setBoatingLicenseNumber(generateLicenseNumber());
				updated = true;
			}
			if (updated) {
				try {
					Ped.PedHistoryUtils.addPed(ped);
				} catch (JAXBException e) {
					logError("Error updating Ped for boating license info 2: ", e);
				}
			}
			createLicenseInfoPopup(pedboatinglicstatusfield, localization.getLocalizedMessage("PedLookup.BoatLicenseInfoTitle", "Boating License Information:"), ped.getName(), ped.getBirthday(), ped.getBoatingLicenseExpiration(), ped.getBoatingLicenseStatus(), ped.getBoatingLicenseNumber());
		} else {
			log("Unexpected boating license status: " + ped.getBoatingLicenseStatus(), LogUtils.Severity.ERROR);
			showNotificationError("Ped Lookup", "Unexpected boating license status: " + ped.getBoatingLicenseStatus());
			pedboatinglicstatusfield.setText("Unknown");
			pedboatinglicstatusfield.getStyleClass().add("text-field");
			pedboatinglicstatusfield.setStyle("-fx-text-fill: red !important;");
		}
		
		pedhuntinglicstatusfield.getStyleClass().clear();
		pedhuntinglicstatusfield.setOnMouseClicked(null);
		pedhuntinglicstatusfield.setText(ped.getHuntingLicenseStatus());
		if (ped.getHuntingLicenseStatus() == null || ped.getHuntingLicenseStatus().equalsIgnoreCase("Not Found") || ped.getHuntingLicenseStatus().equalsIgnoreCase("None")) {
			pedhuntinglicstatusfield.setText("False");
			pedhuntinglicstatusfield.getStyleClass().add("text-field");
			pedhuntinglicstatusfield.setStyle("-fx-text-fill: black !important;");
		} else if (ped.getHuntingLicenseStatus().equalsIgnoreCase("suspended") || ped.getHuntingLicenseStatus().equalsIgnoreCase("expired") || ped.getHuntingLicenseStatus().equalsIgnoreCase("revoked")) {
			pedhuntinglicstatusfield.getStyleClass().add("valid-field");
			pedhuntinglicstatusfield.setStyle("-fx-text-fill: orange !important;");
			pedhuntinglicstatusfield.setText(ped.getHuntingLicenseStatus().toUpperCase());
			
			createLicenseInfoPopup(pedhuntinglicstatusfield, localization.getLocalizedMessage("PedLookup.HuntLicenseInfoTitle", "Hunting License Information:"), ped.getName(), ped.getBirthday(), ped.getHuntingLicenseExpiration(), ped.getHuntingLicenseStatus(), ped.getHuntingLicenseNumber());
			
		} else if (ped.getHuntingLicenseStatus().equalsIgnoreCase("valid")) {
			pedhuntinglicstatusfield.getStyleClass().add("valid-field");
			pedhuntinglicstatusfield.setStyle("-fx-text-fill: #060 !important;");
			pedhuntinglicstatusfield.setText("Valid");
			
			boolean updated = false;
			if (ped.getHuntingLicenseExpiration() == null) {
				ped.setHuntingLicenseExpiration(generateValidLicenseExpirationDate());
				updated = true;
			}
			if (ped.getHuntingLicenseNumber() == null) {
				ped.setHuntingLicenseNumber(generateLicenseNumber());
				updated = true;
			}
			if (updated) {
				try {
					Ped.PedHistoryUtils.addPed(ped);
				} catch (JAXBException e) {
					logError("Error updating Ped for hunting license info 2: ", e);
				}
			}
			createLicenseInfoPopup(pedhuntinglicstatusfield, localization.getLocalizedMessage("PedLookup.HuntLicenseInfoTitle", "Hunting License Information:"), ped.getName(), ped.getBirthday(), ped.getHuntingLicenseExpiration(), ped.getHuntingLicenseStatus(), ped.getHuntingLicenseNumber());
		} else {
			log("Unexpected hunting license status: " + ped.getHuntingLicenseStatus(), LogUtils.Severity.ERROR);
			showNotificationError("Ped Lookup", "Unexpected hunting license status: " + ped.getHuntingLicenseStatus());
			pedhuntinglicstatusfield.setText("Unknown");
			pedhuntinglicstatusfield.getStyleClass().add("text-field");
			pedhuntinglicstatusfield.setStyle("-fx-text-fill: red !important;");
		}
		
		pedlicnumfield.setText(ped.getLicenseNumber() != null ? ped.getLicenseNumber() : "No Data In System");
		pedlicnumfield.setStyle(ped.getLicenseNumber() == null ? "-fx-text-fill: #E65C00 !important;" : "-fx-text-fill: black;");
		
		String affiliations = ped.getAffiliations();
		if (affiliations == null || affiliations.equalsIgnoreCase("No Data In System")) {
			pedaffiliationfield.setText("No Data In System");
			pedaffiliationfield.setStyle("-fx-text-fill: #603417 !important;");
		} else {
			pedaffiliationfield.setText(affiliations);
			pedaffiliationfield.setStyle("-fx-text-fill: black !important;");
		}
		
		String flags = ped.getFlags();
		if (flags == null || flags.equalsIgnoreCase("No Data In System")) {
			pedflagfield.setText("No Data In System");
			pedflagfield.setStyle("-fx-text-fill: #603417 !important;");
		} else {
			pedflagfield.setText(flags);
			pedflagfield.setStyle("-fx-text-fill: red !important;");
		}
		
		String description = ped.getDescription();
		if (description == null || description.equalsIgnoreCase("No Data In System")) {
			peddescfield.setText("No Data In System");
			peddescfield.setStyle("-fx-text-fill: #603417 !important;");
		} else {
			peddescfield.setText(description);
			peddescfield.setStyle("-fx-text-fill: black !important;");
		}
		
		String aliases = ped.getAliases();
		if (aliases == null || aliases.equalsIgnoreCase("No Data In System")) {
			pedaliasfield.setText("No Data In System");
			pedaliasfield.setStyle("-fx-text-fill: #603417 !important;");
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
		
		String timesStopped = ped.getTimesStopped();
		if (timesStopped == null || timesStopped.trim().isEmpty()) {
			pedtimesstoppedfield.setText("0");
			pedtimesstoppedfield.setStyle("-fx-text-fill: black;");
		} else {
			try {
				int timesStoppedValue = Integer.parseInt(timesStopped.trim());
				pedtimesstoppedfield.setText(timesStopped);
				pedtimesstoppedfield.setStyle(timesStoppedValue > 0 ? "-fx-text-fill: #E65C00 !important;" : "-fx-text-fill: black;");
			} catch (NumberFormatException e) {
				pedtimesstoppedfield.setText("0");
				pedtimesstoppedfield.setStyle("-fx-text-fill: black;");
			}
		}
		
		ped6.setText("Birthday: (" + calculateAge(ped.getBirthday()) + ")");
		
		String pedModel = ped.getModel();
		if (pedModel != null && !pedModel.equalsIgnoreCase("Not Found")) {
			File pedImgFolder = new File(URLStrings.pedImageFolderURL);
			if (pedImgFolder.exists()) {
				log("Detected pedImage folder..", LogUtils.Severity.DEBUG);
				try {
					if (ConfigReader.configRead("uiSettings", "enablePedVehImages").equalsIgnoreCase("true")) {
						File[] matchingFiles = pedImgFolder.listFiles((dir, name) -> name.equalsIgnoreCase(pedModel + ".jpg"));
						
						if (matchingFiles != null && matchingFiles.length > 0) {
							File matchingFile = matchingFiles[0];
							log("Matching pedImage found: " + matchingFile.getName(), LogUtils.Severity.INFO);
							
							try {
								String fileURI = matchingFile.toURI().toString();
								pedImageView.setImage(new Image(fileURI));
								noPedImageFoundlbl.setVisible(true);
								noPedImageFoundlbl.setText(localization.getLocalizedMessage("PedLookup.PedImageFoundlbl", "Image Found in File:"));
							} catch (Exception e) {
								setDefaultPedImage();
								logError("Could not set ped image: ", e);
							}
						} else {
							log("No matching image found for the model: " + pedModel + ", trying to use base image", LogUtils.Severity.WARN);
							
							Pattern pattern = Pattern.compile("\\[([^\\]]+)\\]");
							Matcher matcher = pattern.matcher(pedModel);
							String fallbackModel;
							
							if (matcher.find()) {
								fallbackModel = "[" + matcher.group(1) + "][0][0]";
								log("Extracted base model: " + fallbackModel, LogUtils.Severity.DEBUG);
								
								File[] fallbackFiles = pedImgFolder.listFiles((dir, name) -> name.equalsIgnoreCase(fallbackModel + ".jpg"));
								if (fallbackFiles != null && fallbackFiles.length > 0) {
									File fallbackFile = fallbackFiles[0];
									log("Using base model image: " + fallbackFile.getName(), LogUtils.Severity.INFO);
									try {
										String fileURI = fallbackFile.toURI().toString();
										pedImageView.setImage(new Image(fileURI));
										noPedImageFoundlbl.setVisible(true);
										noPedImageFoundlbl.setText(localization.getLocalizedMessage("PedLookup.PedImageFoundlbl", "Image Found in File:"));
									} catch (Exception e) {
										setDefaultPedImage();
										logError("Could not set ped image [2]: ", e);
									}
								}
							}
						}
					} else {
						log("enablePedVehImages is disabled in settings so not displaying ped image", LogUtils.Severity.WARN);
						setDefaultPedImage();
					}
				} catch (IOException e) {
					logError("Could not get enablePedVehImages setting from config", e);
				}
			} else {
				setDefaultPedImage();
			}
		} else {
			setDefaultPedImage();
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
	
	private void setDefaultPedImage() {
		Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
		pedImageView.setImage(defImage);
		noPedImageFoundlbl.setVisible(true);
		noPedImageFoundlbl.setText(localization.getLocalizedMessage("PedLookup.NoPedImageFoundlbl", "No Image Found In System"));
	}
	
	public void createWarrantInfoPopup(TextField label, String headerText, String name, String dob, String issuedDate, String warrantNumber, String agency, String warrant) {
		try {
			AnchorPane popupContent = new AnchorPane();
			popupContent.setPrefWidth(Region.USE_COMPUTED_SIZE);
			popupContent.getStylesheets().add(Launcher.class.getResource("/com/Guess/ReportsPlus/css/courtCase/courtCaseCss.css").toExternalForm());
			
			Label titleLabel = new Label(headerText);
			titleLabel.setPadding(new Insets(0, 33, 0, 33));
			titleLabel.setAlignment(Pos.CENTER);
			titleLabel.setPrefHeight(33.0);
			titleLabel.setStyle("-fx-background-color: " + ConfigReader.configRead("uiColors", "accentColor") + ";");
			titleLabel.setTextFill(Paint.valueOf("WHITE"));
			titleLabel.setFont(new Font("Inter 28pt Bold", 17.0));
			AnchorPane.setTopAnchor(titleLabel, 0.0);
			AnchorPane.setLeftAnchor(titleLabel, 0.0);
			AnchorPane.setRightAnchor(titleLabel, 0.0);
			
			ImageView exitBtn = new ImageView(new Image(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/cross.png")));
			exitBtn.setFitHeight(33.0);
			exitBtn.setFitWidth(15.0);
			exitBtn.setPickOnBounds(true);
			exitBtn.setPreserveRatio(true);
			exitBtn.setEffect(new ColorAdjust(0, 0, 1.0, 0));
			AnchorPane.setTopAnchor(exitBtn, 5.0);
			AnchorPane.setRightAnchor(exitBtn, 5.0);
			
			GridPane gridPane = new GridPane();
			gridPane.setPadding(new Insets(3, 10, 10, 10));
			gridPane.setHgap(15.0);
			gridPane.setVgap(3.0);
			AnchorPane.setTopAnchor(gridPane, 33.0);
			AnchorPane.setBottomAnchor(gridPane, 0.0);
			AnchorPane.setLeftAnchor(gridPane, 0.0);
			AnchorPane.setRightAnchor(gridPane, 0.0);
			
			gridPane.getColumnConstraints().addAll(new ColumnConstraints(100, 100, Double.MAX_VALUE, Priority.SOMETIMES, HPos.LEFT, true), new ColumnConstraints(100, 100, Double.MAX_VALUE, Priority.SOMETIMES, HPos.LEFT, true));
			for (int i = 0; i < 6; i++) {
				gridPane.getRowConstraints().add(new RowConstraints());
			}
			
			TextField nameField = new TextField();
			nameField.setEditable(false);
			GridPane.setRowIndex(nameField, 1);
			
			TextField dobField = new TextField();
			dobField.setEditable(false);
			GridPane.setColumnIndex(dobField, 1);
			GridPane.setRowIndex(dobField, 1);
			
			TextField dateIssuedField = new TextField();
			dateIssuedField.setEditable(false);
			GridPane.setRowIndex(dateIssuedField, 3);
			
			TextField warrantNumField = new TextField();
			warrantNumField.setEditable(false);
			GridPane.setColumnIndex(warrantNumField, 1);
			GridPane.setRowIndex(warrantNumField, 3);
			
			TextField agencyField = new TextField();
			agencyField.setEditable(false);
			agencyField.setPrefColumnCount(Integer.MAX_VALUE);
			GridPane.setRowIndex(agencyField, 5);
			GridPane.setColumnSpan(agencyField, 2);
			
			TextField warrantField = new TextField();
			warrantField.setEditable(false);
			warrantField.setPrefColumnCount(Integer.MAX_VALUE);
			GridPane.setRowIndex(warrantField, 7);
			GridPane.setColumnSpan(warrantField, 2);
			
			nameField.setText(name);
			dobField.setText(dob);
			dateIssuedField.setText(issuedDate);
			warrantNumField.setText(warrantNumber);
			agencyField.setText(agency);
			warrantField.setText(warrant);
			
			Label nameLabel = createLabel(localization.getLocalizedMessage("PedLookup.NameLabel", "Name:"));
			Label dobLabel = createLabel(localization.getLocalizedMessage("PedLookup.DateOfBirthLabel", "Date of Birth:"));
			GridPane.setColumnIndex(dobLabel, 1);
			nameLabel.setMinWidth(Region.USE_PREF_SIZE);
			
			Label dateIssuedLabel = createLabel(localization.getLocalizedMessage("PedLookup.DateIssuedLabel", "Date Issued:"));
			GridPane.setRowIndex(dateIssuedLabel, 2);
			dateIssuedLabel.setMinWidth(Region.USE_PREF_SIZE);
			
			Label warrantNumLabel = createLabel(localization.getLocalizedMessage("PedLookup.WarrantNumberLabel", "Warrant Number:"));
			GridPane.setColumnIndex(warrantNumLabel, 1);
			GridPane.setRowIndex(warrantNumLabel, 2);
			warrantNumLabel.setMinWidth(Region.USE_PREF_SIZE);
			
			Label agencyLabel = createLabel(localization.getLocalizedMessage("PedLookup.IssuingAuthorityLabel", "Issuing Authority:"));
			GridPane.setRowIndex(agencyLabel, 4);
			agencyLabel.setMinWidth(Region.USE_PREF_SIZE);
			
			Label warrantLabel = createLabel(localization.getLocalizedMessage("PedLookup.WarrantLabel", "Warrant:"));
			GridPane.setRowIndex(warrantLabel, 6);
			warrantLabel.setMinWidth(Region.USE_PREF_SIZE);
			
			final String UILightColor = "rgb(255,255,255,0.75)";
			final String UIDarkColor = "rgb(0,0,0,0.75)";
			if (ConfigReader.configRead("uiColors", "UIDarkMode").equalsIgnoreCase("true")) {
				nameLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				dobLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				dateIssuedLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				warrantNumLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				agencyLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				warrantLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
			} else {
				nameLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				dobLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				dateIssuedLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				warrantNumLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				agencyLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				warrantLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
			}
			
			gridPane.getChildren().addAll(nameField, dobField, dateIssuedField, warrantNumField, agencyField, nameLabel, dobLabel, dateIssuedLabel, warrantNumLabel, agencyLabel, warrantLabel, warrantField);
			
			popupContent.getChildren().addAll(titleLabel, exitBtn, gridPane);
			
			popupContent.setOnMouseMoved(event -> {
				double x = event.getX();
				boolean isOnRightEdge = x > popupContent.getWidth() - 10;
				
				if (isOnRightEdge) {
					popupContent.setCursor(Cursor.E_RESIZE);
				} else {
					popupContent.setCursor(Cursor.DEFAULT);
				}
			});
			
			popupContent.setOnMouseDragged(event -> {
				double x = event.getX();
				
				double minWidth = 200;
				
				if (popupContent.getCursor() == Cursor.E_RESIZE) {
					popupContent.setPrefWidth(Math.max(x, minWidth));
				}
			});
			
			DropShadow dropShadow = new DropShadow();
			dropShadow.setColor(new Color(0, 0, 0, 0.3));
			dropShadow.setOffsetX(0);
			dropShadow.setOffsetY(0);
			dropShadow.setRadius(15);
			dropShadow.setSpread(0.3);
			popupContent.setEffect(dropShadow);
			popupContent.setStyle("-fx-background-color: " + ConfigReader.configRead("uiColors", "bkgColor") + ";");
			
			Popup popup = new Popup();
			popup.getContent().add(popupContent);
			
			final boolean[] isPopupShown = {false};
			
			exitBtn.setOnMouseClicked(event -> {
				popup.hide();
				isPopupShown[0] = false;
			});
			
			label.setOnMouseClicked(event -> {
				if (isPopupShown[0]) {
					popup.hide();
					isPopupShown[0] = false;
				} else {
					popup.show(label.getScene().getWindow(), -9999, -9999);
					
					double labelScreenX = label.localToScreen(label.getBoundsInLocal()).getMinX();
					double labelScreenY = label.localToScreen(label.getBoundsInLocal()).getMinY();
					double labelWidth = label.getWidth();
					
					double popupX = labelScreenX + (labelWidth / 2) - (popupContent.getWidth() / 2);
					double popupY = labelScreenY - popupContent.getHeight();
					
					popup.setX(popupX);
					popup.setY(popupY - 15);
					
					isPopupShown[0] = true;
				}
			});
			exitBtn.requestFocus();
		} catch (Exception e) {
			logError("Error creating license popup from field " + label.getText() + ": ", e);
		}
	}
	
	public void createLicenseInfoPopup(TextField label, String headerText, String name, String dob, String exp, String status, String licnum) {
		try {
			AnchorPane popupContent = new AnchorPane();
			popupContent.setPrefWidth(Region.USE_COMPUTED_SIZE);
			popupContent.getStylesheets().add(Launcher.class.getResource("/com/Guess/ReportsPlus/css/courtCase/courtCaseCss.css").toExternalForm());
			
			Label titleLabel = new Label(headerText);
			titleLabel.setPadding(new Insets(0, 33, 0, 33));
			titleLabel.setAlignment(Pos.CENTER);
			titleLabel.setPrefHeight(33.0);
			titleLabel.setStyle("-fx-background-color: " + ConfigReader.configRead("uiColors", "accentColor") + ";");
			titleLabel.setTextFill(Paint.valueOf("WHITE"));
			titleLabel.setFont(new Font("Inter 28pt Bold", 17.0));
			AnchorPane.setTopAnchor(titleLabel, 0.0);
			AnchorPane.setLeftAnchor(titleLabel, 0.0);
			AnchorPane.setRightAnchor(titleLabel, 0.0);
			
			ImageView exitBtn = new ImageView(new Image(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/cross.png")));
			exitBtn.setFitHeight(33.0);
			exitBtn.setFitWidth(15.0);
			exitBtn.setPickOnBounds(true);
			exitBtn.setPreserveRatio(true);
			exitBtn.setEffect(new ColorAdjust(0, 0, 1.0, 0));
			AnchorPane.setTopAnchor(exitBtn, 5.0);
			AnchorPane.setRightAnchor(exitBtn, 5.0);
			
			GridPane gridPane = new GridPane();
			gridPane.setPadding(new Insets(3, 10, 10, 10));
			gridPane.setHgap(15.0);
			gridPane.setVgap(3.0);
			AnchorPane.setTopAnchor(gridPane, 33.0);
			AnchorPane.setBottomAnchor(gridPane, 0.0);
			AnchorPane.setLeftAnchor(gridPane, 0.0);
			AnchorPane.setRightAnchor(gridPane, 0.0);
			
			gridPane.getColumnConstraints().addAll(new ColumnConstraints(100, 100, Double.MAX_VALUE, Priority.SOMETIMES, HPos.LEFT, true), new ColumnConstraints(100, 100, Double.MAX_VALUE, Priority.SOMETIMES, HPos.LEFT, true));
			for (int i = 0; i < 6; i++) {
				gridPane.getRowConstraints().add(new RowConstraints());
			}
			
			TextField nameField = new TextField();
			nameField.setEditable(false);
			GridPane.setRowIndex(nameField, 1);
			
			TextField dobField = new TextField();
			dobField.setEditable(false);
			GridPane.setColumnIndex(dobField, 1);
			GridPane.setRowIndex(dobField, 1);
			
			TextField expField = new TextField();
			expField.setEditable(false);
			GridPane.setRowIndex(expField, 3);
			
			TextField statusField = new TextField();
			statusField.setEditable(false);
			GridPane.setColumnIndex(statusField, 1);
			GridPane.setRowIndex(statusField, 3);
			
			TextField licNumField = new TextField();
			licNumField.setEditable(false);
			licNumField.setPrefColumnCount(Integer.MAX_VALUE);
			GridPane.setRowIndex(licNumField, 5);
			GridPane.setColumnSpan(licNumField, 2);
			
			nameField.setText(name);
			dobField.setText(dob);
			expField.setText(exp);
			statusField.setText(status);
			licNumField.setText(licnum);
			
			Label nameLabel = createLabel(localization.getLocalizedMessage("PedLookup.NameLabel", "Name:"));
			Label dobLabel = createLabel(localization.getLocalizedMessage("PedLookup.DateOfBirthLabel", "Date of Birth:"));
			GridPane.setColumnIndex(dobLabel, 1);
			nameLabel.setMinWidth(Region.USE_PREF_SIZE);
			
			Label expDateLabel = createLabel(localization.getLocalizedMessage("PedLookup.ExpDateLabel", "Expiration Date:"));
			GridPane.setRowIndex(expDateLabel, 2);
			expDateLabel.setMinWidth(Region.USE_PREF_SIZE);
			
			Label licStatusLabel = createLabel(localization.getLocalizedMessage("PedLookup.FieldLicenseStatus", "License Status:"));
			GridPane.setColumnIndex(licStatusLabel, 1);
			GridPane.setRowIndex(licStatusLabel, 2);
			licStatusLabel.setMinWidth(Region.USE_PREF_SIZE);
			
			Label licNumLabel = createLabel(localization.getLocalizedMessage("PedLookup.FieldLicenseNumber", "License Number:"));
			GridPane.setRowIndex(licNumLabel, 4);
			licNumLabel.setMinWidth(Region.USE_PREF_SIZE);
			
			final String UILightColor = "rgb(255,255,255,0.75)";
			final String UIDarkColor = "rgb(0,0,0,0.75)";
			if (ConfigReader.configRead("uiColors", "UIDarkMode").equalsIgnoreCase("true")) {
				nameLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				dobLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				expDateLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				licStatusLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				licNumLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
			} else {
				nameLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				dobLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				expDateLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				licStatusLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				licNumLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
			}
			
			gridPane.getChildren().addAll(nameField, dobField, expField, statusField, licNumField, nameLabel, dobLabel, expDateLabel, licStatusLabel, licNumLabel);
			
			popupContent.getChildren().addAll(titleLabel, exitBtn, gridPane);
			
			popupContent.setOnMouseMoved(event -> {
				double x = event.getX();
				boolean isOnRightEdge = x > popupContent.getWidth() - 10;
				
				if (isOnRightEdge) {
					popupContent.setCursor(Cursor.E_RESIZE);
				} else {
					popupContent.setCursor(Cursor.DEFAULT);
				}
			});
			
			popupContent.setOnMouseDragged(event -> {
				double x = event.getX();
				
				double minWidth = 200;
				
				if (popupContent.getCursor() == Cursor.E_RESIZE) {
					popupContent.setPrefWidth(Math.max(x, minWidth));
				}
			});
			
			DropShadow dropShadow = new DropShadow();
			dropShadow.setColor(new Color(0, 0, 0, 0.3));
			dropShadow.setOffsetX(0);
			dropShadow.setOffsetY(0);
			dropShadow.setRadius(15);
			dropShadow.setSpread(0.3);
			popupContent.setEffect(dropShadow);
			popupContent.setStyle("-fx-background-color: " + ConfigReader.configRead("uiColors", "bkgColor") + ";");
			
			Popup popup = new Popup();
			popup.getContent().add(popupContent);
			
			final boolean[] isPopupShown = {false};
			
			exitBtn.setOnMouseClicked(event -> {
				popup.hide();
				isPopupShown[0] = false;
			});
			
			label.setOnMouseClicked(event -> {
				if (isPopupShown[0]) {
					popup.hide();
					isPopupShown[0] = false;
				} else {
					popup.show(label.getScene().getWindow(), -9999, -9999);
					
					double labelScreenX = label.localToScreen(label.getBoundsInLocal()).getMinX();
					double labelScreenY = label.localToScreen(label.getBoundsInLocal()).getMinY();
					double labelWidth = label.getWidth();
					
					double popupX = labelScreenX + (labelWidth / 2) - (popupContent.getWidth() / 2);
					double popupY = labelScreenY - popupContent.getHeight();
					
					popup.setX(popupX);
					popup.setY(popupY - 15);
					
					isPopupShown[0] = true;
				}
			});
			exitBtn.requestFocus();
		} catch (Exception e) {
			logError("Error creating license popup from field " + label.getText() + ": ", e);
		}
	}
	
	public void createGunLicenseInfoPopup(TextField label, String headerText, String name, String dob, String exp, String status, String licnum, String gunLicType, String gunLicClass) {
		try {
			AnchorPane popupContent = new AnchorPane();
			popupContent.setPrefWidth(Region.USE_COMPUTED_SIZE);
			popupContent.getStylesheets().add(Launcher.class.getResource("/com/Guess/ReportsPlus/css/lookups/lookup.css").toExternalForm());
			
			Label titleLabel = new Label(headerText);
			titleLabel.setPadding(new Insets(0, 50, 0, 50));
			titleLabel.setAlignment(Pos.CENTER);
			titleLabel.setPrefHeight(33.0);
			titleLabel.setStyle("-fx-background-color: " + ConfigReader.configRead("uiColors", "accentColor") + ";");
			titleLabel.setTextFill(Paint.valueOf("WHITE"));
			titleLabel.setFont(new Font("Inter 28pt Bold", 17.0));
			AnchorPane.setTopAnchor(titleLabel, 0.0);
			AnchorPane.setLeftAnchor(titleLabel, 0.0);
			AnchorPane.setRightAnchor(titleLabel, 0.0);
			
			ImageView exitBtn = new ImageView(new Image(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/cross.png")));
			exitBtn.setFitHeight(33.0);
			exitBtn.setFitWidth(15.0);
			exitBtn.setPickOnBounds(true);
			exitBtn.setPreserveRatio(true);
			exitBtn.setEffect(new ColorAdjust(0, 0, 1.0, 0));
			AnchorPane.setTopAnchor(exitBtn, 5.0);
			AnchorPane.setRightAnchor(exitBtn, 5.0);
			
			GridPane gridPane = new GridPane();
			gridPane.setPadding(new Insets(3, 10, 10, 10));
			gridPane.setHgap(15.0);
			gridPane.setVgap(3.0);
			AnchorPane.setTopAnchor(gridPane, 33.0);
			AnchorPane.setBottomAnchor(gridPane, 0.0);
			AnchorPane.setLeftAnchor(gridPane, 0.0);
			AnchorPane.setRightAnchor(gridPane, 0.0);
			
			gridPane.getColumnConstraints().addAll(new ColumnConstraints(100, 100, Double.MAX_VALUE, Priority.SOMETIMES, HPos.LEFT, true), new ColumnConstraints(100, 100, Double.MAX_VALUE, Priority.SOMETIMES, HPos.LEFT, true));
			for (int i = 0; i < 7; i++) {
				gridPane.getRowConstraints().add(new RowConstraints());
			}
			
			TextField nameField = new TextField();
			nameField.setEditable(false);
			GridPane.setRowIndex(nameField, 1);
			
			TextField dobField = new TextField();
			dobField.setEditable(false);
			GridPane.setColumnIndex(dobField, 1);
			GridPane.setRowIndex(dobField, 1);
			
			TextField expField = new TextField();
			expField.setEditable(false);
			GridPane.setRowIndex(expField, 3);
			
			TextField statusField = new TextField();
			statusField.setEditable(false);
			GridPane.setColumnIndex(statusField, 1);
			GridPane.setRowIndex(statusField, 3);
			
			TextField licNumField = new TextField();
			licNumField.setEditable(false);
			licNumField.setPrefColumnCount(Integer.MAX_VALUE);
			GridPane.setRowIndex(licNumField, 5);
			
			TextField gunLicTypeField = new TextField();
			gunLicTypeField.setEditable(false);
			GridPane.setRowIndex(gunLicTypeField, 5);
			GridPane.setColumnIndex(gunLicTypeField, 1);
			
			TextField gunLicClassField = new TextField();
			gunLicClassField.setEditable(false);
			GridPane.setRowIndex(gunLicClassField, 7);
			GridPane.setColumnSpan(gunLicClassField, 2);
			
			nameField.setText(name);
			dobField.setText(dob);
			expField.setText(exp);
			statusField.setText(status);
			licNumField.setText(licnum);
			gunLicTypeField.setText(gunLicType);
			gunLicClassField.setText(gunLicClass);
			
			Label nameLabel = createLabel(localization.getLocalizedMessage("PedLookup.NameLabel", "Name:"));
			Label dobLabel = createLabel(localization.getLocalizedMessage("PedLookup.DateOfBirthLabel", "Date of Birth:"));
			GridPane.setColumnIndex(dobLabel, 1);
			nameLabel.setMinWidth(Region.USE_PREF_SIZE);
			dobLabel.setMinWidth(Region.USE_PREF_SIZE);
			
			Label expDateLabel = createLabel(localization.getLocalizedMessage("PedLookup.ExpDateLabel", "Expiration Date:"));
			GridPane.setRowIndex(expDateLabel, 2);
			expDateLabel.setMinWidth(Region.USE_PREF_SIZE);
			
			Label licStatusLabel = createLabel(localization.getLocalizedMessage("PedLookup.FieldLicenseStatus", "License Status:"));
			GridPane.setColumnIndex(licStatusLabel, 1);
			GridPane.setRowIndex(licStatusLabel, 2);
			licStatusLabel.setMinWidth(Region.USE_PREF_SIZE);
			
			Label licNumLabel = createLabel(localization.getLocalizedMessage("PedLookup.FieldLicenseNumber", "License Number:"));
			GridPane.setRowIndex(licNumLabel, 4);
			licNumLabel.setMinWidth(Region.USE_PREF_SIZE);
			
			Label gunLicTypeLabel = createLabel(localization.getLocalizedMessage("PedLookup.FieldGunLicenseType", "Gun License Type:"));
			GridPane.setRowIndex(gunLicTypeLabel, 4);
			GridPane.setColumnIndex(gunLicTypeLabel, 1);
			gunLicTypeLabel.setMinWidth(Region.USE_PREF_SIZE);
			
			Label gunLicClassLabel = createLabel(localization.getLocalizedMessage("PedLookup.FieldGunLicenseClass", "Gun License Class:"));
			GridPane.setRowIndex(gunLicClassLabel, 6);
			GridPane.setColumnSpan(gunLicClassLabel, 2);
			gunLicClassLabel.setMinWidth(Region.USE_PREF_SIZE);
			
			final String UILightColor = "rgb(255,255,255,0.75)";
			final String UIDarkColor = "rgb(0,0,0,0.75)";
			if (ConfigReader.configRead("uiColors", "UIDarkMode").equalsIgnoreCase("true")) {
				nameLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				dobLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				expDateLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				licStatusLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				licNumLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				gunLicTypeLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				gunLicClassLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
			} else {
				nameLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				dobLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				expDateLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				licStatusLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				licNumLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				gunLicTypeLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				gunLicClassLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
			}
			
			gridPane.getChildren().addAll(nameField, dobField, expField, statusField, licNumField, nameLabel, dobLabel, expDateLabel, licStatusLabel, licNumLabel, gunLicTypeField, gunLicClassField, gunLicTypeLabel, gunLicClassLabel);
			
			popupContent.getChildren().addAll(titleLabel, exitBtn, gridPane);
			
			popupContent.setOnMouseMoved(event -> {
				double x = event.getX();
				boolean isOnRightEdge = x > popupContent.getWidth() - 10;
				
				if (isOnRightEdge) {
					popupContent.setCursor(Cursor.E_RESIZE);
				} else {
					popupContent.setCursor(Cursor.DEFAULT);
				}
			});
			
			popupContent.setOnMouseDragged(event -> {
				double x = event.getX();
				
				double minWidth = 200;
				
				if (popupContent.getCursor() == Cursor.E_RESIZE) {
					popupContent.setPrefWidth(Math.max(x, minWidth));
				}
			});
			
			DropShadow dropShadow = new DropShadow();
			dropShadow.setColor(new Color(0, 0, 0, 0.3));
			dropShadow.setOffsetX(0);
			dropShadow.setOffsetY(0);
			dropShadow.setRadius(15);
			dropShadow.setSpread(0.3);
			popupContent.setEffect(dropShadow);
			popupContent.setStyle("-fx-background-color: " + ConfigReader.configRead("uiColors", "bkgColor") + ";");
			
			Popup popup = new Popup();
			popup.getContent().add(popupContent);
			
			final boolean[] isPopupShown = {false};
			
			exitBtn.setOnMouseClicked(event -> {
				popup.hide();
				isPopupShown[0] = false;
			});
			
			label.setOnMouseClicked(event -> {
				if (isPopupShown[0]) {
					popup.hide();
					isPopupShown[0] = false;
				} else {
					popup.show(label.getScene().getWindow(), -9999, -9999);
					
					double labelScreenX = label.localToScreen(label.getBoundsInLocal()).getMinX();
					double labelScreenY = label.localToScreen(label.getBoundsInLocal()).getMinY();
					double labelWidth = label.getWidth();
					
					double popupX = labelScreenX + (labelWidth / 2) - (popupContent.getWidth() / 2);
					double popupY = labelScreenY - popupContent.getHeight();
					
					popup.setX(popupX);
					popup.setY(popupY - 15);
					
					isPopupShown[0] = true;
				}
			});
			exitBtn.requestFocus();
		} catch (Exception e) {
			logError("Error creating license popup from field " + label.getText() + ": ", e);
		}
	}
	
	private Label createLabel(String text) {
		Label label = new Label(text);
		label.setFont(new Font("Inter 28pt Bold", 12.0));
		
		final String UILightColor = "rgba(255,255,255,0.75)";
		final String UIDarkColor = "rgba(0,0,0,0.75)";
		
		try {
			if (ConfigReader.configRead("uiColors", "UIDarkMode").equalsIgnoreCase("true")) {
				label.setTextFill(Color.web(UIDarkColor));
			} else {
				label.setTextFill(Color.web(UILightColor));
				
			}
		} catch (IOException e) {
			logError("Error creating license label, cannot get uiColors.UIDarkMode: ", e);
		}
		
		return label;
	}
	
	@javafx.fxml.FXML
	public void onPedSearchBtnClick(ActionEvent actionEvent) throws IOException {
		String searchedName = pedSearchField.getEditor().getText().trim();
		updateRecentSearches(recentPedSearches, pedSearchField, searchedName);
		pedSearchField.getEditor().setText(searchedName);
		pedSearchField.getEditor().positionCaret(pedSearchField.getEditor().getText().length());
		
		log("Searched: " + searchedName, LogUtils.Severity.INFO);
		
		PedObject worldPedObject = new PedObject(getServerDataFolderPath() + "ServerWorldPeds.data", searchedName);
		Optional<Ped> pedOptional = findPedByName(searchedName);
		
		Map<String, String> ownerSearch = grabPedData(getServerDataFolderPath() + "ServerWorldCars.data", searchedName);
		String ownerName = ownerSearch.getOrDefault("owner", null);
		String ownerAddress = ownerSearch.getOrDefault("owneraddress", null);
		String ownerModel = ownerSearch.getOrDefault("ownermodel", null);
		String ownerGender = ownerSearch.getOrDefault("ownergender", null);
		String ownerPlateNum = ownerSearch.getOrDefault("licenseplate", null);
		
		if (pedOptional.isPresent()) {
			// Ped was found in PedHistory
			log("Found: [" + pedOptional.get().getName() + "] From PedHistory file", LogUtils.Severity.DEBUG);
			Ped ped = pedOptional.get();
			if (ped.getModel() == null) {
				ped.setModel("Not Found");
				try {
					Ped.PedHistoryUtils.addPed(ped);
				} catch (JAXBException e) {
					logError("Could not save new pedModel: ", e);
				}
				log("Set pedModel as 'Not Found'", LogUtils.Severity.WARN);
			}
			
			processPedData(false, ped.getName(), ped.getLicenseNumber(), ped.getModel(), ped.getBirthday(), ped.getGender(), ped.getAddress(), ped.getWantedStatus(), ped.getLicenseStatus(), null, ped.getGunLicenseType(), ped.getGunLicenseStatus(), ped.getGunLicenseExpiration(), ped.getFishingLicenseStatus(), ped.getFishingLicenseExpiration(), ped.getTimesStopped(), ped.getHuntingLicenseStatus(), ped.getHuntingLicenseExpiration(), ped.getParoleStatus(), ped.getProbationStatus());
			
		} else if (worldPedObject.getName() != null && !worldPedObject.getName().equals("Not Found")) {
			// Ped was found in WorldPed
			log("Found: [" + worldPedObject.getName() + "] From WorldPed file", LogUtils.Severity.DEBUG);
			
			processPedData(false, worldPedObject.getName(), worldPedObject.getLicenseNumber(), worldPedObject.getModelName(), worldPedObject.getBirthday(), worldPedObject.getGender(), worldPedObject.getAddress(), worldPedObject.getIsWanted(), worldPedObject.getLicenseStatus(), null, worldPedObject.getWeaponPermitType(), worldPedObject.getWeaponPermitStatus(), worldPedObject.getWeaponPermitExpiration(), worldPedObject.getFishPermitStatus(), worldPedObject.getFishPermitExpiration(), worldPedObject.getTimesStopped(), worldPedObject.getHuntPermitStatus(), worldPedObject.getHuntPermitExpiration(), worldPedObject.getIsOnParole(), worldPedObject.getIsOnProbation());
			
		} else if (ownerName != null && !ownerName.equalsIgnoreCase("Not Found") && !ownerName.equalsIgnoreCase("Los Santos Police Department") && !ownerName.equalsIgnoreCase("Los Santos Sheriff's Office") && !ownerName.equalsIgnoreCase("Blaine County Sheriff's Office") && !ownerName.equalsIgnoreCase("San Andreas Highway Patrol") && !ownerName.equalsIgnoreCase("government")) {
			// Vehicle Owner was found and the vehicle is not Government
			log("Found Vehicle Owner: [" + ownerName + "] From WorldVeh file, plate#: " + ownerPlateNum, LogUtils.Severity.DEBUG);
			
			processPedData(true, ownerName, null, ownerModel, null, ownerGender, ownerAddress, null, null, null, null, null, null, null, null, null, null, null, null, null);
			
		} else if (searchIDHisForName(searchedName)) {
			// Ped ID Was Found (Ped is probably dead)
			log("Found Ped: [" + searchedName + "] From IDHistory (Possible Dead Ped)", LogUtils.Severity.DEBUG);
			ID searchedNameID = getHistoryIDFromName(searchedName);
			
			if (searchedNameID != null) {
				log(searchedName + " HistoryID not null", LogUtils.Severity.DEBUG);
				
				processPedData(false, searchedNameID.getName(), searchedNameID.getLicenseNumber(), searchedNameID.getPedModel(), searchedNameID.getBirthday(), searchedNameID.getGender(), searchedNameID.getAddress(), null, null, null, null, null, null, null, null, null, null, null, null, null);
			}
			
		} else {
			// Ped was Not Found Anywhere
			log("No Ped With Name: [" + searchedName + "] Found Anywhere", LogUtils.Severity.WARN);
			pedRecordPane.setVisible(false);
			noRecordFoundLabelPed.setVisible(true);
		}
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
		
		Map<String, Object> citationReportMap = (Map<String, Object>) trafficCitationObj.get(localization.getLocalizedMessage("ReportWindows.CitationReportTitle", "Citation Report") + " Map");
		
		TextField offenderName = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
		TextField offenderAge = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"));
		TextField offenderGender = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"));
		TextField offenderAddress = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"));
		TextField offenderDescription = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderDescription", "offender description"));
		
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
		
		Map<String, Object> arrestReportMap = (Map<String, Object>) arrestReportObj.get(localization.getLocalizedMessage("ReportWindows.ArrestReportTitle", "Arrest Report") + " Map");
		
		TextField offenderName = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
		TextField offenderAge = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"));
		TextField offenderGender = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"));
		TextField offenderAddress = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"));
		TextField offenderDescription = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderDescription", "offender description"));
		
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
		Optional<Ped> optionalPed = findPedByNumber(searchedLicenseNum);
		
		if (optionalPed.isPresent()) {
			Ped ped = optionalPed.get();
			
			String pedflagfieldText = pedflagfield.getText();
			if (!pedflagfieldText.equalsIgnoreCase("No Data In System") && !pedflagfieldText.isEmpty()) {
				pedflagfield.setStyle("-fx-text-fill: red !important;");
				ped.setFlags(pedflagfieldText.trim());
			} else {
				pedflagfield.setStyle("-fx-text-fill: #603417 !important;");
				pedflagfield.setText("No Data In System");
				ped.setFlags(null);
			}
			
			String affiliationText = pedaffiliationfield.getText();
			if (!affiliationText.equalsIgnoreCase("No Data In System") && !affiliationText.isEmpty()) {
				pedaffiliationfield.setStyle("-fx-text-fill: black !important;");
				ped.setAffiliations(affiliationText.trim());
			} else {
				pedaffiliationfield.setStyle("-fx-text-fill: #603417 !important;");
				pedaffiliationfield.setText("No Data In System");
				ped.setAffiliations(null);
			}
			
			String descText = peddescfield.getText();
			if (!descText.equalsIgnoreCase("No Data In System") && !descText.isEmpty()) {
				peddescfield.setStyle("-fx-text-fill: black !important;");
				ped.setDescription(descText.trim());
			} else {
				peddescfield.setStyle("-fx-text-fill: #603417 !important;");
				peddescfield.setText("No Data In System");
				ped.setDescription(null);
			}
			
			String aliasText = pedaliasfield.getText();
			if (!aliasText.equalsIgnoreCase("No Data In System") && !aliasText.isEmpty()) {
				pedaliasfield.setStyle("-fx-text-fill: black !important;");
				ped.setAliases(aliasText.trim());
			} else {
				pedaliasfield.setStyle("-fx-text-fill: #603417 !important;");
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
