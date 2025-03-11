module com.taltrakhtenberg.killingspree {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.taltrakhtenberg.killingspree to javafx.fxml;
    exports com.taltrakhtenberg.killingspree;
}