package com.example.qrscannertest;
public class Event {
    String id;
    String eventName;
    String department;
    String faculty;
    String venue;
    String points;
    String date;
    String time;

    public Event() {

    }

    public Event(String id, String eventName, String department, String venue, String faculty, String points, String date, String time) {
        this.id = id;
        this.eventName = eventName;
        this.department = department;
        this.faculty = faculty;
        this.points = points;
        this.date = date;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }


    public String getVenue() { return venue; }

    public void setVenue(String venue) { this.venue = venue; }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}


