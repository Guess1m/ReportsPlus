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
            System.out.println("blank");
            return false;
        } else if (prop.getProperty("database." + database).isEmpty()) {
            System.out.println("empty");
            return false;
        } else if (prop.getProperty("database." + database).matches("value")) {
            System.out.println("matches");
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
        System.out.println("File path: " + file.getAbsolutePath()); // Print file path for debugging
        if (file.exists()) {
            System.out.println("Properties file already exists.");
            return true;
        } else if (file.exists() == false) {
            System.out.println("No properties file, creating..");
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
            System.out.println("else statement");
            return false;
        }
    }
}
