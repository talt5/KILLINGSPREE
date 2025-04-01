package com.taltrakhtenberg.killingspree;

/**
 * The {@code Assignment} class represents the assignment of a patient to a room for treatment.
 * It includes the room assigned, the starting day of treatment, and whether the assignment
 * is considered "dead" (i.e., the patient is not assigned a room and will not receive treatment).
 */
public class Assignment {
    /** The room assigned to the patient. */
    Room room;

    /** The day on which the treatment starts. */
    int startDay;

    /** Indicates whether the assignment is "dead" (i.e., no treatment assigned). */
    boolean dead;

    /**
     * Constructs an {@code Assignment} with a specified room and start day.
     *
     * @param room the room assigned for treatment
     * @param startDay the day the treatment starts
     */
    public Assignment(Room room, int startDay) {
        this.room = room;
        this.startDay = startDay;
        this.dead = false;
    }

    /**
     * Constructs an assignment marked as "dead" (i.e., no treatment assigned).
     *
     * @param dead a boolean indicating that no room is assigned
     */
    public Assignment(boolean dead) {
        this.dead = dead;
        this.room = null;
        this.startDay = -1;
    }

    /**
     * Returns a string representation of the assignment.
     *
     * @return a string describing the assignment, either a room and start day or "dead" if no treatment is assigned
     */
    @Override
    public String toString() {
        if (dead) {
            return "dead";
        } else {
            return "Room " + room.id + " starting at day " + startDay;
        }
    }

    /**
     * Gets the room assigned to the patient.
     *
     * @return the assigned room, or {@code null} if the assignment is "dead"
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Gets the start day of the treatment.
     *
     * @return the day the treatment starts, or -1 if the assignment is "dead"
     */
    public int getStartDay() {
        return startDay;
    }

    /**
     * Checks if the assignment is "dead" (i.e., the patient is not receiving treatment).
     *
     * @return {@code true} if the assignment is "dead", {@code false} otherwise
     */
    public boolean getDead() {
        return dead;
    }
}