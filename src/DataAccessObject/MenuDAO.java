package DataAccessObject;

import DataTransferObject.Entity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MenuDAO 클래스
 * - DB의 menuId 테이블과 Entity.menus 간 동기화를 담당
 * - 메뉴 추가(insertMenu) 및 전체 메뉴를 Entity에 로드(loadAllMenusToEntity) 기능 제공
 */
public class MenuDAO {
    private static Connection connection = null; // DB 연결 객체 (싱글턴처럼 사용)

    /**
     * 생성자
     * @param conn DB 연결 객체
     * @throws SQLException loadAllMenusToEntity 호출 시 예외 전파
     */
    public MenuDAO(Connection conn) throws SQLException {
        connection = conn;
        loadAllMenusToEntity(); // DAO 생성 시 DB의 최신 메뉴 목록을 Entity로 로드
    }

    /**
     * loadAllMenusToEntity 메서드
     * - DB의 모든 메뉴 데이터를 읽어와 Entity.menus 리스트를 갱신
     * @throws SQLException SQL 실행 실패 시 예외 발생
     */
    public static void loadAllMenusToEntity() throws SQLException {
        Entity.menus.clear(); // 기존 메뉴 목록 모두 삭제

        // 메뉴 정보를 가져오는 SQL: category, menuName, price, imagePath, isSoldOut 컬럼 조회
        String sql = "SELECT categoryName, menuName, price, imagePath, isSoldOut FROM test.menuId";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String category   = rs.getString("categoryName");
                String menuName   = rs.getString("menuName");
                int    price      = rs.getInt("price");
                String imagePath  = rs.getString("imagePath");
                boolean isSoldOut = rs.getBoolean("isSoldOut");

                // 저장할 가격 문자열: "1,000원" 형식
                String priceStr = String.format("%,d원", price);

                // Entity.menus에 담을 Object 배열: {카테고리, 메뉴명, "가격원", 품절여부, 이미지경로}
                Object[] row = new Object[]{ category, menuName, priceStr, isSoldOut, imagePath };
                Entity.menus.add(row);
            }
        }
    }
}
