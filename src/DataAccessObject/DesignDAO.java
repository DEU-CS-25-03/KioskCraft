package DataAccessObject;
import java.sql.Connection;
import java.sql.SQLException;

public class DesignDAO {
    private final Connection conn;

    public DesignDAO(Connection conn) {
        this.conn = conn;
    }

    // 예시: 데이터 삽입
    public void insertDesign(String designId, String themeName, String themeColor, String logoPath, String fontName) throws SQLException {
        String sql = "INSERT INTO DESIGN_TBL (Design_ID, Theme_Name, Theme_Color, Logo_Path, Font_Name) VALUES (?, ?, ?, ?, ?)";
        try (var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, designId);
            pstmt.setString(2, themeName);
            pstmt.setString(3, themeColor);
            pstmt.setString(4, logoPath);
            pstmt.setString(5, fontName);
            pstmt.executeUpdate();
        }
    }

    // 추가적인 CRUD 메소드들...
}
