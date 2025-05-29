import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme");
                new OrderTypeSelectionUI().setVisible(true);
                //DB 추가
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "프로그램 시작 오류:\n" + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}