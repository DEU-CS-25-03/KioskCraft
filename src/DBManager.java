import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//
//// DAO import 예시
//// import DataAccessObject.*;

public class DBManager {
//    private static DBManager instance;
//    private Connection connection;
//
//    // DAO 인스턴스들
//    private MenuDAO menuDAO;
//    private CategoryDAO categoryDAO;
//    private DesignDAO designDAO;
//    private PaymentRecordDAO paymentRecordDAO;
//    private PointNumberDAO pointNumberDAO;
//    private OrderStatusDAO orderStatusDAO;
//    private CouponDAO couponDAO;
//    private CartDAO cartDAO;
//    private LanguageDAO languageDAO;
//
//    // DB 연결 정보
//    private final String url = "jdbc:mysql://localhost:3306/cafe";
//    private final String user = "root";
//    private final String password = "password";
//
//    private DBManager() {}
//
//    public static synchronized DBManager getInstance() {
//        if (instance == null) instance = new DBManager();
//        return instance;
//    }
//
//    // DB 연결 및 DAO 인스턴스 생성
//    public void connectDB() throws SQLException {
//        if (connection == null || connection.isClosed()) {
//            connection = DriverManager.getConnection(url, user, password);
//        }
//        // DAO 객체 생성 및 Connection 주입
//        menuDAO = new MenuDAO(connection);
//        categoryDAO = new CategoryDAO(connection);
//        designDAO = new DesignDAO(connection);
//        paymentRecordDAO = new PaymentRecordDAO(connection);
//        pointNumberDAO = new PointNumberDAO(connection);
//        orderStatusDAO = new OrderStatusDAO(connection);
//        couponDAO = new CouponDAO(connection);
//        cartDAO = new CartDAO(connection);
//        languageDAO = new LanguageDAO(connection);
//    }
//
//    public void closeConnection() {
//        try {
//            if (connection != null && !connection.isClosed()) {
//                connection.close();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // DAO Getter
//    public MenuDAO getMenuDAO() { return menuDAO; }
//    public CategoryDAO getCategoryDAO() { return categoryDAO; }
//    public DesignDAO getDesignDAO() { return designDAO; }
//    public PaymentRecordDAO getPaymentRecordDAO() { return paymentRecordDAO; }
//    public PointNumberDAO getPointNumberDAO() { return pointNumberDAO; }
//    public OrderStatusDAO getOrderStatusDAO() { return orderStatusDAO; }
//    public CouponDAO getCouponDAO() { return couponDAO; }
//    public CartDAO getCartDAO() { return cartDAO; }
//    public LanguageDAO getLanguageDAO() { return languageDAO; }
//
//    // Connection getter (필요시)
//    public Connection getConnection() throws SQLException {
//        if (connection == null || connection.isClosed()) {
//            connectDB();
//        }
//        return connection;
//    }
}
