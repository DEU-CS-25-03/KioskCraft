package DataAccessObject;

import DataTransferObject.CategoryDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    // 카테고리 전체 조회
    public List<CategoryDTO> selectAllCategories() throws SQLException {
        String sql = "SELECT categoryName FROM test.categoryId";
        List<CategoryDTO> list = new ArrayList<>();
        // [커넥션 풀] 각 메서드에서 커넥션을 빌려서 사용
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new CategoryDTO(rs.getString("categoryName")));
            }
        }
        return list;
    }

    // 카테고리 등록
    public void insertCategory(CategoryDTO category) throws SQLException {
        String sql = "INSERT INTO test.categoryId (categoryName) VALUES (?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category.getCategoryName());
            ps.executeUpdate();
        }
    }

    // 카테고리명 수정
    public void updateCategory(String oldName, String newName) throws SQLException {
        String sql = "UPDATE test.categoryId SET categoryName=? WHERE categoryName=?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newName);
            ps.setString(2, oldName);
            ps.executeUpdate();
        }
    }

    // 카테고리 삭제
    public void deleteCategory(String categoryName) throws SQLException {
        String sql = "DELETE FROM test.categoryId WHERE categoryName=?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categoryName);
            ps.executeUpdate();
        }
    }
}
