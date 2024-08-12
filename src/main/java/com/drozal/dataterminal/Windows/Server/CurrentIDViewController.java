package com.drozal.dataterminal.Windows.Server;

import com.drozal.dataterminal.Launcher;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Report.reportUtil;
import com.drozal.dataterminal.util.server.Objects.ID.ID;
import com.drozal.dataterminal.util.server.Objects.ID.IDs;
import jakarta.xml.bind.JAXBException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Random;

import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.stringUtil.getJarPath;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class CurrentIDViewController {
	
	@javafx.fxml.FXML
	private BorderPane root;
	@javafx.fxml.FXML
	private TabPane tabPane;
	@javafx.fxml.FXML
	private Label noIDFoundlbl;
	
	public static String generateRandomNumber() {
		Random random = new Random();
		int randomNumber = random.nextInt(9000000) + 1000000;
		return String.valueOf(randomNumber);
	}
	
	public void initialize() throws IOException {
		AnchorPane titleBar = reportUtil.createSimpleTitleBar("Current ID", false);
		root.setTop(titleBar);
		
		Platform.runLater(() -> {
			Stage stage = (Stage) root.getScene().getWindow();
			try {
				IDs idList = ID.loadIDs();
				if (idList != null) {
					if (idList.getIdList() != null) {
						tabPane.getTabs().clear();
						noIDFoundlbl.setVisible(false);
						for (ID mostRecentID : idList.getIdList()) {
							if (mostRecentID != null) {
								String firstName = mostRecentID.getFirstName();
								String lastName = mostRecentID.getLastName();
								String birthday = mostRecentID.getBirthday();
								String Gender = mostRecentID.getGender();
								String Address = mostRecentID.getAddress();
								String genNum1 = generateRandomNumber();
								String genNum2 = generateRandomNumber();
								String fullName = firstName + " " + lastName;
								
								FXMLLoader loader = new FXMLLoader(
										Launcher.class.getResource("Windows/Server/IDTemplate.fxml"));
								Parent vBoxParent = loader.load();
								VBox vBox = (VBox) vBoxParent;
								
								Tab newTab = new Tab(lastName);
								newTab.setOnClosed(event -> {
									log("ID Tab For: " + fullName + " Has Been Closed", LogUtils.Severity.DEBUG);
									try {
										ID.deleteID(fullName);
									} catch (JAXBException e) {
										logError("Could not delete ID for: " + fullName, e);
									}
									if (tabPane.getTabs().isEmpty()) {
										log("Tabpane has no more tabs, displaying noIDlbl", LogUtils.Severity.WARN);
										noIDFoundlbl.setVisible(true);
									}
								});
								newTab.setContent(vBox);
								tabPane.getTabs().add(newTab);
								VBox main = (VBox) vBox.lookup("#main");
								updateVBoxValues(main, firstName, genNum1, genNum2, firstName, lastName, birthday,
								                 Gender, Address);
							}
						}
					} else {
						log("getIDList null", LogUtils.Severity.WARN);
						noIDFoundlbl.setVisible(true);
					}
				} else {
					log("ID list null", LogUtils.Severity.WARN);
					noIDFoundlbl.setVisible(true);
				}
			} catch (JAXBException | IOException e) {
				throw new RuntimeException(e);
			}
			
		});
		watchIDChanges();
	}
	
	public void watchIDChanges() {
		Path dir = Paths.get(getJarPath() + File.separator + "serverData");
		
		Thread watchThread = new Thread(() -> {
			try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
				dir.register(watcher, ENTRY_MODIFY);
				
				while (true) {
					WatchKey key;
					try {
						key = watcher.take();
					} catch (InterruptedException x) {
						Thread.currentThread().interrupt();
						return;
					}
					
					for (WatchEvent<?> event : key.pollEvents()) {
						WatchEvent.Kind<?> kind = event.kind();
						
						if (kind == OVERFLOW) {
							continue;
						}
						
						WatchEvent<Path> ev = (WatchEvent<Path>) event;
						Path fileName = ev.context();
						
						if (fileName.toString().equals("ServerCurrentID.xml")) {
							log("ID is being updated", LogUtils.Severity.INFO);
							
							Platform.runLater(() -> {
								Stage stage = (Stage) root.getScene().getWindow();
								try {
									IDs idList = ID.loadIDs();
									if (idList != null) {
										if (idList.getIdList() != null) {
											tabPane.getTabs().clear();
											noIDFoundlbl.setVisible(false);
											for (ID mostRecentID : idList.getIdList()) {
												if (mostRecentID != null) {
													String firstName = mostRecentID.getFirstName();
													String lastName = mostRecentID.getLastName();
													String birthday = mostRecentID.getBirthday();
													String Gender = mostRecentID.getGender();
													String Address = mostRecentID.getAddress();
													String genNum1 = generateRandomNumber();
													String genNum2 = generateRandomNumber();
													String fullName = firstName + " " + lastName;
													
													FXMLLoader loader = new FXMLLoader(Launcher.class.getResource(
															"Windows/Server/IDTemplate.fxml"));
													Parent vBoxParent = loader.load();
													VBox vBox = (VBox) vBoxParent;
													
													Tab newTab = new Tab(lastName);
													newTab.setOnClosed(event2 -> {
														log("ID Tab For: " + fullName + " Has Been Closed",
														    LogUtils.Severity.DEBUG);
														try {
															ID.deleteID(fullName);
														} catch (JAXBException e) {
															logError("Could not delete ID for: " + fullName, e);
														}
														if (tabPane.getTabs().isEmpty()) {
															log("Tabpane has no more tabs, displaying noIDlbl",
															    LogUtils.Severity.WARN);
															noIDFoundlbl.setVisible(true);
														}
													});
													newTab.setContent(vBox);
													tabPane.getTabs().add(newTab);
													VBox main = (VBox) vBox.lookup("#main");
													updateVBoxValues(main, firstName, genNum1, genNum2, firstName,
													                 lastName, birthday, Gender, Address);
												}
											}
										} else {
											log("getIDList null", LogUtils.Severity.WARN);
											noIDFoundlbl.setVisible(true);
										}
									} else {
										log("ID list null", LogUtils.Severity.WARN);
										noIDFoundlbl.setVisible(true);
									}
								} catch (JAXBException | IOException e) {
									throw new RuntimeException(e);
								}
								
							});
						}
					}
					
					boolean valid = key.reset();
					if (!valid) {
						break;
					}
				}
			} catch (IOException e) {
				logError("I/O Error: ", e);
			}
		});
		
		watchThread.setDaemon(true);
		watchThread.start();
	}
	
	public void updateVBoxValues(VBox root, String cursiveNameText, String genNum1Text, String genNum2Text, String firstText, String lastText, String dobText, String genderText, String addressText) {
		Label cursiveName = (Label) root.lookup("#cursiveName");
		Label genNum1 = (Label) root.lookup("#genNum1");
		Label genNum2 = (Label) root.lookup("#genNum2");
		TextField first = (TextField) root.lookup("#first");
		TextField last = (TextField) root.lookup("#last");
		TextField dob = (TextField) root.lookup("#dob");
		TextField gender = (TextField) root.lookup("#gender");
		TextField address = (TextField) root.lookup("#address");
		
		if (cursiveName != null) {
			cursiveName.setText(cursiveNameText);
			cursiveName.setStyle("-fx-font-family: 'Signerica Fat';");
		}
		if (genNum1 != null) {
			genNum1.setText(genNum1Text);
		}
		if (genNum2 != null) {
			genNum2.setText(genNum2Text);
		}
		if (first != null) {
			first.setText(firstText);
		}
		if (last != null) {
			last.setText(lastText);
		}
		if (dob != null) {
			dob.setText(dobText);
		}
		if (gender != null) {
			gender.setText(genderText);
		}
		if (address != null) {
			address.setText(addressText);
		}
	}
	
}
