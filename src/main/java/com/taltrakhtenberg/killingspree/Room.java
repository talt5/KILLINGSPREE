package com.taltrakhtenberg.killingspree;

import java.util.*;

public class Room {
    int id;
    int capacity;
    List<String> diseases;
    TreeMap<Integer, List<Patient>> schedule;
    Map<Integer, Integer> capacityMap;

    public Room(int id, int capacity, List<String> diseases) {
        this.id = id;
        this.capacity = capacity;
        this.diseases = new ArrayList<>(diseases);
        schedule = new TreeMap<>();
        capacityMap = new TreeMap<>();

    }

    @Override
    public String toString() {
        return "Room id: " + id + " capacity: " + capacity + " diseases: " + diseases.toString();
    }
    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<String> getDiseases() {
        return diseases;
    }

    public TreeMap<Integer, List<Patient>> getSchedule() {
        return schedule;
    }

    public void setSchedule(TreeMap<Integer, List<Patient>> schedule) {
        this.schedule = schedule;
    }
}