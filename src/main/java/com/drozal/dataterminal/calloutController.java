package com.drozal.dataterminal;

import com.drozal.dataterminal.util.Misc.CalloutManager;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Report.reportUtil;
import com.drozal.dataterminal.util.server.Objects.Callout.Callout;
import com.drozal.dataterminal.util.server.Objects.Callout.Callouts;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static com.drozal.dataterminal.actionController.CalloutStage;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.stringUtil.calloutDataURL;
import static com.drozal.dataterminal.util.Misc.stringUtil.getJarPath;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

@SuppressWarnings({"ConstantValue", "unchecked"})
public class calloutController {
	
	static AnchorPane topBar;
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
	
	public static Callout getCallout() {
		String filePath = getJarPath() + File.separator + "serverData" + File.separator + "serverCallout.xml";
		File file = new File(filePath);
		
		if (!file.exists()) {
			log("File does not exist: " + filePath, LogUtils.Severity.WARN);
			return null;
		}
		
		if (file.length() == 0) {
			log("File is empty: " + filePath, LogUtils.Severity.ERROR);
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
		}
	}
	
	public void initialize() {
		topBar = reportUtil.createTitleBar("Callout Manager");
		statusLabel.setVisible(false);
		respondBtn.setVisible(true);
		ignoreBtn.setVisible(true);
		
		root.setTop(topBar);
		
		Platform.runLater(() -> {
			Stage stage = (Stage) root.getScene().getWindow();
			
			Callout callout = getCallout();
			
			assert callout != null;
			String street = callout.getStreet() != null ? callout.getStreet() : "Not Available";
			String type = callout.getType() != null ? callout.getType() : "Not Available";
			String number = callout.getNumber() != null ? callout.getNumber() : "Not Available";
			String area = callout.getArea() != null ? callout.getArea() : "Not Available";
			String priority = callout.getPriority() != null ? callout.getPriority() : "Not Available";
			String time = callout.getStartTime() != null ? callout.getStartTime() : "Not Available";
			String date = callout.getStartDate() != null ? callout.getStartDate() : "Not Available";
			String county = callout.getCounty() != null ? callout.getCounty() : "Not Available";
			String desc = callout.getDescription() != null ? callout.getDescription() : "Not Available";
			String message = callout.getMessage() != null ? callout.getMessage() : "Not Available";
			status = callout.getStatus() != null ? callout.getStatus() : "Not Responded";
			
			respondBtn.setOnAction(actionEvent -> {
				status = "Responded";
				statusLabel.setText("Responded.");
				statusLabel.setVisible(true);
				respondBtn.setVisible(false);
				ignoreBtn.setVisible(false);
			});
			ignoreBtn.setOnAction(actionEvent -> {
				status = "Not Responded";
				statusLabel.setText("Ignored.");
				statusLabel.setVisible(true);
				respondBtn.setVisible(false);
				ignoreBtn.setVisible(false);
			});
			
			if (callout != null) {
				streetField.setText(street);
				numberField.setText(number);
				areaField.setText(area);
				priorityField.setText(priority);
				timeField.setText(time);
				dateField.setText(date);
				countyField.setText(county);
				typeField.setText(type);
				if (!callout.getDescription().isEmpty()) {
					descriptionField.setText(desc);
				}
				if (!callout.getMessage().isEmpty()) {
					descriptionField.appendText("\n " + message);
				}
				
			} else {
				
				log("No Callouts found.", LogUtils.Severity.WARN);
				streetField.setText(/*No Data*/"");
				numberField.setText(/*No Data*/"");
				areaField.setText(/*No Data*/"");
				priorityField.setText(/*No Data*/"");
				timeField.setText(/*No Data*/"");
				dateField.setText(/*No Data*/"");
				countyField.setText(/*No Data*/"");
				descriptionField.setText(/*No Data*/"");
				typeField.setText(/*No Data*/"");
			}
			
			stage.setOnHidden(windowEvent -> {
				log("Added Callout To Active", LogUtils.Severity.INFO);
				CalloutManager.addCallout(calloutDataURL, number, type, desc, message, priority, street, area, county,
				                          time, date, status);
				
				CalloutStage.close();
				CalloutStage = null;
			});
		});
		
		// Fixed rem root.requestFocus()
		
		watchCalloutChanges();
	}
	
	public void watchCalloutChanges() {
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
						
						if (fileName.toString().equals("ServerCallout.xml")) {
							log("Callout is being updated", LogUtils.Severity.INFO);
							
							Platform.runLater(() -> {
								Callout callout = getCallout();
								if (callout != null) {
									streetField.setText(callout.getStreet());
									numberField.setText(callout.getNumber());
									areaField.setText(callout.getArea());
									priorityField.setText(callout.getPriority());
									timeField.setText(callout.getStartTime());
									dateField.setText(callout.getStartDate());
									countyField.setText(callout.getCounty());
									descriptionField.setText(callout.getDescription());
									descriptionField.appendText("\n" + callout.getMessage());
									typeField.setText(callout.getType());
									
								} else {
									
									log("No Callouts found.", LogUtils.Severity.WARN);
									streetField.setText(/*No Data*/"");
									numberField.setText(/*No Data*/"");
									areaField.setText(/*No Data*/"");
									priorityField.setText(/*No Data*/"");
									timeField.setText(/*No Data*/"");
									dateField.setText(/*No Data*/"");
									countyField.setText(/*No Data*/"");
									descriptionField.setText(/*No Data*/"");
									typeField.setText(/*No Data*/"");
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
}
