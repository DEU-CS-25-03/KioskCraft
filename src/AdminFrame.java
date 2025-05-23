import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminFrame extends JFrame{
    public AdminFrame(){
        setTitle("관리자 페이지");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }
}