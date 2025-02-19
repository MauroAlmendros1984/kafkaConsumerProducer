package com.demo.kafkaConsumerProducer.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@Slf4j
public class EventConsumer {

    private volatile boolean running = true;

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ConsumerRecord<String, String> record) {
        if (running) {
            try {
                log.info("Received message: key={}, value={}, topic={}, partition={}, offset={}",
                        record.key(), record.value(), record.topic(), record.partition(), record.offset());
                // Aquí puedes procesar el mensaje como necesites
            } catch (Exception e) {
                log.error("Error processing message: {}", record, e);
            }
        }
    }

    public void close() {
        running = false;
    }
}