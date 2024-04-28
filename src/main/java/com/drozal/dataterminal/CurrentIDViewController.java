package com.drozal.dataterminal;

import com.drozal.dataterminal.util.ID;
import com.drozal.dataterminal.util.IDs;
import com.drozal.dataterminal.util.reportCreationUtil;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Random;

import static com.drozal.dataterminal.util.stringUtil.getJarPath;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class CurrentIDViewController {

    private static AnchorPane titleBar;
    @javafx.fxml.FXML
    private BorderPane root;
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

    public static String generateRandomNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(9000000) + 1000000;
        return String.valueOf(randomNumber);
    }

    public static ID getMostRecentID() {
        String filePath = getJarPath() + File.separator + "serverData" + File.separator + "serverCurrentID.xml";
        File file = new File(filePath);

        if (!file.exists()) {
            System.err.println("File does not exist: " + filePath);
            return null;
        }

        try {
            // Create a JAXB context
            JAXBContext jaxbContext = JAXBContext.newInstance(IDs.class);

            // Create an unmarshaller
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            // Unmarshal the XML file
            IDs ids = (IDs) jaxbUnmarshaller.unmarshal(file);

            // Get the list of IDs
            List<ID> idList = ids.getIdList();

            // Return the last ID in the list (most recent)
            return idList.isEmpty() ? null : idList.get(idList.size() - 1);
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<ID> getIDsInfo() {
        try {
            // Create a JAXB context
            JAXBContext jaxbContext = JAXBContext.newInstance(IDs.class);

            // Create an unmarshaller
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            // Unmarshal the XML file
            IDs ids = (IDs) jaxbUnmarshaller.unmarshal(new File(getJarPath() + File.separator + "serverData" + File.separator + "serverCurrentID.xml"));

            // Return the list of IDs
            return ids.getIdList();
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Label getCursiveName() {
        return cursiveName;
    }

    public Label getGenNum1() {
        return genNum1;
    }

    public TextField getDob() {
        return dob;
    }

    public Label getGenNum2() {
        return genNum2;
    }

    public TextField getFirst() {
        return first;
    }

    public TextField getGender() {
        return gender;
    }

    public TextField getLast() {
        return last;
    }

    public void initialize() {
        titleBar = reportCreationUtil.createTitleBar("Current ID");
        root.setTop(titleBar);
        cursiveName.setStyle("-fx-font-family: 'Signerica Fat';");

        Platform.runLater(() -> {
            ID mostRecentID = getMostRecentID();
            if (mostRecentID != null) {
                String firstName = mostRecentID.getFirstName();
                String lastName = mostRecentID.getLastName();
                String birthday = mostRecentID.getBirthday();
                String Gender = mostRecentID.getGender();

                genNum1.setText(generateRandomNumber());
                genNum2.setText(generateRandomNumber());

                first.setText(firstName);
                cursiveName.setText(firstName);
                last.setText(lastName);
                dob.setText(birthday);
                gender.setText(Gender);

            } else {
                // Show alert or set default values
                System.out.println("No IDs found.");
                first.setText("No data");
                cursiveName.setText("No data");
                last.setText("No data");
                dob.setText("No data");
                gender.setText("No data");

                genNum1.setText(null);
                genNum2.setText(null);

            }
        });

        loadCurrentID();

        root.requestFocus();

        watchIDChanges();


    }

    private void loadCurrentID() {
        // TODO Get values from xml. If they dont throw an alert saying there is no ID to display.
        String FirstName = "Zain";
        String LastName = "Drozal";
        String DateOfBirth = "06/20/2004";
        String Gender = "Male";

        genNum1.setText(generateRandomNumber());
        genNum2.setText(generateRandomNumber());

        first.setText(FirstName);
        cursiveName.setText(FirstName);
        last.setText(LastName);
        dob.setText(DateOfBirth);
        gender.setText(Gender);

    }

    public void watchIDChanges() {
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

                        if (fileName.toString().equals("ServerCurrentID.xml")) {
                            System.out.println("ID is being updated");

                            Platform.runLater(() -> {
                                ID mostRecentID = getMostRecentID();
                                if (mostRecentID != null) {
                                    String firstName = mostRecentID.getFirstName();
                                    String lastName = mostRecentID.getLastName();
                                    String birthday = mostRecentID.getBirthday();
                                    String Gender = mostRecentID.getGender();

                                    genNum1.setText(generateRandomNumber());
                                    genNum2.setText(generateRandomNumber());

                                    first.setText(firstName);
                                    cursiveName.setText(firstName);
                                    last.setText(lastName);
                                    dob.setText(birthday);
                                    gender.setText(Gender);

                                } else {
                                    // Show alert or set default values
                                    System.out.println("No IDs found.");
                                    first.setText("No data");
                                    last.setText("No data");
                                    dob.setText("No data");
                                    gender.setText("No data");
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
                System.out.println("I/O Error: " + e.toString());
            }
        });

        watchThread.setDaemon(true);
        watchThread.start();
    }

}
