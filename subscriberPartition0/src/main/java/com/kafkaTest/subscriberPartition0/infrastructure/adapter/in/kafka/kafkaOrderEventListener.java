package com.kafkaTest.subscriberPartition0.infrastructure.adapter.in.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafkaTest.subscriberPartition0.domain.model.OrderEvent;
import com.kafkaTest.subscriberPartition0.domain.port.in.ConsumerOrderEventUseCase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Component
public class kafkaOrderEventListener {

    private final ConsumerOrderEventUseCase useCase;
    private final ObjectMapper mapper;

    public kafkaOrderEventListener(ConsumerOrderEventUseCase useCase, ObjectMapper mapper){
        this.useCase = useCase;
        this.mapper = mapper;
    }

    @KafkaListener(
            topicPartitions = @TopicPartition(topic = "demo.events", partitions = {"0"})
    )
    public void onMessage(ConsumerRecord<String, String> record) throws Exception{
        OrderEvent event = mapper.readValue(record.value(), OrderEvent.class);

        useCase.consume(
                record.key(),
                event,
                record.partition(),
                record.offset()
        );
    }
}
