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
					
					prop.load(input);
					
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
			
			String jarPath = ConfigReader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			
			jarPath = URLDecoder.decode(jarPath, StandardCharsets.UTF_8);
			
			String jarDir = new File(jarPath).getParent();
			
			String configFilePath = jarDir + File.separator + "config.properties";
			
			File configFile = new File(configFilePath);
			return configFile.exists();
		} catch (URISyntaxException e) {
			logError("DoesConfigExist Error ", e);
			return false;
		}
	}
	
	public static boolean doesConfigValueExist(String property) {
		try {
			Properties prop = new Properties();
			ProtectionDomain protectionDomain = ConfigReader.class.getProtectionDomain();
			CodeSource codeSource = protectionDomain.getCodeSource();
			if (codeSource != null) {
				URL jarUrl = codeSource.getLocation();
				String jarDirPath = new File(jarUrl.toURI()).getParent();
				try (InputStream input = new FileInputStream(jarDirPath + File.separator + "config.properties")) {
					prop.load(input);
					
					return prop.containsKey("database." + property);
				}
			} else {
				log("Unable to determine the location of the JAR file ", LogUtils.Severity.ERROR);
				return false;
			}
		} catch (IOException | URISyntaxException e) {
			log("Error reading config.properties file ", LogUtils.Severity.ERROR);
			return false;
		}
	}
	
	private static void checkAndSetDefaultValue(String property, String defaultValue) {
		if (!doesConfigValueExist(property)) {
			ConfigWriter.configwrite(property, defaultValue);
			log("Loaded " + property + " with default value, " + defaultValue, LogUtils.Severity.DEBUG);
		}
	}
	
	public static void checkAndSetDefaultValues() {
		checkAndSetDefaultValue("UIDarkMode", "true");
		checkAndSetDefaultValue("AOTCallout", "true");
		checkAndSetDefaultValue("AOTClient", "true");
		checkAndSetDefaultValue("AOTDebug", "true");
		checkAndSetDefaultValue("AOTID", "true");
		checkAndSetDefaultValue("AOTMap", "true");
		checkAndSetDefaultValue("AOTNotes", "true");
		checkAndSetDefaultValue("AOTReport", "true");
		checkAndSetDefaultValue("AOTSettings", "true");
		checkAndSetDefaultValue("Agency", "Error");
		checkAndSetDefaultValue("Division", "Error");
		checkAndSetDefaultValue("Name", "Error");
		checkAndSetDefaultValue("Number", "Error");
		checkAndSetDefaultValue("Rank", "Error");
		checkAndSetDefaultValue("accentColor", "#9C95D0");
		checkAndSetDefaultValue("calloutDuration", "7");
		checkAndSetDefaultValue("IDDuration", "infinite");
		checkAndSetDefaultValue("firstLogin", "false");
		checkAndSetDefaultValue("fullscreenOnStartup", "true");
		checkAndSetDefaultValue("mainColor", "#524992");
		checkAndSetDefaultValue("mainWindowLayout", "Default");
		checkAndSetDefaultValue("notesWindowLayout", "Default");
		checkAndSetDefaultValue("reportAccent", "#263238");
		checkAndSetDefaultValue("reportBackground", "#505D62");
		checkAndSetDefaultValue("reportHeading", "#FFFFFF");
		checkAndSetDefaultValue("reportSecondary", "#323C41");
		checkAndSetDefaultValue("reportWindowDarkMode", "false");
		checkAndSetDefaultValue("reportWindowLayout", "Default");
		checkAndSetDefaultValue("secondaryColor", "#665CB6");
		checkAndSetDefaultValue("bkgColor", "#FFFFFF");
	}
	
}
