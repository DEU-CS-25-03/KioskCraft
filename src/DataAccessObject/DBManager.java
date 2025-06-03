package DataAccessObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private static DBManager instance;
    private static Connection connection;

    // DAO 인스턴스들을 모두 null로 선언 (지연 생성할 것)
    public static DesignDAO designDAO;
    public static CouponDAO couponDAO;
    public static CategoryDAO categoryDAO;
    public static MenuDAO menuDAO;
    public static CartDAO cartDAO;
    public static PaymentRecordDAO paymentRecordDAO;
    public static OrderStatusDAO orderStatusDAO;
    public static LanguageDAO languageDAO;

    private DBManager() {}

    public static synchronized DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    /**
     * 커넥션만 빠르게 초기화하고 DAO 생성은 미뤄둔다.
     */
    public static void connectDB() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return; // 이미 연결되어 있으면 패스
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }

        // TiDB (MySQL 호환) 연결 정보
        String url      = "jdbc:mysql://gateway01.us-west-2.prod.aws.tidbcloud.com:4000/kiosk_db";
        String user     = "3tXLfN5hUF3WufM.root";
        String password = "XzG2jb79smpUZ34s";

        // 실제로 물리적 커넥션을 여기서만 한 번 연다.
        connection = DriverManager.getConnection(url, user, password);
        System.out.println("DB Connection Successful");

        // DAO 생성은 모두 지연(lazy) 처리한다.
        designDAO        = null;
        couponDAO        = null;
        categoryDAO      = null;
        menuDAO          = null;
        cartDAO          = null;
        paymentRecordDAO = null;
        orderStatusDAO   = null;
        languageDAO      = null;
    }

    /**
     * 필요한 시점에 커넥션을 보장받을 수 있도록.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connectDB();
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("DB Connection Closed");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // ───────────────────────────────────────────────────────────
    // DAO 지연 생성 (Lazy Loading) 메서드들
    // ───────────────────────────────────────────────────────────
    //일단 리턴 남겨놓을게요 혹시 모르니까
    public static synchronized DesignDAO getDesignDAO() throws SQLException {
        if (designDAO == null) {
            designDAO = new DesignDAO(getConnection());
        }
        return designDAO;
    }

    public synchronized CouponDAO getCouponDAO() throws SQLException {
        if (couponDAO == null) {
            couponDAO = new CouponDAO(getConnection());
        }
        return couponDAO;
    }

    public static synchronized void getCategoryDAO() throws SQLException {
        if (categoryDAO == null) {
            categoryDAO = new CategoryDAO(getConnection());
        }
    }

    public static synchronized void getMenuDAO() throws SQLException {
        if (menuDAO == null) {
            menuDAO = new MenuDAO(getConnection());
        }
    }

    public synchronized CartDAO getCartDAO() throws SQLException {
        if (cartDAO == null) {
            cartDAO = new CartDAO(getConnection());
        }
        return cartDAO;
    }

    public synchronized PaymentRecordDAO getPaymentRecordDAO() throws SQLException {
        if (paymentRecordDAO == null) {
            paymentRecordDAO = new PaymentRecordDAO(getConnection());
        }
        return paymentRecordDAO;
    }

    public synchronized OrderStatusDAO getOrderStatusDAO() throws SQLException {
        if (orderStatusDAO == null) {
            orderStatusDAO = new OrderStatusDAO(getConnection());
        }
        return orderStatusDAO;
    }

    public synchronized LanguageDAO getLanguageDAO() throws SQLException {
        if (languageDAO == null) {
            languageDAO = new LanguageDAO(getConnection());
        }
        return languageDAO;
    }
}
