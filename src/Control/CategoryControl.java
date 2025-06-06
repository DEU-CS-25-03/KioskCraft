package Control;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

/**
 * CategoryControl 클래스
 * - Entity.categories 목록을 테이블에 표시하고, 각 행의 삭제 버튼 기능을 제공
 */
public class CategoryControl {
    /**
     * CategoryTableModel 클래스
     * - AbstractTableModel을 상속하여 "카테고리" 값과 삭제 버튼("X")을 두 개의 컬럼으로 구성
     * - 데이터는 List<String> 형태로 관리
     */
    public static class CategoryTableModel extends AbstractTableModel {
        private final String[] columnNames = { "카테고리", "" }; // 컬럼명: 첫 번째는 카테고리, 두 번째는 삭제 버튼
        private final List<String> data;                       // 실제 카테고리 명을 저장하는 리스트

        public CategoryTableModel(List<String> data) {
            this.data = data;
        }

        /** 지정된 행(row)을 삭제하고 테이블에 변경 사항을 알림 */
        public void removeRow(int row) {
            data.remove(row);
            fireTableRowsDeleted(row, row);
        }

        @Override public int getRowCount() { return data.size(); }           // 행 개수 = 데이터 리스트 크기
        @Override public int getColumnCount() { return columnNames.length; }  // 컬럼 개수 = 2
        @Override public String getColumnName(int column) { return columnNames[column]; }
        @Override public boolean isCellEditable(int row, int column) { return column == 1; } // 1번 컬럼(삭제 버튼)만 편집 가능

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                return data.get(rowIndex); // 첫 번째 컬럼: 카테고리 명
            } else {
                return "X";               // 두 번째 컬럼: 삭제 버튼 표시용 텍스트
            }
        }
    }

    /**
     * ButtonRenderer 클래스
     * - JTable의 테이블 셀에 'X' 버튼 모양을 렌더링
     * - TableCellRenderer를 구현하여 각 셀에 동일한 JButton 인스턴스를 반환
     */
    public static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("X");   // 버튼 텍스트 설정
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this; // 항상 이 JButton 컴포넌트를 렌더링
        }
    }

    /**
     * ButtonEditor 클래스
     * - 삭제 버튼을 클릭하면 해당 행(row)을 모델과 리스트에서 삭제
     * - DefaultCellEditor를 상속하여 JButton 클릭 시 동작 정의
     */
    public static class ButtonEditor extends DefaultCellEditor {
        protected JButton button;      // 편집 시 실제로 사용될 JButton
        private int row;               // 현재 선택된 행 인덱스

        /**
         * 생성자
         * @param checkBox DefaultCellEditor 생성을 위한 JCheckBox
         * @param model CategoryTableModel 인스턴스 (removeRow 호출을 위해)
         */
        public ButtonEditor(JCheckBox checkBox, CategoryTableModel model) {
            super(checkBox);
            button = new JButton("X");
            button.addActionListener(_ -> {
                model.removeRow(row); // 선택된 행 삭제
                JOptionPane.showMessageDialog(null, "카테고리가 삭제되었습니다.");
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row; // 편집할 때 선택된 행 인덱스를 캐싱
            return button;  // 해당 셀에 버튼을 표시
        }

        @Override
        public Object getCellEditorValue() {
            return null;    // 버튼 자체는 값이 없으므로 null 반환
        }
    }
}
