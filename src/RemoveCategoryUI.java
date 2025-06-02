import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.Arrays;

public class RemoveCategoryUI extends JDialog {
    public DefaultTableModel model = getTableModel();
    public RemoveCategoryUI(JFrame owner, String title, boolean modal) {
        super(owner, title, modal);
        setTitle(title);
        setLayout(null);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setResizable(false);

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