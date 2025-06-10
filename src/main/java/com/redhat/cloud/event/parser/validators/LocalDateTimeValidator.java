package com.redhat.cloud.event.parser.validators;

import com.networknt.schema.Format;

import com.networknt.schema.format.AbstractFormat;
import com.networknt.schema.format.BaseFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateTimeValidator extends AbstractFormat {

    public LocalDateTimeValidator() {
        super("date-time", "must be a valid ISO_DATE_TIME date time");
    }
    private String message;

    @Override
    public boolean matches(String text) {
        try {
            DateTimeFormatter.ISO_DATE_TIME.parse(text);
            return true;
        } catch (DateTimeParseException exception) {
            message = exception.getMessage();
            return false;
        }
    }

    @Override
    public String getErrorMessageDescription() {
        return message;
    }
}
