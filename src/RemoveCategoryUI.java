import DataAccessObject.CategoryDAO;
import DataAccessObject.DBManager;
import DataTransferObject.Entity;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class RemoveCategoryUI extends JDialog {
    private final CategoryTableModel model;

    public RemoveCategoryUI(JFrame owner, String title, boolean modal) {
        super(owner, title, modal);
        setTitle(title);
        setLayout(null);
        setSize(335, 360);
        setLocationRelativeTo(null);
        setResizable(false);

        model = new CategoryTableModel(Entity.categories);
        JTable table = new JTable(model);

        // 버튼 렌더러/에디터 설정
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.setCellSelectionEnabled(false);

        table.getColumn("").setCellRenderer(new ButtonRenderer());
        table.getColumn("").setCellEditor(new ButtonEditor(new JCheckBox()));

        table.getColumn("카테고리").setMaxWidth(200);
        table.getColumn("카테고리").setMinWidth(200);
        table.getColumn("카테고리").setPreferredWidth(200);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 10, 300, 300);
        add(scrollPane);
    }

    // 테이블 모델 클래스
    static class CategoryTableModel extends AbstractTableModel {
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
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() { setText("X"); }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) { return this; }
    }

    // 버튼 에디터
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("X");
            button.addActionListener(_ -> {
                // 삭제할 카테고리명 가져오기
                String selectedCategoryName = model.data.get(row);

                // --- DB 삭제 및 동기화 ---
                try (Connection conn = DBManager.getInstance().getConnection()) {
                    CategoryDAO dao = new CategoryDAO(conn);
                    // 1. 카테고리 삭제
                    dao.deleteCategory(selectedCategoryName);
                    // 2. Entity.categories 최신화
                    Entity.refreshCategories();
                    // 3. 테이블 모델 동기화
                    model.data.clear();
                    model.data.addAll(Entity.categories);
                    model.fireTableDataChanged();
                    JOptionPane.showMessageDialog(RemoveCategoryUI.this, "카테고리가 삭제되었습니다.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(RemoveCategoryUI.this, "카테고리 삭제 실패: " + ex.getMessage());
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            return button;
        }
    }
}
