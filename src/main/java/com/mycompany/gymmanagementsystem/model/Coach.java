package com.mycompany.gymmanagementsystem.model;

public class Coach {

    private int id;
    private String coachId;
    private String name;
    private String address;
    private String gender;
    private int phoneNum;
    private String status;

    public Coach(int id, String coachId, String name, String address, String gender, int phoneNum, String status) {
        this.id = id;
        this.coachId = coachId;
        this.name = name;
        this.address = address;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.status = status;
    }

    public int getId() { return id; }
    public String getCoachId() { return coachId; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getGender() { return gender; }
    public int getPhoneNum() { return phoneNum; }
    public String getStatus() { return status; }
}