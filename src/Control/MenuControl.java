package Control;

import DataAccessObject.DBManager;
import DataAccessObject.MenuDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static DataAccessObject.MenuDAO.loadAllMenusToEntity;

public class MenuControl {
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
    public static void insertMenu(String category, String menuName, String priceStr, boolean isSoldOut, String imagePath, Connection conn) throws SQLException {
        // 1) priceStr에서 숫자만 추출하여 int로 변환
        //    ex) "6,000원" → "6000" → int 6000
        String onlyDigits = priceStr.replaceAll("[^0-9]", "");
        int priceInt = Integer.parseInt(onlyDigits);

        // 2) SQL 쿼리: test.menuId 테이블에 category, menuName, price, imagePath, isSoldOut 컬럼 순으로 삽입
        String sql = "INSERT INTO test.menuId (category, menuName, price, imagePath, isSoldOut) VALUES (?, ?, ?, ?, ?)";

        // 3) PreparedStatement 생성 및 파라미터 바인딩
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            ps.setString(2, menuName);
            ps.setInt(3, priceInt);
            ps.setString(4, imagePath);
            ps.setBoolean(5, isSoldOut);

            ps.executeUpdate();         // DB에 메뉴 삽입
            // 메뉴 최신화
            loadAllMenusToEntity();
        }
    }

    /**
     * deleteMenu 메서드
     * - DAO에 delete 기능이 없으므로, Control 계층에서 바로 SQL DELETE를 실행
     * - 삭제 후 Entity.menus를 DAO를 통해 다시 로드하고, 화면 모델(DefaultTableModel)에서도 해당 행 제거
     *
     * @param menuName  삭제할 메뉴 이름
     * @param model     JTable의 DefaultTableModel
     * @param rowIndex  삭제할 행의 인덱스
     */
    public static void deleteMenu(String menuName, DefaultTableModel model, int rowIndex) {
        // 1) DB 연결 후 직접 DELETE SQL 실행
        try (Connection conn = DBManager.getConnection()) {
            String sql = "DELETE FROM test.menuId WHERE menuName = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, menuName);
                int affected = ps.executeUpdate();
                if (affected == 0) {
                    // 삭제할 메뉴가 없을 경우
                    JOptionPane.showMessageDialog(null, "'" + menuName + "' 메뉴를 찾을 수 없습니다.", "삭제 실패", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // 2) DB에서 삭제가 성공했으니 Entity.menus를 최신화
            //    MenuDAO.loadAllMenusToEntity()를 호출하려면 DAO 객체를 생성해야 함.
            //    이때 MenuDAO 생성자를 통해 connection이 세팅되고 loadAllMenusToEntity()가 실행됩니다.
            new MenuDAO(conn);  // 이 한 줄이 loadAllMenusToEntity()를 호출하게 됨
            DBManager.closeConnection(conn);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "메뉴 삭제 중 오류 발생:\n" + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3) 화면 모델에서 해당 행 제거
        model.removeRow(rowIndex);

        // 4) 삭제 완료 메시지
        JOptionPane.showMessageDialog(null, "'" + menuName + "' 메뉴가 삭제되었습니다.", "삭제 완료", JOptionPane.INFORMATION_MESSAGE);
    }
}