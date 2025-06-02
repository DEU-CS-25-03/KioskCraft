import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class ModifyMenuUI extends JDialog {
    public ModifyMenuUI(JFrame owner, String title, boolean modal, DefaultTableModel model) {
        super(owner, title, modal);
        setTitle(title);
        setLayout(null);
        setSize(335, 360);
        setLocationRelativeTo(null);
        setResizable(false);
        SwingUtilities.invokeLater(this::requestFocusInWindow);


        // 테이블 모델 생성 및 테이블 초기화
        JTable table = new JTable(model);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 16));

        // 컬럼 너비 설정
        TableColumnModel colModel = table.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(150); // 카테고리
        colModel.getColumn(1).setPreferredWidth(260); // 메뉴명
        colModel.getColumn(2).setPreferredWidth(80);  // 가격
        colModel.getColumn(3).setPreferredWidth(60);  // 품절여부
        colModel.getColumn(4).setPreferredWidth(40);  // 삭제 버튼

        // 삭제 버튼 렌더러/에디터 설정
        table.getColumnModel().getColumn(4).setCellRenderer(new AdminUI.ButtonRenderer());
        table.getColumnModel().getColumn(4).setCellEditor(new AdminUI.ButtonEditor(new JCheckBox(), model));

        // 스크롤 및 테이블 설정
        JScrollPane scrollPane = new JScrollPane(table);
        table.setRowHeight(35);
        scrollPane.setBounds(10, 70, 830, 500);
        table.setRowSelectionAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        add(scrollPane);
    }
}
