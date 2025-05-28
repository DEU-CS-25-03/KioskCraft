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


        JLabel menuLabel = new JLabel("메뉴명: ");
        menuLabel.setBounds(10, 10,100, 10);
        add(menuLabel);
        JLabel cateogoryLabel = new JLabel("카테고리: ");
        menuLabel.setBounds(10, 30,100, 10);
        add(cateogoryLabel);
        JLabel priceLabel = new JLabel("가격: ");
        menuLabel.setBounds(10, 50,100, 10);
        add(priceLabel);
        JLabel imgPathLabel = new JLabel("이미지 경로: ");
        menuLabel.setBounds(10, 70,100, 10);
        add(imgPathLabel);


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