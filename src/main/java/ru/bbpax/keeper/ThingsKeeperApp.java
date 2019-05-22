package ru.bbpax.keeper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Slf4j
@SpringBootApplication
@EnableMongoRepositories
@EnableHystrix
@EnableHystrixDashboard
public class ThingsKeeperApp {

    public static void main(String[] args) {
        SpringApplication.run(ThingsKeeperApp.class);
    }
}
