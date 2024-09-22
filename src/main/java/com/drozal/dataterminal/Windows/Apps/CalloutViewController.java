package com.drozal.dataterminal.Windows.Apps;

import com.drozal.dataterminal.util.Misc.CalloutManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import static com.drozal.dataterminal.util.Misc.CalloutManager.handleSelectedNodeActive;
import static com.drozal.dataterminal.util.Misc.CalloutManager.handleSelectedNodeHistory;

public class CalloutViewController {
	
	@javafx.fxml.FXML
	private TextField calType;
	@javafx.fxml.FXML
	private Label calloutInfoTitle;
	@javafx.fxml.FXML
	private TextField calTime;
	@javafx.fxml.FXML
	private TextField calPriority;
	@javafx.fxml.FXML
	private TextField calStreet;
	@javafx.fxml.FXML
	private BorderPane root;
	@javafx.fxml.FXML
	private ListView calHistoryList;
	@javafx.fxml.FXML
	private TextField calArea;
	@javafx.fxml.FXML
	private AnchorPane calloutPane;
	@javafx.fxml.FXML
	private TextArea calDesc;
	@javafx.fxml.FXML
	private Label activecalfill;
	@javafx.fxml.FXML
	private TextField calNum;
	@javafx.fxml.FXML
	private Label calfill;
	@javafx.fxml.FXML
	private TextField calCounty;
	@javafx.fxml.FXML
	private ListView calActiveList;
	@javafx.fxml.FXML
	private Label caldetlbl2;
	@javafx.fxml.FXML
	private Label caldetlbl1;
	@javafx.fxml.FXML
	private Label caldetlbl4;
	@javafx.fxml.FXML
	private ToggleButton showCurrentCalToggle;
	@javafx.fxml.FXML
	private Label caldetlbl3;
	@javafx.fxml.FXML
	private Label caldetlbl6;
	@javafx.fxml.FXML
	private Label caldetlbl5;
	@javafx.fxml.FXML
	private Label caldetlbl8;
	@javafx.fxml.FXML
	private Label caldetlbl7;
	@javafx.fxml.FXML
	private Label caldetlbl9;
	@javafx.fxml.FXML
	private TextField calDate;
	@javafx.fxml.FXML
	private AnchorPane currentCalPane;
	
	public static CalloutViewController calloutViewController;
	
	public void initialize() {
		currentCalPane.setPrefHeight(0);
		currentCalPane.setMaxHeight(0);
		currentCalPane.setMinHeight(0);
		currentCalPane.setVisible(false);
		calActiveList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				double toHeight = 329;
				
				Timeline timeline = new Timeline();
				
				KeyValue keyValuePrefHeight = new KeyValue(currentCalPane.prefHeightProperty(), toHeight);
				KeyValue keyValueMaxHeight = new KeyValue(currentCalPane.maxHeightProperty(), toHeight);
				KeyValue keyValueMinHeight = new KeyValue(currentCalPane.minHeightProperty(), toHeight);
				KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValuePrefHeight, keyValueMaxHeight,
				                                 keyValueMinHeight);
				
				timeline.getKeyFrames().add(keyFrame);
				
				timeline.play();
				currentCalPane.setVisible(true);
				handleSelectedNodeActive(calActiveList, currentCalPane, calNum, calArea, calCounty, calDate, calStreet,
				                         calDesc, calType, calTime, calPriority);
				showCurrentCalToggle.setSelected(true);
			}
		});
		
		calHistoryList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				double toHeight = 329;
				
				Timeline timeline = new Timeline();
				
				KeyValue keyValuePrefHeight = new KeyValue(currentCalPane.prefHeightProperty(), toHeight);
				KeyValue keyValueMaxHeight = new KeyValue(currentCalPane.maxHeightProperty(), toHeight);
				KeyValue keyValueMinHeight = new KeyValue(currentCalPane.minHeightProperty(), toHeight);
				KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValuePrefHeight, keyValueMaxHeight,
				                                 keyValueMinHeight);
				
				timeline.getKeyFrames().add(keyFrame);
				
				timeline.play();
				currentCalPane.setVisible(true);
				handleSelectedNodeHistory(calHistoryList, currentCalPane, calNum, calArea, calCounty, calDate,
				                          calStreet, calDesc, calType, calTime, calPriority);
				showCurrentCalToggle.setSelected(true);
			}
		});
		
		double toHeight = 0;
		
		Timeline timeline = new Timeline();
		
		KeyValue keyValuePrefHeight = new KeyValue(currentCalPane.prefHeightProperty(), toHeight);
		KeyValue keyValueMaxHeight = new KeyValue(currentCalPane.maxHeightProperty(), toHeight);
		KeyValue keyValueMinHeight = new KeyValue(currentCalPane.minHeightProperty(), toHeight);
		KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValuePrefHeight, keyValueMaxHeight,
		                                 keyValueMinHeight);
		
		timeline.getKeyFrames().add(keyFrame);
		
		timeline.play();
		currentCalPane.setVisible(false);
		
		CalloutManager.loadActiveCallouts(calActiveList);
		CalloutManager.loadHistoryCallouts(calHistoryList);
	}
	
	@javafx.fxml.FXML
	public void onShowCurrentCalToggled(ActionEvent actionEvent) {
		calActiveList.getSelectionModel().clearSelection();
		calHistoryList.getSelectionModel().clearSelection();
		if (!showCurrentCalToggle.isSelected()) {
			double toHeight = 0;
			
			Timeline timeline = new Timeline();
			
			KeyValue keyValuePrefHeight = new KeyValue(currentCalPane.prefHeightProperty(), toHeight);
			KeyValue keyValueMaxHeight = new KeyValue(currentCalPane.maxHeightProperty(), toHeight);
			KeyValue keyValueMinHeight = new KeyValue(currentCalPane.minHeightProperty(), toHeight);
			KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValuePrefHeight, keyValueMaxHeight,
			                                 keyValueMinHeight);
			
			timeline.getKeyFrames().add(keyFrame);
			
			timeline.play();
			currentCalPane.setVisible(false);
		} else {
			double toHeight = 329;
			
			Timeline timeline = new Timeline();
			
			KeyValue keyValuePrefHeight = new KeyValue(currentCalPane.prefHeightProperty(), toHeight);
			KeyValue keyValueMaxHeight = new KeyValue(currentCalPane.maxHeightProperty(), toHeight);
			KeyValue keyValueMinHeight = new KeyValue(currentCalPane.minHeightProperty(), toHeight);
			KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValuePrefHeight, keyValueMaxHeight,
			                                 keyValueMinHeight);
			
			timeline.getKeyFrames().add(keyFrame);
			
			timeline.play();
			currentCalPane.setVisible(true);
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
	
	public Label getCalloutInfoTitle() {
		return calloutInfoTitle;
	}
	
	public BorderPane getRoot() {
		return root;
	}
	
	public Label getActivecalfill() {
		return activecalfill;
	}
	
	public AnchorPane getCurrentCalPane() {
		return currentCalPane;
	}
	
	public Label getCalfill() {
		return calfill;
	}
	
	public Label getCaldetlbl2() {
		return caldetlbl2;
	}
	
	public Label getCaldetlbl1() {
		return caldetlbl1;
	}
	
	public Label getCaldetlbl4() {
		return caldetlbl4;
	}
	
	public Label getCaldetlbl3() {
		return caldetlbl3;
	}
	
	public Label getCaldetlbl6() {
		return caldetlbl6;
	}
	
	public Label getCaldetlbl5() {
		return caldetlbl5;
	}
	
	public Label getCaldetlbl8() {
		return caldetlbl8;
	}
	
	public Label getCaldetlbl7() {
		return caldetlbl7;
	}
	
	public Label getCaldetlbl9() {
		return caldetlbl9;
	}
	
}
