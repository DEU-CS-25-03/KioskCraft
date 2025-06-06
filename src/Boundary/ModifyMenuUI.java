package Boundary;

import Control.AdminControl;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * ModifyMenuUI 클래스
 * - AdminUI에서 전달된 DefaultTableModel로 메뉴 수정/삭제 기능 제공
 * - AdminControl의 ButtonRenderer, ButtonEditor 사용
 */
public class ModifyMenuUI extends JDialog {
    public ModifyMenuUI(JFrame owner, String title, boolean modal, DefaultTableModel model) {
        super(owner, title, modal);
        setTitle(title);
        setLayout(null);
        setSize(335, 360);
        setLocationRelativeTo(null);
        setResizable(false);
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        // JTable 생성 및 폰트 설정
        JTable table = new JTable(model);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        table.setRowHeight(35);
        table.setRowSelectionAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);

        // 컬럼 너비 고정: 0=카테고리, 1=메뉴명, 2=가격, 3=품절여부, 4=삭제 버튼
        TableColumnModel colModel = table.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(150);
        colModel.getColumn(1).setPreferredWidth(260);
        colModel.getColumn(2).setPreferredWidth(80);
        colModel.getColumn(3).setPreferredWidth(60);
        colModel.getColumn(4).setPreferredWidth(40);

        // 삭제 버튼 렌더러/에디터 설정: 4번 컬럼에 적용
        table.getColumnModel().getColumn(4).setCellRenderer(new AdminControl.ButtonRenderer());
        table.getColumnModel().getColumn(4).setCellEditor(new AdminControl.ButtonEditor(new JCheckBox(), model));

        // JScrollPane에 테이블 추가 후 다이얼로그에 배치
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 70, 310, 260); // 크기를 다이얼로그 크기에 맞게 조정
        add(scrollPane);
    }
}
