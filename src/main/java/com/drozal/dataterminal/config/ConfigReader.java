package com.drozal.dataterminal.config;

import java.io.*;
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
        } else if (file.exists() == false) {
            try {
                Properties prop = new Properties();
                OutputStream output = new FileOutputStream("config.properties");
                prop.store(output, null);
                output.close();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }
}
