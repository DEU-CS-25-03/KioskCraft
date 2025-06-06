package Control;

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

/**
 * CategoryControl 클래스
 * - Entity.categories 목록을 JTable에 표시하고, 각 행에 삭제 버튼을 제공
 * - 새로운 카테고리를 DB에 삽입하고 Entity.categories에 추가하는 insertCategory 메서드 포함
 */
public class CategoryControl {

    /**
     * CategoryTableModel 클래스
     * - AbstractTableModel을 상속하여 "카테고리" 값과 삭제 버튼("X") 컬럼으로 구성
     * - data 리스트(List<String>)를 통해 실제 카테고리 이름을 관리
     */
    public static class CategoryTableModel extends AbstractTableModel {
        private final String[] columnNames = { "카테고리", "" }; // 컬럼명: 첫 번째는 카테고리, 두 번째는 삭제 버튼
        private final List<String> data;                       // 실제 카테고리 이름을 저장하는 리스트

        public CategoryTableModel(List<String> data) {
            this.data = data;
        }

        /**
         * 지정된 행(row)을 삭제하고 테이블에 변경 사항을 알린다.
         * @param row 삭제할 행 인덱스
         */
        public void removeRow(int row) {
            if (row < 0 || row >= data.size()) {
                return;
            }
            // 1) Entity.categories에서도 동일 인덱스 요소 제거
            Entity.categories.remove(row);
            // 2) 내부 데이터 리스트에서도 요소 제거
            data.remove(row);
            // 3) 테이블 뷰 갱신 알림
            fireTableRowsDeleted(row, row);
        }

        /**
         * 리스트 맨 끝에 새로운 카테고리를 추가하고 테이블에 변경 사항을 알린다.
         * @param categoryName 추가할 카테고리 이름
         */
        public void addRow(String categoryName) {
            // 1) Entity.categories에도 추가
            Entity.categories.add(categoryName);
            // 2) 내부 데이터 리스트에도 추가
            data.add(categoryName);
            int newIndex = data.size() - 1;
            // 3) 테이블 뷰 갱신 알림
            fireTableRowsInserted(newIndex, newIndex);
        }

        @Override
        public int getRowCount() {
            return data.size();               // 행 개수 = 데이터 리스트 크기
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;        // 컬럼 개수 = 2
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];       // 컬럼명 반환
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            // 첫 번째 컬럼(카테고리명)은 편집 불가, 두 번째 컬럼(삭제 버튼)만 편집 가능
            return column == 1;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                // 첫 번째 컬럼: 실제 카테고리 이름 표시
                return data.get(rowIndex);
            } else {
                // 두 번째 컬럼: 삭제 버튼 표시용 텍스트 "X"
                return "X";
            }
        }
    }

    /**
     * ButtonRenderer 클래스
     * - JTable 셀에 'X' 버튼으로 보이도록 렌더링 처리
     * - TableCellRenderer를 구현하여 각 셀에 같은 JButton 인스턴스를 반환
     */
    public static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("X");   // 버튼 텍스트 설정
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return this;    // 항상 이 JButton 컴포넌트를 셀에 렌더링
        }
    }

    /**
     * ButtonEditor 클래스
     * - 삭제 버튼을 클릭하면 해당 행을 모델과 Entity.categories에서 삭제
     * - DefaultCellEditor를 상속하여 JButton 클릭 시 동작 정의
     */
    public static class ButtonEditor extends DefaultCellEditor {
        protected JButton button;                 // 실제로 표시되는 삭제 버튼
        private int row;                          // 현재 클릭된 행 인덱스
        private final CategoryTableModel model;   // 해당 테이블 모델 (removeRow 호출용)

        /**
         * 생성자
         * @param checkBox DefaultCellEditor 생성을 위한 JCheckBox
         * @param model CategoryTableModel 인스턴스 (removeRow 호출을 위해)
         */
        public ButtonEditor(JCheckBox checkBox, CategoryTableModel model) {
            super(checkBox);
            this.model = model;
            button = new JButton("X");
            // 버튼 클릭 시 삭제 로직 수행
            button.addActionListener(_ -> {
                try {
                    deleteCategory(Entity.categories.get(row), row);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                // 모델에서 해당 행 삭제 요청
                model.removeRow(row);
                JOptionPane.showMessageDialog(null, "카테고리가 삭제되었습니다.");
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            // 편집이 시작될 때 선택된 행 인덱스를 저장
            this.row = row;
            return button;  // 해당 셀에 JButton을 표시
        }

        @Override
        public Object getCellEditorValue() {
            // 버튼 자체는 값이 없으므로 null 반환
            return null;
        }

        public CategoryTableModel getModel() {
            return model;
        }
    }

    /**
     * insertCategory 메서드
     * - 새로운 카테고리를 DB에 삽입 후 Entity.categories에 추가
     * @param categoryName 새로 추가할 카테고리 이름
     * @throws SQLException DB 삽입 중 오류 발생 시 예외 던짐
     */
    public static void insertCategory(String categoryName) throws SQLException {
        // 1) DB에 INSERT 수행
        try (Connection conn = DBManager.getConnection()) {
            String insertSql = "INSERT INTO test.categoryId (categoryName) VALUES (?)";
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setString(1, categoryName);
                ps.executeUpdate();
            }
            DBManager.closeConnection(conn);
        }
        // 2) Entity.categories에 추가
        Entity.categories.add(categoryName);
        JOptionPane.showMessageDialog(null, "카테고리가 등록되었습니다.");
    }

    /**
     * deleteCategory 메서드
     * - DB에서 카테고리를 삭제하고 Entity.categories에서도 해당 인덱스의 요소 제거
     * @param categoryName 삭제할 카테고리 이름
     * @param rowIndex     Entity.categories 및 모델에서 제거할 인덱스
     * @throws SQLException DB 삭제 중 오류 발생 시 예외 던짐
     */
    public static void deleteCategory(String categoryName, int rowIndex) throws SQLException {
        // 1) DB에서 DELETE 수행
        try (Connection conn = DBManager.getConnection()) {
            String deleteSql = "DELETE FROM test.categoryId WHERE categoryName = ?";
            try (PreparedStatement ps = conn.prepareStatement(deleteSql)) {
                ps.setString(1, categoryName);
                int affected = ps.executeUpdate();
                if (affected == 0) {
                    throw new SQLException("삭제할 카테고리를 찾을 수 없습니다: " + categoryName);
                }
            }
            DBManager.closeConnection(conn);
        }
        // 2) Entity.categories에서 제거
        Entity.categories.remove(rowIndex);
    }

    /**
     * modifyCategory 메서드
     * - DB에서 기존 카테고리 이름을 새 이름으로 업데이트하고,
     *   Entity.categories에서도 해당 인덱스 값을 새 이름으로 교체
     *
     * @param oldName   기존 카테고리 이름
     * @param newName   새 카테고리 이름
     * @param rowIndex  Entity.categories 및 모델에서 수정할 인덱스
     * @throws SQLException DB 업데이트 중 오류 발생 시 예외 던짐
     */
    public static void modifyCategory(String oldName, String newName, int rowIndex) throws SQLException {
        // 1) DB에서 UPDATE 수행
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
            DBManager.closeConnection(conn);
        }
        // 2) Entity.categories에서 해당 인덱스의 값을 새 이름으로 교체
        Entity.categories.set(rowIndex, newName);
    }
}
