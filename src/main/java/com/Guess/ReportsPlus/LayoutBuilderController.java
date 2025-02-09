package com.Guess.ReportsPlus;

import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.Guess.ReportsPlus.util.Report.nestedReportUtils.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.notesViewController;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.updateTextFromNotepad;
import static com.Guess.ReportsPlus.util.Report.reportUtil.createReportWindow;

public class LayoutBuilderController implements Initializable {
	
	public static LayoutBuilderController layoutBuilderController;
	
	List<String> fieldNames = new ArrayList<>();
	@FXML
	private VBox sectionContainer;
	@FXML
	private Button addSectionButton;
	@FXML
	private Button buildLayoutButton;
	
	private Map<String, List<String>> keyMap;
	@FXML
	private TextField reportTitleField;
	@FXML
	private Button addTransferButton;
	@FXML
	private VBox transferContainer;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		addSectionButton.setOnAction(event -> addSection());
		addTransferButton.setOnAction(event -> addTransfer());
		buildLayoutButton.setOnAction(event -> {
			if (reportTitleField.getText().isEmpty()) {
				log("LayoutBuilder; Report Title Field is Empty", LogUtils.Severity.ERROR);
				return;
			}
			
			List<SectionConfig> layout = buildLayout();
			SectionConfig[] sectionsArray = layout.toArray(new SectionConfig[0]);
			
			TransferConfig transferConfig = buildTransferConfig();
			
			Map<String, Object> accidentReportMap = createReportWindow(reportTitleField.getText().trim(),
			                                                           transferConfig, sectionsArray);
			
			Map<String, Object> accidentReport = (Map<String, Object>) accidentReportMap.get(
					reportTitleField.getText().trim() + " Map");
			
			for (String name : accidentReport.keySet()) {
				if (name.startsWith("transfer_")) {
					String[] values = name.split("_");
					Button btn = (Button) accidentReport.get(name);
					btn.setText(values[1]);
					btn.setOnAction(btnAction -> {
						//TODO: add implementation for going to this report (maybe store all reports in list??)
						System.out.println("opening report to: " + values[2]);
					});
				}
			}
			
			MenuButton pullNotesBtn = (MenuButton) accidentReportMap.get("pullNotesBtn");
			
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
								for (String field : fieldNames) {
									log("LayoutBuilder; Processing field: " + field, LogUtils.Severity.DEBUG);
									
									Object fieldValue = accidentReport.get(field);
									
									if (fieldValue == null) {
										log("LayoutBuilder; Field is null: " + field, LogUtils.Severity.ERROR);
										continue;
									}
									
									String key = keyMap.entrySet().stream().filter(
											entry -> entry.getValue().contains(field)).map(
											Map.Entry::getKey).findFirst().orElse(null);
									
									if (key == null) {
										continue;
									}
									
									if (fieldValue instanceof TextField) {
										updateTextFromNotepad((TextField) fieldValue, noteArea, "-" + key);
									} else if (fieldValue instanceof ComboBox) {
										updateTextFromNotepad(((ComboBox<?>) fieldValue).getEditor(), noteArea,
										                      "-" + key);
									} else if (fieldValue instanceof TextArea) {
										updateTextFromNotepad((TextArea) fieldValue, noteArea, "-" + key);
									} else {
										log("LayoutBuilder; Unknown field type: " + fieldValue.getClass().getSimpleName(),
										    LogUtils.Severity.ERROR);
									}
								}
							});
						}
					}
				} else {
					log("NotesViewController Is Null", LogUtils.Severity.ERROR);
				}
			});
		});
	}
	
	private void addSection() {
		SectionPane sectionPane = new SectionPane();
		sectionContainer.getChildren().add(sectionPane);
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
	
	//TODO: Implement Transfer Logic
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
				System.out.println("Using fieldName: " + "transfer_" + fieldName + "_" + fieldReport);
				fieldNames.add("transfer_" + fieldName + "_" + fieldReport);
				FieldConfig fieldConfig = new FieldConfig("transfer_" + fieldName + "_" + fieldReport, 12,
				                                          FieldType.TRANSFER_BUTTON);
				rowConfigs.add(new RowConfig(fieldConfig));
			}
		}
		return new TransferConfig(transferName, rowConfigs.toArray(new RowConfig[0]));
	}
	
	private class SectionPane extends VBox {
		private TextField sectionTitleField;
		private VBox rowContainer;
		private Button addRowButton;
		private Button removeSectionButton;
		
		SectionPane() {
			setSpacing(5);
			setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-padding: 10;");
			HBox header = new HBox(10);
			sectionTitleField = new TextField();
			sectionTitleField.setPromptText("Section Title");
			removeSectionButton = new Button("Remove Section");
			removeSectionButton.setOnAction(event -> sectionContainer.getChildren().remove(this));
			header.getChildren().addAll(new Label("Section:"), sectionTitleField, removeSectionButton);
			rowContainer = new VBox(5);
			addRowButton = new Button("Add Row");
			addRowButton.setOnAction(event -> addRow());
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
			return new SectionConfig(title, rowConfigs.toArray(new RowConfig[0]));
		}
	}
	
	private class RowPane extends VBox {
		private HBox fieldContainer;
		private Button addFieldButton;
		private Button removeRowButton;
		
		RowPane() {
			setSpacing(5);
			setStyle("-fx-border-color: lightgray; -fx-border-width: 1; -fx-padding: 5;");
			fieldContainer = new HBox(10);
			addFieldButton = new Button("Add Field");
			addFieldButton.setOnAction(event -> addField());
			removeRowButton = new Button("Remove Row");
			removeRowButton.setOnAction(event -> {
				VBox parent = (VBox) getParent();
				parent.getChildren().remove(this);
				recalcWidthLimits();
			});
			HBox controlBox = new HBox(10, addFieldButton, removeRowButton);
			getChildren().addAll(fieldContainer, controlBox);
			recalcWidthLimits();
		}
		
		private void addField() {
			int currentTotal = 0;
			for (Node node : fieldContainer.getChildren()) {
				if (node instanceof FieldPane) {
					currentTotal += ((FieldPane) node).widthSpinner.getValue();
				}
			}
			int available = 12 - currentTotal;
			if (available < 1) {
				addFieldButton.setDisable(true);
				return;
			}
			FieldPane fieldPane = new FieldPane(available);
			fieldContainer.getChildren().add(fieldPane);
			fieldPane.widthSpinner.valueProperty().addListener((obs, oldVal, newVal) -> recalcWidthLimits());
			recalcWidthLimits();
		}
		
		private void recalcWidthLimits() {
			int currentTotal = 0;
			List<FieldPane> fields = new ArrayList<>();
			for (Node node : fieldContainer.getChildren()) {
				if (node instanceof FieldPane) {
					FieldPane field = (FieldPane) node;
					fields.add(field);
					currentTotal += field.widthSpinner.getValue();
				}
			}
			int remaining = 12 - currentTotal;
			for (FieldPane field : fields) {
				int currentValue = field.widthSpinner.getValue();
				int allowedMax = currentValue + remaining;
				field.widthSpinner.setValueFactory(
						new SpinnerValueFactory.IntegerSpinnerValueFactory(1, allowedMax, currentValue));
			}
			addFieldButton.setDisable(remaining == 0);
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
	
	private void addTransfer() {
		if (!transferContainer.getChildren().isEmpty()) {
			return;
		}
		TransferPane transferPane = new TransferPane();
		transferContainer.getChildren().add(transferPane);
	}
	
	private class FieldPane extends VBox {
		private TextField fieldNameField;
		private TextField populateKeyField;
		private Spinner<Integer> widthSpinner;
		private ComboBox<String> fieldTypeComboBox;
		private Button removeFieldButton;
		
		FieldPane() {
			this(5);
		}
		
		FieldPane(int defaultWidth) {
			setSpacing(3);
			setStyle("-fx-border-color: #CCC; -fx-border-width: 1; -fx-padding: 5;");
			VBox fieldRow = new VBox(10);
			
			fieldNameField = new TextField();
			fieldNameField.setPromptText("Field Name");
			
			populateKeyField = new TextField();
			populateKeyField.setPromptText("Populate Key (optional)");
			
			widthSpinner = new Spinner<>(1, defaultWidth, defaultWidth);
			
			fieldTypeComboBox = new ComboBox<>();
			fieldTypeComboBox.getItems().addAll("TEXT_FIELD", "COMBO_BOX_STREET", "COMBO_BOX_AREA", "TEXT_AREA",
			                                    "TIME_FIELD", "DATE_FIELD", "OFFICER_NAME", "OFFICER_RANK",
			                                    "OFFICER_DIVISION", "OFFICER_AGENCY", "OFFICER_NUMBER",
			                                    "OFFICER_CALLSIGN");
			fieldTypeComboBox.getSelectionModel().selectFirst();
			
			removeFieldButton = new Button("Remove Field");
			removeFieldButton.setOnAction(event -> {
				HBox parent = (HBox) getParent();
				parent.getChildren().remove(this);
				RowPane row = (RowPane) parent.getParent();
				row.recalcWidthLimits();
			});
			
			fieldRow.getChildren().addAll(new Label("Name:"), fieldNameField, new Label("Populate Key:"),
			                              populateKeyField, new Label("Width:"), widthSpinner, new Label("Type:"),
			                              fieldTypeComboBox, removeFieldButton);
			
			getChildren().add(fieldRow);
		}
		
		private FieldConfig buildFieldConfig() {
			String name = fieldNameField.getText();
			String populateKey = populateKeyField.getText().isEmpty() ? null : populateKeyField.getText();
			int width = widthSpinner.getValue();
			FieldType type = FieldType.valueOf(fieldTypeComboBox.getValue());
			if (keyMap == null) {
				keyMap = new HashMap();
			}
			
			fieldNames.add(name);
			
			if (populateKey == null) {
				populateKey = "";
			}
			
			if (populateKey.length() > 0) {
				keyMap.computeIfAbsent(populateKey, k -> new ArrayList<>()).add(name);
				return new FieldConfig(name, width, type, populateKey);
			} else {
				return new FieldConfig(name, width, type);
			}
		}
	}
	
	private class TransferPane extends VBox {
		private TextField transferNameField;
		private VBox elementContainer;
		private Button addElementButton;
		private Button removeTransferButton;
		
		TransferPane() {
			setSpacing(5);
			setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-padding: 10;");
			HBox header = new HBox(10);
			transferNameField = new TextField();
			transferNameField.setPromptText("Transfer Name");
			removeTransferButton = new Button("Remove Transfer");
			removeTransferButton.setOnAction(event -> transferContainer.getChildren().remove(this));
			header.getChildren().addAll(new Label("Transfer:"), transferNameField, removeTransferButton);
			elementContainer = new VBox(5);
			addElementButton = new Button("Add Element");
			addElementButton.setOnAction(event -> addElement());
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
			elementNameField = new TextField();
			elementNameField.setPromptText("Element Name");
			reportComboBox = new ComboBox<>();
			reportComboBox.getItems().addAll("Report A", "Report B", "Report C");
			removeElementButton = new Button("Remove Element");
			removeElementButton.setOnAction(event -> ((VBox) getParent()).getChildren().remove(this));
			getChildren().addAll(new Label("Element:"), elementNameField, new Label("Report:"), reportComboBox,
			                     removeElementButton);
		}
	}
}