package db;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import model.Board;
import model.Page;


public interface BoardDatabase {

    void addBoard(Board board);

    Optional<Board> findBoardByBoardId(Long boardId);

    Collection<Board> findBoardsByUserId(Long userId);

    List<Board> findAll();

    Page<Board> findByPage(int page, int pageSize);

    void clear();
}
