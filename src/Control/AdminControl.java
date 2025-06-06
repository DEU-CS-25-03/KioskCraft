package Control;

import DataTransferObject.Entity;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * AdminControl 클래스
 * - AdminUI에서 사용할 테이블 모델, 버튼 렌더러/에디터 제공
 */
public class AdminControl {

    /**
     * 메뉴 전체를 Entity.menus에서 읽어와 테이블 모델로 만들어 리턴
     * 컬럼: {카테고리, 메뉴명, 단가, 품절여부, 삭제 버튼}
     */
    public static DefaultTableModel getTableModel() {
        String[] columns = { "카테고리", "메뉴명", "단가", "품절여부", "삭제" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // “삭제” 버튼 컬럼(인덱스 4)만 클릭 가능하도록 허용
                return column == 4;
            }
        };

        // Entity.menus 에 저장된 각 행 데이터를 꺼내서 모델에 추가
        // Entity.menus의 각 요소: { category, menuName, priceStr, isSoldOut, imagePath }
        for (Object[] row : Entity.menus) {
            Object[] rowData = new Object[5];
            rowData[0] = row[0];    // 카테고리
            rowData[1] = row[1];    // 메뉴명
            rowData[2] = row[2];    // 단가         ex) “1,000원”
            rowData[3] = row[3];    // 품절여부     ex) false 또는 true
            rowData[4] = "삭제";     // 버튼 텍스트
            model.addRow(rowData);
        }

        return model;
    }

    /**
     * 테이블에 “삭제” 버튼을 그려줄 렌더러
     */
    public static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("삭제");
            setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    /**
     * “삭제” 버튼 클릭 시 실제 행동을 수행할 에디터
     */
    public static class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final JButton button = new JButton("삭제");
        private final DefaultTableModel model;
        private final JTable table;
        private int currentRow;      // 클릭된(편집 중인) 행 인덱스
        private String currentName;  // 클릭된 행의 메뉴명

        public ButtonEditor(DefaultTableModel model, JTable table) {
            this.model = model;
            this.table = table;
            button.setFont(new Font("맑은 고딕", Font.PLAIN, 14));

            // 버튼 클릭 시 실행되는 리스너
            button.addActionListener(e -> {
                fireEditingStopped();
                SwingUtilities.invokeLater(() -> {
                    // 테이블을 포함하고 있는 최상위 윈도우(Frame or Dialog)를 구함
                    Window parentWindow = SwingUtilities.getWindowAncestor(table);
                    int confirm = JOptionPane.showConfirmDialog(parentWindow, String.format("'%s' 메뉴를 정말 삭제하시겠습니까?", currentName), "메뉴 삭제 확인", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION)
                        MenuControl.deleteMenu(currentName, model, currentRow);
                });
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            // 편집 시작 시 현재 행 및 메뉴명을 저장
            this.currentRow = row;
            this.currentName = (String) model.getValueAt(row, 1);
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "삭제";
        }

        public JTable getTable() {
            return table;
        }
    }
}
