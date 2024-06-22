package com.drozal.dataterminal.config;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

import static com.drozal.dataterminal.util.Misc.LogUtils.logError;

public class ConfigWriter {
	
	public static void configwrite(String database, String value) {
		Properties prop = new Properties();
		OutputStream output = null;
		FileInputStream input = null;
		String configFilePath = null;
		
		try {
			String jarPath = ConfigWriter.class.getProtectionDomain()
			                                   .getCodeSource()
			                                   .getLocation()
			                                   .toURI()
			                                   .getPath();
			
			jarPath = URLDecoder.decode(jarPath, StandardCharsets.UTF_8);
			
			String jarDir = new File(jarPath).getParent();
			
			configFilePath = jarDir + File.separator + "config.properties";
			
			input = new FileInputStream(configFilePath);
			prop.load(input);
		} catch (IOException | URISyntaxException e) {
			logError("ConfigWrite Error Code 1 ", e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logError("ConfigWrite Error Code 2 ", e);
				}
			}
		}
		
		try {
			
			prop.setProperty("database." + database, value);
			
			output = new FileOutputStream(Objects.requireNonNull(configFilePath));
			prop.store(output, null);
		} catch (IOException e) {
			logError("ConfigWrite Error Code 3 ", e);
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					logError("ConfigWrite Error Code 4 ", e);
				}
			}
		}
	}
}
