package com.Guess.ReportsPlus.Windows.Apps;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import static com.Guess.ReportsPlus.Desktop.mainDesktopController.newReportWindow;
import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.logs.Accident.AccidentReportUtils.newAccident;
import static com.Guess.ReportsPlus.logs.Arrest.ArrestReportUtils.newArrest;
import static com.Guess.ReportsPlus.logs.Callout.CalloutReportUtils.newCallout;
import static com.Guess.ReportsPlus.logs.Death.DeathReportUtils.newDeathReport;
import static com.Guess.ReportsPlus.logs.Impound.ImpoundReportUtils.newImpound;
import static com.Guess.ReportsPlus.logs.Incident.IncidentReportUtils.newIncident;
import static com.Guess.ReportsPlus.logs.Patrol.PatrolReportUtils.newPatrol;
import static com.Guess.ReportsPlus.logs.Search.SearchReportUtils.newSearch;
import static com.Guess.ReportsPlus.logs.TrafficCitation.TrafficCitationUtils.newCitation;
import static com.Guess.ReportsPlus.logs.TrafficStop.TrafficStopReportUtils.newTrafficStop;

public class NewReportVewController {
	
	public static NewReportVewController newReportVewController;
	
	@javafx.fxml.FXML
	private BorderPane root;
	@javafx.fxml.FXML
	private AnchorPane mainPane;
	@javafx.fxml.FXML
	private Button patrol;
	@javafx.fxml.FXML
	private Button search;
	@javafx.fxml.FXML
	private Button callout;
	@javafx.fxml.FXML
	private Button trafficstop;
	@javafx.fxml.FXML
	private Button arrest;
	@javafx.fxml.FXML
	private Button death;
	@javafx.fxml.FXML
	private Button impound;
	@javafx.fxml.FXML
	private Button citation;
	@javafx.fxml.FXML
	private Button incident;
	@javafx.fxml.FXML
	private Button accident;
	@javafx.fxml.FXML
	private Label selectReportTypeLabel;
	
	public void initialize() {
		accident.setOnAction(event -> {
			newAccident();
			closeWindow();
		});
		arrest.setOnAction(event -> {
			newArrest();
			closeWindow();
		});
		callout.setOnAction(event -> {
			newCallout();
			closeWindow();
		});
		death.setOnAction(event -> {
			newDeathReport();
			closeWindow();
		});
		impound.setOnAction(event -> {
			newImpound();
			closeWindow();
		});
		incident.setOnAction(event -> {
			newIncident();
			closeWindow();
		});
		patrol.setOnAction(event -> {
			newPatrol();
			closeWindow();
		});
		search.setOnAction(event -> {
			newSearch();
			closeWindow();
		});
		citation.setOnAction(event -> {
			newCitation();
			closeWindow();
		});
		trafficstop.setOnAction(event -> {
			newTrafficStop();
			closeWindow();
		});
		
		addLocale();
	}
	
	private void addLocale() {
		selectReportTypeLabel.setText(
				localization.getLocalizedMessage("NewReportApp.selectReportTypeLabel", "Select a Report Type:"));
		patrol.setText(localization.getLocalizedMessage("NewReportApp.patrol", "Patrol Report"));
		search.setText(localization.getLocalizedMessage("NewReportApp.search", "Search Report"));
		callout.setText(localization.getLocalizedMessage("NewReportApp.callout", "Callout Report"));
		trafficstop.setText(localization.getLocalizedMessage("NewReportApp.trafficstop", "TrafficStop Report"));
		arrest.setText(localization.getLocalizedMessage("NewReportApp.arrest", "Arrest Report"));
		death.setText(localization.getLocalizedMessage("NewReportApp.death", "Death Report"));
		impound.setText(localization.getLocalizedMessage("NewReportApp.impound", "Impound Report"));
		citation.setText(localization.getLocalizedMessage("NewReportApp.citation", "Citation Report"));
		incident.setText(localization.getLocalizedMessage("NewReportApp.incident", "Incident Report"));
		accident.setText(localization.getLocalizedMessage("NewReportApp.accident", "Accident Report"));
	}
	
	private void closeWindow() {
		if (newReportWindow != null) {
			newReportWindow.closeWindow();
		}
	}
	
	public BorderPane getRoot() {
		return root;
	}
	
	public Button getAccident() {
		return accident;
	}
	
	public Button getArrest() {
		return arrest;
	}
	
	public Button getCallout() {
		return callout;
	}
	
	public Button getCitation() {
		return citation;
	}
	
	public Button getDeath() {
		return death;
	}
	
	public Button getImpound() {
		return impound;
	}
	
	public Button getIncident() {
		return incident;
	}
	
	public Button getPatrol() {
		return patrol;
	}
	
	public Button getSearch() {
		return search;
	}
	
	public Label getSelectReportTypeLabel() {
		return selectReportTypeLabel;
	}
	
	public Button getTrafficstop() {
		return trafficstop;
	}
}
