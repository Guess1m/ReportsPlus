package com.drozal.dataterminal.Windows.Settings;

import com.drozal.dataterminal.util.Report.reportUtil;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class ProbabilitySettingsController {
    @javafx.fxml.FXML
    private BorderPane root;
    AnchorPane topBar;
    @javafx.fxml.FXML
    private Label main1;

    public void initialize() {
        topBar = reportUtil.createSimpleTitleBar("Probability Settings", true);

        root.setTop(topBar);

    }

    @javafx.fxml.FXML
    public void resetDefaultsBtn(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void saveBtn(ActionEvent actionEvent) {
    }
}
