package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO: 트랜잭션 처리, 공통 try-catch 묶기
public class UserH2Database implements UserDatabase {
    private static final Logger logger = LoggerFactory.getLogger(UserH2Database.class);

    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper = new UserMapper();

    public UserH2Database(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addUser(User user) {
        String sql = """
                    INSERT INTO USERS (user_id, password, name, email)
                    VALUES (?, ?, ?, ?)
                """;

        jdbcTemplate.executeUpdate(sql, user.userId(), user.password(), user.name(), user.email());
    }

    @Override
    public Optional<User> findUserByUserId(String userId) {
        String sql = """
                    SELECT * FROM USERS WHERE user_id = ?;
                """;

        return jdbcTemplate.executeQueryOne(sql, userMapper, userId);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        String sql = """
                    SELECT * FROM USERS WHERE id = ?;
                """;

        return jdbcTemplate.executeQueryOne(sql, userMapper, id);
    }

    @Override
    public Collection<User> findAll() {
        String sql = """
                    SELECT * FROM USERS;
                """;

        return jdbcTemplate.executeQueryList(sql, new UserMapper());
    }

    @Override
    public void clear() {

    }
}
