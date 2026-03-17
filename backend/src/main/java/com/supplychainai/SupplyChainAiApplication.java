package com.supplychainai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SupplyChainAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupplyChainAiApplication.class, args);
    }
}
