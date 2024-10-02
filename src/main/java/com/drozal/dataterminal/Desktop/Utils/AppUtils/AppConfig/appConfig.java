package com.drozal.dataterminal.Desktop.Utils.AppUtils.AppConfig;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.util.Misc.LogUtils;

import java.io.*;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Objects;
import java.util.Properties;

import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.stringUtil.getJarPath;

public class appConfig {
	
	static String appConfigFilePath = getJarPath() + File.separator + "data" + File.separator + "app.properties";
	
	public static void createAppConfig() {
		File appConfigFile = new File(appConfigFilePath);
		if (appConfigFile.exists()) {
			log("app config exists, printing values", LogUtils.Severity.INFO);
		} else {
			try {
				appConfigFile.createNewFile();
				log("App Config: " + appConfigFile.getAbsolutePath(), LogUtils.Severity.INFO);
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
			log("Unable to determine the location of the JAR file ", LogUtils.Severity.ERROR);
			return null;
		}
	}
	
	public static boolean doesAppConfigExist() {
		File configFile = new File(appConfigFilePath);
		return configFile.exists();
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
				log("Loaded " + property + " with value: " + prop.getProperty(newDatabase + "." + property),
				    LogUtils.Severity.DEBUG);
			} else {
				log("Unable to determine the location of the JAR file ", LogUtils.Severity.ERROR);
			}
		} catch (IOException e) {
			log("Error reading or writing config.properties file ", LogUtils.Severity.ERROR);
		}
	}
	
	public static void checkAndSetDefaultAppValues() {
		log("====================== App Config ======================", LogUtils.Severity.INFO);
		checkAndSetDefaultAppValue("Notes", "x", "20");
		checkAndSetDefaultAppValue("Notes", "y", "50");
		
		checkAndSetDefaultAppValue("Settings", "x", "110");
		checkAndSetDefaultAppValue("Settings", "y", "50");
		
		checkAndSetDefaultAppValue("Updates", "x", "200");
		checkAndSetDefaultAppValue("Updates", "y", "50");
		
		checkAndSetDefaultAppValue("Log Browser", "x", "290");
		checkAndSetDefaultAppValue("Log Browser", "y", "50");
		
		checkAndSetDefaultAppValue("Callouts", "x", "380");
		checkAndSetDefaultAppValue("Callouts", "y", "50");
		
		checkAndSetDefaultAppValue("CourtCase", "x", "470");
		checkAndSetDefaultAppValue("CourtCase", "y", "50");
		
		checkAndSetDefaultAppValue("D.M.V Ped Lookup", "x", "560");
		checkAndSetDefaultAppValue("D.M.V Ped Lookup", "y", "50");
		
		checkAndSetDefaultAppValue("D.M.V Veh Lookup", "x", "650");
		checkAndSetDefaultAppValue("D.M.V Veh Lookup", "y", "50");
		
		checkAndSetDefaultAppValue("Server", "x", "740");
		checkAndSetDefaultAppValue("Server", "y", "50");
		
		checkAndSetDefaultAppValue("Show IDs", "x", "830");
		checkAndSetDefaultAppValue("Show IDs", "y", "50");
		
		log("=========================================================", LogUtils.Severity.INFO);
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
