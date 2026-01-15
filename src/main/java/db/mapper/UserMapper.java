package db.mapper;

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
                rs.getString("nickname"),
                rs.getString("email"),
                rs.getString("image_url")
        );
    }
}
