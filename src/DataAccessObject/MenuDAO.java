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
     * insertMenu 메서드
     * - 새로운 메뉴를 DB에 삽입하고, 삽입 후 Entity.menus를 최신화
     * @param category   메뉴 카테고리 (String)
     * @param menuName   메뉴 이름 (String)
     * @param priceStr   "6,000원" 등의 형태 문자열(price 포함)
     * @param isSoldOut  품절 여부(boolean)
     * @param imagePath  이미지 경로(String)
     * @throws SQLException SQL 실행 실패 시 예외 발생
     */
    public static void insertMenu(String category, String menuName, String priceStr, boolean isSoldOut, String imagePath)
            throws SQLException {
        // 1) priceStr에서 숫자만 추출하여 int로 변환
        //    ex) "6,000원" → "6000" → int 6000
        String onlyDigits = priceStr.replaceAll("[^0-9]", "");
        int priceInt = Integer.parseInt(onlyDigits);

        // 2) SQL 쿼리: test.menuId 테이블에 category, menuName, price, imagePath, isSoldOut 컬럼 순으로 삽입
        String sql = "INSERT INTO test.menuId (category, menuName, price, imagePath, isSoldOut) VALUES (?, ?, ?, ?, ?)";

        // 3) PreparedStatement 생성 및 파라미터 바인딩
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, category);
            ps.setString(2, menuName);
            ps.setInt(3, priceInt);
            ps.setString(4, imagePath);
            ps.setBoolean(5, isSoldOut);

            ps.executeUpdate();         // DB에 메뉴 삽입
            loadAllMenusToEntity();     // 삽입 후 Entity.menus를 다시 로드하여 최신화
        }
    }

    /**
     * loadAllMenusToEntity 메서드
     * - DB의 모든 메뉴 데이터를 읽어와 Entity.menus 리스트를 갱신
     * @throws SQLException SQL 실행 실패 시 예외 발생
     */
    public static void loadAllMenusToEntity() throws SQLException {
        Entity.menus.clear(); // 기존 메뉴 목록 모두 삭제

        // 메뉴 정보를 가져오는 SQL: category, menuName, price, imagePath, isSoldOut 컬럼 조회
        String sql = "SELECT category, menuName, price, imagePath, isSoldOut FROM test.menuId";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String category   = rs.getString("category");
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
