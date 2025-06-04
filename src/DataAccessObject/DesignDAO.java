package DataAccessObject;

import DataTransferObject.Entity;
import java.sql.*;
import java.util.ArrayList;

public class DesignDAO {

    // 생성자에서 커넥션을 받지 않음
    public DesignDAO() throws SQLException {
        String sql = "SELECT themeName, description, isDefault FROM test.themeId";
        ArrayList<Object[]> temp = new ArrayList<>();
        // 커넥션 풀에서 커넥션을 빌려 사용
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                temp.add(new Object[] {
                        rs.getString("themeName"),
                        rs.getString("description"),
                        rs.getBoolean("isDefault")
                });
            }
        }
        Entity.designs = new Object[temp.size()][3];
        for (int i = 0; i < temp.size(); i++) {
            Entity.designs[i] = temp.get(i);
        }
    }

    // 커넥션 풀에서 커넥션을 빌려 트랜잭션 처리
    public static void updateDefaultDesign(String selectedDesign) throws SQLException {
        String clearSql = "UPDATE test.themeId SET isDefault = FALSE";
        String setSql = "UPDATE test.themeId SET isDefault = TRUE WHERE themeName = ?";
        // 커넥션 풀에서 커넥션을 빌려옴
        try (Connection conn = DBManager.getConnection()) {
            boolean originalAutoCommit = conn.getAutoCommit();
            try {
                conn.setAutoCommit(false);
                try (PreparedStatement psClear = conn.prepareStatement(clearSql)) {
                    psClear.executeUpdate();
                }
                try (PreparedStatement psSet = conn.prepareStatement(setSql)) {
                    psSet.setString(1, selectedDesign);
                    int affected = psSet.executeUpdate();
                    if (affected == 0)
                        throw new SQLException("테마 '" + selectedDesign + "' 적용 실패");
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(originalAutoCommit);
            }
        }
    }
}
