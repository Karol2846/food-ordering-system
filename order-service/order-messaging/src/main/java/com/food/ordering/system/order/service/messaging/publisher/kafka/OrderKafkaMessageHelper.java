package com.food.ordering.system.order.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderKafkaMessageHelper<T extends SpecificRecordBase> {

    private final KafkaProducer<String, T> kafkaProducer;

    public void sendKafkaMessage(String orderId, String topicName, T request, String requestModelName) {

        kafkaProducer.send(topicName, orderId, request)
                .thenApply(result -> {
                    logSuccessfulSend(orderId, result);
                    return result;
                })
                .exceptionally(ex -> {
                    logFailedSend(topicName, orderId, ex, requestModelName);
                    return null;
                });
    }

    public void logSuccessfulSend(String orderId, SendResult<String, T> result) {
        var metadata = result.getRecordMetadata();
        log.info("Received successful response from Kafka for order id: {}, topic: {}, " +
                        "partition: {}, offset: {}, timestamp: {}",
                orderId,
                metadata.topic(),
                metadata.partition(),
                metadata.offset(),
                metadata.timestamp());
    }

    public void logFailedSend(String topicName, String orderId, Throwable ex, String requestModelName) {
        log.error("Failed to send {} for order id: {}, to topic: {}, error: {}",
                requestModelName, orderId, topicName, ex.getMessage());
    }
}
