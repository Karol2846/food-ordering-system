package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.out.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreateCommandHandler {

    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;
    private final OrderCreatedPaymentRequestMessagePublisher  orderCreatedPaymentRequestMessagePublisher;

    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        OrderCreatedEvent event = orderCreateHelper.persistOrder(createOrderCommand);

        orderCreatedPaymentRequestMessagePublisher.publish(event);
         return orderDataMapper.toResponse(event.getOrder(), "Order Created Successfully");
    }
}
