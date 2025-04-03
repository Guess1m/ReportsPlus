package com.Guess.ReportsPlus.config;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Properties;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.*;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getJarPath;

public class ConfigReader {
	
	public static String configRead(String database, String property) throws IOException {
		Properties prop = new Properties();
		try {
			ProtectionDomain protectionDomain = ConfigReader.class.getProtectionDomain();
			CodeSource codeSource = protectionDomain.getCodeSource();
			if (codeSource != null) {
				URL jarUrl = codeSource.getLocation();
				String jarDirPath = new File(jarUrl.toURI()).getParent();
				try (InputStream input = new FileInputStream(jarDirPath + File.separator + "config.properties")) {
					
					prop.load(input);
					
					return prop.getProperty(database + "." + property);
				}
			} else {
				logError("Unable to determine the location of the JAR file ");
				
				throw new IOException("");
			}
		} catch (URISyntaxException e) {
			logError("Error reading config.properties file ");
			throw new IOException("Error reading config.properties file.", e);
		}
	}
	
	public static void checkAndSetDefaultValue(String newDatabase, String property, String defaultValue) {
		try {
			Properties prop = new Properties();
			ProtectionDomain protectionDomain = ConfigReader.class.getProtectionDomain();
			CodeSource codeSource = protectionDomain.getCodeSource();
			if (codeSource != null) {
				URL jarUrl = codeSource.getLocation();
				String jarDirPath = new File(jarUrl.toURI()).getParent();
				File configFile = new File(jarDirPath + File.separator + "config.properties");
				
				try (InputStream input = new FileInputStream(configFile)) {
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
				
				try (OutputStream output = new FileOutputStream(configFile)) {
					prop.store(output, null);
				}
				logDebug("Loaded " + property + " with value: " + prop.getProperty(newDatabase + "." + property));
			} else {
				logError("Unable to determine the location of the JAR file ");
			}
		} catch (IOException | URISyntaxException e) {
			logError("Error reading or writing config.properties file");
		}
	}
	
	public static void createConfig() {
		String configFilePath = getJarPath() + File.separator + "config.properties";
		File configFile = new File(configFilePath);
		if (configFile.exists()) {
			ConfigWriter.configwrite("uiSettings", "firstLogin", "false");
			logInfo("exists, printing values");
		} else {
			try {
				configFile.createNewFile();
				logInfo("Created Config: " + configFile.getAbsolutePath());
			} catch (IOException e) {
				logError("Failed to create config file: " + e);
			}
		}
	}
	
	public static void checkAndSetDefaultValues() {
		logInfo("====================== Configuration ======================");
		// Updater Settings
		checkAndSetDefaultValue("updater", "useIntel", "false");
		
		// UI Settings
		checkAndSetDefaultValue("uiColors", "UIDarkMode", "true");
		checkAndSetDefaultValue("uiColors", "accentColor", "#544f7f");
		checkAndSetDefaultValue("uiColors", "mainColor", "#524992");
		checkAndSetDefaultValue("uiColors", "secondaryColor", "#665CB6");
		checkAndSetDefaultValue("uiColors", "bkgColor", "#FFFFFF");
		
		// Lookup Layout
		checkAndSetDefaultValue("lookupWindow", "pedLookupVisible", "true");
		checkAndSetDefaultValue("lookupWindow", "vehLookupVisible", "true");
		checkAndSetDefaultValue("lookupWindow", "lookupOrientation", "horizontal");
		
		// Notification Settings
		checkAndSetDefaultValue("notificationSettings", "enabled", "true");
		checkAndSetDefaultValue("notificationSettings", "displayDuration", "3.5");
		checkAndSetDefaultValue("notificationSettings", "fadeOutDuration", "2.5");
		checkAndSetDefaultValue("notificationSettings", "notificationPosition", "BottomRight");
		checkAndSetDefaultValue("notificationSettings", "notificationPosition", "fadeOutDuration");
		checkAndSetDefaultValue("notificationSettings", "notificationPosition", "displayDuration");
		checkAndSetDefaultValue("notificationSettings", "notificationWarnTextColor", "#ffffff");
		checkAndSetDefaultValue("notificationSettings", "notificationWarnPrimary", "#FFA726");
		checkAndSetDefaultValue("notificationSettings", "notificationInfoTextColor", "#ffffff");
		checkAndSetDefaultValue("notificationSettings", "notificationInfoPrimary", "#367af6");
		
		// User Information
		checkAndSetDefaultValue("userInfo", "Agency", "Error");
		checkAndSetDefaultValue("userInfo", "Division", "Error");
		checkAndSetDefaultValue("userInfo", "Name", "Error");
		checkAndSetDefaultValue("userInfo", "Number", "Error");
		checkAndSetDefaultValue("userInfo", "Rank", "Error");
		checkAndSetDefaultValue("userInfo", "Callsign", "");
		
		// Desktop Settings
		checkAndSetDefaultValue("desktopSettings", "useBackground", "false");
		checkAndSetDefaultValue("desktopSettings", "backgroundPath", "");
		checkAndSetDefaultValue("desktopSettings", "useSolidColor", "true");
		checkAndSetDefaultValue("desktopSettings", "desktopColor", "#306494");
		checkAndSetDefaultValue("desktopSettings", "topBarColor", "#e6e6e6");
		checkAndSetDefaultValue("desktopSettings", "topBarTextColor", "#000000");
		checkAndSetDefaultValue("desktopSettings", "appTextColor", "#ffffff");
		checkAndSetDefaultValue("desktopSettings", "taskBarColor", "#2e2e2e");
		checkAndSetDefaultValue("desktopSettings", "taskBarTextColor", "#ffffff");
		
		checkAndSetDefaultValue("desktopSettings", "saveWindowPosition", "true");
		checkAndSetDefaultValue("desktopSettings", "saveWindowSize", "true");
		
		// Miscellaneous
		checkAndSetDefaultValue("misc", "calloutDuration", "7");
		checkAndSetDefaultValue("misc", "IDDuration", "infinite");
		checkAndSetDefaultValue("misc", "TrafficStopDuration", "infinite");
		checkAndSetDefaultValue("notepad", "notepadMode", "Light");
		
		// UI Settings (continued)
		checkAndSetDefaultValue("uiSettings", "firstLogin", "false");
		checkAndSetDefaultValue("uiSettings", "enableCalloutPopup", "true");
		checkAndSetDefaultValue("uiSettings", "enableIDPopup", "true");
		checkAndSetDefaultValue("uiSettings", "enableSounds", "false");
		checkAndSetDefaultValue("uiSettings", "enablePedVehImages", "false");
		checkAndSetDefaultValue("uiSettings", "enableTrafficStopPopup", "true");
		checkAndSetDefaultValue("uiSettings", "windowDisplaySetting", "Fullscreen");
		checkAndSetDefaultValue("uiSettings", "windowAOT", "false");
		checkAndSetDefaultValue("uiSettings", "use24Hour", "false");
		checkAndSetDefaultValue("uiSettings", "skipOfficerLogin", "false");
		
		// Sound Settings
		checkAndSetDefaultValue("soundSettings", "playCallout", "true");
		checkAndSetDefaultValue("soundSettings", "playCreateReport", "true");
		checkAndSetDefaultValue("soundSettings", "playDeleteReport", "true");
		checkAndSetDefaultValue("soundSettings", "playLookupWarning", "true");
		
		// Connection Settings
		checkAndSetDefaultValue("connectionSettings", "serverAutoConnect", "true");
		checkAndSetDefaultValue("connectionSettings", "lastIPV4Connection", "");
		checkAndSetDefaultValue("connectionSettings", "lastPortConnection", "");
		checkAndSetDefaultValue("connectionSettings", "broadcastPort", "8888");
		checkAndSetDefaultValue("connectionSettings", "socketTimeout", "13000");
		checkAndSetDefaultValue("connectionSettings", "useGameTime", "true");
		checkAndSetDefaultValue("connectionSettings", "autofillLocation", "true");
		
		// Report Settings
		checkAndSetDefaultValue("reportSettings", "reportAccent", "#263238");
		checkAndSetDefaultValue("reportSettings", "reportBackground", "#505D62");
		checkAndSetDefaultValue("reportSettings", "reportHeading", "#FFFFFF");
		checkAndSetDefaultValue("reportSettings", "reportSecondary", "#323C41");
		checkAndSetDefaultValue("reportSettings", "reportWindowDarkMode", "false");
		
		// Veh Lookup Settings
		checkAndSetDefaultValue("vehicleHistory", "hasValidInspection", "85");
		
		// Ped History Settings
		checkAndSetDefaultValue("pedHistory", "courtTrialDelay", "600");
		checkAndSetDefaultValue("pedHistory", "onParoleChance", "15");
		checkAndSetDefaultValue("pedHistory", "onProbationChance", "25");
		checkAndSetDefaultValue("pedHistory", "hasFishingLicense", "40");
		checkAndSetDefaultValue("pedHistory", "hasBoatingLicense", "35");
		checkAndSetDefaultValue("pedHistory", "hasHuntingLicense", "40");
		checkAndSetDefaultValue("pedHistory", "validLicenseChance", "80");
		checkAndSetDefaultValue("pedHistory", "expiredLicenseChance", "15");
		checkAndSetDefaultValue("pedHistory", "suspendedLicenseChance", "5");
		checkAndSetDefaultValue("pedHistory", "baseFlagProbability", "5");
		
		// Arrest
		checkAndSetDefaultValue("pedHistoryArrest", "chanceNoCharges", "60");
		checkAndSetDefaultValue("pedHistoryArrest", "chanceMinimalCharges", "25");
		checkAndSetDefaultValue("pedHistoryArrest", "chanceFewCharges", "10");
		checkAndSetDefaultValue("pedHistoryArrest", "chanceManyCharges", "5");
		// Citation
		checkAndSetDefaultValue("pedHistoryCitation", "chanceNoCitations", "60");
		checkAndSetDefaultValue("pedHistoryCitation", "chanceMinimalCitations", "25");
		checkAndSetDefaultValue("pedHistoryCitation", "chanceFewCitations", "10");
		checkAndSetDefaultValue("pedHistoryCitation", "chanceManyCitations", "5");
		// Gun Permit
		checkAndSetDefaultValue("pedHistoryGunPermit", "hasGunLicense", "33");
		
		checkAndSetDefaultValue("pedHistoryGunPermitType", "concealedCarryChance", "30");
		checkAndSetDefaultValue("pedHistoryGunPermitType", "openCarryChance", "35");
		checkAndSetDefaultValue("pedHistoryGunPermitType", "bothChance", "35");
		
		checkAndSetDefaultValue("pedHistoryGunPermitClass", "handgunChance", "50");
		checkAndSetDefaultValue("pedHistoryGunPermitClass", "longgunChance", "28");
		checkAndSetDefaultValue("pedHistoryGunPermitClass", "shotgunChance", "22");
		
		checkAndSetDefaultValue("update", "type", "client");
		checkAndSetDefaultValue("keybindings", "inputLock", "H");
		checkAndSetDefaultValue("keybindings", "closeWindow", "W");
		checkAndSetDefaultValue("keybindings", "minimizeWindow", "MINUS");
		checkAndSetDefaultValue("keybindings", "toggleMaximize", "PLUS");
		checkAndSetDefaultValue("keybindings", "applicationFullscreen", "F11");
		
		logInfo("=========================================================");
	}
	
}
