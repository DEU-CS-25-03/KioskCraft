import javax.swing.*;

public class CategoryControlUI extends JDialog {
    public CategoryControlUI(JFrame owner, String title, boolean modal) {
        super(owner, title, modal);
        setTitle(title);
        setLayout(null);
        setSize(1280, 720);
    }
}