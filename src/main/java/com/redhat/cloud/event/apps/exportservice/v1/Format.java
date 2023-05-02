package com.redhat.cloud.event.apps.exportservice.v1;

import java.io.IOException;
import com.fasterxml.jackson.annotation.*;

/**
 * The format of the data to be exported
 */
public enum Format {
    CSV, JSON;

    @JsonValue
    public String toValue() {
        switch (this) {
            case CSV: return "csv";
            case JSON: return "json";
        }
        return null;
    }

    @JsonCreator
    public static Format forValue(String value) throws IOException {
        if (value.equals("csv")) return CSV;
        if (value.equals("json")) return JSON;
        throw new IOException("Cannot deserialize Format");
    }
}
