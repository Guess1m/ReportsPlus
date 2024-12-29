package com.Guess.ReportsPlus.Windows.Misc;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Region;

import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.codesWindow;

public class CodesWindow {
	Boolean isToggled = false;
	@javafx.fxml.FXML
	private BorderPane root;
	@javafx.fxml.FXML
	private ColumnConstraints usageColumn;
	@javafx.fxml.FXML
	private AnchorPane sidePane;
	@javafx.fxml.FXML
	private AnchorPane codesPane;
	@javafx.fxml.FXML
	private Label variantslbl;
	@javafx.fxml.FXML
	private Label codelbl;
	@javafx.fxml.FXML
	private Label usagelbl;
	@javafx.fxml.FXML
	private Label examplelbl;
	@javafx.fxml.FXML
	private Label lbl2;
	
	public void initialize() {
		sidePane.setVisible(false);
		usageColumn.setMaxWidth(0);
		
		variantslbl.setText(localization.getLocalizedMessage("CodesWindow.VariantsLabel", "VARIANTS:"));
		codelbl.setText(localization.getLocalizedMessage("CodesWindow.CodeLabel", "CODE:"));
		usagelbl.setText(localization.getLocalizedMessage("CodesWindow.UsageLabel", "USAGE:"));
		examplelbl.setText(localization.getLocalizedMessage("CodesWindow.ExampleLabel", "Example Usage:"));
		lbl2.setText(localization.getLocalizedMessage("CodesWindow.Explanation", "Use -{code} to automatically pull the values from your notes to reports with the \"Pull From Notes\" button on the report windows. "));
	}
	
	@javafx.fxml.FXML
	public void helpbtnpress() {
		if (!isToggled) {
			usageColumn.setMaxWidth(Region.USE_COMPUTED_SIZE);
			codesWindow.getWindowPane().setPrefWidth(650);
			codesWindow.getWindowPane().setMaxWidth(650);
			sidePane.setVisible(true);
			isToggled = true;
		} else {
			usageColumn.setMaxWidth(0);
			codesWindow.getWindowPane().setPrefWidth(333);
			codesWindow.getWindowPane().setMaxWidth(650);
			sidePane.setVisible(false);
			isToggled = false;
		}
	}
}
