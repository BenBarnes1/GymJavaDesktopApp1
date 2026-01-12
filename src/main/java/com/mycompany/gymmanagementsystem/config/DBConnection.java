package com.mycompany.gymmanagementsystem.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    public static Connection connectDb() {
        try {
            // Nạp driver SQLite
            Class.forName("org.sqlite.JDBC");
            // Kết nối file database
            String url = "jdbc:sqlite:gym.db"; 
            Connection connect = DriverManager.getConnection(url);
            
            // Tự động tạo bảng nếu chưa có
            createTables(connect);
            
            return connect;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void createTables(Connection connect) {
        try (Statement stmt = connect.createStatement()) {
            // 1. Bảng Admin (Tài khoản đăng nhập)
            String sqlAdmin = "CREATE TABLE IF NOT EXISTS admin ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "email TEXT, "
                    + "username TEXT UNIQUE, "
                    + "password TEXT)";
            stmt.execute(sqlAdmin);

            // 2. Bảng Member (Hội viên)
            String sqlMember = "CREATE TABLE IF NOT EXISTS member ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "memberId TEXT, "
                    + "name TEXT, "
                    + "address TEXT, "
                    + "phoneNum INTEGER, "
                    + "gender TEXT, "
                    + "schedule TEXT, "
                    + "startDate DATE, " // SQLite lưu Date dưới dạng số (long) hoặc chuỗi
                    + "endDate DATE, "
                    + "price DOUBLE, "
                    + "status TEXT)";
            stmt.execute(sqlMember);

            // 3. Bảng Coach (Huấn luyện viên)
            String sqlCoach = "CREATE TABLE IF NOT EXISTS coach ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "coachId TEXT, "
                    + "name TEXT, "
                    + "address TEXT, "
                    + "gender TEXT, "
                    + "phoneNum INTEGER, "
                    + "status TEXT)";
            stmt.execute(sqlCoach);

            // 4. Bảng Equipment (Thiết bị)
            String sqlEquip = "CREATE TABLE IF NOT EXISTS equipment ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "equipName TEXT, "
                    + "type TEXT, "
                    + "deliveryDate DATE, "
                    + "quality TEXT, "
                    + "price DOUBLE)";
            stmt.execute(sqlEquip);

            // 5. Bảng Settings (Cấu hình giá tiền...)
            String sqlSettings = "CREATE TABLE IF NOT EXISTS settings ("
                    + "config_key TEXT PRIMARY KEY, "
                    + "config_value TEXT)";
            stmt.execute(sqlSettings);

            // Tạo tài khoản Admin mặc định & Giá mặc định
            stmt.execute("INSERT OR IGNORE INTO admin (username, password) VALUES ('admin', 'admin123')");
            stmt.execute("INSERT OR IGNORE INTO settings (config_key, config_value) VALUES ('default_price', '100000')");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}