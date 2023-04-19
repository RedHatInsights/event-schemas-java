package com.redhat.cloud.event.parser.exceptions;

import com.networknt.schema.ValidationMessage;

import java.util.Collection;

public class ConsoleCloudEventValidationException extends ConsoleCloudEventParsingException {

    Collection<ValidationMessage> validationMessages;

    public ConsoleCloudEventValidationException(String message, Collection<ValidationMessage> validationMessages) {
        super(message);
        this.validationMessages = validationMessages;
    }

    @Override
    public String toString() {
        return super.toString() + System.lineSeparator() + this.validationMessages.toString();
    }

    public Collection<ValidationMessage> getValidationMessages() {
        return this.validationMessages;
    }
}
