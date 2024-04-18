package com.drozal.dataterminal;

import com.drozal.dataterminal.util.stringUtil;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class OutputViewController extends Application {
    private boolean inErrorBlock = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ListView<TextFlow> listView = new ListView<>();
        ObservableList<TextFlow> logItems = FXCollections.observableArrayList();

        readLogFile(stringUtil.getJarPath() + File.separator + "output.log", logItems);
        listView.setItems(logItems);

        listView.setCellFactory(lv -> new ListCell<TextFlow>() {
            @Override
            protected void updateItem(TextFlow item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) {
                    setGraphic(item);
                } else {
                    setGraphic(null);
                }
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(listView);
        primaryStage.setScene(new Scene(root, 750, 480));
        primaryStage.show();
    }

    private void readLogFile(String filePath, ObservableList<TextFlow> logItems) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logItems.add(createStyledText(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TextFlow createStyledText(String line) {
        TextFlow textFlow = new TextFlow();

        // Process the text depending on whether we're in an error block
        if (inErrorBlock) {
            int endIndex = line.indexOf("***");
            if (endIndex != -1) {
                String errorTextStr = line.substring(0, endIndex);
                Text errorText = new Text(errorTextStr);
                errorText.setFill(Color.RED);
                textFlow.getChildren().add(errorText);
                inErrorBlock = false; // We've found the end of the error block

                // Any text after the end of an error block is normal text
                if (endIndex + 3 < line.length()) {
                    String afterErrorTextStr = line.substring(endIndex + 3);
                    Text afterErrorText = new Text(afterErrorTextStr);
                    setColorBasedOnTag(afterErrorText, afterErrorTextStr);
                    textFlow.getChildren().add(afterErrorText);
                }
            } else {
                // The whole line is error text
                Text errorText = new Text(line);
                errorText.setFill(Color.RED);
                textFlow.getChildren().add(errorText);
            }
        } else if (line.contains("***")) {
            int startErrorIndex = line.indexOf("***");
            int endErrorIndex = line.indexOf("***", startErrorIndex + 3);

            // Add any normal text before the error block
            if (startErrorIndex > 0) {
                String beforeErrorTextStr = line.substring(0, startErrorIndex);
                Text beforeErrorText = new Text(beforeErrorTextStr);
                setColorBasedOnTag(beforeErrorText, beforeErrorTextStr);
                textFlow.getChildren().add(beforeErrorText);
            }

            // Check if the error block closes on the same line
            if (endErrorIndex != -1) {
                String errorTextStr = line.substring(startErrorIndex + 3, endErrorIndex);
                Text errorText = new Text(errorTextStr);
                errorText.setFill(Color.RED);
                textFlow.getChildren().add(errorText);
                inErrorBlock = false;

                // Any text after the error block
                if (endErrorIndex + 3 < line.length()) {
                    String afterErrorTextStr = line.substring(endErrorIndex + 3);
                    Text afterErrorText = new Text(afterErrorTextStr);
                    setColorBasedOnTag(afterErrorText, afterErrorTextStr);
                    textFlow.getChildren().add(afterErrorText);
                }
            } else {
                // Error block doesn't close on the same line
                String errorTextStr = line.substring(startErrorIndex + 3);
                Text errorText = new Text(errorTextStr);
                errorText.setFill(Color.RED);
                textFlow.getChildren().add(errorText);
                inErrorBlock = true;
            }
        } else {
            // Normal text
            Text normalText = new Text(line);
            setColorBasedOnTag(normalText, line);
            textFlow.getChildren().add(normalText);
        }

        return textFlow;
    }

    private void setColorBasedOnTag(Text text, String line) {
        if (line.contains("[INFO]")) {
            text.setFill(Color.BLUE);
        } else if (line.contains("[WARN]")) {
            text.setFill(Color.ORANGE);
        } else if (line.contains("[DEBUG]")) {
            text.setFill(Color.PURPLE);
        } else if (line.contains("[ERROR]")) {
            text.setFill(Color.RED);
        } else {
            text.setFill(Color.BLACK);
        }
    }
}