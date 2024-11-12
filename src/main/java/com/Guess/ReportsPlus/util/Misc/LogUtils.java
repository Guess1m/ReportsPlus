package com.Guess.ReportsPlus.util.Misc;

import com.Guess.ReportsPlus.MainApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.Guess.ReportsPlus.MainApplication.getDate;
import static com.Guess.ReportsPlus.MainApplication.getTime;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationError;
import static com.Guess.ReportsPlus.util.Misc.controllerUtils.getOperatingSystemAndArch;
import static com.Guess.ReportsPlus.util.Misc.stringUtil.getJarPath;

public class LogUtils {
	
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
		System.out.println(
				"----------------------------- END LOG [" + MainApplication.getTime() + "] -----------------------------");
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
		showNotificationError("ERROR Manager", "ERROR: " + message);
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
