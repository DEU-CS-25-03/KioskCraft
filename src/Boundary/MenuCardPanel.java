package Boundary;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * MenuCardPanel 클래스
 * - 메뉴 이미지, 이름, 가격(품절 여부 포함)을 카드 형태로 표시
 * - 클릭 시 onClick Runnable 실행
 */
public class MenuCardPanel extends JPanel {
    public MenuCardPanel(String name, String price, String imagePath, boolean isDefault, boolean soldOut, Runnable onClick) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(150, 150));
        setBorder(new LineBorder(Color.BLACK));

        // 이미지 라벨 생성: 기본 이미지(isDefault) 또는 실제 이미지
        JLabel imgLabel;
        if (isDefault) {
            BufferedImage placeholder = new BufferedImage(60, 60, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = placeholder.createGraphics();
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillRect(0, 0, 60, 60);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
            g2.drawString("Default Img", 2, 35);
            g2.dispose();
            imgLabel = new JLabel(new ImageIcon(placeholder));
        } else {
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            imgLabel = new JLabel(new ImageIcon(img));
        }
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 메뉴명과 가격 라벨 생성 (품절 시 회색 처리)
        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        JLabel priceLabel = new JLabel(price + (soldOut ? " (품절)" : ""), SwingConstants.CENTER);
        priceLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        if (soldOut) {
            imgLabel.setEnabled(false);
            nameLabel.setForeground(Color.GRAY);
            priceLabel.setForeground(Color.GRAY);
        }

        // 텍스트 패널에 메뉴명과 가격을 세로로 배치
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.add(nameLabel);
        textPanel.add(priceLabel);

        add(imgLabel, BorderLayout.CENTER);
        add(textPanel, BorderLayout.SOUTH);

        // 마우스 클릭 시 광고가 아니면 onClick 실행
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!soldOut) {
                    onClick.run();
                }
            }
        });
    }
}