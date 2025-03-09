package com.beibei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableTransactionManagement
@EnableScheduling
@SpringBootApplication
public class HuiYiHealthApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuiYiHealthApplication.class, args);
    }

}
