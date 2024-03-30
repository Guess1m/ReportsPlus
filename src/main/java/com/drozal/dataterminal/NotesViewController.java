package com.drozal.dataterminal;

import javafx.event.ActionEvent;
import javafx.scene.Node;
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
    private double xOffset = 0;
    private double yOffset = 0;
    @javafx.fxml.FXML
    private ImageView exitButton;
    @javafx.fxml.FXML
    private AnchorPane notesPane;
    @javafx.fxml.FXML
    private Button clearbtnnotepad;
    @javafx.fxml.FXML
    private ToggleButton alwaysTopBtn;

    public void initialize() {
        notepadTextArea.setText(actionController.notesText);
    }

    @javafx.fxml.FXML
    public void onclearclick(ActionEvent actionEvent) {
        notepadTextArea.setText("");
    }

    @javafx.fxml.FXML
    public void onMouseDrag(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() - xOffset);
        stage.setY(mouseEvent.getScreenY() - yOffset);
    }

    @javafx.fxml.FXML
    public void onMousePress(MouseEvent mouseEvent) {
        xOffset = mouseEvent.getSceneX();
        yOffset = mouseEvent.getSceneY();
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
}
