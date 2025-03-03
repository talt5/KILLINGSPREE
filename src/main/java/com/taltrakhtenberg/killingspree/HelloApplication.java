package com.taltrakhtenberg.killingspree;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        List<Room> rooms = Arrays.asList(
                new Room("A", 2, Arrays.asList("COVID-19", "Flu")),
                new Room("B", 1, Arrays.asList("Heart Disease")),
                new Room("C", 3, Arrays.asList("Diabetes", "Flu"))
        );

        List<Patient> patients = Arrays.asList(
                new Patient("P1", "COVID-19", 1, 10, 20),
                new Patient("P2", "Flu", 3, 5, 15),
                new Patient("P3", "Heart Disease", 2, 7, 25),
                new Patient("P4", "Diabetes", 5, 4, 30),
                new Patient("P5", "Flu", 4, 6, 10),
                new Patient("P6", "COVID-19", 1, 10, 20),
                new Patient("P7", "Flu", 3, 5, 15),
                new Patient("P8", "Heart Disease", 2, 9, 10),
                new Patient("P9", "Diabetes", 5, 4, 30),
                new Patient("P10", "Flu", 4, 6, 10)
        );

        CSP_Scheduler scheduler = new CSP_Scheduler(patients, rooms);
        scheduler.solve();
        launch();

    }
}