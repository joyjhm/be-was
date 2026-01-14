package db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import model.Board;
import model.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoardH2Database implements BoardDatabase {

    private static final Logger logger = LoggerFactory.getLogger(BoardH2Database.class);
    private final JdbcTemplate jdbcTemplate;
    private final BoardMapper boardMapper =  new BoardMapper();

    public BoardH2Database(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addBoard(Board board) {
        String sql = """
                    INSERT INTO BOARDS (user_id, content, image_url)
                    VALUES (?, ?, ?)
                """;

        jdbcTemplate.executeUpdate(sql, board.userId(), board.content(), board.imagePath());
    }

    @Override
    public Optional<Board> findBoardByBoardId(Long boardId) {
        String sql = """
                    SELECT * FROM BOARDS WHERE boardId = ?;
                """;

        return jdbcTemplate.executeQueryOne(sql, boardMapper, boardId);
    }

    @Override
    public Collection<Board> findBoardsByUserId(Long userId) {
        List<Board> boards = new ArrayList<>();

        String sql = """
                SELECT * FROM BOARDS WHERE userId = ?;
                """;
        return jdbcTemplate.executeQueryList(sql, boardMapper, userId);
    }

    public Page<Board> findByPage(int page, int pageSize) {
        int offset = pageSize * (page - 1);
        String sql = """
                    SELECT * FROM BOARDS
                    ORDER BY board_id DESC
                    LIMIT ? OFFSET ?;
        """;

        List<Board> boards = jdbcTemplate.executeQueryList(sql, boardMapper, pageSize + 1, offset);

        if(boards.size() > pageSize) {
            return new Page<Board>(page, pageSize, true, boards);
        }

        return new Page<Board>(page, pageSize, false, boards);
    }

    @Override
    public List<Board> findAll() {
        return null;
    }

    @Override
    public void clear() {

    }
}
