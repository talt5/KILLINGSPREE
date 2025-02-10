package com.taltrakhtenberg.killingspree;

import java.util.*;

public class Room {
    String id;
    int capacity;
    List<String> availableEquipment;
    TreeMap<Integer, List<Patient>> schedule;

    public Room(String id, int capacity, List<String> availableEquipment) {
        this.id = id;
        this.capacity = capacity;
        this.availableEquipment = availableEquipment;
        this.schedule = new TreeMap<>();
    }

    public boolean canAssign(Patient patient, int day) {
        return schedule.getOrDefault(day, new ArrayList<>()).size() < capacity && availableEquipment.contains(patient.disease);
    }

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
}