package com.example.ytspringbatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class YtSpringBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(YtSpringBatchApplication.class, args);
    }

}
