package Control;

import Boundary.OrderTypeSelectionUI;
import DataTransferObject.Entity;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class AdminControl extends JFrame {
    public static DefaultTableModel getTableModel() {
        String[] columnNames = { "카테고리", "메뉴명", "가격", "품절여부", "" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) {
                return column == 4;  // 삭제 버튼만 편집 가능
            }
        };
        for (Object[] row : Entity.menus) {
            Object[] rowData = Arrays.copyOf(row, row.length + 1);
            rowData[rowData.length - 1] = "";
            model.addRow(rowData);
        }
        return model;
    }

    public static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() { setOpaque(true); }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("X");
            return this;
        }
    }

    public static class ButtonEditor extends DefaultCellEditor {
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
                        Entity.menus.remove(rowToDelete);
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
