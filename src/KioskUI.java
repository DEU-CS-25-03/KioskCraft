import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;

public class KioskUI extends JFrame {
    private final JPanel menuPanel;
    private final DefaultTableModel cartModel;

    public KioskUI() {
        setTitle("메뉴 페이지");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);

        // 카테고리 버튼
        for (int i = 0; i < DataSet.categories.length; i++) {
            String category = String.valueOf(DataSet.categories[i]);
            JButton categoryBtn = new JButton(category);
            categoryBtn.setBounds(10 + i * 130, 10, 120, 40);
            categoryBtn.addActionListener(_ -> showMenuByCategory(category));
            add(categoryBtn);
        }

        // 메뉴 패널
        menuPanel = new JPanel(new GridLayout(0, 5, 10, 10));
        JScrollPane scrollPane = new JScrollPane(menuPanel);
        scrollPane.setBounds(10, 60, 940, 620);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        add(scrollPane);

        // 장바구니 테이블
        String[] columns = {"메뉴명", "수량", "단가", "총액"};
        cartModel = new DefaultTableModel(columns, 0);
        JTable cartTable = new JTable(cartModel);
        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartScrollPane.setBounds(970, 60, 290, 550);
        add(cartScrollPane);

        // 결제하기 버튼
        JButton payButton = new JButton("결제하기");
        payButton.setBounds(970, 620, 290, 40);
        payButton.addActionListener(_ -> JOptionPane.showMessageDialog(this, "결제 기능은 아직 구현되지 않았습니다."));
        add(payButton);

        showMenuByCategory("커피");
        setVisible(true);
    }

    private void showMenuByCategory(String category) {
        menuPanel.removeAll();

        for (Object[] item : new DataSet().Menus) {
            String cat = (String) item[0];
            String name = (String) item[1];
            String priceStr = (String) item[2];
            boolean soldOut = !((String) item[3]).equalsIgnoreCase("O");

            if (!cat.equals(category)) continue;

            // 이미지 경로 구성
            String imagePath = "images/" + name + ".png";
            boolean imageExists = new File(imagePath).exists();
            if (!imageExists) imagePath = "images/default.png";

            MenuCardPanel card = new MenuCardPanel(name, priceStr, imagePath, !imageExists, soldOut, () -> addToCart(name, priceStr));
            menuPanel.add(card);
        }

        menuPanel.revalidate();
        menuPanel.repaint();
    }

    private void addToCart(String name, String priceStr) {
        int price = Integer.parseInt(priceStr.replace(",", "").replace("원", ""));
        boolean found = false;

        for (int i = 0; i < cartModel.getRowCount(); i++) {
            String existingName = (String) cartModel.getValueAt(i, 0);
            if (existingName.equals(name)) {
                int quantity = (int) cartModel.getValueAt(i, 1) + 1;
                cartModel.setValueAt(quantity, i, 1);
                cartModel.setValueAt(quantity * price, i, 3);
                found = true;
                break;
            }
        }

        if (!found) {
            Vector<Object> row = new Vector<>();
            row.add(name);
            row.add(1);
            row.add(price);
            row.add(price);
            cartModel.addRow(row);
        }
    }

    public static void main(String[] args) {
        new KioskUI();
    }
}

class MenuCardPanel extends JPanel {
    public MenuCardPanel(String name, String price, String imagePath, boolean isDefault, boolean soldOut, Runnable onClick) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(120, 120));
        setBorder(new LineBorder(Color.BLACK));

        JLabel imgLabel;
        if (isDefault) {
            // 기본 이미지 직접 생성
            BufferedImage placeholder = new BufferedImage(60, 60, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = placeholder.createGraphics();
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillRect(0, 0, 60, 60);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            g2.drawString("기본이미지", 2, 35);
            g2.dispose();

            imgLabel = new JLabel(new ImageIcon(placeholder));
        } else {
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            imgLabel = new JLabel(new ImageIcon(img));
        }
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        JLabel priceLabel = new JLabel(price + (soldOut ? " (품절)" : ""), SwingConstants.CENTER);

        if (soldOut) {
            imgLabel.setEnabled(false);
            nameLabel.setForeground(Color.GRAY);
            priceLabel.setForeground(Color.GRAY);
        }

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.add(nameLabel);
        textPanel.add(priceLabel);

        add(imgLabel, BorderLayout.CENTER);
        add(textPanel, BorderLayout.SOUTH);

        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (!soldOut) onClick.run();
            }
        });
    }
}
