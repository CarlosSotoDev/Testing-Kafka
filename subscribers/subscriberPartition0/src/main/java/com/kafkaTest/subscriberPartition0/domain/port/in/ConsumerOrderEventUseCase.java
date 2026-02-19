package com.kafkaTest.subscriberPartition0.domain.port.in;

import com.kafkaTest.subscriberPartition0.domain.model.OrderEvent;

public interface ConsumerOrderEventUseCase {
    void consume(String key, OrderEvent event, int partition, long offset);
}
