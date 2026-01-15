package application.view;

import db.BoardDatabase;
import db.UserDatabase;
import model.Board;
import model.Page;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Model;
import webserver.exception.BadRequestException;
import webserver.handler.ViewHandler;
import webserver.http.HttpSession;
import webserver.http.request.HttpRequest;

public class BoardPageHandler implements ViewHandler {

    private static final Logger logger = LoggerFactory.getLogger(BoardPageHandler.class);
    private static final String MAIN_PAGE_PATH = "/index.html";
    private static final String LOGGED_IN_MAIN_PAGE_PATH = "/main/index.html";

    private final UserDatabase userDatabase;
    private final BoardDatabase boardDatabase;

    public BoardPageHandler(UserDatabase userDatabase, BoardDatabase boardDatabase) {
        this.userDatabase = userDatabase;
        this.boardDatabase = boardDatabase;
    }

    @Override
    public String handle(HttpRequest request, Model model) {
        String pageStr = request.getParam("page");
        int page = pageStr == null ? 1 : Integer.parseInt(pageStr);

        Page<Board> boardPage = boardDatabase.findByPage(page, 1);
        Board board = boardPage.data().get(0);

        User user = userDatabase.findUserById(board.userId())
                .orElseThrow(() -> new BadRequestException("User not found"));

        model.setAttribute("previousPage", page == 1 ? "1" : String.valueOf(page - 1));
        model.setAttribute("nextPage", boardPage.hasNext() ? String.valueOf(page + 1) : String.valueOf(page));
        model.setAttribute("content", board.content());
        model.setAttribute("writer", user.name());
        model.setAttribute("boardImage", board.imagePath());
        model.setAttribute("profile", user.imagePath());

        HttpSession session = request.getSession();

        if (session == null) {
            return MAIN_PAGE_PATH;
        }
        String name = (String) session.getAttribute("name");

        if (name == null) {
            return MAIN_PAGE_PATH;
        }

        model.setAttribute("name", name);

        return LOGGED_IN_MAIN_PAGE_PATH;
    }
}
