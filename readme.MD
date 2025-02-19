Proyecto: Sistema de Eventos con Kafka y Spring Boot

1. Descripción General:

Este es un sistema de mensajería basado en eventos utilizando Apache Kafka y Spring Boot.
Permite la producción, consumo y almacenamiento de eventos de manera distribuida y escalable.

-----------------

2. Arquitectura:

- Patrón de Arquitectura: Event-Driven Architecture (EDA)
- Backend: Spring Boot
- Mensajería: Apache Kafka
- Base de Datos: H2 (en memoria)
- Persistencia: JPA/Hibernate

-----------------

3. Componentes Principales:

a) Modelo de Datos (Event.java):
- Representa los eventos del sistema
- Atributos: id, key, value, timestamp, status, topic, etc.
- Estados posibles: PENDING, PROCESSED, FAILED, RETRYING

b) Productor (EventProducer.java):
- Envía eventos a Kafka
- Maneja la serialización de mensajes
- Gestiona confirmaciones de envío

c) Consumidor (EventConsumer.java):
- Recibe eventos de Kafka
- Procesa los mensajes recibidos
- Maneja la deserialización

d) Servicio (EventService.java):
- Lógica de negocio
- Coordina productores y consumidores
- Maneja transacciones y persistencia

e) Repositorio (EventRepository.java):
- Interfaz para acceso a datos
- Operaciones CRUD
- Consultas personalizadas

-----------------

4. Flujo de Datos:

    1. El cliente envía un evento
    2. EventService recibe la solicitud
    3. Se persiste el evento en estado PENDING
    4. EventProducer envía el evento a Kafka
    5. EventConsumer recibe el evento
    6. Se procesa el evento y se actualiza su estado
    7. Se persiste el resultado en la base de datos

-----------------

5. Características Técnicas:

// Configuración de Kafka
@Configuration
public class KafkaConfig {
// Configuración de productores, consumidores y topics
}

// Manejo de Transacciones
@Transactional
public Event sendEvent(String key, String value) {
// Lógica transaccional
}

// Procesamiento Asíncrono
@KafkaListener(topics = "${app.kafka.topic}")
public void consume(ConsumerRecord<String, String> record) {
// Procesamiento asíncrono de eventos
}

-----------------

6. Funcionalidades Principales:

- Envío de eventos
- Consumo de eventos
- Persistencia en base de datos
- Manejo de estados de eventos
- Recuperación ante fallos
- Monitoreo y logging

-----------------

7. Configuración y Dependencias:

xml
<dependencies>
<!-- Spring Boot -->
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-web</artifactId>
</dependency>

    <!-- Kafka -->
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>

    <!-- Base de Datos -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
    </dependency>
</dependencies>

-----------------

8. Aspectos de Producción:

- Escalabilidad horizontal
- Tolerancia a fallos
- Persistencia de datos
- Monitoreo y métricas
- Manejo de errores
- Logging comprehensivo

-----------------

9. Casos de Uso:

    1. Procesamiento de eventos en tiempo real
    2. Sistema de notificaciones
    3. Integración de sistemas
    4. Procesamiento asíncrono
    5. Logging de actividades

-----------------

10. Ventajas del Diseño:

- Desacoplamiento de componentes
- Alta escalabilidad
- Procesamiento asíncrono
- Persistencia de eventos
- Facilidad de mantenimiento
- Monitoreo efectivo

-----------------

11. Ejemplo de Uso:

// Enviar un evento
eventService.sendEvent("user-created", "{'userId': '123', 'action': 'CREATE'}");

// Consultar eventos
List<Event> pendingEvents = eventService.findPendingEvents();

// Procesar eventos fallidos
eventService.retryFailedEvents();

-----------------

12. Requisitos de Ejecución:

- Java 17 o superior
- Apache Kafka
- Maven
- Base de datos (H2 en este caso)

-----------------

13. Monitoreo y Mantenimiento:

- Consola H2 para base de datos
- Logs de aplicación
- Métricas de Kafka
- Estado de los eventos

Esta explicación proporciona una visión completa del proyecto, desde su arquitectura hasta sus detalles de implementación,
lo que facilita su comprensión y mantenimiento.


--------------------------
Uso:

Limpia y reconstruye el proyecto:

bash
mvn clean install
Ejecuta la aplicación:

bash
mvn spring-boot:run
O ejecuta directamente desde tu IDE.

Una vez que la aplicación esté corriendo, se puede:

Acceder a la consola H2 en: http://localhost:8080/h2-console

Usar las credenciales configuradas (usuario: sa, contraseña: vacía)

Verificar que la tabla events se ha creado correctamente

---------------------------------------
En una terminal correr comando "wsl"

# Descargar Kafka (si no lo tienes)
wget https://downloads.apache.org/kafka/3.9.0/kafka-3.9.0-src.tgz (descargar solo una vez)

Luego
# Iniciar ZooKeeper (en una terminal)
tar -xzf kafka-3.9.0-src.tgz
cd kafka-3.9.0-src
bin/zookeeper-server-start.sh config/zookeeper.properties

# Iniciar Kafka (en otra terminal)
tar -xzf kafka-3.9.0-src.tgz
cd kafka-3.9.0-src
bin/kafka-server-start.sh config/server.properties

# Crear el topic (en otra terminal)
tar -xzf kafka-3.9.0-src.tgz
cd kafka-3.9.0-src
bin/kafka-topics.sh --create --topic mi-topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1