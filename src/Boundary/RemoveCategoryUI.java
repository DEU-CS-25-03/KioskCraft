package Boundary;

import Control.CategoryControl;
import DataTransferObject.Entity;
import javax.swing.*;
import java.awt.*;

/**
 * RemoveCategoryUI 클래스
 * - Entity.categories 목록을 테이블로 표시하여 카테고리 삭제 기능 제공
 * - CategoryControl의 TableModel, ButtonRenderer, ButtonEditor 사용
 */
public class RemoveCategoryUI extends JDialog {
    public RemoveCategoryUI(JFrame owner, String title, boolean modal) {
        super(owner, title, modal);
        setTitle(title);
        setLayout(null);
        setSize(335, 360);
        setLocationRelativeTo(null);
        setResizable(false);

        // CategoryControl 에서 제공하는 카테고리 테이블 모델 생성 (Entity.categories 사용)
        CategoryControl.CategoryTableModel model = new CategoryControl.CategoryTableModel(Entity.categories);
        JTable table = new JTable(model);

        // 테이블 스타일 설정
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.setCellSelectionEnabled(false); // 셀 단위 선택 비활성

        // 마지막 컬럼(삭제 버튼) 렌더러/에디터 등록
        table.getColumn("").setCellRenderer(new CategoryControl.ButtonRenderer());
        table.getColumn("").setCellEditor(new CategoryControl.ButtonEditor(new JCheckBox(), model));

        // "카테고리" 컬럼 너비 고정 (최대/최소/권장 너비 모두 200px)
        table.getColumn("카테고리").setMaxWidth(200);
        table.getColumn("카테고리").setMinWidth(200);
        table.getColumn("카테고리").setPreferredWidth(200);

        // 테이블을 스크롤 패널에 담아 다이얼로그에 추가
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 10, 300, 300);
        add(scrollPane);
    }
}
