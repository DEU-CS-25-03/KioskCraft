import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class OrderTypeSelectionUI extends JFrame {
    private Timer holdTimer;
    public boolean isTakeOut;
    private long keyPressedTime = 0L;
    private final int TRIGGER_KEY = KeyEvent.VK_F10; // 원하는 키 지정

    public OrderTypeSelectionUI() {
        setTitle("키오스크 시스템");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        //배경 설정
        BackgroundPanel bgPanel = new BackgroundPanel("background.png");
        bgPanel.setLayout(new BorderLayout());

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false); // 배경 투명(이미지 안 가리게)
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 30)); // 버튼 간격, 아래 여백
        bgPanel.add(btnPanel, BorderLayout.SOUTH);

        JButton eatInBtn = new JButton("매장");
        eatInBtn.setPreferredSize(new Dimension(200, 100));

        eatInBtn.addActionListener(_ -> {
            new KioskUI().setVisible(true); // 새 창 띄움
            isTakeOut = false;
            dispose(); // StartFrame 닫음
        });
        btnPanel.add(eatInBtn);

        JButton takeOutBtn = new JButton("포장");
        takeOutBtn.setPreferredSize(new Dimension(200, 100));
        takeOutBtn.addActionListener(_ -> {
            new KioskUI().setVisible(true); // 새 창 띄움
            isTakeOut = true;
            dispose(); // StartFrame 닫음
        });
        btnPanel.add(takeOutBtn);

        setContentPane(bgPanel);

        //waitingSpeacialKey
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == TRIGGER_KEY && keyPressedTime == 0L) {
                    keyPressedTime = System.currentTimeMillis();
                    //딜레이 0.1
                    holdTimer = new Timer(100, _ -> {
                        if ((System.currentTimeMillis() - keyPressedTime) >= 100)
                            openAdminUI();
                    });
                    holdTimer.setRepeats(false);
                    holdTimer.start();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == TRIGGER_KEY) {
                    keyPressedTime = 0L;
                    if (holdTimer != null)
                        holdTimer.stop();
                }
            }
        });
    }

    private void openAdminUI() {
        new AdminUI().setVisible(true);
        dispose();
    }
}