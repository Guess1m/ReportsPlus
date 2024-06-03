package com.drozal.dataterminal.util.server;

import com.drozal.dataterminal.actionController;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Window.windowUtils;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.drozal.dataterminal.actionController.CalloutStage;
import static com.drozal.dataterminal.actionController.IDStage;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.stringUtil.getJarPath;

public class ClientUtils {
    private static final int TIMEOUT_SECONDS = 10;
    public static Boolean isConnected = false;
    public static String port;
    public static String inet;
    private static Socket socket = null;
    private static ServerStatusListener statusListener;

    private static boolean canActivateUpdateId = true;
    private static boolean canActivateUpdateCallout = true;
    private static boolean canActivateUpdateWorldPed = true;
    private static boolean canActivateUpdateWorldVeh = true;
    private static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

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
                    switch (fromServer) {
                        case "SHUTDOWN":
                            log("Received shutdown message from server. Disconnecting...", LogUtils.Severity.DEBUG);
                            disconnectFromService();
                            break label;
                        case "UPDATE_ID":
                            if (canActivateUpdateId) {
                                canActivateUpdateId = false;
                                executorService.schedule(() -> canActivateUpdateId = true, 1, TimeUnit.SECONDS);

                                log("Received ID update message from server.", LogUtils.Severity.DEBUG);
                                FileUtlis.receiveFileFromServer(inet, Integer.parseInt(port), getJarPath() + File.separator + "serverData" + File.separator + "serverCurrentID.xml", 4096);
                                Platform.runLater(() -> {
                                    if (IDStage != null && IDStage.isShowing()) {
                                        IDStage.close();
                                        IDStage = null;
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
                                    try {
                                        if (ConfigReader.configRead("AOTID").equals("true")) {
                                            IDStage.setAlwaysOnTop(true);
                                        } else {
                                            IDStage.setAlwaysOnTop(false);
                                        }
                                    } catch (IOException e) {
                                        logError("Could not fetch AOTID: ", e);
                                    }

                                    windowUtils.centerStageOnMainApp(IDStage);

                                    IDStage.setOnHidden(new EventHandler<WindowEvent>() {
                                        @Override
                                        public void handle(WindowEvent event) {
                                            IDStage = null;
                                        }
                                    });
                                });
                            }
                            break;
                        case "UPDATE_CALLOUT":
                            if (canActivateUpdateCallout) {
                                canActivateUpdateCallout = false;
                                executorService.schedule(() -> canActivateUpdateCallout = true, 1, TimeUnit.SECONDS);

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
                                    try {
                                        if (ConfigReader.configRead("AOTCallout").equals("true")) {
                                            CalloutStage.setAlwaysOnTop(true);
                                        } else {
                                            CalloutStage.setAlwaysOnTop(false);
                                        }
                                    } catch (IOException e) {
                                        logError("Could not fetch AOTCallout: ", e);
                                    }
                                    CalloutStage.initStyle(StageStyle.UNDECORATED);
                                    CalloutStage.show();
                                    CalloutStage.centerOnScreen();

                                    windowUtils.centerStageOnMainApp(CalloutStage);

                                    try {
                                        if (!ConfigReader.configRead("calloutDuration").equals("infinite")) {
                                            PauseTransition delay = null;
                                            try {
                                                delay = new PauseTransition(Duration.seconds(Double.parseDouble(ConfigReader.configRead("calloutDuration"))));
                                            } catch (IOException e) {
                                                logError("Callout could not be closed: ", e);
                                            }
                                            delay.setOnFinished(event -> CalloutStage.close());
                                            delay.play();
                                        }
                                    } catch (IOException e) {
                                        logError("could not read calloutDuration: ", e);
                                    }

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
                            }
                            break;
                        case "UPDATE_WORLD_PED":
                            if (canActivateUpdateWorldPed) {
                                canActivateUpdateWorldPed = false;
                                executorService.schedule(() -> canActivateUpdateWorldPed = true, 1, TimeUnit.SECONDS);

                                log("Received World Ped update message from server.", LogUtils.Severity.DEBUG);
                                FileUtlis.receiveFileFromServer(inet, Integer.parseInt(port), getJarPath() + File.separator + "serverData" + File.separator + "serverWorldPeds.data", 4096);
                            }
                            break;
                        case "UPDATE_WORLD_VEH":
                            if (canActivateUpdateWorldVeh) {
                                canActivateUpdateWorldVeh = false;
                                executorService.schedule(() -> canActivateUpdateWorldVeh = true, 1, TimeUnit.SECONDS);

                                log("Received World Veh update message from server.", LogUtils.Severity.DEBUG);
                                FileUtlis.receiveFileFromServer(inet, Integer.parseInt(port), getJarPath() + File.separator + "serverData" + File.separator + "serverWorldCars.data", 4096);
                            }
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