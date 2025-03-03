package com.taltrakhtenberg.killingspree;

import javafx.scene.shape.PathElement;

import java.util.*;

public class CSP_Scheduler {
    PriorityQueue<Patient> patientQueue;
    List<Patient> gpatients;
    List<Room> rooms;

    public CSP_Scheduler(List<Patient> patients, List<Room> rooms) {
        patientQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.daysLeftToLive));
        patientQueue.addAll(patients);
        this.rooms = rooms;
    }
    public void solve() {
        while (!patientQueue.isEmpty()) {
            Patient patient = patientQueue.poll();
            boolean scheduled = false;
            int minEntryDay = Integer.MAX_VALUE;
            Room minEntryRoom = null;
            Patient minEntryPatient = null;

            for (Room room : rooms) {
                if (room.schedule.getOrDefault(0, new ArrayList<Patient>()).size() < room.capacity) {
                    minEntryDay = 0;
                    minEntryRoom = room;
                    minEntryPatient = null;
                    break;
                }
                Patient entryPatient = findEarliestAvailableDay(room, patient);
                if (entryPatient == null) {
                    continue; // No room available him before death
                }
                int day = entryPatient.scheduledEntryDay + entryPatient.daysStaying;

                if (day < minEntryDay) {
                    minEntryDay = day;
                    minEntryRoom = room;
                    minEntryPatient = entryPatient;
                }
            }
            if (minEntryDay != -1) {
                patient.scheduledEntryDay = minEntryDay;
                patient.daysStaying = patient.daysRequired;
                Patient assigning = new Patient(patient);
                minEntryRoom.assignPatient(assigning, minEntryDay);
                if (minEntryPatient != null && minEntryPatient.toBeDeferred) {
                    Iterator<Patient> iterator = minEntryRoom.schedule.get(minEntryPatient.scheduledEntryDay).iterator();
                    while (iterator.hasNext()) {
                        Patient p = iterator.next();
                        if (p.id.equals(minEntryPatient.id)) {
                            iterator.remove();
                            p.isDeferred = true;
                            p.daysStaying = p.daysStaying/2;
                            minEntryRoom.schedule.get(minEntryPatient.scheduledEntryDay).add(p);
                            p.daysLeftToLive = p.daysLeftToLive - p.daysStaying;
                            p.daysRequired = p.daysRequired - p.daysRequired/2;
                            patientQueue.add(p);
                            break;
                        }
                    }

                }
                scheduled = true;
            }

            if (!scheduled) {
                System.out.println("⚠️ לא נמצא מקום לחולה: " + patient.id);
            }
        }

        printSchedule();
    }

    private Patient findEarliestAvailableDay(Room room, Patient patient) {
        List<Patient> currentPatients = new ArrayList<>();
        recursiveFindCurrentDwellers(room, 0, currentPatients);
        int minEntryDay = Integer.MAX_VALUE;
        int minUrgency = 0;
        Patient minPatient = null;
        for (Patient p : currentPatients) {
            if (!p.isDeferred && p.canBeDeferred && patient.daysLeftToLive >= p.scheduledEntryDay + p.daysStaying / 2 && p.scheduledEntryDay + p.daysStaying / 2 <= minEntryDay && p.daysLeftToLive - p.daysStaying > patient.daysLeftToLive && minUrgency < p.daysLeftToLive - p.daysStaying) {
                minEntryDay = p.scheduledEntryDay + p.daysStaying / 2;
                minUrgency = p.daysLeftToLive - p.daysStaying;
                minPatient = new Patient(p);
                minPatient.toBeDeferred = true;
                minPatient.daysStaying = minPatient.daysStaying/2;
            } else if (patient.daysLeftToLive >= p.scheduledEntryDay + p.daysStaying && p.scheduledEntryDay + p.daysStaying <= minEntryDay) {
                minEntryDay = p.scheduledEntryDay + p.daysStaying;
                minPatient = p;
            }
        }
        return minPatient;
    }

    private int recursiveFindOpening(Room room, int day) {
        List<Patient> entry = room.schedule.get(day);
        for (Patient patient : entry) {
            return recursiveFindOpening(room, day + patient.daysStaying);
        }
        return day;
    }

    private int recursiveFindCurrentDwellers(Room room, int day, List<Patient> patients) {
        List<Patient> entry = room.schedule.get(day);
        if (entry == null)
            return 1;
        for (Patient patient : entry) {
            int result = 0;
            result = recursiveFindCurrentDwellers(room, day + patient.daysStaying, patients);
            if (result == 1) {
                patients.add(patient);
            }
        }
        return 0;
    }

    public void printSchedule() {
        for (Room room : rooms) {
            System.out.println("🛏 חדר " + room.id);
            for (Map.Entry<Integer, List<Patient>> entry : room.schedule.entrySet()) {
                int day = entry.getKey();
                List<Patient> assignedPatients = entry.getValue();
                System.out.print("יום " + day + ": ");
                for (Patient patient : assignedPatients) {
                    System.out.print("[" + patient.id + "] ");
                }
                System.out.println();
            }
        }
    }
}