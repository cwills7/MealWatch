package com.wills.carl.mealwatch.model;

import org.joda.time.LocalDate;


public class Meal {

    String description;
    boolean dairy;
    boolean gluten;
    LocalDate date;
    long _id;

    public Meal(String description, boolean dairy, boolean gluten, LocalDate date){
        this.description = description;
        this.dairy = dairy;
        this.gluten = gluten;
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDairy() {
        return dairy;
    }

    public void setDairy(boolean dairy) {
        this.dairy = dairy;
    }

    public boolean isGluten() {
        return gluten;
    }

    public void setGluten(boolean gluten) {
        this.gluten = gluten;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getId() {return _id;}

    public void setId(long id) {this._id = id;}

}
