package com.drozal.dataterminal;

import com.drozal.dataterminal.util.LogUtils;
import com.drozal.dataterminal.util.reportCreationUtil;
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

    public static AnchorPane getTitlebar() {
        return titlebar;
    }

    public void initialize() {
        titlebar = reportCreationUtil.createSimpleTitleBar("Output Log View");
        root.setTop(titlebar);
        LogUtils.addOutputToListview(listView);
    }
}