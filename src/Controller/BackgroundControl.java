package Controller;

import javax.swing.*;
import java.awt.*;

public class BackgroundControl {
    /**
     * BackgroundPanel 클래스
     * - 주어진 이미지 파일을 패널 배경으로 표시
     */
    public static class BackgroundPanel extends JPanel {
        private final Image backgroundImage; // 배경으로 사용할 이미지

        /**
         * 생성자
         * @param imagePath 배경 이미지 파일 경로
         */
        public BackgroundPanel(String imagePath) {
            backgroundImage = new ImageIcon(imagePath).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // 패널 크기에 맞춰 이미지를 그린다
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
