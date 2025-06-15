package com.food.ordering.system.order.service.domain.dto.message;

import com.food.ordering.system.domain.valueobject.OrderApprovalStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record RestaurantApproveResponse(
        String id,
        String sageId,
        String orderId,
        String restaurantId,
        Instant createdAt,
        OrderApprovalStatus orderApprovalStatus,
        List<String> failureMessages
) {}
