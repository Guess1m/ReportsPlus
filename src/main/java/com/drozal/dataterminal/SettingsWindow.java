package com.drozal.dataterminal;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SettingsWindow extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        // Center alignment for headings
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(HPos.CENTER);
        gridPane.getColumnConstraints().add(columnConstraints);

        // Display Settings section
        Label displaySettingsLabel = new Label("Display Settings");
        GridPane.setHalignment(displaySettingsLabel, HPos.CENTER);
        gridPane.add(displaySettingsLabel, 0, 0, 2, 1);

        // ComboBoxes for display placement
        String[] displayPlacements = {"Top Left", "Top Right", "Bottom Left", "Bottom Right"};
        for (int i = 0; i < displayPlacements.length; i++) {
            Label placementLabel = new Label("Display " + (i + 1) + " Placement:");
            ComboBox<String> placementComboBox = new ComboBox<>();
            placementComboBox.getItems().addAll(displayPlacements);
            gridPane.add(placementLabel, 0, i + 1);
            gridPane.add(placementComboBox, 1, i + 1);
        }

        // Color Settings section
        Label colorSettingsLabel = new Label("Color Settings");
        GridPane.setHalignment(colorSettingsLabel, HPos.CENTER);
        gridPane.add(colorSettingsLabel, 0, displayPlacements.length + 1, 2, 1);

        // Color Pickers for UI, Accent, Background, and Text colors
        String[] colorLabels = {"UI Color:", "Accent Color:", "Background Color:", "Text Color:"};
        for (int i = 0; i < colorLabels.length; i++) {
            Label colorLabel = new Label(colorLabels[i]);
            ColorPicker colorPicker = new ColorPicker();
            gridPane.add(colorLabel, 0, displayPlacements.length + i + 2);
            gridPane.add(colorPicker, 1, displayPlacements.length + i + 2);
        }

        // Apply and Cancel buttons
        Button applyButton = new Button("Apply");
        Button cancelButton = new Button("Cancel");
        HBox buttonBox = new HBox(10, applyButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);
        gridPane.add(buttonBox, 0, displayPlacements.length + colorLabels.length + 3, 2, 1);

        // Set gridPane to be scrollable
        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);

        // Set scene
        Scene scene = new Scene(scrollPane, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Settings Window");
        primaryStage.show();
        primaryStage.setAlwaysOnTop(true);
    }
}
