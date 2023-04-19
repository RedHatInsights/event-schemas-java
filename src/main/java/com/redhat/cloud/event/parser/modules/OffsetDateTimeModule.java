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
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAccessor;

public class OffsetDateTimeModule extends SimpleModule {

    private static class OffsetDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {

        @Override
        public OffsetDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            if (jsonParser.hasTokenId(JsonTokenId.ID_STRING)) {
                TemporalAccessor temporalAccessor = Constants.dateTimeFormatter.parse(jsonParser.getText());
                return OffsetDateTime.from(temporalAccessor);
            }

            return (OffsetDateTime) deserializationContext.handleUnexpectedToken(OffsetDateTime.class, jsonParser);
        }
    }

    private static class OffsetDateTimeSerializer extends JsonSerializer<OffsetDateTime> {

        @Override
        public void serialize(OffsetDateTime dateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(dateTime.format(Constants.dateTimeFormatter));
        }

    }

    public OffsetDateTimeModule() {
        addDeserializer(OffsetDateTime.class, new OffsetDateTimeDeserializer());
        addSerializer(OffsetDateTime.class, new OffsetDateTimeSerializer());
    }

}
