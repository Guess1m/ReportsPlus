package com.drozal.dataterminal.Windows.Misc;

import com.drozal.dataterminal.util.Misc.LogUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

public class OutputViewController {
	@FXML
	private ListView listView;
	@FXML
	private BorderPane root;
	
	public void initialize() {
		LogUtils.addOutputToListview(listView);
	}
}