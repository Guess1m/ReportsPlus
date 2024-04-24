package com.drozal.dataterminal.util.server;

import com.drozal.dataterminal.util.LogUtils;
import javafx.application.Platform;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ServerUtils {
    private static final Set<String> resolvedServices = new HashSet<>();
    private static final CountDownLatch latch = new CountDownLatch(1); // For synchronization
    private static final long HEARTBEAT_TIMEOUT = TimeUnit.SECONDS.toMillis(8);  // 10 seconds timeout
    public static Boolean isConnected = false;
    public static String port = "";
    public static String inet = "";
    public static String service = "";
    static JmDNS jmdns;
    private static Socket socket; // Declare socket
    private static long lastHeartbeat = System.currentTimeMillis();
    private static ServerStatusListener statusListener;

    public static void connectToService(String serviceAddress) {
        try {
            // Initialize JmDNS
            jmdns = JmDNS.create(InetAddress.getLocalHost());
            jmdns.addServiceListener("_" + serviceAddress + "._tcp.local.", new ServiceListener() {
                @Override
                public void serviceAdded(ServiceEvent event) {
                    // Request service information
                    jmdns.requestServiceInfo(event.getType(), event.getName());
                }

                @Override
                public void serviceRemoved(ServiceEvent event) {
                    resolvedServices.remove(event.getName());
                    LogUtils.log("Service removed: " + event.getName(), LogUtils.Severity.INFO);
                }

                @Override
                public void serviceResolved(ServiceEvent event) {
                    ServiceInfo info = event.getInfo();
                    String serviceName = event.getName();
                    if (resolvedServices.contains(serviceName)) {
                        return;
                    }
                    resolvedServices.add(serviceName);
                    for (InetAddress address : info.getInetAddresses()) {
                        if (!address.isLoopbackAddress() && !address.isLinkLocalAddress() && !address.isMulticastAddress()) {
                            String serverIp = address.getHostAddress();
                            int serverPort = info.getPort();
                            LogUtils.log("Service resolved: " + info, LogUtils.Severity.INFO);
                            LogUtils.log("IP: " + serverIp + ", Port: " + serverPort, LogUtils.Severity.DEBUG);
                            port = String.valueOf(serverPort);
                            inet = serverIp;
                            service = serviceName;
                            isConnected = true;
                            notifyStatusChanged(isConnected);
                            latch.countDown();
                            try {
                                socket = new Socket(serverIp, serverPort); // Initialize socket
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            BufferedReader in = communicateToServer(serverIp, serverPort);
                            if (in != null) {
                                lastHeartbeat = System.currentTimeMillis(); // Update lastHeartbeat upon connection
                                startListeningForMessages(in);
                            } else {
                                LogUtils.logError("Failed to establish reader from server", null);
                            }
                            return;
                        }
                    }
                }
            });

            // Wait until a service is resolved
            latch.await();

            // Close JmDNS
            jmdns.close();
        } catch (Exception e) {
            LogUtils.logError("Error Connecting to service: ", e);
        }
    }

    private static void startListeningForMessages(BufferedReader in) {
        new Thread(() -> {
            try {
                String fromServer;
                while ((fromServer = in.readLine()) != null) {
                    if ("HEARTBEAT".equals(fromServer)) {
                        System.out.println("Heartbeat received from server.");
                        lastHeartbeat = System.currentTimeMillis();
                    }
                }
            } catch (IOException e) {
                LogUtils.logError("Server Connection may be lost: ", e);
            }
        }).start();

        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                if ((System.currentTimeMillis() - lastHeartbeat) > HEARTBEAT_TIMEOUT) {
                    LogUtils.log("Heartbeat missed. Server is down.", LogUtils.Severity.ERROR);
                    clearConnections();
                    break;
                }
                try {
                    Thread.sleep(5000);  // Check every 5 seconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    public static void clearConnections() {
        try {
            socket.close();
            jmdns.unregisterAllServices();
            jmdns.close();
            jmdns = null;
            resolvedServices.clear();
            isConnected = false;
            notifyStatusChanged(isConnected);
            port = "";
            service = "";
            inet = "";
        } catch (IOException e) {
            LogUtils.logError("Error while closing resources: ", e);
        }
    }

    public static void setStatusListener(ServerStatusListener statusListener) {
        ServerUtils.statusListener = statusListener;
    }

    private static void notifyStatusChanged(boolean isConnected) {
        if (statusListener != null) {
            Platform.runLater(() -> statusListener.onStatusChanged(isConnected));
        }
    }

    public static void sendInfoToServer(String data) throws IOException {
        // Check if socket is initialized
        if (socket != null) {
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);
            writer.println(data);
        } else {
            LogUtils.logError("Socket is null. Unable to send data to server.", null);
        }
    }

    public static BufferedReader communicateToServer(String serverIp, int serverPort) {
        try {
            Socket socket = new Socket(serverIp, serverPort); // Establish a connection to the server
            return new BufferedReader(new InputStreamReader(socket.getInputStream())); // Return the BufferedReader
        } catch (IOException e) {
            LogUtils.logError("Unable to connect to server: ", e);
            return null; // Return null if the connection or reader setup fails
        }
    }
}
