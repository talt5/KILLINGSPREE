package com.taltrakhtenberg.killingspree;

import javafx.fxml.FXML;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;


public class PatientScreenController {
    private DBmanager db;
    private HelloApplication mainApp;
    @FXML
    private VBox mainVBox;
    private TilePane mainPatientTilePane;
    private HashMap<Integer, VBox> patientsObj;

    // Creates the dynamic TilePane which will store the shown patients and such.
    public void init(DBmanager db, HelloApplication mainApp) {
        this.db = db;
        this.mainApp = mainApp;
        patientsObj = new HashMap<>();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        mainPatientTilePane = new TilePane();
        scrollPane.setContent(mainPatientTilePane);
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
        Label pID = new Label(String.valueOf(p.getId()));
        Label pDisease = new Label(p.getDisease());
        Label pDaysReq = new Label(String.valueOf(p.getDaysRequired()));
        Label pDaysLeft = new Label(String.valueOf(p.getDaysLeftToLive()));
        patientVBOX.getChildren().add(pID);
        patientVBOX.getChildren().add(pDisease);
        patientVBOX.getChildren().add(pDaysReq);
        patientVBOX.getChildren().add(pDaysLeft);

        mainPatientTilePane.getChildren().add(patientVBOX);
        patientsObj.put(p.getId(), patientVBOX);
    }

    public TilePane getMainPatientTilePane() {
        return mainPatientTilePane;
    }

    public HashMap<Integer, VBox> getPatientsObj() {
        return patientsObj;
    }
}
