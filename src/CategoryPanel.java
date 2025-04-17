import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Set;

public class CategoryPanel extends JPanel {
    public CategoryPanel(Set<String> categories, ActionListener categoryListener) {
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
            btn.setForeground(Color.WHITE);
            btn.addActionListener(categoryListener);
            add(btn);
        }

        // 종료 버튼, 이건 유저 페이지 기본 홈화면으로 이동하는 버튼으로 수정 예정입니다.
        JButton exitBtn = new JButton("종료");
        exitBtn.setPreferredSize(new Dimension(120, 45));
        exitBtn.setBackground(new Color(200, 60, 60));
        exitBtn.setForeground(Color.WHITE);
        exitBtn.addActionListener(e -> System.exit(0));
        add(exitBtn);
    }
}
