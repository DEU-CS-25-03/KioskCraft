package DataAccessObject;

import java.sql.Connection;

public class LanguageDAO {
    private final Connection conn;

    public LanguageDAO(Connection conn) {
        this.conn = conn;

    }
}
