package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.Report.reportUtil;
import com.drozal.dataterminal.util.Window.windowUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class NotesViewController {
	
	public static AnchorPane titlebar = null;
	public static Stage codesStage = null;
	@javafx.fxml.FXML
	private TextArea notepadTextArea;
	@javafx.fxml.FXML
	private Button clearbtnnotepad;
	@javafx.fxml.FXML
	private BorderPane borderPane;
	@javafx.fxml.FXML
	private ToggleButton modeToggle;
	@javafx.fxml.FXML
	private AnchorPane notesPane;
	@javafx.fxml.FXML
	private Button codesbtnnotepad;
	
	public AnchorPane getTitlebar() {
		return titlebar;
	}
	
	public void initialize() throws IOException {
		String hoverStyle = "-fx-background-color: " + ConfigReader.configRead("mainColor") + ";";
		String initialStyle = "-fx-background-color: " + ConfigReader.configRead("accentColor") + ";";
		String padding = " -fx-padding: 2 7 2 7;";
		
		clearbtnnotepad.setStyle(initialStyle + padding);
		clearbtnnotepad.setOnMouseEntered(e -> clearbtnnotepad.setStyle(hoverStyle + padding));
		clearbtnnotepad.setOnMouseExited(e -> clearbtnnotepad.setStyle(initialStyle + padding));
		
		codesbtnnotepad.setStyle(initialStyle + padding);
		codesbtnnotepad.setOnMouseEntered(e -> codesbtnnotepad.setStyle(hoverStyle + padding));
		codesbtnnotepad.setOnMouseExited(e -> codesbtnnotepad.setStyle(initialStyle + padding));
		
		titlebar = reportUtil.createTitleBar("NotePad");
		borderPane.setTop(titlebar);
		
		notepadTextArea.setText(actionController.notesText);
		
		String notepadMode = ConfigReader.configRead("notepadMode");
		if (notepadMode != null) {
			String notepadStyle;
			String modeToggleStyle;
			if (notepadMode.equals("Dark")) {
				notepadStyle = "-fx-background-color: #666666; -fx-text-fill: white; -fx-border-color: transparent; -fx-background-radius: 0; -fx-border-radius: 0;";
				modeToggleStyle = "-fx-background-color: white;";
			} else {
				notepadStyle = "-fx-background-color: white; -fx-text-fill: black; -fx-border-color: transparent; -fx-background-radius: 0; -fx-border-radius: 0;";
				modeToggleStyle = "-fx-background-color: grey;";
			}
			notepadTextArea.setStyle(notepadStyle);
			modeToggle.setStyle(modeToggleStyle);
		} else {
			notepadTextArea.setStyle(
					"-fx-background-color: white; -fx-text-fill: black; -fx-border-color: transparent; -fx-background-radius: 0; -fx-border-radius: 0;");
			modeToggle.setStyle("-fx-background-color: grey;");
		}
	}
	
	@javafx.fxml.FXML
	public void onclearclick(ActionEvent actionEvent) {
		notepadTextArea.setText("");
	}
	
	public TextArea getNotepadTextArea() {
		return notepadTextArea;
	}
	
	@javafx.fxml.FXML
	public void onDarkModeToggle(ActionEvent actionEvent) {
		if (modeToggle.isSelected()) {
			ConfigWriter.configwrite("notepadMode", "Dark");
			notepadTextArea.setStyle(
					"-fx-background-color: #666666; -fx-text-fill: white; -fx-border-color: transparent;");
			modeToggle.setStyle("-fx-background-color: white;");
		} else {
			ConfigWriter.configwrite("notepadMode", "Light");
			notepadTextArea.setStyle(
					"-fx-background-color: white; -fx-text-fill: black; -fx-border-color: transparent;");
			modeToggle.setStyle("-fx-background-color: grey;");
		}
	}
	
	@javafx.fxml.FXML
	public void oncodesclick(ActionEvent actionEvent) throws IOException {
		if (codesStage != null && codesStage.isShowing()) {
			codesStage.close();
			codesStage = null;
			return;
		}
		codesStage = new Stage();
		codesStage.initStyle(StageStyle.UNDECORATED);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("codes-window.fxml"));
		Parent root = loader.load();
		CodesWindow codesViewController = loader.getController();
		Scene newScene = new Scene(root);
		codesStage.setTitle("Codes");
		codesStage.setScene(newScene);
		codesStage.setAlwaysOnTop(true);
		
		codesStage.show();
		
		windowUtils.centerStageOnMainApp(codesStage);
		
		codesStage.setOnHidden(event -> codesStage = null);
	}
}
