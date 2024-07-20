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
import java.util.ArrayList;

import static com.drozal.dataterminal.util.Report.reportUtil.createTitleBar;

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
		topBar = createTitleBar("Court Cases");
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
						caseNames.add(case1.getName());
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
								hbox.setStyle("-fx-alignment: center;");
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
				
				listView.getSelectionModel().selectedItemProperty().addListener((observable, oldvalue, newValue) -> {
					if (newValue != null) {
						for (Case case1 : courtCases.getCaseList()) {
							if (newValue.equals(case1.getName())) {
								offenceDateField.setText(case1.getOffenceDate() != null ? case1.getOffenceDate() : "");
								ageField.setText(case1.getAge() != null ? case1.getAge() : "");
								offenceLocationField.setText(
										case1.getOffenceLocation() != null ? case1.getOffenceLocation() : "");
								notesField.setText(case1.getNotes() != null ? case1.getNotes() : "");
								nameField.setText(case1.getName() != null ? case1.getName() : "");
								courtDateField.setText(case1.getCourtDate() != null ? case1.getCourtDate() : "");
								caseNumField.setText(case1.getCaseNumber() != null ? case1.getCaseNumber() : "");
								outcomesListView.setItems(FXCollections.observableArrayList(
										case1.getOutcomes() != null ? case1.getOutcomes() : new ArrayList<>()));
								offencesListView.setItems(FXCollections.observableArrayList(
										case1.getOffences() != null ? case1.getOffences() : new ArrayList<>()));
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
