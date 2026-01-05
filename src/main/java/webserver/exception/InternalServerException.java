package webserver.exception;

import webserver.http.response.HttpStatus;

public class InternalServerException extends HttpException {
    public InternalServerException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
