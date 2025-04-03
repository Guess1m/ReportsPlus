package com.Guess.ReportsPlus.Desktop.Utils.AppUtils.AppConfig;

import com.Guess.ReportsPlus.config.ConfigReader;

import java.io.*;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Objects;
import java.util.Properties;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.*;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getDataFolderPath;

public class appConfig {
	
	static String appConfigFilePath = getDataFolderPath() + "app.properties";
	
	public static void createAppConfig() {
		File appConfigFile = new File(appConfigFilePath);
		if (!appConfigFile.exists()) {
			try {
				appConfigFile.createNewFile();
				logInfo("App Config Created: " + appConfigFile.getAbsolutePath());
			} catch (IOException e) {
				logError("Failed to create app config file", e);
			}
		}
	}
	
	public static Double appConfigRead(String database, String property) {
		Properties prop = new Properties();
		ProtectionDomain protectionDomain = appConfig.class.getProtectionDomain();
		CodeSource codeSource = protectionDomain.getCodeSource();
		if (codeSource != null) {
			try (InputStream input = new FileInputStream(appConfigFilePath)) {
				
				prop.load(input);
				
				return Double.valueOf(prop.getProperty(database + "." + property));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			logError("Unable to determine the location of the JAR file ");
			return null;
		}
	}
	
	public static void checkAndSetDefaultAppValue(String newDatabase, String property, String defaultValue) {
		try {
			Properties prop = new Properties();
			ProtectionDomain protectionDomain = ConfigReader.class.getProtectionDomain();
			CodeSource codeSource = protectionDomain.getCodeSource();
			if (codeSource != null) {
				
				try (InputStream input = new FileInputStream(appConfigFilePath)) {
					prop.load(input);
				}
				
				String oldKey = "database." + property;
				if (prop.containsKey(oldKey)) {
					String value = prop.getProperty(oldKey);
					prop.remove(oldKey);
					prop.setProperty(newDatabase + "." + property, value);
				} else if (!prop.containsKey(newDatabase + "." + property)) {
					prop.setProperty(newDatabase + "." + property, defaultValue);
				}
				
				try (OutputStream output = new FileOutputStream(appConfigFilePath)) {
					prop.store(output, null);
				}
				logDebug("Loaded " + newDatabase + " '" + property + "' with value: " + prop.getProperty(newDatabase + "." + property));
			} else {
				logError("Unable to determine the location of the JAR file ");
			}
		} catch (IOException e) {
			logError("Error reading or writing app.properties file ");
		}
	}
	
	public static void checkAndSetDefaultAppValues() {
		logInfo("====================== App Config ======================");
		String x1 = String.valueOf(45.0);
		checkAndSetDefaultAppValue("Callouts", "x", x1);
		checkAndSetDefaultAppValue("Callouts", "y", String.valueOf(20.0));
		checkAndSetDefaultAppValue("CourtCase", "x", x1);
		checkAndSetDefaultAppValue("CourtCase", "y", String.valueOf(320.0));
		checkAndSetDefaultAppValue("Log Browser", "x", x1);
		checkAndSetDefaultAppValue("Log Browser", "y", String.valueOf(220.0));
		checkAndSetDefaultAppValue("Notes", "x", x1);
		checkAndSetDefaultAppValue("Notes", "y", String.valueOf(120.0));
		checkAndSetDefaultAppValue("Show IDs", "x", x1);
		checkAndSetDefaultAppValue("Show IDs", "y", String.valueOf(420.0));
		
		String x2 = String.valueOf(175.0);
		checkAndSetDefaultAppValue("New Report", "x", x2);
		checkAndSetDefaultAppValue("New Report", "y", String.valueOf(220.0));
		checkAndSetDefaultAppValue("Ped Lookup", "x", x2);
		checkAndSetDefaultAppValue("Ped Lookup", "y", String.valueOf(20.0));
		checkAndSetDefaultAppValue("Veh Lookup", "x", x2);
		checkAndSetDefaultAppValue("Veh Lookup", "y", String.valueOf(120.0));
		checkAndSetDefaultAppValue("ALPR", "x", x2);
		checkAndSetDefaultAppValue("ALPR", "y", String.valueOf(320.0));
		
		String x3 = String.valueOf(305.0);
		checkAndSetDefaultAppValue("Server", "x", x3);
		checkAndSetDefaultAppValue("Server", "y", String.valueOf(20.0));
		checkAndSetDefaultAppValue("Settings", "x", x3);
		checkAndSetDefaultAppValue("Settings", "y", String.valueOf(220.0));
		checkAndSetDefaultAppValue("Updates", "x", x3);
		checkAndSetDefaultAppValue("Updates", "y", String.valueOf(120.0));
		
		String x4 = String.valueOf(420.0);
		checkAndSetDefaultAppValue("Profile", "x", x4);
		checkAndSetDefaultAppValue("Profile", "y", String.valueOf(20.0));
		checkAndSetDefaultAppValue("Report Statistics", "x", x4);
		checkAndSetDefaultAppValue("Report Statistics", "y", String.valueOf(120.0));
		logInfo("=========================================================");
	}
	
	public static void appConfigWrite(String database, String property, String value) {
		Properties prop = new Properties();
		OutputStream output = null;
		FileInputStream input = null;
		
		try {
			input = new FileInputStream(appConfigFilePath);
			prop.load(input);
		} catch (IOException e) {
			logError("ConfigWrite Error Code 1 ", e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logError("ConfigWrite Error Code 2 ", e);
				}
			}
		}
		
		try {
			prop.setProperty(database + "." + property, value);
			output = new FileOutputStream(Objects.requireNonNull(appConfigFilePath));
			prop.store(output, null);
		} catch (IOException e) {
			logError("ConfigWrite Error Code 3 ", e);
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					logError("ConfigWrite Error Code 4 ", e);
				}
			}
		}
	}
	
}
