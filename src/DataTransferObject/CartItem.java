package DataTransferObject;

import java.util.ArrayList;
import java.util.List;

public class CartItem {
    /**
     * cartList 리스트
     * - 사용자가 장바구니에 담은 항목을 저장
     * - 각 요소는 Object[]{ menuName(String), unitPrice(int), quantity(int), total(int) }
     */
    public static List<Object[]> cartList = new ArrayList<>();
}
