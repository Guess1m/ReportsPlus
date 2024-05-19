package com.drozal.dataterminal.util.server;

import com.drozal.dataterminal.util.LogUtils;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.file.*;

import static com.drozal.dataterminal.util.LogUtils.log;
import static com.drozal.dataterminal.util.LogUtils.logError;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class FileUtlis {

    public static void receiveFileFromServer(String host, int port, String outputFileNameLocation, int fileSize) throws IOException {
        int bytesRead;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket sock = null;
        try {
            sock = new Socket(host, port);
            byte[] mybytearray = new byte[fileSize];
            InputStream is = sock.getInputStream();
            fos = new FileOutputStream(outputFileNameLocation);
            bos = new BufferedOutputStream(fos);

            while ((bytesRead = is.read(mybytearray)) != -1) {
                bos.write(mybytearray, 0, bytesRead);
            }

            bos.flush();
            log("File " + outputFileNameLocation + " downloaded (" + fileSize + " bytes read)", LogUtils.Severity.INFO);
        } finally {
            try {
                if (bos != null) bos.close();
                if (fos != null) fos.close();
                if (sock != null) sock.close();
            } catch (IOException e) {
                // Handle or log the exception
            }
        }
    }

    /**
     * Monitors file changes in the specified directory for the given file name.
     * Logs an informational message when the watched file has been modified.
     *
     * @param directoryPath   the directory path to watch for file changes
     * @param fileNameToWatch the name of the file to watch for changes
     */
    public static void watchFileChanges(String directoryPath, String fileNameToWatch) {
        Path dir = Paths.get(directoryPath);

        Thread watchThread = new Thread(() -> {
            try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
                dir.register(watcher, ENTRY_MODIFY);

                while (true) {
                    WatchKey key;
                    try {
                        key = watcher.take();
                    } catch (InterruptedException x) {
                        Thread.currentThread().interrupt();
                        return;
                    }

                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();

                        if (kind == OVERFLOW) {
                            continue;
                        }

                        WatchEvent<Path> ev = (WatchEvent<Path>) event;
                        Path fileName = ev.context();

                        if (fileName.toString().equals(fileNameToWatch)) {
                            log(fileName + " has been modified", LogUtils.Severity.INFO);

                        }
                    }

                    boolean valid = key.reset();
                    if (!valid) {
                        break;
                    }
                }
            } catch (IOException e) {
                logError("I/O Error: ", e);
            }
        });

        watchThread.setDaemon(true);
        watchThread.start();
    }

}
