package com.food.ordering.system.order.service.domain.dto.create;

import com.food.ordering.system.domain.valueobject.OrderStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateOrderResponse(
        UUID orderTrackingId,
        OrderStatus status,
        String message
) { }
