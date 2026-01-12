package com.mycompany.gymmanagementsystem.model;

public class User {
    private int id;
    private String email;
    private String username;
    private String password;

    // Constructor
    public User(int id, String email, String username, String password) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    // Getters & Setters
    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}