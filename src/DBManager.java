//import DataAccessObject.*;
//import java.sql.Connection;
//
//public class DBManager {
//    private static DBManager instance;
//    private Connection connection;
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
//    private DBManager() { /* ... */ }
//
//    public static DBManager getInstance() {
//        if (instance == null) instance = new DBManager();
//        return instance;
//    }
//
//    public void connectDB(String url, String user, String pw) {
//        // JDBC 드라이버 로딩 및 Connection 생성
//        // connection = DriverManager.getConnection(url, user, pw);
//        // 각 DAO에 connection 주입
//        menuDAO = new MenuDAO(connection);
//        categoryDAO = new CategoryDAO(connection);
//        // ...
//    }
//
//    public MenuDAO getMenuDAO() { return menuDAO; }
//    public CategoryDAO getCategoryDAO() { return categoryDAO; }
//    // ... 기타 DAO getter
//
//    public static class DBManager {
//        private static DBManager instance;
//        private Connection connection;
//        private MenuDAO menuDAO;
//        private CategoryDAO categoryDAO;
//        // ... 기타 DAO
//
//        private DBManager() { /* ... */ }
//
//        public static DBManager getInstance() {
//            if (instance == null) instance = new DBManager();
//            return instance;
//        }
//
//        public void connectDB(String url, String user, String pw) {
//            // JDBC 드라이버 로딩 및 Connection 생성
//            // connection = DriverManager.getConnection(url, user, pw);
//            // 각 DAO에 connection 주입
//            menuDAO = new MenuDAO(connection);
//            categoryDAO = new CategoryDAO(connection);
//            // ...
//        }
//        public MenuDAO getMenuDAO() { return menuDAO; }
//        public CategoryDAO getCategoryDAO() { return categoryDAO; }
//        // ... 기타 DAO getter
//
//    }
//}
