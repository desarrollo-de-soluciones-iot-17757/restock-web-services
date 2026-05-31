package com.uitopic.restock.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class RestockWebServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestockWebServiceApplication.class, args);
    }

}