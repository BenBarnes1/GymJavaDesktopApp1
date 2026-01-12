package com.mycompany.gymmanagementsystem.model;

import java.sql.Date;

public class Member {

    private int id;
    private String memberId;
    private String name;
    private String address;
    private int phoneNum;
    private String gender;
    private String schedule;
    private Date startDate;
    private Date endDate;
    private Double price;
    private String status;

    public Member(int id, String memberId, String name, String address, int phoneNum, 
                  String gender, String schedule, Date startDate, Date endDate, 
                  Double price, String status) {
        this.id = id;
        this.memberId = memberId;
        this.name = name;
        this.address = address;
        this.phoneNum = phoneNum;
        this.gender = gender;
        this.schedule = schedule;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.status = status;
    }

    // Getters
    public int getId() { return id; }
    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public int getPhoneNum() { return phoneNum; }
    public String getGender() { return gender; }
    public String getSchedule() { return schedule; }
    public Date getStartDate() { return startDate; }
    public Date getEndDate() { return endDate; }
    public Double getPrice() { return price; }
    public String getStatus() { return status; }
}