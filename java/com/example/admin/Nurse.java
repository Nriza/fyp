package com.example.admin;

public class Nurse {

    private String Email;
    private String Password;
    private String Username;
    private String Status;

    public Nurse(){
    }

    public Nurse(String email, String password, String name, String nurse) {
        Email = email;
        Password = password;
        Username = name;
        Status = nurse;
    }

    public void setEmail(String email){
        Email = email;
    }

    public String getEmail(){
        return Email;
    }

    public void setPassword(String password){
        Password = password;
    }

    public String getPassword(){
        return Password;
    }

    public void setUsername(String name){
        Username = name;
    }

    public String getUsername(){
        return Username;
    }

    public void setStatus(String nurse){
        Status = nurse;
    }

    public String getStatus(){
        return Status;
    }
}
