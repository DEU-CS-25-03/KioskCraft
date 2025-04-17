import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class KioskFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private final DataModel dataModel;
    private final Map<String, Integer> selectedItems = new HashMap<>();
    private final CategoryPanel categoryPanel;
    private final MenuPanel menuPanel;
    private final OrderPanel orderPanel;

    public KioskFrame() throws Exception {
        dataModel = CSVDataLoader.loadData();
        setTitle("카페 키오스크 시스템");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));

        //먼저 중앙 메뉴 패널과 오른쪽 주문 내역 패널 초기화
        menuPanel = new MenuPanel();
        orderPanel = new OrderPanel(dataModel, selectedItems);

        //상단 카테고리 패널 생성(버튼 클릭 시 해당 카테고리 반환)
        categoryPanel = new CategoryPanel(dataModel.getMenuData().keySet(), e -> {
            String category = ((JButton) e.getSource()).getText();
            menuPanel.displayCategory(category, dataModel, menu -> {
                //메뉴 선택 시 주문 수량 증가
                selectedItems.merge(menu, 1, Integer::sum);
                orderPanel.updateOrderTable();
            });
        });
        add(categoryPanel, BorderLayout.NORTH);

        // 중앙 메뉴 패널 (스크롤 추가)
        JScrollPane menuScroll = new JScrollPane(menuPanel);
        menuScroll.getVerticalScrollBar().setUnitIncrement(25);
        menuScroll.addMouseWheelListener(e -> {
            JScrollBar bar = menuScroll.getVerticalScrollBar();
            bar.setValue(bar.getValue() + e.getUnitsToScroll() * 30);
        });
        add(menuScroll, BorderLayout.CENTER);

        //오른쪽 주문 내역 패널에 결제 버튼 추가
        JButton paymentBtn = new JButton("결제하기");
        paymentBtn.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        paymentBtn.setBackground(new Color(100, 180, 100));
        paymentBtn.setForeground(Color.WHITE);
        paymentBtn.setPreferredSize(new Dimension(0, 50));
        paymentBtn.addActionListener(e -> processPayment());

        JPanel orderSouthPanel = new JPanel(new BorderLayout());
        orderSouthPanel.add(paymentBtn, BorderLayout.SOUTH);
        orderPanel.add(orderSouthPanel, BorderLayout.SOUTH);

        add(orderPanel, BorderLayout.EAST);

        //첫 카테고리 자동 표시
        if (!dataModel.getMenuData().isEmpty()) {
            String firstCategory = dataModel.getMenuData().keySet().iterator().next();
            menuPanel.displayCategory(firstCategory, dataModel, menu -> {
                selectedItems.merge(menu, 1, Integer::sum);
                orderPanel.updateOrderTable();
            });
        }
    }

    //결제 함수
    private void processPayment() {
        if (orderPanel.processPayment()) {
            saveOrderHistory();
            selectedItems.clear();
            orderPanel.updateOrderTable();
            JOptionPane.showMessageDialog(this, "결제가 완료되었습니다");
        }
    }
    //주문내역 저장
    private void saveOrderHistory() {
        String dirPath = System.getProperty("user.home") + "/Desktop/kiosk_orders/";
        new File(dirPath).mkdirs();
        String fileName = "order_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(dirPath + fileName))) {
            bw.write("메뉴명,수량,단가,금액");
            bw.newLine();
            for (Map.Entry<String, Integer> entry : selectedItems.entrySet()) {
                String menu = entry.getKey();
                int qty = entry.getValue();
                int price = dataModel.getPriceData().get(menu);
                bw.write(String.format("%s,%d,%,d 원,%,d 원", menu, qty, price, price * qty));
                bw.newLine();
            }
            int total = selectedItems.entrySet().stream()
                    .mapToInt(e -> dataModel.getPriceData().get(e.getKey()) * e.getValue())
                    .sum();
            bw.write(String.format(",,총액,%,d 원", total));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "주문 내역 저장 실패:\n" + e.getMessage(),
                    "오류",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
