package com.demo.kafkaConsumerProducer;

import com.demo.kafkaConsumerProducer.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class KafkaConsumerProducerApplicationTests {

	@Test
	void contextLoads() {
		// Test básico que verifica que el contexto de Spring se carga correctamente
	}
}