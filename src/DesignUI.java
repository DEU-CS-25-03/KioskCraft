import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DesignUI extends JDialog {
    public DesignUI(JFrame owner, String title, boolean modal) {
        super(owner, title, modal);
        setLayout(null);
        setSize(284, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        // 테이블 세팅
        DefaultTableModel model = getTableModel();
        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        table.setRowHeight(35);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 10, 250, 350);
        add(scrollPane);

        // 확인 버튼
        JButton setDesignBtn = new JButton("확인");
        setDesignBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        setDesignBtn.setBounds(10, 370, 122, 30);
        setDesignBtn.addActionListener(_ -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String selectedDesign = table.getValueAt(selectedRow, 0).toString();
                try {
                    UIManager.setLookAndFeel("com.formdev.flatlaf.intellijthemes." + selectedDesign);
                    SwingUtilities.updateComponentTreeUI(owner);
                    JOptionPane.showMessageDialog(
                            this,
                            "기본 디자인이 " + selectedDesign + "으로 변경되었습니다.",
                            "알림",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    dispose();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            this,
                            "디자인 적용에 실패했습니다: " + e.getMessage(),
                            "오류",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            } else {
                JOptionPane.showMessageDialog(this, "디자인을 선택해주세요.", "알림", JOptionPane.WARNING_MESSAGE);
            }
        });
        add(setDesignBtn);

        // 취소 버튼
        JButton cancelBtn = new JButton("취소");
        cancelBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        cancelBtn.setBounds(142, 370, 122, 30);
        cancelBtn.addActionListener(_ -> dispose());
        add(cancelBtn);
    }

    private static DefaultTableModel getTableModel() {
        Object[][] rowData = new Object[DataSet.designs.length][1];
        for (int i = 0; i < DataSet.designs.length; i++) {
            rowData[i][0] = DataSet.designs[i];
        }

        String[] columnNames = {"디자인 목록"};

        return new DefaultTableModel(rowData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
}