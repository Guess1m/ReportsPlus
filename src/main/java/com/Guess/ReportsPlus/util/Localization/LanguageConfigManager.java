package com.Guess.ReportsPlus.util.Localization;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.util.Misc.LogUtils;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getJarPath;

import com.Guess.ReportsPlus.config.ConfigWriter;
import java.io.IOException;

public class LanguageConfigManager {

    public static List<String> getLanguageCodes() {

            checkAndSetLanguageConfig(true);

            List<String> languageCodes = new ArrayList<>();
            String directoryPath = getJarPath() + "/locale/";
            File directory = new File(directoryPath);

            // Check if the directory exists
            if (!directory.exists() || !directory.isDirectory()) {
                log("Directory does not exist or is not a directory: " + directoryPath, LogUtils.Severity.ERROR);
                return languageCodes;
            }

            // Check for locale.properties file and add "default"
            File localeFile = new File(directoryPath + File.separator + "locale.properties");
            if (localeFile.exists()) {
                log("Find Default Language", LogUtils.Severity.INFO);
                languageCodes.add("default");
            }

            // List all files in the directory
            File[] files = directory.listFiles((dir, name) -> name.startsWith("locale_") && name.endsWith(".properties"));

            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    // Extract the language code from the file name (excluding locale.properties)
                    if (!fileName.equals("locale.properties")) {
                        String languageCode = fileName.substring("locale_".length(), fileName.lastIndexOf(".properties"));
                        log("Find Language: " + languageCode, LogUtils.Severity.INFO);
                        languageCodes.add(languageCode);
                    }
                }
            }

            return languageCodes;
        }

    public static String checkAndSetLanguageConfig(boolean isCheck) {
        if (ConfigReader.doesConfigExist()){
            String language = null;
            try {
                language = ConfigReader.configRead("userInfo", "languageDuration");
            } catch (IOException e) {
                logError("IOException Error" , e);
            }
            if (isCheck){
                if (language == null || language.isEmpty()) {
                    log("Language set default",LogUtils.Severity.INFO);
                    ConfigWriter.configwrite("userInfo", "languageDuration", "Default");
                }
            }
            else {
                return language;
            }
        }
        return null;
    }

    public static String generateLocalizedFileName(){
        String language = checkAndSetLanguageConfig(false);

        if ("Default".equalsIgnoreCase(language)) {
            return "locale.properties";
        } else {
            return "locale_" + language.toLowerCase() + ".properties";
        }
    }

}
