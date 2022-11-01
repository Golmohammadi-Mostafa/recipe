package com.abn.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Mostafa
 * @version 1.0
 * @since 2022-11-01
 */
public class CustomException extends ResponseStatusException {

    private static final long serialVersionUID = 1L;

    private final String message;
    private final HttpStatus httpStatus;

    public CustomException(String message, HttpStatus httpStatus) {
        super(httpStatus, message);
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
