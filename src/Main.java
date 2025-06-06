import Boundary.OrderTypeSelectionUI;
import DataAccessObject.DBManager;
import DataTransferObject.Entity;

import javax.swing.*;
import java.sql.SQLException;

/**
 * Main 클래스
 * - 애플리케이션 진입점
 * - HikariCP 커넥션 풀 초기화 후 DAO 지연 생성 및 캐시 로드
 * - FlatLAF 테마 적용, 주문 유형 선택 UI 실행
 */
public class Main {
    public static void main(String[] args) {
        // 1) DB 연결 풀 초기화 및 DAO 지연 생성
        try {
            DBManager.connectDB();        // HikariCP 풀 초기화
            DBManager.getCategoryDAO();   // CategoryDAO 지연 생성하여 Entity.categories 로드
            DBManager.getMenuDAO();       // MenuDAO 지연 생성하여 Entity.menus 로드
            DBManager.getDesignDAO();     // DesignDAO 지연 생성하여 Entity.designs 로드
        } catch (SQLException e) {
            System.out.println("DB 초기화 오류: " + e.getMessage());
            System.exit(1);  // DB 연결 또는 캐시 로드 실패 시 프로그램 종료
        }

        // 2) 프로그램 종료 시 커넥션 풀 닫기 (Shutdown Hook 등록)
        Runtime.getRuntime().addShutdownHook(new Thread(DBManager::closeDataSource));

        // 3) Swing UI 스레드에서 FlatLAF 테마 적용 후 주문 유형 선택 UI 실행
        SwingUtilities.invokeLater(() -> {
            try {
                // Entity.designs 배열에서 isDefault=true인 테마를 찾아 LookAndFeel 설정
                for (int i = 0; i < Entity.designs.length; i++) {
                    Object flag = Entity.designs[i][2];
                    if (flag instanceof Boolean && (Boolean) flag) {
                        String themeClass = "com.formdev.flatlaf.intellijthemes." + Entity.designs[i][0].toString();
                        UIManager.setLookAndFeel(themeClass);
                        break;
                    }
                }
                // OrderTypeSelectionUI 인스턴스를 생성하여 화면 표시
                new OrderTypeSelectionUI().setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        null,
                        "프로그램 시작 오류:\n" + e.getMessage(),
                        "오류",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}
