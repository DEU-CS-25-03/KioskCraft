package Boundary;

import DataTransferObject.Entity;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;

public class KioskUI extends JFrame {
    //메뉴 목록을 보여주는 그리드 패널 (5열 구조)
    private final JPanel gridPanel;

    //장바구니(선택한 메뉴, 수량, 총액 등)용 테이블 모델
    private final DefaultTableModel cartModel;

    public KioskUI() {
        setTitle("메뉴 페이지");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 800);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);

        /*
         * 카테고리별 버튼 생성
         * 클릭 시 해당 카테고리 메뉴만 메뉴패널에 표시
         */
        for (int i = 0; i < Entity.categories.toArray().length; i++) {
            String category = String.valueOf(Entity.categories.get(i));
            JButton categoryBtn = new JButton(category);
            categoryBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
            categoryBtn.setBounds(10 + i * 130, 10, 120, 40);
            categoryBtn.addActionListener(_ -> showMenuByCategory(category));
            add(categoryBtn);
        }

         // 홈(초기화면) 이동 버튼 아래 유니코드는 🏠︎과 같음
        JButton goHome = new JButton("\uD83C\uDFE0");
        goHome.setFont(new Font("", Font.PLAIN, 16));
        goHome.setBounds(1425, 10, 50, 50);
        goHome.addActionListener(_ -> {
            new OrderTypeSelectionUI().setVisible(true);
            dispose();
        });
        add(goHome);

        // 메뉴(메뉴 카드) 패널
        gridPanel = new JPanel(new GridLayout(0, 5, 10, 10));
        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.add(gridPanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(menuPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(10, 60, 1080, 690);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        add(scrollPane);

        // 장바구니 모델 생성(4, 5열만 수정 가능)
        String[] columns = {"메뉴명", "수량", "총액", "", ""};
        cartModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 3;
            }
        };

        JTable cartTable = new JTable(cartModel);
        cartTable.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        cartTable.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 16));
        cartTable.setRowHeight(30);
        cartTable.setRowSelectionAllowed(false);

        // - 버튼(수량 감소), x 버튼(행 삭제) 커스텀 렌더러/에디터 적용
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
        cartScrollPane.setBounds(1100, 60, 375, 620);
        add(cartScrollPane);


        // 결제 버튼 (현재는 메시지박스만 표시)
        JButton payBtn = new JButton("결제하기");
        payBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        payBtn.setBounds(1100, 690, 375, 60);
        payBtn.addActionListener(_ -> JOptionPane.showMessageDialog(this, "결제 완료!"));
        add(payBtn);

        // 초기 카테고리 설정
        if (!Entity.categories.isEmpty())
            showMenuByCategory(Entity.categories.getFirst());

        setVisible(true);
    }

    /*
     * 해당 카테고리에 속한 메뉴 카드만 메뉴 패널에 표시
     * 메뉴 이미지 없으면 기본 이미지로 대체, 품절 표시 처리
     */
    private void showMenuByCategory(String category) {
        gridPanel.removeAll();
        for (Object[] item : Entity.menus) {
            String cat = (String) item[0];
            String name = (String) item[1];
            String priceStr = (String) item[2];
            boolean soldOut = (boolean) item[3];

            if (!cat.equals(category)) continue;

            String imagePath = "images/" + name + ".png";
            boolean imageExists = new File(imagePath).exists();
            if (!imageExists) imagePath = "images/default.png";

            MenuCardPanel card = new MenuCardPanel(
                    name, priceStr, imagePath, !imageExists, soldOut,
                    () -> addToCart(name, priceStr)
            );
            gridPanel.add(card);
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    /*
     * 장바구니에 메뉴 추가
     * 이미 담긴 메뉴면 수량+1, 총액 업데이트
     * 없으면 새 행 추가
     */
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
            row.add("-"); // 수량 감소 버튼
            row.add("x"); // 행 삭제 버튼
            cartModel.addRow(row);
        }
    }

    /**
     * 메뉴명으로 단가(가격) 조회
     * @param name 메뉴명
     * @return 메뉴 단가(정수)
     */
    private int getUnitPriceByName(String name) {
        for (Object[] item : Entity.menus) {
            if (item[1].equals(name)) {
                return Integer.parseInt(((String) item[2]).replace(",", "").replace("원", ""));
            }
        }
        return 0;
    }

    // 장바구니 테이블 내 버튼(-, x) 셀에 사용하는 버튼 렌더러
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String label) {
            setText(label);
        }
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    /*
     * 장바구니 테이블 내 버튼(-, x) 셀에 사용하는 버튼 에디터
     * - isDecrease가 true면 수량 감소, false면 행 삭제
     */
    class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private final boolean isDecrease;
        private int editingRow;

        public ButtonEditor(JCheckBox checkBox, String label, boolean isDecrease) {
            super(checkBox);
            this.isDecrease = isDecrease;
            button = new JButton(label);
            button.addActionListener(_ -> handleClick());
        }

        /*
         * 버튼 클릭 시 이벤트 처리
         * - 수량이 1보다 크면 감소/총액 업데이트
         * - 아니면 행 삭제
         */
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

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.editingRow = row;
            return button;
        }
    }
}

/*
 * 메뉴(상품) 카드 한 칸을 나타내는 패널 클래스
 * - 이미지, 이름, 가격(품절 표시 포함) 표시
 * - 클릭 시 메뉴 장바구니 추가
 */
class MenuCardPanel extends JPanel {
    public MenuCardPanel(String name, String price, String imagePath, boolean isDefault, boolean soldOut, Runnable onClick) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(150, 150));
        setBorder(new LineBorder(Color.BLACK));

        // 이미지 라벨
        JLabel imgLabel;
        if (isDefault) {
            // 기본 이미지(없을 때) 직접 그림
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
            // 메뉴 이미지
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            imgLabel = new JLabel(new ImageIcon(img));
        }
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 이름, 가격 라벨(품절이면 색상 gray 처리)
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

        // 클릭 시(품절 아니면) onClick 실행(장바구니 추가)
        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (!soldOut) onClick.run();
            }
        });
    }
}
