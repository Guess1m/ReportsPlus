package com.Guess.ReportsPlus.Windows.Apps;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager;
import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.Windows.Other.LayoutBuilderController;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.Guess.ReportsPlus.util.Report.Database.CustomReport;
import com.Guess.ReportsPlus.util.Report.Database.DynamicDB;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

import static com.Guess.ReportsPlus.Desktop.mainDesktopController.newReportWindow;
import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.MainApplication.mainDesktopControllerObj;
import static com.Guess.ReportsPlus.logs.Accident.AccidentReportUtils.newAccident;
import static com.Guess.ReportsPlus.logs.Arrest.ArrestReportUtils.newArrest;
import static com.Guess.ReportsPlus.logs.Callout.CalloutReportUtils.newCallout;
import static com.Guess.ReportsPlus.logs.Death.DeathReportUtils.newDeathReport;
import static com.Guess.ReportsPlus.logs.Impound.ImpoundReportUtils.newImpound;
import static com.Guess.ReportsPlus.logs.Incident.IncidentReportUtils.newIncident;
import static com.Guess.ReportsPlus.logs.Patrol.PatrolReportUtils.newPatrol;
import static com.Guess.ReportsPlus.logs.Search.SearchReportUtils.newSearch;
import static com.Guess.ReportsPlus.logs.TrafficCitation.TrafficCitationUtils.newCitation;
import static com.Guess.ReportsPlus.logs.TrafficStop.TrafficStopReportUtils.newTrafficStop;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.createFolderIfNotExists;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getCustomDataLogsFolderPath;
import static com.Guess.ReportsPlus.util.Report.Database.DynamicDB.getPrimaryKeyColumn;
import static com.Guess.ReportsPlus.util.Report.Database.DynamicDB.isValidDatabase;

public class NewReportVewController {

    public static NewReportVewController newReportVewController;

    @javafx.fxml.FXML
    private BorderPane root;
    @javafx.fxml.FXML
    private AnchorPane mainPane;
    @javafx.fxml.FXML
    private Button patrol;
    @javafx.fxml.FXML
    private Button search;
    @javafx.fxml.FXML
    private Button callout;
    @javafx.fxml.FXML
    private Button trafficstop;
    @javafx.fxml.FXML
    private Button arrest;
    @javafx.fxml.FXML
    private Button death;
    @javafx.fxml.FXML
    private Button impound;
    @javafx.fxml.FXML
    private Button citation;
    @javafx.fxml.FXML
    private Button incident;
    @javafx.fxml.FXML
    private Button accident;
    @javafx.fxml.FXML
    private Label selectReportTypeLabel;
    @javafx.fxml.FXML
    private Label customReportsLabel;
    @javafx.fxml.FXML
    private GridPane customReportsGrid;
    @javafx.fxml.FXML
    private Button createNewReportBtn;

    public void initialize() {
        accident.setOnAction(event -> {
            newAccident();
            closeWindow();
        });
        arrest.setOnAction(event -> {
            newArrest();
            closeWindow();
        });
        callout.setOnAction(event -> {
            newCallout();
            closeWindow();
        });
        death.setOnAction(event -> {
            newDeathReport();
            closeWindow();
        });
        impound.setOnAction(event -> {
            newImpound();
            closeWindow();
        });
        incident.setOnAction(event -> {
            newIncident();
            closeWindow();
        });
        patrol.setOnAction(event -> {
            newPatrol();
            closeWindow();
        });
        search.setOnAction(event -> {
            newSearch();
            closeWindow();
        });
        citation.setOnAction(event -> {
            newCitation();
            closeWindow();
        });
        trafficstop.setOnAction(event -> {
            newTrafficStop();
            closeWindow();
        });

        addLocale();

        try {
            if (loadDatabaseButtons(getCustomDataLogsFolderPath(), customReportsGrid)) {
                log("NewReport; Successfully loaded database buttons", LogUtils.Severity.INFO);
            } else {
                log("NewReport; No Databases Found", LogUtils.Severity.ERROR);
            }
        } catch (IOException e) {
            logError("error loading database buttons", e);
        }
    }

    private boolean loadDatabaseButtons(String dataFolderPath, GridPane customReportsGrid) throws IOException {
        File folder = new File(dataFolderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            log("NewReport; Invalid data folder path: " + dataFolderPath, LogUtils.Severity.ERROR);
            return false;
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".db"));
        if (files == null || files.length == 0) {
            log("NewReport; No database files found in: " + dataFolderPath, LogUtils.Severity.INFO);
            return false;
        }

        log("NewReport; Found " + files.length + " database files in: " + dataFolderPath, LogUtils.Severity.INFO);

        int buttonCount = 0;

        for (File dbFile : files) {
            String dbFilePath = dbFile.getAbsolutePath();
            String data = null;
            try {
                data = getPrimaryKeyColumn(dbFilePath, "data");
            } catch (SQLException e) {
                logError("Error getting primary key column", e);
            }
            log("NewReport; [" + dbFile.getName() + "] Checking database file", LogUtils.Severity.INFO);

            if (isValidDatabase(dbFilePath, dbFile.getName())) {
                log("NewReport; [" + dbFile.getName() + "] Valid database detected", LogUtils.Severity.INFO);
                Button dbButton = new Button(dbFile.getName().replaceAll(".db", ""));
                String finalData = data;
                dbButton.setOnAction(e -> {
                    log("NewReport; Clicked Database: " + dbFile.getName(), LogUtils.Severity.DEBUG);
                    String reportTitle = dbFile.getName().replaceAll(".db", "").trim();

                    createFolderIfNotExists(getCustomDataLogsFolderPath());

                    if (reportTitle.isEmpty()) {
                        log("LayoutBuilder; Report Title Field is Empty", LogUtils.Severity.ERROR);
                        return;
                    }

                    Map<String, String> layoutScheme = null;
                    Map<String, String> reportSchema = null;
                    try {
                        layoutScheme = DynamicDB.getTableColumnsDefinition(dbFilePath, "layout");
                        reportSchema = DynamicDB.getTableColumnsDefinition(dbFilePath, "data");
                    } catch (SQLException e2) {
                        logError("Failed to extract field names", e2);
                    }

                    new CustomReport(reportTitle, finalData, layoutScheme, reportSchema);

                    closeWindow();
                });

                int row = buttonCount / 2;
                int column = buttonCount % 2;

                if (customReportsGrid.getRowConstraints().size() <= row) {
                    RowConstraints rowConstraints = new RowConstraints();
                    rowConstraints.setMinHeight(Region.USE_COMPUTED_SIZE);
                    rowConstraints.setVgrow(Priority.SOMETIMES);
                    customReportsGrid.getRowConstraints().add(rowConstraints);
                }

                customReportsGrid.add(dbButton, column, row);
                GridPane.setHalignment(dbButton, HPos.CENTER);
                GridPane.setValignment(dbButton, VPos.CENTER);

                buttonCount++;
            } else {
                log("NewReport; Invalid database file: " + dbFilePath, LogUtils.Severity.WARN);
            }
        }

        return buttonCount > 0;
    }

    @javafx.fxml.FXML
    public void createReportButton(ActionEvent actionEvent) {
        CustomWindow layoutWindow = WindowManager.createCustomWindow(mainDesktopControllerObj.getDesktopContainer(),
                "Windows/Other/layout-builder-view.fxml",
                "Layout Builder", true, 1, true, false,
                mainDesktopControllerObj.getTaskBarApps(),
                new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/report.png"))));

        LayoutBuilderController.layoutBuilderController = (LayoutBuilderController) (layoutWindow != null ? layoutWindow.controller : null);

        closeWindow();
    }

    private void addLocale() {
        selectReportTypeLabel.setText(localization.getLocalizedMessage("NewReportApp.selectReportTypeLabel", "Select a Report Type:"));
        patrol.setText(localization.getLocalizedMessage("NewReportApp.patrol", "Patrol Report"));
        search.setText(localization.getLocalizedMessage("NewReportApp.search", "Search Report"));
        callout.setText(localization.getLocalizedMessage("NewReportApp.callout", "Callout Report"));
        trafficstop.setText(localization.getLocalizedMessage("NewReportApp.trafficstop", "TrafficStop Report"));
        arrest.setText(localization.getLocalizedMessage("NewReportApp.arrest", "Arrest Report"));
        death.setText(localization.getLocalizedMessage("NewReportApp.death", "Death Report"));
        impound.setText(localization.getLocalizedMessage("NewReportApp.impound", "Impound Report"));
        citation.setText(localization.getLocalizedMessage("NewReportApp.citation", "Citation Report"));
        incident.setText(localization.getLocalizedMessage("NewReportApp.incident", "Incident Report"));
        accident.setText(localization.getLocalizedMessage("NewReportApp.accident", "Accident Report"));
        customReportsLabel.setText(localization.getLocalizedMessage("NewReportApp.customReport", "Custom Reports:"));
        createNewReportBtn.setText(localization.getLocalizedMessage("NewReportApp.createNewReport", "Create New Report (BETA)"));
    }

    private void closeWindow() {
        if (newReportWindow != null) {
            newReportWindow.closeWindow();
        }
    }

    public BorderPane getRoot() {
        return root;
    }

    public Button getAccident() {
        return accident;
    }

    public Button getArrest() {
        return arrest;
    }

    public Button getCallout() {
        return callout;
    }

    public Button getCitation() {
        return citation;
    }

    public Button getDeath() {
        return death;
    }

    public Button getImpound() {
        return impound;
    }

    public Button getIncident() {
        return incident;
    }

    public Button getPatrol() {
        return patrol;
    }

    public Button getSearch() {
        return search;
    }

    public Label getSelectReportTypeLabel() {
        return selectReportTypeLabel;
    }

    public Button getTrafficstop() {
        return trafficstop;
    }
}
