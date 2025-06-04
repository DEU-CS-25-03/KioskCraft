package Boundary.Admin;

import DataAccessObject.MenuDAO;
import DataTransferObject.Entity;
import DataTransferObject.MenuDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class ModifyMenuUI extends JDialog {
    public ModifyMenuUI(JFrame owner, String title, boolean modal, DefaultTableModel model) {
        super(owner, title, modal);
        setTitle(title);
        setLayout(null);
        setSize(860, 620);
        setLocationRelativeTo(null);
        setResizable(false);
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        // 테이블 모델 생성 및 테이블 초기화
        JTable table = new JTable(model);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 16));

        // 컬럼 너비 설정
        TableColumnModel colModel = table.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(150); // 카테고리
        colModel.getColumn(1).setPreferredWidth(260); // 메뉴명
        colModel.getColumn(2).setPreferredWidth(80);  // 가격
        colModel.getColumn(3).setPreferredWidth(60);  // 품절여부
        colModel.getColumn(4).setPreferredWidth(40);  // 삭제 버튼

        // 삭제 버튼 렌더러/에디터 설정
        table.getColumnModel().getColumn(4).setCellRenderer(new AdminUI.ButtonRenderer());
        table.getColumnModel().getColumn(4).setCellEditor(new AdminUI.ButtonEditor(new JCheckBox(), model));

        // 스크롤 및 테이블 설정
        JScrollPane scrollPane = new JScrollPane(table);
        table.setRowHeight(35);
        scrollPane.setBounds(10, 70, 830, 500);
        table.setRowSelectionAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        add(scrollPane);

        // 메뉴 수정 버튼
        JButton modifyBtn = new JButton("수정");
        modifyBtn.addActionListener(_ -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) return;

            String category = (String) model.getValueAt(selectedRow, 0);
            String name = (String) model.getValueAt(selectedRow, 1);
            int price = Integer.parseInt(model.getValueAt(selectedRow, 2).toString());
            boolean isSoldOut = Boolean.parseBoolean(model.getValueAt(selectedRow, 3).toString());
            String imgPath = ""; // 필요시 이미지 경로도 불러오기

            // --- DB 작업 및 테이블 동기화 비동기 처리 ---
            new Thread(() -> {
                try {
                    // [커넥션 풀] MenuDAO는 커넥션을 멤버로 갖지 않고, 메서드마다 DBManager.getConnection() 사용
                    MenuDAO menuDAO = new MenuDAO();
                    MenuDTO updatedMenu = new MenuDTO(category, name, price, imgPath, isSoldOut);
                    menuDAO.updateMenu(updatedMenu);

                    Entity.refreshMenus();
                    SwingUtilities.invokeLater(() -> {
                        model.setRowCount(0);
                        for (MenuDTO menu : Entity.menus) {
                            model.addRow(new Object[]{
                                    menu.getCategory(), menu.getMenuName(), menu.getPrice(), menu.isSoldOut(), ""
                            });
                        }
                        JOptionPane.showMessageDialog(this, "메뉴 수정 완료");
                    });
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(this, "메뉴 수정 실패: " + ex.getMessage())
                    );
                }
            }).start();
        });
        // 버튼을 다이얼로그에 추가
        modifyBtn.setBounds(10, 10, 100, 40);
        add(modifyBtn);
    }
}
