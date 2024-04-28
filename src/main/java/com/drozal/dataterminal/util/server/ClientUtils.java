package com.drozal.dataterminal.util.server;

import com.catwithawand.borderlessscenefx.scene.BorderlessScene;
import com.drozal.dataterminal.CurrentIDViewController;
import com.drozal.dataterminal.actionController;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.LogUtils;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import static com.drozal.dataterminal.actionController.IDStage;
import static com.drozal.dataterminal.util.LogUtils.log;
import static com.drozal.dataterminal.util.stringUtil.getJarPath;

public class ClientUtils {
    private static final long HEARTBEAT_TIMEOUT = TimeUnit.SECONDS.toMillis((long) 6.5);  // 6.5 seconds timeout
    public static Boolean isConnected = false;
    public static String port;
    public static String inet;
    private static Socket socket = null;
    private static long lastHeartbeat = System.currentTimeMillis();
    private static ServerStatusListener statusListener;

    public static boolean connectToService(String serviceAddress, int servicePort) throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(serviceAddress, servicePort), 10000);
            socket.setSoTimeout(10000);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Thread readerThread = new Thread(() -> receiveMessages(in));
            readerThread.start();

            isConnected = true;
            port = String.valueOf(servicePort);
            inet = serviceAddress;
            ConfigWriter.configwrite("lastIPV4Connection", serviceAddress);
            ConfigWriter.configwrite("lastPortConnection", String.valueOf(servicePort));
            return true;
        } catch (IOException e) {
            isConnected = false;
            notifyStatusChanged(isConnected);
            log("Failed to connect: " + e.getMessage(), LogUtils.Severity.ERROR);
            return false;
        }
    }


    public static void receiveMessages(BufferedReader in) {
        new Thread(() -> {
            try {
                String fromServer;
                while ((fromServer = in.readLine()) != null) {
                    if ("UPDATE_FILE".equals(fromServer)) {
                        log("Received file update message from server.", LogUtils.Severity.DEBUG);
                        FileUtlis.recieveFileFromServer(inet, Integer.parseInt(port), getJarPath() + File.separator + "serverData" + File.separator + "ServerCurrentID.xml", 4096);
                        Platform.runLater(() -> {
                            if (IDStage != null && IDStage.isShowing()) {
                                IDStage.toFront();
                                IDStage.requestFocus();
                                return;
                            }
                            IDStage = new Stage();
                            FXMLLoader loader = new FXMLLoader(actionController.class.getResource("currentID-view.fxml"));
                            Parent root = null;
                            try {
                                root = loader.load();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            BorderlessScene newScene = new BorderlessScene(IDStage, StageStyle.TRANSPARENT, root, Color.TRANSPARENT);
                            AnchorPane topbar = CurrentIDViewController.getTitleBar();
                            newScene.setMoveControl(topbar);
                            IDStage.setTitle("Current ID");
                            IDStage.setScene(newScene);
                            IDStage.show();
                            IDStage.centerOnScreen();

                            IDStage.setOnHidden(new EventHandler<WindowEvent>() {
                                @Override
                                public void handle(WindowEvent event) {
                                    IDStage = null;
                                }
                            });
                        });
                    } else if ("HEARTBEAT".equals(fromServer)) {
                        log("Heartbeat received from server.", LogUtils.Severity.DEBUG);
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

    public static void notifyStatusChanged(boolean isConnected) {
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