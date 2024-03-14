package com.drozal.dataterminal.config;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Properties;

public class ConfigReader {

    // TODO: Get rid of checkforValue not in use
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
        try {
            ProtectionDomain protectionDomain = ConfigReader.class.getProtectionDomain();
            CodeSource codeSource = protectionDomain.getCodeSource();
            if (codeSource != null) {
                URL jarUrl = codeSource.getLocation();
                String jarDirPath = new File(jarUrl.toURI()).getParent();
                try (InputStream input = new FileInputStream(jarDirPath + File.separator + "config.properties")) {
                    // Load a properties file
                    prop.load(input);
                    // Retrieve properties
                    return prop.getProperty("database." + property);
                }
            } else {
                throw new IOException("Unable to determine the location of the JAR file.");
            }
        } catch (URISyntaxException e) {
            throw new IOException("Error reading config.properties file.", e);
        }
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
            return configFile.exists();
        } catch (URISyntaxException | UnsupportedEncodingException e) {
            // Handle exception if URI syntax is incorrect or decoding fails
            e.printStackTrace();
            return false;
        }
    }
}
