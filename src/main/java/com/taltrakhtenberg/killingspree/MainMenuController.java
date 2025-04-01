package com.taltrakhtenberg.killingspree;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.util.Optional;

public class MainMenuController {
    private DBmanager db;
    private HelloApplication mainApp;
    private Alert alert;

    @FXML
    // Opens an Alert for confirming the loading operation.
    protected void loadHospitalClick(){
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure you want to LOAD hospital information?");
        alert.setContentText("This action will OVERWRITE your current data.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            loadHospitalAction();
        }
    }
    // Loads the hospital information from db into apps Lists not including schedule.
    protected void loadHospitalAction() {
        mainApp.patients = db.getPatients();
        mainApp.rooms = db.getRooms();
        mainApp.deleteViewAllPatients();
        mainApp.deleteViewAllRooms();
        mainApp.addViewAllPatients();
        mainApp.addViewAllRooms();
    }
    @FXML
    // Opens an Alert for confirming the saving operation.
    protected void newHospitalClick(){
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("New Hospital");
        alert.setContentText("This action will DELETE all of your data");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            newHospitalAction();
        }


    }
    // Saves the hospital information to db from apps Lists not including schedule.
    protected void newHospitalAction() {
        mainApp.deleteAll();
    }
    @FXML
    // Calls the func for scheduling and saves the schedule to db.
    protected void scheduleClick(){
        mainApp.schedule();
    }
    public void init(DBmanager db, HelloApplication mainApp) {
        this.db = db;
        this.mainApp = mainApp;
        alert = new Alert(Alert.AlertType.NONE);
    }
}