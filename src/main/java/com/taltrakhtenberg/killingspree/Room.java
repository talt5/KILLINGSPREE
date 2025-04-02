package com.taltrakhtenberg.killingspree;

import java.util.*;

/**
 * Represents a hospital room that can accommodate patients.
 * Each room has an ID, a capacity (number of patients it can hold),
 * and a list of diseases it can treat.
 */
public class Room {
    /** The unique identifier for the room. */
    int id;

    /** The maximum number of patients the room can accommodate. */
    int capacity;

    /** A list of diseases that can be treated in this room. */
    List<String> diseases;

    /**
     * Constructs a Room object with the given ID, capacity, and list of diseases it can treat.
     *
     * @param id The unique identifier for the room.
     * @param capacity The maximum number of patients the room can hold.
     * @param diseases A list of diseases that can be treated in this room.
     */
    public Room(int id, int capacity, List<String> diseases) {
        this.id = id;
        this.capacity = capacity;
        this.diseases = new ArrayList<>(diseases);
    }

    /**
     * Returns a string representation of the room.
     *
     * @return A string containing the room ID, capacity, and the diseases it can treat.
     */
    @Override
    public String toString() {
        return "Room id: " + id + " capacity: " + capacity + " diseases: " + diseases.toString();
    }

    /**
     * Gets the unique identifier of the room.
     *
     * @return The room ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the maximum number of patients the room can accommodate.
     *
     * @return The room capacity.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Gets the list of diseases that can be treated in this room.
     *
     * @return A list of diseases the room can handle.
     */
    public List<String> getDiseases() {
        return diseases;
    }
}
