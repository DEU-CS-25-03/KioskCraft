package Controller;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * KioskUIUtils 클래스
 * - 배경 이미지 패널과 키 홀드 동작 디스패처를 제공
 */
public class KioskUIUtils {
    /**
     * KeyHoldActionDispatcher 클래스
     * - 특정 키를 일정 시간 이상 눌렀을 때 지정된 동작을 실행
     */
    public static class KeyHoldActionDispatcher {
        private final int triggerKey;      // 트리거 키 코드 (KeyEvent.VK_*)
        private final int holdMillis;      // 유지 시간(ms)
        private final Runnable action;     // 조건 만족 시 실행할 동작
        private Timer holdTimer;           // 키 누름 시간 체크용 타이머
        private boolean keyHeld = false;   // 키가 현재 눌려 있는지 여부
        private long keyPressedTime = 0L;  // 키가 눌린 시점(ms)
        private KeyEventDispatcher dispatcher; // 등록된 디스패처

        /**
         * 생성자
         * @param triggerKey 트리거로 사용할 키 코드
         * @param holdMillis 키를 이 시간만큼 눌러야 트리거 발생
         * @param action 트리거 발생 시 실행할 Runnable
         */
        public KeyHoldActionDispatcher(int triggerKey, int holdMillis, Runnable action) {
            this.triggerKey = triggerKey;
            this.holdMillis = holdMillis;
            this.action = action;
        }

        /**
         * 디스패처 등록
         * - 키 이벤트를 모니터링하여 일정 시간 키가 눌리면 action을 실행
         */
        public void register() {
            dispatcher = e -> {
                if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == triggerKey && !keyHeld) {
                    keyHeld = true;
                    keyPressedTime = System.currentTimeMillis();
                    holdTimer = new Timer(holdMillis, _ -> {
                        long elapsed = System.currentTimeMillis() - keyPressedTime;
                        if (elapsed >= holdMillis) {
                            SwingUtilities.invokeLater(action);
                        }
                    });
                    holdTimer.setRepeats(false);
                    holdTimer.start();
                } else if (e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == triggerKey) {
                    keyHeld = false;
                    keyPressedTime = 0L;
                    if (holdTimer != null) {
                        holdTimer.stop();
                    }
                }
                return false; // 다른 디스패처도 이벤트를 처리할 수 있도록 false 반환
            };
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(dispatcher);
        }

        /**
         * 디스패처 해제
         * - 더 이상 키 이벤트를 모니터링하지 않도록 제거
         */
        public void unregister() {
            if (dispatcher != null) {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(dispatcher);
                dispatcher = null;
            }
        }
    }
}