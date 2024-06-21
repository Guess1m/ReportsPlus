package com.drozal.dataterminal.util.Misc;

import com.drozal.dataterminal.config.ConfigReader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static com.drozal.dataterminal.util.Misc.LogUtils.logError;

public class stringUtil {
	public static String version = "v1.0.5-alpha";
	public static String name = "Reports Plus";
	public static String calloutLogURL = getDataLogsFolderPath() + "calloutLogs.xml";
	public static String trafficstopLogURL = getDataLogsFolderPath() + "trafficStopLogs.xml";
	public static String incidentLogURL = getDataLogsFolderPath() + "incidentLogs.xml";
	public static String searchLogURL = getDataLogsFolderPath() + "searchLogs.xml";
	public static String arrestLogURL = getDataLogsFolderPath() + "arrestLogs.xml";
	public static String patrolLogURL = getDataLogsFolderPath() + "patrolLogs.xml";
	public static String trafficCitationLogURL = getDataLogsFolderPath() + "trafficCitationLogs.xml";
	public static String impoundLogURL = getDataLogsFolderPath() + "impoundLogs.xml";
	public static String calloutDataURL = getJarPath() + File.separator + "data" + File.separator + "calloutData.xml";
	public static String calloutHistoryURL = getJarPath() + File.separator + "data" + File.separator + "calloutHistory.xml";
	
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
			return ConfigReader.configRead("secondaryColor");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String getDataLogsFolderPath() {
		return getJarPath() + File.separator + "DataLogs" + File.separator;
	}
	
	public static String getJarPath() {
		try {
			
			String jarPath = stringUtil.class.getProtectionDomain()
			                                 .getCodeSource()
			                                 .getLocation()
			                                 .toURI()
			                                 .getPath();
			
			String jarDir = new File(jarPath).getParent();
			
			return jarDir;
		} catch (URISyntaxException e) {
			logError("GetJarPath URI Syntax Error ", e);
			return "";
		}
	}
}
