package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.util.Report.reportCreationUtil;
import com.drozal.dataterminal.util.server.ClientUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientController {
    private static AnchorPane titleBar;
    @javafx.fxml.FXML
    private TextField inetField;
    @javafx.fxml.FXML
    private BorderPane root;
    @javafx.fxml.FXML
    private TextField portField;
    @javafx.fxml.FXML
    private TextField inputPortField;
    @javafx.fxml.FXML
    private TextField inputHostField;
    @javafx.fxml.FXML
    private Label statusLabel;

    public Label getStatusLabel() {
        return statusLabel;
    }

    public TextField getInetField() {
        return inetField;
    }

    public TextField getPortField() {
        return portField;
    }

    public void initialize() throws IOException {
        titleBar = reportCreationUtil.createSimpleTitleBar("Client Interface", false);

        root.setTop(titleBar);

        if (ConfigReader.configRead("lastIPV4Connection") != null) {
            inputHostField.setText(ConfigReader.configRead("lastIPV4Connection"));
        }
        if (ConfigReader.configRead("lastPortConnection") != null) {
            inputPortField.setText(ConfigReader.configRead("lastPortConnection"));
        }
    }

    @javafx.fxml.FXML
    public void connectBtnPress(ActionEvent actionEvent) throws IOException {
        ClientUtils.connectToService(inputHostField.getText(), Integer.parseInt(inputPortField.getText()));
    }

    @javafx.fxml.FXML
    public void helpBtnPress(ActionEvent actionEvent) {
        Stage stage = (Stage) root.getScene().getWindow();
        showHelpDialog(stage);
    }

    /**
     * Shows a dialog with help information for the user.
     * This method creates an Alert of type INFORMATION and sets its content text to provide
     * instructions and descriptions of various features in the application.
     */
    /**
     * Displays a help dialog with instructions on how to use the client.
     * This method ensures that the dialog appears on top of all other windows, including those set as always on top.
     *
     * @param owner the primary stage of the application
     */
    private void showHelpDialog(Stage owner) {
        Alert helpDialog = new Alert(Alert.AlertType.INFORMATION);
        helpDialog.setTitle("Help");
        helpDialog.setHeaderText("How To Use The Client");
        helpDialog.setContentText(
                "1. The top left field is the port you would like to connect to use to connect to the server. This must match the port that the server was started with.\n\n"
                        + "2. The bottom left field is the internal IP to connect to. This number needs to match the internal IP of the server.\n\n"
                        + "3. The top right field is the internal IP of the server the client is connected to.\n\n"
                        + "4. The bottom right field is the port of the established connection.\n\n");

        // Ensure the dialog is on top of all other windows
        helpDialog.initOwner(owner);
        helpDialog.initModality(Modality.APPLICATION_MODAL);

        helpDialog.showAndWait();
    }


}
