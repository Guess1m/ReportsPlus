package com.drozal.dataterminal.util.server;
import com.drozal.dataterminal.util.LogUtils;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class ServerUtils {
    private static final Set<String> resolvedServices = new HashSet<>();
    private static final CountDownLatch latch = new CountDownLatch(1); // For synchronization
    private static Socket socket = null;

    public static void connectToService(String serviceAddress) {
        try {
            // Initialize JmDNS
            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
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
                        // Skip if the service has already been resolved
                        return;
                    }
                    resolvedServices.add(serviceName);
                    for (InetAddress address : info.getInetAddresses()) {
                        if (!address.isLoopbackAddress() && !address.isLinkLocalAddress() && !address.isMulticastAddress()) {
                            String serverIp = address.getHostAddress();
                            int serverPort = info.getPort();
                            LogUtils.log("Service resolved: " + info, LogUtils.Severity.INFO);
                            LogUtils.log("IP: " + serverIp + ", Port: " + serverPort, LogUtils.Severity.DEBUG);
                            latch.countDown(); // Reduce count, allowing the main thread to proceed
                            communicateToServer(serverIp, serverPort);
                            return; // Exit loop after finding a suitable address
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

    public static void sendInfoToServer(String data) throws IOException {
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);
            writer.println(data);
    }

    private static void communicateToServer(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            LogUtils.log("Connected to server at " + ip + ":" + port, LogUtils.Severity.INFO);

            // Send a message to the server
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);
            writer.println("User: John Doe, Action: Login");
        } catch (IOException e) {
            LogUtils.logError("Error Connecting to server: ", e);
        }
    }
}