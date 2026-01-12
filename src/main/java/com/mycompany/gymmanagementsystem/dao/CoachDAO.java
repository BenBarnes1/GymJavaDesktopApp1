package com.mycompany.gymmanagementsystem.dao;

import com.mycompany.gymmanagementsystem.config.DBConnection;
import com.mycompany.gymmanagementsystem.model.Coach;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class CoachDAO {

    public ObservableList<Coach> getCoachesList() {
        ObservableList<Coach> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM coach";
        try (Connection connect = DBConnection.connectDb(); PreparedStatement prepare = connect.prepareStatement(sql); ResultSet result = prepare.executeQuery()) {
            while (result.next()) {
                listData.add(mapCoach(result));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return listData;
    }

    public ObservableList<Coach> searchCoaches(String keyword) {
        ObservableList<Coach> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM coach WHERE coachId LIKE ? OR name LIKE ?";
        try (Connection connect = DBConnection.connectDb(); PreparedStatement prepare = connect.prepareStatement(sql)) {
            String searchKey = "%" + keyword + "%";
            prepare.setString(1, searchKey);
            prepare.setString(2, searchKey);
            ResultSet result = prepare.executeQuery();
            while (result.next()) {
                listData.add(mapCoach(result));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return listData;
    }

    public boolean exists(String coachId) {
        String sql = "SELECT coachId FROM coach WHERE coachId = ?";
        try (Connection connect = DBConnection.connectDb(); PreparedStatement prepare = connect.prepareStatement(sql)) {
            prepare.setString(1, coachId);
            ResultSet result = prepare.executeQuery();
            return result.next();
        } catch (Exception e) { return false; }
    }

    public void addCoach(Coach coach) {
        String sql = "INSERT INTO coach (coachId, name, address, gender, phoneNum, status) VALUES(?,?,?,?,?,?)";
        try (Connection connect = DBConnection.connectDb(); PreparedStatement prepare = connect.prepareStatement(sql)) {
            prepare.setString(1, coach.getCoachId());
            prepare.setString(2, coach.getName());
            prepare.setString(3, coach.getAddress());
            prepare.setString(4, coach.getGender());
            prepare.setInt(5, coach.getPhoneNum());
            prepare.setString(6, coach.getStatus());
            prepare.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void updateCoach(Coach coach) {
        String sql = "UPDATE coach SET name = ?, address = ?, gender = ?, phoneNum = ?, status = ? WHERE coachId = ?";
        try (Connection connect = DBConnection.connectDb(); PreparedStatement prepare = connect.prepareStatement(sql)) {
            prepare.setString(1, coach.getName());
            prepare.setString(2, coach.getAddress());
            prepare.setString(3, coach.getGender());
            prepare.setInt(4, coach.getPhoneNum());
            prepare.setString(5, coach.getStatus());
            prepare.setString(6, coach.getCoachId());
            prepare.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void deleteCoach(String coachId) {
        String sql = "DELETE FROM coach WHERE coachId = ?";
        try (Connection connect = DBConnection.connectDb(); PreparedStatement prepare = connect.prepareStatement(sql)) {
            prepare.setString(1, coachId);
            prepare.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private Coach mapCoach(ResultSet result) throws SQLException {
        return new Coach(
            result.getInt("id"),
            result.getString("coachId"),
            result.getString("name"),
            result.getString("address"),
            result.getString("gender"),
            result.getInt("phoneNum"),
            result.getString("status")
        );
    }
}