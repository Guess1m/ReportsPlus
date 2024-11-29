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
	public static final String version = "v1.2-alpha";
	public static final String[] updatesList = {"clear saved lookup data", "Improved warrant information", "hunting/boating/fishing license info"};
	
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
