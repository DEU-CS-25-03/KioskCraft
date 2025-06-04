package Boundary.Admin;

import DataAccessObject.MenuDAO;
import DataTransferObject.Entity;
import DataTransferObject.MenuDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class ModifyMenuUI extends JDialog {
    private final DefaultTableModel model;

    public ModifyMenuUI(JFrame owner, String title, boolean modal, DefaultTableModel model) {
        super(owner, title, modal);
        this.model = model;
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

        // 여기서는 삭제 기능이 필요하면 별도 구현 필요

        table.getColumnModel().getColumn(4).setCellRenderer(new AdminUI.ButtonRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        table.setRowHeight(35);
        scrollPane.setBounds(10, 70, 830, 500);
        table.setRowSelectionAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        add(scrollPane);

        JButton modifyBtn = new JButton("수정");
        modifyBtn.addActionListener(_ -> onModify(table));
        modifyBtn.setBounds(10, 10, 100, 40);
        add(modifyBtn);
    }

        // 메뉴 수정 버튼
        private void onModify(JTable table){
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "수정할 메뉴를 선택하세요.");
                return;
         }
        try {
            String category = (String) model.getValueAt(selectedRow, 0);
            String name = (String) model.getValueAt(selectedRow, 1);
            int price = Integer.parseInt(model.getValueAt(selectedRow, 2).toString());
            boolean isSoldOut = Boolean.parseBoolean(model.getValueAt(selectedRow, 3).toString());
            String imgPath = ""; // 필요시 이미지 경로도 불러오기

        new Thread(() -> {
            try {
                MenuDAO menuDAO = new MenuDAO();
                MenuDTO updatedMenu = new MenuDTO(category, name, price, imgPath, isSoldOut);
                menuDAO.updateMenu(updatedMenu);

                Entity.refreshMenus();
                SwingUtilities.invokeLater(this::refreshMenuTable);
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this, "메뉴 수정 완료")
                );
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this, "메뉴 수정 실패: " + ex.getMessage())
                );
            }
        }).start();
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "입력값 오류: " + ex.getMessage());
    }
        }
            // 테이블 동기화 메서드
            private void refreshMenuTable() {
                model.setRowCount(0);
                for (MenuDTO menu : Entity.menus) {
                    model.addRow(new Object[]{
                            menu.getCategory(),
                            menu.getMenuName(),
                            menu.getPrice(),
                            menu.isSoldOut(),
                            ""
                    });
                }
            }
        }
