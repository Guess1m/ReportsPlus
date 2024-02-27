package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
    public static Boolean isLoggedIn;
    private double xOffset = 0;
    private double yOffset = 0;

    public static String getDate(){
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentTime.format(formatter);
    }

    public static String getTime(){
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentTime.format(formatter);
    }
    private Spinner<Integer> createSpinner(Spinner spinner, int min, int max, int initialValue) {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initialValue);
        spinner.setValueFactory(valueFactory);
        return spinner;
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {


        mainStage.mainRT = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DataTerminalHome-view.fxml"));
        Parent root = loader.load();



// Simulate custom title bar with a colored region
        VBox customTitleBar = new VBox();

        // Add title label to the custom title bar
        Label titleLabel = new Label("Data Terminal V.1");
        titleLabel.setTextFill(Color.WHITE); // Set text color to black
        titleLabel.setPadding(new Insets(4, 0, 0, 20)); // Adjust left padding as needed
        titleLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 16)); // Set font to Consolas and bold, adjust size as needed

        customTitleBar.getChildren().add(titleLabel);
        customTitleBar.setMinHeight(30); // Adjust the height as needed
        customTitleBar.setStyle("-fx-background-color: #6f77a4"); // Change this to the desired color

        VBox content = new VBox(customTitleBar, root);
        Scene scene = new Scene(content);

// Set stage transparent and remove default window decorations
        mainStage.mainRT.initStyle(StageStyle.TRANSPARENT);
        mainStage.mainRT.setScene(scene);
        mainStage.mainRT.setTitle("Data Terminal");

        // Set up event handlers for mouse pressed, dragged, and released events
        customTitleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        customTitleBar.setOnMouseDragged(event -> {
            mainStage.mainRT.setX(event.getScreenX() - xOffset);
            mainStage.mainRT.setY(event.getScreenY() - yOffset);
        });
        mainStage.mainRT.setResizable(false);
        mainStage.mainRT.show();
        mainStage.mainRT.centerOnScreen();
        mainStage.mainRT.setY(mainStage.mainRT.getY() * 3f / 2f);


        actionController controller = loader.getController();
        StackPane defaultPane = controller.getDefaultPane();
        StackPane notesPane = controller.getNotesPane();
        StackPane codesPane = controller.getCodesPane();
        Label generatedByTag = controller.getGeneratedByTag();
        Label generatedOnTag = controller.getGeneratedDateTag();
        StackPane shiftInfoPane = controller.getShiftInformationPane();

        TextField OfficerInfoName = controller.getOfficerInfoName();
        TextField OfficerInfoDivision = controller.getOfficerInfoDivision();
        TextField OfficerInfoRank = controller.getOfficerInfoRank();
        TextField OfficerInfoNumber = controller.getOfficerInfoNumber();
        TextField OfficerInfoAgency = controller.getOfficerInfoAgency();

        Spinner<Integer> calloutSpinner = controller.getCalloutSpinner();
        Spinner<Integer> searchSpinner = controller.getSearchesSpinner();
        Spinner<Integer> arrestsSpinner = controller.getArrestsSpinner();
        Spinner<Integer> incidentSpinner = controller.getIncidentSpinner();
        Spinner<Integer> patrolsSpinner = controller.getPatrolsSpinner();
        Spinner<Integer> trafficStopsSpinner = controller.getTrafficStopSpinner();

        createSpinner(calloutSpinner,0,999,0);
        createSpinner(arrestsSpinner,0,999,0);
        createSpinner(searchSpinner,0,999,0);
        createSpinner(incidentSpinner,0,999,0);
        createSpinner(patrolsSpinner,0,999,0);
        createSpinner(trafficStopsSpinner,0,999,0);

        //Check for previous Login
        boolean configExists = ConfigReader.doesConfigExist();
        if (configExists) {
            isLoggedIn=true;
            //already was a config
            shiftInfoPane.setVisible(true);
            shiftInfoPane.setDisable(false);

            notesPane.setVisible(false);
            notesPane.setDisable(true);

            codesPane.setVisible(false);
            codesPane.setDisable(true);
            defaultPane.setDisable(true);
            defaultPane.setVisible(false);

            String name = ConfigReader.configRead("Name");
            String division = ConfigReader.configRead("Division");
            String rank = ConfigReader.configRead("Rank");
            String number = ConfigReader.configRead("Number");
            String agency = ConfigReader.configRead("Agency");

            OfficerInfoName.setText(name);
            OfficerInfoDivision.setText(division);
            OfficerInfoRank.setText(rank);
            OfficerInfoAgency.setText(agency);
            OfficerInfoNumber.setText(number);

            //Top Generations
            generatedByTag.setText("Generated By: "+rank+" "+ name);
            String date = DataTerminalHomeApplication.getDate();
            generatedOnTag.setText("Generated On: "+ date);

            System.out.println("Shift Info should be showing since there already was a config");

        } else {
            //blank config
            //disable buttons??
            isLoggedIn=false;
            defaultPane.setVisible(true);
            defaultPane.setDisable(false);

            codesPane.setVisible(false);
            codesPane.setDisable(true);
            notesPane.setDisable(true);
            notesPane.setVisible(false);
            shiftInfoPane.setDisable(true);
            shiftInfoPane.setVisible(false);

            System.out.println("login should be showing since there was no config");
        }


    }
}