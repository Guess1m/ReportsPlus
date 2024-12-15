package com.Guess.ReportsPlus.util.Misc;

import com.Guess.ReportsPlus.config.ConfigReader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;

public class stringUtil {
	// version.txt
	// version
	// updatesList
	// pom.xml
	// New locale
	// Sound List If Changed
	public static final String version = "v1.2-alpha";
	public static final String[] updatesList = {"AutoUpdater Implementation (BETA)", "Sound Pack / PedVeh Images Installer", "Recieve Server Version", "Hunting/Boating/Fishing License Info", "App Text Color Configuration", "More License Outcomes", "Fixed FX Thread Bugs", "Fix Ped License Status Bug", "Added Ped Flags", "Improved Registration/Insurance Info", "Better Show When Veh. Type is None", "Clear Saved Lookup Data", "Improved Warrant Information"};
	public static final String[] soundList = {"alert-callout.wav", "alert-success.wav", "alert-delete.wav", "alert-wanted.wav"};
	/* New Version Locale.properies changes
	NEW:
	Desktop.AvailableLabel=Available!
	Desktop.NewVersionAvailable=New Version Available!
	Settings.desktopAppTextClrLabel=App Text Color
	Settings.ClearLookupDataBtn=CLEAR LOOKUP DATA
	Settings.desktopAppTextClrTT=Set the color of the app name text
	UpdatesWindow.UpdateButton=AutoUpdate (BETA)
	UpdatesWindow.CantUpdateLabel=No Update Available!
	UpdatesWindow.IntelChipCheckbox=Intel Chip (ONLY For Intel MacOS!)
	UpdatesWindow.MissingUpdater=Missing UpdateUtility!
	Settings.installSoundsBtn=INSTALL/UPDATE
	Settings.soundPackNotDetectedLbl=Sound Pack Not Detected
	Settings.imagesNotDetectedLbl=Ped/Veh Images Not Detected
	Settings.enablePedVehImages=Enable Ped/Veh Images
	Settings.enablePedVehImagesTT=Toggle whether ped/veh images will be shown in lookup
	Settings.clearLookupDataLabel=Clear Old Ped / Veh Data
	Settings.clearLookupDataLabelTT=ONLY delete saved ped / veh history data from previous lookups to free space
	PedLookup.PedImageFoundlbl=Image Found in File:
	
	REMOVED:
	UpdatesWindow.CheckUpdatesButton=Check For Updates
	
	CHANGED:
	Settings.AudioSettingsHeader=AUDIO SETTINGS  ->  Settings.AudioSettingsHeader=AUDIO/OPTIONAL SETTINGS
	Settings.AudioBtn=Audio  ->  Settings.AudioBtn=Audio/Optionals
	*/
	
	public static final String name = "Reports Plus";
	public static final String DeathReportLogURL = getDataLogsFolderPath() + "deathReportLogs.xml";
	public static final String calloutLogURL = getDataLogsFolderPath() + "calloutLogs.xml";
	public static final String trafficstopLogURL = getDataLogsFolderPath() + "trafficStopLogs.xml";
	public static final String incidentLogURL = getDataLogsFolderPath() + "incidentLogs.xml";
	public static final String searchLogURL = getDataLogsFolderPath() + "searchLogs.xml";
	public static final String arrestLogURL = getDataLogsFolderPath() + "arrestLogs.xml";
	public static final String accidentLogURL = getDataLogsFolderPath() + "accidentLogs.xml";
	public static final String patrolLogURL = getDataLogsFolderPath() + "patrolLogs.xml";
	public static final String trafficCitationLogURL = getDataLogsFolderPath() + "trafficCitationLogs.xml";
	public static final String impoundLogURL = getDataLogsFolderPath() + "impoundLogs.xml";
	public static final String calloutDataURL = getJarPath() + File.separator + "data" + File.separator + "calloutData.xml";
	public static final String calloutHistoryURL = getJarPath() + File.separator + "data" + File.separator + "calloutHistory.xml";
	public static final String customizationURL = getJarPath() + File.separator + "data" + File.separator + "customization.json";
	public static final String courtDataURL = getJarPath() + File.separator + "data" + File.separator + "courtData.xml";
	public static final String pedHistoryURL = getJarPath() + File.separator + "data" + File.separator + "pedHistory.xml";
	public static final String vehicleHistoryURL = getJarPath() + File.separator + "data" + File.separator + "vehHistory.xml";
	public static final String chargesFilePath = getJarPath() + File.separator + "data" + File.separator + "Charges.xml";
	public static final String currentIDFileURL = getJarPath() + File.separator + "serverData" + File.separator + "ServerCurrentID.xml";
	public static final String currentLocationFileURL = getJarPath() + File.separator + "serverData" + File.separator + "ServerLocation.data";
	public static final String IDHistoryURL = getJarPath() + File.separator + "data" + File.separator + "IDHistory.xml";
	public static final String pedImageFolderURL = getJarPath() + File.separator + "images" + File.separator + "peds";
	public static final String vehImageFolderURL = getJarPath() + File.separator + "images" + File.separator + "vehicles";
	public static final String soundPackDownloadURL = "https://github.com/Guess1m/ReportsPlus/releases/latest/download/sounds-optional.zip";
	public static final String imagePackDownloadURL = "https://github.com/Guess1m/ReportsPlus/releases/latest/download/images-optional.zip";
	
	public static String hexToRgba(String hex, double transparency) {
		if (hex.startsWith("#")) {
			hex = hex.substring(1);
		}
		if (hex.length() != 6) {
			throw new IllegalArgumentException("Invalid hex color string");
		}
		if (transparency < 0.0 || transparency > 1.0) {
			throw new IllegalArgumentException("Transparency must be between 0.0 and 1.0");
		}
		
		int r = Integer.parseInt(hex.substring(0, 2), 16);
		int g = Integer.parseInt(hex.substring(2, 4), 16);
		int b = Integer.parseInt(hex.substring(4, 6), 16);
		
		return String.format("rgb(%d, %d, %d, %.2f)", r, g, b, transparency);
	}
	
	public static String getSecondaryColor() {
		try {
			return ConfigReader.configRead("uiColors", "secondaryColor");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String getDataLogsFolderPath() {
		return getJarPath() + File.separator + "DataLogs" + File.separator;
	}
	
	public static String getJarPath() {
		try {
			
			String jarPath = stringUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			
			String jarDir = new File(jarPath).getParent();
			
			return jarDir;
		} catch (URISyntaxException e) {
			logError("GetJarPath URI Syntax Error ", e);
			return "";
		}
	}
}
