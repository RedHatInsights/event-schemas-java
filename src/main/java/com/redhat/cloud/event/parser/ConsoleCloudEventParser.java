package com.redhat.cloud.event.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.networknt.schema.AbsoluteIri;
import com.networknt.schema.walk.ApplyDefaultsStrategy;
import com.networknt.schema.format.Formats;
import com.networknt.schema.dialect.Dialect;
import com.networknt.schema.Schema;
import com.networknt.schema.SchemaException;
import com.networknt.schema.SchemaRegistry;
import com.networknt.schema.keyword.AnnotationKeyword;
import com.networknt.schema.path.PathType;
import com.networknt.schema.SchemaRegistryConfig;
import com.networknt.schema.SpecificationVersion;
import com.networknt.schema.Result;
import com.networknt.schema.dialect.Dialects;
import com.networknt.schema.dialect.BasicDialectRegistry;
import com.networknt.schema.resource.ResourceLoader;
import com.networknt.schema.resource.InputStreamSource;
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

    static class CustomResourceLoader implements ResourceLoader {
        private static final String baseUrl = "https://console.redhat.com/api";

        @Override
        public InputStreamSource getResource(AbsoluteIri absoluteIri) {
            String iri = absoluteIri.toString();

            // If it's one of our console.redhat.com URLs, map to classpath
            if (iri.startsWith(baseUrl)) {
                String resourcePath = iri.substring(baseUrl.length());

                return () -> {
                    InputStream stream = RHELSystem.class.getResourceAsStream(resourcePath);
                    if (stream == null) {
                        throw new java.io.FileNotFoundException("Schema not found on classpath: " + resourcePath);
                    }
                    return stream;
                };
            }

            // Return null to let other loaders handle it
            return null;
        }
    }

    ObjectMapper objectMapper;

    Schema jsonSchema;

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

    private Schema getJsonSchema() {
        SchemaRegistryConfig schemaRegistryConfig = SchemaRegistryConfig.builder()
            .pathType(PathType.LEGACY)
            .errorMessageKeyword("message")
            .build();

        try (InputStream jsonSchemaStream = RHELSystem.class.getResourceAsStream(schemaPath)) {
            JsonNode schema = objectMapper.readTree(jsonSchemaStream);

            SchemaRegistry schemaRegistry = jsonSchemaFactory(schemaRegistryConfig);
            return schemaRegistry.getSchema(com.networknt.schema.SchemaLocation.of(schemaPath), schema);
        } catch (IOException ioe) {
            throw new SchemaException(ioe);
        }
    }

    private static void validate(JsonNode cloudEvent, Schema jsonSchema) {
        Result result = jsonSchema.walk(cloudEvent, true, executionContext -> {
            executionContext.walkConfig(walkConfig -> {
                walkConfig.applyDefaultsStrategy(applyDefaults -> {
                    applyDefaults.applyArrayDefaults(true)
                        .applyPropertyDefaults(true)
                        .applyPropertyDefaultsIfNull(true);
                });
            });
        });

        if (!result.getErrors().isEmpty()) {
            throw new ConsoleCloudEventValidationException("Cloud event validation failed for: " + cloudEvent, result.getErrors());
        }
    }

    private static SchemaRegistry jsonSchemaFactory(SchemaRegistryConfig schemaRegistryConfig) {
        String ID = "$id";

        Dialect overrideDateTimeValidator = Dialect.builder(Dialects.getDraft7())
                .idKeyword(ID)
                .keywords(keywords -> {
                    keywords.put("examples", new AnnotationKeyword("examples"));
                    keywords.put("$schema", new AnnotationKeyword("$schema"));
                    keywords.put("definitions", new AnnotationKeyword("definitions"));
                    keywords.put(ID, new AnnotationKeyword(ID));
                    keywords.put("title", new AnnotationKeyword("title"));
                    keywords.put("description", new AnnotationKeyword("description"));
                    keywords.put("contentEncoding", new AnnotationKeyword("contentEncoding"));
                })
                .format(new LocalDateTimeValidator())
                .specificationVersion(SpecificationVersion.DRAFT_7)
                .build();

        BasicDialectRegistry dialectRegistry = new BasicDialectRegistry(List.of(overrideDateTimeValidator));

        return SchemaRegistry.builder()
                .defaultDialectId(overrideDateTimeValidator.getId())
                .dialectRegistry(dialectRegistry)
                .schemaRegistryConfig(schemaRegistryConfig)
                .resourceLoaders(resourceLoaders -> {
                    resourceLoaders.add(new CustomResourceLoader());
                })
                .build();
    }

}
