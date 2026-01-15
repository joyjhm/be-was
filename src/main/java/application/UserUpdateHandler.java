package application;

import db.UserDatabase;
import java.util.UUID;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.BadRequestException;
import webserver.exception.InternalServerException;
import webserver.handler.APIHandler;
import webserver.http.HttpSession;
import webserver.http.MultiPartFormData;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseBuilder;
import webserver.http.response.HttpStatus;
import webserver.util.FileUtils;

public class UserUpdateHandler implements APIHandler {

    private static final String IMAGE_STORAGE_PATH = "./src/main/resources/static/img/";
    private static final String IMAGE_URL_PREFIX = "img/";

    private static final Logger logger = LoggerFactory.getLogger(UserUpdateHandler.class);
    private final UserDatabase userDatabase;


    public UserUpdateHandler(UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        HttpSession session = request.getSession();

        Long id = (Long) session.getAttribute("id");
        User user = userDatabase.findUserById(id).orElseThrow(() -> new InternalServerException("User not found"));

        String name = ((MultiPartFormData) request.getAttribute("name")).getBodyAsString();
        String newPassword = ((MultiPartFormData) request.getAttribute("newPassword")).getBodyAsString();
        String confirmPassword = ((MultiPartFormData) request.getAttribute("confirmPassword")).getBodyAsString();
        MultiPartFormData file = (MultiPartFormData) request.getAttribute("file");

        if (name.length() < 4) {
            throw new BadRequestException("닉네임 길이가 4자리 이상이어야 합니다.");
        }

        String password = validateAndGetPassword(newPassword.trim(), confirmPassword.trim(), user.password());
        String imageUrl = resolveImageUrl(file, user.imagePath());

        User updateUser = new User(id, user.userId(), password, name, user.email(), imageUrl);

        userDatabase.update(updateUser);

        session.setAttribute("name", name);

        return new HttpResponseBuilder().statusLine(HttpStatus.OK)
                .build();
    }

    private String validateAndGetPassword(String newPassword, String confirmPassword, String oldPassword) {
        boolean isDefaultPassword =
                newPassword.isBlank() && confirmPassword.isBlank();

        if (!isDefaultPassword) {
            if (!newPassword.equals(confirmPassword)) {
                throw new BadRequestException("비밀번호가 일치하지 않습니다.");
            }
            if (newPassword.trim().length() < 4) {
                throw new BadRequestException("비밀번호 길이가 4자리 이상이어야 합니다.");
            }
            return newPassword;
        }
        return oldPassword;
    }

    private String resolveImageUrl(MultiPartFormData file, String previousImage) {
        String imageUrl = previousImage;

        if (!file.fileName().trim().isBlank()) {
            String storedFileName = UUID.randomUUID() + "_" + file.fileName();
            FileUtils.writeFile(IMAGE_STORAGE_PATH + storedFileName, file.body());
            imageUrl = IMAGE_URL_PREFIX + storedFileName;
        }

        return imageUrl;
    }
}
