package com.drozal.dataterminal;

import com.drozal.dataterminal.util.Report.reportUtil;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Region;

import static com.drozal.dataterminal.NotesViewController.codesStage;

public class CodesWindow {
    AnchorPane topBar;
    Boolean isToggled = false;
    @javafx.fxml.FXML
    private BorderPane root;
    @javafx.fxml.FXML
    private ColumnConstraints usageColumn;

    public void initialize() {
        topBar = reportUtil.createSimpleTitleBar("Notepad Codes", true);
        root.setTop(topBar);
        usageColumn.setMaxWidth(0);
    }

    @javafx.fxml.FXML
    public void helpbtnpress() {
        if (!isToggled) {
            usageColumn.setMaxWidth(Region.USE_COMPUTED_SIZE);
            codesStage.setWidth(650);
            codesStage.setMaxWidth(650);
            isToggled = true;
        } else {
            usageColumn.setMaxWidth(0);
            codesStage.setWidth(333);
            codesStage.setMaxWidth(650);
            isToggled = false;
        }
    }
}
