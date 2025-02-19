package com.demo.kafkaConsumerProducer.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class EventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;

    public EventProducer(KafkaTemplate<String, String> kafkaTemplate,
                         @Value("${app.kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public CompletableFuture<RecordMetadata> sendEvent(String key, String value) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
        return kafkaTemplate.send(record)
                .thenApply(SendResult::getRecordMetadata);
    }

    public void close() {
        if (kafkaTemplate != null) {
            kafkaTemplate.destroy();
        }
    }
}