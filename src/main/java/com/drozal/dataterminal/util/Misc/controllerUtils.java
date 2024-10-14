package com.drozal.dataterminal.util.Misc;

import com.drozal.dataterminal.Launcher;
import com.drozal.dataterminal.Windows.Apps.LogViewController;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.logs.Accident.AccidentReportUtils;
import com.drozal.dataterminal.logs.Arrest.ArrestReportUtils;
import com.drozal.dataterminal.logs.Callout.CalloutReportUtils;
import com.drozal.dataterminal.logs.Death.DeathReportUtils;
import com.drozal.dataterminal.logs.Impound.ImpoundReportUtils;
import com.drozal.dataterminal.logs.Incident.IncidentReportUtils;
import com.drozal.dataterminal.logs.Patrol.PatrolReportUtils;
import com.drozal.dataterminal.logs.Search.SearchReportUtils;
import com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationUtils;
import com.drozal.dataterminal.logs.TrafficStop.TrafficStopReportUtils;
import com.drozal.dataterminal.util.History.Ped;
import com.drozal.dataterminal.util.server.ClientUtils;
import jakarta.xml.bind.JAXBException;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.drozal.dataterminal.util.History.PedHistoryMath.*;
import static com.drozal.dataterminal.util.Misc.AudioUtil.audioExecutor;
import static com.drozal.dataterminal.util.Misc.LogUtils.*;
import static com.drozal.dataterminal.util.Misc.stringUtil.*;

public class controllerUtils {
	
	private static final String[][] keys = {{"-name", "-na", "-n", "-fullname", "-fname"}, {"-number", "-num", "-nu"}, {"-age", "-years", "-birthdate", "-a", "-dob"}, {"-address", "-addr", "-place", "-add", "-ad"}, {"-model", "-mod", "-mo", "-m"}, {"-plate", "-platenum", "-plt", "-p"}, {"-gender", "-sex", "-g", "-gen"}, {"-area", "-region", "-zone", "-ar"}, {"-county", "-cty", "-cnty", "-ct", "-c"}, {"-notes", "-nts", "-note", "-comments", "-cmts"}, {"-description", "-des", "-desc", "-d"}, {"-searchitems", "-si", "-search", "-srch", "-items",}, {"-street", "-st", "-road", "-dr", "-strt"}, {"-type", "-typ", "-tpe"}};
	
	public static String toTitleCase(String input) {
		if (input != null && !input.isEmpty()) {
			
			String[] words = input.split(" ");
			StringBuilder titleCased = new StringBuilder();
			
			for (String word : words) {
				if (word.length() > 0) {
					String lowerCasedWord = word.toLowerCase();
					titleCased.append(Character.toUpperCase(lowerCasedWord.charAt(0))).append(
							lowerCasedWord.substring(1)).append(" ");
				}
			}
			return titleCased.toString().trim();
		}
		return "";
	}
	
	public static void getOperatingSystemAndArch() {
		log("====================== System Info ======================", LogUtils.Severity.INFO);
		log("Operating System Name: " + System.getProperty("os.name"), LogUtils.Severity.DEBUG);
		log("Operating System Architecture: " + System.getProperty("os.arch"), LogUtils.Severity.DEBUG);
		log("Java Version: " + System.getProperty("java.version"), LogUtils.Severity.DEBUG);
		log("Java Runtime Version: " + System.getProperty("java.runtime.version"), LogUtils.Severity.DEBUG);
		log("Java Home Directory: " + System.getProperty("java.home"), LogUtils.Severity.DEBUG);
		log("Java Class Version: " + System.getProperty("java.class.version"), LogUtils.Severity.DEBUG);
		log("=========================================================", LogUtils.Severity.INFO);
	}
	
	public static void addTooltip(Node node, String text) {
		Tooltip tooltip = new Tooltip(text);
		tooltip.setShowDelay(Duration.seconds(0.3));
		tooltip.setStyle(
				"-fx-background-color: rgb(50,50,50,0.6); -fx-font-family: Segoe UI Semibold; -fx-text-fill: rgb(255,255,255);");
		Tooltip.install(node, tooltip);
	}
	
	public static String calculateAge(String dateOfBirth) {
		if (dateOfBirth == null || dateOfBirth.isEmpty()) {
			log("Error calculating age, dateOfBirth is null or empty", LogUtils.Severity.ERROR);
			return "Not Available";
		}
		
		try {
			DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH);
			DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("M/d/yyyy", Locale.ENGLISH);
			DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("M/d/yy", Locale.ENGLISH);
			
			LocalDate birthDate;
			try {
				birthDate = LocalDate.parse(dateOfBirth, formatter1);
			} catch (DateTimeParseException e1) {
				try {
					birthDate = LocalDate.parse(dateOfBirth, formatter2);
				} catch (DateTimeParseException e2) {
					birthDate = LocalDate.parse(dateOfBirth, formatter3);
				}
			}
			
			LocalDate currentDate = LocalDate.now();
			if (birthDate.isAfter(currentDate)) {
				log("Error calculating age, birthdate after current date", LogUtils.Severity.ERROR);
				return "Not Available";
			}
			
			Period age = Period.between(birthDate, currentDate);
			return String.valueOf(age.getYears());
		} catch (DateTimeParseException e) {
			log("Error calculating age, improper syntax", LogUtils.Severity.ERROR);
			return "Not Available";
		} catch (Exception e) {
			log("Unexpected error calculating age", LogUtils.Severity.ERROR);
			return "Not Available";
		}
	}
	
	public static String updateStyleProperty(Node node, String property, String value) {
		String updatedStyle = node.getStyle().replaceAll(property + ": [^;]*;", "");
		return updatedStyle + property + ": " + value + ";";
	}
	
	public static void showAnimation(Node node) {
		ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), node);
		scaleTransition.setFromX(0.9);
		scaleTransition.setFromY(0.9);
		scaleTransition.setToX(1);
		scaleTransition.setToY(1);
		scaleTransition.play();
	}
	
	public static String getJarDirectoryPath() {
		try {
			
			String jarPath = Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			
			return new File(jarPath).getParent();
		} catch (Exception e) {
			logError("GetJarDirPath Exception", e);
			return "";
		}
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
	
	public static void setActive(Node pane) {
		pane.setVisible(true);
		pane.setDisable(false);
	}
	
	public static void setDisable(Node... panes) {
		for (Node pane : panes) {
			pane.setVisible(false);
			pane.setDisable(true);
		}
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
	
	public static void changeBarColors(BarChart<String, Number> barChart) throws IOException {
		
		ObservableList<XYChart.Series<String, Number>> seriesList = barChart.getData();
		
		for (XYChart.Series<String, Number> series : seriesList) {
			
			for (XYChart.Data<String, Number> data : series.getData()) {
				
				Node node = data.getNode();
				
				node.setStyle("-fx-bar-fill: " + ConfigReader.configRead("uiColors",
				                                                         "accentColor") + "; -fx-border-color: " + ConfigReader.configRead(
						"uiColors", "secondaryColor") + "; -fx-border-width: 2.5 2.5 0.5 2.5");
			}
		}
	}
	
	public static void changeStatisticColors(AreaChart chart) throws IOException {
		
		String accclr = ConfigReader.configRead("uiColors", "accentColor");
		String mainclr = ConfigReader.configRead("uiColors", "mainColor");
		String secclr = ConfigReader.configRead("uiColors", "secondaryColor");
		chart.lookup(".chart-series-area-fill").setStyle("-fx-fill: " + accclr + ";");
		chart.lookup(".chart-series-area-line").setStyle("-fx-fill: " + secclr + "; -fx-stroke: " + mainclr + ";");
	}
	
	public static void setSmallColumnWidth(TableColumn column) {
		double minColumnWidthSmall = 120.0;
		column.setMinWidth(minColumnWidthSmall);
		column.setPrefWidth(minColumnWidthSmall);
	}
	
	public static String toHexString(Color color) {
		return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),
		                     (int) (color.getBlue() * 255));
	}
	
	public static void updateChartIfMismatch(BarChart<String, Number> chart) {
		XYChart.Series<String, Number> series = null;
		for (XYChart.Series<String, Number> s : chart.getData()) {
			if (s.getName().equals("Series 1")) {
				series = s;
				break;
			}
		}
		
		if (series != null) {
			for (int i = 0; i < series.getData().size(); i++) {
				XYChart.Data<String, Number> data = series.getData().get(i);
				int reportsCount = switch (i) {
					case 0 -> CalloutReportUtils.countReports();
					case 1 -> ArrestReportUtils.countReports();
					case 2 -> TrafficStopReportUtils.countReports();
					case 3 -> PatrolReportUtils.countReports();
					case 4 -> SearchReportUtils.countReports();
					case 5 -> IncidentReportUtils.countReports();
					case 6 -> ImpoundReportUtils.countReports();
					case 7 -> TrafficCitationUtils.countReports();
					case 8 -> DeathReportUtils.countReports();
					case 9 -> AccidentReportUtils.countReports();
					default -> 0;
				};
				if (data.getYValue().intValue() != reportsCount) {
					
					data.setYValue(reportsCount);
				}
			}
		}
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
		
		Label messageLabel = new Label(
				"Are you sure you want to perform this action?\nThis will remove all save data including logs and config.");
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
			
			String configFilePath = getJarDirectoryPath() + File.separator + "config.properties";
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
	
	public static void parseLogData(String logURL, Map<String, Integer> combinedAreasMap, String value) {
		Map<String, Integer> areasMap = new HashMap<>();
		File xmlFile = new File(logURL);
		if (!xmlFile.exists()) {
			return;
		}
		
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			
			NodeList nodeList = doc.getElementsByTagName("*");
			
			for (int temp = 0; temp < nodeList.getLength(); temp++) {
				Element element = (Element) nodeList.item(temp);
				String nodeName = element.getNodeName();
				if (nodeName.toLowerCase().contains(value) && !nodeName.toLowerCase().contains("textarea")) {
					String area = element.getTextContent().trim();
					if (!area.isEmpty()) {
						combinedAreasMap.put(area, combinedAreasMap.getOrDefault(area, 0) + 1);
					}
				}
			}
		} catch (Exception e) {
			logError("ParseLogData Exception", e);
		}
	}
	
	public static XYChart.Series<String, Number> parseEveryLog(String value) {
		Map<String, Integer> combinedAreasMap = new HashMap<>();
		parseLogData(stringUtil.arrestLogURL, combinedAreasMap, value);
		parseLogData(stringUtil.calloutLogURL, combinedAreasMap, value);
		parseLogData(stringUtil.impoundLogURL, combinedAreasMap, value);
		parseLogData(stringUtil.incidentLogURL, combinedAreasMap, value);
		parseLogData(stringUtil.patrolLogURL, combinedAreasMap, value);
		parseLogData(stringUtil.searchLogURL, combinedAreasMap, value);
		parseLogData(stringUtil.trafficCitationLogURL, combinedAreasMap, value);
		parseLogData(stringUtil.trafficstopLogURL, combinedAreasMap, value);
		parseLogData(stringUtil.DeathReportLogURL, combinedAreasMap, value);
		parseLogData(stringUtil.accidentLogURL, combinedAreasMap, value);
		
		Map<String, Integer> sortedAreasMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		sortedAreasMap.putAll(combinedAreasMap);
		
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		
		for (Map.Entry<String, Integer> entry : sortedAreasMap.entrySet()) {
			if (!entry.getKey().equalsIgnoreCase("N/A")) {
				series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
			}
		}
		return series;
	}
	
	public static void refreshChart(AreaChart<String, Number> chart, String value) {
		chart.getData().clear();
		
		XYChart.Series<String, Number> newSeries = parseEveryLog(value);
		chart.getData().add(newSeries);
		
		try {
			changeStatisticColors(chart);
		} catch (IOException e) {
			logError("RefreshChart IO Exception", e);
			throw new RuntimeException(e);
		}
	}
	
	private static Map<String, String> pullNotesValues(String notepad) {
		String text = notepad;
		Map<String, String> values = new HashMap<>();
		
		for (String[] keyGroup : keys) {
			for (String key : keyGroup) {
				String value = extractValue(text, key);
				if (value != null) {
					for (String k : keyGroup) {
						values.put(k, value);
					}
					break;
				}
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
		endLog();
		ClientUtils.disconnectFromService();
		shutdownAudioExecutor();
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
					label.setStyle("-fx-font-family: \"Segoe UI Semibold\";");
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
					Label label = new Label("Pending Trial");
					label.setStyle("-fx-font-family: \"Segoe UI Semibold\";");
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
	
	public static Ped createPed(String licenseNumber, String name, String gender, String birthday, String address, String isWanted, String licenseStatus) {
		Ped ped = new Ped();
		ped.setLicenseNumber(licenseNumber);
		ped.setName(name);
		ped.setGender(gender);
		ped.setBirthday(birthday);
		ped.setAddress(address);
		ped.setWantedStatus(isWanted);
		ped.setLicenseStatus(licenseStatus);
		return ped;
	}
	
	public static void setGunLicenseStatus(Ped ped) throws IOException {
		Boolean hasGunLicense = calculateTrueFalseProbability(
				ConfigReader.configRead("pedHistoryGunPermit", "hasGunLicense"));
		ped.setGunLicenseStatus(String.valueOf(hasGunLicense));
		
		if (hasGunLicense) {
			String licenseType = getGunLicenseType();
			ped.setGunLicenseType(licenseType);
			
			String licenseClasses = getGunLicenseClass();
			ped.setGunLicenseClass(licenseClasses);
			
			ped.setHuntingLicenseStatus(String.valueOf(
					calculateTrueFalseProbability(ConfigReader.configRead("pedHistory", "hasHuntingLicense"))));
		}
	}
	
	public static int setArrestPriors(Ped ped) throws IOException {
		String chargesFilePath = getJarPath() + File.separator + "data" + File.separator + "Charges.xml";
		List<String> priorCharges;
		try {
			priorCharges = getRandomCharges(chargesFilePath, Double.parseDouble(
					ConfigReader.configRead("pedHistoryArrest", "chanceNoCharges")), Double.parseDouble(
					ConfigReader.configRead("pedHistoryArrest", "chanceMinimalCharges")), Double.parseDouble(
					ConfigReader.configRead("pedHistoryArrest", "chanceFewCharges")), Double.parseDouble(
					ConfigReader.configRead("pedHistoryArrest", "chanceManyCharges")));
		} catch (ParserConfigurationException | SAXException e) {
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
		String citationsFilePath = getJarPath() + File.separator + "data" + File.separator + "Citations.xml";
		List<String> priorCitations;
		try {
			priorCitations = getRandomCitations(citationsFilePath, Double.parseDouble(
					ConfigReader.configRead("pedHistoryCitation", "chanceNoCitations")), Double.parseDouble(
					ConfigReader.configRead("pedHistoryCitation", "chanceMinimalCitations")), Double.parseDouble(
					ConfigReader.configRead("pedHistoryCitation", "chanceFewCitations")), Double.parseDouble(
					ConfigReader.configRead("pedHistoryCitation", "chanceManyCitations")));
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
	
	public static String getGunLicenseType() throws IOException {
		String licenseTypeSet = String.valueOf(getPermitTypeBasedOnChances(
				Integer.parseInt(ConfigReader.configRead("pedHistoryGunPermitType", "concealedCarryChance")),
				Integer.parseInt(ConfigReader.configRead("pedHistoryGunPermitType", "openCarryChance")),
				Integer.parseInt(ConfigReader.configRead("pedHistoryGunPermitType", "bothChance"))));
		
		if (licenseTypeSet.toLowerCase().contains("open")) {
			return "Open Carry";
		} else if (licenseTypeSet.toLowerCase().contains("concealed")) {
			return "Concealed Carry";
		} else {
			return "Open Carry / Concealed Carry";
		}
	}
	
	public static String getGunLicenseClass() throws IOException {
		Set<String> licenseClassSet = getPermitClassBasedOnChances(
				Integer.parseInt(ConfigReader.configRead("pedHistoryGunPermitClass", "handgunChance")),
				Integer.parseInt(ConfigReader.configRead("pedHistoryGunPermitClass", "shotgunChance")),
				Integer.parseInt(ConfigReader.configRead("pedHistoryGunPermitClass", "longgunChance")));
		
		return String.join(" / ", licenseClassSet).trim();
	}
	
	public static void setPedPriors(Ped ped) {
		int totalChargePriors = 0;
		try {
			totalChargePriors = setArrestPriors(ped);
		} catch (IOException e) {
			logError("Could not fetch arrestPriors: ", e);
		}
		int totalCitationPriors = 0;
		try {
			totalCitationPriors = setCitationPriors(ped);
		} catch (IOException e) {
			logError("Could not fetch citationPriors: ", e);
		}
		
		if (totalChargePriors >= 1) {
			try {
				ped.setParoleStatus(String.valueOf(
						calculateTrueFalseProbability(ConfigReader.configRead("pedHistory", "onParoleChance"))));
			} catch (IOException e) {
				logError("Could not set ParoleStatus: ", e);
			}
			try {
				ped.setProbationStatus(String.valueOf(
						calculateTrueFalseProbability(ConfigReader.configRead("pedHistory", "onProbationChance"))));
			} catch (IOException e) {
				logError("Could not set ProbationStatus: ", e);
			}
		}
		
		String totalStops = String.valueOf(calculateTotalStops(totalChargePriors + totalCitationPriors));
		ped.setTimesStopped(totalStops);
	}
	
	public static String formatLicenseStatus(String status) {
		switch (status.toLowerCase()) {
			case "expired":
				return "EXPIRED";
			case "suspended":
				return "SUSPENDED";
			default:
				return "Valid";
		}
	}
	
	public static Ped createNewPed(String name, String licenseNumber, String gender, String birthday, String address, String isWanted, String licenseStatus, String pedModel) throws IOException {
		Ped ped = createPed(licenseNumber, name, gender, birthday, address, isWanted, licenseStatus);
		
		if (isWanted.equalsIgnoreCase("true")) {
			setPedWarrantStatus(ped);
		}
		
		setPedPriors(ped);
		ped.setFishingLicenseStatus(String.valueOf(
				calculateTrueFalseProbability(ConfigReader.configRead("pedHistory", "hasFishingLicense"))));
		ped.setBoatingLicenseStatus(String.valueOf(
				calculateTrueFalseProbability(ConfigReader.configRead("pedHistory", "hasBoatingLicense"))));
		if (!pedModel.equalsIgnoreCase("not available")) {
			ped.setModel(pedModel);
		} else {
			log("ped model is 'not available' so not adding", Severity.WARN);
		}
		try {
			setGunLicenseStatus(ped);
		} catch (IOException e) {
			logError("Could not set gunLicenseStatus: ", e);
		}
		
		try {
			Ped.PedHistoryUtils.addPed(ped);
		} catch (JAXBException e) {
			logError("Error adding ped to PedHistory: ", e);
		}
		return ped;
	}
	
	public static void setPedWarrantStatus(Ped ped) {
		try {
			String warrant = null;
			try {
				warrant = getRandomCharge(chargesFilePath);
			} catch (IOException e) {
				logError("Error getting randomCharge: ", e);
			}
			if (warrant != null) {
				ped.setOutstandingWarrants("WANTED(" + getRandomDepartment() + ") - " + warrant);
			} else {
				ped.setOutstandingWarrants("WANTED - No details");
			}
		} catch (ParserConfigurationException | SAXException e) {
			logError("Error getting random charge: ", e);
			ped.setOutstandingWarrants("WANTED - Error retrieving details");
		}
	}
	
	public static Ped createOwnerPed(String owner, String vehPlateNum) throws IOException {
		String genderOutcome = calculateTrueFalseProbability("50") ? "Male" : "Female";
		String isWantedOutcome = calculateTrueFalseProbability("15") ? "true" : "false";
		Ped ped = createPed(generateLicenseNumber(), owner, genderOutcome, generateBirthday(60), getRandomAddress(),
		                    isWantedOutcome, calculateLicenseStatus(55, 22, 23));
		
		if (isWantedOutcome.equalsIgnoreCase("true")) {
			setPedWarrantStatus(ped);
		}
		
		setPedPriors(ped);
		ped.setFishingLicenseStatus(String.valueOf(
				calculateTrueFalseProbability(ConfigReader.configRead("pedHistory", "hasFishingLicense"))));
		ped.setBoatingLicenseStatus(String.valueOf(
				calculateTrueFalseProbability(ConfigReader.configRead("pedHistory", "hasBoatingLicense"))));
		ped.setVehiclePlateNum(vehPlateNum);
		try {
			setGunLicenseStatus(ped);
		} catch (IOException e) {
			logError("Could not set gunLicenseStatus: ", e);
		}
		
		try {
			Ped.PedHistoryUtils.addPed(ped);
		} catch (JAXBException e) {
			logError("Error adding ped to PedHistory: ", e);
		}
		return ped;
	}
	
	public static void updateRecentSearches(List<String> recentSearches, ComboBox<String> searchField, String newSearch) {
		recentSearches.remove(newSearch);
		recentSearches.add(0, newSearch);
		if (recentSearches.size() > 5) {
			recentSearches.remove(5);
		}
		searchField.getItems().setAll(recentSearches);
	}
	
}