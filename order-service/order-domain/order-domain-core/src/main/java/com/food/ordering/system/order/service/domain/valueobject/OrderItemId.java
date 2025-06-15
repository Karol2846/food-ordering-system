package com.food.ordering.system.order.service.domain.valueobject;

import com.food.ordering.system.domain.valueobject.BaseId;

import java.util.UUID;

public record OrderItemId(UUID value) implements BaseId<UUID> {}
