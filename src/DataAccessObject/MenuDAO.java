package DataAccessObject;

import DataTransferObject.Entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * MenuDAO: test.menuId 테이블에서 메뉴 데이터를 조회하여 Entity.menus에 저장한다.
 */
public class MenuDAO {
    public MenuDAO(Connection conn) throws SQLException {
        // 1) 기존에 남아있는 메뉴 정보가 있으면 초기화
        Entity.menus.clear();

        // 2) SQL 문장: category, menuName, price(int), imagePath, isSoldOut(tinyint) 순서로 조회
        String sql = "SELECT category, menuName, price, imagePath, isSoldOut FROM test.menuId";

        // 3) PreparedStatement 생성 및 실행
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // 4) 결과를 한 행씩 순회하며 Entity.menus에 문자열 형태로 추가
            while (rs.next()) {
                String category   = rs.getString("category");
                String menuName   = rs.getString("menuName");
                int    price      = rs.getInt("price");
                String imagePath  = rs.getString("imagePath");

                // string으로 변경
                String priceStr = NumberFormat.getNumberInstance(Locale.KOREA).format(price) + "원";

                // tinyint 컬럼인 isSoldOut은 0/1로 들어왔다고 가정.
                // getBoolean을 쓰면 0→false, 그 외(1)→true로 자동 변환된다.
                boolean isSoldOut = rs.getBoolean("isSoldOut");

                Object[] Row = new Object[]{ category, menuName, priceStr, isSoldOut, imagePath };
                Entity.menus.add(Row);
            }
        }
    }
}
