package DataAccessObject;

import java.sql.Connection;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * DBManager 클래스
 * - HikariCP 커넥션 풀을 사용하여 DB 커넥션을 관리
 * - DAO 인스턴스는 지연 생성(Lazy Loading) 방식으로 제공
 */
public class DBManager {
    // HikariCP 커넥션 풀
    private static HikariDataSource dataSource;

    // DAO 인스턴스들 (필요 시 지연 생성)
    private static DesignDAO designDAO;
    private static CategoryDAO categoryDAO;
    private static MenuDAO menuDAO;
    // 필요한 DAO만 추가, 사용하지 않는 DAO는 제거 가능

    private DBManager() {
        // 외부에서 인스턴스 생성 못하도록 생성자 비공개
    }

    /**
     * HikariCP 커넥션 풀 초기화
     * - dataSource가 이미 생성되어 있으면 재사용
     * - TiDB(MySQL 호환) 서버에 커넥션 풀 생성
     *
     * @throws SQLException 초기화 중 오류 발생 시 예외 던짐
     */
    public static void connectDB() throws SQLException {
        // 이미 풀 초기화 완료된 상태라면 그대로 반환
        if (dataSource != null && !dataSource.isClosed()) {
            return;
        }

        // HikariCP 설정
        HikariConfig config = new HikariConfig();
        // 1) JDBC URL 설정 (TiDB / MySQL 호환)
        config.setJdbcUrl("jdbc:mysql://gateway01.us-west-2.prod.aws.tidbcloud.com:4000/kiosk_db");
        // 2) DB 사용자 계정 및 비밀번호
        config.setUsername("3tXLfN5hUF3WufM.root");
        config.setPassword("XzG2jb79smpUZ34s");
        // 3) 풀 옵션 조정 (필요 시 변경)
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);  // 30초
        config.setIdleTimeout(600000);       // 10분
        config.setMaxLifetime(1800000);      // 30분
        config.setPoolName("KioskDBPool");   // 풀 이름 지정 (로깅 및 모니터링 용)

        // 4) HikariDataSource 생성으로 풀 초기화
        dataSource = new HikariDataSource(config);
        System.out.println("HikariCP Connection Pool Initialized");

        // DAO 인스턴스 초기화 (지연 생성 대상)
        designDAO   = null;
        categoryDAO = null;
        menuDAO     = null;
    }

    /**
     * 커넥션 반환
     * - dataSource가 없거나 닫혀 있으면 connectDB() 호출하여 초기화
     *
     * @return Connection 객체 (풀에서 가져온 커넥션)
     * @throws SQLException 커넥션 획득 중 오류 발생 시 예외 던짐
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null || dataSource.isClosed()) {
            connectDB();
        }
        return dataSource.getConnection();
    }

    /**
     * 커넥션 풀 전체 종료
     * - 애플리케이션 종료 시 호출하여 풀 자원(스레드, 커넥션 등) 해제
     */
    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("HikariCP Connection Pool Closed");
        }
    }

    // ────────────────────────────────────────────────────────────────────────
    // DAO 지연 생성 메서드들 (Lazy Loading)
    // ────────────────────────────────────────────────────────────────────────

    /**
     * DesignDAO 인스턴스 반환 (지연 생성)
     *
     * @return DesignDAO 인스턴스
     * @throws SQLException 커넥션 획득 중 오류 발생 시 예외 던짐
     */
    public static synchronized DesignDAO getDesignDAO() throws SQLException {
        if (designDAO == null) {
            designDAO = new DesignDAO(getConnection());
        }
        return designDAO;
    }

    /**
     * CategoryDAO 인스턴스 반환 (지연 생성)
     *
     * @return CategoryDAO 인스턴스
     * @throws SQLException 커넥션 획득 중 오류 발생 시 예외 던짐
     */
    public static synchronized CategoryDAO getCategoryDAO() throws SQLException {
        if (categoryDAO == null) {
            categoryDAO = new CategoryDAO(getConnection());
        }
        return categoryDAO;
    }

    /**
     * MenuDAO 인스턴스 반환 (지연 생성)
     *
     * @return MenuDAO 인스턴스
     * @throws SQLException 커넥션 획득 중 오류 발생 시 예외 던짐
     */
    public static synchronized MenuDAO getMenuDAO() throws SQLException {
        if (menuDAO == null) {
            menuDAO = new MenuDAO(getConnection());
        }
        return menuDAO;
    }
}
