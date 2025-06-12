package Controller;

import DataTransferObject.Language;

import javax.swing.table.DefaultTableModel;

/**
 * 언어 변경 UI에서 사용할 테이블 모델을 제공하는 컨트롤러
 */
public class LanguageControl {

    /**
     * getTableModel 메서드
     * - LanguageEntity.languages에서 언어 목록을 가져와 테이블 모델로 반환
     * - 모든 셀 편집 불가 설정
     */
    public static DefaultTableModel getTableModel() {
        // LanguageEntity에서 언어 목록을 가져와 Object 배열로 변환
        String[] columnNames = { "언어 목록" };
        Object[][] rowData = new Object[Language.languages.size()][1];
        for (int i = 0; i < Language.languages.size(); i++) {
            rowData[i][0] = Language.languages.get(i);
        }

        // 테이블 모델 생성 및 셀 편집 불가 설정
        return new DefaultTableModel(rowData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
}
