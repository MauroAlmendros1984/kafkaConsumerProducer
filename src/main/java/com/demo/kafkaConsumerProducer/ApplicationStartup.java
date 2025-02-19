package com.demo.kafkaConsumerProducer;

import com.demo.kafkaConsumerProducer.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationStartup {
    private final EventService eventService;

    @Autowired
    public ApplicationStartup(EventService eventService) {
        this.eventService = eventService;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent() {
        log.info("Starting application...");

        // Enviar algunos eventos de prueba
        eventService.sendEvent("key1", "Evento 1");
        eventService.sendEvent("key2", "Evento 2");
        eventService.sendEvent("key3", "Evento 3");
    }
}