module com.drozal.dataterminal {
	requires javafx.controls;
	requires javafx.fxml;
	requires transitive javafx.graphics;
	requires transitive javafx.base;
	
	requires jakarta.activation;
	requires jakarta.xml.bind;
	requires org.controlsfx.controls;
	requires java.desktop;
	requires com.fasterxml.jackson.databind;
	
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
	
	exports com.drozal.dataterminal;
	exports com.drozal.dataterminal.logs.Callout;
	exports com.drozal.dataterminal.logs.TrafficStop;
	exports com.drozal.dataterminal.logs.Incident;
	exports com.drozal.dataterminal.logs.Search;
	exports com.drozal.dataterminal.logs.Arrest;
	exports com.drozal.dataterminal.logs.Patrol;
	exports com.drozal.dataterminal.logs.Impound;
	exports com.drozal.dataterminal.logs.TrafficCitation;
	exports com.drozal.dataterminal.util.server;
	opens com.drozal.dataterminal.util.server to jakarta.xml.bind, javafx.fxml;
	exports com.drozal.dataterminal.util.server.Objects.Callout;
	opens com.drozal.dataterminal.util.server.Objects.Callout to jakarta.xml.bind, javafx.fxml;
	exports com.drozal.dataterminal.util.server.Objects.ID;
	opens com.drozal.dataterminal.util.server.Objects.ID to jakarta.xml.bind, javafx.fxml;
	exports com.drozal.dataterminal.util.Report;
	opens com.drozal.dataterminal.util.Report to jakarta.xml.bind, javafx.fxml;
	exports com.drozal.dataterminal.util.Window;
	opens com.drozal.dataterminal.util.Window to jakarta.xml.bind, javafx.fxml;
	exports com.drozal.dataterminal.util.Misc;
	opens com.drozal.dataterminal.util.Misc to jakarta.xml.bind, javafx.fxml;
}
