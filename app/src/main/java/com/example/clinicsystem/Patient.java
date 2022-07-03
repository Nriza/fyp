package com.example.clinicsystem;

public class Patient {

    private String Email;
    private String Password;
    private String Username;
    private String Status;

    public Patient(){
    }

    public Patient(String email, String password, String username, String status) {
        Email = email;
        Password = password;
        Username = username;
        Status = status;
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

    public void setUsername(String username){
        Username = username;
    }

    public String getUsername(){
        return Username;
    }

    public void setStatus(String status){
        Status = status;
    }

    public String getStatus(){
        return Status;
    }

}
