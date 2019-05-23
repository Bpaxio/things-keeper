package ru.bbpax.keeper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
@EnableMongoRepositories
public class ThingsKeeperApp {

    public static void main(String[] args) {
        SpringApplication.run(ThingsKeeperApp.class);
    }
}
