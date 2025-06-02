import DataAccessObject.DesignDAO;
import DTO.Entity;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class DesignUI extends JDialog {

    public DesignUI(JFrame owner, String title, boolean modal) {
        super(owner, title, modal);
        setLayout(null);
        setSize(425, 620);
        setLocationRelativeTo(null);
        setResizable(false);
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));

        // ButtonGroup을 final로 선언하여 나중에 리스너에서 참조 가능하게 함
        final ButtonGroup group = new ButtonGroup();

        // 기본 선택 상태를 추적할 플래그
        boolean anyDefaultSelected = false;

        // DTO.Entity.designs: Object[][] { { themeName, description, isDefault }, … }
        for (int i = 0; i < Entity.designs.length; i++) {
            String themeName   = Entity.designs[i][0].toString();
            String description = Entity.designs[i][1].toString();
            boolean isDefault  = (Boolean) Entity.designs[i][2];

            JRadioButton rb = new JRadioButton(themeName);
            rb.setActionCommand(themeName);
            rb.setToolTipText(description);
            rb.setFont(new Font("맑은 고딕", Font.BOLD, 16));
            rb.setAlignmentX(Component.LEFT_ALIGNMENT);

            if (isDefault) {
                rb.setSelected(true);
                anyDefaultSelected = true;
            }

            // 선택 시 즉시 LookAndFeel 적용
            rb.addActionListener(e -> {
                try {
                    UIManager.setLookAndFeel("com.formdev.flatlaf.intellijthemes." + themeName);
                    SwingUtilities.updateComponentTreeUI(owner);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            this,
                            "디자인 적용에 실패했습니다: " + ex.getMessage(),
                            "오류",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            });

            group.add(rb);
            radioPanel.add(rb);

            JLabel descLabel = new JLabel("   └ " + description);
            descLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
            descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            radioPanel.add(descLabel);
        }

        // 만약 DB에서 isDefault=true인 항목이 하나도 없었다면,
        // 첫 번째 라디오 버튼을 기본으로 선택해 둔다.
        if (!anyDefaultSelected && radioPanel.getComponentCount() > 0) {
            // radioPanel의 자식 중 첫 번째 컴포넌트가 JRadioButton임을 보장
            Component firstComp = radioPanel.getComponent(0);
            if (firstComp instanceof JRadioButton) {
                ((JRadioButton) firstComp).setSelected(true);
            }
        }

        JScrollPane scrollPane = new JScrollPane(radioPanel);
        scrollPane.setBounds(10, 10, 390, 500);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        add(scrollPane);

        JButton closeBtn = new JButton("닫기");
        closeBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        closeBtn.setBounds(10, 520, 390, 50);
        closeBtn.addActionListener(e -> {
            // group에서 반드시 하나는 선택되어 있다.
            ButtonModel selectedModel = group.getSelection();
            if (selectedModel == null) {
                // 이론상 여기로 들어오지 않음. 그래도 안전장치로 넣어둠.
                JOptionPane.showMessageDialog(this,
                        "기본 디자인으로 설정할 항목을 선택해주세요.",
                        "알림",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            String selectedDesign = selectedModel.getActionCommand().trim();

            // DAO 호출: 새 Connection을 열고 작업
            try (Connection conn = DBManager.getInstance().getConnection()) {
                DesignDAO.updateDefaultDesign(conn, selectedDesign);
                JOptionPane.showMessageDialog(this,
                        "[" + selectedDesign + "]으로 기본 디자인이 설정되었습니다.",
                        "완료",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "기본 디자인 설정 중 오류가 발생했습니다:\n" + ex.getMessage(),
                        "오류",
                        JOptionPane.ERROR_MESSAGE
                );
            }

            dispose();
        });
        add(closeBtn);
    }
}
