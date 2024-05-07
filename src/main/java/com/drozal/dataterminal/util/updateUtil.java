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

import static com.drozal.dataterminal.util.LogUtils.log;

public class updateUtil {
    public static String gitVersion;

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
                gitVersion=latestVersion;
                log("Git Version: "+latestVersion, LogUtils.Severity.INFO);
                log("App Version: "+stringUtil.version, LogUtils.Severity.INFO);
                reader.close();
            } else {
                log("Failed to fetch version file: HTTP error code " + responseCode, LogUtils.Severity.ERROR);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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