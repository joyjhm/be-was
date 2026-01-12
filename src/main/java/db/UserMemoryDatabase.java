package db;

import java.util.Optional;
import model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserMemoryDatabase implements UserDatabase {
    private static Map<String, User> users = new HashMap<>();

    public void addUser(User user) {
        users.put(user.userId(), user);
    }

    public Optional<User> findUserById(String userId) {
        return Optional.of(users.get(userId));
    }

    public Collection<User> findAll() {
        return users.values();
    }

    public void clear() {
        users.clear();
    }
}
