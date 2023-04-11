package com.redhat.cloud.event.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.networknt.schema.ApplyDefaultsStrategy;
import com.networknt.schema.JsonMetaSchema;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaException;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SchemaValidatorsConfig;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationResult;
import com.networknt.schema.ValidatorTypeCode;
import com.networknt.schema.uri.URIFactory;
import com.networknt.schema.uri.URLFactory;
import com.redhat.cloud.event.core.v1.RHELSystem;
import com.redhat.cloud.event.parser.modules.LocalDateTimeModule;
import com.redhat.cloud.event.parser.validators.LocalDateTimeValidator;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class ConsoleCloudEventParser {

    private static final String schemaPath = "/schemas/events/v1/events.json";

    static class CloudEventURIFactory implements URIFactory {

        private static final String baseUrl = "https://console.redhat.com/api";
        private final URLFactory urlFactory = new URLFactory();
        private final String base;

        CloudEventURIFactory() {
            String fullPath = RHELSystem.class.getResource(schemaPath).toString();
            base = fullPath.substring(0, fullPath.length() - schemaPath.length());
        }

        @Override
        public URI create(String uri) {
            return urlFactory.create(replaceBase(uri));
        }

        @Override
        public URI create(URI baseURI, String segment) {
            return urlFactory.create(URI.create(replaceBase(baseURI.toString())), segment);
        }

        private String replaceBase(String uri) {
            if (uri.startsWith(baseUrl)) {
                uri = base + uri.substring(baseUrl.length(), uri.length() - 1);
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

    public <T extends GenericConsoleCloudEvent<?>> T fromJsonString(String cloudEventJson, Class<T> consoleCloudEventClass) {
        try {
            // Verify it's a valid Json
            JsonNode cloudEvent = objectMapper.readTree(cloudEventJson);
            ValidationResult result = jsonSchema.walk(cloudEvent, true);

            if (result.getValidationMessages().size() > 0) {
                throw new ConsoleCloudEventParsingException("Cloud event validation failed for: " + cloudEventJson + ". Failures: " + result.getValidationMessages().toString());
            }

            return objectMapper.treeToValue(cloudEvent, consoleCloudEventClass);
        } catch (JsonProcessingException jpe) {
            throw new ConsoleCloudEventParsingException("Cloud event parsing failed for: " + cloudEventJson, jpe);
        }
    }

    public String toJson(GenericConsoleCloudEvent<?> consoleCloudEvent) {
        try {
            JsonNode node = objectMapper.valueToTree(consoleCloudEvent);
            ValidationResult result = jsonSchema.walk(node, true);

            if (result.getValidationMessages().size() > 0) {
                throw new ConsoleCloudEventParsingException("Cloud event validation failed for: " + node + ". Failures: " + result.getValidationMessages().toString());
            }

            return objectMapper.writeValueAsString(node);
        } catch (JsonProcessingException jpe) {
            throw new ConsoleCloudEventParsingException("Cloud event serialization failed consoleCloudEvent: " + consoleCloudEvent, jpe);
        }

    }

    public static ObjectMapper buildObjectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .registerModule(new LocalDateTimeModule());
    }

    private JsonSchema getJsonSchema() {
        SchemaValidatorsConfig schemaValidatorsConfig = new SchemaValidatorsConfig();
        schemaValidatorsConfig.setApplyDefaultsStrategy(new ApplyDefaultsStrategy(
                true,
                true,
                true
        ));

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

    private static JsonSchemaFactory jsonSchemaFactory() {
        String ID = "$id";

        JsonMetaSchema overrideDateTimeValidator = new JsonMetaSchema.Builder(JsonMetaSchema.getV7().getUri())
                .idKeyword(ID)
                .addKeywords(ValidatorTypeCode.getNonFormatKeywords(SpecVersion.VersionFlag.V7))
                .addFormats(JsonMetaSchema.COMMON_BUILTIN_FORMATS)
                .addFormat(new LocalDateTimeValidator())
                .build();

        return new JsonSchemaFactory.Builder().defaultMetaSchemaURI(overrideDateTimeValidator.getUri())
                .addMetaSchema(overrideDateTimeValidator)
                .uriFactory(new CloudEventURIFactory(), "https")
                .build();

    }

}
