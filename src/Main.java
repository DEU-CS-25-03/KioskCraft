import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
//        DBManager dbManager = DBManager.getInstance();
//        try {
//            dbManager.connectDB();
//            // dbManager.getUserInfoDAO()...
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            dbManager.closeConnection();
//        }
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme");
                new OrderTypeSelectionUI().setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "프로그램 시작 오류:\n" + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}