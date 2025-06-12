import Boundary.OrderTypeSelectionUI;
import DataAccessObject.DBManager;

import DataTransferObject.Design;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Main 클래스
 * - 애플리케이션 진입점
 * - 스플래시 화면을 띄운 뒤 백그라운드에서 DB 초기화 및 DAO 지연 생성
 * - FlatLAF 테마 적용 후 주문 유형 선택 UI 실행
 */
public class Main {
    public static void main(String[] args) {
        // 1) 스플래시 다이얼로그 생성
        JDialog splash = new JDialog((Frame) null, "로딩 중...", true);
        JLabel label = new JLabel("앱을 준비 중입니다. 잠시만 기다려주세요...", JLabel.CENTER);
        label.setPreferredSize(new Dimension(300, 100));
        splash.getContentPane().add(label);
        splash.pack();
        splash.setLocationRelativeTo(null);

        // 2) 백그라운드에서 DB 초기화 및 DAO 로드
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    // HikariCP 풀 초기화 및 DAO 지연 생성
                    DBManager.connectDB();
                    DBManager.getCategoryDAO();
                    DBManager.getMenuDAO();
                    DBManager.getDesignDAO();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null, "DB 초기화 오류:\n" + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    });
                }
                return null;
            }

            @Override
            protected void done() {
                // 3) 초기화가 완료되면 스플래시 닫고 메인 UI 실행
                splash.dispose();
                SwingUtilities.invokeLater(() -> {
                    try {
                        // FlatLAF 테마 적용
                        for (int i = 0; i < Design.designs.length; i++) {
                            Object flag = Design.designs[i][2];
                            if (flag instanceof Boolean && (Boolean) flag) {
                                String themeClass = "com.formdev.flatlaf.intellijthemes." + Design.designs[i][0].toString();
                                UIManager.setLookAndFeel(themeClass);
                                break;
                            }
                        }
                        // 주문 유형 선택 UI 표시
                        new OrderTypeSelectionUI().setVisible(true);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "메인 UI 로드 중 오류:\n" + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    }
                });
            }
        };

        // 4) 스플래시 화면과 SwingWorker 실행
        worker.execute();
        splash.setVisible(true);

        // 5) 애플리케이션 종료 시 커넥션 풀 닫기
        Runtime.getRuntime().addShutdownHook(new Thread(DBManager::closeDataSource));
    }
}
