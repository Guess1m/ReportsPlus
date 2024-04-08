package com.drozal.dataterminal;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class testWindow extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #EEEEEE;"); // Light grey background

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(40, 40, 40, 40));
        gridPane.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 5;"); // White background with rounded corners
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.color(0.4, 0.4, 0.4, 0.5));
        gridPane.setEffect(dropShadow); // Shadow effect for elevation

        // Title Label
        Label titleLabel = new Label("Login");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #6200EE;"); // Using a Material Design deep purple for the title

        // Username
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        styleTextField(usernameField);

        // Password
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        styleTextField(passwordField);

        // Login Button
        Button loginBtn = new Button("Login");
        loginBtn.setStyle("-fx-background-color: #6200EE; -fx-text-fill: white;"); // Deep purple background
        loginBtn.setOnAction(e -> System.out.println("Login attempt..."));
        GridPane.setMargin(loginBtn, new Insets(20, 0, 0, 0));

        // Adding components to the GridPane
        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameField, 1, 0);
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);
        gridPane.add(loginBtn, 1, 2);

        // Adding components to the root container
        root.getChildren().add(titleLabel);
        root.getChildren().add(gridPane);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Enhanced Material Design Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void styleTextField(TextField textField) {
        textField.setStyle("-fx-padding: 10;" +
                "-fx-border-insets: 0;" +
                "-fx-background-insets: 0;" +
                "-fx-background-color: #FFFFFF;" +
                "-fx-border-radius: 5;" +
                "-fx-background-radius: 5;" +
                "-fx-border-color: #BDBDBD;"); // Text field styling
    }
}
