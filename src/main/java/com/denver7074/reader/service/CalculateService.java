package com.denver7074.reader.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.denver7074.reader.utils.Utils.converter;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CalculateService {

    final ObjectMapper objectMapper;

    Map<String, Duration> map = new HashMap<>();


    @PreDestroy
    @SneakyThrows
    public void reader() {
        InputStream resourceAsStream = getClass().getClassLoader()
                .getResourceAsStream("tickets.json");

        JsonNode jsonNode = objectMapper.readTree(resourceAsStream).get("tickets");
        List<Double> tickets = new ArrayList<>(jsonNode.size());
        double average = 0;
        for (JsonNode j : jsonNode) {
            if (!j.get("origin_name").asText().equals("Владивосток")
                    || !j.get("destination_name").asText().equals("Тель-Авив")) continue;
            calculate(j);
            double price = j.get("price").asDouble();
            average += price;
            tickets.add(price);
        }
        for (String c : map.keySet()) {
            Duration duration = map.get(c);
            System.out.println(format("Carrier: %s  time fly: %02d:%02d", c, duration.toHours(), duration.toMinutesPart()));
        }
        average = average / tickets.size();
        System.out.println(format("Average price: %s", average));
        List<Double> list = tickets.stream().sorted().toList();
        double mediana = 0;
        int index = list.size() / 2;
        if (list.size() % 2 == 0) {
            mediana = (list.get(index) + list.get(index - 1)) / 2;
        } else mediana = list.get(index);
        System.out.println(format("Mediana: %s ", mediana));
        System.out.println(format("Difference %s", average - mediana));
    }

    private void calculate(JsonNode j) {
        //дата и время отбытия
        StringBuilder departure = new StringBuilder();
        departure.append(j.get("departure_date").asText())
                .append(" ")
                .append(j.get("departure_time").asText());
        ZonedDateTime departureZoneTime = converter(departure, ZoneId.of("Asia/Vladivostok"));
        //дата и время прибытия
        StringBuilder arrival = new StringBuilder();
        arrival.append(j.get("arrival_date").asText())
                .append(" ")
                .append(j.get("arrival_time").asText());
        ZonedDateTime arrivalZoneTime = converter(arrival, ZoneId.of("Asia/Jerusalem"));

        Duration duration = Duration.between(departureZoneTime, arrivalZoneTime);
        String carrier = j.get("carrier").asText();

        if (map.containsKey(carrier)) {
            Duration dur = map.get(carrier);
            if (dur.compareTo(duration) > 0) {
                map.put(carrier, dur);
            }
        } else map.put(carrier, duration);
    }





}
