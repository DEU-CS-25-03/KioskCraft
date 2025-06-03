package DataAccessObject;

import DataTransferObject.MenuDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {
    private final Connection conn;
    public MenuDAO(Connection conn) {this.conn = conn;}

    // 메뉴 전체 조회
    public List<MenuDTO> selectAllMenus() throws SQLException {
        String sql = "SELECT category, menuName, price, isSoldOut, imagePath FROM menuId";
        List<MenuDTO> menus = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
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
        String sql = "INSERT INTO menuId (category, menuName, price, isSoldOut, imagePath) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
        String sql = "UPDATE menuId SET category=?, price=?, isSoldOut=?, imagePath=? WHERE menuName=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
        String sql = "DELETE FROM menuId WHERE menuName=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, menuName);
            ps.executeUpdate();
        }
    }
}