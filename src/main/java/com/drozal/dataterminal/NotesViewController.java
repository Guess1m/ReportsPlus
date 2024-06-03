package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.Report.reportCreationUtil;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

/**
 * The type Notes view controller.
 */
public class NotesViewController {
	
	/**
	 * The constant titlebar.
	 */
	public static AnchorPane titlebar = null;
	
	@javafx.fxml.FXML
	private TextArea notepadTextArea;
	@javafx.fxml.FXML
	private Button clearbtnnotepad;
	@javafx.fxml.FXML
	private BorderPane borderPane;
	@javafx.fxml.FXML
	private ToggleButton modeToggle;
	
	/**
	 * Gets titlebar.
	 *
	 * @return the titlebar
	 */
	public AnchorPane getTitlebar() {
		return titlebar;
	}
	
	/**
	 * Initializes the notepad with the appropriate styles and settings.
	 * This method reads configuration values and applies styles to the notepad text area and mode toggle button.
	 * It also sets up event handlers for hover effects on the clear button.
	 *
	 * @throws IOException if there is an error reading the configuration values
	 */
	public void initialize() throws IOException {
		String hoverStyle = "-fx-background-color: " + ConfigReader.configRead("mainColor");
		String initialStyle = "-fx-background-color: " + ConfigReader.configRead("accentColor");
		
		clearbtnnotepad.setStyle(initialStyle);
		clearbtnnotepad.setOnMouseEntered(e -> clearbtnnotepad.setStyle(hoverStyle));
		clearbtnnotepad.setOnMouseExited(e -> clearbtnnotepad.setStyle(initialStyle));
		
		titlebar = reportCreationUtil.createTitleBar("NotePad");
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
			System.out.println(notepadMode);
		} else {
			notepadTextArea.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-border-color: transparent; -fx-background-radius: 0; -fx-border-radius: 0;");
			modeToggle.setStyle("-fx-background-color: grey;");
		}
	}
	
	/**
	 * Onclearclick.
	 *
	 * @param actionEvent the action event
	 */
	@javafx.fxml.FXML
	public void onclearclick(ActionEvent actionEvent) {
		notepadTextArea.setText("");
	}
	
	/**
	 * Gets notepad text area.
	 *
	 * @return the notepad text area
	 */
	public TextArea getNotepadTextArea() {
		return notepadTextArea;
	}
	
	/**
	 * On dark mode toggle.
	 *
	 * @param actionEvent the action event
	 */
	@javafx.fxml.FXML
	public void onDarkModeToggle(ActionEvent actionEvent) {
		if (modeToggle.isSelected()) {
			ConfigWriter.configwrite("notepadMode", "Dark");
			notepadTextArea.setStyle("-fx-background-color: #666666; -fx-text-fill: white; -fx-border-color: transparent;");
			modeToggle.setStyle("-fx-background-color: white;");
		} else {
			ConfigWriter.configwrite("notepadMode", "Light");
			notepadTextArea.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-border-color: transparent;");
			modeToggle.setStyle("-fx-background-color: grey;");
		}
	}
}
