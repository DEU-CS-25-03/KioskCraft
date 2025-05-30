package DataAccessObject;

import java.sql.Connection;

public class CartDAO {
    private final Connection conn;
    public CartDAO(Connection conn) {
        this.conn = conn;
    }
}
