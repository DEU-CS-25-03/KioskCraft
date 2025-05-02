import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Main extends JFrame {

    public Main() {
        setTitle("시작 화면");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(1, 2, 10, 10));

        JButton adminBtn = new JButton("관리자");
        JButton menuBtn  = new JButton("메뉴 선택");

        adminBtn.addActionListener(this::openAdminFrame);
        menuBtn.addActionListener(this::openKioskFrame);   // ★ 여기만 KioskFrame 호출!

        add(adminBtn);
        add(menuBtn);
    }

    // 관리자 버튼 → AdminFrame
    private void openAdminFrame(ActionEvent e) {
        try {
            new AdminFrame(this).setVisible(true);
            this.setVisible(false);
        } catch (Exception ex) {
            // 에러출력
            JOptionPane.showMessageDialog(
                    this,
                    "실행 실패:\n" + ex.getMessage(),
                    "오류",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // 메뉴 선택 버튼 → KioskFrame
    private void openKioskFrame(ActionEvent e) {
        try {
            new KioskFrame(this).setVisible(true);
            this.setVisible(false);
        } catch (Exception ex) {
            // 에러출력
            JOptionPane.showMessageDialog(
                    this,
                    "실행 실패:\n" + ex.getMessage(),
                    "오류",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
