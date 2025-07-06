package com.food.ordering.system.order.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.order.service.domain.ports.in.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.food.ordering.system.kafka.order.avro.model.OrderApprovalStatus.APPROVED;
import static com.food.ordering.system.kafka.order.avro.model.OrderApprovalStatus.REJECTED;
import static org.springframework.kafka.support.KafkaHeaders.OFFSET;
import static org.springframework.kafka.support.KafkaHeaders.PARTITION;
import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_KEY;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantApprovalResponseKafkaListener implements KafkaConsumer<RestaurantApprovalResponseAvroModel> {

    private final RestaurantApprovalResponseMessageListener restaurantApprovalResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}", topics = "${order-service.restaurant-approval-response-topic-name}")
    public void receive(@Payload List<RestaurantApprovalResponseAvroModel> messages,
                        @Header(RECEIVED_KEY) List<String> keys,
                        @Header(PARTITION) List<Integer> partitions,
                        @Header(OFFSET) List<Long> offsets) {
        log.info("{} number of restaurant approval responses received with keys: {}, partitions: {} and ofsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(paymentResponseMessage -> {
            if(paymentResponseMessage.getOrderApprovalStatus() == APPROVED) {
                log.info("Processing approved order for order id: {}", paymentResponseMessage.getOrderId());
                restaurantApprovalResponseMessageListener.orderApproved(
                        orderMessagingDataMapper.toRestaurantApproveResponse(paymentResponseMessage));
            } else if (paymentResponseMessage.getOrderApprovalStatus() == REJECTED) {
                log.info("Processing rejected order for order id: {}", paymentResponseMessage.getOrderId());
                restaurantApprovalResponseMessageListener.orderRejected(
                        orderMessagingDataMapper.toRestaurantApproveResponse(paymentResponseMessage));
            }
        });

    }
}
