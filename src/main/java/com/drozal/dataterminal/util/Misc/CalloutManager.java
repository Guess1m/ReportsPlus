package com.drozal.dataterminal.util.Misc;

import com.drozal.dataterminal.util.server.Objects.Callout.Callout;
import com.drozal.dataterminal.util.server.Objects.Callout.Callouts;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.drozal.dataterminal.util.Misc.stringUtil.*;

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
			newCallout.setStartDate(startDate);
			newCallout.setStartTime(startTime);
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
	
	public static void handleSelectedNode(ListView<Node> listView, AnchorPane pane, TextField calNum, TextField calArea, TextField calCounty, TextField calDate, TextField caladdress, TextArea calDesc, TextField caltype, TextField calTime, TextField calPriority) {
		Node selectedNode = listView.getSelectionModel()
		                            .getSelectedItem();
		if (selectedNode != null) {
			pane.setVisible(true);
			pane.setDisable(false);
			if (selectedNode instanceof GridPane) {
				GridPane gridPane = (GridPane) selectedNode;
				String number = ((Label) gridPane.getChildren()
				                                 .get(1)).getText();
				
				// Retrieve the relevant data from the XML file using the extracted number
				String type = getValueByNumber(calloutDataURL, number, "Type");
				String description = getValueByNumber(calloutDataURL, number, "Description");
				String message = getValueByNumber(calloutDataURL, number, "Message");
				String priority = getValueByNumber(calloutDataURL, number, "Priority");
				String street = getValueByNumber(calloutDataURL, number, "Street");
				String area = getValueByNumber(calloutDataURL, number, "Area");
				String county = getValueByNumber(calloutDataURL, number, "County");
				String status = getValueByNumber(calloutDataURL, number, "Status");
				String time = getValueByNumber(calloutDataURL, number, "StartTime");
				String date = getValueByNumber(calloutDataURL, number, "StartDate");
				
				calArea.setText(area);
				calNum.setText(number);
				calPriority.setText(priority);
				caltype.setText(type);
				calCounty.setText(county);
				calDate.setText(date);
				caladdress.setText(street);
				calDesc.setText(description);
				if (message != null) {
					calDesc.appendText("\n" + message);
				}
				calTime.setText(time);
			}
		} else {
			pane.setVisible(false);
		}
	}
	
	public static String getValueByNumber(String xmlFile, String number, String fieldName) {
		Callouts callouts = null;
		
		try {
			
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
	
	public static void loadActiveCallouts(ListView<Node> listView) {
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
					Node calloutNode = createActiveCalloutNode(callout.getNumber(), callout.getStatus(),
					                                           callout.getType(), callout.getStreet(),
					                                           callout.getPriority(), callout.getArea());
					listView.getItems()
					        .add(calloutNode);
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadHistoryCallouts(ListView<Node> listView) {
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
					Node calloutNode = createHistoryCalloutNode(callout.getNumber(), callout.getStatus(),
					                                            callout.getType(), callout.getStreet(),
					                                            callout.getPriority(), callout.getArea());
					listView.getItems()
					        .add(calloutNode);
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	private static Node createActiveCalloutNode(String number, String status, String type, String street, String priority, String area) {
		GridPane gridPane = new GridPane();
		gridPane.setHgap(6);
		gridPane.setVgap(6);
		gridPane.setPadding(new Insets(10));
		
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setHgrow(Priority.NEVER);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setHgrow(Priority.SOMETIMES);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setHgrow(Priority.NEVER);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setHgrow(Priority.NEVER);
		
		gridPane.getColumnConstraints()
		        .addAll(col1, col2, col3, col4);
		
		Label numberLabel = createLabel("Number:", true);
		gridPane.add(numberLabel, 0, 0);
		gridPane.add(new Label(number), 1, 0);
		
		Label statusLabel = createLabel("Status:", true);
		Label statusVal = new Label(status);
		if (status.equals("Not Responded")) {
			statusVal.setStyle("-fx-text-fill: red;");
		} else if (status.equals("Responded")) {
			statusVal.setStyle("-fx-text-fill: green;");
		}
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
	
	private static Node createHistoryCalloutNode(String number, String status, String type, String street, String priority, String area) {
		GridPane gridPane = new GridPane();
		gridPane.setHgap(20); // Increased horizontal gap for better spacing
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(10));
		
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setHgrow(Priority.NEVER);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setHgrow(Priority.SOMETIMES);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setHgrow(Priority.NEVER);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setHgrow(Priority.SOMETIMES);
		ColumnConstraints col5 = new ColumnConstraints();
		col5.setHgrow(Priority.NEVER);
		ColumnConstraints col6 = new ColumnConstraints();
		col6.setHgrow(Priority.SOMETIMES);
		
		gridPane.getColumnConstraints()
		        .addAll(col1, col2, col3, col4, col5, col6);
		
		Label numberLabel = createLabel("Number:", true);
		gridPane.add(numberLabel, 0, 0);
		gridPane.add(new Label(number), 1, 0);
		
		Label statusLabel = createLabel("Status:", true);
		Label statusVal = new Label(status);
		if (status.equals("Not Responded")) {
			statusVal.setStyle("-fx-text-fill: red;");
		} else if (status.equals("Responded")) {
			statusVal.setStyle("-fx-text-fill: green;");
		}
		gridPane.add(statusLabel, 2, 0);
		gridPane.add(statusVal, 3, 0);
		
		Label typeLabel = createLabel("Type:", true);
		gridPane.add(typeLabel, 4, 0);
		gridPane.add(new Label(type), 5, 0);
		
		Label streetLabel = createLabel("Street:", true);
		gridPane.add(streetLabel, 0, 1);
		gridPane.add(new Label(street), 1, 1, 5, 1);
		
		Label priorityLabel = createLabel("Priority:", true);
		gridPane.add(priorityLabel, 0, 2);
		gridPane.add(new Label(priority), 1, 2, 5, 1);
		
		Label areaLabel = createLabel("Area:", true);
		gridPane.add(areaLabel, 0, 3);
		gridPane.add(new Label(area), 1, 3, 5, 1);
		
		Button actionButton = new Button("Action");
		GridPane.setHalignment(actionButton, HPos.LEFT);
		GridPane.setColumnSpan(actionButton, 2);
		actionButton.setPadding(new Insets(5, 10, 5, 10));
		actionButton.setText("Create Callout Report");
		
		String def = "-fx-background-color: " + hexToRgba(getSecondaryColor(),
		                                                  0.7) + "; -fx-border-color: rgb(100,100,100,0.1); -fx-text-fill: white; -fx-font-family: \"Segoe UI SemiBold\"";
		actionButton.setStyle(def);
		actionButton.setMinWidth(Region.USE_COMPUTED_SIZE);
		
		actionButton.setOnMouseClicked(actionEvent -> {
			System.out.println(number);
		});
		
		gridPane.add(actionButton, 4, 3);
		return gridPane;
	}
	
	private static Label createLabel(String text, boolean bold) {
		Label label = new Label(text);
		label.setStyle("-fx-font-weight: " + (bold ? "bold" : "normal"));
		return label;
	}
	
	public static void extractDataFromSelectedNode(Node selectedNode) {
		if (selectedNode instanceof GridPane) {
			GridPane gridPane = (GridPane) selectedNode;
			String number = ((Label) gridPane.getChildren()
			                                 .get(1)).getText();
			String status = ((Label) gridPane.getChildren()
			                                 .get(3)).getText();
			String type = ((Label) gridPane.getChildren()
			                               .get(5)).getText();
			String street = ((Label) gridPane.getChildren()
			                                 .get(7)).getText();
			String priority = ((Label) gridPane.getChildren()
			                                   .get(9)).getText();
			String area = ((Label) gridPane.getChildren()
			                               .get(11)).getText();
			
			System.out.println("Selected Callout:");
			System.out.println("Number: " + number);
			System.out.println("Status: " + status);
			System.out.println("Type: " + type);
			System.out.println("Street: " + street);
			System.out.println("Priority: " + priority);
			System.out.println("Area: " + area);
		}
	}
	
}