package com.taltrakhtenberg.killingspree;

import java.sql.*;
import java.util.*;

/**
 * The {@code DBmanager} class manages database operations including creating tables,
 * inserting, retrieving, and deleting records related to patients and rooms.
 */
public class DBmanager {
    /** Database connection object. */
    private Connection conn;

    /**
     * Constructs a {@code DBmanager} and initializes the database.
     *
     * @param filename the name of the SQLite database file
     */
    public DBmanager(String filename) {
        initDB(filename);
    }

    /**
     * Initializes the database by creating necessary tables if they do not exist.
     *
     * @param filename the name of the SQLite database file
     */
    public void initDB(String filename) {
        String url = "jdbc:sqlite:" + filename;
        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                Statement stmt = conn.createStatement();

                String creationString = "CREATE TABLE IF NOT EXISTS patient ("
                        + " id INTEGER PRIMARY KEY,"
                        + " disease TEXT NOT NULL,"
                        + " daysRequired INTEGER NOT NULL,"
                        + " daysLeftToLive INTEGER NOT NULL"
                        + ");";
                stmt.execute(creationString);

                creationString = "CREATE TABLE IF NOT EXISTS room ("
                        + " id INTEGER PRIMARY KEY,"
                        + " capacity INTEGER NOT NULL"
                        + ");";
                stmt.execute(creationString);

                creationString = "CREATE TABLE IF NOT EXISTS diseases ("
                        + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + " roomID INTEGER NOT NULL,"
                        + " disease TEXT NOT NULL,"
                        + " FOREIGN KEY(roomID) REFERENCES room(id)"
                        + ");";
                stmt.execute(creationString);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Inserts a patient or room into the database.
     *
     * @param object the {@code Patient} or {@code Room} object to insert
     * @return 1 if successful, 0 if the object is invalid, -1 if an error occurs
     */
    public int insert(Object object) {
        String insertString;
        PreparedStatement stmt;
        try {
            if (object instanceof Patient patient) {
                insertString = "INSERT OR REPLACE INTO patient (id, disease, daysRequired, daysLeftToLive) VALUES (?, ?, ?, ?);";
                stmt = conn.prepareStatement(insertString);
                stmt.setInt(1, patient.getId());
                stmt.setString(2, patient.getDisease());
                stmt.setInt(3, patient.getDaysRequired());
                stmt.setInt(4, patient.getDaysLeftToLive());
                stmt.executeUpdate();
            } else if (object instanceof Room room) {
                insertString = "INSERT OR REPLACE INTO room (id, capacity) VALUES (?, ?);";
                stmt = conn.prepareStatement(insertString);
                stmt.setInt(1, room.getId());
                stmt.setInt(2, room.getCapacity());
                stmt.executeUpdate();

                insertString = "INSERT OR REPLACE INTO diseases (roomID, disease) VALUES (?, ?)";
                stmt = conn.prepareStatement(insertString);
                for (String disease : room.getDiseases()) {
                    stmt.setInt(1, room.getId());
                    stmt.setString(2, disease);
                    stmt.executeUpdate();
                }
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    /**
     * Retrieves all rooms from the database.
     *
     * @return a list of {@code Room} objects, or {@code null} if an error occurs
     */
    public List<Room> getRooms() {
        try {
            String selectAll = "SELECT * FROM room";
            PreparedStatement stmt = conn.prepareStatement(selectAll);
            ResultSet rsRooms = stmt.executeQuery();
            List<Room> rooms = new ArrayList<>();

            while (rsRooms.next()) {
                int id = rsRooms.getInt("id");
                stmt = conn.prepareStatement("SELECT disease FROM diseases WHERE roomid = ?");
                stmt.setInt(1, id);
                ResultSet rsDiseases = stmt.executeQuery();
                List<String> diseases = new ArrayList<>();
                while (rsDiseases.next()) {
                    diseases.add(rsDiseases.getString("disease"));
                }
                rooms.add(new Room(id, rsRooms.getInt("capacity"), diseases));
            }
            return rooms;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Retrieves all patients from the database.
     *
     * @return a list of {@code Patient} objects, or {@code null} if an error occurs
     */
    public List<Patient> getPatients() {
        try {
            String selectAll = "SELECT * FROM patient";
            PreparedStatement stmt = conn.prepareStatement(selectAll);
            ResultSet rsPatients = stmt.executeQuery();
            List<Patient> patients = new ArrayList<>();
            while (rsPatients.next()) {
                patients.add(new Patient(rsPatients.getInt("id"), rsPatients.getString("disease"), rsPatients.getInt("daysRequired"), rsPatients.getInt("daysLeftToLive")));
            }
            return patients;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Deletes a patient or room from the database.
     *
     * @param object the {@code Patient} or {@code Room} object to delete
     * @return 1 if successful, 0 if the object is invalid, -1 if an error occurs
     */
    public int delete(Object object) {
        try {
            PreparedStatement stmt;
            String deleteString;
            if (object instanceof Patient patient) {
                deleteString = "DELETE FROM patient WHERE id = ?";
                stmt = conn.prepareStatement(deleteString);
                stmt.setInt(1, patient.getId());
                stmt.executeUpdate();
                return 1;
            } else if (object instanceof Room room) {
                deleteString = "DELETE FROM room WHERE id = ?";
                stmt = conn.prepareStatement(deleteString);
                stmt.setInt(1, room.getId());
                stmt.executeUpdate();
                deleteString = "DELETE FROM diseases WHERE roomID = ?";
                stmt = conn.prepareStatement(deleteString);
                stmt.setInt(1, room.getId());
                return 1;
            }
            return 0;
        } catch (SQLException e) {
            return -1;
        }
    }

    /**
     * Deletes all records from the database.
     */
    public void deleteAll() {
        try {
            String deleteString = "DELETE FROM room";
            conn.prepareStatement(deleteString).executeUpdate();
            deleteString = "DELETE FROM patient";
            conn.prepareStatement(deleteString).executeUpdate();
            deleteString = "DELETE FROM diseases";
            conn.prepareStatement(deleteString).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

