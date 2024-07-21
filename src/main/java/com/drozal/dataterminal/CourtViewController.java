package com.drozal.dataterminal;

import com.drozal.dataterminal.util.server.Objects.CourtData.Case;
import com.drozal.dataterminal.util.server.Objects.CourtData.CourtCases;
import com.drozal.dataterminal.util.server.Objects.CourtData.CourtUtils;
import jakarta.xml.bind.JAXBException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.IOException;

import static com.drozal.dataterminal.util.Report.reportUtil.createSimpleTitleBar;

public class CourtViewController {
	
	AnchorPane topBar;
	@javafx.fxml.FXML
	private TextField offenceDateField;
	@javafx.fxml.FXML
	private TextField ageField;
	@javafx.fxml.FXML
	private TextField offenceLocationField;
	@javafx.fxml.FXML
	private BorderPane root;
	@javafx.fxml.FXML
	private TextField notesField;
	@javafx.fxml.FXML
	private TextField nameField;
	@javafx.fxml.FXML
	private TextField courtDateField;
	@javafx.fxml.FXML
	private TextField caseNumField;
	@javafx.fxml.FXML
	private ListView outcomesListView;
	@javafx.fxml.FXML
	private ListView offencesListView;
	@javafx.fxml.FXML
	private ListView caseList;
	
	public void initialize() {
		topBar = createSimpleTitleBar("Court Cases", true);
		root.setTop(topBar);
		
		loadCaseLabels(caseList);
	}
	
	public void loadCaseLabels(ListView<String> listView) {
		try {
			CourtCases courtCases = CourtUtils.loadCourtCases();
			ObservableList<String> caseNames = FXCollections.observableArrayList();
			if (courtCases.getCaseList() != null) {
				for (Case case1 : courtCases.getCaseList()) {
					if (!case1.getName().isEmpty()) {
						caseNames.add(case1.getOffenceDate().replaceAll("-", "/") + " " + case1.getName());
					}
				}
				listView.setItems(caseNames);
				
				listView.setCellFactory(new Callback<>() {
					@Override
					public ListCell<String> call(ListView<String> param) {
						return new ListCell<>() {
							private final HBox hbox;
							private final Label label;
							
							{
								hbox = new HBox();
								label = new Label();
								hbox.getChildren().add(label);
							}
							
							@Override
							protected void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (empty || item == null) {
									setGraphic(null);
								} else {
									label.setText(item);
									setGraphic(hbox);
								}
							}
						};
					}
				});
				
				listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
					if (newValue != null) {
						for (Case case1 : courtCases.getCaseList()) {
							if (newValue.equals(case1.getOffenceDate().replaceAll("-", "/") + " " + case1.getName())) {
								offenceDateField.setText(case1.getOffenceDate() != null ? case1.getOffenceDate() : "");
								ageField.setText(case1.getAge() != null ? String.valueOf(case1.getAge()) : "");
								offenceLocationField.setText(
										case1.getOffenceLocation() != null ? case1.getOffenceLocation() : "");
								notesField.setText(case1.getNotes() != null ? case1.getNotes() : "");
								nameField.setText(case1.getName() != null ? case1.getName() : "");
								courtDateField.setText(case1.getCourtDate() != null ? case1.getCourtDate() : "");
								caseNumField.setText(case1.getCaseNumber() != null ? case1.getCaseNumber() : "");
								
								ObservableList<Label> outcomeLabels = FXCollections.observableArrayList();
								if (case1.getOutcomes() != null) {
									String[] outcomes = case1.getOutcomes().split("\\|");
									for (String outcome : outcomes) {
										if (!outcome.trim().isEmpty()) {
											Label label = new Label(outcome.trim());
											outcomeLabels.add(label);
										}
									}
								}
								
								ObservableList<Label> offenceLabels = FXCollections.observableArrayList();
								if (case1.getOffences() != null) {
									String[] offences = case1.getOffences().split("\\|");
									for (String offence : offences) {
										if (!offence.trim().isEmpty()) {
											Label label = new Label(offence.trim());
											offenceLabels.add(label);
										}
									}
								}
								
								outcomesListView.setItems(outcomeLabels);
								offencesListView.setItems(offenceLabels);
								
								outcomesListView.setCellFactory(new Callback<ListView<Label>, ListCell<Label>>() {
									@Override
									public ListCell<Label> call(ListView<Label> param) {
										return new ListCell<Label>() {
											@Override
											protected void updateItem(Label item, boolean empty) {
												super.updateItem(item, empty);
												if (empty || item == null) {
													setText(null);
													setGraphic(null);
												} else {
													setGraphic(item);
												}
											}
										};
									}
								});
								
								offencesListView.setCellFactory(new Callback<ListView<Label>, ListCell<Label>>() {
									@Override
									public ListCell<Label> call(ListView<Label> param) {
										return new ListCell<Label>() {
											@Override
											protected void updateItem(Label item, boolean empty) {
												super.updateItem(item, empty);
												if (empty || item == null) {
													setText(null);
													setGraphic(null);
												} else {
													setGraphic(item);
												}
											}
										};
									}
								});
								
								break;
							}
						}
					}
				});
			}
		} catch (JAXBException | IOException e) {
			System.err.println("Error loading Case labels: " + e.getMessage());
		}
	}
	
}
