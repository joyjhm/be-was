package application;

import db.BoardDatabase;
import model.Board;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handler.APIHandler;
import webserver.http.HttpHeader;
import webserver.http.HttpSession;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseBuilder;
import webserver.http.response.HttpStatus;

public class PostHandler implements APIHandler {

    private static final Logger logger = LoggerFactory.getLogger(PostHandler.class);

    private final BoardDatabase boardDatabase;

    public PostHandler(BoardDatabase boardDatabase) {
        this.boardDatabase = boardDatabase;
    }


    @Override
    public HttpResponse handle(HttpRequest request) {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("id");

        String content = (String) request.getAttribute("content");

        Board board = new Board(userId, content);

        boardDatabase.addBoard(board);

        return new HttpResponseBuilder().statusLine(HttpStatus.FOUND)
                .header(HttpHeader.LOCATION, "/")
                .build();
    }
}
