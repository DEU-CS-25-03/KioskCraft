import javax.swing.*;
import java.util.Objects;

public class RegistCategoryUI extends JDialog {
    public RegistCategoryUI(JFrame owner, String title, boolean modal) {
        super(owner, title, modal);
        setLayout(null);
        setSize(240, 130);
        SwingUtilities.invokeLater(this::requestFocusInWindow);
        setLocationRelativeTo(null);
        setResizable(false);

        JLabel categoryNameLabel = new JLabel("카테고리 이름: ");
        categoryNameLabel.setBounds(10, 10, 100, 30);
        add(categoryNameLabel);

        JTextField categoryNameField = new JTextField();
        categoryNameField.setBounds(90, 10, 120, 30);
        add(categoryNameField);

        JButton confirmBtn = new JButton("등록");
        confirmBtn.setBounds(10, 50, 95, 30);
        confirmBtn.addActionListener(_ -> {
            if (Objects.equals(categoryNameField.getText(), ""))
                JOptionPane.showMessageDialog(null, "카테고리 이름을 작성해주세요.");
            else{
                JOptionPane.showMessageDialog(null, "카테고리가 등록되었습니다.");
                categoryNameField.setText("");
            }
        });
        add(confirmBtn);

        JButton cancelBtn = new JButton("취소");
        cancelBtn.setBounds(115, 50, 95, 30);
        cancelBtn.addActionListener(_ -> dispose());
        add(cancelBtn);
    }
}