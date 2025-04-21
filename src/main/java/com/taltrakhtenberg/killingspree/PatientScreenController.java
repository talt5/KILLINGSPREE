package com.taltrakhtenberg.killingspree;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.util.HashMap;

/**
 * Controller class for managing the patient screen UI.
 * Handles the display and removal of patient information dynamically.
 */
public class PatientScreenController {
    private DBmanager db;
    private MainApplication mainApp;

    @FXML
    private VBox mainVBox;

    private TilePane mainTilePane;
    private HashMap<Integer, VBox> patientsObj;

    /**
     * Initializes the patient screen UI components.
     *
     * @param db      The database manager for handling data operations.
     * @param mainApp The main application instance for handling interactions.
     */
    public void init(DBmanager db, MainApplication mainApp) {
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

    /**
     * Creates and displays a visual representation of a patient in the UI.
     *
     * @param p The patient to be displayed.
     */
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

        patientVBOX.getChildren().addAll(pID, pDisease, pDaysReq, pDaysLeft);
        patientVBOX.setOnMouseClicked(event -> mainApp.deletePatient(p));

        mainTilePane.getChildren().add(patientVBOX);
        patientsObj.put(p.getId(), patientVBOX);
    }

    /**
     * Removes a patient's visual representation from the UI.
     *
     * @param p The patient to be removed.
     */
    public void deletePatientObject(Patient p) {
        VBox patientVBOX = patientsObj.get(p.getId());
        patientsObj.remove(p.getId());
        mainTilePane.getChildren().remove(patientVBOX);
    }

    /**
     * Returns the TilePane containing all patient objects.
     *
     * @return The TilePane containing patient elements.
     */
    public TilePane getMainTilePane() {
        return mainTilePane;
    }

    /**
     * Returns the mapping of patient IDs to their corresponding VBox elements.
     *
     * @return A HashMap mapping patient IDs to their UI representations.
     */
    public HashMap<Integer, VBox> getPatientsObj() {
        return patientsObj;
    }
}

