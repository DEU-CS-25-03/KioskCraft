package Boundary;

import DataAccessObject.DBManager;
import DataAccessObject.DesignDAO;
import DataTransferObject.Entity;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * DesignUI 클래스
 * - 플랫LAF 테마를 선택할 수 있는 다이얼로그
 * - Entity.designs 캐시를 우선 사용하고, 없으면 DB에서 로드
 * - 선택한 테마를 DB에 저장 후 캐시 동기화
 */
public class DesignUI extends JDialog {
    public DesignUI(JFrame owner, String title, boolean modal) {
        super(owner, title, modal);
        setLayout(null);
        setSize(425, 620);
        setLocationRelativeTo(null);
        setResizable(false);
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        // Entity.designs 캐시 확인 후 없으면 DB 로딩 시도
        if (Entity.designs == null) {
            try {
                DesignDAO.loadDesigns();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "디자인 데이터 로딩 실패: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }
        }

        // 라디오 버튼을 세로 BoxLayout 형태로 배치할 패널 생성
        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));
        ButtonGroup group = new ButtonGroup();
        boolean anyDefaultSelected = false;

        // Entity.designs에 담긴 {테마명, 설명, 기본 여부} 순회하며 라디오 버튼 생성
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

            // 선택 시 FlatLAF 테마 적용 및 UI 업데이트
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

        // 기본 선택값이 없으면 첫 번째 라디오 버튼을 기본 선택
        if (!anyDefaultSelected && radioPanel.getComponentCount() > 0) {
            Component firstComp = radioPanel.getComponent(0);
            if (firstComp instanceof JRadioButton) {
                ((JRadioButton) firstComp).setSelected(true);
            }
        }

        // 라디오 버튼 패널을 스크롤 가능한 영역에 추가
        JScrollPane scrollPane = new JScrollPane(radioPanel);
        scrollPane.setBounds(10, 10, 390, 500);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        add(scrollPane);

        // 닫기 버튼 생성 및 위치 설정
        JButton closeBtn = new JButton("닫기");
        closeBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        closeBtn.setBounds(10, 520, 390, 50);

        // 닫기 클릭 시 선택된 테마를 DB에 저장하고 캐시 동기화
        closeBtn.addActionListener(_ -> {
            ButtonModel selectedModel = group.getSelection();
            if (selectedModel == null) {
                JOptionPane.showMessageDialog(this, "선택된 디자인이 없습니다.", "경고", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String selectedDesign = selectedModel.getActionCommand().trim();

            try (Connection conn = DBManager.getConnection()) {
                DesignDAO.updateDefaultDesign(conn, selectedDesign);
                // 캐시 동기화: 선택된 디자인만 isDefault=true로 설정
                for (Object[] design : Entity.designs) {
                    design[2] = design[0].equals(selectedDesign);
                }
                JOptionPane.showMessageDialog(this, "디자인이 변경되었습니다: " + selectedDesign, "성공", JOptionPane.INFORMATION_MESSAGE);
                DBManager.closeConnection(conn);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "디자인 적용 오류: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
            dispose();
        });
        add(closeBtn);
    }
}