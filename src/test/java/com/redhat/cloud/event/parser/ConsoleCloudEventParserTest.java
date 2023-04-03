package com.redhat.cloud.event.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConsoleCloudEventParserTest {

    @Test
    public void shouldParseCorrectCloudEvents() throws IOException {
        ConsoleCloudEventParser consoleCloudEventParser = new ConsoleCloudEventParser();

        ConsoleCloudEvent consoleCloudEvent = consoleCloudEventParser.fromJsonString(readSchema("cloud-events/advisor.json"));

        assertEquals("com.redhat.console.advisor.new-recommendations", consoleCloudEvent.getType());
        assertEquals("org123", consoleCloudEvent.getOrgId());
        assertNull(consoleCloudEvent.getAccountId());
    }

    @Test
    public void shouldFailOnNonCompliantCloudEvents() {
        ConsoleCloudEventParser consoleCloudEventParser = new ConsoleCloudEventParser();
        assertThrows(RuntimeException.class, () -> consoleCloudEventParser.fromJsonString(readSchema("cloud-events/advisor-invalid.json")));
    }

    @Test
    public void shouldFailOnInvalidJson() throws IOException {
        ConsoleCloudEventParser consoleCloudEventParser = new ConsoleCloudEventParser();

        assertThrows(RuntimeException.class, () -> consoleCloudEventParser.fromJsonString("hello world"));
    }

    private String readSchema(String path) throws IOException {
        InputStream content = ConsoleCloudEventParserTest.class.getClassLoader().getResourceAsStream(path);
        assertNotNull(content, "could not load example cloud event for advisor");
        return new String(content.readAllBytes(), UTF_8);
    }
}
