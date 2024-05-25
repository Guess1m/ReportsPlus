package com.drozal.dataterminal.config;

import com.drozal.dataterminal.util.Misc.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Properties;

import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;

public class ConfigReader {


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
                log("Unable to determine the location of the JAR file ", LogUtils.Severity.ERROR);

                throw new IOException("");
            }
        } catch (URISyntaxException e) {
            log("Error reading config.properties file ", LogUtils.Severity.ERROR);
            throw new IOException("Error reading config.properties file.", e);
        }
    }

    public static boolean doesConfigExist() {
        try {
            // Get the location of the JAR file
            String jarPath = ConfigReader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

            // Decode the URI path to handle spaces or special characters
            jarPath = URLDecoder.decode(jarPath, StandardCharsets.UTF_8);

            // Extract the directory path from the JAR path
            String jarDir = new File(jarPath).getParent();

            // Construct the path for the config.properties file
            String configFilePath = jarDir + File.separator + "config.properties";

            // Check if the config.properties file exists
            File configFile = new File(configFilePath);
            return configFile.exists();
        } catch (URISyntaxException e) {
            logError("DoesConfigExist Error ", e);
            return false;
        }
    }
}
