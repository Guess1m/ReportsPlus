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
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import static com.drozal.dataterminal.actionController.IDStage;
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