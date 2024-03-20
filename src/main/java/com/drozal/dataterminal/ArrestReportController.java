package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.logs.Arrest.ArrestLogEntry;
import com.drozal.dataterminal.logs.Arrest.ArrestReportLogs;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.drozal.dataterminal.DataTerminalHomeApplication.*;
import static com.drozal.dataterminal.util.treeViewUtils.*;

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
    public TextArea arrestDetails;
    public TextField officerRank;
    public TextField officerName;
    public TextField officerNumber;
    public TextField officerDivision;
    public TextField officerAgency;
    public Label incompleteLabel;
    public TextField arresteeHomeAddress;
    public Button popOverBtn;
    public TreeView treeView;
    public TextField chargeField;
    public TextField probationChance;
    public TextField minYears;
    public TextField maxYears;
    public TextField minMonths;
    public TextField maxMonths;
    public Pane trafficChargeInfoPane;
    public TextField minSuspension;
    public TextField maxSuspension;
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
        popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_RIGHT);

        return popOver;
    }

    public void initialize() throws IOException, ParserConfigurationException, SAXException {
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

        //Tree View
        File file = new File("data/testXML.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = factory.newDocumentBuilder().parse(file);

        Element root = document.getDocumentElement();

        TreeItem<String> rootItem = new TreeItem<>(root.getNodeName());

        parseTreeXML(root, rootItem);
        treeView.setRoot(rootItem);
        expandTreeItem(rootItem, "Root");
        trafficChargeInfoPane.setVisible(false);
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
            if (medicalInformationController.getTaserNo().isSelected()) {
                TaserYesNo = "no";
            } else {
                TaserYesNo = "yes";
            }
            if (medicalInformationController.getAmbulanceNo().isSelected()) {
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
        controller.getOwnerAge().setText(arresteeAge.getText());
        controller.getOwnerGender().setText(arresteeGender.getText());
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

    public void onTreeViewMouseClick(MouseEvent mouseEvent) {
        TreeItem<String> selectedItem = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
        if (selectedItem != null && selectedItem.isLeaf()) {
            chargeField.setText(selectedItem.getValue());
            // TODO: probably dont use probation chance
            probationChance.setText(findXMLValue(selectedItem.getValue(), "probation_chance", "data/testXML.xml"));
            minYears.setText(findXMLValue(selectedItem.getValue(), "min_years", "data/testXML.xml"));
            maxYears.setText(findXMLValue(selectedItem.getValue(), "max_years", "data/testXML.xml"));
            minMonths.setText(findXMLValue(selectedItem.getValue(), "min_months", "data/testXML.xml"));
            maxMonths.setText(findXMLValue(selectedItem.getValue(), "max_months", "data/testXML.xml"));
            minSuspension.setText(findXMLValue(selectedItem.getValue(), "min_susp", "data/testXML.xml"));
            maxSuspension.setText(findXMLValue(selectedItem.getValue(), "max_susp", "data/testXML.xml"));

            // TODO: check if the textfield text contains (m) or (f) then put misdemeanor or felony in another text-field
        }
    }
}