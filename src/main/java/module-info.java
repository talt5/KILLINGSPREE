module com.taltrakhtenberg.killingspree {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.taltrakhtenberg.killingspree to javafx.fxml;
    exports com.taltrakhtenberg.killingspree;
}