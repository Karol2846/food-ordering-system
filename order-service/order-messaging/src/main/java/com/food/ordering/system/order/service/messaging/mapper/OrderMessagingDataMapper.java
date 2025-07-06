package com.food.ordering.system.order.service.messaging.mapper;

import com.food.ordering.system.domain.valueobject.OrderApprovalStatus;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.kafka.order.avro.model.Product;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.kafka.order.avro.model.RestaurantOrderStatus;
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordering.system.order.service.domain.dto.message.RestaurantApproveResponse;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.food.ordering.system.kafka.order.avro.model.PaymentOrderStatus.CANCELLED;
import static com.food.ordering.system.kafka.order.avro.model.PaymentOrderStatus.PENDING;

@Component
public class OrderMessagingDataMapper {

    public PaymentRequestAvroModel toPaymentRequest(OrderEvent event) {
        return switch (event) {
            case OrderCreatedEvent created -> toPaymentRequest(created);
            case OrderCancelledEvent cancelled -> toPaymentRequest(cancelled);
            default -> null;
        };
    }

        public PaymentRequestAvroModel toPaymentRequest(OrderCreatedEvent event) {
        Order order = event.getOrder();

        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(null)
                .setCustomerId(order.getCustomerId().value())
                .setOrderId(order.id().value())
                .setPrice(order.getPrice().amount())
                .setCreatedAt(event.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PENDING)
                .build();
    }

    public PaymentRequestAvroModel toPaymentRequest(OrderCancelledEvent event) {
        Order order = event.getOrder();

        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(null)
                .setCustomerId(order.getCustomerId().value())
                .setOrderId(order.id().value())
                .setPrice(order.getPrice().amount())
                .setCreatedAt(event.getCreatedAt().toInstant())
                .setPaymentOrderStatus(CANCELLED)
                .build();
    }

    public RestaurantApprovalRequestAvroModel toRestaurantApprovalRequest(OrderPaidEvent event) {
        Order order = event.getOrder();

        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setOrderId(order.id().value())
                .setSagaId(null)
                .setRestaurantId(order.getRestaurantId().value())
                .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
                .setProducts(order.getOrderItems().stream().map(item ->
                        Product.newBuilder()
                                .setId(item.getProduct().id().value())
                                .setQuantity(item.getQuantity())
                                .build()).toList())
                .setPrice(order.getPrice().amount())
                .setCreatedAt(event.getCreatedAt().toInstant())
                .build();
    }

    public PaymentResponse toPaymentResponse(PaymentResponseAvroModel paymentResponseAvroModel) {
        return PaymentResponse.builder()
                .id(paymentResponseAvroModel.getId().toString())
                .sagaId(paymentResponseAvroModel.getSagaId().toString())
                .orderId(paymentResponseAvroModel.getOrderId().toString())
                .paymentId(paymentResponseAvroModel.getPaymentId().toString())
                .customerId(paymentResponseAvroModel.getCustomerId().toString())
                .price(paymentResponseAvroModel.getPrice())
                .createdAt(paymentResponseAvroModel.getCreatedAt())
                .paymentStatus(PaymentStatus.valueOf(paymentResponseAvroModel.getPaymentStatus().name()))
                .failureMessages(paymentResponseAvroModel.getFailureMessages())
                .build();

    }

    public RestaurantApproveResponse toRestaurantApproveResponse(RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel) {
        return RestaurantApproveResponse.builder()
                .id(restaurantApprovalResponseAvroModel.getId().toString())
                .sagaId(restaurantApprovalResponseAvroModel.getSagaId().toString())
                .orderId(restaurantApprovalResponseAvroModel.getOrderId().toString())
                .restaurantId(restaurantApprovalResponseAvroModel.getRestaurantId().toString())
                .createdAt(restaurantApprovalResponseAvroModel.getCreatedAt())
                .orderApprovalStatus(OrderApprovalStatus.valueOf(restaurantApprovalResponseAvroModel.getOrderApprovalStatus().name()))
                .failureMessages(restaurantApprovalResponseAvroModel.getFailureMessages())
                .build();
    }
}
