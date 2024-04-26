package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.util.LogUtils;
import com.drozal.dataterminal.util.stringUtil;
import javafx.application.Platform;
import javafx.scene.text.Font;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.drozal.dataterminal.util.LogUtils.*;
import static com.drozal.dataterminal.util.stringUtil.getJarPath;
import static com.drozal.dataterminal.util.treeViewUtils.copyChargeDataFile;
import static com.drozal.dataterminal.util.treeViewUtils.copyCitationDataFile;

public class Launcher {

    public static void main(String[] args) throws IOException {
        try {
            String filePath = stringUtil.getJarPath() + File.separator + "output.log";
            Files.write(Path.of(filePath), new byte[0]); // Write empty bytes to clear the file
            log("Log file cleared successfully.", Severity.INFO);
        } catch (IOException e) {
            System.out.println("An error occurred while clearing the log file: " + e.getMessage());
        }

        loadFonts();

        String folderPath = "";
        String dataFolderPath = getJarPath() + File.separator + "data";

        File dataFolder = new File(dataFolderPath);
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
            log("Created Data Folder", LogUtils.Severity.INFO);
        } else {
            log("Data Folder Already Exists", LogUtils.Severity.WARN);
        }

        String chargesFilePath = getJarPath() + File.separator + "data" + File.separator + "Charges.xml";
        File chargesFile = new File(chargesFilePath);
        String citationsFilePath = getJarPath() + File.separator + "data" + File.separator + "Citations.xml";
        File citationsFile = new File(citationsFilePath);
        if (!chargesFile.exists()) {
            copyChargeDataFile();
        }
        if (!citationsFile.exists()) {
            copyCitationDataFile();
        }


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
            log("DataLogs Folder already exists.", LogUtils.Severity.WARN);
        }

        if (ConfigReader.doesConfigExist()) {
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

}