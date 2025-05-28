import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class OrderTypeSelectionUI extends JFrame {
    private long keyPressedTime = 0L;
    private final int TRIGGER_KEY = KeyEvent.VK_F10; // 원하는 키 지정
    private Timer holdTimer;
    public boolean isTakeOut;

    public OrderTypeSelectionUI() {
        setTitle("키오스크 시스템");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 720);

        BackgroundPanel bgPanel = new BackgroundPanel("background.png");
        bgPanel.setLayout(new BorderLayout());

        // 버튼 패널 (아래쪽)
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false); // 배경 투명(이미지 안 가리게)
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 30)); // 버튼 간격, 아래 여백

        JButton eatInBtn = new JButton("매장");
        eatInBtn.setPreferredSize(new Dimension(200, 100));

        eatInBtn.addActionListener(_ -> {
            new KioskUI().setVisible(true); // 새 창 띄움
            isTakeOut = false;
            dispose(); // StartFrame 닫음
        });

        JButton takeOutBtn = new JButton("포장");
        takeOutBtn.setPreferredSize(new Dimension(200, 100));

        takeOutBtn.addActionListener(_ -> {
            new KioskUI().setVisible(true); // 새 창 띄움
            isTakeOut = true;
            dispose(); // StartFrame 닫음
        });

        btnPanel.add(eatInBtn);
        btnPanel.add(takeOutBtn);

        // 아래쪽에 버튼 패널 추가
        bgPanel.add(btnPanel, BorderLayout.SOUTH);

        setContentPane(bgPanel);

        // -------------- 여기에 키 리스너 추가 --------------
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == TRIGGER_KEY && keyPressedTime == 0L) {
                    keyPressedTime = System.currentTimeMillis();
                    //테스트 때문에 딜레이 100ms로 줄여놓음 나중에 3000ms으로 변경 예정
                    holdTimer = new Timer(100, _ -> {
                        if ((System.currentTimeMillis() - keyPressedTime) >= 100) {
                            openAdminFrame();
                        }
                    });
                    holdTimer.setRepeats(false);
                    holdTimer.start();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == TRIGGER_KEY) {
                    keyPressedTime = 0L;
                    if (holdTimer != null) holdTimer.stop();
                }
            }
        });
        // -------------- 키 리스너 끝 --------------

        setLocationRelativeTo(null);
        setVisible(true);

        // 프레임 뜨자마자 포커스 강제 부여 (중요)
        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    private void openAdminFrame() {
        new AdminUI().setVisible(true);
        dispose();
    }
}