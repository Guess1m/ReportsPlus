package com.drozal.dataterminal;

import com.drozal.dataterminal.util.stringUtil;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TestWindowViewController {

    @javafx.fxml.FXML
    private AreaChart<String, Number> testchart;

    public static void parseLogData(String logURL, Map<String, Integer> combinedAreasMap, String value) {
        Map<String, Integer> areasMap = new HashMap<>();
        File xmlFile = new File(logURL);
        if (!xmlFile.exists()) {
            return; // Return without parsing if file doesn't exist
        }

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("*");

            for (int temp = 0; temp < nodeList.getLength(); temp++) {
                Element element = (Element) nodeList.item(temp);
                String nodeName = element.getNodeName();
                if (nodeName.toLowerCase().contains(value)) {
                    String area = element.getTextContent().trim();
                    if (!area.isEmpty()) {
                        combinedAreasMap.put(area, combinedAreasMap.getOrDefault(area, 0) + 1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        // Aggregate all areas and their frequencies across different log files
        Map<String, Integer> combinedAreasMap = new HashMap<>();
        parseLogData(stringUtil.arrestLogURL, combinedAreasMap, "area");
        parseLogData(stringUtil.calloutLogURL, combinedAreasMap, "area");
        parseLogData(stringUtil.impoundLogURL, combinedAreasMap, "area");
        parseLogData(stringUtil.incidentLogURL, combinedAreasMap, "area");
        parseLogData(stringUtil.patrolLogURL, combinedAreasMap, "area");
        parseLogData(stringUtil.searchLogURL, combinedAreasMap, "area");
        parseLogData(stringUtil.trafficCitationLogURL, combinedAreasMap, "area");
        parseLogData(stringUtil.trafficstopLogURL, combinedAreasMap, "area");

        // Define the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Area");
        yAxis.setLabel("Frequency");

        // Create the AreaChart
        testchart.setTitle("Report Frequency by Area");

        // Create series and populate with data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : combinedAreasMap.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        // Add the series to the chart
        testchart.getData().add(series);
    }
}