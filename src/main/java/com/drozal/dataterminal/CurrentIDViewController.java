package com.drozal.dataterminal;

import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Report.reportUtil;
import com.drozal.dataterminal.util.server.Objects.ID.ID;
import com.drozal.dataterminal.util.server.Objects.ID.IDs;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
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
	private Label cursiveName;
	@javafx.fxml.FXML
	private Label genNum2;
	@javafx.fxml.FXML
	private TextField last;
	@javafx.fxml.FXML
	private TextField gender;
	@javafx.fxml.FXML
	private Label genNum1;
	@javafx.fxml.FXML
	private TextField dob;
	@javafx.fxml.FXML
	private TextField first;
	@javafx.fxml.FXML
	private TextField address;
	
	public static String generateRandomNumber() {
		Random random = new Random();
		int randomNumber = random.nextInt(9000000) + 1000000;
		return String.valueOf(randomNumber);
	}
	
	public static ID getMostRecentID() {
		String filePath = getJarPath() + File.separator + "serverData" + File.separator + "serverCurrentID.xml";
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
			JAXBContext jaxbContext = JAXBContext.newInstance(IDs.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			IDs ids = (IDs) jaxbUnmarshaller.unmarshal(file);
			List<ID> idList = ids.getIdList();
			return idList.isEmpty() ? null : idList.get(idList.size() - 1);
		} catch (JAXBException e) {
			logError("Error unmarshalling file: " + filePath + " Trace:", e);
			return null;
		}
	}
	
	public void initialize() {
		AnchorPane titleBar = reportUtil.createSimpleTitleBar("Current ID", false);
		root.setTop(titleBar);
		cursiveName.setStyle("-fx-font-family: 'Signerica Fat';");
		
		Platform.runLater(() -> {
			ID mostRecentID = getMostRecentID();
			if (mostRecentID != null) {
				String firstName = mostRecentID.getFirstName();
				String lastName = mostRecentID.getLastName();
				String birthday = mostRecentID.getBirthday();
				String Gender = mostRecentID.getGender();
				String Address = mostRecentID.getAddress();
				
				genNum1.setText(generateRandomNumber());
				genNum2.setText(generateRandomNumber());
				
				first.setText(firstName);
				cursiveName.setText(firstName);
				last.setText(lastName);
				dob.setText(birthday);
				gender.setText(Gender);
				address.setText(Address);
				
			} else {
				
				log("No IDs found.", LogUtils.Severity.WARN);
				first.setText(/*No Data*/"");
				cursiveName.setText(/*No Data*/"");
				last.setText(/*No Data*/"");
				dob.setText(/*No Data*/"");
				gender.setText(/*No Data*/"");
				address.setText(/*No Data*/"");
				
				genNum1.setText(null);
				genNum2.setText(null);
				
			}
		});
		
		root.requestFocus();
		
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
								ID mostRecentID = getMostRecentID();
								if (mostRecentID != null) {
									String firstName = mostRecentID.getFirstName();
									String lastName = mostRecentID.getLastName();
									String birthday = mostRecentID.getBirthday();
									String Gender = mostRecentID.getGender();
									String Address = mostRecentID.getAddress();
									
									genNum1.setText(generateRandomNumber());
									genNum2.setText(generateRandomNumber());
									
									first.setText(firstName);
									cursiveName.setText(firstName);
									last.setText(lastName);
									dob.setText(birthday);
									gender.setText(Gender);
									address.setText(Address);
									
								} else {
									
									log("No IDs found.", LogUtils.Severity.WARN);
									first.setText(/*No Data*/"");
									last.setText(/*No Data*/"");
									dob.setText(/*No Data*/"");
									gender.setText(/*No Data*/"");
									address.setText(/*No Data*/"");
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
