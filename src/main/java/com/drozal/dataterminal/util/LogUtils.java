package com.drozal.dataterminal.util;

import java.io.*;

import static com.drozal.dataterminal.DataTerminalHomeApplication.getDate;
import static com.drozal.dataterminal.DataTerminalHomeApplication.getTime;

public class LogUtils {
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

        /*Thread.setDefaultUncaughtExceptionHandler((thread, e) -> {
            logError("Uncaught exception in thread " + thread, e);
        });*/
    }

    public static void log(String message, Severity severity) {
        String logMessage = "[" + getDate() + "] [" + getTime() + "] [" + severity + "] " + message;
        System.out.println(logMessage); // This alone will log to both console and file
    }

    public static void logError(String message, Throwable e) {
        String errorMessage = "*** [" + getDate() + "] [" + getTime() + "] [ERROR] " + message;
        System.err.println(errorMessage); // This alone will log to both console and file
        e.printStackTrace(System.err);
        System.err.println("***"); // This alone will log to both console and file
    }

    public enum Severity {
        DEBUG, INFO, WARN, ERROR,
    }
}
