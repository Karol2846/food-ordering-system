package com.food.ordering.system.domain.valueobject;

import java.util.UUID;

public record OrderId(UUID value) implements BaseId<UUID> {}
