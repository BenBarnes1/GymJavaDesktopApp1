module com.mycompany.gymmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j; // Driver MySQL
    requires de.jensd.fx.glyphs.fontawesome;
    requires de.jensd.fx.glyphs.commons;
    requires javafx.swing;
    requires java.desktop;
    // Mở quyền cho JavaFX truy cập vào Controller
    opens com.mycompany.gymmanagementsystem.controller to javafx.fxml;
    
    // Mở quyền cho JavaFX truy cập vào Model (để hiển thị dữ liệu lên TableView)
    opens com.mycompany.gymmanagementsystem.model to javafx.base;

    // Export gói gốc chứa file App.java
    exports com.mycompany.gymmanagementsystem;
}