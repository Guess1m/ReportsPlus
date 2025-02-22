package com.Guess.ReportsPlus.util.Server;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager;
import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.Windows.Server.ClientController;
import com.Guess.ReportsPlus.Windows.Server.trafficStopController;
import com.Guess.ReportsPlus.Windows.Settings.settingsController;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.config.ConfigWriter;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.Guess.ReportsPlus.util.Misc.NotificationManager;
import com.Guess.ReportsPlus.util.Misc.Threading.WorkerThread;
import com.Guess.ReportsPlus.util.Other.CalloutManager;
import com.Guess.ReportsPlus.util.Other.controllerUtils;
import com.Guess.ReportsPlus.util.Server.Objects.Callout.Callout;
import com.Guess.ReportsPlus.util.Strings.URLStrings;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.MainApplication.mainDesktopControllerObj;
import static com.Guess.ReportsPlus.Windows.Server.calloutController.getCallout;
import static com.Guess.ReportsPlus.util.Misc.AudioUtil.playSound;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationErrorPersistent;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationInfo;
import static com.Guess.ReportsPlus.util.Server.recordUtils.extractValueByKey;
import static com.Guess.ReportsPlus.util.Strings.URLStrings.serverLookupURL;
import static com.Guess.ReportsPlus.util.Strings.updateStrings.version;

public class ClientUtils {
    public static Boolean isConnected = false;
    public static String port;
    public static String inet;
    public static CustomWindow calloutWindow;
    public static String serverVersion = null;
    private static Socket socket = null;
    private static ServerStatusListener statusListener;

    public static void connectToService(String serviceAddress, int servicePort) throws IOException {
        if (socket != null && !socket.isClosed()) {
            log("Closing existing socket before reconnecting.", LogUtils.Severity.INFO);
            socket.close();
        }
        Runnable serverConnectionTask = () -> {
            log("Started Server Thread", LogUtils.Severity.INFO);
            try {
                log("Initializing socket.", LogUtils.Severity.INFO);
                socket = new Socket();

                if (ClientController.clientController != null) {
                    Platform.runLater(() -> {
                        ClientController.clientController.getStatusLabel().setText(localization.getLocalizedMessage("ServerConnectionWindow.TestingConnection", "Testing Connection..."));
                        ClientController.clientController.getStatusLabel().setStyle("-fx-background-color: orange;");
                    });
                }

                log("Attempting to connect to " + serviceAddress + ":" + servicePort, LogUtils.Severity.INFO);
                socket.connect(new InetSocketAddress(serviceAddress, servicePort), 10000);
                log("Socket connected successfully.", LogUtils.Severity.INFO);

                socket.setSoTimeout(Integer.parseInt(ConfigReader.configRead("connectionSettings", "socketTimeout")));
                log("Socket timeout set to " + Integer.parseInt(ConfigReader.configRead("connectionSettings", "socketTimeout")), LogUtils.Severity.INFO);

                isConnected = true;
                notifyStatusChanged(isConnected);

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                receiveMessages(in);

                log("Reader thread started.", LogUtils.Severity.INFO);
                log("CONNECTED: " + serviceAddress + ":" + servicePort, LogUtils.Severity.INFO);

                port = String.valueOf(servicePort);
                inet = serviceAddress;

                log("Writing connection settings to config. IP: " + serviceAddress + " Port: " + servicePort, LogUtils.Severity.INFO);
                ConfigWriter.configwrite("connectionSettings", "lastIPV4Connection", serviceAddress);
                ConfigWriter.configwrite("connectionSettings", "lastPortConnection", String.valueOf(servicePort));
            } catch (IOException e) {
                sendMessageToServer("SHUTDOWN");
                isConnected = false;
                notifyStatusChanged(isConnected);
                log("Failed to connect: " + e.getMessage(), LogUtils.Severity.ERROR);
                log("Stack trace: " + Arrays.toString(e.getStackTrace()), LogUtils.Severity.ERROR);
            }
        };
        WorkerThread serverConnectionThread = new WorkerThread("ServerConnectionThread", serverConnectionTask);
        serverConnectionThread.start();
    }

    public synchronized static void receiveFileFromServer(int fileSize, String serverFileName) throws IOException {
        int bytesRead;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket sock = null;

        try {
            sock = new Socket(ClientUtils.inet, Integer.parseInt(ClientUtils.port));
            log("Starting file transfer: " + serverFileName + " (Size: " + fileSize + " bytes)", LogUtils.Severity.INFO);

            byte[] mybytearray = new byte[fileSize];
            InputStream is = sock.getInputStream();
            fos = new FileOutputStream(controllerUtils.getServerDataFolderPath() + serverFileName);
            bos = new BufferedOutputStream(fos);

            log("{File Transfer} Receiving file: " + serverFileName, LogUtils.Severity.DEBUG);
            int totalBytesRead = 0;
            while ((bytesRead = is.read(mybytearray)) != -1) {
                bos.write(mybytearray, 0, bytesRead);
                totalBytesRead += bytesRead;
            }
            bos.flush();

            log("{File Transfer} File transfer completed successfully. Total bytes received: " + totalBytesRead, LogUtils.Severity.INFO);
        } catch (IOException e) {
            logError("{File Transfer} Error occurred while receiving the file: " + serverFileName + "; ", e);
            throw e;
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
                log("{File Transfer} Closed recieveFile resources..", LogUtils.Severity.INFO);
            } catch (IOException e) {
                logError("{File Transfer} Error while closing resources: ", e);
            }
        }
    }

    public static void sendMessageToServer(String message) {
        if (socket != null && socket.isConnected()) {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(message);
                if (!message.equalsIgnoreCase("HEARTBEAT")) {
                    log("Message sent to server: " + message, LogUtils.Severity.INFO);
                }
            } catch (IOException e) {
                log("Error sending message to server: " + e.getMessage(), LogUtils.Severity.ERROR);
            }
        } else {
            log("Socket is not connected. Unable to send message: " + message, LogUtils.Severity.WARN);
        }
    }

    public static void receiveMessages(BufferedReader in) {
        Runnable messageReceiverTask = () -> {
            try {
                String fromServer;
                long lastUpdate = System.currentTimeMillis();
                label:
                while ((fromServer = in.readLine()) != null) {
                    if (fromServer.contains("VERSION=")) {
                        String[] split = fromServer.split("=");
                        String serverVer = split[1];
                        log("Checking Server / Application Versions", LogUtils.Severity.INFO);
                        serverVersion = serverVer;

                        if (!serverVer.equalsIgnoreCase(version)) {
                            log("Versions dont match!", LogUtils.Severity.ERROR);
                            log("Server Version: " + serverVer, LogUtils.Severity.DEBUG);
                            log("App Version: " + version, LogUtils.Severity.DEBUG);
                            showNotificationErrorPersistent("Mismatched Versions", "Your Application and Server have mismatched versions, check logs!");
                        } else {
                            log("Versions Match!", LogUtils.Severity.INFO);
                            log("Server Version: " + serverVer, LogUtils.Severity.DEBUG);
                            log("App Version: " + version, LogUtils.Severity.DEBUG);
                        }
                        continue;
                    }
                    switch (fromServer) {
                        case "SHUTDOWN":
                            log("Received shutdown, Disconnecting...", LogUtils.Severity.DEBUG);
                            disconnectFromService();
                            break label;

                        case "UPDATE_GAME_DATA":
                            log("Received Location Update", LogUtils.Severity.DEBUG);
                            receiveFileFromServer(1024, "ServerGameData.data");
                            runUpdateLocation();
                            break;

                        case "UPDATE_ID":
                            log("Received ID Update", LogUtils.Severity.DEBUG);
                            receiveFileFromServer(4096, "ServerCurrentID.xml");
                            runUpdateID();
                            break;

                        case "UPDATE_CALLOUT":
                            log("Received Callout Update", LogUtils.Severity.DEBUG);
                            receiveFileFromServer(1024, "ServerCallout.xml");
                            runUpdateCallout();
                            break;

                        case "UPDATE_WORLD_PED":
                            log("Received World Ped Update", LogUtils.Severity.DEBUG);
                            receiveFileFromServer(4096, "ServerWorldPeds.data");
                            break;

                        case "UPDATE_TRAFFIC_STOP":
                            log("Received Traffic Stop Update", LogUtils.Severity.DEBUG);
                            receiveFileFromServer(1024, "ServerTrafficStop.data");
                            runTrafficStopUpdate();
                            break;

                        case "UPDATE_WORLD_VEH":
                            log("Received World Vehicle Update", LogUtils.Severity.DEBUG);
                            receiveFileFromServer(4096, "ServerWorldCars.data");
                            break;

                        case "UPDATE_LOOKUP":
                            log("Received Lookup Update", LogUtils.Severity.DEBUG);
                            receiveFileFromServer(256, "ServerLookup.data");
                            runLookupUpdate();
                            break;

                        case "HEARTBEAT":
                            long now = System.currentTimeMillis();
                            long delta = now - lastUpdate;
                            log("Server Heartbeat: " + Math.round(delta) + "ms", LogUtils.Severity.DEBUG);
                            sendMessageToServer("HEARTBEAT");
                            lastUpdate = now;
                            break;

                        default:
                            log("Received unknown message: " + fromServer, LogUtils.Severity.DEBUG);
                            break;

                    }
                }
            } catch (SocketTimeoutException e) {
                sendMessageToServer("SHUTDOWN");
                isConnected = false;
                notifyStatusChanged(isConnected);
                try {
                    log("Read timed out after " + socket.getSoTimeout() + " milliseconds, missed heartbeat", LogUtils.Severity.ERROR);
                } catch (SocketException ex) {
                    logError("Could not getSoTimeout: ", e);
                }
            } catch (IOException e) {
                sendMessageToServer("SHUTDOWN");
                isConnected = false;
                notifyStatusChanged(isConnected);
                log("Error reading from server: " + e.getMessage(), LogUtils.Severity.ERROR);
            }
        };
        WorkerThread messageReceiverThread = new WorkerThread("MessageReceiverThread", messageReceiverTask);
        messageReceiverThread.start();
    }

    private static void runLookupUpdate() {
        log("Running Lookup Update", LogUtils.Severity.INFO);
        File lookupFile = new File(serverLookupURL);
        if (!lookupFile.exists()) {
            log("No lookup data file found", LogUtils.Severity.ERROR);
            return;
        }
        String lookupFileContent = "";
        try {
            lookupFileContent = Files.readString(Paths.get(URLStrings.serverLookupURL));
        } catch (IOException e) {
            logError("Error reading lookupFile Content", e);
        }
        if (mainDesktopControllerObj != null && lookupFileContent.length() > 0) {
            String finalLookupFileContent = lookupFileContent;
            Platform.runLater(() -> {
                if (!mainDesktopControllerObj.getTopBarHboxRight().getChildren().contains(mainDesktopControllerObj.getLookupLabel())) {
                    mainDesktopControllerObj.getTopBarHboxRight().getChildren().add(mainDesktopControllerObj.getLookupLabel());
                    mainDesktopControllerObj.getLookupLabel().toBack();
                }
                mainDesktopControllerObj.getLookupLabel().setText(localization.getLocalizedMessage("Desktop.CheckedLabel", "Checked:") + " " + finalLookupFileContent);
                showNotificationInfo("Ped / Vehicle Check", "Ran check for: [" + finalLookupFileContent + "]");
            });
        } else {
            log("lookupFile content is null; not updating", LogUtils.Severity.ERROR);
        }
    }

    public static void disconnectFromService() {
        try {
            sendMessageToServer("SHUTDOWN");
            isConnected = false;
            notifyStatusChanged(isConnected);
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            log("Error disconnecting from service: " + e.getMessage(), LogUtils.Severity.ERROR);
        }
    }

    public static void setStatusListener(ServerStatusListener listener) {
        statusListener = listener;
    }

    private static void notifyStatusChanged(boolean status) {
        if (statusListener != null) {
            log("Client Connection Status Changed: " + isConnected, LogUtils.Severity.DEBUG);
            Platform.runLater(() -> statusListener.onStatusChanged(status));
        }
    }

    public static void listenForServerBroadcasts() {
        int broadCastPort = 8888;
        try {
            broadCastPort = Integer.parseInt(ConfigReader.configRead("connectionSettings", "broadcastPort"));
            log("Using broadcastPort: " + broadCastPort, LogUtils.Severity.DEBUG);
        } catch (IOException e) {
            logError("Could not get broadcastPort from config: ", e);
        }

        try (DatagramSocket socket = new DatagramSocket(broadCastPort, InetAddress.getByName("0.0.0.0"))) {
            socket.setBroadcast(true);
            while (true) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    logError("Error sleeping: ", e);
                }
                if (!isConnected) {
                    byte[] buffer = new byte[256];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    String message = new String(packet.getData(), 0, packet.getLength());
                    if (message.startsWith("SERVER_DISCOVERY:")) {
                        String[] parts = message.split(":");
                        String serverAddress = packet.getAddress().getHostAddress();
                        int serverPort = Integer.parseInt(parts[1]);

                        log("Discovered server at " + serverAddress + ":" + serverPort, LogUtils.Severity.INFO);
                        try {
                            connectToService(serverAddress, serverPort);
                            return;
                        } catch (IOException e) {
                            log("Error connecting to server; " + serverAddress + ":" + serverPort + " | " + e.getMessage(), LogUtils.Severity.ERROR);
                            return;
                        }
                    }
                } else {
                    log("Already connected", LogUtils.Severity.WARN);
                    return;
                }
            }
        } catch (IOException e) {
            log("Error listening for broadcasts: " + e.getMessage(), LogUtils.Severity.ERROR);
            return;
        }
    }

    private static void runUpdateLocation() {
        Platform.runLater(() -> {
            if (!mainDesktopControllerObj.getTopBarHboxRight().getChildren().contains(mainDesktopControllerObj.getLocationDataLabel())) {
                mainDesktopControllerObj.getTopBarHboxRight().getChildren().add(mainDesktopControllerObj.getLocationDataLabel());
                mainDesktopControllerObj.getLocationDataLabel().toBack();
            }
            try {
                String gameDataFileContent = Files.readString(Paths.get(URLStrings.serverGameDataFileURL));
                String locationValue = extractValueByKey(gameDataFileContent, "location");
                if (locationValue != null) {
                    String[] locationList = locationValue.split(",");
                    mainDesktopControllerObj.getLocationStreetLabel().setText(locationList[0] + ",");
                    mainDesktopControllerObj.getLocationAreaLabel().setText(locationList[1] + ",");
                    mainDesktopControllerObj.getLocationCountyLabel().setText(locationList[2]);
                } else {
                    log("locationValue was null; putting blank values", LogUtils.Severity.ERROR);
                    mainDesktopControllerObj.getLocationStreetLabel().setText("");
                    mainDesktopControllerObj.getLocationAreaLabel().setText("");
                    mainDesktopControllerObj.getLocationCountyLabel().setText("");
                }
            } catch (IOException e) {
                logError("Could Not Read FileString For LocationData: ", e);
            }
        });
    }

    private static void runUpdateID() {
        try {
            if (ConfigReader.configRead("uiSettings", "enableIDPopup").equalsIgnoreCase("true")) {
                CustomWindow IDWindow = WindowManager.createCustomWindow(mainDesktopControllerObj.getDesktopContainer(), "Windows/Server/currentID-view.fxml", "Current IDs", true, 1, true, true, mainDesktopControllerObj.getTaskBarApps(), new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Apps/license.png"))));

                try {
                    if (!ConfigReader.configRead("misc", "IDDuration").equals("infinite")) {
                        PauseTransition delay = null;
                        try {
                            delay = new PauseTransition(Duration.seconds(Double.parseDouble(ConfigReader.configRead("misc", "IDDuration"))));
                        } catch (IOException e) {
                            logError("ID could not be closed: ", e);
                        }
                        if (IDWindow != null) {
                            delay.setOnFinished(event -> {
                                try {
                                    IDWindow.closeWindow();
                                } catch (NullPointerException e) {
                                    log("ID Window was closed before it could be automatically closed", LogUtils.Severity.WARN);
                                }
                            });
                        }
                        delay.play();
                    }
                } catch (IOException e) {
                    logError("could not read IDDuration: ", e);
                }
            } else {
                log("Recieved ID Update, but popups are disabled", LogUtils.Severity.INFO);
                NotificationManager.showNotificationInfo("ID Manager", "A New ID Has Been Recieved");
            }
        } catch (IOException e) {
            logError("Could not get enableIDPopup from config: ", e);
        }
    }

    private static void runUpdateCallout() {
        try {
            if (ConfigReader.configRead("uiSettings", "enableCalloutPopup").equalsIgnoreCase("true")) {
                calloutWindow = WindowManager.createCustomWindow(mainDesktopControllerObj.getDesktopContainer(), "Windows/Server/callout-view.fxml", "Callout Display", false, 1, true, true, mainDesktopControllerObj.getTaskBarApps(), new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Apps/callout.png"))));
                try {
                    if (ConfigReader.configRead("soundSettings", "playCallout").equalsIgnoreCase("true")) {
                        playSound(controllerUtils.getJarPath() + "/sounds/alert-callout.wav");
                    }
                } catch (IOException e) {
                    logError("Error getting configValue for playCallout: ", e);
                }

                try {
                    if (!ConfigReader.configRead("misc", "calloutDuration").equals("infinite")) {
                        PauseTransition delay = null;
                        try {
                            delay = new PauseTransition(Duration.seconds(Double.parseDouble(ConfigReader.configRead("misc", "calloutDuration"))));
                        } catch (IOException e) {
                            logError("Callout could not be closed: ", e);
                        }
                        if (calloutWindow != null) {
                            delay.setOnFinished(event -> {
                                try {
                                    calloutWindow.closeWindow();
                                    Callout callout = getCallout();

                                    if (callout != null) {
                                        String street = callout.getStreet() != null ? callout.getStreet() : "Not Found";
                                        String type = callout.getType() != null ? callout.getType() : "Not Found";
                                        String number = callout.getNumber() != null ? callout.getNumber() : "Not Found";
                                        String area = callout.getArea() != null ? callout.getArea() : "Not Found";
                                        String priority = callout.getPriority() != null ? callout.getPriority() : "Not Found";
                                        String time = callout.getStartTime() != null ? callout.getStartTime() : "Not Found";
                                        String date = callout.getStartDate() != null ? callout.getStartDate() : "Not Found";
                                        String county = callout.getCounty() != null ? callout.getCounty() : "Not Found";
                                        String desc = callout.getDescription() != null ? callout.getDescription() : "Not Found";
                                        String message = callout.getMessage() != null ? callout.getMessage() : "Not Found";
                                        String status = callout.getStatus() != null ? callout.getStatus() : "Not Responded";
                                        if (desc.isEmpty()) {
                                            desc = message;
                                        } else {
                                            desc = desc + "\n" + message;
                                        }
                                        CalloutManager.addCallout(URLStrings.calloutDataURL, number, type, desc, message, priority, street, area, county, time, date, status);
                                        NotificationManager.showNotificationInfo("Callout Manager", "Callout Recieved #" + number + ", Type: " + type + ". Added To Active Calls.");
                                    }
                                } catch (NullPointerException e) {
                                    log("Callout Window was closed before it could be automatically closed", LogUtils.Severity.WARN);
                                }
                            });
                        }
                        delay.play();
                    }
                } catch (IOException e) {
                    logError("could not read calloutDuration: ", e);
                }
            } else {
                log("Callout Popups are disabled", LogUtils.Severity.DEBUG);
                log("Adding Callout To Active", LogUtils.Severity.INFO);
                Callout callout = getCallout();

                if (callout != null) {
                    String street = callout.getStreet() != null ? callout.getStreet() : "Not Found";
                    String type = callout.getType() != null ? callout.getType() : "Not Found";
                    String number = callout.getNumber() != null ? callout.getNumber() : "Not Found";
                    String area = callout.getArea() != null ? callout.getArea() : "Not Found";
                    String priority = callout.getPriority() != null ? callout.getPriority() : "Not Found";
                    String time = callout.getStartTime() != null ? callout.getStartTime() : "Not Found";
                    String date = callout.getStartDate() != null ? callout.getStartDate() : "Not Found";
                    String county = callout.getCounty() != null ? callout.getCounty() : "Not Found";
                    String desc = callout.getDescription() != null ? callout.getDescription() : "Not Found";
                    String message = callout.getMessage() != null ? callout.getMessage() : "Not Found";
                    String status = callout.getStatus() != null ? callout.getStatus() : "Not Responded";
                    if (desc.isEmpty()) {
                        desc = message;
                    } else {
                        desc = desc + "\n" + message;
                    }
                    CalloutManager.addCallout(URLStrings.calloutDataURL, number, type, desc, message, priority, street, area, county, time, date, status);
                    NotificationManager.showNotificationInfo("Callout Manager", "Callout Recieved #" + number + ", Type: " + type + ". Added To Active Calls.");
                }
            }
        } catch (IOException e) {
            logError("Could not get enableCallout config option, ", e);
        }
    }

    private static void runTrafficStopUpdate() {
        try {
            if (ConfigReader.configRead("uiSettings", "enableTrafficStopPopup").equalsIgnoreCase("true")) {
                CustomWindow trafficStopWindow = WindowManager.createCustomWindow(mainDesktopControllerObj.getDesktopContainer(), "Windows/Server/trafficStop-view.fxml", "Traffic Stop Data", true, 1, true, true, mainDesktopControllerObj.getTaskBarApps(), new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/trafficStop.png"))));

                if (trafficStopWindow != null && trafficStopWindow.controller != null) {
                    trafficStopController.trafficStopController = (trafficStopController) trafficStopWindow.controller;

                    try {
                        trafficStopController.trafficStopController.updateTrafficStopFields();
                    } catch (IOException e) {
                        logError("Error updating traffic stop fields from UPDATE_TRAFFIC_STOP, ", e);
                    }
                }
                Platform.runLater(() -> {
                    try {
                        settingsController.loadTheme();
                    } catch (IOException e) {
                        logError("Error loading theme from trafficStop", e);
                    }
                });

                try {
                    if (!ConfigReader.configRead("misc", "TrafficStopDuration").equals("infinite")) {
                        PauseTransition delay = null;
                        try {
                            delay = new PauseTransition(Duration.seconds(Double.parseDouble(ConfigReader.configRead("misc", "TrafficStopDuration"))));
                        } catch (IOException e) {
                            logError("TrafficStop could not be closed: ", e);
                        }
                        if (trafficStopWindow != null) {
                            delay.setOnFinished(event12 -> {
                                try {
                                    trafficStopWindow.closeWindow();
                                } catch (NullPointerException e) {
                                    log("TrafficStop Window was closed before it could be automatically closed", LogUtils.Severity.WARN);
                                }
                            });
                        }
                        delay.play();
                    }
                } catch (IOException e) {
                    logError("could not read TrafficStopDuration: ", e);
                }
            } else {
                log("Recieved Traffic Stop Update message but popups are disabled", LogUtils.Severity.WARN);
            }
        } catch (IOException e) {
            logError("Could not get enableTrafficStopPopup from config, ", e);
        }
    }

    public interface ServerStatusListener {

        void onStatusChanged(boolean isConnected);
    }
}
