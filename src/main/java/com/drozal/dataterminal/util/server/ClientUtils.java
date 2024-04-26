package com.drozal.dataterminal.util.server;

import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.LogUtils;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import static com.drozal.dataterminal.util.stringUtil.getJarPath;

public class ClientUtils {
    private static final long HEARTBEAT_TIMEOUT = TimeUnit.SECONDS.toMillis((long) 6.5);  // 6.5 seconds timeout
    public static Boolean isConnected = false;
    public static String port;
    public static String inet;
    private static Socket socket = null;
    private static long lastHeartbeat = System.currentTimeMillis();
    private static ServerStatusListener statusListener;

    public static Boolean connectToService(String serviceAddress, int servicePort) {
        try {
            socket = new Socket(serviceAddress, servicePort);
            port = String.valueOf(servicePort);
            inet = serviceAddress;
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            receiveMessages(in);
            isConnected = true;
            notifyStatusChanged(isConnected);
            ConfigWriter.configwrite("lastIPV4Connection", serviceAddress);
            ConfigWriter.configwrite("lastPortConnection", String.valueOf(servicePort));
            return true;
        } catch (IOException e) {
            LogUtils.log("Server Not Found", LogUtils.Severity.ERROR);
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Connection Error");
                alert.setHeaderText(null);
                alert.setContentText("Please ensure that you have the server open before connecting the client.");
                alert.showAndWait();
                isConnected = false;
                notifyStatusChanged(isConnected);
            });
            return false;
        }
    }

    public static void receiveMessages(BufferedReader in) {
        new Thread(() -> {
            try {
                String fromServer;
                while ((fromServer = in.readLine()) != null) {
                    if ("UPDATE_FILE".equals(fromServer)) {
                        System.out.println("Received file update message from server.");
                        FileUtlis.recieveFileFromServer(inet, Integer.parseInt(port), getJarPath() + File.separator + "serverData" + File.separator + "ServerCurrentID.xml", 4096);
                        Platform.runLater(() -> {

                        });
                    } else if ("HEARTBEAT".equals(fromServer)) {
                        System.out.println("Heartbeat received from server.");
                        lastHeartbeat = System.currentTimeMillis();
                    }
                }
            } catch (IOException e) {
                isConnected = false;
                notifyStatusChanged(isConnected);
                LogUtils.logError("Server Connection may be lost: ", e);
            }
        }).start();
    }

    public static void setStatusListener(ServerStatusListener statusListener) {
        ClientUtils.statusListener = statusListener;
    }

    private static void notifyStatusChanged(boolean isConnected) {
        if (statusListener != null) {
            Platform.runLater(() -> statusListener.onStatusChanged(isConnected));
        }
    }

    public static void sendInfoToServer(String data) throws IOException {
        if (socket != null) {
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);
            writer.println(data);
        } else {
            LogUtils.logError("Socket is not connected.", null);
        }
    }

}