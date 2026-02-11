package com.kafkaTest.subscriberPartition1.domain.model;

import java.math.BigDecimal;

public class OrderEvent {
    private String eventType;
    private Long orderId;
    private BigDecimal total;

    public OrderEvent(){}

    public String getEventType(){
        return eventType;
    }

    public void setEventType(String eventType){
        this.eventType = eventType;
    }

    public Long getOrderId(){
        return orderId;
    }

    public void setOrderId(Long orderId){
        this.orderId = orderId;
    }

    public BigDecimal getTotal(){
        return total;
    }
}
