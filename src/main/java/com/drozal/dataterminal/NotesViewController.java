package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.reportCreationUtil;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class NotesViewController {

    public static AnchorPane titlebar = null;

    @javafx.fxml.FXML
    private TextArea notepadTextArea;
    @javafx.fxml.FXML
    private Button clearbtnnotepad;
    @javafx.fxml.FXML
    private BorderPane borderPane;
    @javafx.fxml.FXML
    private ToggleButton modeToggle;

    public AnchorPane getTitlebar() {
        return titlebar;
    }

    public void initialize() throws IOException {
        if (ConfigReader.configRead("notepadMode") != null) {
            if (ConfigReader.configRead("notepadMode").equals("Dark")) {
                notepadTextArea.setStyle("-fx-background-color: #666666; -fx-text-fill: white; -fx-border-color: transparent;");
                modeToggle.setStyle("-fx-background-color: white;");
            } else {
                notepadTextArea.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-border-color: transparent;");
                modeToggle.setStyle("-fx-background-color: grey;");
            }
        }

        String hoverStyle = "-fx-background-color: " + ConfigReader.configRead("mainColor");
        String initialStyle = "-fx-background-color: " + ConfigReader.configRead("accentColor");

        clearbtnnotepad.setStyle(initialStyle);

        clearbtnnotepad.setOnMouseEntered(e -> clearbtnnotepad.setStyle(hoverStyle));
        clearbtnnotepad.setOnMouseExited(e -> clearbtnnotepad.setStyle(initialStyle));

        titlebar = reportCreationUtil.createTitleBar("NotePad");

        borderPane.setTop(titlebar);

        notepadTextArea.setText(actionController.notesText);
    }

    @javafx.fxml.FXML
    public void onclearclick(ActionEvent actionEvent) {
        notepadTextArea.setText("");
    }


    public TextArea getNotepadTextArea() {
        return notepadTextArea;
    }

    @javafx.fxml.FXML
    public void onDarkModeToggle(ActionEvent actionEvent) {
        if (modeToggle.isSelected()) {
            ConfigWriter.configwrite("notepadMode", "Dark");
            notepadTextArea.setStyle("-fx-background-color: #666666; -fx-text-fill: white; -fx-border-color: transparent;");
            modeToggle.setStyle("-fx-background-color: white;");
        } else {
            ConfigWriter.configwrite("notepadMode", "Light");
            notepadTextArea.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-border-color: transparent;");
            modeToggle.setStyle("-fx-background-color: grey;");
        }
    }
}
