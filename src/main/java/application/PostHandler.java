package application;

import db.BoardDatabase;
import model.Board;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.BadRequestException;
import webserver.handler.APIHandler;
import webserver.http.HttpHeader;
import webserver.http.HttpSession;
import webserver.http.MultiPartFormData;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseBuilder;
import webserver.http.response.HttpStatus;
import webserver.util.FileUtils;

public class PostHandler implements APIHandler {

    private static final Logger logger = LoggerFactory.getLogger(PostHandler.class);

    private final BoardDatabase boardDatabase;
    private static final String IMAGE_STORAGE_PATH = "./src/main/resources/static/img/";
    private static final String IMAGE_URL_PREFIX = "../img/";

    public PostHandler(BoardDatabase boardDatabase) {
        this.boardDatabase = boardDatabase;
    }


    @Override
    public HttpResponse handle(HttpRequest request) {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("id");

        MultiPartFormData content = (MultiPartFormData) request.getAttribute("content");
        MultiPartFormData file = (MultiPartFormData) request.getAttribute("file");

        validateContent(content);

        String imageUrl = null;

        if (file != null && file.fileName() != null) {
            String storedFileName = userId + "_" + file.fileName();
            FileUtils.writeFile(IMAGE_STORAGE_PATH + storedFileName, file.body());
            imageUrl = IMAGE_URL_PREFIX + storedFileName;
        }

        String contentStr = content.getBodyAsString();

        Board board = new Board(userId, contentStr, imageUrl);

        boardDatabase.addBoard(board);

        return new HttpResponseBuilder().statusLine(HttpStatus.FOUND)
                .header(HttpHeader.LOCATION, "/")
                .build();
    }

    private void validateContent(MultiPartFormData content) {
        if (content == null || content.body() == null || content.body().length == 0) {
            throw new BadRequestException("required content");
        }
    }
}
