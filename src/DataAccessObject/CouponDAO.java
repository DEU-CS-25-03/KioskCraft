package DataAccessObject;

import java.sql.Connection;

public class CouponDAO {
    private final Connection conn;
    public CouponDAO(Connection conn) {
        this.conn = conn;
    }
}
