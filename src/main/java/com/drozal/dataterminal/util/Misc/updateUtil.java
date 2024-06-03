package com.drozal.dataterminal.util.Misc;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;

public class updateUtil {
	public static String gitVersion;
	
	public static void checkForUpdates() {
		if (!stringUtil.version.equals("dev")) {
			String rawUrl = "https://raw.githubusercontent.com/Guess1m/ReportsPlus/main/version.txt";
			try {
				URL url = new URL(rawUrl);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(5000);
				connection.setRequestMethod("GET");
				
				int responseCode = connection.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					String latestVersion = reader.readLine();
					gitVersion = latestVersion;
					log("Git Version: " + latestVersion, LogUtils.Severity.INFO);
					log("App Version: " + stringUtil.version, LogUtils.Severity.INFO);
					reader.close();
				} else {
					log("Failed to fetch version file: HTTP error code " + responseCode, LogUtils.Severity.ERROR);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void openWebpage(String url) {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			try {
				desktop.browse(new URI(url));
			} catch (IOException | URISyntaxException e) {
				logError("Failed to open the URL: " + url + " Trace: ", e);
			}
		} else {
			log("Desktop is not supported on this platform.", LogUtils.Severity.ERROR);
		}
	}
	
}
