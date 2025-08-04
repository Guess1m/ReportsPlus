package com.Guess.ReportsPlus.util.Report.Database;

import static com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager.getWindow;
import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.Windows.Apps.CourtViewController.getNextIndex;
import static com.Guess.ReportsPlus.Windows.Other.LayoutBuilderController.loadLayoutFromJSON;
import static com.Guess.ReportsPlus.Windows.Other.LayoutBuilderController.loadTransferConfigFromJSON;
import static com.Guess.ReportsPlus.Windows.Other.LayoutBuilderController.parseAndPopulateMap;
import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.notesViewController;
import static com.Guess.ReportsPlus.util.CourtData.CourtUtils.generateCaseNumber;
import static com.Guess.ReportsPlus.util.CourtData.CourtUtils.loadCourtCases;
import static com.Guess.ReportsPlus.util.CourtData.CourtUtils.parseCourtData;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.calculateAge;
import static com.Guess.ReportsPlus.util.Misc.AudioUtil.playSound;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logDebug;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logInfo;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logWarn;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationError;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationWarning;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.createFolderIfNotExists;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getCustomDataLogsFolderPath;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getJarPath;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.toTitleCase;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.updateTextFromNotepad;
import static com.Guess.ReportsPlus.util.Report.Database.DynamicDB.getPrimaryKeyColumn;
import static com.Guess.ReportsPlus.util.Report.Database.DynamicDB.isValidDatabase;
import static com.Guess.ReportsPlus.util.Report.reportUtil.createReportWindow;
import static com.Guess.ReportsPlus.util.Report.reportUtil.extractMaxFine;
import static com.Guess.ReportsPlus.util.Report.reportUtil.pullValueFromReport;
import static com.Guess.ReportsPlus.util.Report.treeViewUtils.findXMLValue;
import static com.Guess.ReportsPlus.util.Server.ClientUtils.isConnected;
import static com.Guess.ReportsPlus.util.Strings.customizationDataLoader.getValuesForField;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.Windows.Apps.CourtViewController;
import com.Guess.ReportsPlus.Windows.Other.LayoutBuilderController.ILayoutType;
import com.Guess.ReportsPlus.Windows.Other.LayoutBuilderController.PedLayoutTypes;
import com.Guess.ReportsPlus.Windows.Other.LayoutBuilderController.VehicleLayoutTypes;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.logs.ChargesData;
import com.Guess.ReportsPlus.logs.CitationsData;
import com.Guess.ReportsPlus.util.CourtData.Case;
import com.Guess.ReportsPlus.util.CourtData.CourtUtils;
import com.Guess.ReportsPlus.util.History.Ped;
import com.Guess.ReportsPlus.util.Misc.NotificationManager;
import com.Guess.ReportsPlus.util.Report.nestedReportUtils;
import com.Guess.ReportsPlus.util.Server.ClientUtils;

import jakarta.xml.bind.JAXBException;
import javafx.animation.PauseTransition;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class CustomReport {
	Map<String, Object> reportWindowMap;
	Map<String, Map<String, List<String>>> mainMap;
	Map<String, Object> reportMap;

	public CustomReport(String reportTitle, String dataTablePrimaryKey, Map<String, String> layoutScheme,
			Map<String, String> reportSchema, Map<String, Object> pullFromRecord,
			Map<String, Map<String, List<String>>> elementMap) {
		String dataFolderPath = getCustomDataLogsFolderPath();
		createFolderIfNotExists(dataFolderPath);
		if (reportTitle.isEmpty()) {
			logError("CustomReport; Report Title Field is Empty");
			return;
		}
		DynamicDB dbManager = null;
		try {
			dbManager = new DynamicDB(dataFolderPath + reportTitle, "data", dataTablePrimaryKey, reportSchema);
			dbManager.initDB();
		} catch (Exception e2) {
			logError("CustomReport; Failed to extract field names", e2);
		} finally {
			try {
				if (dbManager != null)
					dbManager.close();
			} catch (SQLException e2) {
				logError("CustomReport; Failed to close database connection, null", e2);
			}
		}
		String layoutContent = null;
		String transferContent = null;
		DynamicDB DatabaseLayout = new DynamicDB(dataFolderPath + reportTitle, "layout", "key", layoutScheme);
		if (DatabaseLayout.initDB()) {
			logInfo("CustomReport; Layout Database for: [" + reportTitle + "] Initialized");
			try {
				Map<String, Object> layoutRecord = DatabaseLayout.getRecord("1");
				if (layoutRecord != null) {
					Object layoutValue = layoutRecord.get("layoutData");
					layoutContent = (String) layoutValue;
				} else {
					logError("CustomReport; layout record null for report: " + reportTitle);
				}
				Map<String, Object> transferRecord = DatabaseLayout.getRecord("2");
				if (transferRecord != null) {
					Object layoutValue = transferRecord.get("transferData");
					transferContent = (String) layoutValue;
				} else {
					logError("CustomReport; transfer record null for report: " + reportTitle);
				}
			} catch (SQLException e2) {
				logError("CustomReport; Error adding/replacing record: " + layoutScheme, e2);
			} finally {
				try {
					DatabaseLayout.close();
				} catch (SQLException e2) {
					logError("CustomReport; Error closing Database: [" + DatabaseLayout.getTableName() + "]", e2);
				}
			}
		} else {
			logError("CustomReport; Layout Database not initialized!");
			showNotificationError("Report Creation Utility", "Error initializing layout database!");
		}
		Map<String, Map<String, List<String>>> newMap = parseAndPopulateMap(layoutContent);
		List<nestedReportUtils.SectionConfig> layout = loadLayoutFromJSON(layoutContent);
		nestedReportUtils.TransferConfig transferConfig = loadTransferConfigFromJSON(transferContent);
		if (layout == null) {
			logError("Failed to load layout from content for report: " + reportTitle);
			showNotificationError("Layout Error", "Could not load the report layout.");
			return;
		}
		Map<String, Object> reportWindow = createReportWindow(reportTitle, transferConfig,
				layout.toArray(new nestedReportUtils.SectionConfig[0]));
		Map<String, Object> reportMap = (Map<String, Object>) reportWindow.get(reportTitle + " Map");
		this.reportWindowMap = reportWindow;
		this.mainMap = newMap;
		this.reportMap = reportMap;
		if (newMap != null
				&& newMap.getOrDefault("selectedType", new HashMap<>()).values().stream().flatMap(List::stream)
						.filter(type -> type.equalsIgnoreCase("NUMBER_FIELD")).count() != 1) {
			logError("CustomReport; Exactly one NUMBER_FIELD is required.");
			return;
		}
		for (nestedReportUtils.SectionConfig sectionConfig : layout) {
			if (sectionConfig.isPullFromLookup()) {
				String sectionTitle = sectionConfig.getSectionTitle();
				String lookupType = sectionConfig.getLookupType();
				logInfo("Configuring 'Pull From' button for Section '" + sectionTitle + "' with lookup type: ["
						+ lookupType + "]");
				Button btn = (Button) reportWindow.get(sectionTitle + "_button_" + lookupType);
				if (btn == null) {
					logError("Could not find button: " + sectionTitle + "_button_" + lookupType);
					continue;
				}
				btn.setOnAction(_ -> {
					logDebug("Auto-filling section: " + sectionTitle);
					for (nestedReportUtils.RowConfig rowConfig : sectionConfig.getRowConfigs()) {
						for (nestedReportUtils.FieldConfig fieldConfig : rowConfig.getFieldConfigs()) {
							String fieldName = fieldConfig.getFieldName();
							String lookupKey = fieldConfig.getLookupValue();
							if (lookupKey != null && !lookupKey.isEmpty() && !lookupKey.equalsIgnoreCase("None")) {
								Object uiComponent = reportMap.get(fieldName);
								if (uiComponent == null) {
									logError("UI Component '" + fieldName + "' not found in reportMap.");
									continue;
								}
								String valueToSet = null;
								try {
									ILayoutType layoutType = null;
									if (isKeyInEnum(lookupKey, PedLayoutTypes.class)) {
										layoutType = PedLayoutTypes.valueOf(lookupKey.toUpperCase());
									} else if (isKeyInEnum(lookupKey, VehicleLayoutTypes.class)) {
										layoutType = VehicleLayoutTypes.valueOf(lookupKey.toUpperCase());
									}
									if (layoutType != null) {
										String internalKey = layoutType.getValue();
										if (internalKey.equals("fullname")) {
											String fulln = pullValueFromReport("ped", "Pedfnamefield") + " "
													+ pullValueFromReport("ped", "Pedlnamefield");
											if (!fulln.trim().isEmpty()) {
												valueToSet = fulln;
											}
										} else if (internalKey.equals("age")) {
											valueToSet = calculateAge(
													pullValueFromReport(lookupType, PedLayoutTypes.DOB.getValue()));
										} else {
											valueToSet = pullValueFromReport(lookupType, internalKey);
										}
									} else {
										logError("Invalid lookupKey found in report configuration: " + lookupKey);
									}
								} catch (Exception e) {
									logError("Error processing lookupKey: " + lookupKey, e);
								}
								if (valueToSet != null && !valueToSet.isEmpty()
										&& !valueToSet.equalsIgnoreCase("Not Found")) {
									logDebug("  -> Populating field: '" + fieldName + "' with value: '" + valueToSet
											+ "'" + " (lookupKey: " + lookupKey + ")");
									setTextInUIComponent(uiComponent, valueToSet,
											mainMap.get("selectedType").get(fieldName));
								}
							}
						}
					}
				});
			}
		}
		for (String name : reportMap.keySet()) {
			if (name.startsWith("transfer_")) {
				String[] values = name.split("_");
				Button btn = (Button) reportMap.get(name);
				btn.setText(values[1]);
				btn.setOnAction(btnAction -> {
					File dataFolder = new File(dataFolderPath);
					if (dataFolder.exists() && dataFolder.isDirectory()) {
						File[] files = dataFolder.listFiles((dir, filen) -> filen.endsWith(".db"));
						if (files != null && files.length != 0) {
							logInfo("CustomReport; Searching for [" + values[2] + "] Found " + files.length
									+ " Database(s)");
							boolean found = false;
							for (File dbFile : files) {
								String fileNameWithoutExt = dbFile.getName().replaceFirst("[.][^.]+$", "");
								if (fileNameWithoutExt.equals(values[2])) {
									String dbFilePath = dbFile.getAbsolutePath();
									logInfo("CustomReport; [" + dbFile.getName() + "] Being Checked");
									if (isValidDatabase(dbFilePath, dbFile.getName())) {
										logInfo("CustomReport; [" + dbFile.getName() + "] Valid");
										String data = null;
										try {
											data = getPrimaryKeyColumn(dbFilePath, "data");
										} catch (SQLException e) {
											logError("Error getting primary key column [2]", e);
										}
										Map<String, String> transferLayoutScheme = null;
										Map<String, String> transferSchema = null;
										try {
											transferLayoutScheme = DynamicDB.getTableColumnsDefinition(dbFilePath,
													"layout");
											transferSchema = DynamicDB.getTableColumnsDefinition(dbFilePath, "data");
										} catch (SQLException e2) {
											logError("CustomReport; Failed to extract field names", e2);
										}
										CustomReport customReport = new CustomReport(values[2], data,
												transferLayoutScheme, transferSchema, null, null);
										Map<String, Map<String, List<String>>> customReportMap = customReport
												.getMainMap();
										Map<String, Object> customReportUIMap = customReport.getReportMap();
										Map<String, List<String>> currentReportKeyMap = mainMap.get("keyMap");
										Map<String, List<String>> customReportKeyMap = customReportMap.get("keyMap");
										for (Map.Entry<String, List<String>> entry : currentReportKeyMap
												.entrySet()) {
											String key = entry.getKey();
											if (customReportKeyMap.containsKey(key)) {
												List<String> currentFields = entry.getValue();
												List<String> customFields = customReportKeyMap.get(key);
												int minSize = Math.min(currentFields.size(), customFields.size());
												for (int i = 0; i < minSize; i++) {
													String currentField = currentFields.get(i);
													String customField = customFields.get(i);
													Object currentUIComponent = reportMap.get(currentField);
													String textToTransfer = extractTextFromUIComponent(
															currentUIComponent,
															mainMap.get("selectedType").get(currentField));
													Object customUIComponent = customReportUIMap.get(customField);
													setTextInUIComponent(customUIComponent, textToTransfer,
															customReportMap.get("selectedType").get(customField));
												}
											}
										}
										found = true;
									} else {
										logWarn("CustomReport; Invalid database file: " + dbFilePath);
									}
								}
							}
							if (!found) {
								showNotificationWarning("Report Creation Utility",
										"Database [" + values[2] + "] not found!");
								logWarn("CustomReport; [" + values[2] + "] Not found");
							}
						}
					}
				});
			}
		}
		if (mainMap != null) {
			Map<String, List<String>> dropdownTypeMap = mainMap.getOrDefault("dropdownType", null);
			if (dropdownTypeMap != null) {
				for (Map.Entry<String, List<String>> entry : dropdownTypeMap.entrySet()) {
					String key = entry.getKey();
					List<String> values = entry.getValue();
					for (int i = 0; i < values.size(); i++) {
						String value = values.get(i);
						if (value != null) {
							Object component = reportMap.get(key);
							if (component instanceof ComboBox box) {
								if (!value.equalsIgnoreCase("null")) {
									box.getItems().addAll(getValuesForField(value));
								}
							}
						}
					}
				}
			}
		}
		Label warningLabel = (Label) reportWindow.get("warningLabel");
		MenuButton pullNotesBtn = (MenuButton) reportWindow.get("pullNotesBtn");
		Button submitBtn = (Button) reportWindow.get("submitBtn");
		ComboBox<String> statusValue = (ComboBox) reportWindow.get("statusValue");
		MenuButton transferBox = (MenuButton) reportWindow.get("transferBox");
		if (transferBox != null) {
			transferBox.getItems().clear();
			File dataFolder = new File(dataFolderPath);
			if (dataFolder.exists() && dataFolder.isDirectory()) {
				File[] dbFiles = dataFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".db"));
				if (dbFiles != null) {
					for (File dbFile : dbFiles) {
						String fileNameWithExt = dbFile.getName();
						String reportName = fileNameWithExt.substring(0, fileNameWithExt.lastIndexOf('.'));
						if (reportName.equals(reportTitle)) {
							continue;
						}
						MenuItem menuItem = new MenuItem(reportName);
						menuItem.setOnAction(_ -> {
							String targetReportName = menuItem.getText();
							String dbFilePath = dataFolderPath + targetReportName + ".db";
							if (!isValidDatabase(dbFilePath, targetReportName + ".db")) {
								logWarn("CustomReport; transferBox transfer selected an invalid database: "
										+ dbFilePath);
								showNotificationWarning("Report Transfer", "Selected report database is invalid.");
								return;
							}
							String targetPrimaryKey = null;
							Map<String, String> targetLayoutScheme = null;
							Map<String, String> targetDataSchema = null;
							try {
								targetPrimaryKey = getPrimaryKeyColumn(dbFilePath, "data");
								targetLayoutScheme = DynamicDB.getTableColumnsDefinition(dbFilePath, "layout");
								targetDataSchema = DynamicDB.getTableColumnsDefinition(dbFilePath, "data");
							} catch (SQLException e) {
								logError(
										"CustomReport; Failed to extract schema for transfer target: "
												+ targetReportName,
										e);
								showNotificationError("Report Transfer", "Could not read target report schema.");
								return;
							}
							if (targetPrimaryKey == null || targetLayoutScheme == null || targetDataSchema == null) {
								logError("CustomReport; Schema information for target report is incomplete: "
										+ targetReportName);
								showNotificationError("Report Transfer", "Target report information is incomplete.");
								return;
							}
							CustomReport targetReport = new CustomReport(targetReportName, targetPrimaryKey,
									targetLayoutScheme, targetDataSchema, null, null);
							Map<String, Map<String, List<String>>> targetMainMap = targetReport.getMainMap();
							Map<String, Object> targetUIMap = targetReport.getReportMap();
							Map<String, List<String>> currentKeyMap = mainMap.get("keyMap");
							Map<String, List<String>> targetKeyMap = targetMainMap.get("keyMap");
							for (Map.Entry<String, List<String>> entry : currentKeyMap.entrySet()) {
								String key = entry.getKey();
								if (targetKeyMap.containsKey(key)) {
									List<String> currentFields = entry.getValue();
									List<String> targetFields = targetKeyMap.get(key);
									int minSize = Math.min(currentFields.size(), targetFields.size());
									for (int i = 0; i < minSize; i++) {
										String sourceFieldName = currentFields.get(i);
										String targetFieldName = targetFields.get(i);
										Object sourceComponent = reportMap.get(sourceFieldName);
										List<String> sourceFieldType = mainMap.get("selectedType").get(sourceFieldName);
										String textToTransfer = extractTextFromUIComponent(sourceComponent,
												sourceFieldType);
										Object targetComponent = targetUIMap.get(targetFieldName);
										List<String> targetFieldType = targetMainMap.get("selectedType")
												.get(targetFieldName);
										if (sourceComponent != null && targetComponent != null && textToTransfer != null
												&&
												!textToTransfer.isEmpty()) {
											logDebug("Transferring data for key '" + key + "': {" + sourceFieldName
													+ " -> "
													+ targetFieldName + "} with value [" + textToTransfer + "]");
											setTextInUIComponent(targetComponent, textToTransfer, targetFieldType);
										}
									}
								}
							}
						});
						transferBox.getItems().add(menuItem);
					}
				}
			}
		}
		pullNotesBtn.setOnMouseEntered(event2 -> {
			pullNotesBtn.getItems().clear();
			if (notesViewController != null) {
				for (Tab tab : notesViewController.getTabPane().getTabs()) {
					MenuItem menuItem = new MenuItem(tab.getText());
					pullNotesBtn.getItems().add(menuItem);
					AnchorPane anchorPane = (AnchorPane) tab.getContent();
					TextArea noteArea = (TextArea) anchorPane.lookup("#notepadTextArea");
					if (noteArea != null) {
						menuItem.setOnAction(event3 -> {
							if (mainMap == null) {
								logError("CustomReport; mainMap is null");
								return;
							}
							for (String field : mainMap.getOrDefault("nodeType", new HashMap<>()).keySet()) {
								logDebug("CustomReport; Processing field: " + field);
								Object fieldValue = reportMap.get(field);
								if (fieldValue == null) {
									logError("CustomReport; Field is null: " + field);
									continue;
								}
								String key = mainMap.getOrDefault("keyMap", new HashMap<>()).entrySet().stream()
										.filter(entry -> entry.getValue().contains(field)).map(Map.Entry::getKey)
										.findFirst().orElse(null);
								if (key == null || key.equals("null")) {
									continue;
								}
								if (fieldValue instanceof TextField) {
									updateTextFromNotepad((TextField) fieldValue, noteArea, "-" + key);
								} else if (fieldValue instanceof ComboBox) {
									updateTextFromNotepad(((ComboBox<?>) fieldValue).getEditor(), noteArea, "-" + key);
								} else if (fieldValue instanceof TextArea) {
									updateTextFromNotepad((TextArea) fieldValue, noteArea, "-" + key);
								} else {
									logError("LayoutBuilder; Unknown field type: "
											+ fieldValue.getClass().getSimpleName());
								}
							}
						});
					}
				}
			}
		});

		submitBtn.setOnAction(_ -> {
			Map<String, List<String>> selectedTypes = mainMap.getOrDefault("selectedType", new HashMap<>());
			Map<String, List<String>> fieldNames = mainMap.getOrDefault("fieldNames", new HashMap<>());
			Map<String, Object> reportRecord = new HashMap<>();
			if (reportMap.containsKey("ChargeTableView")) {
				TableView<ChargesData> chargetable = (TableView<ChargesData>) reportMap.get("ChargeTableView");
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
					chargesBuilder.append(parseCourtData(isTraffic, probationChance, minYears, maxYears, minMonths,
							maxMonths, suspChance, minSusp, maxSusp, revokeChance, fine, finek)).append(" | ");
				}
				if (stringBuilder.length() > 0) {
					stringBuilder.setLength(stringBuilder.length() - 3);
				}
				reportRecord.put("Charges", stringBuilder.toString());
				Map<String, String> courtMapping = findCourtCaseMapping(layout, "CHARGES_TREE_VIEW");
				if (courtMapping != null) {
					String offenderName = getMappedValue(courtMapping, CourtCaseFields.SUSPECT_NAME, reportMap,
							mainMap);
					Optional<Ped> pedOptional = Ped.PedHistoryUtils.findPedByName(offenderName);
					if (pedOptional.isPresent()) {
						logDebug("Ped is present in history, adding new charges.. ");
						Ped ped1 = pedOptional.get();
						String beforePriors = ped1.getArrestPriors() != null ? ped1.getArrestPriors() : "";
						if (!beforePriors.contains(stringBuilder.toString())) {
							ped1.setArrestPriors(beforePriors + stringBuilder.toString().trim());
							try {
								Ped.PedHistoryUtils.addPed(ped1);
							} catch (JAXBException e) {
								logError("Error updating ped priors from custom report: ", e);
							}
						}
					}
					if (!offenderName.isEmpty() && stringBuilder.length() > 0) {
						String caseNumberField = getMappedValue(courtMapping, CourtCaseFields.CASE_NUMBER, reportMap,
								mainMap);
						String casenum = generateCaseNumber(caseNumberField);
						Case case1 = new Case();
						case1.setCaseNumber(casenum);
						case1.setName(toTitleCase(offenderName));
						case1.setOffenceDate(
								getMappedValue(courtMapping, CourtCaseFields.OFFENSE_DATE, reportMap, mainMap));
						case1.setCourtDate(
								getMappedValue(courtMapping, CourtCaseFields.OFFENSE_DATE, reportMap, mainMap));
						case1.setCaseTime(
								getMappedValue(courtMapping, CourtCaseFields.OFFENSE_TIME, reportMap, mainMap)
										.replace(".", ""));
						case1.setAge(toTitleCase(
								getMappedValue(courtMapping, CourtCaseFields.SUSPECT_AGE, reportMap, mainMap)));
						case1.setAddress(toTitleCase(
								getMappedValue(courtMapping, CourtCaseFields.SUSPECT_ADDRESS, reportMap, mainMap)));
						case1.setGender(toTitleCase(
								getMappedValue(courtMapping, CourtCaseFields.SUSPECT_GENDER, reportMap, mainMap)));
						case1.setCounty(
								toTitleCase(
										getMappedValue(courtMapping, CourtCaseFields.COUNTY, reportMap, mainMap)));
						case1.setStreet(
								toTitleCase(
										getMappedValue(courtMapping, CourtCaseFields.STREET, reportMap, mainMap)));
						case1.setArea(
								toTitleCase(
										getMappedValue(courtMapping, CourtCaseFields.AREA, reportMap, mainMap)));
						case1.setNotes(getMappedValue(courtMapping, CourtCaseFields.NOTES, reportMap, mainMap));
						case1.setOffences(stringBuilder.toString());
						case1.setOutcomes(chargesBuilder.toString());
						case1.setStatus("Pending");
						try {
							case1.setIndex(getNextIndex(loadCourtCases()));
							CourtUtils.addCase(case1);
							CourtUtils.scheduleOutcomeRevealForSingleCase(case1.getCaseNumber());
							NotificationManager.showNotificationInfo("Report Manager",
									"A new court case has been created. Case#: " + case1.getCaseNumber());
							logInfo("Added case from custom report, Case#: " + case1.getCaseNumber() + " Name: "
									+ offenderName);
							CourtViewController.needCourtRefresh.set(1);
						} catch (JAXBException | IOException e) {
							logError("Failed to create or schedule court case: ", e);
							showNotificationError("Court Case Error", "Failed to create or schedule court case.");
						}
					} else {
						showWarning(warningLabel,
								"Mapped Name or Offences can't be blank");
						logWarn(
								"Could not create court case from report because either mapped name or offences were empty.");
						return;
					}
				}
			}
			if (reportMap.containsKey("CitationTableView")) {
				TableView<CitationsData> citationTable = (TableView<CitationsData>) reportMap.get("CitationTableView");
				ComboBox<String> citationType = (ComboBox<String>) reportMap.get("CitationType");
				Map<String, String> courtMapping = findCourtCaseMapping(layout, "CITATION_TREE_VIEW");
				if (citationType.getValue() == null || citationType.getValue().toString().trim().isEmpty()) {
					citationType.getSelectionModel().selectFirst();
				}
				if (courtMapping != null) {
					String offenderNameValue = getMappedValue(courtMapping, CourtCaseFields.SUSPECT_NAME, reportMap,
							mainMap);
					String plateNumberValue = getMappedValue(courtMapping, CourtCaseFields.PLATE_NUMBER, reportMap,
							mainMap);
					String selectedCitationType = citationType.getValue();
					String printedCitationStr = localization.getLocalizedMessage("ReportWindows.CitationTypePrinted",
							"Printed Citation");
					String parkingCitationStr = localization.getLocalizedMessage("ReportWindows.CitationTypeParking",
							"Parking Citation");
					if (selectedCitationType.equalsIgnoreCase(printedCitationStr)
							&& offenderNameValue.trim().isEmpty()) {
						showWarning(warningLabel, "Offender Name Field Empty!");
						logError("Offender Name Cant Be Empty if printing a ticket.");
						return;
					} else if (selectedCitationType.equalsIgnoreCase(parkingCitationStr)) {
						if (offenderNameValue.trim().isEmpty()) {
							showWarning(warningLabel, "Offender Name must be filled for Parking Citations!");
							logError("Offender Name Cant Be Empty if issuing a parking ticket.");
							return;
						}
						if (plateNumberValue.trim().isEmpty()) {
							showWarning(warningLabel, "Plate Number must be filled for Parking Citations!");
							logError("Vehicle Plate Cant Be Empty if issuing a parking ticket.");
							return;
						}
					}
				}
				ObservableList<CitationsData> formDataList = citationTable.getItems();
				StringBuilder stringBuilder = new StringBuilder();
				StringBuilder chargesBuilder = new StringBuilder();
				for (CitationsData formData : formDataList) {
					stringBuilder.append(formData.getCitation()).append(" | ");
					String fine = findXMLValue(formData.getCitation(), "fine", "data/Citations.xml");
					if (fine != null) {
						chargesBuilder.append("Fined: ").append(fine).append(" | ");
					} else if (formData.getCitation().contains("MaxFine:")) {
						int maxFine = Integer.parseInt(Objects.requireNonNull(extractMaxFine(formData.getCitation())));
						chargesBuilder.append("Fined: ").append(maxFine).append(" | ");
					} else {
						chargesBuilder.append("Fined: Not Found | ");
					}
				}
				if (stringBuilder.length() > 0) {
					stringBuilder.setLength(stringBuilder.length() - 3);
				}
				reportRecord.put("Citations", stringBuilder.toString());
				reportRecord.put("Citation Type",
						citationType.getValue() != null ? citationType.getValue().toString().trim() : "N/A");
				if (courtMapping != null) {
					String offenderName = getMappedValue(courtMapping, CourtCaseFields.SUSPECT_NAME, reportMap,
							mainMap);
					Optional<Ped> pedOptional = Ped.PedHistoryUtils.findPedByName(offenderName);
					if (pedOptional.isPresent()) {
						logDebug("Ped is present in history, adding new citations.. ");
						Ped ped1 = pedOptional.get();
						String beforePriors = ped1.getCitationPriors() != null ? ped1.getCitationPriors() : "";
						if (!beforePriors.contains(stringBuilder.toString())) {
							ped1.setCitationPriors(beforePriors + stringBuilder.toString().trim());
							try {
								Ped.PedHistoryUtils.addPed(ped1);
							} catch (JAXBException e) {
								logError("Error updating ped priors from citation report: ", e);
							}
						}
					}
					if (!offenderName.isEmpty() && stringBuilder.length() > 0) {
						String caseNumberField = getMappedValue(courtMapping, CourtCaseFields.CASE_NUMBER, reportMap,
								mainMap);
						String casenum = generateCaseNumber(caseNumberField);
						Case case1 = new Case();
						case1.setCaseNumber(casenum);
						case1.setName(toTitleCase(offenderName));
						case1.setOffenceDate(
								getMappedValue(courtMapping, CourtCaseFields.OFFENSE_DATE, reportMap, mainMap));
						case1.setCourtDate(
								getMappedValue(courtMapping, CourtCaseFields.OFFENSE_DATE, reportMap, mainMap));
						case1.setCaseTime(
								getMappedValue(courtMapping, CourtCaseFields.OFFENSE_TIME, reportMap, mainMap)
										.replace(".", ""));
						case1.setAge(toTitleCase(
								getMappedValue(courtMapping, CourtCaseFields.SUSPECT_AGE, reportMap, mainMap)));
						case1.setAddress(toTitleCase(
								getMappedValue(courtMapping, CourtCaseFields.SUSPECT_ADDRESS, reportMap, mainMap)));
						case1.setGender(toTitleCase(
								getMappedValue(courtMapping, CourtCaseFields.SUSPECT_GENDER, reportMap, mainMap)));
						case1.setCounty(
								toTitleCase(
										getMappedValue(courtMapping, CourtCaseFields.COUNTY, reportMap, mainMap)));
						case1.setStreet(
								toTitleCase(
										getMappedValue(courtMapping, CourtCaseFields.STREET, reportMap, mainMap)));
						case1.setArea(
								toTitleCase(
										getMappedValue(courtMapping, CourtCaseFields.AREA, reportMap, mainMap)));
						case1.setNotes(getMappedValue(courtMapping, CourtCaseFields.NOTES, reportMap, mainMap));
						case1.setOffences(stringBuilder.toString());
						case1.setOutcomes(chargesBuilder.toString());
						case1.setStatus("Pending");
						try {
							case1.setIndex(getNextIndex(CourtUtils.loadCourtCases()));
							CourtUtils.addCase(case1);
							CourtUtils.scheduleOutcomeRevealForSingleCase(case1.getCaseNumber());
							NotificationManager.showNotificationInfo("Report Manager",
									"A new citation case has been created. Case#: " + case1.getCaseNumber());
							logInfo("Added case from citation, Case#: " + case1.getCaseNumber() + " Name: "
									+ offenderName);
							CourtViewController.needCourtRefresh.set(1);
						} catch (JAXBException | IOException e) {
							logError("Failed to create or schedule citation case: ", e);
							showNotificationError("Court Case Error",
									"Failed to create or schedule citation case.");
						}
						if (isConnected) {
							String plateNumber = getMappedValue(courtMapping, CourtCaseFields.PLATE_NUMBER,
									reportMap, mainMap);
							String selectedCitationType = citationType.getValue();
							int type = 1;
							if (selectedCitationType.equalsIgnoreCase(localization
									.getLocalizedMessage("ReportWindows.CitationTypePrinted", "Printed Citation")))
								type = 2;
							else if (selectedCitationType.equalsIgnoreCase(localization
									.getLocalizedMessage("ReportWindows.CitationTypeParking", "Parking Citation")))
								type = 3;
							ClientUtils.sendMessageToServer("CITATION_UPDATE:name=" + offenderName.trim()
									+ "|plate=" + plateNumber.trim() + "|type=" + type);
						}
					} else {
						showWarning(warningLabel,
								"Mapped Name or Offences can't be blank");
						logWarn(
								"Could not create court case from citation because either mapped name or offences were empty.");
						return;
					}
				}
			}
			String identifier = null;
			for (String fieldName : reportMap.keySet()) {
				Object field = reportMap.get(fieldName);
				if (field instanceof ComboBox<?> comboBox) {
					if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
						comboBox.getSelectionModel().selectFirst();
					}
				}
			}
			for (String field : fieldNames.keySet()) {
				String selectedType = selectedTypes.getOrDefault(field, List.of()).isEmpty() ? ""
						: selectedTypes.get(field).get(0);
				if ("NUMBER_FIELD".equalsIgnoreCase(selectedType)) {
					if (reportMap.get(field) instanceof TextField numberField) {
						if (numberField.getText().trim().isEmpty()) {
							showWarning(warningLabel, "Number can't be empty!");
							logWarn("CustomReport; Number field blank, returning");
							return;
						} else {
							identifier = numberField.getText().trim();
							logDebug("CustomReport; Set identifier for report [" + reportTitle + "] to: " + identifier);
						}
					}
				}
				Object f = reportMap.get(field);
				if (f == null) {
					logError("CustomReport; Field " + field + " not found in map: " + reportMap);
					continue;
				}
				String valueToStore = extractTextFromUIComponent(f, selectedTypes.get(field));
				if (f instanceof TextField) {
					reportRecord.put(field, toTitleCase(valueToStore));
				} else if (f instanceof TextArea) {
					reportRecord.put(field, valueToStore);
				} else {
					reportRecord.put(field, valueToStore);
				}
			}
			reportRecord.put("report_status", statusValue.getValue().trim());
			if (identifier == null) {
				logError("CustomReport; Identifier is null, cannot save report.");
				showNotificationError("Report Manager",
						"Could not save report, required Number field is missing or misconfigured.");
				return;
			}
			reportRecord.put(dataTablePrimaryKey, identifier);
			DynamicDB Database = new DynamicDB(dataFolderPath + reportTitle, "data", dataTablePrimaryKey, reportSchema);
			if (Database.initDB()) {
				logInfo("CustomReport; Database initialized");
				try {
					Database.addOrReplaceRecord(reportRecord);
				} catch (SQLException e2) {
					logError("CustomReport; Error adding/replacing record: " + reportRecord, e2);
				} finally {
					try {
						Database.close();
					} catch (SQLException e2) {
						logError("CustomReport; Error closing Database: [" + Database.getTableName() + "]", e2);
					}
				}
			} else {
				logError("CustomReport; Database not initialized!");
				showNotificationError("Report Creation Utility", "Error initializing database!");
			}
			try {
				if (ConfigReader.configRead("soundSettings", "playCreateReport").equalsIgnoreCase("true")) {
					playSound(getJarPath() + "/sounds/alert-success.wav");
				}
			} catch (IOException e) {
				logError("CustomReport; Error getting configValue for playCreateReport: ", e);
			}
			NotificationManager.showNotificationInfo("Report Manager", "A new " + reportTitle + " has been submitted.");
			CustomWindow window = getWindow(reportTitle);
			if (window != null) {
				window.closeWindow();
			}
		});

		if (pullFromRecord != null && elementMap != null) {
			for (String fieldName : pullFromRecord.keySet()) {
				Object field = reportMap.get(fieldName);
				if (field == null) {
					logError("CustomReport; Field [" + fieldName + "] not found in reportMap.");
					continue;
				}
				Object value = pullFromRecord.get(fieldName);
				if (value == null) {
					logError("CustomReport; Value for field [" + fieldName + "] is null in pullFromrecord.");
					continue;
				}
				List<String> selectedType = elementMap.get("selectedType") != null
						? elementMap.get("selectedType").get(fieldName)
						: null;
				if (selectedType == null) {
					logError("CustomReport; Selected type for field [" + fieldName + "] is null in elementMap.");
					continue;
				}
				setTextInUIComponent(field, value.toString(), selectedType);
			}
		}
	}

	public Map<String, Object> getReportWindowMap() {
		return reportWindowMap;
	}

	public Map<String, Map<String, List<String>>> getMainMap() {
		return mainMap;
	}

	public Map<String, Object> getReportMap() {
		return reportMap;
	}

	private String extractTextFromUIComponent(Object uiComponent, List<String> selectedType) {
		if (uiComponent instanceof TextInputControl) {
			return ((TextInputControl) uiComponent).getText();
		} else if (uiComponent instanceof ComboBox) {
			ComboBox<?> comboBox = (ComboBox<?>) uiComponent;
			if (selectedType.contains("COMBO_BOX_STREET") || selectedType.contains("COMBO_BOX_AREA")) {
				if (comboBox.getEditor() != null) {
					return comboBox.getEditor().getText();
				}
			} else {
				if (comboBox.getValue() != null) {
					return comboBox.getValue().toString();
				}
			}
		} else if (uiComponent instanceof CheckBox) {
			CheckBox checkBox = (CheckBox) uiComponent;
			if (selectedType.contains("CHECK_BOX")) {
				return checkBox.isSelected() ? "true" : "false";
			}
		}
		return "";
	}

	private void setTextInUIComponent(Object uiComponent, String text, List<String> selectedType) {
		if (uiComponent instanceof TextInputControl) {
			((TextInputControl) uiComponent).setText(text);
		} else if (uiComponent instanceof ComboBox) {
			ComboBox<String> comboBox = (ComboBox<String>) uiComponent;
			if (selectedType.contains("COMBO_BOX_STREET") || selectedType.contains("COMBO_BOX_AREA")) {
				if (comboBox.getEditor() != null) {
					comboBox.getEditor().setText(text);
					comboBox.setValue(text);
				}
			} else {
				comboBox.setValue(text);
			}
		} else if (uiComponent instanceof CheckBox) {
			CheckBox checkBox = (CheckBox) uiComponent;
			if (selectedType.contains("CHECK_BOX")) {
				if (text.equalsIgnoreCase("true") || text.equalsIgnoreCase("false")) {
					if (text.equalsIgnoreCase("true")) {
						checkBox.setSelected(true);
					} else {
						checkBox.setSelected(false);
					}
				} else {
					showNotificationError("Custom Reports",
							"Checkbox stored value not true or false, check logs! Value: [" + text + "]");
				}
			}
		} else if (uiComponent instanceof AnchorPane) {
		} else {
			logError("CustomReport; Unknown UI component type: " + uiComponent.getClass().getSimpleName());
		}
	}

	public static <E extends Enum<E>> boolean isKeyInEnum(String key, Class<E> enumClass) {
		if (key == null) {
			return false;
		}
		try {
			Enum.valueOf(enumClass, key.toUpperCase());
			return true;
		} catch (IllegalArgumentException ex) {
			return false;
		}
	}

	public enum CourtCaseFields {
		CASE_NUMBER("Case Number"),
		SUSPECT_NAME("Suspect Name"),
		SUSPECT_AGE("Suspect Age"),
		SUSPECT_GENDER("Suspect Gender"),
		SUSPECT_ADDRESS("Suspect Address"),
		PLATE_NUMBER("Plate Number"),
		OFFENSE_DATE("Offense Date"),
		OFFENSE_TIME("Offense Time"),
		COUNTY("County"),
		STREET("Street"),
		AREA("Area"),
		NOTES("Notes");

		private final String displayName;

		CourtCaseFields(String displayName) {
			this.displayName = displayName;
		}

		public String getDisplayName() {
			return displayName;
		}
	}

	private Map<String, String> findCourtCaseMapping(List<nestedReportUtils.SectionConfig> layout, String fieldType) {
		if (layout == null)
			return null;
		for (nestedReportUtils.SectionConfig section : layout) {
			for (nestedReportUtils.RowConfig row : section.getRowConfigs()) {
				for (nestedReportUtils.FieldConfig field : row.getFieldConfigs()) {
					if (field.getFieldType().name().equals(fieldType) && field.getCourtCaseMapping() != null) {
						return field.getCourtCaseMapping();
					}
				}
			}
		}
		return null;
	}

	private String getMappedValue(Map<String, String> mapping, CourtCaseFields key, Map<String, Object> reportMap,
			Map<String, Map<String, List<String>>> mainMap) {
		if (mapping == null) {
			logError("Court case mapping is null. Cannot get value for key: " + key.name());
			return "";
		}
		String fieldName = mapping.get(key.name());
		if (fieldName == null) {
			logWarn("Court case mapping not found for key: " + key.name());
			return "";
		}
		Object uiComponent = reportMap.get(fieldName);
		if (uiComponent == null) {
			logError("UI Component '" + fieldName + "' for mapped key '" + key.name() + "' not found in reportMap.");
			return "";
		}
		List<String> selectedType = mainMap.get("selectedType").get(fieldName);
		return extractTextFromUIComponent(uiComponent, selectedType);
	}

	private void showWarning(Label warningLabel, String message) {
		warningLabel.setText(message);
		warningLabel.setStyle("-fx-font-family: \"Inter 28pt Bold\"; -fx-text-fill: red;");
		warningLabel.setVisible(true);
		PauseTransition pause = new PauseTransition(Duration.seconds(3));
		pause.setOnFinished(e -> warningLabel.setVisible(false));
		pause.play();
	}
}
