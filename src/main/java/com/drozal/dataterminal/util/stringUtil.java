package com.drozal.dataterminal.util;

import java.io.File;
import java.net.URISyntaxException;

import static com.drozal.dataterminal.util.LogUtils.logError;

public class stringUtil {
    public static String version = "V.1";
    public static String name = "Reports Plus";
    public static String calloutLogURL = getDataLogsFolderPath() + "calloutLogs.xml";
    public static String trafficstopLogURL = getDataLogsFolderPath() + "trafficStopLogs.xml";
    public static String incidentLogURL = getDataLogsFolderPath() + "incidentLogs.xml";
    public static String searchLogURL = getDataLogsFolderPath() + "searchLogs.xml";
    public static String arrestLogURL = getDataLogsFolderPath() + "arrestLogs.xml";
    public static String patrolLogURL = getDataLogsFolderPath() + "patrolLogs.xml";
    public static String trafficCitationLogURL = getDataLogsFolderPath() + "trafficCitationLogs.xml";
    public static String impoundLogURL = getDataLogsFolderPath() + "impoundLogs.xml";

    public static String getDataLogsFolderPath() {
        return getJarPath() + File.separator + "DataLogs" + File.separator;
    }

    public static String getJarPath() {
        try {
            // Get the location of the JAR file
            String jarPath = stringUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

            // Extract the directory path from the JAR path
            String jarDir = new File(jarPath).getParent();

            // Return base location
            return jarDir;
        } catch (URISyntaxException e) {
            logError("GetJarPath URI Syntax Error ", e);
            return ""; // Return empty string if an error occurs
        }
    }
}
