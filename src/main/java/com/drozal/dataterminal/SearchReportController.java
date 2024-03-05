package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.logs.Search.SearchLogEntry;
import com.drozal.dataterminal.logs.Search.SearchReportLogs;
import com.drozal.dataterminal.util.dropdownInfo;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.List;

import static com.drozal.dataterminal.DataTerminalHomeApplication.*;

public class SearchReportController {
    private double xOffset = 0;
    private double yOffset = 0;

    public Spinner SearchNumber;
    public TextField searchedPersons;
    public TextField searchDate;
    public TextField searchTime;
    public TextArea searchSeizedItems;
    public TextField searchGrounds;
    public ComboBox searchType;
    public ComboBox searchMethod;
    public TextField searchWitnesses;
    public TextField officerRank;
    public TextField officerName;
    public TextField officerNumber;
    public TextField officerAgency;
    public TextField officerDivision;
    public TextField searchStreet;
    public TextField searchArea;
    public TextField searchCounty;
    public TextArea searchComments;


    boolean hasEntered = false;
    public VBox vbox;

    public void onMouseEntered(MouseEvent mouseEvent) throws IOException {
        if (hasEntered) {

        } else {
            String name = ConfigReader.configRead("Name");
            String division = ConfigReader.configRead("Division");
            String rank = ConfigReader.configRead("Rank");
            String number = ConfigReader.configRead("Number");
            String agency = ConfigReader.configRead("Agency");

            searchType.getItems().addAll(dropdownInfo.searchTypes);
            searchMethod.getItems().addAll(dropdownInfo.searchMethods);

            officerName.setText(name);
            officerDivision.setText(division);
            officerRank.setText(rank);
            officerAgency.setText(agency);
            officerNumber.setText(number);

            createSpinner(SearchNumber, 0, 999, 0);
            System.out.println("setting");
            searchTime.setText(getTime());
            searchDate.setText(getDate());

        }
    }

    public void onMouseExit(MouseEvent mouseEvent) {
        hasEntered=true;
    }

    public void onSearchReportSubmitBtnClick(ActionEvent actionEvent) {
        // Load existing logs from XML
        SearchReportLogs searchReportLogs = new SearchReportLogs();
        List<SearchLogEntry> logs = SearchReportLogs.loadLogsFromXML();

// Add new entry
        logs.add(new SearchLogEntry(
                SearchNumber.getValue().toString(),
                searchedPersons.getText(),
                searchDate.getText(),
                searchTime.getText(),
                searchSeizedItems.getText(),
                searchGrounds.getText(),
                searchType.getValue().toString(),
                searchMethod.getValue().toString(),
                searchWitnesses.getText(),
                officerRank.getText(),
                officerName.getText(),
                officerNumber.getText(),
                officerAgency.getText(),
                officerDivision.getText(),
                searchStreet.getText(),
                searchArea.getText(),
                searchCounty.getText(),
                searchComments.getText()
        ));

// Save logs to XML
        SearchReportLogs.saveLogsToXML(logs);

// Close the stage
        Stage stage = (Stage) vbox.getScene().getWindow();
        stage.close();

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
        Window window = vbox.getScene().getWindow();

        // Close the window
        window.hide(); // or window.close() if you want to force close
    }
}
