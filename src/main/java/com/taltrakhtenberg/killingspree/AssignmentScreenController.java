package com.taltrakhtenberg.killingspree;

import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.util.HashMap;

/**
 * Controller for the Assignment Screen.
 * Manages the UI for assigning patients to rooms and displaying unassigned patients.
 */
public class AssignmentScreenController {
    private DBmanager db;
    private MainApplication mainApp;
    @FXML
    private HBox mainHBox;
    private TilePane mainTilePane;
    private HashMap<Integer, VBox> roomsObj;
    private VBox unassignedPatientsVBox;

    /**
     * Initializes the assignment screen.
     * @param db The database manager instance.
     * @param mainApp The main application instance.
     */
    public void init(DBmanager db, MainApplication mainApp) {
        this.db = db;
        this.mainApp = mainApp;
        roomsObj = new HashMap<>();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        mainTilePane = new TilePane();
        scrollPane.setContent(mainTilePane);
        mainHBox.getChildren().add(scrollPane);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        mainTilePane.maxHeightProperty().bind(mainHBox.heightProperty());

        VBox unassignedVBox = new VBox();
        String cssLayout = "-fx-border-color: #000000;\n" +
                "-fx-border-insets: 5;\n" +
                "-fx-border-width: 3;\n" +
                "-fx-border-style: solid inside;\n";
        unassignedVBox.setStyle(cssLayout);
        Label unassignedPatientsLabel = new Label("Unassigned Patients");
        unassignedPatientsLabel.setStyle("-fx-font-weight: bold;" +
                "-fx-font-size: 18px;");
        unassignedVBox.getChildren().add(unassignedPatientsLabel);
        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);
        unassignedVBox.getChildren().add(separator);
        ScrollPane unassignedScrollPane = new ScrollPane();
        unassignedPatientsVBox = new VBox();
        unassignedPatientsVBox.setSpacing(10);
        unassignedScrollPane.setContent(unassignedPatientsVBox);
        unassignedVBox.getChildren().add(unassignedScrollPane);
        mainTilePane.getChildren().add(unassignedVBox);
    }

    /**
     * Creates an assignment UI object for a given assignment, room, and patient.
     * @param a The assignment instance.
     * @param r The room instance.
     * @param p The patient instance.
     */
    public void createAssignmentObject(Assignment a, Room r, Patient p) {
        if (!a.getDead() && !roomsObj.containsKey(r.getId())) {
            createRoomObject(r);
        }
        VBox patientVBox = new VBox();
        String cssLayout = "-fx-border-color: #000000;\n" +
                "-fx-border-insets: 5;\n" +
                "-fx-border-width: 3;\n" +
                "-fx-border-style: solid inside;\n";
        patientVBox.setStyle(cssLayout);
        patientVBox.setSpacing(10);
        patientVBox.setAlignment(Pos.CENTER);
        Label pID = new Label("ID: " + p.getId());

        if (a.getDead()) {
            patientVBox.getChildren().add(pID);
            unassignedPatientsVBox.getChildren().add(patientVBox);
        } else {
            Label pDaysStaying = new Label("Days Staying: " + p.getDaysRequired());
            Label pDayEnter = new Label("Entering Day: " + a.getStartDay());
            patientVBox.getChildren().add(pID);
            patientVBox.getChildren().add(pDayEnter);
            patientVBox.getChildren().add(pDaysStaying);
            roomsObj.get(r.getId()).getChildren().add(patientVBox);
        }
    }

    /**
     * Creates a UI representation of a room.
     * @param r The room instance.
     */
    public void createRoomObject(Room r) {
        VBox roomVBOX = new VBox();
        String cssLayout = "-fx-border-color: #000000;\n" +
                "-fx-border-insets: 5;\n" +
                "-fx-border-width: 3;\n" +
                "-fx-border-style: solid inside;\n";
        roomVBOX.setStyle(cssLayout);
        roomVBOX.setSpacing(5);
        roomVBOX.setAlignment(Pos.TOP_CENTER);
        Label rID = new Label("Room " + r.getId());
        rID.setStyle("-fx-font-weight: bold;" +
                "-fx-font-size: 18px;");
        Label rCapacity = new Label("Capacity: " + r.getCapacity());
        Label diseaseLabel = new Label("Diseases:");
        Label lPatients = new Label("Patients:");
        lPatients.setStyle("-fx-font-size: 18px;");
        ListView<String> diseasesListView = new ListView<>();
        diseasesListView.setPrefHeight(120);
        diseasesListView.setPrefWidth(120);
        ScrollPane roomScroll = new ScrollPane();
        VBox pVBox = new VBox();
        pVBox.setSpacing(10);
        pVBox.setAlignment(Pos.CENTER);
        roomScroll.setContent(pVBox);
        roomScroll.setFitToWidth(true);
        VBox.setVgrow(pVBox, Priority.NEVER);

        diseasesListView.getItems().addAll(r.getDiseases());
        roomVBOX.getChildren().add(rID);
        roomVBOX.getChildren().add(rCapacity);
        roomVBOX.getChildren().add(diseaseLabel);
        roomVBOX.getChildren().add(diseasesListView);
        roomVBOX.getChildren().add(lPatients);
        roomVBOX.getChildren().add(roomScroll);

        mainTilePane.getChildren().add(roomVBOX);
        roomsObj.put(r.getId(), pVBox);
    }

    /**
     * Gets the main tile pane.
     * @return The TilePane used in the UI.
     */
    public TilePane getMainTilePane() {
        return mainTilePane;
    }

    /**
     * Gets the rooms object map.
     * @return A HashMap of room IDs to their VBox UI elements.
     */
    public HashMap<Integer, VBox> getRoomsObj() {
        return roomsObj;
    }
}
