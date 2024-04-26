package com.drozal.dataterminal.util.server;

import javafx.application.Platform;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.file.*;

import static com.drozal.dataterminal.util.stringUtil.getJarPath;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class FileUtlis {

/*

public final static int SOCKET_PORT = 13267;
    public final static String SERVER = "127.0.0.1";
    public final static String FILE_TO_RECEIVE = getJarPath() + File.separator + "downloadedtest.xml";
    public final static int FILE_SIZE = 10000;

*/

    public static void recieveFileFromServer(String host, int port, String fileToRecieve, int fileSize) throws IOException {
        int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket sock = null;
        try {
            sock = new Socket(host, port);
            System.out.println("Connecting...");

            byte[] mybytearray = new byte[fileSize];
            InputStream is = sock.getInputStream();
            fos = new FileOutputStream(fileToRecieve);
            bos = new BufferedOutputStream(fos);
            while ((bytesRead = is.read(mybytearray, current, mybytearray.length - current)) > 0) {
                current += bytesRead;
            }

            bos.write(mybytearray, 0, current);
            bos.flush();
            System.out.println("File " + fileToRecieve
                    + " downloaded (" + current + " bytes read)");
        } finally {
            if (bos != null) bos.close();
            if (fos != null) fos.close();
            if (sock != null) sock.close();
        }
    }

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
                            System.out.println(fileName + " has been modified");

                        }
                    }

                    boolean valid = key.reset();
                    if (!valid) {
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("I/O Error: " + e.toString());
            }
        });

        watchThread.setDaemon(true);
        watchThread.start();
    }


}
