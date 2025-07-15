package com.Guess.ReportsPlus.Windows.Server;

import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.Windows.Apps.CalloutViewController.calloutViewController;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logInfo;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logWarn;
import static com.Guess.ReportsPlus.util.Other.Callout.CalloutManager.loadActiveCallouts;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getServerDataFolderPath;
import static com.Guess.ReportsPlus.util.Server.ClientUtils.calloutWindow;
import static com.Guess.ReportsPlus.util.Strings.URLStrings.calloutDataURL;

import java.io.File;
import java.util.List;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager.IShutdownable;
import com.Guess.ReportsPlus.util.Other.Callout.CalloutManager;
import com.Guess.ReportsPlus.util.Server.Objects.Callout.Callout;
import com.Guess.ReportsPlus.util.Server.Objects.Callout.Callouts;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class CalloutPopupController implements IShutdownable {
	private PauseTransition pause;
	@FXML
	private BorderPane root;
	@FXML
	private TextField streetField;
	@FXML
	private TextField numberField;
	@FXML
	private TextField areaField;
	@FXML
	private TextField priorityField;
	@FXML
	private TextField timeField;
	@FXML
	private TextField dateField;
	@FXML
	private TextField countyField;
	@FXML
	private TextArea descriptionField;
	@FXML
	private TextField typeField;
	@FXML
	private ToggleButton respondBtn;
	@FXML
	private ToggleButton ignoreBtn;
	private String status;
	@FXML
	private Label statusLabel;
	@FXML
	private ToggleGroup respondToggle;
	@FXML
	private Label calloutInfoTitle;
	@FXML
	private Label area;
	@FXML
	private Label pri;
	@FXML
	private Label num;
	@FXML
	private Label cty;
	@FXML
	private Label typ;
	@FXML
	private Label str;
	@FXML
	private Label dsc;
	@FXML
	private Label dat;
	@FXML
	private Label tim;

	public static Callout getCallout() {
		String filePath = getServerDataFolderPath() + "ServerCallout.xml";
		File file = new File(filePath);
		if (!file.exists()) {
			logWarn("File does not exist: " + filePath);
			return null;
		}
		if (file.length() == 0) {
			logError("File is empty: " + filePath);
			return null;
		}
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Callouts.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Callouts callouts = (Callouts) jaxbUnmarshaller.unmarshal(file);
			List<Callout> calloutList = callouts.getCalloutList();
			return calloutList.isEmpty() ? null : calloutList.get(calloutList.size() - 1);
		} catch (JAXBException e) {
			logError("Error unmarshalling file: " + filePath + " Trace:", e);
			return null;
		} catch (Exception e) {
			logError("Unexpected error occurred while reading file: " + filePath + " Trace:", e);
			return null;
		}
	}

	public void initialize() {
		statusLabel.setVisible(false);
		respondBtn.setVisible(true);
		ignoreBtn.setVisible(true);
		Callout callout = getCallout();
		String message;
		String desc;
		if (callout != null) {
			String street = callout.getStreet() != null ? callout.getStreet() : "Not Found";
			String type = callout.getType() != null ? callout.getType() : "Not Found";
			String number = callout.getNumber() != null ? callout.getNumber() : "Not Found";
			String area = callout.getArea() != null ? callout.getArea() : "Not Found";
			String priority = callout.getPriority() != null ? callout.getPriority() : "Not Found";
			String time = callout.getStartTime() != null ? callout.getStartTime() : "Not Found";
			String date = callout.getStartDate() != null ? callout.getStartDate() : "Not Found";
			String county = callout.getCounty() != null ? callout.getCounty() : "Not Found";
			desc = callout.getDescription() != null ? callout.getDescription() : "Not Found";
			message = callout.getMessage() != null ? callout.getMessage() : "Not Found";
			status = callout.getStatus() != null ? callout.getStatus() : "Not Responded";
			respondBtn.setOnAction(_ -> {
				status = "Responded";
				statusLabel.setText("Responded.");
				addResponseCode(message, desc);
			});
			ignoreBtn.setOnAction(_ -> {
				status = "Not Responded";
				statusLabel.setText("Ignored.");
				addResponseCode(message, desc);
			});
			streetField.setText(street);
			numberField.setText(number);
			areaField.setText(area);
			priorityField.setText(priority);
			timeField.setText(time);
			dateField.setText(date);
			countyField.setText(county);
			typeField.setText(type);
			descriptionField.setText(desc.isEmpty() ? message : desc + "\n" + message);
		} else {
			message = "No Data";
			desc = "No Data";
			logError("No Callout found, callout == null");
			streetField.setText("No Data");
			numberField.setText("No Data");
			areaField.setText("No Data");
			priorityField.setText("No Data");
			timeField.setText("No Data");
			dateField.setText("No Data");
			countyField.setText("No Data");
			descriptionField.setText("No Data");
			typeField.setText("No Data");
			logError("Null Callout");
		}
		calloutInfoTitle.setText(localization.getLocalizedMessage("CalloutPopup.MainHeading", "Callout Information"));
		num.setText(localization.getLocalizedMessage("Callout_Manager.CalloutNumber", "Number:"));
		typ.setText(localization.getLocalizedMessage("CalloutPopup.TypeLabel", "Type:"));
		dat.setText(localization.getLocalizedMessage("CalloutPopup.DateLabel", "Date:"));
		tim.setText(localization.getLocalizedMessage("CalloutPopup.TimeLabel", "Time:"));
		pri.setText(localization.getLocalizedMessage("CalloutPopup.PriorityLabel", "Priority:"));
		cty.setText(localization.getLocalizedMessage("CalloutPopup.CountyLabel", "County:"));
		area.setText(localization.getLocalizedMessage("CalloutPopup.AreaLabel", "Area:"));
		str.setText(localization.getLocalizedMessage("CalloutPopup.Streetlabel", "Street:"));
		dsc.setText(localization.getLocalizedMessage("CalloutPopup.DescriptionLabel", "Description:"));
		respondBtn.setText(localization.getLocalizedMessage("CalloutPopup.RespondButton", "Respond"));
		ignoreBtn.setText(localization.getLocalizedMessage("CalloutPopup.IgnoreButton", "Ignore"));
	}

	private void addResponseCode(String message, String desc) {
		statusLabel.setVisible(true);
		respondBtn.setVisible(false);
		ignoreBtn.setVisible(false);
		pause = new PauseTransition(Duration.seconds(2));
		pause.setOnFinished(_ -> {
			logInfo("Added Callout To Active as: " + status);
			CalloutManager.addCallout(calloutDataURL, numberField.getText(), typeField.getText(), desc, message,
					priorityField.getText(), streetField.getText(), areaField.getText(), countyField.getText(),
					timeField.getText(), dateField.getText(), status, null);
			calloutWindow.closeWindow();
			if (calloutViewController != null) {
				loadActiveCallouts(calloutViewController.getActiveCalloutsTable());
			}
		});
		pause.play();
	}

	@Override
	public void shutdown() {
		logInfo("Shutting down CalloutPopupController and all resources...");
		if (pause != null) {
			pause.stop();
		}
	}
}
