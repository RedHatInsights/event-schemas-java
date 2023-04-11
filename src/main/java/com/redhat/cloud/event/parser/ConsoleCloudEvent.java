package com.redhat.cloud.event.parser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

public class ConsoleCloudEvent extends GenericConsoleCloudEvent<JsonNode> {

    @JsonIgnore
    private ObjectMapper objectMapper;

    void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> Optional<T> getData(Class<T> asClass) {
        try {
            return Optional.of(this.objectMapper.treeToValue(this.getData(), asClass));
        } catch (JsonProcessingException jpe) {
            return Optional.empty();
        }
    }
}
