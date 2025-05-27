package com.Guess.ReportsPlus.util.Other.Callout;

import com.Guess.ReportsPlus.util.Server.Objects.Callout.Callout;
import com.Guess.ReportsPlus.util.Server.Objects.Callout.Callouts;
import com.Guess.ReportsPlus.util.Strings.URLStrings;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;

public class CalloutManager {
	
	public static void updateCallout(String xmlFile, CalloutDisplayItem calloutDisplayItemToUpdate) {
		Callouts callouts = null;
		File file = new File(xmlFile);
		boolean calloutFoundAndUpdated = false;
		
		try {
			if (file.exists() && file.length() != 0) {
				JAXBContext jaxbContext = JAXBContext.newInstance(Callouts.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				callouts = (Callouts) unmarshaller.unmarshal(file);
			} else {
				logError("File not found or is empty. Cannot update callout: " + xmlFile);
				return;
			}
			
			if (callouts.getCalloutList() == null) {
				logError("Callout list in file is null. Cannot update callout: " + xmlFile);
				return;
			}
			
			for (Callout existingCallout : callouts.getCalloutList()) {
				if (existingCallout.getNumber().equals(calloutDisplayItemToUpdate.getNumber())) {
					existingCallout.setType(calloutDisplayItemToUpdate.getType());
					existingCallout.setDescription(calloutDisplayItemToUpdate.getDescription());
					existingCallout.setMessage(calloutDisplayItemToUpdate.getMessage());
					existingCallout.setPriority(calloutDisplayItemToUpdate.getPriority());
					existingCallout.setStreet(calloutDisplayItemToUpdate.getStreet());
					existingCallout.setArea(calloutDisplayItemToUpdate.getArea());
					existingCallout.setCounty(calloutDisplayItemToUpdate.getCounty());
					existingCallout.setStartDate(calloutDisplayItemToUpdate.getStartDate());
					existingCallout.setStartTime(calloutDisplayItemToUpdate.getStartTime());
					existingCallout.setStatus(calloutDisplayItemToUpdate.getStatus());
					existingCallout.setMessages(calloutDisplayItemToUpdate.getMessages());
					
					calloutFoundAndUpdated = true;
					break;
				}
			}
			
			if (calloutFoundAndUpdated) {
				JAXBContext jaxbContext = JAXBContext.newInstance(Callouts.class);
				Marshaller marshaller = jaxbContext.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				marshaller.marshal(callouts, file);
			} else {
				logError("Callout with number " + calloutDisplayItemToUpdate.getNumber() + " not found for update in " + xmlFile);
			}
		} catch (JAXBException e) {
			logError("Error updating callout in file: " + xmlFile, e);
		}
	}
	
	public static void addCallout(String xmlFile, String number, String type, String description, String message, String priority, String street, String area, String county, String startTime, String startDate, String status, String messages) {
		Callouts callouts = null;
		
		try {
			File file = new File(xmlFile);
			
			if (file.length() != 0) {
				JAXBContext jaxbContext = JAXBContext.newInstance(Callouts.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				callouts = (Callouts) unmarshaller.unmarshal(file);
			} else {
				callouts = new Callouts();
				callouts.setCalloutList(new ArrayList<>());
			}
			
			if (callouts.getCalloutList() == null) {
				callouts.setCalloutList(new ArrayList<>());
			}
			
			for (Callout existingCallout : callouts.getCalloutList()) {
				if (existingCallout.getNumber().equals(number)) {
					return;
				}
			}
			
			Callout newCallout = new Callout();
			newCallout.setNumber(number);
			newCallout.setType(type);
			newCallout.setDescription(description);
			newCallout.setMessage(message);
			newCallout.setPriority(priority);
			newCallout.setStreet(street);
			newCallout.setArea(area);
			newCallout.setCounty(county);
			newCallout.setStartDate(startDate);
			newCallout.setStartTime(startTime);
			newCallout.setStatus(status);
			newCallout.setMessages(messages);
			callouts.getCalloutList().add(newCallout);
			
			JAXBContext jaxbContext = JAXBContext.newInstance(Callouts.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(callouts, file);
		} catch (JAXBException e) {
			logError("Error creating callout: ", e);
		}
	}
	
	public static void deleteCallout(String xmlFile, String number) {
		
		Callouts callouts = null;
		
		try {
			File file = new File(xmlFile);
			
			if (file.exists() && file.length() != 0) {
				try {
					JAXBContext jaxbContext = JAXBContext.newInstance(Callouts.class);
					Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
					callouts = (Callouts) unmarshaller.unmarshal(file);
				} catch (JAXBException e) {
					
					logError("The file is not properly formatted: ", e);
					return;
				}
			} else {
				logError("The file is empty or does not exist.");
				return;
			}
			
			List<Callout> calloutList = callouts.getCalloutList();
			
			if (calloutList == null) {
				return;
			}
			
			calloutList = calloutList.stream().filter(callout -> !callout.getNumber().equals(number)).collect(Collectors.toList());
			
			callouts.setCalloutList(calloutList);
			
			JAXBContext jaxbContext = JAXBContext.newInstance(Callouts.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(callouts, new File(xmlFile));
		} catch (JAXBException e) {
			logError("Error deleting callout: ", e);
			
		}
	}
	
	public static void setValueByNumber(String xmlFile, String number, String fieldName, String newValue) {
		Callouts callouts;
		
		try {
			if (xmlFile.length() != 0) {
				JAXBContext jaxbContext = JAXBContext.newInstance(Callouts.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				callouts = (Callouts) unmarshaller.unmarshal(new File(xmlFile));
			} else {
				return;
			}
			
			List<Callout> calloutList = callouts.getCalloutList();
			if (calloutList == null) {
				return;
			}
			
			for (Callout callout : calloutList) {
				if (callout.getNumber().equals(number)) {
					Method method = Callout.class.getMethod("set" + fieldName, String.class);
					method.invoke(callout, newValue);
					
					JAXBContext jaxbContext = JAXBContext.newInstance(Callouts.class);
					Marshaller marshaller = jaxbContext.createMarshaller();
					marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
					marshaller.marshal(callouts, new File(xmlFile));
					
					return;
				}
			}
		} catch (Exception e) {
			logError("Error getting callout value by num: ", e);
		}
		
	}
	
	public static void loadActiveCallouts(TableView<CalloutDisplayItem> tableView) {
		Platform.runLater(() -> {
			ObservableList<CalloutDisplayItem> displayItems = FXCollections.observableArrayList();
			try {
				File xmlFile = new File(URLStrings.calloutDataURL);
				if (!xmlFile.exists() || xmlFile.length() == 0) {
					tableView.setItems(displayItems);
					tableView.setPlaceholder(new Label("Active callouts file not found or empty."));
					return;
				}
				
				JAXBContext jaxbContext = JAXBContext.newInstance(Callouts.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				Callouts callouts = (Callouts) unmarshaller.unmarshal(xmlFile);
				
				if (callouts != null && callouts.getCalloutList() != null) {
					for (Callout callout : callouts.getCalloutList()) {
						displayItems.add(
								new CalloutDisplayItem(callout.getNumber(), callout.getStatus(), callout.getType(), callout.getStreet(), callout.getPriority(), callout.getArea(), callout.getDescription(), callout.getMessage(), callout.getCounty(), callout.getStartDate(), callout.getStartTime(),
								                       callout.getMessages(), callout));
					}
				}
				tableView.setItems(displayItems);
				if (displayItems.isEmpty()) {
					tableView.setPlaceholder(new Label(localization.getLocalizedMessage("Callout_Manager.NoActiveCallouts", "No active callouts.")));
				}
			} catch (JAXBException e) {
				logError("Error loading active callouts: ", e);
				tableView.setPlaceholder(new Label(localization.getLocalizedMessage("Callout_Manager.ErrorLoadingActive", "Error loading active callouts.")));
			}
		});
	}
	
	public static void loadHistoryCallouts(TableView<CalloutDisplayItem> tableView) {
		Platform.runLater(() -> {
			ObservableList<CalloutDisplayItem> displayItems = FXCollections.observableArrayList();
			try {
				File xmlFile = new File(URLStrings.calloutHistoryURL);
				if (!xmlFile.exists() || xmlFile.length() == 0) {
					tableView.setItems(displayItems);
					tableView.setPlaceholder(new Label(localization.getLocalizedMessage("Callout_Manager.NoCalloutHistory", "No callout history.")));
					return;
				}
				
				JAXBContext jaxbContext = JAXBContext.newInstance(Callouts.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				Callouts callouts = (Callouts) unmarshaller.unmarshal(xmlFile);
				
				if (callouts != null && callouts.getCalloutList() != null) {
					for (Callout callout : callouts.getCalloutList()) {
						displayItems.add(
								new CalloutDisplayItem(callout.getNumber(), callout.getStatus(), callout.getType(), callout.getStreet(), callout.getPriority(), callout.getArea(), callout.getDescription(), callout.getMessage(), callout.getCounty(), callout.getStartDate(), callout.getStartTime(),
								                       callout.getMessages(), callout));
					}
				}
				tableView.setItems(displayItems);
				if (displayItems.isEmpty()) {
					tableView.setPlaceholder(new Label(localization.getLocalizedMessage("Callout_Manager.NoCalloutHistory", "No callout history.")));
				}
			} catch (JAXBException e) {
				logError("Error loading callout history: ", e);
				tableView.setPlaceholder(new Label(localization.getLocalizedMessage("Callout_Manager.ErrorLoadingHistory", "Error loading callout history.")));
			}
		});
	}
	
	public static Label createLabel(String text) {
		Label label = new Label(text);
		label.setStyle("-fx-font-weight: " + ("bold"));
		return label;
	}
	
}