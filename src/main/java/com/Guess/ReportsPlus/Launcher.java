package com.Guess.ReportsPlus;

import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.config.ConfigWriter;
import com.Guess.ReportsPlus.util.Localization.Localization;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
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

import static com.Guess.ReportsPlus.Desktop.Utils.AppUtils.AppConfig.appConfig.*;
import static com.Guess.ReportsPlus.config.ConfigReader.checkAndSetDefaultValues;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.*;

public class Launcher {
	public static Localization localization;
	
	public static void main(String[] args) {
		
		try {
			String filePath = controllerUtils.getJarPath() + File.separator + "output.log";
			Path path = Path.of(filePath);
			if (Files.exists(path)) {
				Files.write(path, new byte[0]);
				log("Log file cleared successfully.", Severity.INFO);
			} else {
				log("Log file does not exist.", Severity.WARN);
			}
		} catch (IOException e) {
			logError("An error occurred while clearing the log file: ", e);
		}
		
		localization = new Localization();
		
		deleteFiles();
		
		loadFonts();
		
		createFilesFolders();
		
		copyInternalFiles();
		
		createDataLogsDir();
		
		if (ConfigReader.doesConfigExist()) {
			ConfigWriter.configwrite("uiSettings", "firstLogin", "false");
			
			checkAndSetDefaultValues();
			if (!doesAppConfigExist()) {
				createAppConfig();
			}
			
			checkAndSetDefaultAppValues();
			
			MainApplication.main(args);
		} else {
			newOfficerApplication.main(args);
		}
		
	}
	
	public static void loadFonts() {
		System.setProperty("prism.lcdtext", "false");
		System.setProperty("prism.text", "t2k");
		
		ArrayList<Font> fonts = new ArrayList<>(Arrays.asList(Font.loadFont(Launcher.class.getResource("fonts/InterBold.ttf").toExternalForm(), 28), Font.loadFont(Launcher.class.getResource("fonts/InterRegular.ttf").toExternalForm(), 28), Font.loadFont(Launcher.class.getResource("fonts/InterSemibold.ttf").toExternalForm(), 28)));
		for (Font f : fonts) {
			log("initialization; Loaded font: [" + f.getName() + "] Family: [" + f.getFamily() + "] Style: [" + f.getStyle() + "]", LogUtils.Severity.INFO);
		}
	}
	
	private static void deleteFiles() {
		try {
			String filePath = controllerUtils.getServerDataFolderPath() + "ServerCallout.xml";
			Path path = Path.of(filePath);
			if (Files.exists(path)) {
				Files.delete(path);
				log("Server files deleted successfully.", Severity.INFO);
			} else {
				log("Server files do not exist.", Severity.WARN);
			}
			Path path2 = Path.of(URLStrings.IDHistoryURL);
			if (Files.exists(path2)) {
				Files.delete(path2);
				log("IDHistory file deleted successfully.", Severity.INFO);
			} else {
				log("IDHistory file does not exist.", Severity.WARN);
			}
		} catch (IOException e) {
			logError("Error while deleting IDHistory file: ", e);
		}
		
		try {
			String filePath = URLStrings.currentIDFileURL;
			Path path = Path.of(filePath);
			if (Files.exists(path)) {
				Files.delete(path);
				log("Server current ID file deleted successfully.", Severity.INFO);
			} else {
				log("Server current ID file does not exist.", Severity.WARN);
			}
		} catch (IOException e) {
			logError("An error occurred while deleting the server current ID file: ", e);
		}
		
		try {
			String filePath = URLStrings.serverGameDataFileURL;
			Path path = Path.of(filePath);
			if (Files.exists(path)) {
				Files.delete(path);
				log("server gameData file deleted successfully.", Severity.INFO);
			} else {
				log("server gameData file does not exist.", Severity.WARN);
			}
		} catch (IOException e) {
			logError("An error occurred while deleting the server gameData file: ", e);
		}
		
		try {
			String filePath = URLStrings.currentLocationFileURL;
			Path path = Path.of(filePath);
			if (Files.exists(path)) {
				Files.delete(path);
				log("Server Location file deleted successfully.", Severity.INFO);
			} else {
				log("Server Location file does not exist.", Severity.WARN);
			}
		} catch (IOException e) {
			logError("An error occurred while deleting the server Location file: ", e);
		}
		
		try {
			String filePath = controllerUtils.getServerDataFolderPath() + "ServerWorldPeds.data";
			Path path = Path.of(filePath);
			if (Files.exists(path)) {
				Files.delete(path);
				log("Server world peds file deleted successfully.", Severity.INFO);
			} else {
				log("Server world peds file does not exist.", Severity.WARN);
			}
		} catch (IOException e) {
			logError("An error occurred while deleting the server world peds file: ", e);
		}
		
		try {
			String filePath = controllerUtils.getServerDataFolderPath() + "ServerWorldCars.data";
			Path path = Path.of(filePath);
			if (Files.exists(path)) {
				Files.delete(path);
				log("Server world cars file deleted successfully.", Severity.INFO);
			} else {
				log("Server world cars file does not exist.", Severity.WARN);
			}
		} catch (IOException e) {
			logError("An error occurred while deleting the server world cars file: ", e);
		}
		
		try {
			String filePath = controllerUtils.getServerDataFolderPath() + "ServerTrafficStop.data";
			Path path = Path.of(filePath);
			if (Files.exists(path)) {
				Files.delete(path);
				log("Server traffic stop file deleted successfully.", Severity.INFO);
			} else {
				log("Server traffic stop file does not exist.", Severity.WARN);
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
			log("Created Data Folder", LogUtils.Severity.INFO);
		} else {
			log("Data Folder Already Exists", LogUtils.Severity.INFO);
		}
		
		File serverDataFolder = new File(serverData);
		if (!serverDataFolder.exists()) {
			serverDataFolder.mkdirs();
			log("Created Server Data Folder", LogUtils.Severity.INFO);
		} else {
			log("Server Data Folder Already Exists", LogUtils.Severity.INFO);
		}
		
		File calloutDataFile = new File(URLStrings.calloutDataURL);
		if (!calloutDataFile.exists()) {
			log("Callout Data File Doesn't Exist, Creating", Severity.INFO);
			try {
				calloutDataFile.createNewFile();
			} catch (IOException e) {
				logError("Could not create Callout Data File: ", e);
			}
		}
		
		File calloutHistoryFile = new File(URLStrings.calloutHistoryURL);
		if (!calloutHistoryFile.exists()) {
			log("Callout History File Doesn't Exist, Creating", Severity.INFO);
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
				log("DataLogs: " + folder.getAbsolutePath(), LogUtils.Severity.INFO);
			} else {
				log("Failed to create the DataLogs Folder.", LogUtils.Severity.ERROR);
			}
		} else {
			log("DataLogs Folder already exists.", LogUtils.Severity.INFO);
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
