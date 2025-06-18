package com.food.ordering.system.order.data.order.mapper;

import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.order.data.order.entity.OrderAddressEntity;
import com.food.ordering.system.order.data.order.entity.OrderEntity;
import com.food.ordering.system.order.data.order.entity.OrderItemEntity;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.food.ordering.system.order.service.domain.entity.Order.FAILURE_MESSAGE_DELIMITER;

@Component
public class OrderDataMapper {

    public OrderEntity mapToOrderEntity(Order order) {
        OrderEntity orderEntity = OrderEntity.builder()
                .id(order.id().value())
                .customerId(order.getCustomerId().value())
                .restaurantId(order.getRestaurantId().value())
                .trackingId(order.getTrackingId().value())
                .address(mapToOrderAddress(order.getDeliveryAddress()))
                .price(order.getPrice().amount())
                .items(mapToOrderItemEntity(order.getOrderItems()))
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages() != null ?
                        String.join(FAILURE_MESSAGE_DELIMITER, order.getFailureMessages()) : "")
                .build();

        orderEntity.getAddress().setOrder(orderEntity);
        orderEntity.getItems().forEach(item -> item.setOrder(orderEntity));
        return orderEntity;
    }

    public Order mapToOrder(OrderEntity orderEntity) {
        return Order.builder()
                .orderId(new OrderId(orderEntity.getId()))
                .customerId(new CustomerId(orderEntity.getCustomerId()))
                .restaurantId(new RestaurantId(orderEntity.getRestaurantId()))
                .trackingId(new TrackingId(orderEntity.getTrackingId()))
                .deliveryAddress(mapToStreetAddress(orderEntity.getAddress()))
                .price(new Money(orderEntity.getPrice()))
                .orderItems(mapToOrderItems(orderEntity.getItems()))
                .orderStatus(orderEntity.getOrderStatus())
                .failureMessages(orderEntity.getFailureMessages().isEmpty() ? new ArrayList<>() :
                        List.of(orderEntity.getFailureMessages().split(FAILURE_MESSAGE_DELIMITER)))
                .build();
    }

    private OrderAddressEntity mapToOrderAddress(StreetAddress address) {
        return OrderAddressEntity.builder()
                .id(address.id())
                .street(address.street())
                .postalCode(address.postalCode())
                .city(address.city())
                .build();
    }

    private List<OrderItemEntity> mapToOrderItemEntity(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> OrderItemEntity.builder()
                        .id(item.id().value())
                        .productId(item.getProduct().id().value())
                        .price(item.getPrice().amount())
                        .quantity(item.getQuantity())
                        .subTotal(item.getSubTotal().amount())
                        .build())
                .toList();
    }

    private StreetAddress mapToStreetAddress(OrderAddressEntity address) {
        return StreetAddress.builder()
                .id(address.getId())
                .street(address.getStreet())
                .postalCode(address.getPostalCode())
                .city(address.getCity())
                .build();
    }

    private List<OrderItem> mapToOrderItems(List<OrderItemEntity> items) {
        return items.stream()
                .map(item -> OrderItem.builder()
                        .orderItemId(new OrderItemId(item.getId()))
                        .product(new Product(new ProductId(item.getProductId())))
                        .price(new Money(item.getPrice()))
                        .quantity(item.getQuantity())
                        .subTotal(new Money(item.getSubTotal()))
                        .build())
                .toList();
    }
}
