package Control;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public class CategoryControl {
    // 테이블 모델 클래스
    public static class CategoryTableModel extends AbstractTableModel {
        private final String[] columnNames = {"카테고리", ""};
        private final List<String> data;

        public CategoryTableModel(List<String> data) { this.data = data; }

        public void removeRow(int row) {
            data.remove(row);
            fireTableRowsDeleted(row, row);
        }

        @Override public int getRowCount() { return data.size(); }
        @Override public int getColumnCount() { return columnNames.length; }
        @Override public String getColumnName(int column) { return columnNames[column]; }
        @Override public boolean isCellEditable(int row, int column) { return column == 1; }
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) return data.get(rowIndex);
            else return "X";
        }
    }

    // 버튼 렌더러
    public static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() { setText("X"); }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) { return this; }
    }

    // 버튼 에디터
    public static class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private int row;

        public ButtonEditor(JCheckBox checkBox, CategoryTableModel model) {
            super(checkBox);
            button = new JButton("X");
            button.addActionListener(_ -> {
                model.removeRow(row);
                JOptionPane.showMessageDialog(null, "카테고리가 삭제되었습니다.");
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            return button;
        }
    }
}
