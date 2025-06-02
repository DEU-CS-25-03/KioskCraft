import DataAccessObject.DBManager;
import DataTransferObject.Entity;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        DBManager dbManager = DBManager.getInstance();
        try {
            dbManager.connectDB();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            dbManager.closeConnection();
        }
        SwingUtilities.invokeLater(() -> {
            try {
                for (int i = 0; i < Entity.designs.length; i++) {
                    Object flag = Entity.designs[i][2];
                    if (flag instanceof Boolean && (Boolean) flag) {
                        UIManager.setLookAndFeel("com.formdev.flatlaf.intellijthemes."+Entity.designs[i][0].toString());
                        break;
                    }
                }
                new OrderTypeSelectionUI().setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "프로그램 시작 오류:\n" + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}