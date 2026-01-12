package com.mycompany.gymmanagementsystem.controller;

import com.mycompany.gymmanagementsystem.util.AlertMessage;
import com.mycompany.gymmanagementsystem.util.Data;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainLayoutController implements Initializable {

    @FXML
    private StackPane contentArea;
    @FXML
    private Button dashboard_btn, coaches_btn, members_btn, payment_btn, equipments_btn, muscleMap_btn;
    @FXML
    private Button close, minimize, logout;
    @FXML
    private Label username;

    private AlertMessage alert = new AlertMessage();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        username.setText(Data.username);
        // Tạm thời chưa load gì cả để tránh lỗi, hoặc nếu đã tạo MemberView thì uncomment dòng dưới
         loadView("MuscleMapView"); 
    }

    public void switchForm(ActionEvent event) {
        if (event.getSource() == dashboard_btn) {
            loadView("DashboardView"); 
        } else if (event.getSource() == members_btn) {
            loadView("MemberView"); 
        } else if (event.getSource() == coaches_btn) {
            loadView("CoachView"); 
        } else if (event.getSource() == payment_btn) {
            loadView("PaymentView"); 
        } else if (event.getSource() == equipments_btn) {
            loadView("EquipmentView"); 
        } else if (event.getSource() == muscleMap_btn) {
            loadView("MuscleMapView");
        }
    }

    private void loadView(String fxmlName) {
        try {
            // In ra đường dẫn nó đang cố tìm để bro check
            String path = "/com/mycompany/gymmanagementsystem/fxml/" + fxmlName + ".fxml";
            System.out.println("Đang load file: " + path);
            
            URL fileUrl = getClass().getResource(path);
            if (fileUrl == null) {
                System.err.println("!!! LỖI TO: Không tìm thấy file tại đường dẫn trên. Kiểm tra lại thư mục resources !!!");
                // Thêm thông báo UI
                alert.errorMessage("Không tìm thấy file: " + fxmlName + ".fxml\nVui lòng Clean & Build lại dự án!");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fileUrl);
            Parent root = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        if (alert.confirmationMessage("Are you sure you want to logout?")) {
            try {
                logout.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("/com/mycompany/gymmanagementsystem/fxml/Login.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        System.exit(0);
    }

    public void minimize() {
        ((Stage) contentArea.getScene().getWindow()).setIconified(true);
    }
}
