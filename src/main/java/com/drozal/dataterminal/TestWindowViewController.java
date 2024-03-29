package com.drozal.dataterminal;

import com.drozal.dataterminal.util.stringUtil;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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

    public static XYChart.Series<String, Number> parseEveryLog(String value) {
        Map<String, Integer> combinedAreasMap = new HashMap<>();
        parseLogData(stringUtil.arrestLogURL, combinedAreasMap, value);
        parseLogData(stringUtil.calloutLogURL, combinedAreasMap, value);
        parseLogData(stringUtil.impoundLogURL, combinedAreasMap, value);
        parseLogData(stringUtil.incidentLogURL, combinedAreasMap, value);
        parseLogData(stringUtil.patrolLogURL, combinedAreasMap, value);
        parseLogData(stringUtil.searchLogURL, combinedAreasMap, value);
        parseLogData(stringUtil.trafficCitationLogURL, combinedAreasMap, value);
        parseLogData(stringUtil.trafficstopLogURL, combinedAreasMap, value);

        // Sort the areas alphabetically ignoring case
        Map<String, Integer> sortedAreasMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        sortedAreasMap.putAll(combinedAreasMap);
        // Create series and populate with sorted data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : sortedAreasMap.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        return series;
    }

    public static void refreshChart(AreaChart chart, String value) {
        chart.getData().clear(); // Clear existing data from the chart
        chart.getData().add(parseEveryLog(value)); // Add new data to the chart
    }

    public void initialize() {
        // Add the series to the chart
        testchart.getData().add(parseEveryLog("area"));
    }
}