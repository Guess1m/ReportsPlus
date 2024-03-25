package com.drozal.dataterminal;

import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class DUIInformationController {
    @javafx.fxml.FXML
    private RadioButton BreathalyzerUsedNo;
    @javafx.fxml.FXML
    private ToggleGroup ambulanceRequired;
    @javafx.fxml.FXML
    private TextField BacMeasure;
    @javafx.fxml.FXML
    private RadioButton BreathalyzerResultFail;

    public RadioButton getBreathalyzerUsedNo() {
        return BreathalyzerUsedNo;
    }

    public ToggleGroup getAmbulanceRequired() {
        return ambulanceRequired;
    }

    public TextField getBacMeasure() {
        return BacMeasure;
    }

    public RadioButton getBreathalyzerResultFail() {
        return BreathalyzerResultFail;
    }
}
