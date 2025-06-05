package Boundary;

import DataAccessObject.DBManager;
import DataAccessObject.MenuDAO;
import DataTransferObject.Entity;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.sql.SQLException;

public class RegistMenuUI extends JDialog {
    private final DefaultTableModel parentModel;

    public RegistMenuUI(JFrame owner, String title, boolean modal, DefaultTableModel model) {
        super(owner, title, modal);
        this.parentModel = model;
        setLayout(null);
        setSize(305, 260);
        setLocationRelativeTo(null);
        setResizable(false);
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        // --- 메뉴명 ---
        JLabel menuLabel = new JLabel("메뉴 이름:");
        menuLabel.setBounds(10, 10, 80, 30);
        add(menuLabel);

        JTextField menuField = new JTextField();
        menuField.setBounds(90, 10, 190, 30);
        add(menuField);

        // --- 카테고리 (콤보박스) ---
        JLabel categoryLabel = new JLabel("카테고리:");
        categoryLabel.setBounds(10, 50, 80, 30);
        add(categoryLabel);

        JComboBox<String> categoryCombo = new JComboBox<>(Entity.categories.toArray(new String[0]));
        categoryCombo.setBounds(90, 50, 190, 30);
        add(categoryCombo);

        // --- 가격 ---
        JLabel priceLabel = new JLabel("가격:");
        priceLabel.setBounds(10, 90, 80, 30);
        add(priceLabel);

        JTextField priceField = new JTextField();
        priceField.setBounds(90, 90, 190, 30);
        add(priceField);

        // --- 이미지 경로 ---
        JLabel imgPathLabel = new JLabel("이미지 경로:");
        imgPathLabel.setBounds(10, 130, 80, 30);
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

        JButton confirmBtn = new JButton("등록");
        confirmBtn.setBounds(10, 170, 130, 40);
        confirmBtn.addActionListener(_ -> {
            String name = menuField.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();
            String price = priceField.getText().trim();
            String imgPath = imgPathField.getText().trim();

            // 빈 칸 체크
            if (name.isEmpty() || category == null || price.isEmpty() || imgPath.isEmpty()) {
                JOptionPane.showMessageDialog(this, "빈 칸 없이 작성해주세요.");
                return;
            }

            // 가격 체크: 숫자로만
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

            String priceStr = String.format("%,d원", Integer.parseInt(price));

            // Entity.menus에 추가할 Object[] 배열 생성
            Object[] newRow = new Object[]{ category, name, priceStr, false, imgPath };
            Entity.menus.add(newRow);

            // DB에 삽입
            try {
                // 인스턴스 이렇게 쓸 때 노란 줄 뜰 수 있는데 무시해도 돼요
                new MenuDAO(DBManager.getConnection());
                MenuDAO.insertMenu(category, name, price, false, imgPath);

                // 테이블 모델에도 새 행 추가
                parentModel.addRow(newRow);

                JOptionPane.showMessageDialog(this, "메뉴가 등록되었습니다.");

                // 입력값 초기화
                menuField.setText("");
                priceField.setText("");
                imgPathField.setText("");
                categoryCombo.setSelectedIndex(0);

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                JOptionPane.showMessageDialog(this, "메뉴 등록 중 오류 발생: " + ex.getMessage(),
                        "오류", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(confirmBtn);

        // --- 취소 버튼 ---
        JButton cancelBtn = new JButton("취소");
        cancelBtn.setBounds(150, 170, 130, 40);
        cancelBtn.addActionListener(_ -> dispose());
        add(cancelBtn);
    }
}
