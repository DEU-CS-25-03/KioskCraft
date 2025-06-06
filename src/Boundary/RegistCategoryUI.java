package Boundary;

import DataTransferObject.Entity;
import javax.swing.*;

/**
 * RegistCategoryUI 클래스
 * - 새로운 카테고리를 입력받아 Entity.categories에 추가
 * - 중복 검사 및 빈 값 검사 후 처리
 */
public class RegistCategoryUI extends JDialog {
    public RegistCategoryUI(JFrame owner, String title, boolean modal) {
        super(owner, title, modal);
        setLayout(null);
        setSize(240, 130);
        setLocationRelativeTo(null);
        setResizable(false);
        SwingUtilities.invokeLater(this::requestFocusInWindow); // 오픈 시 포커스 강제

        // 카테고리명 입력 라벨
        JLabel categoryNameLabel = new JLabel("카테고리 이름: ");
        categoryNameLabel.setBounds(10, 10, 100, 30);
        add(categoryNameLabel);

        // 카테고리명 입력 필드
        JTextField categoryNameField = new JTextField();
        categoryNameField.setBounds(90, 10, 120, 30);
        add(categoryNameField);

        // 등록 버튼: 빈 값 검사, 중복 검사, 정상 입력 시 추가
        JButton confirmBtn = new JButton("등록");
        confirmBtn.setBounds(10, 50, 95, 30);
        confirmBtn.addActionListener(_ -> {
            String input = categoryNameField.getText().trim();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(this, "카테고리 이름을 작성해주세요.");
                return;
            }
            for (String category : Entity.categories) {
                if (input.equals(category)) {
                    JOptionPane.showMessageDialog(this, "중복된 카테고리가 존재합니다.");
                    return;
                }
            }
            Entity.categories.add(input);
            JOptionPane.showMessageDialog(this, "카테고리가 등록되었습니다.");
            categoryNameField.setText(""); // 입력 필드 초기화
        });
        add(confirmBtn);

        // 취소 버튼: 다이얼로그 닫기
        JButton cancelBtn = new JButton("취소");
        cancelBtn.setBounds(115, 50, 95, 30);
        cancelBtn.addActionListener(_ -> dispose());
        add(cancelBtn);
    }
}