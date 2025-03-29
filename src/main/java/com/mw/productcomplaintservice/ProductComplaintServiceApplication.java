package com.mw.productcomplaintservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ProductComplaintServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductComplaintServiceApplication.class, args);
    }

}
