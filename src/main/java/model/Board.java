package model;

public record Board(
        Long boardId,
        Long userId,
        String content,
        String imagePath
) {

    public Board(Long userId, String content, String imagePath) {
        this(null, userId, content,  imagePath);
    }

}
