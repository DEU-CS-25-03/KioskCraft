import DataAccessObject.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private static DBManager instance;
    private Connection connection;

    // TiDB 연결 정보
    private final String url = "jdbc:mysql://3tXLfN5hUF3WufM.root:QnZoDMZWjRoVo7xl@gateway01.us-west-2.prod.aws.tidbcloud.com:4000/kiosk_db";
    private final String user = "3tXLfN5hUF3WufM.root";
    private final String password = "QnZoDMZWjRoVo7xl";  // passward 수시로 수정 필요함

    // DAO 인스턴스들
    private DesignDAO designDAO;
    //private UserInfoDAO userInfoDAO;
    private CouponDAO couponDAO;
    //private PaymentMethodDAO paymentMethodDAO;
    private CategoryDAO categoryDAO;
    private MenuDAO menuDAO;
    private CartDAO cartDAO;
    //private CartItemDAO cartItemDAO;
    private PaymentRecordDAO paymentRecordDAO;
    private OrderStatusDAO orderStatusDAO;
    private LanguageDAO languageDAO;

    private DBManager() {}

    public static synchronized DBManager getInstance() {
        if (instance == null) instance = new DBManager();
        return instance;
    }

    public void connectDB() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new SQLException("MySQL JDBC Driver not found");
            }
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("TiDB 연결 성공!");

            // DAO 인스턴스 생성 및 Connection 주입
            designDAO = new DesignDAO(connection);
            //userInfoDAO = new UserInfoDAO(connection);
            couponDAO = new CouponDAO(connection);
            //paymentMethodDAO = new PaymentMethodDAO(connection);
            categoryDAO = new CategoryDAO(connection);
            menuDAO = new MenuDAO(connection);
            cartDAO = new CartDAO(connection);
            //cartItemDAO = new CartItemDAO(connection);
            paymentRecordDAO = new PaymentRecordDAO(connection);
            orderStatusDAO = new OrderStatusDAO(connection);
            languageDAO = new LanguageDAO(connection);
        }
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connectDB();
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 각 DAO의 getter
    public DesignDAO getDesignDAO() { return designDAO; }
    //public UserInfoDAO getUserInfoDAO() { return userInfoDAO; }
    public CouponDAO getCouponDAO() { return couponDAO; }
    //public PaymentMethodDAO getPaymentMethodDAO() { return paymentMethodDAO; }
    public CategoryDAO getCategoryDAO() { return categoryDAO; }
    public MenuDAO getMenuDAO() { return menuDAO; }
    public CartDAO getCartDAO() { return cartDAO; }

    //public CartItemDAO getCartItemDAO() { return cartItemDAO; }
    public PaymentRecordDAO getPaymentRecordDAO() { return paymentRecordDAO; }
    public OrderStatusDAO getOrderStatusDAO() { return orderStatusDAO; }
    public LanguageDAO getLanguageDAO() { return languageDAO; }
}
