package com.example.clinicsystem;

public class treatList
{
    String teethnum, type;
    treatList()
    {

    }
    public treatList(String teethnum, String type) {
        this.teethnum = teethnum;
        this.type = type;
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