package com.demo.kafkaConsumerProducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.demo.kafkaConsumerProducer.model")
@EnableJpaRepositories("com.demo.kafkaConsumerProducer.repository")
public class KafkaConsumerProducerApplication {
	public static void main(String[] args) {
		SpringApplication.run(KafkaConsumerProducerApplication.class, args);
	}
}