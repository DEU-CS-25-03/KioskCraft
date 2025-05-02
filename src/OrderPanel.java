import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class OrderPanel extends JPanel {
    private DataModel dataModel;
    private Map<String, Integer> selectedItems;
    private DefaultTableModel tableModel;
    private JTable selectedTable;
    private JLabel totalPriceLabel;

    public OrderPanel(DataModel dataModel, Map<String, Integer> selectedItems) {
        this.dataModel = dataModel;
        this.selectedItems = selectedItems;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(350, 0));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 245, 245));

        // 테이블 컬럼: 메뉴, 수량, 합계, -, X(삭제)
        tableModel = new DefaultTableModel(new String[]{"메뉴", "수량", "합계", "", ""}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 3;
            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                // 버튼 컬럼은 모델에 반영하지 않음
                if (column >= 3) return;
                super.setValueAt(aValue, row, column);
            }
        };
        selectedTable = new JTable(tableModel);
        selectedTable.setRowHeight(30);

        // 컬럼 너비 설정
        TableColumnModel colModel = selectedTable.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(120);
        colModel.getColumn(1).setPreferredWidth(40);
        colModel.getColumn(2).setPreferredWidth(75);
        colModel.getColumn(3).setPreferredWidth(50);
        colModel.getColumn(4).setPreferredWidth(50);


        selectedTable.getTableHeader().setReorderingAllowed(false);
        // 컬럼 크기 조정 비활성화
        selectedTable.getTableHeader().setResizingAllowed(false);

        // 선택 및 포커스 비활성화
        selectedTable.setRowSelectionAllowed(false);
        selectedTable.setColumnSelectionAllowed(false);
        selectedTable.setCellSelectionEnabled(false);
        selectedTable.setFocusable(false);

        // 헤더 가운데 정렬 설정
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) selectedTable.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        selectedTable.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        selectedTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        selectedTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);

        selectedTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer("-"));
        selectedTable.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor("-"));
        selectedTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer("X"));
        selectedTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor("X"));

        add(new JScrollPane(selectedTable), BorderLayout.CENTER);

        totalPriceLabel = new JLabel("총 가격: 0 원");
        totalPriceLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(totalPriceLabel, BorderLayout.SOUTH);

        updateOrderTable();
    }

    // 주문 내역 테이블 갱신
    public void updateOrderTable() {
        tableModel.setRowCount(0);
        int total = 0;
        for (Map.Entry<String, Integer> entry : selectedItems.entrySet()) {
            String menu = entry.getKey();
            int qty = entry.getValue();
            int price = dataModel.getPriceData().get(menu);
            int sum = price * qty;
            tableModel.addRow(new Object[]{menu, qty, String.format("%,d 원", sum), "-", "X"});
            total += sum;
        }
        totalPriceLabel.setText("총 가격: " + String.format("%,d 원", total));
    }

    /**
     * 결제 처리 후 주문 초기화
     * @return 항상 true 반환
     */
    public boolean processPayment() {
        int total = 0;
        for (Map.Entry<String, Integer> entry : selectedItems.entrySet()) {
            total += dataModel.getPriceData().get(entry.getKey()) * entry.getValue();
        }
        JOptionPane.showMessageDialog(this, String.format("총 %,d원 결제 완료되었습니다.", total));
        selectedItems.clear();
        updateOrderTable();
        return true;
    }

    // 버튼 렌더러
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String text) {
            setText(text);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // 버튼 에디터: -, X 동작 처리
    class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private JButton button;
        private String actionType;
        private String currentMenu;

        public ButtonEditor(String actionType) {
            this.actionType = actionType;
            button = new JButton(actionType);
            button.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentMenu != null) {
                int qty = selectedItems.getOrDefault(currentMenu, 0);
                switch (actionType) {
                    case "-":
                        if (qty > 1) selectedItems.put(currentMenu, qty - 1);
                        else selectedItems.remove(currentMenu);
                        break;
                    case "X":
                        selectedItems.remove(currentMenu);
                        break;
                }
                updateOrderTable();
            }
            fireEditingStopped();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            currentMenu = (String) table.getValueAt(row, 0);
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return actionType;
        }
    }
}
