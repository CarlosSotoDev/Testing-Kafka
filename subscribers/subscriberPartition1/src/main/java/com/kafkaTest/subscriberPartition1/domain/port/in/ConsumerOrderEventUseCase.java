package com.kafkaTest.subscriberPartition1.domain.port.in;

import com.kafkaTest.subscriberPartition1.domain.model.OrderEvent;

public interface ConsumerOrderEventUseCase {
    void consume(String key, OrderEvent event, int partition, long offset);
}
