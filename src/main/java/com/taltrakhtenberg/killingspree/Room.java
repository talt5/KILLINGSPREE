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

    public boolean canAssign(Patient patient, int day) {
        return schedule.getOrDefault(day, new ArrayList<>()).size() < capacity && diseases.contains(patient.disease);
    }

    // Inserts a patient into the schedule in the given day
    public void assignPatient(Patient patient, int day) {
        schedule.putIfAbsent(day, new ArrayList<>());
        schedule.get(day).add(patient);
        patient.scheduledEntryDay = day;
    }

    public void cutPatientDays(Patient patient, int day) {
        schedule.get(day).remove(patient);
        patient.daysStaying /= 2;
        schedule.get(day).add(patient);
    }

    public void removePatient(Patient patient, int day) {
        if (schedule.containsKey(day)) {
            schedule.get(day).remove(patient);
            if (schedule.get(day).isEmpty()) {
                schedule.remove(day);
            }
        }
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