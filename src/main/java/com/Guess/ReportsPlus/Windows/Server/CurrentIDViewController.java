package com.Guess.ReportsPlus.Windows.Server;

import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.util.History.IDHistory.addServerIDToHistoryIfNotExists;
import static com.Guess.ReportsPlus.util.History.IDHistory.checkAllHistoryIDsClosed;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logDebug;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logInfo;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logWarn;
import static com.Guess.ReportsPlus.util.Strings.URLStrings.pedImageFolderURL;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.util.History.IDHistory;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class CurrentIDViewController {
	public static final String defaultPedImagePath = "/com/Guess/ReportsPlus/imgs/CityLosSantosLogo.png";
	@javafx.fxml.FXML
	private BorderPane root;
	@javafx.fxml.FXML
	private TabPane tabPane;
	@javafx.fxml.FXML
	private Label noIDFoundlbl;

	public static String generateRandomNumber(int digits) {
		if (digits <= 0) {
			throw new IllegalArgumentException("Number of digits must be positive");
		}
		Random random = new Random();
		int lowerBound = (int) Math.pow(10, digits - 1);
		int upperBound = (int) Math.pow(10, digits) - 1;
		if (digits == 1) {
			lowerBound = 0;
		}
		int randomNumber = random.nextInt((upperBound - lowerBound) + 1) + lowerBound;
		return String.valueOf(randomNumber);
	}

	private static void updateVBoxValues(VBox root, String genNum1Text, String genNum2Text,
			String firstText, String lastText, String dobText, String genderText, String addressText, String pedModel,
			String heightText, String weightText, String expirationText, String licNumText) {
		Label genNum1 = (Label) root.lookup("#genNum1");
		Label genNum2 = (Label) root.lookup("#genNum2");
		TextField first = (TextField) root.lookup("#first");
		TextField last = (TextField) root.lookup("#last");
		TextField dob = (TextField) root.lookup("#dob");
		TextField gender = (TextField) root.lookup("#gender");
		Label address = (Label) root.lookup("#address");
		ImageView pedImageView = (ImageView) root.lookup("#pedImageView");
		TextField height = (TextField) root.lookup("#height");
		TextField weight = (TextField) root.lookup("#weight");
		TextField expiration = (TextField) root.lookup("#expiration");
		TextField licNum = (TextField) root.lookup("#licensenumber");
		if (licNum != null) {
			licNum.setText(licNumText);
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
		if (height != null) {
			height.setText(heightText);
		}
		if (weight != null) {
			weight.setText(weightText);
		}
		if (expiration != null) {
			expiration.setText(expirationText);
		}
		if (pedModel != null) {
			if (!pedModel.isEmpty() && !pedModel.equalsIgnoreCase("Not Found")) {
				File pedImgFolder = new File(pedImageFolderURL);
				if (pedImgFolder.exists()) {
					logDebug("pedImage folder detected..");
					File[] matchingFiles = pedImgFolder
							.listFiles((dir, name) -> name.equalsIgnoreCase(pedModel + ".jpg"));
					if (matchingFiles != null && matchingFiles.length > 0) {
						File matchingFile = matchingFiles[0];
						logInfo("Matching pedImage found: " + matchingFile.getName());
						try {
							String fileURI = matchingFile.toURI().toString();
							pedImageView.setImage(new Image(fileURI));
						} catch (Exception e) {
							Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
							pedImageView.setImage(defImage);
							logError("Could not set ped image: ", e);
						}
					} else {
						logWarn("No matching pedImage found for the model: " + pedModel
								+ " Trying base image for ID..");
						Pattern pattern = Pattern.compile("\\[([^\\]]+)\\]");
						Matcher matcher = pattern.matcher(pedModel);
						String fallbackModel;
						if (matcher.find()) {
							fallbackModel = "[" + matcher.group(1) + "][0][0]";
							logDebug("Extracted base model for ID: " + fallbackModel);
							File[] fallbackFiles = pedImgFolder
									.listFiles((dir, name) -> name.equalsIgnoreCase(fallbackModel + ".jpg"));
							if (fallbackFiles != null && fallbackFiles.length > 0) {
								File fallbackFile = fallbackFiles[0];
								logInfo("Using base model image for ID: " + fallbackFile.getName());
								try {
									String fileURI = fallbackFile.toURI().toString();
									pedImageView.setImage(new Image(fileURI));
								} catch (Exception e) {
									Image defImage = new Image(
											Launcher.class.getResourceAsStream(defaultPedImagePath));
									pedImageView.setImage(defImage);
									logError("Could not set ped image for ID: ", e);
								}
							}
						}
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
					logWarn("ID list or getIdList() is null");
					noIDFoundlbl.setVisible(true);
				} else {
					logInfo("ID list not null");
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
							String genNum1 = historyID.getLicenseNumber();
							String genNum2 = "San Andreas ID";
							String fullName = firstName + " " + lastName;
							String height = historyID.getHeight();
							String weight = historyID.getWeight();
							String expiration = historyID.getExpiration();
							String licenseNumber = historyID.getLicenseNumber();
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
									logWarn("TabPane has no more tabs, displaying noIDlbl");
									noIDFoundlbl.setVisible(true);
								}
							});
							newTab.setContent(vBox);
							tabPane.getTabs().add(newTab);
							VBox main = (VBox) vBox.lookup("#main");
							updateVBoxValues(main, genNum1, genNum2, firstName, lastName, birthday, gender,
									address, pedModel, height, weight, expiration, licenseNumber);
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
