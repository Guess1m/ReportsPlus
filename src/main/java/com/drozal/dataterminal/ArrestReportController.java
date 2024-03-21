package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.logs.Arrest.ArrestLogEntry;
import com.drozal.dataterminal.logs.Arrest.ArrestReportLogs;
import com.drozal.dataterminal.logs.ChargesData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    public Pane monthsPane;
    public Pane yearsPane;
    public TableView chargeTable;
    public TableColumn chargeColumn;
    public ScrollPane scrollPane;
    public ScrollPane chargeScrollPane;
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
        popOver.cornerRadiusProperty().setValue(23);
        popOver.setFadeInDuration(Duration.seconds(0.5));
        popOver.setFadeOutDuration(Duration.seconds(0.4));
        popOver.setTitle("Medical Information");
        popOver.setHeaderAlwaysVisible(true);

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
        monthsPane.setVisible(false);

       /* treeView.addEventFilter(ScrollEvent.ANY, event -> {
            // Consume the scroll event to prevent it from reaching the parent scroll pane
            event.consume();
        });
        chargeScrollPane.addEventFilter(ScrollEvent.ANY, event -> {
            // Consume the scroll event to prevent it from reaching the parent scroll pane
            event.consume();
        });*/
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

            ObservableList<ChargesData> formDataList = chargeTable.getItems();
            // StringBuilder to build the string with commas
            StringBuilder stringBuilder = new StringBuilder();
            // Iterate through formDataList to access each FormData object
            for (ChargesData formData : formDataList) {
                // Append the values to the StringBuilder, separated by commas
                stringBuilder.append(formData.getCharge()).append(" | ");
                // Add more values as needed, separated by commas
            }
            // Remove the trailing comma and space
            if (stringBuilder.length() > 0) {
                stringBuilder.setLength(stringBuilder.length() - 2);
            }

            // Add new entry
            logs.add(new ArrestLogEntry(
                    arrestNumber.getValue().toString(),
                    arrestDate.getText(),
                    arrestTime.getText(),
                    stringBuilder.toString(),
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

    public void searchBtnClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("searchReport-view.fxml"));
        Parent root = loader.load();
        SearchReportController controller = loader.getController();
        Scene newScene = new Scene(root);
        stage.setTitle("Search Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
        stage.setY(stage.getY() * 3f / 2f);

        controller.getSearchDate().setText(arrestDate.getText());
        controller.getSearchTime().setText(arrestTime.getText());

        int arrestNumberValue = (int) arrestNumber.getValue(); // Extract the value from the source Spinner
        controller.getSearchNumber().getValueFactory().setValue(arrestNumberValue); // Set the value to the target Spinner's value factory

        controller.getOfficerAgency().setText(officerAgency.getText());
        controller.getOfficerName().setText(officerName.getText());
        controller.getOfficerDivision().setText(officerDivision.getText());
        controller.getOfficerRank().setText(officerRank.getText());
        controller.getOfficerNumber().setText(officerNumber.getText());
        controller.getSearchArea().setText(arrestArea.getText());
        controller.getSearchCounty().setText(arrestCounty.getText());
        controller.getSearchStreet().setText(arrestStreet.getText());
        controller.getSearchedPersons().setText(arresteeName.getText());
    }

    public void incidentBtnClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("incidentReport-view.fxml"));
        Parent root = loader.load();
        IncidentReportController controller = loader.getController();
        Scene newScene = new Scene(root);
        stage.setTitle("Incident Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
        stage.setY(stage.getY() * 3f / 2f);

        controller.getIncidentDate().setText(arrestDate.getText());
        controller.getIncidentTime().setText(arrestTime.getText());

        int arrestNumberValue = (int) arrestNumber.getValue(); // Extract the value from the source Spinner
        controller.getIncidentNumber().getValueFactory().setValue(arrestNumberValue); // Set the value to the target Spinner's value factory

        controller.getOfficerAgency().setText(officerAgency.getText());
        controller.getOfficerName().setText(officerName.getText());
        controller.getOfficerDivision().setText(officerDivision.getText());
        controller.getOfficerRank().setText(officerRank.getText());
        controller.getOfficerNumber().setText(officerNumber.getText());
        controller.getIncidentArea().setText(arrestArea.getText());
        controller.getIncidentCounty().setText(arrestCounty.getText());
        controller.getIncidentStreet().setText(arrestStreet.getText());
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
            if (findXMLValue(selectedItem.getValue(), "min_years", "data/testXML.xml").isBlank()) {
                monthsPane.setVisible(true);
                yearsPane.setVisible(false);
            } else {
                monthsPane.setVisible(false);
                yearsPane.setVisible(true);
            }
            if (findXMLValue(selectedItem.getValue(), "traffic", "data/testXML.xml").matches("true")) {
                minSuspension.setText(findXMLValue(selectedItem.getValue(), "min_susp", "data/testXML.xml"));
                maxSuspension.setText(findXMLValue(selectedItem.getValue(), "max_susp", "data/testXML.xml"));
                trafficChargeInfoPane.setVisible(true);
            } else {
                trafficChargeInfoPane.setVisible(false);
            }

            // TODO: check if the textfield text contains (m) or (f) then put misdemeanor or felony in another text-field
        }
    }

    public void addChargeBtnPress(ActionEvent actionEvent) {
        String charge = chargeField.getText();
        if (!(charge.isBlank() || charge.isEmpty())) {
            // Create a new FormData object
            ChargesData formData = new ChargesData(charge);
            // Add the FormData object to the TableView
            chargeColumn.setCellValueFactory(new PropertyValueFactory<>("charge"));
            chargeTable.getItems().add(formData);
        }
    }

    public void removeChargeBtnPress(ActionEvent actionEvent) {
        // Get the selected item
        ChargesData selectedItem = (ChargesData) chargeTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // Remove the selected item from the TableView
            chargeTable.getItems().remove(selectedItem);
        }
    }
}