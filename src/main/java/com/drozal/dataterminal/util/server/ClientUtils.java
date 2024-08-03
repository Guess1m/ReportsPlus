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
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.drozal.dataterminal.actionController.*;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;

public class ClientUtils {
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
			log("Closing existing socket before reconnecting.", LogUtils.Severity.INFO);
			socket.close();
		}
		new Thread(() -> {
			try {
				log("Initializing socket.", LogUtils.Severity.INFO);
				socket = new Socket();
				
				Platform.runLater(() -> {
					if (actionController.clientController != null) {
						actionController.clientController.getStatusLabel().setText("Testing Connection...");
						actionController.clientController.getStatusLabel().setStyle("-fx-background-color: orange;");
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
								
								try {
									if (ConfigReader.configRead("layout", "rememberIDLocation").equals("true")) {
										if (IDFirstShown) {
											windowUtils.centerStageOnMainApp(IDStage);
											log("IDStage opened via UPDATE_ID message, first time centered",
											    LogUtils.Severity.INFO);
										} else {
											if (IDScreen != null) {
												Rectangle2D screenBounds = IDScreen.getVisualBounds();
												IDStage.setX(IDx);
												IDStage.setY(IDy);
												
												if (IDx < screenBounds.getMinX() || IDx > screenBounds.getMaxX() || IDy < screenBounds.getMinY() || IDy > screenBounds.getMaxY()) {
													windowUtils.centerStageOnMainApp(IDStage);
												}
											} else {
												windowUtils.centerStageOnMainApp(IDStage);
											}
											log("IDStage opened via UPDATE_ID message, XValue: " + IDx + " YValue: " + IDy,
											    LogUtils.Severity.INFO);
										}
									} else {
										windowUtils.centerStageOnMainApp(IDStage);
									}
								} catch (IOException e) {
									logError("Could not read rememberIDLocation from UPDATE_ID: ", e);
								}
								
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
											delay.setOnFinished(event -> {
												try {
													IDStage.close();
												} catch (NullPointerException e) {
													log("IDStage was closed before it could be automtically closed",
													    LogUtils.Severity.WARN);
												}
											});
										}
										delay.play();
									}
								} catch (IOException e) {
									logError("could not read IDDuration: ", e);
								}
								
								IDStage.setOnHidden(new EventHandler<WindowEvent>() {
									@Override
									public void handle(WindowEvent event) {
										IDx = IDStage.getX();
										IDy = IDStage.getY();
										IDScreen = Screen.getScreensForRectangle(IDx, IDy, IDStage.getWidth(),
										                                         IDStage.getHeight()).stream().findFirst().orElse(
												null);
										log("IDStage closed via UPDATE_ID message, set XValue: " + IDx + " YValue: " + IDy,
										    LogUtils.Severity.DEBUG);
										IDFirstShown = false;
										IDStage = null;
									}
								});
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
								
								try {
									if (ConfigReader.configRead("layout", "rememberCalloutLocation").equals("true")) {
										if (CalloutFirstShown) {
											windowUtils.centerStageOnMainApp(CalloutStage);
											log("CalloutStage opened via UPDATE_CALLOUT message, first time centered",
											    LogUtils.Severity.INFO);
										} else {
											if (CalloutScreen != null) {
												Rectangle2D screenBounds = CalloutScreen.getVisualBounds();
												CalloutStage.setX(Calloutx);
												CalloutStage.setY(Callouty);
												
												if (Calloutx < screenBounds.getMinX() || Calloutx > screenBounds.getMaxX() || Callouty < screenBounds.getMinY() || Callouty > screenBounds.getMaxY()) {
													windowUtils.centerStageOnMainApp(CalloutStage);
												}
											} else {
												windowUtils.centerStageOnMainApp(CalloutStage);
											}
											log("CalloutStage opened via UPDATE_CALLOUT message, XValue: " + Calloutx + " YValue: " + Callouty,
											    LogUtils.Severity.INFO);
										}
									}
								} catch (IOException e) {
									logError("Could not read rememberCalloutLocation from UPDATE_CALLOUT: ", e);
								}
								
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
