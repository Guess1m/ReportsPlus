package com.Guess.ReportsPlus.util.Report.Database;

import static com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager.getWindow;
import static com.Guess.ReportsPlus.Windows.Other.LayoutBuilderController.loadLayoutFromJSON;
import static com.Guess.ReportsPlus.Windows.Other.LayoutBuilderController.loadTransferConfigFromJSON;
import static com.Guess.ReportsPlus.Windows.Other.LayoutBuilderController.parseAndPopulateMap;
import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.notesViewController;
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
import static com.Guess.ReportsPlus.util.Report.reportUtil.pullValueFromReport;
import static com.Guess.ReportsPlus.util.Strings.customizationDataLoader.getValuesForField;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.Windows.Other.LayoutBuilderController.ILayoutType;
import com.Guess.ReportsPlus.Windows.Other.LayoutBuilderController.PedLayoutTypes;
import com.Guess.ReportsPlus.Windows.Other.LayoutBuilderController.VehicleLayoutTypes;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.util.Misc.NotificationManager;
import com.Guess.ReportsPlus.util.Report.nestedReportUtils;

import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
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
		var layout = loadLayoutFromJSON(layoutContent);
		nestedReportUtils.TransferConfig transferConfig = loadTransferConfigFromJSON(transferContent);

		Map<String, Object> reportWindow = createReportWindow(reportTitle, transferConfig,
				layout.toArray(new nestedReportUtils.SectionConfig[0]));
		Map<String, Object> reportMap = (Map<String, Object>) reportWindow.get(reportTitle + " Map");

		this.reportWindowMap = reportWindow;
		this.mainMap = newMap;
		this.reportMap = reportMap;

		if (newMap.getOrDefault("selectedType", new HashMap<>()).values().stream().flatMap(List::stream)
				.filter(type -> type.equalsIgnoreCase("NUMBER_FIELD")).count() != 1) {
			logError("CustomReport; Exactly one NUMBER_FIELD is required, found: "
					+ newMap.getOrDefault("selectedType", new HashMap<>()).values().stream().flatMap(List::stream)
							.filter(type -> type.equalsIgnoreCase("NUMBER_FIELD")).count());
			newMap.put("selectedType", null);
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

									if (uiComponent instanceof TextInputControl) {
										((TextInputControl) uiComponent).setText(valueToSet);
									} else if (uiComponent instanceof ComboBox) {
										ComboBox<String> comboBox = (ComboBox<String>) uiComponent;
										if (fieldConfig.getFieldType().toString().contains("COMBO_BOX_STREET")
												|| fieldConfig.getFieldType().toString().contains("COMBO_BOX_AREA")) {
											if (comboBox.getEditor() != null) {
												comboBox.getEditor().setText(valueToSet);
											}
										} else {
											comboBox.setValue(valueToSet);
										}
									} else if (uiComponent instanceof CheckBox) {
										CheckBox checkBox = (CheckBox) uiComponent;
										if (fieldConfig.getFieldType().toString().contains("CHECK_BOX")) {
											if (valueToSet.equalsIgnoreCase("true")
													|| valueToSet.equalsIgnoreCase("valid")
													|| valueToSet.equalsIgnoreCase("false")) {
												checkBox.setSelected(valueToSet.equalsIgnoreCase("true")
														|| valueToSet.equalsIgnoreCase("valid"));
											}
										}
									} else {
										logError("Cannot auto-fill unsupported component type: "
												+ uiComponent.getClass().getSimpleName());
									}
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

										logDebug("CustomReport; Clicked Database: " + dbFile.getName());
										createFolderIfNotExists(getCustomDataLogsFolderPath());

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

										Map<String, List<String>> currentReportKeyMap = newMap.get("keyMap");
										Map<String, List<String>> customReportKeyMap = customReportMap.get("keyMap");

										for (Map.Entry<String, List<String>> entry : currentReportKeyMap.entrySet()) {
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
															newMap.get("selectedType").get(currentField));

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

		Map<String, List<String>> dropdownTypeMap = newMap.getOrDefault("dropdownType", null);
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

		Label warningLabel = (Label) reportWindow.get("warningLabel");
		MenuButton pullNotesBtn = (MenuButton) reportWindow.get("pullNotesBtn");
		Button submitBtn = (Button) reportWindow.get("submitBtn");
		ComboBox<String> statusValue = (ComboBox) reportWindow.get("statusValue");

		MenuButton transferBox = (MenuButton) reportWindow.get("transferBox");
		if (transferBox != null) {
			transferBox.getItems().clear();
			File dataFolder = new File(dataFolderPath);

			if (!dataFolder.exists() || !dataFolder.isDirectory()) {
				return;
			}

			File[] dbFiles = dataFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".db"));
			if (dbFiles == null) {
				return;
			}

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
						logWarn("CustomReport; transferBox transfer selected an invalid database: " + dbFilePath);
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
						logError("CustomReport; Failed to extract schema for transfer target: " + targetReportName,
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

					Map<String, List<String>> currentKeyMap = newMap.get("keyMap");
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
								List<String> sourceFieldType = newMap.get("selectedType").get(sourceFieldName);
								String textToTransfer = extractTextFromUIComponent(sourceComponent,
										sourceFieldType);

								Object targetComponent = targetUIMap.get(targetFieldName);
								List<String> targetFieldType = targetMainMap.get("selectedType")
										.get(targetFieldName);

								if (sourceComponent != null && targetComponent != null && textToTransfer != null &&
										!textToTransfer.isEmpty()) {
									logDebug("Transferring data for key '" + key + "': {" + sourceFieldName + " -> "
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
							if (newMap == null) {
								logError("CustomReport; newMap is null");
								return;
							}
							for (String field : newMap.getOrDefault("nodeType", new HashMap<>()).keySet()) {
								logDebug("CustomReport; Processing field: " + field);

								Object fieldValue = reportMap.get(field);

								if (fieldValue == null) {
									logError("CustomReport; Field is null: " + field);
									continue;
								}

								String key = newMap.getOrDefault("keyMap", new HashMap<>()).entrySet().stream()
										.filter(entry -> entry.getValue().contains(field)).map(Map.Entry::getKey)
										.findFirst().orElse(null);

								if (key == null) {
									continue;
								}

								if (fieldValue instanceof TextField) {
									updateTextFromNotepad((TextField) fieldValue, noteArea, "-" + key);
								} else if (fieldValue instanceof ComboBox) {
									updateTextFromNotepad(((ComboBox<?>) fieldValue).getEditor(), noteArea, "-" + key);
								} else if (fieldValue instanceof TextArea) {
									updateTextFromNotepad((TextArea) fieldValue, noteArea, "-" + key);
								} else if (fieldValue instanceof CheckBox) {
									updateTextFromNotepad((CheckBox) fieldValue, noteArea, "-" + key);
								} else if (fieldValue instanceof AnchorPane) {

								} else {
									logError("CustomReport; Unknown field type: "
											+ fieldValue.getClass().getSimpleName());
								}
							}
						});
					}
				}
			}
		});

		submitBtn.setOnAction(_ -> {
			Map<String, List<String>> selectedTypes = newMap.getOrDefault("selectedType", new HashMap<>());
			Map<String, List<String>> fieldNames = newMap.getOrDefault("fieldNames", new HashMap<>());

			Map<String, Object> reportRecord = new HashMap<>();

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
							warningLabel.setVisible(true);
							warningLabel.setText("Number can't be empty!");
							warningLabel.setStyle("-fx-font-family: \"Inter 28pt Bold\"; -fx-text-fill: red;");
							PauseTransition pause = new PauseTransition(Duration.seconds(2));
							pause.setOnFinished(e2 -> warningLabel.setVisible(false));
							pause.play();
							logWarn("CustomReport; Number field blank, returning");
							return;
						} else {
							identifier = field;
							logDebug("CustomReport; Set identifier for report [" + reportTitle + "] to: " + identifier);
						}
					}
				}

				Object f = reportMap.get(field);
				if (f == null) {
					logError("CustomReport; Field " + field + " not found in map: " + reportMap);
				}

				if (f instanceof ComboBox<?>) {
					if (selectedType.equalsIgnoreCase("combo_box_street")
							|| selectedType.equalsIgnoreCase("combo_box_area")) {
						reportRecord.put(field, toTitleCase(((ComboBox) f).getEditor().getText().trim()));
					} else {
						if (((ComboBox) f).getValue() == null) {
							logWarn("CustomReport; Field " + field + " is null, putting N/A");
							reportRecord.put(field, "N/A");
						} else {
							reportRecord.put(field, ((ComboBox) f).getValue().toString().trim());
						}
					}
				} else if (f instanceof TextField) {
					reportRecord.put(field, toTitleCase(((TextInputControl) f).getText().trim()));
				} else if (f instanceof TextArea) {
					reportRecord.put(field, ((TextInputControl) f).getText().trim());
				} else if (f instanceof CheckBox) {
					String selected = ((CheckBox) f).isSelected() ? "true" : "false";
					reportRecord.put(field, selected);
				} else if (f instanceof AnchorPane) {

				} else {
					logError("CustomReport; Unknown field type: " + f.getClass().getSimpleName());
				}
			}

			reportRecord.put("report_status", statusValue.getValue().trim());

			if (identifier == null) {
				logError("CustomReport; Identifier is null");
				return;
			}

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
}
