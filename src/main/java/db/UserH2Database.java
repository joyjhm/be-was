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

    private final ConnectionManager connectionManager;

    public UserH2Database(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void addUser(User user) {
        String sql = """
                    INSERT INTO USERS (userId, password, name, email)
                    VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.userId());
            ps.setString(2, user.password());
            ps.setString(3, user.name());
            ps.setString(4, user.email());
            int rows = ps.executeUpdate();


            logger.info("add count: {}, User {} has been added", rows, user.userId());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<User> findUserById(String userId) {
        String sql = """
                    SELECT * FROM USERS WHERE userId = ?;
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }

                User findUser = new User(
                        rs.getLong("id"),
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                );

                logger.info("find User: {}", findUser);
                return Optional.of(findUser);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<User> findAll() {
        String sql = """
                    SELECT * FROM USERS;
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            List<User> users = new ArrayList<>();

            while (rs.next()) {
                User findUser = new User(
                        rs.getLong("id"),
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                );
                users.add(findUser);
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear() {

    }
}
