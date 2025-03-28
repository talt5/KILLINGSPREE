package com.taltrakhtenberg.killingspree;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class EnterDetailsScreenController {
    private DBmanager db;
    private HelloApplication mainApp;
    private List<String> diseases;

    // Patient Creation
    @FXML
    protected Button patientSaveButton;
    @FXML
    protected Button patientClearButton;
    @FXML
    protected TextField nameField;
    @FXML
    protected TextField patientIdField;
    @FXML
    protected TextField daysLeftField;
    @FXML
    protected TextField daysRequiredField;
    @FXML
    protected ChoiceBox<String> diseaseSelection;
    // Room Creation
    @FXML
    protected ListView<String> allDiseasesLV;
    @FXML
    protected ListView<String> selectedDiseasesLV;
    @FXML
    protected TextField roomIdField;
    @FXML
    protected TextField roomCapacityField;
    @FXML
    protected Button roomSaveButton;
    @FXML
    protected Button roomClearButton;

    public void init(DBmanager db, HelloApplication mainApp) {
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

    // Clears the fields of patient creation.
    @FXML
    private void clearButtonClick(){
        nameField.clear();
        patientIdField.clear();
        daysLeftField.clear();
        daysRequiredField.clear();
        diseaseSelection.setValue(null);
    }

    @FXML
    private void saveRoomButtonClick(){
        int roomId = Integer.parseInt(roomIdField.getText());
        int roomCapacity = Integer.parseInt(roomCapacityField.getText());
        List<String> diseases = selectedDiseasesLV.getItems();
        Room r = new Room(roomId, roomCapacity, diseases);
        mainApp.addRoom(r);
        System.out.println(r);
    }

    @FXML
    private void clearRoomButtonClick(){
        roomIdField.clear();
        roomCapacityField.clear();
        allDiseasesLV.getItems().addAll(selectedDiseasesLV.getItems());
        selectedDiseasesLV.getItems().clear();
    }
}
