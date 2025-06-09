package Boundary.PaymentSystem;
import javax.swing.*;
import java.awt.*;

public class PointActionUI extends JFrame {

    public int phoneNumber;
    public JButton mileageCheckBtn;
    public JButton confirmBtn;
    public JButton cancelBtn;

    private final JTextField phoneInputField;
    public int totalPrice;
    public PointActionUI(int totalPrice) {
        setTitle("포인트 입력");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.totalPrice = totalPrice;

        // 전체 레이아웃
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // 상단 전화번호 입력창
        phoneInputField = new JTextField();
        phoneInputField.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        phoneInputField.setEditable(false);
        phoneInputField.setHorizontalAlignment(JTextField.CENTER);
        phoneInputField.setPreferredSize(new Dimension(600, 80));
        mainPanel.add(phoneInputField, BorderLayout.NORTH);

        // 가운데 숫자 키패드 (3x4)
        JPanel keypadPanel = new JPanel();
        keypadPanel.setLayout(new GridLayout(4, 3, 10, 10));
        String[] keys = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "←", "0", "전체삭제"};

        for (String key : keys) {
            JButton keyBtn = new JButton(key);
            keyBtn.setFont(new Font("맑은 고딕", Font.BOLD, 24));
            keyBtn.setBackground(new Color(60, 120, 210));
            keyBtn.setForeground(Color.WHITE);
            keypadPanel.add(keyBtn);

            keyBtn.addActionListener(e -> handleKeypadInput(key));
        }

        mainPanel.add(keypadPanel, BorderLayout.CENTER);

        // 하단 버튼 (이전화면, 다음으로)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 20));

        cancelBtn = new JButton("이전화면으로");
        cancelBtn.addActionListener(_ -> {
            new PaymentStartUI(totalPrice); // 새 창 실행
            dispose(); // 현재 창 닫기
        });

        confirmBtn = new JButton("다음으로");
        confirmBtn.addActionListener(_ -> {
            //하이픈 없는 전화번호 정규식
            String regex = "^01[016789]\\d{7,8}$";
                if (!phoneInputField.getText().matches(regex)) {
                    JOptionPane.showMessageDialog(null, "유효하지 않은 번호입니다.");
                    return;
                }
                JOptionPane.showMessageDialog(null, phoneInputField.getText()+"번호로 적립되었습니다.");
                new PaymentStartUI(totalPrice); // 새 창 실행
                dispose(); // 현재 창 닫기
        });

        cancelBtn.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        confirmBtn.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        cancelBtn.setBackground(new Color(60, 120, 210));
        confirmBtn.setBackground(new Color(60, 120, 210));
        cancelBtn.setForeground(Color.WHITE);
        confirmBtn.setForeground(Color.WHITE);
        cancelBtn.setPreferredSize(new Dimension(200, 60));
        confirmBtn.setPreferredSize(new Dimension(200, 60));

        bottomPanel.add(cancelBtn);
        bottomPanel.add(confirmBtn);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // 포인트 조회 버튼 (옵션, 클래스 다이어그램 기반)
        mileageCheckBtn = new JButton("포인트 조회"); // 실제 UI에는 넣지 않았지만 필요한 경우 사용

        add(mainPanel);
        setVisible(true);
    }

    // 숫자 키패드 입력 처리
    private void handleKeypadInput(String input) {
        String current = phoneInputField.getText();

        switch (input) {
            case "←":
                if (!current.isEmpty()) {
                    phoneInputField.setText(current.substring(0, current.length() - 1));
                }
                break;
            case "전체삭제":
                phoneInputField.setText("");
                break;
            default:
                if (current.length() < 11) { // 최대 11자리 전화번호 제한
                    phoneInputField.setText(current + input);
                }
        }
    }
}

