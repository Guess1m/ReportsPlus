package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import javafx.scene.text.Font;

import java.io.File;
import java.io.IOException;

public class Launcher {

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
    }

    public static void main(String[] args) throws IOException {
        loadFonts();
        if (ConfigReader.doesConfigExist()) {

            // Specify the path of the folder to be created
            String folderPath = "DataLogs";

            // Create a File object representing the folder
            File folder = new File(folderPath);

            // Check if the folder already exists
            if (!folder.exists()) {
                // If the folder does not exist, create it
                boolean folderCreated = folder.mkdirs(); // Use mkdir() for a single directory

                if (folderCreated) {
                    System.out.println("Folder created successfully.");
                    DataTerminalHomeApplication.main(args);

                } else {
                    System.out.println("Failed to create the folder.");
                }
            } else {
                System.out.println("Folder already exists.");
                DataTerminalHomeApplication.main(args);

            }
        } else {

            String folderPath = "DataLogs";
            // Create a File object representing the folder
            File folder = new File(folderPath);
            // Check if the folder already exists
            if (!folder.exists()) {
                // If the folder does not exist, create it
                boolean folderCreated = folder.mkdirs(); // Use mkdir() for a single directory
                if (folderCreated) {
                    System.out.println("Folder created successfully.");
                    newOfficerApplication.main(args);
                } else {
                    System.out.println("Failed to create the folder.");
                }
            } else {
                System.out.println("Folder already exists.");
                newOfficerApplication.main(args);
            }

        }
    }
}