package com.drozal.dataterminal;

import com.drozal.dataterminal.util.Misc.stringUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static com.drozal.dataterminal.util.Misc.stringUtil.version;
import static com.drozal.dataterminal.util.Misc.updateUtil.*;
import static com.drozal.dataterminal.util.Report.reportUtil.createSimpleTitleBar;

public class updatesController {

    private static final Duration ANIMATION_DURATION = Duration.seconds(1.2);
    AnchorPane topBar;
    @javafx.fxml.FXML
    private BorderPane root;
    @javafx.fxml.FXML
    private Label currentVer;
    @javafx.fxml.FXML
    private Label recentVer;
    @javafx.fxml.FXML
    private Label verChangelog;
    List<String> updates = new ArrayList<>();
    @javafx.fxml.FXML
    private VBox changelogBox;

    public void initialize() {
        topBar = createSimpleTitleBar("Version Information", true);
        root.setTop(topBar);

        verChangelog.setText(version);

        checkUpdates();

        updates.addAll(List.of(stringUtil.updatesList));

        updates.forEach(string -> {
            Label label = new Label("* " + string);
            label.setStyle("-fx-text-fill:  #5A72A0; -fx-font-family: \"Segoe UI\";");
            changelogBox.getChildren().add(label);
        });

    }

    private void checkUpdates() {
        checkForUpdates();

        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, event -> {
            currentVer.setText("Updating.");
            recentVer.setStyle("-fx-text-fill: #FDFFE2;");
            recentVer.setText("Updating.");
        }), new KeyFrame(Duration.seconds(0.4), event -> {
            currentVer.setText("Updating..");
            recentVer.setStyle("-fx-text-fill: #FDFFE2;");
            recentVer.setText("Updating..");
        }), new KeyFrame(Duration.seconds(0.8), event -> {
            currentVer.setText("Updating...");
            recentVer.setStyle("-fx-text-fill: #FDFFE2;");
            recentVer.setText("Updating...");
        }), new KeyFrame(ANIMATION_DURATION, event -> {
            currentVer.setText(version);
            if (!version.equals(gitVersion)) {
                if (gitVersion == null) {
                    recentVer.setText("New Ver. Available!");
                    recentVer.setStyle("-fx-text-fill: red;");
                    openWebpage("https://github.com/Guess1m/ReportsPlus/releases");
                } else {
                    recentVer.setText(gitVersion);
                    recentVer.setStyle("-fx-text-fill: red;");
                    openWebpage("https://github.com/Guess1m/ReportsPlus/releases");
                }
            } else {
                recentVer.setText(gitVersion);
            }
        }));
        timeline.play();
    }

    @javafx.fxml.FXML
    public void updateBtnAction() {
        checkUpdates();
    }

}
