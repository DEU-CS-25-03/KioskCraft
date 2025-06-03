package DataAccessObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private static DBManager instance;
    public Connection connection;

    // DAO 인스턴스들
    public static DesignDAO designDAO;
    //private static UserInfoDAO userInfoDAO;
    public static CouponDAO couponDAO;
    //private static PaymentMethodDAO paymentMethodDAO;
    public static CategoryDAO categoryDAO;
    public static MenuDAO menuDAO;
    public static CartDAO cartDAO;
    //private static CartItemDAO cartItemDAO;
    public static PaymentRecordDAO paymentRecordDAO;
    public static OrderStatusDAO orderStatusDAO;
    public static LanguageDAO languageDAO;

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
                System.out.println(e.getMessage());
                throw new SQLException("MySQL JDBC Driver not found");
            }

            // TiDB 연결 정보
            String url = "jdbc:mysql://3tXLfN5hUF3WufM.root:XzG2jb79smpUZ34s@gateway01.us-west-2.prod.aws.tidbcloud.com:4000/kiosk_db";
            String user = "3tXLfN5hUF3WufM.root";
            String password = "XzG2jb79smpUZ34s"; // passward 수시로 수정 필요함

            connection = DriverManager.getConnection(url, user, password);
            System.out.println("DB Connection Successful");

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
        if (connection == null || connection.isClosed()) connectDB();
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // 각 DAO의 getter
    //위에 public로 선언해놔서 그대로 참조하면 되니까 getter는 따로 없어도 될 거 같아요
//    public DesignDAO getDesignDAO() { return designDAO; }
//    //public UserInfoDAO getUserInfoDAO() { return userInfoDAO; }
//    public CouponDAO getCouponDAO() { return couponDAO; }
//    //public PaymentMethodDAO getPaymentMethodDAO() { return paymentMethodDAO; }
//    public CategoryDAO getCategoryDAO() { return categoryDAO; }
//    public MenuDAO getMenuDAO() { return menuDAO; }
//    public CartDAO getCartDAO() { return cartDAO; }
//
//    //public CartItemDAO getCartItemDAO() { return cartItemDAO; }
//    public PaymentRecordDAO getPaymentRecordDAO() { return paymentRecordDAO; }
//    public OrderStatusDAO getOrderStatusDAO() { return orderStatusDAO; }
//    public LanguageDAO getLanguageDAO() { return languageDAO; }
}
