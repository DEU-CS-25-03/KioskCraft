import Boundary.OrderTypeSelectionUI;
import DataAccessObject.DBManager;
import DataTransferObject.Entity;
import javax.swing.*;
import java.sql.SQLException;

/**
 * Main 클래스
 * - 애플리케이션 진입점
 * - DB 연결 초기화 후 캐시 로드, 초기 디자인 적용, 주문 유형 선택 UI 표시
 */
public class Main {
    public static void main(String[] args) throws SQLException {
        try {
            DBManager.connectDB();      // DB 연결 초기화
            DBManager.getCategoryDAO(); // CategoryDAO 지연 생성 및 Entity.categories 로드
            DBManager.getMenuDAO();     // MenuDAO 지연 생성 및 Entity.menus 로드
            DBManager.getDesignDAO();   // DesignDAO 지연 생성 및 Entity.designs 로드
        } catch (SQLException e) {
            System.out.println(e.getMessage()); // 연결 또는 로드 오류 시 콘솔에 메시지 출력
        } finally {
            DBManager.closeConnection(DBManager.getConnection()); // DB 연결 종료
        }

        SwingUtilities.invokeLater(() -> {
            try {
                // Entity.designs에서 isDefault=true인 테마를 찾아 FlatLAF 설정
                for (int i = 0; i < Entity.designs.length; i++) {
                    Object flag = Entity.designs[i][2];
                    if (flag instanceof Boolean && (Boolean) flag) {
                        UIManager.setLookAndFeel("com.formdev.flatlaf.intellijthemes." + Entity.designs[i][0].toString());
                        break;
                    }
                }
                new OrderTypeSelectionUI().setVisible(true); // 주문 유형 선택 UI 표시
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "프로그램 시작 오류:\n" + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}