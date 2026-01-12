package com.mycompany.gymmanagementsystem.dao;

import com.mycompany.gymmanagementsystem.config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    public boolean login(String username, String password) {
        String sql = "SELECT * FROM admin WHERE username = ? and password = ?";
        try (Connection connect = DBConnection.connectDb();
             PreparedStatement prepare = connect.prepareStatement(sql)) {
            
            prepare.setString(1, username);
            prepare.setString(2, password);
            
            ResultSet result = prepare.executeQuery();
            return result.next(); // Trả về true nếu tìm thấy tài khoản
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void signup(String email, String username, String password) {
        String sql = "INSERT INTO admin (email, username, password) VALUES(?,?,?)";
        try (Connection connect = DBConnection.connectDb();
             PreparedStatement prepare = connect.prepareStatement(sql)) {
            
            prepare.setString(1, email);
            prepare.setString(2, username);
            prepare.setString(3, password);
            
            prepare.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}