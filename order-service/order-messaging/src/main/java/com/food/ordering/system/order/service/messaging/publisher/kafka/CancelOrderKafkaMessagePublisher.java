package com.food.ordering.system.order.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.order.service.domain.config.OrderServiceConfigData;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.ports.out.message.publisher.payment.OrderCancelledPaymentRequestMessagePublisher;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CancelOrderKafkaMessagePublisher implements OrderCancelledPaymentRequestMessagePublisher {

    private final OrderMessagingDataMapper mapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final OrderKafkaMessageHelper<PaymentRequestAvroModel> orderKafkaMessageHelper;

    @Override
    public void publish(OrderCancelledEvent event) {

        String orderId = event.getOrder().id().value().toString();
        log.info("Received OrderCancelledEvent for order id: {}", orderId);
        String topicName = orderServiceConfigData.getPaymentRequestTopicName();

        orderKafkaMessageHelper.sendKafkaMessage(
                orderId,
                topicName,
                mapper.toPaymentRequest(event),
                "PaymentRequestAvroModel");
    }
}
