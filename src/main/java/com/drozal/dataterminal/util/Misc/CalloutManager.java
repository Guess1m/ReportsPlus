package com.drozal.dataterminal.util.Misc;

import com.drozal.dataterminal.util.server.Objects.Callout.Callout;
import com.drozal.dataterminal.util.server.Objects.Callout.Callouts;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
			
			Random random = new Random();
			status.setCellValueFactory(
					cellData -> new SimpleStringProperty(random.nextBoolean() ? "Responded" : "Not Responded"));
			id.setCellValueFactory(new PropertyValueFactory<>("Number"));
			title.setCellValueFactory(new PropertyValueFactory<>("type"));
			address.setCellValueFactory(new PropertyValueFactory<>("street"));
			priority.setCellValueFactory(new PropertyValueFactory<>("Priority"));
			area.setCellValueFactory(new PropertyValueFactory<>("Area"));
			
			// Set cell factory to style cells based on their content
			status.setCellFactory(column -> new TableCell<Callout, String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (empty || item == null) {
						setText(null);
					} else {
						setText(item);
						setTextFill("Responded".equals(item) ? Color.GREEN : Color.BLACK);
					}
				}
			});
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadCalloutsIntoGrid(GridPane grid) {
		try {
			grid.getChildren()
			    .clear();
			
			JAXBContext jaxbContext = JAXBContext.newInstance(Callouts.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			Callouts callouts = (Callouts) unmarshaller.unmarshal(new File(calloutDataURL));
			
			if (callouts != null && callouts.getCalloutList() != null) {
				ObservableList<Callout> calloutList = FXCollections.observableArrayList(callouts.getCalloutList());
				
				// Add column headers
				Label idLabel = new Label("ID");
				idLabel.getStyleClass()
				       .add("first-row-label");
				grid.add(idLabel, 0, 0);
				
				Label areaLabel = new Label("Area");
				areaLabel.getStyleClass()
				         .add("first-row-label");
				grid.add(areaLabel, 1, 0);
				
				Label titleLabel = new Label("Title");
				titleLabel.getStyleClass()
				          .add("first-row-label");
				grid.add(titleLabel, 2, 0);
				
				Label addressLabel = new Label("Address");
				addressLabel.getStyleClass()
				            .add("first-row-label");
				grid.add(addressLabel, 3, 0);
				
				Label priorityLabel = new Label("Priority");
				priorityLabel.getStyleClass()
				             .add("first-row-label");
				grid.add(priorityLabel, 4, 0);
				
				Label statusLabel = new Label("Status");
				statusLabel.getStyleClass()
				           .add("first-row-label");
				grid.add(statusLabel, 5, 0);
				
				// Add callouts to grid
				for (int i = 0; i < calloutList.size(); i++) {
					Callout callout = calloutList.get(i);
					int row = i + 1;
					
					Label idValueLabel = new Label(callout.getNumber());
					Label areaValueLabel = new Label(callout.getArea());
					Label titleValueLabel = new Label(callout.getType());
					Label addressValueLabel = new Label(callout.getStreet());
					Label priorityValueLabel = new Label(callout.getPriority());
					Label statusValueLabel = new Label(Math.random() < 0.7 ? "Responded" : "Not Responded");
					
					idValueLabel.getStyleClass()
					            .add("data-label");
					areaValueLabel.getStyleClass()
					              .add("data-label");
					titleValueLabel.getStyleClass()
					               .add("data-label");
					addressValueLabel.getStyleClass()
					                 .add("data-label");
					priorityValueLabel.getStyleClass()
					                  .add("data-label");
					statusValueLabel.getStyleClass()
					                .add("data-label");
					
					if ("Responded".equals(statusValueLabel.getText())) {
						statusValueLabel.setTextFill(Color.GREEN);
					} else if ("Not Responded".equals(statusValueLabel.getText())) {
						statusValueLabel.setTextFill(Color.RED);
					}
					
					grid.add(idValueLabel, 0, row);
					grid.add(areaValueLabel, 1, row);
					grid.add(titleValueLabel, 2, row);
					grid.add(addressValueLabel, 3, row);
					grid.add(priorityValueLabel, 4, row);
					grid.add(statusValueLabel, 5, row);
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}