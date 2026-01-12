package com.mycompany.gymmanagementsystem.controller;

import com.mycompany.gymmanagementsystem.service.AuthService;
import com.mycompany.gymmanagementsystem.util.AlertMessage;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private AnchorPane main_form;
    @FXML private AnchorPane sub_form;
    @FXML private Button sub_signupBtn;
    @FXML private Button sub_loginBtn;
    @FXML private Label edit_label;
    
    @FXML private AnchorPane signup_form;
    @FXML private TextField su_email;
    @FXML private TextField su_username;
    @FXML private PasswordField su_password;
    @FXML private Button su_signupBtn;
    
    @FXML private AnchorPane login_form;
    @FXML private TextField si_username;
    @FXML private PasswordField si_password;
    @FXML private Button si_loginBtn;
    
    // Inject Service & Alert
    private final AuthService authService = new AuthService();
    private final AlertMessage alert = new AlertMessage();

    public void login() {
        try {
            // AuthService lo hết logic kiểm tra, nếu lỗi sẽ ném Exception
            if (authService.login(si_username.getText(), si_password.getText())) {
                alert.successMessage("Successfully Login!");
                
                // Chuyển sang màn hình chính (MainLayout)
                si_loginBtn.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("/com/mycompany/gymmanagementsystem/fxml/MainLayout.fxml"));
                
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            alert.errorMessage(e.getMessage());
        }
    }

    public void signup() {
        try {
            authService.signup(su_email.getText(), su_username.getText(), su_password.getText());
            alert.successMessage("Successfully create new account!");
            
            // Xóa trắng form sau khi đăng ký
            su_email.setText("");
            su_username.setText("");
            su_password.setText("");
            
        } catch (Exception e) {
            alert.errorMessage(e.getMessage());
        }
    }

    // --- Giữ nguyên logic Animation trượt panel ---
    public void signupSlider() {
        TranslateTransition slider1 = new TranslateTransition();
        slider1.setNode(sub_form);
        slider1.setToX(300);
        slider1.setDuration(Duration.seconds(.5));
        slider1.play();
        slider1.setOnFinished((ActionEvent event) -> {
            edit_label.setText("Login Account");
            sub_signupBtn.setVisible(false);
            sub_loginBtn.setVisible(true);
        });
    }

    public void loginSlider() {
        TranslateTransition slider1 = new TranslateTransition();
        slider1.setNode(sub_form);
        slider1.setToX(0);
        slider1.setDuration(Duration.seconds(.5));
        slider1.play();
        slider1.setOnFinished((ActionEvent event) -> {
            edit_label.setText("Create Account");
            sub_signupBtn.setVisible(true);
            sub_loginBtn.setVisible(false);
        });
    }
    
    public void close() {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Init
    }
}