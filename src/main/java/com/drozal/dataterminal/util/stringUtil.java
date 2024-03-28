package com.drozal.dataterminal.util;

import java.io.File;
import java.net.URISyntaxException;

public class stringUtil {
    public static String version = "V.1";
    public static String name = "DataTerminal";
    public static String calloutLogURL = getDataLogsFolderPath() + "calloutLogs.xml";
    public static String trafficstopLogURL = getDataLogsFolderPath() + "trafficStopLogs.xml";
    public static String incidentLogURL = getDataLogsFolderPath() + "incidentLogs.xml";
    public static String searchLogURL = getDataLogsFolderPath() + "searchLogs.xml";
    public static String arrestLogURL = getDataLogsFolderPath() + "arrestLogs.xml";
    public static String patrolLogURL = getDataLogsFolderPath() + "patrolLogs.xml";
    public static String trafficCitationLogURL = getDataLogsFolderPath() + "trafficCitationLogs.xml";
    public static String impoundLogURL = getDataLogsFolderPath() + "impoundLogs.xml";

    public static String getDataLogsFolderPath() {
        try {
            // Get the location of the JAR file
            String jarPath = stringUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

            // Extract the directory path from the JAR path
            String jarDir = new File(jarPath).getParent();

            // Construct the path for the DataLogs folder
            return jarDir + File.separator + "DataLogs" + File.separator;
        } catch (URISyntaxException e) {
            // Handle exception if URI syntax is incorrect
            e.printStackTrace();
            return ""; // Return empty string if an error occurs
        }
    }
}
