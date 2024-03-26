package com.drozal.dataterminal;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;

public class NotesViewController {

    @javafx.fxml.FXML
    private TextArea notepadTextArea;
    private double xOffset = 0;
    private double yOffset = 0;

    public void initialize() {
    }

    @javafx.fxml.FXML
    public void onclearclick(ActionEvent actionEvent) {
        notepadTextArea.setText("");
    }

    public void onMouseDrag(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() - xOffset);
        stage.setY(mouseEvent.getScreenY() - yOffset);
    }

    public void onMousePress(MouseEvent mouseEvent) {
        xOffset = mouseEvent.getSceneX();
        yOffset = mouseEvent.getSceneY();
    }

    public void onExitButtonClick(MouseEvent actionEvent) {
        // Get the window associated with the scene
        Window window = notepadTextArea.getScene().getWindow();

        // Close the window
        window.hide(); // or window.close() if you want to force close
    }
}
