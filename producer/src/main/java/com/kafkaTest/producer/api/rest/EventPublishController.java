package com.kafkaTest.producer.api.rest;

import com.kafkaTest.producer.api.rest.dto.PublishEventRequest;
import com.kafkaTest.producer.application.port.in.PublishEventUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class EventPublishController {

    private final PublishEventUseCase publishEventUseCase;

    public EventPublishController(PublishEventUseCase publishEventUseCase){
        this.publishEventUseCase = publishEventUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> publish(@RequestBody PublishEventRequest request){
        publishEventUseCase.publish(request.getPayload(), request.getKey());
        return ResponseEntity.accepted().build();
    }
}
