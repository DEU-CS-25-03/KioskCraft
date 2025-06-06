package Boundary.Customer;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PaymentStartUI extends JFrame {

    // UI 구성 요소 선언
    public JButton creditCardBtn;
    public JButton couponInputBtn;
    public JButton pointsEarnBtn;
    public JButton cancelPaymentBtn;
    public JButton showPhonNumberBtn; // 오타 감안하여 유지
    public int paymentAmount;
    public int totalPrice;
    public Map<String, Integer> selectedMenus;

    public PaymentStartUI() {
        setTitle("결제 화면");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 화면 가운데 정렬

        selectedMenus = new HashMap<>();
        paymentAmount = 0;
        totalPrice = 0;

        // 메인 패널 설정
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        //신용 카드 버튼
        creditCardBtn = new JButton("신용카드");
        creditCardBtn.setPreferredSize(new Dimension(300, 150));

        // ★ 클릭 시 PaymentWaitingUI로 이동
        creditCardBtn.addActionListener(e -> {
            new PaymentWaitingUI(); // 새 창 실행
            dispose(); // 현재 창 닫기
        });

        // 쿠폰 입력
        couponInputBtn = new JButton("쿠폰입력");
        couponInputBtn.setPreferredSize(new Dimension(200, 60));

        // 포인트 정립
        pointsEarnBtn = new JButton("포인트 정립");
        pointsEarnBtn.setPreferredSize(new Dimension(200, 60));



        // 버튼 배치
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        buttonPanel.add(creditCardBtn, gbc);


        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)), gbc);
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        buttonPanel.add(couponInputBtn, gbc);

        gbc.gridx = 1;
        buttonPanel.add(pointsEarnBtn, gbc);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // 하단 정보 패널
        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel infoLabel = new JLabel("주문금액 - 마일리지 금액 - 쿠폰 할인금액 = 결제금액");
        infoLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16)); // ★ 라벨 글자 크기 키움
        infoPanel.add(infoLabel);

        mainPanel.add(infoPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PaymentStartUI::new);
    }
}
