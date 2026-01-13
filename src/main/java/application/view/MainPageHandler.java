package application.view;

import db.BoardDatabase;
import db.UserDatabase;
import model.Board;
import model.Page;
import model.User;
import webserver.Model;
import webserver.exception.InternalServerException;
import webserver.handler.ViewHandler;
import webserver.http.HttpSession;
import webserver.http.request.HttpRequest;

public class MainPageHandler implements ViewHandler {

    private static final String MAIN_PAGE_PATH = "/index.html";
    private static final String LOGGED_IN_MAIN_PAGE_PATH = "/main/index.html";

    private final UserDatabase userDatabase;
    private final BoardDatabase boardDatabase;

    public MainPageHandler(UserDatabase userDatabase, BoardDatabase boardDatabase) {
        this.userDatabase = userDatabase;
        this.boardDatabase = boardDatabase;
    }

    @Override
    public String handle(HttpRequest request, Model model) {
        HttpSession session = request.getSession();

        Page<Board> boardPage = boardDatabase.findByPage(1, 1);
        Board board = boardPage.data().get(0);

        User user = userDatabase.findUserById(board.userId()).orElseThrow(() -> new InternalServerException("User not found"));

        model.setAttribute("content", board.content());
        model.setAttribute("writer", user.name());
        model.setAttribute("previousPage", String.valueOf(1));
        model.setAttribute("nextPage", boardPage.hasNext() ? "2" : "1");

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
