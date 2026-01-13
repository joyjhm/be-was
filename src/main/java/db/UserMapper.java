package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import model.User;

public class UserMapper implements Mapper<User> {

    @Override
    public User map(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("user_id"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email")
        );
    }
}
