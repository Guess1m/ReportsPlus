package com.Guess.ReportsPlus.Windows.Apps;

import static com.Guess.ReportsPlus.Launcher.localization;
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
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.generateBirthday;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.generateExpiredLicenseExpirationDate;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.generateLicenseNumber;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.generateValidLicenseExpirationDate;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.getRandomAddress;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.getRandomChargeWithWarrant;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.getRandomDepartment;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.parseExpirationDate;
import static com.Guess.ReportsPlus.util.Misc.AudioUtil.playSound;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logDebug;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logInfo;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logWarn;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationError;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.createLabels;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getGunLicenseClass;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getJarPath;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getServerDataFolderPath;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.setArrestPriors;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.setCitationPriors;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.toTitleCase;
import static com.Guess.ReportsPlus.util.Server.recordUtils.grabPedData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.logs.LookupObjects.PedObject;
import com.Guess.ReportsPlus.logs.LookupObjects.WorldPedUtils;
import com.Guess.ReportsPlus.util.History.IDHistory;
import com.Guess.ReportsPlus.util.History.Ped;
import com.Guess.ReportsPlus.util.History.PedHistoryMath;
import com.Guess.ReportsPlus.util.Misc.Threading.WorkerThread;
import com.Guess.ReportsPlus.util.Other.NoteTab;
import com.Guess.ReportsPlus.util.Server.Objects.ID.ID;
import com.Guess.ReportsPlus.util.Strings.URLStrings;

import jakarta.xml.bind.JAXBException;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

public class PedLookupViewControllerCopy {

	// #region FXML Variables

	@FXML
	private ListView databaseListView;

	@FXML
	private BorderPane root;

	@FXML
	private AnchorPane lookupPane;

	@FXML
	private Label lookupmainlbl;

	@FXML
	private ScrollPane pedPane;

	@FXML
	private AnchorPane pedLookupPane;

	@FXML
	private AnchorPane pedRecordPane;

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
	private Label info1;

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
	private Label info2;

	@FXML
	private Label info5;

	@FXML
	private Label info3;

	@FXML
	private TextField pedtimesstoppedfield;

	@FXML
	private Label ped14;

	@FXML
	private TextField pedgenfield;

	@FXML
	private Label ped3;

	@FXML
	private Label info4;

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
	private Label noRecordFoundLabelPed;

	@FXML
	private TableView databaseTableView;

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
	private final List<String> recentPedSearches = new ArrayList<>();
	private final ExecutorService searchExecutor = Executors.newSingleThreadExecutor(r -> {
		WorkerThread t = new WorkerThread("Ped-Search-Thread", r);
		t.setDaemon(true);
		return t;
	});

	private Task<?> currentSearchTask;
	private boolean isLoading = false;

	public void initialize() {
		noPedImageFoundlbl.setVisible(false);
		pedRecordPane.setVisible(false);
		noRecordFoundLabelPed.setVisible(false);

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

		addLocalization();
		loadAllRealPeds(true);
	}

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

			{
				String textColor = "black";
				try {
					if (ConfigReader.configRead("uiColors", "UIDarkMode").equalsIgnoreCase("true")) {
						textColor = UIDarkColor;
					} else {
						textColor = UILightColor;
					}
				} catch (Exception e) {
					logError("Error reading UI color config, using default text color: ", e);
				}

				String mainclr;
				try {
					mainclr = ConfigReader.configRead("uiColors", "mainColor");
				} catch (Exception e) {
					logError("Error reading main color config, using default: ", e);
					mainclr = "black";
				}

				String secclr;
				try {
					secclr = ConfigReader.configRead("uiColors", "secondaryColor");
				} catch (Exception e) {
					logError("Error reading secondary color config, using default: ", e);
					secclr = "black";
				}

				String labelstyle = "-fx-font-family: 'Inter 24pt Regular'; -fx-text-fill: " + textColor
						+ ";";
				dobLabel.setStyle(labelstyle);
				addressLabel.setStyle(labelstyle);
				wantedLabel.setStyle(labelstyle);
				licenseNumberLabel.setStyle(labelstyle);
				licenseStatusLabel.setStyle(labelstyle);

				String titleStyle = "-fx-font-family: 'Inter 28pt Bold'; -fx-text-fill: " + secclr
						+ ";";
				nameLabel.setStyle("-fx-font-size: 16px; -fx-font-family: 'Inter 28pt Bold'; -fx-text-fill: "
						+ mainclr + ";");

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

				Pane spacer1 = new Pane();
				HBox.setHgrow(spacer1, Priority.ALWAYS);
				Pane spacer2 = new Pane();
				HBox.setHgrow(spacer2, Priority.ALWAYS);
				detailsBox1.getChildren().addAll(nameLabel, spacer1, licNumTitle, licenseNumberLabel, spacer2, dobTitle,
						dobLabel);

				Pane spacer3 = new Pane();
				HBox.setHgrow(spacer3, Priority.ALWAYS);
				Pane spacer4 = new Pane();
				HBox.setHgrow(spacer4, Priority.ALWAYS);
				detailsBox2.getChildren().addAll(wantedTitle, wantedLabel, spacer3, addressTitle, addressLabel, spacer4,
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
					dobLabel.setText(toTitleCase(ped.getBirthday()));
					addressLabel.setText(toTitleCase(ped.getAddress()));
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
					System.out.println("Selected Ped: " + pedName);
					System.out.println("Is Real Ped: " + (isRealPed ? "real" : "not real"));

					// TODO: !inprogress
					// Load ped data into fields
					// generate data if not real ped
				}
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

	private List<String> prefilterFirstNames(String searchText) {
		String firstNamePrefix = searchText.toLowerCase().split(" ", 2)[0];
		return PedHistoryMath.genericFirstNames.stream()
				.filter(name -> name.toLowerCase().startsWith(firstNamePrefix))
				.collect(Collectors.toList());
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

	private Ped createMatchingPed(List<String> potentialFirstNames, List<String> potentialLastNames,
			String searchText) {
		Ped ped = new Ped();
		Random random = new Random();

		ped.setName(generateMatchingFullName(potentialFirstNames, potentialLastNames, searchText));
		ped.setGender(random.nextBoolean() ? "Male" : "Female");
		ped.setBirthday(PedHistoryMath.generateBirthday(18, 70));
		ped.setAddress(PedHistoryMath.getRandomAddress());
		ped.setWantedStatus(PedHistoryMath.calculateTrueFalseProbability("20") ? "Yes" : "No");

		String[] statuses = { "Valid", "Valid", "Valid", "Valid", "Valid", "Expired", "Suspended", "None" };
		String licenseStatus = statuses[random.nextInt(statuses.length)];
		ped.setLicenseStatus(licenseStatus);

		if (!"None".equalsIgnoreCase(licenseStatus)) {
			String licenseNumber = generateLicenseNumber();
			ped.setLicenseNumber(licenseNumber);
		} else {
			ped.setLicenseNumber("N/A");
		}

		return ped;
	}

	private String generateMatchingFullName(List<String> potentialFirstNames, List<String> potentialLastNames,
			String searchText) {
		Random random = new Random();
		String[] nameParts = searchText.trim().toLowerCase().split("\\s+", 2);
		String firstNamePrefix = nameParts[0];

		String firstName;
		if (potentialFirstNames == null || potentialFirstNames.isEmpty()) {
			firstName = firstNamePrefix.substring(0, 1).toUpperCase() + firstNamePrefix.substring(1);
		} else {
			firstName = potentialFirstNames.get(random.nextInt(potentialFirstNames.size()));
		}

		String lastName;
		if (nameParts.length > 1) {
			String lastNamePrefix = nameParts[1];
			if (potentialLastNames == null || potentialLastNames.isEmpty()) {
				lastName = lastNamePrefix.substring(0, 1).toUpperCase() + lastNamePrefix.substring(1);
			} else {
				lastName = potentialLastNames.get(random.nextInt(potentialLastNames.size()));
			}
		} else {
			lastName = PedHistoryMath.lastNames.get(random.nextInt(PedHistoryMath.lastNames.size()));
		}

		return firstName + " " + lastName;
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
			noRecordFoundLabelPed.setVisible(masterPedList.isEmpty() && !searchText.isEmpty());
			isLoading = false;
		});

		currentSearchTask.setOnFailed(event -> {
			logError("Ped search task failed: ", currentSearchTask.getException());
			isLoading = false;
		});

		currentSearchTask.setOnCancelled(event -> isLoading = false);

		isLoading = true;
		masterPedList.clear();
		noRecordFoundLabelPed.setVisible(false);
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

	public void createLicenseInfoPopup(TextField label, String headerText, String name, String dob, String exp,
			String status, String licnum) {
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
			if (status.toLowerCase().equalsIgnoreCase("expired")) {
				statusField.setText(status + " " + parseExpirationDate(exp));
			} else {
				statusField.setText(status);
			}
			licNumField.setText(licnum);

			Label nameLabel = createLabel(localization.getLocalizedMessage("PedLookup.NameLabel", "Name:"));
			Label dobLabel = createLabel(
					localization.getLocalizedMessage("PedLookup.DateOfBirthLabel", "Date of Birth:"));
			GridPane.setColumnIndex(dobLabel, 1);
			nameLabel.setMinWidth(Region.USE_PREF_SIZE);

			Label expDateLabel = createLabel(
					localization.getLocalizedMessage("PedLookup.ExpDateLabel", "Expiration Date:"));
			GridPane.setRowIndex(expDateLabel, 2);
			expDateLabel.setMinWidth(Region.USE_PREF_SIZE);

			Label licStatusLabel = createLabel(
					localization.getLocalizedMessage("PedLookup.FieldLicenseStatus", "License Status:"));
			GridPane.setColumnIndex(licStatusLabel, 1);
			GridPane.setRowIndex(licStatusLabel, 2);
			licStatusLabel.setMinWidth(Region.USE_PREF_SIZE);

			Label licNumLabel = createLabel(
					localization.getLocalizedMessage("PedLookup.FieldLicenseNumber", "License Number:"));
			GridPane.setRowIndex(licNumLabel, 4);
			licNumLabel.setMinWidth(Region.USE_PREF_SIZE);

			if (ConfigReader.configRead("uiColors", "UIDarkMode").equalsIgnoreCase("true")) {
				nameLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				dobLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				expDateLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				licStatusLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				licNumLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				Node node = nameField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UIDarkColor + " !important;");
				node = dobField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UIDarkColor + " !important;");
				node = expField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UIDarkColor + " !important;");
				node = statusField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UIDarkColor + " !important;");
				node = licNumField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UIDarkColor + " !important;");
			} else {
				nameLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				dobLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				expDateLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				licStatusLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				licNumLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				Node node = nameField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UILightColor + " !important;");
				node = dobField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UILightColor + " !important;");
				node = expField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UILightColor + " !important;");
				node = statusField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UILightColor + " !important;");
				node = licNumField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UILightColor + " !important;");
			}

			gridPane.getChildren().addAll(nameField, dobField, expField, statusField, licNumField, nameLabel, dobLabel,
					expDateLabel, licStatusLabel, licNumLabel);

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

	public void createGunLicenseInfoPopup(TextField label, String headerText, String name, String dob, String exp,
			String status, String licnum, String gunLicType, String gunLicClass) {
		try {
			AnchorPane popupContent = new AnchorPane();
			popupContent.setPrefWidth(Region.USE_COMPUTED_SIZE);
			popupContent.getStylesheets()
					.add(Launcher.class.getResource("/com/Guess/ReportsPlus/css/lookups/lookup.css").toExternalForm());

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
			if (status.toLowerCase().equalsIgnoreCase("expired")) {
				statusField.setText(status + " " + parseExpirationDate(exp));
			} else {
				statusField.setText(status);
			}
			licNumField.setText(licnum);
			gunLicTypeField.setText(gunLicType);
			gunLicClassField.setText(gunLicClass);

			Label nameLabel = createLabel(localization.getLocalizedMessage("PedLookup.NameLabel", "Name:"));
			Label dobLabel = createLabel(
					localization.getLocalizedMessage("PedLookup.DateOfBirthLabel", "Date of Birth:"));
			GridPane.setColumnIndex(dobLabel, 1);
			nameLabel.setMinWidth(Region.USE_PREF_SIZE);
			dobLabel.setMinWidth(Region.USE_PREF_SIZE);

			Label expDateLabel = createLabel(
					localization.getLocalizedMessage("PedLookup.ExpDateLabel", "Expiration Date:"));
			GridPane.setRowIndex(expDateLabel, 2);
			expDateLabel.setMinWidth(Region.USE_PREF_SIZE);

			Label licStatusLabel = createLabel(
					localization.getLocalizedMessage("PedLookup.FieldLicenseStatus", "License Status:"));
			GridPane.setColumnIndex(licStatusLabel, 1);
			GridPane.setRowIndex(licStatusLabel, 2);
			licStatusLabel.setMinWidth(Region.USE_PREF_SIZE);

			Label licNumLabel = createLabel(
					localization.getLocalizedMessage("PedLookup.FieldLicenseNumber", "License Number:"));
			GridPane.setRowIndex(licNumLabel, 4);
			licNumLabel.setMinWidth(Region.USE_PREF_SIZE);

			Label gunLicTypeLabel = createLabel(
					localization.getLocalizedMessage("PedLookup.FieldGunLicenseType", "Gun License Type:"));
			GridPane.setRowIndex(gunLicTypeLabel, 4);
			GridPane.setColumnIndex(gunLicTypeLabel, 1);
			gunLicTypeLabel.setMinWidth(Region.USE_PREF_SIZE);

			Label gunLicClassLabel = createLabel(
					localization.getLocalizedMessage("PedLookup.FieldGunLicenseClass", "Gun License Class:"));
			GridPane.setRowIndex(gunLicClassLabel, 6);
			GridPane.setColumnSpan(gunLicClassLabel, 2);
			gunLicClassLabel.setMinWidth(Region.USE_PREF_SIZE);

			if (ConfigReader.configRead("uiColors", "UIDarkMode").equalsIgnoreCase("true")) {
				nameLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				dobLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				expDateLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				licStatusLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				licNumLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				gunLicTypeLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				gunLicClassLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				Node node = nameField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UIDarkColor + " !important;");
				node = dobField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UIDarkColor + " !important;");
				node = expField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UIDarkColor + " !important;");
				node = statusField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UIDarkColor + " !important;");
				node = licNumField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UIDarkColor + " !important;");
				node = gunLicTypeField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UIDarkColor + " !important;");
				node = gunLicClassField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UIDarkColor + " !important;");
			} else {
				nameLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				dobLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				expDateLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				licStatusLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				licNumLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				gunLicTypeLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				gunLicClassLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				Node node = nameField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UILightColor + " !important;");
				node = dobField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UILightColor + " !important;");
				node = expField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UILightColor + " !important;");
				node = statusField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UILightColor + " !important;");
				node = licNumField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UILightColor + " !important;");
				node = gunLicTypeField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UILightColor + " !important;");
				node = gunLicClassField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UILightColor + " !important;");
			}

			gridPane.getChildren().addAll(nameField, dobField, expField, statusField, licNumField, nameLabel, dobLabel,
					expDateLabel, licStatusLabel, licNumLabel, gunLicTypeField, gunLicClassField, gunLicTypeLabel,
					gunLicClassLabel);

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

	public static Ped performPedLookup(String name) {
		logInfo("Performing staticasdf lookup for: " + name);

		Optional<Ped> pedOptional = findPedByName(name);
		if (pedOptional.isPresent()) {
			logInfo("performPedLookup: Found [" + name + "] in PedHistory file");
			Ped ped = pedOptional.get();
			return generatePedFromData(false, ped.getName(), ped.getLicenseNumber(), ped.getModel(), ped.getBirthday(),
					ped.getGender(), ped.getAddress(), ped.getWantedStatus(), ped.getLicenseStatus(), null,
					ped.getGunLicenseType(), ped.getGunLicenseStatus(), ped.getGunLicenseExpiration(),
					ped.getFishingLicenseStatus(), ped.getFishingLicenseExpiration(), ped.getTimesStopped(),
					ped.getHuntingLicenseStatus(), ped.getHuntingLicenseExpiration(), ped.getParoleStatus(),
					ped.getProbationStatus());
		}

		PedObject worldPedObject = new PedObject(getServerDataFolderPath() + "ServerWorldPeds.data", name);
		if (worldPedObject.getName() != null && !worldPedObject.getName().equals("Not Found")) {
			logInfo("performPedLookup: Found [" + name + "] in WorldPed file");
			return generatePedFromData(false, worldPedObject.getName(), worldPedObject.getLicenseNumber(),
					worldPedObject.getModelName(), worldPedObject.getBirthday(), worldPedObject.getGender(),
					worldPedObject.getAddress(), worldPedObject.getIsWanted(), worldPedObject.getLicenseStatus(), null,
					worldPedObject.getWeaponPermitType(), worldPedObject.getWeaponPermitStatus(),
					worldPedObject.getWeaponPermitExpiration(), worldPedObject.getFishPermitStatus(),
					worldPedObject.getFishPermitExpiration(), worldPedObject.getTimesStopped(),
					worldPedObject.getHuntPermitStatus(),
					worldPedObject.getHuntPermitExpiration(), worldPedObject.getIsOnParole(),
					worldPedObject.getIsOnProbation());
		}

		Map<String, String> ownerSearch;
		try {
			ownerSearch = grabPedData(getServerDataFolderPath() + "ServerWorldCars.data", name);
		} catch (IOException e) {
			logError("performPedLookup: Error reading WorldVeh file for owner search: ", e);
			return null;
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
			return generatePedFromData(true, ownerName, null, ownerSearch.getOrDefault("ownermodel", null),
					null, ownerSearch.getOrDefault("ownergender", null), ownerSearch.getOrDefault("owneraddress", null),
					null, null, null, null, null, null, null, null, null, null, null, null, null);
		}

		if (searchIDHisForName(name)) {
			logWarn("performPedLookup: Found [" + name + "] in IDHistory (Possible Dead Ped)");
			ID searchedNameID = getHistoryIDFromName(name);
			if (searchedNameID != null) {
				return generatePedFromData(false, searchedNameID.getName(), searchedNameID.getLicenseNumber(),
						searchedNameID.getPedModel(), searchedNameID.getBirthday(), searchedNameID.getGender(),
						searchedNameID.getAddress(), null, null, null, null, null, null, null, null, null, null, null,
						null, null);
			}
		}

		logError("performPedLookup: No Ped with name [" + name + "] found anywhere.");
		return null;
	}

	private static Ped generatePedFromData(boolean owner, String name_value, String licenseNumber_value,
			String modelName_value,
			String birthday_value, String gender_value, String address_value, String isWanted_value,
			String licenseStatus_value, String licenseExp_value, String weaponPermitType_value,
			String weaponPermitStatus_value, String weaponPermitExpiration_value, String fishPermitStatus_value,
			String fishPermitExpiration_value, String timesStopped_value, String huntPermitStatus_value,
			String huntPermitExpiration_value, String isOnParole_value, String isOnProbation_value) {

		Optional<Ped> searchedPed = owner ? findPedByName(name_value) : findPedByNumber(licenseNumber_value);

		Ped ped = searchedPed.orElseGet(Ped::new);
		boolean needsSave = !searchedPed.isPresent();

		if (ped.getName() == null && name_value != null) {
			ped.setName(name_value);
			needsSave = true;
		}

		if (ped.getLicenseNumber() == null) {
			ped.setLicenseNumber(licenseNumber_value != null ? licenseNumber_value : generateLicenseNumber());
			needsSave = true;
		}

		if (ped.getGender() == null) {
			ped.setGender(
					gender_value != null ? gender_value : (calculateTrueFalseProbability("50") ? "Male" : "Female"));
			needsSave = true;
		}

		if (ped.getBirthday() == null) {
			ped.setBirthday(birthday_value != null ? birthday_value : generateBirthday(23, 65));
			needsSave = true;
		}

		if (ped.getAddress() == null) {
			ped.setAddress(address_value != null ? address_value : getRandomAddress());
			needsSave = true;
		}

		if (ped.getWantedStatus() == null) {
			ped.setWantedStatus(
					isWanted_value != null ? isWanted_value : (calculateTrueFalseProbability("15") ? "true" : "false"));
			needsSave = true;
		}

		if (ped.getLicenseStatus() == null) {
			ped.setLicenseStatus(
					licenseStatus_value != null ? licenseStatus_value : calculateLicenseStatus(55, 22, 23));
			needsSave = true;
		}

		if (ped.getParoleStatus() == null) {
			ped.setParoleStatus(isOnParole_value != null ? isOnParole_value : "false");
			needsSave = true;
		}

		if (ped.getProbationStatus() == null) {
			ped.setProbationStatus(isOnProbation_value != null ? isOnProbation_value : "false");
			needsSave = true;
		}

		if (ped.getTimesStopped() == null) {
			ped.setTimesStopped(timesStopped_value != null ? timesStopped_value : "0");
			needsSave = true;
		}

		if (ped.getModel() == null) {
			if (modelName_value != null) {
				ped.setModel(modelName_value);
			} else {
				ArrayList<String> maleModels = new ArrayList<>(
						Arrays.asList("[ig_zimbor][0][0]", "[mp_m_weed_01][0][0]"));
				ArrayList<String> femaleModels = new ArrayList<>(
						Arrays.asList("[a_f_m_bevhills_02][0][0]", "[a_f_y_femaleagent][0][0]"));
				Random random = new Random();
				if ("female".equalsIgnoreCase(ped.getGender())) {
					ped.setModel(femaleModels.get(random.nextInt(femaleModels.size())));
				} else {
					ped.setModel(maleModels.get(random.nextInt(maleModels.size())));
				}
			}
			needsSave = true;
		}

		if ("true".equalsIgnoreCase(ped.getWantedStatus()) && ped.getOutstandingWarrants() == null) {
			try {
				String warrant = getRandomChargeWithWarrant(URLStrings.chargesFilePath);
				ped.setOutstandingWarrants(warrant != null ? warrant : "WANTED - No details");
				if (warrant != null) {
					ped.setWarrantAgency(getRandomDepartment());
					ped.setWarrantNumber(generateLicenseNumber());
					ped.setDateWarrantIssued(generateExpiredLicenseExpirationDate(5));
				}
				needsSave = true;
			} catch (Exception e) {
				logError("Could not set warrantStatus: ", e);
			}
		}

		if (ped.getArrestPriors() == null && ped.getCitationPriors() == null) {
			try {
				int totalChargePriors = setArrestPriors(ped);
				int totalCitationPriors = setCitationPriors(ped);

				if (totalChargePriors > 0 && ped.getParoleStatus() == null) {
					ped.setParoleStatus(String.valueOf(
							calculateTrueFalseProbability(ConfigReader.configRead("pedHistory", "onParoleChance"))));
				}
				if (totalChargePriors > 0 && ped.getProbationStatus() == null) {
					ped.setProbationStatus(String.valueOf(
							calculateTrueFalseProbability(ConfigReader.configRead("pedHistory", "onProbationChance"))));
				}
				if (ped.getTimesStopped() == null || "0".equals(ped.getTimesStopped())) {
					ped.setTimesStopped(String.valueOf(calculateTotalStops(totalChargePriors + totalCitationPriors)));
				}
				if (ped.getFlags() == null) {
					int baseFlagFactor = Integer.parseInt(ConfigReader.configRead("pedHistory", "baseFlagProbability"));
					String flags = assignFlagsBasedOnPriors(totalChargePriors, baseFlagFactor, 0.9, 2);
					if (flags != null && !flags.isEmpty()) {
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
				if (fishPermitStatus_value != null && !"not found".equalsIgnoreCase(fishPermitStatus_value)) {
					ped.setFishingLicenseStatus(fishPermitStatus_value);
				} else if (calculateTrueFalseProbability(ConfigReader.configRead("pedHistory", "hasFishingLicense"))) {
					ped.setFishingLicenseStatus(calculateLicenseStatus(55, 22, 23));
				} else {
					ped.setFishingLicenseStatus("None");
				}
				needsSave = true;
			}
			if (!"None".equalsIgnoreCase(ped.getFishingLicenseStatus())) {
				if (ped.getFishingLicenseNumber() == null)
					ped.setFishingLicenseNumber(generateLicenseNumber());
				if (ped.getFishingLicenseExpiration() == null) {
					if ("Valid".equalsIgnoreCase(ped.getFishingLicenseStatus()))
						ped.setFishingLicenseExpiration(generateValidLicenseExpirationDate());
					else if ("Expired".equalsIgnoreCase(ped.getFishingLicenseStatus()))
						ped.setFishingLicenseExpiration(generateExpiredLicenseExpirationDate(3));
					else
						ped.setFishingLicenseExpiration(ped.getFishingLicenseStatus() + " License");
				}
				needsSave = true;
			}
		} catch (IOException e) {
			logError("Could not set fishingLicenseStatus: ", e);
		}

		try {
			if (ped.getHuntingLicenseStatus() == null) {
				if (huntPermitStatus_value != null && !"not found".equalsIgnoreCase(huntPermitStatus_value)) {
					ped.setHuntingLicenseStatus(huntPermitStatus_value);
				} else if (calculateTrueFalseProbability(ConfigReader.configRead("pedHistory", "hasHuntingLicense"))) {
					ped.setHuntingLicenseStatus(calculateLicenseStatus(55, 22, 23));
				} else {
					ped.setHuntingLicenseStatus("None");
				}
				needsSave = true;
			}
			if (!"None".equalsIgnoreCase(ped.getHuntingLicenseStatus())) {
				if (ped.getHuntingLicenseNumber() == null)
					ped.setHuntingLicenseNumber(generateLicenseNumber());
				if (ped.getHuntingLicenseExpiration() == null) {
					if ("Valid".equalsIgnoreCase(ped.getHuntingLicenseStatus()))
						ped.setHuntingLicenseExpiration(generateValidLicenseExpirationDate());
					else if ("Expired".equalsIgnoreCase(ped.getHuntingLicenseStatus()))
						ped.setHuntingLicenseExpiration(generateExpiredLicenseExpirationDate(3));
					else
						ped.setHuntingLicenseExpiration(ped.getHuntingLicenseStatus() + " License");
				}
				needsSave = true;
			}
		} catch (IOException e) {
			logError("Could not set huntingLicenseStatus: ", e);
		}

		try {
			if (ped.getBoatingLicenseStatus() == null) {
				if (calculateTrueFalseProbability(ConfigReader.configRead("pedHistory", "hasBoatingLicense"))) {
					ped.setBoatingLicenseStatus(calculateLicenseStatus(55, 22, 23));
				} else {
					ped.setBoatingLicenseStatus("None");
				}
				needsSave = true;
			}
			if (!"None".equalsIgnoreCase(ped.getBoatingLicenseStatus())) {
				if (ped.getBoatingLicenseNumber() == null)
					ped.setBoatingLicenseNumber(generateLicenseNumber());
				if (ped.getBoatingLicenseExpiration() == null) {
					if ("Valid".equalsIgnoreCase(ped.getBoatingLicenseStatus()))
						ped.setBoatingLicenseExpiration(generateValidLicenseExpirationDate());
					else if ("Expired".equalsIgnoreCase(ped.getBoatingLicenseStatus()))
						ped.setBoatingLicenseExpiration(generateExpiredLicenseExpirationDate(3));
					else
						ped.setBoatingLicenseExpiration(ped.getBoatingLicenseStatus() + " License");
				}
				needsSave = true;
			}
		} catch (IOException e) {
			logError("Could not set boatingLicenseStatus: ", e);
		}

		try {
			if (ped.getGunLicenseStatus() == null) {
				if (weaponPermitStatus_value != null) {
					ped.setGunLicenseStatus(weaponPermitStatus_value);
				} else if (calculateTrueFalseProbability(
						ConfigReader.configRead("pedHistoryGunPermit", "hasGunLicense"))) {
					ped.setGunLicenseStatus(calculateLicenseStatus(55, 22, 23));
				} else {
					ped.setGunLicenseStatus("None");
				}
				needsSave = true;
			}
			if (!"None".equalsIgnoreCase(ped.getGunLicenseStatus())) {
				if (ped.getGunLicenseNumber() == null)
					ped.setGunLicenseNumber(generateLicenseNumber());
				if (ped.getGunLicenseType() == null)
					ped.setGunLicenseType(weaponPermitType_value != null ? weaponPermitType_value : "Concealed Carry");
				if (ped.getGunLicenseClass() == null)
					ped.setGunLicenseClass(getGunLicenseClass());
				if (ped.getGunLicenseExpiration() == null) {
					if ("Valid".equalsIgnoreCase(ped.getGunLicenseStatus()))
						ped.setGunLicenseExpiration(generateValidLicenseExpirationDate());
					else if ("Expired".equalsIgnoreCase(ped.getGunLicenseStatus()))
						ped.setGunLicenseExpiration(generateExpiredLicenseExpirationDate(3));
					else
						ped.setGunLicenseExpiration(ped.getGunLicenseStatus() + " License");
				}
				needsSave = true;
			}
		} catch (IOException e) {
			logError("Could not set gunLicenseStatus: ", e);
		}

		if (needsSave) {
			try {
				Ped.PedHistoryUtils.addPed(ped);
			} catch (JAXBException e) {
				logError("Error saving updated ped to PedHistory: ", e);
			}
		}

		return ped;
	}

	private void addLocalization() {
		lookupmainlbl.setText(localization.getLocalizedMessage("PedLookup.MainHeader", "D.M.V Pedestrian Lookup"));
		lbl1.setText(localization.getLocalizedMessage("PedLookup.SearchPedLabel", "Search Ped:"));
		noPedImageFoundlbl
				.setText(localization.getLocalizedMessage("PedLookup.NoPedImageFoundlbl", "No Image Found In System"));
		noRecordFoundLabelPed
				.setText(localization.getLocalizedMessage("PedLookup.NoPedFoundInSystem", "No Record Found In System"));

		addDataToNotesBtn
				.setText(localization.getLocalizedMessage("PedLookup.AddDataToNotesButton", "Add Data To Notes"));
		// REMOVE:
		// pedSearchBtn.setText(localization.getLocalizedMessage("PedLookup.SearchPedButton",
		// "Search"));
		infobtn3.setText(
				localization.getLocalizedMessage("PedLookup.UpdateOtherInfoButton", "Update Other Information"));

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
		ped18.setText(
				localization.getLocalizedMessage("PedLookup.FieldFishingLicenseStatus", "Fishing License Status"));
		ped19.setText(
				localization.getLocalizedMessage("PedLookup.FieldBoatingLicenseStatus", "Boating License Status"));
		ped20.setText(
				localization.getLocalizedMessage("PedLookup.FieldHuntingLicenseStatus", "Hunting License Status"));
		ped23.setText(localization.getLocalizedMessage("PedLookup.FieldFlags", "Flag(s):"));
	}

	private void processPedData(boolean owner, String name_value, String licenseNumber_value, String modelName_value,
			String birthday_value, String gender_value, String address_value, String isWanted_value,
			String licenseStatus_value, String licenseExp_value, String weaponPermitType_value,
			String weaponPermitStatus_value, String weaponPermitExpiration_value, String fishPermitStatus_value,
			String fishPermitExpiration_value, String timesStopped_value, String huntPermitStatus_value,
			String huntPermitExpiration_value, String isOnParole_value, String isOnProbation_value) {
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
				logWarn("ProcessPedData; name_value was null, set as ERROR");
				ped.setName("ERROR");
			}
			if (licenseNumber_value != null) {
				ped.setLicenseNumber(licenseNumber_value);
			} else {
				logWarn("ProcessPedData; licenseNumber_value was null, generating");
				ped.setLicenseNumber(generateLicenseNumber());
			}
			if (gender_value != null) {
				ped.setGender(gender_value);
			} else {
				logWarn("ProcessPedData; gender_value was null, generating");
				ped.setGender(calculateTrueFalseProbability("50") ? "Male" : "Female");
			}
			if (birthday_value != null) {
				ped.setBirthday(birthday_value);
			} else {
				logWarn("ProcessPedData; birthday_value was null, generating");
				ped.setBirthday(generateBirthday(23, 65));
			}
			if (address_value != null) {
				ped.setAddress(address_value);
			} else {
				logWarn("ProcessPedData; address_value was null, generating");
				ped.setAddress(getRandomAddress());
			}
			if (isWanted_value != null) {
				ped.setWantedStatus(isWanted_value);
			} else {
				logWarn("ProcessPedData; isWanted_value was null, generating");
				ped.setWantedStatus(calculateTrueFalseProbability("15") ? "true" : "false");
			}
			if (licenseStatus_value != null) {
				ped.setLicenseStatus(licenseStatus_value);
			} else {
				logWarn("ProcessPedData; licenseStatus_value was null, generating");
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
					logInfo("ProcessPedData; Generated arrestPriors: " + totalChargePriors);
				} catch (IOException e) {
					logError("Could not fetch arrestPriors: ", e);
				}
				int totalCitationPriors = 0;
				try {
					totalCitationPriors = setCitationPriors(ped);
					logInfo("ProcessPedData; Generated citationPriors: " + totalCitationPriors);
				} catch (IOException e) {
					logError("Could not fetch citationPriors: ", e);
				}

				if (totalChargePriors >= 1) {
					try {
						String paroleStatus = String.valueOf(
								calculateTrueFalseProbability(ConfigReader.configRead("pedHistory", "onParoleChance")));
						ped.setParoleStatus(paroleStatus);
						logInfo("ProcessPedData; Generated paroleStatus: " + paroleStatus);
					} catch (IOException e) {
						logError("Could not set ParoleStatus: ", e);
					}
					try {
						String probationStatus = String.valueOf(calculateTrueFalseProbability(
								ConfigReader.configRead("pedHistory", "onProbationChance")));
						ped.setProbationStatus(probationStatus);
						logInfo("ProcessPedData; Generated probationStatus: " + probationStatus);
					} catch (IOException e) {
						logError("Could not set ProbationStatus: ", e);
					}
				}

				// BUG: not using timesStopped since it relies on chargepriors and
				// citationpriors
				String totalStops = String.valueOf(calculateTotalStops(totalChargePriors + totalCitationPriors));
				ped.setTimesStopped(totalStops);
				logInfo("ProcessPedData; Generated timesStopped: " + totalStops);

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
					logWarn("ProcessPedData; fishPermitStatus_value was null, generating");
					boolean hasFishingLicense = calculateTrueFalseProbability(
							ConfigReader.configRead("pedHistory", "hasFishingLicense"));

					if (hasFishingLicense) {
						String licstatus = calculateLicenseStatus(
								Integer.parseInt(ConfigReader.configRead("pedHistory", "validLicenseChance")),
								Integer.parseInt(ConfigReader.configRead("pedHistory", "expiredLicenseChance")),
								Integer.parseInt(ConfigReader.configRead("pedHistory", "suspendedLicenseChance")));
						ped.setFishingLicenseStatus(licstatus);

						logWarn("ProcessPedData; generated fishingLicenseNumber");
						ped.setFishingLicenseNumber(generateLicenseNumber());
					} else {
						logWarn("ProcessPedData; hasFishingLicense was false, set FishingLicenseStatus None");
						ped.setFishingLicenseStatus("None");
						ped.setFishingLicenseNumber("None");
						ped.setFishingLicenseExpiration("None");
					}
				}

				if (fishPermitExpiration_value != null && !fishPermitExpiration_value.equalsIgnoreCase("not found")) {
					ped.setFishingLicenseExpiration(fishPermitExpiration_value);
				} else {
					logWarn("ProcessPedData; fishPermitExpiration_value was null, generating expiration");
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
					logWarn("ProcessPedData; boating lic status is null, generating status/exp/lic#");
					boolean boatLicStatus = calculateTrueFalseProbability(
							ConfigReader.configRead("pedHistory", "hasBoatingLicense"));
					if (boatLicStatus) {
						String licstatus = calculateLicenseStatus(
								Integer.parseInt(ConfigReader.configRead("pedHistory", "validLicenseChance")),
								Integer.parseInt(ConfigReader.configRead("pedHistory", "expiredLicenseChance")),
								Integer.parseInt(ConfigReader.configRead("pedHistory", "suspendedLicenseChance")));

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
					logWarn("ProcessPedData; modelName_value is null, generating from gender value: "
							+ ped.getGender());

					ArrayList<String> maleModels = new ArrayList<>(
							Arrays.asList("[ig_zimbor][0][0]", "[mp_m_weed_01][0][0]", "[s_m_m_bouncer_01][0][0]",
									"[s_m_m_postal_02][0][0]", "[s_m_y_waretech_01][0][0]", "[a_m_m_eastsa_01][0][0]"));

					ArrayList<String> femaleModels = new ArrayList<>(Arrays.asList("[a_f_m_bevhills_02][0][0]",
							"[a_f_y_femaleagent][0][0]", "[a_f_y_soucent_02][0][0]", "[csb_mrs_r][0][0]",
							"[mp_f_counterfeit_01][0][0]", "[mp_f_cardesign_01][0][0]"));

					if (ped.getGender() != null) {
						Random random = new Random();
						if (ped.getGender().equalsIgnoreCase("female")) {
							String model = femaleModels.get(random.nextInt(femaleModels.size()));
							logWarn("ProcessPedData; Generated new Female model [" + model + "]");
							ped.setModel(model);
						} else if (ped.getGender().equalsIgnoreCase("male")) {
							String model = maleModels.get(random.nextInt(maleModels.size()));
							logWarn("ProcessPedData; Generated new Male model [" + model + "]");
							ped.setModel(model);
						} else {
							logError("ProcessPedData; Set model as 'Not Found'");
							ped.setModel("Not Found");
						}
					} else {
						logError("ProcessPedData; Set model as 'Not Found' [2]");
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
					logWarn("ProcessPedData; fishPermitStatus_value was null, generating");
					boolean hasFishingLicense = calculateTrueFalseProbability(
							ConfigReader.configRead("pedHistory", "hasFishingLicense"));

					if (hasFishingLicense) {
						String licstatus = calculateLicenseStatus(
								Integer.parseInt(ConfigReader.configRead("pedHistory", "validLicenseChance")),
								Integer.parseInt(ConfigReader.configRead("pedHistory", "expiredLicenseChance")),
								Integer.parseInt(ConfigReader.configRead("pedHistory", "suspendedLicenseChance")));
						ped.setFishingLicenseStatus(licstatus);
					} else {
						logWarn("ProcessPedData; hasFishingLicense was false, set FishingLicenseStatus None");
						ped.setFishingLicenseStatus("None");
					}
				}

				if (fishPermitExpiration_value != null) {
					ped.setFishingLicenseExpiration(fishPermitExpiration_value);
				} else {
					logWarn("ProcessPedData; fishPermitExpiration_value was null, generating expiration");
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
					logWarn("ProcessPedData; generated fishingLicenseNumber");
					ped.setFishingLicenseNumber(generateLicenseNumber());
				}

			} catch (IOException e) {
				logError("Could not set fishingLicenseStatus: ", e);
			}

			try {
				if (weaponPermitStatus_value != null) {
					ped.setGunLicenseStatus(weaponPermitStatus_value);
				} else {
					logWarn("ProcessPedData; weaponPermitStatus_value was null, generating");
					Boolean hasGunLicense = calculateTrueFalseProbability(
							ConfigReader.configRead("pedHistoryGunPermit", "hasGunLicense"));

					if (hasGunLicense) {
						String gunlicstatus = calculateLicenseStatus(
								Integer.parseInt(ConfigReader.configRead("pedHistory", "validLicenseChance")),
								Integer.parseInt(ConfigReader.configRead("pedHistory", "expiredLicenseChance")),
								Integer.parseInt(ConfigReader.configRead("pedHistory", "suspendedLicenseChance")));
						ped.setGunLicenseStatus(gunlicstatus);
					} else {
						logWarn("ProcessPedData; hasGunLicense was false, set GunLicenseStatus None");
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
					logWarn("ProcessPedData; weaponPermitExpiration_value was null, generating expiration");

					if (ped.getGunLicenseStatus().equalsIgnoreCase("valid")
							|| ped.getGunLicenseStatus().equalsIgnoreCase("expired")
							|| ped.getGunLicenseStatus().equalsIgnoreCase("suspended")
							|| ped.getGunLicenseStatus().equalsIgnoreCase("revoked")) {

						if (ped.getGunLicenseStatus().equalsIgnoreCase("valid")) {
							ped.setGunLicenseExpiration(generateValidLicenseExpirationDate());
							if (ped.getGunLicenseNumber() == null || ped.getGunLicenseNumber().isEmpty()) {
								logWarn("ProcessPedData; generated gunLicenseNumber");
								ped.setGunLicenseNumber(generateLicenseNumber());
							}
						} else if (ped.getGunLicenseStatus().equalsIgnoreCase("suspended")) {
							ped.setGunLicenseExpiration("Suspended License");
							if (ped.getGunLicenseNumber() == null || ped.getGunLicenseNumber().isEmpty()) {
								logWarn("ProcessPedData; generated gunLicenseNumber");
								ped.setGunLicenseNumber(generateLicenseNumber());
							}
						} else if (ped.getGunLicenseStatus().equalsIgnoreCase("revoked")) {
							ped.setGunLicenseExpiration("Revoked License");
							if (ped.getGunLicenseNumber() == null || ped.getGunLicenseNumber().isEmpty()) {
								logWarn("ProcessPedData; generated gunLicenseNumber");
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
					logWarn("ProcessPedData; weaponPermitType_value is null, setting type 'None'");
					ped.setGunLicenseType("None");
				}

				if (ped.getGunLicenseStatus().equalsIgnoreCase("valid")
						|| ped.getGunLicenseStatus().equalsIgnoreCase("expired")
						|| ped.getGunLicenseStatus().equalsIgnoreCase("revoked")
						|| ped.getGunLicenseStatus().equalsIgnoreCase("suspended")) {
					logWarn("ProcessPedData; weaponPermitType_value was null, generating type/class/number");
					String licclass = getGunLicenseClass();
					String number = generateLicenseNumber();
					ped.setGunLicenseClass(licclass);
					ped.setGunLicenseNumber(number);
				}

				if (huntPermitStatus_value != null) {
					ped.setHuntingLicenseStatus(huntPermitStatus_value);

					if (ped.getHuntingLicenseStatus().equalsIgnoreCase("valid")
							|| ped.getHuntingLicenseStatus().equalsIgnoreCase("expired")
							|| ped.getHuntingLicenseStatus().equalsIgnoreCase("suspended")
							|| ped.getHuntingLicenseStatus().equalsIgnoreCase("revoked")) {
						if (ped.getHuntingLicenseNumber() == null || ped.getHuntingLicenseNumber().isEmpty()) {
							logWarn("ProcessPedData; Generated hunting license number for provided status");
							ped.setHuntingLicenseNumber(generateLicenseNumber());
						}
					}
				} else {
					logWarn("ProcessPedData; huntPermitStatus_value was null, generating");
					boolean huntlic = calculateTrueFalseProbability(
							ConfigReader.configRead("pedHistory", "hasHuntingLicense"));

					if (huntlic) {
						String licstatus = calculateLicenseStatus(
								Integer.parseInt(ConfigReader.configRead("pedHistory", "validLicenseChance")),
								Integer.parseInt(ConfigReader.configRead("pedHistory", "expiredLicenseChance")),
								Integer.parseInt(ConfigReader.configRead("pedHistory", "suspendedLicenseChance")));
						ped.setHuntingLicenseStatus(licstatus);
						ped.setHuntingLicenseNumber(generateLicenseNumber());
					} else {
						logWarn("ProcessPedData; huntlic was false, set HuntingLicenseStatus None");
						ped.setHuntingLicenseStatus("None");
						ped.setHuntingLicenseNumber("None");
						ped.setHuntingLicenseExpiration("None");
					}
				}

				if (huntPermitExpiration_value != null) {
					ped.setHuntingLicenseExpiration(huntPermitExpiration_value);
				} else {
					logWarn("ProcessPedData; huntPermitExpiration_value was null, generating expiration");
					if (ped.getHuntingLicenseStatus().equalsIgnoreCase("valid")
							|| ped.getHuntingLicenseStatus().equalsIgnoreCase("expired")
							|| ped.getHuntingLicenseStatus().equalsIgnoreCase("suspended")
							|| ped.getHuntingLicenseStatus().equalsIgnoreCase("revoked")) {

						if (ped.getHuntingLicenseStatus().equalsIgnoreCase("valid")) {
							ped.setHuntingLicenseExpiration(generateValidLicenseExpirationDate());
							if (ped.getHuntingLicenseNumber() == null || ped.getHuntingLicenseNumber().isEmpty()) {
								logWarn("ProcessPedData; generated HuntingLicenseNumber");
								ped.setHuntingLicenseNumber(generateLicenseNumber());
							}
						} else if (ped.getHuntingLicenseStatus().equalsIgnoreCase("suspended")) {
							ped.setHuntingLicenseExpiration("Suspended License");
							if (ped.getHuntingLicenseNumber() == null || ped.getHuntingLicenseNumber().isEmpty()) {
								logWarn("ProcessPedData; generated HuntingLicenseNumber");
								ped.setHuntingLicenseNumber(generateLicenseNumber());
							}
						} else if (ped.getHuntingLicenseStatus().equalsIgnoreCase("revoked")) {
							ped.setHuntingLicenseExpiration("Revoked License");
							if (ped.getHuntingLicenseNumber() == null || ped.getHuntingLicenseNumber().isEmpty()) {
								logWarn("ProcessPedData; generated HuntingLicenseNumber");
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
		if (ped.getLicenseStatus().equalsIgnoreCase("EXPIRED") || ped.getLicenseStatus().equalsIgnoreCase("SUSPENDED")
				|| ped.getLicenseStatus().equalsIgnoreCase("REVOKED") || ped.getLicenseStatus().equalsIgnoreCase("NONE")
				|| ped.getLicenseStatus().equalsIgnoreCase("UNLICENSED")) {
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

			createWarrantInfoPopup(pedwantedfield,
					localization.getLocalizedMessage("PedLookup.WarrantInformationTitle",
							"Issued Warrant Information:"),
					ped.getName(), ped.getBirthday(), ped.getDateWarrantIssued(), ped.getWarrantNumber(),
					ped.getWarrantAgency(), ped.getOutstandingWarrants());
		} else {
			pedwantedfield.setText("False");
			pedwantedfield.getStyleClass().add("text-field");
			pedwantedfield.setStyle("-fx-text-fill: black !important;");
		}

		pedgunlicensestatusfield.getStyleClass().clear();
		pedgunlicensestatusfield.setOnMouseClicked(null);
		pedgunlicensestatusfield.setText(ped.getGunLicenseStatus());
		if (ped.getGunLicenseStatus() == null || ped.getGunLicenseStatus().equalsIgnoreCase("false")
				|| ped.getGunLicenseStatus().equalsIgnoreCase("Not Found")
				|| ped.getGunLicenseStatus().equalsIgnoreCase("None")) {
			pedgunlicensestatusfield.setText("False");
			pedgunlicensestatusfield.getStyleClass().add("text-field");
			pedgunlicensestatusfield.setStyle("-fx-text-fill: black !important;");
		} else if (ped.getGunLicenseStatus().equalsIgnoreCase("suspended")
				|| ped.getGunLicenseStatus().equalsIgnoreCase("revoked")
				|| ped.getGunLicenseStatus().equalsIgnoreCase("expired")) {
			pedgunlicensestatusfield.getStyleClass().add("valid-field");
			pedgunlicensestatusfield.setStyle("-fx-text-fill: orange !important;");
			pedgunlicensestatusfield.setText(ped.getGunLicenseStatus().toUpperCase());

			createGunLicenseInfoPopup(pedgunlicensestatusfield,
					localization.getLocalizedMessage("PedLookup.GunLicenseInfoTitle", "Gun License Information:"),
					ped.getName(), ped.getBirthday(), ped.getGunLicenseExpiration(), ped.getGunLicenseStatus(),
					ped.getGunLicenseNumber(),
					ped.getGunLicenseType(), ped.getGunLicenseClass());

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
			createGunLicenseInfoPopup(pedgunlicensestatusfield,
					localization.getLocalizedMessage("PedLookup.GunLicenseInfoTitle", "Gun License Information:"),
					ped.getName(), ped.getBirthday(), ped.getGunLicenseExpiration(), ped.getGunLicenseStatus(),
					ped.getGunLicenseNumber(),
					ped.getGunLicenseType(), ped.getGunLicenseClass());

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
		if (ped.getFishingLicenseStatus() == null || ped.getFishingLicenseStatus().equalsIgnoreCase("false")
				|| ped.getFishingLicenseStatus().equalsIgnoreCase("Not Found")
				|| ped.getFishingLicenseStatus().equalsIgnoreCase("None")) {
			pedfishinglicstatusfield.setText("False");
			pedfishinglicstatusfield.getStyleClass().add("text-field");
			pedfishinglicstatusfield.setStyle("-fx-text-fill: black !important;");
		} else if (ped.getFishingLicenseStatus().equalsIgnoreCase("suspended")
				|| ped.getFishingLicenseStatus().equalsIgnoreCase("expired")
				|| ped.getFishingLicenseStatus().equalsIgnoreCase("revoked")) {
			pedfishinglicstatusfield.getStyleClass().add("valid-field");
			pedfishinglicstatusfield.setStyle("-fx-text-fill: orange !important;");
			pedfishinglicstatusfield.setText(ped.getFishingLicenseStatus().toUpperCase());

			createLicenseInfoPopup(pedfishinglicstatusfield,
					localization.getLocalizedMessage("PedLookup.FishLicenseInfoTitle", "Fishing License Information:"),
					ped.getName(), ped.getBirthday(), ped.getFishingLicenseExpiration(), ped.getFishingLicenseStatus(),
					ped.getFishingLicenseNumber());

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
			createLicenseInfoPopup(pedfishinglicstatusfield,
					localization.getLocalizedMessage("PedLookup.FishLicenseInfoTitle", "Fishing License Information:"),
					ped.getName(), ped.getBirthday(), ped.getFishingLicenseExpiration(), ped.getFishingLicenseStatus(),
					ped.getFishingLicenseNumber());

		} else {
			logError("Unexpected fishing license status: " + ped.getFishingLicenseStatus());
			showNotificationError("Ped Lookup", "Unexpected fishing license status: " + ped.getFishingLicenseStatus());

			pedfishinglicstatusfield.setText("Unknown");
			pedfishinglicstatusfield.getStyleClass().add("text-field");
			pedfishinglicstatusfield.setStyle("-fx-text-fill: red !important;");
		}

		pedboatinglicstatusfield.getStyleClass().clear();
		pedboatinglicstatusfield.setOnMouseClicked(null);
		if (ped.getBoatingLicenseStatus() == null || ped.getBoatingLicenseStatus().equalsIgnoreCase("Not Found")
				|| ped.getBoatingLicenseStatus().equalsIgnoreCase("None")) {
			pedboatinglicstatusfield.setText("False");
			pedboatinglicstatusfield.getStyleClass().add("text-field");
			pedboatinglicstatusfield.setStyle("-fx-text-fill: black !important;");
		} else if (ped.getBoatingLicenseStatus().equalsIgnoreCase("suspended")
				|| ped.getBoatingLicenseStatus().equalsIgnoreCase("expired")
				|| ped.getBoatingLicenseStatus().equalsIgnoreCase("revoked")) {
			pedboatinglicstatusfield.getStyleClass().add("valid-field");
			pedboatinglicstatusfield.setStyle("-fx-text-fill: orange !important;");
			pedboatinglicstatusfield.setText(ped.getBoatingLicenseStatus().toUpperCase());

			createLicenseInfoPopup(pedboatinglicstatusfield,
					localization.getLocalizedMessage("PedLookup.BoatLicenseInfoTitle", "Boating License Information:"),
					ped.getName(), ped.getBirthday(), ped.getBoatingLicenseExpiration(), ped.getBoatingLicenseStatus(),
					ped.getBoatingLicenseNumber());

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
			createLicenseInfoPopup(pedboatinglicstatusfield,
					localization.getLocalizedMessage("PedLookup.BoatLicenseInfoTitle", "Boating License Information:"),
					ped.getName(), ped.getBirthday(), ped.getBoatingLicenseExpiration(), ped.getBoatingLicenseStatus(),
					ped.getBoatingLicenseNumber());
		} else {
			logError("Unexpected boating license status: " + ped.getBoatingLicenseStatus());
			showNotificationError("Ped Lookup", "Unexpected boating license status: " + ped.getBoatingLicenseStatus());
			pedboatinglicstatusfield.setText("Unknown");
			pedboatinglicstatusfield.getStyleClass().add("text-field");
			pedboatinglicstatusfield.setStyle("-fx-text-fill: red !important;");
		}

		pedhuntinglicstatusfield.getStyleClass().clear();
		pedhuntinglicstatusfield.setOnMouseClicked(null);
		pedhuntinglicstatusfield.setText(ped.getHuntingLicenseStatus());
		if (ped.getHuntingLicenseStatus() == null || ped.getHuntingLicenseStatus().equalsIgnoreCase("Not Found")
				|| ped.getHuntingLicenseStatus().equalsIgnoreCase("None")) {
			pedhuntinglicstatusfield.setText("False");
			pedhuntinglicstatusfield.getStyleClass().add("text-field");
			pedhuntinglicstatusfield.setStyle("-fx-text-fill: black !important;");
		} else if (ped.getHuntingLicenseStatus().equalsIgnoreCase("suspended")
				|| ped.getHuntingLicenseStatus().equalsIgnoreCase("expired")
				|| ped.getHuntingLicenseStatus().equalsIgnoreCase("revoked")) {
			pedhuntinglicstatusfield.getStyleClass().add("valid-field");
			pedhuntinglicstatusfield.setStyle("-fx-text-fill: orange !important;");
			pedhuntinglicstatusfield.setText(ped.getHuntingLicenseStatus().toUpperCase());

			createLicenseInfoPopup(pedhuntinglicstatusfield,
					localization.getLocalizedMessage("PedLookup.HuntLicenseInfoTitle", "Hunting License Information:"),
					ped.getName(), ped.getBirthday(), ped.getHuntingLicenseExpiration(), ped.getHuntingLicenseStatus(),
					ped.getHuntingLicenseNumber());

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
			createLicenseInfoPopup(pedhuntinglicstatusfield,
					localization.getLocalizedMessage("PedLookup.HuntLicenseInfoTitle", "Hunting License Information:"),
					ped.getName(), ped.getBirthday(), ped.getHuntingLicenseExpiration(), ped.getHuntingLicenseStatus(),
					ped.getHuntingLicenseNumber());
		} else {
			logError("Unexpected hunting license status: " + ped.getHuntingLicenseStatus());
			showNotificationError("Ped Lookup", "Unexpected hunting license status: " + ped.getHuntingLicenseStatus());
			pedhuntinglicstatusfield.setText("Unknown");
			pedhuntinglicstatusfield.getStyleClass().add("text-field");
			pedhuntinglicstatusfield.setStyle("-fx-text-fill: red !important;");
		}

		pedlicnumfield.setText(ped.getLicenseNumber() != null ? ped.getLicenseNumber() : "No Data In System");
		pedlicnumfield.setStyle(
				ped.getLicenseNumber() == null ? "-fx-text-fill: #E65C00 !important;" : "-fx-text-fill: black;");

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
				pedtimesstoppedfield.setStyle(
						timesStoppedValue > 0 ? "-fx-text-fill: #E65C00 !important;" : "-fx-text-fill: black;");
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

	// #region Event Handlers

	@javafx.fxml.FXML
	public void onPedSearchBtnClick(ActionEvent actionEvent) throws IOException {
		/*
		 * REMOVE: String searchedName = pedSearchField.getEditor().getText().trim();
		 * updateRecentSearches(recentPedSearches, pedSearchField, searchedName);
		 * pedSearchField.getEditor().setText(searchedName);
		 * pedSearchField.getEditor().positionCaret(pedSearchField.getEditor().getText()
		 * .length());
		 */

		// TODO: finish
		String searchedName = "";

		logInfo("Searched: " + searchedName);

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

			processPedData(false, ped.getName(), ped.getLicenseNumber(), ped.getModel(), ped.getBirthday(),
					ped.getGender(), ped.getAddress(), ped.getWantedStatus(), ped.getLicenseStatus(), null,
					ped.getGunLicenseType(), ped.getGunLicenseStatus(), ped.getGunLicenseExpiration(),
					ped.getFishingLicenseStatus(), ped.getFishingLicenseExpiration(), ped.getTimesStopped(),
					ped.getHuntingLicenseStatus(), ped.getHuntingLicenseExpiration(), ped.getParoleStatus(),
					ped.getProbationStatus());

		} else if (worldPedObject.getName() != null && !worldPedObject.getName().equals("Not Found")) {
			// Ped was found in WorldPed
			logInfo("Found: [" + worldPedObject.getName() + "] From WorldPed file");

			processPedData(false, worldPedObject.getName(), worldPedObject.getLicenseNumber(),
					worldPedObject.getModelName(), worldPedObject.getBirthday(), worldPedObject.getGender(),
					worldPedObject.getAddress(), worldPedObject.getIsWanted(), worldPedObject.getLicenseStatus(), null,
					worldPedObject.getWeaponPermitType(), worldPedObject.getWeaponPermitStatus(),
					worldPedObject.getWeaponPermitExpiration(), worldPedObject.getFishPermitStatus(),
					worldPedObject.getFishPermitExpiration(), worldPedObject.getTimesStopped(),
					worldPedObject.getHuntPermitStatus(),
					worldPedObject.getHuntPermitExpiration(), worldPedObject.getIsOnParole(),
					worldPedObject.getIsOnProbation());

		} else if (ownerName != null && !ownerName.equalsIgnoreCase("Not Found")
				&& !ownerName.equalsIgnoreCase("Los Santos Police Department")
				&& !ownerName.equalsIgnoreCase("Los Santos Sheriff's Office")
				&& !ownerName.equalsIgnoreCase("Los Santos County Sheriff") && !ownerName.equalsIgnoreCase(
						"Blaine County Sheriff's Office")
				&& !ownerName.equalsIgnoreCase("San Andreas Highway Patrol")
				&& !ownerName.equalsIgnoreCase("government")) {
			// Vehicle Owner was found and the vehicle is not Government
			logInfo("Found Vehicle Owner: [" + ownerName + "] From WorldVeh file, plate#: " + ownerPlateNum);

			processPedData(true, ownerName, null, ownerModel, null, ownerGender, ownerAddress, null, null, null, null,
					null, null, null, null, null, null, null, null, null);

		} else if (searchIDHisForName(searchedName)) {
			// Ped ID Was Found (Ped is probably dead)
			logWarn("Found Ped: [" + searchedName + "] From IDHistory (Possible Dead Ped)");
			ID searchedNameID = getHistoryIDFromName(searchedName);

			if (searchedNameID != null) {
				logDebug(searchedName + " HistoryID not null");

				processPedData(false, searchedNameID.getName(), searchedNameID.getLicenseNumber(),
						searchedNameID.getPedModel(), searchedNameID.getBirthday(), searchedNameID.getGender(),
						searchedNameID.getAddress(), null, null, null, null, null, null, null, null, null, null, null,
						null, null);
			}

		} else {
			// Ped was Not Found Anywhere
			logError("No Ped With Name: [" + searchedName + "] Found Anywhere");
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

	// #endregion

	// #region Getters

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

	public AnchorPane getPedRecordPane() {
		return pedRecordPane;
	}

	/*
	 * REMOVE public Button getPedSearchBtn() {
	 * return pedSearchBtn;
	 * }
	 */

	public Button getAddDataToNotesBtn() {
		return addDataToNotesBtn;
	}

	public Label getLbl1() {
		return lbl1;
	}

	/*
	 * //REMOVE: public ComboBox getPedSearchField() {
	 * return pedSearchField;
	 * }
	 */

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

	public Button getInfobtn3() {
		return infobtn3;
	}

	// #endregion

	// #region Dont Rename

	// Dont rename
	public TextField getPeddobfield() {
		return peddobfield;
	}

	// Dont rename
	public TextField getPedgenfield() {
		return pedgenfield;
	}

	// Dont rename
	public TextField getPedaddressfield() {
		return pedaddressfield;
	}

	// Dont rename
	public TextField getPeddescfield() {
		return peddescfield;
	}

	// Dont rename
	public TextField getPedlnamefield() {
		return pedlnamefield;
	}

	// Dont rename
	public TextField getPedfnamefield() {
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

	// #endregion

}