package com.iot.api.util;

import java.time.Instant;

import org.springframework.core.convert.converter.Converter;

public class InstantToLongConverter implements Converter<Instant, Long> {
    @Override
    public Long convert(Instant instant) {
        return instant.toEpochMilli();
    }
}
