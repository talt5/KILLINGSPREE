package com.taltrakhtenberg.killingspree;

import java.util.*;

public class Room {
    int id;
    int capacity;
    List<String> availableEquipment;
    TreeMap<Integer, List<Patient>> schedule;
    Map<Integer, Integer> capacityMap;

    public Room(int id, int capacity, List<String> availableEquipment) {
        this.id = id;
        this.capacity = capacity;
        this.availableEquipment = availableEquipment;
        schedule = new TreeMap<>();
        capacityMap = new TreeMap<>();

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<String> getDiseases() {
        return availableEquipment;
    }

    public void setAvailableEquipment(List<String> availableEquipment) {
        this.availableEquipment = availableEquipment;
    }

    public TreeMap<Integer, List<Patient>> getSchedule() {
        return schedule;
    }

    public void setSchedule(TreeMap<Integer, List<Patient>> schedule) {
        this.schedule = schedule;
    }

    public Map<Integer, Integer> getCapacityMap() {
        return capacityMap;
    }

    public void setCapacityMap(Map<Integer, Integer> capacityMap) {
        this.capacityMap = capacityMap;
    }
}