package com.redhat.cloud.event.parser.modules;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Constants {

    static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC);
}
