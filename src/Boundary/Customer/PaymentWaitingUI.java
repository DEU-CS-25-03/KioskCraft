package Boundary.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaymentWaitingUI extends JFrame {

    // 필드 정의
    public int leftSecond = 40;
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
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // 대기시간 레이블
        leftSecondLabel = new JLabel("대기시간: " + leftSecond + "초");
        leftSecondLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(leftSecondLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // 이미지 버튼 (둥근 형태 흉내)
        JButton imageButton = new JButton("이미지");
        imageButton.setPreferredSize(new Dimension(100, 100));
        imageButton.setMaximumSize(new Dimension(100, 100));
        imageButton.setBackground(Color.BLUE);
        imageButton.setForeground(Color.WHITE);
        imageButton.setFocusPainted(false);
        imageButton.setBorder(BorderFactory.createEmptyBorder());
        imageButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(imageButton);

        panel.add(Box.createVerticalGlue());

        // 이전 화면으로 버튼
        JButton backButton = new JButton("이전화면으로");
        backButton.setBackground(Color.BLUE);
        backButton.setForeground(Color.WHITE);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
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
        countdownTimer = new Timer(400, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                leftSecond--;
                leftSecondLabel.setText("대기시간: " + leftSecond + "초");
                if (leftSecond <= 0) {
                    countdownTimer.stop();
                    JOptionPane.showMessageDialog(null, "시간 초과");
                    // 필요시 자동 취소 처리
                }
            }
        });
        countdownTimer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PaymentWaitingUI::new);
    }
}

