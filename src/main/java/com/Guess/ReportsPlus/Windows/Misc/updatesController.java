package com.Guess.ReportsPlus.Windows.Misc;

import com.Guess.ReportsPlus.config.ConfigWriter;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.Guess.ReportsPlus.util.Misc.NotificationManager;
import com.Guess.ReportsPlus.util.Misc.stringUtil;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.controllerUtils.handleClose;
import static com.Guess.ReportsPlus.util.Misc.stringUtil.getJarPath;
import static com.Guess.ReportsPlus.util.Misc.stringUtil.version;
import static com.Guess.ReportsPlus.util.Misc.updateUtil.gitVersion;
import static com.Guess.ReportsPlus.util.Misc.updateUtil.runJar;

public class updatesController {
	
	private static final Duration ANIMATION_DURATION = Duration.seconds(0.4);
	List<String> updates = new ArrayList<>();
	boolean updateAvailable = false;
	boolean useIntelApplication = false;
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
	private Label currentverlabel;
	@javafx.fxml.FXML
	private Label changeloglabel;
	@javafx.fxml.FXML
	private Label mostrecentlabel;
	@javafx.fxml.FXML
	private Label verinfolabel;
	@javafx.fxml.FXML
	private Button updateBtn;
	@javafx.fxml.FXML
	private HBox hbox;
	@javafx.fxml.FXML
	private Label updateStatusLabel;
	@javafx.fxml.FXML
	private CheckBox intelChipCheckbox;
	
	public void initialize() {
		verChangelog.setText(version);
		
		updates.addAll(List.of(stringUtil.updatesList));
		
		updates.forEach(string -> {
			Label label = new Label("* " + string);
			label.setStyle("-fx-text-fill:  #5A72A0; -fx-font-family: \"Segoe UI\";");
			changelogBox.getChildren().add(label);
		});
		
		checkUpdates();
		
		intelChipCheckbox.setText(localization.getLocalizedMessage("UpdatesWindow.IntelChipCheckbox", "Intel Chip (ONLY For Intel MacOS!)"));
		updateBtn.setText(localization.getLocalizedMessage("UpdatesWindow.UpdateButton", "AutoUpdate (BETA)"));
		currentverlabel.setText(localization.getLocalizedMessage("UpdatesWindow.CurrentVersionLabel", "Current Version:"));
		changeloglabel.setText(localization.getLocalizedMessage("UpdatesWindow.ChangelogLabel", "Changelog"));
		mostrecentlabel.setText(localization.getLocalizedMessage("UpdatesWindow.MostRecentLabel", "Most Recent Version:"));
		verinfolabel.setText(localization.getLocalizedMessage("UpdatesWindow.VersionInfoLabel", "Version Information"));
		
		intelChipCheckbox.setOnAction(event -> {
			if (intelChipCheckbox.isSelected()) {
				useIntelApplication = true;
			} else {
				useIntelApplication = false;
			}
		});
		
	}
	
	private void checkUpdates() {
		currentVer.setText(version);
		if (!version.equals(gitVersion)) {
			recentVer.setText(Objects.requireNonNullElse(gitVersion, localization.getLocalizedMessage("Desktop.NewVersionAvailable", "New Version Available!")));
			recentVer.setStyle("-fx-text-fill: red;");
			NotificationManager.showNotificationErrorPersistent("Update Available", localization.getLocalizedMessage("Desktop.NewVersionAvailable", "New Version Available!") + " " + gitVersion + " Check Updates App!");
			updateAvailable = true;
		} else {
			recentVer.setText(gitVersion);
		}
	}
	
	@javafx.fxml.FXML
	public void LaunchUpdater(ActionEvent actionEvent) {
		if (updateAvailable) {
			if (useIntelApplication) {
				ConfigWriter.configwrite("updater", "useIntel", "true");
			}
			updateStatusLabel.setText("Updating...");
			log("Shutting Down For AutoUpdate..", LogUtils.Severity.DEBUG);
			boolean canUpdate = runJar(getJarPath() + File.separator + "tools" + File.separator + "Updater.jar");
			if (canUpdate) {
				handleClose();
			} else {
				log("Not able to update", LogUtils.Severity.WARN);
				updateStatusLabel.setText(localization.getLocalizedMessage("UpdatesWindow.MissingUpdater", "Missing UpdateUtility!"));
			}
			
		} else {
			log("No Update Available, Cant Launch Updater", LogUtils.Severity.WARN);
			updateStatusLabel.setText(localization.getLocalizedMessage("UpdatesWindow.CantUpdateLabel", "No Update Available!"));
		}
	}
}
