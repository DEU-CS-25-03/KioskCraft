package Control;

import javax.swing.table.DefaultTableModel;

public class DesignControl {
    // 데이터 모델 생성(내용 수정 불가)
    public static DefaultTableModel getTableModel() {
        Object[][] rowData = {{"한국어"}, {"일본어"}, {"중국어"}, {"영어"}};
        String[] columnNames = {"언어 목록"};

        return new DefaultTableModel(rowData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
}
