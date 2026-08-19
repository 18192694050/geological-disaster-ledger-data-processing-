package com.kuang.geoslopefix;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.kuang.geoslopefix.mapper")
public class GeoSlopeFixApplication {
    public static void main(String[] args) {
        SpringApplication.run(GeoSlopeFixApplication.class, args);
    }
}