package DataTransferObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Entity 클래스
 * - 애플리케이션 전역에서 사용하는 데이터 캐시 역할
 * - menus: 메뉴 정보 목록 ({카테고리, 메뉴명, 가격, 품절여부, 이미지경로})
 * - categories: 카테고리 이름 목록
 * - cartList: 장바구니 목록 ({메뉴명, 단가, 수량, 총액})
 * - designs: 디자인(테마) 정보 배열 ({테마명, 설명, isDefault})
 */
public class Entity {
    /**
     * menus 리스트
     * - DB 또는 사용자 입력으로부터 로드된 메뉴 정보를 저장
     * - 각 요소는 Object[]{ category(String), menuName(String), price(String, "1,000원"), isSoldOut(boolean), imagePath(String) }
     */
    public static final List<Object[]> menus = new ArrayList<>();

    /**
     * categories 리스트
     * - 메뉴 카테고리 이름을 저장
     * - 예: {"커피", "디저트", "음료"}
     */
    public static List<String> categories = new ArrayList<>();

    /**
     * cartList 리스트
     * - 사용자가 장바구니에 담은 항목을 저장
     * - 각 요소는 Object[]{ menuName(String), unitPrice(int), quantity(int), total(int) }
     */
    public static List<Object[]> cartList = new ArrayList<>();

    /**
     * designs 2차원 배열
     * - 로드된 디자인(테마) 정보를 저장
     * - 각 행은 Object[]{ themeName(String), description(String), isDefault(boolean) }
     */
    public static Object[][] designs;

    //언어목록 리스트
    public static final List<String> languages = Arrays.asList("한국어", "일본어", "중국어", "영어");


    /**
     * Order 클래스
     * - 예시용 주문 데이터 객체 (orderId, customerCode, status)
     */
    public static class Order {
        public String orderId;        // 주문 번호
        public String customerCode;   // 고객 코드 혹은 이름
        public String status;         // 주문 상태

        public Order(String orderId, String customerCode, String status) {
            this.orderId = orderId;
            this.customerCode = customerCode;
            this.status = status;
        }
    }
}
