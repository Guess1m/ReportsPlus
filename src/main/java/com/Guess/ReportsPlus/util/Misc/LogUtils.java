package com.Guess.ReportsPlus.util.Misc;

import static com.Guess.ReportsPlus.MainApplication.getDate;
import static com.Guess.ReportsPlus.MainApplication.getTime;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationError;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getJarPath;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getOperatingSystemAndArch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import com.Guess.ReportsPlus.MainApplication;
import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.TerminalController;
import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.Commands.ShowOutputCommand;
import com.Guess.ReportsPlus.util.Strings.updateStrings;

public class LogUtils {

	public enum Severity {
		DEBUG, INFO, WARN, ERROR,
	}

	private static PrintStream filePrintStream;
	private static PrintStream consolePrintStream;
	private static PrintStream consoleErrorStream;
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_PURPLE = "\u001B[35m";

	public static void initLogging() {
		try {
			consolePrintStream = System.out;
			consoleErrorStream = System.err;

			String logsDirPath = getJarPath() + File.separator + "logs";
			File logsDir = new File(logsDirPath);
			if (!logsDir.exists()) {
				logsDir.mkdirs();
			}

			final int MAX_LOG_FILES = 5;
			File[] existingLogs = logsDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".log"));

			if (existingLogs != null && existingLogs.length >= MAX_LOG_FILES) {
				java.util.Arrays.sort(existingLogs, java.util.Comparator.comparingLong(File::lastModified));
				int filesToDelete = existingLogs.length - MAX_LOG_FILES + 1;
				for (int i = 0; i < filesToDelete; i++) {
					existingLogs[i].delete();
				}
			}

			String timeStamp = new java.text.SimpleDateFormat("dd-MM-yyyy_hh-mm-ss").format(new java.util.Date());
			String logFilePath = logsDirPath + File.separator + "client_" + timeStamp + ".log";
			filePrintStream = new PrintStream(new FileOutputStream(logFilePath, true), true);

			Thread.setDefaultUncaughtExceptionHandler(
					(thread, e) -> logError("Uncaught exception in thread: " + thread.getName(), e));
			logInfo("---=== Client Log Initialized ===---");
			logDebug("Logging initialized. Log file: " + timeStamp + ".log");
			getOperatingSystemAndArch();
		} catch (IOException e) {
			if (consolePrintStream == null) {
				consolePrintStream = System.out;
			}
			consolePrintStream.println("Failed to initialize file logging.");
			consolePrintStream.println(checkFolderPermissions(Path.of(getJarPath())));
			e.printStackTrace(consolePrintStream);
		}
	}

	public static void endLog() {
		String endMessage = "----------------------------- END LOG [" + MainApplication.getTime(true, true)
				+ "] -----------------------------";
		consolePrintStream.println(endMessage);
		filePrintStream.println(endMessage);
		filePrintStream.println();
		if (filePrintStream != null) {
			filePrintStream.close();
		}
	}

	public static void logDebug(String message) {
		log(message, Severity.DEBUG, ANSI_PURPLE);
	}

	public static void logInfo(String message) {
		log(message, Severity.INFO, ANSI_GREEN);
	}

	public static void logWarn(String message) {
		log(message, Severity.WARN, ANSI_YELLOW);
	}

	public static void logError(String message) {
		log(message, Severity.ERROR, ANSI_RED);
	}

	public static void logError(String message, Throwable e) {
		log(message, Severity.ERROR, ANSI_RED);
		String stackTrace = getStackTraceAsString(e);
		String coloredStackTrace = updateStrings.showANSILoggingInConsole ? ANSI_RED + stackTrace + ANSI_RESET
				: stackTrace;
		consoleErrorStream.print(coloredStackTrace);
		filePrintStream.print(stackTrace);
		if (TerminalController.terminalController != null && ShowOutputCommand.TerminalLogging) {
			TerminalController.terminalController.printToOutput(message + "\n" + stackTrace);
		}
		showNotificationError("ERROR Manager", message);
	}

	public static String getStackTraceAsString(Throwable throwable) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		throwable.printStackTrace(pw);
		return sw.toString();
	}

	private static void log(String message, Severity severity, String color) {
		String threadInfo = Thread.currentThread().getName();
		if (threadInfo.equalsIgnoreCase("JavaFX Application Thread")) {
			threadInfo = "FX";
		}
		String plainLog = String.format("[%s] [%s] [%s] [%s] %s",
				threadInfo,
				getDate(),
				getTime(true, true),
				severity,
				message);
		String coloredLog;
		if (updateStrings.showANSILoggingInConsole) {
			coloredLog = String.format("%s[%s] [%s] [%s] [%s] %s%s%s",
					ANSI_BLUE,
					threadInfo,
					getDate(),
					getTime(true, true),
					severity,
					ANSI_RESET,
					color,
					message,
					ANSI_RESET);
		} else {
			coloredLog = plainLog;
		}
		consolePrintStream.println(coloredLog);
		filePrintStream.println(plainLog);
		if (TerminalController.terminalController != null && ShowOutputCommand.TerminalLogging) {
			TerminalController.terminalController.printToOutput(plainLog);
		}
	}

	private static String checkFolderPermissions(Path folderPath) {
		StringBuilder permissions = new StringBuilder();
		permissions.append("Permissions for folder ").append(folderPath).append(":\n");
		permissions.append("Readable: ").append(Files.isReadable(folderPath) ? "Yes" : "No").append("\n");
		permissions.append("Writable: ").append(Files.isWritable(folderPath) ? "Yes" : "No").append("\n");
		permissions.append("Executable: ").append(Files.isExecutable(folderPath) ? "Yes" : "No").append("\n");
		return permissions.toString();
	}
}
