package com.kafkaTest.producer.application.port.in;

public interface PublishEventUseCase {
    void publish(Object payload, String key);
}
