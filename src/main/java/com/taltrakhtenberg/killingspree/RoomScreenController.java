package com.taltrakhtenberg.killingspree;

public class RoomScreenController {
    private DBmanager db;
    private HelloApplication mainApp;

    public void init(DBmanager db, HelloApplication mainApp) {
        this.db = db;
        this.mainApp = mainApp;

    }
}
