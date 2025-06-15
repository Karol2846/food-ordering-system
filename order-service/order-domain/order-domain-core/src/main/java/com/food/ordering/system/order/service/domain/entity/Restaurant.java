package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
public class Restaurant extends BaseEntity<RestaurantId> {

    private final List<Product> products;
    private boolean active;

    @Builder
    public Restaurant(RestaurantId id, List<Product> products, boolean active) {
        super.id(id);
        this.products = products;
        this.active = active;
    }
}
