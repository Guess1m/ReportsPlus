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
	private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	private static final boolean canActivateUpdateId = true;
	public static Boolean isConnected = false;
	public static String port;
	public static String inet;
	private static Socket socket = null;
	private static ServerStatusListener statusListener;
	private static boolean canActivateUpdateCallout = true;
	private static boolean canActivateUpdateWorldPed = true;
	private static boolean canActivateUpdateWorldVeh = true;
	
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
					actionController.clientController.getStatusLabel()
					                                 .setText("Testing Connection...");
					actionController.clientController.getStatusLabel()
					                                 .setStyle("-fx-background-color: orange;");
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
							FileUtlis.receiveFileFromServer(inet, Integer.parseInt(port),
							                                getJarPath() + File.separator + "serverData" + File.separator + "serverCurrentID.xml",
							                                4096);
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
									IDStage.setAlwaysOnTop(ConfigReader.configRead("AOTID")
									                                   .equals("true"));
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
							break;
						case "UPDATE_CALLOUT":
							if (canActivateUpdateCallout) {
								canActivateUpdateCallout = false;
								executorService.schedule(() -> canActivateUpdateCallout = true, 1, TimeUnit.SECONDS);
								
								log("Received Callout update message from server.", LogUtils.Severity.DEBUG);
								FileUtlis.receiveFileFromServer(inet, Integer.parseInt(port),
								                                getJarPath() + File.separator + "serverData" + File.separator + "serverCallout.xml",
								                                4096);
								Platform.runLater(() -> {
									if (CalloutStage != null && CalloutStage.isShowing()) {
										CalloutStage.close();
										CalloutStage = null;
										return;
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
										CalloutStage.setAlwaysOnTop(ConfigReader.configRead("AOTCallout")
										                                        .equals("true"));
									} catch (IOException e) {
										logError("Could not fetch AOTCallout: ", e);
									}
									CalloutStage.initStyle(StageStyle.UNDECORATED);
									CalloutStage.show();
									CalloutStage.centerOnScreen();
									
									windowUtils.centerStageOnMainApp(CalloutStage);
									
									try {
										if (!ConfigReader.configRead("calloutDuration")
										                 .equals("infinite")) {
											PauseTransition delay = null;
											try {
												delay = new PauseTransition(Duration.seconds(Double.parseDouble(
														ConfigReader.configRead("calloutDuration"))));
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
								FileUtlis.receiveFileFromServer(inet, Integer.parseInt(port),
								                                getJarPath() + File.separator + "serverData" + File.separator + "serverWorldPeds.data",
								                                4096);
							}
							break;
						case "UPDATE_WORLD_VEH":
							if (canActivateUpdateWorldVeh) {
								canActivateUpdateWorldVeh = false;
								executorService.schedule(() -> canActivateUpdateWorldVeh = true, 1, TimeUnit.SECONDS);
								
								log("Received World Veh update message from server.", LogUtils.Severity.DEBUG);
								FileUtlis.receiveFileFromServer(inet, Integer.parseInt(port),
								                                getJarPath() + File.separator + "serverData" + File.separator + "serverWorldCars.data",
								                                4096);
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
	
	public static void setStatusListener(ServerStatusListener statusListener) {
		ClientUtils.statusListener = statusListener;
	}
	
	public static void notifyStatusChanged(boolean isConnected) {
		if (statusListener != null) {
			Platform.runLater(() -> statusListener.onStatusChanged(isConnected));
		}
	}
	
}