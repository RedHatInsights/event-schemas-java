package com.redhat.cloud.event.parser.exceptions;

public class ConsoleCloudEventParsingException extends RuntimeException {

    public ConsoleCloudEventParsingException(String message) {
        super(message);
    }

    public ConsoleCloudEventParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
