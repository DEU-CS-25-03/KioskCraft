import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Set;


public class CategoryPanel extends JPanel {
    public CategoryPanel(JFrame parent, JFrame owner, Set<String> categories, ActionListener categoryListener) {
        //카테고리 패널 기본 설정
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setBackground(new Color(240, 240, 240));
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // 각 카테고리별 버튼 생성
        for (String category : categories) {
            JButton btn = new JButton(category);
            btn.setPreferredSize(new Dimension(150, 45));
            btn.setFont(new Font("맑은 고딕", Font.BOLD, 14));
            btn.setBackground(new Color(70, 130, 180));
            btn.setForeground(Color.BLACK);
            btn.addActionListener(categoryListener);
            add(btn);
        }

        // 언어선택 버튼
        JButton langBtn = new JButton("언어선택");
        langBtn.setPreferredSize(new Dimension(120, 45));
        langBtn.setBackground(new Color(70, 130, 180));
        langBtn.setForeground(Color.BLACK);
        add(langBtn);

        // 닫기 버튼
        JButton closeBtn = new JButton("닫기");
        closeBtn.setPreferredSize(new Dimension(120, 45));
        closeBtn.setBackground(new Color(255, 130, 180));
        closeBtn.setForeground(Color.BLACK);
        closeBtn.addActionListener(e -> {
            parent.dispose();                // 자기 자신 닫고
            owner.setVisible(true);  // 메인 다시 표시
        });
        add(closeBtn);
    }
}
