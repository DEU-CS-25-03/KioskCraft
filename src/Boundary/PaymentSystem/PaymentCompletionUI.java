package Boundary.PaymentSystem;


import Boundary.KioskUI;

import javax.swing.*;
import java.awt.*;

public class PaymentCompletionUI extends JFrame {

    public int fromDate;
    public int toDate;

    public JButton printByPeroidBtn;
    public JButton printByCategoryBtn;
    public JButton printByChannelBtn;

    public PaymentCompletionUI(int orderNumber) {
        setTitle("결제 완료");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 화면 중앙 정렬

        // 메인 패널 설정
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        panel.setBackground(Color.WHITE);

        // "주문이 완료되었습니다."
        JLabel messageLabel = new JLabel("주문이 완료되었습니다.");
        messageLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 24));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 30, 0));
        panel.add(messageLabel);

        // "주문번호:"
        JLabel orderTextLabel = new JLabel("주문번호:");
        orderTextLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
        orderTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(orderTextLabel);

        // 실제 주문번호 (크게 표시)
        JLabel orderNumberLabel = new JLabel(String.valueOf(orderNumber));
        orderNumberLabel.setFont(new Font("맑은 고딕", Font.BOLD, 60));
        orderNumberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        orderNumberLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 60, 0));
        panel.add(orderNumberLabel);

        // "처음으로" 버튼
        JButton goHomeBtn = new JButton("처음으로");
        goHomeBtn.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        goHomeBtn.setBackground(new Color(60, 120, 210));
        goHomeBtn.setForeground(Color.WHITE);
        goHomeBtn.setFocusPainted(false);
        goHomeBtn.setPreferredSize(new Dimension(200, 60));
        goHomeBtn.setMaximumSize(new Dimension(300, 60));
        goHomeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        goHomeBtn.addActionListener(e -> {
            dispose(); // 현재 창 닫기
            new KioskUI(); // 처음으로 이동 로직
        });

        panel.add(goHomeBtn);

        add(panel);
        setVisible(true);
    }

    // 다이어그램에 포함된 버튼들 (예: 통계용)
    public void setupStatsButtons() {
        printByPeroidBtn = new JButton("기간별 출력");
        printByCategoryBtn = new JButton("카테고리별 출력");
        printByChannelBtn = new JButton("채널별 출력");
        // 필요 시 이 버튼들도 레이아웃에 추가하거나 별도 패널로 구성
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PaymentCompletionUI(471));
    }
}
