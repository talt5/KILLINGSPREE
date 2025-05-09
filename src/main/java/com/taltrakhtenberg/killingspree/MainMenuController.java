package com.taltrakhtenberg.killingspree;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Controller for the Main Menu screen.
 * Handles loading, creating a new hospital, and scheduling operations.
 */
public class MainMenuController {
    private DBmanager db;
    private MainApplication mainApp;
    private Alert alert;

    /**
     * Opens an alert to confirm creating a new hospital.
     * If confirmed, calls {@link #newHospitalAction()}.
     */
    @FXML
    private void newHospitalClick(){
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("New Hospital");
        alert.setContentText("This action will DELETE all of your data");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            newHospitalAction();
        }


    }

    /**
     * Deletes all hospital data from the application and database.
     */
    private void newHospitalAction() {
        mainApp.deleteAll();
    }

    /**
     * Initiates the scheduling process.
     */
    @FXML
    private void scheduleClick(){
        mainApp.schedule();
    }

    /**
     * Initializes the controller with database and main application references.
     *
     * @param db      Database manager instance.
     * @param mainApp Main application instance.
     */
    public void init(DBmanager db, MainApplication mainApp) {
        this.db = db;
        this.mainApp = mainApp;
        alert = new Alert(Alert.AlertType.NONE);
    }
}