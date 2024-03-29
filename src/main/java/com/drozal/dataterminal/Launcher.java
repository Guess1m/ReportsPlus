package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.util.stringUtil;
import javafx.scene.text.Font;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static com.drozal.dataterminal.util.treeViewUtils.copyChargeDataFile;
import static com.drozal.dataterminal.util.treeViewUtils.copyCitationDataFile;

public class Launcher {

    public static void main(String[] args) throws IOException {
        loadFonts();
        String folderPath = "";
        String dataFolderPath = stringUtil.getJarPath() + File.separator + "data";

        File dataFolder = new File(dataFolderPath);
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
            System.out.println("made data folder");
        } else {
            System.out.println("didnt make data folder it exists");
        }

        String chargesFilePath = stringUtil.getJarPath() + File.separator + "data" + File.separator + "Charges.xml";
        File chargesFile = new File(chargesFilePath);
        String citationsFilePath = stringUtil.getJarPath() + File.separator + "data" + File.separator + "Citations.xml";
        File citationsFile = new File(citationsFilePath);
        if (!chargesFile.exists()) {
            copyChargeDataFile();
            System.out.println("copied charge file over");
        }
        if (!citationsFile.exists()) {
            copyCitationDataFile();
            System.out.println("copied citation file over");
        }


        try {
            // Get the directory path where the JAR file is located
            String jarPath = Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            File jarFile = new File(jarPath);
            String jarDir = jarFile.getParent();

            // Append the folder name to the directory path
            folderPath = jarDir + File.separator + "DataLogs";
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // Create a File object representing the folder
        File folder = new File(folderPath);
        // Check if the folder already exists
        if (!folder.exists()) {
            // If the folder does not exist, create it
            boolean folderCreated = folder.mkdirs(); // Use mkdirs() to create parent directories if they don't exist
            if (folderCreated) {
                System.out.println("Folder created successfully.");
                System.out.println(folder.getAbsolutePath());
            } else {
                System.out.println("Failed to create the folder.");
            }
        } else {
            System.out.println("Folder already exists.");
            System.out.println(folder.getAbsolutePath());

        }

        if (ConfigReader.doesConfigExist()) {
            DataTerminalHomeApplication.main(args);
        } else {
            newOfficerApplication.main(args);
        }

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
    }
}