package com.Guess.ReportsPlus.Windows.Apps;

import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.MainApplication.mainDesktopControllerObj;
import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.createNoteTabs;
import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.notesTabList;
import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.notesViewController;
import static com.Guess.ReportsPlus.Windows.Server.CurrentIDViewController.defaultPedImagePath;
import static com.Guess.ReportsPlus.Windows.Settings.settingsController.UIDarkColor;
import static com.Guess.ReportsPlus.Windows.Settings.settingsController.UILightColor;
import static com.Guess.ReportsPlus.logs.Arrest.ArrestReportUtils.newArrest;
import static com.Guess.ReportsPlus.logs.TrafficCitation.TrafficCitationUtils.newCitation;
import static com.Guess.ReportsPlus.util.History.IDHistory.getHistoryIDFromName;
import static com.Guess.ReportsPlus.util.History.IDHistory.searchIDHisForName;
import static com.Guess.ReportsPlus.util.History.Ped.PedHistoryUtils.findPedByName;
import static com.Guess.ReportsPlus.util.History.Ped.PedHistoryUtils.findPedByNumber;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.assignFlagsBasedOnPriors;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.calculateAge;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.calculateLicenseStatus;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.calculateTotalStops;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.calculateTrueFalseProbability;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.femaleFirstNames;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.generateBirthday;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.generateExpiredLicenseExpirationDate;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.generateLicenseNumber;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.generateValidLicenseExpirationDate;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.getRandomAddress;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.getRandomChargeWithWarrant;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.getRandomDepartment;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.maleFirstNames;
import static com.Guess.ReportsPlus.util.Misc.AudioUtil.playSound;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logDebug;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logInfo;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logWarn;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationError;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationInfo;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.createLabels;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getGunLicenseClass;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getJarPath;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getServerDataFolderPath;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.setArrestPriors;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.setCitationPriors;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.toTitleCase;
import static com.Guess.ReportsPlus.util.Server.recordUtils.getAllVehicleOwners;
import static com.Guess.ReportsPlus.util.Server.recordUtils.grabPedData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager;
import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager.IShutdownable;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.config.ConfigWriter;
import com.Guess.ReportsPlus.logs.LookupObjects.PedObject;
import com.Guess.ReportsPlus.logs.LookupObjects.WorldPedUtils;
import com.Guess.ReportsPlus.util.History.IDHistory;
import com.Guess.ReportsPlus.util.History.Ped;
import com.Guess.ReportsPlus.util.History.PedHistoryMath;
import com.Guess.ReportsPlus.util.History.Vehicle;
import com.Guess.ReportsPlus.util.Misc.Threading.WorkerThread;
import com.Guess.ReportsPlus.util.Other.NoteTab;
import com.Guess.ReportsPlus.util.Server.Objects.ID.ID;
import com.Guess.ReportsPlus.util.Strings.URLStrings;

import jakarta.xml.bind.JAXBException;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Popup;

public class PedLookupViewController implements IShutdownable {
	// #region FXML Variables
	@FXML
	private Label physicalinfosubheading;
	@FXML
	private Label otherinfosubheading;
	@FXML
	private BorderPane basicinfobutton;
	@FXML
	private BorderPane licenseinfobutton;
	@FXML
	private BorderPane criminalhistorybutton;
	@FXML
	private HBox pedimagecard;
	@FXML
	private Label basicinfosubheading;
	@FXML
	private VBox basicinfocard;
	@FXML
	private VBox physicalinfocard;
	@FXML
	private VBox otherinfocard;
	@FXML
	private Label licenseinfoheading;
	@FXML
	private VBox licensecard;
	@FXML
	private VBox huntingcard;
	@FXML
	private VBox fishingcard;
	@FXML
	private VBox boatingcard;
	@FXML
	private VBox gunliccard;
	@FXML
	private VBox statuscard;
	@FXML
	private VBox arresthistorycard;
	@FXML
	private VBox citationhistorycard;
	@FXML
	private Label criminalHistoryHeading;
	@FXML
	private Label statusSubheading;
	@FXML
	private Button settingsButton;
	@FXML
	private VBox sidePane;
	@FXML
	private Label driverLicSubheading;
	@FXML
	private Label huntLicSubheading;
	@FXML
	private Label fishlicSubheading;
	@FXML
	private Label boatLicSubheading;
	@FXML
	private Label gunlicSubheading;
	@FXML
	private Label pedWantedLabelField;
	@FXML
	private Label basicInfoSideButton;
	@FXML
	private Label licenseInfoSideButton;
	@FXML
	private Label criminalHistorySideButton;
	@FXML
	private BorderPane databaseSearchPane;
	@FXML
	private BorderPane databaseInfoPane;
	@FXML
	private Label backLabel;
	@FXML
	private ScrollPane pedCriminalHistoryGrid;
	@FXML
	private ScrollPane pedLicenseInfoGrid;
	@FXML
	private Label pedNEWboatinglicnumber;
	@FXML
	private TextField pedboatinglicensenumberfield;
	@FXML
	private Label pedNEWboatinglicexpiration;
	@FXML
	private TextField pedboatinglicexpirationfield;
	@FXML
	private Label pedNEWfishinglicnumber;
	@FXML
	private TextField pedfishinglicensenumberfield;
	@FXML
	private Label pedNEWfishinglicexpiration;
	@FXML
	private TextField pedfishinglicexpirationfield;
	@FXML
	private Label pedNEWdriverlicexpiration;
	@FXML
	private TextField peddriverlicexpirationfield;
	@FXML
	private Label pedNEWhuntinglicnumber;
	@FXML
	private TextField pedhuntinglicnumberfield;
	@FXML
	private Label pedNEWhuntinglicexpiration;
	@FXML
	private TextField pedhuntinglicexpirationfield;
	@FXML
	private Label pedNEWgunlicnum;
	@FXML
	private TextField pedgunlicnumberfield;
	@FXML
	private Label pedNEWgunlicexpiration;
	@FXML
	private TextField pedgunlicexpirationfield;
	@FXML
	private Label pedNEWgunlicensetype;
	@FXML
	private TextField pedgunlicensetypefield;
	@FXML
	private Label pedNEWgunlicclass;
	@FXML
	private TextField pedgunlicclassfield;
	@FXML
	private ScrollPane pedBasicInfoGrid;
	@FXML
	private Label pedFullNameLabelField;
	@FXML
	private Label pedPoliceLabelField;
	@FXML
	private Label pedFlagsLabelField;
	@FXML
	private Label pedNEWheight;
	@FXML
	private TextField pedheightfield;
	@FXML
	private Label pedNEWweight;
	@FXML
	private TextField pedweightfield;
	@FXML
	private Label pedNEWmaritalstatus;
	@FXML
	private TextField pedmaritalstatusfield;
	@FXML
	private Label pedNEWdisability;
	@FXML
	private TextField peddisabilityfield;
	@FXML
	private Label pedNEWcitizenshipstatus;
	@FXML
	private TextField pedcitizenshipstatusfield;
	@FXML
	private ListView databaseListView;
	@FXML
	private BorderPane root;
	@FXML
	private AnchorPane lookupPane;
	@FXML
	private Label lookupmainlbl;
	@FXML
	private TextField pedfnamefield;
	@FXML
	private Label ped1;
	@FXML
	private TextField pedlnamefield;
	@FXML
	private Label ped2;
	@FXML
	private TextField pedwantedfield;
	@FXML
	private Label ped4;
	@FXML
	private TextField pedlicensefield;
	@FXML
	private Label ped5;
	@FXML
	private TextField peddobfield;
	@FXML
	private Label ped6;
	@FXML
	private TextField pedaddressfield;
	@FXML
	private Label ped7;
	@FXML
	private TextField peddescfield;
	@FXML
	private Label ped8;
	@FXML
	private TextField pedlicnumfield;
	@FXML
	private Label ped9;
	@FXML
	private TextField pedparolestatusfield;
	@FXML
	private Label ped12;
	@FXML
	private TextField pedprobationstatusfield;
	@FXML
	private Label ped13;
	@FXML
	private TextField pedgunlicensestatusfield;
	@FXML
	private Label ped15;
	@FXML
	private TextField pedfishinglicstatusfield;
	@FXML
	private Label ped18;
	@FXML
	private TextField pedboatinglicstatusfield;
	@FXML
	private Label ped19;
	@FXML
	private TextField pedhuntinglicstatusfield;
	@FXML
	private Label ped20;
	@FXML
	private Label ped21;
	@FXML
	private ListView pedarrestpriorslistview;
	@FXML
	private Label ped22;
	@FXML
	private ListView pedcitationpriorslistview;
	@FXML
	private TextField pedtimesstoppedfield;
	@FXML
	private Label ped14;
	@FXML
	private TextField pedgenfield;
	@FXML
	private Label ped3;
	@FXML
	private Label noPedImageFoundlbl;
	@FXML
	private ImageView pedImageView;
	@FXML
	private TextField pedflagfield;
	@FXML
	private Label ped23;
	@FXML
	private TextField pedaffiliationfield;
	@FXML
	private Label ped11;
	@FXML
	private TextField pedaliasfield;
	@FXML
	private Label ped10;
	@FXML
	private Button infobtn3;
	@FXML
	private Label lbl1;
	@FXML
	private TextField pedSearchField;
	@FXML
	private Button addDataToNotesBtn;
	// #endregion
	private final ObservableList<Ped> masterPedList = FXCollections.observableArrayList();
	private final List<Ped> allRealPeds = new ArrayList<>();
	public static PedLookupViewController pedLookupViewController;
	private final ExecutorService searchExecutor = Executors.newSingleThreadExecutor(r -> {
		WorkerThread t = new WorkerThread("Ped-Search-Thread", r);
		t.setDaemon(true);
		return t;
	});
	private Task<?> currentSearchTask;
	private boolean isLoading = false;

	private void loadAllRealPeds(boolean printUniqueCount) {
		Map<String, Ped> realPedsMap = new LinkedHashMap<>();
		List<Ped> pedsFromHistory = Ped.PedHistoryUtils.getAllPeds();
		for (Ped ped : pedsFromHistory) {
			if (ped != null && ped.getName() != null && !ped.getName().trim().isEmpty()) {
				realPedsMap.putIfAbsent(ped.getName().toLowerCase(), ped);
			}
		}
		List<PedObject> pedsFromWorld = WorldPedUtils
				.getAllWorldPeds(getServerDataFolderPath() + "ServerWorldPeds.data");
		for (PedObject pObj : pedsFromWorld) {
			if (pObj != null && pObj.getName() != null && !pObj.getName().trim().isEmpty()) {
				String pedName = pObj.getName().toLowerCase();
				if (!realPedsMap.containsKey(pedName)) {
					Ped ped = performPedLookup(pObj.getName());
					if (ped != null) {
						realPedsMap.put(pedName, ped);
					}
				}
			}
		}
		List<String> vehicleOwners = getAllVehicleOwners(getServerDataFolderPath() + "ServerWorldCars.data");
		for (String ownerName : vehicleOwners) {
			if (ownerName != null && !ownerName.trim().isEmpty()) {
				String lowerCaseOwnerName = ownerName.toLowerCase();
				if (!realPedsMap.containsKey(lowerCaseOwnerName)) {
					Ped ped = performPedLookup(ownerName);
					if (ped != null) {
						realPedsMap.put(lowerCaseOwnerName, ped);
					}
				}
			}
		}
		try {
			Vehicle.Vehicles vehicleData = Vehicle.VehicleHistoryUtils.loadVehicles();
			if (vehicleData != null && vehicleData.getVehicleList() != null) {
				List<Vehicle> historyVehicles = vehicleData.getVehicleList();
				for (Vehicle vehicle : historyVehicles) {
					if (vehicle != null && vehicle.getOwner() != null && !vehicle.getOwner().trim().isEmpty()) {
						String ownerName = vehicle.getOwner();
						String lowerCaseOwnerName = ownerName.toLowerCase();
						if (!realPedsMap.containsKey(lowerCaseOwnerName)) {
							Ped ped = performPedLookup(ownerName);
							if (ped != null) {
								realPedsMap.put(lowerCaseOwnerName, ped);
							}
						}
					}
				}
			}
		} catch (JAXBException e) {
			logError("Failed to load vehicle owners from VehHistory.xml", e);
		}
		List<ID> idsFromHistory = IDHistory.getAllHistoryIDs();
		for (ID id : idsFromHistory) {
			if (id != null && id.getName() != null && !id.getName().trim().isEmpty()) {
				String pedName = id.getName().toLowerCase();
				if (!realPedsMap.containsKey(pedName)) {
					Ped ped = performPedLookup(id.getName());
					if (ped != null) {
						realPedsMap.put(pedName, ped);
					}
				}
			}
		}
		allRealPeds.clear();
		allRealPeds.addAll(realPedsMap.values());
		if (printUniqueCount)
			logInfo("Initialized, Loaded " + allRealPeds.size() + " unique real peds into memory.");
	}

	private void setupSearchListener() {
		pedSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
			loadAllRealPeds(false);
			updateSearchResults();
		});
		databaseListView.setItems(masterPedList);
	}

	private List<String> prefilterFirstNames(String searchText) {
		String firstNamePrefix = searchText.toLowerCase().split(" ", 2)[0];
		List<String> combinedNames = new ArrayList<>();
		List<String> maleMatches = maleFirstNames.stream()
				.filter(name -> name.toLowerCase().startsWith(firstNamePrefix))
				.collect(Collectors.toList());
		List<String> femaleMatches = femaleFirstNames.stream()
				.filter(name -> name.toLowerCase().startsWith(firstNamePrefix))
				.collect(Collectors.toList());
		combinedNames.addAll(maleMatches);
		combinedNames.addAll(femaleMatches);
		return combinedNames;
	}

	private List<String> prefilterLastNames(String searchText) {
		String[] nameParts = searchText.toLowerCase().split(" ", 2);
		if (nameParts.length > 1) {
			String lastNamePrefix = nameParts[1];
			return PedHistoryMath.lastNames.stream()
					.filter(name -> name.toLowerCase().startsWith(lastNamePrefix))
					.collect(Collectors.toList());
		}
		return null;
	}

	private void loadMorePeds() {
		if (isLoading)
			return;
		final String searchText = pedSearchField.getText().trim();
		final Set<String> existingNames = masterPedList.stream()
				.map(p -> p.getName().toLowerCase())
				.collect(Collectors.toSet());
		currentSearchTask = new Task<List<Ped>>() {
			@Override
			protected List<Ped> call() throws Exception {
				Random random = new Random();
				int targetSize;
				switch (searchText.length()) {
					case 1:
						targetSize = 40 + random.nextInt(21);
						break;
					case 2:
						targetSize = 20 + random.nextInt(16);
						break;
					case 3:
						targetSize = 10 + random.nextInt(11);
						break;
					case 4:
						targetSize = 5 + random.nextInt(6);
						break;
					default:
						targetSize = 2 + random.nextInt(4);
						break;
				}
				List<Ped> batch = new ArrayList<>();
				List<String> matchingFirstNames = prefilterFirstNames(searchText);
				List<String> matchingLastNames = prefilterLastNames(searchText);
				while (batch.size() < targetSize) {
					if (isCancelled())
						break;
					Ped candidate = createMatchingPed(matchingFirstNames, matchingLastNames, searchText);
					if (existingNames.add(candidate.getName().toLowerCase())) {
						batch.add(candidate);
					}
				}
				return batch;
			}
		};
		currentSearchTask.setOnSucceeded(event -> {
			masterPedList.addAll(((Task<List<Ped>>) event.getSource()).getValue());
			isLoading = false;
		});
		currentSearchTask.setOnFailed(event -> {
			logError("Failed to load more peds: ", currentSearchTask.getException());
			isLoading = false;
		});
		currentSearchTask.setOnCancelled(event -> isLoading = false);
		isLoading = true;
		searchExecutor.submit(currentSearchTask);
	}

	private boolean isSearchFinite(String text) {
		if (text == null) {
			return false;
		}
		return text.trim().length() >= 10 && text.contains(" ");
	}

	private void updateSearchResults() {
		if (currentSearchTask != null && currentSearchTask.isRunning()) {
			currentSearchTask.cancel();
		}
		final String searchText = pedSearchField.getText().trim();
		currentSearchTask = new Task<List<Ped>>() {
			@Override
			protected List<Ped> call() throws Exception {
				if (searchText.isEmpty()) {
					return Collections.emptyList();
				}
				List<Ped> results = new ArrayList<>();
				List<Ped> realMatches = allRealPeds.stream()
						.filter(ped -> pedMatchesSearch(ped, searchText))
						.collect(Collectors.toList());
				results.addAll(realMatches);
				if (isCancelled()) {
					return Collections.emptyList();
				}
				final int MINIMUM_RESULTS = 25;
				int pedsToGenerate = MINIMUM_RESULTS - results.size();
				if (pedsToGenerate > 0 && !isSearchFinite(searchText)) {
					List<String> matchingFirstNames = prefilterFirstNames(searchText);
					if (!matchingFirstNames.isEmpty()) {
						List<String> matchingLastNames = prefilterLastNames(searchText);
						String[] nameParts = searchText.trim().toLowerCase().split("\\s+", 2);
						boolean shouldGenerate = true;
						if (nameParts.length > 1 && (matchingLastNames == null || matchingLastNames.isEmpty())) {
							shouldGenerate = false;
						}
						if (shouldGenerate) {
							Set<String> existingNames = results.stream()
									.map(p -> p.getName().toLowerCase())
									.collect(Collectors.toSet());
							for (int i = 0; i < pedsToGenerate; i++) {
								if (isCancelled())
									break;
								for (int attempt = 0; attempt < 3; attempt++) {
									Ped candidate = createMatchingPed(matchingFirstNames, matchingLastNames,
											searchText);
									if (existingNames.add(candidate.getName().toLowerCase())) {
										results.add(candidate);
										break;
									}
								}
							}
						}
					}
				}
				Collections.shuffle(results);
				return results;
			}
		};
		currentSearchTask.setOnSucceeded(event -> {
			masterPedList.setAll(((Task<List<Ped>>) event.getSource()).getValue());
			isLoading = false;
		});
		currentSearchTask.setOnFailed(event -> {
			logError("Ped search task failed: ", currentSearchTask.getException());
			isLoading = false;
		});
		currentSearchTask.setOnCancelled(event -> isLoading = false);
		isLoading = true;
		masterPedList.clear();
		searchExecutor.submit(currentSearchTask);
	}

	private boolean pedMatchesSearch(Ped ped, String searchText) {
		if (ped == null || ped.getName() == null || searchText == null || searchText.trim().isEmpty()) {
			return false;
		}
		String lowerSearchText = searchText.toLowerCase().trim();
		String[] searchParts = lowerSearchText.split("\\s+");
		String pedFullName = ped.getName().toLowerCase();
		String pedFirstName = ped.getFirstName().toLowerCase();
		String pedLastName = ped.getLastName().toLowerCase();
		if (searchParts.length == 1) {
			return pedFirstName.startsWith(searchParts[0]) ||
					pedLastName.startsWith(searchParts[0]) ||
					pedFullName.startsWith(lowerSearchText);
		} else if (searchParts.length > 1) {
			return pedFirstName.startsWith(searchParts[0]) && pedLastName.startsWith(searchParts[1]);
		}
		return false;
	}

	public void createWarrantInfoPopup(TextField label, String headerText, String name, String dob, String issuedDate,
			String warrantNumber, String agency, String warrant) {
		try {
			AnchorPane popupContent = new AnchorPane();
			popupContent.setPrefWidth(Region.USE_COMPUTED_SIZE);
			popupContent.getStylesheets().add(Launcher.class
					.getResource("/com/Guess/ReportsPlus/css/courtCase/courtCaseCss.css").toExternalForm());
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
			ImageView exitBtn = new ImageView(
					new Image(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/cross.png")));
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
			gridPane.getColumnConstraints().addAll(
					new ColumnConstraints(100, 100, Double.MAX_VALUE, Priority.SOMETIMES, HPos.LEFT, true),
					new ColumnConstraints(100, 100, Double.MAX_VALUE, Priority.SOMETIMES, HPos.LEFT, true));
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
			Label dobLabel = createLabel(
					localization.getLocalizedMessage("PedLookup.DateOfBirthLabel", "Date of Birth:"));
			GridPane.setColumnIndex(dobLabel, 1);
			nameLabel.setMinWidth(Region.USE_PREF_SIZE);
			Label dateIssuedLabel = createLabel(
					localization.getLocalizedMessage("PedLookup.DateIssuedLabel", "Date Issued:"));
			GridPane.setRowIndex(dateIssuedLabel, 2);
			dateIssuedLabel.setMinWidth(Region.USE_PREF_SIZE);
			Label warrantNumLabel = createLabel(
					localization.getLocalizedMessage("PedLookup.WarrantNumberLabel", "Warrant Number:"));
			GridPane.setColumnIndex(warrantNumLabel, 1);
			GridPane.setRowIndex(warrantNumLabel, 2);
			warrantNumLabel.setMinWidth(Region.USE_PREF_SIZE);
			Label agencyLabel = createLabel(
					localization.getLocalizedMessage("PedLookup.IssuingAuthorityLabel", "Issuing Authority:"));
			GridPane.setRowIndex(agencyLabel, 4);
			agencyLabel.setMinWidth(Region.USE_PREF_SIZE);
			Label warrantLabel = createLabel(localization.getLocalizedMessage("PedLookup.WarrantLabel", "Warrant:"));
			GridPane.setRowIndex(warrantLabel, 6);
			warrantLabel.setMinWidth(Region.USE_PREF_SIZE);
			if (ConfigReader.configRead("uiColors", "UIDarkMode").equalsIgnoreCase("true")) {
				nameLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				dobLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				dateIssuedLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				warrantNumLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				agencyLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				warrantLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				Node node = agencyField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UIDarkColor + " !important;");
				node = warrantField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UIDarkColor + " !important;");
				node = nameField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UIDarkColor + " !important;");
				node = dobField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UIDarkColor + " !important;");
				node = dateIssuedField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UIDarkColor + " !important;");
				node = warrantNumField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UIDarkColor + " !important;");
			} else {
				nameLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				dobLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				dateIssuedLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				warrantNumLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				agencyLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				warrantLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				Node node = agencyField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UILightColor + " !important;");
				node = warrantField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UILightColor + " !important;");
				node = nameField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UILightColor + " !important;");
				node = dobField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UILightColor + " !important;");
				node = dateIssuedField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UILightColor + " !important;");
				node = warrantNumField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UILightColor + " !important;");
			}
			gridPane.getChildren().addAll(nameField, dobField, dateIssuedField, warrantNumField, agencyField, nameLabel,
					dobLabel, dateIssuedLabel, warrantNumLabel, agencyLabel, warrantLabel, warrantField);
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
			final boolean[] isPopupShown = { false };
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

	private void addLocalization() {
		backLabel.setText(localization.getLocalizedMessage("PedLookup.BackButton", "Back"));
		lookupmainlbl.setText(localization.getLocalizedMessage("PedLookup.MainHeader", "D.M.V Pedestrian Lookup"));
		lbl1.setText(localization.getLocalizedMessage("PedLookup.SearchPedLabel", "Search Ped:"));
		noPedImageFoundlbl
				.setText(localization.getLocalizedMessage("PedLookup.NoPedImageFoundlbl", "No Image Found In System"));
		addDataToNotesBtn
				.setText(localization.getLocalizedMessage("PedLookup.AddDataToNotesButton", "Add Data To Notes"));
		infobtn3.setText(
				localization.getLocalizedMessage("PedLookup.UpdateOtherInfoButton", "Update Other Information"));
		ped21.setText(localization.getLocalizedMessage("PedLookup.ArrestHistoryLabel", "Arrest History:"));
		ped22.setText(localization.getLocalizedMessage("PedLookup.CitationHistoryLabel", "Citation History:"));
		ped1.setText(localization.getLocalizedMessage("PedLookup.FieldFirstName", "First Name:"));
		ped2.setText(localization.getLocalizedMessage("PedLookup.FieldLastName", "Last Name:"));
		ped3.setText(localization.getLocalizedMessage("PedLookup.FieldGender", "Gender:"));
		pedNEWheight.setText(localization.getLocalizedMessage("PedLookup.Height", "Height:"));
		pedNEWweight.setText(localization.getLocalizedMessage("PedLookup.Weight", "Weight:"));
		statusSubheading.setText(
				localization.getLocalizedMessage("PedLookup.StatusAndAffiliations", "Status & Affiliations"));
		criminalHistoryHeading
				.setText(localization.getLocalizedMessage("PedLookup.CriminalHistory", "Criminal History"));
		criminalHistorySideButton
				.setText(localization.getLocalizedMessage("PedLookup.CriminalHistory", "Criminal History"));
		pedNEWweight.setText(localization.getLocalizedMessage("PedLookup.Weight", "Weight:"));
		licenseinfoheading
				.setText(localization.getLocalizedMessage("PedLookup.LicenseInfoLabel", "Licensing Information"));
		licenseInfoSideButton
				.setText(localization.getLocalizedMessage("PedLookup.LicenseInfoLabel", "Licensing Information"));
		physicalinfosubheading.setText(
				localization.getLocalizedMessage("PedLookup.PhysicalInfoLabel", "Physical Information"));
		otherinfosubheading.setText(
				localization.getLocalizedMessage("PedLookup.OtherInfoLabel", "Other Information"));
		basicinfosubheading.setText(localization.getLocalizedMessage("PedLookup.BasicInfoLabel", "Basic Information"));
		basicInfoSideButton.setText(localization.getLocalizedMessage("PedLookup.BasicInfoLabel", "Basic Information"));
		ped4.setText(localization.getLocalizedMessage("PedLookup.FieldWantedStatus", "Wanted Status:"));
		ped5.setText(localization.getLocalizedMessage("Callout_Manager.CalloutStatus", "Status:"));
		ped6.setText(localization.getLocalizedMessage("PedLookup.FieldBirthday", "Birthday:"));
		ped7.setText(localization.getLocalizedMessage("PedLookup.FieldAddress", "Address:"));
		ped8.setText(localization.getLocalizedMessage("PedLookup.FieldDescription", "Description:"));
		pedNEWmaritalstatus.setText(localization.getLocalizedMessage("PedLookup.MaritalStatus", "Marital Status:"));
		pedNEWdisability.setText(localization.getLocalizedMessage("PedLookup.Disability", "Disability:"));
		pedNEWcitizenshipstatus
				.setText(localization.getLocalizedMessage("PedLookup.CitizenshipStatus", "Citizenship Status:"));
		ped9.setText(localization.getLocalizedMessage("Callout_Manager.CalloutNumber", "Number:"));
		ped10.setText(localization.getLocalizedMessage("PedLookup.FieldAlias", "Alias(s):"));
		ped11.setText(localization.getLocalizedMessage("PedLookup.FieldAffiliations", "Affiliation(s):"));
		ped12.setText(localization.getLocalizedMessage("PedLookup.FieldParoleStatus", "Parole Status:"));
		ped13.setText(localization.getLocalizedMessage("PedLookup.FieldProbationStatus", "Probation Status:"));
		ped14.setText(localization.getLocalizedMessage("PedLookup.FieldTimesStopped", "Times Stopped:"));
		ped15.setText(localization.getLocalizedMessage("Callout_Manager.CalloutStatus", "Status:"));
		ped18.setText(
				localization.getLocalizedMessage("Callout_Manager.CalloutStatus", "Status:"));
		ped19.setText(
				localization.getLocalizedMessage("Callout_Manager.CalloutStatus", "Status:"));
		ped20.setText(
				localization.getLocalizedMessage("Callout_Manager.CalloutStatus", "Status:"));
		ped23.setText(localization.getLocalizedMessage("PedLookup.FieldFlags", "Flag(s):"));
		driverLicSubheading.setText(
				localization.getLocalizedMessage("PedLookup.DriverLicenseSubheading", "Driver License Information"));
		huntLicSubheading.setText(
				localization.getLocalizedMessage("PedLookup.HuntingLicenseSubheading", "Hunting License Information"));
		fishlicSubheading.setText(
				localization.getLocalizedMessage("PedLookup.FishingLicenseSubheading", "Fishing License Information"));
		boatLicSubheading.setText(
				localization.getLocalizedMessage("PedLookup.BoatingLicenseSubheading", "Boating License Information"));
		gunlicSubheading.setText(
				localization.getLocalizedMessage("PedLookup.GunLicenseSubheading", "Gun License Information"));
		pedNEWboatinglicnumber.setText(
				localization.getLocalizedMessage("Callout_Manager.CalloutNumber", "Number:"));
		pedNEWfishinglicnumber.setText(
				localization.getLocalizedMessage("Callout_Manager.CalloutNumber", "Number:"));
		pedNEWhuntinglicnumber.setText(
				localization.getLocalizedMessage("Callout_Manager.CalloutNumber", "Number:"));
		pedNEWgunlicnum.setText(
				localization.getLocalizedMessage("Callout_Manager.CalloutNumber", "Number:"));
		pedNEWboatinglicexpiration.setText(
				localization.getLocalizedMessage("PedLookup.ExpDateLabel", "Expiration Date:"));
		pedNEWfishinglicexpiration.setText(
				localization.getLocalizedMessage("PedLookup.ExpDateLabel", "Expiration Date:"));
		pedNEWhuntinglicexpiration.setText(
				localization.getLocalizedMessage("PedLookup.ExpDateLabel", "Expiration Date:"));
		pedNEWgunlicexpiration.setText(
				localization.getLocalizedMessage("PedLookup.ExpDateLabel", "Expiration Date:"));
		pedNEWdriverlicexpiration.setText(
				localization.getLocalizedMessage("PedLookup.ExpDateLabel", "Expiration Date:"));
		pedNEWgunlicclass.setText(
				localization.getLocalizedMessage("PedLookup.ClassLabel", "Class:"));
		pedNEWgunlicensetype.setText(
				localization.getLocalizedMessage("CalloutPopup.TypeLabel", "Type:"));
	}

	private boolean setPedRecordFields(Ped ped) {
		boolean playAudio = false;
		pedfnamefield.setText(ped.getFirstName());
		pedlnamefield.setText(ped.getLastName());
		pedFullNameLabelField.setText(ped.getName());
		pedgenfield.setText(ped.getGender());
		peddobfield.setText(ped.getBirthday());
		pedaddressfield.setText(ped.getAddress());
		if (ped.getIsPolice().equalsIgnoreCase("true")) {
			pedPoliceLabelField.setText(localization.getLocalizedMessage("PedLookup.PoliceField", "Police Officer"));
			pedPoliceLabelField
					.setStyle("-fx-text-fill:rgb(68, 141, 231) !important; -fx-font-family: 'Inter 28pt Bold';");
		} else
			pedPoliceLabelField.setText(localization.getLocalizedMessage("PedLookup.PedestrianField", "Pedestrian"));
		pedlicensefield.setText(ped.getLicenseStatus());
		if (ped.getLicenseStatus().equalsIgnoreCase("EXPIRED") || ped.getLicenseStatus().equalsIgnoreCase("SUSPENDED")
				|| ped.getLicenseStatus().equalsIgnoreCase("REVOKED")) {
			pedlicensefield.setText(
					ped.getLicenseStatus() + " " + PedHistoryMath.parseExpirationDate(ped.getLicenseExpiration()));
			pedlicensefield.setStyle("-fx-text-fill: red !important; -fx-font-family: 'Inter 28pt Bold';");
			playAudio = true;
		} else if (ped.getLicenseStatus().equalsIgnoreCase("NONE")
				|| ped.getLicenseStatus().equalsIgnoreCase("UNLICENSED")) {
			pedlicensefield.setStyle("-fx-text-fill: red !important; -fx-font-family: 'Inter 28pt Bold';");
			playAudio = true;
		} else if (ped.getLicenseStatus().equalsIgnoreCase("VALID")) {
			pedlicensefield.setStyle("-fx-text-fill: #060 !important; -fx-font-family: 'Inter 28pt Bold';");
		}
		if (ped.getLicenseStatus().equalsIgnoreCase("NONE") || ped.getLicenseStatus().equalsIgnoreCase("UNLICENSED")) {
			peddriverlicexpirationfield.setText("No Data In System");
		} else {
			peddriverlicexpirationfield.setText(ped.getLicenseExpiration() != null ? ped.getLicenseExpiration() : "");
		}
		pedwantedfield.getStyleClass().clear();
		pedwantedfield.setOnMouseClicked(null);
		if (ped.getOutstandingWarrants() != null) {
			playAudio = true;
			pedwantedfield.setStyle("-fx-text-fill: red !important;");
			pedwantedfield.setText("WARRANT");
			pedWantedLabelField.setStyle("-fx-text-fill: red !important;");
			pedWantedLabelField
					.setText("WARRANT - [" + ped.getDateWarrantIssued() + "] [" + ped.getWarrantAgency() + "]");
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
			pedwantedfield.getStyleClass().add("license-value-field-valid");
			createWarrantInfoPopup(pedwantedfield,
					localization.getLocalizedMessage("PedLookup.WarrantInformationTitle",
							"Issued Warrant Information:"),
					ped.getName(), ped.getBirthday(), ped.getDateWarrantIssued(), ped.getWarrantNumber(),
					ped.getWarrantAgency(), ped.getOutstandingWarrants());
		} else {
			pedwantedfield.setText("False");
			pedwantedfield.getStyleClass().add("license-value-field");
			pedwantedfield.setStyle("-fx-text-fill: black !important;");
			pedWantedLabelField.setStyle("-fx-text-fill: black !important;");
			pedWantedLabelField.setText("");
		}
		if (ped.getGunLicenseStatus() == null || ped.getGunLicenseStatus().equalsIgnoreCase("Not Found")
				|| ped.getGunLicenseStatus().equalsIgnoreCase("None")) {
			pedgunlicensestatusfield.setText("False");
			pedgunlicensestatusfield.setStyle("-fx-text-fill: black !important;");
		} else if (ped.getGunLicenseStatus().equalsIgnoreCase("suspended")
				|| ped.getGunLicenseStatus().equalsIgnoreCase("expired")
				|| ped.getGunLicenseStatus().equalsIgnoreCase("revoked")) {
			pedgunlicensestatusfield.setStyle("-fx-text-fill: orange !important; -fx-font-family: 'Inter 28pt Bold';");
			pedgunlicensestatusfield.setText(
					ped.getGunLicenseStatus() + " "
							+ PedHistoryMath.parseExpirationDate(ped.getGunLicenseExpiration()));
		} else if (ped.getGunLicenseStatus().equalsIgnoreCase("valid")) {
			pedgunlicensestatusfield.setStyle("-fx-text-fill: #060 !important;");
			pedgunlicensestatusfield.setText("Valid");
		} else {
			logError("Unexpected gun license status: " + ped.getGunLicenseStatus());
			pedgunlicensestatusfield.setText(ped.getGunLicenseStatus());
			pedgunlicensestatusfield.setStyle("-fx-text-fill: red !important; -fx-font-family: 'Inter 28pt Bold';");
		}
		if (ped.getGunLicenseStatus() == null || ped.getGunLicenseStatus().equalsIgnoreCase("Not Found")
				|| ped.getGunLicenseStatus().equalsIgnoreCase("None")) {
			pedgunlicexpirationfield.setText("No Data In System");
		} else {
			pedgunlicexpirationfield
					.setText(ped.getGunLicenseExpiration() != null ? ped.getGunLicenseExpiration() : "");
		}
		pedgunlicnumberfield.setText(ped.getGunLicenseNumber() != null
				? ped.getGunLicenseNumber()
				: "No Data In System");
		pedgunlicclassfield.setText(ped.getGunLicenseClass() != null
				? ped.getGunLicenseClass()
				: "No Data In System");
		pedgunlicensetypefield.setText(ped.getGunLicenseType() != null
				? ped.getGunLicenseType()
				: "No Data In System");
		pedprobationstatusfield.setText(ped.getProbationStatus() != null ? ped.getProbationStatus() : "False");
		if (ped.getProbationStatus() != null && ped.getProbationStatus().equalsIgnoreCase("true")) {
			pedprobationstatusfield.setStyle("-fx-text-fill: red !important;");
			pedprobationstatusfield.setText("On Probation");
		} else {
			pedprobationstatusfield.setStyle("-fx-text-fill: black !important;");
		}
		if (ped.getFishingLicenseStatus() == null || ped.getFishingLicenseStatus().equalsIgnoreCase("Not Found")
				|| ped.getFishingLicenseStatus().equalsIgnoreCase("None")) {
			pedfishinglicstatusfield.setText("False");
			pedfishinglicstatusfield.setStyle("-fx-text-fill: black !important;");
		} else if (ped.getFishingLicenseStatus().equalsIgnoreCase("suspended")
				|| ped.getFishingLicenseStatus().equalsIgnoreCase("expired")
				|| ped.getFishingLicenseStatus().equalsIgnoreCase("revoked")) {
			pedfishinglicstatusfield.setStyle("-fx-text-fill: orange !important; -fx-font-family: 'Inter 28pt Bold';");
			pedfishinglicstatusfield.setText(
					ped.getFishingLicenseStatus() + " " + PedHistoryMath
							.parseExpirationDate(ped.getFishingLicenseExpiration()));
		} else if (ped.getFishingLicenseStatus().equalsIgnoreCase("valid")) {
			pedfishinglicstatusfield.setStyle("-fx-text-fill: #060 !important;");
			pedfishinglicstatusfield.setText("Valid");
		} else {
			logError("Unexpected fishing license status: " + ped.getFishingLicenseStatus());
			pedfishinglicstatusfield.setText(ped.getFishingLicenseStatus());
			pedfishinglicstatusfield.setStyle("-fx-text-fill: red !important; -fx-font-family: 'Inter 28pt Bold';");
		}
		if (ped.getFishingLicenseStatus() == null || ped.getFishingLicenseStatus().equalsIgnoreCase("Not Found")
				|| ped.getFishingLicenseStatus().equalsIgnoreCase("None")) {
			pedfishinglicexpirationfield.setText("No Data In System");
		} else {
			pedfishinglicexpirationfield
					.setText(ped.getFishingLicenseExpiration() != null ? ped.getFishingLicenseExpiration() : "");
		}
		pedfishinglicensenumberfield.setText(ped.getFishingLicenseNumber() != null
				? ped.getFishingLicenseNumber()
				: "No Data In System");
		if (ped.getBoatingLicenseStatus() == null || ped.getBoatingLicenseStatus().equalsIgnoreCase("Not Found")
				|| ped.getBoatingLicenseStatus().equalsIgnoreCase("None")) {
			pedboatinglicstatusfield.setText("False");
			pedboatinglicstatusfield.setStyle("-fx-text-fill: black !important;");
		} else if (ped.getBoatingLicenseStatus().equalsIgnoreCase("suspended")
				|| ped.getBoatingLicenseStatus().equalsIgnoreCase("expired")
				|| ped.getBoatingLicenseStatus().equalsIgnoreCase("revoked")) {
			pedboatinglicstatusfield.setStyle("-fx-text-fill: orange !important; -fx-font-family: 'Inter 28pt Bold';");
			pedboatinglicstatusfield.setText(
					ped.getBoatingLicenseStatus() + " " + PedHistoryMath
							.parseExpirationDate(ped.getBoatingLicenseExpiration()));
		} else if (ped.getBoatingLicenseStatus().equalsIgnoreCase("valid")) {
			pedboatinglicstatusfield.setStyle("-fx-text-fill: #060 !important;");
			pedboatinglicstatusfield.setText("Valid");
		} else {
			logError("Unexpected boating license status: " + ped.getBoatingLicenseStatus());
			pedboatinglicstatusfield.setText(ped.getBoatingLicenseStatus());
			pedboatinglicstatusfield.setStyle("-fx-text-fill: red !important; -fx-font-family: 'Inter 28pt Bold';");
		}
		if (ped.getBoatingLicenseStatus() == null || ped.getBoatingLicenseStatus().equalsIgnoreCase("Not Found")
				|| ped.getBoatingLicenseStatus().equalsIgnoreCase("None")) {
			pedboatinglicexpirationfield.setText("No Data In System");
		} else {
			pedboatinglicexpirationfield
					.setText(ped.getBoatingLicenseExpiration() != null ? ped.getBoatingLicenseExpiration() : "");
		}
		pedboatinglicensenumberfield.setText(ped.getBoatingLicenseNumber() != null
				? ped.getBoatingLicenseNumber()
				: "No Data In System");
		if (ped.getHuntingLicenseStatus() == null || ped.getHuntingLicenseStatus().equalsIgnoreCase("Not Found")
				|| ped.getHuntingLicenseStatus().equalsIgnoreCase("None")) {
			pedhuntinglicstatusfield.setText("False");
			pedhuntinglicstatusfield.setStyle("-fx-text-fill: black !important;");
		} else if (ped.getHuntingLicenseStatus().equalsIgnoreCase("suspended")
				|| ped.getHuntingLicenseStatus().equalsIgnoreCase("expired")
				|| ped.getHuntingLicenseStatus().equalsIgnoreCase("revoked")) {
			pedhuntinglicstatusfield.setStyle("-fx-text-fill: orange !important; -fx-font-family: 'Inter 28pt Bold';");
			pedhuntinglicstatusfield.setText(
					ped.getHuntingLicenseStatus() + " " + PedHistoryMath
							.parseExpirationDate(ped.getHuntingLicenseExpiration()));
		} else if (ped.getHuntingLicenseStatus().equalsIgnoreCase("valid")) {
			pedhuntinglicstatusfield.setStyle("-fx-text-fill: #060 !important;");
			pedhuntinglicstatusfield.setText("Valid");
		} else {
			logError("Unexpected hunting license status: " + ped.getHuntingLicenseStatus());
			pedhuntinglicstatusfield.setText(ped.getHuntingLicenseStatus());
			pedhuntinglicstatusfield.setStyle("-fx-text-fill: red !important; -fx-font-family: 'Inter 28pt Bold';");
		}
		if (ped.getHuntingLicenseStatus() == null || ped.getHuntingLicenseStatus().equalsIgnoreCase("Not Found")
				|| ped.getHuntingLicenseStatus().equalsIgnoreCase("None")) {
			pedhuntinglicexpirationfield.setText("No Data In System");
		} else {
			pedhuntinglicexpirationfield
					.setText(ped.getHuntingLicenseExpiration() != null ? ped.getHuntingLicenseExpiration() : "");
		}
		pedhuntinglicnumberfield.setText(ped.getHuntingLicenseNumber() != null
				? ped.getHuntingLicenseNumber()
				: "No Data In System");
		pedlicnumfield.setText(ped.getLicenseNumber() != null ? ped.getLicenseNumber() : "No Data In System");
		pedlicnumfield.setStyle(
				ped.getLicenseNumber() == null ? "-fx-text-fill: #E65C00 !important;" : "-fx-text-fill: black;");
		String affiliations = ped.getAffiliations();
		if (affiliations == null || affiliations.equalsIgnoreCase("No Data In System")) {
			pedaffiliationfield.setText("No Data In System");
		} else {
			pedaffiliationfield.setText(affiliations);
		}
		String flags = ped.getFlags();
		if (flags == null || flags.equalsIgnoreCase("No Data In System")) {
			pedflagfield.setText("No Data In System");
			pedFlagsLabelField.setText("");
			pedFlagsLabelField.setStyle("-fx-text-fill: black !important;");
		} else {
			pedflagfield.setText(flags);
			pedFlagsLabelField.setText(flags);
			pedFlagsLabelField.setStyle("-fx-text-fill: red !important;");
		}
		String description = ped.getDescription();
		if (description == null || description.equalsIgnoreCase("No Data In System")) {
			peddescfield.setText("No Data In System");
		} else {
			peddescfield.setText(description);
		}
		String aliases = ped.getAliases();
		if (aliases == null || aliases.equalsIgnoreCase("No Data In System")) {
			pedaliasfield.setText("No Data In System");
		} else {
			pedaliasfield.setText(aliases);
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
				pedtimesstoppedfield.setStyle(
						timesStoppedValue > 0 ? "-fx-text-fill: #E65C00 !important;" : "-fx-text-fill: black;");
			} catch (NumberFormatException e) {
				pedtimesstoppedfield.setText("0");
				pedtimesstoppedfield.setStyle("-fx-text-fill: black;");
			}
		}
		String height = ped.getHeight();
		if (height == null || height.trim().isEmpty()) {
			pedheightfield.setText("No Data In System");
			pedheightfield.setStyle("-fx-text-fill: black;");
		} else {
			pedheightfield.setText(height);
		}
		String weight = ped.getWeight();
		if (weight == null || weight.trim().isEmpty()) {
			pedweightfield.setText("No Data In System");
			pedweightfield.setStyle("-fx-text-fill: black;");
		} else {
			pedweightfield.setText(weight);
		}
		String citizenshipStatus = ped.getCitizenshipStatus();
		if (citizenshipStatus == null || citizenshipStatus.trim().isEmpty()) {
			pedcitizenshipstatusfield.setText("No Data In System");
			pedcitizenshipstatusfield.setStyle("-fx-text-fill: black !important;");
		} else {
			pedcitizenshipstatusfield.setStyle("-fx-text-fill: black !important;");
			pedcitizenshipstatusfield.setText(citizenshipStatus);
			if (citizenshipStatus.equalsIgnoreCase("flagged")) {
				pedcitizenshipstatusfield.setStyle("-fx-text-fill: red !important;");
				playAudio = true;
			}
		}
		String disabilityStatus = ped.getDisabilityStatus();
		if (disabilityStatus == null || disabilityStatus.trim().isEmpty()) {
			peddisabilityfield.setText("No Data In System");
			peddisabilityfield.setStyle("-fx-text-fill: black !important;");
		} else {
			peddisabilityfield.setStyle("-fx-text-fill: black !important;");
			peddisabilityfield.setText(disabilityStatus);
			if (disabilityStatus.equalsIgnoreCase("disabled")) {
				peddisabilityfield.setStyle("-fx-text-fill: red !important;");
			}
		}
		String maritalStatus = ped.getMaritalStatus();
		if (maritalStatus == null || maritalStatus.trim().isEmpty()) {
			pedmaritalstatusfield.setText("No Data In System");
			pedmaritalstatusfield.setStyle("-fx-text-fill: black;");
		} else {
			pedmaritalstatusfield.setText(maritalStatus);
		}
		ped6.setText(localization.getLocalizedMessage("PedLookup.FieldBirthday", "Birthday:") + " ("
				+ calculateAge(ped.getBirthday()) + ")");
		String pedModel = ped.getModel();
		if (pedModel != null && !pedModel.equalsIgnoreCase("Not Found")) {
			File pedImgFolder = new File(URLStrings.pedImageFolderURL);
			if (pedImgFolder.exists()) {
				logDebug("Detected pedImage folder..");
				try {
					if (ConfigReader.configRead("uiSettings", "enablePedVehImages").equalsIgnoreCase("true")) {
						File[] matchingFiles = pedImgFolder
								.listFiles((dir, name) -> name.equalsIgnoreCase(pedModel + ".jpg"));
						if (matchingFiles != null && matchingFiles.length > 0) {
							File matchingFile = matchingFiles[0];
							logInfo("Matching pedImage found: " + matchingFile.getName());
							try {
								String fileURI = matchingFile.toURI().toString();
								pedImageView.setImage(new Image(fileURI));
								noPedImageFoundlbl.setVisible(true);
								noPedImageFoundlbl.setText(localization
										.getLocalizedMessage("PedLookup.PedImageFoundlbl", "Image Found in File:"));
							} catch (Exception e) {
								setDefaultPedImage();
								logError("Could not set ped image: ", e);
							}
						} else {
							logWarn("No matching image found for the model: " + pedModel
									+ ", trying to use base image");
							Pattern pattern = Pattern.compile("\\[([^\\]]+)\\]");
							Matcher matcher = pattern.matcher(pedModel);
							String fallbackModel;
							if (matcher.find()) {
								fallbackModel = "[" + matcher.group(1) + "][0][0]";
								logDebug("Extracted base model: " + fallbackModel);
								File[] fallbackFiles = pedImgFolder
										.listFiles((dir, name) -> name.equalsIgnoreCase(fallbackModel + ".jpg"));
								if (fallbackFiles != null && fallbackFiles.length > 0) {
									File fallbackFile = fallbackFiles[0];
									logInfo("Using base model image: " + fallbackFile.getName());
									try {
										String fileURI = fallbackFile.toURI().toString();
										pedImageView.setImage(new Image(fileURI));
										noPedImageFoundlbl.setVisible(true);
										noPedImageFoundlbl.setText(localization.getLocalizedMessage(
												"PedLookup.PedImageFoundlbl", "Image Found in File:"));
									} catch (Exception e) {
										setDefaultPedImage();
										logError("Could not set ped image [2]: ", e);
									}
								}
							}
						}
					} else {
						logWarn("enablePedVehImages is disabled in settings so not displaying ped image");
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
		noPedImageFoundlbl
				.setText(localization.getLocalizedMessage("PedLookup.NoPedImageFoundlbl", "No Image Found In System"));
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

	private void setActiveGrid(Node pane) {
		pedBasicInfoGrid.setVisible(false);
		pedLicenseInfoGrid.setVisible(false);
		pedCriminalHistoryGrid.setVisible(false);
		if (pane != null) {
			pane.setVisible(true);
			pane.requestFocus();
		}
	}

	private void setupPedListView() {
		databaseListView.setCellFactory(param -> new ListCell<Ped>() {
			private final VBox layoutVBox = new VBox();
			private final Label nameLabel = new Label();
			private final HBox detailsBox1 = new HBox();
			private final Label dobLabel = new Label();
			private final Label addressLabel = new Label();
			private final HBox detailsBox2 = new HBox();
			private final Label wantedLabel = new Label();
			private final Label licenseNumberLabel = new Label();
			private final Label licenseStatusLabel = new Label();
			private final Label genderLabel = new Label();
			{
				String textColor = "black";
				try {
					textColor = ConfigReader.configRead("uiColors", "labelColor");
				} catch (Exception e) {
					logError("Error reading label color config, using default: ", e);
				}
				String secclr;
				try {
					secclr = ConfigReader.configRead("uiColors", "headingColor");
				} catch (Exception e) {
					logError("Error reading secondary color config, using default: ", e);
					secclr = "black";
				}
				String labelstyle = "-fx-font-family: 'Inter 24pt Regular'; -fx-text-fill: " + textColor + ";";
				dobLabel.setStyle(labelstyle);
				addressLabel.setStyle(labelstyle);
				wantedLabel.setStyle(labelstyle);
				licenseNumberLabel.setStyle(labelstyle);
				licenseStatusLabel.setStyle(labelstyle);
				genderLabel.setStyle(labelstyle);
				String titleStyle = "-fx-font-family: 'Inter 28pt Bold'; -fx-text-fill: " + secclr + ";";
				nameLabel.setStyle(
						"-fx-font-size: 16px; -fx-font-family: 'Inter 28pt Bold'; -fx-text-fill: " + secclr + ";");
				Label dobTitle = new Label("DOB:");
				dobTitle.setStyle(titleStyle);
				Label addressTitle = new Label("Address:");
				addressTitle.setStyle(titleStyle);
				Label wantedTitle = new Label("Wanted:");
				wantedTitle.setStyle(titleStyle);
				Label licNumTitle = new Label("License #:");
				licNumTitle.setStyle(titleStyle);
				Label licStatusTitle = new Label("License Status:");
				licStatusTitle.setStyle(titleStyle);
				Label genderTitle = new Label("Gender:");
				genderTitle.setStyle(titleStyle);
				Pane spacer1 = new Pane();
				HBox.setHgrow(spacer1, Priority.ALWAYS);
				Pane spacer2 = new Pane();
				HBox.setHgrow(spacer2, Priority.ALWAYS);
				detailsBox1.getChildren().addAll(nameLabel, spacer1, genderTitle, genderLabel, spacer2, dobTitle,
						dobLabel);
				Pane spacer3 = new Pane();
				HBox.setHgrow(spacer3, Priority.ALWAYS);
				Pane spacer4 = new Pane();
				HBox.setHgrow(spacer4, Priority.ALWAYS);
				detailsBox2.getChildren().addAll(wantedTitle, wantedLabel, spacer3, licNumTitle, licenseNumberLabel,
						spacer4,
						licStatusTitle, licenseStatusLabel);
				detailsBox1.setSpacing(4);
				detailsBox1.setAlignment(Pos.CENTER_LEFT);
				detailsBox2.setSpacing(4);
				detailsBox2.setAlignment(Pos.CENTER_LEFT);
				layoutVBox.getChildren().addAll(detailsBox1, detailsBox2);
				layoutVBox.setSpacing(3);
				layoutVBox.setPadding(new Insets(0, 10, 0, 10));
			}

			@Override
			protected void updateItem(Ped ped, boolean empty) {
				super.updateItem(ped, empty);
				if (empty || ped == null) {
					setText(null);
					setGraphic(null);
				} else {
					nameLabel.setText(toTitleCase(ped.getName()));
					genderLabel.setText(toTitleCase(ped.getGender()));
					dobLabel.setText(toTitleCase(ped.getBirthday()));
					wantedLabel.setText(toTitleCase(ped.getWantedStatus()));
					licenseNumberLabel.setText(toTitleCase(ped.getLicenseNumber()));
					licenseStatusLabel.setText(toTitleCase(ped.getLicenseStatus()));
					setGraphic(layoutVBox);
				}
			}
		});
		databaseListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Ped>() {
			@Override
			public void changed(ObservableValue<? extends Ped> observable, Ped oldValue, Ped newValue) {
				if (newValue != null) {
					String pedName = newValue.getName();
					boolean isRealPed = allRealPeds.stream().anyMatch(p -> Objects.equals(p.getName(), pedName));
					if (isRealPed) {
						String searchedName = pedName;
						logInfo("Searched: " + searchedName);
						PedObject worldPedObject = new PedObject(getServerDataFolderPath() + "ServerWorldPeds.data",
								searchedName);
						Optional<Ped> pedOptional = findPedByName(searchedName);
						Map<String, String> ownerSearch;
						try {
							ownerSearch = grabPedData(getServerDataFolderPath() + "ServerWorldCars.data", searchedName);
						} catch (IOException e) {
							logError("Error grabbing vehicle owner data: ", e);
							ownerSearch = Collections.emptyMap();
						}
						Ped processedPed = null;
						if (pedOptional.isPresent()) {
							logInfo("Found: [" + pedOptional.get().getName() + "] From PedHistory file");
							Ped ped = pedOptional.get();
							if (ped.getModel() == null) {
								ped.setModel("Not Found");
								try {
									Ped.PedHistoryUtils.addPed(ped);
								} catch (JAXBException e) {
									logError("Could not save new pedModel: ", e);
								}
								logWarn("Set pedModel as 'Not Found'");
							}
							processedPed = buildPedObject(false, ped.getName(), ped.getLicenseNumber(), ped.getModel(),
									ped.getBirthday(),
									ped.getGender(), ped.getAddress(), ped.getWantedStatus(), ped.getLicenseStatus(),
									ped.getLicenseExpiration(),
									ped.getGunLicenseType(), ped.getGunLicenseStatus(), ped.getGunLicenseExpiration(),
									ped.getFishingLicenseStatus(), ped.getFishingLicenseExpiration(),
									ped.getTimesStopped(),
									ped.getHuntingLicenseStatus(), ped.getHuntingLicenseExpiration(),
									ped.getParoleStatus(),
									ped.getProbationStatus(), ped.getHeight(), ped.getWeight(),
									ped.getDisabilityStatus(), ped.getMaritalStatus(), ped.getCitizenshipStatus(),
									ped.getIsPolice());
						} else if (worldPedObject.getName() != null && !worldPedObject.getName().equals("Not Found")) {
							logInfo("Found: [" + worldPedObject.getName() + "] From WorldPed file");
							processedPed = buildPedObject(false, worldPedObject.getName(),
									worldPedObject.getLicenseNumber(),
									worldPedObject.getModelName(), worldPedObject.getBirthday(),
									worldPedObject.getGender(),
									worldPedObject.getAddress(), worldPedObject.getIsWanted(),
									worldPedObject.getLicenseStatus(), worldPedObject.getLicenseExp(),
									worldPedObject.getWeaponPermitType(), worldPedObject.getWeaponPermitStatus(),
									worldPedObject.getWeaponPermitExpiration(), worldPedObject.getFishPermitStatus(),
									worldPedObject.getFishPermitExpiration(), worldPedObject.getTimesStopped(),
									worldPedObject.getHuntPermitStatus(),
									worldPedObject.getHuntPermitExpiration(), worldPedObject.getIsOnParole(),
									worldPedObject.getIsOnProbation(), worldPedObject.getHeight(),
									worldPedObject.getWeight(), worldPedObject.getDisabilityStatus(),
									worldPedObject.getMaritalStatus(),
									worldPedObject.getCitizenshipStatus(), worldPedObject.getIsPolice());
						} else if (isOwnerFound(ownerSearch)) {
							String ownerName = ownerSearch.get("owner");
							logInfo("Found Vehicle Owner: [" + ownerName + "] From WorldVeh file, plate#: "
									+ ownerSearch.get("licenseplate"));
							processedPed = buildPedObject(true, ownerName,
									ownerSearch.getOrDefault("ownerlicensenumber",
											null),
									ownerSearch.getOrDefault("ownermodel", null), ownerSearch.getOrDefault("ownerdob",
											null),
									ownerSearch.getOrDefault("ownergender", null),
									ownerSearch.getOrDefault("owneraddress", null),
									ownerSearch.getOrDefault("owneriswanted",
											null),
									ownerSearch.getOrDefault("ownerlicensestate",
											null),
									null, null, null, null,
									null, null, null, null, null, null, null, null, null, null, null, null, null);
						} else if (searchIDHisForName(searchedName)) {
							logWarn("Found Ped: [" + searchedName + "] From IDHistory (Possible Dead Ped)");
							ID searchedNameID = getHistoryIDFromName(searchedName);
							if (searchedNameID != null) {
								logDebug(searchedName + " HistoryID not null");
								processedPed = buildPedObject(false, searchedNameID.getName(),
										searchedNameID.getLicenseNumber(),
										searchedNameID.getPedModel(), searchedNameID.getBirthday(),
										searchedNameID.getGender(),
										searchedNameID.getAddress(), null, null, searchedNameID.getExpiration(), null,
										null, null, null, null,
										null, null, null,
										null, null, searchedNameID.getHeight(), searchedNameID.getWeight(), null, null,
										null, null);
							}
						}
						if (processedPed != null) {
							if (setPedRecordFields(processedPed)) {
								try {
									if (ConfigReader.configRead("soundSettings", "playLookupWarning")
											.equalsIgnoreCase("true")) {
										playSound(getJarPath() + "/sounds/alert-wanted.wav");
									}
								} catch (IOException e) {
									logError("Error getting configValue for playLookupWarning: ", e);
								}
							}
							databaseInfoPane.setVisible(true);
							databaseSearchPane.setVisible(false);
							setActiveGrid(pedBasicInfoGrid);
						} else {
							logError("No Ped With Name: [" + searchedName + "] Found Anywhere");
							showNotificationError("Ped Lookup",
									"Ped with name [" + searchedName + "] not found in any database.");
							databaseInfoPane.setVisible(false);
							databaseSearchPane.setVisible(true);
							setActiveGrid(pedBasicInfoGrid);
						}
					} else {
						logInfo("Selected Ped is not a real Ped, generating info");
						Ped ped = buildPedObject(false, newValue.getName(), newValue.getLicenseNumber(),
								newValue.getModel(), newValue.getBirthday(),
								newValue.getGender(), newValue.getAddress(), newValue.getWantedStatus(),
								newValue.getLicenseStatus(), newValue.getLicenseExpiration(),
								newValue.getGunLicenseType(), newValue.getGunLicenseStatus(),
								newValue.getGunLicenseExpiration(),
								newValue.getFishingLicenseStatus(), newValue.getFishingLicenseExpiration(),
								newValue.getTimesStopped(),
								newValue.getHuntingLicenseStatus(), newValue.getHuntingLicenseExpiration(),
								newValue.getParoleStatus(),
								newValue.getProbationStatus(), newValue.getHeight(), newValue.getWeight(),
								newValue.getDisabilityStatus(), newValue.getMaritalStatus(),
								newValue.getCitizenshipStatus(), newValue.getIsPolice());
						if (ped == null) {
							logError("No Ped With Name: [" + pedName + "] Found Anywhere [2]");
							showNotificationError("Ped Lookup",
									"Error creating ped object for name: " + pedName);
							databaseInfoPane.setVisible(false);
							databaseSearchPane.setVisible(true);
							setActiveGrid(pedBasicInfoGrid);
						} else {
							if (setPedRecordFields(ped)) {
								try {
									if (ConfigReader.configRead("soundSettings", "playLookupWarning")
											.equalsIgnoreCase("true")) {
										playSound(getJarPath() + "/sounds/alert-wanted.wav");
									}
								} catch (IOException e) {
									logError("Error getting configValue for playLookupWarning [2]: ", e);
								}
							}
							databaseInfoPane.setVisible(true);
							databaseSearchPane.setVisible(false);
							setActiveGrid(pedBasicInfoGrid);
						}
					}
				}
			}

			private boolean isOwnerFound(Map<String, String> ownerSearch) {
				String ownerName = ownerSearch.getOrDefault("owner", null);
				return ownerName != null && !ownerName.equalsIgnoreCase("Not Found")
						&& !ownerName.equalsIgnoreCase("Los Santos Police Department")
						&& !ownerName.equalsIgnoreCase("Los Santos Sheriff's Office")
						&& !ownerName.equalsIgnoreCase("Los Santos County Sheriff")
						&& !ownerName.equalsIgnoreCase(
								"Blaine County Sheriff's Office")
						&& !ownerName.equalsIgnoreCase("San Andreas Highway Patrol")
						&& !ownerName.equalsIgnoreCase("government");
			}
		});
		databaseListView.skinProperty().addListener((obs, oldSkin, newSkin) -> {
			if (newSkin == null) {
				return;
			}
			ScrollBar scrollBar = (ScrollBar) databaseListView.lookup(".scroll-bar:vertical");
			if (scrollBar != null) {
				scrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
					if (newValue.doubleValue() >= scrollBar.getMax() && !isLoading
							&& !isSearchFinite(pedSearchField.getText())) {
						loadMorePeds();
					}
				});
			}
		});
	}

	public static Ped performPedLookup(String name) {
		logInfo("Performing static lookup for: " + name);
		Optional<Ped> pedOptional = findPedByName(name);
		if (pedOptional.isPresent()) {
			logInfo("performPedLookup: Found [" + name + "] in PedHistory file");
			Ped ped = pedOptional.get();
			return buildPedObject(false, ped.getName(), ped.getLicenseNumber(),
					ped.getModel(), ped.getBirthday(),
					ped.getGender(), ped.getAddress(), ped.getWantedStatus(), ped.getLicenseStatus(),
					ped.getLicenseExpiration(),
					ped.getGunLicenseType(), ped.getGunLicenseStatus(), ped.getGunLicenseExpiration(),
					ped.getFishingLicenseStatus(), ped.getFishingLicenseExpiration(), ped.getTimesStopped(),
					ped.getHuntingLicenseStatus(), ped.getHuntingLicenseExpiration(), ped.getParoleStatus(),
					ped.getProbationStatus(), ped.getHeight(), ped.getWeight(),
					ped.getDisabilityStatus(), ped.getMaritalStatus(), ped.getCitizenshipStatus(), ped.getIsPolice());
		}
		PedObject worldPedObject = new PedObject(getServerDataFolderPath() + "ServerWorldPeds.data", name);
		if (worldPedObject.getName() != null && !worldPedObject.getName().equals("Not Found")) {
			logInfo("performPedLookup: Found [" + name + "] in WorldPed file");
			return buildPedObject(false, worldPedObject.getName(),
					worldPedObject.getLicenseNumber(),
					worldPedObject.getModelName(), worldPedObject.getBirthday(), worldPedObject.getGender(),
					worldPedObject.getAddress(), worldPedObject.getIsWanted(), worldPedObject.getLicenseStatus(),
					worldPedObject.getLicenseExp(),
					worldPedObject.getWeaponPermitType(), worldPedObject.getWeaponPermitStatus(),
					worldPedObject.getWeaponPermitExpiration(), worldPedObject.getFishPermitStatus(),
					worldPedObject.getFishPermitExpiration(), worldPedObject.getTimesStopped(),
					worldPedObject.getHuntPermitStatus(),
					worldPedObject.getHuntPermitExpiration(), worldPedObject.getIsOnParole(),
					worldPedObject.getIsOnProbation(), worldPedObject.getHeight(),
					worldPedObject.getWeight(), worldPedObject.getDisabilityStatus(),
					worldPedObject.getMaritalStatus(), worldPedObject.getCitizenshipStatus(),
					worldPedObject.getIsPolice());
		}
		Map<String, String> ownerSearch;
		try {
			ownerSearch = grabPedData(getServerDataFolderPath() + "ServerWorldCars.data", name);
		} catch (IOException e) {
			logError("performPedLookup: Error reading WorldVeh file for owner search: ", e);
			ownerSearch = new java.util.HashMap<>();
		}
		String ownerName = ownerSearch.getOrDefault("owner", null);
		if (ownerName != null && !ownerName.equalsIgnoreCase("Not Found")
				&& !ownerName.equalsIgnoreCase("Los Santos Police Department")
				&& !ownerName.equalsIgnoreCase("Los Santos Sheriff's Office")
				&& !ownerName.equalsIgnoreCase("Los Santos County Sheriff")
				&& !ownerName.equalsIgnoreCase("Blaine County Sheriff's Office")
				&& !ownerName.equalsIgnoreCase("San Andreas Highway Patrol")
				&& !ownerName.equalsIgnoreCase("government")) {
			logInfo("performPedLookup: Found Vehicle Owner [" + ownerName + "] in WorldVeh file");
			return buildPedObject(true, ownerName, ownerSearch.getOrDefault("ownerlicensenumber",
					null),
					ownerSearch.getOrDefault("ownermodel", null),
					ownerSearch.getOrDefault("ownerdob",
							null),
					ownerSearch.getOrDefault("ownergender", null), ownerSearch.getOrDefault("owneraddress", null),
					ownerSearch.getOrDefault("owneriswanted",
							null),
					ownerSearch.getOrDefault("ownerlicensestate",
							null),
					null, null, null, null, null, null, null, null, null, null, null, null, null, null,
					null, null, null);
		}
		try {
			Vehicle.Vehicles vehicleData = Vehicle.VehicleHistoryUtils.loadVehicles();
			if (vehicleData != null && vehicleData.getVehicleList() != null) {
				Optional<Vehicle> foundVehicle = vehicleData.getVehicleList().stream()
						.filter(v -> v != null && v.getOwner() != null && v.getOwner().equalsIgnoreCase(name))
						.findFirst();
				if (foundVehicle.isPresent()) {
					String foundOwnerName = foundVehicle.get().getOwner();
					logInfo("performPedLookup: Found Vehicle Owner [" + foundOwnerName + "] in vehhistory.xml");
					return buildPedObject(true, foundOwnerName, foundVehicle.get().getOwnerLicenseNumber(),
							foundVehicle.get().getOwnerModel(), foundVehicle.get().getOwnerDob(),
							foundVehicle.get().getOwnerGender(), foundVehicle.get().getOwnerAddress(),
							foundVehicle.get().getOwnerIsWanted(), foundVehicle.get().getOwnerLicenseState(), null,
							null,
							null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
				}
			}
		} catch (JAXBException e) {
			logError("performPedLookup: Error reading vehhistory.xml for owner search: ", e);
		}
		if (searchIDHisForName(name)) {
			logWarn("performPedLookup: Found [" + name + "] in IDHistory (Possible Dead Ped)");
			ID searchedNameID = getHistoryIDFromName(name);
			if (searchedNameID != null) {
				return buildPedObject(false, searchedNameID.getName(),
						searchedNameID.getLicenseNumber(),
						searchedNameID.getPedModel(), searchedNameID.getBirthday(), searchedNameID.getGender(),
						searchedNameID.getAddress(), null, null, searchedNameID.getExpiration(), null,
						null, null, null, null,
						null, null, null,
						null, null, searchedNameID.getHeight(), searchedNameID.getWeight(), null, null,
						null, null);
			}
		}
		logError("performPedLookup: No Ped with name [" + name + "] found anywhere.");
		return null;
	}

	private static Ped buildPedObject(boolean owner, String name_value, String licenseNumber_value,
			String modelName_value, String birthday_value, String gender_value, String address_value,
			String isWanted_value, String licenseStatus_value, String licenseExp_value, String weaponPermitType_value,
			String weaponPermitStatus_value, String weaponPermitExpiration_value, String fishPermitStatus_value,
			String fishPermitExpiration_value, String timesStopped_value, String huntPermitStatus_value,
			String huntPermitExpiration_value, String isOnParole_value, String isOnProbation_value, String height_value,
			String weight_value, String disabilityStatus_value, String maritalStatus_value,
			String citizenshipStatus_value, String isPolice_value) {
		Optional<Ped> searchedPed = owner ? findPedByName(name_value) : findPedByNumber(licenseNumber_value);
		Ped ped = searchedPed.orElseGet(Ped::new);
		boolean needsSave = !searchedPed.isPresent();
		if (ped.getName() == null && name_value != null) {
			ped.setName(name_value);
			needsSave = true;
		}
		if (ped.getGender() == null) {
			if (gender_value != null) {
				ped.setGender(gender_value);
			} else {
				String firstName = ped.getName() != null ? ped.getFirstName() : "";
				if (maleFirstNames.stream().anyMatch(name -> name.equalsIgnoreCase(firstName))) {
					ped.setGender("Male");
				} else if (femaleFirstNames.stream().anyMatch(name -> name.equalsIgnoreCase(firstName))) {
					ped.setGender("Female");
				} else {
					ped.setGender(calculateTrueFalseProbability("50") ? "Male" : "Female");
				}
			}
			needsSave = true;
		}
		if (ped.getName() == null) {
			Random random = new Random();
			String firstName = "Unknown";
			if ("Male".equalsIgnoreCase(ped.getGender()) && !maleFirstNames.isEmpty()) {
				firstName = maleFirstNames.get(random.nextInt(maleFirstNames.size()));
			} else if ("Female".equalsIgnoreCase(ped.getGender()) && !femaleFirstNames.isEmpty()) {
				firstName = femaleFirstNames.get(random.nextInt(femaleFirstNames.size()));
			}
			String lastName = PedHistoryMath.lastNames.get(random.nextInt(PedHistoryMath.lastNames.size()));
			ped.setName(firstName + " " + lastName);
			needsSave = true;
		}
		String pedId = ped.getName();
		if (ped.getLicenseNumber() == null) {
			if (licenseNumber_value != null) {
				ped.setLicenseNumber(licenseNumber_value);
			} else {
				String newLicenseNumber = generateLicenseNumber();
				logInfo("Ped [" + pedId + "] missing license number. Generated: " + newLicenseNumber);
				ped.setLicenseNumber(newLicenseNumber);
			}
			needsSave = true;
		}
		if (ped.getBirthday() == null) {
			if (birthday_value != null) {
				ped.setBirthday(birthday_value);
			} else {
				String newBirthday = generateBirthday(23, 65);
				logInfo("Ped [" + pedId + "] missing birthday. Generated: " + newBirthday);
				ped.setBirthday(newBirthday);
			}
			needsSave = true;
		}
		if (ped.getAddress() == null) {
			if (address_value != null) {
				ped.setAddress(address_value);
			} else {
				String newAddress = getRandomAddress();
				logInfo("Ped [" + pedId + "] missing address. Generated: " + newAddress);
				ped.setAddress(newAddress);
			}
			needsSave = true;
		}
		if (ped.getHeight() == null || ped.getWeight() == null) {
			if (height_value != null && weight_value != null) {
				ped.setHeight(height_value);
				ped.setWeight(weight_value);
			} else {
				String[] heightAndWeight = PedHistoryMath.generateHeightAndWeight(ped.getGender());
				logInfo("Ped [" + pedId + "] missing height/weight. Generated Height: " + heightAndWeight[0]
						+ ", Weight: " + heightAndWeight[1]);
				ped.setHeight(heightAndWeight[0]);
				ped.setWeight(heightAndWeight[1]);
			}
			needsSave = true;
		}
		if (ped.getMaritalStatus() == null) {
			if (maritalStatus_value != null) {
				ped.setMaritalStatus(maritalStatus_value);
			} else {
				String newMaritalStatus = PedHistoryMath.generateMaritalStatus(Integer.parseInt(ped.getAge()));
				logInfo("Ped [" + pedId + "] missing marital status. Generated: " + newMaritalStatus);
				ped.setMaritalStatus(newMaritalStatus);
			}
			needsSave = true;
		}
		if (ped.getCitizenshipStatus() == null) {
			if (citizenshipStatus_value != null) {
				ped.setCitizenshipStatus(citizenshipStatus_value);
			} else {
				String flaggedChance = "5";
				try {
					flaggedChance = ConfigReader.configRead("pedHistory", "chanceCitizenshipFlagged");
				} catch (IOException e) {
					logError("Error reading chanceCitizenshipFlagged config, using default [5]: ", e);
				}
				String newStatus = calculateTrueFalseProbability(flaggedChance) ? "FLAGGED" : "Citizen";
				logInfo("Ped [" + pedId + "] missing citizenship status. Generated: " + newStatus);
				ped.setCitizenshipStatus(newStatus);
			}
			needsSave = true;
		}
		if (ped.getDisabilityStatus() == null) {
			if (disabilityStatus_value != null) {
				ped.setDisabilityStatus(disabilityStatus_value);
			} else {
				int baseChance = 5;
				try {
					baseChance = Integer.parseInt(ConfigReader.configRead("pedHistory", "chancePedDisabled"));
				} catch (IOException | NumberFormatException e) {
					logError("Error reading chancePedDisabled config, using default [5]: ", e);
				}
				int age = 0;
				try {
					age = Integer.parseInt(ped.getAge());
				} catch (NumberFormatException e) {
					logWarn("Could not parse age for disability calculation for ped: " + ped.getName());
				}
				int finalChance = baseChance;
				if (age >= 45) {
					finalChance += 15;
				}
				finalChance = Math.min(finalChance, 95);
				String newStatus = calculateTrueFalseProbability(String.valueOf(finalChance)) ? "Disabled" : "None";
				logInfo("Ped [" + pedId + "] missing disability status. Age: " + age + ", Final Chance: " + finalChance
						+ "%. Generated: " + newStatus);
				ped.setDisabilityStatus(newStatus);
			}
			needsSave = true;
		}
		if (ped.getWantedStatus() == null) {
			if (isWanted_value != null) {
				ped.setWantedStatus(isWanted_value);
			} else {
				String newWantedStatus = calculateTrueFalseProbability("15") ? "true" : "false";
				logInfo("Ped [" + pedId + "] missing wanted status. Generated: " + newWantedStatus);
				ped.setWantedStatus(newWantedStatus);
			}
			needsSave = true;
		}
		if (ped.getLicenseStatus() == null) {
			if (licenseStatus_value != null) {
				ped.setLicenseStatus(licenseStatus_value);
			} else {
				String newLicenseStatus = calculateLicenseStatus(55, 22, 23);
				logInfo("Ped [" + pedId + "] missing license status. Generated: " + newLicenseStatus);
				ped.setLicenseStatus(newLicenseStatus);
			}
			needsSave = true;
		}
		if (ped.getLicenseExpiration() == null) {
			if (licenseExp_value != null) {
				ped.setLicenseExpiration(licenseExp_value);
			} else {
				String expiration;
				if ("Valid".equalsIgnoreCase(ped.getLicenseStatus())) {
					expiration = generateValidLicenseExpirationDate();
				} else if ("Expired".equalsIgnoreCase(ped.getLicenseStatus())) {
					expiration = generateExpiredLicenseExpirationDate(3);
				} else {
					expiration = ped.getLicenseStatus() + " License";
				}
				logInfo("Ped [" + pedId + "] generated license expiration: " + expiration);
				ped.setLicenseExpiration(expiration);
			}
			needsSave = true;
		}
		if (ped.getParoleStatus() == null) {
			if (isOnParole_value != null) {
				ped.setParoleStatus(isOnParole_value);
			} else {
				logInfo("Ped [" + pedId + "] missing parole status. Defaulted to: false");
				ped.setParoleStatus("false");
			}
			needsSave = true;
		}
		if (ped.getProbationStatus() == null) {
			if (isOnProbation_value != null) {
				ped.setProbationStatus(isOnProbation_value);
			} else {
				logInfo("Ped [" + pedId + "] missing probation status. Defaulted to: false");
				ped.setProbationStatus("false");
			}
			needsSave = true;
		}
		if (ped.getTimesStopped() == null) {
			if (timesStopped_value != null) {
				ped.setTimesStopped(timesStopped_value);
			} else {
				logInfo("Ped [" + pedId + "] missing times stopped. Defaulted to: 0");
				ped.setTimesStopped("0");
			}
			needsSave = true;
		}
		if (ped.getModel() == null) {
			if (modelName_value != null) {
				ped.setModel(modelName_value);
			} else {
				ArrayList<String> maleModels = new ArrayList<>(
						Arrays.asList("[ig_zimbor][0][0]", "[mp_m_weed_01][0][0]", "[s_m_m_bouncer_01][0][0]",
								"[s_m_m_postal_02][0][0]", "[s_m_y_waretech_01][0][0]", "[a_m_m_eastsa_01][0][0]"));
				ArrayList<String> femaleModels = new ArrayList<>(
						Arrays.asList("[a_f_m_bevhills_02][0][0]",
								"[a_f_y_femaleagent][0][0]", "[a_f_y_soucent_02][0][0]", "[csb_mrs_r][0][0]",
								"[mp_f_counterfeit_01][0][0]", "[mp_f_cardesign_01][0][0]"));
				Random random = new Random();
				String model;
				if ("female".equalsIgnoreCase(ped.getGender())) {
					model = femaleModels.get(random.nextInt(femaleModels.size()));
				} else {
					model = maleModels.get(random.nextInt(maleModels.size()));
				}
				logInfo("Ped [" + pedId + "] missing model. Generated: " + model);
				ped.setModel(model);
			}
			needsSave = true;
		}
		if ("true".equalsIgnoreCase(ped.getWantedStatus()) && ped.getOutstandingWarrants() == null) {
			try {
				String warrant = getRandomChargeWithWarrant(URLStrings.chargesFilePath);
				logInfo("Ped [" + pedId + "] is wanted, generating warrant details. Charge: "
						+ (warrant != null ? warrant : "WANTED - No details"));
				ped.setOutstandingWarrants(warrant != null ? warrant : "WANTED - No details");
				if (warrant != null) {
					String agency = getRandomDepartment();
					String warrantNum = generateLicenseNumber();
					String dateIssued = generateExpiredLicenseExpirationDate(5);
					logInfo("Ped [" + pedId + "] warrant generated. Agency: " + agency + ", Number: " + warrantNum
							+ ", Date: " + dateIssued);
					ped.setWarrantAgency(agency);
					ped.setWarrantNumber(warrantNum);
					ped.setDateWarrantIssued(dateIssued);
				}
				needsSave = true;
			} catch (Exception e) {
				logError("Could not set warrantStatus: ", e);
			}
		}
		if (ped.getArrestPriors() == null && ped.getCitationPriors() == null) {
			try {
				logInfo("Ped [" + pedId + "] missing prior history, generating...");
				int totalChargePriors = setArrestPriors(ped);
				int totalCitationPriors = setCitationPriors(ped);
				logInfo("Ped [" + pedId + "] generated priors. Arrests: " + totalChargePriors + ", Citations: "
						+ totalCitationPriors);
				if (totalChargePriors > 0 && ped.getParoleStatus() == null) {
					String paroleStatus = String.valueOf(
							calculateTrueFalseProbability(ConfigReader.configRead("pedHistory", "onParoleChance")));
					logInfo("Ped [" + pedId + "] generating parole status based on priors. Result: " + paroleStatus);
					ped.setParoleStatus(paroleStatus);
				}
				if (totalChargePriors > 0 && ped.getProbationStatus() == null) {
					String probationStatus = String.valueOf(
							calculateTrueFalseProbability(ConfigReader.configRead("pedHistory", "onProbationChance")));
					logInfo("Ped [" + pedId + "] generating probation status based on priors. Result: "
							+ probationStatus);
					ped.setProbationStatus(probationStatus);
				}
				if (ped.getTimesStopped() == null || "0".equals(ped.getTimesStopped())) {
					String totalStops = String.valueOf(calculateTotalStops(totalChargePriors + totalCitationPriors));
					logInfo("Ped [" + pedId + "] generating total stops based on priors. Result: " + totalStops);
					ped.setTimesStopped(totalStops);
				}
				if (ped.getFlags() == null) {
					int baseFlagFactor = Integer
							.parseInt(ConfigReader.configRead("pedHistory", "baseFlagProbability"));
					String flags = assignFlagsBasedOnPriors(totalChargePriors, baseFlagFactor, 0.9, 2);
					if (flags != null && !flags.isEmpty()) {
						logInfo("Ped [" + pedId + "] generating flags based on priors. Result: " + flags);
						ped.setFlags(flags);
					}
				}
				needsSave = true;
			} catch (Exception e) {
				logError("Could not set priors: ", e);
			}
		}
		try {
			if (ped.getFishingLicenseStatus() == null) {
				logInfo("Ped [" + pedId + "] missing fishing license, generating...");
				if (fishPermitStatus_value != null && !"not found".equalsIgnoreCase(fishPermitStatus_value)) {
					ped.setFishingLicenseStatus(fishPermitStatus_value);
				} else if (calculateTrueFalseProbability(ConfigReader.configRead("pedHistory", "hasFishingLicense"))) {
					String status = calculateLicenseStatus(55, 22, 23);
					logInfo("Ped [" + pedId + "] generated fishing license status: " + status);
					ped.setFishingLicenseStatus(status);
				} else {
					logInfo("Ped [" + pedId + "] generated fishing license status: None");
					ped.setFishingLicenseStatus("None");
				}
				needsSave = true;
			}
			if (!"None".equalsIgnoreCase(ped.getFishingLicenseStatus())) {
				if (ped.getFishingLicenseNumber() == null) {
					String number = generateLicenseNumber();
					logInfo("Ped [" + pedId + "] generated fishing license number: " + number);
					ped.setFishingLicenseNumber(number);
				}
				if (ped.getFishingLicenseExpiration() == null) {
					String expiration;
					if ("Valid".equalsIgnoreCase(ped.getFishingLicenseStatus()))
						expiration = generateValidLicenseExpirationDate();
					else if ("Expired".equalsIgnoreCase(ped.getFishingLicenseStatus()))
						expiration = generateExpiredLicenseExpirationDate(3);
					else
						expiration = ped.getFishingLicenseStatus() + " License";
					logInfo("Ped [" + pedId + "] generated fishing license expiration: " + expiration);
					ped.setFishingLicenseExpiration(expiration);
				}
				needsSave = true;
			}
		} catch (IOException e) {
			logError("Could not set fishingLicenseStatus: ", e);
		}
		try {
			if (ped.getHuntingLicenseStatus() == null) {
				logInfo("Ped [" + pedId + "] missing hunting license, generating...");
				if (huntPermitStatus_value != null && !"not found".equalsIgnoreCase(huntPermitStatus_value)) {
					ped.setHuntingLicenseStatus(huntPermitStatus_value);
				} else if (calculateTrueFalseProbability(ConfigReader.configRead("pedHistory", "hasHuntingLicense"))) {
					String status = calculateLicenseStatus(55, 22, 23);
					logInfo("Ped [" + pedId + "] generated hunting license status: " + status);
					ped.setHuntingLicenseStatus(status);
				} else {
					logInfo("Ped [" + pedId + "] generated hunting license status: None");
					ped.setHuntingLicenseStatus("None");
				}
				needsSave = true;
			}
			if (!"None".equalsIgnoreCase(ped.getHuntingLicenseStatus())) {
				if (ped.getHuntingLicenseNumber() == null) {
					String number = generateLicenseNumber();
					logInfo("Ped [" + pedId + "] generated hunting license number: " + number);
					ped.setHuntingLicenseNumber(number);
				}
				if (ped.getHuntingLicenseExpiration() == null) {
					String expiration;
					if ("Valid".equalsIgnoreCase(ped.getHuntingLicenseStatus()))
						expiration = generateValidLicenseExpirationDate();
					else if ("Expired".equalsIgnoreCase(ped.getHuntingLicenseStatus()))
						expiration = generateExpiredLicenseExpirationDate(3);
					else
						expiration = ped.getHuntingLicenseStatus() + " License";
					logInfo("Ped [" + pedId + "] generated hunting license expiration: " + expiration);
					ped.setHuntingLicenseExpiration(expiration);
				}
				needsSave = true;
			}
		} catch (IOException e) {
			logError("Could not set huntingLicenseStatus: ", e);
		}
		try {
			if (ped.getBoatingLicenseStatus() == null) {
				logInfo("Ped [" + pedId + "] missing boating license, generating...");
				if (calculateTrueFalseProbability(ConfigReader.configRead("pedHistory", "hasBoatingLicense"))) {
					String status = calculateLicenseStatus(55, 22, 23);
					logInfo("Ped [" + pedId + "] generated boating license status: " + status);
					ped.setBoatingLicenseStatus(status);
				} else {
					logInfo("Ped [" + pedId + "] generated boating license status: None");
					ped.setBoatingLicenseStatus("None");
				}
				needsSave = true;
			}
			if (!"None".equalsIgnoreCase(ped.getBoatingLicenseStatus())) {
				if (ped.getBoatingLicenseNumber() == null) {
					String number = generateLicenseNumber();
					logInfo("Ped [" + pedId + "] generated boating license number: " + number);
					ped.setBoatingLicenseNumber(number);
				}
				if (ped.getBoatingLicenseExpiration() == null) {
					String expiration;
					if ("Valid".equalsIgnoreCase(ped.getBoatingLicenseStatus()))
						expiration = generateValidLicenseExpirationDate();
					else if ("Expired".equalsIgnoreCase(ped.getBoatingLicenseStatus()))
						expiration = generateExpiredLicenseExpirationDate(3);
					else
						expiration = ped.getBoatingLicenseStatus() + " License";
					logInfo("Ped [" + pedId + "] generated boating license expiration: " + expiration);
					ped.setBoatingLicenseExpiration(expiration);
				}
				needsSave = true;
			}
		} catch (IOException e) {
			logError("Could not set boatingLicenseStatus: ", e);
		}
		try {
			if (ped.getGunLicenseStatus() == null) {
				logInfo("Ped [" + pedId + "] missing gun license, generating...");
				if (weaponPermitStatus_value != null) {
					ped.setGunLicenseStatus(weaponPermitStatus_value);
				} else if (calculateTrueFalseProbability(
						ConfigReader.configRead("pedHistoryGunPermit", "hasGunLicense"))) {
					String status = calculateLicenseStatus(55, 22, 23);
					logInfo("Ped [" + pedId + "] generated gun license status: " + status);
					ped.setGunLicenseStatus(status);
				} else {
					logInfo("Ped [" + pedId + "] generated gun license status: None");
					ped.setGunLicenseStatus("None");
				}
				needsSave = true;
			}
			if (!"None".equalsIgnoreCase(ped.getGunLicenseStatus())) {
				if (ped.getGunLicenseNumber() == null) {
					String number = generateLicenseNumber();
					logInfo("Ped [" + pedId + "] generated gun license number: " + number);
					ped.setGunLicenseNumber(number);
				}
				if (ped.getGunLicenseType() == null) {
					String type = weaponPermitType_value != null ? weaponPermitType_value : "Concealed Carry";
					logInfo("Ped [" + pedId + "] generated gun license type: " + type);
					ped.setGunLicenseType(type);
				}
				if (ped.getGunLicenseClass() == null) {
					String licenseClass = getGunLicenseClass();
					logInfo("Ped [" + pedId + "] generated gun license class: " + licenseClass);
					ped.setGunLicenseClass(licenseClass);
				}
				if (ped.getGunLicenseExpiration() == null) {
					String expiration;
					if ("Valid".equalsIgnoreCase(ped.getGunLicenseStatus()))
						expiration = generateValidLicenseExpirationDate();
					else if ("Expired".equalsIgnoreCase(ped.getGunLicenseStatus()))
						expiration = generateExpiredLicenseExpirationDate(3);
					else
						expiration = ped.getGunLicenseStatus() + " License";
					logInfo("Ped [" + pedId + "] generated gun license expiration: " + expiration);
					ped.setGunLicenseExpiration(expiration);
				}
				needsSave = true;
			}
		} catch (IOException e) {
			logError("Could not set gunLicenseStatus: ", e);
		}
		if (ped.getIsPolice() == null) {
			if (isPolice_value != null) {
				ped.setIsPolice(isPolice_value);
			} else {
				boolean isCleanRecord = (ped.getArrestPriors() == null || ped.getArrestPriors().isEmpty()) &&
						(ped.getCitationPriors() == null || ped.getCitationPriors().isEmpty()) &&
						("false".equalsIgnoreCase(ped.getParoleStatus()) || ped.getParoleStatus() == null) &&
						("false".equalsIgnoreCase(ped.getProbationStatus()) || ped.getProbationStatus() == null) &&
						("0".equals(ped.getTimesStopped()) || ped.getTimesStopped() == null) &&
						("false".equalsIgnoreCase(ped.getWantedStatus()) || ped.getWantedStatus() == null);
				String probabilityIsPolice;
				try {
					probabilityIsPolice = ConfigReader.configRead("pedHistory", "chancePedIsPolice");
				} catch (IOException e) {
					logError("Error reading config for chancePedIsPolice: ", e);
					probabilityIsPolice = "5";
				}
				String probability = isCleanRecord ? probabilityIsPolice : "0";
				String newIsPoliceValue = calculateTrueFalseProbability(probability) ? "True" : "False";
				logInfo("Ped [" + pedId + "] missing isPolice. Clean Record: " + isCleanRecord + ". Generated with "
						+ probability + "% chance: " + newIsPoliceValue);
				ped.setIsPolice(newIsPoliceValue);
			}
			needsSave = true;
		}
		if (needsSave) {
			try {
				logInfo("Ped: [" + pedId + "] has new generated data and needs to be saved to PedHistory...");
				Ped.PedHistoryUtils.addPed(ped);
			} catch (JAXBException e) {
				logError("Error saving updated ped to PedHistory: ", e);
			}
		}
		return ped;
	}

	private Ped createMatchingPed(List<String> potentialFirstNames, List<String> potentialLastNames,
			String searchText) {
		Ped ped = new Ped();
		Random random = new Random();
		String firstName = generateMatchingFirstName(potentialFirstNames, searchText);
		String lastName = generateMatchingLastName(potentialLastNames, searchText);
		ped.setName(firstName + " " + lastName);
		// Determine gender based on the chosen first name
		if (maleFirstNames.stream().anyMatch(name -> name.equalsIgnoreCase(firstName))) {
			ped.setGender("Male");
		} else if (femaleFirstNames.stream().anyMatch(name -> name.equalsIgnoreCase(firstName))) {
			ped.setGender("Female");
		} else {
			// Fallback if the name isn't in either list
			ped.setGender(random.nextBoolean() ? "Male" : "Female");
		}
		ped.setBirthday(PedHistoryMath.generateBirthday(18, 70));
		ped.setAddress(PedHistoryMath.getRandomAddress());
		ped.setWantedStatus(PedHistoryMath.calculateTrueFalseProbability("10") ? "True" : "False");
		String[] statuses = { "Valid", "Valid", "Valid", "Valid", "Valid", "Expired", "Suspended", "None" };
		String licenseStatus = statuses[random.nextInt(statuses.length)];
		ped.setLicenseStatus(licenseStatus);
		if (!"None".equalsIgnoreCase(licenseStatus)) {
			String licenseNumber = generateLicenseNumber();
			ped.setLicenseNumber(licenseNumber);
		} else {
			ped.setLicenseNumber(null);
		}
		return ped;
	}

	private String generateMatchingFirstName(List<String> potentialFirstNames, String searchText) {
		Random random = new Random();
		String[] nameParts = searchText.trim().toLowerCase().split("\\s+", 2);
		String firstNamePrefix = nameParts[0];
		if (potentialFirstNames != null && !potentialFirstNames.isEmpty()) {
			return potentialFirstNames.get(random.nextInt(potentialFirstNames.size()));
		} else {
			// If no potential names match, choose randomly from combined lists
			List<String> allFirstNames = new ArrayList<>();
			allFirstNames.addAll(maleFirstNames);
			allFirstNames.addAll(femaleFirstNames);
			if (!allFirstNames.isEmpty()) {
				return allFirstNames.get(random.nextInt(allFirstNames.size()));
			}
		}
		// Absolute fallback
		return firstNamePrefix.substring(0, 1).toUpperCase() + firstNamePrefix.substring(1);
	}

	private String generateMatchingLastName(List<String> potentialLastNames, String searchText) {
		Random random = new Random();
		String[] nameParts = searchText.trim().toLowerCase().split("\\s+", 2);
		if (nameParts.length > 1) {
			String lastNamePrefix = nameParts[1];
			if (potentialLastNames != null && !potentialLastNames.isEmpty()) {
				return potentialLastNames.get(random.nextInt(potentialLastNames.size()));
			} else {
				return lastNamePrefix.substring(0, 1).toUpperCase() + lastNamePrefix.substring(1);
			}
		} else {
			return PedHistoryMath.lastNames.get(random.nextInt(PedHistoryMath.lastNames.size()));
		}
	}

	// #region Event Handlers
	@javafx.fxml.FXML
	public void onPedSearchBtnClick(ActionEvent actionEvent) throws IOException {
	}

	@javafx.fxml.FXML
	public void pedAddDataToNotes(ActionEvent actionEvent) throws IOException {
		if (!databaseInfoPane.isVisible()) {
			String name = "";
			String age;
			String gender;
			String address;
			String description;
			StringBuilder fullString = new StringBuilder();
			if (pedfnamefield != null && !pedfnamefield.getText().isEmpty() && pedlnamefield != null
					&& !pedlnamefield.getText().isEmpty()) {
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
		Map<String, Object> citationReportMap = (Map<String, Object>) trafficCitationObj
				.get(localization.getLocalizedMessage("ReportWindows.CitationReportTitle", "Citation Report") + " Map");
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
		Map<String, Object> arrestReportMap = (Map<String, Object>) arrestReportObj
				.get(localization.getLocalizedMessage("ReportWindows.ArrestReportTitle", "Arrest Report") + " Map");
		TextField offenderName = (TextField) arrestReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
		TextField offenderAge = (TextField) arrestReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"));
		TextField offenderGender = (TextField) arrestReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"));
		TextField offenderAddress = (TextField) arrestReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"));
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
		if (searchedLicenseNum == null || searchedLicenseNum.isEmpty()
				|| searchedLicenseNum.equalsIgnoreCase("No Data In System")) {
			showNotificationError("Update Error", "Cannot update a Ped with no License Number.");
			return;
		}
		Optional<Ped> optionalPed = findPedByNumber(searchedLicenseNum);
		if (!optionalPed.isPresent()) {
			showNotificationError("Update Error", "Could not find Ped with License #: " + searchedLicenseNum);
			return;
		}
		Ped ped = optionalPed.get();
		// --- Styling and Layout ---
		String bkgColor = "#F0F2F5";
		String cardColor = "#FFFFFF";
		String shadowColor = "rgba(0, 0, 0, 0.1)";
		String saveBtnColor = "#28a745";
		String cancelBtnColor = "#6c757d";
		VBox editorContent = new VBox(20);
		editorContent.setPadding(new Insets(20));
		editorContent.setStyle("-fx-background-color: " + bkgColor + ";");
		ScrollPane scrollPane = new ScrollPane(editorContent);
		scrollPane.setFitToWidth(true);
		scrollPane.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		BorderPane layoutPane = new BorderPane();
		layoutPane.setCenter(scrollPane);
		layoutPane.setPrefSize(750, 650);
		layoutPane.setStyle("-fx-background-color: " + bkgColor + ";");
		Map<String, TextField> fieldMap = new HashMap<>();
		// --- Biographical Information ---
		Map<String, String> bioFields = new LinkedHashMap<>();
		bioFields.put("Gender", ped.getGender());
		bioFields.put("Birthday", ped.getBirthday());
		bioFields.put("Address", ped.getAddress());
		bioFields.put("Description", ped.getDescription());
		bioFields.put("Height", ped.getHeight());
		bioFields.put("Weight", ped.getWeight());
		bioFields.put("Marital Status", ped.getMaritalStatus());
		bioFields.put("Citizenship Status", ped.getCitizenshipStatus());
		bioFields.put("Disability Status", ped.getDisabilityStatus());
		editorContent.getChildren().add(createSection("Biographical Information", bioFields, fieldMap));
		// --- Status & Identification ---
		Map<String, String> statusFields = new LinkedHashMap<>();
		statusFields.put("Police Status", ped.getIsPolice());
		statusFields.put("Aliases", ped.getAliases());
		statusFields.put("Affiliations", ped.getAffiliations());
		statusFields.put("Flags", ped.getFlags());
		editorContent.getChildren().add(createSection("Status & Identification", statusFields, fieldMap));
		// --- Legal & Criminal ---
		Map<String, String> legalFields = new LinkedHashMap<>();
		legalFields.put("Wanted Status", ped.getWantedStatus());
		legalFields.put("Outstanding Warrants", ped.getOutstandingWarrants());
		legalFields.put("Warrant Issued Date", ped.getDateWarrantIssued());
		legalFields.put("Warrant Number", ped.getWarrantNumber());
		legalFields.put("Warrant Agency", ped.getWarrantAgency());
		legalFields.put("Parole Status", ped.getParoleStatus());
		legalFields.put("Probation Status", ped.getProbationStatus());
		legalFields.put("Times Stopped", ped.getTimesStopped());
		editorContent.getChildren().add(createSection("Legal & Criminal Status", legalFields, fieldMap));
		// --- Licenses ---
		Map<String, String> driverLicFields = new LinkedHashMap<>();
		driverLicFields.put("License Status", ped.getLicenseStatus());
		driverLicFields.put("License Expiration", ped.getLicenseExpiration());
		editorContent.getChildren().add(createSection("Driver's License", driverLicFields, fieldMap));
		Map<String, String> gunLicFields = new LinkedHashMap<>();
		gunLicFields.put("Gun License Status", ped.getGunLicenseStatus());
		gunLicFields.put("Gun License Expiration", ped.getGunLicenseExpiration());
		gunLicFields.put("Gun License Number", ped.getGunLicenseNumber());
		gunLicFields.put("Gun License Type", ped.getGunLicenseType());
		gunLicFields.put("Gun License Class", ped.getGunLicenseClass());
		editorContent.getChildren().add(createSection("Firearms License", gunLicFields, fieldMap));
		Map<String, String> otherLicFields = new LinkedHashMap<>();
		otherLicFields.put("Fishing License Status", ped.getFishingLicenseStatus());
		otherLicFields.put("Fishing License Expiration", ped.getFishingLicenseExpiration());
		otherLicFields.put("Fishing License Number", ped.getFishingLicenseNumber());
		otherLicFields.put("Boating License Status", ped.getBoatingLicenseStatus());
		otherLicFields.put("Boating License Expiration", ped.getBoatingLicenseExpiration());
		otherLicFields.put("Boating License Number", ped.getBoatingLicenseNumber());
		otherLicFields.put("Hunting License Status", ped.getHuntingLicenseStatus());
		otherLicFields.put("Hunting License Expiration", ped.getHuntingLicenseExpiration());
		otherLicFields.put("Hunting License Number", ped.getHuntingLicenseNumber());
		editorContent.getChildren().add(createSection("Other Licenses", otherLicFields, fieldMap));
		// --- Buttons ---
		Button saveButton = new Button("Save Changes");
		saveButton.setStyle("-fx-background-color: " + saveBtnColor
				+ "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 8 16;");
		Button cancelButton = new Button("Cancel");
		cancelButton.setStyle("-fx-background-color: " + cancelBtnColor
				+ "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 8 16;");
		HBox buttonBox = new HBox(15, saveButton, cancelButton);
		buttonBox.setAlignment(Pos.CENTER_RIGHT);
		buttonBox.setPadding(new Insets(15, 20, 15, 20));
		buttonBox.setStyle("-fx-background-color: " + cardColor + "; -fx-border-color: " + shadowColor
				+ " transparent transparent transparent; -fx-border-width: 1;");
		layoutPane.setBottom(buttonBox);
		// --- Window ---
		CustomWindow editorWindow = WindowManager.createCustomWindow(mainDesktopControllerObj.getDesktopContainer(),
				layoutPane, "Edit Ped: " + ped.getName(), true, 1, true, true,
				mainDesktopControllerObj.getTaskBarApps(),
				new Image(Objects.requireNonNull(
						Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Apps/setting.png"))));
		// --- Actions ---
		saveButton.setOnAction(event -> {
			logInfo("Saving changes for Ped: " + ped.getName());
			fieldMap.forEach((key, textField) -> {
				String value = textField.getText();
				String finalValue = (value == null || value.trim().isEmpty()
						|| value.equalsIgnoreCase("No Data In System")) ? null : value.trim();
				switch (key) {
					case "Police Status":
						ped.setIsPolice(finalValue);
						break;
					case "Gender":
						ped.setGender(finalValue);
						break;
					case "Birthday":
						ped.setBirthday(finalValue);
						break;
					case "Address":
						ped.setAddress(finalValue);
						break;
					case "Description":
						ped.setDescription(finalValue);
						break;
					case "Height":
						ped.setHeight(finalValue);
						break;
					case "Weight":
						ped.setWeight(finalValue);
						break;
					case "Marital Status":
						ped.setMaritalStatus(finalValue);
						break;
					case "Citizenship Status":
						ped.setCitizenshipStatus(finalValue);
						break;
					case "Disability Status":
						ped.setDisabilityStatus(finalValue);
						break;
					case "Aliases":
						ped.setAliases(finalValue);
						break;
					case "Affiliations":
						ped.setAffiliations(finalValue);
						break;
					case "Flags":
						ped.setFlags(finalValue);
						break;
					case "Wanted Status":
						ped.setWantedStatus(finalValue);
						break;
					case "Outstanding Warrants":
						ped.setOutstandingWarrants(finalValue);
						break;
					case "Warrant Issued Date":
						ped.setDateWarrantIssued(finalValue);
						break;
					case "Warrant Number":
						ped.setWarrantNumber(finalValue);
						break;
					case "Warrant Agency":
						ped.setWarrantAgency(finalValue);
						break;
					case "License Status":
						ped.setLicenseStatus(finalValue);
						break;
					case "License Expiration":
						ped.setLicenseExpiration(finalValue);
						break;
					case "Parole Status":
						ped.setParoleStatus(finalValue);
						break;
					case "Probation Status":
						ped.setProbationStatus(finalValue);
						break;
					case "Times Stopped":
						ped.setTimesStopped(finalValue);
						break;
					case "Gun License Status":
						ped.setGunLicenseStatus(finalValue);
						break;
					case "Gun License Expiration":
						ped.setGunLicenseExpiration(finalValue);
						break;
					case "Gun License Number":
						ped.setGunLicenseNumber(finalValue);
						break;
					case "Gun License Type":
						ped.setGunLicenseType(finalValue);
						break;
					case "Gun License Class":
						ped.setGunLicenseClass(finalValue);
						break;
					case "Fishing License Status":
						ped.setFishingLicenseStatus(finalValue);
						break;
					case "Fishing License Expiration":
						ped.setFishingLicenseExpiration(finalValue);
						break;
					case "Fishing License Number":
						ped.setFishingLicenseNumber(finalValue);
						break;
					case "Boating License Status":
						ped.setBoatingLicenseStatus(finalValue);
						break;
					case "Boating License Expiration":
						ped.setBoatingLicenseExpiration(finalValue);
						break;
					case "Boating License Number":
						ped.setBoatingLicenseNumber(finalValue);
						break;
					case "Hunting License Status":
						ped.setHuntingLicenseStatus(finalValue);
						break;
					case "Hunting License Expiration":
						ped.setHuntingLicenseExpiration(finalValue);
						break;
					case "Hunting License Number":
						ped.setHuntingLicenseNumber(finalValue);
						break;
				}
			});
			try {
				Ped.PedHistoryUtils.addPed(ped);
				showNotificationInfo("Update Successful", "Ped record for '" + ped.getName() + "' has been updated.");
				setPedRecordFields(ped); // Refresh the main view
			} catch (JAXBException e) {
				logError("Could not save updated ped record: ", e);
				showNotificationError("Update Failed", "Could not save ped record to file.");
			}
			editorWindow.closeWindow();
		});
		cancelButton.setOnAction(event -> editorWindow.closeWindow());
	}

	private VBox createSection(String title, Map<String, String> fields, Map<String, TextField> fieldMap) {
		VBox sectionBox = new VBox(15);
		sectionBox.setPadding(new Insets(20));
		sectionBox.setStyle(
				"-fx-background-color: #FFFFFF; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0.1, 0, 2);");
		Label titleLabel = new Label(title);
		titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1D2C4D;");
		titleLabel.setPadding(new Insets(0, 0, 5, 0));
		titleLabel.setBorder(new javafx.scene.layout.Border(new javafx.scene.layout.BorderStroke(Color.web("#DDDDDD"),
				javafx.scene.layout.BorderStrokeStyle.SOLID, null, new javafx.scene.layout.BorderWidths(0, 0, 1, 0))));
		titleLabel.setMaxWidth(Double.MAX_VALUE);
		sectionBox.getChildren().add(titleLabel);
		GridPane grid = new GridPane();
		grid.setVgap(12);
		grid.setHgap(15);
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(30);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(70);
		grid.getColumnConstraints().addAll(col1, col2);
		int rowIndex = 0;
		for (Map.Entry<String, String> entry : fields.entrySet()) {
			addStyledEditorField(grid, entry.getKey(), entry.getValue(), rowIndex++, fieldMap);
		}
		sectionBox.getChildren().add(grid);
		return sectionBox;
	}

	private void addStyledEditorField(GridPane grid, String labelText, String value, int row,
			Map<String, TextField> map) {
		Label label = new Label(labelText + ":");
		label.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #555555;");
		label.setAlignment(Pos.CENTER_LEFT);
		TextField textField = new TextField(value != null ? value : "No Data In System");
		textField.setStyle(
				"-fx-background-color: #F8F9FA; -fx-border-color: #CED4DA; -fx-border-radius: 4; -fx-padding: 5 8; -fx-font-size: 13px;");
		textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal) {
				textField.setStyle(
						"-fx-background-color: #FFFFFF; -fx-border-color: #0078D7; -fx-border-radius: 4; -fx-padding: 5 8; -fx-font-size: 13px; -fx-effect: dropshadow(gaussian, rgba(0,120,215,0.3), 5, 0, 0, 0);");
			} else {
				textField.setStyle(
						"-fx-background-color: #F8F9FA; -fx-border-color: #CED4DA; -fx-border-radius: 4; -fx-padding: 5 8; -fx-font-size: 13px;");
			}
		});
		grid.add(label, 0, row);
		grid.add(textField, 1, row);
		GridPane.setValignment(label, javafx.geometry.VPos.CENTER);
		map.put(labelText, textField);
	}

	@javafx.fxml.FXML
	public void backLabelClicked(MouseEvent actionEvent) {
		databaseInfoPane.setVisible(false);
		databaseSearchPane.setVisible(true);
		setActiveGrid(null);
		pedSearchField.setText("");
	}

	@javafx.fxml.FXML
	public void basicInfoClicked(MouseEvent actionEvent) {
		setActiveGrid(pedBasicInfoGrid);
	}

	@javafx.fxml.FXML
	public void licenseInfoClicked(MouseEvent actionEvent) {
		setActiveGrid(pedLicenseInfoGrid);
	}

	@javafx.fxml.FXML
	public void criminalHistoryClicked(MouseEvent actionEvent) {
		setActiveGrid(pedCriminalHistoryGrid);
	}

	@FXML
	private void openSettingsWindow(ActionEvent event) throws IOException {
		BorderPane layoutPane = new BorderPane();
		layoutPane.setPrefSize(450, 380);
		layoutPane.setStyle("-fx-background-color: #F4F4F4;");
		GridPane settingsGrid = new GridPane();
		settingsGrid.setVgap(12);
		settingsGrid.setHgap(15);
		settingsGrid.setPadding(new Insets(20));
		settingsGrid.setAlignment(Pos.CENTER);
		ColorPicker sidePaneColorPicker = new ColorPicker();
		ColorPicker sidePaneTextColorPicker = new ColorPicker();
		ColorPicker labelColorPicker = new ColorPicker();
		ColorPicker backgroundColorPicker = new ColorPicker();
		ColorPicker headingColorPicker = new ColorPicker();
		ColorPicker cardBackgroundColorPicker = new ColorPicker();
		ColorPicker buttonColorPicker = new ColorPicker();
		addSettingRow(settingsGrid, "Side Pane Color:", sidePaneColorPicker, 0);
		addSettingRow(settingsGrid, "Side Pane Text Color:", sidePaneTextColorPicker, 1);
		addSettingRow(settingsGrid, "Label Color (Black/White):", labelColorPicker, 2);
		addSettingRow(settingsGrid, "Background Color:", backgroundColorPicker, 3);
		addSettingRow(settingsGrid, "Heading Color:", headingColorPicker, 4);
		addSettingRow(settingsGrid, "Card Background Color:", cardBackgroundColorPicker, 5);
		addSettingRow(settingsGrid, "Button Color:", buttonColorPicker, 6);
		sidePaneColorPicker.setValue(loadColorFromConfig("sidePaneColor", Color.web("#323c41")));
		sidePaneTextColorPicker.setValue(loadColorFromConfig("sidePaneTextColor", Color.WHITE));
		labelColorPicker.setValue(loadColorFromConfig("labelColor", Color.web("#323c41")));
		backgroundColorPicker.setValue(loadColorFromConfig("bkgColor", Color.WHITE));
		headingColorPicker.setValue(loadColorFromConfig("headingColor", Color.web("#323c41")));
		cardBackgroundColorPicker.setValue(loadColorFromConfig("cardBkgColor", Color.web("#f6f6f6")));
		buttonColorPicker.setValue(loadColorFromConfig("buttonColor", Color.web("#3c484e")));
		Button saveButton = new Button("Save");
		saveButton.setStyle("-fx-background-color:rgb(92, 142, 93); -fx-text-fill: white; -fx-font-weight: bold;");
		Button cancelButton = new Button("Cancel");
		Button resetDefaultsButton = new Button("Reset Defaults");
		resetDefaultsButton.setStyle("-fx-background-color: #E0E0E0; -fx-text-fill: black; -fx-font-weight: bold;");
		HBox buttonBox = new HBox(10, resetDefaultsButton, saveButton, cancelButton);
		buttonBox.setAlignment(Pos.CENTER_RIGHT);
		buttonBox.setPadding(new Insets(15));
		layoutPane.setCenter(settingsGrid);
		layoutPane.setBottom(buttonBox);
		CustomWindow settingsWindow = WindowManager.createCustomWindow(
				mainDesktopControllerObj.getDesktopContainer(),
				layoutPane,
				"UI Color Settings",
				true,
				1,
				true,
				true,
				mainDesktopControllerObj.getTaskBarApps(),
				new Image(Objects.requireNonNull(
						Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Apps/setting.png"))));
		saveButton.setOnAction(e -> {
			ConfigWriter.configwrite("uiColors", "sidePaneColor", toWebString(sidePaneColorPicker.getValue()));
			ConfigWriter.configwrite("uiColors", "sidePaneTextColor",
					toWebString(sidePaneTextColorPicker.getValue()));
			ConfigWriter.configwrite("uiColors", "labelColor", toWebString(labelColorPicker.getValue()));
			ConfigWriter.configwrite("uiColors", "bkgColor", toWebString(backgroundColorPicker.getValue()));
			ConfigWriter.configwrite("uiColors", "headingColor", toWebString(headingColorPicker.getValue()));
			ConfigWriter.configwrite("uiColors", "cardBkgColor",
					toWebString(cardBackgroundColorPicker.getValue()));
			ConfigWriter.configwrite("uiColors", "buttonColor", toWebString(buttonColorPicker.getValue()));
			logInfo("UI color settings have been updated successfully.");
			loadTheming();
			settingsWindow.closeWindow();
		});
		cancelButton.setOnAction(e -> settingsWindow.closeWindow());
		resetDefaultsButton.setOnAction(e -> {
			sidePaneColorPicker.setValue(Color.web("#323c41"));
			sidePaneTextColorPicker.setValue(Color.WHITE);
			labelColorPicker.setValue(Color.web("#323c41"));
			backgroundColorPicker.setValue(Color.WHITE);
			headingColorPicker.setValue(Color.web("#323c41"));
			cardBackgroundColorPicker.setValue(Color.web("#f6f6f6"));
			buttonColorPicker.setValue(Color.web("#3c484e"));
		});
	}

	// #endregion
	// #region Getters
	public Label getBackLabel() {
		return backLabel;
	}

	public VBox getSidePane() {
		return sidePane;
	}

	public Label getBasicInfoSideButton() {
		return basicInfoSideButton;
	}

	public Label getLicenseInfoSideButton() {
		return licenseInfoSideButton;
	}

	public Label getCriminalHistorySideButton() {
		return criminalHistorySideButton;
	}

	public ListView getDatabaseListView() {
		return databaseListView;
	}

	public TextField getPedSearchField() {
		return pedSearchField;
	}

	public ScrollPane getPedCriminalHistoryGrid() {
		return pedCriminalHistoryGrid;
	}

	public ScrollPane getPedLicenseInfoGrid() {
		return pedLicenseInfoGrid;
	}

	public ScrollPane getPedBasicInfoGrid() {
		return pedBasicInfoGrid;
	}

	public BorderPane getDatabaseInfoPane() {
		return databaseInfoPane;
	}

	public BorderPane getDatabaseSearchPane() {
		return databaseSearchPane;
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

	public AnchorPane getLookupPane() {
		return lookupPane;
	}

	public Button getAddDataToNotesBtn() {
		return addDataToNotesBtn;
	}

	public Label getLbl1() {
		return lbl1;
	}

	public Button getInfobtn3() {
		return infobtn3;
	}

	// #endregion
	// #region Dont Rename
	// Dont rename
	public TextField getpeddobfield() {
		return peddobfield;
	}

	// Dont rename
	public TextField getpedgenfield() {
		return pedgenfield;
	}

	// Dont rename
	public TextField getpedaddressfield() {
		return pedaddressfield;
	}

	// Dont rename
	public TextField getpeddescfield() {
		return peddescfield;
	}

	// Dont rename
	public TextField getpedlnamefield() {
		return pedlnamefield;
	}

	// Dont rename
	public TextField getpedfnamefield() {
		return pedfnamefield;
	}

	// Dont rename
	public TextField getpedtimesstoppedfield() {
		return pedtimesstoppedfield;
	}

	// Dont rename
	public TextField getpedlicnumfield() {
		return pedlicnumfield;
	}

	// dont rename
	public TextField getpedflagfield() {
		return pedflagfield;
	}

	// dont rename
	public TextField getpedaffiliationfield() {
		return pedaffiliationfield;
	}

	// dont rename
	public TextField getpedaliasfield() {
		return pedaliasfield;
	}

	// dont rename
	public TextField getpedwantedfield() {
		return pedwantedfield;
	}

	// dont rename
	public TextField getpedprobationstatusfield() {
		return pedprobationstatusfield;
	}

	// dont rename
	public TextField getpedparolestatusfield() {
		return pedparolestatusfield;
	}

	// dont rename
	public TextField getpedboatinglicstatusfield() {
		return pedboatinglicstatusfield;
	}

	// dont rename
	public TextField getpedfishinglicstatusfield() {
		return pedfishinglicstatusfield;
	}

	// dont rename
	public TextField getpedhuntinglicstatusfield() {
		return pedhuntinglicstatusfield;
	}

	// dont rename
	public TextField getpedgunlicensestatusfield() {
		return pedgunlicensestatusfield;
	}

	// dont rename
	public TextField getpedlicensefield() {
		return pedlicensefield;
	}

	// dont rename
	public TextField getpedmaritalstatusfield() {
		return pedmaritalstatusfield;
	}

	// dont rename
	public TextField getpeddisabilityfield() {
		return peddisabilityfield;
	}

	// dont rename
	public TextField getpedcitizenshipstatusfield() {
		return pedcitizenshipstatusfield;
	}

	// Dont rename
	public TextField getpedheightfield() {
		return pedheightfield;
	}

	// Dont rename
	public TextField getpedweightfield() {
		return pedweightfield;
	}

	// Dont rename
	public TextField getpeddriverlicenseexpirationfield() {
		return peddriverlicexpirationfield;
	}

	// #endregion
	private void addSettingRow(GridPane grid, String labelText, ColorPicker colorPicker, int rowIndex) {
		Label label = new Label(labelText);
		label.setStyle("-fx-font-weight: bold;");
		colorPicker.setPrefWidth(150);
		grid.add(label, 0, rowIndex);
		grid.add(colorPicker, 1, rowIndex);
	}

	public static Color loadColorFromConfig(String key, Color defaultColor) {
		try {
			String colorStr = ConfigReader.configRead("uiColors", key);
			if (colorStr != null && !colorStr.trim().isEmpty()) {
				return Color.web(colorStr);
			}
		} catch (Exception e) {
			logError("Could not read color for key '" + key + "'. Using default.", e);
		}
		return defaultColor;
	}

	public static String toWebString(Color color) {
		int r = (int) (color.getRed() * 255);
		int g = (int) (color.getGreen() * 255);
		int b = (int) (color.getBlue() * 255);
		return String.format("#%02x%02x%02x", r, g, b);
	}

	private void loadTheming() {
		Color sidePaneTextColor = loadColorFromConfig("sidePaneTextColor", Color.WHITE);
		Color labelColor = loadColorFromConfig("labelColor", Color.BLACK);
		Color headingColor = loadColorFromConfig("headingColor", Color.web("#0078D7"));
		Color cardBkgColor = loadColorFromConfig("cardBkgColor", Color.web("#FFFFFF"));
		Color buttonColor = loadColorFromConfig("buttonColor", Color.web("#DCDCDC"));
		noPedImageFoundlbl.setStyle("-fx-text-fill: " + toWebString(labelColor)
				+ " !important; -fx-font-family: 'Inter 24pt Regular';");
		sidePane.setStyle("-fx-background-color: " + toWebString(
				loadColorFromConfig("sidePaneColor", Color.web("#3E3E3E"))) + ";");
		root.setStyle(
				"-fx-background-color: " + toWebString(loadColorFromConfig("bkgColor", Color.web("#F4F4F4"))) + ";");
		for (Node card : Arrays.asList(arresthistorycard, citationhistorycard, statuscard, licensecard, huntingcard,
				boatingcard, fishingcard, gunliccard, basicinfocard, otherinfocard, physicalinfocard, pedimagecard)) {
			card.setStyle("-fx-background-color: " + toWebString(cardBkgColor)
					+ "; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.08), 10, 0.1, 0, 2);");
		}
		for (Label heading : Arrays.asList(criminalHistoryHeading, licenseinfoheading, pedFullNameLabelField,
				lookupmainlbl, lbl1)) {
			heading.setStyle(
					"-fx-text-fill: " + toWebString(headingColor)
							+ " !important; -fx-font-family: 'Inter 28pt Bold';");
		}
		for (Label label : Arrays.asList(ped11, ped23, ped4, ped14, ped13, ped12, statusSubheading, ped21, ped22,
				driverLicSubheading, huntLicSubheading, boatLicSubheading, fishlicSubheading, gunlicSubheading, ped5,
				ped9, pedNEWdriverlicexpiration, ped20, pedNEWhuntinglicexpiration, pedNEWhuntinglicnumber, ped18,
				pedNEWfishinglicexpiration, pedNEWfishinglicnumber, ped19, pedNEWboatinglicexpiration,
				pedNEWboatinglicnumber, ped15, pedNEWgunlicclass, pedNEWgunlicensetype, pedNEWgunlicexpiration,
				pedNEWgunlicnum, ped8, ped10, pedNEWheight, pedNEWweight, ped1, ped2, ped3, ped7, ped6,
				pedNEWmaritalstatus, pedNEWdisability, pedNEWcitizenshipstatus, basicinfosubheading,
				pedPoliceLabelField, pedWantedLabelField, pedFlagsLabelField)) {
			label.setStyle(
					"-fx-text-fill: " + toWebString(labelColor)
							+ " !important; -fx-font-family: 'Inter 24pt Regular'; -fx-border-color: "
							+ toWebString(
									loadColorFromConfig("sidePaneColor", Color.web(
											"#3E3E3E")))
							+ ";");
		}
		for (Label sidebutton : Arrays.asList(basicInfoSideButton, licenseInfoSideButton, criminalHistorySideButton)) {
			sidebutton.setStyle("-fx-text-fill: " + toWebString(sidePaneTextColor)
					+ " !important; -fx-font-family: 'Inter 28pt Medium';");
		}
		backLabel.setStyle("-fx-text-fill: " + toWebString(sidePaneTextColor)
				+ " !important; -fx-font-family: 'Inter 28pt Medium'; -fx-cursor: hand; -fx-border-color: "
				+ toWebString(sidePaneTextColor) + "; -fx-border-width: 0.3; -fx-padding: 1 10;");
		basicinfobutton.setStyle("-fx-background-color: " + toWebString(buttonColor) + " !important;");
		licenseinfobutton.setStyle("-fx-background-color: " + toWebString(buttonColor) + " !important;");
		criminalhistorybutton.setStyle("-fx-background-color: " + toWebString(buttonColor) + " !important;");
		infobtn3.setStyle("-fx-background-color: " + toWebString(buttonColor) + " !important;");
		addDataToNotesBtn.setStyle("-fx-background-color: " + toWebString(buttonColor) + " !important;");
		settingsButton.setStyle("-fx-background-color: " + toWebString(buttonColor) + " !important;");
	}

	public void initialize() {
		noPedImageFoundlbl.setVisible(false);
		databaseSearchPane.setVisible(true);
		databaseInfoPane.setVisible(false);
		pedCriminalHistoryGrid.setVisible(false);
		pedLicenseInfoGrid.setVisible(false);
		pedBasicInfoGrid.setVisible(false);
		setupPedListView();
		setupSearchListener();
		pedSearchField.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.ENTER) {
				try {
					onPedSearchBtnClick(new ActionEvent());
				} catch (IOException e) {
					logError("Error executing pedsearch from Enter: ", e);
				}
			}
		});
		Platform.runLater(() -> {
			addLocalization();
			loadAllRealPeds(true);
			loadTheming();
			logInfo("Ped Lookup View initialized successfully.");
		});
	}

	// TODO: add to all controllers
	@Override
	public void shutdown() {
		logInfo("Shutting down Ped Lookup View and all resources...");
		if (currentSearchTask != null && currentSearchTask.isRunning()) {
			currentSearchTask.cancel();
		}
		searchExecutor.shutdownNow();
		if (masterPedList != null) {
			masterPedList.clear();
		}
		if (allRealPeds != null) {
			allRealPeds.clear();
		}
		if (pedLookupViewController == this) {
			pedLookupViewController = null;
		}
	}
}