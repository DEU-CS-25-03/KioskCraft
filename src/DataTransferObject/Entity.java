package DataTransferObject;

import DataAccessObject.DesignDAO;
import DataAccessObject.MenuDAO;
import DataAccessObject.CategoryDAO;


import java.util.ArrayList;
import java.util.List;

public class Entity {

    public static List<MenuDTO> menus = new ArrayList<>();
    public static List<String> categories = new ArrayList<>();

    // [수정] 커넥션 풀 방식: DAO 생성자에서 커넥션을 받지 않고, 각 메서드에서 getConnection() 사용
    public static void refreshMenus() throws Exception {
        MenuDAO menuDAO = new MenuDAO(); // 커넥션 주입 없이 생성
        menus = menuDAO.selectAllMenus(); // 내부에서 DBManager.getConnection() 사용
    }

    public static void refreshCategories() throws Exception {
        CategoryDAO dao = new CategoryDAO(); // 커넥션 주입 없이 생성
        List<CategoryDTO> list = dao.selectAllCategories(); // 내부에서 DBManager.getConnection() 사용
        categories.clear();
        for (CategoryDTO dto : list) {
            categories.add(dto.getCategoryName());
        }
    }

    public static Object[][] designs;
    public static void refreshDesigns() throws Exception {
        new DesignDAO(); // 커넥션 주입 없이 생성, 내부에서 DBManager.getConnection() 사용
    }
}
