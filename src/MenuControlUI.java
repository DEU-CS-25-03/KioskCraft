import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MenuControlUI extends JFrame {
    private String btnText = "수정";
    private String title = "메뉴 수정 시스템";
    private JTable menuControlTable;

    public MenuControlUI(boolean isRegist, JTable table) {
        if (isRegist) {
            btnText = "등록";
            title = "메뉴 등록 시스템";
        }

        setTitle(title);
        setLayout(null);
        setSize(1280, 720);



        JButton menuControlBtn = new JButton(btnText);
        menuControlBtn.addActionListener(_ -> JOptionPane.showMessageDialog(null, "메뉴가 " + btnText + " 되었습니다."));
        menuControlBtn.setBounds(10, 600, 200, 50);
        add(menuControlBtn);

        JButton cancelBtn = new JButton("취소");
        cancelBtn.addActionListener(_ -> dispose());
        cancelBtn.setBounds(220, 600, 200, 50);
        add(cancelBtn);
    }
}