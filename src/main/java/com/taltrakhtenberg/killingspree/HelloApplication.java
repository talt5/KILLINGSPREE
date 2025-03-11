package com.taltrakhtenberg.killingspree;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.List;

public class HelloApplication extends Application {
    private MainMenuController MMController;
    private PatientScreenController PSController;
    private EnterDetailsScreenController EDSController;
    protected List<Room> rooms;
    protected List<Patient> patients;
    protected CSP_Scheduler cspScheduler;
    private DBmanager db;

    @Override
    // Initializes all the screens.
    public void start(Stage stage) throws IOException {
        rooms = null;
        patients = null;
        db = new DBmanager("testing.db");

        // Delete "testing()" after testing.
        testing();
        // Loading Screens
        // Main menu
        FXMLLoader MainMenu = new FXMLLoader(HelloApplication.class.getResource("MainMenu.fxml"));
        Scene MMscene = new Scene(MainMenu.load(), 400, 240);
        MMController = MainMenu.getController();
        MMController.init(db, this);
        stage.setTitle("Main Menu");
        stage.setScene(MMscene);
        stage.show();
        // Patient List Screen
        FXMLLoader PatientScreen = new FXMLLoader(HelloApplication.class.getResource("PatientScreen.fxml"));
        Scene PSscene = new Scene(PatientScreen.load(), 300, 400);
        PSController = PatientScreen.getController();
        PSController.init(db, this);
        Stage PSStage = new Stage();
        PSStage.setTitle("Patient List");
        PSStage.setScene(PSscene);
        PSStage.setX(stage.getX() - PSscene.getWidth());
        PSStage.setY(stage.getY());
        PSStage.show();

        // Enter Details Screen
        FXMLLoader EnterDetailsScreen = new FXMLLoader(HelloApplication.class.getResource("EnterDetailsScreen.fxml"));
        Scene EDSScene = new Scene(EnterDetailsScreen.load(), 600, 345);
        EDSController = EnterDetailsScreen.getController();
        EDSController.init(db, this);
        Stage EDSStage = new Stage();
        EDSStage.setTitle("Add to system");
        EDSStage.setScene(EDSScene);
        EDSStage.show();
        EDSStage.setX(stage.getX());
        EDSStage.setY(stage.getY() - EDSStage.getHeight());

    }
    // Adds all the patients from the List to the ui.
    public void addViewAllPatients() {
        for (Patient p : patients) {
            PSController.createPatientObject(p);
        }
    }

    // TODO: IMPLEMENT WHEN ROOM UI READY
    public void addViewAllRooms() {
    }

    // Deletes from ui all of the patients.
    public void deleteViewAllPatients() {
        PSController.getMainPatientTilePane().getChildren().clear();
        PSController.getPatientsObj().clear();
    }

    // TODO: IMPLEMENT WHEN ROOM UI READY
    public void deleteViewAllRooms(){
    }

    // Adds the patient to the List, the db, and the ui.
    public void addPatient(Patient p) {
        patients.add(p);
        db.insert(p);
        PSController.createPatientObject(p);
    }
    /*
    INSTRUCTIONS FOR FIRST RUN - Until UI is ready:
    To check if db works, click "Save Hospital" (data is already present in memory).
    Now click on "Load Hospital"
    The Patient list should populate and console will show the objects that loaded from db.
     */
    public static void main(String[] args) {
        launch();
    }

    public void testing(){
        rooms = Arrays.asList(
                new Room(101, 2, Arrays.asList("COVID-19", "Flu")),
                new Room(102, 1, Arrays.asList("Heart Disease")),
                new Room(103, 3, Arrays.asList("Diabetes", "Flu"))
        );
        patients = Arrays.asList(
                new Patient(1, "COVID-19", 10, 20),
                new Patient(2, "Flu", 5, 15),
                new Patient(3, "Heart Disease", 7, 25),
                new Patient(4, "Diabetes", 4, 30),
                new Patient(5, "Flu", 6, 10),
                new Patient(6, "COVID-19", 10, 20),
                new Patient(7, "Flu",  5, 15),
                new Patient(8, "Heart Disease", 9, 10),
                new Patient(9, "Diabetes", 4, 30),
                new Patient(10, "Flu", 6, 10)
        );
    }

}