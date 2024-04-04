package com.drozal.dataterminal;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class webviewtestapp extends Application {

    private JavaFXBridge bridge; // Declare bridge object as a class variable

    public static void main(String[] args) {
        launch(args);
    }

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
    // javafx.submitFormValues(firstName, lastName, email, websiteName, message);
    public class JavaFXBridge {
        public void submitFormValues(String firstName, String lastName, String email, String websiteName, String message) {
            System.out.println(firstName);
            System.out.println(lastName);
            System.out.println(email);
            System.out.println(websiteName);
            System.out.println(message);
        }

        public void clearForm() {
        }
    }

}