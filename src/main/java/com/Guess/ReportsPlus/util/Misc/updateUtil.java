package com.Guess.ReportsPlus.util.Misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;

public class updateUtil {
	public static String gitVersion;
	
	public static void runJar(String jarPath) {
		File jarFile = new File(jarPath);
		if (jarFile.exists()) {
			try {
				ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarPath);
				processBuilder.start();
			} catch (IOException e) {
				logError("Error Starting Updater: " + jarPath, e);
			}
		} else {
			log("Updater Not Found At: " + jarPath, LogUtils.Severity.ERROR);
		}
	}
	
	public static void checkForUpdates() {
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
		} catch (UnknownHostException e) {
			log("UnknownHostException: Unable to resolve host " + rawUrl + ". Check your network connection.",
			    LogUtils.Severity.ERROR);
		} catch (IOException e) {
			logError("Cant check for updates: ", e);
		}
	}
	
}
