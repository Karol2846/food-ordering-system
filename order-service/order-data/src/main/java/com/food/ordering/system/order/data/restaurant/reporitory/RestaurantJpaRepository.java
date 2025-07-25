package com.food.ordering.system.order.data.restaurant.reporitory;

import com.food.ordering.system.order.data.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.order.data.restaurant.entity.RestaurantEntityId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, RestaurantEntityId> {

    List<RestaurantEntity> findByRestaurantIdAndProductIdIn(UUID restaurantId, List<UUID> productIds);
}
