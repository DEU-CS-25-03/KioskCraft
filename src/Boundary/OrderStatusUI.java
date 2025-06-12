package Boundary;

import DataTransferObject.Order;
import Controller.OrderStateControl;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OrderStatusUI extends JDialog {
    public OrderStatusUI(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        setSize(500, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        // 엔티티 클래스 Order에서 주문 데이터를 가져온다
        List<Order> orderList = new ArrayList<>(Order.orders);

        // 예시 데이터가 필요하다면 추가
        if (orderList.isEmpty()) {
            orderList.add(new Order("001", "A001", "접수"));
            orderList.add(new Order("002", "A002", "처리중"));
            orderList.add(new Order("003", "A003", "완료"));
            Order.orders.addAll(orderList); // (선택) 엔티티에도 추가
        }

        OrderStateControl.OrderTableModel tableModel = new OrderStateControl.OrderTableModel(orderList);
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
