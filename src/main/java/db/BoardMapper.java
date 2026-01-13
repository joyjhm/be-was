package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import model.Board;

public class BoardMapper implements Mapper<Board> {

    @Override
    public Board map(ResultSet rs) throws SQLException {
        return new Board(
                rs.getLong("board_id"),
                rs.getLong("user_id"),
                rs.getString("content")
        );
    }
}
