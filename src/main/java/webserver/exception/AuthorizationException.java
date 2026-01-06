package webserver.exception;

import webserver.http.response.HttpStatus;

public class AuthorizationException extends HttpException {

    public AuthorizationException(String message) {
        super(message, webserver.http.response.HttpStatus.UNAUTHORIZED);
    }
}
