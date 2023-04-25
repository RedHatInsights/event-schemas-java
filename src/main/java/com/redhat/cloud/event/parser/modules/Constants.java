package com.redhat.cloud.event.parser.modules;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Constants {

    static final DateTimeFormatter dateTimeFormatterReader = DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC);
    static final DateTimeFormatter dateTimeFormatterWriter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
}
