package com.mycompany.gymmanagementsystem.controller;

import com.mycompany.gymmanagementsystem.model.Member;
import com.mycompany.gymmanagementsystem.service.MemberService;
import com.mycompany.gymmanagementsystem.util.AlertMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javax.imageio.ImageIO;
import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class MemberController implements Initializable {

    private final MemberService memberService = new MemberService();
    private final AlertMessage alert = new AlertMessage();

    // UI Fields
    @FXML
    private TextField members_customerId;
    @FXML
    private TextField members_name;
    @FXML
    private TextArea members_caddress;
    @FXML
    private TextField members_phoneNum;
    @FXML
    private ComboBox<String> members_gender;
    @FXML
    private ComboBox<String> members_schedule;
    @FXML
    private DatePicker members_startDate;
    @FXML
    private DatePicker members_endDate;
    @FXML
    private ComboBox<String> members_status;

    // Table
    @FXML
    private TableView<Member> members_tableView;
    @FXML
    private TableColumn<Member, String> members_col_customerID;
    @FXML
    private TableColumn<Member, String> members_col_name;
    @FXML
    private TableColumn<Member, String> members_col_address;
    @FXML
    private TableColumn<Member, Integer> members_col_phoneNum;
    @FXML
    private TableColumn<Member, String> members_col_gender;
    @FXML
    private TableColumn<Member, String> members_col_schedule;
    @FXML
    private TableColumn<Member, Date> members_col_startDate;
    @FXML
    private TableColumn<Member, Date> members_col_endDate;
    @FXML
    private TableColumn<Member, String> members_col_status;

    @FXML
    private TextField members_search;

    // Card Layout
    @FXML
    private AnchorPane card_layout;
    @FXML
    private Label card_name;
    @FXML
    private Label card_id;

    private boolean isAscending = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupComboBoxes();

        // BẮT BUỘC PHẢI CÓ DÒNG NÀY ĐỂ HIỆN DỮ LIỆU
        membersShowData();

        // Listener search
        members_search.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                membersShowData();
            } else {
                membersSearchBtn();
            }
        });
    }

    private void setupComboBoxes() {
        members_gender.setItems(FXCollections.observableArrayList("Male", "Female", "Others"));
        members_schedule.setItems(FXCollections.observableArrayList("9AM - 11AM", "11AM - 1PM", "1PM - 3PM", "3PM - 5PM", "5PM - 7PM"));
        members_status.setItems(FXCollections.observableArrayList("Paid", "Unpaid"));
    }

    public void membersShowData() {
        ObservableList<Member> list = memberService.getAllMembers();

        // Mapping chính xác với class Member.java của bro
        members_col_customerID.setCellValueFactory(new PropertyValueFactory<>("memberId")); // Đúng
        members_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        members_col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        members_col_phoneNum.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        members_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        members_col_schedule.setCellValueFactory(new PropertyValueFactory<>("schedule"));
        members_col_startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        members_col_endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        members_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        members_tableView.setItems(list);
    }

    private void setupTable(ObservableList<Member> list) {
        members_col_customerID.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        members_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        members_col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        members_col_phoneNum.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        members_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        members_col_schedule.setCellValueFactory(new PropertyValueFactory<>("schedule"));
        members_col_startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        members_col_endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        members_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        members_tableView.setItems(list);
    }

    public void membersAddBtn() {
        try {
            memberService.addMember(
                    members_customerId.getText(),
                    members_name.getText(),
                    members_caddress.getText(),
                    members_phoneNum.getText(),
                    members_gender.getValue(),
                    members_schedule.getValue(),
                    members_startDate.getValue(),
                    members_endDate.getValue(),
                    members_status.getValue()
            );
            alert.successMessage("Thêm thành công!");
            membersShowData();
            membersClear();
        } catch (Exception e) {
            alert.errorMessage(e.getMessage());
        }
    }

    public void membersUpdate() {
        try {
            if (members_customerId.getText().isEmpty()) {
                return;
            }
            if (alert.confirmationMessage("Cập nhật Member ID: " + members_customerId.getText() + "?")) {
                memberService.updateMember(
                        members_customerId.getText(),
                        members_name.getText(),
                        members_caddress.getText(),
                        members_phoneNum.getText(),
                        members_gender.getValue(),
                        members_schedule.getValue(),
                        members_startDate.getValue(),
                        members_endDate.getValue(),
                        members_status.getValue()
                );
                alert.successMessage("Cập nhật thành công!");
                membersShowData();
                membersClear();
            }
        } catch (Exception e) {
            alert.errorMessage(e.getMessage());
        }
    }

    public void membersDelete() {
        String id = members_customerId.getText();
        if (id.isEmpty()) {
            alert.errorMessage("Chọn hội viên cần xóa!");
            return;
        }
        if (alert.confirmationMessage("Xóa Member ID: " + id + "?")) {
            memberService.deleteMember(id);
            alert.successMessage("Đã xóa!");
            membersShowData();
            membersClear();
        }
    }

    public void membersClear() {
        members_customerId.setText("");
        members_name.setText("");
        members_caddress.setText("");
        members_phoneNum.setText("");
        members_startDate.setValue(null);
        members_endDate.setValue(null);
        members_gender.getSelectionModel().clearSelection();
        members_schedule.getSelectionModel().clearSelection();
        members_status.getSelectionModel().clearSelection();
    }

    public void membersSelect() {
        Member md = members_tableView.getSelectionModel().getSelectedItem();
        if (md == null) {
            return;
        }
        members_customerId.setText(md.getMemberId());
        members_name.setText(md.getName());
        members_caddress.setText(md.getAddress());
        members_phoneNum.setText(String.valueOf(md.getPhoneNum()));
        if (md.getStartDate() != null) {
            members_startDate.setValue(md.getStartDate().toLocalDate());
        }
        if (md.getEndDate() != null) {
            members_endDate.setValue(md.getEndDate().toLocalDate());
        }
        members_gender.setValue(md.getGender());
        members_schedule.setValue(md.getSchedule());
        members_status.setValue(md.getStatus());
    }

    public void membersSearchBtn() {
        String keyword = members_search.getText();
        setupTable(memberService.searchMembers(keyword));
    }

    public void membersSortBtn() {
        ObservableList<Member> list = members_tableView.getItems();
        if (isAscending) {
            list.sort((o1, o2) -> o1.getMemberId().compareTo(o2.getMemberId()));
        } else {
            list.sort((o1, o2) -> o2.getMemberId().compareTo(o1.getMemberId()));
        }
        isAscending = !isAscending;
        members_tableView.setItems(list);
    }

    public void exportMemberCard() {
        Member member = members_tableView.getSelectionModel().getSelectedItem();
        if (member == null) {
            alert.errorMessage("Vui lòng chọn thành viên để in thẻ!");
            return;
        }
        card_name.setText(member.getName().toUpperCase());
        card_id.setText("ID: " + member.getMemberId());
        card_layout.setVisible(true);

        javafx.application.Platform.runLater(() -> {
            try {
                WritableImage image = card_layout.snapshot(new SnapshotParameters(), null);
                String path = System.getProperty("user.home") + "/Desktop/";
                String fileName = "GymCard_" + member.getMemberId() + ".png";
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File(path + fileName));
                card_layout.setVisible(false);
                alert.successMessage("Đã lưu thẻ tại Desktop: " + fileName);
            } catch (Exception e) {
                e.printStackTrace();
                card_layout.setVisible(false);
            }
        });
    }
}
