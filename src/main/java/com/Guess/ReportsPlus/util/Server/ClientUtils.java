package com.Guess.ReportsPlus.util.Server;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager;
import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.Windows.Apps.ALPRViewController;
import com.Guess.ReportsPlus.Windows.Server.ClientController;
import com.Guess.ReportsPlus.Windows.Server.trafficStopController;
import com.Guess.ReportsPlus.Windows.Settings.settingsController;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.config.ConfigWriter;
import com.Guess.ReportsPlus.util.Misc.NotificationManager;
import com.Guess.ReportsPlus.util.Misc.Threading.WorkerThread;
import com.Guess.ReportsPlus.util.Other.Callout.CalloutManager;
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
import static com.Guess.ReportsPlus.Windows.Apps.CalloutViewController.calloutViewController;
import static com.Guess.ReportsPlus.Windows.Server.CalloutPopupController.getCallout;
import static com.Guess.ReportsPlus.util.Misc.AudioUtil.playSound;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.*;
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
			logInfo("Closing existing socket before reconnecting.");
			socket.close();
		}
		Runnable serverConnectionTask = () -> {
			logInfo("Started Server Thread");
			try {
				logInfo("Initializing socket.");
				socket = new Socket();
				
				if (ClientController.clientController != null) {
					Platform.runLater(() -> {
						ClientController.clientController.getStatusLabel().setText(localization.getLocalizedMessage("ServerConnectionWindow.TestingConnection", "Testing Connection..."));
						ClientController.clientController.getStatusLabel().setStyle("-fx-background-color: orange;");
					});
				}
				
				logInfo("Attempting to connect to " + serviceAddress + ":" + servicePort);
				socket.connect(new InetSocketAddress(serviceAddress, servicePort), 10000);
				logInfo("Socket connected successfully.");
				
				socket.setSoTimeout(Integer.parseInt(ConfigReader.configRead("connectionSettings", "socketTimeout")));
				logInfo("Socket timeout set to " + Integer.parseInt(ConfigReader.configRead("connectionSettings", "socketTimeout")));
				
				isConnected = true;
				notifyStatusChanged(isConnected);
				
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				receiveMessages(in);
				
				logInfo("Reader thread started.");
				logInfo("CONNECTED: " + serviceAddress + ":" + servicePort);
				
				port = String.valueOf(servicePort);
				inet = serviceAddress;
				
				logInfo("Writing connection settings to config. IP: " + serviceAddress + " Port: " + servicePort);
				ConfigWriter.configwrite("connectionSettings", "lastIPV4Connection", serviceAddress);
				ConfigWriter.configwrite("connectionSettings", "lastPortConnection", String.valueOf(servicePort));
			} catch (IOException e) {
				sendMessageToServer("SHUTDOWN");
				isConnected = false;
				notifyStatusChanged(isConnected);
				logError("Failed to connect: " + e.getMessage());
				logError("Stack trace: " + Arrays.toString(e.getStackTrace()));
			}
		};
		WorkerThread serverConnectionThread = new WorkerThread("ServerConnectionThread", serverConnectionTask);
		serverConnectionThread.start();
	}
	
	public synchronized static void receiveFileFromServer(int fileSize, String serverFileName, boolean showdebug) throws IOException {
		int bytesRead;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		Socket sock = null;
		
		try {
			sock = new Socket(ClientUtils.inet, Integer.parseInt(ClientUtils.port));
			if (showdebug) {
				logInfo("Starting file transfer: " + serverFileName + " (Size: " + fileSize + " bytes)");
			}
			
			byte[] mybytearray = new byte[fileSize];
			InputStream is = sock.getInputStream();
			fos = new FileOutputStream(controllerUtils.getServerDataFolderPath() + serverFileName);
			bos = new BufferedOutputStream(fos);
			
			if (showdebug) {
				logDebug("{File Transfer} Receiving file: " + serverFileName);
			}
			int totalBytesRead = 0;
			while ((bytesRead = is.read(mybytearray)) != -1) {
				bos.write(mybytearray, 0, bytesRead);
				totalBytesRead += bytesRead;
			}
			bos.flush();
			
			if (showdebug) {
				logInfo("{File Transfer} File transfer completed successfully. Total bytes received: " + totalBytesRead);
			}
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
				if (showdebug) {
					logInfo("{File Transfer} Closed recieveFile resources..");
				}
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
					logInfo("Message sent to server: " + message);
				}
			} catch (IOException e) {
				logError("Error sending message to server: " + e.getMessage());
			}
		} else {
			logWarn("Socket is not connected. Unable to send message: " + message);
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
						logInfo("Checking Server / Application Versions");
						serverVersion = serverVer;
						
						if (!serverVer.equalsIgnoreCase(version)) {
							logError("Versions dont match!");
							logDebug("Server Version: " + serverVer);
							logDebug("App Version: " + version);
							showNotificationErrorPersistent("Mismatched Versions", "Your Application and Server have mismatched versions, check logs!");
						} else {
							logInfo("Versions Match!");
							logDebug("Server Version: " + serverVer);
							logDebug("App Version: " + version);
						}
						continue;
					}
					switch (fromServer) {
						case "SHUTDOWN":
							logDebug("Received shutdown, Disconnecting...");
							disconnectFromService();
							break label;
						
						case "UPDATE_ALPR":
							logDebug("ALPR Update");
							receiveFileFromServer(4096, "ServerALPR.data", false);
							ALPRViewController.loadData();
							break;
						
						case "UPDATE_GAME_DATA":
							logDebug("Received Location Update");
							receiveFileFromServer(1024, "ServerGameData.data", true);
							runUpdateLocation();
							break;
						
						case "UPDATE_ID":
							logDebug("Received ID Update");
							receiveFileFromServer(4096, "ServerCurrentID.xml", true);
							runUpdateID();
							break;
						
						case "UPDATE_CALLOUT":
							logDebug("Received Callout Update");
							receiveFileFromServer(1024, "ServerCallout.xml", true);
							runUpdateCallout();
							break;
						
						case "UPDATE_WORLD_PED":
							logDebug("Received World Ped Update");
							receiveFileFromServer(8192, "ServerWorldPeds.data", true);
							break;
						
						case "UPDATE_TRAFFIC_STOP":
							logDebug("Received Traffic Stop Update");
							receiveFileFromServer(1024, "ServerTrafficStop.data", true);
							runTrafficStopUpdate();
							break;
						
						case "UPDATE_WORLD_VEH":
							logDebug("Received World Vehicle Update");
							receiveFileFromServer(16384, "ServerWorldCars.data", true);
							break;
						
						case "UPDATE_LOOKUP":
							logDebug("Received Lookup Update");
							receiveFileFromServer(256, "ServerLookup.data", true);
							runLookupUpdate();
							break;
						
						case "HEARTBEAT":
							long now = System.currentTimeMillis();
							long delta = now - lastUpdate;
							logDebug("Server Heartbeat: " + Math.round(delta) + "ms");
							sendMessageToServer("HEARTBEAT");
							lastUpdate = now;
							break;
						
						default:
							logDebug("Received unknown message: " + fromServer);
							break;
						
					}
				}
			} catch (SocketTimeoutException e) {
				sendMessageToServer("SHUTDOWN");
				isConnected = false;
				notifyStatusChanged(isConnected);
				try {
					logError("Read timed out after " + socket.getSoTimeout() + " milliseconds, missed heartbeat");
				} catch (SocketException ex) {
					logError("Could not getSoTimeout: ", e);
				}
			} catch (IOException e) {
				sendMessageToServer("SHUTDOWN");
				isConnected = false;
				notifyStatusChanged(isConnected);
				logError("Error reading from server: " + e.getMessage());
			}
		};
		WorkerThread messageReceiverThread = new WorkerThread("MessageReceiverThread", messageReceiverTask);
		messageReceiverThread.start();
	}
	
	private static void runLookupUpdate() {
		logInfo("Running Lookup Update");
		File lookupFile = new File(serverLookupURL);
		if (!lookupFile.exists()) {
			logError("No lookup data file found");
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
			logError("lookupFile content is null; not updating");
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
			logError("Error disconnecting from service: " + e.getMessage());
		}
	}
	
	public static void setStatusListener(ServerStatusListener listener) {
		statusListener = listener;
	}
	
	private static void notifyStatusChanged(boolean status) {
		if (statusListener != null) {
			logDebug("Client Connection Status Changed: " + isConnected);
			Platform.runLater(() -> statusListener.onStatusChanged(status));
		}
	}
	
	public static void listenForServerBroadcasts() {
		int broadCastPort = 8888;
		try {
			broadCastPort = Integer.parseInt(ConfigReader.configRead("connectionSettings", "broadcastPort"));
			logDebug("Using broadcastPort: " + broadCastPort);
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
						
						logInfo("Discovered server at " + serverAddress + ":" + serverPort);
						try {
							connectToService(serverAddress, serverPort);
							return;
						} catch (IOException e) {
							logError("Error connecting to server; " + serverAddress + ":" + serverPort + " | " + e.getMessage());
							return;
						}
					}
				} else {
					logWarn("Already connected");
					return;
				}
			}
		} catch (IOException e) {
			logError("Error listening for broadcasts: " + e.getMessage());
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
					logError("locationValue was null; putting blank values");
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
				CustomWindow IDWindow = WindowManager.createCustomWindow(mainDesktopControllerObj.getDesktopContainer(), "Windows/Server/currentID-view.fxml", "Current IDs", true, 1, true, true, mainDesktopControllerObj.getTaskBarApps(),
				                                                         new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Apps/license.png"))));
				
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
									logWarn("ID Window was closed before it could be automatically closed");
								}
							});
						}
						delay.play();
					}
				} catch (IOException e) {
					logError("could not read IDDuration: ", e);
				}
			} else {
				logInfo("Recieved ID Update, but popups are disabled");
				NotificationManager.showNotificationInfo("ID Manager", "A New ID Has Been Recieved");
			}
		} catch (IOException e) {
			logError("Could not get enableIDPopup from config: ", e);
		}
	}
	
	private static void runUpdateCallout() {
		try {
			if (ConfigReader.configRead("uiSettings", "enableCalloutPopup").equalsIgnoreCase("true")) {
				calloutWindow = WindowManager.createCustomWindow(mainDesktopControllerObj.getDesktopContainer(), "Windows/Server/callout-view.fxml", "Callout Display", false, 1, true, true, mainDesktopControllerObj.getTaskBarApps(),
				                                                 new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Apps/callout.png"))));
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
										CalloutManager.addCallout(URLStrings.calloutDataURL, number, type, desc, message, priority, street, area, county, time, date, status, null);
										NotificationManager.showNotificationInfo("Callout Manager", "Callout Recieved #" + number + ", Type: " + type + ". Added To Active Calls.");
										if (calloutViewController != null) {
											CalloutManager.loadActiveCallouts(calloutViewController.getActiveCalloutsTable());
											CalloutManager.loadHistoryCallouts(calloutViewController.getHistoryCalloutsTable());
										}
									}
								} catch (NullPointerException e) {
									logWarn("Callout Window was closed before it could be automatically closed");
								}
							});
						}
						delay.play();
					}
				} catch (IOException e) {
					logError("could not read calloutDuration: ", e);
				}
			} else {
				logDebug("Callout Popups are disabled");
				logInfo("Adding Callout To Active");
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
					CalloutManager.addCallout(URLStrings.calloutDataURL, number, type, desc, message, priority, street, area, county, time, date, status, null);
					NotificationManager.showNotificationInfo("Callout Manager", "Callout Recieved #" + number + ", Type: " + type + ". Added To Active Calls.");
					if (calloutViewController != null) {
						CalloutManager.loadActiveCallouts(calloutViewController.getActiveCalloutsTable());
						CalloutManager.loadHistoryCallouts(calloutViewController.getHistoryCalloutsTable());
					}
				}
			}
		} catch (IOException e) {
			logError("Could not get enableCallout config option, ", e);
		}
	}
	
	private static void runTrafficStopUpdate() {
		try {
			if (ConfigReader.configRead("uiSettings", "enableTrafficStopPopup").equalsIgnoreCase("true")) {
				CustomWindow trafficStopWindow = WindowManager.createCustomWindow(mainDesktopControllerObj.getDesktopContainer(), "Windows/Server/trafficStop-view.fxml", "Traffic Stop Data", true, 1, true, true, mainDesktopControllerObj.getTaskBarApps(),
				                                                                  new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/trafficStop.png"))));
				
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
									logWarn("TrafficStop Window was closed before it could be automatically closed");
								}
							});
						}
						delay.play();
					}
				} catch (IOException e) {
					logError("could not read TrafficStopDuration: ", e);
				}
			} else {
				logWarn("Recieved Traffic Stop Update message but popups are disabled");
			}
		} catch (IOException e) {
			logError("Could not get enableTrafficStopPopup from config, ", e);
		}
	}
	
	public interface ServerStatusListener {
		
		void onStatusChanged(boolean isConnected);
	}
}
