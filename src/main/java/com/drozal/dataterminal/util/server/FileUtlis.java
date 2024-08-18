package com.drozal.dataterminal.util.server;

import com.drozal.dataterminal.util.Misc.LogUtils;

import java.io.*;
import java.net.Socket;

import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.stringUtil.*;

public class FileUtlis {

    public static void receiveIDFromServer(int fileSize) throws IOException {
        int bytesRead;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket sock = null;
        try {
            sock = new Socket(ClientUtils.inet, Integer.parseInt(ClientUtils.port));
            byte[] mybytearray = new byte[fileSize];
            InputStream is = sock.getInputStream();
            fos = new FileOutputStream(currentIDFileURL);
            bos = new BufferedOutputStream(fos);

            while ((bytesRead = is.read(mybytearray)) != -1) {
                bos.write(mybytearray, 0, bytesRead);
            }

            bos.flush();
            log("CurrentID File Downloaded", LogUtils.Severity.INFO);
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (sock != null) {
                    sock.close();
                }
            } catch (IOException e) {
                logError("Could Not Close All Elements: ", e);
            }
        }
    }

    public static void receiveLocationFromServer(int fileSize) throws IOException {
        int bytesRead;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket sock = null;
        try {
            sock = new Socket(ClientUtils.inet, Integer.parseInt(ClientUtils.port));
            byte[] mybytearray = new byte[fileSize];
            InputStream is = sock.getInputStream();
            fos = new FileOutputStream(currentLocationFileURL);
            bos = new BufferedOutputStream(fos);

            while ((bytesRead = is.read(mybytearray)) != -1) {
                bos.write(mybytearray, 0, bytesRead);
            }

            bos.flush();
            log("Location File Downloaded", LogUtils.Severity.INFO);
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (sock != null) {
                    sock.close();
                }
            } catch (IOException e) {
                logError("Could Not Close All Elements: ", e);
            }
        }
    }

    public static void receiveCalloutFromServer(int fileSize) throws IOException {
        int bytesRead;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket sock = null;
        try {
            sock = new Socket(ClientUtils.inet, Integer.parseInt(ClientUtils.port));
            byte[] mybytearray = new byte[fileSize];
            InputStream is = sock.getInputStream();
            fos = new FileOutputStream(getJarPath() + File.separator + "serverData" + File.separator + "serverCallout.xml");
            bos = new BufferedOutputStream(fos);

            while ((bytesRead = is.read(mybytearray)) != -1) {
                bos.write(mybytearray, 0, bytesRead);
            }

            bos.flush();
            log("Callout File Downloaded", LogUtils.Severity.INFO);
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (sock != null) {
                    sock.close();
                }
            } catch (IOException e) {
                logError("Could Not Close All Elements: ", e);
            }
        }
    }

    public static void receiveWorldPedFromServer(int fileSize) throws IOException {
        int bytesRead;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket sock = null;
        try {
            sock = new Socket(ClientUtils.inet, Integer.parseInt(ClientUtils.port));
            byte[] mybytearray = new byte[fileSize];
            InputStream is = sock.getInputStream();
            fos = new FileOutputStream(getJarPath() + File.separator + "serverData" + File.separator + "serverWorldPeds.data");
            bos = new BufferedOutputStream(fos);

            while ((bytesRead = is.read(mybytearray)) != -1) {
                bos.write(mybytearray, 0, bytesRead);
            }

            bos.flush();
            log("WorldPed File Downloaded", LogUtils.Severity.INFO);
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (sock != null) {
                    sock.close();
                }
            } catch (IOException e) {
                logError("Could Not Close All Elements: ", e);
            }
        }
    }

    public static void receiveWorldVehFromServer(int fileSize) throws IOException {
        int bytesRead;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket sock = null;
        try {
            sock = new Socket(ClientUtils.inet, Integer.parseInt(ClientUtils.port));
            byte[] mybytearray = new byte[fileSize];
            InputStream is = sock.getInputStream();
            fos = new FileOutputStream(getJarPath() + File.separator + "serverData" + File.separator + "serverWorldCars.data");
            bos = new BufferedOutputStream(fos);

            while ((bytesRead = is.read(mybytearray)) != -1) {
                bos.write(mybytearray, 0, bytesRead);
            }

            bos.flush();
            log("WorldVeh File Downloaded", LogUtils.Severity.INFO);
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (sock != null) {
                    sock.close();
                }
            } catch (IOException e) {
                logError("Could Not Close All Elements: ", e);
            }
        }
    }

}
