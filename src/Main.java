import DataTransferObject.Entity;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // DBManager는 커넥션 풀을 static 블록에서 자동 초기화하므로 별도 인스턴스/연결/종료 관리가 필요 없음

        // (필요하다면 프로그램 시작 시 DB 데이터 동기화)
        try {
            Entity.refreshDesigns();
            // Entity.refreshMenus();
            // Entity.refreshCategories();
        } catch (Exception e) {
            System.out.println("초기 데이터 로딩 실패: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            try {
                for (int i = 0; i < Entity.designs.length; i++) {
                    Object flag = Entity.designs[i][2];
                    if (flag instanceof Boolean && (Boolean) flag) {
                        UIManager.setLookAndFeel("com.formdev.flatlaf.intellijthemes." + Entity.designs[i][0].toString());
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
