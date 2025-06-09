package DataAccessObject;

import DataTransferObject.Entity;
import java.sql.*;
import java.util.ArrayList;

/**
 * DesignDAO 클래스
 * - 테마(themeId) 정보를 DB에서 로드 및 기본 테마 업데이트 기능 제공
 */
public class DesignDAO {
    private static Connection con;

    /**
     * 생성자
     * @param conn DB 연결 객체
     * @throws SQLException 연결 상태에 따라 예외 던짐
     */
    public DesignDAO(Connection conn) throws SQLException {
        con = conn;
        loadDesigns(); // 인스턴스 생성 시 캐시 업데이트
    }

    /**
     * updateDefaultDesign 메서드
     * - 모든 테마(isDefault) 값을 FALSE로 설정한 뒤, 선택된 테마만 TRUE로 업데이트
     * @param conn            DB 연결 객체
     * @param selectedDesign  적용할 기본 테마 이름
     * @throws SQLException   SQL 실행 중 오류 발생 시
     */
    public static void updateDefaultDesign(Connection conn, String selectedDesign) throws SQLException {
        String clearSql = "UPDATE KioskDB.themeId SET isDefault = FALSE";
        String setSql   = "UPDATE KioskDB.themeId SET isDefault = TRUE WHERE themeName = ?";
        boolean originalAutoCommit = conn.getAutoCommit();

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement psClear = conn.prepareStatement(clearSql)) {
                psClear.executeUpdate(); // 모든 isDefault를 FALSE로
            }

            try (PreparedStatement psSet = conn.prepareStatement(setSql)) {
                psSet.setString(1, selectedDesign);
                int affected = psSet.executeUpdate();
                if (affected == 0) {
                    throw new SQLException("테마 '" + selectedDesign + "' 적용 실패");
                }
            }

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(originalAutoCommit);
        }
    }

    /**
     * loadDesigns 메서드
     * - DB에서 themeId 테이블을 조회하여 Entity.designs 캐시에 저장
     * @throws SQLException  조회 중 오류 발생 시
     */
    public static void loadDesigns() throws SQLException {
        String sql = "SELECT themeName, description, isDefault FROM KioskDB.themeId";
        ArrayList<Object[]> temp = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                temp.add(new Object[] {rs.getString("themeName"), rs.getString("description"), rs.getBoolean("isDefault")});
            }
        }

        Entity.designs = new Object[temp.size()][3];
        for (int i = 0; i < temp.size(); i++) {
            Entity.designs[i] = temp.get(i);
        }
    }
}
