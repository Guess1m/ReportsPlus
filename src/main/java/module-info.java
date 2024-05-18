module com.drozal.dataterminal {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires transitive javafx.graphics;
    requires transitive javafx.base;
    requires jdk.jsobject;

    requires jakarta.activation;
    requires jakarta.xml.bind;
    requires org.controlsfx.controls;
    requires jmdns;
    requires java.desktop;

    opens com.drozal.dataterminal.logs to javafx.base;
    opens com.drozal.dataterminal to javafx.fxml;
    opens com.drozal.dataterminal.logs.Callout to jakarta.xml.bind;
    opens com.drozal.dataterminal.logs.TrafficStop to jakarta.xml.bind;
    opens com.drozal.dataterminal.logs.Incident to jakarta.xml.bind;
    opens com.drozal.dataterminal.logs.Search to jakarta.xml.bind;
    opens com.drozal.dataterminal.logs.Arrest to jakarta.xml.bind;
    opens com.drozal.dataterminal.logs.Patrol to jakarta.xml.bind;
    opens com.drozal.dataterminal.logs.Impound to jakarta.xml.bind;
    opens com.drozal.dataterminal.logs.TrafficCitation to jakarta.xml.bind;
    opens com.drozal.dataterminal.util to jakarta.xml.bind, javafx.fxml;

    exports com.drozal.dataterminal;
    exports com.drozal.dataterminal.logs.Callout;
    exports com.drozal.dataterminal.logs.TrafficStop;
    exports com.drozal.dataterminal.logs.Incident;
    exports com.drozal.dataterminal.logs.Search;
    exports com.drozal.dataterminal.logs.Arrest;
    exports com.drozal.dataterminal.logs.Patrol;
    exports com.drozal.dataterminal.logs.Impound;
    exports com.drozal.dataterminal.logs.TrafficCitation;
    exports com.drozal.dataterminal.util;
    exports com.drozal.dataterminal.util.server;
    opens com.drozal.dataterminal.util.server to jakarta.xml.bind, javafx.fxml;
}
