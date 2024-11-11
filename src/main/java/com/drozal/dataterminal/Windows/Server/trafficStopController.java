package com.drozal.dataterminal.Windows.Server;

import com.drozal.dataterminal.Desktop.Utils.WindowUtils.CustomWindow;
import com.drozal.dataterminal.Desktop.Utils.WindowUtils.WindowManager;
import com.drozal.dataterminal.Windows.Apps.VehLookupViewController;
import com.drozal.dataterminal.Windows.Settings.settingsController;
import com.drozal.dataterminal.util.Misc.LogUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.drozal.dataterminal.DataTerminalHomeApplication.mainDesktopControllerObj;
import static com.drozal.dataterminal.Desktop.Utils.WindowUtils.WindowManager.createFakeWindow;
import static com.drozal.dataterminal.Desktop.mainDesktopController.vehLookupAppObj;
import static com.drozal.dataterminal.Windows.Apps.VehLookupViewController.vehLookupViewController;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.stringUtil.getJarPath;
import static com.drozal.dataterminal.util.Server.recordUtils.grabTrafficStop;

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

        setStyleBasedOnCondition(registration, getRegistration());
        setStyleBasedOnCondition(insurance, getInsurance());
        setStyleBasedOnCondition(isStolen, getStolen());

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

    private void setStyleBasedOnCondition(String condition, TextInputControl control) {
        if (condition.equalsIgnoreCase("expired") || condition.equalsIgnoreCase(
                "suspended") || condition.equalsIgnoreCase("none") || condition.equalsIgnoreCase("true")) {
            control.setStyle("-fx-text-fill: red !important;");
        } else {
            control.setStyle("-fx-text-fill: rgb(140, 140, 140) !important;");
        }
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

    @javafx.fxml.FXML
    public void searchDMV(ActionEvent actionEvent) {
        CustomWindow vehWindow = WindowManager.getWindow("Vehicle Lookup");
        if (vehWindow == null) {
            CustomWindow vehApp = createFakeWindow(mainDesktopControllerObj.getDesktopContainer(),
                    "Windows/Apps/lookup-veh-view.fxml", "Vehicle Lookup", true, 1, true,
                    false, mainDesktopControllerObj.getTaskBarApps(),
                    vehLookupAppObj.getImage());
            if (vehApp != null && vehApp.controller != null) {
                vehLookupViewController = (VehLookupViewController) vehApp.controller;
            }
            try {
                settingsController.loadTheme();
            } catch (IOException e) {
                logError("Error loading theme from searchDMV", e);
            }
            vehWindow = vehApp;
        }
        if (vehWindow != null) {
            if (vehWindow.isMinimized) {
                vehWindow.restoreWindow(vehWindow.title);
            }
            if (vehLookupViewController != null) {
                vehLookupViewController.getVehSearchField().getEditor().setText(platenum.getText());
                try {
                    vehLookupViewController.onVehSearchBtnClick(new ActionEvent());
                    vehWindow.bringToFront();
                    log("Bringing up veh search for: " + platenum.getText() + " from searchDMV",
                            LogUtils.Severity.DEBUG);
                } catch (IOException e) {
                    logError("Error searching plate from traffic stop window plate: " + platenum.getText(), e);
                }
            }
        }
    }
}
