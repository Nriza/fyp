package com.example.clinicsystem;

public class app {

    private String Date;
    private String Time;
    private String Username;

    public app(){
    }

    public app(String date, String time, String username) {
        Date = date;
        Time = time;
        Username = username;
    }

    public void setUsername(String username){
        Username = username;
    }

    public String getUsername(){
        return Username;
    }

    public void setDate(String date){
        Date = date;
    }

    public String getDate(){
        return Date;
    }

    public void setTime(String time){
        Time = time;
    }

    public String getTime(){
        return Time;
    }
}
