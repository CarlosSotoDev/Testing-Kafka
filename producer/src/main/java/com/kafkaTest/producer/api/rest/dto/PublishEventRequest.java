package com.kafkaTest.producer.api.rest.dto;

public class PublishEventRequest {

    private String key;
    private Object payload;

    public PublishEventRequest(){};

    public String getKey(){
        return key;
    }

    public Object getPayload(){
        return payload;
    }

    public void setKey(String key){
        this.key = key;
    }

    public void setPayload(Object payload){
        this.payload = payload;
    }
}
