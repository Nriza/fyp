package com.example.admin;

public class treatList
{
    String teethnum, type, username, status;

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    treatList()
    {

    }
    public treatList(String teethnum, String type, String username, String status) {
        this.teethnum = teethnum;
        this.type = type;
        this.username = username;
        this.status = status;
    }

    public String getTeethnum() {
        return teethnum;
    }

    public void setTeethnum(String teethnum) {
        this.teethnum = teethnum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}