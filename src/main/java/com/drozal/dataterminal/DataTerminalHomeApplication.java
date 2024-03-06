package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.util.stringUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataTerminalHomeApplication extends Application {

    public static String getDate() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentTime.format(formatter);
    }

    public static String getTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        return currentTime.format(formatter);
    }

    public static void main(String[] args) {
        launch();
    }

    public static Spinner<Integer> createSpinner(Spinner spinner, int min, int max, int initialValue) {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initialValue);
        spinner.setValueFactory(valueFactory);
        return spinner;
    }

    @Override
    public void start(Stage stage) throws IOException {

        Font.loadFont(getClass().getResourceAsStream("fonts/seguibl.ttf"), 14);

        mainStage.mainRT = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DataTerminalHome-view.fxml"));
        Parent root = loader.load();

        // Add title label to the custom title bar
        Label titleLabel = new Label("Data Terminal " + stringUtil.version);
        titleLabel.setTextFill(Color.WHITE); // Set text color to black
        titleLabel.setPadding(new Insets(0, 0, 0, 20)); // Adjust left padding as needed
        titleLabel.setFont(Font.font("Segoe UI Black", FontWeight.BOLD, 16)); // Set font to Consolas and bold, adjust size as needed

        VBox content = new VBox(root);
        Scene scene = new Scene(content);


// Set stage transparent and remove default window decorations
        mainStage.mainRT.initStyle(StageStyle.TRANSPARENT);
        mainStage.mainRT.setScene(scene);
        mainStage.mainRT.setResizable(false);
        mainStage.mainRT.show();
        mainStage.mainRT.centerOnScreen();
        mainStage.mainRT.setY(mainStage.mainRT.getY() * 3f / 2f);


        actionController controller = loader.getController();
        StackPane notesPane = controller.getNotesPane();
        StackPane codesPane = controller.getCodesPane();
        Label generatedByTag = controller.getGeneratedByTag();
        Label generatedOnTag = controller.getGeneratedDateTag();
        StackPane shiftInfoPane = controller.getShiftInformationPane();
        StackPane informationPane = controller.getInfoPane();
        TabPane logsPane = controller.getLogPane();

        TextField OfficerInfoName = controller.getOfficerInfoName();
        ComboBox OfficerInfoDivision = controller.getOfficerInfoDivision();
        ComboBox OfficerInfoRank = controller.getOfficerInfoRank();
        TextField OfficerInfoNumber = controller.getOfficerInfoNumber();
        ComboBox OfficerInfoAgency = controller.getOfficerInfoAgency();

        Spinner<Integer> calloutSpinner = controller.getCalloutSpinner();
        Spinner<Integer> searchSpinner = controller.getSearchesSpinner();
        Spinner<Integer> arrestsSpinner = controller.getArrestsSpinner();
        Spinner<Integer> incidentSpinner = controller.getIncidentSpinner();
        Spinner<Integer> patrolsSpinner = controller.getPatrolsSpinner();
        Spinner<Integer> trafficStopsSpinner = controller.getTrafficStopSpinner();

        createSpinner(calloutSpinner, 0, 999, 0);
        createSpinner(arrestsSpinner, 0, 999, 0);
        createSpinner(searchSpinner, 0, 999, 0);
        createSpinner(incidentSpinner, 0, 999, 0);
        createSpinner(patrolsSpinner, 0, 999, 0);
        createSpinner(trafficStopsSpinner, 0, 999, 0);

        shiftInfoPane.setVisible(true);
        shiftInfoPane.setDisable(false);
        notesPane.setVisible(false);
        notesPane.setDisable(true);
        codesPane.setVisible(false);
        codesPane.setDisable(true);
        informationPane.setVisible(false);
        informationPane.setDisable(true);
        logsPane.setVisible(false);
        logsPane.setDisable(true);

        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");

        OfficerInfoName.setText(name);
        OfficerInfoDivision.setValue(division);
        OfficerInfoRank.setValue(rank);
        OfficerInfoAgency.setValue(agency);
        OfficerInfoNumber.setText(number);

        //Top Generations
        generatedByTag.setText("Generated By:" + " " + name);
        String date = DataTerminalHomeApplication.getDate();
        generatedOnTag.setText("Generated On: " + date);
    }
}
