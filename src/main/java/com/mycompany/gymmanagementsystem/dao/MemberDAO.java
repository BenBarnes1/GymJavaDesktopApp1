package com.mycompany.gymmanagementsystem.dao;

import com.mycompany.gymmanagementsystem.config.DBConnection;
import com.mycompany.gymmanagementsystem.model.Member;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class MemberDAO {

    // Lấy toàn bộ danh sách Member
    public ObservableList<Member> getMembersList() {
        ObservableList<Member> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM member";

        try (Connection connect = DBConnection.connectDb(); PreparedStatement prepare = connect.prepareStatement(sql); ResultSet result = prepare.executeQuery()) {

            while (result.next()) {
                listData.add(mapMember(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    // Tìm kiếm an toàn (Fix SQL Injection)
    public ObservableList<Member> searchMembers(String keyword) {
        ObservableList<Member> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM member WHERE memberId LIKE ? OR name LIKE ?";

        try (Connection connect = DBConnection.connectDb(); PreparedStatement prepare = connect.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            prepare.setString(1, searchPattern);
            prepare.setString(2, searchPattern);

            ResultSet result = prepare.executeQuery();
            while (result.next()) {
                listData.add(mapMember(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    // Kiểm tra ID đã tồn tại chưa
    public boolean checkMemberIdExists(String memberId) {
        String sql = "SELECT memberId FROM member WHERE memberId = ?";
        try (Connection connect = DBConnection.connectDb(); PreparedStatement prepare = connect.prepareStatement(sql)) {
            prepare.setString(1, memberId);
            ResultSet result = prepare.executeQuery();
            return result.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Thêm mới
    public void addMember(Member member) {
        String sql = "INSERT INTO member (memberId, name, address, phoneNum, gender, schedule, startDate, endDate, price, status) VALUES(?,?,?,?,?,?,?,?,?,?)";
        try (Connection connect = DBConnection.connectDb(); PreparedStatement prepare = connect.prepareStatement(sql)) {

            prepare.setString(1, member.getMemberId());
            prepare.setString(2, member.getName());
            prepare.setString(3, member.getAddress());
            prepare.setInt(4, member.getPhoneNum());
            prepare.setString(5, member.getGender());
            prepare.setString(6, member.getSchedule());
            prepare.setDate(7, member.getStartDate());
            prepare.setDate(8, member.getEndDate());
            prepare.setDouble(9, member.getPrice());
            prepare.setString(10, member.getStatus());

            prepare.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Cập nhật
    public void updateMember(Member member) {
        String sql = "UPDATE member SET name=?, address=?, phoneNum=?, gender=?, schedule=?, startDate=?, endDate=?, price=?, status=? WHERE memberId=?";
        try (Connection connect = DBConnection.connectDb(); PreparedStatement prepare = connect.prepareStatement(sql)) {

            prepare.setString(1, member.getName());
            prepare.setString(2, member.getAddress());
            prepare.setInt(3, member.getPhoneNum());
            prepare.setString(4, member.getGender());
            prepare.setString(5, member.getSchedule());
            prepare.setDate(6, member.getStartDate());
            prepare.setDate(7, member.getEndDate());
            prepare.setDouble(8, member.getPrice());
            prepare.setString(9, member.getStatus());
            prepare.setString(10, member.getMemberId());

            prepare.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Xóa
    public void deleteMember(String memberId) {
        String sql = "DELETE FROM member WHERE memberId = ?";
        try (Connection connect = DBConnection.connectDb(); PreparedStatement prepare = connect.prepareStatement(sql)) {
            prepare.setString(1, memberId);
            prepare.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hàm phụ trợ: Chuyển dòng dữ liệu SQL thành Object Member
    private Member mapMember(ResultSet result) throws SQLException {
        // Dùng getObject để kiểm tra null an toàn, sau đó dùng getLong nếu là số
        // SQLite có thể lưu date là String hoặc Long. Ta ưu tiên đọc Long cho timestamp.
        Date startDate = null;
        Date endDate = null;

        try {
            // Cố gắng đọc dưới dạng timestamp (Long)
            long startTs = result.getLong("startDate");
            if (!result.wasNull()) {
                startDate = new Date(startTs);
            }

            long endTs = result.getLong("endDate");
            if (!result.wasNull()) {
                endDate = new Date(endTs);
            }
        } catch (Exception e) {
            // Nếu lỗi, thử đọc dưới dạng String rồi parse (phòng hờ)
            // Nhưng với lỗi "Unparseable date: 1767..." thì getLong là chuẩn nhất.
        }

        return new Member(
                result.getInt("id"),
                result.getString("memberId"),
                result.getString("name"),
                result.getString("address"),
                result.getInt("phoneNum"),
                result.getString("gender"),
                result.getString("schedule"),
                startDate,
                endDate,
                result.getDouble("price"),
                result.getString("status")
        );
    }

    // Lấy thông tin 1 member theo ID (Dùng cho Payment sau này)
    public Member getMemberById(String memberId) {
        String sql = "SELECT * FROM member WHERE memberId = ?";
        try (Connection connect = DBConnection.connectDb(); PreparedStatement prepare = connect.prepareStatement(sql)) {
            prepare.setString(1, memberId);
            ResultSet result = prepare.executeQuery();
            if (result.next()) {
                return mapMember(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
