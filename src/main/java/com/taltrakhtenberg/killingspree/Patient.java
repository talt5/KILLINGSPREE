package com.taltrakhtenberg.killingspree;

public class Patient {
    String id;
    String disease;
    int urgency;
    int daysRequired;
    int daysLeftToLive;
    boolean canBeDeferred;
    boolean isDeferred;
//    int scheduledEntryDay;

    public Patient(String id, String disease, int urgency, int daysRequired, int daysLeftToLive) {
        this.id = id;
        this.disease = disease;
        this.urgency = urgency;
        this.daysRequired = daysRequired;
        this.daysLeftToLive = daysLeftToLive;
        this.isDeferred = false;
//        this.scheduledEntryDay = -1; // עדיין לא שובץ
    }

    public boolean GetCanBeDeferred() {
        return canBeDeferred;
    }

    public void defer() {
        this.daysLeftToLive *= 2;
        this.isDeferred = true;
    }
}