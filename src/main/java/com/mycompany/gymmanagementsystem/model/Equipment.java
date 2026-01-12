package com.mycompany.gymmanagementsystem.model;

import java.sql.Date;

public class Equipment {
    private int id;
    private String equipName;
    private String type;
    private Date deliveryDate;
    private String quality;
    private Double price;

    public Equipment(int id, String equipName, String type, Date deliveryDate, String quality, Double price) {
        this.id = id;
        this.equipName = equipName;
        this.type = type;
        this.deliveryDate = deliveryDate;
        this.quality = quality;
        this.price = price;
    }

    public int getId() { return id; }
    public String getEquipName() { return equipName; }
    public String getType() { return type; }
    public Date getDeliveryDate() { return deliveryDate; }
    public String getQuality() { return quality; }
    public Double getPrice() { return price; }
}