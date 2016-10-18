package com.flipkart.flap.thunderingrhino.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Created by anurag.laddha on 11/04/15.
 */

public enum JacksonObjectMapper {
    INSTANCE;

    private final ObjectMapper objectMapper;

    private JacksonObjectMapper(){
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public ObjectMapper getObjectMapper(){ return this.objectMapper; }
}
