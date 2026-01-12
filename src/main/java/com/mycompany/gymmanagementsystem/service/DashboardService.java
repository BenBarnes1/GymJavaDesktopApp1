package com.mycompany.gymmanagementsystem.service;

import com.mycompany.gymmanagementsystem.config.DBConnection;
import javafx.scene.chart.XYChart;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class DashboardService {

    public int getActiveMembersCount() {
        int count = 0;
        String sql = "SELECT COUNT(id) FROM member WHERE status = 'Paid'";
        try (Connection connect = DBConnection.connectDb(); PreparedStatement prepare = connect.prepareStatement(sql); ResultSet result = prepare.executeQuery()) {
            if (result.next()) {
                count = result.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public int getCoachesCount() {
        int count = 0;
        String sql = "SELECT COUNT(id) FROM coach WHERE status = 'Active'";
        try (Connection connect = DBConnection.connectDb(); PreparedStatement prepare = connect.prepareStatement(sql); ResultSet result = prepare.executeQuery()) {
            if (result.next()) {
                count = result.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public double getTotalIncome() {
        double total = 0;
        // Tính tổng tiền từ bảng Member (những người đã trả tiền)
        String sql = "SELECT SUM(price) FROM member WHERE status = 'Paid'";
        try (Connection connect = DBConnection.connectDb(); PreparedStatement prepare = connect.prepareStatement(sql); ResultSet result = prepare.executeQuery()) {
            if (result.next()) {
                total = result.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    // Dữ liệu cho biểu đồ (Nhóm theo ngày bắt đầu tập)
    public XYChart.Series<String, Number> getIncomeChartData() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Income Summary");

        //  SQLITE: Bỏ hàm TIMESTAMP(), chỉ dùng ORDER BY startDate ---
        String sql = "SELECT startDate, SUM(price) FROM member WHERE status = 'Paid' GROUP BY startDate ORDER BY startDate ASC LIMIT 10";

        try (Connection connect = DBConnection.connectDb(); PreparedStatement prepare = connect.prepareStatement(sql); ResultSet result = prepare.executeQuery()) {

            // Format ngày tháng cho đẹp (dd-MM-yyyy)
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            while (result.next()) {
                long timestamp = result.getLong(1);
                Double income = result.getDouble(2);

                if (timestamp != 0) {
                    Date dateObj = new Date(timestamp);
                    String dateFormatted = sdf.format(dateObj);

                    series.getData().add(new XYChart.Data<>(dateFormatted, income));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return series;
    }
}
