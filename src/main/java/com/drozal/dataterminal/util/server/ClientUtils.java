package com.drozal.dataterminal.util.server;

import com.drozal.dataterminal.actionController;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Window.windowUtils;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.drozal.dataterminal.actionController.CalloutStage;
import static com.drozal.dataterminal.actionController.IDStage;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;

@SuppressWarnings("ConstantValue")
public class ClientUtils {
	private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	public static Boolean isConnected = false;
	public static String port;
	public static String inet;
	private static Socket socket = null;
	private static ServerStatusListener statusListener;
	private static volatile boolean canUpdateCallout = true;
	
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
	
	public static void connectToService(String serviceAddress, int servicePort) throws IOException {
		if (socket != null && !socket.isClosed()) {
			socket.close();
		}
		new Thread(() -> {
			try {
				socket = new Socket();
				Platform.runLater(() -> {
					if (actionController.clientController != null) {
						actionController.clientController.getStatusLabel().setText("Testing Connection...");
						actionController.clientController.getStatusLabel().setStyle("-fx-background-color: orange;");
					}
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
				ConfigWriter.configwrite("connectionSettings", "lastIPV4Connection", serviceAddress);
				ConfigWriter.configwrite("connectionSettings", "lastPortConnection", String.valueOf(servicePort));
			} catch (IOException e) {
				isConnected = false;
				notifyStatusChanged(isConnected);
				log("Failed to connect: " + e.getMessage(), LogUtils.Severity.ERROR);
			}
		}).start();
	}
	
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
							log("Received ID update message from server.", LogUtils.Severity.DEBUG);
							FileUtlis.receiveIDFromServer(4096);
							Platform.runLater(() -> {
								if (IDStage != null && IDStage.isShowing()) {
									IDStage.close();
									IDStage = null;
								}
								IDStage = new Stage();
								IDStage.initStyle(StageStyle.UNDECORATED);
								FXMLLoader loader = new FXMLLoader(
										actionController.class.getResource("currentID-view.fxml"));
								//noinspection UnusedAssignment
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
									IDStage.setAlwaysOnTop(
											ConfigReader.configRead("AOTSettings", "AOTID").equals("true"));
								} catch (IOException e) {
									logError("Could not fetch AOTID: ", e);
								}
								
								windowUtils.centerStageOnMainApp(IDStage);
								
								try {
									if (!ConfigReader.configRead("misc", "IDDuration").equals("infinite")) {
										PauseTransition delay = null;
										try {
											delay = new PauseTransition(Duration.seconds(
													Double.parseDouble(ConfigReader.configRead("misc", "IDDuration"))));
										} catch (IOException e) {
											logError("ID could not be closed: ", e);
										}
										if (IDStage != null) {
											//noinspection DataFlowIssue
											delay.setOnFinished(event -> {
												try {
													IDStage.close();
												} catch (NullPointerException e) {
													log("IDStage was closed before it could be automtically closed",
													    LogUtils.Severity.WARN);
												}
											});
										}
										//noinspection DataFlowIssue
										delay.play();
									}
								} catch (IOException e) {
									logError("could not read IDDuration: ", e);
								}
								
								
								IDStage.setOnHidden(event -> IDStage = null);
							});
							break;
						case "UPDATE_CALLOUT":
							if (!canUpdateCallout) {
								break;
							}
							
							canUpdateCallout = false;
							scheduler.schedule(() -> canUpdateCallout = true, 4, TimeUnit.SECONDS);
							
							log("Received Callout update message from server.", LogUtils.Severity.DEBUG);
							FileUtlis.receiveCalloutFromServer(4096);
							Platform.runLater(() -> {
								if (CalloutStage != null && CalloutStage.isShowing()) {
									CalloutStage.close();
									CalloutStage = null;
								}
								CalloutStage = new Stage();
								FXMLLoader loader = new FXMLLoader(
										actionController.class.getResource("callout-view.fxml"));
								//noinspection UnusedAssignment
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
									CalloutStage.setAlwaysOnTop(
											ConfigReader.configRead("AOTSettings", "AOTCallout").equals("true"));
								} catch (IOException e) {
									logError("Could not fetch AOTCallout: ", e);
								}
								CalloutStage.initStyle(StageStyle.UNDECORATED);
								CalloutStage.show();
								CalloutStage.centerOnScreen();
								
								windowUtils.centerStageOnMainApp(CalloutStage);
								
								try {
									if (!ConfigReader.configRead("misc", "calloutDuration").equals("infinite")) {
										PauseTransition delay = null;
										try {
											delay = new PauseTransition(Duration.seconds(Double.parseDouble(
													ConfigReader.configRead("misc", "calloutDuration"))));
										} catch (IOException e) {
											logError("Callout could not be closed: ", e);
										}
										if (CalloutStage != null) {
											//noinspection DataFlowIssue
											delay.setOnFinished(event -> {
												try {
													CalloutStage.close();
													CalloutStage = null;
												} catch (NullPointerException e) {
													log("CalloutStage was closed before it could be automatically closed",
													    LogUtils.Severity.WARN);
												}
											});
										}
										//noinspection DataFlowIssue
										delay.play();
									}
								} catch (IOException e) {
									logError("could not read calloutDuration: ", e);
								}
								
								CalloutStage.setOnHidden(event -> CalloutStage = null);
							});
							break;
						case "UPDATE_WORLD_PED":
							log("Received World Ped update message from server.", LogUtils.Severity.DEBUG);
							FileUtlis.receiveWorldPedFromServer(4096);
							break;
						case "UPDATE_WORLD_VEH":
							log("Received World Veh update message from server.", LogUtils.Severity.DEBUG);
							FileUtlis.receiveWorldVehFromServer(4096);
							
							break;
						default:
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
	
	public static void setStatusListener(ServerStatusListener listener) {
		statusListener = listener;
	}
	
	private static void notifyStatusChanged(boolean status) {
		if (statusListener != null) {
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
						Platform.runLater(() -> {
							try {
								connectToService(serverAddress, serverPort);
							} catch (IOException e) {
								log("Error connecting to server; " + serverAddress + ":" + serverPort + " | " + e.getMessage(),
								    LogUtils.Severity.ERROR);
							}
						});
					}
				} else {
					log("Already connected", LogUtils.Severity.WARN);
				}
			}
		} catch (IOException e) {
			log("Error listening for broadcasts: " + e.getMessage(), LogUtils.Severity.ERROR);
		}
	}
	
	public interface ServerStatusListener {
		void onStatusChanged(boolean isConnected);
	}
}
