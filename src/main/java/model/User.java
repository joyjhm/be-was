package model;

public record User(
        Long id,
        String userId,
        String password,
        String name,
        String email,
        String imagePath
) {
    public User(String userId, String password, String name, String email, String imagePath) {
        this(null, userId, password, name, email,  imagePath);
    }

    public static User of(String userId, String password, String name, String email) {
        return new User(userId, password, name, email, "../img/img.png");
    }
}

