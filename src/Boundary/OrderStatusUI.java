package Boundary;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import Controller.OrderStateControl;

/**
 * OrderStatusUI 클래스
 * - OrderStateControl에서 제공하는 주문 리스트를 테이블로 표시
 * - 닫기 버튼을 눌러 다이얼로그 종료
 */
public class OrderStatusUI extends JDialog {
    public OrderStatusUI(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        setSize(500, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        // 예시 주문 데이터 (OrderStateControl.Order 객체 생성)
        List<OrderStateControl.Order> orderList = new ArrayList<>();
        orderList.add(new OrderStateControl.Order("001", "A001", "접수"));
        orderList.add(new OrderStateControl.Order("002", "A002", "처리중"));
        orderList.add(new OrderStateControl.Order("003", "A003", "완료"));

        // OrderTableModel을 사용한 JTable 생성
        OrderStateControl.OrderTableModel tableModel = new OrderStateControl.OrderTableModel(orderList);
        JTable orderTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(orderTable);
        add(scrollPane, BorderLayout.CENTER);

        // 닫기 버튼 생성 및 이벤트 등록
        JButton closeButton = new JButton("닫기");
        closeButton.addActionListener(_ -> dispose());
        JPanel btnPanel = new JPanel();
        btnPanel.add(closeButton);
        add(btnPanel, BorderLayout.SOUTH);
    }
}