package db;

import java.util.Optional;
import model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserMemoryDatabase implements UserDatabase {
    private static Map<Long, User> users = new HashMap<>();

    @Override
    public void addUser(User user) {
        users.put(user.id(), user);
    }

    @Override
    public Optional<User> findUserByUserId(String userId) {
        return users.values().stream()
                .filter(user -> user.userId().equals(userId))
                .findFirst();
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return Optional.of(users.get(id));
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public void clear() {
        users.clear();
    }
}
