package com.wills.carl.mealwatch.model;

import org.joda.time.LocalDate;


public class UpsetEvent {

    LocalDate date;
    int numGluten, numDairy;

    public UpsetEvent(LocalDate date, int numDairy, int numGluten){
        this.date = date;
        this.numDairy = numDairy;
        this.numGluten = numGluten;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getNumGluten() {
        return numGluten;
    }

    public void setNumGluten(int numGluten) {
        this.numGluten = numGluten;
    }

    public int getNumDairy() {
        return numDairy;
    }

    public void setNumDairy(int numDairy) {
        this.numDairy = numDairy;
    }

}
