package Boundary.Admin;

import Controller.CategoryTableModel;
import DataTransferObject.Entity;
import DataAccessObject.CategoryDAO;
import DataTransferObject.CategoryDTO;

import javax.swing.*;


public class RegistCategoryUI extends CategoryTableModel {
    private JDialog dialog;
    private JTextField categoryNameField;

    /**
     * 카테고리 등록 다이얼로그 생성자
     *
     * @param owner 부모 프레임
     * @param title 다이얼로그 제목
     * @param modal 모달 여부
     */
    public RegistCategoryUI(JFrame owner, String title, boolean modal) {
        super(Entity.categories); // Controller.CategoryTableModel 생성자 호출

        dialog = new JDialog(owner, title, modal);
        dialog.setLayout(null);
        dialog.setSize(240, 130);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);

        // 오픈시 포커스 강제
        SwingUtilities.invokeLater(dialog::requestFocusInWindow);

        // 카테고리명 입력 라벨/필드
        JLabel categoryNameLabel = new JLabel("카테고리 이름: ");
        categoryNameLabel.setBounds(10, 10, 100, 30);
        dialog.add(categoryNameLabel);

        categoryNameField = new JTextField();
        categoryNameField.setBounds(90, 10, 120, 30);
        dialog.add(categoryNameField);

        // 등록 버튼
        JButton confirmBtn = new JButton("등록");
        confirmBtn.setBounds(10, 50, 95, 30);
        confirmBtn.addActionListener(_ -> onRegisterAsync());
        dialog.add(confirmBtn);

        // 취소 버튼
        JButton cancelBtn = new JButton("취소");
        cancelBtn.setBounds(115, 50, 95, 30);
        cancelBtn.addActionListener(_ -> dialog.dispose());
        dialog.add(cancelBtn);

        dialog.setVisible(true);
    }

    // [비동기] 카테고리 등록 및 동기화
    private void onRegisterAsync() {
        String newCategory = categoryNameField.getText().trim();
        if (newCategory.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "카테고리 이름을 작성해주세요.");
            return;
        }
        new Thread(() -> {
            try {
                CategoryDAO dao = new CategoryDAO();
                dao.insertCategory(new CategoryDTO(newCategory)); // 커넥션 풀 사용
                Entity.refreshCategories(); // 커넥션 풀 사용
                SwingUtilities.invokeLater(() -> {
                    setData(Entity.categories); // 테이블 모델 동기화
                    JOptionPane.showMessageDialog(dialog, "카테고리가 등록되었습니다.");
                    categoryNameField.setText("");
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(dialog, "카테고리 등록 실패: " + ex.getMessage())
                );
            }
        }).start();
    }
}