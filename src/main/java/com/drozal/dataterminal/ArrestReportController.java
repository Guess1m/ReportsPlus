package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.logs.Arrest.ArrestLogEntry;
import com.drozal.dataterminal.logs.Arrest.ArrestReportLogs;
import com.drozal.dataterminal.util.dropdownInfo;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.util.List;

import static com.drozal.dataterminal.DataTerminalHomeApplication.*;

public class ArrestReportController {

    public VBox vbox;
    public Spinner arrestNumber;
    public TextField arrestDate;
    public TextField arrestTime;
    public TextField arrestCounty;
    public TextField arrestArea;
    public TextField arrestStreet;
    public TextField arresteeName;
    public TextField arresteeAge;
    public TextField arresteeGender;
    public TextField arresteeEthnicity;
    public TextField arresteeDescription;
    public TextArea arresteeMedicalInformation;
    public TextArea arrestDetails;
    public TextField officerRank;
    public TextField officerName;
    public TextField officerNumber;
    public TextField officerDivision;
    public TextField officerAgency;
    public Label incompleteLabel;
    public TextField arresteeHomeAddress;
    public Button popOverBtn;
    private double xOffset = 0;
    private double yOffset = 0;
    private MedicalInformation medicalInformationController;

    private PopOver popOver() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("popOvers/medicalInformation.fxml"));
        Parent root = loader.load();
        medicalInformationController = loader.getController();

        // Create a PopOver and set the content node
        PopOver popOver = new PopOver();
        popOver.setContentNode(root);

        // Optionally configure other properties of the PopOver
        popOver.setDetachable(false);
        popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_LEFT);

        return popOver;
    }

    public void initialize() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("popOvers/medicalInformation.fxml"));
        loader.load();
        medicalInformationController = loader.getController();

        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");
        officerName.setText(name);
        officerDivision.setText(division);
        officerRank.setText(rank);
        officerAgency.setText(agency);
        officerNumber.setText(number);
        createSpinner(arrestNumber, 0, 9999, 0);
        arrestTime.setText(getTime());
        arrestDate.setText(getDate());
    }

    public void onArrestReportSubmitBtnClick(ActionEvent actionEvent) {

        if (arrestNumber.getValue() == null) {
            incompleteLabel.setText("Fill Out Form.");
            incompleteLabel.setStyle("-fx-text-fill: red;");
            incompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                incompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {
            // Load existing logs from XML
            List<ArrestLogEntry> logs = ArrestReportLogs.loadLogsFromXML();
            String TaserYesNo;
            String AmbulanceYesNo;
            if (medicalInformationController.getTaserNo().isSelected()){
                TaserYesNo = "no";
            } else {
                TaserYesNo = "yes";
            }
            if (medicalInformationController.getAmbulanceNo().isSelected()){
                AmbulanceYesNo = "no";
            } else {
                AmbulanceYesNo = "yes";
            }
            // Add new entry
            logs.add(new ArrestLogEntry(
                    arrestNumber.getValue().toString(),
                    arrestDate.getText(),
                    arrestTime.getText(),
                    arrestCounty.getText(),
                    arrestArea.getText(),
                    arrestStreet.getText(),
                    arresteeName.getText(),
                    arresteeAge.getText(),
                    arresteeGender.getText(),
                    arresteeEthnicity.getText(),
                    arresteeDescription.getText(),
                    AmbulanceYesNo,
                    TaserYesNo,
                    medicalInformationController.getArresteeMedicalInformation().getText(),
                    arresteeHomeAddress.getText(),
                    arrestDetails.getText(),
                    officerRank.getText(),
                    officerName.getText(),
                    officerNumber.getText(),
                    officerDivision.getText(),
                    officerAgency.getText()
            ));
            // Save logs to XML
            ArrestReportLogs.saveLogsToXML(logs);
            // Close the stage
            Stage stage = (Stage) vbox.getScene().getWindow();
            stage.close();
        }
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
        Window window = vbox.getScene().getWindow();
        window.hide();
    }

    public void impoundBtnClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("impoundReport-view.fxml"));
        Parent root = loader.load();
        ImpoundReportController controller = loader.getController();
        Scene newScene = new Scene(root);
        stage.setTitle("Impound Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
        stage.setY(stage.getY() * 3f / 2f);

        // TODO: add all the values transferring over | Including the officer name, event location, date/time, etc. in case they changed it during arrest report
        controller.getImpoundDate().setText(arrestDate.getText());
        controller.getImpoundTime().setText(arrestTime.getText());

        int arrestNumberValue = (int) arrestNumber.getValue(); // Extract the value from the source Spinner
        controller.getImpoundNumber().getValueFactory().setValue(arrestNumberValue); // Set the value to the target Spinner's value factory


        controller.getOfficerAgency().setText(officerAgency.getText());
        controller.getOfficerName().setText(officerName.getText());
        controller.getOfficerDivision().setText(officerDivision.getText());
        controller.getOfficerRank().setText(officerRank.getText());
        controller.getOfficerNumber().setText(officerNumber.getText());
        controller.getOwnerName().setText(arresteeName.getText());
        controller.getOwnerAddress().setText(arresteeHomeAddress.getText());
    }

    public void searchBtnClick(ActionEvent actionEvent) {
    }

    public void incidentBtnClick(ActionEvent actionEvent) {
    }

    public void onPopOverBtnPress(ActionEvent actionEvent) throws IOException {
        PopOver popOver = popOver();
        if (popOver != null) {
            popOver.show(popOverBtn);
        } else {
            System.err.println("PopOver creation failed.");
        }
    }

}
