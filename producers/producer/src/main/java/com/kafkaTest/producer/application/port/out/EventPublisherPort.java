package com.kafkaTest.producer.application.port.out;

public interface EventPublisherPort {
    void publish(String topic, String key, Object payload);
}
