import DataAccessObject.DBManager;
import DataTransferObject.Entity;
import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        DBManager dbManager = DBManager.getInstance();
        try {
            DBManager.connectDB();
            // 첫 데이터 세팅
            // DB 관련된 것들은 객체 다 static으로 해놔서 그냥 바로바로 호출해서 쓰면 됩니다
            DBManager.getCategoryDAO();
            DBManager.getMenuDAO();
            DBManager.getDesignDAO();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            dbManager.closeConnection();
        }
        SwingUtilities.invokeLater(() -> {
            try {
                //첫 디자인 설정
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