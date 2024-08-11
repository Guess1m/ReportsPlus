package com.drozal.dataterminal.Windows.Settings;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Report.reportUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.io.IOException;

import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;

public class ProbabilitySettingsController {
    @javafx.fxml.FXML
    private BorderPane root;
    AnchorPane topBar;
    @javafx.fxml.FXML
    private Label main1;
    @javafx.fxml.FXML
    private TextField permitTypeConcealed;
    @javafx.fxml.FXML
    private TextField onParole;
    @javafx.fxml.FXML
    private TextField caseOutcomeDelay;
    @javafx.fxml.FXML
    private Label permitClassLabel;
    @javafx.fxml.FXML
    private TextField manyCitations;
    @javafx.fxml.FXML
    private TextField fishingLicense;
    @javafx.fxml.FXML
    private TextField boatingLicense;
    @javafx.fxml.FXML
    private TextField permitTypeBoth;
    @javafx.fxml.FXML
    private TextField permitClassHandgun;
    @javafx.fxml.FXML
    private Label chargeProbabilityLabel;
    @javafx.fxml.FXML
    private TextField minimalCitations;
    @javafx.fxml.FXML
    private TextField minimalCharges;
    @javafx.fxml.FXML
    private TextField permitClassShotgun;
    @javafx.fxml.FXML
    private TextField gunLicense;
    @javafx.fxml.FXML
    private TextField onProbation;
    @javafx.fxml.FXML
    private Label permitTypeLabel;
    @javafx.fxml.FXML
    private TextField permitTypeOpenCarry;
    @javafx.fxml.FXML
    private TextField fewCitations;
    @javafx.fxml.FXML
    private TextField permitClassLonggun;
    @javafx.fxml.FXML
    private TextField noCitations;
    @javafx.fxml.FXML
    private TextField huntingLicense;
    @javafx.fxml.FXML
    private TextField manyCharges;
    @javafx.fxml.FXML
    private TextField noCharges;
    @javafx.fxml.FXML
    private TextField fewCharges;
    @javafx.fxml.FXML
    private Label citationProbabilityLabel;

    private void setInitialValues() throws IOException {
        // Gun Permit Type
        permitTypeConcealed.setText(ConfigReader.configRead("pedHistoryGunPermitType", "concealedCarryChance"));
        permitTypeOpenCarry.setText(ConfigReader.configRead("pedHistoryGunPermitType", "openCarryChance"));
        permitTypeBoth.setText(ConfigReader.configRead("pedHistoryGunPermitType", "bothChance"));

        // Gun Permit Class
        permitClassHandgun.setText(ConfigReader.configRead("pedHistoryGunPermitClass", "handgunChance"));
        permitClassLonggun.setText(ConfigReader.configRead("pedHistoryGunPermitClass", "longgunChance"));
        permitClassShotgun.setText(ConfigReader.configRead("pedHistoryGunPermitClass", "shotgunChance"));

        // Arrest
        noCharges.setText(ConfigReader.configRead("pedHistoryArrest", "chanceNoCharges"));
        minimalCharges.setText(ConfigReader.configRead("pedHistoryArrest", "chanceMinimalCharges"));
        fewCharges.setText(ConfigReader.configRead("pedHistoryArrest", "chanceFewCharges"));
        manyCharges.setText(ConfigReader.configRead("pedHistoryArrest", "chanceManyCharges"));

        // Citation
        noCitations.setText(ConfigReader.configRead("pedHistoryCitation", "chanceNoCitations"));
        minimalCitations.setText(ConfigReader.configRead("pedHistoryCitation", "chanceMinimalCitations"));
        fewCitations.setText(ConfigReader.configRead("pedHistoryCitation", "chanceFewCitations"));
        manyCitations.setText(ConfigReader.configRead("pedHistoryCitation", "chanceManyCitations"));

        // Other Licenses
        fishingLicense.setText(ConfigReader.configRead("pedHistory", "hasFishingLicense"));
        boatingLicense.setText(ConfigReader.configRead("pedHistory", "hasBoatingLicense"));
        huntingLicense.setText(ConfigReader.configRead("pedHistory", "hasHuntingLicense"));
        gunLicense.setText(ConfigReader.configRead("pedHistoryGunPermit", "hasGunLicense"));

        // Probation and Parole
        onProbation.setText(ConfigReader.configRead("pedHistory", "onProbationChance"));
        onParole.setText(ConfigReader.configRead("pedHistory", "onParoleChance"));

        // Court Trial Delay
        caseOutcomeDelay.setText(ConfigReader.configRead("pedHistory", "courtTrialDelay"));
    }

    private void addNumericOnlyListener(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void addNumericOnlyAndBeyondMaxListener(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }

            if (!newValue.isEmpty()) {
                try {
                    int value = Integer.parseInt(textField.getText());
                    if (value > 100) {
                        textField.setText("100");
                    }
                } catch (NumberFormatException e) {
                    textField.setText(oldValue);
                }
            }
        });
    }

    public void initialize() {
        topBar = reportUtil.createSimpleTitleBar("Probability Settings", true);

        root.setTop(topBar);

        try {
            setInitialValues();
        } catch (IOException e) {
            logError("Could not set InitialValues for ProbabilitySettings: ", e);
        }

        addNumericOnlyAndBeyondMaxListener(permitTypeConcealed);
        addNumericOnlyAndBeyondMaxListener(onParole);
        addNumericOnlyListener(caseOutcomeDelay);
        addNumericOnlyAndBeyondMaxListener(manyCitations);
        addNumericOnlyAndBeyondMaxListener(fishingLicense);
        addNumericOnlyAndBeyondMaxListener(boatingLicense);
        addNumericOnlyAndBeyondMaxListener(permitTypeBoth);
        addNumericOnlyAndBeyondMaxListener(permitClassHandgun);
        addNumericOnlyAndBeyondMaxListener(minimalCitations);
        addNumericOnlyAndBeyondMaxListener(minimalCharges);
        addNumericOnlyAndBeyondMaxListener(permitClassShotgun);
        addNumericOnlyAndBeyondMaxListener(gunLicense);
        addNumericOnlyAndBeyondMaxListener(onProbation);
        addNumericOnlyAndBeyondMaxListener(permitTypeOpenCarry);
        addNumericOnlyAndBeyondMaxListener(fewCitations);
        addNumericOnlyAndBeyondMaxListener(permitClassLonggun);
        addNumericOnlyAndBeyondMaxListener(noCitations);
        addNumericOnlyAndBeyondMaxListener(huntingLicense);
        addNumericOnlyAndBeyondMaxListener(manyCharges);
        addNumericOnlyAndBeyondMaxListener(noCharges);
        addNumericOnlyAndBeyondMaxListener(fewCharges);
    }

    private boolean checkChargePrior() {
        int chargePriors = Integer.parseInt(noCharges.getText()) + Integer.parseInt(minimalCharges.getText()) + Integer.parseInt(fewCharges.getText()) + Integer.parseInt(manyCharges.getText());
        if (chargePriors != 100) {
            log("Charge Prior Probabilities Do Not Add Up To 100%: " + chargePriors, LogUtils.Severity.ERROR);
            chargeProbabilityLabel.setText("Charge Prior Probabilities Do Not Add Up To 100%: " + chargePriors);
            chargeProbabilityLabel.setStyle("-fx-text-fill: red;");
            chargeProbabilityLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(3.5), evt -> {
                chargeProbabilityLabel.setText("ALL FOUR MUST ADD UP TO 100 COMBINED");
                chargeProbabilityLabel.setStyle("-fx-text-fill: #ffa3a3;");
            }));
            timeline1.play();
            return false;
        } else {
            log("Charge Priors Validated, They = " + chargePriors + "%", LogUtils.Severity.INFO);
            chargeProbabilityLabel.setText("Validated, They = " + chargePriors + "%");
            chargeProbabilityLabel.setStyle("-fx-text-fill: green;");
            chargeProbabilityLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(3.5), evt -> {
                chargeProbabilityLabel.setText("ALL FOUR MUST ADD UP TO 100 COMBINED");
                chargeProbabilityLabel.setStyle("-fx-text-fill: #ffa3a3;");
            }));
            timeline1.play();
            return true;
        }
    }

    private boolean checkCitationPrior() {
        int citationPriors = Integer.parseInt(noCitations.getText()) + Integer.parseInt(minimalCitations.getText()) + Integer.parseInt(fewCitations.getText()) + Integer.parseInt(manyCitations.getText());
        if (citationPriors != 100) {
            log("Citation Prior Probabilities Do Not Add Up To 100%: " + citationPriors, LogUtils.Severity.ERROR);
            citationProbabilityLabel.setText("Citation Prior Probabilities Do Not Add Up To 100%: " + citationPriors);
            citationProbabilityLabel.setStyle("-fx-text-fill: red;");
            citationProbabilityLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(3.5), evt -> {
                citationProbabilityLabel.setText("ALL FOUR MUST ADD UP TO 100 COMBINED");
                citationProbabilityLabel.setStyle("-fx-text-fill: #ffa3a3;");
            }));
            timeline1.play();
            return false;
        } else {
            log("Citation Priors Validated, They = " + citationPriors + "%", LogUtils.Severity.INFO);
            citationProbabilityLabel.setText("Validated, They = " + citationPriors + "%");
            citationProbabilityLabel.setStyle("-fx-text-fill: green;");
            citationProbabilityLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(3.5), evt -> {
                citationProbabilityLabel.setText("ALL FOUR MUST ADD UP TO 100 COMBINED");
                citationProbabilityLabel.setStyle("-fx-text-fill: #ffa3a3;");
            }));
            timeline1.play();
            return true;
        }
    }

    private boolean checkPermitType() {
        int permitTypeTotal = Integer.parseInt(permitTypeConcealed.getText()) + Integer.parseInt(permitTypeOpenCarry.getText()) + Integer.parseInt(permitTypeBoth.getText());
        if (permitTypeTotal != 100) {
            log("Permit Type Probabilities Do Not Add Up To 100%: " + permitTypeTotal, LogUtils.Severity.ERROR);
            permitTypeLabel.setText("Permit Type Probabilities Do Not Add Up To 100%: " + permitTypeTotal);
            permitTypeLabel.setStyle("-fx-text-fill: red;");
            permitTypeLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(3.5), evt -> {
                permitTypeLabel.setText("ALL THREE MUST ADD UP TO 100 COMBINED");
                permitTypeLabel.setStyle("-fx-text-fill: #ffa3a3;");
            }));
            timeline1.play();
            return false;
        } else {
            log("Permit Types Validated, They = " + permitTypeTotal + "%", LogUtils.Severity.INFO);
            permitTypeLabel.setText("Validated, They = " + permitTypeTotal + "%");
            permitTypeLabel.setStyle("-fx-text-fill: green;");
            permitTypeLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(3.5), evt -> {
                permitTypeLabel.setText("ALL THREE MUST ADD UP TO 100 COMBINED");
                permitTypeLabel.setStyle("-fx-text-fill: #ffa3a3;");
            }));
            timeline1.play();
            return true;
        }
    }

    private boolean checkPermitClass() {
        int permitClassTotal = Integer.parseInt(permitClassHandgun.getText()) + Integer.parseInt(permitClassLonggun.getText()) + Integer.parseInt(permitClassShotgun.getText());
        if (permitClassTotal != 100) {
            log("Permit Class Probabilities Do Not Add Up To 100%: " + permitClassTotal, LogUtils.Severity.ERROR);
            permitClassLabel.setText("Permit Class Probabilities Do Not Add Up To 100%: " + permitClassTotal);
            permitClassLabel.setStyle("-fx-text-fill: red;");
            permitClassLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(3.5), evt -> {
                permitClassLabel.setText("ALL THREE MUST ADD UP TO 100 COMBINED (Ped Can Have Multiple)");
                permitClassLabel.setStyle("-fx-text-fill: #ffa3a3;");
            }));
            timeline1.play();
            return false;
        } else {
            log("Permit Classes Validated, They = " + permitClassTotal + "%", LogUtils.Severity.INFO);
            permitClassLabel.setText("Validated, They = " + permitClassTotal + "%");
            permitClassLabel.setStyle("-fx-text-fill: green;");
            permitClassLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(3.5), evt -> {
                permitClassLabel.setText("ALL THREE MUST ADD UP TO 100 COMBINED (Ped Can Have Multiple)");
                permitClassLabel.setStyle("-fx-text-fill: #ffa3a3;");
            }));
            timeline1.play();
            return true;
        }
    }

    private boolean runAllChecks() {
        boolean allTrue = true;

        allTrue &= checkChargePrior();
        allTrue &= checkPermitType();
        allTrue &= checkCitationPrior();
        allTrue &= checkPermitClass();

        return allTrue;
    }

    @javafx.fxml.FXML
    public void saveBtn(ActionEvent actionEvent) {
        if (runAllChecks()) {
            System.out.println("VALID");
        } else {
            System.out.println("NOT VALID");
        }
    }
}
