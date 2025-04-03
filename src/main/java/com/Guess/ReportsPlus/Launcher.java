package com.Guess.ReportsPlus;

import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.util.Localization.Localization;
import com.Guess.ReportsPlus.util.Other.controllerUtils;
import com.Guess.ReportsPlus.util.Strings.URLStrings;
import javafx.scene.text.Font;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import static com.Guess.ReportsPlus.Desktop.Utils.AppUtils.AppConfig.appConfig.checkAndSetDefaultAppValues;
import static com.Guess.ReportsPlus.Desktop.Utils.AppUtils.AppConfig.appConfig.createAppConfig;
import static com.Guess.ReportsPlus.config.ConfigReader.checkAndSetDefaultValues;
import static com.Guess.ReportsPlus.config.ConfigReader.createConfig;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.*;
import static com.Guess.ReportsPlus.util.Strings.customizationDataLoader.loadJsonData;

public class Launcher {
	public static Localization localization;
	
	public static void main(String[] args) {
		try {
			String filePath = controllerUtils.getJarPath() + File.separator + "output.log";
			Path path = Path.of(filePath);
			if (Files.exists(path)) {
				Files.write(path, new byte[0]);
			}
		} catch (IOException e) {
			logError("An error occurred while clearing the log file: ", e);
		}
		
		initLogging();
		
		loadJsonData();
		
		localization = new Localization();
		
		deleteFiles();
		
		loadFonts();
		
		createFilesFolders();
		
		copyInternalFiles();
		
		createDataLogsDir();
		
		createConfig();
		createAppConfig();
		
		checkAndSetDefaultValues();
		checkAndSetDefaultAppValues();
		
		try {
			if (ConfigReader.configRead("uiSettings", "skipOfficerLogin").equalsIgnoreCase("true")) {
				logDebug("skipOfficerLogin is true, trying to open main desktop..");
				logDebug("Trying to login with officer name: [" + ConfigReader.configRead("userInfo", "Name") + "]");
				MainApplication.main(args);
			} else {
				logDebug("skipOfficerLogin is false, opening login screen");
				newOfficerApplication.main(args);
			}
		} catch (IOException e) {
			logError("Error opening main desktop / reading uiSettings.skipOfficerLogin: " + e);
			newOfficerApplication.main(args);
		} catch (NullPointerException e) {
			logWarn("skipOfficerLogin null, Likely first launch, running login window: ");
			newOfficerApplication.main(args);
		}
	}
	
	public static void loadFonts() {
		System.setProperty("prism.lcdtext", "false");
		System.setProperty("prism.text", "t2k");
		
		ArrayList<Font> fonts = new ArrayList<>(
				Arrays.asList(Font.loadFont(Launcher.class.getResource("fonts/InterBold.ttf").toExternalForm(), 28), Font.loadFont(Launcher.class.getResource("fonts/InterRegular.ttf").toExternalForm(), 28), Font.loadFont(Launcher.class.getResource("fonts/InterSemibold.ttf").toExternalForm(), 28)));
		for (Font f : fonts) {
			logInfo("initialization; Loaded font: [" + f.getName() + "] Family: [" + f.getFamily() + "] Style: [" + f.getStyle() + "]");
		}
	}
	
	private static void deleteFiles() {
		try {
			String filePath = controllerUtils.getServerDataFolderPath() + "ServerCallout.xml";
			Path path = Path.of(filePath);
			if (Files.exists(path)) {
				Files.delete(path);
				logInfo("Server files deleted successfully.");
			} else {
				logWarn("Server files do not exist.");
			}
			Path path2 = Path.of(URLStrings.IDHistoryURL);
			if (Files.exists(path2)) {
				Files.delete(path2);
				logInfo("IDHistory file deleted successfully.");
			} else {
				logWarn("IDHistory file does not exist.");
			}
		} catch (IOException e) {
			logError("Error while deleting IDHistory file: ", e);
		}
		
		try {
			String filePath = URLStrings.currentIDFileURL;
			Path path = Path.of(filePath);
			if (Files.exists(path)) {
				Files.delete(path);
				logInfo("Server current ID file deleted successfully.");
			} else {
				logWarn("Server current ID file does not exist.");
			}
		} catch (IOException e) {
			logError("An error occurred while deleting the server current ID file: ", e);
		}
		
		try {
			String filePath = URLStrings.serverGameDataFileURL;
			Path path = Path.of(filePath);
			if (Files.exists(path)) {
				Files.delete(path);
				logInfo("server gameData file deleted successfully.");
			} else {
				logWarn("server gameData file does not exist.");
			}
		} catch (IOException e) {
			logError("An error occurred while deleting the server gameData file: ", e);
		}
		
		try {
			String filePath = URLStrings.currentLocationFileURL;
			Path path = Path.of(filePath);
			if (Files.exists(path)) {
				Files.delete(path);
				logInfo("Server Location file deleted successfully.");
			} else {
				logWarn("Server Location file does not exist.");
			}
		} catch (IOException e) {
			logError("An error occurred while deleting the server Location file: ", e);
		}
		
		try {
			String filePath = controllerUtils.getServerDataFolderPath() + "ServerWorldPeds.data";
			Path path = Path.of(filePath);
			if (Files.exists(path)) {
				Files.delete(path);
				logInfo("Server world peds file deleted successfully.");
			} else {
				logWarn("Server world peds file does not exist.");
			}
		} catch (IOException e) {
			logError("An error occurred while deleting the server world peds file: ", e);
		}
		
		try {
			String filePath = controllerUtils.getServerDataFolderPath() + "ServerWorldCars.data";
			Path path = Path.of(filePath);
			if (Files.exists(path)) {
				Files.delete(path);
				logInfo("Server world cars file deleted successfully.");
			} else {
				logWarn("Server world cars file does not exist.");
			}
		} catch (IOException e) {
			logError("An error occurred while deleting the server world cars file: ", e);
		}
		
		try {
			String filePath = controllerUtils.getServerDataFolderPath() + "ServerALPR.data";
			Path path = Path.of(filePath);
			if (Files.exists(path)) {
				Files.delete(path);
				logInfo("Server ALPR file deleted successfully.");
			} else {
				logWarn("Server ALPR file does not exist.");
			}
		} catch (IOException e) {
			logError("An error occurred while deleting the server ALPR file: ", e);
		}
		
		try {
			String filePath = controllerUtils.getServerDataFolderPath() + "ServerTrafficStop.data";
			Path path = Path.of(filePath);
			if (Files.exists(path)) {
				Files.delete(path);
				logInfo("Server traffic stop file deleted successfully.");
			} else {
				logWarn("Server traffic stop file does not exist.");
			}
		} catch (IOException e) {
			logError("An error occurred while deleting the server traffic stop file: ", e);
		}
	}
	
	private static void createFilesFolders() {
		String dataFolderPath = controllerUtils.getJarPath() + File.separator + "data";
		String serverData = controllerUtils.getJarPath() + File.separator + "serverData";
		
		File dataFolder = new File(dataFolderPath);
		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
			logInfo("Created Data Folder");
		} else {
			logInfo("Data Folder Already Exists");
		}
		
		File serverDataFolder = new File(serverData);
		if (!serverDataFolder.exists()) {
			serverDataFolder.mkdirs();
			logInfo("Created Server Data Folder");
		} else {
			logInfo("Server Data Folder Already Exists");
		}
		
		File calloutDataFile = new File(URLStrings.calloutDataURL);
		if (!calloutDataFile.exists()) {
			logInfo("Callout Data File Doesn't Exist, Creating");
			try {
				calloutDataFile.createNewFile();
			} catch (IOException e) {
				logError("Could not create Callout Data File: ", e);
			}
		}
		
		File calloutHistoryFile = new File(URLStrings.calloutHistoryURL);
		if (!calloutHistoryFile.exists()) {
			logInfo("Callout History File Doesn't Exist, Creating");
			try {
				calloutHistoryFile.createNewFile();
			} catch (IOException e) {
				logError("Could not create Callout Data File: ", e);
			}
		}
	}
	
	private static void createDataLogsDir() {
		String folderPath = "";
		try {
			String jarPath = Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			File jarFile = new File(jarPath);
			String jarDir = jarFile.getParent();
			folderPath = jarDir + File.separator + "DataLogs";
		} catch (URISyntaxException e) {
			logError("JarDir Syntax Error ", e);
		}
		File folder = new File(folderPath);
		if (!folder.exists()) {
			boolean folderCreated = folder.mkdirs();
			if (folderCreated) {
				logInfo("DataLogs: " + folder.getAbsolutePath());
			} else {
				logError("Failed to create the DataLogs Folder.");
			}
		} else {
			logInfo("DataLogs Folder already exists.");
		}
	}
	
	private static void copyInternalFiles() {
		File chargesFile = new File(URLStrings.chargesFilePath);
		String citationsFilePath = controllerUtils.getDataFolderPath() + "Citations.xml";
		File citationsFile = new File(citationsFilePath);
		String customizationFilePath = controllerUtils.getDataFolderPath() + "customization.json";
		File customizationFile = new File(customizationFilePath);
		if (!chargesFile.exists()) {
			try {
				controllerUtils.copyChargeDataFile();
			} catch (IOException e) {
			}
		}
		if (!citationsFile.exists()) {
			try {
				controllerUtils.copyCitationDataFile();
			} catch (IOException e) {
				logError("Could not copy CitationData file: ", e);
			}
		}
		if (!customizationFile.exists()) {
			try {
				controllerUtils.copyCustomizationDataFile();
			} catch (IOException e) {
				logError("Could not copy Customization file: ", e);
			}
		}
	}
}
