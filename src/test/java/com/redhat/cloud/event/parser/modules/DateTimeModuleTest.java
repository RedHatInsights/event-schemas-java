package com.redhat.cloud.event.parser.modules;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateTimeModuleTest {

    @JsonSerialize
    private static class Helper {
        @JsonProperty
        LocalDateTime localDateTime;

        @JsonProperty
        OffsetDateTime offsetDateTime;
    }

    ObjectMapper objectMapper;

    @BeforeEach
    public void setupMapper() {
        objectMapper = new ObjectMapper();
        objectMapper
                .registerModule(new OffsetDateTimeModule())
                .registerModule(new LocalDateTimeModule());
    }

    @Test
    public void noOffsetTest() throws JsonProcessingException {
        Helper helper = new Helper();
        helper.offsetDateTime = OffsetDateTime.parse("2021-03-13T18:44:00+00:00");
        helper.localDateTime = LocalDateTime.parse("2021-03-13T18:44:00");

        String serialized = objectMapper.writeValueAsString(helper);

        assertEquals("{\"localDateTime\":\"2021-03-13T18:44:00Z\",\"offsetDateTime\":\"2021-03-13T18:44:00Z\"}", serialized);

        helper = objectMapper.readValue(serialized, Helper.class);

        assertEquals(LocalDateTime.of(2021, 3, 13, 18, 44, 0), helper.localDateTime);
        assertEquals(OffsetDateTime.of(2021, 3, 13, 18, 44, 0, 0, ZoneOffset.UTC), helper.offsetDateTime);
    }

    @Test
    public void withPositiveOffset() throws JsonProcessingException {
        Helper helper = new Helper();
        helper.offsetDateTime = OffsetDateTime.parse("2021-03-13T18:44:00+01:00");
        helper.localDateTime = LocalDateTime.parse("2021-03-13T18:44:00");

        String serialized = objectMapper.writeValueAsString(helper);

        assertEquals("{\"localDateTime\":\"2021-03-13T18:44:00Z\",\"offsetDateTime\":\"2021-03-13T17:44:00Z\"}", serialized);

        helper = objectMapper.readValue(serialized, Helper.class);

        assertEquals(LocalDateTime.of(2021, 3, 13, 18, 44, 0), helper.localDateTime);
        assertEquals(OffsetDateTime.of(2021, 3, 13, 17, 44, 0, 0, ZoneOffset.UTC), helper.offsetDateTime);
    }

    @Test
    public void withNegativeOffset() throws JsonProcessingException {
        Helper helper = new Helper();
        helper.offsetDateTime = OffsetDateTime.parse("2021-03-13T18:44:00-05:00");
        helper.localDateTime = LocalDateTime.parse("2021-03-13T18:44:00");

        String serialized = objectMapper.writeValueAsString(helper);

        assertEquals("{\"localDateTime\":\"2021-03-13T18:44:00Z\",\"offsetDateTime\":\"2021-03-13T23:44:00Z\"}", serialized);

        helper = objectMapper.readValue(serialized, Helper.class);

        assertEquals(LocalDateTime.of(2021, 3, 13, 18, 44, 0), helper.localDateTime);
        assertEquals(OffsetDateTime.of(2021, 3, 13, 23, 44, 0, 0, ZoneOffset.UTC), helper.offsetDateTime);
    }

    @Test
    public void withNegativeOffsetToReachNextDay() throws JsonProcessingException {
        Helper helper = new Helper();
        helper.offsetDateTime = OffsetDateTime.parse("2021-03-13T18:44:00-06:00");
        helper.localDateTime = LocalDateTime.parse("2021-03-13T18:44:00");

        String serialized = objectMapper.writeValueAsString(helper);

        assertEquals("{\"localDateTime\":\"2021-03-13T18:44:00Z\",\"offsetDateTime\":\"2021-03-14T00:44:00Z\"}", serialized);

        helper = objectMapper.readValue(serialized, Helper.class);

        assertEquals(LocalDateTime.of(2021, 3, 13, 18, 44, 0), helper.localDateTime);
        assertEquals(OffsetDateTime.of(2021, 3, 14, 0, 44, 0, 0, ZoneOffset.UTC), helper.offsetDateTime);
    }
}
