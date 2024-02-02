package com.denver7074.reader.service;

import com.denver7074.reader.domain.Ticket;
import com.denver7074.reader.utils.Utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
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
import java.util.List;

import static com.denver7074.reader.utils.Utils.converter;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CalculateService {

    ObjectMapper objectMapper;

    @PreDestroy
    @SneakyThrows
    public void reader() {
        InputStream resourceAsStream = getClass().getClassLoader()
                .getResourceAsStream("tickets.json");

        JsonNode jsonNode = objectMapper.readTree(resourceAsStream).get("tickets");
        List<Ticket> tickets = new ArrayList<>(jsonNode.size());
        double average = 0;
        for (JsonNode j : jsonNode) {
            if (!j.get("origin_name").asText().equals("Владивосток")
                    || !j.get("destination_name").asText().equals("Тель-Авив")) continue;
            Ticket ticket = calculate(j);
            System.out.println(ticket);
            average += ticket.getPrice();
            tickets.add(ticket);
        }
        average = average / tickets.size();
        System.out.println(format("Average price: %s", average));
        List<Double> list = tickets.stream().map(Ticket::getPrice).sorted().toList();
        double mediana = 0;
        int index = list.size() / 2;
        if (list.size() % 2 == 0) {
            mediana = (list.get(index) + list.get(index - 1)) / 2;
        } else mediana = list.get(index);
        System.out.println(format("Mediana: %s ", mediana));
        System.out.println(format("Difference %s", average - mediana));
    }

    private Ticket calculate(JsonNode j) {
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
        double price = j.get("price").asDouble();

        return new Ticket()
                .setCarrier(carrier)
                .setPrice(price)
                .setDuration(duration);
    }





}
