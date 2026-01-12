package com.mycompany.gymmanagementsystem.controller;

import com.mycompany.gymmanagementsystem.dao.EquipmentDAO;
import com.mycompany.gymmanagementsystem.model.Equipment;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.ResourceBundle;

public class MuscleMapController implements Initializable {

    private final EquipmentDAO equipmentDAO = new EquipmentDAO();

    // --- FIX 1: Tên biến phải khớp ID trong FXML ---
    @FXML
    private TableView<Equipment> muscleMap_tableView;

    @FXML
    private TableColumn<Equipment, String> muscleMap_col_name;
    @FXML
    private TableColumn<Equipment, String> muscleMap_col_type;
    @FXML
    private TableColumn<Equipment, String> muscleMap_col_status;

    @FXML
    private Label muscleMap_targetLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Mặc định load tất cả
        loadData("");
    }

    // Hàm lọc cho các nút Text (nếu dùng)
    public void filterMuscle(ActionEvent event) {
        Button btn = (Button) event.getSource();
        String muscle = btn.getText();

        // --- FIX 2: Sửa tên biến label ---
        muscleMap_targetLabel.setText("Showing Equipment for: " + muscle);
        loadData(muscle);
    }

    // Hàm Reset (Show All)
    public void resetFilter() {
        // --- FIX 2: Sửa tên biến label ---
        muscleMap_targetLabel.setText("All Equipment");
        loadData("");
    }

    // Hàm xử lý click trên ẢNH BODY
    public void muscleMapSelect(ActionEvent event) {
        Button btn = (Button) event.getSource();
        String btnId = btn.getId();
        String muscleGroup = "";

        if (btnId.equals("btn_chest")) {
            muscleGroup = "Chest";
        } else if (btnId.equals("btn_arm_left") || btnId.equals("btn_arm_right")) {
            muscleGroup = "Dumbbell"; // Hoặc tên loại máy tương ứng trong DB 
        } else if (btnId.equals("btn_legs")) {
            muscleGroup = "Leg";
        } else if (btnId.equals("btn_abs")) {
            muscleGroup = "Cardio";
        }

        muscleMap_targetLabel.setText("Target Area: " + muscleGroup.toUpperCase());
        loadData(muscleGroup);
    }

    private void loadData(String muscle) {
        ObservableList<Equipment> list;
        if (muscle == null || muscle.isEmpty() || muscle.equals("All")) {
            list = equipmentDAO.getEquipmentList();
        } else {
            list = equipmentDAO.getEquipmentByMuscle(muscle);
        }

        //  Sửa tên cột cho khớp với khai báo ở trên ---
        muscleMap_col_name.setCellValueFactory(new PropertyValueFactory<>("equipName"));
        muscleMap_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        muscleMap_col_status.setCellValueFactory(new PropertyValueFactory<>("quality"));

        muscleMap_tableView.setItems(list);
    }
}
