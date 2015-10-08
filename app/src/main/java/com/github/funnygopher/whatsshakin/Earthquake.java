package com.github.funnygopher.whatsshakin;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

import android.location.Location;

public class Earthquake {
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

    public String getDateString() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        String monthString = month < 10 ? "0" + month : Integer.toString(month);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dayString = day < 10 ? "0" + day : Integer.toString(day);
        String date = year + "-" + monthString + "-" + dayString;

        // Get the hour
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String hourString;
        String ampm = "am";
        if(hour > 12) {
            hour -= 12;
            ampm = "pm";
        }
        if(hour == 0) {
            hour = 12;
        }
        hourString = hour < 10 ? "0" + hour : Integer.toString(hour);

        // Get the minute
        int minute = calendar.get(Calendar.MINUTE);
        String minuteString = minute < 10 ? "0" + minute : Integer.toString(minute);

        String time = hour + ":" + minuteString + " " + ampm;

        return date + " @ " + time;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public String getMagnitudeString() {
        return "Magnitude: " + Double.toString(magnitude);
    }
}