package DataTransferObject;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    /**
     * menus 리스트
     * - DB 또는 사용자 입력으로부터 로드된 메뉴 정보를 저장
     * - 각 요소는 Object[]{ category(String), menuName(String), price(String, "1,000원"), isSoldOut(boolean), imagePath(String) }
     */
    public static final List<Object[]> menus = new ArrayList<>();
}
