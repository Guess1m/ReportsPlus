package com.drozal.dataterminal.util.Misc;

import com.drozal.dataterminal.DataTerminalHomeApplication;
import com.drozal.dataterminal.actionController;
import com.drozal.dataterminal.logs.Callout.CalloutReportUtils;
import com.drozal.dataterminal.newOfficerController;
import com.drozal.dataterminal.util.Report.reportCreationUtil;
import com.drozal.dataterminal.util.server.Objects.Callout.Callout;
import com.drozal.dataterminal.util.server.Objects.Callout.Callouts;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.stringUtil.*;

public class CalloutManager {
	
	private static actionController controllerVar;
	
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
			callouts.getCalloutList().add(newCallout);
			
			JAXBContext jaxbContext = JAXBContext.newInstance(Callouts.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(callouts, file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteCallout(String xmlFile, String number) {
		//noinspection UnusedAssignment
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
				log("The file is empty or does not exist.", LogUtils.Severity.ERROR);
				return;
			}
			
			List<Callout> calloutList = callouts.getCalloutList();
			
			if (calloutList == null) {
				return;
			}
			
			calloutList = calloutList.stream().filter(callout -> !callout.getNumber().equals(number)).collect(
					Collectors.toList());
			
			callouts.setCalloutList(calloutList);
			
			JAXBContext jaxbContext = JAXBContext.newInstance(Callouts.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(callouts, new File(xmlFile));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public static void handleSelectedNodeActive(ListView<Node> listView, AnchorPane pane, TextField calNum, TextField calArea, TextField calCounty, TextField calDate, TextField caladdress, TextArea calDesc, TextField caltype, TextField calTime, TextField calPriority) {
		Node selectedNode = listView.getSelectionModel().getSelectedItem();
		if (selectedNode != null) {
			pane.setVisible(true);
			pane.setDisable(false);
			if (selectedNode instanceof GridPane gridPane) {
				String number = ((Label) gridPane.getChildren().get(1)).getText();
				
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
					description = description + "\n" + message;
				}
				calTime.setText(time);
				String text = description.trim();
				if (!text.isEmpty() || !text.isBlank()) {
					calDesc.setText(description);
				} else {
					calDesc.setText("No further Information");
				}
			}
		} else {
			pane.setVisible(false);
		}
	}
	
	public static void handleSelectedNodeHistory(ListView<Node> listView, AnchorPane pane, TextField calNum, TextField calArea, TextField calCounty, TextField calDate, TextField caladdress, TextArea calDesc, TextField caltype, TextField calTime, TextField calPriority) {
		Node selectedNode = listView.getSelectionModel().getSelectedItem();
		if (selectedNode != null) {
			pane.setVisible(true);
			pane.setDisable(false);
			if (selectedNode instanceof GridPane gridPane) {
				String number = ((Label) gridPane.getChildren().get(1)).getText();
				
				String type = getValueByNumber(calloutHistoryURL, number, "Type");
				String description = getValueByNumber(calloutHistoryURL, number, "Description");
				String message = getValueByNumber(calloutHistoryURL, number, "Message");
				String priority = getValueByNumber(calloutHistoryURL, number, "Priority");
				String street = getValueByNumber(calloutHistoryURL, number, "Street");
				String area = getValueByNumber(calloutHistoryURL, number, "Area");
				String county = getValueByNumber(calloutHistoryURL, number, "County");
				String status = getValueByNumber(calloutHistoryURL, number, "Status");
				String time = getValueByNumber(calloutHistoryURL, number, "StartTime");
				String date = getValueByNumber(calloutHistoryURL, number, "StartDate");
				
				calArea.setText(area);
				calNum.setText(number);
				calPriority.setText(priority);
				caltype.setText(type);
				calCounty.setText(county);
				calDate.setText(date);
				caladdress.setText(street);
				if (message != null) {
					description = description + "\n" + message;
				}
				calTime.setText(time);
				String text = description.trim();
				if (!text.isEmpty() || !text.isBlank()) {
					calDesc.setText(description);
				} else {
					calDesc.setText("No further Information");
				}
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
				if (callout.getNumber().equals(number)) {
					Method method = Callout.class.getMethod("get" + fieldName);
					return (String) method.invoke(callout);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static boolean setValueByNumber(String xmlFile, String number, String fieldName, String newValue) {
		//noinspection UnusedAssignment
		Callouts callouts = null;
		
		try {
			if (xmlFile.length() != 0) {
				JAXBContext jaxbContext = JAXBContext.newInstance(Callouts.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				callouts = (Callouts) unmarshaller.unmarshal(new File(xmlFile));
			} else {
				return false;
			}
			
			List<Callout> calloutList = callouts.getCalloutList();
			if (calloutList == null) {
				return false;
			}
			
			for (Callout callout : calloutList) {
				if (callout.getNumber().equals(number)) {
					Method method = Callout.class.getMethod("set" + fieldName, String.class);
					method.invoke(callout, newValue);
					
					JAXBContext jaxbContext = JAXBContext.newInstance(Callouts.class);
					Marshaller marshaller = jaxbContext.createMarshaller();
					marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
					marshaller.marshal(callouts, new File(xmlFile));
					
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static void loadActiveCallouts(ListView<Node> listView) {
		try {
			listView.getItems().clear();
			
			File xmlFile = new File(calloutDataURL);
			if (!xmlFile.exists() || xmlFile.length() == 0) {
				return;
			}
			
			JAXBContext jaxbContext = JAXBContext.newInstance(Callouts.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			Callouts callouts = (Callouts) unmarshaller.unmarshal(xmlFile);
			
			if (callouts != null && callouts.getCalloutList() != null) {
				List<Callout> calloutList = callouts.getCalloutList();
				for (Callout callout : calloutList) {
					String statusProp = "Not Available";
					if (callout.getStatus() != null) {
						statusProp = callout.getStatus();
					}
					
					Node calloutNode = createActiveCalloutNode(callout.getNumber(), statusProp, callout.getType(),
					                                           callout.getStreet(), callout.getPriority(),
					                                           callout.getArea());
					listView.getItems().add(calloutNode);
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadHistoryCallouts(ListView<Node> listView) {
		try {
			listView.getItems().clear();
			
			File xmlFile = new File(calloutHistoryURL);
			if (!xmlFile.exists() || xmlFile.length() == 0) {
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
					listView.getItems().add(calloutNode);
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
		
		gridPane.getColumnConstraints().addAll(col1, col2, col3, col4);
		
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
		
		ComboBox statusDropdown = new ComboBox();
		statusDropdown.setValue(status);
		String[] statuses = {"Responded", "Not Responded"};
		statusDropdown.getItems().addAll(statuses);
		statusDropdown.setOnAction(actionEvent -> {
			String selected = statusDropdown.getSelectionModel().getSelectedItem().toString();
			if (selected.equals("Responded")) {
				setValueByNumber(calloutDataURL, number, "Status", "Responded");
				statusVal.setText("Responded");
				statusVal.setStyle("-fx-text-fill: green;");
			} else if (selected.equals("Not Responded")) {
				setValueByNumber(calloutDataURL, number, "Status", "Not Responded");
				statusVal.setText("Not Responded");
				statusVal.setStyle("-fx-text-fill: red;");
			}
		});
		BorderPane statusPane = new BorderPane(statusDropdown);
		statusDropdown.getStylesheets().add(Objects.requireNonNull(
				actionController.class.getResource("css/form/formComboBox.css")).toExternalForm());
		statusDropdown.getStyleClass().add("combo-boxCal");
		statusPane.setStyle("-fx-background-color: transparent;");
		gridPane.add(statusPane, 2, 1, 2, 2);
		
		Button closeBtn = new Button("Close Callout");
		String def = "-fx-background-color: " + hexToRgba(getSecondaryColor(),
		                                                  0.5) + "; -fx-border-color: rgb(100,100,100,0.1); -fx-text-fill: white; -fx-font-family: \"Segoe UI SemiBold\"; -fx-padding: 3 10 3 10;";
		closeBtn.setStyle(def);
		GridPane.setHalignment(closeBtn, HPos.RIGHT);
		GridPane.setValignment(closeBtn, VPos.CENTER);
		
		String number1 = CalloutManager.getValueByNumber(calloutDataURL, number, "Number");
		String type1 = CalloutManager.getValueByNumber(calloutDataURL, number, "Type");
		String desc1 = CalloutManager.getValueByNumber(calloutDataURL, number, "Description");
		String message1 = CalloutManager.getValueByNumber(calloutDataURL, number, "Message");
		String priority1 = CalloutManager.getValueByNumber(calloutDataURL, number, "Priority");
		String street1 = CalloutManager.getValueByNumber(calloutDataURL, number, "Street");
		String area1 = CalloutManager.getValueByNumber(calloutDataURL, number, "Area");
		String county1 = CalloutManager.getValueByNumber(calloutDataURL, number, "County");
		String startdate1 = CalloutManager.getValueByNumber(calloutDataURL, number, "StartDate");
		String starttime1 = CalloutManager.getValueByNumber(calloutDataURL, number, "StartTime");
		
		closeBtn.setOnAction(actionEvent -> {
			addCallout(calloutHistoryURL, number1, type1, desc1, message1, priority1, street1, area1, county1,
			           starttime1, startdate1, statusVal.getText());
			if (DataTerminalHomeApplication.controller != null) {
				controllerVar = DataTerminalHomeApplication.controller;
			} else if (newOfficerController.controller != null) {
				controllerVar = newOfficerController.controller;
			} else {
				log("Callout Controller Var 1 could not be set", LogUtils.Severity.ERROR);
			}
			deleteCallout(calloutDataURL, number);
			CalloutManager.loadActiveCallouts(controllerVar.getCalActiveList());
			CalloutManager.loadHistoryCallouts(controllerVar.getCalHistoryList());
		});
		
		gridPane.add(closeBtn, 2, 3, 2, 2);
		
		return gridPane;
	}
	
	private static Node createHistoryCalloutNode(String number, String status, String type, String street, String priority, String area) {
		GridPane gridPane = new GridPane();
		gridPane.setHgap(20);
		gridPane.setVgap(15);
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
		ColumnConstraints col7 = new ColumnConstraints();
		col7.setHgrow(Priority.NEVER);
		
		gridPane.getColumnConstraints().addAll(col1, col2, col3, col4, col5, col6, col7);
		
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
		gridPane.add(new Label(street), 1, 1, 6, 1);
		
		Label priorityLabel = createLabel("Priority:", true);
		gridPane.add(priorityLabel, 0, 2);
		gridPane.add(new Label(priority), 1, 2, 6, 1);
		
		Label areaLabel = createLabel("Area:", true);
		gridPane.add(areaLabel, 0, 3);
		gridPane.add(new Label(area), 1, 3, 6, 1);
		
		Button actionButton = new Button("Create Callout Report");
		GridPane.setHalignment(actionButton, HPos.CENTER);
		GridPane.setColumnSpan(actionButton, 1);
		GridPane.setRowSpan(actionButton, 1);
		actionButton.setPadding(new Insets(5, 10, 5, 10));
		
		String def = "-fx-background-color: " + hexToRgba(getSecondaryColor(),
		                                                  0.7) + "; -fx-border-color: rgb(100,100,100,0.1); -fx-text-fill: white; -fx-font-family: \"Segoe UI SemiBold\"; -fx-padding: 3 13 3 13;";
		actionButton.setStyle(def);
		actionButton.setMinWidth(Region.USE_COMPUTED_SIZE);
		
		actionButton.setOnMouseClicked(actionEvent -> {
			if (DataTerminalHomeApplication.controller != null) {
				controllerVar = DataTerminalHomeApplication.controller;
			} else if (newOfficerController.controller != null) {
				controllerVar = newOfficerController.controller;
			} else {
				log("Callout Controller Var 2 could not be set", LogUtils.Severity.ERROR);
			}
			
			Map<String, Object> calloutReportObj = CalloutReportUtils.newCallout(controllerVar.getReportChart(),
			                                                                     controllerVar.getAreaReportChart(),
			                                                                     controllerVar.getNotesViewController());
			
			Map<String, Object> callout = (Map<String, Object>) calloutReportObj.get("Callout Report Map");
			
			TextField calloutnum = (TextField) callout.get("calloutnumber");
			ComboBox calloutarea = (ComboBox) callout.get("area");
			TextArea calloutnotes = (TextArea) callout.get("notes");
			TextField calloutcounty = (TextField) callout.get("county");
			TextField calloutstreet = (TextField) callout.get("street");
			TextField calloutdate = (TextField) callout.get("date");
			TextField callouttime = (TextField) callout.get("time");
			TextField callouttype = (TextField) callout.get("type");
			TextField calloutcode = (TextField) callout.get("code");
			
			String number1 = CalloutManager.getValueByNumber(calloutHistoryURL, number, "Number");
			String type1 = CalloutManager.getValueByNumber(calloutHistoryURL, number, "Type");
			String desc1 = CalloutManager.getValueByNumber(calloutHistoryURL, number, "Description");
			String message1 = CalloutManager.getValueByNumber(calloutHistoryURL, number, "Message");
			String priority1 = CalloutManager.getValueByNumber(calloutHistoryURL, number, "Priority");
			String street1 = CalloutManager.getValueByNumber(calloutHistoryURL, number, "Street");
			String area1 = CalloutManager.getValueByNumber(calloutHistoryURL, number, "Area");
			String county1 = CalloutManager.getValueByNumber(calloutHistoryURL, number, "County");
			String startdate1 = CalloutManager.getValueByNumber(calloutHistoryURL, number, "StartDate");
			String starttime1 = CalloutManager.getValueByNumber(calloutHistoryURL, number, "StartTime");
			calloutnum.setText(number1);
			calloutarea.setValue(area1);
			calloutnotes.setText(desc1);
			if (message1 != null) {
				calloutnotes.appendText("\n" + message1);
			}
			calloutcounty.setText(county1);
			calloutstreet.setText(street1);
			calloutdate.setText(startdate1);
			callouttime.setText(starttime1);
			callouttype.setText(type1);
			calloutcode.setText(priority1);
			
		});
		
		BorderPane actionButtonPane = new BorderPane(actionButton);
		
		gridPane.add(actionButtonPane, 3, 2, 3, 2);
		
		return gridPane;
	}
	
	private static Label createLabel(String text, boolean bold) {
		Label label = new Label(text);
		label.setStyle("-fx-font-weight: " + (bold ? "bold" : "normal"));
		return label;
	}
	
}