package com.taltrakhtenberg.killingspree;

public class Patient {
    int id;
    String disease;
    int daysRequired;
    int daysLeftToLive;

    public Patient(int id, String disease, int daysRequired, int daysLeftToLive) {
        this.id = id;
        this.disease = disease;
        this.daysRequired = daysRequired;
        this.daysLeftToLive = daysLeftToLive;
    }
    public Patient(Patient p) {
        this.id = p.id;
        this.disease = p.disease;
        this.daysRequired = p.daysRequired;
        this.daysLeftToLive = p.daysLeftToLive;
    }

    @Override
    public String toString() {
        return "Patient id: " + id + " disease: " + disease + " daysRequired: " + daysRequired + " daysLeftToLive: " + daysLeftToLive;
    }

    public int getDaysLeftToLive() {
        return daysLeftToLive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisease() {
        return disease;
    }

    public int getDaysRequired() {
        return daysRequired;
    }

}