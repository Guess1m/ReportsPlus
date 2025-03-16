package com.Guess.ReportsPlus.util.Strings;

import com.Guess.ReportsPlus.util.Other.controllerUtils;

import java.io.File;

public class URLStrings {
    public static final String DeathReportLogURL = controllerUtils.getDataLogsFolderPath() + "deathReportLogs.xml";
    public static final String calloutLogURL = controllerUtils.getDataLogsFolderPath() + "calloutLogs.xml";
    public static final String trafficstopLogURL = controllerUtils.getDataLogsFolderPath() + "trafficStopLogs.xml";
    public static final String incidentLogURL = controllerUtils.getDataLogsFolderPath() + "incidentLogs.xml";
    public static final String searchLogURL = controllerUtils.getDataLogsFolderPath() + "searchLogs.xml";
    public static final String arrestLogURL = controllerUtils.getDataLogsFolderPath() + "arrestLogs.xml";
    public static final String accidentLogURL = controllerUtils.getDataLogsFolderPath() + "accidentLogs.xml";
    public static final String patrolLogURL = controllerUtils.getDataLogsFolderPath() + "patrolLogs.xml";
    public static final String trafficCitationLogURL = controllerUtils.getDataLogsFolderPath() + "trafficCitationLogs.xml";
    public static final String impoundLogURL = controllerUtils.getDataLogsFolderPath() + "impoundLogs.xml";
    public static final String calloutDataURL = controllerUtils.getDataFolderPath() + "calloutData.xml";
    public static final String calloutHistoryURL = controllerUtils.getDataFolderPath() + "calloutHistory.xml";
    public static final String customizationURL = controllerUtils.getDataFolderPath() + "customization.json";
    public static final String customDropdownURL = controllerUtils.getDataFolderPath() + "customDropdowns.json";
    public static final String courtDataURL = controllerUtils.getDataFolderPath() + "courtData.xml";
    public static final String pedHistoryURL = controllerUtils.getDataFolderPath() + "pedHistory.xml";
    public static final String vehicleHistoryURL = controllerUtils.getDataFolderPath() + "vehHistory.xml";
    public static final String chargesFilePath = controllerUtils.getDataFolderPath() + "Charges.xml";
    public static final String IDHistoryURL = controllerUtils.getDataFolderPath() + "IDHistory.xml";
    public static final String currentUserFileURL = controllerUtils.getDataFolderPath() + "UserProfiles.xml";
    public static final String currentIDFileURL = controllerUtils.getServerDataFolderPath() + "ServerCurrentID.xml";
    public static final String serverALPRFileURL = controllerUtils.getServerDataFolderPath() + "ServerALPR.data";
    public static final String currentLocationFileURL = controllerUtils.getServerDataFolderPath() + "ServerLocation.data";
    public static final String serverGameDataFileURL = controllerUtils.getServerDataFolderPath() + "ServerGameData.data";
    public static final String serverLookupURL = controllerUtils.getServerDataFolderPath() + "ServerLookup.data";
    public static final String pedImageFolderURL = controllerUtils.getJarPath() + File.separator + "images" + File.separator + "peds";
    public static final String vehImageFolderURL = controllerUtils.getJarPath() + File.separator + "images" + File.separator + "vehicles";
    public static final String soundPackDownloadURL = "https://github.com/Guess1m/ReportsPlus/releases/latest/download/sounds-optional.zip";
    public static final String imagePackDownloadURL = "https://github.com/Guess1m/ReportsPlus/releases/latest/download/images-optional.zip";
}
