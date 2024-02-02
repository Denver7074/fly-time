package com.denver7074.reader;

import com.denver7074.reader.service.CalculateService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

import static java.lang.String.format;

@SpringBootApplication
public class ReaderApplication {


	public static void main(String[] args) {
		SpringApplication.run(ReaderApplication.class, args);
	}

}
