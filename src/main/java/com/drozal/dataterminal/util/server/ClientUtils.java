package com.drozal.dataterminal.util.server;

import com.drozal.dataterminal.Desktop.Utils.WindowUtils.CustomWindow;
import com.drozal.dataterminal.Launcher;
import com.drozal.dataterminal.Windows.Server.ClientController;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.Misc.CalloutManager;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Misc.NotificationManager;
import com.drozal.dataterminal.util.server.Objects.Callout.Callout;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

import static com.drozal.dataterminal.DataTerminalHomeApplication.mainDesktopControllerObj;
import static com.drozal.dataterminal.Desktop.Utils.WindowUtils.WindowManager.createFakeWindow;
import static com.drozal.dataterminal.Windows.Server.calloutController.getCallout;
import static com.drozal.dataterminal.util.Misc.AudioUtil.playSound;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.stringUtil.*;

public class ClientUtils {
	public static Boolean isConnected = false;
	public static String port;
	public static String inet;
	public static CustomWindow calloutWindow;
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
	
	public static void connectToService(String serviceAddress, int servicePort) throws IOException {
		if (socket != null && !socket.isClosed()) {
			log("Closing existing socket before reconnecting.", LogUtils.Severity.INFO);
			socket.close();
		}
		new Thread(() -> {
			try {
				log("Initializing socket.", LogUtils.Severity.INFO);
				socket = new Socket();
				
				Platform.runLater(() -> {
					if (ClientController.clientController != null) {
						ClientController.clientController.getStatusLabel().setText("Testing Connection...");
						ClientController.clientController.getStatusLabel().setStyle("-fx-background-color: orange;");
					}
				});
				
				log("Attempting to connect to " + serviceAddress + ":" + servicePort, LogUtils.Severity.INFO);
				socket.connect(new InetSocketAddress(serviceAddress, servicePort), 10000);
				log("Socket connected successfully.", LogUtils.Severity.INFO);
				
				socket.setSoTimeout(Integer.parseInt(ConfigReader.configRead("connectionSettings", "socketTimeout")));
				log("Socket timeout set to " + Integer.parseInt(
						ConfigReader.configRead("connectionSettings", "socketTimeout")), LogUtils.Severity.INFO);
				
				isConnected = true;
				notifyStatusChanged(isConnected);
				
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				Thread readerThread = new Thread(() -> receiveMessages(in));
				readerThread.start();
				
				log("Reader thread started.", LogUtils.Severity.INFO);
				log("CONNECTED: " + serviceAddress + ":" + servicePort, LogUtils.Severity.INFO);
				
				port = String.valueOf(servicePort);
				inet = serviceAddress;
				
				log("Writing connection settings to config. IP: " + serviceAddress + " Port: " + servicePort,
				    LogUtils.Severity.INFO);
				ConfigWriter.configwrite("connectionSettings", "lastIPV4Connection", serviceAddress);
				ConfigWriter.configwrite("connectionSettings", "lastPortConnection", String.valueOf(servicePort));
			} catch (IOException e) {
				isConnected = false;
				notifyStatusChanged(isConnected);
				log("Failed to connect: " + e.getMessage(), LogUtils.Severity.ERROR);
				log("Stack trace: " + Arrays.toString(e.getStackTrace()), LogUtils.Severity.ERROR);
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
							log("Received shutdown, Disconnecting...", LogUtils.Severity.DEBUG);
							disconnectFromService();
							break label;
						
						case "UPDATE_LOCATION":
							log("Received Location update", LogUtils.Severity.DEBUG);
							FileUtlis.receiveLocationFromServer(1024);
							Platform.runLater(() -> {
								if (!mainDesktopControllerObj.getTopBar().getChildren().contains(
										mainDesktopControllerObj.getLocationDataLabel())) {
									mainDesktopControllerObj.getTopBar().getChildren().add(
											mainDesktopControllerObj.getLocationDataLabel());
								}
								try {
									mainDesktopControllerObj.getLocationDataLabel().setText(
											Files.readString(Paths.get(currentLocationFileURL)));
								} catch (IOException e) {
									logError("Could Not Read FileString For LocationData: ", e);
								}
							});
							break;
						
						case "UPDATE_ID":
							log("Received ID update", LogUtils.Severity.DEBUG);
							FileUtlis.receiveIDFromServer(4096);
							if (ConfigReader.configRead("uiSettings", "enableIDPopup").equalsIgnoreCase("true")) {
								Platform.runLater(() -> {
									CustomWindow IDWindow = createFakeWindow(
											mainDesktopControllerObj.getDesktopContainer(),
											"Windows/Server/currentID-view.fxml", "Current IDs", false, 2, true, true,
											mainDesktopControllerObj.getTaskBarApps(), new Image(Objects.requireNonNull(
													Launcher.class.getResourceAsStream(
															"/com/drozal/dataterminal/imgs/icons/Apps/license.png"))));
									
									try {
										if (!ConfigReader.configRead("misc", "IDDuration").equals("infinite")) {
											PauseTransition delay = null;
											try {
												delay = new PauseTransition(Duration.seconds(Double.parseDouble(
														ConfigReader.configRead("misc", "IDDuration"))));
											} catch (IOException e) {
												logError("ID could not be closed: ", e);
											}
											if (IDWindow != null) {
												delay.setOnFinished(event -> {
													try {
														IDWindow.closeWindow();
													} catch (NullPointerException e) {
														log("ID Window was closed before it could be automatically closed",
														    LogUtils.Severity.WARN);
													}
												});
											}
											delay.play();
										}
									} catch (IOException e) {
										logError("could not read IDDuration: ", e);
									}
								});
							} else {
								log("Recieved ID Update, but popups are disabled", LogUtils.Severity.INFO);
								NotificationManager.showNotificationInfo("ID Manager", "A New ID Has Been Recieved");
							}
							break;
						
						case "UPDATE_CALLOUT":
							log("Received Callout update", LogUtils.Severity.DEBUG);
							FileUtlis.receiveCalloutFromServer(4096);
							if (ConfigReader.configRead("uiSettings", "enableCalloutPopup").equalsIgnoreCase("true")) {
								Platform.runLater(() -> {
									calloutWindow = createFakeWindow(mainDesktopControllerObj.getDesktopContainer(),
									                                 "Windows/Server/callout-view.fxml",
									                                 "Callout Display", false, 4, true, true,
									                                 mainDesktopControllerObj.getTaskBarApps(),
									                                 new Image(Objects.requireNonNull(
											                                 Launcher.class.getResourceAsStream(
													                                 "/com/drozal/dataterminal/imgs/icons/Apps/callout.png"))));
									try {
										if (ConfigReader.configRead("soundSettings", "playCallout").equalsIgnoreCase(
												"true")) {
											playSound(getJarPath() + "/sounds/alert-callout.wav");
										}
									} catch (IOException e) {
										logError("Error getting configValue for playCallout: ", e);
									}
									// todo remove all rememberCalloutLocation references
									
									try {
										if (!ConfigReader.configRead("misc", "calloutDuration").equals("infinite")) {
											PauseTransition delay = null;
											try {
												delay = new PauseTransition(Duration.seconds(Double.parseDouble(
														ConfigReader.configRead("misc", "calloutDuration"))));
											} catch (IOException e) {
												logError("Callout could not be closed: ", e);
											}
											if (calloutWindow != null) {
												delay.setOnFinished(event -> {
													try {
														calloutWindow.closeWindow();
													} catch (NullPointerException e) {
														log("Callout Window was closed before it could be automatically closed",
														    LogUtils.Severity.WARN);
													}
												});
											}
											delay.play();
										}
									} catch (IOException e) {
										logError("could not read calloutDuration: ", e);
									}
								});
							} else {
								log("Callout Popups are disabled", LogUtils.Severity.DEBUG);
								log("Adding Callout To Active", LogUtils.Severity.INFO);
								Callout callout = getCallout();
								
								String message;
								String desc;
								String status;
								if (callout != null) {
									String street = callout.getStreet() != null ? callout.getStreet() : "Not Available";
									String type = callout.getType() != null ? callout.getType() : "Not Available";
									String number = callout.getNumber() != null ? callout.getNumber() : "Not Available";
									String area = callout.getArea() != null ? callout.getArea() : "Not Available";
									String priority = callout.getPriority() != null ? callout.getPriority() : "Not Available";
									String time = callout.getStartTime() != null ? callout.getStartTime() : "Not Available";
									String date = callout.getStartDate() != null ? callout.getStartDate() : "Not Available";
									String county = callout.getCounty() != null ? callout.getCounty() : "Not Available";
									desc = callout.getDescription() != null ? callout.getDescription() : "Not Available";
									message = callout.getMessage() != null ? callout.getMessage() : "Not Available";
									status = callout.getStatus() != null ? callout.getStatus() : "Not Responded";
									if (desc.isEmpty()) {
										desc = message;
									} else {
										desc = desc + "\n" + message;
									}
									CalloutManager.addCallout(calloutDataURL, number, type, desc, message, priority,
									                          street, area, county, time, date, status);
									NotificationManager.showNotificationInfo("Callout Manager",
									                                         "Callout Recieved #" + number + ", Type: " + type + ". Added To Active Calls.");
								}
							}
							break;
						
						case "UPDATE_WORLD_PED":
							log("Received World Ped update", LogUtils.Severity.DEBUG);
							FileUtlis.receiveWorldPedFromServer(4096);
							break;
						
						case "UPDATE_WORLD_VEH":
							log("Received World Veh update", LogUtils.Severity.DEBUG);
							FileUtlis.receiveWorldVehFromServer(4096);
							break;
						
						default:
							break;
					}
				}
			} catch (SocketTimeoutException e) {
				isConnected = false;
				notifyStatusChanged(isConnected);
				try {
					log("Read timed out after " + socket.getSoTimeout() + " milliseconds", LogUtils.Severity.ERROR);
				} catch (SocketException ex) {
					logError("Could not getSoTimeout: ", e);
				}
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
					break;
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
