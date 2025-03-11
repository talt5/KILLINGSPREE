package com.taltrakhtenberg.killingspree;

import java.io.File;
import java.sql.*;
import java.util.*;

public class DBmanager {
    Connection conn;

    public DBmanager(String filename) {
        initDB(filename);
    }
    // Loads or creates the database.
    public void initDB(String filename){
        String url = "jdbc:sqlite:" + filename;
        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                Statement stmt = conn.createStatement();

                String creationString = "CREATE TABLE IF NOT EXISTS patient ("
                        + "	id INTEGER PRIMARY KEY,"
                        + " disease TEXT NOT NULL,"
                        + "	daysRequired INTEGER NOT NULL,"
                        + " daysLeftToLive INTEGER NOT NULL"
                        + ");";
                stmt.execute(creationString);
                creationString = "CREATE TABLE IF NOT EXISTS room ("
                        + "	id INTEGER PRIMARY KEY,"
                        + "	capacity INTEGER NOT NULL"
                        + ");";
                stmt.execute(creationString);
                creationString = "CREATE TABLE IF NOT EXISTS schedule (" +
                        " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " day INTEGER NOT NULL," +
                        " roomID INTEGER NOT NULL," +
                        " patientID INTEGER NOT NULL," +
                        " FOREIGN KEY(roomID) REFERENCES room(id)," +
                        " FOREIGN KEY(patientID) REFERENCES patient(id)" +
                        ");";
                stmt.execute(creationString);
                creationString = "CREATE TABLE IF NOT EXISTS diseases (" +
                        " 	id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " roomID INTEGER NOT NULL," +
                        " disease TEXT NOT NULL," +
                        " FOREIGN KEY(roomID) REFERENCES room(id)" +
                        ");";
                stmt.execute(creationString);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Inserting the rooms schedule to db
    public int insertSchedule(Room room) {
        // Need to delete all entries first, the whole schedule will change when adding or removing hospital objects.
        String deleteAll = "DELETE FROM schedule  WHERE roomID = (?)";
        String insertString = "INSERT INTO schedule (day,roomID,patientID) VALUES (?, ?, ?)";
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(deleteAll);
            stmt.setInt(1, room.getId());
            stmt.executeUpdate();
            stmt = conn.prepareStatement(insertString);
            for (Map.Entry<Integer, List<Patient>> entry : room.schedule.entrySet()) {
                for (Patient pat : entry.getValue()) {
                    stmt.setInt(1, entry.getKey());
                    stmt.setInt(2, room.getId());
                    stmt.setInt(3, pat.getId());
                    stmt.executeUpdate();
                }
            }
            return 1;
        }
        catch (SQLException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));

            return -1;
        }
    }
    // Inserts a room or a patient to db
    public int insert(Object object){
        String insertString;
        PreparedStatement stmt;
        try {
            if (object instanceof Patient patient) {
                insertString = "INSERT OR REPLACE INTO " + "patient" + " (" + "id,disease,daysRequired,daysLeftToLive" + ") VALUES (" + "?,?,?,?" + ");";
                stmt = conn.prepareStatement(insertString);
                stmt.setInt(1, patient.getId());
                stmt.setString(2, patient.getDisease());
                stmt.setInt(3, patient.getDaysRequired());
                stmt.setInt(4, patient.getDaysLeftToLive());
                stmt.executeUpdate();
            }
            else if (object instanceof Room room) {
                insertString = "INSERT OR REPLACE INTO " + "room" + " (" + "id,capacity" + ") VALUES (" + "?,?" + ");";
                stmt = conn.prepareStatement(insertString);
                stmt.setInt(1, room.getId());
                stmt.setInt(2, room.getCapacity());
                stmt.executeUpdate();
                // TODO: Make it avoid duplicating diseases.
                insertString = "INSERT OR REPLACE INTO diseases (roomID, disease) VALUES (?,?)";
                stmt = conn.prepareStatement(insertString);
                for (String disease: room.getDiseases()){
                    stmt.setInt(1, room.getId());
                    stmt.setString(2, disease);
                    stmt.executeUpdate();
                }

            }
            else {
                return 0;
            }

        }
        catch (SQLException e) {
            e.printStackTrace();;
            return -1;
        }
        return 1;
    }
    // Creates a List of rooms from the database
    public List<Room> getRooms() {
        try {
            String selectAll = "SELECT * FROM room";
            PreparedStatement stmt = conn.prepareStatement(selectAll);
            ResultSet rsRooms = stmt.executeQuery();
            List<Room> rooms = new ArrayList<>();
            while(rsRooms.next()) {
                int id = rsRooms.getInt("id");
                stmt = conn.prepareStatement("SELECT disease FROM diseases WHERE roomid = ?");
                stmt.setInt(1, id);
                ResultSet rsDiseases = stmt.executeQuery();
                List<String> diseases = new ArrayList<>();
                while (rsDiseases.next()) {
                    diseases.add(rsDiseases.getString("disease"));
                }
                rooms.add(new Room(rsRooms.getInt("id"), rsRooms.getInt("capacity"), diseases));
            }
            // For testing
            for (Room room : rooms) {
                System.out.println(room);
            }
            return rooms;
        }
        catch (SQLException e) {
            return null;
        }
    }
    // Creates a list of patients from the database
    public List<Patient> getPatients() {
        try {
            String selectAll = "SELECT * FROM patient";
            PreparedStatement stmt = conn.prepareStatement(selectAll);
            ResultSet rsPatients = stmt.executeQuery();
            List<Patient> patients = new ArrayList<>();
            while(rsPatients.next()) {
                patients.add(new Patient(rsPatients.getInt("id"), rsPatients.getString("disease"), rsPatients.getInt("daysRequired"), rsPatients.getInt("daysLeftToLive")));
            }
            // For testing
            for (Patient patient : patients) {
                System.out.println(patient);
            }
            return patients;
        }
        catch (SQLException e) {
            return null;
        }
    }
    // TODO: Check if it actually works.
    // Will be used for loading the VIEW's schedule when ui is ready.
    // Inserts the schedule from the db into every room's schedule list.
    public void getSchedule(List<Room> rooms, List<Patient> patients) {
        try {
            String selectString = "SELECT * FROM schedule WHERE roomID = ?";
            PreparedStatement stmt = conn.prepareStatement(selectString);
            for (Room room : rooms) {
                stmt.setInt(1, room.getId());
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int day =  rs.getInt("day");
                    Patient p = patients.get(rs.getInt("patientID"));
                    List<Patient> patientList = room.getSchedule().getOrDefault(day, new ArrayList<>());
                    patientList.add(p);
                    room.getSchedule().put(day, patientList);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Deletes a room or patient from the database
    public int delete(Object object){
        try {
            PreparedStatement stmt;
            String deleteString;
            // Delete patient from db
            if (object instanceof Patient patient) {
                deleteString = "DELETE FROM patient WHERE id = ?";
                stmt = conn.prepareStatement(deleteString);
                stmt.setInt(1, patient.getId());
                stmt.executeUpdate();
                return 1;
            }
            // Delete room form db including its diseases list
            else if (object instanceof Room room) {
                // Deletes room
                deleteString = "DELETE FROM room WHERE id = ?";
                stmt = conn.prepareStatement(deleteString);
                stmt.setInt(1, room.getId());
                stmt.executeUpdate();
                // Deletes the diseases list
                deleteString = "DELETE FROM diseases WHERE roomID = ?";
                stmt = conn.prepareStatement(deleteString);
                stmt.setInt(1, room.getId());
                return 1;
            }
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
