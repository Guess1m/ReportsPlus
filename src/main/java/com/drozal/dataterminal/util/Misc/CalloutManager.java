package com.drozal.dataterminal.util.Misc;

import com.drozal.dataterminal.util.server.Objects.Callout.Callout;
import com.drozal.dataterminal.util.server.Objects.Callout.Callouts;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.drozal.dataterminal.util.Misc.stringUtil.calloutDataURL;

public class CalloutManager {
	
	public static void addCallout(String xmlFile, String number, String type, String description, String message, String priority, String street, String area, String county, String startTime, String startDate, String status) {
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
			newCallout.setStatus(status);
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
			File file = new File(xmlFile);
			
			// Check if the file exists and is not empty
			if (file.exists() && file.length() != 0) {
				try {
					JAXBContext jaxbContext = JAXBContext.newInstance(Callouts.class);
					Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
					callouts = (Callouts) unmarshaller.unmarshal(file);
				} catch (JAXBException e) {
					// Handle file format issues here
					System.out.println("The file is not properly formatted: " + e.getMessage());
					return;
				}
			} else {
				System.out.println("The file is empty or does not exist.");
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
	
	public static void loadCalloutsIntoListView(ListView<Node> listView) {
		try {
			listView.getItems()
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
				List<Callout> calloutList = callouts.getCalloutList();
				for (Callout callout : calloutList) {
					Node calloutNode = createCalloutNode(callout.getNumber(), callout.getStatus(), callout.getType(), callout.getStreet(),
					                                     callout.getPriority(), callout.getArea());
					listView.getItems()
					        .add(calloutNode);
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	private static Node createCalloutNode(String number, String status, String type, String street, String priority, String area) {
		GridPane gridPane = new GridPane();
		gridPane.setHgap(6);
		gridPane.setVgap(6);
		
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setHgrow(Priority.NEVER);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setHgrow(Priority.SOMETIMES);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setHgrow(Priority.NEVER);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setHgrow(Priority.NEVER);
		
		gridPane.getColumnConstraints().addAll(col1, col2, col3, col4);
		
		Label numberLabel = createLabel("Number:", true);
		gridPane.add(numberLabel, 0, 0);
		gridPane.add(new Label(number), 1, 0);
		
		Label statusLabel = createLabel("Status:", true);
		Label statusVal = new Label(status);
		if (status.equals("Not Responded")) statusVal.setStyle("-fx-text-fill: red;");
		gridPane.add(statusLabel, 2, 0);
		gridPane.add(statusVal, 3, 0);
		
		Label typeLabel = createLabel("Type:", true);
		gridPane.add(typeLabel, 0, 1);
		gridPane.add(new Label(type), 1, 1, 3, 1);
		
		Label streetLabel = createLabel("Street:", true);
		gridPane.add(streetLabel, 0, 2);
		gridPane.add(new Label(street), 1, 2, 3, 1);
		
		Label priorityLabel = createLabel("Priority:", true);
		gridPane.add(priorityLabel, 0, 3);
		gridPane.add(new Label(priority), 1, 3, 3, 1);
		
		Label areaLabel = createLabel("Area:", true);
		gridPane.add(areaLabel, 0, 4);
		gridPane.add(new Label(area), 1, 4, 3, 1);
		
		return gridPane;
	}
	
	private static Label createLabel(String text, boolean bold) {
		Label label = new Label(text);
		label.setStyle("-fx-font-weight: " + (bold ? "bold" : "normal"));
		return label;
	}
}