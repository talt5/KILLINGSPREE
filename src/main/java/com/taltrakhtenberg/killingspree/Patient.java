package com.taltrakhtenberg.killingspree;

public class Patient {
    String id;
    String disease;
    int urgency;
    int daysRequired;
    int daysLeftToLive;
    boolean canBeDeferred;
    boolean isDeferred;
    boolean toBeDeferred;
    int daysStaying;
    int scheduledEntryDay;

    public Patient(String id, String disease, int urgency, int daysRequired, int daysLeftToLive) {
        this.id = id;
        this.disease = disease;
        this.urgency = urgency;
        this.daysRequired = daysRequired;
        this.daysLeftToLive = daysLeftToLive;
        this.canBeDeferred = true;
        this.isDeferred = false;
        this.toBeDeferred = false;
        this.daysStaying = 0;
    }
    public Patient(Patient p) {
        this.id = p.id;
        this.disease = p.disease;
        this.urgency = p.urgency;
        this.daysRequired = p.daysRequired;
        this.daysLeftToLive = p.daysLeftToLive;
        this.canBeDeferred = p.canBeDeferred;
        this.isDeferred = p.isDeferred;
        this.toBeDeferred = p.toBeDeferred;
        this.daysStaying = p.daysStaying;
        this.scheduledEntryDay = p.scheduledEntryDay;
    }

    public boolean GetCanBeDeferred() {
        return canBeDeferred;
    }

    public void defer() {
        this.daysLeftToLive *= 2;
        this.isDeferred = true;
    }
}