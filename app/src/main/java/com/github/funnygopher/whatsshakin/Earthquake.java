package com.github.funnygopher.whatsshakin;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

import android.location.Location;

public class Earthquake {
    private Long _id;
    private String title;
    private Date date;
    private double latitude;
    private double longitude;
    private double magnitude;

    public Earthquake(String title, Date date, double magnitude) {
        this.title = title;
        this.date = date;
        this.magnitude = magnitude;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public double getMagnitude() {
        return magnitude;
    }
}