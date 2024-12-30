package com.Guess.ReportsPlus.Windows.Misc;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager;
import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.Guess.ReportsPlus.util.Misc.stringUtil;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.*;

import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.MainApplication.mainDesktopControllerObj;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationError;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationErrorPersistent;
import static com.Guess.ReportsPlus.util.Misc.stringUtil.version;
import static com.Guess.ReportsPlus.util.Misc.updateUtil.gitVersion;

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
		
		updates.addAll(List.of(stringUtil.updatesList));
		
		updates.forEach(string -> {
			Label label = new Label("* " + string);
			label.setMinWidth(Region.USE_PREF_SIZE);
			label.setStyle("-fx-text-fill:  #5A72A0; -fx-font-family: \"Segoe UI\";");
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
		
		updateBtn.setText(localization.getLocalizedMessage("UpdatesWindow.UpdateButton", "Launch Update Utility (BETA)"));
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
			CustomWindow updaterToolWindow = WindowManager.createCustomWindow(mainDesktopControllerObj.getDesktopContainer(), "Windows/Misc/autoUpdater-tool.fxml", "AutoUpdate Utility", true, 2, true, true, mainDesktopControllerObj.getTaskBarApps(),
			                                                                  new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Logo.png"))));
		}
	}
	
	@javafx.fxml.FXML
	public void localeChangesClick(ActionEvent actionEvent) {
		String changes = stringUtil.localeChanges;
		
		Map<String, Map<String, String>> categorizedChanges = parseAndCategorizeChanges(changes);
		
		StringBuilder formattedChanges = new StringBuilder();
		
		formattedChanges.append("NEW:\n");
		categorizedChanges.get("NEW").forEach((key, value) -> formattedChanges.append(key).append(" = ").append(value).append("\n"));
		
		formattedChanges.append("\nREMOVED:\n");
		categorizedChanges.get("REMOVED").forEach((key, value) -> formattedChanges.append(key).append("\n")); // No value for removed items
		
		formattedChanges.append("\nCHANGED:\n");
		categorizedChanges.get("CHANGED").forEach((key, value) -> formattedChanges.append(key).append(" -> ").append(value).append("\n"));
		
		showChangesWindow(formattedChanges.toString());
	}
	
	private Map<String, Map<String, String>> parseAndCategorizeChanges(String changes) {
		Map<String, Map<String, String>> categorizedChanges = new LinkedHashMap<>();
		categorizedChanges.put("NEW", new LinkedHashMap<>());
		categorizedChanges.put("REMOVED", new LinkedHashMap<>());
		categorizedChanges.put("CHANGED", new LinkedHashMap<>());
		
		String currentCategory = null;
		
		String[] lines = changes.split("\n");
		for (String line : lines) {
			line = line.trim();
			if (line.isEmpty()) {
				continue;
			}
			
			if (line.equalsIgnoreCase("NEW:")) {
				currentCategory = "NEW";
			} else if (line.equalsIgnoreCase("REMOVED:")) {
				currentCategory = "REMOVED";
			} else if (line.equalsIgnoreCase("CHANGED:")) {
				currentCategory = "CHANGED";
			} else if (currentCategory != null) {
				if (currentCategory.equals("NEW") || currentCategory.equals("CHANGED")) {
					String[] parts = line.split("=", 2);
					String key = parts[0].trim();
					String value = parts.length > 1 ? parts[1].trim() : "";
					categorizedChanges.get(currentCategory).put(key, value);
				} else if (currentCategory.equals("REMOVED")) {
					categorizedChanges.get("REMOVED").put(line, "");
				}
			}
		}
		
		return categorizedChanges;
	}
	
	private void showChangesWindow(String changes) {
		Stage stage = new Stage();
		stage.setTitle("Locale Changes");
		stage.initModality(Modality.APPLICATION_MODAL);
		
		TextArea textArea = new TextArea(changes);
		textArea.setWrapText(true);
		textArea.setEditable(false);
		
		VBox vbox = new VBox(textArea);
		VBox.setVgrow(textArea, javafx.scene.layout.Priority.ALWAYS);
		
		Scene scene = new Scene(vbox, 600, 500);
		stage.setScene(scene);
		
		stage.setFullScreen(false);
		stage.setAlwaysOnTop(true);
		stage.showAndWait();
	}
	
}
