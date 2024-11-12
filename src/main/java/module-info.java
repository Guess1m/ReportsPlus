module com.Guess.ReportsPlus {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive javafx.base;

    requires jakarta.activation;
    requires jakarta.xml.bind;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;
    requires jdk.compiler;
    
    opens com.Guess.ReportsPlus.logs to javafx.base;
    opens com.Guess.ReportsPlus to javafx.fxml;
    opens com.Guess.ReportsPlus.logs.Callout to jakarta.xml.bind;
    opens com.Guess.ReportsPlus.logs.TrafficStop to jakarta.xml.bind;
    opens com.Guess.ReportsPlus.logs.Incident to jakarta.xml.bind;
    opens com.Guess.ReportsPlus.logs.Search to jakarta.xml.bind;
    opens com.Guess.ReportsPlus.logs.Arrest to jakarta.xml.bind;
    opens com.Guess.ReportsPlus.logs.Patrol to jakarta.xml.bind;
    opens com.Guess.ReportsPlus.logs.Impound to jakarta.xml.bind;
    opens com.Guess.ReportsPlus.logs.TrafficCitation to jakarta.xml.bind;
    opens com.Guess.ReportsPlus.util.CourtData to jakarta.xml.bind;
    opens com.Guess.ReportsPlus.logs.Death to jakarta.xml.bind, javafx.base;
    opens com.Guess.ReportsPlus.logs.Accident to jakarta.xml.bind, javafx.base;
    opens com.Guess.ReportsPlus.util.History to jakarta.xml.bind, javafx.base;
    
    exports com.Guess.ReportsPlus;
    exports com.Guess.ReportsPlus.logs.Callout;
    exports com.Guess.ReportsPlus.logs.TrafficStop;
    exports com.Guess.ReportsPlus.logs.Incident;
    exports com.Guess.ReportsPlus.logs.Search;
    exports com.Guess.ReportsPlus.logs.Arrest;
    exports com.Guess.ReportsPlus.logs.Patrol;
    exports com.Guess.ReportsPlus.logs.Impound;
    exports com.Guess.ReportsPlus.logs.TrafficCitation;
    exports com.Guess.ReportsPlus.util.Server;
    opens com.Guess.ReportsPlus.util.Server to jakarta.xml.bind, javafx.fxml;
    exports com.Guess.ReportsPlus.util.Server.Objects.Callout;
    opens com.Guess.ReportsPlus.util.Server.Objects.Callout to jakarta.xml.bind, javafx.fxml;
    exports com.Guess.ReportsPlus.util.Server.Objects.ID;
    opens com.Guess.ReportsPlus.util.Server.Objects.ID to jakarta.xml.bind, javafx.fxml;
    exports com.Guess.ReportsPlus.util.Report;
    opens com.Guess.ReportsPlus.util.Report to jakarta.xml.bind, javafx.fxml;
    exports com.Guess.ReportsPlus.util.Misc;
    opens com.Guess.ReportsPlus.util.Misc to jakarta.xml.bind, javafx.fxml;
    exports com.Guess.ReportsPlus.Windows.Settings;
    opens com.Guess.ReportsPlus.Windows.Settings to jakarta.xml.bind, javafx.fxml;
    exports com.Guess.ReportsPlus.Windows.Misc;
    opens com.Guess.ReportsPlus.Windows.Misc to javafx.fxml;
    exports com.Guess.ReportsPlus.Windows.Server;
    opens com.Guess.ReportsPlus.Windows.Server to javafx.fxml;
    exports com.Guess.ReportsPlus.Windows.Other;
    opens com.Guess.ReportsPlus.Windows.Other to javafx.fxml;
    exports com.Guess.ReportsPlus.Desktop;
    opens com.Guess.ReportsPlus.Desktop to javafx.fxml;
    exports com.Guess.ReportsPlus.Windows.Apps;
    opens com.Guess.ReportsPlus.Windows.Apps to javafx.fxml;

}
