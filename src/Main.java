import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        DBManager dbManager = DBManager.getInstance();
        try {
            dbManager.connectDB();


            // dbManager.getUserInfoDAO()...
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.closeConnection();
        }
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme");
                new OrderTypeSelectionUI().setVisible(true);

                /*
                 * DB 연동 및 초기화 영역 (필요 시 확장)
                 * - 예: DBManager.getInstance().initConnection();
                 */
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "프로그램 시작 오류:\n" + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}