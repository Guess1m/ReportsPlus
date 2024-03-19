package com.drozal.dataterminal;

import javafx.event.Event;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static com.drozal.dataterminal.util.treeViewUtils.*;

public class TestWindowViewController {

    @javafx.fxml.FXML
    private TreeView treeView;
    @javafx.fxml.FXML
    private TextField fineField;
    @javafx.fxml.FXML
    private TextField chargeField;
    @javafx.fxml.FXML
    private TextField maxYears;
    @javafx.fxml.FXML
    private TextField probationChance;
    @javafx.fxml.FXML
    private TextField minYears;
    @javafx.fxml.FXML
    private TextField maxMonths;
    @javafx.fxml.FXML
    private TextField minMonths;

    public void initialize() throws ParserConfigurationException, IOException, SAXException {
        // Load XML file
        File file = new File("data/testXML.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = factory.newDocumentBuilder().parse(file);

        // Get root element
        Element root = document.getDocumentElement();

        // Create root tree item
        TreeItem<String> rootItem = new TreeItem<>(root.getNodeName());

        // Parse XML and populate tree
        parseTreeXML(root, rootItem);
        treeView.setRoot(rootItem);
        expandTreeItem(rootItem, "Root");
    }

    @javafx.fxml.FXML
    public void onMouseClick(Event event) {
        TreeItem<String> selectedItem = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
        if (selectedItem != null && selectedItem.isLeaf()) {
            chargeField.setText(selectedItem.getValue());
            // TODO: only use FINE value when in citations not in charges | Fines arent accurate
            fineField.setText(findXMLValue(selectedItem.getValue(), "fine", "data/testXML.xml"));
            // TODO: probably dont use probation chance
            probationChance.setText(findXMLValue(selectedItem.getValue(), "probation_chance", "data/testXML.xml"));
            minYears.setText(findXMLValue(selectedItem.getValue(), "min_years", "data/testXML.xml"));
            maxYears.setText(findXMLValue(selectedItem.getValue(), "max_years", "data/testXML.xml"));
            minMonths.setText(findXMLValue(selectedItem.getValue(), "min_months", "data/testXML.xml"));
            maxMonths.setText(findXMLValue(selectedItem.getValue(), "max_months", "data/testXML.xml"));
            // TODO: check if the textfield text contains (m) or (f) then put misdemeanor or felony in another text-field
        }
    }
}
