package com.taltrakhtenberg.killingspree;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.util.HashMap;

/**
 * Controller class for managing the room screen in the application.
 */
public class RoomScreenController {
    private DBmanager db;
    private MainApplication mainApp;

    @FXML
    private VBox mainVBox;

    private TilePane mainTilePane;
    private HashMap<Integer, VBox> roomsObj;

    /**
     * Initializes the RoomScreenController with database manager and main application reference.
     *
     * @param db       The database manager instance.
     * @param mainApp  The main application instance.
     */
    public void init(DBmanager db, MainApplication mainApp) {
        this.db = db;
        this.mainApp = mainApp;
        roomsObj = new HashMap<>();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        mainTilePane = new TilePane();
        scrollPane.setContent(mainTilePane);
        mainVBox.getChildren().add(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
    }

    /**
     * Creates a visual representation of a room and adds it to the UI.
     *
     * @param r The room object to display.
     */
    public void createRoomObject(Room r) {
        VBox roomVBOX = new VBox();
        String cssLayout = "-fx-border-color: #000000;\n" +
                "-fx-border-insets: 5;\n" +
                "-fx-border-width: 3;\n" +
                "-fx-border-style: solid inside;\n";
        roomVBOX.setStyle(cssLayout);
        roomVBOX.setSpacing(5);
        roomVBOX.setAlignment(Pos.CENTER);

        Label rID = new Label("ID: " + r.getId());
        Label rCapacity = new Label("Capacity: " + r.getCapacity());
        Label diseaseLabel = new Label("Diseases:");
        ListView<String> diseasesListView = new ListView<>();
        diseasesListView.setPrefHeight(120);
        diseasesListView.setPrefWidth(120);

        diseasesListView.getItems().addAll(r.getDiseases());

        roomVBOX.getChildren().addAll(rID, rCapacity, diseaseLabel, diseasesListView);
        roomVBOX.setOnMouseClicked(event -> mainApp.deleteRoom(r));

        mainTilePane.getChildren().add(roomVBOX);
        roomsObj.put(r.getId(), roomVBOX);
    }

    /**
     * Removes a visual representation of a room from the UI.
     *
     * @param r The room object to remove.
     */
    public void deleteRoomObject(Room r) {
        VBox roomVBOX = roomsObj.get(r.getId());
        roomsObj.remove(r.getId());
        mainTilePane.getChildren().remove(roomVBOX);
    }

    /**
     * Returns the main TilePane containing room UI elements.
     *
     * @return The TilePane containing rooms.
     */
    public TilePane getMainTilePane() {
        return mainTilePane;
    }

    /**
     * Returns the HashMap storing room UI elements.
     *
     * @return The HashMap mapping room IDs to VBox elements.
     */
    public HashMap<Integer, VBox> getRoomsObj() {
        return roomsObj;
    }
}
