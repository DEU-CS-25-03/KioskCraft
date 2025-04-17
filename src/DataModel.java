import java.util.*;

public class DataModel {
    private final Map<String, List<String>> menuData = new HashMap<>();
    private final Map<String, Integer> priceData = new HashMap<>();
    private final Map<String, Boolean> soldOutMap = new HashMap<>();
    //이 클래스는 그냥 데이터 프레임입니다.
    public Map<String, List<String>> getMenuData() {
        return menuData;
    }

    public Map<String, Integer> getPriceData() {
        return priceData;
    }

    public Map<String, Boolean> getSoldOutMap() {
        return soldOutMap;
    }
    public void addMenuItem(String category, String menu, int price, boolean soldOut) {
        menuData.computeIfAbsent(category, k -> new ArrayList<>()).add(menu);
        priceData.put(menu, price);
        soldOutMap.put(menu, soldOut);
    }
}
