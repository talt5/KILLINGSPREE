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
        DBmanager db = new DBmanager("testing.dibil");
        List<Room> rooms = Arrays.asList(
                new Room(101, 2, Arrays.asList("COVID-19", "Flu")),
                new Room(102, 1, Arrays.asList("Heart Disease")),
                new Room(103, 3, Arrays.asList("Diabetes", "Flu"))
        );
//        List<Room> rooms = db.getRooms();
//        List<Patient> patients = db.getPatients();

        List<Patient> patients = Arrays.asList(
                new Patient(1, "COVID-19", 10, 20),
                new Patient(2, "Flu", 5, 15),
                new Patient(3, "Heart Disease", 7, 25),
                new Patient(4, "Diabetes", 4, 30),
                new Patient(5, "Flu", 6, 10),
                new Patient(6, "COVID-19", 10, 20),
                new Patient(7, "Flu",  5, 15),
                new Patient(8, "Heart Disease", 9, 10),
                new Patient(9, "Diabetes", 4, 30),
                new Patient(10, "Flu", 6, 10)
        );
        for  (Room room : rooms) {
            System.out.println(db.insert(room));
        }
        for  (Patient patient : patients) {
            System.out.println(db.insert(patient));
        }
        CSP_Scheduler scheduler = new CSP_Scheduler(patients, rooms);
        scheduler.solve();
        for (Room room : rooms) {
            System.out.println(db.insertSchedule(room));
        }

        launch();

    }
}