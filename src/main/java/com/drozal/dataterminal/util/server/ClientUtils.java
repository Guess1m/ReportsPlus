package com.drozal.dataterminal.util.server;

import com.drozal.dataterminal.Launcher;
import com.drozal.dataterminal.Windows.Server.ClientController;
import com.drozal.dataterminal.Windows.Server.CurrentIDViewController;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.Misc.CalloutManager;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Misc.NotificationManager;
import com.drozal.dataterminal.util.Window.windowUtils;
import com.drozal.dataterminal.util.server.Objects.Callout.Callout;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static com.drozal.dataterminal.DataTerminalHomeApplication.mainDesktopControllerObj;
import static com.drozal.dataterminal.DataTerminalHomeApplication.mainRT;
import static com.drozal.dataterminal.Windows.Server.CurrentIDViewController.*;
import static com.drozal.dataterminal.Windows.Server.calloutController.*;
import static com.drozal.dataterminal.util.Misc.AudioUtil.playSound;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.stringUtil.*;

public class ClientUtils {
	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
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
								mainDesktopControllerObj.getLocationDataLabel().setVisible(true);
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
									if (CurrentIDViewController.IDStage != null && CurrentIDViewController.IDStage.isShowing()) {
										CurrentIDViewController.IDStage.close();
										CurrentIDViewController.IDStage = null;
									}
									CurrentIDViewController.IDStage = new Stage();
									CurrentIDViewController.IDStage.initStyle(StageStyle.UNDECORATED);
									FXMLLoader loader = new FXMLLoader(
											Launcher.class.getResource("Windows/Server/currentID-view.fxml"));
									Parent root = null;
									try {
										root = loader.load();
									} catch (IOException e) {
										throw new RuntimeException(e);
									}
									Scene newScene = new Scene(root);
									CurrentIDViewController.IDStage.setTitle("Current ID");
									CurrentIDViewController.IDStage.setScene(newScene);
									CurrentIDViewController.IDStage.show();
									CurrentIDViewController.IDStage.centerOnScreen();
									try {
										CurrentIDViewController.IDStage.setAlwaysOnTop(
												ConfigReader.configRead("AOTSettings", "AOTID").equals("true"));
									} catch (IOException e) {
										logError("Could not fetch AOTID: ", e);
									}
									
									try {
										if (ConfigReader.configRead("layout", "rememberIDLocation").equals("true")) {
											if (IDFirstShown) {
												windowUtils.centerStageOnMainApp(CurrentIDViewController.IDStage);
												log("IDStage opened via UPDATE_ID message, first time centered",
												    LogUtils.Severity.INFO);
											} else {
												if (IDScreen != null) {
													Rectangle2D screenBounds = IDScreen.getVisualBounds();
													CurrentIDViewController.IDStage.setX(IDx);
													CurrentIDViewController.IDStage.setY(IDy);
													
													if (IDx < screenBounds.getMinX() || IDx > screenBounds.getMaxX() || IDy < screenBounds.getMinY() || IDy > screenBounds.getMaxY()) {
														windowUtils.centerStageOnMainApp(
																CurrentIDViewController.IDStage);
													}
												} else {
													windowUtils.centerStageOnMainApp(CurrentIDViewController.IDStage);
												}
												log("IDStage opened via UPDATE_ID message, XValue: " + IDx + " YValue: " + IDy,
												    LogUtils.Severity.INFO);
											}
										} else {
											windowUtils.centerStageOnMainApp(CurrentIDViewController.IDStage);
										}
									} catch (IOException e) {
										logError("Could not read rememberIDLocation from UPDATE_ID: ", e);
									}
									
									try {
										if (!ConfigReader.configRead("misc", "IDDuration").equals("infinite")) {
											PauseTransition delay = null;
											try {
												delay = new PauseTransition(Duration.seconds(Double.parseDouble(
														ConfigReader.configRead("misc", "IDDuration"))));
											} catch (IOException e) {
												logError("ID could not be closed: ", e);
											}
											if (CurrentIDViewController.IDStage != null) {
												delay.setOnFinished(event -> {
													try {
														CurrentIDViewController.IDStage.close();
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
									
									CurrentIDViewController.IDStage.setOnHidden(new EventHandler<WindowEvent>() {
										@Override
										public void handle(WindowEvent event) {
											IDx = CurrentIDViewController.IDStage.getX();
											IDy = CurrentIDViewController.IDStage.getY();
											IDScreen = Screen.getScreensForRectangle(IDx, IDy,
											                                         CurrentIDViewController.IDStage.getWidth(),
											                                         CurrentIDViewController.IDStage.getHeight()).stream().findFirst().orElse(
													null);
											log("IDStage closed via UPDATE_ID message, set XValue: " + IDx + " YValue: " + IDy,
											    LogUtils.Severity.DEBUG);
											IDFirstShown = false;
											CurrentIDViewController.IDStage = null;
										}
									});
								});
							} else {
								log("Recieved ID Update, but popups are disabled", LogUtils.Severity.INFO);
								NotificationManager.showNotificationInfo("ID Manager", "A New ID Has Been Recieved",
								                                         mainRT);
							}
							break;
						case "UPDATE_CALLOUT":
							log("Received Callout update", LogUtils.Severity.DEBUG);
							FileUtlis.receiveCalloutFromServer(4096);
							if (ConfigReader.configRead("uiSettings", "enableCalloutPopup").equalsIgnoreCase("true")) {
								Platform.runLater(() -> {
									if (CalloutStage != null && CalloutStage.isShowing()) {
										CalloutStage.close();
										CalloutStage = null;
									}
									CalloutStage = new Stage();
									FXMLLoader loader = new FXMLLoader(
											Launcher.class.getResource("Windows/Server/callout-view.fxml"));
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
									try {
										if (ConfigReader.configRead("soundSettings", "playCallout").equalsIgnoreCase(
												"true")) {
											playSound(getJarPath() + "/sounds/alert-callout.wav");
										}
									} catch (IOException e) {
										logError("Error getting configValue for playCallout: ", e);
									}
									
									CalloutStage.centerOnScreen();
									
									try {
										if (ConfigReader.configRead("layout", "rememberCalloutLocation").equals(
												"true")) {
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
									                                         "Callout Recieved #" + number + ", Type: " + type + ". Added To Active Calls.",
									                                         mainRT);
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
