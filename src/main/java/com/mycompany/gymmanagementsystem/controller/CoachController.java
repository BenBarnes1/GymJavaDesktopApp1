package com.mycompany.gymmanagementsystem.controller;

import com.mycompany.gymmanagementsystem.model.Coach;
import com.mycompany.gymmanagementsystem.service.CoachService;
import com.mycompany.gymmanagementsystem.util.AlertMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class CoachController implements Initializable {

    private final CoachService coachService = new CoachService();
    private final AlertMessage alert = new AlertMessage();

    @FXML
    private TextField coaches_coachID, coaches_name, coaches_phoneNum;
    @FXML
    private TextArea coaches_address;
    @FXML
    private ComboBox<String> coaches_gender, coaches_status;
    @FXML
    private TextField coaches_search;

    @FXML
    private TableView<Coach> coaches_tableView;
    @FXML
    private TableColumn<Coach, String> coaches_col_coachID, coaches_col_name, coaches_col_address, coaches_col_gender, coaches_col_status;
    @FXML
    private TableColumn<Coach, Integer> coaches_col_phoneNum;
    private boolean isAscending = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupComboBoxes();
        coachesShowData();

        coaches_search.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                coachesShowData();
            } else {
                coachesSearchBtn();
            }
        });
    }

    private void setupComboBoxes() {
        coaches_gender.setItems(FXCollections.observableArrayList("Male", "Female", "Others"));
        coaches_status.setItems(FXCollections.observableArrayList("Active", "Inactive"));
    }

    public void coachesShowData() {
        ObservableList<Coach> list = coachService.getAllCoaches();
        setupTable(list);
    }

    private void setupTable(ObservableList<Coach> list) {
        coaches_col_coachID.setCellValueFactory(new PropertyValueFactory<>("coachId"));
        coaches_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        coaches_col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        coaches_col_phoneNum.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        coaches_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        coaches_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        coaches_tableView.setItems(list);
    }

    public void coachesAddBtn() {
        try {
            coachService.addCoach(
                    coaches_coachID.getText(), coaches_name.getText(), coaches_address.getText(),
                    coaches_phoneNum.getText(), coaches_gender.getValue(), coaches_status.getValue()
            );
            alert.successMessage("Thêm thành công!");
            coachesShowData();
            coachesClearBtn();
        } catch (Exception e) {
            alert.errorMessage(e.getMessage());
        }
    }

    public void coachesUpdateBtn() {
        try {
            if (coaches_coachID.getText().isEmpty()) {
                return;
            }
            if (alert.confirmationMessage("Cập nhật Coach ID: " + coaches_coachID.getText() + "?")) {
                coachService.updateCoach(
                        coaches_coachID.getText(), coaches_name.getText(), coaches_address.getText(),
                        coaches_phoneNum.getText(), coaches_gender.getValue(), coaches_status.getValue()
                );
                alert.successMessage("Cập nhật thành công!");
                coachesShowData();
                coachesClearBtn();
            }
        } catch (Exception e) {
            alert.errorMessage(e.getMessage());
        }
    }

    public void coachesDeleteBtn() {
        if (coaches_coachID.getText().isEmpty()) {
            alert.errorMessage("Vui lòng chọn HLV để xóa!");
            return;
        }
        if (alert.confirmationMessage("Xóa Coach ID: " + coaches_coachID.getText() + "?")) {
            coachService.deleteCoach(coaches_coachID.getText());
            alert.successMessage("Đã xóa!");
            coachesShowData();
            coachesClearBtn();
        }
    }

    public void coachesClearBtn() {
        coaches_coachID.setText("");
        coaches_name.setText("");
        coaches_address.setText("");
        coaches_phoneNum.setText("");
        coaches_gender.getSelectionModel().clearSelection();
        coaches_status.getSelectionModel().clearSelection();
    }

    public void coachesSelect() {
        Coach coach = coaches_tableView.getSelectionModel().getSelectedItem();
        if (coach == null) {
            return;
        }
        coaches_coachID.setText(coach.getCoachId());
        coaches_name.setText(coach.getName());
        coaches_address.setText(coach.getAddress());
        coaches_phoneNum.setText(String.valueOf(coach.getPhoneNum()));
        coaches_gender.setValue(coach.getGender());
        coaches_status.setValue(coach.getStatus());
    }

    public void coachesSearchBtn() {
        setupTable(coachService.searchCoaches(coaches_search.getText()));
    }

    public void coachesSortBtn(ActionEvent event) {
        ObservableList<Coach> list = coaches_tableView.getItems();
        if (isAscending) {
            list.sort((c1, c2) -> c1.getCoachId().compareTo(c2.getCoachId()));
        } else {
            list.sort((c1, c2) -> c2.getCoachId().compareTo(c1.getCoachId()));
        }
        isAscending = !isAscending;
        coaches_tableView.setItems(list);
    }
}
