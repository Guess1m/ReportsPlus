package com.drozal.dataterminal;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class Main extends Application {

    private JavaFXBridge bridge; // Declare bridge object as a class variable

    @Override
    public void start(Stage primaryStage) {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // Enable JavaScript
        webEngine.setJavaScriptEnabled(true);

        // Load the HTML document containing the form
        webEngine.load(getClass().getResource("html/form.html").toExternalForm());

        // Initialize the bridge object
        bridge = new JavaFXBridge();

        // Inject JavaFX bridge object into JavaScript only once
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javafx", bridge);
            }
        });

        primaryStage.setScene(new Scene(webView, 550, 400));
        primaryStage.show();
    }

    // JavaFX bridge class to handle communication between JavaFX and JavaScript
    public class JavaFXBridge {
        public void submitFormValues(String username, String password) {
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
        }

        public void clearForm() {
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}