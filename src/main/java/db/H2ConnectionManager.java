package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class H2ConnectionManager implements ConnectionManager {

    private static final Logger logger = LoggerFactory.getLogger(H2ConnectionManager.class);

    private static final ThreadLocal<Connection> txConn = new ThreadLocal<>();

    private static final String URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public H2ConnectionManager() {
        initSchema();
    }

    private void initSchema() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS USERS (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            user_id VARCHAR(255) NOT NULL UNIQUE,
                            email VARCHAR(255) NOT NULL,
                            name VARCHAR(255) NOT NULL,
                            password VARCHAR(255) NOT NULL
                        )
                    """);

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS BOARDS (
                            board_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            user_id BIGINT NOT NULL,
                            content VARCHAR(500) NOT NULL,
                            image_url VARCHAR(255),
                            CONSTRAINT fk_posts_user
                                FOREIGN KEY (user_id)
                                REFERENCES USERS(id)
                                ON DELETE CASCADE
                        )
                    """);

            stmt.execute("""
                INSERT INTO USERS (user_id, password, name, email)
                VALUES ('test', '1234', 'test', 'test@example.com')
            """);

            stmt.execute("""
                INSERT INTO BOARDS (user_id, content)
                VALUES (1,  '테스트 게시물 입니다. 테스트 게시물 입니다. 테스트 게시물 입니다. 테스트 게시물 입니다. 테스트 게시물 입니다.')
            """);

            logger.info("H2 schema initialized");

        } catch (SQLException e) {
            throw new RuntimeException("DB 초기화 실패", e);
        }
    }

    public Connection getConnection() throws SQLException {
        Connection c = txConn.get();
        if (c != null) return c;
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


}
