package model;

public record User(
        Long id,
        String userId,
        String password,
        String name,
        String email
) {
    public User(String userId, String password, String name, String email) {
        this(null, userId, password, name, email);
    }
}

