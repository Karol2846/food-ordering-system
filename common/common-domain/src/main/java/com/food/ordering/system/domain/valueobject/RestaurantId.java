package com.food.ordering.system.domain.valueobject;

import java.util.UUID;

public record RestaurantId(UUID value) implements BaseId<UUID> {}
