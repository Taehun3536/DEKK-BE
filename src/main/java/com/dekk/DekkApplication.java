package com.dekk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DekkApplication {

    public static void main(String[] args) {
        SpringApplication.run(DekkApplication.class, args);
    }
}
