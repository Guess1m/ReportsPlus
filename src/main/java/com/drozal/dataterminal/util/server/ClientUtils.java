package com.drozal.dataterminal.util.server;

import com.drozal.dataterminal.actionController;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.LogUtils;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import static com.drozal.dataterminal.actionController.CalloutStage;
import static com.drozal.dataterminal.actionController.IDStage;
import static com.drozal.dataterminal.util.LogUtils.log;
import static com.drozal.dataterminal.util.stringUtil.getJarPath;

public class ClientUtils {
    private static final int TIMEOUT_SECONDS = 10; // Timeout in seconds
    public static Boolean isConnected = false;
    public static String port;
    public static String inet;
    private static Socket socket = null;
    private static ServerStatusListener statusListener;

    public static void disconnectFromService() {
        try {
            isConnected = false;
            notifyStatusChanged(isConnected);
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            log("Error disconnecting from service: " + e.getMessage(), LogUtils.Severity.ERROR);
        }
    }

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
                socket = new Socket();
                Platform.runLater(() -> {
                    actionController.clientController.getStatusLabel().setText("Testing Connection...");
                    actionController.clientController.getStatusLabel().setStyle("-fx-background-color: orange;");
                });

                socket.connect(new InetSocketAddress(serviceAddress, servicePort), 10000);
                socket.setSoTimeout(10000);

                isConnected = true;
                notifyStatusChanged(isConnected);
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
        // Start a separate thread to check for server timeout
        /* TODO: temp removed */
            /*new Thread(() -> {
            ScheduledExecutorService timeoutExecutor = Executors.newScheduledThreadPool(1);
            timeoutExecutor.scheduleAtFixedRate(() -> {
                try {
                    if (socket != null && !socket.isClosed()) {
                        // Send a heartbeat message and wait for a response
                        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                        writer.println("HEARTBEAT");
                        log("sent heartbeat", LogUtils.Severity.DEBUG);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String response = reader.readLine();
                        if (response == null) {
                            // Server did not respond
                            log("heartbeat not received", LogUtils.Severity.ERROR);
                            isConnected = false;
                            notifyStatusChanged(isConnected);
                            // Attempt to reconnect
                            connectToService(serviceAddress, servicePort);
                            timeoutExecutor.shutdown(); // Stop the timeout checker
                        }
                    } else {
                        // Socket is closed, stop the timeout checker
                        timeoutExecutor.shutdown();
                    }
                } catch (IOException e) {
                    System.err.println("Error checking server connection: " + e.getMessage());
                }
            }, TIMEOUT_SECONDS, TIMEOUT_SECONDS, TimeUnit.SECONDS);
        }).start();*/
    }

    /**
     * Receives messages from the server through the provided BufferedReader.
     * Upon receiving an "UPDATE_ID" message, performs actions such as file retrieval and UI updates.
     *
     * @param in the BufferedReader connected to the server's input stream
     */
    public static void receiveMessages(BufferedReader in) {
        new Thread(() -> {
            try {
                String fromServer;
                label:
                while ((fromServer = in.readLine()) != null) {
                    System.out.println("reading line");
                    switch (fromServer) {
                        case "SHUTDOWN":
                            log("Received shutdown message from server. Disconnecting...", LogUtils.Severity.DEBUG);
                            disconnectFromService();
                            break label; // Exit the loop
                        case "UPDATE_ID":
                            log("Received ID update message from server.", LogUtils.Severity.DEBUG);
                            FileUtlis.receiveFileFromServer(inet, Integer.parseInt(port), getJarPath() + File.separator + "serverData" + File.separator + "serverCurrentID.xml", 4096);
                            Platform.runLater(() -> {
                                if (IDStage != null && IDStage.isShowing()) {
                                    IDStage.close();
                                    return;
                                }
                                IDStage = new Stage();
                                IDStage.initStyle(StageStyle.UNDECORATED);
                                FXMLLoader loader = new FXMLLoader(actionController.class.getResource("currentID-view.fxml"));
                                Parent root = null;
                                try {
                                    root = loader.load();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                Scene newScene = new Scene(root);
                                IDStage.setTitle("Current ID");
                                IDStage.setScene(newScene);
                                IDStage.show();
                                IDStage.centerOnScreen();
                                IDStage.setAlwaysOnTop(true);

                                IDStage.setOnHidden(new EventHandler<WindowEvent>() {
                                    @Override
                                    public void handle(WindowEvent event) {
                                        IDStage = null;
                                    }
                                });
                            });
                            break;
                        case "UPDATE_CALLOUT":
                            log("Received Callout update message from server.", LogUtils.Severity.DEBUG);
                            FileUtlis.receiveFileFromServer(inet, Integer.parseInt(port), getJarPath() + File.separator + "serverData" + File.separator + "serverCallout.xml", 4096);
                            Platform.runLater(() -> {
                                if (CalloutStage != null && CalloutStage.isShowing()) {
                                    CalloutStage.close();
                                    return;
                                }
                                CalloutStage = new Stage();
                                FXMLLoader loader = new FXMLLoader(actionController.class.getResource("callout-view.fxml"));
                                Parent root = null;
                                try {
                                    root = loader.load();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                Scene newScene = new Scene(root);
                                CalloutStage.setTitle("Callout Display");
                                CalloutStage.setScene(newScene);
                                CalloutStage.setAlwaysOnTop(true);
                                CalloutStage.initStyle(StageStyle.UNDECORATED);
                                CalloutStage.show();
                                CalloutStage.centerOnScreen();


                                CalloutStage.setOnHidden(new EventHandler<WindowEvent>() {
                                    @Override
                                    public void handle(WindowEvent event) {
                                        CalloutStage = null;
                                    }
                                });

                                CalloutStage.setOnHidden(new EventHandler<WindowEvent>() {
                                    @Override
                                    public void handle(WindowEvent event) {
                                        CalloutStage = null;
                                    }
                                });
                            });
                            break;
                        case "UPDATE_WORLD_PED":
                            log("Received World Ped update message from server.", LogUtils.Severity.DEBUG);
                            FileUtlis.receiveFileFromServer(inet, Integer.parseInt(port), getJarPath() + File.separator + "serverData" + File.separator + "serverWorldPeds.data", 4096);
                            break;
                        case "UPDATE_WORLD_VEH":
                            log("Received World Veh update message from server.", LogUtils.Severity.DEBUG);
                            FileUtlis.receiveFileFromServer(inet, Integer.parseInt(port), getJarPath() + File.separator + "serverData" + File.separator + "serverWorldCars.data", 4096);
                            break;
                        case "HEARTBEAT":
                            break;
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

}