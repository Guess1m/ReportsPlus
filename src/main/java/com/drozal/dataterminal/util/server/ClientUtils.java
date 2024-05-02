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
import java.net.SocketException;

import static com.drozal.dataterminal.actionController.IDStage;
import static com.drozal.dataterminal.util.LogUtils.log;
import static com.drozal.dataterminal.util.stringUtil.getJarPath;

public class ClientUtils {
    public static Boolean isConnected = false;
    public static String port;
    public static String inet;
    private static Socket socket = null;
    private static ServerStatusListener statusListener;

    /**
     * Connects to the specified service address and port.
     * If a previous socket connection exists, it is closed before establishing a new connection.
     * Upon successful connection, initiates a background thread for receiving messages from the server.
     * Updates the configuration with the last used service address and port upon successful connection.
     *
     * @param serviceAddress the IP address or hostname of the service
     * @param servicePort    the port number of the service
     * @throws IOException if an I/O error occurs while connecting to the service
     */
    public static void connectToService(String serviceAddress, int servicePort) throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        new Thread(() -> {
            try {
                isConnected = true;
                notifyStatusChanged(isConnected);
                socket = new Socket();
                socket.connect(new InetSocketAddress(serviceAddress, servicePort), 10000);
                socket.setSoTimeout(10000);

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Thread readerThread = new Thread(() -> receiveMessages(in));
                readerThread.start();

                log("CONNECTED: " + serviceAddress + ":" + servicePort, LogUtils.Severity.INFO);
                port = String.valueOf(servicePort);
                inet = serviceAddress;
                ConfigWriter.configwrite("lastIPV4Connection", serviceAddress);
                ConfigWriter.configwrite("lastPortConnection", String.valueOf(servicePort));
            } catch (IOException e) {
                isConnected = false;
                notifyStatusChanged(isConnected);
                log("Failed to connect: " + e.getMessage(), LogUtils.Severity.ERROR);
            }
        }).start();
    }

    /**
     * Receives messages from the server through the provided BufferedReader.
     * Upon receiving an "UPDATE_FILE" message, performs actions such as file retrieval and UI updates.
     *
     * @param in the BufferedReader connected to the server's input stream
     */
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
                    }
                }
            } catch (SocketException e) {
                isConnected = false;
                notifyStatusChanged(isConnected);
                log("Server Disconnected", LogUtils.Severity.ERROR);
            } catch (IOException e) {
                isConnected = false;
                notifyStatusChanged(isConnected);
                log("Error reading from server: " + e.getMessage(), LogUtils.Severity.ERROR);
            }
        }).start();
    }

    /**
     * Sets the status listener for monitoring connection status changes.
     *
     * @param statusListener the listener for connection status changes
     */
    public static void setStatusListener(ServerStatusListener statusListener) {
        ClientUtils.statusListener = statusListener;
    }

    /**
     * Notifies the status listener about the change in connection status.
     * Executes the notification on the JavaFX application thread.
     *
     * @param isConnected the current connection status
     */
    public static void notifyStatusChanged(boolean isConnected) {
        if (statusListener != null) {
            Platform.runLater(() -> statusListener.onStatusChanged(isConnected));
        }
    }

    /**
     * Sends information to the server over the established socket connection.
     * If the socket is not connected, logs an error.
     *
     * @param data the data to send to the server
     * @throws IOException if an I/O error occurs while sending data to the server
     */
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