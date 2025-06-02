package DataAccessObject;

import DTO.Entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * DesignDAO는 test.themeId 테이블에서
 * themeName, description, isDefault 컬럼을 읽어와
 * DataSet.designs 배열을 초기화합니다.
 */
public class DesignDAO {
    public DesignDAO(Connection conn) throws SQLException {
        // 1) 임시 리스트에 조회 결과를 담는다.
        String fetchSql = "SELECT themeName, description, isDefault FROM test.themeId";
        ArrayList<Object[]> temp = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(fetchSql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String themeName   = rs.getString("themeName");
                String description = rs.getString("description");
                boolean isDefault  = rs.getBoolean("isDefault");

                temp.add(new Object[]{ themeName, description, isDefault });
            }
        }

        // 2) 리스트 크기만큼 DataSet.designs 배열을 할당
        Entity.designs = new Object[temp.size()][3];
        for (int i = 0; i < temp.size(); i++) {
            Entity.designs[i][0] = temp.get(i)[0];
            Entity.designs[i][1] = temp.get(i)[1];
            Entity.designs[i][2] = temp.get(i)[2];
        }
    }

    public static void updateDefaultDesign(Connection conn, String selectedDesign) throws SQLException {
        // 공백 제거
        selectedDesign = selectedDesign.trim();

        String clearSql = "UPDATE test.themeId SET isDefault = FALSE";
        String setSql   = "UPDATE test.themeId SET isDefault = TRUE WHERE themeName = ?";

        // 원래 auto-commit 상태를 저장
        boolean originalAutoCommit = conn.getAutoCommit();
        try {
            // 트랜잭션 시작
            conn.setAutoCommit(false);

            // 전부 0으로 변경
            try (PreparedStatement psClear = conn.prepareStatement(clearSql)) {
                int cleared = psClear.executeUpdate();
                System.out.println("▶ updateDefaultDesign: clearedRows = " + cleared);
            }

            // 선택된 디자인만 1로 변경
            try (PreparedStatement psSet = conn.prepareStatement(setSql)) {
                psSet.setString(1, selectedDesign);
                int affectedRows = psSet.executeUpdate();
                if (affectedRows == 0) {
                    // 원하는 테마가 DB에 존재하지 않는 경우
                    throw new SQLException("테마 '" + selectedDesign + "'을(를) default로 설정하지 못했습니다.");
                }
            }

            // 4) 이상 없으면 커밋
            conn.commit();
            System.out.println("▶ updateDefaultDesign: 커밋 완료");
        } catch (SQLException e) {
            conn.rollback();
            System.err.println("▶ updateDefaultDesign: 롤백됨 - " + e.getMessage());
            throw e;
        } finally {
            // 트랜잭션 종료 후 원래 auto-commit 상태로 복원
            conn.setAutoCommit(originalAutoCommit);
        }
    }


    /**
     * 이미 열린 Connection(conn)을 받아 test.themeId 테이블에서 모든 레코드를 조회한 뒤,
     * DataSet.designs 를 [rowCount][3] 형태로 채웁니다.
     *
     * @param conn DB 연결 객체 (이미 오픈된 상태)
     * @throws SQLException 쿼리 실행 도중 예외 발생 시
     */
}


// 예시: 데이터 삽입
//    public void insertDesign(String designId, String themeName, String themeColor, String logoPath, String fontName) throws SQLException {
//        String sql = "INSERT INTO DESIGN_TBL (Design_ID, Theme_Name, Theme_Color, Logo_Path, Font_Name) VALUES (?, ?, ?, ?, ?)";
//        try (var pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, designId);
//            pstmt.setString(2, themeName);
//            pstmt.setString(3, themeColor);
//            pstmt.setString(4, logoPath);
//            pstmt.setString(5, fontName);
//            pstmt.executeUpdate();
//        }
//    }
// 추가적인 CRUD 메소드들...

