package com.mycompany.gymmanagementsystem.dao;

import com.mycompany.gymmanagementsystem.config.DBConnection;
import com.mycompany.gymmanagementsystem.model.Equipment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class EquipmentDAO {

    public ObservableList<Equipment> getEquipmentList() {
        ObservableList<Equipment> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM equipment";
        try (Connection connect = DBConnection.connectDb(); PreparedStatement prepare = connect.prepareStatement(sql); ResultSet result = prepare.executeQuery()) {
            while (result.next()) {
                list.add(mapEquipment(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addEquipment(Equipment eq) {
        String sql = "INSERT INTO equipment (equipName, type, deliveryDate, quality, price) VALUES(?,?,?,?,?)";
        try (Connection connect = DBConnection.connectDb(); PreparedStatement prepare = connect.prepareStatement(sql)) {
            prepare.setString(1, eq.getEquipName());
            prepare.setString(2, eq.getType());
            prepare.setDate(3, eq.getDeliveryDate());
            prepare.setString(4, eq.getQuality());
            prepare.setDouble(5, eq.getPrice());
            prepare.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateEquipment(Equipment eq) {
        String sql = "UPDATE equipment SET equipName=?, type=?, deliveryDate=?, quality=?, price=? WHERE id=?";
        try (Connection connect = DBConnection.connectDb(); PreparedStatement prepare = connect.prepareStatement(sql)) {
            prepare.setString(1, eq.getEquipName());
            prepare.setString(2, eq.getType());
            prepare.setDate(3, eq.getDeliveryDate());
            prepare.setString(4, eq.getQuality());
            prepare.setDouble(5, eq.getPrice());
            prepare.setInt(6, eq.getId());
            prepare.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteEquipment(int id) {
        String sql = "DELETE FROM equipment WHERE id=?";
        try (Connection connect = DBConnection.connectDb(); PreparedStatement prepare = connect.prepareStatement(sql)) {
            prepare.setInt(1, id);
            prepare.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Tìm kiếm thiết bị theo nhóm cơ (Muscle Map)
    public ObservableList<Equipment> getEquipmentByMuscle(String muscleGroup) {
        ObservableList<Equipment> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM equipment WHERE type LIKE ? OR equipName LIKE ?";
        try (Connection connect = DBConnection.connectDb(); PreparedStatement prepare = connect.prepareStatement(sql)) {
            String key = "%" + muscleGroup + "%";
            prepare.setString(1, key);
            prepare.setString(2, key);
            ResultSet result = prepare.executeQuery();
            while (result.next()) {
                list.add(mapEquipment(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private Equipment mapEquipment(ResultSet result) throws SQLException {
        return new Equipment(
                result.getInt("id"),
                result.getString("equipName"),
                result.getString("type"),
                result.getDate("deliveryDate"),
                result.getString("quality"),
                result.getDouble("price")
        );
    }
}
