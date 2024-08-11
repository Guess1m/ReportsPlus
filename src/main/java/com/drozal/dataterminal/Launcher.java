package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Misc.stringUtil;
import javafx.application.Platform;
import javafx.scene.text.Font;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.drozal.dataterminal.config.ConfigReader.checkAndSetDefaultValues;
import static com.drozal.dataterminal.util.Misc.LogUtils.*;
import static com.drozal.dataterminal.util.Misc.stringUtil.*;
import static com.drozal.dataterminal.util.Report.treeViewUtils.*;

public class Launcher {

    public static void main(String[] args) throws IOException {

        try {
            String filePath = stringUtil.getJarPath() + File.separator + "output.log";
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

        deleteFiles();

        loadFonts();

        createFilesFolders();

        copyInternalFiles();

        createDataLogsDir();

        if (ConfigReader.doesConfigExist()) {
            ConfigWriter.configwrite("uiSettings", "firstLogin", "false");

            checkAndSetDefaultValues();

            DataTerminalHomeApplication.main(args);
        } else {
            newOfficerApplication.main(args);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log("Shutdown Request Recieved", Severity.DEBUG);
            endLog();
            Platform.exit();
            System.exit(0);
        }));

    }

    public static void loadFonts() {
        Font.loadFont(Launcher.class.getResourceAsStream("fonts/seguibl.ttf"), 14);
        Font.loadFont(Launcher.class.getResourceAsStream("fonts/seguisb.ttf"), 14);
        Font.loadFont(Launcher.class.getResourceAsStream("fonts/Candara.ttf"), 14);
        Font.loadFont(Launcher.class.getResourceAsStream("fonts/Candara_Bold.ttf"), 14);
        Font.loadFont(Launcher.class.getResourceAsStream("fonts/Roboto-Regular.ttf"), 14);
        Font.loadFont(Launcher.class.getResourceAsStream("fonts/SansSerifFLF.otf"), 14);
        Font.loadFont(Launcher.class.getResourceAsStream("fonts/CONSOLA.ttf"), 14);
        Font.loadFont(Launcher.class.getResourceAsStream("fonts/CONSOLAB.ttf"), 14);
        Font.loadFont(Launcher.class.getResourceAsStream("fonts/arial.ttf"), 14);
        Font.loadFont(Launcher.class.getResourceAsStream("fonts/Blanka-Regular.otf"), 14);
        Font.loadFont(Launcher.class.getResourceAsStream("fonts/Segoe UI.ttf"), 14);
        Font.loadFont(Launcher.class.getResourceAsStream("fonts/Roboto Bold.ttf"), 14);
        Font.loadFont(Launcher.class.getResourceAsStream("fonts/Segoe UI Semibold.ttf"), 14);
        Font.loadFont(Launcher.class.getResourceAsStream("fonts/Signerica_Fat.ttf"), 14);
    }

    private static void deleteFiles() {
        try {
            String filePath = stringUtil.getJarPath() + File.separator + "serverData" + File.separator + "ServerCallout.xml";
            Path path = Path.of(filePath);
            if (Files.exists(path)) {
                Files.delete(path);
                log("Server callout file deleted successfully.", Severity.INFO);
            } else {
                log("Server callout file does not exist.", Severity.WARN);
            }
        } catch (IOException e) {
            logError("An error occurred while deleting the server callout file: ", e);
        }

        try {
            String filePath = stringUtil.getJarPath() + File.separator + "serverData" + File.separator + "ServerCurrentID.xml";
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
            String filePath = stringUtil.getJarPath() + File.separator + "serverData" + File.separator + "ServerWorldPeds.data";
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
            String filePath = stringUtil.getJarPath() + File.separator + "serverData" + File.separator + "ServerWorldCars.data";
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
    }

    private static void createFilesFolders() {
        String dataFolderPath = getJarPath() + File.separator + "data";
        String serverData = getJarPath() + File.separator + "serverData";

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

        File calloutDataFile = new File(calloutDataURL);
        if (!calloutDataFile.exists()) {
            log("Callout Data File Doesn't Exist, Creating", Severity.INFO);
            //noinspection ResultOfMethodCallIgnored
            try {
                calloutDataFile.createNewFile();
            } catch (IOException e) {
                logError("Could not create Callout Data File: ", e);
            }
        }

        File calloutHistoryFile = new File(calloutHistoryURL);
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
        File chargesFile = new File(chargesFilePath);
        String citationsFilePath = getJarPath() + File.separator + "data" + File.separator + "Citations.xml";
        File citationsFile = new File(citationsFilePath);
        String customizationFilePath = getJarPath() + File.separator + "data" + File.separator + "customization.json";
        File customizationFile = new File(customizationFilePath);
        if (!chargesFile.exists()) {
            try {
                copyChargeDataFile();
            } catch (IOException e) {
            }
        }
        if (!citationsFile.exists()) {
            try {
                copyCitationDataFile();
            } catch (IOException e) {
                logError("Could not copy CitationData file: ", e);
            }
        }
        if (!customizationFile.exists()) {
            try {
                copyCustomizationDataFile();
            } catch (IOException e) {
                logError("Could not copy Customization file: ", e);
            }
        }
    }

}