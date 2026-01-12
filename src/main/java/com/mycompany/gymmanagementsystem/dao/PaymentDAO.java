package com.mycompany.gymmanagementsystem.dao;

import com.mycompany.gymmanagementsystem.config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PaymentDAO {

    // Lấy giá mặc định từ bảng settings
    public double getDefaultPrice() {
        String sql = "SELECT config_value FROM settings WHERE config_key = 'default_price'";
        try (Connection connect = DBConnection.connectDb(); PreparedStatement prepare = connect.prepareStatement(sql); ResultSet result = prepare.executeQuery()) {

            if (result.next()) {
                return Double.parseDouble(result.getString("config_value"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 100000; // Giá dự phòng nếu DB lỗi
    }

    // Cập nhật giá mặc định mới
    public void updateDefaultPrice(double newPrice) {
        String sql = "UPDATE settings SET config_value = ? WHERE config_key = 'default_price'";
        try (Connection connect = DBConnection.connectDb(); PreparedStatement prepare = connect.prepareStatement(sql)) {

            prepare.setString(1, String.valueOf(newPrice));
            prepare.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
