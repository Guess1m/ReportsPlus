package com.Guess.ReportsPlus.Windows.Server;

import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.util.History.IDHistory;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.Guess.ReportsPlus.util.Server.Objects.ID.ID;
import com.Guess.ReportsPlus.util.Server.Objects.ID.IDs;
import jakarta.xml.bind.JAXBException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.util.History.IDHistory.addServerIDToHistoryIfNotExists;
import static com.Guess.ReportsPlus.util.History.IDHistory.checkAllHistoryIDsClosed;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.stringUtil.pedImageFolderURL;

public class CurrentIDViewController {
	
	public static final String defaultPedImagePath = "/com/Guess/ReportsPlus/imgs/CityLosSantosLogo.png";
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
	
	private static void updateVBoxValues(VBox root, String cursiveNameText, String genNum1Text, String genNum2Text, String firstText, String lastText, String dobText, String genderText, String addressText, String pedModel) {
		Label cursiveName = (Label) root.lookup("#cursiveName");
		Label genNum1 = (Label) root.lookup("#genNum1");
		Label genNum2 = (Label) root.lookup("#genNum2");
		TextField first = (TextField) root.lookup("#first");
		TextField last = (TextField) root.lookup("#last");
		TextField dob = (TextField) root.lookup("#dob");
		TextField gender = (TextField) root.lookup("#gender");
		TextField address = (TextField) root.lookup("#address");
		ImageView pedImageView = (ImageView) root.lookup("#pedImgaeView");
		
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
		if (pedModel != null) {
			if (!pedModel.isEmpty() && !pedModel.equalsIgnoreCase("Not Found")) {
				File pedImgFolder = new File(pedImageFolderURL);
				if (pedImgFolder.exists()) {
					log("pedImage folder detected..", LogUtils.Severity.DEBUG);
					
					File[] matchingFiles = pedImgFolder.listFiles(
							(dir, name) -> name.equalsIgnoreCase(pedModel + ".jpg"));
					
					if (matchingFiles != null && matchingFiles.length > 0) {
						File matchingFile = matchingFiles[0];
						log("Matching pedImage found: " + matchingFile.getName(), LogUtils.Severity.INFO);
						
						try {
							String fileURI = matchingFile.toURI().toString();
							pedImageView.setImage(new Image(fileURI));
						} catch (Exception e) {
							Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
							pedImageView.setImage(defImage);
							logError("Could not set ped image: ", e);
						}
					} else {
						log("No matching pedImage found for the model: " + pedModel, LogUtils.Severity.WARN);
						Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
						pedImageView.setImage(defImage);
					}
				} else {
					Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
					pedImageView.setImage(defImage);
				}
			} else {
				Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
				pedImageView.setImage(defImage);
			}
		}
	}
	
	public void initialize() {
		noIDFoundlbl.setVisible(true);
		
		IDUpdate();
		
		noIDFoundlbl.setText(localization.getLocalizedMessage("CurrentIDWindow.NoIDFoundLabel", "No IDs Found."));
		
	}
	
	private void IDUpdate() {
		Platform.runLater(() -> {
			try {
				IDs idList = ID.loadServerIDs();
				if (idList == null || idList.getIdList() == null) {
					log("ID list or getIdList() is null", LogUtils.Severity.WARN);
					noIDFoundlbl.setVisible(true);
				} else {
					log("ID list not null", LogUtils.Severity.INFO);
					tabPane.getTabs().clear();
					noIDFoundlbl.setVisible(false);
					
					for (ID mostRecentID : idList.getIdList()) {
						String status = mostRecentID.getStatus();
						if (status == null) {
							mostRecentID.setStatus("Open");
							try {
								ID.addServerID(mostRecentID);
							} catch (JAXBException e) {
								logError("Could not add ID with null status", e);
							}
						}
						addServerIDToHistoryIfNotExists(mostRecentID);
					}
					
					for (ID historyID : IDHistory.loadHistoryIDs().getIdList()) {
						if (!historyID.getStatus().equalsIgnoreCase("closed")) {
							String firstName = historyID.getFirstName();
							String lastName = historyID.getLastName();
							String birthday = historyID.getBirthday();
							String gender = historyID.getGender();
							String address = historyID.getAddress();
							String pedModel = historyID.getPedModel();
							String genNum1 = generateRandomNumber();
							String genNum2 = generateRandomNumber();
							String fullName = firstName + " " + lastName;
							
							FXMLLoader loader = new FXMLLoader(
									Launcher.class.getResource("Windows/Templates/IDTemplate.fxml"));
							Parent vBoxParent = loader.load();
							VBox vBox = (VBox) vBoxParent;
							
							Tab newTab = new Tab(firstName);
							newTab.setOnClosed(event2 -> {
								try {
									historyID.setStatus("Closed");
									IDHistory.addHistoryID(historyID);
								} catch (JAXBException e) {
									logError("Could update ID status for: " + fullName, e);
								}
								if (tabPane.getTabs().isEmpty()) {
									log("TabPane has no more tabs, displaying noIDlbl", LogUtils.Severity.WARN);
									noIDFoundlbl.setVisible(true);
								}
							});
							newTab.setContent(vBox);
							tabPane.getTabs().add(newTab);
							VBox main = (VBox) vBox.lookup("#main");
							updateVBoxValues(main, firstName, genNum1, genNum2, firstName, lastName, birthday, gender,
							                 address, pedModel);
						}
					}
					if (checkAllHistoryIDsClosed()) {
						noIDFoundlbl.setVisible(true);
					}
				}
			} catch (JAXBException | IOException e) {
				throw new RuntimeException(e);
			}
		});
	}
	
}
