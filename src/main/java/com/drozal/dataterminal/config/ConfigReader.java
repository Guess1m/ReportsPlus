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

/**
 * The type Config reader.
 */
public class ConfigReader {
	
	/**
	 * Config read string.
	 *
	 * @param property the property
	 * @return the string
	 * @throws IOException the io exception
	 */
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
	
	/**
	 * Does config exist boolean.
	 *
	 * @return the boolean
	 */
	public static boolean doesConfigExist() {
		try {
			
			String jarPath = ConfigReader.class.getProtectionDomain()
					.getCodeSource()
					.getLocation()
					.toURI()
					.getPath();
			
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
}
