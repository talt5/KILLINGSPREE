package com.taltrakhtenberg.killingspree;
/**
 * The {@code Patient} class represents a patient in the scheduling system.
 * Each patient has an ID, a disease they suffer from, the number of days required for treatment,
 * and the number of days left before a transfer to another hospital.
 */
public class Patient {
    /** The unique identifier of the patient. */
    private int id;
    /** The disease the patient is suffering from. */
    private String disease;
    /** The number of days required for the patient's treatment. */
    private int daysRequired;
    /** The number of days left before the patient needs to be transferred. */
    private int daysLeftToLive;

    /**
     * Constructs a new {@code Patient} with the specified attributes.
     *
     * @param id the unique identifier of the patient
     * @param disease the disease the patient is suffering from
     * @param daysRequired the number of days required for treatment
     * @param daysLeftToLive the number of days left before the patient dies if untreated
     */
    public Patient(int id, String disease, int daysRequired, int daysLeftToLive) {
        this.id = id;
        this.disease = disease;
        this.daysRequired = daysRequired;
        this.daysLeftToLive = daysLeftToLive;
    }

    /**
     * Copy constructor that creates a new {@code Patient} from an existing one.
     *
     * @param p the patient object to copy
     */
    public Patient(Patient p) {
        this.id = p.id;
        this.disease = p.disease;
        this.daysRequired = p.daysRequired;
        this.daysLeftToLive = p.daysLeftToLive;
    }

    /**
     * Returns a string representation of the {@code Patient}.
     *
     * @return a string containing the patient's details
     */
    @Override
    public String toString() {
        return "Patient id: " + id + " disease: " + disease + " daysRequired: " + daysRequired + " daysLeftToLive: " + daysLeftToLive;
    }

    /**
     * Gets the number of days left before the patient dies if untreated.
     *
     * @return the number of days left to live
     */
    public int getDaysLeftToLive() {
        return daysLeftToLive;
    }

    /**
     * Gets the unique identifier of the patient.
     *
     * @return the patient ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the patient.
     *
     * @param id the new patient ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the disease the patient is suffering from.
     *
     * @return the name of the disease
     */
    public String getDisease() {
        return disease;
    }

    /**
     * Gets the number of days required for the patient's treatment.
     *
     * @return the required treatment duration in days
     */
    public int getDaysRequired() {
        return daysRequired;
    }

}