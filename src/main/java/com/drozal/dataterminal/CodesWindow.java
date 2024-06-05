package com.drozal.dataterminal;

import com.drozal.dataterminal.util.Report.reportCreationUtil;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class CodesWindow {
	AnchorPane topBar;
	@javafx.fxml.FXML
	private AnchorPane codesPane;
	@javafx.fxml.FXML
	private BorderPane root;
	
	public void initialize() {
		topBar = reportCreationUtil.createSimpleTitleBar("Notepad Codes", true);
		root.setTop(topBar);
		
	}
	
}
