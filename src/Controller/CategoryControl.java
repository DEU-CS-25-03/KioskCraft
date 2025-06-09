package Controller;

import DataAccessObject.CategoryDAO;
import DataAccessObject.DBManager;
import DataAccessObject.MenuDAO;
import DataTransferObject.Entity;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
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

        public ButtonEditor(JCheckBox checkBox, CategoryTableModel model, DefaultTableModel adminModel) {
            super(checkBox);
            this.model = model;
            button = new JButton("X");
            button.addActionListener(_ -> {
                try (Connection conn = DBManager.getConnection()){
                    String target = model.data.get(row);
                    // DB에서만 삭제 처리
                    deleteCategory(target);
                    CategoryDAO.loadCategories();
                    model.fireTableDataChanged();


                    // 1) 뒤에서부터(for i = rowCount-1 → 0) 순회하면서 삭제
                    for (int i = adminModel.getRowCount() - 1; i >= 0; i--) {
                        Object cellValue = adminModel.getValueAt(i, 0); // 0번 컬럼이 카테고리명이라고 가정
                        if (cellValue != null && cellValue.equals(target)) {
                            adminModel.removeRow(i);
                            // removeRow(i) 안에서 내부적으로 fireTableRowsDeleted(i,i)가 호출됩니다.
                        }
                    }
                    // 모델에서 리스트까지 한 번에 제거
                    new MenuDAO(conn);
                    MenuDAO.loadAllMenusToEntity();
                    adminModel.fireTableDataChanged();
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
            String insertSql = "INSERT INTO KioskDB.categoryId (categoryName) VALUES (?)";
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
            conn.setAutoCommit(false);
            try {
                // 1) menuId 테이블에서 해당 카테고리의 모든 메뉴 삭제
                String deleteMenusSql = "DELETE FROM KioskDB.menuId WHERE categoryName = ?";
                try (PreparedStatement psMenus = conn.prepareStatement(deleteMenusSql)) {
                    psMenus.setString(1, categoryName);
                    psMenus.executeUpdate();
                }

                // 2) categoryId 테이블에서 해당 카테고리 삭제
                String deleteCategorySql = "DELETE FROM KioskDB.categoryId WHERE categoryName = ?";
                try (PreparedStatement psCat = conn.prepareStatement(deleteCategorySql)) {
                    psCat.setString(1, categoryName);
                    int affected = psCat.executeUpdate();
                    if (affected == 0) {
                        conn.rollback();
                        throw new SQLException("삭제할 카테고리를 찾을 수 없습니다: " + categoryName);
                    }
                }

                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }

        // 3) Entity 내부 리스트에서도 삭제
        Entity.categories.removeIf(cat -> cat.equals(categoryName));
        Entity.menus.removeIf(menuRow -> {
            // 예시: menuRow[4]가 카테고리명이라고 가정
            return categoryName.equals(menuRow[4]);
        });
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
            // 트랜잭션 시작
            conn.setAutoCommit(false);
            try {
                // 1) menuId 테이블에서 category 컬럼 업데이트
                String updateMenusSql = "UPDATE KioskDB.menuId SET categoryName = ? WHERE categoryName = ?";
                try (PreparedStatement psMenus = conn.prepareStatement(updateMenusSql)) {
                    psMenus.setString(1, newName);
                    psMenus.setString(2, oldName);
                    psMenus.executeUpdate();
                }

                // 2) categoryId 테이블에서 카테고리명 업데이트
                String updateCatSql = "UPDATE KioskDB.categoryId SET categoryName = ? WHERE categoryName = ?";
                try (PreparedStatement psCat = conn.prepareStatement(updateCatSql)) {
                    psCat.setString(1, newName);
                    psCat.setString(2, oldName);
                    int affected = psCat.executeUpdate();
                    if (affected == 0) {
                        conn.rollback();
                        throw new SQLException("수정할 카테고리를 찾을 수 없습니다: " + oldName);
                    }
                }

                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
        // Entity.categories는 호출한 쪽에서 리스트 교체 처리한다고 가정
    }
}
