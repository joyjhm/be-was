package db;

import java.util.Collection;
import java.util.Optional;
import model.User;

public interface UserDatabase {

    void addUser(User user);

    Optional<User> findUserById(String userId);

    Collection<User> findAll();

    void clear();
}
