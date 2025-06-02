import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class OrderTypeSelectionUI extends JFrame {
    // F10 키 꾹 누름 감지용 디스패처
    private KioskUIUtils.KeyHoldActionDispatcher keyDispatcher;


    // 포장 여부 선택 결과값 (매장: false, 포장: true)
    public boolean isTakeOut;
    public OrderTypeSelectionUI() {
        setTitle("키오스크 시스템");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 720);
        setLocationRelativeTo(null);
        setResizable(false);

        //배경 패널 적용(이미지 리사이즈 포함)
        KioskUIUtils.BackgroundPanel bgPanel = new KioskUIUtils.BackgroundPanel("background.png");
        bgPanel.setLayout(null);

        /*
         * "매장" 버튼
         * - 클릭 시 포장 아님(false) 설정, KioskUI로 진입
         */
        JButton eatInBtn = new JButton("매장");
        eatInBtn.setBounds(40, 520, 200, 130);
        eatInBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        eatInBtn.addActionListener(_ -> {
            isTakeOut = false;
            new KioskUI().setVisible(true);
            dispose();
        });
        bgPanel.add(eatInBtn);

        /*
         * "포장" 버튼
         * - 클릭 시 포장(true) 설정, KioskUI로 진입
         */
        JButton takeOutBtn = new JButton("포장");
        takeOutBtn.setBounds(280, 520, 200, 130);
        takeOutBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        takeOutBtn.addActionListener(_ -> {
            isTakeOut = true;
            new KioskUI().setVisible(true);
            dispose();
        });
        bgPanel.add(takeOutBtn);

        /*
         * 언어 변경 버튼(🌐)
         * - 실제 동작은 미구현, UI만 배치
         */
        JButton setLangBtn = new JButton("\uD83C\uDF10");
        setLangBtn.setBounds(445, 10, 50, 50);
        setLangBtn.setFont(new Font(null, Font.PLAIN, 20));
        setLangBtn.addActionListener(_ -> new LanguageChangeUI(this, "언어 변경", true).setVisible(true));
        bgPanel.add(setLangBtn);

        setContentPane(bgPanel);


        // 창 닫힐 때 키 디스패처(관리자 진입용) 해제 보장

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

    /*
     * 창이 보여질 때(F10 관리자 진입 단축키 등록),
     * 숨겨질 때 해제(메모리/중복 방지)
     */
    @Override
    public void setVisible(boolean b) {
        if (b) {
            keyDispatcher = new KioskUIUtils.KeyHoldActionDispatcher(
                    KeyEvent.VK_F10,   // 트리거 키: F10
                    100,               // 누르는 시간: 100ms 이상
                    () -> {            // 동작: 관리자 페이지로 진입
                        new AdminUI().setVisible(true);
                        dispose();
                    }
            );
            keyDispatcher.register();
        } else {
            unregisterKeyDispatcher();
        }
        super.setVisible(b);
    }

    /*
     * 키 디스패처(관리자 진입 단축키) 해제
     * - 창 닫힐 때 반드시 호출
     */
    private void unregisterKeyDispatcher() {
        if (keyDispatcher != null) {
            keyDispatcher.unregister();
            keyDispatcher = null;
        }
    }
}
