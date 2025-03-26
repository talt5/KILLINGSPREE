package com.taltrakhtenberg.killingspree;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.util.Optional;

public class MainMenuController {
    private DBmanager db;
    private HelloApplication mainApp;
    private Alert alert;

    @FXML
    // Opens an Alert for confirming the loading operation.
    protected void LoadHospitalClick(){
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure you want to LOAD hospital information?");
        alert.setContentText("This will OVERWRITE your current data.");
        alert.showAndWait();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            LoadHospitalAction();
        }
    }
    // Loads the hospital information from db into apps Lists not including schedule.
    protected void LoadHospitalAction() {
        mainApp.patients = db.getPatients();
        mainApp.rooms = db.getRooms();
        mainApp.deleteViewAllPatients();
        mainApp.deleteViewAllRooms();
        mainApp.addViewAllPatients();
        mainApp.addViewAllRooms();
    }
    @FXML
    // Opens an Alert for confirming the saving operation.
    protected void SaveHospitalClick(){
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure you want to SAVE hospital information?");
        alert.setContentText("This will OVERWRITE the database.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            SaveHospitalAction();
        }
    }
    // Saves the hospital information to db from apps Lists not including schedule.
    protected void SaveHospitalAction() {
        for (Room room : mainApp.rooms) {
            db.insert(room);
        }
        for (Patient patient : mainApp.patients) {
            db.insert(patient);
        }
        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Hospital data saved!");
        alert.showAndWait();
    }
    @FXML
    // Calls the func for scheduling and saves the schedule to db.
    protected void ScheduleClick(){
        mainApp.cspScheduler = new CSP_Scheduler(mainApp.patients, mainApp.rooms);
        mainApp.cspScheduler.schedule();
//        for (Room room : mainApp.rooms) {
//            db.insertSchedule(room);
//        }
    }
    public void init(DBmanager db, HelloApplication mainApp) {
        this.db = db;
        this.mainApp = mainApp;
        alert = new Alert(Alert.AlertType.NONE);
    }
}