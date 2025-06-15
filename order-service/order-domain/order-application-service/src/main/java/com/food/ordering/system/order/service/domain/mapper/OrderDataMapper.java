package com.food.ordering.system.order.service.domain.mapper;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.ProductId;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.create.OrderAddress;
import com.food.ordering.system.order.service.domain.dto.create.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class OrderDataMapper {

    public Order toOrder(CreateOrderCommand createOrderCommand) {
        return Order.builder()
                .customerId(new CustomerId(createOrderCommand.customerId()))
                .restaurantId(new RestaurantId(createOrderCommand.restaurantId()))
                .deliveryAddress(mapAddress(createOrderCommand.address()))
                .price(new Money(createOrderCommand.price()))
                .orderItems(mapOrderItems(createOrderCommand.orderItems()))
                .build();

    }

    private List<com.food.ordering.system.order.service.domain.entity.OrderItem> mapOrderItems(
            List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::mapOrderItem)
                .toList();
    }

    private com.food.ordering.system.order.service.domain.entity.OrderItem mapOrderItem(OrderItem item) {
        return com.food.ordering.system.order.service.domain.entity.OrderItem.builder()
                .price(new Money(item.price()))
                .quantity(item.quantity())
                .subTotal(new Money(item.subTotal()))
                .product(new Product(new ProductId(item.productId())))
                .build();
    }

    private StreetAddress mapAddress(OrderAddress address) {
        return StreetAddress.builder()
                .id(UUID.randomUUID())
                .postalCode(address.postalCode())
                .city(address.city())
                .street(address.street())
                .build();
    }

    public Restaurant toRestaurant(CreateOrderCommand createOrderCommand) {
        return Restaurant.builder()
                .id(new RestaurantId(createOrderCommand.restaurantId()))
                .products(getProducts(createOrderCommand.orderItems()))
                .build();
    }

    private List<Product> getProducts(@NotNull List<OrderItem> orderItems) {
        return orderItems.stream().map(item ->
                new Product(new ProductId(item.productId()))).toList();
    }

    public CreateOrderResponse toResponse(Order order) {
        return CreateOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().value())
                .status(order.getOrderStatus())
                .build();
    }
}
