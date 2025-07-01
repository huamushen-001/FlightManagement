package com.flightmanagement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.flightmanagement.mapper")
public class FlightCrewSchedulingApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlightCrewSchedulingApplication.class, args);
    }
} 