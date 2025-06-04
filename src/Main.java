import Boundary.OrderTypeSelectionUI;
import DataTransferObject.Entity;
import DataAccessObject.DBManager;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // (필요하다면 프로그램 시작 시 DB 데이터 동기화)
        // 동기화는 프로그램 시작(최초 실행) 시에만 한 번 발생
        try {
            Entity.refreshDesigns();
            Entity.refreshMenus();
            Entity.refreshCategories();
        } catch (Exception e) {
            System.out.println("초기 데이터 로딩 실패: " + e.getMessage());
        }

        // 종료 훅 등록: 프로그램이 종료될 때 커넥션 풀도 안전하게 종료
        Runtime.getRuntime().addShutdownHook(new Thread(DBManager::closeDataSource));

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
