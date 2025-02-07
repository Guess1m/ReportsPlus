package com.Guess.ReportsPlus.Windows.Misc;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager;
import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.Guess.ReportsPlus.util.Strings.updateStrings;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.MainApplication.mainDesktopControllerObj;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationError;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationErrorPersistent;
import static com.Guess.ReportsPlus.util.Misc.updateUtil.gitVersion;
import static com.Guess.ReportsPlus.util.Strings.updateStrings.version;

public class updatesController {
	
	List<String> updates = new ArrayList<>();
	boolean updateAvailable = false;
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
	private Button localeChangesBtn;
	
	public void initialize() {
		verChangelog.setText(version);
		hbox.setVisible(false);
		
		updates.addAll(List.of(updateStrings.updatesList));
		
		updates.forEach(string -> {
			Label label = new Label("* " + string);
			label.setMinWidth(Region.USE_PREF_SIZE);
			label.setStyle("-fx-text-fill:  #5A72A0; -fx-font-family: \"Inter 24pt Regular\";");
			changelogBox.getChildren().add(label);
		});
		
		checkUpdates();
		
		if (updateAvailable) {
			log("Found update, Displaying update utility..", LogUtils.Severity.DEBUG);
			hbox.setVisible(true);
		} else {
			log("No Update Available, not displaying updateUtility", LogUtils.Severity.WARN);
			hbox.setVisible(false);
		}
		
		updateBtn.setText(localization.getLocalizedMessage("UpdatesWindow.launchAutoUpdateBtn", "Launch Update Utility (BETA)"));
		currentverlabel.setText(localization.getLocalizedMessage("UpdatesWindow.CurrentVersionLabel", "Current Version:"));
		changeloglabel.setText(localization.getLocalizedMessage("UpdatesWindow.ChangelogLabel", "Changelog"));
		mostrecentlabel.setText(localization.getLocalizedMessage("UpdatesWindow.MostRecentLabel", "Most Recent Version:"));
		verinfolabel.setText(localization.getLocalizedMessage("UpdatesWindow.VersionInfoLabel", "Version Information"));
		localeChangesBtn.setText(version + " " + localization.getLocalizedMessage("UpdatesWindow.LocaleChangesButton", "Locale Changes"));
	}
	
	private void checkUpdates() {
		currentVer.setText(version);
		if (gitVersion == null) {
			showNotificationErrorPersistent("Version Error", "Unable to find latest GIT version, check network connection!");
			return;
		}
		
		if (!version.equals(gitVersion)) {
			recentVer.setText(Objects.requireNonNullElse(gitVersion, localization.getLocalizedMessage("Desktop.NewVersionAvailable", "New Version Available!")));
			recentVer.setStyle("-fx-text-fill: red;");
			showNotificationError("Update Available", localization.getLocalizedMessage("Desktop.NewVersionAvailable", "New Version Available!") + " " + gitVersion + ". You can try autoupdating!");
			updateAvailable = true;
		} else {
			recentVer.setText(gitVersion);
		}
	}
	
	@javafx.fxml.FXML
	public void LaunchUpdater(ActionEvent actionEvent) {
		if (updateAvailable) {
			CustomWindow updaterToolWindow = WindowManager.createCustomWindow(mainDesktopControllerObj.getDesktopContainer(), "Windows/Misc/autoUpdater-tool.fxml", "AutoUpdate Utility", true, 2, true, true, mainDesktopControllerObj.getTaskBarApps(), new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Logo.png"))));
		}
	}
	
	@javafx.fxml.FXML
	public void localeChangesClick(ActionEvent actionEvent) {
		String changes = updateStrings.localeChanges;
		
		showChangesWindow(changes);
	}
	
	private void showChangesWindow(String changes) {
		Stage stage = new Stage();
		stage.setTitle("Locale Changes");
		stage.initModality(Modality.APPLICATION_MODAL);
		
		VBox vbox = new VBox();
		vbox.setSpacing(15);
		
		String[] lines = changes.split("\n");
		
		StringBuilder currentText = new StringBuilder();
		boolean inVersionSection = false;
		
		for (String line : lines) {
			line = line.trim();
			
			if (line.toLowerCase().startsWith("version:")) {
				if (inVersionSection) {
					TextArea textArea = new TextArea(currentText.toString());
					textArea.setWrapText(false);
					textArea.setEditable(false);
					textArea.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
					vbox.getChildren().add(textArea);
					textArea.setMinHeight(500);
					VBox.setVgrow(textArea, Priority.ALWAYS);
					currentText.setLength(0);
				}
				
				if (!line.toLowerCase().startsWith("version: " + version.toLowerCase())) {
					Separator separator = new Separator();
					separator.setOrientation(Orientation.HORIZONTAL);
					separator.setStyle("-fx-padding: 40 0 40 0;");
					vbox.getChildren().add(separator);
				}
				
				Label versionLabel = new Label(line);
				versionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");
				vbox.getChildren().add(versionLabel);
				
				inVersionSection = true;
			} else {
				if (inVersionSection) {
					currentText.append(line).append("\n");
				}
			}
		}
		
		if (currentText.length() > 0) {
			TextArea textArea = new TextArea(currentText.toString());
			textArea.setWrapText(true);
			textArea.setEditable(false);
			textArea.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
			vbox.getChildren().add(textArea);
			textArea.setMinHeight(500);
			VBox.setVgrow(textArea, Priority.ALWAYS);
		}
		
		ScrollPane scrollPane = new ScrollPane(vbox);
		scrollPane.setStyle("-fx-padding: 30;");
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);
		
		Scene scene = new Scene(scrollPane, 600, 500);
		stage.setScene(scene);
		
		stage.setFullScreen(false);
		stage.setAlwaysOnTop(true);
		stage.showAndWait();
	}
}
