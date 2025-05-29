import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;

public class KioskUI extends JFrame {
    private JPanel gridPanel;
    private JTable cartTable;
    private DefaultTableModel cartModel;

    public KioskUI() {
        setTitle("메뉴 페이지");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 800);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);

        // 카테고리 버튼
        for (int i = 0; i < DataSet.categories.length; i++) {
            String category = String.valueOf(DataSet.categories[i]);
            JButton categoryBtn = new JButton(category);
            categoryBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
            categoryBtn.setBounds(10 + i * 130, 10, 120, 40);
            categoryBtn.addActionListener(e -> showMenuByCategory(category));
            add(categoryBtn);
        }

        // 메뉴 패널
        gridPanel = new JPanel(new GridLayout(0, 5, 10, 10));
        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.add(gridPanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(menuPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(10, 60, 1080, 690);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        add(scrollPane);

        // 장바구니 테이블
        String[] columns = {"메뉴명", "수량", "총액", "", ""};
        cartModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 3;
            }
        };

        cartTable = new JTable(cartModel);
        cartTable.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        cartTable.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 16));
        cartTable.setRowHeight(30);
        cartTable.setRowSelectionAllowed(false);

        // 버튼 렌더러/에디터
        cartTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer("-"));
        cartTable.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox(), "-", true));
        cartTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer("x"));
        cartTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), "x", false));
        cartTable.getColumnModel().getColumn(0).setPreferredWidth(250);
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        cartTable.getColumnModel().getColumn(2).setPreferredWidth(130);
        cartTable.getColumnModel().getColumn(3).setPreferredWidth(40);
        cartTable.getColumnModel().getColumn(4).setPreferredWidth(40);

        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartScrollPane.setBounds(1100, 60, 380, 620);
        add(cartScrollPane);

        // 결제 버튼
        JButton payBtn = new JButton("결제하기");
        payBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        payBtn.setBounds(1100, 690, 320, 60);
        payBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "결제 완료!"));
        add(payBtn);

        showMenuByCategory("커피");
        setVisible(true);
    }

    private void showMenuByCategory(String category) {
        gridPanel.removeAll();

        for (Object[] item : new DataSet().menus) {
            String cat = (String) item[0];
            String name = (String) item[1];
            String priceStr = (String) item[2];
            boolean soldOut = !((String) item[3]).equalsIgnoreCase("O");

            if (!cat.equals(category)) continue;

            String imagePath = "images/" + name + ".png";
            boolean imageExists = new File(imagePath).exists();
            if (!imageExists) imagePath = "images/default.png";

            MenuCardPanel card = new MenuCardPanel(name, priceStr, imagePath, !imageExists, soldOut, () -> addToCart(name, priceStr));
            gridPanel.add(card);
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void addToCart(String name, String priceStr) {
        int price = Integer.parseInt(priceStr.replace(",", "").replace("원", ""));
        boolean found = false;

        for (int i = 0; i < cartModel.getRowCount(); i++) {
            String existingName = (String) cartModel.getValueAt(i, 0);
            if (existingName.equals(name)) {
                int quantity = (int) cartModel.getValueAt(i, 1) + 1;
                cartModel.setValueAt(quantity, i, 1);
                cartModel.setValueAt(quantity * price, i, 2);
                found = true;
                break;
            }
        }

        if (!found) {
            Vector<Object> row = new Vector<>();
            row.add(name);
            row.add(1);
            row.add(price);
            row.add("-");
            row.add("x");
            cartModel.addRow(row);
        }
    }

    private int getUnitPriceByName(String name) {
        for (Object[] item : new DataSet().menus) {
            if (item[1].equals(name)) {
                return Integer.parseInt(((String) item[2]).replace(",", "").replace("원", ""));
            }
        }
        return 0;
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String label) {
            setText(label);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean isDecrease;
        private int editingRow;

        public ButtonEditor(JCheckBox checkBox, String label, boolean isDecrease) {
            super(checkBox);
            this.isDecrease = isDecrease;
            button = new JButton(label);
            button.addActionListener(e -> handleClick());
        }

        private void handleClick() {
            if (isDecrease) {
                int quantity = (int) cartModel.getValueAt(editingRow, 1);
                String name = (String) cartModel.getValueAt(editingRow, 0);
                int unitPrice = getUnitPriceByName(name);

                if (quantity > 1) {
                    cartModel.setValueAt(quantity - 1, editingRow, 1);
                    cartModel.setValueAt((quantity - 1) * unitPrice, editingRow, 2);
                } else {
                    cartModel.removeRow(editingRow);
                }
            } else {
                cartModel.removeRow(editingRow);
            }
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.editingRow = row;
            return button;
        }
    }

    public static void main(String[] args) {
        new KioskUI();
    }
}


// 메뉴 카드 클래스는 그대로 유지
class MenuCardPanel extends JPanel {
    public MenuCardPanel(String name, String price, String imagePath, boolean isDefault, boolean soldOut, Runnable onClick) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(150, 150));
        setBorder(new LineBorder(Color.BLACK));

        JLabel imgLabel;
        if (isDefault) {
            BufferedImage placeholder = new BufferedImage(60, 60, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = placeholder.createGraphics();
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillRect(0, 0, 60, 60);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
            g2.drawString("Default Img", 2, 35);
            g2.dispose();
            imgLabel = new JLabel(new ImageIcon(placeholder));
        } else {
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            imgLabel = new JLabel(new ImageIcon(img));
        }
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        JLabel priceLabel = new JLabel(price + (soldOut ? " (품절)" : ""), SwingConstants.CENTER);
        priceLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));

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
