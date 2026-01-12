package com.mycompany.gymmanagementsystem.service;

import com.mycompany.gymmanagementsystem.dao.MemberDAO;
import com.mycompany.gymmanagementsystem.model.Member;
import javafx.collections.ObservableList;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class MemberService {

    private final MemberDAO memberDAO;

    public MemberService() {
        this.memberDAO = new MemberDAO();
    }

    public ObservableList<Member> getAllMembers() {
        return memberDAO.getMembersList();
    }

    public ObservableList<Member> searchMembers(String keyword) {
        return memberDAO.searchMembers(keyword);
    }

    // Logic Thêm Mới
    public void addMember(String id, String name, String address, String phoneStr,
            String gender, String schedule, LocalDate start, LocalDate end,
            String status) throws Exception {

        validateInput(id, name, phoneStr, start, end, schedule);

        if (memberDAO.checkMemberIdExists(id)) {
            throw new Exception("Member ID " + id + " đã tồn tại!");
        }

        double price = calculatePrice(start, end);

        Member member = new Member(0, id, name, address, Integer.parseInt(phoneStr),
                gender, schedule, Date.valueOf(start),
                Date.valueOf(end), price, status);
        memberDAO.addMember(member);
    }

    // Logic Cập Nhật
    public void updateMember(String id, String name, String address, String phoneStr,
            String gender, String schedule, LocalDate start, LocalDate end,
            String status) throws Exception {
        validateInput(id, name, phoneStr, start, end, schedule);

        // Cập nhật lại giá tiền nếu ngày thay đổi (hoặc giữ nguyên tùy nghiệp vụ)
        double price = calculatePrice(start, end);

        Member member = new Member(0, id, name, address, Integer.parseInt(phoneStr),
                gender, schedule, Date.valueOf(start),
                Date.valueOf(end), price, status);
        memberDAO.updateMember(member);
    }

    public void deleteMember(String id) {
        memberDAO.deleteMember(id);
    }

    // Logic tính tiền cơ bản: Số ngày * 100.000
    private double calculatePrice(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            return 0;
        }
        long days = ChronoUnit.DAYS.between(start, end);
        if (days < 0) {
            return 0;
        }
        return days * 100000;
    }

    private void validateInput(String id, String name, String phone, LocalDate s, LocalDate e, String schedule) throws Exception {
        if (id == null || id.isEmpty() || name.isEmpty() || phone.isEmpty() || s == null || e == null || schedule == null) {
            throw new Exception("Vui lòng điền đầy đủ thông tin!");
        }
        try {
            Integer.parseInt(phone);
        } catch (NumberFormatException ex) {
            throw new Exception("Số điện thoại phải là số!");
        }
    }
}
