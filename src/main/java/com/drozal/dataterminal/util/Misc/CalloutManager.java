package com.drozal.dataterminal.util.Misc;

import com.drozal.dataterminal.util.server.Objects.Callout.Callout;
import com.drozal.dataterminal.util.server.Objects.Callout.Callouts;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.drozal.dataterminal.util.Misc.stringUtil.calloutDataURL;

public class CalloutManager {
	
	public static void addCallout(String xmlFile, String number, String type, String description, String message, String priority, String street, String area, String county, String startTime, String startDate) {
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
				if (existingCallout.getNumber()
				                   .equals(number)) {
					
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
			newCallout.setStartTime(startTime);
			newCallout.setStartDate(startDate);
			callouts.getCalloutList()
			        .add(newCallout);
			
			
			JAXBContext jaxbContext = JAXBContext.newInstance(Callouts.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(callouts, file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteCallout(String xmlFile, String number) {
		Callouts callouts = null;
		
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
			
			
			calloutList = calloutList.stream()
			                         .filter(callout -> !callout.getNumber()
			                                                    .equals(number))
			                         .collect(Collectors.toList());
			
			
			callouts.setCalloutList(calloutList);
			
			
			JAXBContext jaxbContext = JAXBContext.newInstance(Callouts.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(callouts, new File(xmlFile));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public static String getValueByNumber(String xmlFile, String number, String fieldName) {
		Callouts callouts = null;
		
		try {
			
			fieldName = fieldName.substring(0, 1)
			                     .toUpperCase() + fieldName.substring(1)
			                                               .toLowerCase();
			
			
			if (xmlFile.length() != 0) {
				
				JAXBContext jaxbContext = JAXBContext.newInstance(Callouts.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				callouts = (Callouts) unmarshaller.unmarshal(new File(xmlFile));
			} else {
				
				return null;
			}
			
			
			List<Callout> calloutList = callouts.getCalloutList();
			
			
			if (calloutList == null) {
				return null;
			}
			
			
			for (Callout callout : calloutList) {
				if (callout.getNumber()
				           .equals(number)) {
					Method method = Callout.class.getMethod("get" + fieldName);
					return (String) method.invoke(callout);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	public static void loadCalloutsIntoTable(TableView<Callout> tableView, TableColumn<Callout, String> id, TableColumn<Callout, String> status, TableColumn<Callout, String> title, TableColumn<Callout, String> address, TableColumn<Callout, String> priority, TableColumn<Callout, String> area) {
		try {
			tableView.getItems()
			         .clear();
			
			File xmlFile = new File(calloutDataURL);
			if (!xmlFile.exists() || xmlFile.length() == 0) {
				System.err.println("The XML file does not exist or is empty.");
				return;
			}
			
			JAXBContext jaxbContext = JAXBContext.newInstance(Callouts.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			Callouts callouts = (Callouts) unmarshaller.unmarshal(xmlFile);
			
			if (callouts != null && callouts.getCalloutList() != null) {
				ObservableList<Callout> calloutList = FXCollections.observableArrayList(callouts.getCalloutList());
				tableView.setItems(calloutList);
			} else {
				tableView.setItems(FXCollections.observableArrayList());
			}
			
			id.setCellValueFactory(new PropertyValueFactory<>("Number"));
			status.setCellValueFactory(new PropertyValueFactory<>("number"));
			title.setCellValueFactory(new PropertyValueFactory<>("type"));
			address.setCellValueFactory(new PropertyValueFactory<>("street"));
			priority.setCellValueFactory(new PropertyValueFactory<>("Priority"));
			area.setCellValueFactory(new PropertyValueFactory<>("Area"));
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
}