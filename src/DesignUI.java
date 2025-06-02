import javax.swing.*;
import java.awt.*;

public class DesignUI extends JDialog {

    /**
     * 디자인 설정 다이얼로그 생성자
     * @param owner 부모 프레임 (UI를 동기화할 대상)
     * @param title 다이얼로그 제목
     * @param modal 모달 여부(모달이면 부모 UI 조작 불가)
     */
    public DesignUI(JFrame owner, String title, boolean modal) {
        super(owner, title, modal);
        setLayout(null);
        setSize(425, 620);
        setLocationRelativeTo(null);
        setResizable(false);
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        // 라디오 버튼을 담을 패널 (세로 방향 BoxLayout)
        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));

        ButtonGroup group = new ButtonGroup();

        // DataSet.designs: Object[][] { { themeName, description, isDefault }, … }
        for (int i = 0; i < Entity.designs.length; i++) {
            String themeName   = Entity.designs[i][0].toString();
            String description = Entity.designs[i][1].toString();
            boolean isDefault  = (Boolean) Entity.designs[i][2];

            // 라디오 버튼 생성
            JRadioButton rb = new JRadioButton(themeName);
            rb.setToolTipText(description);
            rb.setFont(new Font("맑은 고딕", Font.BOLD, 16));
            rb.setAlignmentX(Component.LEFT_ALIGNMENT);
            if (isDefault) {
                rb.setSelected(true);
            }

            // 선택 시 즉시 LookAndFeel 적용
            rb.addActionListener(e -> {
                try {
                    UIManager.setLookAndFeel("com.formdev.flatlaf.intellijthemes." + themeName);
                    SwingUtilities.updateComponentTreeUI(owner);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "디자인 적용에 실패했습니다: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                }
            });

            group.add(rb);
            radioPanel.add(rb);

            JLabel descLabel = new JLabel("   └ " + description);
            descLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
            descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            radioPanel.add(descLabel);
        }

        // 스크롤 팬에 라디오 버튼 패널 담기
        JScrollPane scrollPane = new JScrollPane(radioPanel);
        scrollPane.setBounds(10, 10, 390, 500);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        scrollPane.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        add(scrollPane);

        // 닫기 버튼
        JButton closeBtn = new JButton("닫기");
        closeBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        closeBtn.setBounds(10, 520, 390, 50);
        closeBtn.addActionListener(e -> dispose());
        add(closeBtn);
    }
}
