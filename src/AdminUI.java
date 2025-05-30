import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

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
        table.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), model));

        // 스크롤 및 테이블 설정
        JScrollPane scrollPane = new JScrollPane(table);
        table.setRowHeight(35);
        scrollPane.setBounds(10, 70, 830, 500);
        table.setRowSelectionAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        add(scrollPane);

        // 버튼들 추가
        addAdminButtons(table);

        // 창 닫힐 때 키 디스패처 해제
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent e) { unregisterKeyDispatcher(); }
            @Override public void windowClosing(WindowEvent e) { unregisterKeyDispatcher(); }
        });
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

    private void addAdminButtons(JTable table) {
        JButton registMenuBtn = new JButton("메뉴 등록");
        registMenuBtn.setBounds(10, 10, 200, 50);
        registMenuBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        registMenuBtn.addActionListener(_ -> new RegistMenuUI(this, "메뉴 등록", true, model).setVisible(true));
        add(registMenuBtn);

        JButton modifyMenuBtn = new JButton("메뉴 수정");
        modifyMenuBtn.setBounds(220, 10, 200, 50);
        modifyMenuBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        modifyMenuBtn.addActionListener(_ -> new MenuControlUI(false, table, this, "메뉴 수정", true).setVisible(true));
        add(modifyMenuBtn);

        JButton orderedCheckBtn = new JButton("매출 분석");
        orderedCheckBtn.setBounds(430, 10, 200, 50);
        orderedCheckBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        orderedCheckBtn.addActionListener(_ -> new DesignUI(this, "매출 분석", true).setVisible(true));
        add(orderedCheckBtn);

        JButton setDefaultDesignBtn = new JButton("디자인 변경");
        setDefaultDesignBtn.setBounds(640, 10, 200, 50);
        setDefaultDesignBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        setDefaultDesignBtn.addActionListener(_ -> new DesignUI(this, "디자인 변경", true).setVisible(true));
        add(setDefaultDesignBtn);

        JButton registCategoryBtn = new JButton("카테고리 등록");
        registCategoryBtn.setBounds(10, 580, 200, 50);
        registCategoryBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        registCategoryBtn.addActionListener(_ -> new RegistCategoryUI(this, "카테고리 등록", true).setVisible(true));
        add(registCategoryBtn);

        JButton modifyCategoryBtn = new JButton("카테고리 수정");
        modifyCategoryBtn.setBounds(220, 580, 200, 50);
        modifyCategoryBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        modifyCategoryBtn.addActionListener(_ -> new CategoryControlUI(this, "카테고리 수정", true).setVisible(true));
        add(modifyCategoryBtn);

        JButton deleteCategoryBtn = new JButton("카테고리 삭제");
        deleteCategoryBtn.setBounds(430, 580, 200, 50);
        deleteCategoryBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        deleteCategoryBtn.addActionListener(_ -> new CategoryControlUI(this, "카테고리 삭제", true).setVisible(true));
        add(deleteCategoryBtn);

        JButton showOrderedListBtn = new JButton("주문현황 확인");
        showOrderedListBtn.setBounds(640, 580, 200, 50);
        showOrderedListBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        showOrderedListBtn.addActionListener(_ -> new OrderStatusUI(this, "주문현황", true).setVisible(true));
        add(showOrderedListBtn);
    }

    private DefaultTableModel getTableModel() {
        String[] columnNames = { "카테고리", "메뉴명", "가격", "품절여부", "" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) {
                return column == 4;  // 삭제 버튼만 편집 가능
            }
        };
        for (Object[] row : DataSet.menus) {
            Object[] rowData = Arrays.copyOf(row, row.length + 1);
            rowData[rowData.length - 1] = "";
            model.addRow(rowData);
        }
        return model;
    }

    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() { setOpaque(true); }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("X");
            return this;
        }
    }

    static class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox, DefaultTableModel model) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(_ -> {
                int rowToDelete = selectedRow;
                fireEditingStopped();
                SwingUtilities.invokeLater(() -> {
                    if (rowToDelete >= 0 && rowToDelete < model.getRowCount()) {
                        DataSet.menus.remove(rowToDelete);
                        model.removeRow(rowToDelete);
                        JOptionPane.showMessageDialog(null, "메뉴가 삭제되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                    }
                });
            });
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
