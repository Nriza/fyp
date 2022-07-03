package com.example.admin;

public class Doctor {

    private String Email;
    private String Password;
    private String Username;
    private String Status;

    public Doctor(){
    }

    public Doctor(String email, String password, String name, String status) {
        Email = email;
        Password = password;
        Username = name;
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

    public void setUsername(String name){
        Username = name;
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

