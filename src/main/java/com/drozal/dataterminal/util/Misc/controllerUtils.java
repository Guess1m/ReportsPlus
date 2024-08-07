package com.drozal.dataterminal.util.Misc;

import com.drozal.dataterminal.actionController;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.logs.Arrest.ArrestReportUtils;
import com.drozal.dataterminal.logs.Callout.CalloutReportUtils;
import com.drozal.dataterminal.logs.Death.DeathReportUtils;
import com.drozal.dataterminal.logs.Impound.ImpoundReportUtils;
import com.drozal.dataterminal.logs.Incident.IncidentReportUtils;
import com.drozal.dataterminal.logs.Patrol.PatrolReportUtils;
import com.drozal.dataterminal.logs.Search.SearchReportUtils;
import com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationUtils;
import com.drozal.dataterminal.logs.TrafficStop.TrafficStopReportUtils;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.drozal.dataterminal.actionController.handleClose;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.stringUtil.getDataLogsFolderPath;
import static com.drozal.dataterminal.util.Misc.stringUtil.getJarPath;

public class controllerUtils {
	
	private static final String[][] keys = {{"-name", "-na", "-n", "-fullname", "-fname"}, {"-number", "-num", "-nu"}, {"-age", "-years", "-birthdate", "-a", "-dob"}, {"-address", "-addr", "-place", "-add", "-ad"}, {"-model", "-mod", "-mo", "-m"}, {"-plate", "-platenum", "-plt", "-p"}, {"-gender", "-sex", "-g", "-gen"}, {"-area", "-region", "-zone", "-ar"}, {"-county", "-cty", "-cnty", "-ct", "-c"}, {"-notes", "-nts", "-note", "-comments", "-cmts"}, {"-description", "-des", "-desc", "-d"}, {"-searchitems", "-si", "-search", "-srch", "-items",}, {"-street", "-st", "-road", "-dr", "-strt"}};
	
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
	
	public static String lowerPaneToToRGB(String hexColor, double alpha) {
		if (!hexColor.matches("^#[0-9A-Fa-f]{6}$")) {
			throw new IllegalArgumentException("Invalid hexadecimal color: " + hexColor);
		}
		
		int r = Integer.parseInt(hexColor.substring(1, 3), 16) + 70;
		int g = Integer.parseInt(hexColor.substring(3, 5), 16) + 70;
		int b = Integer.parseInt(hexColor.substring(5, 7), 16) + 70;
		
		return String.format("rgb(%d, %d, %d, %.2f)", r, g, b, alpha);
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
			
			String jarPath = actionController.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			
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
	
	public static void showLogClearNotification(String title, String message, Object owner) {
		Label label = new Label(message);
		
		VBox vbox1 = new VBox(label);
		vbox1.setAlignment(Pos.CENTER);
		Notifications noti = Notifications.create().title(title).text(message).graphic(null).position(
				Pos.TOP_CENTER).hideAfter(Duration.seconds(1.15)).owner(owner);
		noti.show();
		noti.getStyleClass().add("notification-pane");
	}
	
	public static void setActive(AnchorPane pane) {
		pane.setVisible(true);
		pane.setDisable(false);
	}
	
	public static void setDisable(AnchorPane... panes) {
		for (AnchorPane pane : panes) {
			pane.setVisible(false);
			pane.setDisable(true);
		}
	}
	
	public static void confirmLogClearDialog(Stage ownerStage, BarChart barChart, AreaChart areaChart) {
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
				updateChartIfMismatch(barChart);
				controllerUtils.refreshChart(areaChart, "area");
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
			if (extractedValue != null) {
				break;
			}
			
			for (Map.Entry<String, String> entry : values.entrySet()) {
				for (String altKey : entry.getKey().split("\\|")) {
					if (altKey.equals(key)) {
						extractedValue = entry.getValue();
						break;
					}
				}
				if (extractedValue != null) {
					break;
				}
			}
			if (extractedValue != null) {
				break;
			}
		}
		
		String labelText = extractedValue;
		textField.setText(labelText);
	}
	
	public static void updateTextFromNotepad(TextArea textArea, TextArea notepadText, String... keys) {
		Map<String, String> values = pullNotesValues(notepadText.getText());
		String extractedValue = null;
		for (String key : keys) {
			extractedValue = values.get(key);
			if (extractedValue != null) {
				break;
			}
			
			for (Map.Entry<String, String> entry : values.entrySet()) {
				for (String altKey : entry.getKey().split("\\|")) {
					if (altKey.equals(key)) {
						extractedValue = entry.getValue();
						break;
					}
				}
				if (extractedValue != null) {
					break;
				}
			}
			if (extractedValue != null) {
				break;
			}
		}
		String labelText = extractedValue;
		textArea.setText(labelText);
	}
	
}
