package com.demo.kafkaConsumerProducer.repository;

import com.demo.kafkaConsumerProducer.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByStatus(Event.EventStatus status);
    List<Event> findByTopicAndStatus(String topic, Event.EventStatus status);
    List<Event> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    List<Event> findByKeyAndStatus(String key, Event.EventStatus status);
    long countByStatus(Event.EventStatus status);
}