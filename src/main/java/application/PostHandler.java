package application;

import db.BoardDatabase;
import java.util.UUID;
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
    private static final String IMAGE_URL_PREFIX = "img/";

    public PostHandler(BoardDatabase boardDatabase) {
        this.boardDatabase = boardDatabase;
    }


    @Override
    public HttpResponse handle(HttpRequest request) {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("id");

        MultiPartFormData content = (MultiPartFormData) request.getAttribute("content");
        MultiPartFormData file = (MultiPartFormData) request.getAttribute("file");

        validateFormData(content, file);

        String imageUrl = null;

        if (!file.fileName().trim().isBlank()) {
            String storedFileName = UUID.randomUUID() + "_" + file.fileName();
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

    private void validateFormData(MultiPartFormData content, MultiPartFormData file) {
        boolean hasContent = !content.getBodyAsString().trim().isBlank();
        boolean hasFile = !file.fileName().trim().isBlank();

        if (!hasContent && !hasFile) {
            throw new BadRequestException("게시글 내용 또는 이미지는 반드시 하나 이상 있어야 합니다.");
        }
    }
}
