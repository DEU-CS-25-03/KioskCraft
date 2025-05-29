import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.Objects;

public class RegistMenuUI extends JDialog {
    /**
     * 메뉴 등록 다이얼로그 생성자
     * @param owner 부모 프레임
     * @param title 다이얼로그 제목
     * @param modal 모달 여부
     */
    public RegistMenuUI(JFrame owner, String title, boolean modal) {
        super(owner, title, modal);
        setLayout(null);
        setSize(305, 260);
        setLocationRelativeTo(null);
        setResizable(false);

        // 오픈시 포커스 자동 설정(엔터 입력 등 편의)
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        // 메뉴명, 카테고리, 가격, 이미지 경로 입력 필드
        JLabel menuLabel = new JLabel("메뉴 이름: ");
        menuLabel.setBounds(10, 10, 100, 30);
        add(menuLabel);

        JTextField menueField = new JTextField();
        menueField.setBounds(90, 10, 190, 30);
        add(menueField);

        JLabel categoryLabel = new JLabel("카테고리: ");
        categoryLabel.setBounds(10, 50, 100, 30);
        add(categoryLabel);

        JTextField categoryField = new JTextField();
        categoryField.setBounds(90, 50, 190, 30);
        add(categoryField);

        JLabel priceLabel = new JLabel("가격: ");
        priceLabel.setBounds(10, 90, 100, 30);
        add(priceLabel);

        JTextField priceField = new JTextField();
        priceField.setBounds(90, 90, 190, 30);
        add(priceField);

        JLabel imgPathLabel = new JLabel("이미지 경로: ");
        imgPathLabel.setBounds(10, 130, 100, 30);
        add(imgPathLabel);

        JTextField imgPathField = new JTextField();
        imgPathField.setBounds(90, 130, 150, 30);
        add(imgPathField);

        /*
         * 이미지 파일 선택 버튼
         * - 파일 탐색기 열기(JPG, PNG, JPEG만)
         * - 선택 시 이미지 경로 입력란에 경로 반영
         */
        JButton imgPathBtn = new JButton("..");
        imgPathBtn.setBounds(250, 130, 30, 30);
        imgPathBtn.addActionListener(_ -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "이미지 파일 (JPG, PNG, JPEG)", "jpg", "jpeg", "png", "JPG", "JPEG", "PNG");
            chooser.setFileFilter(filter);
            int result = chooser.showOpenDialog(owner);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedDir = chooser.getSelectedFile();
                imgPathField.setText(selectedDir.getAbsolutePath());
            }
        });
        add(imgPathBtn);

        /*
         * 등록 버튼
         * - 빈 칸 있으면 경고
         * - 모두 작성시 등록 메시지 후 입력란 초기화
         */
        JButton confirmBtn = new JButton("등록");
        confirmBtn.setBounds(10, 170, 130, 40);
        confirmBtn.addActionListener(_ -> {
            if (Objects.equals(menueField.getText(), "") ||
                    Objects.equals(categoryField.getText(), "") ||
                    Objects.equals(priceField.getText(), "") ||
                    Objects.equals(imgPathField.getText(), ""))
                JOptionPane.showMessageDialog(null, "빈 칸 없이 작성해주세요.");
            else {
                JOptionPane.showMessageDialog(null, "메뉴가 등록되었습니다.");
                menueField.setText("");
                categoryField.setText("");
                priceField.setText("");
                imgPathField.setText("");
            }
        });
        add(confirmBtn);

        /*
         * 취소 버튼 - 다이얼로그 닫기
         */
        JButton cancelBtn = new JButton("취소");
        cancelBtn.setBounds(150, 170, 130, 40);
        cancelBtn.addActionListener(_ -> dispose());
        add(cancelBtn);
    }
}
