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

import static com.drozal.dataterminal.DataTerminalHomeApplication.getDate;
import static com.drozal.dataterminal.DataTerminalHomeApplication.getTime;

public class LogUtils {
	private static boolean inErrorBlock = false;
	
	static {
		try {
			PrintStream console = System.out;
			FileOutputStream fos = new FileOutputStream(stringUtil.getJarPath() + File.separator + "output.log", true);
			PrintStream fileAndConsole = new PrintStream(new OutputStream() {
				@Override
				public void write(int b) throws IOException {
					console.write(b);
					fos.write(b);
				}
				
				@Override
				public void write(byte[] b) throws IOException {
					console.write(b);
					fos.write(b);
				}
				
				@Override
				public void write(byte[] b, int off, int len) throws IOException {
					console.write(b, off, len);
					fos.write(b, off, len);
				}
				
				@Override
				public void flush() throws IOException {
					console.flush();
					fos.flush();
				}
				
				@Override
				public void close() throws IOException {
					console.close();
					fos.close();
				}
			});
			System.setOut(fileAndConsole);
			System.setErr(fileAndConsole);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Failed to create log file", e);
		}
		
		Thread.setDefaultUncaughtExceptionHandler((thread, e) -> {
			logError("Uncaught exception in thread " + thread, e);
		});
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
	}
	
	private static void readLogFile(String filePath, ObservableList<TextFlow> logItems) {
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.trim()
				        .equals("***")) {
					if (inErrorBlock) {
						inErrorBlock = false;
						continue;
					}
				}
				
				TextFlow textFlow = createStyledText(line);
				textFlow.setStyle("-fx-background-color: transparent;");
				if (!textFlow.getChildren()
				             .isEmpty()) {
					logItems.add(textFlow);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static TextFlow createStyledText(String line) {
		TextFlow textFlow = new TextFlow();
		if (line.trim()
		        .equals("***")) {
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
				textFlow.getChildren()
				        .add(errorText);
				inErrorBlock = false;
				
				if (endIndex + 3 < line.length()) {
					String afterErrorTextStr = line.substring(endIndex + 3);
					Text afterErrorText = new Text(afterErrorTextStr);
					setColorBasedOnTag(afterErrorText, afterErrorTextStr);
					textFlow.getChildren()
					        .add(afterErrorText);
				}
			} else {
				Text errorText = new Text(line);
				errorText.setFill(Color.RED);
				textFlow.getChildren()
				        .add(errorText);
			}
		} else if (line.contains("***")) {
			int startErrorIndex = line.indexOf("***");
			int endErrorIndex = line.indexOf("***", startErrorIndex + 3);
			
			if (startErrorIndex > 0) {
				String beforeErrorTextStr = line.substring(0, startErrorIndex);
				Text beforeErrorText = new Text(beforeErrorTextStr);
				setColorBasedOnTag(beforeErrorText, beforeErrorTextStr);
				textFlow.getChildren()
				        .add(beforeErrorText);
			}
			
			if (endErrorIndex != -1) {
				String errorTextStr = line.substring(startErrorIndex + 3, endErrorIndex);
				Text errorText = new Text(errorTextStr);
				errorText.setFill(Color.RED);
				textFlow.getChildren()
				        .add(errorText);
				inErrorBlock = false;
				
				if (endErrorIndex + 3 < line.length()) {
					String afterErrorTextStr = line.substring(endErrorIndex + 3);
					Text afterErrorText = new Text(afterErrorTextStr);
					setColorBasedOnTag(afterErrorText, afterErrorTextStr);
					textFlow.getChildren()
					        .add(afterErrorText);
				}
			} else {
				String errorTextStr = line.substring(startErrorIndex + 4);
				Text errorText = new Text(errorTextStr);
				errorText.setFill(Color.RED);
				textFlow.getChildren()
				        .add(errorText);
				inErrorBlock = true;
			}
		} else {
			Text normalText = new Text(line);
			setColorBasedOnTag(normalText, line);
			textFlow.getChildren()
			        .add(normalText);
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
		readLogFile(stringUtil.getJarPath() + File.separator + "output.log", logItems);
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
			readLogFile(stringUtil.getJarPath() + File.separator + "output.log", logItems);
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}
	
	public enum Severity {
		DEBUG, INFO, WARN, ERROR,
	}
	
}
