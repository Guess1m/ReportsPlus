package com.drozal.dataterminal;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

public class NotesViewController {

    @javafx.fxml.FXML
    private TextArea notepadTextArea;
    @javafx.fxml.FXML
    private ImageView exitButton;
    @javafx.fxml.FXML
    private AnchorPane notesPane;
    @javafx.fxml.FXML
    private Button clearbtnnotepad;
    @javafx.fxml.FXML
    private ToggleButton alwaysTopBtn;
    @javafx.fxml.FXML
    private AnchorPane topbar;

    public void initialize() {
        notepadTextArea.setText(actionController.notesText);
    }

    @javafx.fxml.FXML
    public void onclearclick(ActionEvent actionEvent) {
        notepadTextArea.setText("");
    }

    @javafx.fxml.FXML
    public void onExitButtonClick(MouseEvent actionEvent) {
        actionController.notesText = notepadTextArea.getText();
        // Get the window associated with the scene
        Window window = notepadTextArea.getScene().getWindow();

        // Close the window
        window.hide(); // or window.close() if you want to force close
    }

    @javafx.fxml.FXML
    public void onNotepadAlwaysTopBtnClick(ActionEvent actionEvent) {
        Stage stage = (Stage) notesPane.getScene().getWindow();
        stage.setAlwaysOnTop(alwaysTopBtn.isSelected());

    }

    public TextArea getNotepadTextArea() {
        return notepadTextArea;
    }

    public AnchorPane getTopbar() {
        return topbar;
    }
}
