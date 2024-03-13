package com.drozal.dataterminal.config;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.Properties;

public class ConfigReader {

    public static Boolean checkforValue(String database, String value) throws IOException {

        Properties prop = new Properties();
        InputStream input = null;

        input = new FileInputStream("config.properties");

        // Load a properties file
        prop.load(input);

        // check if its blank
        if (prop.getProperty("database." + database).isBlank()) {
            return false;
        } else if (prop.getProperty("database." + database).isEmpty()) {
            return false;
        } else if (prop.getProperty("database." + database).matches("value")) {
            return true;
        }
        return null;
    }

    public static String configRead(String property) throws IOException {
        Properties prop = new Properties();
        InputStream input = null;
        input = new FileInputStream("config.properties");
        // Load a properties file
        prop.load(input);
        // Retrieve properties
        return prop.getProperty("database." + property);
    }

    public static boolean doesConfigExist() {
        try {
            // Get the location of the JAR file
            String jarPath = ConfigReader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

            // Decode the URI path to handle spaces or special characters
            jarPath = URLDecoder.decode(jarPath, "UTF-8");

            // Extract the directory path from the JAR path
            String jarDir = new File(jarPath).getParent();

            // Construct the path for the config.properties file
            String configFilePath = jarDir + File.separator + "config.properties";

            // Check if the config.properties file exists
            File configFile = new File(configFilePath);
            System.out.println(configFile.getAbsolutePath());
            return configFile.exists();
        } catch (URISyntaxException | UnsupportedEncodingException e) {
            // Handle exception if URI syntax is incorrect or decoding fails
            e.printStackTrace();
            return false;
        }
    }
}
