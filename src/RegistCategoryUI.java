import DTO.Entity;

import javax.swing.*;
import java.util.Objects;

public class RegistCategoryUI extends JDialog {
    /**
     * 카테고리 등록 다이얼로그 생성자
     * @param owner 부모 프레임
     * @param title 다이얼로그 제목
     * @param modal 모달 여부
     */
    public RegistCategoryUI(JFrame owner, String title, boolean modal) {
        super(owner, title, modal);

        setLayout(null);
        setSize(240, 130);
        setLocationRelativeTo(null);
        setResizable(false);

        /*
         * 오픈시 포커스 강제 (엔터키 입력 등 편의)
         */
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        /*
         * 카테고리명 입력 라벨/필드
         */
        JLabel categoryNameLabel = new JLabel("카테고리 이름: ");
        categoryNameLabel.setBounds(10, 10, 100, 30);
        add(categoryNameLabel);

        JTextField categoryNameField = new JTextField();
        categoryNameField.setBounds(90, 10, 120, 30);
        add(categoryNameField);

        /*
         * 등록 버튼
         * - 빈 값이면 알림
         * - 정상 입력시 "등록되었습니다" 메시지, 입력칸 초기화
         */
        JButton confirmBtn = new JButton("등록");
        confirmBtn.setBounds(10, 50, 95, 30);
        confirmBtn.addActionListener(_ -> {
            if (Objects.equals(categoryNameField.getText(), ""))
                JOptionPane.showMessageDialog(null, "카테고리 이름을 작성해주세요.");
            else {
                for(String category: Entity.categories){
                    if(categoryNameField.getText().equals(category)){
                        JOptionPane.showMessageDialog(null, "중복된 카테고리가 존재합니다.");
                        return;
                    }
                }
                Entity.categories.add(categoryNameField.getText());
                JOptionPane.showMessageDialog(null, "카테고리가 등록되었습니다.");
                categoryNameField.setText("");
            }
        });
        add(confirmBtn);

        /*
         * 취소 버튼
         * - 다이얼로그 닫기
         */
        JButton cancelBtn = new JButton("취소");
        cancelBtn.setBounds(115, 50, 95, 30);
        cancelBtn.addActionListener(_ -> dispose());
        add(cancelBtn);
    }
}
