package Boundary.Admin;

import Controller.CategoryTableModel;
import DataAccessObject.CategoryDAO;
import DataTransferObject.Entity;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ModifyCategoryUI extends JDialog {
    private final CategoryTableModel model;

    public ModifyCategoryUI(JFrame owner, String title, boolean modal) {
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

    // 버튼 렌더러
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() { setText("수정"); }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) { return this; }
    }

    // 버튼 에디터
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("수정");
            button.addActionListener(_ -> {
                String oldCategoryName = model.getCategoryAt(row);

                String newCategoryName = JOptionPane.showInputDialog(
                        null,
                        "새 카테고리명을 입력하세요:",
                        oldCategoryName
                );

                if (newCategoryName == null || newCategoryName.trim().isEmpty() || newCategoryName.equals(oldCategoryName)) {
                    return;
                }

                // --- 비동기 + 커넥션 풀 ---
                new Thread(() -> {
                    try {
                        CategoryDAO dao = new CategoryDAO();
                        dao.updateCategory(oldCategoryName, newCategoryName.trim()); // DBManager.getConnection() 내부 사용
                        Entity.refreshCategories(); // DBManager.getConnection() 내부 사용
                        SwingUtilities.invokeLater(() -> {
                            model.setData(Entity.categories); // 테이블 동기화
                            JOptionPane.showMessageDialog(null, "카테고리명이 수정되었습니다.");
                        });
                    } catch (Exception ex) {
                        SwingUtilities.invokeLater(() ->
                                JOptionPane.showMessageDialog(null, "카테고리명 수정 실패: " + ex.getMessage())
                        );
                    }
                }).start();
            });
        }


        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            return button;
        }
    }
}
