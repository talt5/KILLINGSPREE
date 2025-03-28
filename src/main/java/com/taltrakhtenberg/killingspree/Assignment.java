package com.taltrakhtenberg.killingspree;

public class Assignment {
    Room room;
    int startDay;
    boolean dead;

    // Constructor for a room assignment.
    public Assignment(Room room, int startDay) {
        this.room = room;
        this.startDay = startDay;
        this.dead = false;
    }

    // Constructor for a dead assignment.
    public Assignment(boolean dead) {
        this.dead = dead;
        this.room = null;
        this.startDay = -1;
    }

    @Override
    public String toString() {
        if (dead) {
            return "dead";
        } else {
            return "Room " + room.id + " starting at day " + startDay;
        }
    }
    public Room getRoom() {
        return room;
    }
    public int getStartDay() {
        return startDay;
    }

    public boolean getDead() {
        return dead;
    }
}
