package com.kafkaTest.subscriberPartition0.application.service;

import com.kafkaTest.subscriberPartition0.domain.model.OrderEvent;
import com.kafkaTest.subscriberPartition0.domain.port.in.ConsumerOrderEventUseCase;
import org.springframework.stereotype.Service;

@Service
public class ConsumerOrderEventService implements ConsumerOrderEventUseCase {

    @Override
    public void consume(String key, OrderEvent event, int partition, long offset) {
        System.out.println("""
            ▶ CONSUMED (P0)
            key=%s
            partition=%d offset=%d
            eventType=%s orderId=%d total=%s
            """.formatted(
                key, partition, offset,
                event.getEventType(), event.getOrderId(), event.getTotal()
        ));
    }




}
