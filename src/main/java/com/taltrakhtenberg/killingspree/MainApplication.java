package com.taltrakhtenberg.killingspree;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * The main application class that initializes and manages the user interface.
 */
public class MainApplication extends Application {
    private MainMenuController MMController;
    private PatientScreenController PSController;
    private EnterDetailsScreenController EDSController;
    private RoomScreenController RSController;
    private AssignmentScreenController ASController;
    protected List<Room> rooms;
    protected List<Patient> patients;
    private CSP_Scheduler cspScheduler;
    private DBmanager db;
    private List<Stage> stages;

    /**
     * Initializes and displays all application screens.
     *
     * @param stage The primary stage for the application.
     * @throws IOException If loading FXML files fails.
     */
    @Override
    public void start(Stage stage) throws IOException {
        rooms = new ArrayList<>();
        patients = new ArrayList<>();
        db = new DBmanager("testing.db");
        stages = new LinkedList<>();

        // Delete "testing()" after testing.
        // testing();
        // Loading Screens
        // Main menu
        FXMLLoader MainMenu = new FXMLLoader(MainApplication.class.getResource("MainMenu.fxml"));
        Scene MMscene = new Scene(MainMenu.load(), 400, 240);
        MMController = MainMenu.getController();
        MMController.init(db, this);
        stage.setTitle("Main Menu");
        stage.setScene(MMscene);
        stage.show();
        stage.setY(Screen.getPrimary().getBounds().getHeight()/2);
        stages.add(stage);

        // Patient List Screen
        FXMLLoader PatientScreen = new FXMLLoader(MainApplication.class.getResource("PatientScreen.fxml"));
        Scene PSscene = new Scene(PatientScreen.load(), 300, 400);
        PSController = PatientScreen.getController();
        PSController.init(db, this);
        Stage PSStage = new Stage();
        PSStage.setTitle("Patient List");
        PSStage.setScene(PSscene);
        PSStage.setX(stage.getX() - PSscene.getWidth());
        PSStage.setY(stage.getY());
        PSStage.show();
        stages.add(PSStage);

        // Enter Details Screen
        FXMLLoader EnterDetailsScreen = new FXMLLoader(MainApplication.class.getResource("EnterDetailsScreen.fxml"));
        Scene EDSScene = new Scene(EnterDetailsScreen.load(), 600, 345);
        EDSController = EnterDetailsScreen.getController();
        EDSController.init(db, this);
        Stage EDSStage = new Stage();
        EDSStage.setTitle("Add to system");
        EDSStage.setScene(EDSScene);
        EDSStage.show();
        EDSStage.resizableProperty().setValue(false);
        EDSStage.setX(stage.getX() + stage.getWidth()/2 - EDSScene.getWidth()/2);
        EDSStage.setY(stage.getY() - EDSStage.getHeight());
        stages.add(EDSStage);


        // Room List Screen
        FXMLLoader RoomScreen = new FXMLLoader(MainApplication.class.getResource("RoomScreen.fxml"));
        Scene RSScene = new Scene(RoomScreen.load(), 300, 400);
        RSController = RoomScreen.getController();
        RSController.init(db, this);
        Stage RSStage = new Stage();
        RSStage.setTitle("Room List");
        RSStage.setScene(RSScene);
        RSStage.show();
        RSStage.setX(stage.getX() + stage.getWidth());
        RSStage.setY(stage.getY());
        stages.add(RSStage);

        // Load saved hospital
        patients = db.getPatients();
        rooms = db.getRooms();
        addViewAllPatients();
        addViewAllRooms();

    }

    /**
     * Adds all patients to the user interface.
     */
    public void addViewAllPatients() {
        for (Patient p : patients) {
            PSController.createPatientObject(p);
        }
    }

    /**
     * Adds all rooms to the user interface.
     */
    public void addViewAllRooms() {
        for (Room r : rooms) {
            RSController.createRoomObject(r);
        }
    }

    /**
     * Removes all patients from the user interface.
     */
    public void deleteViewAllPatients() {
        PSController.getMainTilePane().getChildren().clear();
        PSController.getPatientsObj().clear();
    }

    /**
     * Removes all rooms from the user interface.
     */
    public void deleteViewAllRooms(){
        RSController.getMainTilePane().getChildren().clear();
        RSController.getRoomsObj().clear();
    }

    /**
     * Closes all application windows.
     */
    public void closeAllWindows() {
        for (Stage stage : stages) {
            stage.close();
        }
    }

    /**
     * Runs the scheduling algorithm and displays the assignment screen.
     */
    public void schedule(){
        cspScheduler = new CSP_Scheduler(patients, rooms);
        Map<Integer, Assignment> assignments = cspScheduler.schedule();
        closeAllWindows();
        openAssignmentScreen();
        for (Patient p : patients) {
            Assignment assignment = assignments.get(p.getId());
            ASController.createAssignmentObject(assignment, assignment.getRoom(), p);
        }
    }

    /**
     * Opens the assignment screen.
     */
    public void openAssignmentScreen() {
        try {
            FXMLLoader AssignmentScreen = new FXMLLoader(MainApplication.class.getResource("AssignmentScreen.fxml"));
            Scene ASScene = new Scene(AssignmentScreen.load(), 300, 400); // 🍑
            ASController = AssignmentScreen.getController();
            ASController.init(db, this);
            Stage ASStage = new Stage(); // more 🍑
            ASStage.setTitle("Assignment Screen");
            ASStage.setScene(ASScene);
            ASStage.show();
            closeAllWindows();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new patient to the list, database, and UI.
     *
     * @param p The patient to add.
     */
    public void addPatient(Patient p) {
        patients.add(p);
        db.insert(p);
        PSController.createPatientObject(p);
    }

    /**
     * Adds a new room to the list, database, and UI.
     *
     * @param r The room to add.
     */
    public void addRoom(Room r) {
        rooms.add(r);
        db.insert(r);
        RSController.createRoomObject(r);
    }

    /**
     * Deletes a patient from the list, database, and UI.
     *
     * @param p The patient to delete.
     */
    public void deletePatient(Patient p) {
        patients.remove(p);
        db.delete(p);
        PSController.deletePatientObject(p);
    }

    /**
     * Deletes a room from the list, database, and UI.
     *
     * @param r The room to delete.
     */
    public void deleteRoom(Room r) {
        rooms.remove(r);
        db.delete(r);
        RSController.deleteRoomObject(r);
    }

    /**
     * Deletes all rooms and patients from the system and UI.
     */
    public void deleteAll(){
        RSController.getMainTilePane().getChildren().clear();
        PSController.getMainTilePane().getChildren().clear();
        PSController.getPatientsObj().clear();
        RSController.getRoomsObj().clear();
        rooms.clear();
        patients.clear();
        db.deleteAll();
    }

    /**
     * Entry point of the JavaFX application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        launch();
    }

    public void testing(){
        rooms = new ArrayList<>(Arrays.asList(
                new Room(101, 2, Arrays.asList("COVID-19", "Flu")),
                new Room(102, 1, Arrays.asList("Heart Disease")),
                new Room(103, 1, Arrays.asList("Diabetes", "Flu")))
        );
        patients = new ArrayList<>(Arrays.asList(
                new Patient(1, "COVID-19", 10, 20),
                new Patient(2, "Flu", 5, 15),
                new Patient(3, "Heart Disease", 7, 7),
                new Patient(4, "Diabetes", 4, 6),
                new Patient(5, "Flu", 6, 10),
                new Patient(6, "COVID-19", 10, 20),
                new Patient(7, "Flu",  5, 15),
                new Patient(8, "Heart Disease", 9, 7),
                new Patient(9, "Diabetes", 4, 3),
                new Patient(10, "Flu", 6, 10)
        ));
    }

}