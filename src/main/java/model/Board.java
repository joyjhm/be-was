package model;

public record Board(
        Long boardId,
        Long userId,
        String content
) {

    public Board(Long userId, String content) {
        this(null, userId, content);
    }

}
