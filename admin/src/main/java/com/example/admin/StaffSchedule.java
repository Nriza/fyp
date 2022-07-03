package com.example.admin;

public class StaffSchedule {
    String username, status, day1, day2;

    public StaffSchedule(String username, String status, String day1, String day2) {
        this.username = username;
        this.status = status;
        this.day1 = day1;
        this.day2 = day2;
    }

    public StaffSchedule() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDay1() {
        return day1;
    }

    public void setDay1(String day1) {
        this.day1 = day1;
    }

    public String getDay2() {
        return day2;
    }

    public void setDay2(String day2) {
        this.day2 = day2;
    }
}
