package com.taltrakhtenberg.killingspree;

public class Patient {
    int id;
    String disease;
    int daysRequired;
    int daysLeftToLive;
    boolean canBeDeferred;
    boolean isDeferred;
    boolean toBeDeferred;
    int daysStaying;
    int scheduledEntryDay;

    public Patient(int id, String disease, int daysRequired, int daysLeftToLive) {
        this.id = id;
        this.disease = disease;
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

    public boolean GetIsDeferred() {
        return isDeferred;
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

    public void setDisease(String disease) {
        this.disease = disease;
    }



    public int getDaysRequired() {
        return daysRequired;
    }

    public void setDaysRequired(int daysRequired) {
        this.daysRequired = daysRequired;
    }

    public void setDaysLeftToLive(int daysLeftToLive) {
        this.daysLeftToLive = daysLeftToLive;
    }

    public boolean isCanBeDeferred() {
        return canBeDeferred;
    }

    public void setCanBeDeferred(boolean canBeDeferred) {
        this.canBeDeferred = canBeDeferred;
    }

    public boolean isDeferred() {
        return isDeferred;
    }

    public void setDeferred(boolean deferred) {
        isDeferred = deferred;
    }

    public boolean isToBeDeferred() {
        return toBeDeferred;
    }

    public void setToBeDeferred(boolean toBeDeferred) {
        this.toBeDeferred = toBeDeferred;
    }

    public int getDaysStaying() {
        return daysStaying;
    }

    public void setDaysStaying(int daysStaying) {
        this.daysStaying = daysStaying;
    }

    public int getScheduledEntryDay() {
        return scheduledEntryDay;
    }

    public void setScheduledEntryDay(int scheduledEntryDay) {
        this.scheduledEntryDay = scheduledEntryDay;
    }
}