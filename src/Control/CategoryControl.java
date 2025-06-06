package Control;

import DataAccessObject.CategoryDAO;
import DataAccessObject.DBManager;
import DataTransferObject.Entity;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CategoryControl {

    public static class CategoryTableModel extends AbstractTableModel {
        private final String[] columnNames = { "카테고리", "" };
        private final List<String> data;

        public CategoryTableModel(List<String> data) {
            this.data = data;
        }

        public void removeRow(int row) {
            if (row < 0 || row >= data.size()) return;
            Entity.categories.remove(row);
            data.remove(row);
            fireTableRowsDeleted(row, row);
        }

        public void addRow(String categoryName) {
            Entity.categories.add(categoryName);
            data.add(categoryName);
            int newIndex = data.size() - 1;
            fireTableRowsInserted(newIndex, newIndex);
        }

        @Override public int getRowCount()     { return data.size(); }
        @Override public int getColumnCount()  { return columnNames.length; }
        @Override public String getColumnName(int col) { return columnNames[col]; }
        @Override public boolean isCellEditable(int row, int col) { return col == 1; }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) return data.get(rowIndex);
            else               return "X";
        }
    }

    public static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() { setText("X"); }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    public static class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private int row;
        private final CategoryTableModel model;

        public ButtonEditor(JCheckBox checkBox, CategoryTableModel model) {
            super(checkBox);
            this.model = model;
            button = new JButton("X");
            button.addActionListener(_ -> {
                try {
                    // DB에서만 삭제 처리
                    deleteCategory(model.data.get(row));
                    //업데이트
                    CategoryDAO.loadCategories();
                    model.fireTableDataChanged();
                    // 모델에서 리스트까지 한 번에 제거
                    JOptionPane.showMessageDialog(null, "카테고리가 삭제되었습니다.");
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "삭제 오류:\n" + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.row = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }

        public CategoryTableModel getModel() {
            return model;
        }
    }

    public static void insertCategory(String categoryName) throws SQLException {
        try (Connection conn = DBManager.getConnection()) {
            String insertSql = "INSERT INTO test.categoryId (categoryName) VALUES (?)";
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setString(1, categoryName);
                ps.executeUpdate();
            }
            //업데이트
            CategoryDAO.loadCategories();
        }
        JOptionPane.showMessageDialog(null, "카테고리가 등록되었습니다.");
    }

    public static void deleteCategory(String categoryName) throws SQLException {
        try (Connection conn = DBManager.getConnection()) {
            String deleteSql = "DELETE FROM test.categoryId WHERE categoryName = ?";
            try (PreparedStatement ps = conn.prepareStatement(deleteSql)) {
                ps.setString(1, categoryName);
                int affected = ps.executeUpdate();
                if (affected == 0) {
                    throw new SQLException("삭제할 카테고리를 찾을 수 없습니다: " + categoryName);
                }
            }
        }
    }

    /**
     * modifyCategory 메서드
     * - DB 카테고리 이름을 oldName → newName 업데이트
     * - 호출한 쪽(UI)에서 Entity.categories 리스트와 테이블 모델을 갱신하도록 설계
     *
     * @param oldName   기존 카테고리 이름
     * @param newName   새 카테고리 이름
     * @param rowIndex  변경할 인덱스 (Entity.categorise 위치)
     * @throws SQLException DB 업데이트 중 오류 발생 시 예외 던짐
     */
    public static void modifyCategory(String oldName, String newName, int rowIndex) throws SQLException {
        try (Connection conn = DBManager.getConnection()) {
            String updateSql = "UPDATE test.categoryId SET categoryName = ? WHERE categoryName = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setString(1, newName);
                ps.setString(2, oldName);
                int affected = ps.executeUpdate();
                if (affected == 0) {
                    throw new SQLException("수정할 카테고리를 찾을 수 없습니다: " + oldName);
                }
            }
            //업데이트
            CategoryDAO.loadCategories();
        }
        // 엔티티 리스트 변경은 호출한 쪽에서 처리
    }
}
