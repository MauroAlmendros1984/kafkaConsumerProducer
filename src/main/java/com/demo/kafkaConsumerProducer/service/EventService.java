package com.demo.kafkaConsumerProducer.service;

import com.demo.kafkaConsumerProducer.model.Event;
import com.demo.kafkaConsumerProducer.producer.EventProducer;
import com.demo.kafkaConsumerProducer.consumer.EventConsumer;
import com.demo.kafkaConsumerProducer.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final EventProducer producer;
    private final EventConsumer consumer;
    private final String topic;
    private final AtomicBoolean isRunning;

    @Autowired
    public EventService(
            EventRepository eventRepository,
            EventProducer producer,
            EventConsumer consumer,
            @Value("${app.kafka.topic}") String topic) {
        this.eventRepository = eventRepository;
        this.producer = producer;
        this.consumer = consumer;
        this.topic = topic;
        this.isRunning = new AtomicBoolean(false);
    }

    @Transactional
    public Event sendEvent(String key, String value) {
        try {
            final Event newEvent = Event.builder()
                    .key(key)
                    .value(value)
                    .topic(topic)
                    .status(Event.EventStatus.PENDING)
                    .timestamp(LocalDateTime.now())
                    .build();

            final Event savedEvent = eventRepository.save(newEvent);

            producer.sendEvent(key, value)
                    .thenAccept(metadata -> updateEventSuccess(savedEvent, metadata))
                    .exceptionally(ex -> {
                        updateEventFailure(savedEvent);
                        return null;
                    });

            return savedEvent;
        } catch (Exception e) {
            log.error("Error sending event", e);
            throw new RuntimeException("Failed to send event", e);
        }
    }

    @Transactional
    protected void updateEventSuccess(Event event, RecordMetadata metadata) {
        try {
            event.setKafkaPartition(metadata.partition());
            event.setKafkaOffset(metadata.offset());
            event.setStatus(Event.EventStatus.PROCESSED);
            eventRepository.save(event);
            log.info("Event sent successfully: {}", event);
        } catch (Exception e) {
            log.error("Error updating event status", e);
        }
    }

    @Transactional
    protected void updateEventFailure(Event event) {
        try {
            event.setStatus(Event.EventStatus.FAILED);
            eventRepository.save(event);
            log.error("Failed to send event: {}", event);
        } catch (Exception e) {
            log.error("Error updating event failure status", e);
        }
    }

    @Transactional(readOnly = true)
    public List<Event> findPendingEvents() {
        return eventRepository.findByStatus(Event.EventStatus.PENDING);
    }

    @Transactional(readOnly = true)
    public List<Event> findFailedEvents() {
        return eventRepository.findByStatus(Event.EventStatus.FAILED);
    }

    @Transactional(readOnly = true)
    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    @Transactional
    public void processReceivedEvent(ConsumerRecord<String, String> record) {
        try {
            Event event = Event.builder()
                    .key(record.key())
                    .value(record.value())
                    .topic(record.topic())
                    .kafkaPartition(record.partition())
                    .kafkaOffset(record.offset())
                    .status(Event.EventStatus.PROCESSED)
                    .timestamp(LocalDateTime.now())
                    .build();

            eventRepository.save(event);
            log.info("Processed event: {}", event);
        } catch (Exception e) {
            log.error("Error processing received event", e);
        }
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutting down Event Service...");
        isRunning.set(false);
        try {
            producer.close();
            consumer.close();
        } catch (Exception e) {
            log.error("Error during shutdown", e);
        }
    }
}