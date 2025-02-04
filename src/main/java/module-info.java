module com.taltrakhtenberg.killingspree {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.taltrakhtenberg.killingspree to javafx.fxml;
    exports com.taltrakhtenberg.killingspree;
}