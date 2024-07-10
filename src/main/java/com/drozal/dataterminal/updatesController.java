package com.drozal.dataterminal;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import static com.drozal.dataterminal.util.Misc.stringUtil.version;
import static com.drozal.dataterminal.util.Misc.updateUtil.*;
import static com.drozal.dataterminal.util.Report.reportUtil.createSimpleTitleBar;

public class updatesController {
	
	private static final Duration ANIMATION_DURATION = Duration.seconds(1.2);
	AnchorPane topBar;
	@javafx.fxml.FXML
	private BorderPane root;
	@javafx.fxml.FXML
	private Label currentVer;
	@javafx.fxml.FXML
	private Label recentVer;
	@javafx.fxml.FXML
	private Label verChangelog;
	
	public void initialize() {
		topBar = createSimpleTitleBar("Version Information", false);
		root.setTop(topBar);
		
		verChangelog.setText(version);
		
		checkUpdates();
	}
	
	@SuppressWarnings("IfStatementWithIdenticalBranches")
	private void checkUpdates() {
		checkForUpdates();
		
		Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, event -> {
			currentVer.setText("Updating.");
			recentVer.setStyle("-fx-text-fill: #FDFFE2;");
			recentVer.setText("Updating.");
		}), new KeyFrame(Duration.seconds(0.4), event -> {
			currentVer.setText("Updating..");
			recentVer.setStyle("-fx-text-fill: #FDFFE2;");
			recentVer.setText("Updating..");
		}), new KeyFrame(Duration.seconds(0.8), event -> {
			currentVer.setText("Updating...");
			recentVer.setStyle("-fx-text-fill: #FDFFE2;");
			recentVer.setText("Updating...");
		}), new KeyFrame(ANIMATION_DURATION, event -> {
			currentVer.setText(version);
			if (!version.equals(gitVersion)) {
				if (gitVersion == null) {
					recentVer.setText("New Ver. Available!");
					recentVer.setStyle("-fx-text-fill: red;");
					openWebpage("https://github.com/Guess1m/ReportsPlus/releases");
				} else {
					recentVer.setText(gitVersion);
					recentVer.setStyle("-fx-text-fill: red;");
					openWebpage("https://github.com/Guess1m/ReportsPlus/releases");
				}
			} else {
				recentVer.setText(gitVersion);
			}
		}));
		timeline.play();
	}
	
	@javafx.fxml.FXML
	public void updateBtnAction() {
		checkUpdates();
	}
	
}
