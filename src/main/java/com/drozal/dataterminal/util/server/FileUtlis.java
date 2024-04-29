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

    /* Receive a file from a server using a TCP socket
     * Establishes a socket connection to the server and reads a specified file
     * Parameters:
     *   host - the IP address or hostname of the server
     *   port - the port number on the server
     *   fileToReceive - the path where the downloaded file will be saved
     *   fileSize - expected size of the file to allocate buffer
     * Logs the connection and file download progress
     * Uses a buffer to read the file in chunks and writes it to disk using a BufferedOutputStream
     * Ensures all resources (socket, streams) are closed in the finally block
     * Throws:
     *   IOException if there are issues connecting, reading, or writing to the file
     */
    public static void recieveFileFromServer(String host, int port, String fileToRecieve, int fileSize) throws IOException {
        int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket sock = null;
        try {
            sock = new Socket(host, port);
            log("Connecting...", LogUtils.Severity.INFO);

            byte[] mybytearray = new byte[fileSize];
            InputStream is = sock.getInputStream();
            fos = new FileOutputStream(fileToRecieve);
            bos = new BufferedOutputStream(fos);
            while ((bytesRead = is.read(mybytearray, current, mybytearray.length - current)) > 0) {
                current += bytesRead;
            }

            bos.write(mybytearray, 0, current);
            bos.flush();
            log("File " + fileToRecieve
                    + " downloaded (" + current + " bytes read)", LogUtils.Severity.INFO);
        } finally {
            if (bos != null) bos.close();
            if (fos != null) fos.close();
            if (sock != null) sock.close();
        }
    }

    /* Watch for modifications to a specific file in a given directory
     * Sets up a WatchService to monitor file events such as modifications
     * Runs the watching process on a separate daemon thread
     * Parameters:
     *   directoryPath - the directory path where the file is located
     *   fileNameToWatch - the specific file to monitor for changes
     * Logs each modification event detected for the file
     * Continues monitoring until the watch key is invalid or the thread is interrupted
     * Handles IOException and InterruptedException, logging any issues encountered
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
