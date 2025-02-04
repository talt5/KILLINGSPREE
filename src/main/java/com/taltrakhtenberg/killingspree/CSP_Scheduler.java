package com.taltrakhtenberg.killingspree;

import java.util.*;

public class CSP_Scheduler {
    PriorityQueue<Patient> patientQueue;
    List<Patient> gpatients;
    List<Room> rooms;

    public CSP_Scheduler(List<Patient> patients, List<Room> rooms) {
        patientQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.daysLeftToLive));
        patientQueue.addAll(patients);
//        this.patients = new ArrayList<>(patients);
        this.rooms = rooms;
    }

    public void solve() {
//        patients.sort(Comparator.comparingInt(p -> p.urgency)); // מיון לפי דחיפות

        while (!patientQueue.isEmpty()) {
            Patient patient = patientQueue.poll();
            boolean scheduled = false;

            for (Room room : rooms) {
                int entryDay = findEarliestAvailableDay(room, patient);

                if (entryDay != -1) {
                    room.assignPatient(patient, entryDay);
                    scheduled = true;
                    break;
                }
            }

            if (!scheduled) {
                System.out.println("⚠️ לא נמצא מקום לחולה: " + patient.id);
            }
        }

        printSchedule();
    }

    private int findEarliestAvailableDay(Room room, Patient patient) {
        for (int day = 0; day <= 100; day++) { // חיפוש עד 100 ימים קדימה
            if (room.canAssign(patient, day)) {
                return day;
            }

            // בדיקה: האם נוכל להוציא חולה **בעתיד** ולפנות מקום לחולה זה?
            for (Map.Entry<Integer, List<Patient>> entry : room.schedule.entrySet()) {
                int scheduledDay = entry.getKey();
                List<Patient> assignedPatients = entry.getValue();

                for (Patient assigned : assignedPatients) {
                    int releaseDay = scheduledDay + assigned.daysRequired;
                    if (day >= releaseDay && day < patient.daysLeftToLive) {
                        room.assignPatient(patient, scheduledDay);
                        return scheduledDay;
                    }
                    else if (assigned.GetCanBeDeferred() && assigned.daysRequired - day < assigned.daysRequired/2) {
                        assigned.defer();
                        room.removePatient(assigned, scheduledDay);
                        room.assignPatient(patient, scheduledDay);
                        return scheduledDay;
                    } else if (day >= patient.daysLeftToLive) {
                        return -1;
                    }
                }
            }
        }
        return -1; // לא נמצא יום מתאים
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