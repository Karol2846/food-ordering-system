package com.food.ordering.system.domain.valueobject;

import java.util.UUID;

public record CustomerId(UUID value) implements BaseId<UUID> {}
