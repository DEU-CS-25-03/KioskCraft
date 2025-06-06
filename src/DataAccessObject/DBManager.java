package DataAccessObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBManager 클래스
 * - 싱글톤 패턴으로 DB 커넥션을 관리
 * - DAO 인스턴스는 지연 생성(Lazy Loading) 방식으로 관리
 */
public class DBManager {
    private static DBManager instance;         // 싱글톤 인스턴스
    private static Connection connection;      // JDBC 커넥션

    // DAO 인스턴스들 (지연 생성 예정)
    public static DesignDAO designDAO;
    public static CouponDAO couponDAO;
    public static CategoryDAO categoryDAO;
    public static MenuDAO menuDAO;
    public static CartDAO cartDAO;
    public static PaymentRecordDAO paymentRecordDAO;
    public static OrderStatusDAO orderStatusDAO;
    public static LanguageDAO languageDAO;

    private DBManager() {} // 생성자 비공개

    /**
     * 싱글톤 인스턴스 반환
     * @return DBManager 인스턴스
     */
    public static synchronized DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    /**
     * DB 연결 초기화
     * - 기존 연결이 유효하면 재사용
     * - MySQL JDBC 드라이버 로드 후 TiDB(MySQL 호환) 연결 생성
     * @throws SQLException 드라이버 로드 실패 또는 연결 중 오류 시
     */
    public static void connectDB() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return; // 이미 연결이 되어 있으면 패스
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
        String url      = "jdbc:mysql://gateway01.us-west-2.prod.aws.tidbcloud.com:4000/kiosk_db";
        String user     = "3tXLfN5hUF3WufM.root";
        String password = "XzG2jb79smpUZ34s";
        connection = DriverManager.getConnection(url, user, password);
        System.out.println("DB Connection Successful");
        // DAO 인스턴스는 지연 생성
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
     * 커넥션 반환
     * - 연결이 없거나 닫혀 있으면 connectDB() 호출하여 새 연결 생성
     * @return Connection 객체
     * @throws SQLException 연결 중 오류 시
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connectDB();
        }
        return connection;
    }

    /**
     * DB 연결 종료
     * - 연결이 열려 있으면 닫고 메시지 출력
     */
    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("DB Connection Closed");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // ────────────────────────────────────────────────────────────────────────
    // DAO 지연 생성 메서드들 (Lazy Loading)
    // ────────────────────────────────────────────────────────────────────────

    /**
     * DesignDAO 인스턴스 반환
     * @return DesignDAO 인스턴스
     * @throws SQLException 연결 중 오류 시
     */
    public static synchronized DesignDAO getDesignDAO() throws SQLException {
        if (designDAO == null) {
            designDAO = new DesignDAO(getConnection());
        }
        return designDAO;
    }

    /**
     * CouponDAO 인스턴스 반환
     * @return CouponDAO 인스턴스
     * @throws SQLException 연결 중 오류 시
     */
    public synchronized CouponDAO getCouponDAO() throws SQLException {
        if (couponDAO == null) {
            couponDAO = new CouponDAO(getConnection());
        }
        return couponDAO;
    }

    /**
     * CategoryDAO 인스턴스 생성 (반환 없음)
     * @throws SQLException 연결 중 오류 시
     */
    public static synchronized void getCategoryDAO() throws SQLException {
        if (categoryDAO == null) {
            categoryDAO = new CategoryDAO(getConnection());
        }
    }

    /**
     * MenuDAO 인스턴스 생성 (반환 없음)
     * @throws SQLException 연결 중 오류 시
     */
    public static synchronized void getMenuDAO() throws SQLException {
        if (menuDAO == null) {
            menuDAO = new MenuDAO(getConnection());
        }
    }

    /**
     * CartDAO 인스턴스 반환
     * @return CartDAO 인스턴스
     * @throws SQLException 연결 중 오류 시
     */
    public synchronized CartDAO getCartDAO() throws SQLException {
        if (cartDAO == null) {
            cartDAO = new CartDAO(getConnection());
        }
        return cartDAO;
    }

    /**
     * PaymentRecordDAO 인스턴스 반환
     * @return PaymentRecordDAO 인스턴스
     * @throws SQLException 연결 중 오류 시
     */
    public synchronized PaymentRecordDAO getPaymentRecordDAO() throws SQLException {
        if (paymentRecordDAO == null) {
            paymentRecordDAO = new PaymentRecordDAO(getConnection());
        }
        return paymentRecordDAO;
    }

    /**
     * OrderStatusDAO 인스턴스 반환
     * @return OrderStatusDAO 인스턴스
     * @throws SQLException 연결 중 오류 시
     */
    public synchronized OrderStatusDAO getOrderStatusDAO() throws SQLException {
        if (orderStatusDAO == null) {
            orderStatusDAO = new OrderStatusDAO(getConnection());
        }
        return orderStatusDAO;
    }

    /**
     * LanguageDAO 인스턴스 반환
     * @return LanguageDAO 인스턴스
     * @throws SQLException 연결 중 오류 시
     */
    public synchronized LanguageDAO getLanguageDAO() throws SQLException {
        if (languageDAO == null) {
            languageDAO = new LanguageDAO(getConnection());
        }
        return languageDAO;
    }
}
