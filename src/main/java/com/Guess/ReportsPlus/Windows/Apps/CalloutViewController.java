package com.Guess.ReportsPlus.Windows.Apps;

import com.Guess.ReportsPlus.util.Misc.CalloutManager;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationInfo;
import static com.Guess.ReportsPlus.util.Misc.stringUtil.calloutHistoryURL;

public class CalloutViewController {
	
	public static CalloutViewController calloutViewController;
	@javafx.fxml.FXML
	private BorderPane root;
	@javafx.fxml.FXML
	private ListView calHistoryList;
	@javafx.fxml.FXML
	private AnchorPane calloutPane;
	@javafx.fxml.FXML
	private Label activecalfill;
	@javafx.fxml.FXML
	private Label calfill;
	@javafx.fxml.FXML
	private ListView calActiveList;
	@javafx.fxml.FXML
	private Button delBtn;
	
	public void initialize() {
		CalloutManager.loadActiveCallouts(calActiveList);
		CalloutManager.loadHistoryCallouts(calHistoryList);
		
		activecalfill.setText(localization.getLocalizedMessage("Callout_Manager.ActiveCallsLabel", "Active Calls"));
		calfill.setText(localization.getLocalizedMessage("Callout_Manager.CallHistoryLabel", "Call History"));
		delBtn.setText(localization.getLocalizedMessage("Callout_Manager.DelCalloutButton", "Delete"));
	}
	
	@javafx.fxml.FXML
	public void delBtn(ActionEvent actionEvent) {
		Node selectedNode = (Node) calHistoryList.getSelectionModel().getSelectedItem();
		if (selectedNode != null) {
			if (selectedNode instanceof GridPane gridPane) {
				String number = ((Label) gridPane.getChildren().get(1)).getText();
				
				CalloutManager.deleteCallout(calloutHistoryURL, number);
				log("Deleted callout #" + number + " from Callout History", LogUtils.Severity.INFO);
				showNotificationInfo("Callout Manager", "Deleted callout #" + number + " from Callout History");
				
				CalloutManager.loadHistoryCallouts(calHistoryList);
			}
		}
	}
	
	public ListView getCalHistoryList() {
		return calHistoryList;
	}
	
	public ListView getCalActiveList() {
		return calActiveList;
	}
	
	public AnchorPane getCalloutPane() {
		return calloutPane;
	}
	
	public BorderPane getRoot() {
		return root;
	}
	
	public Label getActivecalfill() {
		return activecalfill;
	}
	
	public Label getCalfill() {
		return calfill;
	}
}
