package com.food.ordering.system.order.service.domain.dto.track;

import lombok.Builder;

import java.util.UUID;

@Builder
public record TrackOrderQuery(
        UUID orderTrackingId
) {
}
