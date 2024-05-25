package com.drozal.dataterminal.util.server;

import com.drozal.dataterminal.util.Misc.LogUtils;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static com.drozal.dataterminal.util.Misc.LogUtils.log;

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
}
