package com.drozal.dataterminal;

import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;

public class MedicalInformation {

    @javafx.fxml.FXML
    private RadioButton ambulanceNo;
    @javafx.fxml.FXML
    private RadioButton taserNo;
    @javafx.fxml.FXML
    private TextArea arresteeMedicalInformation;

    public RadioButton getAmbulanceNo() {
        return ambulanceNo;
    }

    public RadioButton getTaserNo() {
        return taserNo;
    }

    public TextArea getArresteeMedicalInformation() {
        return arresteeMedicalInformation;
    }

    public void initialize() {

    }
}
