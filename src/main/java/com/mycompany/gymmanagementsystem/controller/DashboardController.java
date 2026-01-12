package com.mycompany.gymmanagementsystem.controller;

import com.mycompany.gymmanagementsystem.service.DashboardService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    private final DashboardService dashboardService = new DashboardService();

    @FXML
    private Label dashboard_NM; // Số thành viên Active
    @FXML
    private Label dashboard_NC; // Số HLV
    @FXML
    private Label dashboard_TI; // Tổng thu nhập
    @FXML
    private AreaChart<String, Number> dashboard_incomeChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dashboardDisplayNM();
        dashboardDisplayNC();
        dashboardDisplayTI();
        javafx.application.Platform.runLater(() -> {
            dashboardDisplayChart();
        });
    }

    public void dashboardDisplayNM() {
        int count = dashboardService.getActiveMembersCount();
        dashboard_NM.setText(String.valueOf(count));
    }

    public void dashboardDisplayNC() {
        int count = dashboardService.getCoachesCount();
        dashboard_NC.setText(String.valueOf(count));
    }

    public void dashboardDisplayTI() {
        double income = dashboardService.getTotalIncome();
        dashboard_TI.setText(String.format("%.0f VND", income));
    }

    @SuppressWarnings("unchecked")
    public void dashboardDisplayChart() {
        // 1. Xóa sạch dữ liệu cũ
        dashboard_incomeChart.getData().clear();

        // 2. Tắt animation tạm thời để reset trục
        dashboard_incomeChart.setAnimated(false);

        // 3. Lấy dữ liệu từ Service
        XYChart.Series<String, Number> series = dashboardService.getIncomeChartData();

        // 4. Bật lại animation TRƯỚC KHI add dữ liệu
        dashboard_incomeChart.setAnimated(true);

        // 5. Add dữ liệu vào (Lúc này JavaFX sẽ diễn hoạt hình từ dưới lên)
        dashboard_incomeChart.getData().add(series);
    }
}
