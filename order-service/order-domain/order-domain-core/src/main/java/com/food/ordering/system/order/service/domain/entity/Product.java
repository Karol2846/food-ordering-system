package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.ProductId;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;

    @Builder
    public Product(ProductId productId, String name, Money price) {
        super.id(productId);
        this.name = name;
        this.price = price;
    }

    public Product(ProductId productId) {
        super.id(productId);
    }

    public void updateWithConfirmedData(Product product) {
        name =  product.getName();
        price = product.getPrice();
    }
}
