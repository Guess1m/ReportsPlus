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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	private static final Pattern FINE_PATTERN = Pattern.compile("Fined: (\\d+)");
	@javafx.fxml.FXML
	private Label totalLabel;
	
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
								updateFields(case1);
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
	
	private void updateFields(Case case1) {
		offenceDateField.setText(case1.getOffenceDate() != null ? case1.getOffenceDate() : "");
		ageField.setText(case1.getAge() != null ? String.valueOf(case1.getAge()) : "");
		offenceLocationField.setText(case1.getOffenceLocation() != null ? case1.getOffenceLocation() : "");
		notesField.setText(case1.getNotes() != null ? case1.getNotes() : "");
		nameField.setText(case1.getName() != null ? case1.getName() : "");
		courtDateField.setText(case1.getCourtDate() != null ? case1.getCourtDate() : "");
		caseNumField.setText(case1.getCaseNumber() != null ? case1.getCaseNumber() : "");
		
		ObservableList<Label> offenceLabels = createLabels(case1.getOffences());
		ObservableList<Label> outcomeLabels = createLabels(case1.getOutcomes());
		
		int fineTotal = calculateFineTotal(case1.getOutcomes());
		totalLabel.setText("Fine Total: " + fineTotal);
		
		outcomesListView.setItems(outcomeLabels);
		offencesListView.setItems(offenceLabels);
		
		setCellFactory(outcomesListView);
		setCellFactory(offencesListView);
	}
	
	private ObservableList<Label> createLabels(String text) {
		ObservableList<Label> labels = FXCollections.observableArrayList();
		if (text != null) {
			String[] items = text.split("\\|");
			for (String item : items) {
				if (!item.trim().isEmpty()) {
					labels.add(new Label(item.trim()));
				}
			}
		}
		return labels;
	}
	
	private int calculateFineTotal(String outcomes) {
		int fineTotal = 0;
		if (outcomes != null) {
			Matcher matcher = FINE_PATTERN.matcher(outcomes);
			while (matcher.find()) {
				fineTotal += Integer.parseInt(matcher.group(1));
			}
		}
		return fineTotal;
	}
	
	private void setCellFactory(ListView<Label> listView) {
		listView.setCellFactory(new Callback<>() {
			@Override
			public ListCell<Label> call(ListView<Label> param) {
				return new ListCell<>() {
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
	}
}
