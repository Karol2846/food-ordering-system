package com.food.ordering.system.order.service.domain.valueobject;

import com.food.ordering.system.domain.valueobject.BaseId;

public record OrderItemId(Long value) implements BaseId<Long> {}
