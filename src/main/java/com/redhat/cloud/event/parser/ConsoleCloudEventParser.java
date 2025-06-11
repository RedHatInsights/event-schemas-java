package com.redhat.cloud.event.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.networknt.schema.AbsoluteIri;
import com.networknt.schema.ApplyDefaultsStrategy;
import com.networknt.schema.Formats;
import com.networknt.schema.JsonMetaSchema;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaException;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.NonValidationKeyword;
import com.networknt.schema.PathType;
import com.networknt.schema.SchemaValidatorsConfig;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationResult;
import com.networknt.schema.ValidatorTypeCode;
import com.networknt.schema.resource.SchemaMapper;
import com.redhat.cloud.event.core.v1.RHELSystem;
import com.redhat.cloud.event.parser.exceptions.ConsoleCloudEventParsingException;
import com.redhat.cloud.event.parser.exceptions.ConsoleCloudEventValidationException;
import com.redhat.cloud.event.parser.modules.LocalDateTimeModule;
import com.redhat.cloud.event.parser.modules.OffsetDateTimeModule;
import com.redhat.cloud.event.parser.validators.LocalDateTimeValidator;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ConsoleCloudEventParser {

    private static final String schemaPath = "/schemas/events/v1/events.json";

    static class CustomSchemaMapper implements SchemaMapper {

        private static final String baseUrl = "https://console.redhat.com/api";
        private final String base;

        CustomSchemaMapper() {
            String fullPath = RHELSystem.class.getResource(schemaPath).toString();
            base = fullPath.substring(0, fullPath.length() - schemaPath.length());
        }

        @Override
        public AbsoluteIri map(AbsoluteIri absoluteIRI) {
            String iri = absoluteIRI.toString();
            if (iri.startsWith(baseUrl)) {
                return AbsoluteIri.of(replaceBase(iri));
            }
            return null;
        }

        private String replaceBase(String uri) {
            if (uri.startsWith(baseUrl)) {
                uri = base + uri.substring(baseUrl.length());
            }
            return uri;
        }
    }

    ObjectMapper objectMapper;

    JsonSchema jsonSchema;

    public ConsoleCloudEventParser() {
        this(buildObjectMapper());
    }

    public ConsoleCloudEventParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        jsonSchema = getJsonSchema();
    }

    public ConsoleCloudEvent fromJsonString(String cloudEventJson) {
        ConsoleCloudEvent consoleCloudEvent = fromJsonString(cloudEventJson, ConsoleCloudEvent.class);
        consoleCloudEvent.setObjectMapper(this.objectMapper);
        return consoleCloudEvent;
    }

    public void validate(String cloudEventJson) {
        try {
            JsonNode cloudEvent = objectMapper.readTree(cloudEventJson);
            validate(cloudEvent, jsonSchema);
        } catch (JsonProcessingException jpe) {
            throw new ConsoleCloudEventParsingException("Cloud event validation failed for: " + cloudEventJson, jpe);
        }

    }

    public <T extends GenericConsoleCloudEvent<?>> T fromJsonString(String cloudEventJson, Class<T> consoleCloudEventClass) {
        try {
            // Verify it's a valid Json
            JsonNode cloudEvent = objectMapper.readTree(cloudEventJson);
            validate(cloudEvent, jsonSchema);
            T genericCloudEvent = objectMapper.treeToValue(cloudEvent, consoleCloudEventClass);
            if (genericCloudEvent instanceof ConsoleCloudEvent) {
                ConsoleCloudEvent consoleCloudEvent = (ConsoleCloudEvent) genericCloudEvent;
                consoleCloudEvent.setObjectMapper(this.objectMapper);
            }

            return genericCloudEvent;
        } catch (JsonProcessingException jpe) {
            throw new ConsoleCloudEventParsingException("Cloud event parsing failed for: " + cloudEventJson, jpe);
        }
    }

    public String toJson(GenericConsoleCloudEvent<?> consoleCloudEvent) {
        try {
            JsonNode node = objectMapper.valueToTree(consoleCloudEvent);
            validate(node, jsonSchema);

            return objectMapper.writeValueAsString(node);
        } catch (JsonProcessingException jpe) {
            throw new ConsoleCloudEventParsingException("Cloud event serialization failed consoleCloudEvent: " + consoleCloudEvent, jpe);
        }

    }

    public static ObjectMapper buildObjectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .registerModule(new OffsetDateTimeModule())
                .registerModule(new LocalDateTimeModule());
    }

    private JsonSchema getJsonSchema() {
        SchemaValidatorsConfig schemaValidatorsConfig = SchemaValidatorsConfig.builder()
            .pathType(PathType.LEGACY)
            .errorMessageKeyword("message")
            .nullableKeywordEnabled(true)
            .applyDefaultsStrategy(
                new ApplyDefaultsStrategy(
            true,
            true,
            true
                ))
            .build();


        try (InputStream jsonSchemaStream = RHELSystem.class.getResourceAsStream(schemaPath)) {
            JsonNode schema = objectMapper.readTree(jsonSchemaStream);

            return jsonSchemaFactory().getSchema(
                    schema,
                    schemaValidatorsConfig
            );
        } catch (IOException ioe) {
            throw new JsonSchemaException(ioe);
        }
    }

    private static void validate(JsonNode cloudEvent, JsonSchema jsonSchema) {
        ValidationResult result = jsonSchema.walk(cloudEvent, true);

        if (result.getValidationMessages().size() > 0) {
            throw new ConsoleCloudEventValidationException("Cloud event validation failed for: " + cloudEvent, result.getValidationMessages());
        }
    }

    private static JsonSchemaFactory jsonSchemaFactory() {
        String ID = "$id";

        JsonMetaSchema overrideDateTimeValidator = new JsonMetaSchema.Builder(JsonMetaSchema.getV7().getIri())
                .idKeyword(ID)
                .keywords(ValidatorTypeCode.getKeywords(SpecVersion.VersionFlag.V7))
                .keywords(List.of(
                        new NonValidationKeyword("examples"),
                        new NonValidationKeyword("$schema"),
                        new NonValidationKeyword("definitions"),
                        new NonValidationKeyword(ID),
                        new NonValidationKeyword("title"),
                        new NonValidationKeyword("description"),
                        new NonValidationKeyword("contentEncoding")
                ))
                .formats(Formats.DEFAULT)
                .format(new LocalDateTimeValidator())
                .specification(SpecVersion.VersionFlag.V7)
                .build();

        return new JsonSchemaFactory.Builder().defaultMetaSchemaIri(overrideDateTimeValidator.getIri())
                .metaSchema(overrideDateTimeValidator)
                .schemaMappers(schemaMappers -> schemaMappers.add(new CustomSchemaMapper()))
                .build();

    }

}
