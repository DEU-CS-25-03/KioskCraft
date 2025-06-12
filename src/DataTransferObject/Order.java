package DataTransferObject;

import java.util.ArrayList;
import java.util.List;

public class Order {
    public String orderId;
    public String customerCode;
    public String status;

    public Order(String orderId, String customerCode, String status) {
        this.orderId = orderId;
        this.customerCode = customerCode;
        this.status = status;
    }

    // (선택) 엔티티 전체 리스트 관리 시
    public static List<Order> orders = new ArrayList<>();
}
