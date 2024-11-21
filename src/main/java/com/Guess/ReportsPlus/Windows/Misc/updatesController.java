package com.Guess.ReportsPlus.Windows.Misc;

import com.Guess.ReportsPlus.util.Misc.NotificationManager;
import com.Guess.ReportsPlus.util.Misc.stringUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.util.Misc.stringUtil.version;
import static com.Guess.ReportsPlus.util.Misc.updateUtil.gitVersion;

public class updatesController {
	
	private static final Duration ANIMATION_DURATION = Duration.seconds(1.2);
	List<String> updates = new ArrayList<>();
	@javafx.fxml.FXML
	private BorderPane root;
	@javafx.fxml.FXML
	private Label currentVer;
	@javafx.fxml.FXML
	private Label recentVer;
	@javafx.fxml.FXML
	private Label verChangelog;
	@javafx.fxml.FXML
	private VBox changelogBox;
	@javafx.fxml.FXML
	private Button checkupdatesbtn;
	@javafx.fxml.FXML
	private Label currentverlabel;
	@javafx.fxml.FXML
	private Label changeloglabel;
	@javafx.fxml.FXML
	private Label mostrecentlabel;
	@javafx.fxml.FXML
	private Label verinfolabel;
	
	public void initialize() {
		verChangelog.setText(version);
		
		updates.addAll(List.of(stringUtil.updatesList));
		
		updates.forEach(string -> {
			Label label = new Label("* " + string);
			label.setStyle("-fx-text-fill:  #5A72A0; -fx-font-family: \"Segoe UI\";");
			changelogBox.getChildren().add(label);
		});
		
		checkUpdates();
		
		checkupdatesbtn.setText(
				localization.getLocalizedMessage("UpdatesWindow.CheckUpdatesButton", "Check For Updates"));
		currentverlabel.setText(
				localization.getLocalizedMessage("UpdatesWindow.CurrentVersionLabel", "Current Version:"));
		changeloglabel.setText(localization.getLocalizedMessage("UpdatesWindow.ChangelogLabel", "Changelog"));
		mostrecentlabel.setText(
				localization.getLocalizedMessage("UpdatesWindow.MostRecentLabel", "Most Recent Version:"));
		verinfolabel.setText(localization.getLocalizedMessage("UpdatesWindow.VersionInfoLabel", "Version Information"));
		
	}
	
	private void checkUpdates() {
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
				recentVer.setText(Objects.requireNonNullElse(gitVersion, localization.getLocalizedMessage(
						"Desktop.NewVersionAvailable", "New Version Available!")));
				recentVer.setStyle("-fx-text-fill: red;");
				NotificationManager.showNotificationError("Update Available", localization.getLocalizedMessage(
						"Desktop.NewVersionAvailable",
						"New Version Available!") + " " + gitVersion + " Visit LCPDFR Website!");
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
