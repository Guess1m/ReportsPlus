package com.drozal.dataterminal.Windows.Misc;

import com.drozal.dataterminal.util.Misc.NotificationManager;
import com.drozal.dataterminal.util.Misc.stringUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.drozal.dataterminal.util.Misc.stringUtil.version;
import static com.drozal.dataterminal.util.Misc.updateUtil.gitVersion;

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
	
	public void initialize() {
		verChangelog.setText(version);
		
		updates.addAll(List.of(stringUtil.updatesList));
		
		updates.forEach(string -> {
			Label label = new Label("* " + string);
			label.setStyle("-fx-text-fill:  #5A72A0; -fx-font-family: \"Segoe UI\";");
			changelogBox.getChildren().add(label);
		});
		
		checkUpdates();
		
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
				recentVer.setText(Objects.requireNonNullElse(gitVersion, "New Ver. Available!"));
				recentVer.setStyle("-fx-text-fill: red;");
				NotificationManager.showNotificationError("Update Available",
				                                          "There is a New Verion Available! " + gitVersion + " Check LCPDFR Website!");
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
