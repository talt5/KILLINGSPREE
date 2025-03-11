package com.taltrakhtenberg.killingspree;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class EnterDetailsScreenController {
    private DBmanager db;
    private HelloApplication mainApp;
    @FXML
    protected Button saveButton;
    @FXML
    protected Button clearButton;
    @FXML
    protected TextField nameField;
    @FXML
    protected TextField idField;
    @FXML
    protected TextField daysLeftField;
    @FXML
    protected TextField daysRequiredField;
    @FXML
    protected ChoiceBox<String> diseaseSelection;
    private List<String> diseases;

    public void init(DBmanager db, HelloApplication mainApp) {
        this.db = db;
        this.mainApp = mainApp;
        diseases = new ArrayList<>();
        diseases.addAll(List.of(new String[]{"COVID-19", "Flu", "Heart Disease", "Diabetes"}));
        for (String disease : diseases){
            diseaseSelection.getItems().add(disease);
        }
    }

    @FXML
    // Adds the patient to the system.
    protected void saveButtonClick(){
        String name = nameField.getText();
        int id = Integer.parseInt(idField.getText());
        int daysLeft = Integer.parseInt(daysLeftField.getText());
        int daysRequired = Integer.parseInt(daysRequiredField.getText());
        String disease = diseaseSelection.getValue();
        // Adding the patient to the system
        Patient p = new Patient(id,disease,daysRequired,daysLeft);
        mainApp.addPatient(p);
        System.out.println(p);
    }

    @FXML
    // Clears the fields of patient creation.
    protected void clearButtonClick(){
        nameField.clear();
        idField.clear();
        daysLeftField.clear();
        daysRequiredField.clear();
        diseaseSelection.setValue(null);
    }
}
