package com.drozal.dataterminal.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class ConfigWriter {
    public static void configwrite(String database, String value) {
        Properties prop = new Properties();
        OutputStream output = null;
        FileInputStream input = null;

        try {
            // Load existing properties
            input = new FileInputStream("config.properties");
            prop.load(input);
        } catch (IOException io) {
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
            // Append or overwrite the property value
            output = new FileOutputStream("config.properties");
            prop.setProperty("database." + database, value);

            // Save properties to the project root folder
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
