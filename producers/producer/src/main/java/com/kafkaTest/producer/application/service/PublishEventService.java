package com.kafkaTest.producer.application.service;

import com.kafkaTest.producer.application.port.in.PublishEventUseCase;
import com.kafkaTest.producer.application.port.out.EventPublisherPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PublishEventService implements PublishEventUseCase {

    private final EventPublisherPort eventPublisherPort;

    //Lee el topico desde el application.yml -> app.kafka.topic = demo.events
    @Value("${app.kafka.topic}")
    private String topic;

    public PublishEventService(EventPublisherPort eventPublisherPort){
        this.eventPublisherPort = eventPublisherPort;
    }

    @Override
    public void publish(Object payload, String key){
        eventPublisherPort.publish(topic, key, payload);
    }

}
