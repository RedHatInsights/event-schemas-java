package com.redhat.cloud.event.parser.modules;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAccessor;

public class LocalDateTimeModule extends SimpleModule {

    private static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

        @Override
        public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            if (jsonParser.hasTokenId(JsonTokenId.ID_STRING)) {
                TemporalAccessor temporalAccessor = Constants.dateTimeFormatter.parse(jsonParser.getText());
                return OffsetDateTime.from(temporalAccessor).atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            }

            return (LocalDateTime) deserializationContext.handleUnexpectedToken(LocalDateTime.class, jsonParser);
        }
    }

    private static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

        @Override
        public void serialize(LocalDateTime dateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(dateTime.atZone(ZoneOffset.UTC).format(Constants.dateTimeFormatter));
        }

    }

    public LocalDateTimeModule() {
        addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
    }

}
