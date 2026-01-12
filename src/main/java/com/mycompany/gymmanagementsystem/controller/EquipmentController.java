package com.mycompany.gymmanagementsystem.controller;

import com.mycompany.gymmanagementsystem.dao.EquipmentDAO;
import com.mycompany.gymmanagementsystem.model.Equipment;
import com.mycompany.gymmanagementsystem.util.AlertMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class EquipmentController implements Initializable {

    private final EquipmentDAO equipmentDAO = new EquipmentDAO();
    private final AlertMessage alert = new AlertMessage();

    @FXML
    private TextField equipments_name, equipments_price;
    @FXML
    private ComboBox<String> equipments_type, equipments_status;
    @FXML
    private DatePicker equipments_date;
    @FXML
    private TableView<Equipment> equipments_tableView;
    @FXML
    private TableColumn<Equipment, String> equipments_col_name, equipments_col_type, equipments_col_status;
    @FXML
    private TableColumn<Equipment, Double> equipments_col_price;
    @FXML
    private TableColumn<Equipment, Date> equipments_col_date;

    private int equipId = 0; // Lưu ID tạm để sửa/xóa

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupComboBoxes();
        equipmentsShowData();
    }

    private void setupComboBoxes() {
        equipments_type.setItems(FXCollections.observableArrayList(
                "Chest", "Leg", "Back", "Shoulders", "Cardio", "Dumbbell", "Machine", "Yoga Mat", "Others"
        ));
        equipments_status.setItems(FXCollections.observableArrayList("Good", "Maintenance Needed", "Damaged"));
    }

    public void equipmentsShowData() {
        ObservableList<Equipment> list = equipmentDAO.getEquipmentList();
        equipments_col_name.setCellValueFactory(new PropertyValueFactory<>("equipName"));
        equipments_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        equipments_col_date.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));
        equipments_col_status.setCellValueFactory(new PropertyValueFactory<>("quality"));
        equipments_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        equipments_tableView.setItems(list);
    }

    public void equipmentsSelect() {
        Equipment eq = equipments_tableView.getSelectionModel().getSelectedItem();
        if (eq == null) {
            return;
        }
        equipId = eq.getId();
        equipments_name.setText(eq.getEquipName());
        equipments_price.setText(String.valueOf(eq.getPrice()));
        if (eq.getDeliveryDate() != null) {
            equipments_date.setValue(eq.getDeliveryDate().toLocalDate());
        }
        equipments_type.setValue(eq.getType());
        equipments_status.setValue(eq.getQuality());
    }

    public void equipmentsAddBtn() {
        try {
            if (equipments_name.getText().isEmpty() || equipments_type.getValue() == null) {
                alert.errorMessage("Thiếu thông tin!");
                return;
            }
            Equipment eq = new Equipment(0, equipments_name.getText(), equipments_type.getValue(),
                    Date.valueOf(equipments_date.getValue()), equipments_status.getValue(),
                    Double.parseDouble(equipments_price.getText()));
            equipmentDAO.addEquipment(eq);
            alert.successMessage("Thêm thành công!");
            equipmentsShowData();
            equipmentsClear();
        } catch (Exception e) {
            alert.errorMessage("Lỗi nhập liệu!");
        }
    }

    public void equipmentsUpdateBtn() {
        if (equipId == 0) {
            return;
        }
        if (alert.confirmationMessage("Cập nhật thiết bị ID: " + equipId + "?")) {
            Equipment eq = new Equipment(equipId, equipments_name.getText(), equipments_type.getValue(),
                    Date.valueOf(equipments_date.getValue()), equipments_status.getValue(),
                    Double.parseDouble(equipments_price.getText()));
            equipmentDAO.updateEquipment(eq);
            alert.successMessage("Cập nhật thành công!");
            equipmentsShowData();
            equipmentsClear();
        }
    }

    public void equipmentsDeleteBtn() {
        if (equipId == 0) {
            return;
        }
        if (alert.confirmationMessage("Xóa thiết bị ID: " + equipId + "?")) {
            equipmentDAO.deleteEquipment(equipId);
            alert.successMessage("Đã xóa!");
            equipmentsShowData();
            equipmentsClear();
        }
    }

    public void equipmentsClear() {
        equipments_name.setText("");
        equipments_price.setText("");
        equipments_date.setValue(null);
        equipments_type.getSelectionModel().clearSelection();
        equipments_status.getSelectionModel().clearSelection();
        equipId = 0;
    }
}
