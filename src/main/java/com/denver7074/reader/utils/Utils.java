package com.denver7074.reader.utils;

import lombok.SneakyThrows;

import java.time.LocalDateTime;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {

    public final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy H:mm");

    @SneakyThrows
    public static ZonedDateTime converter(StringBuilder value, ZoneId zone) {
        LocalDateTime dateTime = LocalDateTime.parse(value.toString(), dateTimeFormatter);
        return dateTime.atZone(zone);
    }
}
