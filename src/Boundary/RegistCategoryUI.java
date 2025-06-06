package Boundary;

import Controller.CategoryControl;
import DataTransferObject.Entity;

import javax.swing.*;
import java.sql.SQLException;

/**
 * RegistCategoryUI 클래스
 * - 새로운 카테고리를 입력받아 DB, Entity.categories에 추가하고
 *   등록된 카테고리를 즉시 갱신할 수 있는 다이얼로그 UI
 */
public class RegistCategoryUI extends JDialog {
    /**
     * 생성자: 카테고리 등록 다이얼로그를 초기화하고 표시하는 역할을 수행
     *
     * @param owner 부모 JFrame (카테고리 관리 화면)
     * @param title 다이얼로그 제목
     * @param modal 모달 여부 (true로 설정 시 이 다이얼로그가 닫힐 때까지 부모 창 비활성화)
     */
    public RegistCategoryUI(JFrame owner, String title, boolean modal) {
        super(owner, title, modal);
        setLayout(null);
        setSize(245, 130);
        setLocationRelativeTo(null);
        setResizable(false);
        SwingUtilities.invokeLater(this::requestFocusInWindow); // 다이얼로그 오픈 시 포커스 강제

        // 1) 카테고리명 입력을 위한 라벨 생성
        JLabel categoryNameLabel = new JLabel("카테고리 이름:");
        categoryNameLabel.setBounds(10, 10, 100, 30);
        add(categoryNameLabel);

        // 2) 카테고리명 입력을 위한 텍스트 필드 생성
        JTextField categoryNameField = new JTextField();
        categoryNameField.setBounds(100, 10, 120, 30);
        add(categoryNameField);

        // 3) 등록 버튼 생성 및 액션 설정
        JButton confirmBtn = new JButton("등록");
        confirmBtn.setBounds(10, 50, 100, 30);
        confirmBtn.addActionListener(_ -> {
            String input = categoryNameField.getText().trim();
            // 3-1) 빈 문자열 검사
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(this, "카테고리 이름을 작성해주세요.");
                return;
            }
            // 3-2) Entity.categories 리스트에서 중복 여부 검사
            for (String category : Entity.categories) {
                if (input.equals(category)) {
                    JOptionPane.showMessageDialog(this, "중복된 카테고리가 존재합니다.");
                    return;
                }
            }
            // 3-3) DB에 INSERT 수행 후, Entity.categories 및 테이블 모델 업데이트
            try {
                CategoryControl.insertCategory(input);
                categoryNameField.setText(""); // 입력 필드 초기화
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "카테고리 등록 중 오류 발생:\n" + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(confirmBtn);

        // 4) 취소 버튼 생성 및 액션 설정: 클릭 시 다이얼로그 닫기
        JButton cancelBtn = new JButton("취소");
        cancelBtn.setBounds(120, 50, 100, 30);
        cancelBtn.addActionListener(_ -> dispose());
        add(cancelBtn);
    }
}
