// ✅ OrderTypeSelectionUI.java (F10 키 리스너 해제 보완 포함)
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class OrderTypeSelectionUI extends JFrame {
    private long keyPressedTime = 0L;
    private final int TRIGGER_KEY = KeyEvent.VK_F10;
    private Timer holdTimer;
    private boolean adminOpened = false;
    private KeyEventDispatcher dispatcher;

    public boolean isTakeOut;

    public OrderTypeSelectionUI() {
        setTitle("키오스크 시스템");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 720);
        setLocationRelativeTo(null);
        setResizable(false);

        BackgroundPanel bgPanel = new BackgroundPanel("background.png");
        bgPanel.setLayout(null);

        JButton eatInBtn = new JButton("매장");
        eatInBtn.setBounds(40, 520, 200, 130);
        eatInBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        eatInBtn.addActionListener(_ -> {
            isTakeOut = false;
            new KioskUI().setVisible(true);
            dispose();
        });
        bgPanel.add(eatInBtn);

        JButton takeOutBtn = new JButton("포장");
        takeOutBtn.setBounds(280, 520, 200, 130);
        takeOutBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        takeOutBtn.addActionListener(_ -> {
            isTakeOut = true;
            new KioskUI().setVisible(true);
            dispose();
        });
        bgPanel.add(takeOutBtn);

        JButton setLangBtn = new JButton("\uD83C\uDF10");
        setLangBtn.setBounds(445, 10, 50, 50);
        setLangBtn.setFont(new Font(null, Font.PLAIN, 20));
        bgPanel.add(setLangBtn);

        setContentPane(bgPanel);

        // 창 닫힐 때 리스너 해제 보장
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                unregisterKeyDispatcher();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                unregisterKeyDispatcher();
            }
        });
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            registerKeyDispatcher();
        } else {
            unregisterKeyDispatcher();
        }
        super.setVisible(b);
    }

    private void registerKeyDispatcher() {
        dispatcher = new KeyEventDispatcher() {
            private boolean keyHeld = false;

            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == TRIGGER_KEY && !keyHeld) {
                    keyHeld = true;
                    keyPressedTime = System.currentTimeMillis();
                    holdTimer = new Timer(100, _ -> {
                        if ((System.currentTimeMillis() - keyPressedTime) >= 100 && !adminOpened) {
                            adminOpened = true;
                            SwingUtilities.invokeLater(() -> {
                                new AdminUI().setVisible(true);
                                dispose();
                            });
                        }
                    });
                    holdTimer.setRepeats(false);
                    holdTimer.start();
                } else if (e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == TRIGGER_KEY) {
                    keyHeld = false;
                    keyPressedTime = 0L;
                    if (holdTimer != null) holdTimer.stop();
                }
                return false;
            }
        };
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(dispatcher);
    }

    private void unregisterKeyDispatcher() {
        if (dispatcher != null) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(dispatcher);
            dispatcher = null;
        }
    }
}
