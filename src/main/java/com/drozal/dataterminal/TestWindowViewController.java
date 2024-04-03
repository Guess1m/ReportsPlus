package com.drozal.dataterminal;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.util.Random;

public class TestWindowViewController {

    @javafx.fxml.FXML
    private AnchorPane parent;
    @javafx.fxml.FXML
    private TableView<String[]> table; // TableView<String[]> instead of TableView<String>
    @javafx.fxml.FXML
    private TableColumn<String[], String> caseNumCol; // TableColumn<String[], String> instead of TableColumn
    @javafx.fxml.FXML
    private TableColumn<String[], String> dateCol; // TableColumn<String[], String> instead of TableColumn
    @javafx.fxml.FXML
    private TableColumn<String[], String> outcomeCol; // TableColumn<String[], String> instead of TableColumn
    @javafx.fxml.FXML
    private TableColumn<String[], String> nameCol; // TableColumn<String[], String> instead of TableColumn

    public void initialize() {
        // Set cell value factories for each column
        caseNumCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));
        dateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));
        outcomeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[3]));

        // Add data to the table
        addDataToTable();
    }

    // Method to add data to the table
    private void addDataToTable() {
        ObservableList<String[]> data = FXCollections.observableArrayList();
        for (int t = 0; t < 50; t++) {
            String[] rowData = new String[4];
            rowData[0] = generateCaseNum(); // Case number
            rowData[1] = generateRandomDate(); // Date
            rowData[2] = "Some outcome"; // Outcome (example value)
            rowData[3] = "Some name"; // Name (example value)
            data.add(rowData);
        }
        table.setItems(data);
    }

    // Method to generate a random date in the format "00/00/0000"
    private String generateRandomDate() {
        Random random = new Random();

        // Generate random day (01 to 31)
        int day = random.nextInt(31) + 1;
        // Generate random month (01 to 12)
        int month = random.nextInt(12) + 1;
        // Generate random year (1900 to 2099)
        int year = random.nextInt(200) + 1900;

        // Return the date in the format "00/00/0000"
        return String.format("%02d/%02d/%04d", day, month, year);
    }

    // Method to generate a random case number
    private String generateCaseNum() {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        stringBuilder.append("CN-");
        for (int i = 0; i < 10; i++) {
            if (random.nextBoolean()) {
                stringBuilder.append((char) ('a' + random.nextInt(26))); // Append a random letter
            } else {
                stringBuilder.append(random.nextInt(10)); // Append a random digit between 0 and 9
            }
        }
        return stringBuilder.toString();
    }
}
