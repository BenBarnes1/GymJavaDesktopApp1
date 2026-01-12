package com.mycompany.gymmanagementsystem.service;

import com.mycompany.gymmanagementsystem.dao.CoachDAO;
import com.mycompany.gymmanagementsystem.model.Coach;
import javafx.collections.ObservableList;

public class CoachService {
    
    private final CoachDAO coachDAO;

    public CoachService() {
        this.coachDAO = new CoachDAO();
    }

    public ObservableList<Coach> getAllCoaches() {
        return coachDAO.getCoachesList();
    }
    
    public ObservableList<Coach> searchCoaches(String keyword) {
        return coachDAO.searchCoaches(keyword);
    }

    public void addCoach(String id, String name, String address, String phoneStr, String gender, String status) throws Exception {
        validateInput(id, name, address, phoneStr, gender, status);

        if (coachDAO.exists(id)) {
            throw new Exception("Coach ID " + id + " đã tồn tại!");
        }

        Coach coach = new Coach(0, id, name, address, gender, Integer.parseInt(phoneStr), status);
        coachDAO.addCoach(coach);
    }

    public void updateCoach(String id, String name, String address, String phoneStr, String gender, String status) throws Exception {
        validateInput(id, name, address, phoneStr, gender, status);
        
        Coach coach = new Coach(0, id, name, address, gender, Integer.parseInt(phoneStr), status);
        coachDAO.updateCoach(coach);
    }

    public void deleteCoach(String id) {
        coachDAO.deleteCoach(id);
    }

    private void validateInput(String id, String name, String address, String phone, String gender, String status) throws Exception {
        if (id.isEmpty() || name.isEmpty() || address.isEmpty() || phone.isEmpty() || gender == null || status == null) {
            throw new Exception("Vui lòng điền đầy đủ thông tin!");
        }
        try {
            Integer.parseInt(phone);
        } catch (NumberFormatException e) {
            throw new Exception("Số điện thoại không hợp lệ!");
        }
    }
}