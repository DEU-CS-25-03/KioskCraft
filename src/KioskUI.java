import javax.swing.*;
import java.awt.*;

public class KioskUI extends JFrame{
    public KioskUI(){
        setTitle("메뉴 페이지");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 30)); // 버튼 간격, 아래 여백

        JButton closeBtn = new JButton("홈으로");
        closeBtn.setSize(200, 50);

        closeBtn.addActionListener(_ -> {
            new OrderTypeSelectionUI().setVisible(true); // 새 창 띄움
            dispose(); // StartFrame 닫음
        });

        btnPanel.add(closeBtn);
        add(btnPanel);
    }
}