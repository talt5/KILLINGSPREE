package com.taltrakhtenberg.killingspree;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for handling the Enter Details screen.
 * This class manages user input for creating patients and rooms,
 * and interacts with the database.
 */
public class EnterDetailsScreenController {
    private DBmanager db;
    private MainApplication mainApp;
    private List<String> diseases;

    // Patient Creation
    @FXML
    private Button patientSaveButton;
    @FXML
    private Button patientClearButton;
    @FXML
    private TextField nameField;
    @FXML
    private TextField patientIdField;
    @FXML
    private TextField daysLeftField;
    @FXML
    private TextField daysRequiredField;
    @FXML
    private ChoiceBox<String> diseaseSelection;
    // Room Creation
    @FXML
    private ListView<String> allDiseasesLV;
    @FXML
    private ListView<String> selectedDiseasesLV;
    @FXML
    private TextField roomIdField;
    @FXML
    private TextField roomCapacityField;
    @FXML
    private Button roomSaveButton;
    @FXML
    private Button roomClearButton;

    /**
     * Initializes the controller with a database manager and main application reference.
     * Populates disease options and sets up event listeners.
     *
     * @param db       The database manager.
     * @param mainApp  The main application instance.
     */
    public void init(DBmanager db, MainApplication mainApp) {
        this.db = db;
        this.mainApp = mainApp;
        diseases = new ArrayList<>();
        diseases.addAll(List.of(new String[]{"COVID-19", "Flu", "Heart Disease", "Diabetes"}));
        for (String disease : diseases){
            diseaseSelection.getItems().add(disease);
            allDiseasesLV.getItems().add(disease);
        }
        allDiseasesLV.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null){
                // RunLater because we cant change the selection while a selection change is processed, and auto-selecting the whole list.
                Platform.runLater(() -> {
                    allDiseasesLV.getSelectionModel().select(-1);
                    selectedDiseasesLV.getItems().add(newValue);
                    allDiseasesLV.getItems().remove(newValue);
                });
            }
        });
        // TODO: make a single method to remove code duplication
        selectedDiseasesLV.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null){
                // RunLater because we cant change the selection while a selection change is processed, and auto-selecting the whole list.
                Platform.runLater(() -> {
                    selectedDiseasesLV.getSelectionModel().select(-1);
                    allDiseasesLV.getItems().add(newValue);
                    selectedDiseasesLV.getItems().remove(newValue);
                });
            }
        });

    }

    /**
     * Handles the action of saving a new patient.
     * Extracts user input and creates a Patient object.
     */
    @FXML
    // Adds the patient to the system.
    private void saveButtonClick(){
        String name = nameField.getText();
        int id = Integer.parseInt(patientIdField.getText());
        int daysLeft = Integer.parseInt(daysLeftField.getText());
        int daysRequired = Integer.parseInt(daysRequiredField.getText());
        String disease = diseaseSelection.getValue();
        // Adding the patient to the system
        Patient p = new Patient(id,disease,daysRequired,daysLeft);
        mainApp.addPatient(p);
        System.out.println(p);
    }

    /**
     * Clears all patient input fields.
     */
    // Clears the fields of patient creation.
    @FXML
    private void clearButtonClick(){
        nameField.clear();
        patientIdField.clear();
        daysLeftField.clear();
        daysRequiredField.clear();
        diseaseSelection.setValue(null);
    }

    /**
     * Handles the action of saving a new room.
     * Extracts user input and creates a Room object.
     */
    @FXML
    private void saveRoomButtonClick(){
        int roomId = Integer.parseInt(roomIdField.getText());
        int roomCapacity = Integer.parseInt(roomCapacityField.getText());
        List<String> diseases = selectedDiseasesLV.getItems();
        Room r = new Room(roomId, roomCapacity, diseases);
        mainApp.addRoom(r);
        System.out.println(r);
    }

    /**
     * Clears all room input fields.
     */
    @FXML
    private void clearRoomButtonClick(){
        roomIdField.clear();
        roomCapacityField.clear();
        allDiseasesLV.getItems().addAll(selectedDiseasesLV.getItems());
        selectedDiseasesLV.getItems().clear();
    }
}
