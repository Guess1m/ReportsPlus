package com.Guess.ReportsPlus.Windows.Other;

import static com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager.createCustomWindow;
import static com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager.getWindow;
import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.MainApplication.mainDesktopControllerObj;
import static com.Guess.ReportsPlus.MainApplication.mainRT;
import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.notesViewController;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logDebug;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logInfo;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logWarn;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationError;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationInfo;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationWarning;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.createFolderIfNotExists;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getCustomDataLogsFolderPath;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.updateTextFromNotepad;
import static com.Guess.ReportsPlus.util.Report.Database.DynamicDB.getLayoutJsonFromDb;
import static com.Guess.ReportsPlus.util.Report.Database.DynamicDB.isValidDatabase;
import static com.Guess.ReportsPlus.util.Report.reportUtil.createReportWindow;
import static com.Guess.ReportsPlus.util.Strings.customizationDataLoader.DEFAULT_FIELDS;
import static com.Guess.ReportsPlus.util.Strings.customizationDataLoader.addCustomField;
import static com.Guess.ReportsPlus.util.Strings.customizationDataLoader.deleteCustomField;
import static com.Guess.ReportsPlus.util.Strings.customizationDataLoader.getCustomizationFields;
import static com.Guess.ReportsPlus.util.Strings.customizationDataLoader.getValuesForField;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;

import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager.IShutdownable;
import com.Guess.ReportsPlus.util.Report.nestedReportUtils.FieldConfig;
import com.Guess.ReportsPlus.util.Report.nestedReportUtils.FieldType;
import com.Guess.ReportsPlus.util.Report.nestedReportUtils.FullLayoutConfig;
import com.Guess.ReportsPlus.util.Report.nestedReportUtils.RowConfig;
import com.Guess.ReportsPlus.util.Report.nestedReportUtils.SectionConfig;
import com.Guess.ReportsPlus.util.Report.nestedReportUtils.TransferConfig;
import com.Guess.ReportsPlus.util.Report.Database.CustomReport.CourtCaseFields;
import com.Guess.ReportsPlus.util.Report.Database.DynamicDB;
import com.Guess.ReportsPlus.util.Strings.reportLayoutTemplates;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class LayoutBuilderController implements IShutdownable {
	// TODO: !inprogress
	// documentation for custom reports
	// Add field sobriety
	CustomWindow addDropdownWindow = null;
	public static LayoutBuilderController layoutBuilderController;
	// #region FXML Components
	@FXML
	private ScrollPane customLayoutScrollPane;
	@FXML
	private VBox containersVbox;
	@FXML
	private VBox sectionContainer;
	@FXML
	private Button addSectionButton;
	@FXML
	private Button buildLayoutButton;
	@FXML
	private TextField reportTitleField;
	@FXML
	private Button addTransferButton;
	@FXML
	private VBox transferContainer;
	@FXML
	private Button customDropdownButton;
	@FXML
	private Button importExportButton;
	@FXML
	private Label heading;

	// #endregion
	// #region LayoutTypes
	public interface ILayoutType {
		String getValue();

		String name();
	}

	public enum PedLayoutTypes implements ILayoutType {
		FIRST_NAME("Pedfnamefield"), LAST_NAME("Pedlnamefield"), FULL_NAME("fullname"), DOB("Peddobfield"), AGE("age"),
		GENDER("Pedgenfield"),
		ADDRESS("Pedaddressfield"), DESCRIPTION("Peddescfield"), LICENSE_STATUS("pedlicensefield"),
		LICENSE_EXP("peddriverlicenseexpirationfield"), LICENSE_NUMBER("pedlicnumfield"),
		TIMES_STOPPED("pedtimesstoppedfield"), WANTED_STATUS("pedwantedfield"),
		PROBATION_STATUS("pedprobationstatusfield"),
		PAROLE_STATUS("pedparolestatusfield"), BOATING_LICENSE_STATUS("pedboatinglicstatusfield"),
		FISHING_LICENSE_STATUS("pedfishinglicstatusfield"),
		HUNTING_LICENSE_STATUS("pedhuntinglicstatusfield"), GUN_LICENSE_STATUS("pedgunlicensestatusfield"),
		FLAGS("pedflagfield"), AFFILIATIONS("pedaffiliationfield"),
		ALIASES("pedaliasfield"),
		MARITAL_STATUS("pedmaritalstatusfield"), CITIZENSHIP_STATUS("pedcitizenshipstatusfield"),
		DISABILITY("peddisabilityfield"),
		HEIGHT("pedheightfield"),
		WEIGHT("pedweightfield");

		private final String value;

		PedLayoutTypes(String value) {
			this.value = value;
		}

		@Override
		public String getValue() {
			return value;
		}
	}

	public enum VehicleLayoutTypes implements ILayoutType {
		PLATE_NUMBER("vehplatefield2"), VEHICLE_MAKE("vehmakefield"), MODEL("vehmodelfield"),
		STOLEN_STATUS("vehstolenfield"),
		IS_POLICE("vehpolicefield"),
		VEHICLE_TYPE("vehtypecombobox"),
		OWNER_NAME("vehownerfield"),
		REGISTRATION_STATUS("vehregstatusfield"), REGISTRATION_NUM("vehregnumberfield"),
		REGISTRATION_EXPIRATION("vehregexpfield"), INSURANCE_STATUS("vehinsstatusfield"),
		INSURANCE_NUMBER("vehinsnumberfield"), INSURANCE_EXPIRATION("vehinsexpfield"),
		INSURANCE_COVERAGE("vehinscoveragefield"),
		INSPECTION_STATUS("vehinspectionfield"),
		VIN("vehvinfield");

		private final String value;

		VehicleLayoutTypes(String value) {
			this.value = value;
		}

		@Override
		public String getValue() {
			return value;
		}
	}

	// #endregion
	// #region CustomReportTypes
	private class SectionPane extends VBox {
		private TextField sectionTitleField;
		private VBox rowContainer;
		private Button addRowButton;
		private Button removeSectionButton;
		private CheckBox pullFromLookupCheckbox;
		private ComboBox<String> lookupTypeComboBox;

		SectionPane() {
			setSpacing(10);
			setStyle("-fx-background-color: #ECEFF1; " +
					"-fx-background-radius: 8; " +
					"-fx-border-radius: 8; " +
					"-fx-border-width: 2; " +
					"-fx-border-color: #B0BEC5; " +
					"-fx-padding: 12;");
			HBox header = new HBox(10);
			header.setAlignment(Pos.CENTER_LEFT);
			header.setPadding(new Insets(0, 0, 10, 0));
			header.setStyle("-fx-border-color: transparent transparent #CFD8DC transparent; -fx-border-width: 1.5;");
			sectionTitleField = new TextField();
			sectionTitleField.setPromptText(localization.getLocalizedMessage("ReportWindows.SectionButton", "Section")
					+ " " + localization.getLocalizedMessage("Login_Window.NamePromptText", "Name"));
			Button moveUpButton = new Button("↑");
			Button moveDownButton = new Button("↓");
			moveUpButton.setFocusTraversable(false);
			moveDownButton.setFocusTraversable(false);
			moveUpButton.getStyleClass().add("moveButton");
			moveDownButton.getStyleClass().add("moveButton");
			moveUpButton.setOnAction(_ -> {
				int index = sectionContainer.getChildren().indexOf(this);
				if (index > 0) {
					sectionContainer.getChildren().remove(index);
					sectionContainer.getChildren().add(index - 1, this);
				}
			});
			moveDownButton.setOnAction(_ -> {
				int index = sectionContainer.getChildren().indexOf(this);
				if (index < sectionContainer.getChildren().size() - 1) {
					sectionContainer.getChildren().remove(index);
					sectionContainer.getChildren().add(index + 1, this);
				}
			});
			removeSectionButton = new Button(localization.getLocalizedMessage("ReportWindows.RemoveButton", "Remove")
					+ " " + localization.getLocalizedMessage("ReportWindows.SectionButton", "Section"));
			removeSectionButton.setFocusTraversable(false);
			removeSectionButton.getStyleClass().add("removeButton");
			removeSectionButton.setOnAction(_ -> sectionContainer.getChildren().remove(this));
			pullFromLookupCheckbox = new CheckBox("Pull from Lookup");
			lookupTypeComboBox = new ComboBox<>();
			lookupTypeComboBox.getItems().addAll("Ped", "Vehicle", "Both");
			lookupTypeComboBox.getSelectionModel().selectFirst();
			lookupTypeComboBox.setVisible(false);
			lookupTypeComboBox.setManaged(false);
			lookupTypeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
				if (newVal != null) {
					for (Node rowNode : rowContainer.getChildren()) {
						if (rowNode instanceof RowPane) {
							for (Node fieldNode : ((RowPane) rowNode).fieldContainer.getChildren()) {
								if (fieldNode instanceof FieldPane) {
									((FieldPane) fieldNode).updateLookupOptions(newVal);
								}
							}
						}
					}
				}
			});
			pullFromLookupCheckbox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
				lookupTypeComboBox.setVisible(isNowSelected);
				lookupTypeComboBox.setManaged(isNowSelected);
				for (Node rowNode : rowContainer.getChildren()) {
					if (rowNode instanceof RowPane) {
						for (Node fieldNode : ((RowPane) rowNode).fieldContainer.getChildren()) {
							if (fieldNode instanceof FieldPane) {
								((FieldPane) fieldNode).setLookupValueSelectorVisible(isNowSelected);
							}
						}
					}
				}
			});
			Pane spacer = new Pane();
			HBox.setHgrow(spacer, Priority.ALWAYS);
			header.getChildren().addAll(
					new Label(localization.getLocalizedMessage("ReportWindows.SectionButton", "Section") + ":"),
					sectionTitleField, pullFromLookupCheckbox, lookupTypeComboBox, spacer, moveUpButton, moveDownButton,
					removeSectionButton);
			rowContainer = new VBox(8);
			rowContainer.setStyle("-fx-background-color: transparent; -fx-padding: 10 2 2 2;");
			addRowButton = new Button(localization.getLocalizedMessage("ReportWindows.AddButton", "Add") + " "
					+ localization.getLocalizedMessage("ReportWindows.RowButton", "Row"));
			addRowButton.getStyleClass().add("addButton");
			addRowButton.setOnAction(_ -> addRow());
			addRowButton.setFocusTraversable(false);
			getChildren().addAll(header, rowContainer, addRowButton);
		}

		private void addRow() {
			RowPane rowPane = new RowPane();
			rowContainer.getChildren().add(rowPane);
		}

		private SectionConfig buildSectionConfig() {
			String title = sectionTitleField.getText();
			List<RowConfig> rowConfigs = new ArrayList<>();
			for (Node node : rowContainer.getChildren()) {
				if (node instanceof RowPane) {
					rowConfigs.add(((RowPane) node).buildRowConfig());
				}
			}
			SectionConfig config = new SectionConfig(title, rowConfigs.toArray(new RowConfig[0]));
			boolean pullFromLookup = pullFromLookupCheckbox.isSelected();
			config.setPullFromLookup(pullFromLookup);
			if (pullFromLookup) {
				config.setLookupType(lookupTypeComboBox.getValue());
			}
			return config;
		}
	}

	private class FieldPane extends HBox {
		private TextField fieldNameField;
		private TextField populateKeyField;
		private Spinner<Integer> widthSpinner;
		private ComboBox<String> fieldTypeComboBox;
		private ComboBox<String> customDropdownComboBox;
		private ComboBox<String> lookupValueComboBox;
		private Label lookupValueLabel;
		private Button removeFieldButton;
		private Button moveLeftButton;
		private Button moveRightButton;
		private VBox courtCaseMappingPane;
		private Map<CourtCaseFields, ComboBox<String>> courtCaseMappingBoxes = new HashMap<>();
		private Spinner<Integer> growthSpinner;

		FieldPane() {
			super(10);
			setStyle(
					"-fx-background-color: white; " +
							"-fx-border-color: #ECEFF1; " +
							"-fx-border-width: 1; " +
							"-fx-border-radius: 4; " +
							"-fx-background-radius: 4; " +
							"-fx-padding: 10;");
			VBox leftControls = new VBox(5);
			fieldNameField = new TextField();
			fieldNameField.setPromptText(localization.getLocalizedMessage("ReportWindows.FieldButton", "Field") + " "
					+ localization.getLocalizedMessage("Login_Window.NamePromptText", "Name"));
			populateKeyField = new TextField();
			populateKeyField.setPromptText("Populate Key (optional)");
			growthSpinner = new Spinner<>(1, 12, 1);
			growthSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
				if (getParent() != null && getParent().getParent() instanceof RowPane) {
					((RowPane) getParent().getParent()).updateFieldWidths();
				}
			});
			fieldTypeComboBox = new ComboBox<>();
			fieldTypeComboBox.getItems().add("NUMBER_FIELD");
			fieldTypeComboBox.getItems().addAll("CHARGES_TREE_VIEW", "CITATION_TREE_VIEW", "TEXT_FIELD",
					"CUSTOM_DROPDOWN", "CHECK_BOX", "COMBO_BOX_STREET", "COMBO_BOX_AREA", "COUNTY_FIELD",
					"COMBO_BOX_COLOR", "COMBO_BOX_TYPE", "BLANK_SPACE", "TEXT_AREA", "TIME_FIELD", "DATE_FIELD",
					"OFFICER_NAME", "OFFICER_RANK", "OFFICER_DIVISION", "OFFICER_AGENCY", "OFFICER_NUMBER",
					"OFFICER_CALLSIGN");
			fieldTypeComboBox.getSelectionModel().selectFirst();
			lookupValueComboBox = new ComboBox<>();
			updateLookupOptions("Ped");
			lookupValueComboBox.getSelectionModel().selectFirst();
			lookupValueComboBox.setVisible(false);
			lookupValueComboBox.setManaged(false);
			lookupValueLabel = new Label("Lookup Value:");
			lookupValueLabel.setVisible(false);
			lookupValueLabel.setManaged(false);
			courtCaseMappingPane = new VBox(5);
			courtCaseMappingPane.setStyle(
					"-fx-border-color: #007bff; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 8; -fx-background-color: rgba(0,123,255,0.05);");
			courtCaseMappingPane.setVisible(false);
			courtCaseMappingPane.setManaged(false);
			fieldTypeComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
				boolean isChargesTreeViewNew = "CHARGES_TREE_VIEW".equals(newVal);
				boolean isCitationTreeViewNew = "CITATION_TREE_VIEW".equals(newVal);
				if (isChargesTreeViewNew && treeViewExists("CITATION_TREE_VIEW")) {
					showNotificationWarning("Invalid Operation",
							"A Citation Tree View already exists. You cannot add a Charges Tree View.");
					Platform.runLater(() -> fieldTypeComboBox.setValue(oldVal));
					return;
				}
				if (isCitationTreeViewNew && treeViewExists("CHARGES_TREE_VIEW")) {
					showNotificationWarning("Invalid Operation",
							"A Charges Tree View already exists. You cannot add a Citation Tree View.");
					Platform.runLater(() -> fieldTypeComboBox.setValue(oldVal));
					return;
				}
				RowPane parentRow = (getParent() != null && getParent().getParent() instanceof RowPane)
						? (RowPane) getParent().getParent()
						: null;
				boolean isTreeView = "CHARGES_TREE_VIEW".equals(newVal) || "CITATION_TREE_VIEW".equals(newVal);
				courtCaseMappingPane.setVisible(isTreeView);
				courtCaseMappingPane.setManaged(isTreeView);
				if (isTreeView) {
					updateCourtCaseMappingPane(newVal);
				}
				boolean wasTreeView = "CHARGES_TREE_VIEW".equals(oldVal) || "CITATION_TREE_VIEW".equals(oldVal);
				if (isTreeView) {
					if (parentRow != null && parentRow.fieldContainer.getChildren().size() > 1) {
						showNotificationWarning("Invalid Operation",
								"Tree View components must be the only field in a row.");
						Platform.runLater(() -> fieldTypeComboBox.setValue(oldVal));
						return;
					}
					if (parentRow != null) {
						parentRow.addFieldButton.setDisable(true);
					}
				} else if (wasTreeView) {
					if (parentRow != null) {
						parentRow.addFieldButton.setDisable(false);
					}
				}
				if ("CUSTOM_DROPDOWN".equals(newVal)) {
					if (customDropdownComboBox == null) {
						customDropdownComboBox = new ComboBox<>();
						List<String> customFields = getCustomizationFields();
						customDropdownComboBox.getItems().setAll(customFields);
						customDropdownComboBox.setPromptText(
								localization.getLocalizedMessage("ReportWindows.customField", "Select Custom Field"));
						int typeIndex = leftControls.getChildren().indexOf(fieldTypeComboBox);
						leftControls.getChildren().add(typeIndex + 1, customDropdownComboBox);
					}
				} else {
					if (customDropdownComboBox != null) {
						leftControls.getChildren().remove(customDropdownComboBox);
						customDropdownComboBox = null;
					}
				}
				boolean isBlank = "BLANK_SPACE".equals(newVal);
				fieldNameField.setDisable(isBlank || isTreeView);
				populateKeyField.setDisable(isBlank || isTreeView);
				growthSpinner.setDisable(isBlank || isTreeView);
				if (isBlank) {
					int randomNumber = ThreadLocalRandom.current().nextInt(100000, 1000000);
					fieldNameField.setText("blank_" + randomNumber);
					populateKeyField.clear();
				} else if (isTreeView) {
					fieldNameField
							.setText(newVal.toLowerCase() + "_" + ThreadLocalRandom.current().nextInt(100000, 1000000));
					populateKeyField.clear();
				}
				boolean isLookupSection = false;
				if (getParent() != null && getParent().getParent() instanceof RowPane) {
					RowPane rowPane = (RowPane) getParent().getParent();
					if (rowPane.getParent() != null && rowPane.getParent().getParent() instanceof SectionPane) {
						SectionPane sectionPane = (SectionPane) rowPane.getParent().getParent();
						isLookupSection = sectionPane.pullFromLookupCheckbox.isSelected();
					}
				}
				setLookupValueSelectorVisible(isLookupSection);
			});
			removeFieldButton = new Button("Remove Field");
			removeFieldButton.setMinWidth(USE_PREF_SIZE);
			removeFieldButton.setOnAction(_ -> {
				HBox parentHBox = (HBox) getParent();
				if (parentHBox == null)
					return;
				RowPane row = (RowPane) parentHBox.getParent();
				String myType = this.fieldTypeComboBox.getValue();
				boolean iWasATreeView = "CHARGES_TREE_VIEW".equals(myType) || "CITATION_TREE_VIEW".equals(myType);
				parentHBox.getChildren().remove(this);
				if (iWasATreeView) {
					row.addFieldButton.setDisable(false);
				}
				List<Node> remainingChildren = parentHBox.getChildren();
				if (remainingChildren.size() == 1) {
					Node lastNode = remainingChildren.get(0);
					if (lastNode instanceof FieldPane) {
						FieldPane lastField = (FieldPane) lastNode;
						if (!lastField.fieldTypeComboBox.getItems().contains("CHARGES_TREE_VIEW")) {
							lastField.fieldTypeComboBox.getItems().add(1, "CHARGES_TREE_VIEW");
						}
						if (!lastField.fieldTypeComboBox.getItems().contains("CITATION_TREE_VIEW")) {
							lastField.fieldTypeComboBox.getItems().add(2, "CITATION_TREE_VIEW");
						}
					}
				}
				row.updateFieldWidths();
			});
			removeFieldButton.getStyleClass().add("removeButton");
			moveLeftButton = new Button("←");
			moveRightButton = new Button("→");
			moveLeftButton.setMinWidth(USE_PREF_SIZE);
			moveRightButton.setMinWidth(USE_PREF_SIZE);
			moveLeftButton.getStyleClass().add("moveButton");
			moveRightButton.getStyleClass().add("moveButton");
			moveLeftButton.setFocusTraversable(false);
			moveRightButton.setFocusTraversable(false);
			removeFieldButton.setFocusTraversable(false);
			growthSpinner.setFocusTraversable(false);
			fieldTypeComboBox.setFocusTraversable(false);
			if (customDropdownComboBox != null) {
				customDropdownComboBox.setFocusTraversable(false);
			}
			HBox buttonBox = new HBox(5, moveLeftButton, moveRightButton, removeFieldButton);
			leftControls.getChildren().addAll(
					new Label(localization.getLocalizedMessage("Login_Window.NamePromptText", "Name") + ": "),
					fieldNameField, new Label("Populate Key:"), populateKeyField, new Label("Growth:"), growthSpinner,
					new Label(localization.getLocalizedMessage("ReportWindows.TypeLabel", "Type:")), fieldTypeComboBox,
					lookupValueLabel, lookupValueComboBox, buttonBox);
			moveLeftButton.setOnAction(_ -> moveField(-1));
			moveRightButton.setOnAction(_ -> moveField(1));
			getChildren().addAll(leftControls, courtCaseMappingPane);
			HBox.setHgrow(courtCaseMappingPane, Priority.ALWAYS);
		}

		private VBox getSectionContainer() {
			Node parent = this.getParent(); // HBox fieldContainer
			if (parent == null)
				return null;
			parent = parent.getParent(); // RowPane
			if (parent == null)
				return null;
			parent = parent.getParent(); // VBox rowContainer
			if (parent == null)
				return null;
			parent = parent.getParent(); // SectionPane
			if (parent == null)
				return null;
			return (VBox) parent.getParent(); // VBox sectionContainer
		}

		private boolean treeViewExists(String treeViewType) {
			VBox container = getSectionContainer();
			if (container == null)
				return false;
			for (Node sectionNode : container.getChildren()) {
				if (sectionNode instanceof SectionPane) {
					for (Node rowNode : ((SectionPane) sectionNode).rowContainer.getChildren()) {
						if (rowNode instanceof RowPane) {
							for (Node fieldNode : ((RowPane) rowNode).fieldContainer.getChildren()) {
								if (fieldNode instanceof FieldPane) {
									FieldPane otherField = (FieldPane) fieldNode;
									if (otherField != this
											&& treeViewType.equals(otherField.fieldTypeComboBox.getValue())) {
										return true;
									}
								}
							}
						}
					}
				}
			}
			return false;
		}

		private FieldConfig buildFieldConfig() {
			String name = fieldNameField.getText();
			String populateKey = populateKeyField.getText().isEmpty() ? null : populateKeyField.getText();
			HBox parentContainer = (HBox) getParent();
			List<FieldPane> siblings = new ArrayList<>();
			if (parentContainer != null) {
				for (Node node : parentContainer.getChildren()) {
					if (node instanceof FieldPane) {
						siblings.add((FieldPane) node);
					}
				}
			}
			int width;
			if (siblings.isEmpty()) {
				width = 12;
			} else {
				List<Integer> growthFactors = new ArrayList<>();
				int totalGrowth = 0;
				for (FieldPane p : siblings) {
					int growth = p.growthSpinner.getValue();
					growthFactors.add(growth);
					totalGrowth += growth;
				}
				if (totalGrowth == 0) {
					width = 12 / siblings.size(); // Fallback for zero growth
				} else {
					List<Double> doubleWidths = new ArrayList<>();
					for (int growth : growthFactors) {
						doubleWidths.add((double) growth / totalGrowth * 12.0);
					}
					List<Integer> intWidths = new ArrayList<>();
					double fractionalSum = 0;
					for (double d : doubleWidths) {
						intWidths.add((int) Math.floor(d));
						fractionalSum += d - Math.floor(d);
					}
					int remainder = (int) Math.round(fractionalSum);
					List<Integer> sortedIndices = new ArrayList<>();
					for (int i = 0; i < siblings.size(); i++) {
						sortedIndices.add(i);
					}
					sortedIndices.sort((i, j) -> {
						double fracI = doubleWidths.get(i) - Math.floor(doubleWidths.get(i));
						double fracJ = doubleWidths.get(j) - Math.floor(doubleWidths.get(j));
						return Double.compare(fracJ, fracI);
					});
					for (int i = 0; i < remainder; i++) {
						int indexToIncrement = sortedIndices.get(i);
						intWidths.set(indexToIncrement, intWidths.get(indexToIncrement) + 1);
					}
					int myIndex = siblings.indexOf(this);
					width = (myIndex != -1) ? intWidths.get(myIndex) : 12;
				}
			}
			FieldType type = FieldType.valueOf(fieldTypeComboBox.getValue());
			if (name == null) {
				name = "";
			}
			FieldConfig config = new FieldConfig(name, width, type);
			if ("CUSTOM_DROPDOWN".equals(fieldTypeComboBox.getValue()) && customDropdownComboBox != null) {
				String customField = customDropdownComboBox.getValue();
				if (customField != null && !customField.trim().isEmpty()) {
					config.setDropdownType(customField);
				}
			}
			if (courtCaseMappingPane.isVisible()) {
				Map<String, String> mappings = new HashMap<>();
				for (Map.Entry<CourtCaseFields, ComboBox<String>> entry : courtCaseMappingBoxes.entrySet()) {
					String selectedField = entry.getValue().getValue();
					if (selectedField != null && !"None".equals(selectedField)) {
						mappings.put(entry.getKey().name(), selectedField);
					}
				}
				config.setCourtCaseMapping(mappings);
			}
			config.setNodeType(type.getValue());
			if (populateKey != null && populateKey.length() > 0) {
				config.setPopulateKey(populateKey);
			}
			if (lookupValueComboBox.isVisible()) {
				config.setLookupValue(lookupValueComboBox.getValue());
			}
			return config;
		}

		private void updateCourtCaseMappingPane(String treeViewType) {
			courtCaseMappingPane.getChildren().clear();
			courtCaseMappingBoxes.clear();
			List<String> fieldNames = getAllCompatibleFieldNames();
			Label title = new Label("Court Case Field Mappings");
			title.setStyle("-fx-font-weight: bold; -fx-underline: true;");
			courtCaseMappingPane.getChildren().add(title);
			for (CourtCaseFields field : CourtCaseFields.values()) {
				if (field == CourtCaseFields.PLATE_NUMBER && !"CITATION_TREE_VIEW".equals(treeViewType)) {
					continue;
				}
				HBox row = new HBox(10);
				row.setAlignment(Pos.CENTER_LEFT);
				String labelText = field.name().replace('_', ' ').toLowerCase();
				labelText = labelText.substring(0, 1).toUpperCase() + labelText.substring(1);
				boolean isRequired = (field == CourtCaseFields.SUSPECT_NAME) ||
						(field == CourtCaseFields.PLATE_NUMBER && "CITATION_TREE_VIEW".equals(treeViewType));
				Label fieldLabel = new Label(labelText + (isRequired ? " *" : ""));
				fieldLabel.setPrefWidth(120);
				if (isRequired) {
					fieldLabel.setStyle("-fx-font-weight: bold;");
				}
				ComboBox<String> fieldSelector = new ComboBox<>();
				fieldSelector.getItems().add("None");
				fieldSelector.getItems().addAll(fieldNames);
				fieldSelector.getSelectionModel().selectFirst();
				HBox.setHgrow(fieldSelector, Priority.ALWAYS);
				row.getChildren().addAll(fieldLabel, fieldSelector);
				courtCaseMappingPane.getChildren().add(row);
				courtCaseMappingBoxes.put(field, fieldSelector);
			}
		}

		private List<String> getAllCompatibleFieldNames() {
			List<String> names = new ArrayList<>();
			for (Node sectionNode : sectionContainer.getChildren()) {
				if (sectionNode instanceof SectionPane) {
					for (Node rowNode : ((SectionPane) sectionNode).rowContainer.getChildren()) {
						if (rowNode instanceof RowPane) {
							for (Node fieldNode : ((RowPane) rowNode).fieldContainer.getChildren()) {
								if (fieldNode instanceof FieldPane && fieldNode != this) {
									String fieldName = ((FieldPane) fieldNode).fieldNameField.getText();
									if (fieldName != null && !fieldName.trim().isEmpty()) {
										names.add(fieldName.trim());
									}
								}
							}
						}
					}
				}
			}
			return names;
		}

		public boolean validateCourtCaseMappings() {
			String fieldType = fieldTypeComboBox.getValue();
			if ("CHARGES_TREE_VIEW".equals(fieldType)) {
				ComboBox<String> nameBox = courtCaseMappingBoxes.get(CourtCaseFields.SUSPECT_NAME);
				if (nameBox == null || nameBox.getValue() == null || "None".equals(nameBox.getValue())) {
					showNotificationWarning("Mapping Incomplete",
							"You must map the 'Suspect Name' field for a Charges report.");
					return false;
				}
			} else if ("CITATION_TREE_VIEW".equals(fieldType)) {
				ComboBox<String> nameBox = courtCaseMappingBoxes.get(CourtCaseFields.SUSPECT_NAME);
				ComboBox<String> plateBox = courtCaseMappingBoxes.get(CourtCaseFields.PLATE_NUMBER);
				if (nameBox == null || nameBox.getValue() == null || "None".equals(nameBox.getValue())) {
					showNotificationWarning("Mapping Incomplete",
							"You must map the 'Suspect Name' field for a Citation report.");
					return false;
				}
				if (plateBox == null || plateBox.getValue() == null || "None".equals(plateBox.getValue())) {
					showNotificationWarning("Mapping Incomplete",
							"You must map the 'Plate Number' field for a Citation report.");
					return false;
				}
			}
			return true;
		}

		public void updateLookupOptions(String lookupType) {
			String selectedValue = lookupValueComboBox.getValue();
			lookupValueComboBox.getItems().clear();
			lookupValueComboBox.getItems().add("None");
			switch (lookupType.toLowerCase()) {
				case "ped":
					for (PedLayoutTypes type : PedLayoutTypes.values()) {
						lookupValueComboBox.getItems().add(type.name());
					}
					break;
				case "vehicle":
					for (VehicleLayoutTypes type : VehicleLayoutTypes.values()) {
						lookupValueComboBox.getItems().add(type.name());
					}
					break;
				case "both":
					for (PedLayoutTypes type : PedLayoutTypes.values()) {
						lookupValueComboBox.getItems().add(type.name());
					}
					for (VehicleLayoutTypes type : VehicleLayoutTypes.values()) {
						lookupValueComboBox.getItems().add(type.name());
					}
					break;
			}
			if (lookupValueComboBox.getItems().contains(selectedValue)) {
				lookupValueComboBox.setValue(selectedValue);
			} else {
				lookupValueComboBox.getSelectionModel().selectFirst();
			}
		}

		public void setLookupValueSelectorVisible(boolean visible) {
			String currentType = fieldTypeComboBox.getValue();
			boolean shouldBeVisible = visible && !"BLANK_SPACE".equals(currentType)
					&& !"NUMBER_FIELD".equals(currentType);
			lookupValueComboBox.setVisible(shouldBeVisible);
			lookupValueComboBox.setManaged(shouldBeVisible);
			lookupValueLabel.setVisible(shouldBeVisible);
			lookupValueLabel.setManaged(shouldBeVisible);
		}

		private void moveField(int direction) {
			HBox parentHBox = (HBox) getParent();
			if (parentHBox == null) {
				return;
			}
			int currentIndex = parentHBox.getChildren().indexOf(this);
			int newIndex = currentIndex + direction;
			if (newIndex >= 0 && newIndex < parentHBox.getChildren().size()) {
				parentHBox.getChildren().remove(currentIndex);
				parentHBox.getChildren().add(newIndex, this);
			}
		}
	}

	private class RowPane extends VBox {
		private HBox fieldContainer;
		private Button addFieldButton;
		private Button removeRowButton;

		RowPane() {
			setSpacing(8);
			setStyle(
					"-fx-background-color: #F5F5F5; " +
							"-fx-border-color: #CFD8DC; " +
							"-fx-border-width: 1.5; " +
							"-fx-border-style: dashed; " +
							"-fx-border-radius: 6; " +
							"-fx-background-radius: 6; " +
							"-fx-padding: 10;");
			fieldContainer = new HBox(10);
			addFieldButton = new Button(localization.getLocalizedMessage("ReportWindows.AddButton", "Add") + " "
					+ localization.getLocalizedMessage("ReportWindows.FieldButton", "Field"));
			addFieldButton.getStyleClass().add("addButton");
			addFieldButton.setOnAction(_ -> addField());
			removeRowButton = new Button(localization.getLocalizedMessage("ReportWindows.RemoveButton", "Remove") + " "
					+ localization.getLocalizedMessage("ReportWindows.RowButton", "Row"));
			removeRowButton.getStyleClass().add("removeButton");
			removeRowButton.setOnAction(_ -> {
				VBox parent = (VBox) getParent();
				parent.getChildren().remove(this);
			});
			Button moveUpButton = new Button("↑");
			Button moveDownButton = new Button("↓");
			moveUpButton.getStyleClass().add("moveButton");
			moveDownButton.getStyleClass().add("moveButton");
			moveUpButton.setFocusTraversable(false);
			moveDownButton.setFocusTraversable(false);
			removeRowButton.setFocusTraversable(false);
			addFieldButton.setFocusTraversable(false);
			moveUpButton.setOnAction(_ -> {
				VBox parent = (VBox) getParent();
				int index = parent.getChildren().indexOf(this);
				if (index > 0) {
					parent.getChildren().remove(index);
					parent.getChildren().add(index - 1, this);
				}
			});
			moveDownButton.setOnAction(_ -> {
				VBox parent = (VBox) getParent();
				int index = parent.getChildren().indexOf(this);
				if (index < parent.getChildren().size() - 1) {
					parent.getChildren().remove(index);
					parent.getChildren().add(index + 1, this);
				}
			});
			HBox controlBox = new HBox(10, addFieldButton, moveUpButton, moveDownButton, removeRowButton);
			getChildren().addAll(fieldContainer, controlBox);
		}

		private void addField() {
			for (Node node : fieldContainer.getChildren()) {
				if (node instanceof FieldPane) {
					String fieldType = ((FieldPane) node).fieldTypeComboBox.getValue();
					if ("CHARGES_TREE_VIEW".equals(fieldType) || "CITATION_TREE_VIEW".equals(fieldType)) {
						showNotificationWarning("Invalid Operation",
								"Tree View components must be the only field in a row.");
						return;
					}
				}
			}
			FieldPane fieldPane = new FieldPane();
			fieldContainer.getChildren().add(fieldPane);
			if (fieldContainer.getChildren().size() > 1) {
				for (Node node : fieldContainer.getChildren()) {
					if (node instanceof FieldPane) {
						((FieldPane) node).fieldTypeComboBox.getItems().remove("CHARGES_TREE_VIEW");
						((FieldPane) node).fieldTypeComboBox.getItems().remove("CITATION_TREE_VIEW");
					}
				}
			}
			SectionPane parentSection = (SectionPane) getParent().getParent();
			String currentLookupType = parentSection.lookupTypeComboBox.getValue();
			fieldPane.updateLookupOptions(currentLookupType);
			updateFieldWidths();
		}

		private void updateFieldWidths() {
			List<FieldPane> fields = new ArrayList<>();
			for (Node node : fieldContainer.getChildren()) {
				if (node instanceof FieldPane) {
					fields.add((FieldPane) node);
				}
			}
			int totalGrowth = 0;
			for (FieldPane field : fields) {
				totalGrowth += field.growthSpinner.getValue();
			}
			if (totalGrowth > 0) {
				for (FieldPane field : fields) {
					field.prefWidthProperty().unbind();
					double proportion = (double) field.growthSpinner.getValue() / totalGrowth;
					field.prefWidthProperty().bind(fieldContainer.widthProperty().multiply(proportion));
				}
			}
		}

		private RowConfig buildRowConfig() {
			List<FieldConfig> fieldConfigs = new ArrayList<>();
			for (Node node : fieldContainer.getChildren()) {
				if (node instanceof FieldPane) {
					fieldConfigs.add(((FieldPane) node).buildFieldConfig());
				}
			}
			return new RowConfig(fieldConfigs.toArray(new FieldConfig[0]));
		}
	}

	private class TransferPane extends VBox {
		private TextField transferNameField;
		private VBox elementContainer;
		private Button addElementButton;
		private Button removeTransferButton;

		TransferPane() {
			setSpacing(10);
			setStyle(
					"-fx-background-color: #E0F2F1; " +
							"-fx-background-radius: 8; " +
							"-fx-border-radius: 8; " +
							"-fx-border-width: 2; " +
							"-fx-border-color: #80CBC4; " +
							"-fx-padding: 12;");
			HBox header = new HBox(10);
			header.setAlignment(Pos.CENTER_LEFT);
			header.setPadding(new Insets(0, 0, 10, 0));
			header.setStyle("-fx-border-color: transparent transparent #B2DFDB transparent; -fx-border-width: 1.5;");
			transferNameField = new TextField();
			transferNameField.setPromptText(localization.getLocalizedMessage("ReportWindows.TransferButton", "Transfer")
					+ " " + localization.getLocalizedMessage("Login_Window.NamePromptText", "Name"));
			removeTransferButton = new Button(localization.getLocalizedMessage("ReportWindows.RemoveButton", "Remove")
					+ " " + localization.getLocalizedMessage("ReportWindows.TransferButton", "Transfer"));
			removeTransferButton.getStyleClass().add("removeButton");
			removeTransferButton.setOnAction(_ -> transferContainer.getChildren().remove(this));
			Pane spacer = new Pane();
			HBox.setHgrow(spacer, Priority.ALWAYS);
			header.getChildren().addAll(new Label("Transfer:"), transferNameField, spacer, removeTransferButton);
			elementContainer = new VBox(8);
			elementContainer.setStyle("-fx-background-color: transparent; -fx-padding: 10 2 2 2;");
			addElementButton = new Button(localization.getLocalizedMessage("ReportWindows.AddButton", "Add") + " "
					+ localization.getLocalizedMessage("ReportWindows.ElementButton", "Element"));
			addElementButton.getStyleClass().add("addButton");
			addElementButton.setOnAction(_ -> addElement());
			addElementButton.setFocusTraversable(false);
			removeTransferButton.setFocusTraversable(false);
			getChildren().addAll(header, elementContainer, addElementButton);
		}

		private void addElement() {
			ElementPane elementPane = new ElementPane();
			elementContainer.getChildren().add(elementPane);
		}
	}

	private class ElementPane extends HBox {
		private TextField elementNameField;
		private ComboBox<String> reportComboBox;
		private Button removeElementButton;

		ElementPane() {
			setSpacing(5);
			setStyle(
					"-fx-background-color: white; " +
							"-fx-border-color: #B2DFDB; " +
							"-fx-border-width: 1; " +
							"-fx-border-radius: 4; " +
							"-fx-background-radius: 4; " +
							"-fx-padding: 10;");
			elementNameField = new TextField();
			elementNameField.setPromptText(localization.getLocalizedMessage("ReportWindows.ElementButton", "Element")
					+ " " + localization.getLocalizedMessage("Login_Window.NamePromptText", "Name"));
			reportComboBox = new ComboBox<>();
			String dataFolderPath = getCustomDataLogsFolderPath();
			ArrayList<String> dbArray = new ArrayList<>();
			File dataFolder = new File(dataFolderPath);
			if (dataFolder.exists() && dataFolder.isDirectory()) {
				File[] files = dataFolder.listFiles((dir, filen) -> filen.endsWith(".db"));
				if (files != null && files.length != 0) {
					logInfo("LayoutBuilder; Found " + files.length + " Databases For Transfer List");
					for (File dbFile : files) {
						String fileNameWithoutExt = dbFile.getName().replaceFirst("[.][^.]+$", "");
						String dbFilePath = dbFile.getAbsolutePath();
						if (isValidDatabase(dbFilePath, dbFile.getName())) {
							logInfo("LayoutBuilder; [" + fileNameWithoutExt + "] Added To Transfer List");
							dbArray.add(fileNameWithoutExt);
						} else {
							logWarn("LayoutBuilder; Invalid database file: " + dbFilePath);
						}
					}
				}
			}
			reportComboBox.getItems().addAll(dbArray);
			removeElementButton = new Button(localization.getLocalizedMessage("ReportWindows.RemoveButton", "Remove")
					+ " " + localization.getLocalizedMessage("ReportWindows.ElementButton", "Element"));
			removeElementButton.getStyleClass().add("removeButton");
			removeElementButton.setOnAction(_ -> ((VBox) getParent()).getChildren().remove(this));
			Button moveUpButton = new Button("↑");
			Button moveDownButton = new Button("↓");
			moveUpButton.getStyleClass().add("moveButton");
			moveDownButton.getStyleClass().add("moveButton");
			moveUpButton.setFocusTraversable(false);
			moveDownButton.setFocusTraversable(false);
			removeElementButton.setFocusTraversable(false);
			reportComboBox.setFocusTraversable(false);
			moveUpButton.setOnAction(_ -> {
				VBox parent = (VBox) getParent();
				int index = parent.getChildren().indexOf(this);
				if (index > 0) {
					parent.getChildren().remove(index);
					parent.getChildren().add(index - 1, this);
				}
			});
			moveDownButton.setOnAction(_ -> {
				VBox parent = (VBox) getParent();
				int index = parent.getChildren().indexOf(this);
				if (index < parent.getChildren().size() - 1) {
					parent.getChildren().remove(index);
					parent.getChildren().add(index + 1, this);
				}
			});
			getChildren().addAll(
					new Label(localization.getLocalizedMessage("ReportWindows.ElementButton", "Element") + ":"),
					elementNameField,
					new Label(localization.getLocalizedMessage("ReportWindows.ReportLabel", "Report") + ":"),
					reportComboBox, removeElementButton, moveUpButton,
					moveDownButton);
		}
	}

	// #endregion
	// #region Init/Close
	public void initialize() {
		addSectionButton.setOnAction(_ -> addSection());
		addTransferButton.setOnAction(_ -> addTransfer());
		buildLayoutButton.setOnAction(this::handleBuildLayoutClick);
		importExportButton.setOnAction(this::handleImportExportClick);
		customDropdownButton.setOnAction(_ -> {
			openCustomDropdownWindow();
		});
		addLocale();
	}

	private void addLocale() {
		customDropdownButton
				.setText(localization.getLocalizedMessage("LayoutBuilder.CustomDropdownButton", "Custom Dropdowns"));
		importExportButton
				.setText(localization.getLocalizedMessage("LayoutBuilder.ImportExportButton",
						"Import/Export and Templates"));
		heading.setText(localization.getLocalizedMessage("LayoutBuilder.Heading", "Custom Layout Designer"));
		buildLayoutButton
				.setText(localization.getLocalizedMessage("LayoutBuilder.BuildLayoutButton", "View Report Layout"));
	}

	@Override
	public void shutdown() {
		logInfo("Shutting down Layout Builder and all resources...");
		if (addDropdownWindow != null) {
			addDropdownWindow.closeWindow();
		}
		if (layoutBuilderController == this) {
			layoutBuilderController = null;
		}
	}

	// #endregion
	public static String extractNumberField(String jsonString) {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root;
		try {
			root = mapper.readTree(jsonString);
		} catch (JsonProcessingException e) {
			logError("Error parsing JSON: " + jsonString, e);
			return null;
		}
		if (root.isArray()) {
			for (JsonNode sectionNode : root) {
				Iterator<Map.Entry<String, JsonNode>> fields = sectionNode.fields();
				while (fields.hasNext()) {
					Map.Entry<String, JsonNode> entry = fields.next();
					JsonNode valueNode = entry.getValue();
					if (valueNode.isArray()) {
						for (JsonNode elementNode : valueNode) {
							JsonNode fieldConfigs = elementNode.get("fieldConfigs");
							if (fieldConfigs != null && fieldConfigs.isArray()) {
								for (JsonNode configNode : fieldConfigs) {
									JsonNode fieldTypeNode = configNode.get("fieldType");
									if (fieldTypeNode != null && fieldTypeNode.isTextual()
											&& "NUMBER_FIELD".equals(fieldTypeNode.asText())) {
										JsonNode fieldNameNode = configNode.get("fieldName");
										if (fieldNameNode != null && fieldNameNode.isTextual()) {
											return fieldNameNode.asText();
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	public static Map<String, String> extractFieldNames(String jsonString) throws Exception {
		Map<String, String> reportSchema = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(jsonString);
		if (root.isArray()) {
			for (JsonNode sectionNode : root) {
				Iterator<Map.Entry<String, JsonNode>> fields = sectionNode.fields();
				while (fields.hasNext()) {
					Map.Entry<String, JsonNode> entry = fields.next();
					JsonNode valueNode = entry.getValue();
					if (valueNode.isArray()) {
						for (JsonNode elementNode : valueNode) {
							JsonNode fieldConfigs = elementNode.get("fieldConfigs");
							if (fieldConfigs != null && fieldConfigs.isArray()) {
								for (JsonNode configNode : fieldConfigs) {
									JsonNode fieldNameNode = configNode.get("fieldName");
									if (fieldNameNode != null && fieldNameNode.isTextual()) {
										String fieldName = fieldNameNode.asText();
										reportSchema.put(fieldName, "TEXT");
									}
								}
							}
						}
					}
				}
			}
		}
		return reportSchema;
	}

	public static Map<String, Map<String, List<String>>> parseAndPopulateMap(String jsonString) {
		try {
			Map<String, Map<String, List<String>>> map = new HashMap<>();
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(jsonString);
			for (JsonNode section : rootNode) {
				for (JsonNode rowConfig : section.get("rowConfigs")) {
					for (JsonNode fieldConfig : rowConfig.get("fieldConfigs")) {
						String fieldName = fieldConfig.get("fieldName").asText();
						String nodeType = fieldConfig.get("nodeType").asText();
						String fieldType = fieldConfig.get("fieldType").asText();
						String dropdownType = fieldConfig.get("dropdownType").asText();
						String populateKey = fieldConfig.has("populateKey") && !fieldConfig.get("populateKey").isNull()
								? fieldConfig.get("populateKey").asText()
								: "null";
						map.computeIfAbsent("dropdownType", k -> new HashMap<>())
								.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(dropdownType);
						map.computeIfAbsent("fieldNames", k -> new HashMap<>()).computeIfAbsent(fieldName,
								k -> new ArrayList<>());
						map.computeIfAbsent("nodeType", k -> new HashMap<>())
								.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(nodeType);
						map.computeIfAbsent("selectedType", k -> new HashMap<>())
								.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(fieldType);
						map.computeIfAbsent("keyMap", k -> new HashMap<>())
								.computeIfAbsent(populateKey, k -> new ArrayList<>()).add(fieldName);
					}
				}
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<SectionConfig> loadLayoutFromJSON(String data) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			List<SectionConfig> layout = objectMapper.readValue(data, new TypeReference<List<SectionConfig>>() {
			});
			return layout;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	public static TransferConfig loadTransferConfigFromJSON(String data) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			TransferConfig transferConfig = objectMapper.readValue(data, TransferConfig.class);
			return transferConfig;
		} catch (IOException e) {
			logError("Error loading transferConfig from data [" + data + "]:", e);
		}
		return null;
	}

	public String saveLayoutToJSON() {
		List<SectionConfig> layout = buildLayout();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String layoutJson = objectMapper.writeValueAsString(layout);
			return layoutJson;
		} catch (JsonProcessingException e) {
			logError("Error saving layout to json: " + layout, e);
		}
		return null;
	}

	public String saveTransferConfigToJSON() {
		TransferConfig layout = buildTransferConfig();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String layoutJson = objectMapper.writeValueAsString(layout);
			return layoutJson;
		} catch (JsonProcessingException e) {
			logError("Error saving transferConfig to json: " + layout, e);
		}
		return null;
	}

	private void handleImportExportClick(ActionEvent actionEvent) {
		VBox layout = new VBox(10);
		layout.setPadding(new Insets(15));
		layout.setStyle("-fx-background-color: #F4F4F4; -fx-border-radius: 10; -fx-background-radius: 10;");
		Label titleLabel = new Label(localization.getLocalizedMessage("ImportExport.title", "JSON Import/Export"));
		titleLabel.setStyle("-fx-font-size: 18px; -fx-font-family: 'Inter 28pt Bold'; -fx-text-fill: #2c3e50;");
		Label descriptionLabel = new Label(localization.getLocalizedMessage("ImportExport.description",
				"Import or export layout configuration from/to JSON format"));
		descriptionLabel.setWrapText(true);
		descriptionLabel.setStyle("-fx-text-fill: #7f8c8d;");
		Label templateLabel = new Label("Select a template (optional):");
		templateLabel.setStyle("-fx-font-family: 'Inter 28pt Medium';");
		ComboBox<String> templateComboBox = new ComboBox<>();
		templateComboBox.setPromptText("Select a template...");
		templateComboBox.getItems().addAll("Callout", "Traffic Stop", "Impound", "Search", "Accident", "Death",
				"Patrol", "Incident", "Use of Force", "Arrest", "Citation");
		templateComboBox.setPrefWidth(Double.MAX_VALUE);
		TextArea jsonTextArea = new TextArea();
		jsonTextArea.setWrapText(false);
		jsonTextArea.setPromptText("Paste JSON here or select a template to load...");
		jsonTextArea.setStyle("-fx-font-family: Consolas; -fx-font-size: 12px;");
		templateComboBox.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
			if (newValue != null) {
				String templateJson = getTemplateJsonFor(newValue);
				jsonTextArea.setText(templateJson);
				logDebug("Loaded " + newValue + " template");
			}
		});
		ScrollPane jsonScrollPane = new ScrollPane(jsonTextArea);
		VBox.setVgrow(jsonScrollPane, Priority.ALWAYS);
		jsonScrollPane.setFitToWidth(true);
		jsonScrollPane.setFitToHeight(true);
		jsonScrollPane.setStyle("-fx-background-color: transparent;");
		HBox buttonContainer = new HBox(10);
		buttonContainer.setAlignment(Pos.CENTER_LEFT);
		HBox fileButtons = new HBox(10);
		fileButtons.setAlignment(Pos.CENTER_LEFT);
		Button loadFileButton = new Button(
				localization.getLocalizedMessage("ImportExport.importFromFile", "Import from File"));
		loadFileButton.setOnAction(e -> loadJsonFromFile(jsonTextArea));
		Button importFromDbButton = new Button(
				localization.getLocalizedMessage("ImportExport.importFromDbButton", "Import from DB"));
		importFromDbButton.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(new File(getCustomDataLogsFolderPath()));
			fileChooser.setTitle("Open Report DB File");
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Database Files", "*.db"));
			File selectedFile = fileChooser.showOpenDialog(mainRT);
			if (selectedFile != null) {
				String jsonContent = getLayoutJsonFromDb(selectedFile);
				if (jsonContent != null && !jsonContent.isEmpty()) {
					jsonTextArea.setText(jsonContent);
					showNotificationInfo("Import Success", "Layout JSON loaded from DB.");
				} else {
					showNotificationError("Import Failed", "Could not retrieve layout from the selected DB file.");
				}
			}
		});
		Button saveFileButton = new Button(
				localization.getLocalizedMessage("ImportExport.exportToFile", "Export Current Layout to File"));
		saveFileButton.setOnAction(e -> saveJsonToFile());
		fileButtons.getChildren().addAll(loadFileButton, importFromDbButton, saveFileButton);
		HBox importButtonBox = new HBox();
		importButtonBox.setAlignment(Pos.CENTER_RIGHT);
		Button importButton = new Button(localization.getLocalizedMessage("ImportExport.applyLayout", "Apply Layout"));
		importButton.setOnAction(e -> handleImport(jsonTextArea));
		Pane spacer = new Pane();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		buttonContainer.getChildren().addAll(fileButtons, spacer, importButtonBox);
		importButtonBox.getChildren().add(importButton);
		layout.getChildren().addAll(titleLabel, descriptionLabel, templateLabel, templateComboBox, jsonScrollPane,
				buttonContainer);
		BorderPane window = new BorderPane();
		window.setCenter(layout);
		window.setPrefSize(700, 450);
		window.getStylesheets()
				.add(Launcher.class.getResource("/com/Guess/ReportsPlus/css/newReport/newReport.css").toExternalForm());
		addDropdownWindow = createCustomWindow(mainDesktopControllerObj.getDesktopContainer(), window,
				localization.getLocalizedMessage("ImportExport.windowTitle", "Layout Import/Export"), true, 1, true,
				true, mainDesktopControllerObj.getTaskBarApps(),
				new Image(Objects.requireNonNull(
						Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/report.png"))));
	}

	private String getTemplateJsonFor(String newValue) {
		switch (newValue) {
			case "Citation":
				return reportLayoutTemplates.citationReport;
			case "Arrest":
				return reportLayoutTemplates.arrestReport;
			case "Use of Force":
				return reportLayoutTemplates.useOfForceReport;
			case "Callout":
				return reportLayoutTemplates.calloutReport;
			case "Traffic Stop":
				return reportLayoutTemplates.trafficStopReport;
			case "Impound":
				return reportLayoutTemplates.impoundReport;
			case "Search":
				return reportLayoutTemplates.searchReport;
			case "Accident":
				return reportLayoutTemplates.accidentReport;
			case "Death":
				return reportLayoutTemplates.deathReport;
			case "Patrol":
				return reportLayoutTemplates.patrolReport;
			case "Incident":
				return reportLayoutTemplates.incidentReport;
			default:
				return "";
		}
	}

	private void loadJsonFromFile(TextArea jsonTextArea) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(localization.getLocalizedMessage("ImportExport.openFileTitle", "Open JSON File"));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
		File file = fileChooser.showOpenDialog(mainRT);
		if (file != null) {
			try {
				String content = new String(Files.readAllBytes(file.toPath()));
				jsonTextArea.setText(content);
			} catch (IOException e) {
				showNotificationWarning("File Error", "Could not read file: " + e.getMessage());
				logError("Could not read file: ", e);
			}
		} else {
			showNotificationWarning("File Error", "File selection canceled");
			logWarn("File selection canceled");
		}
	}

	private void saveJsonToFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(localization.getLocalizedMessage("ImportExport.saveFileTitle", "Save JSON File"));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
		String jsonExportText;
		try {
			FullLayoutConfig config = new FullLayoutConfig();
			config.setLayout(buildLayout());
			config.setTransfer(buildTransferConfig());
			ObjectMapper mapper = new ObjectMapper();
			jsonExportText = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(config);
			showNotificationInfo("Export Successful", "Configuration exported successfully");
		} catch (Exception e) {
			showNotificationWarning("Export Error", "Failed to generate JSON: " + e.getMessage());
			return;
		}
		File file = fileChooser.showSaveDialog(mainRT);
		if (file != null) {
			try {
				Files.write(file.toPath(), jsonExportText.getBytes());
				showNotificationInfo("Save Successful", "File saved successfully");
			} catch (IOException e) {
				showNotificationWarning("Save Error", "Could not save file: " + e.getMessage());
				logError("Could not save file: ", e);
			}
		} else {
			logWarn("File save canceled");
			showNotificationWarning("Save Error", "File save canceled");
		}
	}

	private void openCustomDropdownWindow() {
		VBox layout = new VBox(10);
		layout.setPadding(new Insets(15));
		layout.setStyle("-fx-background-color: #F4F4F4; -fx-border-radius: 10; -fx-background-radius: 10;");
		ComboBox<String> dropdownSelector = new ComboBox<>();
		dropdownSelector
				.setPromptText(localization.getLocalizedMessage("DropdownCreator.selectPrompt", "Select Dropdown"));
		dropdownSelector.setPrefWidth(250);
		List<String> fields = getCustomizationFields();
		dropdownSelector.getItems().setAll("New Dropdown");
		dropdownSelector.getItems().addAll(fields);
		dropdownSelector.setValue("New Dropdown");
		Label dropdownTitleLabel = new Label(
				localization.getLocalizedMessage("DropdownCreator.titleLabel", "Dropdown Title:"));
		dropdownTitleLabel.setStyle("-fx-font-family: \"Inter 28pt Bold\";");
		TextField dropdownTitleField = new TextField();
		dropdownTitleField.setPrefWidth(250);
		Label dropdownItemsLabel = new Label(
				localization.getLocalizedMessage("DropdownCreator.itemsLabel", "Dropdown Items:"));
		dropdownItemsLabel.setStyle("-fx-font-family: \"Inter 28pt Bold\";");
		VBox dropdownItemsBox = new VBox(5);
		dropdownItemsBox.setStyle(
				"-fx-background-color: #FFFFFF; -fx-border-color: #CCC; -fx-border-width: 1; -fx-border-radius: 7; -fx-background-radius: 7; -fx-padding: 10;");
		Button addItemBtn = new Button(localization.getLocalizedMessage("ReportWindows.AddButton", "Add"));
		Button saveBtn = new Button(localization.getLocalizedMessage("Settings.NotiSaveButton", "Save"));
		Button deleteBtn = new Button(localization.getLocalizedMessage("Settings.DelCalloutButton", "Delete"));
		deleteBtn.setDisable(true);
		Button importBtn = new Button("Import");
		Button exportBtn = new Button("Export");
		HBox selectorBox = new HBox(10, dropdownSelector, importBtn, exportBtn);
		selectorBox.setAlignment(Pos.CENTER_LEFT);
		BiFunction<String, Boolean, HBox> createItemRow = (value, deletable) -> {
			HBox row = new HBox(5);
			row.setAlignment(Pos.CENTER_LEFT);
			TextField field = new TextField(value);
			field.setPromptText("Item Name");
			HBox.setHgrow(field, Priority.ALWAYS);
			Button upBtn = new Button("↑");
			upBtn.setFocusTraversable(false);
			upBtn.setStyle("-fx-background-color: transparent; -fx-font-weight: bold;");
			Button downBtn = new Button("↓");
			downBtn.setFocusTraversable(false);
			downBtn.setStyle("-fx-background-color: transparent; -fx-font-weight: bold;");
			Button itemDelBtn = new Button("×");
			itemDelBtn.setFocusTraversable(false);
			itemDelBtn.setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-text-fill: #ff4444;");
			upBtn.setOnAction(e -> {
				int index = dropdownItemsBox.getChildren().indexOf(row);
				if (index > 0) {
					dropdownItemsBox.getChildren().remove(index);
					dropdownItemsBox.getChildren().add(index - 1, row);
				}
			});
			downBtn.setOnAction(e -> {
				int index = dropdownItemsBox.getChildren().indexOf(row);
				if (index < dropdownItemsBox.getChildren().size() - 1) {
					dropdownItemsBox.getChildren().remove(index);
					dropdownItemsBox.getChildren().add(index + 1, row);
				}
			});
			itemDelBtn.setOnAction(e -> dropdownItemsBox.getChildren().remove(row));
			if (deletable) {
				row.getChildren().addAll(field, upBtn, downBtn, itemDelBtn);
			} else {
				row.getChildren().add(field);
			}
			return row;
		};
		addItemBtn.setOnAction(e -> {
			HBox itemRow = createItemRow.apply("", true);
			dropdownItemsBox.getChildren().add(itemRow);
		});
		dropdownSelector.valueProperty().addListener((obs, oldVal, selectedDropdown) -> {
			if (selectedDropdown == null || selectedDropdown.equals("New Dropdown")) {
				dropdownTitleField.clear();
				dropdownItemsBox.getChildren().clear();
				deleteBtn.setDisable(true);
			} else {
				dropdownTitleField.setText(selectedDropdown);
				dropdownItemsBox.getChildren().clear();
				getValuesForField(selectedDropdown).forEach(item -> {
					if (!item.equals("N/A")) {
						dropdownItemsBox.getChildren().add(createItemRow.apply(item, true));
					}
				});
				deleteBtn.setDisable(false);
			}
		});
		saveBtn.setOnAction(e -> {
			String title = dropdownTitleField.getText().trim();
			String originalName = dropdownSelector.getValue();
			if (title.isEmpty()) {
				showNotificationWarning("Validation Error", "Title cannot be empty");
				return;
			}
			List<String> items = new ArrayList<>();
			for (Node node : dropdownItemsBox.getChildren()) {
				HBox row = (HBox) node;
				TextField field = (TextField) row.getChildren().get(0);
				String text = field.getText().trim();
				if (!text.isEmpty()) {
					items.add(text);
				}
			}
			if (items.isEmpty()) {
				showNotificationWarning("Validation Error", "Must have at least one item");
				return;
			}
			try {
				if (originalName != null && !originalName.equals("New Dropdown")) {
					deleteCustomField(originalName);
				}
				if (addCustomField(title, items)) {
					List<String> updatedFields = getCustomizationFields();
					dropdownSelector.getItems().setAll("New Dropdown");
					dropdownSelector.getItems().addAll(updatedFields);
					dropdownSelector.setValue(title);
				}
			} catch (IOException ex) {
				logError("Dropdown save failed", ex);
			}
		});
		deleteBtn.setOnAction(e -> {
			String dropdownName = dropdownSelector.getValue();
			if (dropdownName == null || dropdownName.equals("New Dropdown")) {
				showNotificationError("Invalid Selection", "No dropdown selected");
				return;
			}
			Label message = new Label("Are you sure you want to delete '" + dropdownName + "'?");
			message.setStyle("-fx-font-family: \"Inter 28pt Bold\"; -fx-font-size: 14;");
			Button yesBtn = new Button("Yes");
			yesBtn.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
			Button noBtn = new Button("No");
			noBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
			HBox buttonBox = new HBox(10, yesBtn, noBtn);
			buttonBox.setAlignment(Pos.CENTER_RIGHT);
			VBox dialogContent = new VBox(15, message, buttonBox);
			dialogContent.setPadding(new Insets(20));
			dialogContent.setStyle("-fx-background-color: #F4F4F4;");
			BorderPane layoutPane = new BorderPane();
			layoutPane.setCenter(dialogContent);
			layoutPane.setPrefSize(450, 150);
			CustomWindow confirmDialog = createCustomWindow(mainDesktopControllerObj.getDesktopContainer(), layoutPane,
					"Confirm", true, 1, true, true, mainDesktopControllerObj.getTaskBarApps(),
					new Image(Objects.requireNonNull(
							Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/report.png"))));
			yesBtn.setOnAction(_ -> {
				confirmDialog.closeWindow();
				try {
					if (deleteCustomField(dropdownName)) {
						showNotificationInfo("Success", "Deleted '" + dropdownName + "' successfully");
						List<String> updatedFields = getCustomizationFields();
						dropdownSelector.getItems().setAll("New Dropdown");
						dropdownSelector.getItems().addAll(updatedFields);
						dropdownSelector.setValue("New Dropdown");
						dropdownTitleField.clear();
						dropdownItemsBox.getChildren().clear();
					} else {
						showNotificationError("Error", "Failed to delete '" + dropdownName + "'");
					}
				} catch (IOException ex) {
					logError("Dropdown deletion failed: ", ex);
					showNotificationError("Error", "Failed to delete '" + dropdownName + "'");
				}
			});
			noBtn.setOnAction(_ -> confirmDialog.closeWindow());
		});
		exportBtn.setOnAction(e -> {
			List<String> customFields = getCustomizationFields();
			if (customFields.isEmpty()) {
				showNotificationWarning("Export Error", "No custom dropdowns to export");
				return;
			}
			ListView<String> listView = new ListView<>();
			listView.getItems().addAll(customFields);
			listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			Button exportSelectedBtn = new Button("Export Selected");
			VBox exportLayout = new VBox(10, listView, exportSelectedBtn);
			exportLayout.setPadding(new Insets(15));
			BorderPane exportBorderPane = new BorderPane(exportLayout);
			exportBorderPane.setPrefSize(350, 400);
			CustomWindow exportDialog = createCustomWindow(mainDesktopControllerObj.getDesktopContainer(),
					exportBorderPane, "Export", false, 1, true, true, mainDesktopControllerObj.getTaskBarApps(),
					new Image(Objects.requireNonNull(
							Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/report.png"))));
			exportSelectedBtn.setOnAction(_ -> {
				List<String> selected = listView.getSelectionModel().getSelectedItems();
				if (selected.isEmpty()) {
					showNotificationWarning("Export Error", "No dropdowns selected");
					return;
				}
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Save Export File");
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
				File file = fileChooser.showSaveDialog(mainRT);
				if (file == null) {
					return;
				}
				try {
					ObjectMapper mapper = new ObjectMapper();
					ObjectNode exportData = mapper.createObjectNode();
					for (String field : selected) {
						List<String> items = getValuesForField(field);
						ArrayNode arrayNode = mapper.createArrayNode();
						items.forEach(arrayNode::add);
						exportData.set(field, arrayNode);
					}
					mapper.writerWithDefaultPrettyPrinter().writeValue(file, exportData);
					showNotificationInfo("Export Successful", "Exported " + selected.size() + " dropdown(s)");
				} catch (IOException ex) {
					logError("Export failed: ", ex);
					showNotificationError("Export Error", "Failed to export dropdowns");
				}
				exportDialog.closeWindow();
			});
		});
		importBtn.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Import Dropdowns");
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
			File file = fileChooser.showOpenDialog(null);
			if (file == null) {
				return;
			}
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode rootNode = mapper.readTree(file);
				if (!rootNode.isObject()) {
					showNotificationError("Import Error", "Invalid file format");
					return;
				}
				List<String> existingFields = getCustomizationFields();
				Map<String, List<String>> importCandidates = new LinkedHashMap<>();
				Iterator<Map.Entry<String, JsonNode>> fieldsTwo = rootNode.fields();
				while (fieldsTwo.hasNext()) {
					Map.Entry<String, JsonNode> entry = fieldsTwo.next();
					String fieldName = entry.getKey();
					JsonNode itemsNode = entry.getValue();
					if (DEFAULT_FIELDS.contains(fieldName) || existingFields.contains(fieldName)) {
						continue;
					}
					if (!itemsNode.isArray()) {
						continue;
					}
					Set<String> uniqueItems = new LinkedHashSet<>();
					uniqueItems.add("N/A");
					itemsNode.forEach(itemNode -> {
						String item = itemNode.asText().trim();
						if (!item.isEmpty() && !item.equalsIgnoreCase("N/A")) {
							uniqueItems.add(item);
						}
					});
					if (uniqueItems.size() > 1) {
						importCandidates.put(fieldName, new ArrayList<>(uniqueItems));
					}
				}
				if (importCandidates.isEmpty()) {
					showNotificationInfo("Import", "No new dropdowns to import");
					return;
				}
				ListView<String> listView = new ListView<>();
				listView.getItems().addAll(importCandidates.keySet());
				Button confirmImportBtn = new Button("Import");
				VBox confirmLayout = new VBox(10, new Label("The following dropdowns will be imported:"), listView,
						confirmImportBtn);
				confirmLayout.setPadding(new Insets(15));
				BorderPane layoutPane = new BorderPane(confirmLayout);
				layoutPane.setPrefSize(350, 400);
				CustomWindow confirmDialog = createCustomWindow(mainDesktopControllerObj.getDesktopContainer(),
						layoutPane, "Import", false, 1, true, true, mainDesktopControllerObj.getTaskBarApps(),
						new Image(Objects.requireNonNull(
								Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/report.png"))));
				confirmImportBtn.setOnAction(_ -> {
					int successCount = 0;
					for (Map.Entry<String, List<String>> entry : importCandidates.entrySet()) {
						try {
							if (addCustomField(entry.getKey(), entry.getValue())) {
								successCount++;
							}
						} catch (IOException ex) {
							logError("Import failed for " + entry.getKey() + ": ", ex);
						}
					}
					List<String> updatedFields = getCustomizationFields();
					dropdownSelector.getItems().setAll("New Dropdown");
					dropdownSelector.getItems().addAll(updatedFields);
					showNotificationInfo("Import Successful", "Imported " + successCount + " dropdown(s)");
					confirmDialog.closeWindow();
				});
			} catch (IOException ex) {
				logError("Import failed: ", ex);
				showNotificationError("Import Error", "Failed to import dropdowns");
			}
		});
		HBox buttonBox = new HBox(10, saveBtn, deleteBtn);
		buttonBox.setAlignment(Pos.CENTER_RIGHT);
		ScrollPane dropdownScrollPane = new ScrollPane(dropdownItemsBox);
		VBox.setVgrow(dropdownScrollPane, Priority.ALWAYS);
		dropdownScrollPane.getStylesheets().add(Launcher.class
				.getResource("/com/Guess/ReportsPlus/css/main/transparentScrollPane.css").toExternalForm());
		dropdownScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		dropdownScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		dropdownScrollPane.setFitToWidth(true);
		dropdownScrollPane.setFitToHeight(true);
		dropdownScrollPane.setPannable(true);
		layout.getStylesheets()
				.add(Launcher.class.getResource("/com/Guess/ReportsPlus/css/newReport/newReport.css").toExternalForm());
		layout.getChildren().addAll(selectorBox, dropdownTitleLabel, dropdownTitleField, dropdownItemsLabel,
				dropdownScrollPane, addItemBtn, buttonBox);
		BorderPane window = new BorderPane();
		window.setCenter(layout);
		window.setPrefSize(500, 550);
		addDropdownWindow = createCustomWindow(mainDesktopControllerObj.getDesktopContainer(), window,
				localization.getLocalizedMessage("DropdownCreator.windowTitle", "Dropdown Manager"), true, 1, true,
				true, mainDesktopControllerObj.getTaskBarApps(),
				new Image(Objects.requireNonNull(
						Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/report.png"))));
	}

	private TransferConfig buildTransferConfig() {
		if (transferContainer.getChildren().isEmpty()) {
			return null;
		}
		TransferPane transferPane = (TransferPane) transferContainer.getChildren().get(0);
		String transferName = transferPane.transferNameField.getText();
		List<RowConfig> rowConfigs = new ArrayList<>();
		for (Node node : transferPane.elementContainer.getChildren()) {
			if (node instanceof ElementPane) {
				ElementPane elementPane = (ElementPane) node;
				String fieldName = elementPane.elementNameField.getText().trim();
				String fieldReport = elementPane.reportComboBox.getValue().trim();
				FieldConfig fieldConfig = new FieldConfig("transfer_" + fieldName + "_" + fieldReport, 12,
						FieldType.TRANSFER_BUTTON);
				fieldConfig.setNodeType("BUTTON");
				rowConfigs.add(new RowConfig(fieldConfig));
			}
		}
		return new TransferConfig(transferName, rowConfigs.toArray(new RowConfig[0]));
	}

	private List<SectionConfig> buildLayout() {
		List<SectionConfig> sections = new ArrayList<>();
		for (Node node : sectionContainer.getChildren()) {
			if (node instanceof SectionPane) {
				sections.add(((SectionPane) node).buildSectionConfig());
			}
		}
		return sections;
	}

	private void addSection() {
		SectionPane sectionPane = new SectionPane();
		sectionContainer.getChildren().add(sectionPane);
	}

	private void addTransfer() {
		if (!transferContainer.getChildren().isEmpty()) {
			return;
		}
		TransferPane transferPane = new TransferPane();
		transferContainer.getChildren().add(transferPane);
	}

	private void handleBuildLayoutClick(ActionEvent event) {
		String reportTitle = reportTitleField.getText().trim();
		if (!validateFields()) {
			logError("LayoutBuilder; " + "Could not validate fields");
			return;
		}
		String dataFolderPath = getCustomDataLogsFolderPath();
		createFolderIfNotExists(dataFolderPath);
		File[] files = new File(dataFolderPath).listFiles((dir, name) -> name.endsWith(".db"));
		if (files != null || files.length != 0) {
			for (File file : files) {
				if (file.getName().equalsIgnoreCase(reportTitle + ".db")) {
					logError("NewReport; Report title already exists: " + reportTitle);
					showNotificationWarning("Report Creation Utility", "Report title already exists: " + reportTitle);
					return;
				}
			}
		}
		String data = saveLayoutToJSON();
		List<SectionConfig> layout = loadLayoutFromJSON(data);
		String transferData = saveTransferConfigToJSON();
		TransferConfig transferConfig = loadTransferConfigFromJSON(transferData);
		Map<String, Map<String, List<String>>> newMap = parseAndPopulateMap(data);
		var numFieldCount = newMap.getOrDefault("selectedType", new HashMap<>()).values().stream().flatMap(List::stream)
				.filter(type -> type.equalsIgnoreCase("NUMBER_FIELD")).count();
		if (numFieldCount != 1) {
			showNotificationWarning("Report Creation Utility",
					"Exactly one NUMBER_FIELD is required, found: " + numFieldCount);
			logError("LayoutBuilder; Exactly one NUMBER_FIELD is required, found: " + numFieldCount);
			newMap.put("selectedType", null);
			return;
		}
		Map<String, Object> reportWindow = createReportWindow(reportTitle, transferConfig,
				layout.toArray(new SectionConfig[0]));
		Map<String, Object> reportMap = (Map<String, Object>) reportWindow.get(reportTitle + " Map");
		MenuButton pullNotesBtn = (MenuButton) reportWindow.get("pullNotesBtn");
		Button submitBtn = (Button) reportWindow.get("submitBtn");
		submitBtn.setText("Create New Report Type: " + reportTitle);
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
		for (String name : reportMap.keySet()) {
			if (name.startsWith("transfer_")) {
				String[] values = name.split("_");
				Button btn = (Button) reportMap.get(name);
				btn.setText(values[1]);
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
								logError("LayoutBuilder; newMap is null");
								return;
							}
							for (String field : newMap.getOrDefault("nodeType", new HashMap<>()).keySet()) {
								logDebug("LayoutBuilder; Processing field: " + field);
								Object fieldValue = reportMap.get(field);
								if (fieldValue == null) {
									logError("LayoutBuilder; Field is null: " + field);
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
			logInfo("layoutBuilder; trying to create DB: " + reportTitle);
			Map<String, String> layoutScheme = new HashMap<>();
			layoutScheme.put("key", "TEXT");
			layoutScheme.put("layoutData", "TEXT");
			layoutScheme.put("transferData", "TEXT");
			Map<String, Object> layoutMap = new HashMap<>();
			layoutMap.put("key", "1");
			layoutMap.put("layoutData", data);
			Map<String, Object> transferMap = new HashMap<>();
			transferMap.put("key", "2");
			transferMap.put("transferData", transferData);
			Map<String, String> reportSchema = new HashMap<>();
			DynamicDB dbManager = null;
			try {
				reportSchema = extractFieldNames(data);
				reportSchema.put("report_status", "TEXT");
				dbManager = new DynamicDB(getCustomDataLogsFolderPath() + reportTitle, "data", extractNumberField(data),
						reportSchema);
				dbManager.initDB();
			} catch (Exception e) {
				logError("Failed to extract field names", e);
			} finally {
				try {
					dbManager.close();
				} catch (SQLException e) {
					logError("Failed to close database connection, null", e);
				}
			}
			DynamicDB DatabaseLayout = new DynamicDB(getCustomDataLogsFolderPath() + reportTitle, "layout", "key",
					layoutScheme);
			if (DatabaseLayout.initDB()) {
				logInfo("LayoutBuilder; Layout Database for: [" + reportTitle + "] Initialized");
				try {
					DatabaseLayout.addOrReplaceRecord(layoutMap);
					DatabaseLayout.addOrReplaceRecord(transferMap);
				} catch (SQLException e) {
					logError("Error adding/replacing record: " + layoutScheme, e);
				} finally {
					try {
						DatabaseLayout.close();
					} catch (SQLException e) {
						logError("Error closing Database: [" + DatabaseLayout.getTableName() + "]", e);
					}
				}
			} else {
				logError("LayoutBuilder; Layout Database not initialized!");
				showNotificationError("Report Creation Utility", "Error initializing layout database!");
			}
			for (String fieldName : reportMap.keySet()) {
				Object field = reportMap.get(fieldName);
				if (field instanceof ComboBox<?> comboBox) {
					if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
						comboBox.getSelectionModel().selectFirst();
					}
				}
			}
			DynamicDB Database = new DynamicDB(getCustomDataLogsFolderPath() + reportTitle, "data",
					extractNumberField(data), reportSchema);
			try {
				if (Database.initDB()) {
					logInfo("LayoutBuilder; Database initialized");
				} else {
					logError("LayoutBuilder; Database not initialized!");
					showNotificationError("Report Creation Utility", "Error initializing database!");
				}
			} finally {
				try {
					Database.close();
				} catch (SQLException e) {
					logError("Error closing Database: [" + Database.getTableName() + "]", e);
				}
			}
			showNotificationInfo("Report Manager", "New Report Type [" + reportTitle + "] Created!");
			CustomWindow window = getWindow(reportTitle);
			if (window != null) {
				window.closeWindow();
			}
		});
	}

	private boolean containsNumbers(String str) {
		return str.matches(".*\\d.*");
	}

	private boolean validateFields() {
		Set<String> usedNames = new HashSet<>();
		String reportTitle = reportTitleField.getText().trim();
		if (reportTitle.isEmpty()) {
			showNotificationWarning("Report Creation Utility", "Report Title Field is Empty");
			logError("LayoutBuilder; Report Title Field is Empty");
			return false;
		}
		if (usedNames.contains(reportTitle)) {
			showNotificationWarning("Report Creation Utility", "Report Title must be unique.");
			logError("LayoutBuilder; Duplicate Report Title: " + reportTitle);
			return false;
		}
		usedNames.add(reportTitle);
		if (!transferContainer.getChildren().isEmpty()) {
			TransferPane transferPane = (TransferPane) transferContainer.getChildren().get(0);
			String transferName = transferPane.transferNameField.getText().trim();
			if (transferName.isEmpty()) {
				logError("Transfer Name cannot be empty.");
				showNotificationWarning("Report Creation Utility", "Transfer Name cannot be empty.");
				return false;
			}
			if (containsNumbers(transferName)) {
				logError("Transfer Name cannot contain numbers.");
				showNotificationWarning("Report Creation Utility", "Transfer Name cannot contain numbers.");
				return false;
			}
			if (usedNames.contains(transferName)) {
				showNotificationWarning("Report Creation Utility", "Transfer Name must be unique.");
				logError("LayoutBuilder; Duplicate Transfer Name: " + transferName);
				return false;
			}
			usedNames.add(transferName);
			for (Node node : transferPane.elementContainer.getChildren()) {
				if (node instanceof ElementPane) {
					ElementPane elementPane = (ElementPane) node;
					String elementName = elementPane.elementNameField.getText().trim();
					if (elementName.isEmpty()) {
						logError("Element Name cannot be empty.");
						showNotificationWarning("Report Creation Utility", "Element Name cannot be empty.");
						return false;
					}
					if (containsNumbers(elementName)) {
						logError("Element Name cannot contain numbers.");
						showNotificationWarning("Report Creation Utility", "Element Name cannot contain numbers.");
						return false;
					}
					if (usedNames.contains(elementName)) {
						showNotificationWarning("Report Creation Utility", "Element Name must be unique.");
						logError("LayoutBuilder; Duplicate Element Name: " + elementName);
						return false;
					}
					usedNames.add(elementName);
					String reportValue = elementPane.reportComboBox.getValue() != null
							? elementPane.reportComboBox.getValue().trim()
							: "";
					if (reportValue.isEmpty()) {
						logError("Report ComboBox cannot be empty.");
						showNotificationWarning("Report Creation Utility", "Report ComboBox cannot be empty.");
						return false;
					}
					if (containsNumbers(reportValue)) {
						logError("Report ComboBox value cannot contain numbers.");
						showNotificationWarning("Report Creation Utility",
								"Report ComboBox value cannot contain numbers.");
						return false;
					}
				}
			}
		}
		for (Node node : sectionContainer.getChildren()) {
			if (node instanceof SectionPane) {
				SectionPane sectionPane = (SectionPane) node;
				String sectionTitle = sectionPane.sectionTitleField.getText().trim();
				if (sectionTitle.isEmpty()) {
					logError("Section Title cannot be empty.");
					showNotificationWarning("Report Creation Utility", "Section Title cannot be empty.");
					return false;
				}
				if (containsNumbers(sectionTitle)) {
					logError("Section Title cannot contain numbers.");
					showNotificationWarning("Report Creation Utility", "Section Title cannot contain numbers.");
					return false;
				}
				if (usedNames.contains(sectionTitle)) {
					showNotificationWarning("Report Creation Utility", "Section Title must be unique.");
					logError("LayoutBuilder; Duplicate Section Title: " + sectionTitle);
					return false;
				}
				usedNames.add(sectionTitle);
				for (Node rowNode : sectionPane.rowContainer.getChildren()) {
					if (rowNode instanceof RowPane) {
						RowPane rowPane = (RowPane) rowNode;
						for (Node fieldNode : rowPane.fieldContainer.getChildren()) {
							if (fieldNode instanceof FieldPane) {
								FieldPane fieldPane = (FieldPane) fieldNode;
								String fieldName = fieldPane.fieldNameField.getText().trim();
								String normalizedFieldName = fieldName.toLowerCase();
								if (fieldName.isEmpty()) {
									logError("Field Name cannot be empty.");
									showNotificationWarning("Report Creation Utility", "Field Name cannot be empty.");
									return false;
								}
								if (!fieldName.matches("^[\\w ]+$")) {
									showNotificationWarning("Invalid Characters", "Use only letters, and underscores");
									return false;
								}
								if (fieldName.equalsIgnoreCase("report_status")) {
									logError("Field Name cannot be 'report_status'.");
									showNotificationWarning("Report Creation Utility",
											"Field Name cannot be 'report_status' (RESERVED)");
								}
								if (usedNames.contains(normalizedFieldName)) {
									showNotificationWarning("Duplicate Field",
											"Field name '" + fieldName + "' conflicts with another");
									logError("Duplicate field name: " + fieldName);
									return false;
								}
								usedNames.add(normalizedFieldName);
								if (!fieldPane.validateCourtCaseMappings()) {
									return false;
								}
								String fieldType = fieldPane.fieldTypeComboBox.getValue() != null
										? fieldPane.fieldTypeComboBox.getValue().trim()
										: "";
								if (fieldType.isEmpty()) {
									logError("Field Type ComboBox cannot be empty.");
									showNotificationWarning("Report Creation Utility",
											"Field Type ComboBox cannot be empty.");
									return false;
								}
								if (containsNumbers(fieldType)) {
									logError("Field Type ComboBox value cannot contain numbers.");
									showNotificationWarning("Report Creation Utility",
											"Field Type ComboBox value cannot contain numbers.");
									return false;
								}
								if (fieldType.contains(" ")) {
									logError("Field Type cannot contain spaces.");
									showNotificationWarning("Report Creation Utility",
											"Field Type cannot contain spaces.");
									return false;
								}
								String keyFieldName = fieldPane.populateKeyField.getText().trim();
								if (containsNumbers(keyFieldName)) {
									logError("Key Field cannot contain numbers.");
									showNotificationWarning("Report Creation Utility",
											"Key Field cannot contain numbers.");
									return false;
								}
								if (keyFieldName.contains(" ")) {
									logError("Key Field cannot contain spaces.");
									showNotificationWarning("Report Creation Utility",
											"Key Field cannot contain spaces.");
									return false;
								}
							}
						}
					}
				}
			}
		}
		return true;
	}

	private boolean validateJsonStructure(String json) {
		try {
			JsonNode root = new ObjectMapper().readTree(json);
			return root.has("layout") || root.has("transfer");
		} catch (IOException e) {
			return false;
		}
	}

	private void handleImport(TextArea jsonTextArea) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			FullLayoutConfig config = mapper.readValue(jsonTextArea.getText(), FullLayoutConfig.class);
			if (!validateJsonStructure(jsonTextArea.getText())) {
				logError("Invalid JSON format", new IllegalArgumentException("Invalid JSON Structure"));
			}
			sectionContainer.getChildren().clear();
			transferContainer.getChildren().clear();
			if (config.getLayout() != null) {
				for (SectionConfig sectionConfig : config.getLayout()) {
					SectionPane sectionPane = createSectionPaneFromConfig(sectionConfig);
					sectionContainer.getChildren().add(sectionPane);
				}
			}
			if (config.getTransfer() != null) {
				TransferPane transferPane = createTransferPaneFromConfig(config.getTransfer());
				transferContainer.getChildren().add(transferPane);
			}
			showNotificationInfo("Import Successful", "Configuration loaded successfully");
		} catch (Exception e) {
			showNotificationWarning("Import Error", "Invalid JSON format: " + e.getMessage());
		}
	}

	// #region Config Creation Methods
	private FieldPane createFieldPaneFromConfig(FieldConfig config) {
		FieldPane pane = new FieldPane();
		pane.fieldNameField.setText(config.getFieldName());
		if (config.getPopulateKey() != null) {
			pane.populateKeyField.setText(config.getPopulateKey());
		}
		pane.fieldTypeComboBox.setValue(config.getFieldType().name());
		pane.growthSpinner.getValueFactory().setValue(config.getSize());
		if (config.getFieldType() == FieldType.CUSTOM_DROPDOWN) {
			pane.customDropdownComboBox.setValue(config.getDropdownType());
		}
		boolean isTreeView = config.getFieldType() == FieldType.CHARGES_TREE_VIEW
				|| config.getFieldType() == FieldType.CITATION_TREE_VIEW;
		if (config.getFieldType() == FieldType.BLANK_SPACE || isTreeView) {
			pane.fieldNameField.setDisable(true);
			pane.populateKeyField.setDisable(true);
			pane.growthSpinner.setDisable(true);
		}
		if (config.getLookupValue() != null) {
			pane.lookupValueComboBox.setValue(config.getLookupValue());
		}
		if (isTreeView && config.getCourtCaseMapping() != null && !config.getCourtCaseMapping().isEmpty()) {
			Platform.runLater(() -> {
				List<String> allFieldNames = pane.getAllCompatibleFieldNames();
				for (ComboBox<String> box : pane.courtCaseMappingBoxes.values()) {
					box.getItems().clear();
					box.getItems().add("None");
					box.getItems().addAll(allFieldNames);
				}
				for (Map.Entry<String, String> mappingEntry : config.getCourtCaseMapping().entrySet()) {
					try {
						CourtCaseFields fieldEnum = CourtCaseFields.valueOf(mappingEntry.getKey());
						String targetFieldName = mappingEntry.getValue();
						ComboBox<String> box = pane.courtCaseMappingBoxes.get(fieldEnum);
						if (box != null) {
							box.setValue(targetFieldName);
						}
					} catch (IllegalArgumentException e) {
						logError("Invalid CourtCaseField enum in imported JSON: " + mappingEntry.getKey());
					}
				}
			});
		}
		return pane;
	}

	private RowPane createRowPaneFromConfig(RowConfig config) {
		RowPane pane = new RowPane();
		for (FieldConfig fieldConfig : config.getFieldConfigs()) {
			FieldPane fieldPane = createFieldPaneFromConfig(fieldConfig);
			pane.fieldContainer.getChildren().add(fieldPane);
		}
		Platform.runLater(pane::updateFieldWidths);
		return pane;
	}

	private TransferPane createTransferPaneFromConfig(TransferConfig config) {
		TransferPane pane = new TransferPane();
		pane.transferNameField.setText(config.getTitle());
		for (RowConfig rowConfig : config.getRowConfigs()) {
			FieldConfig elementConfig = rowConfig.getFieldConfigs()[0];
			ElementPane elementPane = createElementPaneFromConfig(elementConfig);
			pane.elementContainer.getChildren().add(elementPane);
		}
		return pane;
	}

	private ElementPane createElementPaneFromConfig(FieldConfig config) {
		ElementPane pane = new ElementPane();
		String[] parts = config.getFieldName().split("_");
		if (parts.length >= 3) {
			pane.elementNameField.setText(parts[1]);
			String reportName = parts[2];
			pane.reportComboBox.setValue(reportName);
			if (!pane.reportComboBox.getItems().contains(reportName)) {
				pane.reportComboBox.getItems().add(reportName);
			}
		}
		return pane;
	}

	private SectionPane createSectionPaneFromConfig(SectionConfig config) {
		SectionPane pane = new SectionPane();
		pane.sectionTitleField.setText(config.getSectionTitle());
		if (config.isPullFromLookup()) {
			pane.pullFromLookupCheckbox.setSelected(true);
			pane.lookupTypeComboBox.setValue(config.getLookupType());
		}
		for (RowConfig rowConfig : config.getRowConfigs()) {
			RowPane rowPane = createRowPaneFromConfig(rowConfig);
			pane.rowContainer.getChildren().add(rowPane);
		}
		if (config.isPullFromLookup()) {
			for (Node rowNode : pane.rowContainer.getChildren()) {
				if (rowNode instanceof RowPane) {
					for (Node fieldNode : ((RowPane) rowNode).fieldContainer.getChildren()) {
						if (fieldNode instanceof FieldPane) {
							((FieldPane) fieldNode).setLookupValueSelectorVisible(true);
						}
					}
				}
			}
		}
		return pane;
	}
	// #endregion
}