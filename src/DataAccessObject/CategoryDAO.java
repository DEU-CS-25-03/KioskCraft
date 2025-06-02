package DataAccessObject;

import DataTransferObject.Entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryDAO {
    private final Connection conn;

    public CategoryDAO(Connection conn) {
        this.conn = conn;
        loadCategories();
    }

    private void loadCategories() {
        String sql = "SELECT categoryName FROM test.categoryId";
        Entity.categories.clear();  // 기존 값을 비우고
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                Entity.categories.add(rs.getString("categoryName"));

        } catch (SQLException e) {
            throw new RuntimeException("카테고리 로드 실패", e);
        }
    }
}
