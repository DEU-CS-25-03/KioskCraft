import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminFrame extends JFrame {

    private final JFrame parent;      // 메인 창

    public AdminFrame(JFrame parent) {
        super("관리자 화면");
        this.parent = parent;         // 저장
        setSize(400, 300);
        setLocationRelativeTo(parent);

        add(new JLabel("관리자 기능 UI 자리"), BorderLayout.CENTER);

        JButton closeBtn = new JButton("닫기");
        closeBtn.addActionListener(e -> {
            dispose();                // 자기 자신 닫고
            parent.setVisible(true);  // 메인 다시 표시
        });
        add(closeBtn, BorderLayout.SOUTH);
    }
}
