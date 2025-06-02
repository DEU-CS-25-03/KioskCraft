import DataAccessObject.DBManager;
import DataAccessObject.DesignDAO;
import DataTransferObject.Entity;
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

        // ✅ Entity 캐시 먼저 검사하고 사용
        if (Entity.designs == null) {
            try {
                Entity.refreshDesigns();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "디자인 데이터 로딩 실패: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }
        }

        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));
        final ButtonGroup group = new ButtonGroup();
        boolean anyDefaultSelected = false;

        for (Object[] design : Entity.designs) {
            String themeName = design[0].toString();
            String description = design[1].toString();
            boolean isDefault = (Boolean) design[2];

            JRadioButton rb = new JRadioButton(themeName);
            rb.setActionCommand(themeName);
            rb.setToolTipText(description);
            rb.setFont(new Font("맑은 고딕", Font.BOLD, 16));
            rb.setAlignmentX(Component.LEFT_ALIGNMENT);

            if (isDefault) {
                rb.setSelected(true);
                anyDefaultSelected = true;
            }

            rb.addActionListener(_ -> {
                try {
                    UIManager.setLookAndFeel("com.formdev.flatlaf.intellijthemes." + themeName);
                    SwingUtilities.updateComponentTreeUI(owner);
                    SwingUtilities.updateComponentTreeUI(this);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "디자인 적용 실패: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                }
            });

            group.add(rb);
            radioPanel.add(rb);
            radioPanel.add(new JLabel("   └ " + description));
        }

        if (!anyDefaultSelected && radioPanel.getComponentCount() > 0) {
            Component firstComp = radioPanel.getComponent(0);
            if (firstComp instanceof JRadioButton)
                ((JRadioButton) firstComp).setSelected(true);
        }

        JScrollPane scrollPane = new JScrollPane(radioPanel);
        scrollPane.setBounds(10, 10, 390, 500);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        add(scrollPane);

        JButton closeBtn = new JButton("닫기");
        closeBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        closeBtn.setBounds(10, 520, 390, 50);
        closeBtn.addActionListener(_ -> {
            ButtonModel selectedModel = group.getSelection();
            if (selectedModel == null) {
                JOptionPane.showMessageDialog(this, "선택된 디자인이 없습니다.", "경고", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String selectedDesign = selectedModel.getActionCommand().trim();

            try (Connection con = DBManager.getInstance().getConnection()) {
                DesignDAO.updateDefaultDesign(con, selectedDesign);

                // 캐시 동기화
                for (Object[] design : Entity.designs)
                    design[2] = design[0].equals(selectedDesign);

                JOptionPane.showMessageDialog(this, "디자인이 변경되었습니다: " + selectedDesign, "성공", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "디자인 적용 오류: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
            dispose();
        });
        add(closeBtn);
    }
}
