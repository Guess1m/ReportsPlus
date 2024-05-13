package com.drozal.dataterminal;

import com.drozal.dataterminal.util.reportCreationUtil;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class ReportManagerViewController {
    public static AnchorPane titlebar = null;
    @javafx.fxml.FXML
    private BorderPane root;

    public static AnchorPane getTitlebar() {
        return titlebar;
    }

    public void initialize() {
        titlebar = reportCreationUtil.createSimpleTitleBar("Report Manager");

        root.setTop(titlebar);
    }

}
