package com.food.ordering.system.order.application.exception.handler;

import com.food.ordering.system.order.application.handler.ErrorDto;
import com.food.ordering.system.order.application.handler.GlobalExceptionHandler;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.exception.OrderNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
public class OrderGlobalExceptionHandler extends GlobalExceptionHandler {

    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(OrderDomainException.class)
    public ErrorDto handleException(OrderDomainException orderDomainException) {

        log.error(orderDomainException.getMessage(), orderDomainException);
        return ErrorDto.builder()
                .code(BAD_REQUEST.getReasonPhrase())
                .message(orderDomainException.getMessage())
                .build();
    }

    @ResponseBody
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(OrderNotFoundException.class)
    public ErrorDto handleException(OrderNotFoundException orderDomainException) {

        log.error(orderDomainException.getMessage(), orderDomainException);
        return ErrorDto.builder()
                .code(NOT_FOUND.getReasonPhrase())
                .message(orderDomainException.getMessage())
                .build();
    }
}
