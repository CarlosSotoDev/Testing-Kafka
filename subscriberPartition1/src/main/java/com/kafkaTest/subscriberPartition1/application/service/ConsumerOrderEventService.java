package com.kafkaTest.subscriberPartition1.application.service;

import com.kafkaTest.subscriberPartition1.domain.model.OrderEvent;
import com.kafkaTest.subscriberPartition1.domain.port.in.ConsumerOrderEventUseCase;
import org.springframework.stereotype.Service;

@Service
public class ConsumerOrderEventService implements ConsumerOrderEventUseCase {

    @Override
    public void consume(String key, OrderEvent event, int partition, long offset) {
        System.out.println("""
            ▶ CONSUMED (P1)
            key=%s
            partition=%d offset=%d
            eventType=%s orderId=%d total=%s
            """.formatted(
                key, partition, offset,
                event.getEventType(), event.getOrderId(), event.getTotal()
        ));
    }




}
