package Boundary;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import Control.OrderStateControl;

public class OrderStatusUI extends JDialog {
    public OrderStatusUI(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        setSize(500, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        // 예시 주문 데이터
        List<OrderStateControl.Order> orderList = new ArrayList<>();
        orderList.add(new OrderStateControl.Order("001", "A001", "접수"));
        orderList.add(new OrderStateControl.Order("002", "A002", "처리중"));
        orderList.add(new OrderStateControl.Order("003", "A003", "완료"));

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

