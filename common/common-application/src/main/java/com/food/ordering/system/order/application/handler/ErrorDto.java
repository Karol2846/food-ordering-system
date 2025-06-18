package com.food.ordering.system.order.application.handler;

import lombok.Builder;

@Builder
public record ErrorDto(
        String code,
        String message
) { }
