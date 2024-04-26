package com.drozal.dataterminal;

import com.drozal.dataterminal.util.reportCreationUtil;
import com.drozal.dataterminal.util.server.FileUtlis;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.nio.file.*;
import java.util.Date;
import java.util.Random;

import static com.drozal.dataterminal.util.stringUtil.getJarPath;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class CurrentIDViewController {
    @javafx.fxml.FXML
    private BorderPane root;
    private static AnchorPane titleBar;
    @javafx.fxml.FXML
    private Label cursiveName;
    @javafx.fxml.FXML
    private Label genNum2;
    @javafx.fxml.FXML
    private TextField last;
    @javafx.fxml.FXML
    private TextField gender;
    @javafx.fxml.FXML
    private Label genNum1;
    @javafx.fxml.FXML
    private TextField dob;
    @javafx.fxml.FXML
    private TextField first;

    public static AnchorPane getTitleBar() {
        return titleBar;
    }

    public void initialize(){
        titleBar = reportCreationUtil.createTitleBar("Current ID");
        root.setTop(titleBar);

        loadCurrentID();

        root.requestFocus();

        watchIDChanges();


    }

    private void loadCurrentID(){
        // TODO Get values from xml. If they dont throw an alert saying there is no ID to display.
        String FirstName = "Zain";
        String LastName = "Drozal";
        String DateOfBirth = "06/20/2004";
        String Gender = "Male";

        cursiveName.setStyle("-fx-font-family: 'Signerica Fat';");

        genNum1.setText(generateRandomNumber());
        genNum2.setText(generateRandomNumber());

        first.setText(FirstName);
        cursiveName.setText(FirstName);
        last.setText(LastName);
        dob.setText(DateOfBirth);
        gender.setText(Gender);

    }

    public static String generateRandomNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(9000000) + 1000000;
        return String.valueOf(randomNumber);
    }

    public void watchIDChanges() {
        Path dir = Paths.get(getJarPath());

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

                        if (fileName.toString().equals("currentid.xml")) {
                            System.out.println(fileName + " has been modified");

                            // TODO add code for reading the values from the currentid.xml
                            String FirstName = "GETFIRSTNAME";
                            String LastName = "GETLASTNAME";
                            String DateOfBirth = "GETDOB";
                            String Gender = "GETGENDER";

                            Platform.runLater(() -> {
                                first.setText(FirstName);
                                cursiveName.setText(FirstName);
                                last.setText(LastName);
                                dob.setText(DateOfBirth);
                                gender.setText(Gender);
                            });
                        }
                    }

                    boolean valid = key.reset();
                    if (!valid) {
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("I/O Error: " + e.toString());
            }
        });

        watchThread.setDaemon(true);
        watchThread.start();
    }


}
