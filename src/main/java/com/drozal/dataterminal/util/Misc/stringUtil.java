package com.drozal.dataterminal.util.Misc;

import java.io.File;
import java.net.URISyntaxException;

import static com.drozal.dataterminal.util.Misc.LogUtils.logError;

/**
 * The type String util.
 */
public class stringUtil {
	/**
	 * The constant version.
	 */
	public static String version = "v1.0.2-alpha";
	/**
	 * The constant name.
	 */
	public static String name = "Reports Plus";
	/**
	 * The constant calloutLogURL.
	 */
	public static String calloutLogURL = getDataLogsFolderPath() + "calloutLogs.xml";
	/**
	 * The constant trafficstopLogURL.
	 */
	public static String trafficstopLogURL = getDataLogsFolderPath() + "trafficStopLogs.xml";
	/**
	 * The constant incidentLogURL.
	 */
	public static String incidentLogURL = getDataLogsFolderPath() + "incidentLogs.xml";
	/**
	 * The constant searchLogURL.
	 */
	public static String searchLogURL = getDataLogsFolderPath() + "searchLogs.xml";
	/**
	 * The constant arrestLogURL.
	 */
	public static String arrestLogURL = getDataLogsFolderPath() + "arrestLogs.xml";
	/**
	 * The constant patrolLogURL.
	 */
	public static String patrolLogURL = getDataLogsFolderPath() + "patrolLogs.xml";
	/**
	 * The constant trafficCitationLogURL.
	 */
	public static String trafficCitationLogURL = getDataLogsFolderPath() + "trafficCitationLogs.xml";
	/**
	 * The constant impoundLogURL.
	 */
	public static String impoundLogURL = getDataLogsFolderPath() + "impoundLogs.xml";
	
	/**
	 * Gets data logs folder path.
	 *
	 * @return the data logs folder path
	 */
	public static String getDataLogsFolderPath() {
		return getJarPath() + File.separator + "DataLogs" + File.separator;
	}
	
	/**
	 * Gets jar path.
	 *
	 * @return the jar path
	 */
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
