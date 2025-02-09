package com.Guess.ReportsPlus.util.Other;

import com.Guess.ReportsPlus.Windows.Apps.LogViewController;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.config.ConfigWriter;
import com.Guess.ReportsPlus.util.History.Ped;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.Guess.ReportsPlus.util.Report.treeViewUtils;
import com.Guess.ReportsPlus.util.Server.ClientUtils;
import com.Guess.ReportsPlus.util.Strings.updateStrings;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.*;
import static com.Guess.ReportsPlus.util.Misc.AudioUtil.audioExecutor;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.*;

public class controllerUtils {
	
	private static final String[][] keys = {{"-name", "-na", "-n", "-fullname", "-fname"}, {"-number", "-num", "-nu"}, {"-age", "-years", "-birthdate", "-a", "-dob"}, {"-address", "-addr", "-place", "-add", "-ad"}, {"-model", "-mod", "-mo", "-m"}, {"-plate", "-platenum", "-plt", "-p"}, {"-gender", "-sex", "-g", "-gen"}, {"-area", "-region", "-zone", "-ar"}, {"-county", "-cty", "-cnty", "-ct", "-c"}, {"-notes", "-nts", "-note", "-comments", "-cmts"}, {"-description", "-des", "-desc", "-d"}, {"-searchitems", "-si", "-search", "-srch", "-items",}, {"-street", "-st", "-road", "-dr", "-strt"}, {"-type", "-typ", "-tpe"}};
	
	private static Map<String, String> pullNotesValues(String notepad) {
		Map<String, String> values = new HashMap<>();
		String text = notepad;
		
		// Extract values for predefined keys
		for (String[] keyGroup : keys) {
			for (String key : keyGroup) {
				String value = extractValue(text, key);
				if (value != null) {
					for (String k : keyGroup) {
						values.put(k, value); // Store value under all known aliases
					}
					break;
				}
			}
		}
		
		// Extract values for keys that are not in the predefined keys array
		Pattern pattern = Pattern.compile("(-\\w+)\\s+([^\\-]+)");
		Matcher matcher = pattern.matcher(text);
		
		while (matcher.find()) {
			String key = matcher.group(1).trim();   // Key (e.g., "-num")
			String value = matcher.group(2).trim(); // Value (e.g., "3")
			
			if (!values.containsKey(key)) { // Only add if not already extracted via predefined keys
				values.put(key, value);
			}
		}
		
		return values;
	}
	
	private static String extractValue(String text, String key) {
		Pattern pattern = Pattern.compile(key + "\\s+(.*?)(?=\\s+-|$)");
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}
	
	public static void updateTextFromNotepad(TextField textField, TextArea notepadText, String... keys) {
		
		// Get values from notepad
		Map<String, String> values = pullNotesValues(notepadText.getText());
		
		String extractedValue = null;
		
		// Loop through the provided keys to extract corresponding values
		for (String key : keys) {
			extractedValue = values.get(key);
			if (extractedValue != null && !extractedValue.trim().isEmpty()) {
				break;
			}
			
			// Check for alternative keys (if any) by looping through map entries
			for (Map.Entry<String, String> entry : values.entrySet()) {
				for (String altKey : entry.getKey().split("\\|")) {
					if (altKey.equals(key)) {
						extractedValue = entry.getValue();
						if (extractedValue != null && !extractedValue.trim().isEmpty()) {
							break;
						}
					}
				}
				if (extractedValue != null && !extractedValue.trim().isEmpty()) {
					break;
				}
			}
			
			if (extractedValue != null && !extractedValue.trim().isEmpty()) {
				break;
			}
		}
		
		// If a valid extracted value is found, update the TextField
		if (extractedValue != null && !extractedValue.trim().isEmpty()) {
			textField.setText(extractedValue);
		}
	}
	
	public static void updateTextFromNotepad(TextArea textArea, TextArea notepadText, String... keys) {
		Map<String, String> values = pullNotesValues(notepadText.getText());
		String extractedValue = null;
		
		for (String key : keys) {
			extractedValue = values.get(key);
			if (extractedValue != null && !extractedValue.trim().isEmpty()) {
				break;
			}
			
			for (Map.Entry<String, String> entry : values.entrySet()) {
				for (String altKey : entry.getKey().split("\\|")) {
					if (altKey.equals(key)) {
						extractedValue = entry.getValue();
						if (extractedValue != null && !extractedValue.trim().isEmpty()) {
							break;
						}
					}
				}
				if (extractedValue != null && !extractedValue.trim().isEmpty()) {
					break;
				}
			}
			if (extractedValue != null && !extractedValue.trim().isEmpty()) {
				break;
			}
		}
		
		if (extractedValue != null && !extractedValue.trim().isEmpty()) {
			textArea.setText(extractedValue);
		}
	}
	
	public static void updateTextFromNotepad(ComboBox comboBox, TextArea notepadText, String... keys) {
		Map<String, String> values = pullNotesValues(notepadText.getText());
		String extractedValue = null;
		
		for (String key : keys) {
			extractedValue = values.get(key);
			if (extractedValue != null && !extractedValue.trim().isEmpty()) {
				break;
			}
			
			for (Map.Entry<String, String> entry : values.entrySet()) {
				for (String altKey : entry.getKey().split("\\|")) {
					if (altKey.equals(key)) {
						extractedValue = entry.getValue();
						if (extractedValue != null && !extractedValue.trim().isEmpty()) {
							break;
						}
					}
				}
				if (extractedValue != null && !extractedValue.trim().isEmpty()) {
					break;
				}
			}
			if (extractedValue != null && !extractedValue.trim().isEmpty()) {
				break;
			}
		}
		
		if (extractedValue != null && !extractedValue.trim().isEmpty()) {
			comboBox.setValue(extractedValue);
		}
	}
	
	public static String toTitleCase(String input) {
		if (input != null && !input.isEmpty()) {
			
			String[] words = input.split(" ");
			StringBuilder titleCased = new StringBuilder();
			
			for (String word : words) {
				if (word.length() > 0) {
					String lowerCasedWord = word.toLowerCase();
					titleCased.append(Character.toUpperCase(lowerCasedWord.charAt(0))).append(lowerCasedWord.substring(1)).append(" ");
				}
			}
			return titleCased.toString().trim();
		}
		return "";
	}
	
	public static void getOperatingSystemAndArch() {
		log("====================== System Info ======================", LogUtils.Severity.INFO);
		log("----- OS Info -----", LogUtils.Severity.INFO);
		log("Operating System Name: " + System.getProperty("os.name"), LogUtils.Severity.DEBUG);
		log("Operating System Version: " + System.getProperty("os.version"), LogUtils.Severity.DEBUG);
		log("Operating System Architecture: " + System.getProperty("os.arch"), LogUtils.Severity.DEBUG);
		log("----- Java Info -----", LogUtils.Severity.INFO);
		log("Java Version: " + System.getProperty("java.version"), LogUtils.Severity.DEBUG);
		log("Java Runtime Version: " + System.getProperty("java.runtime.version"), LogUtils.Severity.DEBUG);
		log("Java Home Directory: " + System.getProperty("java.home"), LogUtils.Severity.DEBUG);
		log("Java Class Version: " + System.getProperty("java.class.version"), LogUtils.Severity.DEBUG);
		log("JVM Name: " + System.getProperty("java.vm.name"), LogUtils.Severity.DEBUG);
		log("JVM Vendor: " + System.getProperty("java.vm.vendor"), LogUtils.Severity.DEBUG);
		log("JVM Version: " + System.getProperty("java.vm.version"), LogUtils.Severity.DEBUG);
		log("=========================================================", LogUtils.Severity.INFO);
	}
	
	public static String updateStyleProperty(Node node, String property, String value) {
		String updatedStyle = node.getStyle().replaceAll(property + ": [^;]*;", "");
		return updatedStyle + property + ": " + value + ";";
	}
	
	public static void updateSecondary(Color color) {
		String hexColor = toHexString(color);
		ConfigWriter.configwrite("uiColors", "secondaryColor", hexColor);
	}
	
	public static void updateAccent(Color color) {
		String hexColor = toHexString(color);
		ConfigWriter.configwrite("uiColors", "accentColor", hexColor);
	}
	
	public static void updatebackground(Color color) {
		String hexColor = toHexString(color);
		ConfigWriter.configwrite("uiColors", "bkgColor", hexColor);
	}
	
	public static void updateMain(Color color) {
		String hexColor = toHexString(color);
		ConfigWriter.configwrite("uiColors", "mainColor", hexColor);
	}
	
	public static void updateReportBackground(Color color) {
		String hexColor = toHexString(color);
		ConfigWriter.configwrite("reportSettings", "reportBackground", hexColor);
	}
	
	public static void updateReportSecondary(Color color) {
		String hexColor = toHexString(color);
		ConfigWriter.configwrite("reportSettings", "reportSecondary", hexColor);
	}
	
	public static void updateReportAccent(Color color) {
		String hexColor = toHexString(color);
		ConfigWriter.configwrite("reportSettings", "reportAccent", hexColor);
	}
	
	public static void updateReportHeading(Color color) {
		String hexColor = toHexString(color);
		ConfigWriter.configwrite("reportSettings", "reportHeading", hexColor);
	}
	
	public static void updateInfoNotiPrim(Color color) {
		String hexColor = toHexString(color);
		ConfigWriter.configwrite("notificationSettings", "notificationInfoPrimary", hexColor);
	}
	
	public static void updateInfoNotiTextColor(Color color) {
		String hexColor = toHexString(color);
		ConfigWriter.configwrite("notificationSettings", "notificationInfoTextColor", hexColor);
	}
	
	public static void updateWarnNotiPrim(Color color) {
		String hexColor = toHexString(color);
		ConfigWriter.configwrite("notificationSettings", "notificationWarnPrimary", hexColor);
	}
	
	public static void updateWarnNotiTextColor(Color color) {
		String hexColor = toHexString(color);
		ConfigWriter.configwrite("notificationSettings", "notificationWarnTextColor", hexColor);
	}
	
	public static WritableImage changeImageColor(Image image, String hexColor) {
		Color color = Color.web(hexColor);
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		WritableImage coloredImage = new WritableImage(width, height);
		PixelWriter pixelWriter = coloredImage.getPixelWriter();
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color originalColor = image.getPixelReader().getColor(x, y);
				double opacity = originalColor.getOpacity();
				pixelWriter.setColor(x, y, new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity));
			}
		}
		return coloredImage;
	}
	
	public static void confirmLogClearDialog(Stage ownerStage) {
		Dialog<Boolean> dialog = new Dialog<>();
		dialog.initOwner(ownerStage);
		dialog.setTitle("Confirm Action");
		dialog.initModality(Modality.APPLICATION_MODAL);
		
		Label messageLabel = new Label("Are you sure you want to perform this action?\nThis will clear all your logs.");
		Button yesButton = new Button("Yes");
		yesButton.setOnAction(e -> {
			dialog.setResult(true);
			dialog.close();
		});
		Button noButton = new Button("No");
		noButton.getStyleClass().add("menuButton");
		noButton.setOnAction(e -> {
			dialog.setResult(false);
			dialog.close();
		});
		
		dialog.getDialogPane().setContent(new VBox(10, messageLabel, yesButton, noButton));
		
		dialog.showAndWait().ifPresent(result -> {
			if (result) {
				clearDataLogs();
				LogViewController.needRefresh.set(1);
			}
		});
	}
	
	public static void setSmallColumnWidth(TableColumn column) {
		double minColumnWidthSmall = 160.0;
		column.setMaxWidth(minColumnWidthSmall);
	}
	
	public static String toHexString(Color color) {
		return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
	}
	
	public static Color rgbToHexString(String rgb) {
		rgb = rgb.replace("rgb(", "").replace(")", "");
		String[] rgbValues = rgb.split(",");
		int red = Integer.parseInt(rgbValues[0].trim());
		int green = Integer.parseInt(rgbValues[1].trim());
		int blue = Integer.parseInt(rgbValues[2].trim());
		
		return Color.rgb(red, green, blue);
	}
	
	public static void clearDataLogs() {
		try {
			
			String dataLogsFolderPath = getDataLogsFolderPath();
			
			log("DataLogs folder path: " + dataLogsFolderPath, LogUtils.Severity.INFO);
			
			File dataLogsFolder = new File(dataLogsFolderPath);
			if (dataLogsFolder.exists() && dataLogsFolder.isDirectory()) {
				log("DataLogs folder exists.", LogUtils.Severity.INFO);
				
				File[] files = dataLogsFolder.listFiles();
				
				if (files != null) {
					
					for (File file : files) {
						if (file.isFile()) {
							try {
								Files.deleteIfExists(file.toPath());
								log("Deleted file: " + file.getName(), LogUtils.Severity.INFO);
							} catch (IOException e) {
								logError("Failed to delete file: " + file.getName() + " ", e);
							}
						}
					}
					log("All files in DataLogs folder deleted successfully.", LogUtils.Severity.INFO);
				} else {
					log("DataLogs folder is empty.", LogUtils.Severity.WARN);
				}
			} else {
				log("DataLogs folder does not exist.", LogUtils.Severity.WARN);
			}
		} catch (Exception e) {
			logError("Clear Datalogs Error ", e);
		}
	}
	
	public static void clearDataFolder() {
		try {
			String dataFolderPath = getJarPath() + File.separator + "data";
			log("Data folder path: " + dataFolderPath, LogUtils.Severity.INFO);
			
			File dataFolder = new File(dataFolderPath);
			if (dataFolder.exists() && dataFolder.isDirectory()) {
				log("Data folder exists.", LogUtils.Severity.INFO);
				
				File[] files = dataFolder.listFiles();
				if (files != null) {
					for (File file : files) {
						if (file.isFile()) {
							try {
								Files.deleteIfExists(file.toPath());
								log("Deleted file: " + file.getName(), LogUtils.Severity.INFO);
							} catch (IOException e) {
								logError("Failed to delete file: " + file.getName() + " ", e);
							}
						} else if (file.isDirectory()) {
							clearDirectory(file);
						}
					}
					log("All files and subdirectories in Data folder deleted successfully.", LogUtils.Severity.INFO);
				} else {
					log("Data folder is empty.", LogUtils.Severity.WARN);
				}
			} else {
				log("Data folder does not exist.", LogUtils.Severity.WARN);
			}
		} catch (Exception e) {
			logError("Error Clearing Data Folder ", e);
		}
	}
	
	private static void clearDirectory(File directory) {
		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					try {
						Files.deleteIfExists(file.toPath());
						log("Deleted file: " + file.getName(), LogUtils.Severity.INFO);
					} catch (IOException e) {
						logError("Failed to delete file: " + file.getName() + " ", e);
					}
				} else if (file.isDirectory()) {
					clearDirectory(file);
				}
			}
		}
		try {
			Files.deleteIfExists(directory.toPath());
			log("Deleted directory: " + directory.getName(), LogUtils.Severity.INFO);
		} catch (IOException e) {
			logError("Failed to delete directory: " + directory.getName() + " ", e);
		}
	}
	
	public static void clearDataFolderAsync() {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		
		executor.submit(() -> {
			clearDataFolder();
		});
		
		executor.shutdown();
	}
	
	public static void clearDataLogsAsync() {
		try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
			
			executor.submit(() -> {
				clearDataLogs();
			});
			
			executor.shutdown();
		}
	}
	
	public static void confirmSaveDataClearDialog(Stage ownerStage) {
		Dialog<Boolean> dialog = new Dialog<>();
		dialog.initOwner(ownerStage);
		dialog.setTitle("Confirm Action");
		dialog.initModality(Modality.APPLICATION_MODAL);
		
		Label messageLabel = new Label("Are you sure you want to perform this action?\nThis will remove all save data including logs and config.");
		Button yesButton = new Button("Yes");
		yesButton.setOnAction(e -> {
			dialog.setResult(true);
			dialog.close();
		});
		Button noButton = new Button("No");
		noButton.getStyleClass().add("menuButton");
		noButton.setOnAction(e -> {
			dialog.setResult(false);
			dialog.close();
		});
		
		dialog.getDialogPane().setContent(new VBox(10, messageLabel, yesButton, noButton));
		
		dialog.showAndWait().ifPresent(result -> {
			if (result) {
				clearDataLogsAsync();
				clearConfig();
				clearDataFolderAsync();
				handleClose();
			}
		});
	}
	
	public static void clearConfig() {
		try {
			
			String configFilePath = getJarPath() + File.separator + "config.properties";
			File configFile = new File(configFilePath);
			
			if (configFile.exists() && configFile.isFile()) {
				
				try {
					Files.deleteIfExists(configFile.toPath());
					log("Deleted config.properties file.", LogUtils.Severity.INFO);
				} catch (IOException e) {
					logError("Failed to delete config.properties file: ", e);
				}
			} else {
				log("config.properties file does not exist.", LogUtils.Severity.INFO);
			}
		} catch (Exception e) {
			logError("Error Clearing Config", e);
		} finally {
			Platform.exit();
		}
	}
	
	public static void shutdownAudioExecutor() {
		audioExecutor.shutdown();
		try {
			if (!audioExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
				audioExecutor.shutdownNow();
			}
		} catch (InterruptedException e) {
			audioExecutor.shutdownNow();
		}
	}
	
	public static void handleClose() {
		log("Stop Request Recieved", LogUtils.Severity.DEBUG);
		ClientUtils.disconnectFromService();
		shutdownAudioExecutor();
		endLog();
		Platform.exit();
		System.exit(0);
	}
	
	public static String calculateTotalTime(String input, String key) {
		String patternString = key + ": ([^\\.]+)\\.";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(input);
		
		int totalMonths = 0;
		
		while (matcher.find()) {
			String timeString = matcher.group(1).trim();
			
			Pattern yearsPattern = Pattern.compile("(\\d+) years?");
			Pattern monthsPattern = Pattern.compile("(\\d+) months?");
			
			Matcher yearsMatcher = yearsPattern.matcher(timeString);
			Matcher monthsMatcher = monthsPattern.matcher(timeString);
			
			int months = 0;
			
			if (yearsMatcher.find()) {
				int years = Integer.parseInt(yearsMatcher.group(1));
				months += years * 12;
			}
			
			if (monthsMatcher.find()) {
				months += Integer.parseInt(monthsMatcher.group(1));
			}
			
			totalMonths += months;
		}
		
		int years = totalMonths / 12;
		int months = totalMonths % 12;
		
		return (years > 0 ? years + " years " : "") + (months > 0 ? months + " months" : "").trim();
	}
	
	public static List<String> parseCharges(String input, String key) {
		List<String> results = new ArrayList<>();
		
		String patternString = key + ": ([^\\.]+)\\.";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(input);
		
		while (matcher.find()) {
			results.add(matcher.group(1).trim());
		}
		return results;
	}
	
	public static String extractInteger(String input) {
		Pattern pattern = Pattern.compile("-?\\d+");
		Matcher matcher = pattern.matcher(input);
		
		if (matcher.find()) {
			return matcher.group();
		} else {
			return "";
		}
	}
	
	public static ObservableList<Label> createLabels(String text) {
		ObservableList<Label> labels = FXCollections.observableArrayList();
		if (text != null) {
			String[] items = text.split("\\|");
			for (String item : items) {
				if (!item.trim().isEmpty()) {
					Label label = new Label(item.trim());
					label.setStyle("-fx-font-family: \"Inter 28pt Medium\";");
					labels.add(label);
				}
			}
		}
		return labels;
	}
	
	public static ObservableList<Label> createPendingLabels(String text) {
		ObservableList<Label> labels = FXCollections.observableArrayList();
		if (text != null) {
			String[] items = text.split("\\|");
			for (String item : items) {
				if (!item.trim().isEmpty()) {
					Label label = new Label(localization.getLocalizedMessage("CourtView.PendingTrialLabel", "Pending Trial"));
					label.setStyle("-fx-font-family: \"Inter 28pt Medium\";");
					labels.add(label);
				}
			}
		}
		return labels;
	}
	
	public static int calculateFineTotal(String outcomes) {
		int fineTotal = 0;
		if (outcomes != null) {
			Pattern FINE_PATTERN = Pattern.compile("Fined: (\\d+)");
			Matcher matcher = FINE_PATTERN.matcher(outcomes);
			while (matcher.find()) {
				fineTotal += Integer.parseInt(matcher.group(1));
			}
		}
		return fineTotal;
	}
	
	public static int setArrestPriors(Ped ped) throws IOException {
		String chargesFilePath = getDataFolderPath() + "Charges.xml";
		List<String> priorCharges;
		try {
			priorCharges = getRandomCharges(chargesFilePath, Double.parseDouble(ConfigReader.configRead("pedHistoryArrest", "chanceNoCharges")), Double.parseDouble(ConfigReader.configRead("pedHistoryArrest", "chanceMinimalCharges")), Double.parseDouble(ConfigReader.configRead("pedHistoryArrest", "chanceFewCharges")), Double.parseDouble(ConfigReader.configRead("pedHistoryArrest", "chanceManyCharges")));
		} catch (ParserConfigurationException | SAXException e) {
			logError("Error parsing XML file: " + chargesFilePath, e);
			throw new RuntimeException(e);
		}
		StringBuilder stringBuilder = new StringBuilder();
		int chargeCount = 0;
		for (String charge : priorCharges) {
			chargeCount++;
			stringBuilder.append(charge).append(" | ");
		}
		String chargelist = stringBuilder.toString().trim();
		if (!chargelist.isEmpty()) {
			ped.setArrestPriors(chargelist);
		}
		return chargeCount;
	}
	
	public static int setCitationPriors(Ped ped) throws IOException {
		String citationsFilePath = getDataFolderPath() + "Citations.xml";
		List<String> priorCitations;
		try {
			priorCitations = getRandomCitations(citationsFilePath, Double.parseDouble(ConfigReader.configRead("pedHistoryCitation", "chanceNoCitations")), Double.parseDouble(ConfigReader.configRead("pedHistoryCitation", "chanceMinimalCitations")), Double.parseDouble(ConfigReader.configRead("pedHistoryCitation", "chanceFewCitations")), Double.parseDouble(ConfigReader.configRead("pedHistoryCitation", "chanceManyCitations")));
		} catch (ParserConfigurationException | SAXException e) {
			throw new RuntimeException(e);
		}
		StringBuilder stringBuilder = new StringBuilder();
		int citCount = 0;
		for (String cit : priorCitations) {
			citCount++;
			stringBuilder.append(cit).append(" | ");
		}
		String citList = stringBuilder.toString().trim();
		if (!citList.isEmpty()) {
			ped.setCitationPriors(citList);
		}
		return citCount;
	}
	
	public static String getGunLicenseClass() throws IOException {
		Set<String> licenseClassSet = getPermitClassBasedOnChances(Integer.parseInt(ConfigReader.configRead("pedHistoryGunPermitClass", "handgunChance")), Integer.parseInt(ConfigReader.configRead("pedHistoryGunPermitClass", "shotgunChance")), Integer.parseInt(ConfigReader.configRead("pedHistoryGunPermitClass", "longgunChance")));
		
		return String.join(" / ", licenseClassSet).trim();
	}
	
	public static void updateRecentSearches(List<String> recentSearches, ComboBox<String> searchField, String newSearch) {
		if (!newSearch.isEmpty()) {
			recentSearches.remove(newSearch);
			recentSearches.add(0, newSearch);
			if (recentSearches.size() > 5) {
				recentSearches.remove(5);
			}
			searchField.getItems().setAll(recentSearches);
		}
	}
	
	public static void copyChargeDataFile() throws IOException {
		
		String sourcePathCharges = "/com/Guess/ReportsPlus/data/Charges.xml";
		Path destinationDir = Paths.get(getJarPath(), "data");
		
		if (!Files.exists(destinationDir)) {
			Files.createDirectories(destinationDir);
		}
		
		try (InputStream inputStream = treeViewUtils.class.getResourceAsStream(sourcePathCharges)) {
			if (inputStream != null) {
				
				Path destinationPathCharges = destinationDir.resolve(Paths.get(sourcePathCharges).getFileName());
				
				Files.copy(inputStream, destinationPathCharges, StandardCopyOption.REPLACE_EXISTING);
			} else {
				log("Resource not found: " + sourcePathCharges, Severity.ERROR);
			}
		}
	}
	
	public static void copyCitationDataFile() throws IOException {
		
		String sourcePathCitations = "/com/Guess/ReportsPlus/data/Citations.xml";
		Path destinationDir = Paths.get(getJarPath(), "data");
		
		if (!Files.exists(destinationDir)) {
			Files.createDirectories(destinationDir);
		}
		
		try (InputStream inputStream = treeViewUtils.class.getResourceAsStream(sourcePathCitations)) {
			if (inputStream != null) {
				
				Path destinationPathCitations = destinationDir.resolve(Paths.get(sourcePathCitations).getFileName());
				
				Files.copy(inputStream, destinationPathCitations, StandardCopyOption.REPLACE_EXISTING);
			} else {
				log("Resource not found: " + sourcePathCitations, Severity.ERROR);
			}
		}
	}
	
	public static void copyCustomizationDataFile() throws IOException {
		
		String sourcePathCustomization = "/com/Guess/ReportsPlus/data/customization.json";
		Path destinationDir = Paths.get(getJarPath(), "data");
		
		if (!Files.exists(destinationDir)) {
			Files.createDirectories(destinationDir);
		}
		
		try (InputStream inputStream = treeViewUtils.class.getResourceAsStream(sourcePathCustomization)) {
			if (inputStream != null) {
				
				Path destinationPathCitations = destinationDir.resolve(Paths.get(sourcePathCustomization).getFileName());
				
				Files.copy(inputStream, destinationPathCitations, StandardCopyOption.REPLACE_EXISTING);
			} else {
				log("Resource not found: " + sourcePathCustomization, Severity.ERROR);
			}
		}
	}
	
	public static void setColumnAlignment(TableColumn<?, String> calloutStatusColumn, Pos position, String text) {
		Label label = new Label(text);
		HBox hbox = new HBox(label);
		hbox.setAlignment(position);
		hbox.setPadding(new Insets(0, 0, 0, 5));
		
		calloutStatusColumn.setText(null);
		calloutStatusColumn.setGraphic(hbox);
	}
	
	public static String hexToRgba(String hex, double transparency) {
		if (hex.startsWith("#")) {
			hex = hex.substring(1);
		}
		if (hex.length() != 6) {
			throw new IllegalArgumentException("Invalid hex color string");
		}
		if (transparency < 0.0 || transparency > 1.0) {
			throw new IllegalArgumentException("Transparency must be between 0.0 and 1.0");
		}
		
		int r = Integer.parseInt(hex.substring(0, 2), 16);
		int g = Integer.parseInt(hex.substring(2, 4), 16);
		int b = Integer.parseInt(hex.substring(4, 6), 16);
		
		return String.format("rgb(%d, %d, %d, %.2f)", r, g, b, transparency);
	}
	
	public static String getDataLogsFolderPath() {
		return getJarPath() + File.separator + "DataLogs" + File.separator;
	}
	
	public static String getDataFolderPath() {
		return getJarPath() + File.separator + "data" + File.separator;
	}
	
	public static String getServerDataFolderPath() {
		return getJarPath() + File.separator + "serverData" + File.separator;
	}
	
	public static String getJarPath() {
		try {
			
			String jarPath = updateStrings.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			
			String jarDir = new File(jarPath).getParent();
			
			return jarDir;
		} catch (URISyntaxException e) {
			logError("GetJarPath URI Syntax Error ", e);
			return "";
		}
	}
}