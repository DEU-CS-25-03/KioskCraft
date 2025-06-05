package Boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import static Control.DesignControl.getTableModel;

public class LanguageChangeUI extends JDialog {
    public LanguageChangeUI(JFrame owner, String title, boolean modal) {
        super(owner, title, modal);
        setLayout(null);
        setSize(284, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        // 디자인 목록을 테이블로 표시 (단일 선택만 허용)
        DefaultTableModel model = getTableModel();
        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        table.setRowHeight(35);

        // 스크롤 팬 추가
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 10, 250, 350);
        add(scrollPane);

        // 디자인 변경 시 알림 표시 및 에러 처리
        JButton setLangBtn = new JButton("확인");
        setLangBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        setLangBtn.setBounds(10, 370, 122, 30);
        setLangBtn.addActionListener(_ -> {
            JOptionPane.showMessageDialog(null, "언어가 변경되었습니다.");
            dispose();
        });
        add(setLangBtn);

        // 취소 버튼
        JButton cancelBtn = new JButton("취소");
        cancelBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        cancelBtn.setBounds(142, 370, 122, 30);
        cancelBtn.addActionListener(_ -> dispose());
        add(cancelBtn);
    }
}
