package com.denver7074.reader.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Duration;

@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ticket {

    //название авиакомпании
    String carrier;
    //Время перелета
    Duration duration;
    //цена
    Double price;

    @Override
    public String toString() {
        return String.format("Carrier: %s  time fly: %02d:%02d", carrier, duration.toHours(), duration.toMinutesPart());
    }
}
