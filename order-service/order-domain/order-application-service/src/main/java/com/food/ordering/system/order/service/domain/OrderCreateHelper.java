package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.out.reporitory.CustomerRepository;
import com.food.ordering.system.order.service.domain.ports.out.reporitory.OrderRepository;
import com.food.ordering.system.order.service.domain.ports.out.reporitory.RestaurantRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreateHelper {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;

    @Transactional
    public OrderCreatedEvent persistOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.customerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.toOrder(createOrderCommand);

        OrderCreatedEvent event = orderDomainService.validateAndInitiateOrder(order, restaurant);
        Order savedOrder = saveOrder(order);
        log.info("Order created with id: {}", savedOrder.id().value());
        return event;
    }

    private Order saveOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        if(savedOrder == null) {
            log.error("Could not save order");
            throw new OrderDomainException("Could not save order");
        }

        log.info("Saved order with id: {}", savedOrder.id().value());
        return savedOrder;
    }

    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
        Restaurant restaurant = orderDataMapper.toRestaurant(createOrderCommand);

        return restaurantRepository.findRestaurantInformation(restaurant).orElseThrow(() -> {
            log.warn("Restaurant not found with id {}", createOrderCommand.restaurantId());
            return new OrderDomainException("Restaurant not found with id " + createOrderCommand.restaurantId());
        });
    }

    private void checkCustomer(@NotNull UUID customerId) {
        customerRepository.findCustomer(customerId).orElseThrow(() -> {
            log.error("Customer not found with id {}", customerId);
            return new OrderDomainException("Customer not found with id " + customerId);
        });
    }


}
