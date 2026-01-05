package webserver.exception;

import webserver.http.response.HttpStatus;

public abstract class HttpException extends RuntimeException {

    private final HttpStatus HttpStatus;

    public HttpException(String message, HttpStatus httpStatus) {
        super(message);
        this.HttpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus;
    }
}

