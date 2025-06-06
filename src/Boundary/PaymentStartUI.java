package Boundary;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PaymentStartUI extends JFrame {

    // UI 구성 요소 선언
    public JButton creditCardBtn;
    public JButton couponInputBtn;
    public JButton pointsEarnBtn;
    public JButton backButton ;
    public JButton showPhonNumberBtn; // 오타 감안하여 유지
    public int paymentAmount;
    public int totalPrice;
    public Map<String, Integer> selectedMenus;

    // 결제금액 라벨을 필드로 선언 (필요시 동적 갱신 가능)
    private JLabel priceLabel;

    // 장바구니 정보와 총 금액을 받아 생성
    public PaymentStartUI(Map<String, Integer> selectedMenus, int totalPrice) {
        this.selectedMenus = selectedMenus != null ? selectedMenus : new HashMap<>();
        this.totalPrice = totalPrice;
        initUI();
    }

    // 기본 생성자 (빈 장바구니, 0원)
    public PaymentStartUI() {
        this(new HashMap<>(), 0);
    }

    // UI 초기화 메서드
    private void initUI() {
        setTitle("결제 화면");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 화면 가운데 정렬

        // 메인 패널 설정
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 버튼 패널
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);


        // 신용카드 버튼
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        buttonPanel.add(creditCardBtn = new JButton("신용카드"), gbc);
        creditCardBtn.setPreferredSize(new Dimension(300, 150));
        creditCardBtn.addActionListener(e -> {
            new PaymentWaitingUI(); // 새 창 실행
            dispose(); // 현재 창 닫기
        });

        // 쿠폰 입력 버튼
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        buttonPanel.add(couponInputBtn= new JButton("쿠폰입력"), gbc);
        couponInputBtn.setPreferredSize(new Dimension(200, 60));
        // 포인트 정립 버튼
        gbc.gridx = 1;
        buttonPanel.add(pointsEarnBtn = new JButton("포인트 정립"), gbc);
        pointsEarnBtn.setPreferredSize(new Dimension(200, 60));
        // 이전화면 버튼
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        buttonPanel.add(backButton = new JButton("이전 화면으로"), gbc);
        backButton.setPreferredSize(new Dimension(200, 60));
        backButton.addActionListener(e -> {
            new KioskUI(); // 키오스크 메인 화면으로 이동
            dispose();     // 현재 결제 화면 닫기
        });

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // 하단 정보 패널
        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        infoPanel.setLayout(new GridLayout(2, 1));
        // 크기 키우기 (예: 폭은 부모 패널에 맞추고, 높이는 120px로)
        infoPanel.setPreferredSize(new Dimension(0, 120));

        // 결제금액 라벨
        priceLabel = new JLabel("총 결제금액: " + totalPrice + "원", SwingConstants.CENTER);
        priceLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        infoPanel.add(priceLabel);

        // 안내 라벨
        JLabel infoLabel = new JLabel("주문금액 - 마일리지 금액 - 쿠폰 할인금액 = 결제금액", SwingConstants.CENTER);
        infoLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        infoPanel.add(infoLabel);

        mainPanel.add(infoPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }
}
