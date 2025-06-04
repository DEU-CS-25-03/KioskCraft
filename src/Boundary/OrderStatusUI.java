package Boundary;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class OrderStatusUI extends JDialog {
    // 예시용 주문 데이터 클래스
    static class Order {
        String orderId;
        String customerCode;
        String status;

        Order(String orderId, String customerCode, String status) {
            this.orderId = orderId;
            this.customerCode = customerCode;
            this.status = status;
        }
    }

    // 테이블 모델 정의
    static class OrderTableModel extends AbstractTableModel {
        private final String[] columns = {"주문번호", "고객명", "상태"};
        private final List<Order> orders;

        public OrderTableModel(List<Order> orders) {
            this.orders = orders;
        }

        @Override
        public int getRowCount() {
            return orders.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Order order = orders.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> order.orderId;
                case 1 -> order.customerCode;
                case 2 -> order.status;
                default -> null;
            };
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }
    }

    public OrderStatusUI(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        setSize(500, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        // 예시 주문 데이터
        List<Order> orderList = new ArrayList<>();
        orderList.add(new Order("001", "A001", "접수"));
        orderList.add(new Order("002", "A002", "처리중"));
        orderList.add(new Order("003", "A003", "완료"));

        OrderTableModel tableModel = new OrderTableModel(orderList);
        JTable orderTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(orderTable);
        add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("닫기");
        closeButton.addActionListener(_ -> dispose());
        JPanel btnPanel = new JPanel();
        btnPanel.add(closeButton);
        add(btnPanel, BorderLayout.SOUTH);
    }
}

