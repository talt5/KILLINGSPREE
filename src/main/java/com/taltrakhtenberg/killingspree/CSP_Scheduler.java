package com.taltrakhtenberg.killingspree;

import java.util.*;
/**
 * The CSP_Scheduler class schedules patients into rooms over a planning horizon
 * using a constraint satisfaction problem (CSP) approach with forward checking.
 * It considers factors such as room capacity, disease compatibility, and treatment deadlines.
 *
 * <p>Each patient is assigned to a room with a specific start day for treatment,
 * or marked as dead if no valid schedule exists. The scheduling process computes
 * the domains of possible assignments for each patient, then uses forward checking
 * with the Minimum Remaining Values (MRV) heuristic to find a valid schedule.</p>
 *
 * <p>Note: The Patient, Room, and Assignment classes must be defined elsewhere
 * and are expected to provide fields such as {@code id}, {@code disease},
 * {@code daysLeftToLive}, {@code daysRequired}, etc.</p>
 */
public class CSP_Scheduler {
    /**
     * List of patients to be scheduled.
     */
    List<Patient> patients;
    /**
     * List of available rooms.
     */
    List<Room> rooms;
    /**
     * Map containing disease incompatibility relationships.
     * The key is a disease and the value is a set of diseases that are incompatible with it.
     */
    Map<String, Set<String>> diseaseIncompatibility;
    /**
     * The planning horizon, defined as the maximum number of days left among all patients.
     */
    int planningHorizon;
    /**
     * Occupancy map which keeps track of room assignments per day.
     * The key is a room id and the value is a list (indexed by day) of patient ids scheduled on that day.
     */
    Map<Integer, List<List<Integer>>> occupancy;

    /**
     * Constructs a CSP_Scheduler with the given patients and rooms.
     *
     * @param patients the list of patients to be scheduled
     * @param rooms the list of available rooms
     */
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

    /**
     * Schedules patients into rooms based on their treatment requirements and constraints.
     * <p>
     * The method calculates the planning horizon, initializes occupancy for each room,
     * computes the domain of assignments for each patient, and applies forward checking
     * to generate a schedule.
     * </p>
     *
     * @return a map of patient id to their corresponding Assignment if a valid schedule is found;
     *         otherwise, returns null.
     */
    public Map<Integer, Assignment> schedule() {
        // Update correct planning horizon
        for (Patient p : patients) {
            planningHorizon = Math.max(planningHorizon, p.getDaysLeftToLive());
        }
        for (Room room : rooms) {
            List<List<Integer>> occupancyForRoom = new ArrayList<>();
            for (int d = 0; d < planningHorizon; d++) {
                occupancyForRoom.add(new ArrayList<>());
            }
            occupancy.put(room.getId(), occupancyForRoom);
        }
        // Compute initial domains for each patient.
        Map<Integer, List<Assignment>> domains = new HashMap<>();
        for (Patient p : patients) {
            domains.put(p.getId(), computeDomain(p, rooms));
        }
        // Set of unassigned patient ids.
        Set<Integer> unassigned = new HashSet<>();
        for (Patient p : patients) {
            unassigned.add(p.getId());
        }
        Map<Integer, Assignment> solution = forwardCheck(new HashMap<>(), domains, patients, rooms, unassigned);
        if (solution != null) {
            System.out.println("Schedule found:");
            List<Integer> deadPatients = new ArrayList<>();
            for (Patient p : patients) {
                Assignment a = solution.get(p.getId());
                if (a.getDead()) {
                    deadPatients.add(p.getId());
                } else {
                    System.out.println("Patient " + p.getId() + " is scheduled in " + a);
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

    /**
     * Checks whether a given assignment for a patient is feasible with respect to
     * room capabilities, treatment deadlines, occupancy, and disease incompatibility.
     *
     * @param patient the patient for which the assignment is to be checked
     * @param assign the proposed assignment
     * @param patients the list of all patients for compatibility checks
     * @return true if the assignment is feasible; false otherwise
     */
    private boolean isFeasible(Patient patient, Assignment assign, List<Patient> patients) {
        // Dead assignment is always allowed.
        if (assign.getDead()) return true;
        // Check that the room can cure the patient’s disease.
        if (!assign.getRoom().getDiseases().contains(patient.getDisease())) {
            return false;
        }
        // Treatment must complete before patient runs out of days.
        if (assign.getStartDay() > patient.getDaysLeftToLive()) {
            return false;
        }
        // For each day in the treatment period, check capacity and incompatibility.
        for (int d = assign.getStartDay(); d < assign.getStartDay() + patient.getDaysRequired(); d++) {
            if (d >= planningHorizon) {
                return true; // beyond planning horizon
            }
            List<Integer> scheduled = occupancy.get(assign.getRoom().getId()).get(d);
            if (scheduled.size() >= assign.getRoom().getCapacity()) {
                return false; // capacity exceeded
            }
            // Check incompatibility with already scheduled patients.
            for (Integer otherId : scheduled) {
                Patient other = patients.stream().filter(p -> p.getId() == otherId).findFirst().orElse(null);
                if (other != null) {
                    if (diseaseIncompatibility.getOrDefault(patient.getDisease(), Collections.emptySet()).contains(other.getDisease()) ||
                            diseaseIncompatibility.getOrDefault(other.getDisease(), Collections.emptySet()).contains(patient.getDisease())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Adds the given patient's assignment to the occupancy schedule.
     *
     * @param patient the patient to be scheduled
     * @param assign the assignment containing room and start day information
     */
    private void addAssignmentToOccupancy(Patient patient, Assignment assign) {
        for (int d = assign.getStartDay(); d < Math.min(assign.getStartDay() + patient.getDaysRequired(), planningHorizon); d++) {
            occupancy.get(assign.getRoom().getId()).get(d).add(patient.getId());
        }
    }

    /**
     * Removes the given patient's assignment from the occupancy schedule.
     *
     * @param patient the patient whose assignment is to be removed
     * @param assign the assignment containing room and start day information
     */
    private void removeAssignmentFromOccupancy(Patient patient, Assignment assign) {
        for (int d = assign.getStartDay(); d < Math.min(assign.getStartDay() + patient.getDaysRequired(), planningHorizon); d++) {
            occupancy.get(assign.getRoom().getId()).get(d).remove((Integer) patient.getId());
        }
    }

    /**
     * Computes the domain of possible assignments for a given patient.
     * <p>
     * For each room capable of treating the patient's disease, possible assignments are generated
     * with start days that allow treatment to complete before the patient runs out of days.
     * Additionally, a dead assignment is always included.
     * </p>
     *
     * @param patient the patient for whom the domain is to be computed
     * @param rooms the list of available rooms
     * @return a list of possible Assignments for the patient
     */
    private List<Assignment> computeDomain(Patient patient, List<Room> rooms) {
        List<Assignment> domain = new ArrayList<>();
        // For each room that can treat the disease...
        for (Room room : rooms) {
            if (room.getDiseases().contains(patient.getDisease())) {
                // startDay must be such that treatment completes by daysLeft.
                for (int startDay = 0; startDay <= patient.getDaysLeftToLive(); startDay++) {
                    // At initial state, occupancy is empty so these assignments are acceptable.
                    domain.add(new Assignment(room, startDay));
                }
            }
        }
        // Always include the dead assignment.
        domain.add(new Assignment(true));
        return domain;
    }

    /**
     * Creates a deep copy of the current domains for each patient.
     *
     * @param domains the current map of patient id to list of assignments
     * @return a cloned map of the domains for forward checking
     */
    private Map<Integer, List<Assignment>> cloneDomains(Map<Integer, List<Assignment>> domains) {
        Map<Integer, List<Assignment>> copy = new HashMap<>();
        for (Map.Entry<Integer, List<Assignment>> entry : domains.entrySet()) {
            copy.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return copy;
    }

    /**
     * Applies forward checking to assign patients to rooms.
     * <p>
     * This recursive method selects an unassigned patient (using the Minimum Remaining Values heuristic),
     * checks feasible assignments, updates occupancy and domains, and recurses until either a complete
     * assignment is found or failure occurs, prompting backtracking.
     * </p>
     *
     * @param assignments current assignments of patients
     * @param domains current domains of possible assignments for each patient
     * @param patients the list of all patients
     * @param rooms the list of available rooms
     * @param unassigned the set of unassigned patient ids
     * @return a map of patient id to Assignment if a valid schedule is found; otherwise, null
     */
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
        int finalSelectedId = selectedId;
        Patient patient = patients.stream().filter(p -> p.getId() == finalSelectedId).findFirst().orElse(null);
        if (patient == null) return null; // Should not happen

        // Try each value in the domain of the selected patient.
        for (Assignment value : domains.get(selectedId)) {
            // Check if this value is feasible with current occupancy.
            if (isFeasible(patient, value, patients)) {
                // Create a copy of the current assignments and domains.
                Map<Integer, Assignment> newAssignments = new HashMap<>(assignments);
                newAssignments.put(selectedId, value);

                // For non-dead assignments, update occupancy.
                boolean occupancyUpdated = false;
                if (!value.getDead()) {
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
                    Patient other = patients.stream().filter(p -> p.getId() == pid).findFirst().orElse(null);
                    if (other != null) {
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
        }
        return null;
    }
}