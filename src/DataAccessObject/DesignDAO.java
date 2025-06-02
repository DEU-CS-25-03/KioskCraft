package DataAccessObject;

import DataTransferObject.Entity;
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
    Connection con;
    public DesignDAO(Connection conn) throws SQLException {
        con = conn;
        // themeId 테이블 긁어오기
        String fetchSql = "SELECT themeName, description, isDefault FROM test.themeId";
        // 임시로 저장하는 배열 생성
        ArrayList<Object[]> temp = new ArrayList<>();
        //PS 생성하고 데이터 긁어오기
        try (PreparedStatement ps = con.prepareStatement(fetchSql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String themeName   = rs.getString("themeName");
                String description = rs.getString("description");
                boolean isDefault  = rs.getBoolean("isDefault");

                temp.add(new Object[]{ themeName, description, isDefault });
            }
        }

        // 임시로 저장해놓은 거 Entity에 할당
        Entity.designs = new Object[temp.size()][3];
        for (int i = 0; i < temp.size(); i++) {
            Entity.designs[i][0] = temp.get(i)[0];
            Entity.designs[i][1] = temp.get(i)[1];
            Entity.designs[i][2] = temp.get(i)[2];
        }
    }

    public void updateDefaultDesign(String selectedDesign) throws SQLException {
        // 공백 제거
        selectedDesign = selectedDesign.trim();
        String clearSql = "UPDATE test.themeId SET isDefault = FALSE";
        String setSql   = "UPDATE test.themeId SET isDefault = TRUE WHERE themeName = ?";
        // 원래 auto-commit 상태를 저장
        boolean originalAutoCommit = con.getAutoCommit();
        try {
            // 트랜잭션 시작
            con.setAutoCommit(false);

            // 전부 0으로 변경
            try (PreparedStatement psClear = con.prepareStatement(clearSql)) {
                int cleared = psClear.executeUpdate();
                System.out.println("▶ updateDefaultDesign: clearedRows = " + cleared);
            }
            // 선택된 디자인만 1로 변경
            try (PreparedStatement psSet = con.prepareStatement(setSql)) {
                psSet.setString(1, selectedDesign);
                int affectedRows = psSet.executeUpdate();
                if (affectedRows == 0) {
                    // 원하는 테마가 DB에 존재하지 않는 경우
                    throw new SQLException("테마 '" + selectedDesign + "'을(를) default로 설정하지 못했습니다.");
                }
            }
            // 이상 없으면 커밋
            con.commit();
            System.out.println("▶ updateDefaultDesign: 커밋 완료");
        } catch (SQLException e) {
            con.rollback();
            System.err.println("▶ updateDefaultDesign: 롤백됨 - " + e.getMessage());
            throw e;
        } finally {
            // 트랜잭션 종료 후 원래 auto-commit 상태로 복원
            con.setAutoCommit(originalAutoCommit);
        }
    }
}