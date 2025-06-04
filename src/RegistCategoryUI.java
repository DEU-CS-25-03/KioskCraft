import DataTransferObject.Entity;
import DataAccessObject.CategoryDAO;
import DataTransferObject.CategoryDTO;

import javax.swing.*;

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

        // 오픈시 포커스 강제
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        // 카테고리명 입력 라벨/필드
        JLabel categoryNameLabel = new JLabel("카테고리 이름: ");
        categoryNameLabel.setBounds(10, 10, 100, 30);
        add(categoryNameLabel);

        JTextField categoryNameField = new JTextField();
        categoryNameField.setBounds(90, 10, 120, 30);
        add(categoryNameField);

        // 등록 버튼
        JButton confirmBtn = new JButton("등록");
        confirmBtn.setBounds(10, 50, 95, 30);
        confirmBtn.addActionListener(_ -> {
            String newCategory = categoryNameField.getText().trim();
            if (newCategory.isEmpty()) {
                JOptionPane.showMessageDialog(this, "카테고리 이름을 작성해주세요.");
                return;
            }
            try {
                // [커넥션 풀] CategoryDAO는 커넥션을 멤버로 갖지 않고, 각 메서드에서 DBManager.getConnection() 사용
                CategoryDAO dao = new CategoryDAO();
                dao.insertCategory(new CategoryDTO(newCategory));
                Entity.refreshCategories();
                JOptionPane.showMessageDialog(this, "카테고리가 등록되었습니다.");
                categoryNameField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "카테고리 등록 실패: " + ex.getMessage());
            }
        });
        add(confirmBtn);

        // 취소 버튼
        JButton cancelBtn = new JButton("취소");
        cancelBtn.setBounds(115, 50, 95, 30);
        cancelBtn.addActionListener(_ -> dispose());
        add(cancelBtn);
    }
}
