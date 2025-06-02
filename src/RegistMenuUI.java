import DTO.Entity;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.io.File;

public class RegistMenuUI extends JDialog {
    private final DefaultTableModel parentModel;

    public RegistMenuUI(JFrame owner, String title, boolean modal, DefaultTableModel model) {
        super(owner, title, modal);
        this.parentModel = model;
        setLayout(null);
        setSize(305, 260);
        setLocationRelativeTo(null);
        setResizable(false);

        // 포커스
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        // --- 메뉴명 ---
        JLabel menuLabel = new JLabel("메뉴 이름: ");
        menuLabel.setBounds(10, 10, 100, 30);
        add(menuLabel);

        JTextField menueField = new JTextField();
        menueField.setBounds(90, 10, 190, 30);
        add(menueField);

        // --- 카테고리 (콤보박스) ---
        JLabel categoryLabel = new JLabel("카테고리: ");
        categoryLabel.setBounds(10, 50, 100, 30);
        add(categoryLabel);

        JComboBox<String> categoryCombo = new JComboBox<>(Entity.categories.toArray(new String[0]));
        categoryCombo.setBounds(90, 50, 190, 30);
        add(categoryCombo);

        // --- 가격 ---
        JLabel priceLabel = new JLabel("가격: ");
        priceLabel.setBounds(10, 90, 100, 30);
        add(priceLabel);

        JTextField priceField = new JTextField();
        priceField.setBounds(90, 90, 190, 30);
        add(priceField);

        // --- 이미지 경로 ---
        JLabel imgPathLabel = new JLabel("이미지 경로: ");
        imgPathLabel.setBounds(10, 130, 100, 30);
        add(imgPathLabel);

        JTextField imgPathField = new JTextField();
        imgPathField.setBounds(90, 130, 150, 30);
        add(imgPathField);

        JButton imgPathBtn = new JButton("..");
        imgPathBtn.setBounds(250, 130, 30, 30);
        imgPathBtn.addActionListener(_ -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter(
                    "이미지 파일 (JPG, PNG, JPEG)", "jpg", "jpeg", "png"
            ));
            if (chooser.showOpenDialog(owner) == JFileChooser.APPROVE_OPTION) {
                File sel = chooser.getSelectedFile();
                imgPathField.setText(sel.getAbsolutePath());
            }
        });
        add(imgPathBtn);

        // --- 등록 버튼 액션 ---
        JButton confirmBtn = new JButton("등록");
        confirmBtn.setBounds(10, 170, 130, 40);
        confirmBtn.addActionListener(_ -> {
            String name = menueField.getText().trim();
            String category = (String)categoryCombo.getSelectedItem();
            String price = priceField.getText().trim();
            String imgPath = imgPathField.getText().trim();

            // 빈 칸 체크
            if (name.isEmpty() || category == null || price.isEmpty() || imgPath.isEmpty()) {
                JOptionPane.showMessageDialog(this, "빈 칸 없이 작성해주세요.");
                return;
            }

            // 가격 체크
            if (!price.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "가격은 숫자로만 입력해주세요.");
                return;
            }

            // 중복 체크
            for (Object[] menu : Entity.menus) {
                if (name.equals(menu[1])) {
                    JOptionPane.showMessageDialog(this, "중복된 메뉴가 존재합니다.");
                    return;
                }
            }

            Object[] newRow = new Object[]{ category, name, price, false, imgPath };
            Entity.menus.add(newRow);
            parentModel.addRow(newRow);

            JOptionPane.showMessageDialog(this, "메뉴가 등록되었습니다.");
            menueField.setText("");
            priceField.setText("");
            imgPathField.setText("");
            categoryCombo.setSelectedIndex(0);
        });
        add(confirmBtn);

        // --- 취소 ---
        JButton cancelBtn = new JButton("취소");
        cancelBtn.setBounds(150, 170, 130, 40);
        cancelBtn.addActionListener(_ -> dispose());
        add(cancelBtn);
    }
}
