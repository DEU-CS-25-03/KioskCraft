package Boundary.PaymentSystem;

import Boundary.OrderTypeSelectionUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaymentWaitingUI extends JFrame {

    // 필드 정의
    public int leftSecond = 10;
    public JLabel leftSecondLabel;

    private Timer countdownTimer;

    public PaymentWaitingUI() {
        setTitle("카드 대기 화면");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 화면 중앙 정렬

        // 메인 패널
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 상단 텍스트
        JLabel titleLabel = new JLabel("카드를 넣어 주세요");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        panel.add(titleLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        // 대기시간 레이블
        leftSecondLabel = new JLabel("대기시간: " + leftSecond + "초");
        leftSecondLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(leftSecondLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        // 이미지 버튼 (둥근 형태 흉내)
        ImageIcon qrIcon = new ImageIcon("Payment_QR.png"); // 이미지 경로에 맞게 수정
        Image img = qrIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        JButton imageButton = new JButton(new ImageIcon(img));
        imageButton.setPreferredSize(new Dimension(300, 300));
        imageButton.setMaximumSize(new Dimension(300, 300));
        imageButton.setBackground(Color.MAGENTA);
        imageButton.setFocusPainted(false);
        imageButton.setBorder(BorderFactory.createEmptyBorder());
        imageButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        // 버튼 클릭 시 결제 완료 후 초기화면으로 이동
        imageButton.addActionListener(e -> {
            if (countdownTimer != null && countdownTimer.isRunning()) {
                countdownTimer.stop(); // 타이머 멈춤
            }
            JOptionPane.showMessageDialog(this, "결제가 완료되었습니다.");
            new OrderTypeSelectionUI(); // 초기화면(키오스크 메인)으로 이동
        });
        panel.add(imageButton);

        panel.add(Box.createVerticalGlue());

        // 이전 화면으로 버튼
        JButton backButton = new JButton("이전화면으로");
        backButton.setBackground(Color.BLUE);
        backButton.setForeground(Color.WHITE);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(backButton);
        // 크기 키우기 (예: 가로 300, 세로 60)
        backButton.setPreferredSize(new Dimension(300, 60));
        panel.add(backButton);

        // ★ 클릭 시 PaymentStartUII로 이동
        backButton.addActionListener(e -> {
            new PaymentStartUI(); // 새 창 실행
            dispose(); // 현재 창 닫기
        });

        add(panel);
        setVisible(true);

        // 타이머 시작
        countLeftSecond();

        // 이전 화면 버튼 이벤트 (샘플 액션)
        backButton.addActionListener(e -> {
            if (countdownTimer != null) countdownTimer.stop();
            dispose(); // 현재 창 닫기
            // 이전 화면으로 전환 코드 위치
        });
    }

    public void checkInputCreditCard() {
        // 카드 인식 확인 로직 구현 (샘플)
        System.out.println("카드가 인식되었습니다.");
    }

    public void countLeftSecond() {
        countdownTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                leftSecond--;
                leftSecondLabel.setText("대기시간: " + leftSecond + "초");
                if (leftSecond <= 0) {
                    countdownTimer.stop();
                    JOptionPane.showMessageDialog(null, "시간 초과");
                    // 필요시 자동 취소 처리
                    new PaymentStartUI();
                    dispose();
                }
            }
        });
        countdownTimer.start();
    }
}