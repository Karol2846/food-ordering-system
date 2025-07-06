package com.food.ordering.system.order.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.order.service.domain.ports.in.message.listener.payment.PaymentResponseMessageListener;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.food.ordering.system.kafka.order.avro.model.PaymentStatus.CANCELLED;
import static com.food.ordering.system.kafka.order.avro.model.PaymentStatus.COMPLETED;
import static com.food.ordering.system.kafka.order.avro.model.PaymentStatus.FAILED;
import static org.springframework.kafka.support.KafkaHeaders.OFFSET;
import static org.springframework.kafka.support.KafkaHeaders.PARTITION;
import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_KEY;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponseAvroModel> {

    private final PaymentResponseMessageListener paymentResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}", topics = "${order-service.payment-response.topic-name}")
    public void receive(@Payload List<PaymentResponseAvroModel> messages,
                        @Header(RECEIVED_KEY) List<String> keys,
                        @Header(PARTITION) List<Integer> partitions,
                        @Header(OFFSET) List<Long> offsets) {
        log.info("{} number of payment responses received with keys: {}, partitions: {} and ofsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(paymentResponseMessage -> {
            if(paymentResponseMessage.getPaymentStatus() == COMPLETED) {
                log.info("Processing successful payment message for order id: {}", paymentResponseMessage.getOrderId());
                paymentResponseMessageListener.paymentCompleted(orderMessagingDataMapper.toPaymentResponse(paymentResponseMessage));
            } else if (paymentResponseMessage.getPaymentStatus() == CANCELLED || paymentResponseMessage.getPaymentStatus() == FAILED) {
                log.info("Processing unsuccessful payment message for order id: {}", paymentResponseMessage.getOrderId());
                paymentResponseMessageListener.paymentCancelled(orderMessagingDataMapper.toPaymentResponse(paymentResponseMessage));
            }
        });

    }
}
