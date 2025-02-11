package com.Guess.ReportsPlus.util.Localization;

import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.Guess.ReportsPlus.util.Strings.updateStrings;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getJarPath;

public class Localization {

    private final CommentedProperties properties = new CommentedProperties();
    private final String filePath = getJarPath() + "/locale/locale.properties";

    public Localization() {
        loadProperties();
    }

    private void loadProperties() {
        File file = new File(filePath);
        File localizationFolder = new File(getJarPath() + "/locale");
        if (!localizationFolder.exists()) {
            localizationFolder.mkdirs();
            log("Created Localization Folder", LogUtils.Severity.INFO);
        } else {
            log("Localization Folder Already Exists", LogUtils.Severity.INFO);
        }
        if (file.exists()) {
            try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
                properties.load(reader);
            } catch (IOException e) {
                logError("Unable to load locale properties from " + filePath, e);
            }
        }
    }

    public synchronized String getLocalizedMessage(String key, String defaultValue) {
        String value = properties.getProperty(key);

        if (value == null) {
            properties.put(key, defaultValue);
            saveProperties();
            value = defaultValue;
            log("Value not found for: " + key + ", using default", LogUtils.Severity.INFO);
        }

        return value;
    }

    private void saveProperties() {
        try (OutputStream output = new FileOutputStream(filePath)) {
            properties.storeWithComments(output, "ReportPlus Localization For Version: " + updateStrings.version + "\n# Reccomended to keep capital words capital");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
