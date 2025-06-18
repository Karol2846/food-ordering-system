package com.food.ordering.system.order.application.handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorDto handleException(Exception exception) {

        log.error(exception.getMessage(), exception);
        return ErrorDto.builder()
                .code(INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("Unexpected error")
                .build();
    }

    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ErrorDto handleException(ValidationException exception) {

        ErrorDto errorDto;
        if(exception instanceof ConstraintViolationException) {
            String violations = extractViolation((ConstraintViolationException)exception);
            log.error(violations, exception);
            errorDto = ErrorDto.builder()
                    .message(violations)
                    .code(BAD_REQUEST.getReasonPhrase())
                    .build();
        } else {
            String exceptionMessage = exception.getMessage();
            log.error(exceptionMessage, exception);
            errorDto = ErrorDto.builder()
                    .message(exceptionMessage)
                    .code(BAD_REQUEST.getReasonPhrase())
                    .build();
        }
        return errorDto;
    }

    private String extractViolation(ConstraintViolationException exception) {
        return exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("--"));
    }
}
