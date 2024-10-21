package com.drozal.dataterminal.Windows.Misc;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Region;

import static com.drozal.dataterminal.Windows.Other.NotesViewController.codesWindow;

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

    public void initialize() {
        sidePane.setVisible(false);
        usageColumn.setMaxWidth(0);
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
