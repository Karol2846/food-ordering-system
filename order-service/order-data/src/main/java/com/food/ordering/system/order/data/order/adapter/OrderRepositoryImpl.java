package com.food.ordering.system.order.data.order.adapter;

import com.food.ordering.system.order.data.order.entity.OrderEntity;
import com.food.ordering.system.order.data.order.mapper.OrderDataMapper;
import com.food.ordering.system.order.data.order.reporitory.OrderJpaRepository;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.ports.out.reporitory.OrderRepository;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderDataMapper mapper;
    private final OrderJpaRepository repository;

    @Override
    public Order save(Order order) {
        return mapper.mapToOrder(repository.save(
                mapper.mapToOrderEntity(order)));
    }

    @Override
    public Optional<Order> findByTrackingId(TrackingId trackingId) {
        return repository.findByTrackingId(trackingId.value())
                .map(mapper::mapToOrder);
    }
}
