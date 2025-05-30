package DataAccessObject;

import java.sql.Connection;

public class CategoryDAO {
    private final Connection conn;
    public CategoryDAO(Connection conn) {
        this.conn = conn;
    }
}
