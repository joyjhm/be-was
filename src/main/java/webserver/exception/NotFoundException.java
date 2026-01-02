package webserver.exception;

import webserver.response.HttpStatus;

public class NotFoundException extends HttpException {

    public NotFoundException(String message) {
        super(message, webserver.response.HttpStatus.NOT_FOUND);
    }
}
