package com.drozal.dataterminal.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

        // Do something with the retrieved properties

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
        File file = new File("config.properties");
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }
}
