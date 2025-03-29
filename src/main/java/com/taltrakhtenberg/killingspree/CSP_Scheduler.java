package com.taltrakhtenberg.killingspree;

import java.util.*;

public class CSP_Scheduler {
    List<Patient> patients; // maybe make it a linked list?
    List<Room> rooms;
    Map<String, Set<String>> diseaseIncompatibility;
    int planningHorizon;
    Map<Integer, List<List<Integer>>> occupancy;

    public CSP_Scheduler(List<Patient> patients, List<Room> rooms) {
        // Variable inits
        this.patients = patients;
        this.rooms = rooms;
        this.diseaseIncompatibility = new HashMap<>();
        diseaseIncompatibility.put("diseaseA", new HashSet<>(Arrays.asList("diseaseB")));
        diseaseIncompatibility.put("diseaseB", new HashSet<>(Arrays.asList("diseaseA")));
        planningHorizon = 0;
        occupancy = new HashMap<>();
    }

    public Map<Integer, Assignment> schedule() {
        // Update correct planning horizon
        for (Patient p : patients) {
            planningHorizon = Math.max(planningHorizon, p.daysLeftToLive);
        }
        for (Room room : rooms) {
            List<List<Integer>> occupancyForRoom = new ArrayList<>();
            for (int d = 0; d < planningHorizon; d++) {
                occupancyForRoom.add(new ArrayList<>());
            }
            occupancy.put(room.id, occupancyForRoom);
        }
        // Compute initial domains for each patient.
        Map<Integer, List<Assignment>> domains = new HashMap<>();
        for (Patient p : patients) {
            domains.put(p.id, computeDomain(p, rooms));
        }
        // Set of unassigned patient ids.
        Set<Integer> unassigned = new HashSet<>();
        for (Patient p : patients) {
            unassigned.add(p.id);
        }
        Map<Integer, Assignment> solution = forwardCheck(new HashMap<>(), domains, patients, rooms, unassigned);
        if (solution != null) {
            System.out.println("Schedule found:");
            List<Integer> deadPatients = new ArrayList<>();
            for (Patient p : patients) {
                Assignment a = solution.get(p.id);
                if (a.dead) {
                    deadPatients.add(p.id);
                } else {
                    System.out.println("Patient " + p.id + " is scheduled in " + a);
                }
            }
            if (!deadPatients.isEmpty()) {
                System.out.println("Dead patients: " + deadPatients);
            }
        } else {
            System.out.println("No valid schedule exists for the given set of patients and rooms.");
        }
        return solution;
    }

    private boolean isFeasible(Patient patient, Assignment assign, List<Patient> patients) {
        // Dead assignment is always allowed.
        if (assign.dead) return true;
        // Check that the room can cure the patient’s disease.
        if (!assign.room.diseases.contains(patient.disease)) {
            return false;
        }
        // Treatment must complete before patient runs out of days.
        if (assign.startDay > patient.daysLeftToLive) {
            return false;
        }
        // For each day in the treatment period, check capacity and incompatibility.
        for (int d = assign.startDay; d < assign.startDay + patient.daysRequired; d++) {
            if (d >= planningHorizon) {
                return true; // beyond planning horizon
            }
            List<Integer> scheduled = occupancy.get(assign.room.id).get(d);
            if (scheduled.size() >= assign.room.capacity) {
                return false; // capacity exceeded
            }
            // Check incompatibility with already scheduled patients.
            for (Integer otherId : scheduled) {
                Patient other = null;
                for (Patient p : patients) {
                    if (p.id == otherId) {
                        other = p;
                        break;
                    }
                }
                if (other != null) {
                    if (diseaseIncompatibility.getOrDefault(patient.disease, Collections.emptySet()).contains(other.disease) ||
                            diseaseIncompatibility.getOrDefault(other.disease, Collections.emptySet()).contains(patient.disease)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void addAssignmentToOccupancy(Patient patient, Assignment assign) {
        for (int d = assign.startDay; d < Math.min(assign.startDay + patient.daysRequired, planningHorizon); d++) {
            occupancy.get(assign.room.id).get(d).add(patient.id);
        }
    }

    private void removeAssignmentFromOccupancy(Patient patient, Assignment assign) {
        for (int d = assign.startDay; d < Math.min(assign.startDay + patient.daysRequired, planningHorizon); d++) {
            occupancy.get(assign.room.id).get(d).remove((Integer) patient.id);
        }
    }

    private List<Assignment> computeDomain(Patient patient, List<Room> rooms) {
        List<Assignment> domain = new ArrayList<>();
        // For each room that can treat the disease...
        for (Room room : rooms) {
            if (!room.diseases.contains(patient.disease)) continue;
            // startDay must be such that treatment completes by daysLeft.
            for (int startDay = 0; startDay <= patient.daysLeftToLive; startDay++) {
                // At initial state, occupancy is empty so these assignments are acceptable.
                domain.add(new Assignment(room, startDay));
            }
        }
        // Always include the dead assignment.
        domain.add(new Assignment(true));
        return domain;
    }

    private Map<Integer, List<Assignment>> cloneDomains(Map<Integer, List<Assignment>> domains) {
        Map<Integer, List<Assignment>> copy = new HashMap<>();
        for (Map.Entry<Integer, List<Assignment>> entry : domains.entrySet()) {
            copy.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return copy;
    }

    private Map<Integer, Assignment> forwardCheck(Map<Integer, Assignment> assignments,
                                                  Map<Integer, List<Assignment>> domains,
                                                  List<Patient> patients,
                                                  List<Room> rooms,
                                                  Set<Integer> unassigned) {
        // If no unassigned variables remain, return assignments.
        if (unassigned.isEmpty()) {
            return assignments;
        }
        // Select a patient with the smallest domain (MRV heuristic).
        int selectedId = -1;
        int minSize = Integer.MAX_VALUE;
        for (int pid : unassigned) {
            int size = domains.get(pid).size();
            if (size < minSize) {
                minSize = size;
                selectedId = pid;
            }
        }

        // Get the patient object.
        Patient patient = null;
        for (Patient p : patients) {
            if (p.id == selectedId) {
                patient = p;
                break;
            }
        }
        if (patient == null) return null; // Should not happen

        // Try each value in the domain of the selected patient.
        for (Assignment value : domains.get(selectedId)) {
            // Check if this value is feasible with current occupancy.
            if (!isFeasible(patient, value, patients)) continue;
            // Create a copy of the current assignments and domains.
            Map<Integer, Assignment> newAssignments = new HashMap<>(assignments);
            newAssignments.put(selectedId, value);

            // For non-dead assignments, update occupancy.
            boolean occupancyUpdated = false;
            if (!value.dead) {
                addAssignmentToOccupancy(patient, value);
                occupancyUpdated = true;
            }

            // Prepare a new domains map for forward checking.
            Map<Integer, List<Assignment>> newDomains = cloneDomains(domains);
            // Remove the selected patient from unassigned.
            Set<Integer> newUnassigned = new HashSet<>(unassigned);
            newUnassigned.remove(selectedId);

            // For each remaining unassigned patient, filter its domain.
            boolean failure = false;
            for (int pid : newUnassigned) {
                List<Assignment> filtered = new ArrayList<>();
                // Get the patient object.
                Patient other = null;
                for (Patient p : patients) {
                    if (p.id == pid) {
                        other = p;
                        break;
                    }
                }
                if (other == null) continue;
                // Filter assignments that are still feasible.
                for (Assignment a : newDomains.get(pid)) {
                    if (isFeasible(other, a, patients)) {
                        filtered.add(a);
                    }
                }
                newDomains.put(pid, filtered);
                // If the domain becomes empty, then we have a failure.
                if (filtered.isEmpty()) {
                    failure = true;
                    break;
                }
            }

            if (!failure) {
                // Recurse.
                Map<Integer, Assignment> result = forwardCheck(newAssignments, newDomains, patients, rooms, newUnassigned);
                if (result != null) {
                    return result;
                }
            }
            // Backtrack: revert occupancy update.
            if (occupancyUpdated) {
                removeAssignmentFromOccupancy(patient, value);
            }
        }
        return null;
    }
}