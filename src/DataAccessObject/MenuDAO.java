package DataAccessObject;

import DataTransferObject.Entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MenuDAO {
    private static Connection connection = null;

    public MenuDAO(Connection conn) throws SQLException {
        connection = conn;
        loadAllMenusToEntity();
    }

    public static void insertMenu(String category, String menuName, String priceStr, boolean isSoldOut, String imagePath)
            throws SQLException {
        // 1. priceStr에서 숫자만 뽑아서 int로 변환
        //    ex) "6,000원" → "6000" → 6000 (int)
        String onlyDigits = priceStr.replaceAll("[^0-9]", "");
        int priceInt = Integer.parseInt(onlyDigits);

        // 2. SQL 준비 (테이블명이 test.menuId, 컬럼 순서: category, menuName, price, imagePath, isSoldOut)
        String sql = "INSERT INTO test.menuId " + "(category, menuName, price, imagePath, isSoldOut) " + "VALUES (?, ?, ?, ?, ?)";

        // 3. PreparedStatement 생성 및 값 세팅
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, category);
            ps.setString(2, menuName);
            ps.setInt(3, priceInt);
            ps.setString(4, imagePath);
            ps.setBoolean(5, isSoldOut);

            ps.executeUpdate();
            loadAllMenusToEntity();
        }
    }

    public static void loadAllMenusToEntity() throws SQLException {
        Entity.menus.clear();
        String sql = "SELECT category, menuName, price, imagePath, isSoldOut " + "FROM test.menuId";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String category   = rs.getString("category");
                String menuName   = rs.getString("menuName");
                int    price      = rs.getInt("price");
                String imagePath  = rs.getString("imagePath");
                boolean isSoldOut = rs.getBoolean("isSoldOut");
                String priceStr = String.format("%,d원", price);
                Object[] row    = new Object[]{ category, menuName, priceStr, isSoldOut, imagePath };
                Entity.menus.add(row);
            }
        }
    }
}
