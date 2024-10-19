package com.drozal.dataterminal.Windows.Server;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.drozal.dataterminal.util.Misc.stringUtil.getJarPath;
import static com.drozal.dataterminal.util.server.recordUtils.grabTrafficStop;

public class trafficStopController {
	
	public static trafficStopController trafficStopController;
	@javafx.fxml.FXML
	private TextField insurance;
	@javafx.fxml.FXML
	private TextField area;
	@javafx.fxml.FXML
	private TextField owner;
	@javafx.fxml.FXML
	private TextField stolen;
	@javafx.fxml.FXML
	private TextField police;
	@javafx.fxml.FXML
	private AnchorPane color;
	@javafx.fxml.FXML
	private TextField street;
	@javafx.fxml.FXML
	private BorderPane root;
	@javafx.fxml.FXML
	private TextField model;
	@javafx.fxml.FXML
	private TextField registration;
	@javafx.fxml.FXML
	private TextField platenum;
	@javafx.fxml.FXML
	private Label noColorFoundlbl;
	
	public void initialize() {
		noColorFoundlbl.setVisible(true);
		color.setStyle("-fx-background-color: white; -fx-border-color: black;");
		
		try {
			updateTrafficStopFields();
		} catch (IOException e) {
			System.err.println("Could not update traffic stop fields");
		}
		
	}
	
	public void updateTrafficStopFields() throws IOException {
		String filePath = getJarPath() + File.separator + "serverData" + File.separator + "ServerTrafficStop.data";
		Map<String, String> vehData = grabTrafficStop(filePath);
		
		String licensePlate = vehData.getOrDefault("licensePlate", "Not Found");
		String model = vehData.getOrDefault("model", "Not Found");
		String isStolen = vehData.getOrDefault("isStolen", "Not Found");
		String isPolice = vehData.getOrDefault("isPolice", "Not Found");
		String registration = vehData.getOrDefault("registration", "Not Found");
		String insurance = vehData.getOrDefault("insurance", "Not Found");
		String colorValue = vehData.getOrDefault("color", "Not Found");
		String owner = vehData.getOrDefault("owner", "Not Found");
		String street = vehData.getOrDefault("street", "Not Found");
		String area = vehData.getOrDefault("area", "Not Found");
		
		String[] rgb = colorValue.split("-");
		String color = "Not Found";
		
		if (rgb.length == 3) {
			color = "rgb(" + rgb[0] + "," + rgb[1] + "," + rgb[2] + ")";
			getColor().setStyle("-fx-background-color: " + color + "; -fx-border-color: black;");
			getNoColorFoundlbl().setVisible(false);
		} else {
			getNoColorFoundlbl().setVisible(true);
			getColor().setStyle("-fx-background-color: white; -fx-border-color: black;");
		}
		
		getPlatenum().setText(licensePlate);
		getModel().setText(model);
		getRegistration().setText(registration);
		getInsurance().setText(insurance);
		getPolice().setText(isPolice);
		getStolen().setText(isStolen);
		getOwner().setText(owner);
		getStreet().setText(street);
		getArea().setText(area);
		
	}
	
	public Label getNoColorFoundlbl() {
		return noColorFoundlbl;
	}
	
	public TextField getPlatenum() {
		return platenum;
	}
	
	public TextField getRegistration() {
		return registration;
	}
	
	public TextField getModel() {
		return model;
	}
	
	public BorderPane getRoot() {
		return root;
	}
	
	public TextField getStreet() {
		return street;
	}
	
	public AnchorPane getColor() {
		return color;
	}
	
	public TextField getPolice() {
		return police;
	}
	
	public TextField getStolen() {
		return stolen;
	}
	
	public TextField getOwner() {
		return owner;
	}
	
	public TextField getArea() {
		return area;
	}
	
	public TextField getInsurance() {
		return insurance;
	}
}
