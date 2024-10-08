package com.drozal.dataterminal.Windows.Misc;

import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Report.reportUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class OutputViewController {
	private static AnchorPane titlebar = null;
	@FXML
	private ListView listView;
	@FXML
	private BorderPane root;
	
	public void initialize() {
		titlebar = reportUtil.createSimpleTitleBar("Output Log View", true);
		root.setTop(titlebar);
		LogUtils.addOutputToListview(listView);
	}
}