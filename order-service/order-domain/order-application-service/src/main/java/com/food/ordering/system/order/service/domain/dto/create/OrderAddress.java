package com.food.ordering.system.order.service.domain.dto.create;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record OrderAddress(
        @NotNull
        @Size(min = 1, max=7)
        String postalCode,

        @NotNull
        @Size(min = 1, max=50)
        String street,

        @NotNull
        String city
) {
}
