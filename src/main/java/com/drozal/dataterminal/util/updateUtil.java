package com.drozal.dataterminal.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

public class updateUtil {

    public static void checkForUpdates() {
        String rawUrl = "https://raw.githubusercontent.com/zainrd123/DataTerminal/main/version.txt";
        try {
            URL url = new URL(rawUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String latestVersion = reader.readLine();
                System.out.println("found ver in git: "+latestVersion);
                System.out.println("application version: "+stringUtil.version);
                reader.close();
                if (!stringUtil.version.equals(latestVersion)) {
                    notifyUserOfUpdate();
                }
            } else {
                System.out.println("Failed to fetch version file: HTTP error code " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void notifyUserOfUpdate() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Update Available");
            alert.setHeaderText("A new version of the application is available!");
            alert.setContentText("Please visit our website to download the latest version.");

            ButtonType websiteButton = new ButtonType("Visit Website");
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(websiteButton, cancelButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == websiteButton) {
                openWebpage("https://github.com/zainrd123/DataTerminal/releases");
            }
        });
    }
    public static void openWebpage(String url) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
                System.out.println("Failed to open the URL: " + url);
            }
        } else {
            System.out.println("Desktop is not supported on this platform.");
        }
    }

}
