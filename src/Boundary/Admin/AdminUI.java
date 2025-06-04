package Boundary.Admin;

import Boundary.*;
import Controller.KioskUIUtils;
import DataAccessObject.MenuDAO;
import DataTransferObject.Entity;
import DataTransferObject.MenuDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;

public class AdminUI extends JFrame {
    private KioskUIUtils.KeyHoldActionDispatcher keyDispatcher;
    public DefaultTableModel model = getTableModel();

    public AdminUI() {
        setTitle("관리자 페이지");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(865, 675);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        // [1] 진입 시 커넥션 풀을 통해 DB에서 최신 메뉴를 불러와 테이블 초기화
        refreshMenuTable(); // ★ 주석: 최초 진입 시 DB 동기화

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
        table.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), model, this)); // ★ 수정

        // 스크롤 및 테이블 설정
        JScrollPane scrollPane = new JScrollPane(table);
        table.setRowHeight(35);
        scrollPane.setBounds(10, 70, 830, 500);
        table.setRowSelectionAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        add(scrollPane);

        // 버튼들 추가
        addAdminButtons();

        // 창 닫힐 때 키 디스패처 해제
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent e) { unregisterKeyDispatcher(); }
            @Override public void windowClosing(WindowEvent e) { unregisterKeyDispatcher(); }
        });
    }
    // 테이블 갱신 전용 메서드 (model은 필드로 가정)
    private void refreshMenuTable() {
        try {
            Entity.refreshMenus();
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
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "테이블 갱신 실패: " + ex.getMessage());
        }
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            keyDispatcher = new KioskUIUtils.KeyHoldActionDispatcher(
                    KeyEvent.VK_F10,
                    100,
                    () -> { new OrderTypeSelectionUI().setVisible(true); dispose(); }
            );
            keyDispatcher.register();
        } else {
            unregisterKeyDispatcher();
        }
        super.setVisible(b);
    }

    private void unregisterKeyDispatcher() {
        if (keyDispatcher != null) {
            keyDispatcher.unregister();
            keyDispatcher = null;
        }
    }

    private void addAdminButtons() {
        addAdminButton("메뉴 등록", 10, 10, () -> new RegistMenuUI(this, "메뉴 등록", true, model).setVisible(true));
        addAdminButton("메뉴 수정", 220, 10, () -> new ModifyMenuUI(this, "메뉴 수정", true, model).setVisible(true));
        addAdminButton("매출 분석", 430, 10, () -> new SalesAnalysisUI(this, "매출 분석", true).setVisible(true));
        addAdminButton("디자인 변경", 640, 10, () -> new DesignUI(this, "디자인 변경", true).setVisible(true));

        addAdminButton("카테고리 등록", 10, 580, () -> new RegistCategoryUI(this, "카테고리 등록", true));
        addAdminButton("카테고리 수정", 220, 580, () -> new ModifyCategoryUI(this, "카테고리 수정", true).setVisible(true));
        addAdminButton("카테고리 삭제", 430, 580, () -> new RemoveCategoryUI(this, "카테고리 삭제", true).setVisible(true));
        addAdminButton("주문현황 확인", 640, 580, () -> new OrderStatusUI(this, "주문현황", true).setVisible(true));
    }

    // 3. 버튼 클릭 시에도 동일 메서드 사용
    private void addAdminButton(String name, int x, int y, Runnable showDialog) {
        JButton btn = new JButton(name);
        btn.setBounds(x, y, 200, 50);
        btn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        btn.addActionListener(_ -> {
            showDialog.run();
            refreshMenuTable(); // UI 닫힌 뒤 항상 테이블 동기화
        });
        add(btn);
    }


    private DefaultTableModel getTableModel() {
        String[] columnNames = { "카테고리", "메뉴명", "가격", "품절여부", "" };
        return new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) {
                return column == 4;  // 삭제 버튼만 편집 가능
            }
        };
//        for (Object[] row : Entity.menus) {
//            Object[] rowData = Arrays.copyOf(row, row.length + 1);
//            rowData[rowData.length - 1] = "";
//            model.addRow(rowData);
//        }
    }

    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() { setOpaque(true); }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("X");
            return this;
        }
    }

    // Boundary.Admin.AdminUI.java (삭제 버튼 부분만 발췌)
    static class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private final DefaultTableModel model;
        private final AdminUI adminUI; // ★ AdminUI 인스턴스 보관
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox, DefaultTableModel model, AdminUI adminUI) {
            super(checkBox);
            this.model = model;
            this.adminUI = adminUI;
            button = new JButton("X");
            button.setOpaque(true);
            button.addActionListener(_ -> handleDelete());
        }

        private void handleDelete() {
            int rowToDelete = selectedRow;
            fireEditingStopped();

            new Thread(() -> {
                if (rowToDelete >= 0 && rowToDelete < model.getRowCount()) {
                    String menuName = (String) model.getValueAt(rowToDelete, 1);
                    try {
                        MenuDAO menuDAO = new MenuDAO();
                        menuDAO.deleteMenu(menuName);
                        Entity.refreshMenus();
                        SwingUtilities.invokeLater(() -> {
                            adminUI.refreshMenuTable();
                            JOptionPane.showMessageDialog(adminUI, "메뉴가 삭제되었습니다.");
                        });
                    } catch (Exception ex) {
                        SwingUtilities.invokeLater(() ->
                                JOptionPane.showMessageDialog(adminUI, "메뉴 삭제 실패: " + ex.getMessage())
                        );
                    }
                }
            }).start();
        }
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            button.setText("X");
            selectedRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }
}
