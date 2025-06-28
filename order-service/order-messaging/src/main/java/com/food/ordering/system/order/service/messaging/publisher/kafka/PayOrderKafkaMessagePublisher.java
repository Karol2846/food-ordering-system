package com.food.ordering.system.order.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.order.service.domain.config.OrderServiceConfigData;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.ports.out.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PayOrderKafkaMessagePublisher implements OrderPaidRestaurantRequestMessagePublisher {

    private final OrderMessagingDataMapper mapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private OrderKafkaMessageHelper<RestaurantApprovalRequestAvroModel> orderKafkaMessageHelper;

    @Override
    public void publish(OrderPaidEvent event) {

        String orderId = event.getOrder().id().value().toString();
        log.info("Received OrderPaidEvent for orderId: {}", orderId);
        String topicName = orderServiceConfigData.getRestaurantApprovalRequestTopicName();

        orderKafkaMessageHelper.sendKafkaMessage(
                orderId,
                topicName,
                mapper.toRestaurantApprovalRequest(event),
                "RestaurantApprovalRequestAvroModel");
    }
}
