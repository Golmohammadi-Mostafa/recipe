package com.abn.exception;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandlerController {

    @Bean
    public ErrorAttributes errorAttributes() {
        // Hide exception field in the return object
        return new DefaultErrorAttributes() {

        };
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ex.getHttpStatus().value())
                .errorMessage(ex.getMessage())
                .build();
        return ResponseEntity.status(ex.getHttpStatus().value()).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException() {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(HttpStatus.FORBIDDEN.value())
                .errorMessage("Access denied")
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public void handleException(HttpServletResponse res) throws IOException {
        res.sendError(HttpStatus.BAD_REQUEST.value(), "Something went wrong");
    }

}
