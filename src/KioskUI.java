import javax.swing.*;
import java.awt.*;

public class KioskUI extends JFrame{
    public KioskUI(){
        setTitle("메뉴 페이지");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(120, 140);
        SwingUtilities.invokeLater(this::requestFocusInWindow);
        setLocationRelativeTo(null);
        setFocusable(true);
        setResizable(false);
        setLayout(null);

        JButton closeBtn = new JButton("돌아가기");
        closeBtn.setBounds(10, 10, 100, 100);
        closeBtn.addActionListener(_ -> {
            new OrderTypeSelectionUI().setVisible(true);
            dispose();
        });

        add(closeBtn);
    }
}