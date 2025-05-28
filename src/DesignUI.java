import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DesignUI extends JDialog{
    public DesignUI(JFrame owner, String title, boolean modal){
        //모달 설정
        super(owner, title, modal);
        setSize(284, 450);
        setLayout(null);
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        DefaultTableModel model = getTableModel();
        JTable table = new JTable(model);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(table);
        table.setRowHeight(35);
        scrollPane.setBounds(10, 10, 250, 350);
        add(scrollPane);

        JButton cancelBtn = new JButton("취소");
        cancelBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        cancelBtn.setBounds(142, 370, 122, 30);
        cancelBtn.addActionListener(_ -> dispose());
        add(cancelBtn);

        JButton setDesignBtn = new JButton("확인");
        setDesignBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        setDesignBtn.setBounds(10, 370, 122, 30);
        setDesignBtn.addActionListener(_ -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String selectedDesign = table.getValueAt(selectedRow, 0).toString();
                JOptionPane.showMessageDialog(
                        null,
                        "기본 디자인이 " + selectedDesign + "으로 변경되었습니다.",
                        "알림",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        add(setDesignBtn);

        setLocationRelativeTo(null);
        setResizable(false);
    }

    private static DefaultTableModel getTableModel() {
        Object[][] rowData = {
                {"Metal"},
                {"Nimbus"},
                {"CDE/Motif"},
                {"Windows"},
                {"Windows Classic"},
                {"GTK+"},
                {"Mac OS X"},
                {"FlatLaf Light"},
                {"FlatLaf Dark"},
                {"FlatLaf IntelliJ"},
                {"FlatLaf Darcula"},
                {"FlatLaf Material Light"},
                {"FlatLaf Material Dark"},
                {"FlatLaf Dracula"},
                {"FlatLaf Arc"},
                {"FlatLaf Arc Dark"},
                {"JTattoo Acryl"},
                {"JTattoo Aero"},
                {"JTattoo Aluminium"},
                {"JTattoo HiFi"},
                {"JTattoo Luna"}
        };

        String[] columnNames = {"디자인 목록"};

        // 테이블 모델 생성
        return new DefaultTableModel(rowData, columnNames) {
            @Override
            // 4번째 열(작업)만 수정 가능(=버튼 클릭 가능)
            public boolean isCellEditable(int row, int column) { return column == 3; }
        };
    }
}