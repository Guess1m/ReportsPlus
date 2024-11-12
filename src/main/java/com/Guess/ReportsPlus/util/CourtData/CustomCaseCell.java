package com.Guess.ReportsPlus.util.CourtData;

import com.Guess.ReportsPlus.config.ConfigReader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;

import static com.Guess.ReportsPlus.util.Misc.controllerUtils.toHexString;

public class CustomCaseCell extends GridPane {
	private final Label caseNumLabel;
	private final Label nameLabel;
	private final Label offenceDateLabel;
	private final Label offenceTimeLabel;
	
	public CustomCaseCell() {
		this.setVgap(3.0);
		this.setPadding(new Insets(2.0, 3.0, 2.0, 3.0));
		
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setHalignment(javafx.geometry.HPos.LEFT);
		col1.setHgrow(Priority.NEVER);
		col1.setMinWidth(10.0);
		col1.setPercentWidth(40.0);
		
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setHalignment(javafx.geometry.HPos.LEFT);
		col2.setHgrow(Priority.ALWAYS);
		col2.setMinWidth(10.0);
		col2.setPercentWidth(60.0);
		
		this.getColumnConstraints().addAll(col1, col2);
		
		RowConstraints row1 = new RowConstraints();
		row1.setMinHeight(10.0);
		row1.setVgrow(Priority.SOMETIMES);
		
		RowConstraints row2 = new RowConstraints();
		row2.setMinHeight(10.0);
		row2.setVgrow(Priority.SOMETIMES);
		
		this.getRowConstraints().addAll(row1, row2);
		
		caseNumLabel = createLabel("", "Segoe UI Black", 12.0, 0, 0);
		nameLabel = createLabel("", "Segoe UI Black", 12.0, 0, 1);
		offenceDateLabel = createLabel("", "Segoe UI Semibold", 12.0, 1, 0);
		offenceTimeLabel = createLabel("", "Segoe UI Semibold", 12.0, 1, 1);
		
		this.getChildren().addAll(caseNumLabel, nameLabel, offenceDateLabel, offenceTimeLabel);
	}
	
	private Label createLabel(String text, String fontName, double fontSize, int rowIndex, int columnIndex) {
		Label label = new Label(text);
		label.setFont(new Font(fontName, fontSize));
		GridPane.setRowIndex(label, rowIndex);
		GridPane.setColumnIndex(label, columnIndex);
		GridPane.setHalignment(label, Pos.CENTER.getHpos());
		GridPane.setValignment(label, Pos.CENTER.getVpos());
		return label;
	}
	
	public void updateCase(Case case1) {
		Color primary = null;
		try {
			primary = Color.valueOf(ConfigReader.configRead("uiColors", "mainColor"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		nameLabel.setStyle("-fx-text-fill: " + toHexString(primary) + ";");
		if (case1 != null) {
			caseNumLabel.setText(case1.getCaseNumber());
			nameLabel.setText(case1.getName());
			offenceDateLabel.setText(case1.getOffenceDate());
			offenceTimeLabel.setText(case1.getCaseTime().replace(".", ""));
		} else {
			caseNumLabel.setText("");
			nameLabel.setText("");
			offenceDateLabel.setText("");
			offenceTimeLabel.setText("");
		}
	}
}
