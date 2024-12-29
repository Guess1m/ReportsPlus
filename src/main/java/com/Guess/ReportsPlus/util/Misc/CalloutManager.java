package com.Guess.ReportsPlus.util.Misc;

import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.logs.Callout.CalloutReportUtils;
import com.Guess.ReportsPlus.util.Server.Objects.Callout.Callout;
import com.Guess.ReportsPlus.util.Server.Objects.Callout.Callouts;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.application.Platform;
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

import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.Windows.Apps.CalloutViewController.calloutViewController;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.stringUtil.*;

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
				log("The file is empty or does not exist.", LogUtils.Severity.ERROR);
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
			logError("Error getting callout value by num: ", e);
			
		}
		
		return null;
	}
	
	public static void setValueByNumber(String xmlFile, String number, String fieldName, String newValue) {
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
					String statusProp = "Not Found";
					if (callout.getStatus() != null) {
						statusProp = callout.getStatus();
					}
					
					Node calloutNode = createActiveCalloutNode(callout.getNumber(), statusProp, callout.getType(), callout.getStreet(), callout.getPriority(), callout.getArea());
					listView.getItems().add(calloutNode);
				}
			}
		} catch (JAXBException e) {
			logError("Error loading active callouts: ", e);
			
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
		
		Label numberLabel = createLabel(localization.getLocalizedMessage("Callout_Manager.CalloutNumber", "Number:"));
		gridPane.add(numberLabel, 0, 0);
		gridPane.add(new Label(number), 1, 0);
		
		Label statusLabel = createLabel(localization.getLocalizedMessage("Callout_Manager.CalloutStatus", "Status:"));
		Label statusVal = new Label(status);
		if (status.equals("Not Responded")) {
			statusVal.setStyle("-fx-text-fill: red;");
		} else if (status.equals("Responded")) {
			statusVal.setStyle("-fx-text-fill: green;");
		}
		gridPane.add(statusLabel, 2, 0);
		gridPane.add(statusVal, 3, 0);
		
		Label typeLabel = createLabel(localization.getLocalizedMessage("Callout_Manager.CalloutType", "Type:"));
		gridPane.add(typeLabel, 0, 1);
		gridPane.add(new Label(type), 1, 1, 3, 1);
		
		Label streetLabel = createLabel(localization.getLocalizedMessage("Callout_Manager.CalloutStreet", "Street:"));
		gridPane.add(streetLabel, 0, 2);
		gridPane.add(new Label(street), 1, 2, 3, 1);
		
		Label priorityLabel = createLabel(localization.getLocalizedMessage("Callout_Manager.CalloutPriority", "Priority:"));
		gridPane.add(priorityLabel, 0, 3);
		gridPane.add(new Label(priority), 1, 3, 3, 1);
		
		Label areaLabel = createLabel(localization.getLocalizedMessage("Callout_Manager.CalloutArea", "Area:"));
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
		statusDropdown.getStylesheets().add(Objects.requireNonNull(Launcher.class.getResource("css/callout/calloutManager.css")).toExternalForm());
		statusDropdown.getStyleClass().add("combo-boxCal");
		statusPane.setStyle("-fx-background-color: transparent;");
		gridPane.add(statusPane, 2, 1, 2, 2);
		
		Button closeBtn = new Button(localization.getLocalizedMessage("Callout_Manager.CloseCalloutButton", "Close Callout"));
		String def = "-fx-background-color: " + hexToRgba(getSecondaryColor(), 0.5) + "; -fx-border-color: rgb(100,100,100,0.1); -fx-text-fill: white; -fx-font-family: \"Segoe UI SemiBold\"; -fx-padding: 3 10 3 10;";
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
			addCallout(calloutHistoryURL, number1, type1, desc1, message1, priority1, street1, area1, county1, starttime1, startdate1, statusVal.getText());
			deleteCallout(calloutDataURL, number);
			if (calloutViewController != null) {
				CalloutManager.loadActiveCallouts(calloutViewController.getCalActiveList());
				CalloutManager.loadHistoryCallouts(calloutViewController.getCalHistoryList());
			}
		});
		
		gridPane.add(closeBtn, 2, 3, 2, 2);
		
		return gridPane;
	}
	
	public static void loadHistoryCallouts(ListView<Node> listView) {
		Platform.runLater(() -> {
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
						Node calloutNode = createHistoryCalloutNode(callout.getNumber(), callout.getStatus(), callout.getType(), callout.getStreet(), callout.getPriority(), callout.getArea());
						listView.getItems().add(calloutNode);
					}
				}
			} catch (JAXBException e) {
				logError("Error loading callout history: ", e);
			}
		});
		
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
		
		Label numberLabel = createLabel(localization.getLocalizedMessage("Callout_Manager.CalloutNum", "Number:"));
		gridPane.add(numberLabel, 0, 0);
		gridPane.add(new Label(number), 1, 0);
		
		Label statusLabel = createLabel(localization.getLocalizedMessage("Callout_Manager.CalloutStatus", "Status:"));
		Label statusVal = new Label(status);
		if (status.equals("Not Responded")) {
			statusVal.setStyle("-fx-text-fill: red;");
		} else if (status.equals("Responded")) {
			statusVal.setStyle("-fx-text-fill: green;");
		}
		gridPane.add(statusLabel, 2, 0);
		gridPane.add(statusVal, 3, 0);
		
		Label typeLabel = createLabel(localization.getLocalizedMessage("Callout_Manager.CalloutType", "Type:"));
		gridPane.add(typeLabel, 4, 0);
		gridPane.add(new Label(type), 5, 0);
		
		Label streetLabel = createLabel(localization.getLocalizedMessage("Callout_Manager.CalloutStreet", "Street:"));
		gridPane.add(streetLabel, 0, 1);
		gridPane.add(new Label(street), 1, 1, 6, 1);
		
		Label priorityLabel = createLabel(localization.getLocalizedMessage("Callout_Manager.CalloutPriority", "Priority:"));
		gridPane.add(priorityLabel, 0, 2);
		gridPane.add(new Label(priority), 1, 2, 6, 1);
		
		Label areaLabel = createLabel(localization.getLocalizedMessage("Callout_Manager.CalloutArea", "Area:"));
		gridPane.add(areaLabel, 0, 3);
		gridPane.add(new Label(area), 1, 3, 6, 1);
		
		Button actionButton = new Button(localization.getLocalizedMessage("Callout_Manager.NewCalloutButton", "Create Callout Report"));
		GridPane.setHalignment(actionButton, HPos.CENTER);
		GridPane.setColumnSpan(actionButton, 1);
		GridPane.setRowSpan(actionButton, 1);
		actionButton.setPadding(new Insets(5, 10, 5, 10));
		
		String def = "-fx-background-color: " + hexToRgba(getSecondaryColor(), 0.7) + "; -fx-border-color: rgb(100,100,100,0.1); -fx-text-fill: white; -fx-font-family: \"Segoe UI SemiBold\"; -fx-padding: 3 13 3 13;";
		actionButton.setStyle(def);
		actionButton.setMinWidth(Region.USE_COMPUTED_SIZE);
		
		actionButton.setOnMouseClicked(actionEvent -> {
			
			Map<String, Object> calloutReportObj = CalloutReportUtils.newCallout();
			
			Map<String, Object> callout = (Map<String, Object>) calloutReportObj.get(localization.getLocalizedMessage("ReportWindows.CalloutReportTitle", "Callout Report") + " Map");
			
			TextField calloutnum = (TextField) callout.get(localization.getLocalizedMessage("ReportWindows.CalloutNumberField", "callout num"));
			ComboBox calloutarea = (ComboBox) callout.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
			TextArea calloutnotes = (TextArea) callout.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "notes"));
			TextField calloutcounty = (TextField) callout.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
			ComboBox calloutstreet = (ComboBox) callout.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
			TextField calloutdate = (TextField) callout.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
			TextField callouttime = (TextField) callout.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
			TextField callouttype = (TextField) callout.get(localization.getLocalizedMessage("ReportWindows.FieldType", "type"));
			TextField calloutcode = (TextField) callout.get(localization.getLocalizedMessage("ReportWindows.CalloutCodeField", "code"));
			
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
			calloutstreet.setValue(street1);
			calloutdate.setText(startdate1);
			callouttime.setText(starttime1);
			callouttype.setText(type1);
			calloutcode.setText(priority1);
			
		});
		
		BorderPane actionButtonPane = new BorderPane(actionButton);
		
		gridPane.add(actionButtonPane, 3, 2, 3, 2);
		
		return gridPane;
	}
	
	public static Label createLabel(String text) {
		Label label = new Label(text);
		label.setStyle("-fx-font-weight: " + ("bold"));
		return label;
	}
	
}