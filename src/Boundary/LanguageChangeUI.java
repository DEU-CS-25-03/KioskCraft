package Boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import static Controller.DesignControl.getTableModel;

/**
 * LanguageChangeUI 클래스
 * - 다이얼로그 형태로 언어 변경 옵션을 테이블로 표시
 * - 테이블에서 한 행만 선택 가능하며, 확인 버튼 클릭 시 메시지 출력 후 닫힘
 */
public class LanguageChangeUI extends JDialog {

    public LanguageChangeUI(JFrame owner, String title, boolean modal) {
        super(owner, title, modal);
        setLayout(null);
        setSize(284, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        // 테이블 모델 가져와서 테이블 생성 (단일 선택만 허용)
        DefaultTableModel model = getTableModel();
        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        table.setRowHeight(35);

        // 테이블을 스크롤 영역에 담아 위치 지정
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 10, 250, 350);
        add(scrollPane);

        // 확인 버튼: 클릭 시 언어 변경 메시지 표시 후 다이얼로그 닫기
        JButton setLangBtn = new JButton("확인");
        setLangBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        setLangBtn.setBounds(10, 370, 122, 30);
        setLangBtn.addActionListener(_ -> {
            JOptionPane.showMessageDialog(this, "언어가 변경되었습니다.");
            dispose();
        });
        add(setLangBtn);

        // 취소 버튼: 클릭 시 다이얼로그 닫기
        JButton cancelBtn = new JButton("취소");
        cancelBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        cancelBtn.setBounds(142, 370, 122, 30);
        cancelBtn.addActionListener(_ -> dispose());
        add(cancelBtn);
    }
}
