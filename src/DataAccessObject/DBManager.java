package DataAccessObject;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBManager {
    // HikariCP 커넥션 풀 객체 (정적 필드)
    private static HikariDataSource dataSource;

    // DAO 인스턴스들 (필요시 getConnection() 사용)
    public static DesignDAO designDAO;
    public static CouponDAO couponDAO;
    public static CategoryDAO categoryDAO;
    public static MenuDAO menuDAO;
    public static CartDAO cartDAO;
    public static PaymentRecordDAO paymentRecordDAO;
    public static OrderStatusDAO orderStatusDAO;
    public static LanguageDAO languageDAO;

    // 생성자 비공개 (싱글톤 불필요, 정적 클래스 형태)
    private DBManager() {}

    // 정적 초기화 블록에서 커넥션 풀 설정 및 생성
    static {
        try {
            HikariConfig config = new HikariConfig();

            // DB 접속 정보 설정
            config.setJdbcUrl("jdbc:mysql://gateway01.us-west-2.prod.aws.tidbcloud.com:4000/kiosk_db");
            config.setUsername("3tXLfN5hUF3WufM.root");
            config.setPassword("XzG2jb79smpUZ34s");

            // 커넥션 풀 옵션 설정 (실무에서는 환경에 맞게 조정)
            config.setMaximumPoolSize(10);           // 최대 커넥션 수
            config.setMinimumIdle(2);                // 최소 유휴 커넥션 수
            config.setConnectionTimeout(30000);      // 커넥션 대기 최대 시간(ms)
            config.setIdleTimeout(600000);           // 유휴 커넥션 유지 시간(ms)
            config.setMaxLifetime(1800000);          // 커넥션 최대 수명(ms)
            config.setConnectionTestQuery("SELECT 1"); // 커넥션 유효성 검사 쿼리

            // 커넥션 풀 생성
            dataSource = new HikariDataSource(config);
            System.out.println("HikariCP Connection Pool Initialized");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize HikariCP", e);
        }
    }

    /**
     * 커넥션 풀에서 커넥션을 빌려 반환
     * 각 DAO/서비스에서 필요할 때마다 호출하여 사용 (try-with-resources 권장)
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * DAO 인스턴스 예시 (생성자에서 커넥션을 받지 않고, 내부에서 getConnection() 사용하도록 리팩토링 필요)
     */
    public static synchronized DesignDAO getDesignDAO() throws SQLException {
        if (designDAO == null) {
            designDAO = new DesignDAO(); // 내부에서 getConnection() 사용하도록 구현
        }
        return designDAO;
    }
    // 다른 DAO들도 동일하게 구현

    /**
     * 커넥션 풀 종료 (애플리케이션 종료 시 호출)
     */
    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("HikariCP Connection Pool Closed");
        }
    }
}
