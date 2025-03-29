package com.taltrakhtenberg.killingspree;

import javafx.fxml.FXML;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.util.HashMap;


public class PatientScreenController {
    private DBmanager db;
    private HelloApplication mainApp;
    @FXML
    private VBox mainVBox;
    private TilePane mainTilePane;
    private HashMap<Integer, VBox> patientsObj;

    // Creates the dynamic TilePane which will store the shown patients and such.
    public void init(DBmanager db, HelloApplication mainApp) {
        this.db = db;
        this.mainApp = mainApp;
        patientsObj = new HashMap<>();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        mainTilePane = new TilePane();
        scrollPane.setContent(mainTilePane);
        mainVBox.getChildren().add(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
    }

    // Adds the given Patient to the ui.
    public void createPatientObject(Patient p) {
        VBox patientVBOX = new VBox();
        String cssLayout = "-fx-border-color: #000000;\n" +
                "-fx-border-insets: 5;\n" +
                "-fx-border-width: 3;\n" +
                "-fx-border-style: solid inside;\n";
        patientVBOX.setStyle(cssLayout);
        patientVBOX.setSpacing(5);
        patientVBOX.setAlignment(Pos.CENTER);
        Label pID = new Label("ID: " + p.getId());
        Label pDisease = new Label("Disease: " + p.getDisease());
        Label pDaysReq = new Label("Days Required: " + p.getDaysRequired());
        Label pDaysLeft = new Label("Days Left: " + p.getDaysLeftToLive());
        patientVBOX.getChildren().add(pID);
        patientVBOX.getChildren().add(pDisease);
        patientVBOX.getChildren().add(pDaysReq);
        patientVBOX.getChildren().add(pDaysLeft);
        patientVBOX.setOnMouseClicked(event -> mainApp.deletePatient(p));
        mainTilePane.getChildren().add(patientVBOX);
        patientsObj.put(p.getId(), patientVBOX);
    }

    public void deletePatientObject(Patient p) {
        VBox patientVBOX = patientsObj.get(p.getId());
        patientsObj.remove(p.getId());
        mainTilePane.getChildren().remove(patientVBOX);
    }

    public TilePane getMainTilePane() {
        return mainTilePane;
    }

    public HashMap<Integer, VBox> getPatientsObj() {
        return patientsObj;
    }
}
