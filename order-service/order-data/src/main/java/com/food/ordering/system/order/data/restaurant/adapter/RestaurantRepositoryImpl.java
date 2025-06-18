package com.food.ordering.system.order.data.restaurant.adapter;

import com.food.ordering.system.order.data.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.order.data.restaurant.mapper.RestaurantDataMapper;
import com.food.ordering.system.order.data.restaurant.reporitory.RestaurantJpaRepository;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.ports.out.reporitory.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository repository;
    private final RestaurantDataMapper mapper;

    @Override
    public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {

        List<UUID> productsIds = mapper.toProductIds(restaurant);
        List<RestaurantEntity> entities = repository.findByRestaurantIdAndProductIdIn(
                restaurant.id().value(), productsIds);

        return Optional.ofNullable(mapper.toRestaurant(entities));
    }
}
