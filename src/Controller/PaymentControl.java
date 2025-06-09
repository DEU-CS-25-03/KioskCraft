package Controller;

import DataAccessObject.DBManager;
import DataTransferObject.Entity;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class PaymentControl {
    public static void saveOrderToDB(int totalAmount) {
        Connection conn = null;
        PreparedStatement psHistory = null;
        PreparedStatement psDetail = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            conn.setAutoCommit(false);  // 트랜잭션 시작

            // 1. PaymentHistory insert
            String sqlHistory = "INSERT INTO test.PaymentHistory (paymentTime, totalAmount) VALUES (NOW(), ?)";
            psHistory = conn.prepareStatement(sqlHistory, Statement.RETURN_GENERATED_KEYS);
            psHistory.setInt(1, totalAmount);
            psHistory.executeUpdate();

            rs = psHistory.getGeneratedKeys();
            int paymentId = -1;
            if (rs.next()) {
                paymentId = rs.getInt(1);
            }

            // 2. PaymentDetails insert
            String sqlDetail = "INSERT INTO test.PaymentDetails (paymentId, menuName, unitPrice, quantity, total) VALUES (?, ?, ?, ?, ?)";
            psDetail = conn.prepareStatement(sqlDetail);

            for (Object[] item : Entity.cartList) {
                // Object[] 구조: [메뉴명, 단가, 수량, 총단가] 라고 가정
                String menuName = (String) item[0];
                int unitPrice = (int) item[1];
                int quantity = (int) item[2];
                int totalPrice = (int) item[3];

                psDetail.setInt(1, paymentId);
                psDetail.setString(2, menuName);
                psDetail.setInt(3, unitPrice);
                psDetail.setInt(4, quantity);
                psDetail.setInt(5, totalPrice);
                psDetail.addBatch();
            }
            psDetail.executeBatch();

            conn.commit();  // 성공시 커밋
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (Exception ignored) {}
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "결제내역 저장 중 오류 발생: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (psDetail != null) psDetail.close(); } catch (Exception ignored) {}
            try { if (psHistory != null) psHistory.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }
}
