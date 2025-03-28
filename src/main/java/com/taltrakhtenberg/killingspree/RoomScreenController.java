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

public class RoomScreenController {
    private DBmanager db;
    private HelloApplication mainApp;
    @FXML
    private VBox mainVBox;
    private TilePane mainTilePane;
    private HashMap<Integer, VBox> roomsObj;


    public void init(DBmanager db, HelloApplication mainApp) {
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
        roomVBOX.getChildren().add(rID);
        roomVBOX.getChildren().add(rCapacity);
        roomVBOX.getChildren().add(diseaseLabel);
        roomVBOX.getChildren().add(diseasesListView);

        mainTilePane.getChildren().add(roomVBOX);
        roomsObj.put(r.getId(), roomVBOX);
    }

    public TilePane getMainTilePane() {
        return mainTilePane;
    }

    public HashMap<Integer, VBox> getRoomsObj() {
        return roomsObj;
    }
}
