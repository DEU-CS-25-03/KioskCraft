package Control;

import javax.swing.table.DefaultTableModel;

/**
 * DesignControl 클래스
 * - 언어 변경 UI에서 사용될 테이블 모델을 제공
 * - 수정 불가능한 언어 목록을 반환
 */
public class DesignControl {

    /**
     * getTableModel 메서드
     * - 언어 목록(한국어, 일본어, 중국어, 영어)을 테이블 모델 형태로 반환
     * - 모든 셀 편집 불가 설정
     */
    public static DefaultTableModel getTableModel() {
        Object[][] rowData = {
                { "한국어" },
                { "일본어" },
                { "중국어" },
                { "영어" }
        };
        String[] columnNames = { "언어 목록" };

        return new DefaultTableModel(rowData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 셀 편집 불가
            }
        };
    }
}
