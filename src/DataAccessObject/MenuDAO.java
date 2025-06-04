package DataAccessObject;

import DataTransferObject.MenuDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {

    // 생성자에서 커넥션을 받지 않음 (커넥션 풀 사용)
    public MenuDAO() {}

    // 메뉴 전체 조회
    public List<MenuDTO> selectAllMenus() throws SQLException {
        String sql = "SELECT category, menuName, price, isSoldOut, imagePath FROM test.menuId";
        List<MenuDTO> menus = new ArrayList<>();
        // 커넥션 풀에서 커넥션을 빌려 사용
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                menus.add(new MenuDTO(
                        rs.getString("category"),
                        rs.getString("menuName"),
                        rs.getInt("price"),
                        rs.getString("imagePath"),
                        rs.getBoolean("isSoldOut")
                ));
            }
        }
        return menus;
    }

    // 메뉴 등록
    public void insertMenu(MenuDTO menu) throws SQLException {
        String sql = "INSERT INTO test.menuId (category, menuName, price, isSoldOut, imagePath) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, menu.getCategory());
            ps.setString(2, menu.getMenuName());
            ps.setInt(3, menu.getPrice());
            ps.setBoolean(4, menu.isSoldOut());
            ps.setString(5, menu.getImagePath());
            ps.executeUpdate();
        }
    }

    // 메뉴 수정
    public void updateMenu(MenuDTO menu) throws SQLException {
        String sql = "UPDATE test.menuId SET category=?, price=?, isSoldOut=?, imagePath=? WHERE menuName=?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, menu.getCategory());
            ps.setInt(2, menu.getPrice());
            ps.setBoolean(3, menu.isSoldOut());
            ps.setString(4, menu.getImagePath());
            ps.setString(5, menu.getMenuName());
            ps.executeUpdate();
        }
    }

    // 메뉴 삭제
    public void deleteMenu(String menuName) throws SQLException {
        String sql = "DELETE FROM test.menuId WHERE menuName=?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, menuName);
            ps.executeUpdate();
        }
    }
}
