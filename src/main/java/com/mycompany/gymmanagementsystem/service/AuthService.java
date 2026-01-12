package com.mycompany.gymmanagementsystem.service;

import com.mycompany.gymmanagementsystem.dao.UserDAO;
import com.mycompany.gymmanagementsystem.util.Data;

public class AuthService {

    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public boolean login(String username, String password) throws Exception {
        if (username.isEmpty() || password.isEmpty()) {
            throw new Exception("Please fill all blank fields");
        }
        
        if (userDAO.login(username, password)) {
            Data.username = username; // Lưu session
            return true;
        } else {
            throw new Exception("Wrong Username or Password");
        }
    }

    public void signup(String email, String username, String password) throws Exception {
        if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            throw new Exception("Please fill all blank fields");
        }
        if (password.length() < 8) {
            throw new Exception("Password must be at least 8 characters");
        }
        
        // Gọi DAO để lưu vào DB
        userDAO.signup(email, username, password);
    }
}