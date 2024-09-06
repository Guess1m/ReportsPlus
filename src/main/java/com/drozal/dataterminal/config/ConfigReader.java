package com.drozal.dataterminal.config;

import com.drozal.dataterminal.util.Misc.LogUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Properties;

import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;

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
				log("Unable to determine the location of the JAR file ", LogUtils.Severity.ERROR);
				
				throw new IOException("");
			}
		} catch (URISyntaxException e) {
			log("Error reading config.properties file ", LogUtils.Severity.ERROR);
			throw new IOException("Error reading config.properties file.", e);
		}
	}
	
	public static boolean doesConfigExist() {
		try {
			
			String jarPath = ConfigReader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			
			jarPath = URLDecoder.decode(jarPath, StandardCharsets.UTF_8);
			
			String jarDir = new File(jarPath).getParent();
			
			String configFilePath = jarDir + File.separator + "config.properties";
			
			File configFile = new File(configFilePath);
			return configFile.exists();
		} catch (URISyntaxException e) {
			logError("DoesConfigExist Error ", e);
			return false;
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
				log("Loaded " + property + " with value: " + prop.getProperty(newDatabase + "." + property),
				    LogUtils.Severity.DEBUG);
			} else {
				log("Unable to determine the location of the JAR file ", LogUtils.Severity.ERROR);
			}
		} catch (IOException | URISyntaxException e) {
			log("Error reading or writing config.properties file ", LogUtils.Severity.ERROR);
		}
	}
	
	public static void checkAndSetDefaultValues() {
		log("====================== Configuration ======================", LogUtils.Severity.INFO);
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
		checkAndSetDefaultValue("notificationSettings", "displayDuration", "2.2");
		checkAndSetDefaultValue("notificationSettings", "fadeOutDuration", "1.5");
		checkAndSetDefaultValue("notificationSettings", "notificationPosition", "BottomLeft");
		checkAndSetDefaultValue("notificationSettings", "notificationPosition", "fadeOutDuration");
		checkAndSetDefaultValue("notificationSettings", "notificationPosition", "displayDuration");
		checkAndSetDefaultValue("notificationSettings", "notificationWarnTextColor", "#ffffff");
		checkAndSetDefaultValue("notificationSettings", "notificationWarnPrimary", "#FFA726");
		checkAndSetDefaultValue("notificationSettings", "notificationInfoTextColor", "#ffffff");
		checkAndSetDefaultValue("notificationSettings", "notificationInfoPrimary", "#367af6");
		
		// AOT (Always on Top) Settings
		checkAndSetDefaultValue("AOTSettings", "AOTCallout", "true");
		checkAndSetDefaultValue("AOTSettings", "AOTClient", "true");
		checkAndSetDefaultValue("AOTSettings", "AOTDebug", "true");
		checkAndSetDefaultValue("AOTSettings", "AOTID", "true");
		checkAndSetDefaultValue("AOTSettings", "AOTMap", "true");
		checkAndSetDefaultValue("AOTSettings", "AOTNotes", "true");
		checkAndSetDefaultValue("AOTSettings", "AOTReport", "true");
		checkAndSetDefaultValue("AOTSettings", "AOTSettings", "true");
		
		// User Information
		checkAndSetDefaultValue("userInfo", "Agency", "Error");
		checkAndSetDefaultValue("userInfo", "Division", "Error");
		checkAndSetDefaultValue("userInfo", "Name", "Error");
		checkAndSetDefaultValue("userInfo", "Number", "Error");
		checkAndSetDefaultValue("userInfo", "Rank", "Error");
		checkAndSetDefaultValue("userInfo", "Callsign", "");
		
		// Miscellaneous
		checkAndSetDefaultValue("misc", "calloutDuration", "7");
		checkAndSetDefaultValue("misc", "IDDuration", "infinite");
		checkAndSetDefaultValue("notepad", "notepadMode", "Light");
		
		// UI Settings (continued)
		checkAndSetDefaultValue("uiSettings", "firstLogin", "false");
		checkAndSetDefaultValue("uiSettings", "fullscreenOnStartup", "true");
		checkAndSetDefaultValue("uiSettings", "enableCalloutPopup", "true");
		checkAndSetDefaultValue("uiSettings", "enableIDPopup", "true");
		checkAndSetDefaultValue("uiSettings", "enableSounds", "false");
		
		// Sound Settings
		checkAndSetDefaultValue("soundSettings", "playCallout", "true");
		checkAndSetDefaultValue("soundSettings", "playCreateReport", "true");
		checkAndSetDefaultValue("soundSettings", "playDeleteReport", "true");
		
		// Connection Settings
		checkAndSetDefaultValue("connectionSettings", "serverAutoConnect", "true");
		checkAndSetDefaultValue("connectionSettings", "lastIPV4Connection", "");
		checkAndSetDefaultValue("connectionSettings", "lastPortConnection", "");
		checkAndSetDefaultValue("connectionSettings", "broadcastPort", "8888");
		checkAndSetDefaultValue("connectionSettings", "socketTimeout", "10000");
		
		// Layout Settings
		checkAndSetDefaultValue("layout", "mainWindowLayout", "Default");
		checkAndSetDefaultValue("layout", "notesWindowLayout", "Default");
		checkAndSetDefaultValue("layout", "reportWindowLayout", "Default");
		checkAndSetDefaultValue("layout", "rememberIDLocation", "true");
		checkAndSetDefaultValue("layout", "rememberCalloutLocation", "true");
		checkAndSetDefaultValue("layout", "rememberReportLocation", "true");
		checkAndSetDefaultValue("layout", "rememberNotesLocation", "true");
		
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
		checkAndSetDefaultValue("pedHistory", "hasFishingLicense", "20");
		checkAndSetDefaultValue("pedHistory", "hasBoatingLicense", "20");
		checkAndSetDefaultValue("pedHistory", "hasHuntingLicense", "20");
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
		checkAndSetDefaultValue("pedHistoryGunPermit", "hasGunLicense", "25");
		/*Type*/
		checkAndSetDefaultValue("pedHistoryGunPermitType", "concealedCarryChance", "30");
		checkAndSetDefaultValue("pedHistoryGunPermitType", "openCarryChance", "35");
		checkAndSetDefaultValue("pedHistoryGunPermitType", "bothChance", "35");
		/*Class*/
		checkAndSetDefaultValue("pedHistoryGunPermitClass", "handgunChance", "50");
		checkAndSetDefaultValue("pedHistoryGunPermitClass", "longgunChance", "28");
		checkAndSetDefaultValue("pedHistoryGunPermitClass", "shotgunChance", "22");
		
		log("=========================================================", LogUtils.Severity.INFO);
	}
	
}
