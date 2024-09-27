package com.drozal.dataterminal.Desktop.Utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PositionSaver {
	
	private static final String FILE_PATH = "app_positions.properties";
	private static Properties properties = new Properties();
	
	/**
	 * Saves the position of the app (X, Y) to a properties file.
	 *
	 * @param appName Name of the app
	 * @param x       X coordinate
	 * @param y       Y coordinate
	 */
	public static void savePosition(String appName, double x, double y) {
		properties.setProperty(appName + ".x", String.valueOf(x));
		properties.setProperty(appName + ".y", String.valueOf(y));
		try (FileOutputStream outputStream = new FileOutputStream(FILE_PATH)) {
			properties.store(outputStream, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads the position (X, Y) for the given app from the properties file.
	 *
	 * @param appName Name of the app
	 * @return A double array with X and Y coordinates [x, y]
	 */
	public static double[] loadPosition(String appName) {
		try (FileInputStream inputStream = new FileInputStream(FILE_PATH)) {
			properties.load(inputStream);
			String x = properties.getProperty(appName + ".x", "0");
			String y = properties.getProperty(appName + ".y", "0");
			return new double[]{Double.parseDouble(x), Double.parseDouble(y)};
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new double[]{0, 0};  // Default position if no saved data
	}
}
