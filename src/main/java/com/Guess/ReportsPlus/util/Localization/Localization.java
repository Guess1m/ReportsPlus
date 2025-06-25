package com.Guess.ReportsPlus.util.Localization;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.logDebug;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logInfo;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getJarPath;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.Guess.ReportsPlus.util.Strings.updateStrings;

public class Localization {

	private final CommentedProperties properties = new CommentedProperties();
	private final String filePath = getJarPath() + "/locale/locale.properties";

	public Localization() {
		loadProperties();
	}

	public synchronized String getLocalizedMessage(String key, String defaultValue) {
		String value = properties.getProperty(key);

		if (value == null) {
			properties.put(key, defaultValue);
			saveProperties();
			value = defaultValue;
			logDebug("Locale Missing: [" + key + "="
					+ defaultValue + "]");
		}

		return value;
	}

	private void loadProperties() {
		File file = new File(filePath);
		File localizationFolder = new File(getJarPath() + "/locale");
		if (!localizationFolder.exists()) {
			localizationFolder.mkdirs();
			logInfo("Created Localization Folder");
		} else {
			logInfo("Localization Folder Already Exists");
		}
		if (file.exists()) {
			try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
				properties.load(reader);
			} catch (IOException e) {
				logError("Unable to load locale properties from " + filePath, e);
			}
		}
	}

	private void saveProperties() {
		try (OutputStream output = new FileOutputStream(filePath)) {
			properties.storeWithComments(output, "ReportPlus Localization For Version: " + updateStrings.version
					+ "\n# Reccomended to keep capital words capital");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
