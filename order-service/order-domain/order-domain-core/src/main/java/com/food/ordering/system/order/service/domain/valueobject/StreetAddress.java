package com.food.ordering.system.order.service.domain.valueobject;

import lombok.Builder;

import java.util.Objects;
import java.util.UUID;

@Builder
public record StreetAddress(UUID id,
                            String street,
                            String postalCode,
                            String city) {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StreetAddress that = (StreetAddress) o;
        return Objects.equals(city, that.city) && Objects.equals(street, that.street) && Objects.equals(postalCode, that.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, postalCode, city);
    }
}
