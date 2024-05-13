package com.drozal.dataterminal;

import com.drozal.dataterminal.util.LogUtils;
import com.drozal.dataterminal.util.reportCreationUtil;
import com.drozal.dataterminal.util.server.Callout;
import com.drozal.dataterminal.util.server.Callouts;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static com.drozal.dataterminal.util.LogUtils.log;
import static com.drozal.dataterminal.util.LogUtils.logError;
import static com.drozal.dataterminal.util.stringUtil.getJarPath;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class calloutController {

    static AnchorPane topBar;
    @javafx.fxml.FXML
    private Label calloutInfoTitle;
    @javafx.fxml.FXML
    private BorderPane root;
    @javafx.fxml.FXML
    private TextField streetField;
    @javafx.fxml.FXML
    private TextField numberField;
    @javafx.fxml.FXML
    private TextField areaField;
    @javafx.fxml.FXML
    private TextField priorityField;
    @javafx.fxml.FXML
    private TextField timeField;
    @javafx.fxml.FXML
    private TextField dateField;
    @javafx.fxml.FXML
    private TextField countyField;
    @javafx.fxml.FXML
    private TextArea descriptionField;
    @javafx.fxml.FXML
    private TextField typeField;

    public static AnchorPane getTopBar() {
        return topBar;
    }

    public static Callout getCallout() {
        String filePath = getJarPath() + File.separator + "serverData" + File.separator + "serverCallout.xml";
        File file = new File(filePath);

        if (!file.exists()) {
            System.err.println("File does not exist: " + filePath);
            return null;
        }

        if (file.length() == 0) {
            System.err.println("File is empty: " + filePath);
            return null;
        }

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Callouts.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Callouts callouts = (Callouts) jaxbUnmarshaller.unmarshal(file);
            List<Callout> calloutList = callouts.getCalloutList();
            return calloutList.isEmpty() ? null : calloutList.get(calloutList.size() - 1);
        } catch (JAXBException e) {
            System.err.println("Error unmarshalling file: " + filePath);
            e.printStackTrace();
            return null;
        }
    }

    public void initialize() {
        topBar = reportCreationUtil.createSimpleTitleBar("Callout Manager");

        root.setTop(topBar);

        Platform.runLater(() -> {
            Callout callout = getCallout();
            if (callout != null) {
                streetField.setText(callout.getStreet());
                numberField.setText(callout.getNumber());
                areaField.setText(callout.getArea());
                priorityField.setText(callout.getPriority());
                timeField.setText(callout.getStartTime());
                dateField.setText(callout.getStartDate());
                countyField.setText(callout.getCounty());
                descriptionField.setText(callout.getDescription());
                typeField.setText(callout.getType());

            } else {
                // Show alert or set default values
                log("No Callouts found.", LogUtils.Severity.WARN);
                streetField.setText("No data");
                numberField.setText("No data");
                areaField.setText("No data");
                priorityField.setText("No data");
                timeField.setText("No data");
                dateField.setText("No data");
                countyField.setText("No data");
                descriptionField.setText("No data");
                typeField.setText("No data");
            }
        });

        root.requestFocus();

        watchCalloutChanges();
    }

    public void watchCalloutChanges() {
        Path dir = Paths.get(getJarPath() + File.separator + "serverData");

        Thread watchThread = new Thread(() -> {
            try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
                dir.register(watcher, ENTRY_MODIFY);

                while (true) {
                    WatchKey key;
                    try {
                        key = watcher.take();
                    } catch (InterruptedException x) {
                        Thread.currentThread().interrupt();
                        return;
                    }

                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();

                        if (kind == OVERFLOW) {
                            continue;
                        }

                        WatchEvent<Path> ev = (WatchEvent<Path>) event;
                        Path fileName = ev.context();

                        if (fileName.toString().equals("ServerCallout.xml")) {
                            log("Callout is being updated", LogUtils.Severity.INFO);

                            Platform.runLater(() -> {
                                Callout callout = getCallout();
                                if (callout != null) {
                                    streetField.setText(callout.getStreet());
                                    numberField.setText(callout.getNumber());
                                    areaField.setText(callout.getArea());
                                    priorityField.setText(callout.getPriority());
                                    timeField.setText(callout.getStartTime());
                                    dateField.setText(callout.getStartDate());
                                    countyField.setText(callout.getCounty());
                                    descriptionField.setText(callout.getDescription());
                                    typeField.setText(callout.getType());

                                } else {
                                    // Show alert or set default values
                                    log("No Callouts found.", LogUtils.Severity.WARN);
                                    streetField.setText("No data");
                                    numberField.setText("No data");
                                    areaField.setText("No data");
                                    priorityField.setText("No data");
                                    timeField.setText("No data");
                                    dateField.setText("No data");
                                    countyField.setText("No data");
                                    descriptionField.setText("No data");
                                    typeField.setText("No data");
                                }
                            });
                        }
                    }

                    boolean valid = key.reset();
                    if (!valid) {
                        break;
                    }
                }
            } catch (IOException e) {
                logError("I/O Error: ", e);
            }
        });

        watchThread.setDaemon(true);
        watchThread.start();
    }

}
