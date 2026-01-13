package db;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import model.User;

public interface UserDatabase {

    void addUser(User user);

    Optional<User> findUserByUserId(String userId);

    Optional<User> findUserById(Long id);

    Collection<User> findAll();

    void clear();
}
