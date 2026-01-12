package com.mycompany.gymmanagementsystem.controller;

import com.mycompany.gymmanagementsystem.dao.MemberDAO;
import com.mycompany.gymmanagementsystem.dao.PaymentDAO;
import com.mycompany.gymmanagementsystem.model.Member;
import com.mycompany.gymmanagementsystem.service.MemberService;
import com.mycompany.gymmanagementsystem.util.AlertMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import java.net.URL;
import java.sql.Date;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {

    private final MemberDAO memberDAO = new MemberDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final MemberService memberService = new MemberService(); // Dùng lại service cũ
    private final AlertMessage alert = new AlertMessage();

    @FXML
    private ComboBox<String> payment_customerID;
    @FXML
    private ComboBox<String> payment_name;
    @FXML
    private Label payment_total;
    @FXML
    private Label payment_change;
    @FXML
    private TextField payment_amount;
    @FXML
    private TextField payment_pricePerDay;
    @FXML
    private TextField payment_discount;

    @FXML
    private TableView<Member> payment_tableView;
    @FXML
    private TableColumn<Member, String> payment_col_customerID;
    @FXML
    private TableColumn<Member, String> payment_col_name;
    @FXML
    private TableColumn<Member, String> payment_col_status;
    @FXML
    private TableColumn<Member, Date> payment_col_startDate;
    @FXML
    private TableColumn<Member, Date> payment_col_endDate;

    private double totalP = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        paymentShowData();
        paymentLoadMemberIds();
        loadDefaultPrice();

        // Tự động tính tiền khi thay đổi giá hoặc discount
        payment_pricePerDay.textProperty().addListener((obs, oldVal, newVal) -> paymentDisplayTotal());
        payment_discount.textProperty().addListener((obs, oldVal, newVal) -> paymentDisplayTotal());
    }

    private void loadDefaultPrice() {
        double price = paymentDAO.getDefaultPrice();
        payment_pricePerDay.setText(String.format("%.0f", price));
        payment_discount.setText("0");
    }

    public void paymentShowData() {
        ObservableList<Member> list = memberService.getAllMembers();
        payment_col_customerID.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        payment_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        payment_col_startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        payment_col_endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        payment_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        payment_tableView.setItems(list);
    }

    public void paymentLoadMemberIds() {
        // Lấy danh sách ID các member chưa thanh toán (Bro tự thêm hàm getUnpaidMemberIds vào MemberDAO nếu chưa có, hoặc dùng logic lọc list)
        // Ở đây tôi dùng tạm list full, bro có thể tối ưu sau
        ObservableList<Member> allMembers = memberDAO.getMembersList();
        ObservableList<String> ids = FXCollections.observableArrayList();
        for (Member m : allMembers) {
            if ("Unpaid".equals(m.getStatus())) {
                ids.add(m.getMemberId());
            }
        }
        payment_customerID.setItems(ids);
    }

    public void paymentMemberId() {
        String memId = payment_customerID.getSelectionModel().getSelectedItem();
        if (memId != null) {
            Member m = memberDAO.getMemberById(memId);
            if (m != null) {
                payment_name.setItems(FXCollections.observableArrayList(m.getName()));
                payment_name.getSelectionModel().selectFirst();
                paymentDisplayTotal();
            }
        }
    }

    public void paymentName(ActionEvent event) {
        paymentDisplayTotal();
    }

    public void paymentDisplayTotal() {
        String memId = payment_customerID.getValue();
        if (memId != null) {
            Member member = memberDAO.getMemberById(memId);
            if (member != null && member.getStartDate() != null && member.getEndDate() != null) {
                long days = ChronoUnit.DAYS.between(
                        member.getStartDate().toLocalDate(),
                        member.getEndDate().toLocalDate());

                if (days < 0) {
                    days = 0;
                }

                double price = 0;
                double discount = 0;
                try {
                    price = Double.parseDouble(payment_pricePerDay.getText().isEmpty() ? "0" : payment_pricePerDay.getText());
                    discount = Double.parseDouble(payment_discount.getText().isEmpty() ? "0" : payment_discount.getText());
                } catch (NumberFormatException e) {
                }

                totalP = (days * price) * ((100 - discount) / 100);
                payment_total.setText(String.format("%.0f VND", totalP));
            }
        }
    }

    public void paymentAmount() {
        if (payment_amount.getText().isEmpty() || totalP == 0) {
            return;
        }
        try {
            double amount = Double.parseDouble(payment_amount.getText());
            if (amount >= totalP) {
                payment_change.setText(String.format("%.0f VND", (amount - totalP)));
            } else {
                payment_change.setText("0 VND");
            }
        } catch (NumberFormatException e) {
            alert.errorMessage("Số tiền không hợp lệ");
        }
    }

    public void paymentPayBtn() {
        if (totalP == 0 || payment_amount.getText().isEmpty() || payment_customerID.getValue() == null) {
            alert.errorMessage("Vui lòng chọn thành viên và nhập số tiền!");
            return;
        }
        if (alert.confirmationMessage("Xác nhận thanh toán?")) {
            // Update trạng thái Paid vào DB
            // Bro cần thêm hàm updatePayment vào MemberDAO như code bên dưới
            updatePaymentStatus(payment_customerID.getValue(), totalP);

            alert.successMessage("Thanh toán thành công!");
            paymentShowData();
            paymentLoadMemberIds();
            payment_total.setText("0 VND");
            payment_amount.setText("");
            payment_change.setText("0 VND");
        }
    }

    // Hàm phụ gọi DAO để update (tốt nhất nên chuyển vào Service)
    private void updatePaymentStatus(String id, double finalPrice) {
        try {
            // Logic SQL update đơn giản
            java.sql.Connection connect = com.mycompany.gymmanagementsystem.config.DBConnection.connectDb();
            String sql = "UPDATE member SET status = 'Paid', price = ? WHERE memberId = ?";
            java.sql.PreparedStatement prepare = connect.prepareStatement(sql);
            prepare.setDouble(1, finalPrice);
            prepare.setString(2, id);
            prepare.executeUpdate();
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveNewDefaultPrice() {
        try {
            double newPrice = Double.parseDouble(payment_pricePerDay.getText());
            if (alert.confirmationMessage("Lưu giá mặc định mới: " + String.format("%.0f", newPrice) + " VND?")) {
                paymentDAO.updateDefaultPrice(newPrice);
                alert.successMessage("Đã lưu!");
            }
        } catch (Exception e) {
            alert.errorMessage("Giá không hợp lệ!");
        }
    }
}
