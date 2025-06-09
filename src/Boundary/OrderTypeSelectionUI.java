package Boundary;

import Controller.BackgroundControl;
import Controller.KioskUIUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * OrderTypeSelectionUI 클래스
 * - 매장/포장 선택 화면을 표시
 * - F10 키 홀드 시 관리자 페이지(AdminUI)로 전환
 */
public class OrderTypeSelectionUI extends JFrame {
    public static boolean isTakeOut; // 포장 여부
    private KioskUIUtils.KeyHoldActionDispatcher keyDispatcher; // F10 키 디스패처

    public OrderTypeSelectionUI() {
        setTitle("키오스크 시스템");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 720);
        setLocationRelativeTo(null);
        setResizable(false);

        // 배경 패널 적용 (이미지 리사이즈 포함)
        BackgroundControl.BackgroundPanel bgPanel = new BackgroundControl.BackgroundPanel("background.png");
        bgPanel.setLayout(null);

        // "매장" 버튼: 클릭 시 isTakeOut=false, KioskUI 열고 현재 창 닫기
        JButton eatInBtn = new JButton("매장");
        eatInBtn.setBounds(40, 520, 200, 130);
        eatInBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        eatInBtn.addActionListener(_ -> {
            isTakeOut = false;
            new KioskUI().setVisible(true);
            dispose();
        });
        bgPanel.add(eatInBtn);

        // "포장" 버튼: 클릭 시 isTakeOut=true, KioskUI 열고 현재 창 닫기
        JButton takeOutBtn = new JButton("포장");
        takeOutBtn.setBounds(280, 520, 200, 130);
        takeOutBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        takeOutBtn.addActionListener(_ -> {
            isTakeOut = true;
            new KioskUI().setVisible(true);
            dispose();
        });
        bgPanel.add(takeOutBtn);

        // 언어 변경 버튼(🌐): 클릭 시 LanguageChangeUI 표시
        JButton setLangBtn = new JButton("\uD83C\uDF10");
        setLangBtn.setBounds(445, 10, 50, 50);
        setLangBtn.setFont(new Font(null, Font.PLAIN, 20));
        setLangBtn.addActionListener(_ -> new LanguageChangeUI(this, "언어 변경", true).setVisible(true));
        bgPanel.add(setLangBtn);

        setContentPane(bgPanel);

        // 창 닫힐 때 키 디스패처 해제 보장
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent e) { unregisterKeyDispatcher(); }
            @Override public void windowClosing(WindowEvent e) { unregisterKeyDispatcher(); }
        });
    }

    /**
     * setVisible 오버라이드
     * - visible=true 시 F10 키 디스패처 등록 (관리자 페이지 진입)
     * - visible=false 시 디스패처 해제
     */
    @Override
    public void setVisible(boolean b) {
        if (b) {
            keyDispatcher = new KioskUIUtils.KeyHoldActionDispatcher(KeyEvent.VK_F10, 100, () -> {
                new AdminUI().setVisible(true);
                dispose();
            });
            keyDispatcher.register();
        } else unregisterKeyDispatcher();
        super.setVisible(b);
    }

    /**
     * 키 디스패처 해제
     * - F10 키 리스너 해제 및 null 처리
     */
    private void unregisterKeyDispatcher() {
        if (keyDispatcher != null) {
            keyDispatcher.unregister();
            keyDispatcher = null;
        }
    }
}