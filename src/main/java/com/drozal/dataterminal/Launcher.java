package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;

import java.io.File;
import java.io.IOException;

public class Launcher {
    public static void main(String[] args) throws IOException {

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