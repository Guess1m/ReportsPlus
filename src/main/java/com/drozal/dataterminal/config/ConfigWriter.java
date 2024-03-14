package com.drozal.dataterminal.config;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ConfigWriter {

    public static void configwrite(String database, String value) {
        Properties prop = new Properties();
        OutputStream output = null;
        FileInputStream input = null;
        String configFilePath = null; // Declare configFilePath outside the try-catch block

        try {
            // Get the location of the JAR file
            String jarPath = ConfigWriter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

            // Decode the URI path to handle spaces or special characters
            jarPath = URLDecoder.decode(jarPath, StandardCharsets.UTF_8);

            // Extract the directory path from the JAR path
            String jarDir = new File(jarPath).getParent();

            // Construct the path for the config.properties file
            configFilePath = jarDir + File.separator + "config.properties";

            // Load existing properties
            input = new FileInputStream(configFilePath);
            prop.load(input);
        } catch (IOException | URISyntaxException io) {
            io.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            // Set the new property value
            prop.setProperty("database." + database, value);

            // Write the updated properties to the file
            output = new FileOutputStream(configFilePath);
            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
