package com.drozal.dataterminal.util.Misc;

import com.drozal.dataterminal.DataTerminalHomeApplication;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.drozal.dataterminal.DataTerminalHomeApplication.*;
import static com.drozal.dataterminal.util.Misc.NotificationManager.showNotificationError;
import static com.drozal.dataterminal.util.Misc.controllerUtils.getOperatingSystemAndArch;
import static com.drozal.dataterminal.util.Misc.stringUtil.getJarPath;

public class LogUtils {
	private static boolean inErrorBlock = false;
	
	static {
		try {
			String logFilePath = getJarPath() + File.separator + "output.log";
			FileOutputStream fos = new FileOutputStream(logFilePath, true);
			PrintStream fileAndConsole = new TeePrintStream(System.out, new PrintStream(fos));
			System.setOut(fileAndConsole);
			System.setErr(fileAndConsole);
			log("---=== Client Log ===---", Severity.INFO);
			getOperatingSystemAndArch();
		} catch (IOException e) {
			System.out.println(checkFolderPermissions(Path.of(getJarPath())));
			logError("Unable to create output.log file, Check folder permissions: ", e);
		}
		
		Thread.setDefaultUncaughtExceptionHandler((thread, e) -> logError("Uncaught exception in thread " + thread, e));
	}
	
	private static String checkFolderPermissions(Path folderPath) {
		StringBuilder permissions = new StringBuilder();
		permissions.append("Permissions for folder ").append(folderPath).append(":\n");
		
		if (Files.isReadable(folderPath)) {
			permissions.append("Readable: Yes\n");
		} else {
			permissions.append("Readable: No\n");
		}
		
		if (Files.isWritable(folderPath)) {
			permissions.append("Writable: Yes\n");
		} else {
			permissions.append("Writable: No\n");
		}
		
		if (Files.isExecutable(folderPath)) {
			permissions.append("Executable: Yes\n");
		} else {
			permissions.append("Executable: No\n");
		}
		return permissions.toString();
	}
	
	public static void endLog() {
		String logMessage = "----------------------------- END LOG [" + DataTerminalHomeApplication.getTime() + "] -----------------------------";
		System.out.println(logMessage);
		System.out.println();
	}
	
	public static void log(String message, Severity severity) {
		String logMessage = "[" + getDate() + "] [" + getTime() + "] [" + severity + "] " + message;
		System.out.println(logMessage);
	}
	
	public static void logError(String message, Throwable e) {
		String errorMessage = "*** [" + getDate() + "] [" + getTime() + "] [ERROR] " + message;
		System.err.println(errorMessage);
		e.printStackTrace(System.err);
		System.err.println("***");
		showNotificationError("ERROR Manager", "ERROR: " + message, mainRT);
	}
	
	private static void readLogFile(String filePath, ObservableList<TextFlow> logItems) {
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.trim().equals("***")) {
					if (inErrorBlock) {
						inErrorBlock = false;
						continue;
					}
				}
				
				TextFlow textFlow = createStyledText(line);
				textFlow.setStyle("-fx-background-color: transparent;");
				if (!textFlow.getChildren().isEmpty()) {
					logItems.add(textFlow);
				}
			}
		} catch (IOException e) {
			logError("Cant Read Log File: ", e);
		}
	}
	
	private static TextFlow createStyledText(String line) {
		TextFlow textFlow = new TextFlow();
		if (line.trim().equals("***")) {
			if (inErrorBlock) {
				inErrorBlock = false;
			}
			return textFlow;
		}
		
		if (inErrorBlock) {
			int endIndex = line.indexOf("***");
			if (endIndex != -1) {
				String errorTextStr = line.substring(0, endIndex);
				Text errorText = new Text(errorTextStr);
				errorText.setFill(Color.RED);
				textFlow.getChildren().add(errorText);
				inErrorBlock = false;
				
				if (endIndex + 3 < line.length()) {
					String afterErrorTextStr = line.substring(endIndex + 3);
					Text afterErrorText = new Text(afterErrorTextStr);
					setColorBasedOnTag(afterErrorText, afterErrorTextStr);
					textFlow.getChildren().add(afterErrorText);
				}
			} else {
				Text errorText = new Text(line);
				errorText.setFill(Color.RED);
				textFlow.getChildren().add(errorText);
			}
		} else if (line.contains("***")) {
			int startErrorIndex = line.indexOf("***");
			int endErrorIndex = line.indexOf("***", startErrorIndex + 3);
			
			if (startErrorIndex > 0) {
				String beforeErrorTextStr = line.substring(0, startErrorIndex);
				Text beforeErrorText = new Text(beforeErrorTextStr);
				setColorBasedOnTag(beforeErrorText, beforeErrorTextStr);
				textFlow.getChildren().add(beforeErrorText);
			}
			
			if (endErrorIndex != -1) {
				String errorTextStr = line.substring(startErrorIndex + 3, endErrorIndex);
				Text errorText = new Text(errorTextStr);
				errorText.setFill(Color.RED);
				textFlow.getChildren().add(errorText);
				inErrorBlock = false;
				
				if (endErrorIndex + 3 < line.length()) {
					String afterErrorTextStr = line.substring(endErrorIndex + 3);
					Text afterErrorText = new Text(afterErrorTextStr);
					setColorBasedOnTag(afterErrorText, afterErrorTextStr);
					textFlow.getChildren().add(afterErrorText);
				}
			} else {
				String errorTextStr = line.substring(startErrorIndex + 4);
				Text errorText = new Text(errorTextStr);
				errorText.setFill(Color.RED);
				textFlow.getChildren().add(errorText);
				inErrorBlock = true;
			}
		} else {
			Text normalText = new Text(line);
			setColorBasedOnTag(normalText, line);
			textFlow.getChildren().add(normalText);
		}
		return textFlow;
	}
	
	private static void setColorBasedOnTag(Text text, String line) {
		text.setStyle("-fx-font-size: 12.9;");
		if (line.contains("[INFO]")) {
			text.setFill(Color.BLUE);
		} else if (line.contains("[WARN]")) {
			text.setFill(Color.ORANGERED);
		} else if (line.contains("[DEBUG]")) {
			text.setFill(Color.PURPLE);
			text.setStyle("-fx-font-weight: bold;");
		} else if (line.contains("[ERROR]")) {
			text.setFill(Color.DARKRED);
		} else if (line.contains("END LOG")) {
			text.setFill(Color.DARKGREEN);
		} else {
			text.setFill(Color.BLACK);
		}
	}
	
	public static void addOutputToListview(ListView<TextFlow> listView) {
		ObservableList<TextFlow> logItems = FXCollections.observableArrayList();
		readLogFile(getJarPath() + File.separator + "output.log", logItems);
		listView.setItems(logItems);
		
		listView.setStyle("-fx-padding: 0;");
		listView.setCellFactory(lv -> new ListCell<>() {
			@Override
			protected void updateItem(TextFlow item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null && !empty) {
					setGraphic(item);
					if (getIndex() % 2 == 0) {
						setStyle("-fx-background-color: #f9f9f9;");
					} else {
						setStyle("-fx-background-color: #f3f3f3;");
					}
				} else {
					setGraphic(null);
				}
			}
		});
		
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
			logItems.clear();
			readLogFile(getJarPath() + File.separator + "output.log", logItems);
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}
	
	public enum Severity {
		DEBUG, INFO, WARN, ERROR,
	}
	
	private static class TeePrintStream extends PrintStream {
		private final PrintStream second;
		
		public TeePrintStream(PrintStream main, PrintStream second) {
			super(main);
			this.second = second;
		}
		
		@Override
		public void write(byte[] buf, int off, int len) {
			super.write(buf, off, len);
			second.write(buf, off, len);
		}
		
		@Override
		public void flush() {
			super.flush();
			second.flush();
		}
		
		@Override
		public void close() {
			super.close();
			second.close();
		}
	}
}
