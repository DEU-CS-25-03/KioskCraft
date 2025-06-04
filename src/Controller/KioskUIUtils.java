package Controller;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class KioskUIUtils {
    public static class BackgroundPanel extends JPanel {
        // 배경에 쓸 이미지 인스턴스
        private final Image backgroundImage;

        /**
         * 생성자: 이미지 경로를 받아서 패널 배경으로 설정
         * @param imagePath 배경 이미지 파일 경로
         */
        public BackgroundPanel(String imagePath) {
            backgroundImage = new ImageIcon(imagePath).getImage();
        }

        /*
         * 패널 그리기 오버라이드
         * - 패널 크기에 맞게 이미지를 자동 조절해서 그림
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /*
     * 특정 키를 지정 시간(밀리초) 동안 꾹 누르면
     * 지정한 동작을 실행하는 키 이벤트 디스패처
     * - 관리자/홈 화면 전환에서 활용
     */
    public static class KeyHoldActionDispatcher {
        // 트리거로 쓸 키 (KeyEvent.VK_*)
        private final int triggerKey;
        // 누르는 시간 기준(ms)
        private final int holdMillis;
        // 조건 충족 시 실행할 람다(동작)
        private final Runnable action;
        private Timer holdTimer;       // 누름 체크용 타이머
        private boolean keyHeld = false; // 현재 눌려있는지 여부
        private long keyPressedTime = 0L; // 눌린 시점(ms)
        private KeyEventDispatcher dispatcher; // 등록할 디스패처 인스턴스

        /**
         * 생성자
         * @param triggerKey 트리거 키(KeyEvent.VK_*)
         * @param holdMillis 트리거 판정 시간(밀리초)
         * @param action 트리거 발생 시 실행할 동작
         */
        public KeyHoldActionDispatcher(int triggerKey, int holdMillis, Runnable action) {
            this.triggerKey = triggerKey;
            this.holdMillis = holdMillis;
            this.action = action;
        }

        /*
         * 디스패처를 현재 포커스 매니저에 등록
         * - 등록 후 해당 키를 꾹 누르면 콜백이 실행됨
         */
        public void register() {
            dispatcher = e -> {
                if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == triggerKey && !keyHeld) {
                    keyHeld = true;
                    keyPressedTime = System.currentTimeMillis();
                    holdTimer = new Timer(holdMillis, _ -> {
                        // 꾹 누른 시간 체크
                        if ((System.currentTimeMillis() - keyPressedTime) >= holdMillis) {
                            SwingUtilities.invokeLater(action);
                        }
                    });
                    holdTimer.setRepeats(false);
                    holdTimer.start();
                } else if (e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == triggerKey) {
                    keyHeld = false;
                    keyPressedTime = 0L;
                    if (holdTimer != null) holdTimer.stop();
                }
                return false;
            };
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(dispatcher);
        }

        /*
         * 디스패처를 포커스 매니저에서 해제
         * - 창 닫힐 때/숨겨질 때 반드시 호출
         */
        public void unregister() {
            if (dispatcher != null) {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(dispatcher);
                dispatcher = null;
            }
        }
    }
}
