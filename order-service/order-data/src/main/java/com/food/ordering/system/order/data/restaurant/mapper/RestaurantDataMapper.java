package com.food.ordering.system.order.data.restaurant.mapper;

import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.ProductId;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.order.data.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.order.data.restaurant.exception.RestaurantDataException;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class RestaurantDataMapper {

    public List<UUID> toProductIds(Restaurant restaurant) {
        return restaurant.getProducts().stream()
                .map(product -> product.id().value())
                .toList();
    }

    public Restaurant toRestaurant(List<RestaurantEntity> entities) {

        if(entities == null || entities.isEmpty()) {
            return null;
        }

        RestaurantEntity restaurantEntity = entities.stream().findFirst().orElseThrow(
                () -> new RestaurantDataException("Restaurant could not be found")
        );

        List<Product> products = entities.stream()
                .map(res -> Product.builder()
                        .productId(new ProductId(res.getProductId()))
                        .price(new Money(res.getProductPrice()))
                        .build()
                )
                .toList();

        return Restaurant.builder()
                .id(new RestaurantId(restaurantEntity.getRestaurantId()))
                .products(products)
                .active(restaurantEntity.getRestaurantActive())
                .build();
    }
}
